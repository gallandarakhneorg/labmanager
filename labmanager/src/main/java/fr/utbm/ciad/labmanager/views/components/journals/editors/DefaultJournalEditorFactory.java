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

package fr.utbm.ciad.labmanager.views.components.journals.editors;

import fr.utbm.ciad.labmanager.data.journal.Journal;
import fr.utbm.ciad.labmanager.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityEditingContext;
import fr.utbm.ciad.labmanager.services.journal.JournalService;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityEditor;
import fr.utbm.ciad.labmanager.views.components.journals.editors.regular.EmbeddedJournalEditor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/** Factory that is providing a journal editor according to the editing context.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Component
public class DefaultJournalEditorFactory implements JournalEditorFactory {

	@Override
	public AbstractEntityEditor<Journal> createAdditionEditor(EntityEditingContext<Journal> context,
			JournalService journalService, AuthenticatedUser authenticatedUser, MessageSourceAccessor messages) {
		return new EmbeddedJournalEditor(context, journalService, authenticatedUser, messages);
	}

	@Override
	public AbstractEntityEditor<Journal> createUpdateEditor(EntityEditingContext<Journal> context,
			JournalService journalService, AuthenticatedUser authenticatedUser, MessageSourceAccessor messages) {
		return new EmbeddedJournalEditor(context, journalService, authenticatedUser, messages);
	}

}
