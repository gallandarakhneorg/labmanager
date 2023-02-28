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

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.entities.project.ProjectActivityType;
import fr.ciadlab.labmanager.entities.project.ProjectBudget;
import fr.ciadlab.labmanager.entities.project.ProjectCategory;
import fr.ciadlab.labmanager.entities.project.ProjectContractType;
import fr.ciadlab.labmanager.entities.project.ProjectMember;
import fr.ciadlab.labmanager.entities.project.ProjectStatus;
import fr.ciadlab.labmanager.entities.project.ProjectWebPageNaming;
import fr.ciadlab.labmanager.entities.project.Role;
import fr.ciadlab.labmanager.entities.scientificaxis.ScientificAxis;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.organization.ResearchOrganizationRepository;
import fr.ciadlab.labmanager.repository.project.ProjectMemberRepository;
import fr.ciadlab.labmanager.repository.project.ProjectRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import fr.ciadlab.labmanager.service.member.MembershipService;
import fr.ciadlab.labmanager.utils.CountryCodeUtils;
import fr.ciadlab.labmanager.utils.funding.FundingScheme;
import fr.ciadlab.labmanager.utils.trl.TRL;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.util.CountryCode;
import org.arakhne.afc.util.MultiCollection;
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

	private MembershipService membershipService;

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
	 * @param membershipService the service for memberships.
	 * @param fileManager the manager of the uploaded and downloadable files.
	 */
	public ProjectService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ProjectRepository projectRepository,
			@Autowired ProjectMemberRepository projectMemberRepository,
			@Autowired ResearchOrganizationRepository organizationRepository,
			@Autowired PersonRepository personRepository,
			@Autowired MembershipService membershipService,
			@Autowired DownloadableFileManager fileManager) {
		super(messages, constants);
		this.projectRepository = projectRepository;
		this.projectMemberRepository = projectMemberRepository;
		this.organizationRepository = organizationRepository;
		this.personRepository = personRepository;
		this.fileManager = fileManager;
		this.membershipService = membershipService;
	}

	/** Replies the list of all the projects from the database.
	 *
	 * @return the list of projects, never {@code null}.
	 */
	public List<Project> getAllProjects() {
		return this.projectRepository.findAll();
	}

	/** Replies the list of all the public projects from the database.
	 * A public project is not confidential and has status "accepted".
	 *
	 * @return the list of public projects, never {@code null}.
	 */
	public List<Project> getAllPublicProjects() {
		return this.projectRepository.findDistinctByConfidentialAndStatus(Boolean.FALSE, ProjectStatus.ACCEPTED);
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
		return this.projectRepository.findDistinctOrganizationProjects(idObj);
	}

	/** Replies the public projects that are associated to the organization as coordinator,
	 * local organization, super organization or other partners. LEAR organization is not
	 * considered.
	 * A public project is not confidential and has status "accepted".
	 *
	 * @param organizationId the identifier of he organization.
	 * @return the list of public projects for the organization with the given id.
	 */
	public List<Project> getPublicProjectsByOrganizationId(int organizationId) {
		final Integer idObj = Integer.valueOf(organizationId);
		return this.projectRepository.findDistinctOrganizationProjects(Boolean.FALSE, ProjectStatus.ACCEPTED, idObj);
	}

	/** Replies the projects that are associated to the person with the given identifier.
	 *
	 * @param id the identifier of the person.
	 * @return the projects.
	 */
	public Set<Project> getProjectsByPersonId(int id) {
		return this.projectRepository.findDistinctPersonProjects(Integer.valueOf(id));
	}

	/** Replies public the projects that are associated to the person with the given identifier.
	 * A public project is not confidential and has status "accepted".
	 *
	 * @param id the identifier of the person.
	 * @return the public projects.
	 */
	public List<Project> getPublicProjectsByPersonId(int id) {
		return this.projectRepository.findDistinctPersonProjects(
				Boolean.FALSE, ProjectStatus.ACCEPTED, Integer.valueOf(id));
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
	 * @param contractType the name of the type of contract.
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
	 * @param axes the scientific axes to which the project is associated to.
	 * @throws IOException if the uploaded files cannot be saved on the server.
	 */
	protected void updateProject(Project project,
			boolean validated, String acronym, String scientificTitle,
			boolean openSource, LocalDate startDate, int duration, String description,
			MultipartFile pathToLogo, boolean removePathToLogo, URL projectURL,
			ProjectWebPageNaming webPageNaming, float globalBudget, ProjectContractType contractType,
			ProjectActivityType activityType, TRL trl, int coordinator, int localOrganization,
			int superOrganization, int learOrganization, List<Integer> otherPartners,
			Map<Integer, Role> participants, MultipartFile pathToScientificRequirements, boolean removePathToScientificRequirements,
			boolean confidential, MultipartFile[] pathsToImages, boolean removePathsToImages, 
			List<String> videoURLs, MultipartFile pathToPowerpoint, boolean removePathToPowerpoint,
			MultipartFile pathToPressDocument, boolean removePathToPressDocument, ProjectStatus status,
			List<ProjectBudget> localOrganizationBudgets, List<ScientificAxis> axes) throws IOException {
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
		project.setContractType(contractType);
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

		// Links to scientific axes

		project.setScientificAxes(axes);
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
	 * @param contractType the name of the type of contract.
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
	 * @param axes the scientific axes to which the project is associated to.
	 * @return the reference to the created project.
	 * @throws IOException if the uploaded files cannot be saved on the server.
	 */
	public Optional<Project> createProject(
			boolean validated, String acronym, String scientificTitle,
			boolean openSource, LocalDate startDate, int duration, String description,
			MultipartFile pathToLogo, boolean removePathToLogo, URL projectURL,
			ProjectWebPageNaming webPageNaming, float globalBudget,
			ProjectContractType contractType, ProjectActivityType activityType, TRL trl, int coordinator, int localOrganization,
			int superOrganization, int learOrganization, List<Integer> otherPartners, Map<Integer, Role> participants,
			MultipartFile pathToScientificRequirements, boolean removePathToScientificRequirements,
			boolean confidential, MultipartFile[] pathsToImages, boolean removePathsToImages,
			List<String> videoURLs, MultipartFile pathToPowerpoint, boolean removePathToPowerpoint,
			MultipartFile pathToPressDocument, boolean removePathToPressDocument, ProjectStatus status,
			List<ProjectBudget> localOrganizationBudgets, List<ScientificAxis> axes) throws IOException {
		final Project project = new Project();
		try {
			updateProject(project, validated, acronym, scientificTitle, openSource,
					startDate, duration, description, pathToLogo, removePathToLogo, projectURL, webPageNaming, globalBudget,
					contractType, activityType, trl, coordinator, localOrganization, superOrganization, learOrganization,
					otherPartners, participants, pathToScientificRequirements, removePathToScientificRequirements, confidential,
					pathsToImages, removePathsToImages, videoURLs, pathToPowerpoint, removePathToPowerpoint,
					pathToPressDocument, removePathToPressDocument, status, localOrganizationBudgets, axes);
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
	 * @param contractType the name of the type of contract.
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
	 * @param axes the scientific axes to which the project is associated to.
	 * @return the reference to the updated project.
	 * @throws IOException if the uploaded files cannot be saved on the server.
	 */
	public Optional<Project> updateProject(int projectId,
			boolean validated, String acronym, String scientificTitle,
			boolean openSource, LocalDate startDate, int duration, String description,
			MultipartFile pathToLogo, boolean removePathToLogo, URL projectURL,
			ProjectWebPageNaming webPageNaming, float globalBudget,
			ProjectContractType contractType, ProjectActivityType activityType, TRL trl, int coordinator, int localOrganization,
			int superOrganization, int learOrganization, List<Integer> otherPartners, Map<Integer, Role> participants, 
			MultipartFile pathToScientificRequirements, boolean removePathToScientificRequirements,
			boolean confidential, MultipartFile[] pathsToImages, boolean removePathsToImages,
			List<String> videoURLs, MultipartFile pathToPowerpoint, boolean removePathToPowerpoint,
			MultipartFile pathToPressDocument, boolean removePathToPressDocument, ProjectStatus status,
			List<ProjectBudget> localOrganizationBudgets, List<ScientificAxis> axes) throws IOException {
		final Optional<Project> res;
		if (projectId >= 0) {
			res = this.projectRepository.findById(Integer.valueOf(projectId));
		} else {
			res = Optional.empty();
		}
		if (res.isPresent()) {
			updateProject(res.get(), validated, acronym, scientificTitle, openSource,
					startDate, duration, description, pathToLogo, removePathToLogo, projectURL, webPageNaming, globalBudget,
					contractType, activityType, trl, coordinator, localOrganization, superOrganization, learOrganization,
					otherPartners, participants, pathToScientificRequirements, removePathToScientificRequirements, confidential,
					pathsToImages, removePathsToImages, videoURLs, pathToPowerpoint, removePathToPowerpoint,
					pathToPressDocument, removePathToPressDocument, status, localOrganizationBudgets, axes);
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
		final Optional<Project> projectOpt = this.projectRepository.findById(id);
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
			project.setScientificAxes(null);
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

	/** Replies the list of memberships that corresponds to recruited persons for the project.
	 * The recruited persons are defined as persons who are involved in the project and
	 * who are associated to a not-permanent membership during the period of the project.
	 *
	 * @param project the project for which the cruited persons should be extracted.
	 * @return the list of involved persons.
	 * @since 3.4
	 */
	public Stream<Membership> getRecuitedPersonStream(Project project) {
		final LocalDate sdt = project.getStartDate();
		final LocalDate edt = project.getEndDate();
		if (sdt != null && edt != null) {
			return project.getParticipants().stream()
					.filter(it -> it.getPerson() != null)
					.map(it -> {
						final Optional<Membership> mbrs = this.membershipService.getMembershipsForPerson(it.getPerson().getId()).stream()
							.filter(it1 -> !it1.isPermanentPosition() && it1.isActiveIn(sdt, edt)).findAny();
						if (mbrs.isEmpty()) {
							return null;
						}
						return mbrs.get();
					})
					.filter(Objects::nonNull);
		}
		return Collections.<Membership>emptyList().stream();
	}

	/** Replies the list of memberships that corresponds to recruited persons for the project.
	 * The recruited persons are defined as persons who are involved in the project and
	 * who are associated to a not-permanent membership during the period of the project.
	 *
	 * @param project the project for which the cruited persons should be extracted.
	 * @return the list of involved persons.
	 * @since 3.4
	 */
	public List<Membership> getRecuitedPersons(Project project) {
		return getRecuitedPersonStream(project).collect(Collectors.toList());
	}

	/** Replies the project instances that corresponds to the given identifiers.
	 *
	 * @param identifiers the list of identifiers.
	 * @return the list of projects.
	 * @since 3.5
	 */
	public List<Project> getProjectsByIds(List<Integer> identifiers) {
		final List<Project> projects = this.projectRepository.findAllById(identifiers);
		if (projects.size() != identifiers.size()) {
			throw new IllegalArgumentException("Project not found"); //$NON-NLS-1$
		}
		return projects;
	}

	/** Replies the numbers of projects have started per year for the given set of projects.
	 *
	 * @param projects the projects to analyze.
	 * @return rows with: year, academic project count, not-academic project count, other projects.
	 * @since 3.6
	 */
	@SuppressWarnings("static-method")
	public List<List<Number>> getNumberOfStartingProjectsPerYear(List<Project> projects) {
		final Map<Integer, Collection<Project>> projectsPerYear = projects.stream()
				.filter(it -> it.getStatus() == ProjectStatus.ACCEPTED)
				.collect(Collectors.toMap(
						it -> Integer.valueOf(it.getStartYear()),
						it -> Collections.singleton(it),
						(a, b) -> {
							final MultiCollection<Project> multi = new MultiCollection<>();
							multi.addCollection(a);
							multi.addCollection(b);
							return multi;
						}));
		return projectsPerYear.entrySet().stream()
			.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
			.map(it -> {
				int nbAap = 0;
				int NbIndus = 0;
				int nbAutoFunded = 0;
				float budget = 0f;
				for (final Project prj : it.getValue()) {
					switch (prj.getCategory()) {
					case COMPETITIVE_CALL_PROJECT:
						++nbAap;
						budget += prj.getTotalLocalOrganizationBudget();
						break;
					case NOT_ACADEMIC_PROJECT:
						++NbIndus;
						budget += prj.getTotalLocalOrganizationBudget();
						break;
					case AUTO_FUNDING:
					case OPEN_SOURCE:
						++nbAutoFunded;
						budget += prj.getTotalLocalOrganizationBudget();
						break;
					default:
						break;
					}
				}
				final List<Number> columns = new ArrayList<>(3);
				columns.add(it.getKey());
				columns.add(Integer.valueOf(nbAap));
				columns.add(Integer.valueOf(NbIndus));
				columns.add(Integer.valueOf(nbAutoFunded));
				columns.add(Float.valueOf(budget));
				return columns;
			})
			.collect(Collectors.toList());
	}

	/** Replies the numbers of projects are running per year for the given set of projects.
	 *
	 * @param projects the projects to analyze.
	 * @return rows with: year, academic project count, not-academic project count, other projects.
	 * @since 3.6
	 */
	@SuppressWarnings("static-method")
	public List<List<Number>> getNumberOfOngoingProjectsPerYear(List<Project> projects) {
		final Map<Integer, List<Project>> projectsPerYear = new HashMap<>();
		projects.stream()
			.filter(it -> it.getStatus() == ProjectStatus.ACCEPTED)
			.forEach(it -> {
				for (int y = it.getStartYear(); y <= it.getEndYear(); ++y) {
					final List<Project> yearProjects = projectsPerYear.computeIfAbsent(Integer.valueOf(y), it0 -> new ArrayList<>());
					yearProjects.add(it);
				}
			});
		return projectsPerYear.entrySet().stream()
			.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
			.map(it -> {
				int aap = 0;
				int indus = 0;
				int autoFunded = 0;
				float budget = 0f;
				for (final Project prj : it.getValue()) {
					switch (prj.getCategory()) {
					case COMPETITIVE_CALL_PROJECT:
						++aap;
						budget += prj.getEstimatedAnnualLocalOrganizationBudgetFor(it.getKey().intValue());
						break;
					case NOT_ACADEMIC_PROJECT:
						++indus;
						budget += prj.getEstimatedAnnualLocalOrganizationBudgetFor(it.getKey().intValue());
						break;
					case AUTO_FUNDING:
					case OPEN_SOURCE:
						++autoFunded;
						budget += prj.getEstimatedAnnualLocalOrganizationBudgetFor(it.getKey().intValue());
						break;
					default:
						break;
					}
				}
				final List<Number> columns = new ArrayList<>(3);
				columns.add(it.getKey());
				columns.add(Integer.valueOf(aap));
				columns.add(Integer.valueOf(indus));
				columns.add(Integer.valueOf(autoFunded));
				columns.add(Float.valueOf(budget));
				return columns;
			})
			.collect(Collectors.toList());
	}

	/** Replies the numbers of projects per funding scheme for the given set of projects.
	 *
	 * @param projects the projects to analyze.
	 * @return rows with: type, count.
	 * @since 3.6
	 */
	@SuppressWarnings("static-method")
	public List<List<Object>> getNumberOfProjectsPerType(Collection<? extends Project> projects) {
		final Map<ProjectCategory, Integer> projectsPerCategory = projects.stream()
				.filter(it -> it.getStatus() == ProjectStatus.ACCEPTED)
				.collect(Collectors.toMap(
						it -> it.getCategory(),
						it -> Integer.valueOf(1),
						(a, b) -> {
							return Integer.valueOf(a.intValue() + b.intValue());
						}));
		return projectsPerCategory.entrySet().stream()
			.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
			.map(it -> {
				final List<Object> columns = new ArrayList<>(2);
				columns.add(it.getKey().getLabel());
				columns.add(it.getValue());
				return columns;
			})
			.collect(Collectors.toList());
	}

	/** Replies the numbers of academic projects per funding scheme for the given set of projects.
	 *
	 * @param projects the projects to analyze.
	 * @return rows with: type, count.
	 * @since 3.6
	 */
	@SuppressWarnings("static-method")
	public List<List<Object>> getNumberOfAcademicProjectsPerType(Collection<? extends Project> projects) {
		final Map<FundingScheme, Integer> projectsPerScheme = projects.stream()
				.filter(it -> it.getMajorFundingScheme().isCompetitive() && it.getStatus() == ProjectStatus.ACCEPTED)
				.collect(Collectors.toMap(
						it -> it.getMajorFundingScheme(),
						it -> Integer.valueOf(1),
						(a, b) -> {
							return Integer.valueOf(a.intValue() + b.intValue());
						}));
		return projectsPerScheme.entrySet().stream()
			.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
			.map(it -> {
				final List<Object> columns = new ArrayList<>(2);
				columns.add(it.getKey().getLabel());
				columns.add(it.getValue());
				return columns;
			})
			.collect(Collectors.toList());
	}

	/** Replies the numbers of not-academic projects per funding scheme for the given set of projects.
	 *
	 * @param projects the projects to analyze.
	 * @return rows with: type, count.
	 * @since 3.6
	 */
	@SuppressWarnings("static-method")
	public List<List<Object>> getNumberOfNotAcademicProjectsPerType(Collection<? extends Project> projects) {
		final Map<FundingScheme, Integer> projectsPerScheme = projects.stream()
				.filter(it -> (it.getMajorFundingScheme().isAcademicButContractual() || it.getMajorFundingScheme().isNotAcademic())
							&& it.getStatus() == ProjectStatus.ACCEPTED)
				.collect(Collectors.toMap(
						it -> it.getMajorFundingScheme(),
						it -> Integer.valueOf(1),
						(a, b) -> {
							return Integer.valueOf(a.intValue() + b.intValue());
						}));
		return projectsPerScheme.entrySet().stream()
			.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
			.map(it -> {
				final List<Object> columns = new ArrayList<>(2);
				columns.add(it.getKey().getLabel());
				columns.add(it.getValue());
				return columns;
			})
			.collect(Collectors.toList());
	}

	/** Replies the numbers of projects per activity type for the given set of projects.
	 *
	 * @param projects the projects to analyze.
	 * @return rows with: type, count.
	 * @since 3.6
	 */
	@SuppressWarnings("static-method")
	public List<List<Object>> getNumberOfProjectsPerActivityType(Collection<? extends Project> projects) {
		final Map<ProjectActivityType, Integer> projectsPerType = projects.stream()
				.filter(it -> it.getActivityType() != null && it.getStatus() == ProjectStatus.ACCEPTED)
				.collect(Collectors.toMap(
						it -> it.getActivityType(),
						it -> Integer.valueOf(1),
						(a, b) -> {
							return Integer.valueOf(a.intValue() + b.intValue());
						}));
		return projectsPerType.entrySet().stream()
			.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
			.map(it -> {
				final List<Object> columns = new ArrayList<>(2);
				columns.add(it.getKey().getLabel());
				columns.add(it.getValue());
				return columns;
			})
			.collect(Collectors.toList());
	}

	/** Replies the numbers of projects per TRL for the given set of projects.
	 *
	 * @param projects the projects to analyze.
	 * @return rows with: type, count.
	 * @since 3.6
	 */
	@SuppressWarnings("static-method")
	public List<List<Object>> getNumberOfProjectsPerTRL(Collection<? extends Project> projects) {
		final Map<TRL, Integer> projectsPerTRL = projects.stream()
				.filter(it -> it.getTRL() != null && it.getStatus() == ProjectStatus.ACCEPTED)
				.collect(Collectors.toMap(
						it -> it.getTRL(),
						it -> Integer.valueOf(1),
						(a, b) -> {
							return Integer.valueOf(a.intValue() + b.intValue());
						}));
		return projectsPerTRL.entrySet().stream()
			.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
			.map(it -> {
				final List<Object> columns = new ArrayList<>(2);
				columns.add(it.getKey().name() + " - " + it.getKey().getLabel()); //$NON-NLS-1$
				columns.add(it.getValue());
				return columns;
			})
			.collect(Collectors.toList());
	}

	/** Replies the numbers of projects per research axis for the given set of projects.
	 *
	 * @param projects the projects to analyze.
	 * @return rows with: axis, count.
	 * @since 3.6
	 */
	public List<List<Object>> getNumberOfProjectsPerScientificAxis(Collection<? extends Project> projects) {
		final Map<ScientificAxis, Integer> projectsPerAxis = new HashMap<>();
		final AtomicInteger outsideAxis = new AtomicInteger(); 
		projects.stream()
				.forEach(it -> {
					if (!it.getScientificAxes().isEmpty()) {
						for (final ScientificAxis axis : it.getScientificAxes()) {
							final Integer oldValue = projectsPerAxis.get(axis);
							if (oldValue == null) {
								projectsPerAxis.put(axis, Integer.valueOf(1));
							} else {
								projectsPerAxis.put(axis, Integer.valueOf(oldValue.intValue() + 1));
							}
						}
					} else {
						outsideAxis.incrementAndGet();
					}
				});
		if (outsideAxis.intValue() > 0) {
			final ScientificAxis outAxis = new ScientificAxis();
			outAxis.setName(getMessage("projectService.outsideScientificAxis")); //$NON-NLS-1$
			projectsPerAxis.put(outAxis, Integer.valueOf(outsideAxis.get()));
		}
		return projectsPerAxis.entrySet().stream()
			.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
			.map(it -> {
				final List<Object> columns = new ArrayList<>(2);
				final String name = Strings.isNullOrEmpty(it.getKey().getAcronym()) ? it.getKey().getName()
						: it.getKey().getAcronym() + " - " + it.getKey().getName(); //$NON-NLS-1$
				columns.add(name);
				columns.add(it.getValue());
				return columns;
			})
			.collect(Collectors.toList());
	}

	/** Replies the numbers of projects per country for the given set of publications.
	 *
	 * @param projects the projects to analyze.
	 * @param referenceOrganization the organization for which the associated members are ignored in the counting.
	 * @return rows with: country name, count.
	 * @since 3.6
	 */
	@SuppressWarnings("static-method")
	public List<List<Object>> getNumberOfProjectsPerCountry(Collection<? extends Project> projects, ResearchOrganization referenceOrganization) {
		final AtomicInteger projectsWithUnknownCountry = new AtomicInteger();
		final int[] numbers = new int[CountryCode.values().length + 1];
		projects.stream()
				.filter(it -> it.getStatus() == ProjectStatus.ACCEPTED)
				.forEach(it -> {
					final Set<CountryCode> countries = new HashSet<>();
					boolean unknown = false;
					if (!getCountry(it.getCoordinator(), referenceOrganization, countries)) {
						unknown = true;
					}
					for (final ResearchOrganization member : it.getOtherPartners()) {
						if (!getCountry(member, referenceOrganization, countries)) {
							unknown = true;
						}							
					}
					for (final CountryCode country : countries) {
						++numbers[country.ordinal()];
					}
					if (unknown) {
						projectsWithUnknownCountry.incrementAndGet();
					}
				});
		numbers[numbers.length - 1] = projectsWithUnknownCountry.get();
		final CountryCode[] allCountries = CountryCode.values();
		final AtomicInteger index = new AtomicInteger();
		final IntFunction<List<Object>> converter = it -> {
			final List<Object> columns = new ArrayList<>(2);
			final int idx = index.getAndIncrement();
			if (idx < allCountries.length) {
				columns.add(CountryCodeUtils.getDisplayCountry(allCountries[idx]));
			} else {
				columns.add("?"); //$NON-NLS-1$
			}
			columns.add(Integer.valueOf(it));
			return columns;
		};
		return Arrays.stream(numbers)
			.mapToObj(converter)
			.filter(it -> ((Integer) it.get(1)).intValue() > 0)
			.sorted((a, b) -> - ((Integer) a.get(1)).compareTo((Integer) b.get(1)))
			.collect(Collectors.toList());
	}

	private static boolean getCountry(ResearchOrganization member, ResearchOrganization referenceOrganization,
			Set<CountryCode> countries) {
		if (member != null && member.getId() != referenceOrganization.getId()) {
			CountryCode code = member.getCountry();
			if (code == null) {
				return false;
			}
			countries.add(code);
		}
		return true;
	}

}
