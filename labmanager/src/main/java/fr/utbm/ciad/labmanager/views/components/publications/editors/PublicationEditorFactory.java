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
import org.springframework.context.support.MessageSourceAccessor;

/** Factory that is providing a publication editor according to the editing context.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public interface PublicationEditorFactory {

	/** Create an editor that may be used for creating a new publication.
	 * 
	 * @param context the context for editing the entity.
	 * @param supportedTypes list of publication types that are supported by the editor. Only the publications of a type from this list could be edited.
	 * @param enableTypeSelector indicates if the type selector is enabled or disabled.
	 * @param fileManager the manager of files at the server-side.
	 * @param publicationService the service for accessing the JPA entities for publications.
	 * @param personService the service for accessing the JPA entities for persons.
	 * @param personEditorFactory the factory for creating the person editors.
	 * @param userService the service for accessing the JPA entities for users.
	 * @param journalService the service for accessing the JPA entities for journal.
	 * @param journalEditorFactory the factory for creating journal editors.
	 * @param conferenceService the service for accessing the JPA entities for conference.
	 * @param conferenceEditorFactory the factory for creating the conference editors.
	 * @param axisService service for accessing to the JPA entities of scientific axes. 
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 * @param personCreationLabelKey the key that is used for retrieving the text for creating a new person and associating it to the publication.
	 * @param personFieldLabelKey the key that is used for retrieving the text for the label of the author/editor field.
	 * @param personFieldHelperLabelKey the key that is used for retrieving the text for the helper of the author/editor field.
	 * @param personNullErrorKey the key that is used for retrieving the text of the author/editor null error.
	 * @param personDuplicateErrorKey the key that is used for retrieving the text of the author/editor duplicate error.
	 * @return the editor, never {@code null}.
	 */
	AbstractEntityEditor<Publication> createAdditionEditor(EntityEditingContext<Publication> context,
			PublicationType[] supportedTypes, boolean enableTypeSelector,
			DownloadableFileManager fileManager, PublicationService publicationService,
			PersonService personService, PersonEditorFactory personEditorFactory, UserService userService,
			JournalService journalService, JournalEditorFactory journalEditorFactory,
			ConferenceService conferenceService, ConferenceEditorFactory conferenceEditorFactory, 
			ScientificAxisService axisService, AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			String personCreationLabelKey, String personFieldLabelKey, String personFieldHelperLabelKey,
			String personNullErrorKey, String personDuplicateErrorKey);

	/** Create an editor that may be used for updating an existing person.
	 * 
	 * @param context the context for editing the entity.
	 * @param supportedTypes list of publication types that are supported by the editor. Only the publications of a type from this list could be edited.
	 * @param enableTypeSelector indicates if the type selector is enabled or disabled.
	 * @param fileManager the manager of files at the server-side.
	 * @param publicationService the service for accessing the JPA entities for publications.
	 * @param personService the service for accessing the JPA entities for persons.
	 * @param personEditorFactory the factory for creating the person editors.
	 * @param userService the service for accessing the JPA entities for users.
	 * @param journalService the service for accessing the JPA entities for journal.
	 * @param journalEditorFactory the factory for creating journal editors.
	 * @param conferenceService the service for accessing the JPA entities for conference.
	 * @param conferenceEditorFactory the factory for creating the conference editors.
	 * @param axisService service for accessing to the JPA entities of scientific axes. 
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 * @param personCreationLabelKey the key that is used for retrieving the text for creating a new person and associating it to the publication.
	 * @param personFieldLabelKey the key that is used for retrieving the text for the label of the author/editor field.
	 * @param personFieldHelperLabelKey the key that is used for retrieving the text for the helper of the author/editor field.
	 * @param personNullErrorKey the key that is used for retrieving the text of the author/editor null error.
	 * @param personDuplicateErrorKey the key that is used for retrieving the text of the author/editor duplicate error.
	 * @return the editor, never {@code null}.
	 */
	AbstractEntityEditor<Publication> createUpdateEditor(EntityEditingContext<Publication> context,
			PublicationType[] supportedTypes, boolean enableTypeSelector,
			DownloadableFileManager fileManager, PublicationService publicationService,
			PersonService personService, PersonEditorFactory personEditorFactory, UserService userService,
			JournalService journalService, JournalEditorFactory journalEditorFactory,
			ConferenceService conferenceService, ConferenceEditorFactory conferenceEditorFactory, 
			ScientificAxisService axisService, AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			String personCreationLabelKey, String personFieldLabelKey, String personFieldHelperLabelKey,
			String personNullErrorKey, String personDuplicateErrorKey);

}
