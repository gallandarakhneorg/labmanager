/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.utbm.ciad.labmanager.rest.member;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.invitation.PersonInvitationRepository;
import fr.utbm.ciad.labmanager.data.jury.JuryMembershipRepository;
import fr.utbm.ciad.labmanager.data.member.MembershipRepository;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.publication.AuthorshipRepository;
import fr.utbm.ciad.labmanager.data.supervision.SupervisionRepository;
import fr.utbm.ciad.labmanager.rest.AbstractApiController;
import fr.utbm.ciad.labmanager.services.member.PersonMergingService;
import fr.utbm.ciad.labmanager.utils.io.json.JsonUtils;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

/** REST Controller for merging persons.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@RestController
@CrossOrigin
public class PersonMergingApiController extends AbstractApiController {

	private PersonMergingService mergingService;

	private MembershipRepository membershipRepository;

	private AuthorshipRepository authorshipRepository;

	private SupervisionRepository supervisionRepository;

	private JuryMembershipRepository juryMembershipRepository;

	private PersonInvitationRepository invitationRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param constants the constants of the app.
	 * @param mergingService the service for merging persons.
	 * @param membershipRepository the repository for organization memberships.
	 * @param authorshipRepository the repository for authorships.
	 * @param supervisionRepository the repository for supervisions.
	 * @param juryMembershipRepository the repository for jury memberships.
	 * @param invitationRepository the repository for person invitations.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public PersonMergingApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PersonMergingService mergingService,
			@Autowired MembershipRepository membershipRepository,
			@Autowired AuthorshipRepository authorshipRepository,
			@Autowired SupervisionRepository supervisionRepository,
			@Autowired JuryMembershipRepository juryMembershipRepository,
			@Autowired PersonInvitationRepository invitationRepository,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.mergingService = mergingService;
		this.membershipRepository = membershipRepository;
		this.authorshipRepository = authorshipRepository;
		this.supervisionRepository = supervisionRepository;
		this.juryMembershipRepository = juryMembershipRepository;
		this.invitationRepository = invitationRepository;
	}

	/** Merge multiple persons into the database.
	 * Publications for a given list of authors is associated to a target author and
	 * unlinked from the old authors.
	 *
	 * @param target the identifier of the target person.
	 * @param sources the list of person identifiers that are considered as old persons.
	 * @param username the name of the logged-in user.
	 * @throws Exception if it is impossible to merge the persons.
	 */
	@PatchMapping("/mergePersons")
	public void mergePersons(
			@RequestParam(required = true) Integer target,
			@RequestParam(required = true) List<Integer> sources,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		//TODO ensureCredentials(username, "mergePersons", target); //$NON-NLS-1$
		if (sources != null && !sources.isEmpty()) {
			try {
				this.mergingService.mergePersonsById(sources, target);
			} catch (Throwable ex) {
				getLogger().error(ex.getLocalizedMessage(), ex);
				throw ex;
			}
		}
	}

	/** Compute duplicate names.
	 *
	 * @param username the name of the logged-in user.
	 * @return the asynchronous response.
	 */
	@GetMapping(value = "/" + Constants.COMPUTE_PERSON_DUPLICATE_NAMES_ENDPOINT, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter computePersonDuplicateNames(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		//TODO ensureCredentials(username, Constants.COMPUTE_PERSON_DUPLICATE_NAMES_ENDPOINT);
		//
		final ExecutorService service = Executors.newSingleThreadExecutor();
		final SseEmitter emitter = new SseEmitter(Long.valueOf(Constants.SSE_TIMEOUT));
		service.execute(() -> {
			try {
				final MutableInt dupCount = new MutableInt();
				final MutableInt totCount = new MutableInt();
				sendDuplicateComputationStep(emitter, -1, 0, 1);
				final List<Set<Person>> matchingAuthors = this.mergingService.getPersonDuplicates(
						// Use default person comparator
						null,
						(personIndex, duplicateCount, personTotal) -> {
							dupCount.setValue(duplicateCount);
							totCount.setValue(personTotal);
							sendDuplicateComputationStep(emitter, personIndex, duplicateCount, personTotal);
						});
				int i = 0;
				final List<List<Map<String, Object>>> allDuplicates = new LinkedList<>();
				for (final Set<Person> duplicates : matchingAuthors) {
					final List<Map<String, Object>> duplicateJson = new LinkedList<>();
					for (final Person person : duplicates) {
						final int pid = person.getId();
						final Map<String, Object> personJson = new HashMap<>();
						personJson.put("id", Integer.toString(pid)); //$NON-NLS-1$
						personJson.put("fullName", person.getFullNameWithLastNameFirst()); //$NON-NLS-1$
						final int mbrCount = this.membershipRepository.countDistinctByPersonId(pid);
						personJson.put("memberships", Integer.valueOf(mbrCount)); //$NON-NLS-1$
						final int autCount = this.authorshipRepository.countDistinctByPersonId(pid);
						personJson.put("authorships", Integer.valueOf(autCount)); //$NON-NLS-1$
						final int supCount = this.supervisionRepository.countDistinctBySupervisedPersonPersonId(pid);
						personJson.put("supervisions", Integer.valueOf(supCount)); //$NON-NLS-1$
						final int juryCount = this.juryMembershipRepository.countDistinctByPersonId(pid);
						personJson.put("jurys", Integer.valueOf(juryCount)); //$NON-NLS-1$
						final int invCount = this.invitationRepository.countDistinctByGuestIdOrInviterId(pid, pid);
						personJson.put("invitations", Integer.valueOf(invCount)); //$NON-NLS-1$
						personJson.put("lockedForDeletion", Boolean.valueOf( //$NON-NLS-1$
								mbrCount > 0 || autCount > 0 || supCount > 0 || juryCount > 0));
						duplicateJson.add(personJson);
					}
					allDuplicates.add(duplicateJson);
					Thread.sleep(1000);
					sendDuplicateArrayBuildingStep(emitter, i, dupCount.intValue(), totCount.intValue());
					++i;
				}
				sendDuplicateTermination(emitter, allDuplicates);
				emitter.complete();
			} catch (ClientAbortException ex) {
				// Do not log a message because the connection was closed by the client.
				emitter.completeWithError(ex);
			} catch (Throwable ex) {
				getLogger().error(ex.getLocalizedMessage(), ex);
				emitter.completeWithError(ex);
			}
		});
		return emitter;
	}

	private static void sendDuplicateTermination(SseEmitter emitter, List<List<Map<String, Object>>> allDuplicates) throws IOException {
		//
		final Map<String, Object> content = new HashMap<>();
		content.put("terminated", Boolean.TRUE); //$NON-NLS-1$
		content.put("duplicates", allDuplicates); //$NON-NLS-1$
		//
		final ObjectMapper mapper = JsonUtils.createMapper();
		final SseEventBuilder event = SseEmitter.event().data(mapper.writeValueAsString(content));
		emitter.send(event);
	}

	private void sendDuplicateArrayBuildingStep(SseEmitter emitter, int duplicateIndex, int duplicateCount, int personTotal) throws IOException {
		final int duplicatePercent = (duplicateCount * Constants.HUNDRED) / personTotal;
		final int extraPercent = ((duplicateIndex + 1) * Constants.HUNDRED) / duplicateCount;
		//
		final String message = getMessage("personMergingApiController.Progress2", //$NON-NLS-1$
				Integer.toString(Constants.HUNDRED), Integer.toString(duplicatePercent), Integer.valueOf(extraPercent));
		//
		final Map<String, Object> content = new HashMap<>();
		content.put("duplicates", Integer.valueOf(duplicatePercent)); //$NON-NLS-1$
		content.put("percent", Integer.valueOf(100)); //$NON-NLS-1$
		content.put("extra", Integer.valueOf(extraPercent)); //$NON-NLS-1$
		content.put("terminated", Boolean.FALSE); //$NON-NLS-1$
		content.put("message", Strings.nullToEmpty(message)); //$NON-NLS-1$
		//
		final ObjectMapper mapper = JsonUtils.createMapper();
		final SseEventBuilder event = SseEmitter.event().data(mapper.writeValueAsString(content));
		emitter.send(event);
	}

	private void sendDuplicateComputationStep(SseEmitter emitter, int personIndex, int duplicateCount, int personTotal) throws IOException {
		final int percent = ((personIndex + 1) * Constants.HUNDRED) / personTotal;
		final int duplicatePercent = (duplicateCount * Constants.HUNDRED) / personTotal;
		//
		final String message = getMessage("personMergingApiController.Progress", //$NON-NLS-1$
				Integer.toString(percent), Integer.toString(duplicatePercent));
		//
		final Map<String, Object> content = new HashMap<>();
		content.put("duplicates", Integer.valueOf(duplicatePercent)); //$NON-NLS-1$
		content.put("percent", Integer.valueOf(percent)); //$NON-NLS-1$
		content.put("extra", Integer.valueOf(0)); //$NON-NLS-1$
		content.put("terminated", Boolean.FALSE); //$NON-NLS-1$
		content.put("message", Strings.nullToEmpty(message)); //$NON-NLS-1$
		//
		final ObjectMapper mapper = JsonUtils.createMapper();
		final SseEventBuilder event = SseEmitter.event().data(mapper.writeValueAsString(content));
		emitter.send(event);
	}

}
