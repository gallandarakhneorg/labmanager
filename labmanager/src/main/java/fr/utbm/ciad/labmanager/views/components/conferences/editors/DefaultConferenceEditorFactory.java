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
import org.springframework.beans.factory.annotation.Autowired;
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

	private final ConferenceService conferenceService;

	private final AuthenticatedUser authenticatedUser;

	private final MessageSourceAccessor messages;
	
	/** Constructor.
	 *
	 * @param conferenceService the service for accessing to the conference entities.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 */
	public DefaultConferenceEditorFactory(
			@Autowired ConferenceService conferenceService,
			@Autowired AuthenticatedUser authenticatedUser,
			@Autowired MessageSourceAccessor messages) {
		this.conferenceService = conferenceService;
		this.authenticatedUser = authenticatedUser;
		this.messages = messages;
	}
	
	@Override
	public EntityEditingContext<Conference> createContextFor(Conference conference) {
		return this.conferenceService.startEditing(conference);
	}

	@Override
	public AbstractEntityEditor<Conference> createAdditionEditor(EntityEditingContext<Conference> context) {
		return new EmbeddedConferenceEditor(context, this.conferenceService, this.authenticatedUser, this.messages);
	}

	@Override
	public AbstractEntityEditor<Conference> createUpdateEditor(EntityEditingContext<Conference> context) {
		return new EmbeddedConferenceEditor(context, this.conferenceService, this.authenticatedUser, this.messages);
	}

}
