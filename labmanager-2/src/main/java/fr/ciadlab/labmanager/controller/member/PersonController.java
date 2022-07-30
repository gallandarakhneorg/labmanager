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
 * you entered into with the CIAD.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.controller.member;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import fr.ciadlab.labmanager.controller.AbstractController;
import fr.ciadlab.labmanager.controller.publication.AuthorController;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.locale.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for persons.
 * <p>
 * This controller does not manage the memberships' or authorships' databases themselves.
 * You must use {@link AuthorController} for managing the authorships entries.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see AuthorController
 */
@RestController
@CrossOrigin
public class PersonController extends AbstractController {

	private static final String TOOL_NAME = "personTool"; //$NON-NLS-1$

	private PersonService personService;

	private PersonNameParser nameParser;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param personService the person service.
	 * @param nameParser the parser of person names.
	 */
	public PersonController(@Autowired PersonService personService, @Autowired PersonNameParser nameParser) {
		super(TOOL_NAME);
		this.personService = personService;
		this.nameParser = nameParser;
	}

	/** Replies the model-view component for managing the persons.
	 *
	 * @return the model-view component.
	 */
	@GetMapping("/" + TOOL_NAME)
	public ModelAndView showPersonTool() {
		final ModelAndView modelAndView = new ModelAndView(TOOL_NAME);
		modelAndView.addObject("persons", this.personService.getAllPersons()); //$NON-NLS-1$
		return modelAndView;
	}

	/** Replies the information about a person.
	 *
	 * @param name the name of the person. The format should be recognized by a {@link PersonNameParser}.
	 *     Usually, it is {@code "FIRST LAST"} or {@code "LAST, FIRST"}.
	 * @return the person, or {@code null} if the person with the given name was not found.
	 */
	@GetMapping("/getPersonData")
	public Person getPersonData(@RequestParam String name) {
		final String oldFirstName = this.nameParser.parseFirstName(name);
		final String oldLastName = this.nameParser.parseLastName(name);
		final int id = this.personService.getPersonIdByName(oldFirstName, oldLastName);
		if (id != 0) {
			return this.personService.getPerson(id);
		}
		return null;
	}

	/** Add a person into the database.
	 *
	 * @param response JEE response.
	 * @param firstName the first name of the person.
	 * @param lastName the last name of the person.
	 * @param email the email of the person.
	 * @throws Exception if the redirection to success/failure page cannot be done.
	 */
	@RequestMapping(value = "/addPerson", method = RequestMethod.POST)
	public void addPerson(HttpServletResponse response,
			@RequestParam String firstName,
			@RequestParam String lastName,
			@RequestParam String email) throws Exception {
		try {
			this.personService.createPerson(firstName, lastName, email);
			final String msg;
			if (!Strings.isNullOrEmpty(email)) {
				msg = firstName + " " + lastName + " <" + email + ">"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} else {
				msg = firstName + " " + lastName; //$NON-NLS-1$
			}
			redirectCreated(response, msg);
		} catch (Exception ex) {
			redirectError(response, ex);
		}
	}

	/** Edit the fields of a person in the database.
	 *
	 * @param response the HTTP response.
	 * @param person the name of the person to be changed. The format should be recognized by a {@link PersonNameParser}.
	 *     Usually, it is {@code "FIRST LAST"} or {@code "LAST, FIRST"}.
	 * @param firstName the new first name.
	 * @param lastName the new last name.
	 * @param email the new email.
	 * @throws Exception if the redirection to success/failure page cannot be done.
	 */
	@PostMapping("/editPerson")
	public void editPerson(HttpServletResponse response,
			@RequestParam String person,
			@RequestParam String firstName,
			@RequestParam String lastName ,
			@RequestParam String email) throws Exception {
		try {
			final String oldFirstName = this.nameParser.parseFirstName(person);
			final String oldLastName = this.nameParser.parseLastName(person);
			final int id = this.personService.getPersonIdByName(oldFirstName, oldLastName);
			if (id != 0) {
				this.personService.updatePerson(id, firstName, lastName, email);
				redirectUpdated(response, person);
			} else {
				redirectError(response, Locale.getString("NO_PERSON_ERROR", person)); //$NON-NLS-1$
			}
		} catch (Exception ex) {
			redirectError(response, ex);
		}
	}

	/** Delete a person from the database.
	 *
	 * @param response the HTTP response.
	 * @param name the name of the person to be changed. The format should be recognized by a {@link PersonNameParser}.
	 *     Usually, it is {@code "FIRST LAST"} or {@code "LAST, FIRST"}.
	 * @throws Exception if the redirection to success/failure page cannot be done.
	 */
	@PostMapping("/deletePerson")
	public void deletePerson(HttpServletResponse response,
			@RequestParam String name) throws Exception {
		try {
			final String oldFirstName = this.nameParser.parseFirstName(name);
			final String oldLastName = this.nameParser.parseLastName(name);
			final int id = this.personService.getPersonIdByName(oldFirstName, oldLastName);
			if (id != 0) {
				this.personService.removePerson(id);
				redirectDeleted(response, name);
			} else {
				redirectError(response, Locale.getString("NO_PERSON_ERROR", name)); //$NON-NLS-1$
			}
		} catch (Exception ex) {
			redirectError(response, ex);
		}
	}

	/** Replies the duplicate person names.
	 *
	 * @return the model-view that shows the duplicate persons.
	 */
	@GetMapping("/personDuplicate")
	public ModelAndView personDuplicate() {
		final ModelAndView modelAndView = new ModelAndView("personDuplicate"); //$NON-NLS-1$
		final List<Set<Person>> matchingAuthors = this.personService.computePersonDuplicate();
		modelAndView.addObject("matchingAuthors", matchingAuthors); //$NON-NLS-1$
		return modelAndView;
	}

}



