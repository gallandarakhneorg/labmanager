package fr.utbm.ciad.wprest.person.data.dto;

import fr.utbm.ciad.labmanager.data.member.MemberStatus;
import fr.utbm.ciad.wprest.organization.data.OrganizationData;

/**
 * Data Transfer Object (DTO) representing a person's membership in an organization.
 *
 * @param status       the status of the person in the organization
 * @param organization the data of the organization
 */
public record PersonMembershipDTO(MemberStatus status,
                                  OrganizationData organization) {
}
