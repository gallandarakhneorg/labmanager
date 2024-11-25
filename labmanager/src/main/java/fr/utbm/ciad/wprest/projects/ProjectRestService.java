package fr.utbm.ciad.wprest.projects;

import fr.utbm.ciad.labmanager.Constants;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.project.Project;
import fr.utbm.ciad.labmanager.data.project.ProjectMember;
import fr.utbm.ciad.labmanager.data.project.ProjectStatus;
import fr.utbm.ciad.labmanager.services.project.ProjectService;
import fr.utbm.ciad.wprest.data.DateRange;
import fr.utbm.ciad.wprest.data.PersonOnWebsite;
import fr.utbm.ciad.wprest.projects.data.dto.ProjectDataDto;
import fr.utbm.ciad.wprest.projects.data.ProjectLinksData;
import fr.utbm.ciad.wprest.projects.data.ProjectOrganizationData;
import fr.utbm.ciad.wprest.projects.data.ProjectParticipantData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

/**
 * REST controller for managing project-related operations.
 *
 * <p>This controller provides endpoints for accessing and manipulating
 * project-related data, including retrieving information about organizations,
 * participants and links related to a project.</p>
 *
 * <p>Base URL: /api/v{majorVersion}/projects</p>
 *
 * <p>This class is annotated with {@link RestController} and handles
 * HTTP requests mapped to the /api/v{majorVersion}/persons endpoint.
 * The version of the API is determined by the constant
 * {@link Constants#MANAGER_MAJOR_VERSION}.</p>
 */
@RestController
@RequestMapping("/api/v" + Constants.MANAGER_MAJOR_VERSION + "/projects")
public class ProjectRestService {

    ProjectService projectService;

    public ProjectRestService(@Autowired ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Get the project with the given id
     * @param id - the id of the project
     * @return - the data project data
     */
    @GetMapping
    @Transactional
    public ResponseEntity<ProjectDataDto> getProject(
            @RequestParam Long id
    ) {
        Project p = projectService.getProjectById(id);

        if (p == null) {
            return ResponseEntity.notFound().build();
        }

        // cannot fetch a project if it is not public.
        if (p.isConfidential() || p.getStatus() != ProjectStatus.ACCEPTED) {
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(getProjectData(p));
    }

    /**
     * Get all public projects
     * @return the whole list of projects
     */
    @Transactional
    @GetMapping("/all")
    public ResponseEntity<List<ProjectDataDto>> getAllProjects(

    ) {
        List<Project> projects = new ArrayList<>(projectService.getAllProjects());
        return getPublicProjectsFromList(projects);
    }


    /**
     * Get all public projects data from a given list
     * @param projects - the list of projects to extract data from
     * @return - a list of the data associated to each project
     */
    public ResponseEntity<List<ProjectDataDto>> getPublicProjectsFromList(List<Project> projects) {
        List<ProjectDataDto> projectsDtos = new ArrayList<>();

        for (Project project : projects) {
            if (!project.isConfidential() && (project.getStatus() == ProjectStatus.ACCEPTED)) {
                projectsDtos.add(getProjectData(project));
            }
        }

        return ResponseEntity.ok(projectsDtos);
    }

    /**
     * Get the different organizations names
     * @param p - the project
     * @return - the organization data
     */
    private ProjectOrganizationData getProjectOrganizationData(Project p) {
        ResearchOrganization superOrganization = p.getSuperOrganization();
        String superOrganizationName = superOrganization.getName();
        String learOrganizationName = p.getLearOrganization().getName();
        String localOrganizationName = p.getLocalOrganization().getName();

        List<ResearchOrganization> partners = new ArrayList<>(p.getOtherPartners());
        List<String> partnersNames = new ArrayList<>();
        partners.forEach(partner -> partnersNames.add(partner.getName()));

        return new ProjectOrganizationData(superOrganizationName, learOrganizationName, localOrganizationName, partnersNames);
    }


    /**
     * Gets the information related to the page of a project
     * @param p - the project to get information from.
     * @return - the project information DTO
     */
    private ProjectDataDto getProjectData(Project p) {
        String acronym = p.getAcronym();
        String title = p.getScientificTitle();
        String description = p.getDescription();

        LocalDate startDate = p.getStartDate();
        LocalDate endDate = p.getEndDate();
        DateRange date = new DateRange(startDate, endDate);

        String projectURL = p.getProjectURL();
        List<String> videosURL = new ArrayList<>(p.getVideoURLs());

        List<ResearchOrganization> partners = new ArrayList<>(p.getOtherPartners());
        Map<String, String> partnersLinks = new HashMap<>();
        partners.forEach(partner -> partnersLinks.put(partner.getName(), partner.getOrganizationURL()));

        ProjectLinksData links = new ProjectLinksData(projectURL, videosURL, partnersLinks);

        ProjectOrganizationData organizationData = getProjectOrganizationData(p);

        boolean openSource = p.isOpenSource();

        boolean isDone = endDate != null && (LocalDate.now().isAfter(endDate));

        List<ProjectMember> participants = new ArrayList<>(p.getParticipants());
        List<ProjectParticipantData> participantsData = new ArrayList<>();

        List<String> images = new ArrayList<>(p.getPathsToImages());
        String logo = p.getPathToLogo();

        String webpageId = null;

        if (p.getWebPageURI() != null) {
            webpageId = p.getWebPageURI().toString();
        }

        for (ProjectMember participant : participants) {
            String name = participant.getPerson().getFullName();
            String id = participant.getPerson().getWebPageId();
            PersonOnWebsite person = new PersonOnWebsite(name, id);

            participantsData.add(new ProjectParticipantData(person, participant.getRole()));
        }

        return new ProjectDataDto(acronym, title, description, date, organizationData, participantsData, images, logo, links, webpageId, openSource, isDone);
    }
}
