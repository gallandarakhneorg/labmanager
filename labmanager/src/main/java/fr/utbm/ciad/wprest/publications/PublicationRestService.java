package fr.utbm.ciad.wprest.publications;

import fr.utbm.ciad.labmanager.Constants;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.data.publication.PublicationLanguage;
import fr.utbm.ciad.labmanager.data.publication.PublicationType;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.organization.ResearchOrganizationService;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.wprest.data.PersonOnWebsite;
import fr.utbm.ciad.wprest.publications.data.dto.PublicationsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing publication-related operations.
 *
 * <p>This controller provides endpoints for accessing and manipulating
 * publication-related data, including retrieving information about the publications for a person or an organization</p>
 *
 * <p>Base URL: /api/v{majorVersion}/publications</p>
 *
 *
 * <p>This class is annotated with {@link RestController} and handles
 * HTTP requests mapped to the /api/v{majorVersion}/publications endpoint.
 * The version of the API is determined by the constant
 * {@link Constants#MANAGER_MAJOR_VERSION}.</p>
 */
@RestController
@RequestMapping("/api/v" + Constants.MANAGER_MAJOR_VERSION + "/publications")
public class PublicationRestService {

    PersonService personService;
    PublicationService publicationService;
    ResearchOrganizationService researchOrganizationService;

    public PublicationRestService(@Autowired PersonService personService,
                                  @Autowired PublicationService publicationService,
                                  @Autowired ResearchOrganizationService researchOrganizationService
    ) {
        this.personService = personService;
        this.publicationService = publicationService;
        this.researchOrganizationService = researchOrganizationService;
    }


    /**
     * Retrieves a list of publications for a specified person, allowing for optional filtering by year, language, and keywords.
     *
     * <p>This endpoint allows clients to filter the publications based on the specified parameters. If the optional parameters
     * are not provided, all publications for the specified person will be returned.</p>
     *
     * @param id the ID of the user or either
     * @param pageId the webpage_id of the user
     * @param year     (optional) the year of publication to filter results. If not provided, all years will be included
     * @param language (optional) the language of publication to filter results. If not provided, all languages will be included
     * @param keywords (optional) a comma-separated list of keywords to filter results. If not provided, all keywords will be included
     * @return a ResponseEntity containing a list of PublicationsDTO objects that match the specified filters, or an appropriate error response
     * if the person is not found or no publications match the criteria.
     */
    @GetMapping("/persons")
    @Transactional
    public ResponseEntity<List<PublicationsDTO>> getPersonPublications(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String pageId,
            @RequestParam(required = false) Long year,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String keywords
    ) {
        if ((id == null && pageId == null) || (id != null && pageId != null)) {
            return ResponseEntity.badRequest().build();
        }

        Person p = null;
        if (id != null) {
            p = personService.getPersonById(id);
        }
        else if (pageId != null) {
            p = personService.getPersonByWebPageId(pageId);
        }

        if (p == null) {
            return ResponseEntity.notFound().build();
        }

        List<Publication> publicationList = new ArrayList<>(publicationService.getPublicationsByPersonId(p.getId()));
        List<PublicationsDTO> publications = getAndFilterPublicationsDataFrom(publicationList, year, language, keywords);

        return ResponseEntity.ok(publications);
    }

    /**
     * Retrieves a list of publications for a specified organization, allowing for optional filtering by year, language, and keywords.
     *
     * <p>This endpoint allows clients to filter the publications based on the specified parameters. If the optional parameters
     * are not provided, all publications for the specified organization will be returned.</p>
     *
     * @param id the ID of the user or either
     * @param acronym the acronym of the organization
     * @param year     (optional) the year of publication to filter results. If not provided, all years will be included
     * @param language (optional) the language of publication to filter results. If not provided, all languages will be included
     * @param keywords (optional) a comma-separated list of keywords to filter results. If not provided, all keywords will be included
     * @return a ResponseEntity containing a list of PublicationsDTO objects that match the specified filters, or an appropriate error response
     * if the organization is not found or no publications match the criteria.
     */
    @GetMapping("/organizations")
    @Transactional
    public ResponseEntity<List<PublicationsDTO>> getOrganizationsPublications(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String acronym,
            @RequestParam(required = false) Long year,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String keywords,
            @RequestParam(required = false, defaultValue = "false") Boolean subOrganizations,
            @RequestParam(required = false, defaultValue = "false") Boolean filterActive
    ) {

        if ((id == null && acronym == null) || (id != null && acronym != null)) {
            return ResponseEntity.badRequest().build();
        }

        Optional<ResearchOrganization> optionalResearchOrganization = Optional.empty();

        if (id != null) {
            optionalResearchOrganization = researchOrganizationService.getResearchOrganizationById(id);
        } else if (acronym != null) {
            optionalResearchOrganization =  researchOrganizationService.getResearchOrganizationByAcronym(acronym);
        }

        if (optionalResearchOrganization.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ResearchOrganization organization = optionalResearchOrganization.get();

        Set<Publication> publications = new HashSet<>(publicationService.getPublicationsByOrganizationId(organization.getId(), subOrganizations, filterActive));
        List<Publication> publicationsList = new ArrayList<>(publications);
        publicationsList.addAll(publications);

        List<PublicationsDTO> publicationsDTOList = getAndFilterPublicationsDataFrom(publicationsList, year, language, keywords);

        return ResponseEntity.ok(publicationsDTOList);
    }

    /**
     * Filters a list of publications with the provided filters
     *
     * @param publicationList - the list of publication to filter
     * @param year - the year to filter
     * @param language - the language to filter
     * @param keywords - the list of keywords to filter
     * @return The filtered publications
     */
    @Transactional
    public List<PublicationsDTO> getAndFilterPublicationsDataFrom(List<Publication> publicationList,
                                                                  Long year,
                                                                  String language,
                                                                  String keywords) {
        List<PublicationsDTO> publications = new ArrayList<>();

        for (Publication publication : publicationList) {

            // Filter by year
            if (year != null && (publication.getPublicationDate() == null || year.intValue() != publication.getPublicationYear())) {
                continue;
            }

            // Filter by language spoken
            if (language != null) {
                PublicationLanguage spokenLanguage = publication.getMajorLanguage();
                if (!spokenLanguage.equals(PublicationLanguage.valueOfCaseInsensitive(language, PublicationLanguage.OTHER))) {
                    continue;
                }
            }

            // Filter by keywords (OR operator)
            if (keywords != null && publication.getKeywords() != null) {
                String[] splitKeywords = keywords.split("[ ,;]");
                boolean found = false;

                for (String keyword : splitKeywords) {
                    if (publication.getKeywords().toUpperCase(Locale.ROOT).contains(keyword.toUpperCase(Locale.ROOT))) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    continue;
                }
            }

            // get keywords as list
            List<String> containedKeywordsList = new ArrayList<>();;
            if (publication.getKeywords() != null) {
                containedKeywordsList = Arrays.asList(publication.getKeywords().split("[,;]"));
            }


            //get the list of authors name and webpageId
            List<Person> authorsList = new ArrayList<>(publication.getAuthors());
            List<PersonOnWebsite> authorsPersons = authorsList.stream()
                    .map(author -> new PersonOnWebsite(author.getFullName(), author.getWebPageId()))
                    .collect(Collectors.toList());


            String title = publication.getTitle();
            LocalDate publicationDate = publication.getPublicationDate();
            PublicationType publicationType = publication.getType();
            String abstractText = publication.getAbstractText();
            String pdfUrl = publication.getPathToDownloadablePDF();
            String doi = publication.getDOI();
            String issn = publication.getISSN();

            publications.add(new PublicationsDTO(title, doi, issn, publicationDate, publicationType, authorsPersons, abstractText, pdfUrl,
                    publication.getMajorLanguage(), containedKeywordsList));
        }

        return publications;
    }
}
