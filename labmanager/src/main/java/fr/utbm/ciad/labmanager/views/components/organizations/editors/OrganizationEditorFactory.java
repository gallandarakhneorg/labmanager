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

package fr.utbm.ciad.labmanager.views.components.organizations.editors;

import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityEditingContext;
import fr.utbm.ciad.labmanager.services.organization.OrganizationAddressService;
import fr.utbm.ciad.labmanager.services.organization.ResearchOrganizationService;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityEditor;
import org.springframework.context.support.MessageSourceAccessor;

/** Factory that is providing an organization editor according to the editing context.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public interface OrganizationEditorFactory {

	/** Create an editor that may be used for creating a new organization.
	 * 
	 * @param context the context for editing the entity.
	 * @param fileManager the manager of files at the server-side.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 * @param organizationService the service for accessing the organizations.
	 * @param addressService the service for accessing the organization addresses.
	 * @return the editor, never {@code null}.
	 */
	AbstractEntityEditor<ResearchOrganization> createAdditionEditor(EntityEditingContext<ResearchOrganization> context,
			DownloadableFileManager fileManager,
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			ResearchOrganizationService organizationService,
			OrganizationAddressService addressService);

	/** Create an editor that may be used for updating an existing organization.
	 * 
	 * @param context the context for editing the entity.
	 * @param fileManager the manager of files at the server-side.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 * @param organizationService the service for accessing the organizations.
	 * @param addressService the service for accessing the organization addresses.
	 * @return the editor, never {@code null}.
	 */
	AbstractEntityEditor<ResearchOrganization> createUpdateEditor(EntityEditingContext<ResearchOrganization> context,
			DownloadableFileManager fileManager,
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			ResearchOrganizationService organizationService,
			OrganizationAddressService addressService);

}
