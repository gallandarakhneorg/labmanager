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

package fr.utbm.ciad.labmanager.services.project;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.member.PersonRepository;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganizationRepository;
import fr.utbm.ciad.labmanager.data.project.Project;
import fr.utbm.ciad.labmanager.data.project.ProjectActivityType;
import fr.utbm.ciad.labmanager.data.project.ProjectBudget;
import fr.utbm.ciad.labmanager.data.project.ProjectCategory;
import fr.utbm.ciad.labmanager.data.project.ProjectContractType;
import fr.utbm.ciad.labmanager.data.project.ProjectMember;
import fr.utbm.ciad.labmanager.data.project.ProjectMemberRepository;
import fr.utbm.ciad.labmanager.data.project.ProjectRepository;
import fr.utbm.ciad.labmanager.data.project.ProjectStatus;
import fr.utbm.ciad.labmanager.data.project.ProjectWebPageNaming;
import fr.utbm.ciad.labmanager.data.project.Role;
import fr.utbm.ciad.labmanager.data.scientificaxis.ScientificAxis;
import fr.utbm.ciad.labmanager.services.AbstractService;
import fr.utbm.ciad.labmanager.services.member.MembershipService;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import fr.utbm.ciad.labmanager.utils.funding.FundingScheme;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.utils.io.json.JsonUtils;
import fr.utbm.ciad.labmanager.utils.trl.TRL;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.util.MultiCollection;
import org.arakhne.afc.vmutil.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

	/** Replies the list of all the projects from the database.
	 *
	 * @param filter the filter of projects.
	 * @return the list of projects, never {@code null}.
	 * @since 4.0
	 */
	public List<Project> getAllProjects(Specification<Project> filter) {
		return this.projectRepository.findAll(filter);
	}

	/** Replies the list of all the projects from the database.
	 *
	 * @param filter the filter of projects.
	 * @param sortOrder the order specification to use for sorting the projects.
	 * @return the list of projects, never {@code null}.
	 * @since 4.0
	 */
	public List<Project> getAllProjects(Specification<Project> filter, Sort sortOrder) {
		return this.projectRepository.findAll(filter, sortOrder);
	}

	/** Replies the list of all the projects from the database.
	 *
	 * @param sortOrder the order specification to use for sorting the projects.
	 * @return the list of projects, never {@code null}.
	 * @since 4.0
	 */
	public List<Project> getAllProjects(Sort sortOrder) {
		return this.projectRepository.findAll(sortOrder);
	}

	/** Replies the list of all the projects from the database.
	 *
	 * @param pageable the manager of pages.
	 * @return the list of projects, never {@code null}.
	 * @since 4.0
	 */
	public Page<Project> getAllProjects(Pageable pageable) {
		return this.projectRepository.findAll(pageable);
	}

	/** Replies the list of all the projects from the database.
	 *
	 * @param pageable the manager of pages.
	 * @param filter the filter of projects.
	 * @return the list of projects, never {@code null}.
	 * @since 4.0
	 */
	public Page<Project> getAllProjects(Pageable pageable, Specification<Project> filter) {
		return this.projectRepository.findAll(filter, pageable);
	}

	/** Replies the list of all the public projects from the database.
	 * A public project is not confidential and has status "accepted".
	 *
	 * @return the list of public projects, never {@code null}.
	 */
	public List<Project> getAllPublicProjects() {
		return this.projectRepository.findDistinctByConfidentialAndStatus(Boolean.FALSE, ProjectStatus.ACCEPTED);
	}

	/** Replies the list of all the public projects from the database.
	 * A public project is not confidential and has status "accepted".
	 *
	 * @param pageable the manager of pages.
	 * @return the list of public projects, never {@code null}.
	 * @since 4.0
	 */
	public Page<Project> getAllPublicProjects(Pageable pageable) {
		return this.projectRepository.findAll(PublicProjectSpecification.SINGLETON, pageable);
	}

	/** Replies the list of all the public projects from the database.
	 * A public project is not confidential and has status "accepted".
	 *
	 * @param pageable the manager of pages.
	 * @param filter the filter of projects.
	 * @return the list of public projects, never {@code null}.
	 * @since 4.0
	 */
	public Page<Project> getAllPublicProjects(Pageable pageable, Specification<Project> filter) {
		return this.projectRepository.findAll(PublicProjectSpecification.SINGLETON.and(filter), pageable);
	}

	/** Replies the project with the given identifier.
	 *
	 * @param id the identifier of the project in the database.
	 * @return the project or {@code null} if there is no project with the given id.
	 */
	public Project getProjectById(long id) {
		final var projectOpt = this.projectRepository.findById(Long.valueOf(id));
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
	public List<Project> getProjectsByOrganizationId(long organizationId) {
		final var idObj = Long.valueOf(organizationId);
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
	public List<Project> getPublicProjectsByOrganizationId(long organizationId) {
		final var idObj = Long.valueOf(organizationId);
		return this.projectRepository.findDistinctOrganizationProjects(Boolean.FALSE, ProjectStatus.ACCEPTED, idObj);
	}

	/** Replies the projects that are associated to the person with the given identifier.
	 *
	 * @param id the identifier of the person.
	 * @return the projects.
	 */
	public Set<Project> getProjectsByPersonId(long id) {
		return this.projectRepository.findDistinctPersonProjects(Long.valueOf(id));
	}

	/** Replies public the projects that are associated to the person with the given identifier.
	 * A public project is not confidential and has status "accepted".
	 *
	 * @param id the identifier of the person.
	 * @return the public projects.
	 */
	public List<Project> getPublicProjectsByPersonId(long id) {
		return this.projectRepository.findDistinctPersonProjects(
				Boolean.FALSE, ProjectStatus.ACCEPTED, Long.valueOf(id));
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
			ProjectActivityType activityType, TRL trl, long coordinator, long localOrganization,
			long superOrganization, long learOrganization, List<Long> otherPartners,
			Map<Long, Role> participants, MultipartFile pathToScientificRequirements, boolean removePathToScientificRequirements,
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
		for (final var budget : localOrganizationBudgets) {
			if (budget.getFundingScheme() == null || budget.getBudget() < 0f) {
				throw new IllegalArgumentException("Funding and budget for the local organization is mandatory"); //$NON-NLS-1$
			}
		}
		project.setBudgets(localOrganizationBudgets);
		this.projectRepository.save(project);

		// Link the organizations

		final var coordinatorOrg = this.organizationRepository.findById(Long.valueOf(coordinator));
		if (coordinatorOrg.isEmpty()) {
			throw new IllegalArgumentException("Coordinator organization not found with id " + coordinator); //$NON-NLS-1$
		}
		project.setCoordinator(coordinatorOrg.get());

		final Optional<ResearchOrganization> localOrganizationOrg;
		if (coordinator == localOrganization && coordinatorOrg.get() != null) {
			localOrganizationOrg = coordinatorOrg;
		} else {
			localOrganizationOrg = this.organizationRepository.findById(Long.valueOf(localOrganization));
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
				superOrganizationOrg = this.organizationRepository.findById(Long.valueOf(superOrganization));
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
			learOrganizationOrg = this.organizationRepository.findById(Long.valueOf(learOrganization));
		}
		if (learOrganizationOrg.isEmpty()) {
			throw new IllegalArgumentException("LEAR organization not found with id " + learOrganization); //$NON-NLS-1$
		}
		project.setLearOrganization(learOrganizationOrg.get());
		
		final var otherPartnersOrg = new HashSet<ResearchOrganization>();
		if (otherPartners != null && !otherPartners.isEmpty()) {
			otherPartners.stream().forEach(it -> {
				final var orga = this.organizationRepository.findById(it);
				if (orga.isPresent()) {
					otherPartnersOrg.add(orga.get());
				}
			});
		}
		project.setOtherPartners(otherPartnersOrg);

		this.projectRepository.save(project);

		// Link the participants

		final var projectParticipants = new ArrayList<ProjectMember>();
		if (participants != null && !participants.isEmpty()) {
			participants.entrySet().stream().forEach(it -> {
				final var personId = it.getKey();
				final var person = this.personRepository.findById(personId);
				if (person.isEmpty()) {
					throw new IllegalStateException("Person not found with id " + personId); //$NON-NLS-1$
				}
				final var role = it.getValue();
				final var member = new ProjectMember();
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
		var changed = false;
		if (explicitRemove) {
			var i = 0;
			for (final var imagePath : project.getPathsToImages()) {
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
			final var paths = new ArrayList<String>(uploadedFiles.length);
			final var i = new MutableInt(0);
			for (; i.intValue() < uploadedFiles.length; i.increment()) {
				final var uploadedFile = uploadedFiles[i.intValue()];
				final String ext;
				if (uploadedFile != null) {
					ext = FileSystem.extension(uploadedFile.getOriginalFilename());
				} else {
					final var filename = project.getPathToLogo();
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
			final var filename = project.getPathToPowerpoint();
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
			final var filename = project.getPathToLogo();
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
			ProjectContractType contractType, ProjectActivityType activityType, TRL trl, long coordinator, long localOrganization,
			long superOrganization, long learOrganization, List<Long> otherPartners, Map<Long, Role> participants,
			MultipartFile pathToScientificRequirements, boolean removePathToScientificRequirements,
			boolean confidential, MultipartFile[] pathsToImages, boolean removePathsToImages,
			List<String> videoURLs, MultipartFile pathToPowerpoint, boolean removePathToPowerpoint,
			MultipartFile pathToPressDocument, boolean removePathToPressDocument, ProjectStatus status,
			List<ProjectBudget> localOrganizationBudgets, List<ScientificAxis> axes) throws IOException {
		final var project = new Project();
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
	public Optional<Project> updateProject(long projectId,
			boolean validated, String acronym, String scientificTitle,
			boolean openSource, LocalDate startDate, int duration, String description,
			MultipartFile pathToLogo, boolean removePathToLogo, URL projectURL,
			ProjectWebPageNaming webPageNaming, float globalBudget,
			ProjectContractType contractType, ProjectActivityType activityType, TRL trl, long coordinator, long localOrganization,
			long superOrganization, long learOrganization, List<Long> otherPartners, Map<Long, Role> participants, 
			MultipartFile pathToScientificRequirements, boolean removePathToScientificRequirements,
			boolean confidential, MultipartFile[] pathsToImages, boolean removePathsToImages,
			List<String> videoURLs, MultipartFile pathToPowerpoint, boolean removePathToPowerpoint,
			MultipartFile pathToPressDocument, boolean removePathToPressDocument, ProjectStatus status,
			List<ProjectBudget> localOrganizationBudgets, List<ScientificAxis> axes) throws IOException {
		final Optional<Project> res;
		if (projectId >= 0) {
			res = this.projectRepository.findById(Long.valueOf(projectId));
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
	public void removeProject(long identifier, boolean removeAssociatedFiles) {
		final var id = Long.valueOf(identifier);
		final var projectOpt = this.projectRepository.findById(id);
		if (projectOpt.isPresent()) {
			final var project = projectOpt.get();
			//
			final var pathToLogo = project.getPathToLogo();
			final var pathToPowerpoint = project.getPathToPowerpoint();
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
						final var ext = FileSystem.extension(pathToLogo);
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
						final var ext = FileSystem.extension(pathToPowerpoint);
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
		final var sdt = project.getStartDate();
		final var edt = project.getEndDate();
		if (sdt != null && edt != null) {
			return project.getParticipants().stream()
					.filter(it -> it.getPerson() != null)
					.map(it -> {
						final var mbrs = this.membershipService.getMembershipsForPerson(it.getPerson().getId()).stream()
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
	public List<Project> getProjectsByIds(List<Long> identifiers) {
		final var projects = this.projectRepository.findAllById(identifiers);
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
							final var multi = new MultiCollection<Project>();
							multi.addCollection(a);
							multi.addCollection(b);
							return multi;
						}));
		return projectsPerYear.entrySet().stream()
			.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
			.map(it -> {
				var nbAap = 0;
				var NbIndus = 0;
				var nbAutoFunded = 0;
				var budget = 0f;
				for (final var prj : it.getValue()) {
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
				final var columns = new ArrayList<Number>(3);
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
				for (var y = it.getStartYear(); y <= it.getEndYear(); ++y) {
					final var yearProjects = projectsPerYear.computeIfAbsent(Integer.valueOf(y), it0 -> new ArrayList<>());
					yearProjects.add(it);
				}
			});
		return projectsPerYear.entrySet().stream()
			.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
			.map(it -> {
				var aap = 0;
				var indus = 0;
				var autoFunded = 0;
				var budget = 0f;
				for (final var prj : it.getValue()) {
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
				final var columns = new ArrayList<Number>(3);
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
	 * @param locale the  locale to use.
	 * @return rows with: type, count.
	 * @since 3.6
	 */
	public List<List<Object>> getNumberOfProjectsPerType(Collection<? extends Project> projects, Locale locale) {
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
				columns.add(it.getKey().getLabel(getMessageSourceAccessor(), locale));
				columns.add(it.getValue());
				return columns;
			})
			.collect(Collectors.toList());
	}

	/** Replies the numbers of academic projects per funding scheme for the given set of projects.
	 *
	 * @param projects the projects to analyze.
	 * @param locale the locale to use.
	 * @return rows with: type, count.
	 * @since 3.6
	 */
	public List<List<Object>> getNumberOfAcademicProjectsPerType(Collection<? extends Project> projects, Locale locale) {
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
				columns.add(it.getKey().getLabel(getMessageSourceAccessor(), locale));
				columns.add(it.getValue());
				return columns;
			})
			.collect(Collectors.toList());
	}

	/** Replies the numbers of not-academic projects per funding scheme for the given set of projects.
	 *
	 * @param projects the projects to analyze.
	 * @param locale the locale to use.
	 * @return rows with: type, count.
	 * @since 3.6
	 */
	public List<List<Object>> getNumberOfNotAcademicProjectsPerType(Collection<? extends Project> projects, Locale locale) {
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
				columns.add(it.getKey().getLabel(getMessageSourceAccessor(), locale));
				columns.add(it.getValue());
				return columns;
			})
			.collect(Collectors.toList());
	}

	/** Replies the numbers of projects per activity type for the given set of projects.
	 *
	 * @param projects the projects to analyze.
	 * @param locale the locale to use.
	 * @return rows with: type, count.
	 * @since 3.6
	 */
	public List<List<Object>> getNumberOfProjectsPerActivityType(Collection<? extends Project> projects, Locale locale) {
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
				columns.add(it.getKey().getLabel(getMessageSourceAccessor(), locale));
				columns.add(it.getValue());
				return columns;
			})
			.collect(Collectors.toList());
	}

	/** Replies the numbers of projects per TRL for the given set of projects.
	 *
	 * @param projects the projects to analyze.
	 * @param locale the locale to use.
	 * @return rows with: type, count.
	 * @since 3.6
	 */
	public List<List<Object>> getNumberOfProjectsPerTRL(Collection<? extends Project> projects, Locale locale) {
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
				columns.add(it.getKey().name() + " - " + it.getKey().getLabel(getMessageSourceAccessor(), locale)); //$NON-NLS-1$
				columns.add(it.getValue());
				return columns;
			})
			.collect(Collectors.toList());
	}

	/** Replies the numbers of projects per research axis for the given set of projects.
	 *
	 * @param projects the projects to analyze.
	 * @param locale the locale to use.
	 * @return rows with: axis, count.
	 * @since 3.6
	 */
	public List<List<Object>> getNumberOfProjectsPerScientificAxis(Collection<? extends Project> projects, Locale locale) {
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
			outAxis.setName(getMessage(locale, "projectService.outsideScientificAxis")); //$NON-NLS-1$
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
	public List<List<Object>> getNumberOfProjectsPerCountry(Collection<? extends Project> projects, ResearchOrganization referenceOrganization) {
		final var projectsPerCountry = new TreeMap<CountryCode, Integer>((a, b) -> {
			if (a == b) {
				return 0;
			}
			if (a == null) {
				return Integer.MIN_VALUE;
			}
			if (b == null) {
				return Integer.MAX_VALUE;
			}
			return a.compareTo(b);
		});
		final var anonymousProjects = new TreeMap<Long, List<Map<String, Object>>>();
		projects.stream()
			.filter(it -> it.getStatus() == ProjectStatus.ACCEPTED)
			.forEach(it -> {
				final Set<CountryCode> countriesForProject = new TreeSet<>();
				if (!getCountry(it.getCoordinator(), referenceOrganization, countriesForProject)) {
					addAnonymousProject(anonymousProjects, it, it.getCoordinator());
				}
				for (final ResearchOrganization member : it.getOtherPartners()) {
					if (!getCountry(member, referenceOrganization, countriesForProject)) {
						addAnonymousProject(anonymousProjects, it, it.getCoordinator());
					}
				}
				for (final CountryCode country : countriesForProject) {
					final Integer value = projectsPerCountry.get(country);
					if (value == null) {
						projectsPerCountry.put(country, Integer.valueOf(1));
					} else {
						projectsPerCountry.put(country, Integer.valueOf(value.intValue() + 1));
					}
				}
			});
		if (!anonymousProjects.isEmpty()) {
			try {
				final ObjectMapper jsonMapper = JsonUtils.createMapper();
				getLogger().info("Project with country-less participant: " + jsonMapper.writeValueAsString(anonymousProjects)); //$NON-NLS-1$
			} catch (JsonProcessingException ex) {
				getLogger().error(ex.getLocalizedMessage(), ex);
			}
			projectsPerCountry.put(null, Integer.valueOf(anonymousProjects.size()));
		}
		return projectsPerCountry.entrySet().stream()
				.map(it -> {
					final List<Object> columns = new ArrayList<>(2);
					final CountryCode cc = it.getKey();
					if (cc == null) {
						columns.add("?"); //$NON-NLS-1$
					} else {
						columns.add(cc.getDisplayCountry());
					}
					columns.add(it.getValue());
					return columns;
				})
				// Sorted by quantity and country code
				.sorted((a, b) -> {
					final Integer na = (Integer) a.get(1);
					final Integer nb = (Integer) b.get(1);
					int cmp = nb.compareTo(na);
					if (cmp != 0) {
						return cmp;
					}
					final String la = (String) a.get(0);
					final String lb = (String) b.get(0);
					return la.compareToIgnoreCase(lb);
				})
				.collect(Collectors.toList());
	}

	private static void addAnonymousProject(Map<Long, List<Map<String, Object>>> anonymousProjects, Project project, ResearchOrganization organization) {
		final List<Map<String, Object>> invalidOrgas = anonymousProjects.computeIfAbsent(Long.valueOf(project.getId()), it -> new ArrayList<>());
		final Map<String, Object> orgaDesc = new HashMap<>();
		orgaDesc.put("id", Long.valueOf(organization.getId())); //$NON-NLS-1$
		orgaDesc.put("name", organization.getName()); //$NON-NLS-1$
		invalidOrgas.add(orgaDesc);
	}

	private static boolean getCountry(ResearchOrganization member, ResearchOrganization referenceOrganization,
			Set<CountryCode> countries) {
		if (member != null && member.getId() != referenceOrganization.getId()) {
			CountryCode code = member.getCountry();
			if (code == null) {
				return false;
			}
			countries.add(code.normalize());
		}
		return true;
	}

	/** Replies if the given identifier is for a person who is involved in a project.
	 *
	 * @param id the identifier of the person.
	 * @return {@code true} if the person is participating to a project.
	 * @since 3.6
	 */
	public boolean isInvolved(long id) {
		return !this.projectRepository.findDistinctPersonProjects(Long.valueOf(id)).isEmpty();
	}

	/** Replies the project with the given acronym or name.
	 *
	 * @param acronymOrName the acronym or the name of the project.
	 * @return the project.
	 * @since 3.6
	 */
	public Optional<Project> getProjectByAcronymOrName(String acronymOrName) {
		return this.projectRepository.findDistinctByAcronymOrScientificTitle(acronymOrName, acronymOrName);
	}

	/** Replies the public project with the given acronym or name.
	 * A public project is not confidential and has status "accepted".
	 *
	 * @param acronymOrName the acronym or the name of the project.
	 * @return the project.
	 * @since 3.6
	 */
	public Optional<Project> getPublicProjectByAcronymOrName(String acronymOrName) {
		final Optional<Project> project = this.projectRepository.findDistinctByAcronymOrScientificTitle(acronymOrName, acronymOrName);
		if (project.isPresent()) {
			final Project prj = project.get();
			if (prj.getStatus() == ProjectStatus.ACCEPTED && !prj.isConfidential()) {
				return project;
			}
		}
		return Optional.empty();
	}

	/** Specification that i validating public project.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	public static class PublicProjectSpecification implements Specification<Project> {

		/** Singleton for this criteria.
		 */
		public static final PublicProjectSpecification SINGLETON = new PublicProjectSpecification();
		
		private static final long serialVersionUID = 7747763215609636462L;

		@Override
		public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			final Predicate p1 = criteriaBuilder.equal(root.get("confidential"), Boolean.FALSE); //$NON-NLS-1$
			final Predicate p2 = criteriaBuilder.equal(root.get("status"), ProjectStatus.ACCEPTED); //$NON-NLS-1$
			return criteriaBuilder.and(p1, p2);
		}

	}

}
