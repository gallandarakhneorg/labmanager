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

package fr.ciadlab.labmanager.controller.publication;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import fr.ciadlab.labmanager.controller.AbstractController;
import fr.ciadlab.labmanager.controller.member.PersonController;
import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.member.PersonComparator;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.service.member.MemberFiltering;
import fr.ciadlab.labmanager.service.member.MembershipService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.service.publication.AuthorshipService;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for authors.
 * <p>
 * This controller does not manage the persons' database itself.
 * You must use {@link PersonController} for managing the person entries.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see PersonController
 */
@RestController
@CrossOrigin
public class AuthorController extends AbstractController {

	private static final String TOOL_NAME = "authorTool"; //$NON-NLS-1$

	private static final Random RANDOM = new Random();

	private AuthorshipService authorshipService;

	private ResearchOrganizationService organizationService;

	private MembershipService memberService;

	private PersonService personService;

	private PersonNameParser nameParser;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param authorshipService the authorship service.
	 * @param organizationService the service for managing the organizations.
	 * @param memberService the service for managing the memberships to organization.
	 * @param personService the service for the persons.
	 * @param nameParser the parser of person names.
	 */
	public AuthorController(@Autowired AuthorshipService authorshipService,
			@Autowired PersonService personService,
			@Autowired ResearchOrganizationService organizationService,
			@Autowired MembershipService memberService,
			@Autowired PersonNameParser nameParser) {
		super(TOOL_NAME);
		this.authorshipService = authorshipService;
		this.organizationService = organizationService;
		this.memberService = memberService;
		this.personService = personService;
		this.nameParser = nameParser;
	}

//	/** Replies the model-view component for managing the authors.
//	 *
//	 * @return the model-view component.
//	 */
//	@SuppressWarnings("static-method")
//	@GetMapping("/" + TOOL_NAME)
//	public ModelAndView showAuthorTool() {
//		final ModelAndView modelAndView = new ModelAndView(TOOL_NAME);
//		return modelAndView;
//	}
//
//	/** Merge two authors into the database.
//	 *
//	 * @param response the HTTP response.
//	 * @param newAuthor the name of the person who is the real author. The format should be recognized by a {@link PersonNameParser}.
//	 *     Usually, it is {@code "FIRST LAST"} or {@code "LAST, FIRST"}.
//	 * @param oldAuthor the name of the person who is invalid author. The format should be recognized by a {@link PersonNameParser}.
//	 *     Usually, it is {@code "FIRST LAST"} or {@code "LAST, FIRST"}.
//	 * @throws Exception if the redirection to success/failure page cannot be done.
//	 */
//	@PostMapping("/mergeAuthors")
//	public void mergeAuthors(HttpServletResponse response,
//			@RequestParam String newAuthor,
//			@RequestParam String oldAuthor) throws Exception {
//		try {
//			final int pubCount = this.authorshipService.mergeAuthors(oldAuthor, newAuthor);
//			redirectSuccess(response, oldAuthor, "count", Integer.toString(pubCount)); //$NON-NLS-1$
//		} catch (Exception ex) {
//			redirectError(response, ex);
//		}
//	}
//
//	/** Merge multiple authors into the database.
//	 * Publications for a given list of authors is associated to a target author and
//	 * unlinked from the old authors.
//	 *
//	 * @param response the HTTP response.
//	 * @param firstName the first name of the target author.
//	 * @param lastName the last name of the target author.
//	 * @param authorDuplicates the list of person identifiers that are considered as old authors.
//	 * @throws Exception if the redirection to success/failure page cannot be done.
//	 */
//	@PostMapping("/mergeMultipleAuthors")
//	public void mergeMultipleAuthors(HttpServletResponse response,
//			@RequestParam String firstName,
//			@RequestParam String lastName,
//			@RequestParam List<Integer> authorDuplicates) throws Exception {
//		try {
//			final int pubCount = this.authorshipService.mergeAuthors(authorDuplicates, firstName, lastName);
//			redirectSuccess(response, firstName + " " + lastName, "count", Integer.toString(pubCount)); //$NON-NLS-1$ //$NON-NLS-2$
//		} catch (Exception ex) {
//			redirectError(response, ex);
//		}
//	}
//
//	/** Replies the names of authors, except those given as arguments.
//	 *
//	 * @param excludedAuthors the list of names of authors to exclude from the list of the replied authors.
//	 * @return the key is the identifier of the person, and the value is the full name of the person.
//	 */
//	@GetMapping(value = "/getAuthorsList")
//	public @ResponseBody Map<Integer, String> getAuthorsList(
//			@RequestParam(required = true) String[] excludedAuthors) {
//		Stream<Person> stream = this.personService.getAllPersons().stream();
//		if (excludedAuthors != null && excludedAuthors.length > 0) {
//			// Build the list of persons to exclude.
//			final Set<Person> personsToExclude = new TreeSet<>(PersonComparator.DEFAULT);
//			for (final String fullName : excludedAuthors) { 
//				final String firstName = this.nameParser.parseFirstName(fullName); 
//				final String lastName = this.nameParser.parseLastName(fullName);
//				final int id = this.personService.getPersonIdByName(firstName, lastName);
//				if (id >= 0) {
//					final Person personToIgnore = this.personService.getPerson(id);
//					if (personToIgnore != null) {
//						personsToExclude.add(personToIgnore); 
//					}
//				}
//			}
//			if (!personsToExclude.isEmpty()) {
//				stream = this.personService.getAllPersons().stream().filter(
//						it -> !personsToExclude.contains(it));
//			}
//		}
//		return stream.collect(Collectors.toMap(it -> Integer.valueOf(it.getId()), it -> it.getFullName()));
//	}
//
//	/** Replies the model-view that contains the list of the authors.
//	 *
//	 * @param organization the short name of the organization for which the authors must be replied. By default, it is {@code CIAD}.
//	 * @param filtering indicates the type of members that should be considered. By default, it is {@link MemberFiltering#ALL}.
//	 * @param status the name of the member status that should be considered. If it is {@code null} or empty, all the statuses
//	 *     will be considered.
//	 * @return the model view.
//	 */
//	@GetMapping("/showAuthorsList")
//	public ModelAndView showAuthorsList(
//			@RequestParam(required = false, defaultValue = DEFAULT_ORGANIZATION) String organization,
//			@RequestParam(required = false) MemberFiltering filtering,
//			@RequestParam(required = false) String status) {
//		final String organizationName;
//		if (Strings.isNullOrEmpty(organization)) {
//			organizationName = DEFAULT_ORGANIZATION;
//		} else {
//			organizationName = organization;
//		}
//
//		final MemberFiltering memberFiltering;
//		if (filtering == null) {
//			memberFiltering = MemberFiltering.ALL;
//		} else {
//			memberFiltering = filtering;
//		}
//
//		Predicate<MemberStatus> statusFilter = null;
//		if (!Strings.isNullOrEmpty(status)) {
//			try {
//				MemberStatus s = MemberStatus.valueOfCaseInsensitive(organizationName);
//				statusFilter = it -> it == s;
//			} catch (Throwable ex) {
//				statusFilter = null;
//			}
//		}
//
//		final ModelAndView modelAndView = new ModelAndView("showAuthorsList"); //$NON-NLS-1$
//
//		Optional<ResearchOrganization> researchOrganization = this.organizationService.getResearchOrganizationByName(
//				organizationName);
//		if (researchOrganization.isEmpty()) {
//			researchOrganization = this.organizationService.getResearchOrganizationByAcronym(
//					organizationName);
//		}
//		if (researchOrganization.isPresent()) {
//			final List<Membership> members = this.memberService.getOrganizationMembers(
//					researchOrganization.get(), memberFiltering, statusFilter);
//			modelAndView.addObject("otherOrganisationsForMembers", //$NON-NLS-1$
//					this.memberService.getOtherOrganizationsForMembers(members, organizationName));
//			modelAndView.addObject("members", members); //$NON-NLS-1$
//			// UUID to generate unique html elements
//			modelAndView.addObject("uuid", Integer.valueOf(Math.abs(RANDOM.nextInt()))); //$NON-NLS-1$
//		}
//
//		return modelAndView;
//	} 

}
