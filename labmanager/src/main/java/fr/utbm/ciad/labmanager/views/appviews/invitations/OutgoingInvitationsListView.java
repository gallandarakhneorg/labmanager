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

package fr.utbm.ciad.labmanager.views.appviews.invitations;

import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.user.UserRole;
import fr.utbm.ciad.labmanager.services.invitation.PersonInvitationService;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.user.UserService;
import fr.utbm.ciad.labmanager.views.appviews.MainLayout;
import fr.utbm.ciad.labmanager.views.components.invitations.StandardOutgoingInvitationListView;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;

/** Enable to list the outgoing invitations.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Route(value = "invitationsout", layout = MainLayout.class)
@RolesAllowed({UserRole.RESPONSIBLE_GRANT, UserRole.ADMIN_GRANT})
public class OutgoingInvitationsListView extends StandardOutgoingInvitationListView implements HasDynamicTitle {

	private static final long serialVersionUID = 739540543538358993L;

	private static final Logger LOGGER = LoggerFactory.getLogger(OutgoingInvitationsListView.class);

	/** Constructor.
	 * 
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param invitationService the service for accessing the outgoing invitations.
	 * @param personService the service for accessing the JPA entities for persons.
	 * @param userService the service for accessing the JPA entities for users.
	 */
	public OutgoingInvitationsListView(AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			PersonInvitationService invitationService, PersonService personService,
			UserService userService) {
		super(authenticatedUser, messages, invitationService, personService, userService, LOGGER);
	}

	@Override
	public String getPageTitle() {
		return getTranslation("views.outgoing_invitation.invitations.list"); //$NON-NLS-1$
	}

}