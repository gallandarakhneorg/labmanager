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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.member.Gender;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.member.WebPageNaming;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.rest.AbstractApiController;
import fr.utbm.ciad.labmanager.rest.publication.PublicationApiController;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.member.PersonService.PersonIndicators;
import fr.utbm.ciad.labmanager.services.organization.ResearchOrganizationService;
import fr.utbm.ciad.labmanager.utils.io.json.JsonUtils;
import fr.utbm.ciad.labmanager.utils.io.vcard.VcardBuilder;
import fr.utbm.ciad.labmanager.utils.names.PersonNameComparator;
import fr.utbm.ciad.labmanager.utils.names.PersonNameParser;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.progress.DefaultProgression;
import org.arakhne.afc.progress.ProgressionEvent;
import org.arakhne.afc.progress.ProgressionListener;
import org.arakhne.afc.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

/** REST Controller for persons.
 * <p>
 * This controller does not manage the memberships' or authorships' databases themselves.
 * You must use {@link MembershipApiController} for managing the memberships, and
 * {@link PublicationApiController} for authorships.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see MembershipApiController
 * @see PublicationApiController
 */
@RestController
@CrossOrigin
public class PersonApiController extends AbstractApiController {

	private PersonService personService;

	private PersonNameParser nameParser;

	private ResearchOrganizationService organizationService;

