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

package fr.utbm.ciad.labmanager.views.components.invitations.editors;

import fr.utbm.ciad.labmanager.data.invitation.PersonInvitation;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityEditingContext;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityEditor;

/** Factory that is providing an invitation editor according to the editing context.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public interface InvitationEditorFactory {

	/** Replies the editing context for the given person invitation.
	 *
	 * @param invitation the invitation to be edited.
	 * @return the editing context.
	 */
	EntityEditingContext<PersonInvitation> createContextFor(PersonInvitation invitation);

	/** Create an editor that may be used for creating a new incoming invitation.
	 * 
	 * @param context the context for editing the entity.
	 * @return the editor, never {@code null}.
	 */
	AbstractEntityEditor<PersonInvitation> createIncomingInvitationAdditionEditor(EntityEditingContext<PersonInvitation> context);

	/** Create an editor that may be used for creating a new incoming invitation.
	 * 
	 * @param invitation the invitation to be edited.
	 * @return the editor, never {@code null}.
	 */
	default AbstractEntityEditor<PersonInvitation> createIncomingInvitationAdditionEditor(PersonInvitation invitation) {
		final var context = createContextFor(invitation);
		return createIncomingInvitationAdditionEditor(context);
	}

	/** Create an editor that may be used for updating an existing incoming invitation.
	 * 
	 * @param context the context for editing the entity.
	 * @return the editor, never {@code null}.
	 */
	AbstractEntityEditor<PersonInvitation> createIncomingInvitationUpdateEditor(EntityEditingContext<PersonInvitation> context);

	/** Create an editor that may be used for updating an existing incoming invitation.
	 * 
	 * @param invitation the invitation to be edited.
	 * @return the editor, never {@code null}.
	 */
	default AbstractEntityEditor<PersonInvitation> createIncomingInvitationUpdateEditor(PersonInvitation invitation) {
		final var context = createContextFor(invitation);
		return createIncomingInvitationUpdateEditor(context);
	}

	/** Create an editor that may be used for creating a new outgoing invitation.
	 * 
	 * @param context the context for editing the entity.
	 * @return the editor, never {@code null}.
	 */
	AbstractEntityEditor<PersonInvitation> createOutgoingInvitationAdditionEditor(EntityEditingContext<PersonInvitation> context);

	/** Create an editor that may be used for creating a new outgoing invitation.
	 * 
	 * @param invitation the invitation to be edited.
	 * @return the editor, never {@code null}.
	 */
	default AbstractEntityEditor<PersonInvitation> createOutgoingInvitationAdditionEditor(PersonInvitation invitation) {
		final var context = createContextFor(invitation);
		return createOutgoingInvitationAdditionEditor(context);
	}

	/** Create an editor that may be used for updating an existing outgoing invitation.
	 * 
	 * @param context the context for editing the entity.
	 * @return the editor, never {@code null}.
	 */
	AbstractEntityEditor<PersonInvitation> createOutgoingInvitationUpdateEditor(EntityEditingContext<PersonInvitation> context);

	/** Create an editor that may be used for updating an existing outgoing invitation.
	 * 
	 * @param invitation the invitation to be edited.
	 * @return the editor, never {@code null}.
	 */
	default AbstractEntityEditor<PersonInvitation> createOutgoingInvitationUpdateEditor(PersonInvitation invitation) {
		final var context = createContextFor(invitation);
		return createOutgoingInvitationUpdateEditor(context);
	}

}
