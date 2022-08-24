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

package fr.ciadlab.labmanager.controller.member;

import java.util.List;
import java.util.Set;

import fr.ciadlab.labmanager.controller.AbstractController;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.service.member.PersonMergingService;
import fr.ciadlab.labmanager.service.member.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for merging persons.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@RestController
@CrossOrigin
public class PersonMergingController extends AbstractController {

	private PersonService personService;

	private PersonMergingService mergingService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param personService the person service.
	 * @param mergingService the service for merging persons.
	 */
	public PersonMergingController(
			@Autowired MessageSourceAccessor messages,
			@Autowired PersonService personService,
			@Autowired PersonMergingService mergingService) {
		super(messages);
		this.personService = personService;
		this.mergingService = mergingService;
	}

	/** Show the view that permits to analyze duplicate persons and merge them.
	 *
	 * @param username the login of the logged-in person.
	 * @return the model-view that shows the duplicate persons.
	 */
	@GetMapping("/personDuplicateList")
	public ModelAndView showPersonDuplicateList(
			@CurrentSecurityContext(expression="authentication?.name") String username) {
		final ModelAndView modelAndView = new ModelAndView("personDuplicateList"); //$NON-NLS-1$
		initModelViewProperties(modelAndView, username);
		final List<Set<Person>> matchingAuthors = this.personService.getPersonDuplicates((a, b) -> {
			if (a == b) {
				return 0;
			}
			if (a == null) {
				return -1;
			}
			if (b == null) {
				return 1;
			}
			int cmp = Integer.compare(b.getAuthorships().size(), a.getAuthorships().size());
			if (cmp != 0) {
				return cmp;
			}
			cmp = Integer.compare(b.getActiveMemberships().size(), a.getActiveMemberships().size());
			if (cmp != 0) {
				return cmp;
			}
			return EntityUtils.getPreferredPersonComparator().compare(a, b);
		});
		modelAndView.addObject("matchingPersons", matchingAuthors); //$NON-NLS-1$
		return modelAndView;
	}

	/** Merge multiple persons into the database.
	 * Publications for a given list of authors is associated to a target author and
	 * unlinked from the old authors.
	 *
	 * @param target the identifier of the target person.
	 * @param sources the list of person identifiers that are considered as old persons.
	 * @param username the login of the logged-in person.
	 * @throws Exception if it is impossible to merge the persons.
	 */
	@PostMapping("/mergePersons")
	public void mergePersons(
			@RequestParam(required = true) Integer target,
			@RequestParam(required = true) List<Integer> sources,
			@CurrentSecurityContext(expression="authentication?.name") String username) throws Exception {
		if (isLoggedUser(username).booleanValue()) {
			try {
				this.mergingService.mergePersonsById(sources, target);
			} catch (Throwable ex) {
				getLogger().error(ex.getLocalizedMessage(), ex);
				throw ex;
			}
		} else {
			throw new IllegalAccessException(getMessage("all.notLogged")); //$NON-NLS-1$
		}
	}

}
