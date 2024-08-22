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

package fr.utbm.ciad.labmanager.views.components.assocstructures.editors;

import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructure;
import fr.utbm.ciad.labmanager.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityEditingContext;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.organization.OrganizationAddressService;
import fr.utbm.ciad.labmanager.services.organization.ResearchOrganizationService;
import fr.utbm.ciad.labmanager.services.project.ProjectService;
import fr.utbm.ciad.labmanager.services.scientificaxis.ScientificAxisService;
import fr.utbm.ciad.labmanager.services.user.UserService;
import fr.utbm.ciad.labmanager.views.components.organizations.editors.OrganizationEditorFactory;
import fr.utbm.ciad.labmanager.views.components.persons.editors.PersonEditorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;

/** Editor of associated structure information that may be embedded. This editor does not provide
 * the components for saving the information. It is the role of the component that
 * is embedding this editor to save the edited associated structure.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public final class EmbeddedAssociatedStructureEditor extends AbstractAssociatedStructureEditor {

	private static final long serialVersionUID = -1397381071432537857L;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedAssociatedStructureEditor.class);

	/** Constructor.
	 *
	 * @param context the editing context for the associated structure.
	 * @param projectService the service for accessing the JPA entities for projects.
	 * @param organizationService the service for accessing the JPA entities for research organizations.
	 * @param addressService the service for accessing the JPA entities for organization addresses.
	 * @param personService the service for accessing the JPA entities for persons.
	 * @param personEditorFactory the factory for creating the person editors.
	 * @param userService the service for accessing the JPA entities for users.
	 * @param authenticatedUser the connected user.
	 * @param axisService the service for accessing the JPA entities for scientific axes.
	 * @param organizationEditorFactory the factory for creating the organization editors.
	 * @param messages the accessor to the localized messages (Spring layer).
	 */
	public EmbeddedAssociatedStructureEditor(EntityEditingContext<AssociatedStructure> context,
			ProjectService projectService, ResearchOrganizationService organizationService,
			OrganizationAddressService addressService, PersonService personService, PersonEditorFactory personEditorFactory,
			UserService userService, AuthenticatedUser authenticatedUser, ScientificAxisService axisService,
			OrganizationEditorFactory organizationEditorFactory, MessageSourceAccessor messages) {
		super(context, false, projectService, organizationService, addressService, personService, personEditorFactory,
				userService, authenticatedUser, axisService, organizationEditorFactory, messages, LOGGER);
		createEditorContentAndLinkBeans();
	}

}
