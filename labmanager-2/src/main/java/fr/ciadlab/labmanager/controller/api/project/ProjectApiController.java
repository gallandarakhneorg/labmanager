/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
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

package fr.ciadlab.labmanager.controller.api.project;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.entities.project.ProjectActivityType;
import fr.ciadlab.labmanager.entities.project.ProjectBudget;
import fr.ciadlab.labmanager.entities.project.ProjectContractType;
import fr.ciadlab.labmanager.entities.project.ProjectStatus;
import fr.ciadlab.labmanager.entities.project.ProjectWebPageNaming;
import fr.ciadlab.labmanager.entities.project.Role;
import fr.ciadlab.labmanager.entities.scientificaxis.ScientificAxis;
import fr.ciadlab.labmanager.io.json.JsonUtils;
import fr.ciadlab.labmanager.service.project.ProjectService;
import fr.ciadlab.labmanager.service.scientificaxis.ScientificAxisService;
import fr.ciadlab.labmanager.utils.funding.FundingScheme;
import fr.ciadlab.labmanager.utils.trl.TRL;
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
import org.springframework.web.multipart.MultipartFile;

/** REST Controller for research project API.
 * 
 * @author $Author: sgalland$
 * @author $Author: bpdj$
 * @author $Author: anoubli$
 * @author $Author: pgoubet$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.0
 */
@RestController
@CrossOrigin
public class ProjectApiController extends AbstractApiController {

	private ProjectService projectService;

