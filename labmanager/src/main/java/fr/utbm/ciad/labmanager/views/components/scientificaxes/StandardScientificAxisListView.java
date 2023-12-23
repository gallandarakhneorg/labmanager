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

package fr.utbm.ciad.labmanager.views.components.scientificaxes;

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
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.scientificaxis.ScientificAxis;
import fr.utbm.ciad.labmanager.services.scientificaxis.ScientificAxisService;
import fr.utbm.ciad.labmanager.views.components.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.badges.BadgeRenderer;
import fr.utbm.ciad.labmanager.views.components.badges.BadgeState;
import fr.utbm.ciad.labmanager.views.components.entities.AbstractEntityListView;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/** List all the scientific axes.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class StandardScientificAxisListView extends AbstractEntityListView<ScientificAxis> {

	private static final long serialVersionUID = -1929465946434562506L;

	private final ScientificAxisDataProvider dataProvider;

	private ScientificAxisService axisService;

	private Column<ScientificAxis> nameColumn;

	private Column<ScientificAxis> startDateColumn;

	private Column<ScientificAxis> endDateColumn;

	private Column<ScientificAxis> validationColumn;

	/** Constructor.
	 *
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param axisService the service for accessing the scientific axes.
	 * @param logger the logger to use.
	 */
	public StandardScientificAxisListView(
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			ScientificAxisService axisService, Logger logger) {
		super(ScientificAxis.class, authenticatedUser, messages, logger);
		this.axisService = axisService;
		this.dataProvider = (ps, query, filters) -> ps.getAllScientificAxes(query, filters);
	}

	@Override
	protected Filters<ScientificAxis> createFilters() {
		return new AxisFilters(this::refreshGrid);
	}

	@Override
	protected boolean createGridColumns(Grid<ScientificAxis> grid) {
		this.nameColumn = grid.addColumn(address -> address.getName())
				.setRenderer(new ComponentRenderer<>(this::createNameComponent))
				.setAutoWidth(true)
				.setSortProperty("acronym", "name"); //$NON-NLS-1$ //$NON-NLS-2$
		this.startDateColumn = grid.addColumn(new LocalDateRenderer<>(it -> it.getStartDate()))
				.setAutoWidth(true)
				.setSortProperty("startDate"); //$NON-NLS-1$
		this.endDateColumn = grid.addColumn(new LocalDateRenderer<>(it -> it.getEndDate()))
				.setAutoWidth(true)
				.setSortProperty("endDate"); //$NON-NLS-1$
		this.validationColumn = grid.addColumn(new BadgeRenderer<>((data, callback) -> {
			if (data.isValidated()) {
				callback.create(BadgeState.SUCCESS, null, getTranslation("views.validated")); //$NON-NLS-1$
			} else {
				callback.create(BadgeState.ERROR, null, getTranslation("views.validable")); //$NON-NLS-1$
			}
		}))
				.setAutoWidth(true)
				.setFlexGrow(0)
				.setSortProperty("validated") //$NON-NLS-1$
				.setWidth("0%"); //$NON-NLS-1$
		// Create the hover tool bar only if administrator role
		return isAdminRole();
	}

	private Component createNameComponent(ScientificAxis axis) {
		final var acronym = axis.getAcronym();
		final var name = axis.getName();
		final var label = new StringBuilder();
		if (Strings.isNullOrEmpty(acronym)) {
			label.append(name);
		} else if (Strings.isNullOrEmpty(name)) {
			label.append(acronym);
		} else {
			label.append(acronym).append(" - ").append(name); //$NON-NLS-1$
		}
		return new Span(label.toString());
	}

	@Override
	protected Column<ScientificAxis> getInitialSortingColumn() {
		return this.nameColumn;
	}

	@Override
	protected FetchCallback<ScientificAxis, Void> getFetchCallback(Filters<ScientificAxis> filters) {
		return query -> {
			return this.dataProvider.fetch(
					this.axisService,
					VaadinSpringDataHelpers.toSpringPageRequest(query),
					filters).stream();
		};
	}

	@Override
	protected void deleteWithQuery(Set<ScientificAxis> axes) {
		if (!axes.isEmpty()) {
			final var size = axes.size();
			ComponentFactory.createDeletionDialog(this,
					getTranslation("views.addresses.delete.title", Integer.valueOf(size)), //$NON-NLS-1$
					getTranslation("views.addresses.delete.message", Integer.valueOf(size)), //$NON-NLS-1$
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
			for (final var axis : new ArrayList<>(grd.getSelectedItems())) {
				this.axisService.removeScientificAxis(axis.getId());
				final var msg = new StringBuilder("Scientific axis: "); //$NON-NLS-1$
				msg.append(axis.getNameOrAcronym());
				msg.append(" (id: "); //$NON-NLS-1$
				msg.append(axis.getId());
				msg.append(") has been deleted by "); //$NON-NLS-1$
				msg.append(userName);
				log.info(msg.toString());
				// Deselected the address
				grd.getSelectionModel().deselect(axis);
				++realSize;
			}
			refreshGrid();
			notifyDeleted(realSize);
		} catch (Throwable ex) {
			refreshGrid();
			notifyDeletionError(ex);
		}
	}

	/** Notify the user that the addresses were deleted.
	 *
	 * @param size the number of deleted addresses
	 */
	protected void notifyDeleted(int size) {
		notifyDeleted(size, "views.scientific_axes.delete_success"); //$NON-NLS-1$
	}

	/** Notify the user that the addresses cannot be deleted.
	 */
	protected void notifyDeletionError(Throwable error) {
		notifyDeletionError(error, "views.scientific_axes.delete_error"); //$NON-NLS-1$
	}

	@Override
	protected void addEntity() {
		openAxisEditor(new ScientificAxis(), getTranslation("views.scientific_axes.add_axis")); //$NON-NLS-1$
	}

	@Override
	protected void edit(ScientificAxis axis) {
		openAxisEditor(axis, getTranslation("views.scientific_axes.edit_axis", axis.getAcronymOrName())); //$NON-NLS-1$
	}

	/** Show the editor of an axis.
	 *
	 * @param axis the axis to edit.
	 * @param title the title of the editor.
	 */
	protected void openAxisEditor(ScientificAxis axis, String title) {
		final var editor = new EmbeddedScientificAxisEditor(
				this.axisService.startEditing(axis),
				getAuthenticatedUser(), getMessageSourceAccessor());
		ComponentFactory.openEditionModalDialog(title, editor, false,
				// Refresh the "old" item, even if its has been changed in the JPA database
				dialog -> refreshItem(axis));
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.nameColumn.setHeader(getTranslation("views.name")); //$NON-NLS-1$
		this.startDateColumn.setHeader(getTranslation("views.date.start")); //$NON-NLS-1$
		this.endDateColumn.setHeader(getTranslation("views.date.end")); //$NON-NLS-1$
		this.validationColumn.setHeader(getTranslation("views.validated")); //$NON-NLS-1$
	}

	/** UI and JPA filters for {@link StandardScientificAxisListView}.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class AxisFilters extends Filters<ScientificAxis> {

		private static final long serialVersionUID = 7590936731361248312L;

		private Checkbox includeAcronyms;

		private Checkbox includeNames;

		private Checkbox includeDates;

		/** Constructor.
		 *
		 * @param onSearch
		 */
		public AxisFilters(Runnable onSearch) {
			super(onSearch);
		}

		@Override
		protected Component getOptionsComponent() {
			this.includeAcronyms = new Checkbox(true);
			this.includeNames = new Checkbox(true);
			this.includeDates = new Checkbox(true);

			final var options = new HorizontalLayout();
			options.setSpacing(false);
			options.add(this.includeAcronyms, this.includeNames, this.includeDates);

			return options;
		}

		@Override
		protected void resetFilters() {
			this.includeAcronyms.setValue(Boolean.TRUE);
			this.includeNames.setValue(Boolean.TRUE);
			this.includeDates.setValue(Boolean.TRUE);
		}

		@Override
		protected void buildQueryFor(String keywords, List<Predicate> predicates, Root<ScientificAxis> root,
				CriteriaBuilder criteriaBuilder) {
			if (this.includeAcronyms.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("acronym")), keywords)); //$NON-NLS-1$
			}
			if (this.includeNames.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), keywords)); //$NON-NLS-1$
			}
			if (this.includeDates.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.toString(root.get("startDate"))), keywords)); //$NON-NLS-1$
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.toString(root.get("endDate"))), keywords)); //$NON-NLS-1$
			}
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			this.includeAcronyms.setLabel(getTranslation("views.filters.include_acronyms")); //$NON-NLS-1$
			this.includeNames.setLabel(getTranslation("views.filters.include_names")); //$NON-NLS-1$
			this.includeDates.setLabel(getTranslation("views.filters.include_dates")); //$NON-NLS-1$
		}

	}

	/** Provider of data for scientific axes to be displayed in the list of axes view.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	@FunctionalInterface
	protected interface ScientificAxisDataProvider {

		/** Fetch scientific axis data.
		 *
		 * @param axisService the service to have access to the JPA.
		 * @param pageRequest the request for paging the data.
		 * @param filters the filters to apply for selecting the data.
		 * @return the lazy data page.
		 */
		Page<ScientificAxis> fetch(ScientificAxisService axisService, PageRequest pageRequest, Filters<ScientificAxis> filters);

	}

}
