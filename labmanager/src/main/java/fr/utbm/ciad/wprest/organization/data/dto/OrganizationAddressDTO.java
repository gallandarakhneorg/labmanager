package fr.utbm.ciad.wprest.organization.data.dto;

/**
 * Describes the address of an organization
 *
 * @param name          - the name of the lab
 * @param campusName    - the name of the organization
 * @param street        - the street
 * @param zipCode       - the zipCode
 * @param city          - the city name
 * @param googleMapsUrl - the Google Maps URL of the organization
 */
public record OrganizationAddressDTO(String name,
                                     String campusName,
                                     String street,
                                     String zipCode,
                                     String city,
                                     String googleMapsUrl) {
}
