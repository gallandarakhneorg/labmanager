package fr.utbm.ciad.wprest.person.data;

import fr.utbm.ciad.labmanager.data.supervision.SupervisorType;
import fr.utbm.ciad.wprest.data.PersonOnWebsite;

/**
 * Describes information about a person's supervisor.
 *
 * @param person the name and webpageId of the supervisor
 * @param type   the type of supervision (e.g., primary, co-supervisor)
 */
public record SupervisedPersonSupervisorData(PersonOnWebsite person,
                                             SupervisorType type) {
}
