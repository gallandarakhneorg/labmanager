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

import javax.servlet.http.HttpServletResponse;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.AbstractController;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.journal.JournalQualityAnnualIndicators;
import fr.ciadlab.labmanager.service.journal.JournalService;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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

	private static final String DEFAULT_ENDPOINT = "journalList"; //$NON-NLS-1$

	private JournalService journalService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param journalService the journal service.
	 */
	public JournalController(
			@Autowired MessageSourceAccessor messages,
			@Autowired JournalService journalService) {
		super(messages);
		this.journalService = journalService;
	}

	/** Replies the model-view component for managing the journals.
	 *
	 * @param username the login of the logged-in person.
	 * @return the model-view component.
	 */
	@GetMapping("/" + DEFAULT_ENDPOINT)
	public ModelAndView showJournalList(
			@CurrentSecurityContext(expression="authentication?.name") String username) {
		final ModelAndView modelAndView = new ModelAndView(DEFAULT_ENDPOINT);
		initModelViewProperties(modelAndView, username);
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

	/** Show the editor for a journal. This editor permits to create or to edit a journal.
	 *
	 * @param journal the identifier of the journal to edit. If it is {@code null}, the endpoint
	 *     is dedicated to the creation of a journal.
	 * @param success flag that indicates the previous operation was a success.
	 * @param failure flag that indicates the previous operation was a failure.
	 * @param message the message that is associated to the state of the previous operation.
	 * @param username the login of the logged-in person.
	 * @return the model-view object.
	 */
	@GetMapping(value = "/" + Constants.JOURNAL_EDITING_ENDPOINT)
	public ModelAndView showJournalEditor(
			@RequestParam(required = false) Integer journal,
			@RequestParam(required = false, defaultValue = "false") Boolean success,
			@RequestParam(required = false, defaultValue = "false") Boolean failure,
			@RequestParam(required = false) String message,
			@CurrentSecurityContext(expression="authentication?.name") String username) {
		final ModelAndView modelAndView = new ModelAndView("journalEditor"); //$NON-NLS-1$
		//
		final Journal journalObj;
		if (journal != null && journal.intValue() != 0) {
			journalObj = this.journalService.getJournalById(journal.intValue());
			if (journalObj == null) {
				throw new IllegalArgumentException("Journal not found: " + journal); //$NON-NLS-1$
			}
		} else {
			journalObj = null;
		}
		//
		initModelViewProperties(modelAndView, username, success, failure, message);
		modelAndView.addObject("journal", journalObj); //$NON-NLS-1$
		modelAndView.addObject("formActionUrl", "/" + Constants.JOURNAL_SAVING_ENDPOINT); //$NON-NLS-1$ //$NON-NLS-2$
		//
		return modelAndView;
	}

	/** Saving information of a journal. 
	 *
	 * @param journal the identifier of the journal. If the identifier is not provided, this endpoint is supposed to create
	 *     a journal in the database.
	 * @param name the name of the journal.
	 * @param address the address of the publisher of the journal.
	 * @param publisher the name of the publisher of the journal.
	 * @param isbn the ISBN number for the journal.
	 * @param issn the ISSN number for the journal.
	 * @param openAccess indicates if the journal is open access or not.
	 * @param journalUrl the URL to the page of the journal on the publisher website.
	 * @param scimagoId the identifier to the page of the journal on the Scimago website.
	 * @param wosId the identifier to the page of the journal on the Web-Of-Science website.
	 * @param username the login of the logged-in person.
	 * @param response the HTTP response to the client.
	 */
	@PostMapping(value = "/" + Constants.JOURNAL_SAVING_ENDPOINT)
	public void saveJournal(
			@RequestParam(required = false) Integer journal,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String address,
			@RequestParam(required = false) String publisher,
			@RequestParam(required = false) String isbn,
			@RequestParam(required = false) String issn,
			@RequestParam(required = false) Boolean openAccess,
			@RequestParam(required = false) String journalUrl,
			@RequestParam(required = false) String scimagoId,
			@RequestParam(required = false) String wosId,
			@CurrentSecurityContext(expression="authentication?.name") String username,
			HttpServletResponse response) {
		if (isLoggedUser(username).booleanValue()) {
			try {
				final Journal optJournal;
				//
				final boolean newJournal;
				if (journal == null) {
					newJournal = true;
					optJournal = this.journalService.createJournal(
							name, address, publisher, isbn, issn,
							openAccess, journalUrl, scimagoId, wosId);
				} else {
					newJournal = false;
					optJournal = this.journalService.updateJournal(journal.intValue(),
							name, address, publisher, isbn, issn,
							openAccess, journalUrl, scimagoId, wosId);
				}
				if (optJournal == null) {
					throw new IllegalStateException("Journal not found"); //$NON-NLS-1$
				}
				//
				redirectSuccessToEndPoint(response, Constants.JOURNAL_EDITING_ENDPOINT,
						getMessage(
								newJournal ? "journalController.AdditionSuccess" //$NON-NLS-1$
										: "journalController.EditionSuccess", //$NON-NLS-1$
										optJournal.getJournalName(),
										Integer.valueOf(optJournal.getId())));
			} catch (Throwable ex) {
				redirectFailureToEndPoint(response, Constants.JOURNAL_EDITING_ENDPOINT, ex.getLocalizedMessage());
			}
		} else {
			redirectFailureToEndPoint(response, Constants.JOURNAL_EDITING_ENDPOINT, getMessage("all.notLogged")); //$NON-NLS-1$
		}
	}

	/** Delete a journal from the database.
	 *
	 * @param journal the identifier of the journal .
	 * @param username the login of the logged-in person.
	 * @return the HTTP response.
	 */
	@DeleteMapping("/deleteJournal")
	public ResponseEntity<Integer> deleteJournal(
			@RequestParam Integer journal,
			@CurrentSecurityContext(expression="authentication?.name") String username) {
		if (isLoggedUser(username).booleanValue()) {
			try {
				if (journal == null || journal.intValue() == 0) {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				this.journalService.removeJournal(journal.intValue());
				return new ResponseEntity<>(journal, HttpStatus.OK);
			} catch (Exception ex) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

}
