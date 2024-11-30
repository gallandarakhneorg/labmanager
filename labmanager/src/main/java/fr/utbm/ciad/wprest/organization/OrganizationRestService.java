package fr.utbm.ciad.wprest.organization;

import fr.utbm.ciad.labmanager.Constants;
import fr.utbm.ciad.labmanager.components.indicators.Indicator;
import fr.utbm.ciad.labmanager.data.member.MemberStatus;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.member.Responsibility;
import fr.utbm.ciad.labmanager.data.organization.OrganizationAddress;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.services.indicator.GlobalIndicatorsService;
import fr.utbm.ciad.labmanager.services.organization.OrganizationAddressService;
import fr.utbm.ciad.labmanager.services.organization.ResearchOrganizationService;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.wprest.data.PersonOnWebsite;
import fr.utbm.ciad.wprest.organization.data.OrganizationMemberData;
import fr.utbm.ciad.wprest.organization.data.dto.OrganizationAddressDTO;
import fr.utbm.ciad.wprest.organization.data.dto.OrganizationMembersDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * REST controller for managing organization-related operations.
 *
 * <p>This controller provides endpoints for accessing and manipulating
 * organization-related data, including retrieving information about the members</p>
 *
 * <p>Base URL: /api/v{majorVersion}/organizations</p>
 *
 * <p>Use the organization ID or acronym to request data for an organization.</p>
 *
 * <p>This class is annotated with {@link RestController} and handles
 * HTTP requests mapped to the /api/v{majorVersion}/organizations endpoint.
 * The version of the API is determined by the constant
 * {@link Constants#MANAGER_MAJOR_VERSION}.</p>
 */
@RestController
@RequestMapping("/api/v" + Constants.MANAGER_MAJOR_VERSION + "/organizations")
public class OrganizationRestService {

    OrganizationAddressService organizationAddressService;
    ResearchOrganizationService researchOrganizationService;
    PublicationService publicationService;
    GlobalIndicatorsService globalIndicatorsService;

    public OrganizationRestService(@Autowired OrganizationAddressService organizationAddressService,
                                   @Autowired ResearchOrganizationService researchOrganizationService,
                                   @Autowired PublicationService publicationService,
                                   @Autowired GlobalIndicatorsService globalIndicatorsService) {
        this.organizationAddressService = organizationAddressService;
        this.researchOrganizationService = researchOrganizationService;
        this.publicationService = publicationService;
        this.globalIndicatorsService = globalIndicatorsService;
    }

    /**
     * Retrieves all organization members.
     *
     * @return a list of {@link OrganizationMembersDTO} containing all organization members
     */
    @Operation(summary = "Retrieves all organization members", description = "Fetches all members from every organization.", tags = {"Organization API"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all organization members", content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrganizationMembersDTO.class)))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Transactional
    @GetMapping("/members/all")
    public ResponseEntity<List<OrganizationMembersDTO>> getAllMembers() {
        List<OrganizationMembersDTO> oraganizationMembers = new ArrayList<>();

        List<ResearchOrganization> organizations = new ArrayList<>(researchOrganizationService.getAllResearchOrganizations());
        organizations.forEach(organization -> oraganizationMembers.add(getOrganizationMembersData(organization)));

        return ResponseEntity.ok(oraganizationMembers);
    }

    /**
     * Retrieves members of a specific organization by either its ID or acronym.
     *
     * @param id      the ID of the organization (optional)
     * @param acronym the acronym of the organization (optional)
     * @return a response containing the organization members or HTTP 400 / 404 errors
     */
    @Operation(summary = "Retrieves members of a specific organization", description = "Fetches all members from a specific organization.", tags = {"Organization API"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved organization members", content = @Content(schema = @Schema(implementation = OrganizationMembersDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad request if both or neither id and acronym are provided"),
        @ApiResponse(responseCode = "404", description = "Not Found if no organization is found with the provided ID or acronym.")
    })
    @Transactional
    @GetMapping("/members")
    public ResponseEntity<OrganizationMembersDTO> getOrganizationMembers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String acronym
    ) {
        if ((id == null && acronym == null) || (id != null && acronym != null)) {
            return ResponseEntity.badRequest().build();
        }

        Optional<ResearchOrganization> organizationOptional = getOrganization(id, acronym);
        if (organizationOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ResearchOrganization organization = organizationOptional.get();
        OrganizationMembersDTO membersDTO = getOrganizationMembersData(organization);

        return ResponseEntity.ok(membersDTO);
    }

    /**
     * Retrieves the indicators of a specific organization by either its ID or acronym.
     *
     * @param id      the ID of the organization (optional)
     * @param acronym the acronym of the organization (optional)
     * @param useCache whether to get indicators from cache memory or not (default = false)
     * @return a response containing the indicators of the organization or HTTP 400 / 404 errors
     */
    @Operation(summary = "Retrieves organization indicators", description = "Fetches indicators for a specific organization, identified by ID or acronym.", tags = {"Organization API"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved organization indicators", content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "Bad request if both or neither id and acronym are provided"),
        @ApiResponse(responseCode = "404", description = "Not Found if no organization is found with the provided ID or acronym.")
    })
    @GetMapping("/indicators")
    @Transactional
    public ResponseEntity<Map<String, Number>> getIndicators(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String acronym,
            @RequestParam(required = false, defaultValue = "false") Boolean useCache
    ) {
        if ((id == null && acronym == null) || (id != null && acronym != null)) {
            return ResponseEntity.badRequest().build();
        }

        Optional<ResearchOrganization> organizationOptional = getOrganization(id, acronym);
        if (organizationOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ResearchOrganization organization = organizationOptional.get();

        List<Pair<? extends Indicator, Number>> indicators = new ArrayList<>(globalIndicatorsService.getVisibleIndicatorsWithValues(organization, useCache));
        Map<String, Number> indicatorsMap = new HashMap<>();

        for (var indicator : indicators) {
            String key = indicator.getKey().getKey();
            Number value = indicator.getValue();
            indicatorsMap.put(key, value);
        }

        return ResponseEntity.ok(indicatorsMap);
    }

    /**
     * Retrieves all organization addresses.
     *
     * @return a list of {@link OrganizationAddressDTO} containing all organization addresses
     */
    @Operation(summary = "Retrieves all organization addresses", description = "Fetches all addresses from every organization.", tags = {"Organization API"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all organization addresses", content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrganizationAddressDTO.class)))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Transactional
    @GetMapping("/addresses")
    public ResponseEntity<List<OrganizationAddressDTO>> getAddresses() {
        String googleMapsUrl = "https://www.google.com/maps?q=";

        List<OrganizationAddress> addresses = new ArrayList<>(organizationAddressService.getAllAddresses());
        List<OrganizationAddressDTO> addressDTOs = new ArrayList<>();

        for (OrganizationAddress address : addresses) {
            String name = address.getName();
            String street = address.getStreet();
            String city = address.getCity();
            String zipCode = address.getZipCode();
            String complement = address.getComplement();

            String googleMaps = googleMapsUrl + address.getMapCoordinates();

            addressDTOs.add(new OrganizationAddressDTO(name, complement, street, zipCode, city, googleMaps));
        }

        return ResponseEntity.ok(addressDTOs);
    }

    /**
     * Retrieves the list of members for a given organization.
     *
     * @param organization the {@link ResearchOrganization} to retrieve members from
     * @return a list of {@link OrganizationMemberData} representing the members of the organization
     */
    @Transactional
    public List<OrganizationMemberData> getOrganizationMembers(ResearchOrganization organization) {
        List<OrganizationMemberData> members = new ArrayList<>();

        Set<Membership> memberships = new HashSet<>(organization.getDirectOrganizationMemberships());

        for (Membership membership : memberships) {
            String personName = membership.getPerson().getFullName();
            String personWebsiteId = membership.getPerson().getWebPageId();
            PersonOnWebsite person = new PersonOnWebsite(personName, personWebsiteId);

            MemberStatus status = membership.getMemberStatus();
            Responsibility responsibility = membership.getResponsibility();

            members.add(new OrganizationMemberData(person, status, responsibility));
        }

        return members;
    }


    /**
     * Retrieves the organization members data for a given organization.
     *
     * @param organization the {@link ResearchOrganization} to retrieve the members data for
     * @return an {@link OrganizationMembersDTO} containing the organization name, acronym, website, and members
     */
    @Transactional
    public OrganizationMembersDTO getOrganizationMembersData(ResearchOrganization organization) {
        String organizationName = organization.getName();
        String organizationAcronym = organization.getAcronym();
        String organizationWebsite = organization.getOrganizationURL();
        List<OrganizationMemberData> members = getOrganizationMembers(organization);

        return new OrganizationMembersDTO(organizationName, organizationAcronym, organizationWebsite, members);
    }

    /**
     * Retrieves a {@link ResearchOrganization} by either its ID or acronym.
     *
     * @param id      the ID of the organization (optional)
     * @param acronym the acronym of the organization (optional)
     * @return an {@link Optional} containing the found {@link ResearchOrganization} or empty if not found
     */
    private Optional<ResearchOrganization> getOrganization(Long id, String acronym) {
        if (id != null) {
            return researchOrganizationService.getResearchOrganizationById(id);
        } else if (acronym != null) {
            return researchOrganizationService.getResearchOrganizationByAcronym(acronym);
        }
        return Optional.empty();
    }
}
