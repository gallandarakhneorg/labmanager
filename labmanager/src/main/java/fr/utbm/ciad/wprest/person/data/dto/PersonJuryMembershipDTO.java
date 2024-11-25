package fr.utbm.ciad.wprest.person.data.dto;

import fr.utbm.ciad.labmanager.data.jury.JuryType;
import fr.utbm.ciad.wprest.data.PersonOnWebsite;
import fr.utbm.ciad.wprest.person.data.UniversityData;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a person's membership in a jury.
 *
 * @param title      the title of the jury membership
 * @param year       the year of the jury membership
 * @param candidate  the name and webpageId of the candidate being evaluated
 * @param directors  a list of directors involved in the jury
 * @param university the university associated with the jury
 * @param type       the type of jury
 */
public record PersonJuryMembershipDTO(String title,
                                      int year,
                                      PersonOnWebsite candidate,
                                      List<PersonOnWebsite> directors,
                                      JuryType type,
                                      UniversityData university) {
}
