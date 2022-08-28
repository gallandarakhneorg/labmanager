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

package fr.ciadlab.labmanager.controller.view.member;

import java.util.Collection;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.member.MembershipApiController;
import fr.ciadlab.labmanager.controller.api.publication.PublicationApiController;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.member.Gender;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.member.WebPageNaming;
import fr.ciadlab.labmanager.service.member.MembershipService;
import fr.ciadlab.labmanager.service.member.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for persons views.
 * <p>
 * This controller does not manage the memberships' or authorships' databases themselves.
 * You must use {@link MembershipApiController} for managing the memberships, and
 * {@link PublicationApiController} for authorships.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see MembershipApiController
 * @see PublicationApiController
 */
@RestController
@CrossOrigin
public class PersonViewController extends AbstractViewController {

	private PersonService personService;

	private MembershipService membershipService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param personService the person service.
	 * @param membershipService the service for managing the memberships.
	 */
	public PersonViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PersonService personService,
			MembershipService membershipService) {
		super(messages, constants);
		this.personService = personService;
		this.membershipService = membershipService;
	}

	/** Replies the model-view component for showing the persons independently of the organization memberships.
	 *
	 * @param organization identifier of the organization for which the members must be displayed. If it is ot provided,
	 *      all the persons are considered, independently of the memberships. If this parameter is provided but it is equal
	 *      to {@code 0}, all the persons outside an organization will be considered.
	 * @param username the login of the logged-in person.
	 * @return the model-view component.
	 */
	@GetMapping("/" + Constants.PERSON_LIST_ENDPOINT)
	public ModelAndView showPersonList(
			@RequestParam(required = false) Integer organization,
			@CurrentSecurityContext(expression="authentication?.name") String username) {
		final ModelAndView modelAndView = new ModelAndView(Constants.PERSON_LIST_ENDPOINT);
		initModelViewProperties(modelAndView, username);
		initAdminTableButtons(modelAndView, endpoint(Constants.PERSON_EDITING_ENDPOINT, "person")); //$NON-NLS-1$
		Collection<Person> persons = null;
		if (organization != null) {
			if (organization.intValue() == 0) {
				persons = this.personService.getAllPersons().stream().filter(it -> it.getMemberships().isEmpty()).collect(Collectors.toList());
			} else {
				persons = this.membershipService.getMembersOf(organization.intValue());
			}
		}
		if (persons == null) {
			persons = this.personService.getAllPersons();
		}
		modelAndView.addObject("persons", persons); //$NON-NLS-1$
		modelAndView.addObject("membershipdeletionUrl", rooted(Constants.MEMBERSHIP_DELETION_ENDPOINT)); //$NON-NLS-1$
		return modelAndView;
	}

	/** Show the editor for a person. This editor permits to create or to edit a person.
	 *
	 * @param person the identifier of the person to edit. If it is {@code null}, the endpoint
	 *     is dedicated to the creation of a person.
	 * @param username the login of the logged-in person.
	 * @return the model-view object.
	 */
	@GetMapping(value = "/" + Constants.PERSON_EDITING_ENDPOINT)
	public ModelAndView showPersonEditor(
			@RequestParam(required = false) Integer person,
			@CurrentSecurityContext(expression="authentication?.name") String username) {
		final ModelAndView modelAndView = new ModelAndView("personEditor"); //$NON-NLS-1$
		initModelViewProperties(modelAndView, username);
		//
		final Person personObj;
		if (person != null && person.intValue() != 0) {
			personObj = this.personService.getPersonById(person.intValue());
			if (personObj == null) {
				throw new IllegalArgumentException("Person not found: " + person); //$NON-NLS-1$
			}
		} else {
			personObj = null;
		}
		//
		modelAndView.addObject("person", personObj); //$NON-NLS-1$
		modelAndView.addObject("formActionUrl", rooted(Constants.PERSON_SAVING_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("formRedirectUrl", rooted(Constants.PERSON_LIST_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("gravatarLink", Person.GRAVATAR_URL + "{0}"); //$NON-NLS-1$ //$NON-NLS-2$
		modelAndView.addObject("defaultGender", Gender.NOT_SPECIFIED); //$NON-NLS-1$
		modelAndView.addObject("defaultNaming", WebPageNaming.UNSPECIFIED); //$NON-NLS-1$
		//
		return modelAndView;
	}

}



