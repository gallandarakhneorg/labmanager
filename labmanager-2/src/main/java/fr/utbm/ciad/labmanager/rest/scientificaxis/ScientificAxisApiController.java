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

package fr.utbm.ciad.labmanager.rest.scientificaxis;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.project.Project;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.data.scientificaxis.ScientificAxis;
import fr.utbm.ciad.labmanager.rest.AbstractApiController;
import fr.utbm.ciad.labmanager.services.member.MembershipService;
import fr.utbm.ciad.labmanager.services.project.ProjectService;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.services.scientificaxis.ScientificAxisService;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST Controller for scientific axis API.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.5
 */
@RestController
@CrossOrigin
public class ScientificAxisApiController extends AbstractApiController {

	private ScientificAxisService scientificAxisService;

	private ProjectService projectService;

	private PublicationService publicationService;

	private MembershipService membershipService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param scientificAxisService the service for managing the scientific axes.
	 * @param projectService the service for managing the projects.
	 * @param publicationService the service for managing the publications.
	 * @param membershipService the service for managing the memberships.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public ScientificAxisApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ScientificAxisService scientificAxisService,
			@Autowired ProjectService projectService,
			@Autowired PublicationService publicationService,
			@Autowired MembershipService membershipService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.scientificAxisService = scientificAxisService;
		this.projectService = projectService;
		this.publicationService = publicationService;
		this.membershipService = membershipService;
	}

	/** Saving information of a scientific axis. 
	 *
	 * @param axis the identifier of the scientific axis. If the identifier is not provided, this endpoint is supposed to create
	 *     a scientific axis in the database.
	 * @param acronym the short name of acronym of the scientific axis.
	 * @param name the name of the scientific axis.
	 * @param startDate the start date of the scientific axis in format {@code YYY-MM-DD}.
	 * @param endDate the end date of the scientific axis in format {@code YYY-MM-DD}, or {@code null}.
	 * @param projects the list of the identifiers of the projects associated to the scientific axis.
	 * @param publications the list of the identifiers of the publications associated to the scientific axis.
	 * @param memberships the list of the identifiers of the memberships associated to the scientific axis.
	 * @param validated indicates if the project is validated by a local authority.
	 * @param username the name of the logged-in user.
	 * @throws Exception if the axis cannot be saved.
	 */
	@PutMapping(value = "/" + Constants.SCIENTIFIC_AXIS_SAVING_ENDPOINT)
	public void saveScientificAxis(
			@RequestParam(required = false) Integer axis,
			@RequestParam(required = true) String acronym,
			@RequestParam(required = true) String name,
			@RequestParam(required = true) String startDate,
			@RequestParam(required = false) String endDate,
			@RequestParam(required = false) List<Integer> projects,
			@RequestParam(required = false) List<Integer> publications,
			@RequestParam(required = false) List<Integer> memberships,
			@RequestParam(required = false, defaultValue = "false") boolean validated,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		//TODO ensureCredentials(username, Constants.SCIENTIFIC_AXIS_SAVING_ENDPOINT, axis);

		// Analyze parameters
		final String inAcronym = inString(acronym);
		if (Strings.isNullOrEmpty(inAcronym)) {
			throw new IllegalArgumentException("Acronym is missed"); //$NON-NLS-1$
		}
		final String inName = inString(name);
		if (Strings.isNullOrEmpty(inName)) {
			throw new IllegalArgumentException("Name is missed"); //$NON-NLS-1$
		}
		final LocalDate inStartDate = LocalDate.parse(inString(startDate));
		final String inEndDateStr = inString(endDate);
		final LocalDate inEndDate = Strings.isNullOrEmpty(inEndDateStr) ? null : LocalDate.parse(inEndDateStr);
		
		final List<Project> projectObjs;
		if (projects != null) {
			projectObjs = this.projectService.getProjectsByIds(projects);
		} else {
			projectObjs = Collections.emptyList();
		}

		final List<Publication> publicationObjs;
		if (publications != null) {
			publicationObjs = this.publicationService.getPublicationsByIds(publications);
		} else {
			publicationObjs = Collections.emptyList();
		}

		final List<Membership> membershipObjs;
		if (memberships != null) {
			membershipObjs = this.membershipService.getMembershipsByIds(memberships);
		} else {
			membershipObjs = Collections.emptyList();
		}

		// Create or update the structure
		Optional<ScientificAxis> axisOpt = Optional.empty();
		if (axis == null) {
			axisOpt = this.scientificAxisService.createScientificAxis(
					validated, inAcronym, inName, inStartDate, inEndDate,
					projectObjs, publicationObjs, membershipObjs);
		} else {
			axisOpt = this.scientificAxisService.updateScientificAxis(axis.intValue(),
					validated, inAcronym, inName, inStartDate, inEndDate,
					projectObjs, publicationObjs, membershipObjs);
		}
		if (axisOpt.isEmpty()) {
			throw new IllegalStateException("Scientific axis not found"); //$NON-NLS-1$
		}
	}

	/** Delete a scientific axis from the database.
	 *
	 * @param axis the identifier of the scientific axis.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@DeleteMapping("/" + Constants.SCIENTIFIC_AXIS_DELETING_ENDPOINT)
	public void deleteScientificAxis(
			@RequestParam(required = false, name = Constants.AXIS_ENDPOINT_PARAMETER) Integer axis,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		//TODO ensureCredentials(username, Constants.SCIENTIFIC_AXIS_DELETING_ENDPOINT, axis);
		if (axis == null || axis.intValue() == 0) {
			throw new IllegalStateException("Scientific axis not found"); //$NON-NLS-1$
		}
		this.scientificAxisService.removeScientificAxis(axis.intValue());
	}

}
