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

package fr.ciadlab.labmanager.controller.view.assostructure;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.assostructure.AssociatedStructure;
import fr.ciadlab.labmanager.entities.assostructure.AssociatedStructureType;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.service.assostructure.AssociatedStructureService;
import fr.ciadlab.labmanager.service.member.PersonService;
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

/** REST Controller for research associated structure views.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.2
 */
@RestController
@CrossOrigin
public class AssociatedStructureViewController extends AbstractViewController {

	private AssociatedStructureService structureService;

	private ResearchOrganizationService organizationService;

	private PersonService personService;

	private ProjectService projectService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param structureService the service for accessing the associated structures.
	 * @param organizationService the service for accessing the organizations.
	 * @param personService the service for accessing the persons.
	 * @param projectService the service for accessing the projects.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public AssociatedStructureViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired AssociatedStructureService structureService,
			@Autowired ResearchOrganizationService organizationService,
			@Autowired PersonService personService,
			@Autowired ProjectService projectService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.structureService = structureService;
		this.organizationService = organizationService;
		this.personService = personService;
		this.projectService = projectService;
	}

	/** Replies the model-view component for managing the associated structures.
	 *
	 * @param username the name of the logged-in user.
	 * @param dbId the database identifier of the person for who the associated structures must be exported.
	 * @param webId the webpage identifier of the person for who the associated structures must be exported.
	 * @param organization the identifier of the organization for which the associated structures must be exported.
	 * @return the model-view component.
	 */
	@GetMapping("/" + Constants.ASSOCIATED_STRUCTURE_LIST_ENDPOINT)
	public ModelAndView associatedStructureList(
			@RequestParam(required = false, name = Constants.DBID_ENDPOINT_PARAMETER) Integer dbId,
			@RequestParam(required = false, name = Constants.WEBID_ENDPOINT_PARAMETER) String webId,
			@RequestParam(required = false, name = Constants.ORGANIZATION_ENDPOINT_PARAMETER) Integer organization,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		final String inWebId = inString(webId);
		readCredentials(username, Constants.ASSOCIATED_STRUCTURE_LIST_ENDPOINT, dbId, inWebId, organization);
		final ModelAndView modelAndView = new ModelAndView(Constants.ASSOCIATED_STRUCTURE_LIST_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView, false);
		initAdminTableButtons(modelAndView, endpoint(Constants.ASSOCIATED_STRUCTURE_EDITING_ENDPOINT, Constants.STRUCTURE_ENDPOINT_PARAMETER));
		final List<AssociatedStructure> structures = extractAssociatedStructureListWithoutFilter(dbId, inWebId, organization);
		modelAndView.addObject("structures", structures); //$NON-NLS-1$
		return modelAndView;
	}

	private List<AssociatedStructure> extractAssociatedStructureListWithoutFilter(Integer dbId, String webId, Integer organization) {
		final List<AssociatedStructure> structures;
		if (organization != null && organization.intValue() != 0) {
			structures = this.structureService.getAssociatedStructuresByOrganizationId(organization.intValue());
		} else if (dbId != null && dbId.intValue() != 0) {
			structures = this.structureService.getAssociatedStructuresByPersonId(dbId.intValue());
		} else if (!Strings.isNullOrEmpty(webId)) {
			final Person person = this.personService.getPersonByWebPageId(webId);
			if (person == null) {
				throw new IllegalArgumentException("Person not found with web identifier: " + webId); //$NON-NLS-1$
			}
			structures = this.structureService.getAssociatedStructuresByPersonId(person.getId());
		} else {
			structures = this.structureService.getAllAssociatedStructures();
		}
		return structures;
	}

	/** Show the editor for an associated structure. This editor permits to create or to edit an associated structure.
	 *
	 * @param structure the identifier of the associated structure to edit. If it is {@code null}, the endpoint
	 *     is dedicated to the creation of an associated structure.
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 * @throws IOException if there is some internal IO error when building the form's data.
	 */
	@GetMapping(value = "/" + Constants.ASSOCIATED_STRUCTURE_EDITING_ENDPOINT)
	public ModelAndView associatedStructureEditor(
			@RequestParam(required = false, name = Constants.STRUCTURE_ENDPOINT_PARAMETER) Integer structure,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws IOException {
		ensureCredentials(username, Constants.ASSOCIATED_STRUCTURE_EDITING_ENDPOINT, structure);
		final ModelAndView modelAndView = new ModelAndView(Constants.ASSOCIATED_STRUCTURE_EDITING_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView, false);
		//
		final AssociatedStructure structureObj;
		if (structure != null && structure.intValue() != 0) {
			structureObj = this.structureService.getAssociatedStructureById(structure.intValue());
			if (structureObj == null) {
				throw new IllegalArgumentException("Associated structure not found: " + structure); //$NON-NLS-1$
			}

			// Provide a YEAR-MONTH start date
			if (structureObj.getCreationDate() != null) {
				final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd"); //$NON-NLS-1$
				modelAndView.addObject("formattedCreationDate", structureObj.getCreationDate().format(formatter)); //$NON-NLS-1$
			}
		} else {
			structureObj = null;
		}
		//
		List<AssociatedStructureType> sortedTypes = Arrays.asList(AssociatedStructureType.values());
		modelAndView.addObject("sortedTypes", sortedTypes); //$NON-NLS-1$
		//
		final List<ResearchOrganization> organizations = this.organizationService.getAllResearchOrganizations().stream()
				.sorted((a, b) -> a.getAcronymOrName().compareToIgnoreCase(b.getAcronymOrName()))
				.collect(Collectors.toList());
		modelAndView.addObject("organizations", organizations); //$NON-NLS-1$
		//
		final List<Project> projects = this.projectService.getAllProjects().stream()
				.sorted((a, b) -> a.getAcronymOrScientificTitle().compareToIgnoreCase(b.getAcronymOrScientificTitle()))
				.collect(Collectors.toList());
		modelAndView.addObject("projects", projects); //$NON-NLS-1$
		//
		final List<Person> persons = this.personService.getAllPersons().stream()
				.filter(it -> !it.getMemberships().isEmpty())
				.sorted((a, b) -> a.getFullNameWithLastNameFirst().compareToIgnoreCase(b.getFullNameWithLastNameFirst()))
				.collect(Collectors.toList());
		modelAndView.addObject("persons", persons); //$NON-NLS-1$
		//
		modelAndView.addObject("structure", structureObj); //$NON-NLS-1$
		modelAndView.addObject("formActionUrl", rooted(Constants.ASSOCIATED_STRUCTURE_SAVING_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("formRedirectUrl", rooted(Constants.ASSOCIATED_STRUCTURE_LIST_ENDPOINT)); //$NON-NLS-1$
		//
		return modelAndView;
	}

}
