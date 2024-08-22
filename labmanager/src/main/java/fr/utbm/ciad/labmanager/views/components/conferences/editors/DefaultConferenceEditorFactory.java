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

package fr.utbm.ciad.labmanager.views.components.conferences.editors;

import fr.utbm.ciad.labmanager.data.conference.Conference;
import fr.utbm.ciad.labmanager.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityEditingContext;
import fr.utbm.ciad.labmanager.services.conference.ConferenceService;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityEditor;
import fr.utbm.ciad.labmanager.views.components.conferences.editors.regular.EmbeddedConferenceEditor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/** Factory that is providing a conference editor according to the editing context.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Component
public class DefaultConferenceEditorFactory implements ConferenceEditorFactory {

	@Override
	public AbstractEntityEditor<Conference> createAdditionEditor(EntityEditingContext<Conference> context, ConferenceService conferenceService,
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages) {
		return new EmbeddedConferenceEditor(context, conferenceService, authenticatedUser, messages);
	}

	@Override
	public AbstractEntityEditor<Conference> createUpdateEditor(EntityEditingContext<Conference> context, ConferenceService conferenceService,
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages) {
		return new EmbeddedConferenceEditor(context, conferenceService, authenticatedUser, messages);
	}

}
