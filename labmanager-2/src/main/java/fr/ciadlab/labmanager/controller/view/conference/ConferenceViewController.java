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

package fr.ciadlab.labmanager.controller.view.conference;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.conference.Conference;
import fr.ciadlab.labmanager.entities.conference.ConferenceQualityAnnualIndicators;
import fr.ciadlab.labmanager.service.conference.ConferenceService;
import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for conference views.
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
public class ConferenceViewController extends AbstractViewController {

	private ConferenceService conferenceService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param conferenceService the conference service.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public ConferenceViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ConferenceService conferenceService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.conferenceService = conferenceService;
	}

	/** Replies the model-view component for managing the conferences.
	 *
	 * @param username the name of the logged-in user.
	 * @return the model-view component.
	 */
	@GetMapping("/" + Constants.CONFERENCE_LIST_ENDPOINT)
	public ModelAndView conferenceList(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		readCredentials(username, Constants.CONFERENCE_LIST_ENDPOINT);
		final ModelAndView modelAndView = new ModelAndView(Constants.CONFERENCE_LIST_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView, false);
		initAdminTableButtons(modelAndView, endpoint(Constants.CONFERENCE_EDITING_ENDPOINT, "conference")); //$NON-NLS-1$
		modelAndView.addObject("conferences", this.conferenceService.getAllConferences()); //$NON-NLS-1$
		modelAndView.addObject("currentYear", Integer.valueOf(LocalDate.now().getYear())); //$NON-NLS-1$
		return modelAndView;
	}

	/** Show the editor for a conference. This editor permits to create or to edit a conference.
	 *
	 * @param conference the identifier of the conference to edit. If it is {@code null}, the endpoint
	 *     is dedicated to the creation of a conference.
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 */
	@GetMapping(value = "/" + Constants.CONFERENCE_EDITING_ENDPOINT)
	public ModelAndView conferenceEditor(
			@RequestParam(required = false) Integer conference,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		ensureCredentials(username, Constants.CONFERENCE_EDITING_ENDPOINT);
		final ModelAndView modelAndView = new ModelAndView("conferenceEditor"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, false);
		//
		final Conference conferenceObj;
		if (conference != null && conference.intValue() != 0) {
			conferenceObj = this.conferenceService.getConferenceById(conference.intValue());
			if (conferenceObj == null) {
				throw new IllegalArgumentException("Conference not found: " + conference); //$NON-NLS-1$
			}
		} else {
			conferenceObj = null;
		}
		//
		List<Conference> otherConferences = this.conferenceService.getAllConferences();
		if (conferenceObj != null) {
			otherConferences = otherConferences.stream().filter(it -> it.getId() != conferenceObj.getId())
					.sorted((a, b) -> a.getAcronym().compareToIgnoreCase(b.getAcronym())).collect(Collectors.toList());
		}
		//
		modelAndView.addObject("conference", conferenceObj); //$NON-NLS-1$
		modelAndView.addObject("otherConferences", otherConferences); //$NON-NLS-1$
		modelAndView.addObject("formActionUrl", rooted(Constants.CONFERENCE_SAVING_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("formRedirectUrl", rooted(Constants.CONFERENCE_LIST_ENDPOINT)); //$NON-NLS-1$
		//
		return modelAndView;
	}

	/** Show the editor for a conference ranking.
	 *
	 * @param id the identifier of the conference to edit.
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 */
	@GetMapping(value = "/conferenceRankingEditor")
	public ModelAndView conferenceRankingEditor(
			@RequestParam(required = true) int id,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		ensureCredentials(username, "conferenceRankingEditor", Integer.valueOf(id)); //$NON-NLS-1$
		final ModelAndView modelAndView = new ModelAndView("conferenceRankingEditor"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, false);
		//
		final Conference conference = this.conferenceService.getConferenceById(id);
		if (conference == null) {
			throw new IllegalArgumentException("Conference not found: " + id); //$NON-NLS-1$
		}
		//
		modelAndView.addObject("savingUrl", endpoint(Constants.SAVE_CONFERENCE_RANKING_ENDPOINT, null)); //$NON-NLS-1$
		modelAndView.addObject("deletionUrl", endpoint(Constants.DELETE_CONFERENCE_RANKING_ENDPOINT, null)); //$NON-NLS-1$
		modelAndView.addObject("conference", conference); //$NON-NLS-1$
		final Map<Integer, ConferenceQualityAnnualIndicators> sortedMap = new TreeMap<>((a, b) -> {
			if (a == b) {
				return 0;
			}
			if (a == null) {
				return 1;
			}
			if (b == null) {
				return -1;
			}
			return Integer.compare(b.intValue(), a.intValue());
		});
		sortedMap.putAll(conference.getQualityIndicators());
		modelAndView.addObject("qualityIndicators", sortedMap); //$NON-NLS-1$
		//
		Integer year = null;
		CoreRanking core = null;
		final Iterator<ConferenceQualityAnnualIndicators> iterator = sortedMap.values().iterator();
		while (iterator.hasNext() && core == null) {
			final ConferenceQualityAnnualIndicators indicators = iterator.next();
			if (year == null) {
				year = Integer.valueOf(indicators.getReferenceYear());
			}
			if (indicators.getCoreIndex() != null) {
				core = indicators.getCoreIndex();
			}
		}
		if (core == CoreRanking.NR) {
			core = null;
		}
		final int currentYear = LocalDate.now().getYear();
		modelAndView.addObject("currentYear", Integer.valueOf(currentYear)); //$NON-NLS-1$
		modelAndView.addObject("lastReferenceYear", year); //$NON-NLS-1$
		modelAndView.addObject("lastCoreIndex", core); //$NON-NLS-1$
		final Set<Integer> years = new TreeSet<>((a, b) -> {
			if (a == b) {
				return 0;
			}
			if (a == null) {
				return 1;
			}
			if (b == null) {
				return -1;
			}
			return Integer.compare(b.intValue(), a.intValue());
		});
		for (int y = currentYear - 20; y <= currentYear; ++y) {
			if (!sortedMap.containsKey(Integer.valueOf(y))) {
				years.add(Integer.valueOf(y));
			}
		}
		modelAndView.addObject("years", years); //$NON-NLS-1$
		//
		return modelAndView;
	}

	/** Show the updater for a conference rankings.
	 *
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 */
	@GetMapping(value = "/conferenceRankingUpdater")
	public ModelAndView conferenceRankingUpdater(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		ensureCredentials(username, "conferenceRankingUpdater"); //$NON-NLS-1$
		final ModelAndView modelAndView = new ModelAndView("conferenceRankingUpdater"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, false);
		//
		modelAndView.addObject("getJsonUpdates", endpoint(Constants.GET_JSON_FOR_CONFERENCE_INDICATOR_UPDATES_ENDPOINT, Constants.YEAR_ENDPOINT_PARAMETER, "")); //$NON-NLS-1$ //$NON-NLS-2$
		modelAndView.addObject("referenceYear", Integer.valueOf(LocalDate.now().getYear() - 1)); //$NON-NLS-1$
		modelAndView.addObject("formActionUrl", endpoint(Constants.SAVE_CONFERENCE_INDICATOR_UPDATES_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("succesRedirectionUrl", endpoint(Constants.ADMIN_ENDPOINT)); //$NON-NLS-1$
		//
		return modelAndView;
	}

}
