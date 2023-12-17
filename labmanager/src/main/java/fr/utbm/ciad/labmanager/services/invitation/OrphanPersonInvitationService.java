/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.utbm.ciad.labmanager.services.invitation;

import java.util.Locale;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.invitation.PersonInvitation;
import fr.utbm.ciad.labmanager.data.invitation.PersonInvitationRepository;
import fr.utbm.ciad.labmanager.services.AbstractOrphanService;
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
	public void computeOrphans(ArrayNode receiver, Locale locale, Progression progress) {
		computeOrphansInJson(receiver, this.invitationRepository, this,
				Constants.PERSON_INVITATION_EDITING_ENDPOINT, Constants.PERSON_ENDPOINT_PARAMETER,
				Constants.PERSON_INVITATION_DELETION_ENDPOINT, Constants.ID_ENDPOINT_PARAMETER,
				locale, progress);
	}

	@Override
	public String getOrphanCriteria(PersonInvitation invitation, Locale locale) {
		if (invitation.getGuest() == null) {
			return getMessage(locale, MESSAGE_PREFIX + "NoGuestPerson"); //$NON-NLS-1$
		}
		if (invitation.getInviter() == null) {
			return getMessage(locale, MESSAGE_PREFIX + "NoInviterPerson"); //$NON-NLS-1$
		}
		if (invitation.getStartDate() == null) {
			return getMessage(locale, MESSAGE_PREFIX + "NoStartDate"); //$NON-NLS-1$
		}
		if (invitation.getEndDate() == null) {
			return getMessage(locale, MESSAGE_PREFIX + "NoEndDate"); //$NON-NLS-1$
		}
		return null;
	}

	@Override
	public String getOrphanEntityLabel(PersonInvitation entity, Locale locale) {
		return entity.getGuest() + " - " + entity.getInviter() + " - " + entity.getStartDate(); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public String getOrphanTypeLabel(Locale locale) {
		return getMessage(locale, MESSAGE_PREFIX + "Name"); //$NON-NLS-1$
	}

}
