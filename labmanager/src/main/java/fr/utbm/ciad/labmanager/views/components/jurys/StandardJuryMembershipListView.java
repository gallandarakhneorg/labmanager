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

package fr.utbm.ciad.labmanager.views.components.jurys;

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
import fr.utbm.ciad.labmanager.data.jury.JuryMembership;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityDeletingContext;
import fr.utbm.ciad.labmanager.services.jury.JuryMembershipService;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityListView;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/** List all the jury memberships.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class StandardJuryMembershipListView extends AbstractEntityListView<JuryMembership> {

	private static final long serialVersionUID = 7493099024816373107L;

	private final JuryMembershipDataProvider dataProvider;

	private JuryMembershipService membershipService;

	private Column<JuryMembership> candidateColumn;

	private Column<JuryMembership> defenseTypeColumn;

	private Column<JuryMembership> universityColumn;

	private Column<JuryMembership> dateColumn;

	private Column<JuryMembership> participantColumn;

	private Column<JuryMembership> roleColumn;

	/** Constructor.
	 *
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param membershipService the service for accessing the jury memberships.
	 * @param logger the logger to use.
	 */
	public StandardJuryMembershipListView(
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			JuryMembershipService membershipService, Logger logger) {
		super(JuryMembership.class, authenticatedUser, messages, logger,
				"views.jury_membership.delete.title", //$NON-NLS-1$
				"views.jury_membership.delete.message", //$NON-NLS-1$
				"views.jury_memberships.delete_success", //$NON-NLS-1$
				"views.jury_memberships.delete_error"); //$NON-NLS-1$
		this.membershipService = membershipService;
		this.dataProvider = (ps, query, filters) -> ps.getAllJuryMemberships(query, filters);
		initializeDataInGrid(getGrid(), getFilters());
	}

	@Override
	protected Filters<JuryMembership> createFilters() {
		return new JuryMembershipFilters(this::refreshGrid);
	}

	@Override
	protected boolean createGridColumns(Grid<JuryMembership> grid) {
		this.candidateColumn = grid.addColumn(this::getCandidateLabel)
				.setAutoWidth(true)
				.setSortProperty("candidate"); //$NON-NLS-1$
		this.defenseTypeColumn = grid.addColumn(this::getDefenseTypeLabel)
				.setAutoWidth(true)
				.setSortProperty("defenseType"); //$NON-NLS-1$
		this.universityColumn = grid.addColumn(membership -> membership.getUniversity())
				.setAutoWidth(true)
				.setSortProperty("university"); //$NON-NLS-1$
		this.dateColumn = grid.addColumn(this::getDateLabel)
				.setAutoWidth(true)
				.setSortProperty("date"); //$NON-NLS-1$
		this.participantColumn = grid.addColumn(new ComponentRenderer<>(this::createNameComponent))
				.setAutoWidth(true)
				.setSortProperty("person"); //$NON-NLS-1$
		this.roleColumn = grid.addColumn(this::getRoleLabel)
				.setAutoWidth(true)
				.setSortProperty("type"); //$NON-NLS-1$
		return isAdminRole();
	}

	private String getRoleLabel(JuryMembership membership) {
		// TODO: Support gender
		return membership.getType().getLabel(getMessageSourceAccessor(), null, getLocale());
	}

	private String getDefenseTypeLabel(JuryMembership membership) {
		return membership.getDefenseType().getLabel(getMessageSourceAccessor(), getLocale());
	}

	private String getCandidateLabel(JuryMembership membership) {
		// TODO
		return "";
	}

	private String getDateLabel(JuryMembership membership) {
		return Integer.toString(membership.getDate().getYear());
	}

	private Component createNameComponent(JuryMembership membership) {
		// TODO
		return new Span("");
	}

	@Override
	protected Column<JuryMembership> getInitialSortingColumn() {
		return this.dateColumn;
	}

	@Override
	protected FetchCallback<JuryMembership, Void> getFetchCallback(Filters<JuryMembership> filters) {
		return query -> {
			return this.dataProvider.fetch(
					this.membershipService,
					VaadinSpringDataHelpers.toSpringPageRequest(query),
					filters).stream();
		};
	}

	@Override
	protected void addEntity() {
		openMembershipEditor(new JuryMembership(), getTranslation("views.jury_membership.add_membership")); //$NON-NLS-1$
	}

	@Override
	protected void edit(JuryMembership membership) {
		openMembershipEditor(membership, getTranslation("views.jury_membership.edit_membership", membership.getTitle())); //$NON-NLS-1$
	}

	/** Show the editor of a jury membership.
	 *
	 * @param membership the jury membership to edit.
	 * @param title the title of the editor.
	 */
	protected void openMembershipEditor(JuryMembership membership, String title) {
		final var editor = new EmbeddedJuryMembershipEditor(
				this.membershipService.startEditing(membership),
				getAuthenticatedUser(), getMessageSourceAccessor());
		ComponentFactory.openEditionModalDialog(title, editor, false,
				// Refresh the "old" item, even if its has been changed in the JPA database
				dialog -> refreshItem(membership),
				null);
	}

	@Override
	protected EntityDeletingContext<JuryMembership> createDeletionContextFor(Set<JuryMembership> entities) {
		return this.membershipService.startDeletion(entities);
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.candidateColumn.setHeader(getTranslation("views.candidates")); //$NON-NLS-1$
		this.defenseTypeColumn.setHeader(getTranslation("views.type")); //$NON-NLS-1$
		this.universityColumn.setHeader(getTranslation("views.university")); //$NON-NLS-1$
		this.dateColumn.setHeader(getTranslation("views.date")); //$NON-NLS-1$
		this.participantColumn.setHeader(getTranslation("views.participant")); //$NON-NLS-1$
		this.roleColumn.setHeader(getTranslation("views.role")); //$NON-NLS-1$
	}

	/** UI and JPA filters for {@link StandardJuryMembershipListView}.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class JuryMembershipFilters extends Filters<JuryMembership> {

		private static final long serialVersionUID = 5727620980092385680L;

		private Checkbox includeCandidates;

		private Checkbox includeUniversities;

		private Checkbox includeParticipants;

		private Checkbox includeRoles;

		/** Constructor.
		 *
		 * @param onSearch
		 */
		public JuryMembershipFilters(Runnable onSearch) {
			super(onSearch);
		}

		@Override
		protected Component getOptionsComponent() {
			this.includeCandidates = new Checkbox(true);
			this.includeUniversities = new Checkbox(true);
			this.includeParticipants = new Checkbox(true);
			this.includeRoles = new Checkbox(true);

			final var options = new HorizontalLayout();
			options.setSpacing(false);
			options.add(this.includeCandidates, this.includeUniversities, this.includeParticipants, this.includeRoles);

			return options;
		}

		@Override
		protected void resetFilters() {
			this.includeCandidates.setValue(Boolean.TRUE);
			this.includeUniversities.setValue(Boolean.TRUE);
			this.includeParticipants.setValue(Boolean.TRUE);
			this.includeRoles.setValue(Boolean.TRUE);
		}

		@Override
		protected void buildQueryFor(String keywords, List<Predicate> predicates, Root<JuryMembership> root,
				CriteriaBuilder criteriaBuilder) {
			if (this.includeCandidates.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("candidate")), keywords)); //$NON-NLS-1$
			}
			if (this.includeUniversities.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("defenseType")), keywords)); //$NON-NLS-1$
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("university")), keywords)); //$NON-NLS-1$
			}
			if (this.includeParticipants.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("person")), keywords)); //$NON-NLS-1$
			}
			if (this.includeRoles.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("type")), keywords)); //$NON-NLS-1$
			}
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			this.includeCandidates.setLabel(getTranslation("views.filters.include_candidates")); //$NON-NLS-1$
			this.includeUniversities.setLabel(getTranslation("views.filters.include_universities")); //$NON-NLS-1$
			this.includeParticipants.setLabel(getTranslation("views.filters.include_participants")); //$NON-NLS-1$
			this.includeRoles.setLabel(getTranslation("views.filters.include_roles")); //$NON-NLS-1$
		}

	}

	/** Provider of data for jury memberships to be displayed in the list of memberships view.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	@FunctionalInterface
	protected interface JuryMembershipDataProvider {

		/** Fetch jury memberships data.
		 *
		 * @param membershipService the service to have access to the JPA.
		 * @param pageRequest the request for paging the data.
		 * @param filters the filters to apply for selecting the data.
		 * @return the lazy data page.
		 */
		Page<JuryMembership> fetch(JuryMembershipService membershipService, PageRequest pageRequest, Filters<JuryMembership> filters);

	}

}
