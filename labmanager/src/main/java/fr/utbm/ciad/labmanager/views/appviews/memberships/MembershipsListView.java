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

package fr.utbm.ciad.labmanager.views.appviews.memberships;

import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.user.UserRole;
import fr.utbm.ciad.labmanager.services.member.MembershipService;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.organization.OrganizationAddressService;
import fr.utbm.ciad.labmanager.services.organization.ResearchOrganizationService;
import fr.utbm.ciad.labmanager.services.scientificaxis.ScientificAxisService;
import fr.utbm.ciad.labmanager.services.user.UserService;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.views.appviews.MainLayout;
import fr.utbm.ciad.labmanager.views.components.memberships.StandardMembershipListView;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;

/** Enable to edit the memberships for a person.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Route(value = "positions", layout = MainLayout.class)
@RolesAllowed({UserRole.RESPONSIBLE_GRANT, UserRole.ADMIN_GRANT})
public class MembershipsListView extends StandardMembershipListView implements HasDynamicTitle {

	private static final long serialVersionUID = -557163925957008625L;

	private static final Logger LOGGER = LoggerFactory.getLogger(MembershipsListView.class);

	/** Constructor.
	 * 
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param membershipService the service for accessing the memberships.
	 * @param personService the service for accessing the JPA entities for persons.
	 * @param userService the service for accessing the JPA entities for users.
	 * @param organizationService the service for accessing the JPA entities for research organizations.
	 * @param addressService the service for accessing the JPA entities for organization addresses.
	 * @param axisService the service for accessing the JPA entities for scientific axes.
	 * @param fileManager the manager of local files for accessing the associated icons to the entities.
	 */
	public MembershipsListView(AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			MembershipService membershipService, PersonService personService, UserService userService,
			ResearchOrganizationService organizationService, OrganizationAddressService addressService,
			ScientificAxisService axisService, DownloadableFileManager fileManager) {
		super(authenticatedUser, messages, membershipService, personService, userService,
				organizationService, addressService, axisService, fileManager, LOGGER);
	}

	@Override
	public String getPageTitle() {
		return getTranslation("views.membership.memberships.list"); //$NON-NLS-1$
	}

}
