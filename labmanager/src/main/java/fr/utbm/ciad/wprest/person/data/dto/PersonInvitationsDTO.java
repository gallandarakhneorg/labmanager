package fr.utbm.ciad.wprest.person.data.dto;

import fr.utbm.ciad.labmanager.data.invitation.PersonInvitationType;
import fr.utbm.ciad.wprest.person.data.PersonInvitationData;

import java.util.Map;
import java.util.Set;

/**
 * Data Transfer Object (DTO) representing invitations related to a person, including both guest and inviter invitations data.
 *
 * @param guestInvitations   a map of guest invitations categorized by invitation type
 * @param inviterInvitations a map of invitations where the person is the inviter, categorized by invitation type
 */
public record PersonInvitationsDTO(Map<PersonInvitationType, Set<PersonInvitationData>> guestInvitations,
                                   Map<PersonInvitationType, Set<PersonInvitationData>> inviterInvitations) {
}
