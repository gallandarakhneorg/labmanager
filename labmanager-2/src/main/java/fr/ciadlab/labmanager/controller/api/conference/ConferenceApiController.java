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

package fr.ciadlab.labmanager.controller.api.conference;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.conference.Conference;
import fr.ciadlab.labmanager.io.json.JsonUtils;
import fr.ciadlab.labmanager.service.conference.ConferenceService;
import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.progress.DefaultProgression;
import org.arakhne.afc.progress.ProgressionEvent;
import org.arakhne.afc.progress.ProgressionListener;
import org.arakhne.afc.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

/** REST Controller for conferences.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see ConferenceService
 * @since 3.6
 */
@RestController
@CrossOrigin
public class ConferenceApiController extends AbstractApiController {

	private ConferenceService conferenceService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param conferenceService the conference service.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public ConferenceApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ConferenceService conferenceService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.conferenceService = conferenceService;
	}

	/** Saving information of a conference. 
	 *
	 * @param conference the identifier of the conference. If the identifier is not provided, this endpoint is supposed to create
	 *     a conference in the database.
	 * @param acronym the acronym of the conference.
	 * @param name the name of the conference.
	 * @param publisher the name of the publisher of the conference.
	 * @param isbn the ISBN number for the conference.
	 * @param issn the ISSN number for the conference.
	 * @param openAccess indicates if the conference is open access or not.
	 * @param conferenceUrl the URL to the page of the conference.
	 * @param coreId the identifier of the conference on the CORE website.
	 * @param enclosingConference the identifiers of the conference that is enclosing the current conference.
	 * @param validated indicates if the journal is validated by a local authority.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case the journal cannot be saved
	 */
	@PutMapping(value = "/" + Constants.CONFERENCE_SAVING_ENDPOINT)
	public void saveConference(
			@RequestParam(required = false) Integer conference,
			@RequestParam(required = false) String acronym,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String publisher,
			@RequestParam(required = false) String isbn,
			@RequestParam(required = false) String issn,
			@RequestParam(required = false) Boolean openAccess,
			@RequestParam(required = false) String conferenceUrl,
			@RequestParam(required = false) String coreId,
			@RequestParam(required = false) Integer enclosingConference,
			@RequestParam(required = false, defaultValue = "false") boolean validated,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.CONFERENCE_SAVING_ENDPOINT, conference);
		//
		final String inAcronym = inString(acronym);
		final String inName = inString(name);
		final String inPublisher = inString(publisher);
		final String inIsbn = inString(isbn);
		final String inIssn = inString(issn);
		final String inConferenceUrl = inString(conferenceUrl);
		final String inCoreId = inString(coreId);
		//
		final Optional<Conference> optConference;
		if (conference == null) {
			optConference = this.conferenceService.createConference(
					validated, inAcronym, inName, inPublisher, inIsbn, inIssn,
					openAccess, inConferenceUrl, inCoreId, enclosingConference);
		} else {
			optConference = this.conferenceService.updateConference(conference.intValue(),
					validated, inAcronym, inName, inPublisher, inIsbn, inIssn,
					openAccess, inConferenceUrl, inCoreId, enclosingConference);
		}
		if (optConference == null || optConference.isEmpty()) {
			throw new IllegalStateException("Conference not found"); //$NON-NLS-1$
		}
	}

	/** Delete a conference from the database.
	 *
	 * @param conference the identifier of the conference.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@DeleteMapping("/" + Constants.CONFERENCE_DELETING_ENDPOINT)
	public void deleteConference(
			@RequestParam(name = Constants.CONFERENCE_ENDPOINT_PARAMETER) Integer conference,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.CONFERENCE_DELETING_ENDPOINT, conference);
		if (conference == null || conference.intValue() == 0) {
			throw new IllegalStateException("Conference not found"); //$NON-NLS-1$
		}
		this.conferenceService.removeConference(conference.intValue());
	}

	/** Saving ranking of a conference. 
	 *
	 * @param conference the identifier of the conference.
	 * @param year the identifier of the conference.
	 * @param coreIndex the CORE Index of the conference for the given year.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case the journal ranking cannot be saved
	 */
	@PutMapping(value = "/" + Constants.SAVE_CONFERENCE_RANKING_ENDPOINT)
	public void saveConferenceRanking(
			@RequestParam(required = true) int conference,
			@RequestParam(required = true) int year,
			@RequestParam(required = false) String coreIndex,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.SAVE_CONFERENCE_RANKING_ENDPOINT, Integer.valueOf(conference));
		final Conference conferenceObj = this.conferenceService.getConferenceById(conference);
		if (conferenceObj == null) {
			throw new IllegalArgumentException("Conference not found with: " + conference); //$NON-NLS-1$
		}
		final String inCoreIndex = inString(coreIndex);
		final CoreRanking core = inCoreIndex == null ? null : CoreRanking.valueOfCaseInsensitive(inCoreIndex);
		this.conferenceService.setQualityIndicators(conferenceObj, year, core);
	}

	/** Delete a conference ranking from the database.
	 *
	 * @param conference the identifier of the conference.
	 * @param year the identifier of the conference.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@DeleteMapping("/" + Constants.DELETE_CONFERENCE_RANKING_ENDPOINT)
	public void deleteJournalRanking(
			@RequestParam(required = true) int conference,
			@RequestParam(required = true) int year,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.DELETE_CONFERENCE_RANKING_ENDPOINT, Integer.valueOf(conference), Integer.valueOf(year));
		final Conference conferenceObj = this.conferenceService.getConferenceById(conference);
		if (conferenceObj == null) {
			throw new IllegalArgumentException("Conference not found with: " + conference); //$NON-NLS-1$
		}
		this.conferenceService.deleteQualityIndicators(conferenceObj, year);
	}

	/** Show the updater for conference rankings.
	 *
	 * @param year the reference year.
	 * @param username the name of the logged-in user.
	 * @return the dynamic updated for the front-end.
	 * @throws Exception if there is error for obtaining the new indicators.
	 */
	@GetMapping(value = "/" + Constants.GET_JSON_FOR_CONFERENCE_INDICATOR_UPDATES_ENDPOINT)
	public SseEmitter getJsonForConferenceIndicatorUpdates(
			@RequestParam(required = true) int year,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.GET_JSON_FOR_CONFERENCE_INDICATOR_UPDATES_ENDPOINT, Integer.valueOf(year));
		//
		final ExecutorService service = Executors.newSingleThreadExecutor();
		final SseEmitter emitter = new SseEmitter(Long.valueOf(Constants.SSE_TIMEOUT));
		service.execute(() -> {
			asyncGetJsonForConferenceIndicatorUpdates(emitter, year);
		});
		return emitter;
	}

	private void asyncGetJsonForConferenceIndicatorUpdates(SseEmitter emitter, int year) {
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
			this.conferenceService.computeConferenceRankingIndicatorUpdates(year, progress, (conference, indicators) -> {
				indicators.put("id", Integer.valueOf(conference.getId())); //$NON-NLS-1$
				indicators.put("name", conference.getAcronym() + " - " + conference.getName()); //$NON-NLS-1$ //$NON-NLS-2$
				// Add the entry in the list according to the name of the conferences
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
	}

	/** Save the updates of the conferences' quality indicators.
	 *
	 * @param data the map of the changes. Expected keys are {@code referenceYear} for the reference year; and
	 *     {@code changes} for the changes to apply to the quality indicators.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/" + Constants.SAVE_CONFERENCE_INDICATOR_UPDATES_ENDPOINT)
	public void saveJournalIndicatorUpdates(
			@RequestParam(required = true) Map<String, String> data,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.SAVE_CONFERENCE_INDICATOR_UPDATES_ENDPOINT);
		final int referenceYear = ensureYear(data, "referenceYear"); //$NON-NLS-1$
		final String rawChanges = ensureString(data, "changes"); //$NON-NLS-1$
		if (Strings.isNullOrEmpty(rawChanges)) {
			throw new IllegalArgumentException("Changes are expected"); //$NON-NLS-1$
		}
		Map<Integer, CoreRanking> changes = null;
		try (StringReader sr = new StringReader(rawChanges)) {
			final ObjectMapper mapper = JsonUtils.createMapper();
			try (JsonParser parser = mapper.createParser(sr)) {
				final Map<String, String> stringMap = parser.readValueAs(Map.class);
				changes = stringMap.entrySet().parallelStream().collect(Collectors.toConcurrentMap(
						it -> Integer.valueOf(it.getKey().toString()),
						it -> CoreRanking.valueOfCaseInsensitive(it.getValue())));
			}
		}
		if (changes != null) {
			this.conferenceService.updateConferenceIndicators(referenceYear, changes);
		}
	}
	
}
