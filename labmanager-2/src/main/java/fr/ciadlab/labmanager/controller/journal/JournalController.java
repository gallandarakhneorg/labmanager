/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.controller.journal;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import fr.ciadlab.labmanager.controller.AbstractController;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.ranking.QuartileRanking;
import fr.ciadlab.labmanager.service.journal.JournalService;
import org.arakhne.afc.vmutil.locale.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for journals.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see JournalService
 */
@RestController
@CrossOrigin
public class JournalController extends AbstractController {

	private static final String TOOL_NAME = "journalTool"; //$NON-NLS-1$

	private JournalService journalService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param journalService the journal service.
	 */
	public JournalController(@Autowired JournalService journalService) {
		super(TOOL_NAME);
		this.journalService = journalService;
	}

	/** Replies the model-view component for managing the journals.
	 *
	 * @return the model-view component.
	 */
	@GetMapping("/" + TOOL_NAME)
	public ModelAndView showJournalTool() {
		final ModelAndView modelAndView = new ModelAndView(TOOL_NAME);
		modelAndView.addObject("journals", this.journalService.getAllJournals()); //$NON-NLS-1$
		return modelAndView;
	}

	/** Add a journal into the database.
	 *
	 * @param response JEE response.
	 * @param name the name of the journal.
	 * @param publisher the name of the publisher of the journal.
	 * @param url the URL to the page of the journal on the publisher website.
	 * @param scimagoId the identifier to the page of the journal on the Scimago website.
	 * @param wosId the identifier to the page of the journal on the Web-Of-Science website.
	 * @throws Exception if the redirection to success/failure page cannot be done.
	 */
	@PostMapping("/addJournal")
	public void addJournal(HttpServletResponse response,
			@RequestParam String name,
			@RequestParam String publisher,
			@RequestParam String url,
			@RequestParam String scimagoId,
			@RequestParam String wosId) throws Exception {
		try {
			this.journalService.createJournal(
					name, publisher, url, scimagoId, wosId);
			redirectCreated(response, name);
		} catch (Exception ex) {
			redirectError(response, ex);
		}

	}

	/** Replies data about a specific journal from the database.
	 *
	 * @param response JEE response.
	 * @param name the name of the journal.
	 * @return the journal.
	 */
	@GetMapping("/getJournalData")
	public Journal getJournalData(@RequestParam String name) {
		return this.journalService.getJournalByName(name);
	}

	/** Replies the quality indicators for a specific journal.
	 *
	 * @param year the year for which the indicators must be replied.
	 * @param name the name of the journal.
	 * @return the quality indicators, or {@code null} if no indicator is available.
	 */
	@GetMapping("/getJournalQualityIndicators")
	public @ResponseBody Map<String, String> getJournalQualityIndicators(
			@RequestParam int year,
			@RequestParam String name) {
		final Journal journal = this.journalService.getJournalByName(name);
		if (journal != null && journal.hasQualityIndicatorsForYear(year)) {
			// creation of a JSON object containing each of the journal's quality indicators
			final Map<String, String> journalIndicators = new HashMap<>();

			// Get of the individual indicators
			final QuartileRanking scimago = journal.getScimagoQIndexByYear(year);
			final QuartileRanking wos = journal.getWosQIndexByYear(year);
			final float impactFactor = journal.getImpactFactorByYear(year);

			// Addition of the indicators in the json map
			if (scimago != null) {
				journalIndicators.put("scimagoQuartile", scimago.name()); //$NON-NLS-1$
			}
			if (wos != null) {
				journalIndicators.put("wosQuartile", wos.name()); //$NON-NLS-1$
			}
			if (impactFactor != 0) {
				journalIndicators.put("impactFactor", Float.toString(impactFactor)); //$NON-NLS-1$
			}

			if (!journalIndicators.isEmpty()) {
				return journalIndicators;
			}
		}
		return null;
	}

	/** Edit the fields of a journal in the database.
	 *
	 * @param response the HTTP response.
	 * @param journal original name of the journal that allows to find it in the database. 
	 * @param name the name of the journal.
	 * @param publisher the name of the publisher of the journal.
	 * @param url the URL to the page of the journal on the publisher website.
	 * @param scimagoId the identifier to the page of the journal on the Scimago website.
	 * @param wosId the identifier to the page of the journal on the Web-Of-Science website.
	 * @throws Exception if the redirection to success/failure page cannot be done.
	 */
	@PostMapping("/editJournal")
	public void editJournal(HttpServletResponse response,
			@RequestParam String journal,
			@RequestParam String name,
			@RequestParam String publisher,
			@RequestParam String url,
			@RequestParam String scimagoId,
			@RequestParam String wosId) throws Exception {
		try {
			final int journalId = this.journalService.getJournalIdByName(journal);
			if (journalId != 0) {
				this.journalService.updateJournal(journalId, name, publisher, url, scimagoId, wosId);
				redirectUpdated(response, journal);
			} else {
				redirectError(response, Locale.getString("NO_JOURNAL_ERROR", journal)); //$NON-NLS-1$
			}
		} catch (Exception ex) {
			redirectError(response, ex);
		}
	}

}
