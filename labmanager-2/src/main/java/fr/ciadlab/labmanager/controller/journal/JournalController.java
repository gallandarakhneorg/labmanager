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

package fr.ciadlab.labmanager.controller.journal;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ciadlab.labmanager.controller.AbstractController;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.journal.JournalQualityAnnualIndicators;
import fr.ciadlab.labmanager.service.journal.JournalService;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriBuilderFactory;

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

	private static final String DEFAULT_ENDPOINT = "journalList"; //$NON-NLS-1$

	private static final String MESSAGES_PREFIX = "journalController."; //$NON-NLS-1$

	private MessageSourceAccessor messages;

	private JournalService journalService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param uriBuilderFactory the factory of URI builders.
	 * @param messages the accessor to the localized messages.
	 * @param journalService the journal service.
	 */
	public JournalController(
			@Autowired MessageSourceAccessor messages,
			@Autowired JournalService journalService) {
		super(DEFAULT_ENDPOINT);
		this.messages = messages;
		this.journalService = journalService;
	}

	/** Replies the model-view component for managing the journals.
	 *
	 * @return the model-view component.
	 */
	@GetMapping("/" + DEFAULT_ENDPOINT)
	public ModelAndView showJournalList() {
		final ModelAndView modelAndView = new ModelAndView(DEFAULT_ENDPOINT);
		modelAndView.addObject("journals", this.journalService.getAllJournals()); //$NON-NLS-1$
		modelAndView.addObject("currentYear", Integer.valueOf(LocalDate.now().getYear())); //$NON-NLS-1$
		modelAndView.addObject("uuid", generateUUID()); //$NON-NLS-1$
		return modelAndView;
	}

	/** Replies data about a specific journal from the database.
	 * This endpoint accepts one of the two parameters: the name or the identifier of the journal.
	 *
	 * @param name the name of the journal.
	 * @param id the identifier of the journal.
	 * @return the journal.
	 */
	@GetMapping(value = "/getJournalData", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Journal getJournalData(@RequestParam(required = false) String name, @RequestParam(required = false) Integer id) {
		if (id == null && Strings.isNullOrEmpty(name)) {
			throw new IllegalArgumentException("Name and identifier parameters are missed"); //$NON-NLS-1$
		}
		if (id != null) {
			return this.journalService.getJournalById(id.intValue());
		}
		return this.journalService.getJournalByName(name);
	}

	/** Replies the quality indicators for a specific journal.
	 * This endpoint accepts one of the two parameters: the name or the identifier of the journal.
	 * If the years are provided, only the quality indicators for these years are replied. Otherwise,
	 * all the quality indicators are replied.
	 *
	 * @param name the name of the journal.
	 * @param id the identifier of the journal.
	 * @param years the list of years for which the indicators must be replied.
	 * @return a map of quality indicators per year.
	 */
	@GetMapping(value = "/getJournalQualityIndicators", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Map<Integer, JournalQualityAnnualIndicators> getJournalQualityIndicators(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false, name = "year") List<Integer> years) {
		if (id == null && Strings.isNullOrEmpty(name)) {
			throw new IllegalArgumentException("Name and identifier parameters are missed"); //$NON-NLS-1$
		}
		final Journal journal;
		if (id != null) {
			journal = this.journalService.getJournalById(id.intValue());
		} else {
			journal = this.journalService.getJournalByName(name);
		}
		if (journal == null) {
			throw new IllegalArgumentException("Journal not found"); //$NON-NLS-1$
		}
		if (years != null && !years.isEmpty()) {
			final Map<Integer, JournalQualityAnnualIndicators> indicators = new HashMap<>();
			for (final Integer year : years) {
				if (year != null) {
					indicators.computeIfAbsent(year, it -> {
						return journal.getQualityIndicatorsFor(year.intValue(), null);
					});
				}
			}
			return indicators;
		}
		return journal.getQualityIndicators();
	}

	//	/** Add a journal into the database.
	//	 *
	//	 * @param response JEE response.
	//	 * @param name the name of the journal.
	//	 * @param publisher the name of the publisher of the journal.
	//	 * @param url the URL to the page of the journal on the publisher website.
	//	 * @param scimagoId the identifier to the page of the journal on the Scimago website.
	//	 * @param wosId the identifier to the page of the journal on the Web-Of-Science website.
	//	 * @throws Exception if the redirection to success/failure page cannot be done.
	//	 */
	//	@PostMapping("/addJournal")
	//	public void addJournal(HttpServletResponse response,
	//			@RequestParam String name,
	//			@RequestParam String publisher,
	//			@RequestParam String url,
	//			@RequestParam String scimagoId,
	//			@RequestParam String wosId) throws Exception {
	//		try {
	//			this.journalService.createJournal(
	//					name, publisher, url, scimagoId, wosId);
	//			redirectCreated(response, name);
	//		} catch (Exception ex) {
	//			redirectError(response, ex);
	//		}
	//
	//	}
	//
	//	/** Edit the fields of a journal in the database.
	//	 *
	//	 * @param response the HTTP response.
	//	 * @param journal original name of the journal that allows to find it in the database. 
	//	 * @param name the name of the journal.
	//	 * @param publisher the name of the publisher of the journal.
	//	 * @param url the URL to the page of the journal on the publisher website.
	//	 * @param scimagoId the identifier to the page of the journal on the Scimago website.
	//	 * @param wosId the identifier to the page of the journal on the Web-Of-Science website.
	//	 * @throws Exception if the redirection to success/failure page cannot be done.
	//	 */
	//	@PostMapping("/editJournal")
	//	public void editJournal(HttpServletResponse response,
	//			@RequestParam String journal,
	//			@RequestParam String name,
	//			@RequestParam String publisher,
	//			@RequestParam String url,
	//			@RequestParam String scimagoId,
	//			@RequestParam String wosId) throws Exception {
	//		try {
	//			final int journalId = this.journalService.getJournalIdByName(journal);
	//			if (journalId != 0) {
	//				this.journalService.updateJournal(journalId, name, publisher, url, scimagoId, wosId);
	//				redirectUpdated(response, journal);
	//			} else {
	//				redirectError(response, this.messages.getMessage(MESSAGES_PREFIX + "NO_JOURNAL_ERROR", new Object[] {journal})); //$NON-NLS-1$
	//			}
	//		} catch (Exception ex) {
	//			redirectError(response, ex);
	//		}
	//	}

}
