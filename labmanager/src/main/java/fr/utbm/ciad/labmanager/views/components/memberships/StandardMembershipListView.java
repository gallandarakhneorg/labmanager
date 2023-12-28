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

package fr.utbm.ciad.labmanager.views.components.memberships;

import java.util.ArrayList;
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
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.services.member.MembershipService;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityListView;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/** List all the memberships.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class StandardMembershipListView extends AbstractEntityListView<Membership> {

	private static final long serialVersionUID = 2041226012686503537L;

	private final MembershipDataProvider dataProvider;

	private MembershipService membershipService;

	private Column<Membership> personColumn;

	private Column<Membership> positionColumn;

	private Column<Membership> dateColumn;

	private Column<Membership> organizationColumn;

	/** Constructor.
	 *
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param membershipService the service for accessing the memberships.
	 * @param logger the logger to use.
	 */
	public StandardMembershipListView(
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			MembershipService membershipService, Logger logger) {
		super(Membership.class, authenticatedUser, messages, logger);
		this.membershipService = membershipService;
		this.dataProvider = (ps, query, filters) -> ps.getAllMemberships(query, filters);
	}

	@Override
	protected Filters<Membership> createFilters() {
		return new MembershipFilters(this::refreshGrid);
	}

	@Override
	protected boolean createGridColumns(Grid<Membership> grid) {
		this.personColumn = grid.addColumn(new ComponentRenderer<>(this::createPersonComponent))
				.setAutoWidth(true)
				.setSortProperty("person"); //$NON-NLS-1$
		this.positionColumn = grid.addColumn(this::getPositionLabel)
				.setAutoWidth(true)
				.setSortProperty("memberStatus"); //$NON-NLS-1$
		this.dateColumn = grid.addColumn(this::getDateLabel)
				.setAutoWidth(true)
				.setSortProperty("memberSinceWhen", "memberToWhen"); //$NON-NLS-1$ //$NON-NLS-2$
		this.organizationColumn = grid.addColumn(this::getOrganizationLabel)
				.setAutoWidth(true)
				.setSortProperty("researchOrganization"); //$NON-NLS-1$
		return isAdminRole();
	}

	private Component createPersonComponent(Membership membership) {
		// TODO
		return new Span("?");
	}

	private String getPositionLabel(Membership membership) {
		// TODO Gender
		return membership.getMemberStatus().getLabel(getMessageSourceAccessor(), null, false, getLocale());
	}

	private String getDateLabel(Membership membership) {
		final var sdate = membership.getMemberSinceWhen();
		final var edate = membership.getMemberToWhen();
		if (sdate == null) {
			if (edate == null) {
				return ""; //$NON-NLS-1$
			}
			return getTranslation("views.membership.date.to", Integer.toString(edate.getYear())); //$NON-NLS-1$
		} else if (edate == null) {
			return getTranslation("views.membership.date.since", Integer.toString(sdate.getYear())); //$NON-NLS-1$
		}
		if (sdate.getYear() == edate.getYear()) {
			return getTranslation("views.membership.date.single", Integer.toString(sdate.getYear())); //$NON-NLS-1$
		}
		return getTranslation("views.membership.date.since_to", Integer.toString(sdate.getYear()), Integer.toString(edate.getYear())); //$NON-NLS-1$
	}

	private String getOrganizationLabel(Membership membership) {
		// TODO
		return "?";
	}

	@Override
	protected Column<Membership> getInitialSortingColumn() {
		return this.personColumn;
	}

	@Override
	protected FetchCallback<Membership, Void> getFetchCallback(Filters<Membership> filters) {
		return query -> {
			return this.dataProvider.fetch(
					this.membershipService,
					VaadinSpringDataHelpers.toSpringPageRequest(query),
					filters).stream();
		};
	}

	@Override
	protected void deleteWithQuery(Set<Membership> memberships) {
		if (!memberships.isEmpty()) {
			final var size = memberships.size();
			ComponentFactory.createDeletionDialog(this,
					getTranslation("views.memberships.delete.title", Integer.valueOf(size)), //$NON-NLS-1$
					getTranslation("views.memberships.delete.message", Integer.valueOf(size)), //$NON-NLS-1$
					it ->  deleteCurrentSelection())
			.open();
		}
	}

	@Override
	protected void deleteCurrentSelection() {
		try {
			// Get the selection again because this handler is run in another session than the one of the function
			var realSize = 0;
			final var grd = getGrid();
			final var log = getLogger();
			final var userName = AuthenticatedUser.getUserName(getAuthenticatedUser());
			for (final var membership : new ArrayList<>(grd.getSelectedItems())) {
				this.membershipService.removeMembership(membership.getId());
				final var msg = new StringBuilder("Membership: "); //$NON-NLS-1$
				msg.append(membership.getMemberStatus().name());
				msg.append(" (id: "); //$NON-NLS-1$
				msg.append(membership.getId());
				msg.append(") has been deleted by "); //$NON-NLS-1$
				msg.append(userName);
				log.info(msg.toString());
				// Deselected the supervision
				grd.getSelectionModel().deselect(membership);
				++realSize;
			}
			refreshGrid();
			notifyDeleted(realSize);
		} catch (Throwable ex) {
			refreshGrid();
			notifyDeletionError(ex);
		}
	}

	/** Notify the user that the memberships were deleted.
	 *
	 * @param size the number of deleted memberships
	 */
	protected void notifyDeleted(int size) {
		notifyDeleted(size, "views.membership.delete_success"); //$NON-NLS-1$
	}

	/** Notify the user that the memberships cannot be deleted.
	 */
	protected void notifyDeletionError(Throwable error) {
		notifyDeletionError(error, "views.membership.delete_error"); //$NON-NLS-1$
	}

	@Override
	protected void addEntity() {
		openMembershipEditor(new Membership(), getTranslation("views.membership.add_membership")); //$NON-NLS-1$
	}

	@Override
	protected void edit(Membership membership) {
		openMembershipEditor(membership, getTranslation("views.membership.edit_membership", //$NON-NLS-1$
				membership.getMemberStatus().getLabel(getMessageSourceAccessor(), null, false, getLocale())));
	}

	/** Show the editor of a membership.
	 *
	 * @param membership the membership to edit.
	 * @param title the title of the editor.
	 */
	protected void openMembershipEditor(Membership membership, String title) {
		final var editor = new EmbeddedMembershipEditor(
				this.membershipService.startEditing(membership),
				getAuthenticatedUser(), getMessageSourceAccessor());
		ComponentFactory.openEditionModalDialog(title, editor, false,
				// Refresh the "old" item, even if its has been changed in the JPA database
				dialog -> refreshItem(membership),
				null);
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.personColumn.setHeader(getTranslation("views.person")); //$NON-NLS-1$
		this.positionColumn.setHeader(getTranslation("views.position")); //$NON-NLS-1$
		this.dateColumn.setHeader(getTranslation("views.date")); //$NON-NLS-1$
		this.organizationColumn.setHeader(getTranslation("views.organization")); //$NON-NLS-1$
	}

	/** UI and JPA filters for {@link StandardMembershipListView}.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class MembershipFilters extends Filters<Membership> {

		private static final long serialVersionUID = -7866307628748739653L;

		private Checkbox includePersons;

		private Checkbox includeTypes;

		private Checkbox includeOrganizations;

		/** Constructor.
		 *
		 * @param onSearch
		 */
		public MembershipFilters(Runnable onSearch) {
			super(onSearch);
		}

		@Override
		protected Component getOptionsComponent() {
			this.includePersons = new Checkbox(true);
			this.includeTypes = new Checkbox(true);
			this.includeOrganizations = new Checkbox(true);

			final var options = new HorizontalLayout();
			options.setSpacing(false);
			options.add(this.includePersons, this.includeTypes, this.includeOrganizations);

			return options;
		}

		@Override
		protected void resetFilters() {
			this.includePersons.setValue(Boolean.TRUE);
			this.includeTypes.setValue(Boolean.TRUE);
			this.includeOrganizations.setValue(Boolean.TRUE);
		}

		@Override
		protected void buildQueryFor(String keywords, List<Predicate> predicates, Root<Membership> root,
				CriteriaBuilder criteriaBuilder) {
			if (this.includePersons.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("person")), keywords)); //$NON-NLS-1$
			}
			if (this.includeTypes.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("memberStatus")), keywords)); //$NON-NLS-1$
			}
			if (this.includeOrganizations.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("researchOrganization")), keywords)); //$NON-NLS-1$
			}
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			this.includePersons.setLabel(getTranslation("views.filters.include_persons")); //$NON-NLS-1$
			this.includeTypes.setLabel(getTranslation("views.filters.include_types")); //$NON-NLS-1$
			this.includeOrganizations.setLabel(getTranslation("views.filters.include_organizations")); //$NON-NLS-1$
		}

	}

	/** Provider of data for memberships to be displayed in the list of memberships view.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	@FunctionalInterface
	protected interface MembershipDataProvider {

		/** Fetch memberships data.
		 *
		 * @param membershipService the service to have access to the JPA.
		 * @param pageRequest the request for paging the data.
		 * @param filters the filters to apply for selecting the data.
		 * @return the lazy data page.
		 */
		Page<Membership> fetch(MembershipService membershipService, PageRequest pageRequest, Filters<Membership> filters);

	}

}
