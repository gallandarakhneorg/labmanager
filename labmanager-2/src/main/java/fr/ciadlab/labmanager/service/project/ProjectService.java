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

package fr.ciadlab.labmanager.service.project;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.entities.project.ProjectActivityType;
import fr.ciadlab.labmanager.entities.project.ProjectBudget;
import fr.ciadlab.labmanager.entities.project.ProjectMember;
import fr.ciadlab.labmanager.entities.project.ProjectStatus;
import fr.ciadlab.labmanager.entities.project.ProjectWebPageNaming;
import fr.ciadlab.labmanager.entities.project.Role;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.organization.ResearchOrganizationRepository;
import fr.ciadlab.labmanager.repository.project.ProjectMemberRepository;
import fr.ciadlab.labmanager.repository.project.ProjectRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import fr.ciadlab.labmanager.utils.trl.TRL;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/** Service for the research projects.
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
@Service
public class ProjectService extends AbstractService {

	private ProjectRepository projectRepository;

	private ProjectMemberRepository projectMemberRepository;

	private ResearchOrganizationRepository organizationRepository;

	private PersonRepository personRepository;

	private DownloadableFileManager fileManager;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param projectRepository the repository for the research projects.
	 * @param projectMemberRepository the repository for the members of research projects.
	 * @param organizationRepository the repository for the research organizations.
	 * @param personRepository the repository for the persons.
	 * @param fileManager the manager of the uploaded and downloadable files.
	 */
	public ProjectService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ProjectRepository projectRepository,
			@Autowired ProjectMemberRepository projectMemberRepository,
			@Autowired ResearchOrganizationRepository organizationRepository,
			@Autowired PersonRepository personRepository,
			@Autowired DownloadableFileManager fileManager) {
		super(messages, constants);
		this.projectRepository = projectRepository;
		this.projectMemberRepository = projectMemberRepository;
		this.organizationRepository = organizationRepository;
		this.personRepository = personRepository;
		this.fileManager = fileManager;
	}

	/** Replies the list of all the projects from the database.
	 *
	 * @return the list of projects, never {@code null}.
	 */
	public List<Project> getAllProjects() {
		return this.projectRepository.findAll();
	}

	/** Replies the project with the given identifier.
	 *
	 * @param id the identifier of the project in the database.
	 * @return the project or {@code null} if there is no project with the given id.
	 */
	public Project getProjectById(int id) {
		final Optional<Project> projectOpt = this.projectRepository.findById(Integer.valueOf(id));
		if (projectOpt.isPresent()) {
			return projectOpt.get();
		}
		return null;
	}

	/** Replies the projects that are associated to the organization as coordinator,
	 * local organization, super organization or other partners. LEAR organization is not
	 * considered.
	 *
	 * @param organizationId the identifier of he organization.
	 * @return the list of projects for the organization with the given id.
	 */
	public List<Project> getProjectsByOrganizationId(int organizationId) {
		final Integer idObj = Integer.valueOf(organizationId);
		return this.projectRepository.findDistinctByCoordinatorIdOrLocalOrganizationIdOrSuperOrganizationIdOrOtherPartnersId(
				idObj, idObj, idObj, idObj);
	}

	/** Replies the projects that are associated to the person with the given identifier.
	 *
	 * @param id the identifier of the person.
	 * @return the projects.
	 */
	public List<Project> getProjectsByPersonId(int id) {
		return this.projectRepository.findDistinctByParticipantsPersonId(Integer.valueOf(id));
	}

	/** Update a project with the given information.
	 * 
	 * @param project the project to be updated.
	 * @param validated indicates if the project is validated by a local authority.
	 * @param acronym the short name of acronym of the project
	 * @param scientificTitle the title of the project with a strong highlight on the scientific contribution.
	 * @param openSource indicates if the project is open source or not.
	 * @param startDate the start date of the project in format {@code YYY-MM-DD}.
	 * @param duration the duration of the project in months.
	 * @param description the public description of the project (markdown syntax is accepted).
	 * @param pathToLogo the local path to the logo of the project.
	 * @param removePathToLogo remove the logo file before uploading.
	 * @param projectURL the URL of the project.
	 * @param webPageNaming the naming convention for the project page on the institution website.
	 * @param globalBudget the budget for all the partners in the project.
	 * @param activityType the name of the type of project activity.
	 * @param trl the name of the TRL.
	 * @param coordinator the identifier of the research organization which is coordinating the project.
	 * @param localOrganization the identifier of the local organization involved in the project.
	 * @param superOrganization the identifier of the super research organization of the local organization, if needed.
	 * @param learOrganization the identifier of the research organization which is the LEAR.
	 * @param otherPartners the set of partners in the project that are not already specified in other arguments.
	 * @param participants maps the identifiers of the participants from the local organization to their role in the project.
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
	 * @param localOrganizationBudgets the list of associated budgets and their funding scheme for the local organization.
	 * @throws IOException if the uploaded files cannot be saved on the server.
	 */
	protected void updateProject(Project project,
			boolean validated, String acronym, String scientificTitle,
			boolean openSource, LocalDate startDate, int duration, String description,
			MultipartFile pathToLogo, boolean removePathToLogo, URL projectURL,
			ProjectWebPageNaming webPageNaming, float globalBudget,
			ProjectActivityType activityType, TRL trl, int coordinator, int localOrganization,
			int superOrganization, int learOrganization, List<Integer> otherPartners,
			Map<Integer, Role> participants, MultipartFile pathToScientificRequirements, boolean removePathToScientificRequirements,
			boolean confidential, MultipartFile[] pathsToImages, boolean removePathsToImages, 
			List<String> videoURLs, MultipartFile pathToPowerpoint, boolean removePathToPowerpoint,
			MultipartFile pathToPressDocument, boolean removePathToPressDocument, ProjectStatus status,
			List<ProjectBudget> localOrganizationBudgets) throws IOException {
		project.setValidated(validated);
		project.setAcronym(acronym);
		project.setScientificTitle(scientificTitle);
		project.setOpenSource(openSource);
		project.setStartDate(startDate);
		project.setDuration(duration);
		project.setDescription(description);
		project.setProjectURL(projectURL);
		project.setWebPageNaming(webPageNaming);
		project.setGlobalBudget(globalBudget);
		project.setActivityType(activityType);
		project.setTRL(trl);
		project.setConfidential(confidential);
		project.setStatus(status);
		project.setVideoURLs(videoURLs);
		this.projectRepository.save(project);
		
		// Link budgets
		if (localOrganizationBudgets == null || localOrganizationBudgets.isEmpty()) {
			throw new IllegalArgumentException("Funding and budget for the local organization is mandatory"); //$NON-NLS-1$
		}
		for (final ProjectBudget budget : localOrganizationBudgets) {
			if (budget.getFundingScheme() == null || budget.getBudget() < 0f) {
				throw new IllegalArgumentException("Funding and budget for the local organization is mandatory"); //$NON-NLS-1$
			}
		}
		project.setBudgets(localOrganizationBudgets);
		this.projectRepository.save(project);

		// Link the organizations

		final Optional<ResearchOrganization> coordinatorOrg = this.organizationRepository.findById(Integer.valueOf(coordinator));
		if (coordinatorOrg.isEmpty()) {
			throw new IllegalArgumentException("Coordinator organization not found with id " + coordinator); //$NON-NLS-1$
		}
		project.setCoordinator(coordinatorOrg.get());

		final Optional<ResearchOrganization> localOrganizationOrg;
		if (coordinator == localOrganization && coordinatorOrg.get() != null) {
			localOrganizationOrg = coordinatorOrg;
		} else {
			localOrganizationOrg = this.organizationRepository.findById(Integer.valueOf(localOrganization));
		}
		if (localOrganizationOrg.isEmpty()) {
			throw new IllegalArgumentException("Local organization not found with id " + localOrganization); //$NON-NLS-1$
		}
		project.setLocalOrganization(localOrganizationOrg.get());

		final Optional<ResearchOrganization> superOrganizationOrg;
		if (superOrganization > 0) {
			if (superOrganization == coordinator && coordinatorOrg.get() != null) {
				superOrganizationOrg = coordinatorOrg;
			} else if (superOrganization == localOrganization && localOrganizationOrg.get() != null) {
				superOrganizationOrg = localOrganizationOrg;
			} else {
				superOrganizationOrg = this.organizationRepository.findById(Integer.valueOf(superOrganization));
			}
			if (superOrganizationOrg.isEmpty()) {
				project.setSuperOrganization(null);
			} else {
				project.setSuperOrganization(superOrganizationOrg.get());
			}
		} else {
			superOrganizationOrg = Optional.empty();
			project.setSuperOrganization(null);
		}

		final Optional<ResearchOrganization> learOrganizationOrg;
		if (learOrganization == coordinator && coordinatorOrg.get() != null) {
			learOrganizationOrg = coordinatorOrg;
		} else if (learOrganization == localOrganization && localOrganizationOrg.get() != null) {
			learOrganizationOrg = localOrganizationOrg;
		} else if (learOrganization == superOrganization && superOrganizationOrg.get() != null) {
			learOrganizationOrg = superOrganizationOrg;
		} else {
			learOrganizationOrg = this.organizationRepository.findById(Integer.valueOf(learOrganization));
		}
		if (learOrganizationOrg.isEmpty()) {
			throw new IllegalArgumentException("LEAR organization not found with id " + learOrganization); //$NON-NLS-1$
		}
		project.setLearOrganization(learOrganizationOrg.get());
		
		final Set<ResearchOrganization> otherPartnersOrg = new HashSet<>();
		if (otherPartners != null && !otherPartners.isEmpty()) {
			otherPartners.stream().forEach(it -> {
				final Optional<ResearchOrganization> orga = this.organizationRepository.findById(it);
				if (orga.isPresent()) {
					otherPartnersOrg.add(orga.get());
				}
			});
		}
		project.setOtherPartners(otherPartnersOrg);

		this.projectRepository.save(project);

		// Link the participants

		final List<ProjectMember> projectParticipants = new ArrayList<>();
		if (participants != null && !participants.isEmpty()) {
			participants.entrySet().stream().forEach(it -> {
				final Integer personId = it.getKey();
				final Optional<Person> person = this.personRepository.findById(personId);
				if (person.isEmpty()) {
					throw new IllegalStateException("Person not found with id " + personId); //$NON-NLS-1$
				}
				final Role role = it.getValue();
				final ProjectMember member = new ProjectMember();
				member.setPerson(person.get());
				member.setRole(role);
				this.projectMemberRepository.save(member);
				projectParticipants.add(member);
			});
		}
		project.setParticipants(projectParticipants);
		this.projectRepository.save(project);

		// Link the uploaded files

		updateLogo(project, removePathToLogo, pathToLogo);
		updateScientificRequirements(project, removePathToScientificRequirements, pathToScientificRequirements);
		updatePowerpoint(project, removePathToPowerpoint, pathToPowerpoint);
		updatePressDocument(project, removePathToPressDocument, pathToPressDocument);
		updateImages(project, removePathsToImages, pathsToImages);
		this.projectRepository.save(project);
	}

	private boolean updateImages(Project project, boolean explicitRemove, MultipartFile[] uploadedFiles) throws IOException {
		boolean changed = false;
		if (explicitRemove) {
			int i = 0;
			for (final String imagePath : project.getPathsToImages()) {
				final String ext;
				if (!Strings.isNullOrEmpty(imagePath)) {
					ext = FileSystem.extension(imagePath);
				} else {
					ext = null;
				}
				try {
					this.fileManager.deleteProjectImage(project.getId(), i, ext);
				} catch (Throwable ex) {
					// Silent
				}
				changed = true;
				++i;
			}
			if (changed) {
				project.setPathsToImages(null);
			}
		}
		if (uploadedFiles != null && uploadedFiles.length > 0) {
			final List<String> paths = new ArrayList<>(uploadedFiles.length);
			final MutableInt i = new MutableInt(0);
			for (; i.intValue() < uploadedFiles.length; i.increment()) {
				final MultipartFile uploadedFile = uploadedFiles[i.intValue()];
				final String ext;
				if (uploadedFile != null) {
					ext = FileSystem.extension(uploadedFile.getOriginalFilename());
				} else {
					final String filename = project.getPathToLogo();
					if (!Strings.isNullOrEmpty(filename)) {
						ext = FileSystem.extension(filename);
					} else {
						ext = null;
					}
				}
				if (updateUploadedFile(false, uploadedFile,
						"Project image uploaded at: ", //$NON-NLS-1$
						it -> paths.add(it),
						() -> this.fileManager.makeProjectImageFilename(project.getId(), i.intValue(), ext),
						() -> this.fileManager.deleteProjectImage(project.getId(), i.intValue(), ext),
						(fn, th) -> this.fileManager.saveImage(fn, uploadedFile))) {
					changed = true;
				}
			}
			project.setPathsToImages(paths);
		}
		return changed;
	}

	private boolean updatePressDocument(Project project, boolean explicitRemove, MultipartFile uploadedFile) throws IOException {
		return updateUploadedFile(explicitRemove, uploadedFile,
				"Project press document uploaded at: ", //$NON-NLS-1$
				it -> project.setPathToPressDocument(it),
				() -> this.fileManager.makeProjectPressDocumentFilename(project.getId()),
				() -> this.fileManager.deleteProjectPressDocument(project.getId()),
				(fn, th) -> this.fileManager.savePdfAndThumbnailFiles(fn, th, uploadedFile));
	}

	private boolean updatePowerpoint(Project project, boolean explicitRemove, MultipartFile uploadedFile) throws IOException {
		final String ext;
		if (uploadedFile != null) {
			ext = FileSystem.extension(uploadedFile.getOriginalFilename());
		} else {
			final String filename = project.getPathToPowerpoint();
			if (!Strings.isNullOrEmpty(filename)) {
				ext = FileSystem.extension(filename);
			} else {
				ext = null;
			}
		}
		return updateUploadedFile(explicitRemove, uploadedFile,
				"Project powerpoint uploaded at: ", //$NON-NLS-1$
				it -> project.setPathToPowerpoint(it),
				() -> this.fileManager.makeProjectPowerpointFilename(project.getId(), ext),
				() -> this.fileManager.deleteProjectPowerpoint(project.getId(), ext),
				(fn, th) -> this.fileManager.savePowerpointAndThumbnailFiles(fn, th, uploadedFile));
	}

	private boolean updateScientificRequirements(Project project, boolean explicitRemove, MultipartFile uploadedFile) throws IOException {
		return updateUploadedFile(explicitRemove, uploadedFile,
				"Project scientific requirements uploaded at: ", //$NON-NLS-1$
				it -> project.setPathToScientificRequirements(it),
				() -> this.fileManager.makeProjectScientificRequirementsFilename(project.getId()),
				() -> this.fileManager.deleteProjectScientificRequirements(project.getId()),
				(fn, th) -> this.fileManager.savePdfAndThumbnailFiles(fn, th, uploadedFile));
	}

	private boolean updateLogo(Project project, boolean explicitRemove, MultipartFile uploadedFile) throws IOException {
		final String ext;
		if (uploadedFile != null) {
			ext = FileSystem.extension(uploadedFile.getOriginalFilename());
		} else {
			final String filename = project.getPathToLogo();
			if (!Strings.isNullOrEmpty(filename)) {
				ext = FileSystem.extension(filename);
			} else {
				ext = null;
			}
		}
		return updateUploadedFile(explicitRemove, uploadedFile,
				"Project logo uploaded at: ", //$NON-NLS-1$
				it -> project.setPathToLogo(it),
				() -> this.fileManager.makeProjectLogoFilename(project.getId(), ext),
				() -> this.fileManager.deleteProjectLogo(project.getId(), ext),
				(fn, th) -> this.fileManager.saveImage(fn, uploadedFile));
	}

	private boolean updateUploadedFile(boolean explicitRemove, MultipartFile uploadedFile,
			String logMessage, Consumer<String> setter, Supplier<File> filename,
			Callback delete, Saver save) throws IOException {
		// Treat the uploaded files
		boolean hasUploaded = false;
		if (explicitRemove) {
			try {
				delete.apply();
			} catch (Throwable ex) {
				// Silent
			}
			setter.accept(null);
			hasUploaded = true;
		}
		if (uploadedFile != null && !uploadedFile.isEmpty()) {
			final File fn = filename.get();
			final File th = FileSystem.replaceExtension(fn, DownloadableFileManager.JPEG_FILE_EXTENSION);
			save.apply(fn, th);
			setter.accept(fn.getPath());
			hasUploaded = true;
			getLogger().info(logMessage + fn.getPath());
		}
		return hasUploaded;
	}

	/** Create a project with the given information.
	 * 
	 * @param validated indicates if the project is validated by a local authority.
	 * @param acronym the short name of acronym of the project
	 * @param scientificTitle the title of the project with a strong highlight on the scientific contribution.
	 * @param openSource indicates if the project is open source or not.
	 * @param startDate the start date of the project in format {@code YYY-MM-DD}.
	 * @param duration the duration of the project in months.
	 * @param description the public description of the project (markdown syntax is accepted).
	 * @param pathToLogo the local path to the logo of the project.
	 * @param removePathToLogo remove the existing logo before uploading.
	 * @param projectURL the URL of the project.
	 * @param webPageNaming the naming convention for the project page on the institution website.
	 * @param globalBudget the budget for all the partners in the project.
	 * @param activityType the name of the type of project activity.
	 * @param trl the name of the TRL.
	 * @param coordinator the identifier of the research organization which is coordinating the project.
	 * @param localOrganization the identifier of the local organization involved in the project.
	 * @param superOrganization the identifier of the super research organization of the local organization, if needed.
	 * @param learOrganization the identifier of the research organization which is the LEAR.
	 * @param otherPartners the set of partners in the project that are not already specified in other arguments.
	 * @param participants maps the identifiers of the participants from the local organization to their role in the project.
	 * @param pathToScientificRequirements the local path to a document that contains the scientific requirements of the project.
	 * @param removePathToScientificRequirements remove the existing requirements before uploading.
	 * @param confidential indicates if the project should be confidential or not.
	 * @param pathsToImages the local paths to the associated images.
	 * @param removePathsToImages remove the associated images before uploading.
	 * @param videoURLs list of URLs of videos that are related to the project
	 * @param pathToPowerpoint the local path to a PowerPoint presentation of the project.
	 * @param removePathToPowerpoint remove the existing PowerPoint before uploading.
	 * @param pathToPressDocument the local path to a press document related to the project.
	 * @param removePathToPressDocument remove the existing press document before uploading.
	 * @param status the name of the project status.
	 * @param localOrganizationBudgets the list of associated budgets and their funding scheme for the local organization.
	 * @return the reference to the created project.
	 * @throws IOException if the uploaded files cannot be saved on the server.
	 */
	public Optional<Project> createProject(
			boolean validated, String acronym, String scientificTitle,
			boolean openSource, LocalDate startDate, int duration, String description,
			MultipartFile pathToLogo, boolean removePathToLogo, URL projectURL,
			ProjectWebPageNaming webPageNaming, float globalBudget,
			ProjectActivityType activityType, TRL trl, int coordinator, int localOrganization,
			int superOrganization, int learOrganization, List<Integer> otherPartners, Map<Integer, Role> participants,
			MultipartFile pathToScientificRequirements, boolean removePathToScientificRequirements,
			boolean confidential, MultipartFile[] pathsToImages, boolean removePathsToImages,
			List<String> videoURLs, MultipartFile pathToPowerpoint, boolean removePathToPowerpoint,
			MultipartFile pathToPressDocument, boolean removePathToPressDocument, ProjectStatus status,
			List<ProjectBudget> localOrganizationBudgets) throws IOException {
		final Project project = new Project();
		try {
			updateProject(project, validated, acronym, scientificTitle, openSource,
					startDate, duration, description, pathToLogo, removePathToLogo, projectURL, webPageNaming, globalBudget,
					activityType, trl, coordinator, localOrganization, superOrganization, learOrganization,
					otherPartners, participants, pathToScientificRequirements, removePathToScientificRequirements, confidential,
					pathsToImages, removePathsToImages, videoURLs, pathToPowerpoint, removePathToPowerpoint,
					pathToPressDocument, removePathToPressDocument, status, localOrganizationBudgets);
		} catch (Throwable ex) {
			// Delete created project
			if (project.getId() != 0) {
				try {
					removeProject(project.getId(), true);
				} catch (Throwable ex0) {
					// Silent
				}
			}
			getLogger().error(ex.getLocalizedMessage(), ex);
			throw ex;
		}
		return Optional.of(project);
	}

	/** Update a project with the given information.
	 * 
	 * @param projectId the identifier of the project to be updated.
	 * @param validated indicates if the project is validated by a local authority.
	 * @param acronym the short name of acronym of the project
	 * @param scientificTitle the title of the project with a strong highlight on the scientific contribution.
	 * @param openSource indicates if the project is open source or not.
	 * @param startDate the start date of the project in format {@code YYY-MM-DD}.
	 * @param duration the duration of the project in months.
	 * @param description the public description of the project (markdown syntax is accepted).
	 * @param pathToLogo the local path to the logo of the project.
	 * @param removePathToLogo remove the existing logo before uploading.
	 * @param projectURL the URL of the project.
	 * @param webPageNaming the naming convention for the project page on the institution website.
	 * @param globalBudget the budget for all the partners in the project.
	 * @param activityType the name of the type of project activity.
	 * @param trl the name of the TRL.
	 * @param coordinator the identifier of the research organization which is coordinating the project.
	 * @param localOrganization the identifier of the local organization involved in the project.
	 * @param superOrganization the identifier of the super research organization of the local organization, if needed.
	 * @param learOrganization the identifier of the research organization which is the LEAR.
	 * @param otherPartners the set of partners in the project that are not already specified in other arguments.
	 * @param participants maps the identifiers of the participants from the local organization to their role in the project.
	 * @param pathToScientificRequirements the local path to a document that contains the scientific requirements of the project.
	 * @param removePathToScientificRequirements remove the existing requirements before uploading.
	 * @param confidential indicates if the project should be confidential or not.
	 * @param pathsToImages the local paths to the associated images.
	 * @param removePathsToImages remove the associated images before uploading.
	 * @param videoURLs list of URLs of videos that are related to the project
	 * @param pathToPowerpoint the local path to a PowerPoint presentation of the project.
	 * @param removePathToPowerpoint remove the existing PowerPoint before uploading.
	 * @param pathToPressDocument the local path to a press document related to the project.
	 * @param removePathToPressDocument remove the existing press document before uploading.
	 * @param status the name of the project status.
	 * @param localOrganizationBudgets the list of associated budgets and their funding scheme for the local organization.
	 * @return the reference to the updated project.
	 * @throws IOException if the uploaded files cannot be saved on the server.
	 */
	public Optional<Project> updateProject(int projectId,
			boolean validated, String acronym, String scientificTitle,
			boolean openSource, LocalDate startDate, int duration, String description,
			MultipartFile pathToLogo, boolean removePathToLogo, URL projectURL,
			ProjectWebPageNaming webPageNaming, float globalBudget,
			ProjectActivityType activityType, TRL trl, int coordinator, int localOrganization,
			int superOrganization, int learOrganization, List<Integer> otherPartners, Map<Integer, Role> participants, 
			MultipartFile pathToScientificRequirements, boolean removePathToScientificRequirements,
			boolean confidential, MultipartFile[] pathsToImages, boolean removePathsToImages,
			List<String> videoURLs, MultipartFile pathToPowerpoint, boolean removePathToPowerpoint,
			MultipartFile pathToPressDocument, boolean removePathToPressDocument, ProjectStatus status,
			List<ProjectBudget> localOrganizationBudgets) throws IOException {
		final Optional<Project> res;
		if (projectId >= 0) {
			res = this.projectRepository.findById(Integer.valueOf(projectId));
		} else {
			res = Optional.empty();
		}
		if (res.isPresent()) {
			updateProject(res.get(), validated, acronym, scientificTitle, openSource,
					startDate, duration, description, pathToLogo, removePathToLogo, projectURL, webPageNaming, globalBudget,
					activityType, trl, coordinator, localOrganization, superOrganization, learOrganization,
					otherPartners, participants, pathToScientificRequirements, removePathToScientificRequirements, confidential,
					pathsToImages, removePathsToImages, videoURLs, pathToPowerpoint, removePathToPowerpoint,
					pathToPressDocument, removePathToPressDocument, status, localOrganizationBudgets);
		}
		return res;
	}


	/** Remove the project with the given identifier.
	 *
	 * @param identifier the identifier of the publication to remove.
	 * @param removeAssociatedFiles indicates if the associated files should be also deleted.
	 */
	public void removeProject(int identifier, boolean removeAssociatedFiles) {
		final Integer id = Integer.valueOf(identifier);
		final Optional<Project> projectOpt = this.projectRepository.findById(Integer.valueOf(identifier));
		if (projectOpt.isPresent()) {
			final Project project = projectOpt.get();
			//
			final String pathToLogo = project.getPathToLogo();
			final String pathToPowerpoint = project.getPathToPowerpoint();
			//
			project.setCoordinator(null);
			project.setLocalOrganization(null);
			project.setSuperOrganization(null);
			project.setLearOrganization(null);
			project.setOtherPartners(null);
			project.setParticipants(null);
			project.setPathToLogo(null);
			project.setPathsToImages(null);
			project.setPathToScientificRequirements(null);
			project.setPathToPowerpoint(null);
			project.setPathToPressDocument(null);
			this.projectRepository.deleteById(id);
			if (removeAssociatedFiles) {
				try {
					if (!Strings.isNullOrEmpty(pathToLogo)) {
						final String ext = FileSystem.extension(pathToLogo);
						this.fileManager.deleteProjectLogo(identifier, ext);
					}
				} catch (Throwable ex) {
					// Silent
				}
				try {
					this.fileManager.deleteProjectScientificRequirements(identifier);
				} catch (Throwable ex) {
					// Silent
				}
				try {
					if (!Strings.isNullOrEmpty(pathToPowerpoint)) {
						final String ext = FileSystem.extension(pathToPowerpoint);
						this.fileManager.deleteProjectPowerpoint(identifier, ext);
					}
				} catch (Throwable ex) {
					// Silent
				}
				try {
					this.fileManager.deleteProjectPressDocument(identifier);
				} catch (Throwable ex) {
					// Silent
				}
			}
		}
	}

	/** Internal callback object.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 3.0
	 */
	@FunctionalInterface
	private interface Callback {

		/** Callback function.
		 *
		 * @throws IOException in case of error
		 */
		void apply() throws IOException;
		
	}

	/** Internal callback object.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 3.0
	 */
	@FunctionalInterface
	private interface Saver {

		/** Callback function.
		 *
		 * @param filename the name of the file.
		 * @param thumbnail the filename of the thumbnail.
		 * @throws IOException in case of error
		 */
		void apply(File filename, File thumbnail) throws IOException;
		
	}

}