	private ScientificAxisService scientificAxisService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param projectService the service for managing the research projects.
	 * @param scientificAxisService the service for managing the scientific axes.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public ProjectApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ProjectService projectService,
			@Autowired ScientificAxisService scientificAxisService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.projectService = projectService;
		this.scientificAxisService = scientificAxisService;
	}

	/** Saving information of a project. 
	 *
	 * @param project the identifier of the project. If the identifier is not provided, this endpoint is supposed to create
	 *     a project in the database.
	 * @param acronym the short name of acronym of the project
	 * @param scientificTitle the title of the project with a strong highlight on the scientific contribution.
	 * @param openSource indicates if the project is open source or not.
	 * @param startDate the start date of the project in format {@code YYY-MM-DD}.
	 * @param duration the duration of the project in months.
	 * @param description the public description of the project (markdown syntax is accepted).
	 * @param pathToLogo the local path to the logo of the project.
	 * @param removePathToLogo remove the logo before uploading.
	 * @param projectURL the URL of the project.
	 * @param webPageNaming the naming convention for the project page on the institution website.
	 * @param globalBudget the budget for all the partners in the project.
	 * @param contractType the name of the type of contract.
	 * @param activityType the name of the type of project activity.
	 * @param trl the name of the TRL.
	 * @param coordinator the identifier of the research organization which is coordinating the project.
	 * @param localOrganization the identifier of the local organization involved in the project.
	 * @param superOrganization the identifier of the super research organization of the local organization, if needed.
	 * @param learOrganization the identifier of the research organization which is the LEAR.
	 * @param otherPartners the list of the orther partners in the project.
	 * @param participants Json associative array that maps the identifiers of the participants from
	 *     the local organization to their roles in the project.
	 * @param pathToScientificRequirements the local path to a document that contains the scientific requirements of the project.
	 * @param removePathToScientificRequirements remove the scientific requirements before uploading.
	 * @param confidential indicates if the project should be confidential or not.
	 * @param pathsToImages the local paths to the associated images.
	 * @param removePathsToImages remove the associated images before uploading.
	 * @param videoURLs list of URLs of videos that are related to the project
	 * @param pathToPowerpoint the local path to a PowerPoint presentation of the project.
	 * @param removePathToPowerpoint remove the PowerPoint before uploading.
	 * @param pathToPressDocument the local path to a press document related to the project.
	 * @param removePathToPressDocument remove the press document before uploading.
	 * @param status the name of the project status.
	 * @param localOrganizationBudgets the list of the descriptions of the budgets and funding schemes for the local organization.
	 *     It is a JSON string that contains a array of associative arrays (one for each budget definition). Each associative array
	 *     contains the fields {@code fundingScheme} with the name of the {@link FundingScheme}, {@code budget} with the
	 *     budget in keuros associated to the funding scheme, and {@code grant} the grant or contract number.
	 * @param scientificAxes the list of scientific axes that are associated to the project. 
	 * @param validated indicates if the project is validated by a local authority.
	 * @param username the name of the logged-in user.
	 * @throws Exception if the publication cannot be saved.
	 */
	@PutMapping(value = "/" + Constants.PROJECT_SAVING_ENDPOINT)
	public void saveProject(
			@RequestParam(required = false) Integer project,
			@RequestParam(required = true) String acronym,
			@RequestParam(required = true) String scientificTitle,
			@RequestParam(required = false, defaultValue = "false") boolean openSource,
			@RequestParam(required = true) String startDate,
			@RequestParam(required = true) int duration,
			@RequestParam(required = false) String description,
			@RequestParam(required = false) MultipartFile pathToLogo,
			@RequestParam(required = false, defaultValue = "false", name = "@fileUpload_removed_pathToLogo") boolean removePathToLogo,
			@RequestParam(required = false) String projectURL,
			@RequestParam(required = false) String webPageNaming,
			@RequestParam(required = true) float globalBudget,
			@RequestParam(required = true) String contractType,
			@RequestParam(required = true) String activityType,
			@RequestParam(required = true) String trl,
			@RequestParam(required = true) int coordinator,
			@RequestParam(required = true) int localOrganization,
			@RequestParam(required = false, defaultValue = "0") int superOrganization,
			@RequestParam(required = true) int learOrganization,
			@RequestParam(required = false) List<Integer> otherPartners,
			@RequestParam(required = false) String participants,
			@RequestParam(required = false) MultipartFile pathToScientificRequirements,
			@RequestParam(required = false, defaultValue = "false", name = "@fileUpload_removed_pathToScientificRequirements") boolean removePathToScientificRequirements,
			@RequestParam(required = false, defaultValue = "false") boolean confidential,
			@RequestParam(required = false) MultipartFile[] pathsToImages,
			@RequestParam(required = false, defaultValue = "false", name = "@fileUpload_removed_pathsToImages") boolean removePathsToImages,
			@RequestParam(required = false) List<String> videoURLs,
			@RequestParam(required = false) MultipartFile pathToPowerpoint,
			@RequestParam(required = false, defaultValue = "false", name = "@fileUpload_removed_pathToPowerpoint") boolean removePathToPowerpoint,
			@RequestParam(required = false) MultipartFile pathToPressDocument,
			@RequestParam(required = false, defaultValue = "false", name = "@fileUpload_removed_pathToPressDocument") boolean removePathToPressDocument,
			@RequestParam(required = true) String status,
			@RequestParam(required = true) String localOrganizationBudgets,
			@RequestParam(required = false) List<Integer> scientificAxes,
			@RequestParam(required = false, defaultValue = "false") boolean validated,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.PROJECT_SAVING_ENDPOINT, project);

		// Analyze parameters
		final String inAcronym = inString(acronym);
		if (Strings.isNullOrEmpty(inAcronym)) {
			throw new IllegalArgumentException("Acronym is missed"); //$NON-NLS-1$
		}
		final String inScientificTitle = inString(scientificTitle);
		if (Strings.isNullOrEmpty(inScientificTitle)) {
			throw new IllegalArgumentException("Scientific title is missed"); //$NON-NLS-1$
		}
		final LocalDate inStartDate = LocalDate.parse(inString(startDate));
		final String inDescription = inString(description);
		final String inProjectURLStr = inString(projectURL);
		final URL inProjectURLObj;
		if (!Strings.isNullOrEmpty(inProjectURLStr)) {
			inProjectURLObj = new URL(inProjectURLStr);
		} else {
			inProjectURLObj = null;
		}
		final String inWebPageNaming = inString(webPageNaming);
		final ProjectWebPageNaming projectWebPageNaming = ProjectWebPageNaming.valueOfCaseInsensitive(inWebPageNaming);
		final ProjectContractType inContactType = ProjectContractType.valueOfCaseInsensitive(inString(contractType));
		final ProjectActivityType inActivityType = ProjectActivityType.valueOfCaseInsensitive(inString(activityType));
		final TRL inTrl = TRL.valueOfCaseInsensitive(inString(trl));
		if (trl == null) {
			throw new IllegalArgumentException("TRL is missed"); //$NON-NLS-1$
		}
		final ProjectStatus inStatus = ProjectStatus.valueOfCaseInsensitive(inString(status));
		final String inParticipants = inString(participants);
		final Map<Integer, Role> participantMap = new HashMap<>();
		if (!Strings.isNullOrEmpty(inParticipants)) {
			final ObjectMapper jsonMapper = JsonUtils.createMapper();
			@SuppressWarnings("unchecked")
			final List<Map<String,String>> input = jsonMapper.readValue(inParticipants, List.class);
			if (input == null) {
				throw new IllegalArgumentException("Invalid value for participants argument: " + participants); //$NON-NLS-1$
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
					throw new IllegalArgumentException("Invalid person id in participants argument: " + person); //$NON-NLS-1$
				}
				final String role = it.get("role"); //$NON-NLS-1$
				final Role roleInstance = Role.valueOfCaseInsensitive(role);
				participantMap.put(personId, roleInstance);
			});
		}
		final List<ProjectBudget> budgets = new ArrayList<>();
		final String inLocalOrganizationBudgets = inString(localOrganizationBudgets);
		if (!Strings.isNullOrEmpty(inLocalOrganizationBudgets)) {
			final ObjectMapper jsonMapper = JsonUtils.createMapper();
			@SuppressWarnings("unchecked")
			final List<Map<String,String>> input = jsonMapper.readValue(inLocalOrganizationBudgets, List.class);
			if (input != null) {
				for (final Map<String, String> budget : input) {
					final FundingScheme funding = FundingScheme.valueOfCaseInsensitive(inString(budget.get("fundingScheme"))); //$NON-NLS-1$
					if (funding == null) {
						throw new IllegalArgumentException("Funding scheme of a budget for local organization is mandatory"); //$NON-NLS-1$
					}
					final Float budgetValue = inFloat(budget.get("budget")); //$NON-NLS-1$
					if (budgetValue == null || budgetValue.floatValue() < 0f) {
						throw new IllegalArgumentException("Budget value for local organization is mandatory"); //$NON-NLS-1$
					}
					final ProjectBudget budgetObject = new ProjectBudget();
					budgetObject.setFundingScheme(funding);
					budgetObject.setBudget(budgetValue.floatValue());
					budgetObject.setGrant(inString(budget.get("grant"))); //$NON-NLS-1$
					budgets.add(budgetObject);
				}
			}
		} else {
			throw new IllegalArgumentException("Budget for local organization is mandatory"); //$NON-NLS-1$
		}
	
		final List<ScientificAxis> axes;
		if (scientificAxes != null) {
			axes = this.scientificAxisService.getScientificAxesFor(scientificAxes);
		} else {
			axes = Collections.emptyList();
		}

		// Create or update the project
		Optional<Project> projectOpt = Optional.empty();
		if (project == null) {
			projectOpt = this.projectService.createProject(
					validated, inAcronym, inScientificTitle, openSource, inStartDate, duration, inDescription,
					pathToLogo, removePathToLogo, inProjectURLObj, projectWebPageNaming, globalBudget,
					inContactType, inActivityType, inTrl,
					coordinator, localOrganization, superOrganization, learOrganization, otherPartners, participantMap,
					pathToScientificRequirements, removePathToScientificRequirements, confidential,
					pathsToImages, removePathsToImages, videoURLs, pathToPowerpoint, removePathToPowerpoint,
					pathToPressDocument, removePathToPressDocument, inStatus, budgets, axes);
		} else {
			projectOpt = this.projectService.updateProject(project.intValue(),
					validated, inAcronym, inScientificTitle, openSource, inStartDate, duration, inDescription,
					pathToLogo, removePathToLogo, inProjectURLObj, projectWebPageNaming, globalBudget,
					inContactType, inActivityType,
					inTrl, coordinator, localOrganization, superOrganization, learOrganization, otherPartners,
					participantMap, pathToScientificRequirements, removePathToScientificRequirements, confidential,
					pathsToImages, removePathsToImages, videoURLs, pathToPowerpoint, removePathToPowerpoint,
					pathToPressDocument, removePathToPressDocument, inStatus, budgets, axes);
		}
		if (projectOpt.isEmpty()) {
			throw new IllegalStateException("Project not found"); //$NON-NLS-1$
		}
	}

	/** Delete a project from the database.
	 *
	 * @param project the identifier of the project.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@DeleteMapping("/" + Constants.PROJECT_DELETING_ENDPOINT)
	public void deleteProject(
			@RequestParam(name = Constants.PROJECT_ENDPOINT_PARAMETER) Integer project,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, "deleteProject", project); //$NON-NLS-1$
		if (project == null || project.intValue() == 0) {
			throw new IllegalStateException("Project not found"); //$NON-NLS-1$
		}
		this.projectService.removeProject(project.intValue(), true);
	}

}
