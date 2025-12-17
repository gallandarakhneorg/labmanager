package fr.utbm.ciad.wprest.organization.data.dto;

import fr.utbm.ciad.wprest.organization.data.OrganizationMemberData;

import java.util.List;

/**
 * Describes information about the members of an organization
 *
 * @param name                - the name of the organization
 * @param acronym             - the acronym of the organization
 * @param organizationWebsite - the webstie of the organization
 * @param members             - the list of members of the organization
 */
public record OrganizationMembersDTO(String name,
                                     String acronym,
                                     String organizationWebsite,
                                     List<OrganizationMemberData> members) {
}
