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

import fr.ciadlab.labmanager.controller.AbstractController;
import fr.ciadlab.labmanager.controller.publication.AuthorController;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.utils.names.PersonNameComparator;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

	private static final String DEFAULT_ENDPOINT = "personList"; //$NON-NLS-1$

	private static final String MESSAGES_PREFIX = "personController."; //$NON-NLS-1$

	private MessageSourceAccessor messages;

	private PersonService personService;

	private PersonNameParser nameParser;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param personService the person service.
	 * @param nameParser the parser of person names.
	 */
	public PersonController(@Autowired MessageSourceAccessor messages,
			@Autowired PersonService personService, @Autowired PersonNameParser nameParser) {
		super(DEFAULT_ENDPOINT);
		this.messages = messages;
		this.personService = personService;
		this.nameParser = nameParser;
	}

	/** Replies the model-view component for showing the persons independently of the organization memberships.
	 *
	 * @return the model-view component.
	 */
	@GetMapping("/" + DEFAULT_ENDPOINT)
	public ModelAndView personList() {
		final ModelAndView modelAndView = new ModelAndView(DEFAULT_ENDPOINT);
		modelAndView.addObject("persons", this.personService.getAllPersons()); //$NON-NLS-1$
		return modelAndView;
	}

	/** Replies the information about a person as a JSON stream.
	 * This endpoint accepts one of the two parameters: the name or the identifier of the person.
	 * The name test is based on
	 * {@link PersonNameComparator#isSimilar(String, String) name similarity}, and not on strict equality.
	 *
	 * @param name the name of the person. The format should be recognized by a {@link PersonNameParser}.
	 *     Usually, it is {@code "FIRST LAST"} or {@code "LAST, FIRST"}.
	 * @param id the identifier of the person.
	 * @param strictName indicates if the name test must be strict (equality test) or not (similarity test).
	 *     By default, this parameter has the value {@code false}.
	 * @return the person, or {@code null} if the person with the given name was not found.
	 * @see PersonNameComparator
	 */
	@GetMapping("/getPersonData")
	public Person getPersonData(@RequestParam(required = false) String name,
			@RequestParam(required = false) Integer id,
			@RequestParam(defaultValue = "false", required = false) boolean strictName) {
		if (id == null && Strings.isNullOrEmpty(name)) {
			throw new IllegalArgumentException("Name and identifier parameters are missed"); //$NON-NLS-1$
		}
		int pid = 0;
		if (Strings.isNullOrEmpty(name)) {
			if (id != null) {
				pid = id.intValue();
			}
		} else {
			final String firstName = this.nameParser.parseFirstName(name);
			final String lastName = this.nameParser.parseLastName(name);
			if (strictName) {
				pid = this.personService.getPersonIdByName(firstName, lastName);
			} else {
				pid = this.personService.getPersonIdBySimilarName(firstName, lastName);
			}
		}
		if (pid != 0) {
			return this.personService.getPerson(pid);
		}
		return null;
	}

	//	/** Add a person into the database.
	//	 *
	//	 * @param response JEE response.
	//	 * @param firstName the first name of the person.
	//	 * @param lastName the last name of the person.
	//	 * @param gender the string representation of the persons' gender.
	//	 * @param email the email of the person.
	//	 * @param orcid the ORCID of the person.
	//	 * @throws Exception if the redirection to success/failure page cannot be done.
	//	 * @see Gender
	//	 */
	//	@RequestMapping(value = "/addPerson", method = RequestMethod.POST)
	//	public void addPerson(HttpServletResponse response,
	//			@RequestParam String firstName,
	//			@RequestParam String lastName,
	//			@RequestParam String gender,
	//			@RequestParam String email,
	//			@RequestParam String orcid) throws Exception {
	//		try {
	//			this.personService.createPerson(firstName, lastName, gender, email, orcid);
	//			final String msg;
	//			if (!Strings.isNullOrEmpty(email)) {
	//				msg = firstName + " " + lastName + " <" + email + ">"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	//			} else {
	//				msg = firstName + " " + lastName; //$NON-NLS-1$
	//			}
	//			redirectCreated(response, msg);
	//		} catch (Exception ex) {
	//			redirectError(response, ex);
	//		}
	//	}
	//
	//	/** Edit the fields of a person in the database.
	//	 *
	//	 * @param response the HTTP response.
	//	 * @param person the name of the person to be changed. The format should be recognized by a {@link PersonNameParser}.
	//	 *     Usually, it is {@code "FIRST LAST"} or {@code "LAST, FIRST"}.
	//	 * @param firstName the new first name.
	//	 * @param lastName the new last name.
	//	 * @param email the new email.
	//	 * @param orcid the new ORCID.
	//	 * @throws Exception if the redirection to success/failure page cannot be done.
	//	 */
	//	@PostMapping("/editPerson")
	//	public void editPerson(HttpServletResponse response,
	//			@RequestParam String person,
	//			@RequestParam String firstName,
	//			@RequestParam String lastName,
	//			@RequestParam String email,
	//			@RequestParam String orcid) throws Exception {
	//		try {
	//			final String oldFirstName = this.nameParser.parseFirstName(person);
	//			final String oldLastName = this.nameParser.parseLastName(person);
	//			final int id = this.personService.getPersonIdByName(oldFirstName, oldLastName);
	//			if (id != 0) {
	//				this.personService.updatePerson(id, firstName, lastName, email, orcid);
	//				redirectUpdated(response, person);
	//			} else {
	//				redirectError(response, this.messages.getMessage(MESSAGES_PREFIX + "NO_PERSON_ERROR", new Object[] {person})); //$NON-NLS-1$
	//			}
	//		} catch (Exception ex) {
	//			redirectError(response, ex);
	//		}
	//	}
	//
	//	/** Delete a person from the database.
	//	 *
	//	 * @param response the HTTP response.
	//	 * @param name the name of the person to be changed. The format should be recognized by a {@link PersonNameParser}.
	//	 *     Usually, it is {@code "FIRST LAST"} or {@code "LAST, FIRST"}.
	//	 * @throws Exception if the redirection to success/failure page cannot be done.
	//	 */
	//	@PostMapping("/deletePerson")
	//	public void deletePerson(HttpServletResponse response,
	//			@RequestParam String name) throws Exception {
	//		try {
	//			final String oldFirstName = this.nameParser.parseFirstName(name);
	//			final String oldLastName = this.nameParser.parseLastName(name);
	//			final int id = this.personService.getPersonIdByName(oldFirstName, oldLastName);
	//			if (id != 0) {
	//				this.personService.removePerson(id);
	//				redirectDeleted(response, name);
	//			} else {
	//				redirectError(response, this.messages.getMessage(MESSAGES_PREFIX + "NO_PERSON_ERROR", new String[] {name})); //$NON-NLS-1$
	//			}
	//		} catch (Exception ex) {
	//			redirectError(response, ex);
	//		}
	//	}
	//
	//	/** Replies the duplicate person names.
	//	 *
	//	 * @return the model-view that shows the duplicate persons.
	//	 */
	//	@GetMapping("/personDuplicate")
	//	public ModelAndView personDuplicate() {
	//		final ModelAndView modelAndView = new ModelAndView("personDuplicate"); //$NON-NLS-1$
	//		final List<Set<Person>> matchingAuthors = this.personService.computePersonDuplicate();
	//		modelAndView.addObject("matchingAuthors", matchingAuthors); //$NON-NLS-1$
	//		return modelAndView;
	//	}

}



