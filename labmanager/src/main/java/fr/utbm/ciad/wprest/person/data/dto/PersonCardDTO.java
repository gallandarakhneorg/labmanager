package fr.utbm.ciad.wprest.person.data.dto;

import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.utils.phone.PhoneNumber;

import java.net.URL;

/**
 * Data Transfer Object (DTO) representing a person's card containing their personal and contact information.
 *
 * @param firstName   the first name of the person
 * @param lastName    the last name of the person
 * @param email       the email address of the person
 * @param mobilePhone the mobile phone number of the person
 * @param officePhone the office phone number of the person
 * @param room        the office room number or location of the person
 * @param ranking     the ranking information of the person
 * @param links       the links related to the person's profile
 * @param webpageId   the id of the webpage related to the person's profile if any
 */
public record PersonCardDTO(String firstName,
                            String lastName,
                            String email,
                            URL photo,
                            PhoneNumber mobilePhone,
                            PhoneNumber officePhone,
                            String room,
                            PersonService.PersonRankingUpdateInformation ranking,
                            PersonService.PersonLinks links,
                            String webpageId) {
}
