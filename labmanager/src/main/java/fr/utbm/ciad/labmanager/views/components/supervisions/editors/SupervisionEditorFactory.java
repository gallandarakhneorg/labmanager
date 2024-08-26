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

package fr.utbm.ciad.labmanager.views.components.supervisions.editors;

import fr.utbm.ciad.labmanager.data.supervision.Supervision;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityEditingContext;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityEditor;

/** Factory that is providing a person supervision editor according to the editing context.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public interface SupervisionEditorFactory {

	/** Replies the editing context for the given person supervision.
	 *
	 * @param supervision the person supervision to be edited.
	 * @return the editing context.
	 */
	EntityEditingContext<Supervision> createContextFor(Supervision supervision);

	/** Create an editor that may be used for creating a new person supervision.
	 * 
	 * @param context the context for editing the entity.
	 * @return the editor, never {@code null}.
	 */
	AbstractEntityEditor<Supervision> createAdditionEditor(EntityEditingContext<Supervision> context);

	/** Create an editor that may be used for creating a new person supervision.
	 * 
	 * @param supervision the person supervision to be edited.
	 * @return the editor, never {@code null}.
	 */
	default AbstractEntityEditor<Supervision> createAdditionEditor(Supervision supervision) {
		final var context = createContextFor(supervision);
		return createAdditionEditor(context);
	}

	/** Create an editor that may be used for updating an existing person supervision.
	 * 
	 * @param context the context for editing the entity.
	 * @return the editor, never {@code null}.
	 */
	AbstractEntityEditor<Supervision> createUpdateEditor(EntityEditingContext<Supervision> context);

	/** Create an editor that may be used for updating an existing person supervision.
	 * 
	 * @param supervision the person supervision to be edited.
	 * @return the editor, never {@code null}.
	 */
	default AbstractEntityEditor<Supervision> createUpdateEditor(Supervision supervision) {
		final var context = createContextFor(supervision);
		return createUpdateEditor(context);
	}

}
