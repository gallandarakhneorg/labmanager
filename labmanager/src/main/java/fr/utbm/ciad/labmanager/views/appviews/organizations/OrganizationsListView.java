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

package fr.utbm.ciad.labmanager.views.appviews.organizations;

import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.user.UserRole;
import fr.utbm.ciad.labmanager.services.organization.OrganizationAddressService;
import fr.utbm.ciad.labmanager.services.organization.ResearchOrganizationService;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.views.appviews.MainLayout;
import fr.utbm.ciad.labmanager.views.components.organizations.StandardOrganizationListView;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;

/** List all the organizations.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Route(value = "organizations", layout = MainLayout.class)
@RolesAllowed({UserRole.RESPONSIBLE_GRANT, UserRole.ADMIN_GRANT})
public class OrganizationsListView extends StandardOrganizationListView implements HasDynamicTitle {

	private static final long serialVersionUID = -6870101518355964055L;

	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationsListView.class);

	/** Constructor.
	 *
	 * @param fileManager the manager of filenames for uploaded files.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param organizationService the service for accessing the organizations.
	 * @param addressService the service for accessing the organization addresses.
	 */
	public OrganizationsListView(
			@Autowired DownloadableFileManager fileManager,
			@Autowired AuthenticatedUser authenticatedUser, @Autowired MessageSourceAccessor messages,
			@Autowired ResearchOrganizationService organizationService,
			@Autowired OrganizationAddressService addressService) {
		super(fileManager, authenticatedUser, messages, organizationService, addressService, LOGGER);
	}

	@Override
	public String getPageTitle() {
		return getTranslation("views.organizations.organizations.list"); //$NON-NLS-1$
	}

}
