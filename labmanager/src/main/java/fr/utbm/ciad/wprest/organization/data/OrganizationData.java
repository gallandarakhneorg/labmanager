package fr.utbm.ciad.wprest.organization.data;

import java.util.Set;

/**
 * Describes basic information about an organization.
 *
 * @param name      the name of the organization
 * @param url       the website of the organization
 * @param addresses the different addresses of the organization
 * @param country   the country of the organization
 */
public record OrganizationData(String name, String url,
                               Set<String> addresses,
                               String country) {
}
