package fr.utbm.ciad.wprest.person.data;

import fr.utbm.ciad.wprest.data.DateRange;
import fr.utbm.ciad.wprest.data.PersonOnWebsite;

/**
 * Describes an invitation for a person.
 *
 * @param title      the title of the invitation
 * @param guest      the name and webpageId of the supervisor
 * @param university the university associated with the invitation
 * @param dates      the date range for the invitation
 */
public record PersonInvitationData(String title,
                                   PersonOnWebsite guest,
                                   UniversityData university,
                                   DateRange dates) {
}
