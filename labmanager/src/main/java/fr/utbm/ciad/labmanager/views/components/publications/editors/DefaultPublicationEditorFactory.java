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

package fr.utbm.ciad.labmanager.views.components.publications.editors;

import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.data.publication.PublicationType;
import fr.utbm.ciad.labmanager.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityEditingContext;
import fr.utbm.ciad.labmanager.services.conference.ConferenceService;
import fr.utbm.ciad.labmanager.services.journal.JournalService;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.services.scientificaxis.ScientificAxisService;
import fr.utbm.ciad.labmanager.services.user.UserService;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityEditor;
import fr.utbm.ciad.labmanager.views.components.conferences.editors.ConferenceEditorFactory;
import fr.utbm.ciad.labmanager.views.components.journals.editors.JournalEditorFactory;
import fr.utbm.ciad.labmanager.views.components.persons.editors.PersonEditorFactory;
import fr.utbm.ciad.labmanager.views.components.publications.editors.regular.EmbeddedPublicationEditor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/** Factory that is providing a publication editor according to the editing context.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Component
public class DefaultPublicationEditorFactory implements PublicationEditorFactory {

	@Override
	public AbstractEntityEditor<Publication> createAdditionEditor(EntityEditingContext<Publication> context,
			PublicationType[] supportedTypes, boolean enableTypeSelector,
			DownloadableFileManager fileManager, PublicationService publicationService,
			PersonService personService, PersonEditorFactory personEditorFactory, UserService userService,
			JournalService journalService, JournalEditorFactory journalEditorFactory,
			ConferenceService conferenceService, ConferenceEditorFactory conferenceEditorFactory, 
			ScientificAxisService axisService, AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			String personCreationLabelKey, String personFieldLabelKey, String personFieldHelperLabelKey,
			String personNullErrorKey, String personDuplicateErrorKey) {
		return new EmbeddedPublicationEditor(context, supportedTypes, enableTypeSelector, fileManager, publicationService, personService, personEditorFactory, userService,
				journalService, journalEditorFactory, conferenceService, conferenceEditorFactory, axisService, authenticatedUser, messages, personCreationLabelKey,
				personFieldLabelKey, personFieldHelperLabelKey, personNullErrorKey, personDuplicateErrorKey);
	}

	@Override
	public AbstractEntityEditor<Publication> createUpdateEditor(EntityEditingContext<Publication> context,
			PublicationType[] supportedTypes, boolean enableTypeSelector,
			DownloadableFileManager fileManager, PublicationService publicationService,
			PersonService personService, PersonEditorFactory personEditorFactory, UserService userService,
			JournalService journalService, JournalEditorFactory journalEditorFactory,
			ConferenceService conferenceService, ConferenceEditorFactory conferenceEditorFactory, 
			ScientificAxisService axisService, AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			String personCreationLabelKey, String personFieldLabelKey, String personFieldHelperLabelKey,
			String personNullErrorKey, String personDuplicateErrorKey) {
		return new EmbeddedPublicationEditor(context, supportedTypes, enableTypeSelector, fileManager, publicationService, personService, personEditorFactory, userService,
				journalService, journalEditorFactory, conferenceService, conferenceEditorFactory, axisService, authenticatedUser, messages,
				personCreationLabelKey, personFieldLabelKey, personFieldHelperLabelKey, personNullErrorKey, personDuplicateErrorKey);
	}

}
