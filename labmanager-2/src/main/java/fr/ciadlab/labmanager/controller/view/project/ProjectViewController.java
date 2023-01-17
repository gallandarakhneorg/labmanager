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

package fr.ciadlab.labmanager.controller.view.project;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.entities.project.ProjectActivityType;
import fr.ciadlab.labmanager.entities.project.ProjectStatus;
import fr.ciadlab.labmanager.entities.project.ProjectWebPageNaming;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.service.project.ProjectService;
import fr.ciadlab.labmanager.utils.funding.FundingScheme;
import fr.ciadlab.labmanager.utils.trl.TRL;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for research project views.
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
public class ProjectViewController extends AbstractViewController {

	private ProjectService projectService;

	private ResearchOrganizationService organizationService;

	private PersonService personService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param projectService the service for accessing the research projects.
	 * @param organizationService the service for accessing the research organizations.
	 * @param personService the service for accessing the persons.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public ProjectViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ProjectService projectService,
			@Autowired ResearchOrganizationService organizationService,
			@Autowired PersonService personService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.projectService = projectService;
		this.organizationService = organizationService;
		this.personService = personService;
	}

	/** Replies the model-view component for managing the projects.
	 *
	 * @param username the name of the logged-in user.
	 * @param dbId the database identifier of the person for who the projects must be exported.
	 * @param webId the webpage identifier of the person for who the projects must be exported.
	 * @param organization the identifier of the organization for which the projects must be exported.
	 * @return the model-view component.
	 * @see #showProjects(Integer, String, Integer, String, boolean, byte[])
	 */
	@GetMapping("/" + Constants.PROJECT_LIST_ENDPOINT)
	public ModelAndView projectList(
			@RequestParam(required = false, name = Constants.DBID_ENDPOINT_PARAMETER) Integer dbId,
			@RequestParam(required = false, name = Constants.WEBID_ENDPOINT_PARAMETER) String webId,
			@RequestParam(required = false, name = Constants.ORGANIZATION_ENDPOINT_PARAMETER) Integer organization,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		final String inWebId = inString(webId);
		readCredentials(username, Constants.PROJECT_LIST_ENDPOINT, dbId, inWebId, organization);
		final ModelAndView modelAndView = new ModelAndView(Constants.PROJECT_LIST_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView, false);
		initAdminTableButtons(modelAndView, endpoint(Constants.PROJECT_EDITING_ENDPOINT, Constants.PROJECT_ENDPOINT_PARAMETER));
		final List<Project> projects = extractProjectListWithoutFilter(dbId, inWebId, organization);
		modelAndView.addObject("projects", projects); //$NON-NLS-1$
		return modelAndView;
	}

	private List<Project> extractProjectListWithoutFilter(Integer dbId, String webId, Integer organization) {
		final List<Project> projects;
		if (organization != null && organization.intValue() != 0) {
			projects = this.projectService.getProjectsByOrganizationId(organization.intValue());
		} else if (dbId != null && dbId.intValue() != 0) {
			projects = this.projectService.getProjectsByPersonId(dbId.intValue());
		} else if (!Strings.isNullOrEmpty(webId)) {
			final Person person = this.personService.getPersonByWebPageId(webId);
			if (person == null) {
				throw new IllegalArgumentException("Person not found with web identifier: " + webId); //$NON-NLS-1$
			}
			projects = this.projectService.getProjectsByPersonId(person.getId());
		} else {
			projects = this.projectService.getAllProjects();
		}
		return projects;
	}

	private List<Project> extractProjectListWithFilter(Integer dbId, String webId, Integer organization) {
		final List<Project> projects;
		if (organization != null && organization.intValue() != 0) {
			projects = this.projectService.getPublicProjectsByOrganizationId(organization.intValue());
		} else if (dbId != null && dbId.intValue() != 0) {
			projects = this.projectService.getPublicProjectsByPersonId(dbId.intValue());
		} else if (!Strings.isNullOrEmpty(webId)) {
			final Person person = this.personService.getPersonByWebPageId(webId);
			if (person == null) {
				throw new IllegalArgumentException("Person not found with web identifier: " + webId); //$NON-NLS-1$
			}
			projects = this.projectService.getPublicProjectsByPersonId(person.getId());
		} else {
			projects = this.projectService.getAllPublicProjects();
		}
		return projects;
	}

	private String rootedThumbnail(String filename, boolean preserveFileExtension) {
		try {
			final URL url = new URL("file:" + filename); //$NON-NLS-1$
			final URL normalized;
			if (preserveFileExtension) {
				normalized = url;
			} else {
				normalized = FileSystem.replaceExtension(url, DownloadableFileManager.JPEG_FILE_EXTENSION);
			}
			return rooted(normalized.getPath());
		} catch (RuntimeException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/** Show the editor for a project. This editor permits to create or to edit a project.
	 *
	 * @param project the identifier of the project to edit. If it is {@code null}, the endpoint
	 *     is dedicated to the creation of a project.
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 * @throws IOException if there is some internal IO error when building the form's data.
	 */
	@GetMapping(value = "/" + Constants.PROJECT_EDITING_ENDPOINT)
	public ModelAndView projectEditor(
			@RequestParam(required = false, name = Constants.PROJECT_ENDPOINT_PARAMETER) Integer project,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws IOException {
		ensureCredentials(username, Constants.PROJECT_EDITING_ENDPOINT, project);
		final ModelAndView modelAndView = new ModelAndView(Constants.PROJECT_EDITING_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView, false);
		//
		final Project projectObj;
		if (project != null && project.intValue() != 0) {
			projectObj = this.projectService.getProjectById(project.intValue());
			if (projectObj == null) {
				throw new IllegalArgumentException("Project not found: " + project); //$NON-NLS-1$
			}

			// Provide more information about uploaded logo
			final String logoPath = projectObj.getPathToLogo();
			if (!Strings.isNullOrEmpty(logoPath)) {
				modelAndView.addObject("pathToLogo_basename", FileSystem.largeBasename(logoPath)); //$NON-NLS-1$
				modelAndView.addObject("pathToLogo_picture", rootedThumbnail(logoPath, true)); //$NON-NLS-1$
			}

			// Provide more information about uploaded scientific requirements
			final String reqPath = projectObj.getPathToScientificRequirements();
			if (!Strings.isNullOrEmpty(reqPath)) {
				modelAndView.addObject("pathToScientificRequirements_basename", FileSystem.largeBasename(reqPath)); //$NON-NLS-1$
				modelAndView.addObject("pathToScientificRequirements_picture", rootedThumbnail(reqPath, false)); //$NON-NLS-1$
			}

			// Provide more information about uploaded Powerpoint
			final String pptPath = projectObj.getPathToPowerpoint();
			if (!Strings.isNullOrEmpty(pptPath)) {
				modelAndView.addObject("pathToPowerpoint_basename", FileSystem.largeBasename(pptPath)); //$NON-NLS-1$
				modelAndView.addObject("pathToPowerpoint_picture", rootedThumbnail(pptPath, false)); //$NON-NLS-1$
			}

			// Provide more information about uploaded press document
			final String pressPath = projectObj.getPathToPressDocument();
			if (!Strings.isNullOrEmpty(pressPath)) {
				modelAndView.addObject("pathToPressDocument_basename", FileSystem.largeBasename(pressPath)); //$NON-NLS-1$
				modelAndView.addObject("pathToPressDocument_picture", rootedThumbnail(pressPath, false)); //$NON-NLS-1$
			}

			// Provide more information about uploaded associated pictures
			final List<String> imagePaths = projectObj.getPathsToImages();
			if (!imagePaths.isEmpty()) {
				final List<ImagePath> description = imagePaths.stream().map(
						it -> {
							final String basename = FileSystem.basename(it);
							final String picture = rootedThumbnail(it, true);
							return new ImagePath(basename, picture);
						}).collect(Collectors.toList());
				modelAndView.addObject("pathsToImages", description); //$NON-NLS-1$
			}
			
			// Provide a YEAR-MONTH start date
			if (projectObj.getStartDate() != null) {
				final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd"); //$NON-NLS-1$
				modelAndView.addObject("formattedStartDate", projectObj.getStartDate().format(formatter)); //$NON-NLS-1$
			}
		} else {
			projectObj = null;
		}
		//
		final List<FundingScheme> sortedFundingSchemes = buildSortedEnumConstants(FundingScheme.class, it -> it.getLabel());
		modelAndView.addObject("sortedFundingSchemes", sortedFundingSchemes); //$NON-NLS-1$
		//
		final List<ProjectActivityType> sortedActivityTypes = Arrays.asList(ProjectActivityType.values());
		modelAndView.addObject("sortedActivityTypes", sortedActivityTypes); //$NON-NLS-1$
		//
		final List<TRL> sortedTrls = Arrays.asList(TRL.values());
		modelAndView.addObject("sortedTrls", sortedTrls); //$NON-NLS-1$
		//
		final List<ProjectStatus> sortedStatus = Arrays.asList(ProjectStatus.values());
		modelAndView.addObject("sortedStatus", sortedStatus); //$NON-NLS-1$
		//
		final List<ResearchOrganization> organizations = this.organizationService.getAllResearchOrganizations().stream()
				.sorted((a, b) -> a.getAcronymOrName().compareTo(b.getAcronymOrName()))
				.collect(Collectors.toList());
		modelAndView.addObject("organizations", organizations); //$NON-NLS-1$
		//
		final List<Person> persons = this.personService.getAllPersons().stream()
				.filter(it -> !it.getMemberships().isEmpty())
				.sorted((a, b) -> a.getFullNameWithLastNameFirst().compareTo(b.getFullNameWithLastNameFirst()))
				.collect(Collectors.toList());
		modelAndView.addObject("persons", persons); //$NON-NLS-1$
		//
		modelAndView.addObject("project", projectObj); //$NON-NLS-1$
		modelAndView.addObject("formActionUrl", rooted(Constants.PROJECT_SAVING_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("formRedirectUrl", rooted(Constants.PROJECT_LIST_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("defaultFundingScheme", FundingScheme.NOT_FUNDED); //$NON-NLS-1$
		modelAndView.addObject("defaultLocalOrganization", getOrganizationWith(this.constants.getDefaultOrganization(), this.organizationService)); //$NON-NLS-1$
		modelAndView.addObject("defaultSuperOrganization", getOrganizationWith(this.constants.getDefaultSuperOrganization(), this.organizationService)); //$NON-NLS-1$
		modelAndView.addObject("defaultLearOrganization", getOrganizationWith(this.constants.getDefaultLearOrganization(), this.organizationService)); //$NON-NLS-1$
		modelAndView.addObject("defaultNaming", ProjectWebPageNaming.UNSPECIFIED); //$NON-NLS-1$
		//
		return modelAndView;
	}

	/** Replies the list of projects.
	 *
	 * @param dbId the database identifier of the person for who the projects must be exported.
	 * @param webId the webpage identifier of the person for who the projects must be exported.
	 * @param organization the identifier of the organization for which the projects must be exported.
	 * @param embedded indicates if the view will be embedded into a larger page, e.g., WordPress page. 
	 * @param username the name of the logged-in user.
	 * @return the model-view of the list of projects.
	 * @see #projectList(byte[])
	 */
	@GetMapping("/showProjects")
	public ModelAndView showProjects(
			@RequestParam(required = false, name = Constants.DBID_ENDPOINT_PARAMETER) Integer dbId,
			@RequestParam(required = false, name = Constants.WEBID_ENDPOINT_PARAMETER) String webId,
			@RequestParam(required = false, name = Constants.ORGANIZATION_ENDPOINT_PARAMETER) Integer organization,
			@RequestParam(required = false, defaultValue = "false") boolean embedded,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		final String inWebId = inString(webId);
		readCredentials(username, "showProjects", dbId, inWebId, organization); //$NON-NLS-1$
		final ModelAndView modelAndView = new ModelAndView("showProjects"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, embedded);
		//
		final List<Project> projects = extractProjectListWithFilter(dbId, inWebId, organization);
		modelAndView.addObject("projects", projects); //$NON-NLS-1$
		if (isLoggedIn()) {
			modelAndView.addObject("additionUrl", endpoint(Constants.PROJECT_EDITING_ENDPOINT)); //$NON-NLS-1$
			modelAndView.addObject("editionUrl", endpoint(Constants.PROJECT_EDITING_ENDPOINT, //$NON-NLS-1$
					Constants.PROJECT_ENDPOINT_PARAMETER));
		}
		return modelAndView;
	}

	/** Description of an image for a project.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 3.0
	 */
	private static class ImagePath {

		/** The basename of the image.
		 */
		@SuppressWarnings("unused")
		public final String basename;

		/** The picture of the image.
		 */
		@SuppressWarnings("unused")
		public final String picture;

		/** Constructor.
		 *
		 * @param basename the basename.
		 * @param picture the picture.
		 */
		ImagePath(String basename, String picture) {
			this.basename = basename;
			this.picture = picture;
		}
	}

}
