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

package fr.ciadlab.labmanager.controller.api.assostructure;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.assostructure.AssociatedStructure;
import fr.ciadlab.labmanager.entities.assostructure.AssociatedStructureType;
import fr.ciadlab.labmanager.entities.assostructure.HolderRole;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.service.assostructure.AssociatedStructureService;
import fr.ciadlab.labmanager.service.assostructure.AssociatedStructureService.HolderDescription;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.service.project.ProjectService;
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

/** REST Controller for associated structure API.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.2
 */
@RestController
@CrossOrigin
public class AssociatedStructureApiController extends AbstractApiController {

	private AssociatedStructureService structureService;

	private PersonService personService;

	private ResearchOrganizationService organizationService;

	private ProjectService projectService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param structureService the service for managing the associated structures.
	 * @param personService the service for managing the persons.
	 * @param organizationService the service for managing the research organizations.
	 * @param projectService the service for managing the projects.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public AssociatedStructureApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired AssociatedStructureService structureService,
			@Autowired PersonService personService,
			@Autowired ResearchOrganizationService organizationService,
			@Autowired ProjectService projectService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.structureService = structureService;
		this.personService = personService;
		this.organizationService = organizationService;
		this.projectService = projectService;
	}

	/** Saving information of an associated structure. 
	 *
	 * @param structure the identifier of the associated structure. If the identifier is not provided, this endpoint is supposed to create
	 *     an associated structure in the database.
	 * @param acronym the short name of acronym of the associated structure.
	 * @param name the name of the associated structure.
	 * @param type the name of the type of associated structure.
	 * @param creationDate the creation date of the associated structure in format {@code YYY-MM-DD}.
	 * @param creationDuration the duration of the creation of the associated structure in months.
	 * @param fundingOrganization the identifier of the research organization which is funding the associated structure.
	 * @param holders Json associative array that maps the identifiers of the holding persons to their roles in the associated structure.
	 * @param description the public description of the associated structure (markdown syntax is accepted).
	 * @param budget the budget for creating the associated structure.
	 * @param projects the list of the identifiers of the projects that are related to the creation of the associated structure.
	 * @param confidential indicates if the structure should be confidential or not.
	 * @param validated indicates if the structure is validated by a local authority.
	 * @param username the name of the logged-in user.
	 * @throws Exception if the structure cannot be saved.
	 */
	@PutMapping(value = "/" + Constants.ASSOCIATED_STRUCTURE_SAVING_ENDPOINT)
	public void saveAssociatedStructure(
			@RequestParam(required = false) Integer structure,
			@RequestParam(required = true) String acronym,
			@RequestParam(required = true) String name,
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String creationDate,
			@RequestParam(required = false, defaultValue = "0") int creationDuration,
			@RequestParam(required = true) int fundingOrganization,
			@RequestParam(required = false) String holders,
			@RequestParam(required = false) String description,
			@RequestParam(required = false, defaultValue = "0") float budget,
			@RequestParam(required = false) List<Integer> projects,
			@RequestParam(required = false, defaultValue = "false") boolean confidential,
			@RequestParam(required = false, defaultValue = "false") boolean validated,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.ASSOCIATED_STRUCTURE_SAVING_ENDPOINT, structure);

		// Analyze parameters
		final String inAcronym = inString(acronym);
		if (Strings.isNullOrEmpty(inAcronym)) {
			throw new IllegalArgumentException("Acronym is missed"); //$NON-NLS-1$
		}
		final String inName = inString(name);
		if (Strings.isNullOrEmpty(inName)) {
			throw new IllegalArgumentException("Name is missed"); //$NON-NLS-1$
		}
		final LocalDate inCreationDate = LocalDate.parse(inString(creationDate));
		final String inDescription = inString(description);
		final AssociatedStructureType inType = AssociatedStructureType.valueOfCaseInsensitive(inString(type));

		final String inHolders = inString(holders);
		final Map<Integer, HolderDescription> holderList = new HashMap<>();
		if (!Strings.isNullOrEmpty(inHolders)) {
			final ObjectMapper jsonMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			final List<Map<String,String>> input = jsonMapper.readValue(inHolders, List.class);
			if (input == null) {
				throw new IllegalArgumentException("Invalid value for holder argument: " + holders); //$NON-NLS-1$
			}
			input.stream().forEach(it -> {
				final String person = it.get("person"); //$NON-NLS-1$
				Integer personId = null;
				try {
					personId = Integer.valueOf(person);
				} catch (Throwable ex) {
					personId = null;
				}
				if (personId == null) {
					throw new IllegalArgumentException("Invalid person id in holders argument: " + person); //$NON-NLS-1$
				}
				final Person personObj = this.personService.getPersonById(personId.intValue());
				if (personObj == null) {
					throw new IllegalArgumentException("Invalid person id in holders argument: " + person); //$NON-NLS-1$
				}
				//
				final String role = it.get("role"); //$NON-NLS-1$
				final HolderRole roleInstance = HolderRole.valueOfCaseInsensitive(role);
				//
				final String roleDescription = inString(it.get("roleDescription")); //$NON-NLS-1$
				//
				final String organization = it.get("organization"); //$NON-NLS-1$
				Integer organizationId = null;
				try {
					organizationId = Integer.valueOf(organization);
				} catch (Throwable ex) {
					organizationId = null;
				}
				if (organizationId == null) {
					throw new IllegalArgumentException("Invalid organization id in holders argument: " + organization); //$NON-NLS-1$
				}
				final Optional<ResearchOrganization> organizationObj = this.organizationService.getResearchOrganizationById(organizationId.intValue());
				if (organizationObj.isEmpty()) {
					throw new IllegalArgumentException("Invalid organization id in holders argument: " + organization); //$NON-NLS-1$
				}
				//
				final String superOrganization = it.get("superOrganization"); //$NON-NLS-1$
				Integer superOrganizationId = null;
				try {
					superOrganizationId = Integer.valueOf(superOrganization);
				} catch (Throwable ex) {
					superOrganizationId = null;
				}
				ResearchOrganization superOrganizationObj = null;
				if (superOrganizationId != null) {
					final Optional<ResearchOrganization> superOrganizationOpt = this.organizationService.getResearchOrganizationById(superOrganizationId.intValue());
					if (superOrganizationOpt.isPresent()) {
						superOrganizationObj = superOrganizationOpt.get();
					}
				}
				//
				holderList.put(personId, new HolderDescription(personObj, roleInstance, roleDescription, organizationObj.get(), superOrganizationObj));
			});
		}

		List<Project> projectObj = new ArrayList<>();
		if (projects != null && !projects.isEmpty()) {
			projectObj = projects.stream().map(
					it -> {
						final Project prj = this.projectService.getProjectById(it.intValue());
						if (prj == null) {
							throw new IllegalArgumentException("Invalid project id: " + it.intValue()); //$NON-NLS-1$
						}
						return prj;
					}).collect(Collectors.toList());
		} else {
			projectObj = Collections.emptyList();
		}

		// Create or update the structure
		Optional<AssociatedStructure> structureOpt = Optional.empty();
		if (structure == null) {
			structureOpt = this.structureService.createAssosiatedStructure(
					validated, inAcronym, inName, inType, inCreationDate, creationDuration,
					fundingOrganization, holderList, inDescription, budget, projectObj, confidential);
		} else {
			structureOpt = this.structureService.updateAssociatedStructure(structure.intValue(),
					validated, inAcronym, inName, inType, inCreationDate, creationDuration,
					fundingOrganization, holderList, inDescription, budget, projectObj, confidential);
		}
		if (structureOpt.isEmpty()) {
			throw new IllegalStateException("Associated structure not found"); //$NON-NLS-1$
		}
	}

	/** Delete an associated structure from the database.
	 *
	 * @param structure the identifier of the associated structure.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@DeleteMapping("/deleteAssoStruct")
	public void deleteAssociatedStructure(
			@RequestParam Integer structure,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, "deleteAssoStruct", structure); //$NON-NLS-1$
		if (structure == null || structure.intValue() == 0) {
			throw new IllegalStateException("Associated structure not found"); //$NON-NLS-1$
		}
		this.structureService.removeAssociatedStructure(structure.intValue());
	}

}
