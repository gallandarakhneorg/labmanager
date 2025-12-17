package fr.utbm.ciad.wprest.person.data.dto;

import fr.utbm.ciad.labmanager.data.supervision.SupervisorType;
import fr.utbm.ciad.wprest.data.PersonOnWebsite;
import fr.utbm.ciad.wprest.person.data.PersonOrganizationData;
import fr.utbm.ciad.wprest.person.data.SupervisedPersonSupervisorData;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing supervision information for a research project.
 *
 * @param name                 the name and webpageId of the supervision
 * @param supervisedPerson     the person being supervised
 * @param year                 the year of supervision
 * @param supervisionType      the type of supervision
 * @param researchOrganization the research organization involved
 * @param supervisors          a list of supervisors for the supervised person
 */
public record SupervisionsDTO(String name,
                              PersonOnWebsite supervisedPerson,
                              int year,
                              SupervisorType supervisionType,
                              PersonOrganizationData researchOrganization,
                              List<SupervisedPersonSupervisorData> supervisors) {
}
