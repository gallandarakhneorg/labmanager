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

package fr.ciadlab.labmanager.controller.view.dashboard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.organization.OrganizationAddress;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.scientificaxis.ScientificAxis;
import fr.ciadlab.labmanager.service.conference.ConferenceService;
import fr.ciadlab.labmanager.service.member.MembershipService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.service.project.ProjectService;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import fr.ciadlab.labmanager.utils.ranking.JournalRankingSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for the dashboard views.
 * 
 * @author $Author: sgalland$
 * @author $Author: pgoubet$
 * @author $Author: anoubli$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see ConferenceService
 * @since 3.6
 */
@RestController
@CrossOrigin
public class DashboardViewController extends AbstractViewController {

	private ResearchOrganizationService organizationService;

	private PublicationService publicationService;
	
	private ProjectService projectService;

	private MembershipService membershipService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param organizationService the service for accessing to the research organizations.
	 * @param publicationService the service for accessing to the publications.
	 * @param projectService the service for accessing to the projects.
	 * @param membershipService the service for accessing the memberships.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public DashboardViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ResearchOrganizationService organizationService,
			@Autowired PublicationService publicationService,
			@Autowired ProjectService projectService,
			@Autowired MembershipService membershipService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.organizationService = organizationService;
		this.publicationService = publicationService;
		this.projectService = projectService;
		this.membershipService = membershipService;
	}

	/** Replies the model-view component for displaying the dashboard of the publications of a specific research organization.
	 *
	 * @param organization the identifier or the name of the organization.
	 * @param startYear the first year of the publications. If it is provided, the {@code age} is ignored.
	 * @param endYear the last year of the publications. If it is provided, the {@code age} is ignored.
	 * @param age the age of the publications. If it is not provided, the default age is {@code 6}.
	 * @param journalRanking the name of the ranking system. If it is not provided, the default ranking system is provided by
	 *      {@link JournalRankingSystem#getDefault()}, i.e. {@link JournalRankingSystem#SCIMAGO}.
	 * @param username the name of the logged-in user.
	 * @return the model-view component.
	 */
	@GetMapping("/publicationDashboard")
	public ModelAndView publicationDashboard(
			@RequestParam(required = true) String organization,
			@RequestParam(required = false) Integer startYear,
			@RequestParam(required = false) Integer endYear,
			@RequestParam(required = false, defaultValue = "6") int age,
			@RequestParam(required = false) String journalRanking,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		readCredentials(username, "publicationDashboard", organization); //$NON-NLS-1$
		//
		final JournalRankingSystem journalRankingSystem = inEnum(journalRanking, JournalRankingSystem.class, JournalRankingSystem.getDefault());
		//
		final ResearchOrganization organizationObj = getOrganizationWith(organization, this.organizationService);
		if (organizationObj == null) {
			throw new IllegalArgumentException("Organization not found for: " + organization); //$NON-NLS-1$
		}
		//
		final Set<Publication> publicationSet = this.publicationService.getPublicationsByOrganizationId(organizationObj.getId(), true, false);
		final List<Publication> publications;
		final int referenceYear;
		if (startYear != null && endYear != null) {
			final int sy;
			final int ey;
			if (startYear.intValue() > endYear.intValue()) {
				sy = endYear.intValue();
				ey = startYear.intValue();
			} else {
				sy = startYear.intValue();
				ey = endYear.intValue();
			}
			referenceYear = ey;
			publications = publicationSet.stream()
					.filter(it -> {
						final int y = it.getPublicationYear();
						return sy <= y && y <= ey;
					})
					.collect(Collectors.toList());				
		} else if (startYear != null) {
			referenceYear = LocalDate.now().getYear();
			publications = publicationSet.stream()
					.filter(it -> it.getPublicationYear() >= startYear.intValue())
					.collect(Collectors.toList());				
		} else if (endYear != null) {
			referenceYear = endYear.intValue();
			publications = publicationSet.stream()
					.filter(it -> it.getPublicationYear() <= endYear.intValue())
					.collect(Collectors.toList());				
		} else {
			referenceYear = LocalDate.now().getYear();
			final int firstYear = referenceYear - age;
			publications = publicationSet.stream()
					.filter(it -> it.getPublicationYear() >= firstYear)
					.collect(Collectors.toList());
		}
		//
		final ModelAndView modelAndView = new ModelAndView("publicationDashboard"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, false);
		modelAndView.addObject("journalRankingSystem", journalRankingSystem.getLabel()); //$NON-NLS-1$
		modelAndView.addObject("publicationsPerYear", this.publicationService.getNumberOfPublicationsPerYear(publications)); //$NON-NLS-1$
		modelAndView.addObject("publicationsPerCategory", this.publicationService.getNumberOfPublicationsPerCategory(publications, journalRankingSystem)); //$NON-NLS-1$
		modelAndView.addObject("publicationsPerQuartile", this.publicationService.getNumberOfPublicationsPerQuartile(publications, journalRankingSystem)); //$NON-NLS-1$
		modelAndView.addObject("publicationsPerJournal", this.publicationService.getNumberOfPublicationsPerJournal(publications, referenceYear)); //$NON-NLS-1$
		modelAndView.addObject("publicationsPerCountry", this.publicationService.getNumberOfPublicationsPerCountry(publications, organizationObj)); //$NON-NLS-1$
		modelAndView.addObject("publicationsPerResearchAxis", this.publicationService.getNumberOfPublicationsPerScientificAxis(publications)); //$NON-NLS-1$
		//
		final AtomicInteger minYear = new AtomicInteger();
		final AtomicInteger maxYear = new AtomicInteger();
		modelAndView.addObject("publicationsPerMember", this.publicationService.getNumberOfPublicationsPerMember(publications, organizationObj, journalRankingSystem, minYear, maxYear)); //$NON-NLS-1$
		modelAndView.addObject("publicationsPerMemberMinYear", Integer.valueOf(minYear.get())); //$NON-NLS-1$
		modelAndView.addObject("publicationsPerMemberMaxYear", Integer.valueOf(maxYear.get())); //$NON-NLS-1$
		//
		return modelAndView;
	}

	/** Replies the model-view component for displaying the dashboard of the projects of a specific research organization.
	 *
	 * @param organization the identifier or the name of the organization.
	 * @param startYear the first year of the publications. If it is provided, the {@code age} is ignored.
	 * @param endYear the last year of the publications. If it is provided, the {@code age} is ignored.
	 * @param age the age of the publications. If it is not provided, the default age is {@code 6}.
	 * @param username the name of the logged-in user.
	 * @return the model-view component.
	 */
	@GetMapping("/projectDashboard")
	public ModelAndView projectDashboard(
			@RequestParam(required = true) String organization,
			@RequestParam(required = false) Integer startYear,
			@RequestParam(required = false) Integer endYear,
			@RequestParam(required = false, defaultValue = "6") int age,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		readCredentials(username, "projectDashboard", organization); //$NON-NLS-1$
		//
		final ResearchOrganization organizationObj = getOrganizationWith(organization, this.organizationService);
		if (organizationObj == null) {
			throw new IllegalArgumentException("Organization not found for: " + organization); //$NON-NLS-1$
		}
		//
		final List<Project> projectSet = this.projectService.getProjectsByOrganizationId(organizationObj.getId());
		final List<Project> projects;
		if (startYear != null && endYear != null) {
			final LocalDate sy;
			final LocalDate ey;
			if (startYear.intValue() > endYear.intValue()) {
				sy = LocalDate.of(endYear.intValue(), 1, 1);
				ey = LocalDate.of(startYear.intValue(), 12, 31);
			} else {
				sy = LocalDate.of(startYear.intValue(), 1, 1);
				ey = LocalDate.of(endYear.intValue(), 12, 31);
			}
			projects = projectSet.stream()
					.filter(it -> it.isActiveIn(sy, ey))
					.collect(Collectors.toList());				
		} else if (startYear != null) {
			projects = projectSet.stream()
					.filter(it -> it.getEndYear() >= startYear.intValue())
					.collect(Collectors.toList());				
		} else if (endYear != null) {
			projects = projectSet.stream()
					.filter(it -> it.getStartYear() <= endYear.intValue())
					.collect(Collectors.toList());				
		} else {
			final int referenceYear = LocalDate.now().getYear();
			final int firstYear = referenceYear - age;
			projects = projectSet.stream()
					.filter(it -> it.getEndYear() >= firstYear && it.getStartYear() <= referenceYear)
					.collect(Collectors.toList());
		}
		//
		final ModelAndView modelAndView = new ModelAndView("projectDashboard"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, false);
		modelAndView.addObject("projectsPerYear", this.projectService.getNumberOfStartingProjectsPerYear(projects)); //$NON-NLS-1$
		modelAndView.addObject("ongoingProjectsPerYear", this.projectService.getNumberOfOngoingProjectsPerYear(projects)); //$NON-NLS-1$
		modelAndView.addObject("projectsPerType", this.projectService.getNumberOfProjectsPerType(projects)); //$NON-NLS-1$
		modelAndView.addObject("academicProjectsPerType", this.projectService.getNumberOfAcademicProjectsPerType(projects)); //$NON-NLS-1$
		modelAndView.addObject("notAcademicProjectsPerType", this.projectService.getNumberOfNotAcademicProjectsPerType(projects)); //$NON-NLS-1$
		modelAndView.addObject("projectsPerActivityType", this.projectService.getNumberOfProjectsPerActivityType(projects)); //$NON-NLS-1$
		modelAndView.addObject("projectsPerTRL", this.projectService.getNumberOfProjectsPerTRL(projects)); //$NON-NLS-1$
		modelAndView.addObject("projectsPerResearchAxis", this.projectService.getNumberOfProjectsPerScientificAxis(projects)); //$NON-NLS-1$
		modelAndView.addObject("projectsPerCountry", this.projectService.getNumberOfProjectsPerCountry(projects, organizationObj)); //$NON-NLS-1$
		//
		return modelAndView;
	}

	/** Replies the model-view component for displaying the dashboard of the memberships of a specific research organization.
	 *
	 * @param organization the identifier or the name of the organization.
	 * @param startYear the first year of the publications. If it is provided, the {@code age} is ignored.
	 * @param endYear the last year of the publications. If it is provided, the {@code age} is ignored.
	 * @param age the age of the publications. If it is not provided, the default age is {@code 6}.
	 * @param username the name of the logged-in user.
	 * @return the model-view component.
	 */
	@GetMapping("/membershipDashboard")
	public ModelAndView membershipDashboard(
			@RequestParam(required = true) String organization,
			@RequestParam(required = false) Integer startYear,
			@RequestParam(required = false) Integer endYear,
			@RequestParam(required = false, defaultValue = "6") int age,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		readCredentials(username, "membershipDashboard", organization); //$NON-NLS-1$
		//
		final ResearchOrganization organizationObj = getOrganizationWith(organization, this.organizationService);
		if (organizationObj == null) {
			throw new IllegalArgumentException("Organization not found for: " + organization); //$NON-NLS-1$
		}
		//
		final Set<Membership> membershipSet = organizationObj.getMemberships();
		final List<Membership> memberships;
		final int minYear;
		final int maxYear;
		if (startYear != null && endYear != null) {
			final LocalDate sy;
			final LocalDate ey;
			if (startYear.intValue() > endYear.intValue()) {
				sy = LocalDate.of(endYear.intValue(), 1, 1);
				ey = LocalDate.of(startYear.intValue(), 12, 31);
			} else {
				sy = LocalDate.of(startYear.intValue(), 1, 1);
				ey = LocalDate.of(endYear.intValue(), 12, 31);
			}
			memberships = membershipSet.stream()
					.filter(it -> it.isActiveIn(sy, ey))
					.collect(Collectors.toList());
			minYear = sy.getYear();
			maxYear = ey.getYear();
		} else if (startYear != null) {
			memberships = membershipSet.stream()
					.filter(it -> it.getMemberToWhen() == null || it.getMemberToWhen().getYear() >= startYear.intValue())
					.collect(Collectors.toList());
			minYear = startYear.intValue();
			maxYear = LocalDate.now().getYear();
		} else if (endYear != null) {
			memberships = membershipSet.stream()
					.filter(it -> it.getMemberSinceWhen() == null || it.getMemberSinceWhen().getYear() <= endYear.intValue())
					.collect(Collectors.toList());				
			minYear = endYear.intValue() - age;
			maxYear = endYear.intValue();
		} else {
			maxYear = LocalDate.now().getYear();
			minYear = maxYear - age;
			memberships = membershipSet.stream()
					.filter(it -> (it.getMemberToWhen() == null || it.getMemberToWhen().getYear() >= minYear)
							&& (it.getMemberSinceWhen() == null || it.getMemberSinceWhen().getYear() <= maxYear))
					.collect(Collectors.toList());
		}
		//
		final ModelAndView modelAndView = new ModelAndView("membershipDashboard"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, false);
		modelAndView.addObject("membersPerYear", this.membershipService.getNumberOfMembersPerYear(memberships, minYear, maxYear)); //$NON-NLS-1$
		//
		final List<OrganizationAddress> addresses = organizationObj.getAddresses().stream().collect(Collectors.toList());
		modelAndView.addObject("membersPerAddress", this.membershipService.getNumberOfMembersPerAddress(memberships, //$NON-NLS-1$ 
				minYear, maxYear, organizationObj, addresses));
		modelAndView.addObject("addresses", addresses.stream().map(it -> it.getName()).collect(Collectors.toList())); //$NON-NLS-1$
		//
		final List<ScientificAxis> axes = new ArrayList<>();
		modelAndView.addObject("membersPerScientificAxis", this.membershipService.getNumberOfMembersPerScientificAxis(memberships, //$NON-NLS-1$ 
				minYear, maxYear, organizationObj, axes));
		modelAndView.addObject("scientificAxes", axes.stream() //$NON-NLS-1$
				.map(it -> it.getAcronym() + " - " + it.getName()) //$NON-NLS-1$$
				.collect(Collectors.toList()));
		//
		return modelAndView;
	}

}
