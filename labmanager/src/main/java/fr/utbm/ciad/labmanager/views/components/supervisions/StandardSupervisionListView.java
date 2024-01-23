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

package fr.utbm.ciad.labmanager.views.components.supervisions;

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
import fr.utbm.ciad.labmanager.data.supervision.Supervision;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityDeletingContext;
import fr.utbm.ciad.labmanager.services.supervision.SupervisionService;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityListView;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/** List all the supervisions.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class StandardSupervisionListView extends AbstractEntityListView<Supervision> {

	private static final long serialVersionUID = -7908569503954366686L;

	private final SupervisionDataProvider dataProvider;

	private SupervisionService supervisionService;

	private Column<Supervision> supervisedPersonColumn;

	private Column<Supervision> typeColumn;

	private Column<Supervision> dateColumn;

	private Column<Supervision> supervisorsColumn;

	/** Constructor.
	 *
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param supervisionService the service for accessing the supervisions.
	 * @param logger the logger to use.
	 */
	public StandardSupervisionListView(
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			SupervisionService supervisionService, Logger logger) {
		super(Supervision.class, authenticatedUser, messages, logger,
				"views.supervisions.delete.title", //$NON-NLS-1$
				"views.supervisions.delete.message", //$NON-NLS-1$
				"views.supervision.delete_success", //$NON-NLS-1$
				"views.supervision.delete_error"); //$NON-NLS-1$
		this.supervisionService = supervisionService;
		this.dataProvider = (ps, query, filters) -> ps.getAllSupervisions(query, filters);
		initializeDataInGrid(getGrid(), getFilters());
	}

	@Override
	protected Filters<Supervision> createFilters() {
		return new SupervisionFilters(this::refreshGrid);
	}

	@Override
	protected boolean createGridColumns(Grid<Supervision> grid) {
		this.supervisedPersonColumn = grid.addColumn(new ComponentRenderer<>(this::createSupervisedPersonComponent))
				.setAutoWidth(true)
				.setSortProperty("supervisedPerson"); //$NON-NLS-1$
		this.typeColumn = grid.addColumn(this::getTypeLabel)
				.setAutoWidth(true)
				.setSortProperty("type"); //$NON-NLS-1$
		this.dateColumn = grid.addColumn(this::getDateLabel)
				.setAutoWidth(true)
				.setSortProperty("startDate", "endDate"); //$NON-NLS-1$ //$NON-NLS-2$
		this.supervisorsColumn = grid.addColumn(this::getSupervisorsLabel)
				.setAutoWidth(true)
				.setSortProperty("supervisors"); //$NON-NLS-1$
		return isAdminRole();
	}

	private Component createSupervisedPersonComponent(Supervision supervision) {
		// TODO
		return new Span("?");
	}

	private String getTypeLabel(Supervision supervision) {
		// TODO
		return "?";
	}

	private String getDateLabel(Supervision supervision) {
		// TODO
		return "?";
	}

	private String getSupervisorsLabel(Supervision supervision) {
		// TODO
		return "?";
	}

	@Override
	protected Column<Supervision> getInitialSortingColumn() {
		return this.supervisedPersonColumn;
	}

	@Override
	protected FetchCallback<Supervision, Void> getFetchCallback(Filters<Supervision> filters) {
		return query -> {
			return this.dataProvider.fetch(
					this.supervisionService,
					VaadinSpringDataHelpers.toSpringPageRequest(query),
					filters).stream();
		};
	}

	@Override
	protected void addEntity() {
		openSupervisionEditor(new Supervision(), getTranslation("views.supervision.add_supervision")); //$NON-NLS-1$
	}

	@Override
	protected void edit(Supervision supervision) {
		openSupervisionEditor(supervision, getTranslation("views.supervision.edit_supervision", supervision.getTitle())); //$NON-NLS-1$
	}

	/** Show the editor of a supervision.
	 *
	 * @param supervision the supervision to edit.
	 * @param title the title of the editor.
	 */
	protected void openSupervisionEditor(Supervision supervision, String title) {
		final var editor = new EmbeddedSupervisionEditor(
				this.supervisionService.startEditing(supervision),
				getAuthenticatedUser(), getMessageSourceAccessor());
		ComponentFactory.openEditionModalDialog(title, editor, false,
				// Refresh the "old" item, even if its has been changed in the JPA database
				dialog -> refreshItem(supervision),
				null);
	}

	@Override
	protected EntityDeletingContext<Supervision> createDeletionContextFor(Set<Supervision> entities) {
		return this.supervisionService.startDeletion(entities);
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.supervisedPersonColumn.setHeader(getTranslation("views.supervised_person")); //$NON-NLS-1$
		this.typeColumn.setHeader(getTranslation("views.type")); //$NON-NLS-1$
		this.dateColumn.setHeader(getTranslation("views.date")); //$NON-NLS-1$
		this.supervisorsColumn.setHeader(getTranslation("views.supervisors")); //$NON-NLS-1$
	}

	/** UI and JPA filters for {@link StandardSupervisionListView}.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class SupervisionFilters extends Filters<Supervision> {

		private static final long serialVersionUID = 829919544629910175L;

		private Checkbox includeSupervisedPersons;

		private Checkbox includeTypes;

		private Checkbox includeSupervisors;

		/** Constructor.
		 *
		 * @param onSearch
		 */
		public SupervisionFilters(Runnable onSearch) {
			super(onSearch);
		}

		@Override
		protected Component getOptionsComponent() {
			this.includeSupervisedPersons = new Checkbox(true);
			this.includeTypes = new Checkbox(true);
			this.includeSupervisors = new Checkbox(true);

			final var options = new HorizontalLayout();
			options.setSpacing(false);
			options.add(this.includeSupervisedPersons, this.includeTypes, this.includeSupervisors);

			return options;
		}

		@Override
		protected void resetFilters() {
			this.includeSupervisedPersons.setValue(Boolean.TRUE);
			this.includeTypes.setValue(Boolean.TRUE);
			this.includeSupervisors.setValue(Boolean.TRUE);
		}

		@Override
		protected void buildQueryFor(String keywords, List<Predicate> predicates, Root<Supervision> root,
				CriteriaBuilder criteriaBuilder) {
			if (this.includeSupervisedPersons.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("supervisedPerson")), keywords)); //$NON-NLS-1$
			}
			if (this.includeTypes.getValue() == Boolean.TRUE) {
				// TODO predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("dates")), keywords)); //$NON-NLS-1$
			}
			if (this.includeSupervisors.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("supervisors")), keywords)); //$NON-NLS-1$
			}
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			this.includeSupervisedPersons.setLabel(getTranslation("views.filters.include_supervised_persons")); //$NON-NLS-1$
			this.includeTypes.setLabel(getTranslation("views.filters.include_types")); //$NON-NLS-1$
			this.includeSupervisors.setLabel(getTranslation("views.filters.include_supervisors")); //$NON-NLS-1$
		}

	}

	/** Provider of data for supervisions to be displayed in the list of supervisions view.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	@FunctionalInterface
	protected interface SupervisionDataProvider {

		/** Fetch supervisions data.
		 *
		 * @param supervisionService the service to have access to the JPA.
		 * @param pageRequest the request for paging the data.
		 * @param filters the filters to apply for selecting the data.
		 * @return the lazy data page.
		 */
		Page<Supervision> fetch(SupervisionService supervisionService, PageRequest pageRequest, Filters<Supervision> filters);

	}

}
