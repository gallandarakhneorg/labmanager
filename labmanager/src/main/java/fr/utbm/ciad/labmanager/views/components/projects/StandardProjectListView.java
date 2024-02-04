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

package fr.utbm.ciad.labmanager.views.components.projects;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider.FetchCallback;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.project.Project;
import fr.utbm.ciad.labmanager.data.project.ProjectContractType;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityDeletingContext;
import fr.utbm.ciad.labmanager.services.project.ProjectService;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.avatars.AvatarItem;
import fr.utbm.ciad.labmanager.views.components.addons.badges.BadgeRenderer;
import fr.utbm.ciad.labmanager.views.components.addons.badges.BadgeState;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityListView;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.arakhne.afc.vmutil.FileSystem;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/** List all the projects.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class StandardProjectListView extends AbstractEntityListView<Project> {

	private static final long serialVersionUID = -1815606905515706259L;

	private static final String UNKNOWN = "?"; //$NON-NLS-1$

	private static final int ORIGIN_YEAR = 2000;
	
	private final ProjectDataProvider dataProvider;

	private ProjectService projectService;

	private Column<Project> nameColumn;

	private Column<Project> dateColumn;

	private Column<Project> typeColumn;

	private Column<Project> fundingColumn;

	private Column<Project> stateColumn;

	private Column<Project> validationColumn;

	private final DownloadableFileManager fileManager;

	/** Constructor.
	 *
	 * @param fileManager the manager of the filenames for the uploaded files.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param projectService the service for accessing the projects.
	 * @param logger the logger to use.
	 */
	public StandardProjectListView(
			DownloadableFileManager fileManager,
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			ProjectService projectService, Logger logger) {
		super(Project.class, authenticatedUser, messages, logger,
				"views.projects.delete.title", //$NON-NLS-1$
				"views.project.delete.message", //$NON-NLS-1$
				"views.projects.delete_success", //$NON-NLS-1$
				"views.projects.delete_error"); //$NON-NLS-1$
		this.fileManager = fileManager;
		this.projectService = projectService;
		this.dataProvider = (ps, query, filters) -> ps.getAllProjects(query, filters);
		initializeDataInGrid(getGrid(), getFilters());
	}

	@Override
	protected Filters<Project> createFilters() {
		return new ProjectFilters(this::refreshGrid);
	}

	@Override
	protected boolean createGridColumns(Grid<Project> grid) {
		this.nameColumn = grid.addColumn(new ComponentRenderer<>(this::createNameComponent))
				.setAutoWidth(true)
				.setSortProperty("acronym", "scientificTitle"); //$NON-NLS-1$ //$NON-NLS-2$
		this.dateColumn = grid.addColumn(this::getDateLabel)
				.setAutoWidth(false)
				.setSortProperty("creationDate"); //$NON-NLS-1$
		this.typeColumn = grid.addColumn(new ComponentRenderer<>(this::createTypeComponent))
				.setAutoWidth(false)
				.setSortProperty("projectType", "contractType"); //$NON-NLS-1$ //$NON-NLS-2$
		this.fundingColumn = grid.addColumn(this::getFundingLabel)
				.setAutoWidth(true)
				.setSortProperty("budgets"); //$NON-NLS-1$
		this.stateColumn = grid.addColumn(new BadgeRenderer<>((data, callback) -> {
			switch (data.getStatus()) {
			case EVALUATION:
				final var evalText = getTranslation("views.projects.status_evaluation"); //$NON-NLS-1$
				callback.create(BadgeState.PILL, evalText, evalText);
				break;
			case CANCELED:
				final var canText = getTranslation("views.projects.status_canceled"); //$NON-NLS-1$
				callback.create(BadgeState.CONTRAST, canText, canText);
				break;
			case REJECTED:
				final var rejText = getTranslation("views.projects.status_rejected"); //$NON-NLS-1$
				callback.create(BadgeState.ERROR, rejText, rejText);
				break;
			case ACCEPTED:
				final var sucText = getTranslation("views.projects.status_accepted"); //$NON-NLS-1$
				callback.create(BadgeState.SUCCESS, sucText, sucText);
				break;
			case PREPARATION:
			default:
				final var prepText = getTranslation("views.projects.status_preparation"); //$NON-NLS-1$
				callback.create(BadgeState.PRIMARY, prepText, prepText);
				break;
			}
		}))
				.setAutoWidth(false)
				.setSortProperty("status"); //$NON-NLS-1$
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
	
	private String getFundingLabel(Project project) {
		// TODO
		return "";
	}

	private Component createTypeComponent(Project project) {
		final var div = new Div();
		final var contract = project.getContractType();
		if (contract != null) {
			final var span = new Span();
			span.setText(contract == ProjectContractType.NOT_SPECIFIED ? UNKNOWN : contract.name());
			span.setTitle(contract.getLabel(getMessageSourceAccessor(), getLocale()));
			div.add(span);
		}
		final var activity = project.getActivityType();
		if (activity != null) {
			if (contract != null) {
				div.add(new Text(" - ")); //$NON-NLS-1$
			}
			final var span = new Span();
			span.setText(activity.getLabel(getMessageSourceAccessor(), getLocale()));
			div.add(span);
		}
		return div;
	}

	private String getDateLabel(Project project) {
		final var startDate = project.getStartDate();
		final var endDate = project.getEndDate();
		final var duration = project.getDuration();
		final var year0 = startDate == null ? ORIGIN_YEAR : startDate.getYear();
		final var year1 = endDate == null
				? LocalDate.now().getYear()
				: endDate.getYear();
		if (year0 == year1) {
			if (duration > 1) {
				return getTranslation("views.projects.date_months", Integer.toString(year0), Integer.valueOf(duration)); //$NON-NLS-1$
			}
			if (duration > 0) {
				return getTranslation("views.projects.date_month", Integer.toString(year0), Integer.valueOf(duration)); //$NON-NLS-1$
			}
			return Integer.toString(year0);
		}
		if (duration > 1) {
			return getTranslation("views.projects.dates_months", Integer.toString(year0), Integer.toString(year1), Integer.valueOf(duration)); //$NON-NLS-1$
		}
		if (duration > 0) {
			return getTranslation("views.projects.dates_month", Integer.toString(year0), Integer.toString(year1), Integer.valueOf(duration)); //$NON-NLS-1$
		}
		return getTranslation("views.projects.dates", Integer.toString(year0), Integer.toString(year1)); //$NON-NLS-1$
	}

	private Component createNameComponent(Project project) {
		final var acronym = project.getAcronym();
		final var logo = project.getPathToLogo();

		final var avatar = new AvatarItem();
		avatar.setHeading(acronym);
		var logoFile = FileSystem.convertStringToFile(logo);
		var emptyLogo = true;
		if (logoFile != null) {
			logoFile = this.fileManager.normalizeForServerSide(logoFile);
			if (logoFile != null) {
				avatar.setAvatarResource(ComponentFactory.newStreamImage(logoFile));
				emptyLogo = false;
			}
		}
		if (emptyLogo) {
			avatar.setAvatarResource(ComponentFactory.newEmptyBackgroundStreamImage());
		}
		return avatar;
	}

	@Override
	protected List<Column<Project>> getInitialSortingColumns() {
		return Collections.singletonList(this.nameColumn);
	}

	@Override
	protected FetchCallback<Project, Void> getFetchCallback(Filters<Project> filters) {
		return query -> {
			return this.dataProvider.fetch(
					this.projectService,
					VaadinSpringDataHelpers.toSpringPageRequest(query),
					filters).stream();
		};
	}

	@Override
	protected void addEntity() {
		openProjectEditor(new Project(), getTranslation("views.projects.add_project")); //$NON-NLS-1$
	}

	@Override
	protected void edit(Project project) {
		openProjectEditor(project, getTranslation("views.projects.edit_project", project.getAcronym())); //$NON-NLS-1$
	}

	/** Show the editor of an project.
	 *
	 * @param project the project to edit.
	 * @param title the title of the editor.
	 */
	protected void openProjectEditor(Project project, String title) {
		final var editor = new EmbeddedProjectEditor(
				this.projectService.startEditing(project),
				this.fileManager,
				getAuthenticatedUser(), getMessageSourceAccessor());
		final var newEntity = editor.isNewEntity();
		final SerializableBiConsumer<Dialog, Project> refreshAll = (dialog, entity) -> refreshGrid();
		final SerializableBiConsumer<Dialog, Project> refreshOne = (dialog, entity) -> refreshItem(entity);
		ComponentFactory.openEditionModalDialog(title, editor, false,
				// Refresh the "old" item, even if its has been changed in the JPA database
				newEntity ? refreshAll : refreshOne,
				newEntity ? null : refreshAll);
	}

	@Override
	protected EntityDeletingContext<Project> createDeletionContextFor(Set<Project> entities) {
		return this.projectService.startDeletion(entities);
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.nameColumn.setHeader(getTranslation("views.name")); //$NON-NLS-1$
		this.dateColumn.setHeader(getTranslation("views.period")); //$NON-NLS-1$
		this.typeColumn.setHeader(getTranslation("views.type")); //$NON-NLS-1$
		this.fundingColumn.setHeader(getTranslation("views.fundings")); //$NON-NLS-1$
		this.stateColumn.setHeader(getTranslation("views.states")); //$NON-NLS-1$
		this.validationColumn.setHeader(getTranslation("views.validated")); //$NON-NLS-1$
		refreshGrid();
	}

	/** UI and JPA filters for {@link StandardProjectListView}.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class ProjectFilters extends Filters<Project> {

		private static final long serialVersionUID = 7079030666137901350L;

		private Checkbox includeNames;

		private Checkbox includeDates;

		private Checkbox includeTypes;

		private Checkbox includeFundings;

		private Checkbox includeStates;

		/** Constructor.
		 *
		 * @param onSearch
		 */
		public ProjectFilters(Runnable onSearch) {
			super(onSearch);
		}

		@Override
		protected Component getOptionsComponent() {
			this.includeNames = new Checkbox(true);
			this.includeDates = new Checkbox(false);
			this.includeTypes = new Checkbox(true);
			this.includeFundings = new Checkbox(false);
			this.includeStates = new Checkbox(true);

			final var options = new HorizontalLayout();
			options.setSpacing(false);
			options.add(this.includeNames);
			options.add(this.includeDates);
			options.add(this.includeTypes);
			options.add(this.includeFundings);
			options.add(this.includeStates);
			
			this.includeFundings.setEnabled(false);

			return options;
		}

		@Override
		protected void resetFilters() {
			this.includeNames.setValue(Boolean.TRUE);
			this.includeDates.setValue(Boolean.TRUE);
			this.includeTypes.setValue(Boolean.TRUE);
			this.includeFundings.setValue(Boolean.FALSE);
			this.includeStates.setValue(Boolean.TRUE);
		}

		@Override
		protected void buildQueryFor(String keywords, List<Predicate> predicates, Root<Project> root,
				CriteriaBuilder criteriaBuilder) {
			if (this.includeNames.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("acronym")), keywords)); //$NON-NLS-1$
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("scientificTitle")), keywords)); //$NON-NLS-1$
			}
			if (this.includeDates.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("startDate")), keywords)); //$NON-NLS-1$
			}
			if (this.includeTypes.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("activityType")), keywords)); //$NON-NLS-1$
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("contractType")), keywords)); //$NON-NLS-1$
			}
			if (this.includeFundings.getValue() == Boolean.TRUE) {
				//predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("budgets")), keywords)); //$NON-NLS-1$
			}
			if (this.includeStates.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), keywords)); //$NON-NLS-1$
			}
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			this.includeNames.setLabel(getTranslation("views.filters.include_names")); //$NON-NLS-1$
			this.includeDates.setLabel(getTranslation("views.filters.include_periods")); //$NON-NLS-1$
			this.includeTypes.setLabel(getTranslation("views.filters.include_types")); //$NON-NLS-1$
			this.includeFundings.setLabel(getTranslation("views.filters.include_fundings")); //$NON-NLS-1$
			this.includeStates.setLabel(getTranslation("views.filters.include_states")); //$NON-NLS-1$
		}

	}


	/** Provider of data for projects to be displayed in the list of projects view.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	@FunctionalInterface
	protected interface ProjectDataProvider {

		/** Fetch project data.
		 *
		 * @param projectService the service to have access to the JPA.
		 * @param pageRequest the request for paging the data.
		 * @param filters the filters to apply for selecting the data.
		 * @return the lazy data page.
		 */
		Page<Project> fetch(ProjectService projectService, PageRequest pageRequest, Filters<Project> filters);

	}

}
