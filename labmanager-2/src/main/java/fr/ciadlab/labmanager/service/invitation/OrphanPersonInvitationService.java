/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the CIAD laboratory and the Université de Technologie
 * de Belfort-Montbéliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD-UTBM.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.service.invitation;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.invitation.PersonInvitation;
import fr.ciadlab.labmanager.repository.invitation.PersonInvitationRepository;
import fr.ciadlab.labmanager.service.AbstractOrphanService;
import org.arakhne.afc.progress.Progression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for the orphan person invitations.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Service
public class OrphanPersonInvitationService extends AbstractOrphanService<PersonInvitation> {

	private static final String MESSAGE_PREFIX = "orphanPersonInvitationService."; //$NON-NLS-1$

	private PersonInvitationRepository invitationRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param invitationRepository the person invitation repository.
	 */
	public OrphanPersonInvitationService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PersonInvitationRepository invitationRepository) {
		super(messages, constants);
		this.invitationRepository = invitationRepository;
	}

	@Override
	public void computeOrphans(ArrayNode receiver, Progression progress) {
		computeOrphansInJson(receiver, this.invitationRepository, this,
				Constants.PERSON_INVITATION_EDITING_ENDPOINT, Constants.PERSON_ENDPOINT_PARAMETER,
				Constants.PERSON_INVITATION_DELETION_ENDPOINT, Constants.ID_ENDPOINT_PARAMETER,
				progress);
	}

	@Override
	public String getOrphanCriteria(PersonInvitation invitation) {
		if (invitation.getGuest() == null) {
			return getMessage(MESSAGE_PREFIX + "NoGuestPerson"); //$NON-NLS-1$
		}
		if (invitation.getInviter() == null) {
			return getMessage(MESSAGE_PREFIX + "NoInviterPerson"); //$NON-NLS-1$
		}
		if (invitation.getStartDate() == null) {
			return getMessage(MESSAGE_PREFIX + "NoStartDate"); //$NON-NLS-1$
		}
		if (invitation.getEndDate() == null) {
			return getMessage(MESSAGE_PREFIX + "NoEndDate"); //$NON-NLS-1$
		}
		return null;
	}

	@Override
	public String getOrphanEntityLabel(PersonInvitation entity) {
		return entity.getGuest() + " - " + entity.getInviter() + " - " + entity.getStartDate(); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public String getOrphanTypeLabel() {
		return getMessage(MESSAGE_PREFIX + "Name"); //$NON-NLS-1$
	}

}
