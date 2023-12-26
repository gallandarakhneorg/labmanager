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

package fr.utbm.ciad.labmanager.views.components.assocstructures;

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
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructure;
import fr.utbm.ciad.labmanager.services.assostructure.AssociatedStructureService;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.badges.BadgeRenderer;
import fr.utbm.ciad.labmanager.views.components.addons.badges.BadgeState;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityListView;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/** List all the associated structures.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class StandardAssociatedStructureListView extends AbstractEntityListView<AssociatedStructure> {

	private static final long serialVersionUID = 3844606138630969467L;

	private final AssociatedStructureDataProvider dataProvider;

	private AssociatedStructureService structureService;

	private Column<AssociatedStructure> nameColumn;

	private Column<AssociatedStructure> dateColumn;

	private Column<AssociatedStructure> validationColumn;

	/** Constructor.
	 *
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param structureService the service for accessing the associated structures.
	 * @param logger the logger to use.
	 */
	public StandardAssociatedStructureListView(
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			AssociatedStructureService structureService, Logger logger) {
		super(AssociatedStructure.class, authenticatedUser, messages, logger);
		this.structureService = structureService;
		this.dataProvider = (ps, query, filters) -> ps.getAllAssociatedStructures(query, filters);
	}

	@Override
	protected Filters<AssociatedStructure> createFilters() {
		return new StructureFilters(this::refreshGrid);
	}

	@Override
	protected boolean createGridColumns(Grid<AssociatedStructure> grid) {
		this.nameColumn = grid.addColumn(structure -> structure.getName())
				.setRenderer(new ComponentRenderer<>(this::createNameComponent))
				.setFrozen(true)
				.setAutoWidth(true)
				.setSortProperty("acronym", "name"); //$NON-NLS-1$ //$NON-NLS-2$
		this.dateColumn = grid.addColumn(structure -> structure.getCreationDate())
				.setAutoWidth(true)
				.setSortProperty("creationDate"); //$NON-NLS-1$
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

	private Component createNameComponent(AssociatedStructure structure) {
		final var acronym = structure.getAcronym();
		final var name = structure.getName();
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
	protected Column<AssociatedStructure> getInitialSortingColumn() {
		return this.nameColumn;
	}

	@Override
	protected FetchCallback<AssociatedStructure, Void> getFetchCallback(Filters<AssociatedStructure> filters) {
		return query -> {
			return this.dataProvider.fetch(
					this.structureService,
					VaadinSpringDataHelpers.toSpringPageRequest(query),
					filters).stream();
		};
	}

	@Override
	protected void deleteWithQuery(Set<AssociatedStructure> structures) {
		if (!structures.isEmpty()) {
			final var size = structures.size();
			ComponentFactory.createDeletionDialog(this,
					getTranslation("views.associated_structures.delete.title", Integer.valueOf(size)), //$NON-NLS-1$
					getTranslation("views.associated_structures.delete.message", Integer.valueOf(size)), //$NON-NLS-1$
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
			for (final var structure : new ArrayList<>(grd.getSelectedItems())) {
				this.structureService.removeAssociatedStructure(structure.getId());
				final var msg = new StringBuilder("Associated structure: "); //$NON-NLS-1$
				msg.append(structure.getNameOrAcronym());
				msg.append(" (id: "); //$NON-NLS-1$
				msg.append(structure.getId());
				msg.append(") has been deleted by "); //$NON-NLS-1$
				msg.append(userName);
				log.info(msg.toString());
				// Deselected the address
				grd.getSelectionModel().deselect(structure);
				++realSize;
			}
			refreshGrid();
			notifyDeleted(realSize);
		} catch (Throwable ex) {
			refreshGrid();
			notifyDeletionError(ex);
		}
	}

	/** Notify the user that the structures were deleted.
	 *
	 * @param size the number of deleted structures
	 */
	protected void notifyDeleted(int size) {
		notifyDeleted(size, "views.associated_structure.delete_success"); //$NON-NLS-1$
	}

	/** Notify the user that the structures cannot be deleted.
	 */
	protected void notifyDeletionError(Throwable error) {
		notifyDeletionError(error, "views.associated_structure.delete_error"); //$NON-NLS-1$
	}

	@Override
	protected void addEntity() {
		openStructureEditor(new AssociatedStructure(), getTranslation("views.associated_structure.add_address")); //$NON-NLS-1$
	}

	@Override
	protected void edit(AssociatedStructure structure) {
		openStructureEditor(structure, getTranslation("views.associated_structure.edit_structure", structure.getAcronymOrName())); //$NON-NLS-1$
	}

	/** Show the editor of a structure.
	 *
	 * @param structure the structure to edit.
	 * @param title the title of the editor.
	 */
	protected void openStructureEditor(AssociatedStructure structure, String title) {
		final var editor = new EmbeddedAssociatedStructureEditor(
				this.structureService.startEditing(structure),
				getAuthenticatedUser(), getMessageSourceAccessor());
		ComponentFactory.openEditionModalDialog(title, editor, false,
				// Refresh the "old" item, even if its has been changed in the JPA database
				dialog -> refreshItem(structure));
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.nameColumn.setHeader(getTranslation("views.name")); //$NON-NLS-1$
		this.dateColumn.setHeader(getTranslation("views.associated_structure.creation_date.short")); //$NON-NLS-1$
		this.validationColumn.setHeader(getTranslation("views.validated")); //$NON-NLS-1$
	}

	/** UI and JPA filters for {@link StandardAssociatedStructureListView}.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class StructureFilters extends Filters<AssociatedStructure> {

		private static final long serialVersionUID = 520337561490106907L;

		private Checkbox includeNames;

		/** Constructor.
		 *
		 * @param onSearch
		 */
		public StructureFilters(Runnable onSearch) {
			super(onSearch);
		}

		@Override
		protected Component getOptionsComponent() {
			this.includeNames = new Checkbox(true);

			final var options = new HorizontalLayout();
			options.setSpacing(false);
			options.add(this.includeNames);

			return options;
		}

		@Override
		protected void resetFilters() {
			this.includeNames.setValue(Boolean.TRUE);
		}

		@Override
		protected void buildQueryFor(String keywords, List<Predicate> predicates, Root<AssociatedStructure> root,
				CriteriaBuilder criteriaBuilder) {
			if (this.includeNames.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), keywords)); //$NON-NLS-1$
			}
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			this.includeNames.setLabel(getTranslation("views.filters.include_names")); //$NON-NLS-1$
		}

	}

	/** Provider of data for associated structures to be displayed in the list of structures view.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	@FunctionalInterface
	protected interface AssociatedStructureDataProvider {

		/** Fetch associated structure data.
		 *
		 * @param structureService the service to have access to the JPA.
		 * @param pageRequest the request for paging the data.
		 * @param filters the filters to apply for selecting the data.
		 * @return the lazy data page.
		 */
		Page<AssociatedStructure> fetch(AssociatedStructureService structureService, PageRequest pageRequest, Filters<AssociatedStructure> filters);

	}

}
