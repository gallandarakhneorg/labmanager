package fr.utbm.ciad.wprest.projects;

import fr.utbm.ciad.labmanager.Constants;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.project.Project;
import fr.utbm.ciad.labmanager.data.project.ProjectMember;
import fr.utbm.ciad.labmanager.data.project.ProjectStatus;
import fr.utbm.ciad.labmanager.data.project.Role;
import fr.utbm.ciad.labmanager.services.project.ProjectService;
import fr.utbm.ciad.wprest.data.DateRange;
import fr.utbm.ciad.wprest.data.PersonOnWebsite;
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

    /**
     * Describes the information related to the page of a project
     * @param acronym - the acronym of the project
     * @param title - the title of the project
     * @param description - the description of the project
     * @param date - the date of the project (start and end)
     * @param organizations - the organizations of the project
     * @param participants - the participants of the project
     * @param images - the images URIs of the project
     * @param logo - the logo URI
     * @param links - the links of the project
     * @param webpageId - the webpageId (webpage URL)
     * @param openSource - true if the project is open source
     * @param isDone - true if the project is done
     */
    public record ProjectDataDto(String acronym,
                                 String title,
                                 String description,
                                 DateRange date,
                                 ProjectOrganizationData organizations,
                                 List<ProjectParticipantData> participants,
                                 List<String> images,
                                 String logo,
                                 ProjectLinksData links,
                                 String webpageId,
                                 boolean openSource,
                                 boolean isDone) {}


    /**
     * Describes the links associated to a project
     * @param projectUrl - the project url on the website
     * @param videoLinks - the list of videos linked to the project
     * @param partnersLinks - a map which maps each partner name to its url
     */
    public record ProjectLinksData(String projectUrl,
                                   List<String> videoLinks,
                                   Map<String, String> partnersLinks) {}

    /**
     * Describes the organization of a project
     * @param superOrganization - the super organization name
     * @param learOrganization - the lear organization name
     * @param localOrganization - the local organization name
     * @param partners - the list of partners names
     */
    public record ProjectOrganizationData(String superOrganization,
                                          String learOrganization,
                                          String localOrganization,
                                          List<String> partners) {}

    /**
     * Describes a participant of a project
     * @param person - the person
     * @param role - the role of the person in the project
     */
    public record ProjectParticipantData(PersonOnWebsite person,
                                         Role role) {}
}
