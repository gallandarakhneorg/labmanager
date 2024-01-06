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

import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.services.member.MembershipService;
import fr.utbm.ciad.labmanager.views.components.addons.avatars.AvatarItem;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractTwoLevelTreeListView;
import fr.utbm.ciad.labmanager.views.components.addons.entities.TreeListEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;

/** List all the memberships.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class StandardMembershipListView extends AbstractTwoLevelTreeListView<Person, Membership> {

	private static final long serialVersionUID = -5070828159033969321L;

	private MembershipService membershipService;

	private Column<TreeListEntity<Person, Membership>> organizationColumn;

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
		super(Person.class, Membership.class, authenticatedUser, messages, logger);
		this.membershipService = membershipService;
		setHoverMenu(isAdminRole());
		setRootEntityFetcher(
				(parentId, pageRequest, filters) -> {
					return this.membershipService.getAllPersonsWithMemberships(pageRequest, filters);
				},
				(rootEntity) -> {
					return rootEntity.getMemberships().size();
				});
		setChildEntityFetcher((parentId, pageRequest, filters) -> {
			return this.membershipService.getMembershipsForPerson(parentId, pageRequest, filters);
		});
		initializeDataInGrid(getGrid(), getFilters());
	}

	@Override
	protected Filters<Membership> createFilters() {
		return new MembershipFilters(this::refreshGrid);
	}

	@Override
	protected void configureFirstColumn(Column<TreeListEntity<Person, Membership>> rootColumn) {
		super.configureFirstColumn(rootColumn);
		//TODO rootColumn.setSortProperty("person"); //$NON-NLS-1$
	}

	@Override
	protected Component createRootEntityComponent(Person entity) {
		final var avatar = new AvatarItem();
		avatar.setHeading(entity.getFullNameWithLastNameFirst());
		final var contact = entity.getEmail();
		if (!Strings.isNullOrEmpty(contact)) {
			avatar.setDescription(contact);
		}
		final var url = entity.getPhotoURL();
		if (url != null) {
			avatar.setAvatarURL(url.toExternalForm());
		}
		return avatar;
	}

	@Override
	protected Component createChildEntityComponent(Membership entity) {
		final var position = new Span(entity.getMemberStatus().getLabel(getMessageSourceAccessor(), null, false, getLocale()));

		final var sdate = entity.getMemberSinceWhen();
		final var edate = entity.getMemberToWhen();
		String periodStr;
		if (sdate == null) {
			if (edate == null) {
				periodStr = ""; //$NON-NLS-1$
			} else {
				periodStr = getTranslation("views.membership.date.to", Integer.toString(edate.getYear())); //$NON-NLS-1$
			}
		} else if (edate == null) {
			periodStr = getTranslation("views.membership.date.since", Integer.toString(sdate.getYear())); //$NON-NLS-1$
		} else {
			final var year0 = sdate.getYear();
			final var year1 = edate.getYear();
			if (year0 == year1) {
				periodStr = getTranslation("views.membership.date.single", Integer.toString(year0)); //$NON-NLS-1$
			} else {
				periodStr = getTranslation("views.membership.date.since_to", Integer.toString(year0), Integer.toString(year1)); //$NON-NLS-1$
			}
		}

		final var period = new Span(periodStr);
		period.getStyle()
			.set("color", "var(--lumo-secondary-text-color)")  //$NON-NLS-1$//$NON-NLS-2$
			.set("font-size", "var(--lumo-font-size-s)"); //$NON-NLS-1$ //$NON-NLS-2$

		final var column = new VerticalLayout(position, period);
		column.getStyle().set("line-height", "var(--lumo-line-height-m)"); //$NON-NLS-1$ //$NON-NLS-2$
		column.setPadding(false);
		column.setSpacing(false);
		return column;
	}

	@Override
	protected void createAdditionalColumns(TreeGrid<TreeListEntity<Person, Membership>> grid) {
		this.organizationColumn = grid.addColumn(this::getOrganizationLabel)
				.setAutoWidth(true)
				.setSortProperty("researchOrganization"); //$NON-NLS-1$
	}

	private String getOrganizationLabel(TreeListEntity<Person, Membership> entity) {
		// TODO
		return "?";
	}

	@Override
	protected void addEntity() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void deleteCurrentSelection() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void deleteWithQuery(Set<TreeListEntity<Person, Membership>> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void edit(TreeListEntity<Person, Membership> entity) {
		// TODO Auto-generated method stub

	}

	//	@Override
	//	protected void deleteWithQuery(Set<Membership> memberships) {
	//		if (!memberships.isEmpty()) {
	//			final var size = memberships.size();
	//			ComponentFactory.createDeletionDialog(this,
	//					getTranslation("views.memberships.delete.title", Integer.valueOf(size)), //$NON-NLS-1$
	//					getTranslation("views.memberships.delete.message", Integer.valueOf(size)), //$NON-NLS-1$
	//					it ->  deleteCurrentSelection())
	//			.open();
	//		}
	//	}
	//
	//	@Override
	//	protected void deleteCurrentSelection() {
	//		try {
	//			// Get the selection again because this handler is run in another session than the one of the function
	//			var realSize = 0;
	//			final var grd = getGrid();
	//			final var log = getLogger();
	//			final var userName = AuthenticatedUser.getUserName(getAuthenticatedUser());
	//			for (final var membership : new ArrayList<>(grd.getSelectedItems())) {
	//				this.membershipService.removeMembership(membership.getId());
	//				final var msg = new StringBuilder("Membership: "); //$NON-NLS-1$
	//				msg.append(membership.getMemberStatus().name());
	//				msg.append(" (id: "); //$NON-NLS-1$
	//				msg.append(membership.getId());
	//				msg.append(") has been deleted by "); //$NON-NLS-1$
	//				msg.append(userName);
	//				log.info(msg.toString());
	//				// Deselected the supervision
	//				grd.getSelectionModel().deselect(membership);
	//				++realSize;
	//			}
	//			refreshGrid();
	//			notifyDeleted(realSize);
	//		} catch (Throwable ex) {
	//			refreshGrid();
	//			notifyDeletionError(ex);
	//		}
	//	}
	//
	//	/** Notify the user that the memberships were deleted.
	//	 *
	//	 * @param size the number of deleted memberships
	//	 */
	//	protected void notifyDeleted(int size) {
	//		notifyDeleted(size, "views.membership.delete_success"); //$NON-NLS-1$
	//	}
	//
	//	/** Notify the user that the memberships cannot be deleted.
	//	 */
	//	protected void notifyDeletionError(Throwable error) {
	//		notifyDeletionError(error, "views.membership.delete_error"); //$NON-NLS-1$
	//	}
	//
	//	@Override
	//	protected void addEntity() {
	//		openMembershipEditor(new Membership(), getTranslation("views.membership.add_membership")); //$NON-NLS-1$
	//	}
	//
	//	@Override
	//	protected void edit(Membership membership) {
	//		openMembershipEditor(membership, getTranslation("views.membership.edit_membership", //$NON-NLS-1$
	//				membership.getMemberStatus().getLabel(getMessageSourceAccessor(), null, false, getLocale())));
	//	}
	//
	//	/** Show the editor of a membership.
	//	 *
	//	 * @param membership the membership to edit.
	//	 * @param title the title of the editor.
	//	 */
	//	protected void openMembershipEditor(Membership membership, String title) {
	//		final var editor = new EmbeddedMembershipEditor(
	//				this.membershipService.startEditing(membership),
	//				getAuthenticatedUser(), getMessageSourceAccessor());
	//		ComponentFactory.openEditionModalDialog(title, editor, false,
	//				// Refresh the "old" item, even if its has been changed in the JPA database
	//				dialog -> refreshItem(membership),
	//				null);
	//	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		getFirstColumn().setHeader(getTranslation("views.person")); //$NON-NLS-1$
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
				//TODO predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("person")), keywords)); //$NON-NLS-1$
			}
			if (this.includeTypes.getValue() == Boolean.TRUE) {
				//TODO predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("memberStatus")), keywords)); //$NON-NLS-1$
			}
			if (this.includeOrganizations.getValue() == Boolean.TRUE) {
				//TODO predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("researchOrganization")), keywords)); //$NON-NLS-1$
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

}
