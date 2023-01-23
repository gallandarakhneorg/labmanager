/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the CIAD laboratory and the Université de Technologie
 * de Belfort-Montbéliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD-UTBM.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.controller.api.organization;

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
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.service.organization.OrganizationMergingService;
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

/** REST Controller for merging organizations.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.2
 */
@RestController
@CrossOrigin
public class ResearchOrganizationMergingApiController extends AbstractApiController {

	private static final int COMPUTE_DUPLICATE_ORGANIZATIONS_SERVICE_TIMEOUT = 1200000;

	private static final int PERCENT_100_NUM = 100;

	private OrganizationMergingService mergingService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param constants the constants of the app.
	 * @param mergingService the service for merging persons.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public ResearchOrganizationMergingApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired OrganizationMergingService mergingService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.mergingService = mergingService;
	}

	/** Merge multiple organizations into the database.
	 *
	 * @param target the identifier of the target organization.
	 * @param sources the list of organization identifiers that are considered as old organizations.
	 * @param username the name of the logged-in user.
	 * @throws Exception if it is impossible to merge the organizations.
	 */
	@PatchMapping("/mergeOrganizations")
	public void mergeOrganizations(
			@RequestParam(required = true) Integer target,
			@RequestParam(required = true) List<Integer> sources,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, "mergeOrganizations", target); //$NON-NLS-1$
		if (sources != null && !sources.isEmpty()) {
			try {
				this.mergingService.mergeOrganizationsById(sources, target);
			} catch (Throwable ex) {
				getLogger().error(ex.getLocalizedMessage(), ex);
				throw ex;
			}
		}
	}

	/** Compute duplicate organizations.
	 *
	 * @param username the name of the logged-in user.
	 * @return the asynchronous response.
	 */
	@GetMapping(value = "/" + Constants.COMPUTE_DUPLICATE_ORGANIZATIONS_ENDPOINT, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter computeDuplicateOrganizations(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		ensureCredentials(username, Constants.COMPUTE_DUPLICATE_ORGANIZATIONS_ENDPOINT);
		//
		final ExecutorService service = Executors.newSingleThreadExecutor();
		final SseEmitter emitter = new SseEmitter(Long.valueOf(COMPUTE_DUPLICATE_ORGANIZATIONS_SERVICE_TIMEOUT));
		service.execute(() -> {
			try {
				final MutableInt dupCount = new MutableInt();
				final MutableInt totCount = new MutableInt();
				sendDuplicateComputationStep(emitter, -1, 0, 1);
				final List<Set<ResearchOrganization>> matchingOrganizations = this.mergingService.getOrganizationDuplicates(
						// Use default organization comparator
						null,
						(organizationIndex, duplicateCount, organizationTotal) -> {
							dupCount.setValue(duplicateCount);
							totCount.setValue(organizationTotal);
							sendDuplicateComputationStep(emitter, organizationIndex, duplicateCount, organizationTotal);
						});
				int i = 0;
				final List<List<Map<String, Object>>> allDuplicates = new LinkedList<>();
				for (final Set<ResearchOrganization> duplicates : matchingOrganizations) {
					final List<Map<String, Object>> duplicateJson = new LinkedList<>();
					for (final ResearchOrganization organization : duplicates) {
						final int oid = organization.getId();
						final Map<String, Object> organizationJson = new HashMap<>();
						organizationJson.put("id", Integer.toString(oid)); //$NON-NLS-1$
						organizationJson.put("acronym", organization.getAcronym()); //$NON-NLS-1$
						organizationJson.put("name", organization.getName()); //$NON-NLS-1$
						duplicateJson.add(organizationJson);
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
		final ObjectMapper mapper = new ObjectMapper();
		final SseEventBuilder event = SseEmitter.event().data(mapper.writeValueAsString(content));
		emitter.send(event);
	}

	private void sendDuplicateArrayBuildingStep(SseEmitter emitter, int duplicateIndex, int duplicateCount, int personTotal) throws IOException {
		final int duplicatePercent = (duplicateCount * PERCENT_100_NUM) / personTotal;
		final int extraPercent = ((duplicateIndex + 1) * PERCENT_100_NUM) / duplicateCount;
		//
		final String message = getMessage("researchOrganizationMergingApiController.Progress2", //$NON-NLS-1$
				Integer.toString(PERCENT_100_NUM), Integer.toString(duplicatePercent), Integer.valueOf(extraPercent));
		//
		final Map<String, Object> content = new HashMap<>();
		content.put("duplicates", Integer.valueOf(duplicatePercent)); //$NON-NLS-1$
		content.put("percent", Integer.valueOf(100)); //$NON-NLS-1$
		content.put("extra", Integer.valueOf(extraPercent)); //$NON-NLS-1$
		content.put("terminated", Boolean.FALSE); //$NON-NLS-1$
		content.put("message", Strings.nullToEmpty(message)); //$NON-NLS-1$
		//
		final ObjectMapper mapper = new ObjectMapper();
		final SseEventBuilder event = SseEmitter.event().data(mapper.writeValueAsString(content));
		emitter.send(event);
	}

	private void sendDuplicateComputationStep(SseEmitter emitter, int personIndex, int duplicateCount, int personTotal) throws IOException {
		final int percent = ((personIndex + 1) * PERCENT_100_NUM) / personTotal;
		final int duplicatePercent = (duplicateCount * PERCENT_100_NUM) / personTotal;
		//
		final String message = getMessage("researchOrganizationMergingApiController.Progress", //$NON-NLS-1$
				Integer.toString(percent), Integer.toString(duplicatePercent));
		//
		final Map<String, Object> content = new HashMap<>();
		content.put("duplicates", Integer.valueOf(duplicatePercent)); //$NON-NLS-1$
		content.put("percent", Integer.valueOf(percent)); //$NON-NLS-1$
		content.put("extra", Integer.valueOf(0)); //$NON-NLS-1$
		content.put("terminated", Boolean.FALSE); //$NON-NLS-1$
		content.put("message", Strings.nullToEmpty(message)); //$NON-NLS-1$
		//
		final ObjectMapper mapper = new ObjectMapper();
		final SseEventBuilder event = SseEmitter.event().data(mapper.writeValueAsString(content));
		emitter.send(event);
	}

}
