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

package fr.utbm.ciad.labmanager.views.components.memberships;

import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityEditingContext;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.organization.OrganizationAddressService;
import fr.utbm.ciad.labmanager.services.organization.ResearchOrganizationService;
import fr.utbm.ciad.labmanager.services.scientificaxis.ScientificAxisService;
import fr.utbm.ciad.labmanager.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;

/** Editor of membership information that may be embedded. This editor does not provide
 * the components for saving the information. It is the role of the component that
 * is embedding this editor to save the edited membership.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public final class EmbeddedMembershipEditor extends AbstractMembershipEditor {

	private static final long serialVersionUID = 5271631470481314239L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedMembershipEditor.class);

	/** Constructor.
	 *
	 * @param context the editing context for the membership.
	 * @param editAssociatedPerson indicates if the associated person could be edited or not.
	 * @param personService the service for accessing the JPA entities for persons.
	 * @param userService the service for accessing the JPA entities for users.
	 * @param organizationService the service for accessing the JPA entities for research organizations.
	 * @param addressService the service for accessing the JPA entities for organization addresses.
	 * @param axisService the service for accessing the JPA entities for scientific axes.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 */
	public EmbeddedMembershipEditor(EntityEditingContext<Membership> context,
			boolean editAssociatedPerson, PersonService personService, UserService userService,
			ResearchOrganizationService organizationService, OrganizationAddressService addressService,
			ScientificAxisService axisService, AuthenticatedUser authenticatedUser,
			MessageSourceAccessor messages) {
		super(context, editAssociatedPerson, false, personService, userService,
				organizationService, addressService, axisService, authenticatedUser, messages, LOGGER);
		createEditorContentAndLinkBeans();
	}

}
