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

package fr.ciadlab.labmanager.controller.view.supervision;

import java.util.List;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.member.PersonComparator;
import fr.ciadlab.labmanager.entities.supervision.Supervision;
import fr.ciadlab.labmanager.service.member.MembershipService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.supervision.SupervisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for person supervision views.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.1
 */
@RestController
@CrossOrigin
public class SupervisionViewController extends AbstractViewController {

	private MembershipService membershipService;

	private SupervisionService supervisionService;

	private PersonService personService;

	private PersonComparator personComparator;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param supervisionService the service for accessing the supervisions.
	 * @param membershipService the service for managing the memberships.
	 * @param personService the service for managing the persons.
	 * @param personComparator the comparator of persons.
	 * @param nameParser the parser of person names.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public SupervisionViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired SupervisionService supervisionService,
			@Autowired MembershipService membershipService,
			@Autowired PersonService personService,
			@Autowired PersonComparator personComparator,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.supervisionService = supervisionService;
		this.membershipService = membershipService;
		this.personService = personService;
		this.personComparator = personComparator;
	}

	/** Replies the model-view component for showing the persons independently of the organization memberships.
	 *
	 * @param person the identifier of the person for who the jury memberships must be edited.
	 * @param username the name of the logged-in user.
	 * @return the model-view component.
	 */
	@GetMapping("/" + Constants.SUPERVISION_EDITING_ENDPOINT)
	public ModelAndView juryMembershipEditor(
			@RequestParam(required = true) int person,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		readCredentials(username, Constants.SUPERVISION_EDITING_ENDPOINT);
		final ModelAndView modelAndView = new ModelAndView(Constants.SUPERVISION_EDITING_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView, false);
		//
		final Person personObj = this.personService.getPersonById(person);
		if (personObj == null) {
			throw new RuntimeException("Person not found: " + person); //$NON-NLS-1$
		}
		modelAndView.addObject("person", personObj); //$NON-NLS-1$
		//
		final List<Supervision> supervisions = this.supervisionService.getSupervisionsForSupervisedPerson(person).stream()
				.sorted(EntityUtils.getPreferredSupervisionComparator()).collect(Collectors.toList());
		modelAndView.addObject("supervisions", supervisions); //$NON-NLS-1$
		//
		final List<Membership> memberships = this.membershipService.getMembershipsForPerson(person).stream()
				.filter(it -> it.getMemberStatus().isSupervisable())
				.sorted(EntityUtils.getPreferredMembershipComparator()).collect(Collectors.toList());
		modelAndView.addObject("memberships", memberships); //$NON-NLS-1$
		//
		modelAndView.addObject("allPersons", this.personService.getAllPersons().stream() //$NON-NLS-1$
				.filter(it -> it.getId() != personObj.getId() && !it.getSupervisorMemberships().isEmpty())
				.sorted(this.personComparator).collect(Collectors.toList()));
		//
		modelAndView.addObject("savingUrl", rooted(Constants.SUPERVISION_SAVING_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("deletionUrl", rooted(Constants.SUPERVISION_DELETION_ENDPOINT)); //$NON-NLS-1$
		//
		return modelAndView;
	}

}
