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

package fr.ciadlab.labmanager.controller.view.journal;

import java.time.LocalDate;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.service.journal.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for journals views.
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
public class JournalViewController extends AbstractViewController {

	private static final String DEFAULT_ENDPOINT = "journalList"; //$NON-NLS-1$

	private JournalService journalService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param journalService the journal service.
	 */
	public JournalViewController(
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
		modelAndView.addObject("scimagoQIndex_imageUrl", JournalService.SCIMAGO_URL_PREFIX + "{0}"); //$NON-NLS-1$ //$NON-NLS-2$
		modelAndView.addObject("journal", journalObj); //$NON-NLS-1$
		modelAndView.addObject("formActionUrl", "/" + Constants.JOURNAL_SAVING_ENDPOINT); //$NON-NLS-1$ //$NON-NLS-2$
		//
		return modelAndView;
	}

}
