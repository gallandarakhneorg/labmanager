package fr.utbm.ciad.wprest.organization.data;

import fr.utbm.ciad.labmanager.data.member.MemberStatus;
import fr.utbm.ciad.labmanager.data.member.Responsibility;
import fr.utbm.ciad.wprest.data.PersonOnWebsite;

/**
 * Describe the data associated to a person in an organization
 *
 * @param person         - The name and website if of the person
 * @param status         - The status of the person in the organization
 * @param responsibility - The responsibility of the person in the organization
 */
public record OrganizationMemberData(PersonOnWebsite person,
                                     MemberStatus status,
                                     Responsibility responsibility) {
}
