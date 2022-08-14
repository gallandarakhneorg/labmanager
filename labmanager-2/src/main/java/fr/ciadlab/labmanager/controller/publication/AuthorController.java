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

import java.util.Random;

import fr.ciadlab.labmanager.controller.AbstractController;
import fr.ciadlab.labmanager.controller.member.PersonController;
import fr.ciadlab.labmanager.service.member.MembershipService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.service.publication.AuthorshipService;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

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
