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

package fr.utbm.ciad.labmanager.views.components.organizationaddresses.editors.regular;

import fr.utbm.ciad.labmanager.data.organization.OrganizationAddress;
import fr.utbm.ciad.labmanager.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityEditingContext;
import fr.utbm.ciad.labmanager.utils.builders.ConstructionPropertiesBuilder;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.views.components.addons.entities.EntityCreationStatusComputer;
import org.springframework.context.support.MessageSourceAccessor;

/** Editor of organization address information that may be embedded. This editor does not provide
 * the components for saving the information. It is the role of the component that
 * is embedding this editor to save the edited address.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public final class EmbeddedAddressEditor extends AbstractAddressEditor {

	private static final long serialVersionUID = 7033019483159095081L;

	/** Constructor.
	 *
	 * @param context the context for editing the entity.
	 * @param addressCreationStatusComputer the tool for computer the creation status for the organization adddresses.
	 * @param fileManager the manager of files at the server-side.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 */
	public EmbeddedAddressEditor(EntityEditingContext<OrganizationAddress> context,
			EntityCreationStatusComputer<OrganizationAddress> addressCreationStatusComputer,
			DownloadableFileManager fileManager, AuthenticatedUser authenticatedUser,
			MessageSourceAccessor messages) {
		super(context, addressCreationStatusComputer, false, fileManager, authenticatedUser, messages, ConstructionPropertiesBuilder.create());
		createEditorContentAndLinkBeans();
	}

}
