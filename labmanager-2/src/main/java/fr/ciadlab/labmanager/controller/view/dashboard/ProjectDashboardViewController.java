/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.ciadlab.labmanager.controller.view.dashboard;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.service.conference.ConferenceService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.service.project.ProjectService;
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
public class ProjectDashboardViewController extends AbstractViewController {

	private ResearchOrganizationService organizationService;

	private ProjectService projectService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param organizationService the service for accessing to the research organizations.
	 * @param projectService the service for accessing to the projects.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public ProjectDashboardViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ResearchOrganizationService organizationService,
			@Autowired ProjectService projectService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.organizationService = organizationService;
		this.projectService = projectService;
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

}
