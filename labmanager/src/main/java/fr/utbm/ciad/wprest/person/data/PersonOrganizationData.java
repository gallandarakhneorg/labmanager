package fr.utbm.ciad.wprest.person.data;

import fr.utbm.ciad.wprest.organization.data.OrganizationData;

/**
 * Describes information about a person's organization.
 *
 * @param directResearchOrganization the person's direct research organization
 * @param superResearchOrganization  the person's supervising research organization
 */
public record PersonOrganizationData(OrganizationData directResearchOrganization,
                                     OrganizationData superResearchOrganization) {
}