	private VcardBuilder vcardBuilder;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param personService the person service.
	 * @param organizationService the organization service.
	 * @param nameParser the parser of person names.
	 * @param vcardBuilder the builder of Vcards.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public PersonApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PersonService personService,
			@Autowired ResearchOrganizationService organizationService,
			@Autowired PersonNameParser nameParser,
			@Autowired VcardBuilder vcardBuilder,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.personService = personService;
		this.organizationService = organizationService;
		this.nameParser = nameParser;
		this.vcardBuilder = vcardBuilder;
	}

	/** Replies the information about a person as a JSON stream.
	 * This endpoint accepts one of the two parameters: the name or the identifier of the person.
	 * The name test is based on
	 * {@link PersonNameComparator#isSimilar(String, String) name similarity}, and not on strict equality.
	 *
	 * @param dbId the database identifier of the person. You should provide one of {@code dbId}, {@code webId} or {@code name}.
	 * @param webId the identifier of the webpage of the person. You should provide one of {@code dbId}, {@code webId} or {@code name}.
	 * @param strictName indicates if the name test must be strict (equality test) or not (similarity test).
	 *     By default, this parameter has the value {@code false}.
	 * @param username the name of the logged-in user.
	 * @return the person, or {@code null} if the person with the given name was not found.
	 * @see PersonNameComparator
	 */
	@GetMapping(value = "/getPersonData", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Person getPersonData(
			@RequestParam(required = false) Integer dbId,
			@RequestParam(required = false) String webId,
			@RequestParam(defaultValue = "false", required = false) boolean strictName,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		final String inWebId = inString(webId);
		ensureCredentials(username, "getPersonData", dbId, inWebId); //$NON-NLS-1$
		final Person person = getPersonWith(dbId, inWebId, null, this.personService, this.nameParser);
		if (person == null) {
			throw new IllegalArgumentException("Person not found"); //$NON-NLS-1$
		}
		return person;
	}

	/** Saving information of a person. 
	 *
	 * @param person the identifier of the person. If the identifier is not provided, this endpoint is supposed to create
	 *     a person in the database.
	 * @param firstName the first name of the person.
	 * @param lastName the last name of the person.
	 * @param gender the gender.
	 * @param email the email of the person.
	 * @param officePhone the phone number at office.
	 * @param mobilePhone the mobile phone number.
	 * @param officeRoom the number of the office room.
	 * @param gravatarId the identifier for obtaining a photo on Gravatar.
	 * @param orcid the ORCID of the person.
	 * @param researcherId the identifier of the person on ResearchId/WOS/Publon.
	 * @param scopusId the identifier of the person on Scopus.
	 * @param googleScholarId the identifier of the person on Google Scholar.
	 * @param idhal the identifier of the person on HAL.
	 * @param linkedInId the identifier of the person on LinkedIn.
	 * @param githubId the identifier of the person on Github.
	 * @param researchGateId the identifier of the person on ResearchGate.
	 * @param adScientificIndexId the identifier of the person on AD Scientific Index.
	 * @param facebookId the identifier of the person on Facebook.
	 * @param dblpURL the URL of the person's page on DBLP.
	 * @param academiaURL the URL of the person's page on Academia.edu.
	 * @param cordisURL the URL of the person's page on European Commission's Cordis.
	 * @param webPageNaming the type of naming for the person's webpage on the organization server.
	 * @param googleScholarHindex the Hindex of the person on Google Scholar.
	 * @param wosHindex the Hindex of the person on WOS.
	 * @param scopusHindex the Hindex of the person on Scopus.
	 * @param googleScholarCitations the number of citations for the person on Google Scholar.
	 * @param wosCitations the number of citations for the person on WOS.
	 * @param scopusCitations the number of citations for the person on Scopus.
	 * @param validated indicates if the journal is validated by a local authority.
	 * @param username the name of the logged-in user.
	 * @throws Exception if the person cannot be saved.
	 */
	@PutMapping(value = "/" + Constants.PERSON_SAVING_ENDPOINT)
	public void personSave(
			@RequestParam(required = false) Integer person,
			@RequestParam(required = true) String firstName,
			@RequestParam(required = true) String lastName,
			@RequestParam(required = false) String gender,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String officePhone,
			@RequestParam(required = false) String mobilePhone,
			@RequestParam(required = false) String officeRoom,
			@RequestParam(required = false) String gravatarId,
			@RequestParam(required = false) String orcid,
			@RequestParam(required = false) String researcherId,
			@RequestParam(required = false) String scopusId,
			@RequestParam(required = false) String googleScholarId,
			@RequestParam(required = false) String idhal,
			@RequestParam(required = false) String linkedInId,
			@RequestParam(required = false) String githubId,
			@RequestParam(required = false) String researchGateId,
			@RequestParam(required = false) String adScientificIndexId,
			@RequestParam(required = false) String facebookId,
			@RequestParam(required = false) String dblpURL,
			@RequestParam(required = false) String academiaURL,
			@RequestParam(required = false) String cordisURL,
			@RequestParam(required = false) String webPageNaming,
			@RequestParam(required = false) Integer googleScholarHindex,
			@RequestParam(required = false) Integer wosHindex,
			@RequestParam(required = false) Integer scopusHindex,
			@RequestParam(required = false) Integer googleScholarCitations,
			@RequestParam(required = false) Integer wosCitations,
			@RequestParam(required = false) Integer scopusCitations,
			@RequestParam(required = false, defaultValue = "false") boolean validated,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.PERSON_SAVING_ENDPOINT, person);
		final String inFirstName = inString(firstName);
		final String inLastName = inString(lastName);
		final String inGender = inString(gender);
		final String inEmail = inString(email);
		final String inOfficePhone = inString(officePhone);
		final String inMobilePhone = inString(mobilePhone);
		final String inOfficeRoom = inString(officeRoom);
		final String inGravatarId = inString(gravatarId);
		final String inOrcid = inString(orcid);
		final String inResearcherId = inString(researcherId);
		final String inScopusId = inString(scopusId);
		final String inGoogleScholarId = inString(googleScholarId);
		final String inIdhal = inString(idhal);
		final String inLinkedInId = inString(linkedInId);
		final String inGithubId = inString(githubId);
		final String inResearchGateId = inString(researchGateId);
		final String inAdScientificIndexId = inString(adScientificIndexId);
		final String inFacebookId = inString(facebookId);
		final String inDblpURL = inString(dblpURL);
		final String inAcademiaURL = inString(academiaURL);
		final String inCordisURL = inString(cordisURL);
		final String inWebPageNaming = inString(webPageNaming);
		//
		final Gender genderObj = inGender == null ? Gender.NOT_SPECIFIED : Gender.valueOfCaseInsensitive(inGender);
		final WebPageNaming webPageNamingObj = inWebPageNaming == null ? WebPageNaming.UNSPECIFIED : WebPageNaming.valueOfCaseInsensitive(inWebPageNaming);
		final int shindex = googleScholarHindex == null ? 0 : googleScholarHindex.intValue();
		final int whindex = wosHindex == null ? 0 : wosHindex.intValue();
		final int uhindex = scopusHindex == null ? 0 : scopusHindex.intValue();
		final int scitations = googleScholarCitations == null ? 0 : googleScholarCitations.intValue();
		final int wcitations = wosCitations == null ? 0 : wosCitations.intValue();
		final int ucitations = scopusCitations == null ? 0 : scopusCitations.intValue();
		//
		final Person optPerson;
		//
		if (person == null) {
			optPerson = this.personService.createPerson(
					validated, inFirstName, inLastName, genderObj, inEmail, inOfficePhone, inMobilePhone, inOfficeRoom,
					inGravatarId, inOrcid, inResearcherId, inScopusId, inGoogleScholarId, inIdhal, inLinkedInId, inGithubId,
					inResearchGateId, inAdScientificIndexId,
					inFacebookId, inDblpURL, inAcademiaURL, inCordisURL, webPageNamingObj,
					shindex, whindex, uhindex, scitations, wcitations, ucitations);
		} else {
			optPerson = this.personService.updatePerson(person.intValue(),
					validated, inFirstName, inLastName, genderObj, inEmail, inOfficePhone, inMobilePhone, inOfficeRoom,
					inGravatarId, inOrcid, inResearcherId, inScopusId, inGoogleScholarId, inIdhal, inLinkedInId, inGithubId,
					inResearchGateId, inAdScientificIndexId,
					inFacebookId, inDblpURL, inAcademiaURL, inCordisURL, webPageNamingObj,
					shindex, whindex, uhindex, scitations, wcitations, ucitations);
		}
		if (optPerson == null) {
			throw new IllegalStateException("Person not found"); //$NON-NLS-1$
		}
	}

	/** Delete a person from the database.
	 *
	 * @param person the identifier of the person.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@DeleteMapping("/" + Constants.PERSON_DELETING_ENDPOINT)
	public void deletePerson(
			@RequestParam(name = Constants.PERSON_ENDPOINT_PARAMETER) Integer person,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.PERSON_DELETING_ENDPOINT, person);
		if (person == null || person.intValue() == 0) {
			throw new IllegalStateException("Person not found"); //$NON-NLS-1$
		}
		this.personService.removePerson(person.intValue());
	}

	/** Replies the person's virtual card. This card is a description of the person that could 
	 * be used for building a Internet page for the person.
	 *
	 * @param dbId the database identifier of the person. You should provide one of {@code dbId}, {@code webId} or {@code name}.
	 * @param webId the identifier of the webpage of the person. You should provide one of {@code dbId}, {@code webId} or {@code name}.
	 * @param organization the identifier or the name of the organization to restrict the card to.
	 * @param inAttachment indicates if the JSON is provided as attached document or not. By default, the value is
	 *     {@code false}.
	 * @return the Vcard.
	 */
	@GetMapping(value = "/" + Constants.PERSON_VCARD_ENDPOINT)
	public ResponseEntity<String> personVcard(
			@RequestParam(required = false) Integer dbId,
			@RequestParam(required = false) String webId,
			@RequestParam(required = false) String organization,
			@RequestParam(required = false, defaultValue = "false", name = Constants.INATTACHMENT_ENDPOINT_PARAMETER) Boolean inAttachment) {
		final Person personObj = getPersonWith(dbId, inString(webId), null, this.personService, this.nameParser);
		if (personObj == null) {
			throw new IllegalArgumentException("Person not found"); //$NON-NLS-1$
		}
		//
		final ResearchOrganization organizationObj = getOrganizationWith(inString(organization), this.organizationService);
		//
		final String content = this.vcardBuilder.build(personObj, organizationObj);
		//
		BodyBuilder bb = ResponseEntity.ok().contentType(VcardBuilder.VCARD_MIME_TYPE);
		if (inAttachment != null && inAttachment.booleanValue()) {
			bb = bb.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + personObj.getFullName() + ".vcf\""); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return bb.body(content);
	}

	/** Show the updater for person rankings (h-index and citations).
	 *
	 * @param organization the identifier of the organization in which the persons are considered.
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 * @throws Exception if there is error for obtaining the new indicators.
	 */
	@GetMapping(value = "/" + Constants.GET_JSON_FOR_PERSON_INDICATOR_UPDATES_ENDPOINT)
	public SseEmitter getJsonForPersonIndicatorUpdates(
			@RequestParam(required = true) int organization,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		final Integer id = Integer.valueOf(organization);
		ensureCredentials(username, Constants.GET_JSON_FOR_PERSON_INDICATOR_UPDATES_ENDPOINT, id);
		//
		final ResearchOrganization organizationObj = getOrganizationWith(id, null, this.organizationService);
		if (organizationObj == null) {
			throw new IllegalArgumentException("Organization not found: " + organization); //$NON-NLS-1$
		}
		//
		final ExecutorService service = Executors.newSingleThreadExecutor();
		final SseEmitter emitter = new SseEmitter(Long.valueOf(Constants.SSE_TIMEOUT));
		service.execute(() -> {
			asyncGetJsonForPersonIndicatorUpdates(emitter, organizationObj);
		});
		return emitter;
	}

	private void asyncGetJsonForPersonIndicatorUpdates(SseEmitter emitter, ResearchOrganization organization) {
		final DefaultProgression progress = new DefaultProgression(0, 0, Constants.HUNDRED, false);
		progress.addProgressionListener(new ProgressionListener() {
			@Override
			public void onProgressionValueChanged(ProgressionEvent event) {
				final Map<String, Object> content = new HashMap<>();
				content.put("percent", Integer.valueOf((int) event.getPercent())); //$NON-NLS-1$
				content.put("terminated", Boolean.FALSE); //$NON-NLS-1$
				try {
					final ObjectMapper mapper = JsonUtils.createMapper();
					final SseEventBuilder sseevent = SseEmitter.event().data(mapper.writeValueAsString(content), MediaType.APPLICATION_JSON);
					emitter.send(sseevent);
				} catch (RuntimeException ex) {
					throw ex;
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
		//
		try {
			final List<Map<String, Object>> data = new ArrayList<>();
			this.personService.computePersonRankingIndicatorUpdates(organization, progress, (person, indicators) -> {
				indicators.put("id", Integer.valueOf(person.getId())); //$NON-NLS-1$
				indicators.put("name", person.getFullNameWithLastNameFirst()); //$NON-NLS-1$
				// Add the entry in the list according to the name of the person
				ListUtil.add(data, (a, b) -> {
					if (a == b) {
						return 0;
					}
					if (a == null) {
						return Integer.MIN_VALUE;
					}
					if (b == null) {
						return Integer.MAX_VALUE;
					}
					final String sa = Objects.toString(a.get("name")); //$NON-NLS-1$
					final String sb = Objects.toString(b.get("name")); //$NON-NLS-1$
					if (sa == sb) {
						return 0;
					}
					if (sa == null) {
						return Integer.MIN_VALUE;
					}
					if (sb == null) {
						return Integer.MAX_VALUE;
					}
					return sa.compareToIgnoreCase(sb);
				}, indicators, false, false);
			});
			final Map<String, Object> content = new HashMap<>();
			content.put("percent", Integer.valueOf(Constants.HUNDRED)); //$NON-NLS-1$
			content.put("terminated", Boolean.TRUE); //$NON-NLS-1$
			content.put("data", data); //$NON-NLS-1$
			try {
				final ObjectMapper mapper = new ObjectMapper();
				final SseEventBuilder sseevent = SseEmitter.event().data(mapper.writeValueAsString(content));
				emitter.send(sseevent);
			} catch (RuntimeException ex) {
				throw ex;
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		} catch (RuntimeException ex) {
			throw ex;
		} catch (Throwable ex) {
			throw new RuntimeException(ex);
		}
		//
	}

	/** Save the updates of the persons' quality indicators.
	 *
	 * @param data the Json map of the changes.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@PostMapping(value = "/" + Constants.SAVE_PERSON_INDICATOR_UPDATES_ENDPOINT)
	public void savePersonIndicatorUpdates(
			@RequestParam(required = true) String data,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.SAVE_PERSON_INDICATOR_UPDATES_ENDPOINT);
		//
		final Map<Integer, PersonIndicators> updates = new HashMap<>();
		if (!Strings.isNullOrEmpty(data)) {
			final ObjectMapper mapper = JsonUtils.createMapper();
			@SuppressWarnings("unchecked")
			Map<String, Map<String, Object>> data0 = mapper.readValue(data, Map.class);
			for (final Entry<String, Map<String, Object>> entry : data0.entrySet()) {
				final Integer id = inInteger(entry.getKey());
				if (id == null) {
					throw new IllegalArgumentException("Invalid person identifier: " + entry.getKey()); //$NON-NLS-1$
				}
				final Map<String, Object> json = entry.getValue();
				final PersonIndicators indicators = new PersonIndicators(
						inInt(json.get("wosHindex"), -1), inInt(json.get("wosCitations"), -1), //$NON-NLS-1$ //$NON-NLS-2$
						inInt(json.get("scopusHindex"), -1), inInt(json.get("scopusCitations"), -1), //$NON-NLS-1$ //$NON-NLS-2$
						inInt(json.get("scholarHindex"), -1), inInt(json.get("scholarCitations"), -1)); //$NON-NLS-1$ //$NON-NLS-2$
				updates.put(id, indicators);
			}
		}
		//
		this.personService.setPersonIndicators(updates);
	}

}
