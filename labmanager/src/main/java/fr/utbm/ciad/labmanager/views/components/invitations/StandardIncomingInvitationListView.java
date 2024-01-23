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

package fr.utbm.ciad.labmanager.views.components.invitations;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider.FetchCallback;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.invitation.PersonInvitation;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityDeletingContext;
import fr.utbm.ciad.labmanager.services.invitation.PersonInvitationService;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityListView;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/** List all the incoming invitations.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class StandardIncomingInvitationListView extends AbstractEntityListView<PersonInvitation> {

	private static final long serialVersionUID = 4093173516761830625L;

	private final IncomingInvitationDataProvider dataProvider;

	private PersonInvitationService invitationService;

	private Column<PersonInvitation> inviterColumn;

	private Column<PersonInvitation> universityColumn;

	private Column<PersonInvitation> dateColumn;

	private Column<PersonInvitation> guestColumn;

	/** Constructor.
	 *
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param invitationService the service for accessing the incoming invitations.
	 * @param logger the logger to use.
	 */
	public StandardIncomingInvitationListView(
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			PersonInvitationService invitationService, Logger logger) {
		super(PersonInvitation.class, authenticatedUser, messages, logger,
				"views.incoming_invitation.delete.title", //$NON-NLS-1$
				"views.incoming_invitation.delete.message", //$NON-NLS-1$
				"views.incoming_invitation.delete_success", //$NON-NLS-1$
				"views.incoming_invitation.delete_error"); //$NON-NLS-1$
		this.invitationService = invitationService;
		this.dataProvider = (ps, query, filters) -> ps.getAllIncomingInvitations(query, filters);
		initializeDataInGrid(getGrid(), getFilters());
	}

	@Override
	protected Filters<PersonInvitation> createFilters() {
		return new IncomingInvitationFilters(this::refreshGrid);
	}

	@Override
	protected boolean createGridColumns(Grid<PersonInvitation> grid) {
		this.inviterColumn = grid.addColumn(new ComponentRenderer<>(this::createInviterComponent))
				.setAutoWidth(true)
				.setSortProperty("inviter"); //$NON-NLS-1$
		this.universityColumn = grid.addColumn(inviter -> inviter.getUniversity())
				.setAutoWidth(true)
				.setSortProperty("university"); //$NON-NLS-1$
		this.dateColumn = grid.addColumn(this::getDateLabel)
				.setAutoWidth(true)
				.setSortProperty("startDate", "endDate"); //$NON-NLS-1$ //$NON-NLS-2$
		this.guestColumn = grid.addColumn(new ComponentRenderer<>(this::createGuestComponent))
				.setAutoWidth(true)
				.setSortProperty("guest"); //$NON-NLS-1$
		return isAdminRole();
	}

	private Component createGuestComponent(PersonInvitation invitation) {
		// TODO
		return new Span("");
	}

	private String getDateLabel(PersonInvitation invitation) {
		int year0;
		int year1;
		if (invitation.getStartDate() != null) {
			year0 = invitation.getStartDate().getYear();
			if (invitation.getEndDate() != null) {
				year1 = invitation.getEndDate().getYear();
			} else {
				year1 = year0;
			}
		} else if (invitation.getEndDate() != null) {
			year0 = invitation.getEndDate().getYear();
			year1 = year0;
		} else {
			year0 = LocalDate.now().getYear();
			year1 = year0;
		}
		if (year0 != year1) {
			return getTranslation("views.incoming_invitation.dates", Integer.toString(year0), Integer.toString(year1)); //$NON-NLS-1$
		}
		return Integer.toString(year0);
	}

	private Component createInviterComponent(PersonInvitation invitation) {
		// TODO
		return new Span("");
	}

	@Override
	protected Column<PersonInvitation> getInitialSortingColumn() {
		return this.dateColumn;
	}

	@Override
	protected FetchCallback<PersonInvitation, Void> getFetchCallback(Filters<PersonInvitation> filters) {
		return query -> {
			return this.dataProvider.fetch(
					this.invitationService,
					VaadinSpringDataHelpers.toSpringPageRequest(query),
					filters).stream();
		};
	}

	@Override
	protected void addEntity() {
		openInvitationEditor(new PersonInvitation(), getTranslation("views.incoming_invitation.add_invitation")); //$NON-NLS-1$
	}

	@Override
	protected void edit(PersonInvitation invitation) {
		openInvitationEditor(invitation, getTranslation("views.incoming_invitation.edit_invitation", invitation.getTitle())); //$NON-NLS-1$
	}

	/** Show the editor of a incoming invitation.
	 *
	 * @param invitation the incoming invitation to edit.
	 * @param title the title of the editor.
	 */
	protected void openInvitationEditor(PersonInvitation invitation, String title) {
		final var editor = new EmbeddedIncomingInvitationEditor(
				this.invitationService.startEditing(invitation),
				getAuthenticatedUser(), getMessageSourceAccessor());
		ComponentFactory.openEditionModalDialog(title, editor, false,
				// Refresh the "old" item, even if its has been changed in the JPA database
				dialog -> refreshItem(invitation),
				null);
	}

	@Override
	protected EntityDeletingContext<PersonInvitation> createDeletionContextFor(Set<PersonInvitation> entities) {
		return this.invitationService.startDeletion(entities);
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.inviterColumn.setHeader(getTranslation("views.inviter")); //$NON-NLS-1$
		this.universityColumn.setHeader(getTranslation("views.university")); //$NON-NLS-1$
		this.dateColumn.setHeader(getTranslation("views.date")); //$NON-NLS-1$
		this.guestColumn.setHeader(getTranslation("views.guest")); //$NON-NLS-1$
	}

	/** UI and JPA filters for {@link StandardIncomingInvitationListView}.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class IncomingInvitationFilters extends Filters<PersonInvitation> {

		private static final long serialVersionUID = -9189492887286082033L;

		private Checkbox includeInviters;

		private Checkbox includeUniversities;

		private Checkbox includeGuests;

		/** Constructor.
		 *
		 * @param onSearch
		 */
		public IncomingInvitationFilters(Runnable onSearch) {
			super(onSearch);
		}

		@Override
		protected Component getOptionsComponent() {
			this.includeInviters = new Checkbox(true);
			this.includeUniversities = new Checkbox(true);
			this.includeGuests = new Checkbox(true);

			final var options = new HorizontalLayout();
			options.setSpacing(false);
			options.add(this.includeInviters, this.includeUniversities, this.includeGuests);

			return options;
		}

		@Override
		protected void resetFilters() {
			this.includeInviters.setValue(Boolean.TRUE);
			this.includeUniversities.setValue(Boolean.TRUE);
			this.includeGuests.setValue(Boolean.TRUE);
		}

		@Override
		protected void buildQueryFor(String keywords, List<Predicate> predicates, Root<PersonInvitation> root,
				CriteriaBuilder criteriaBuilder) {
			if (this.includeInviters.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("inviter")), keywords)); //$NON-NLS-1$
			}
			if (this.includeUniversities.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("university")), keywords)); //$NON-NLS-1$
			}
			if (this.includeGuests.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("guest")), keywords)); //$NON-NLS-1$
			}
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			this.includeInviters.setLabel(getTranslation("views.filters.include_inviters")); //$NON-NLS-1$
			this.includeUniversities.setLabel(getTranslation("views.filters.include_universities")); //$NON-NLS-1$
			this.includeGuests.setLabel(getTranslation("views.filters.include_guests")); //$NON-NLS-1$
		}

	}

	/** Provider of data for incoming invitations to be displayed in the list of invitations view.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	@FunctionalInterface
	protected interface IncomingInvitationDataProvider {

		/** Fetch incoming invitations data.
		 *
		 * @param invitationService the service to have access to the JPA.
		 * @param pageRequest the request for paging the data.
		 * @param filters the filters to apply for selecting the data.
		 * @return the lazy data page.
		 */
		Page<PersonInvitation> fetch(PersonInvitationService invitationService, PageRequest pageRequest, Filters<PersonInvitation> filters);

	}

}
