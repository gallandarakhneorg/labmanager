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

package fr.utbm.ciad.labmanager.views.components.teaching;

import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider.FetchCallback;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.teaching.TeachingActivity;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityDeletingContext;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.organization.OrganizationAddressService;
import fr.utbm.ciad.labmanager.services.organization.ResearchOrganizationService;
import fr.utbm.ciad.labmanager.services.teaching.TeachingService;
import fr.utbm.ciad.labmanager.services.user.UserService;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.avatars.AvatarItem;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityListView;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/** List all the teaching activites.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class StandardTeachingActivitiesListView extends AbstractEntityListView<TeachingActivity> {

	private static final long serialVersionUID = -2664722351226443633L;

	private final TeachingActivityDataProvider dataProvider;

	private final TeachingService teachingService;

	private final PersonService personService;

	private final UserService userService;

	private final ResearchOrganizationService organizationService;

	private final OrganizationAddressService addressService;

	private Column<TeachingActivity> titleColumn;

	private Column<TeachingActivity> levelColumn;

	private Column<TeachingActivity> universityColumn;

	private Column<TeachingActivity> periodColumn;

	private Column<TeachingActivity> teacherColumn;

	private final DownloadableFileManager fileManager;

	/** Constructor.
	 *
	 * @param fileManager the manager of the downloadable files.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param teachingService the service for accessing the teaching activities.
	 * @param personService the service for accessing the JPA entities for persons.
	 * @param userService the service for accessing the JPA entities for users.
	 * @param organizationService the service for accessing the JPA entities for research organizations.
	 * @param addressService the service for accessing the JPA entities for organization addresses.
	 * @param logger the logger to use.
	 */
	public StandardTeachingActivitiesListView(
			DownloadableFileManager fileManager,
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			TeachingService teachingService, PersonService personService,
			UserService userService, ResearchOrganizationService organizationService,
			OrganizationAddressService addressService, Logger logger) {
		super(TeachingActivity.class, authenticatedUser, messages, logger,
				"views.teaching_activities.delete.title", //$NON-NLS-1$
				"views.teaching_activities.delete.message", //$NON-NLS-1$
				"views.teaching_activities.delete_success", //$NON-NLS-1$
				"views.teaching_activities.delete_error"); //$NON-NLS-1$
		this.fileManager = fileManager;
		this.teachingService = teachingService;
		this.personService = personService;
		this.userService = userService;
		this.organizationService = organizationService;
		this.addressService = addressService;
		this.dataProvider = (ps, query, filters) -> ps.getAllActivities(query, filters, this::initializeEntityFromJPA);
		initializeDataInGrid(getGrid(), getFilters());
	}

	private void initializeEntityFromJPA(TeachingActivity entity) {
		// Force the loaded of the lazy data that is needed for rendering the table
		Hibernate.initialize(entity.getPerson());
		Hibernate.initialize(entity.getUniversity());
	}

	@Override
	protected Filters<TeachingActivity> createFilters() {
		return new TeachingActivityFilters(this::refreshGrid);
	}

	private Component createNameComponent(TeachingActivity activity) {
		final var code = activity.getCode();
		final var title = activity.getTitle();
		final var slides = activity.getPathToSlides();

		final var avatar = new AvatarItem();
		avatar.setHeading(title);
		if (!Strings.isNullOrEmpty(code)) {
			avatar.setDescription(code);
		}
		var slidesFile = FileSystem.convertStringToFile(slides);
		var needPicture = true;
		if (slidesFile != null) {
			var picture = this.fileManager.toThumbnailFilename(slidesFile);
			picture = this.fileManager.normalizeForServerSide(picture);
			if (picture != null) {
				avatar.setAvatarResource(ComponentFactory.newStreamImage(picture));
				needPicture = false;
			}
		}
		if (needPicture) {
			avatar.setAvatarResource(ComponentFactory.newEmptyBackgroundStreamImage());
		}
		return avatar;
	}
	
	private String getLevelStudentTypeDegree(TeachingActivity activity) {
		final var level = activity.getLevel();
		final var studentType = activity.getStudentType();
		final var degree = activity.getDegree();
		final var label = new StringBuilder();
		label.append(level.getLabel(getMessageSourceAccessor(), getLocale()));
		label.append(" - "); //$NON-NLS-1$
		label.append(studentType.getLabel(getMessageSourceAccessor(), getLocale()));
		if (Strings.isNullOrEmpty(degree)) {
			label.append(" - "); //$NON-NLS-1$
			label.append(degree);
		}
		return label.toString();
	}

	private Component createUniversityComponent(TeachingActivity activity) {
		final var university = activity.getUniversity();
		if (university != null) {
			return ComponentFactory.newOrganizationAvatar(university, this.fileManager);
		}
		return new Span();
	}

	private Component createTeacherComponent(TeachingActivity activity) {
		final var person = activity.getPerson();
		if (person != null) {
			return ComponentFactory.newPersonAvatar(person);
		}
		return new Span();
	}

	private String getPeriodLabel(TeachingActivity activity) {
		final var startDate = activity.getStartDate();
		final var endDate = activity.getStartDate();
		if (startDate != null) {
			final var sy = startDate.getYear();
			if (endDate != null) {
				final var ey = endDate.getYear();
				if (sy != ey) {
					return new StringBuilder().append(sy).append("-").append(ey).toString(); //$NON-NLS-1$
				}
			}
			return Integer.toString(sy);
		} else if (endDate != null) {
			final var ey = endDate.getYear();
			return Integer.toString(ey);
		}
		return ""; //$NON-NLS-1$
	}

	@Override
	protected boolean createGridColumns(Grid<TeachingActivity> grid) {
		this.titleColumn = grid.addColumn(new ComponentRenderer<>(this::createNameComponent))
				.setAutoWidth(true)
				.setFrozen(true)
				.setSortProperty("code", "title"); //$NON-NLS-1$ //$NON-NLS-2$
		this.teacherColumn = grid.addColumn(new ComponentRenderer<>(this::createTeacherComponent))
				.setAutoWidth(true);
		this.levelColumn = grid.addColumn(this::getLevelStudentTypeDegree)
				.setAutoWidth(true);
		this.universityColumn = grid.addColumn(new ComponentRenderer<>(this::createUniversityComponent))
				.setAutoWidth(true);
		this.periodColumn = grid.addColumn(this::getPeriodLabel)
				.setAutoWidth(true)
				.setSortProperty("startDate", "endDate"); //$NON-NLS-1$ //$NON-NLS-2$
		// Create the hover tool bar only if administrator role
		return isAdminRole();
	}

	@Override
	protected Column<TeachingActivity> getInitialSortingColumn() {
		return this.titleColumn;
	}

	@Override
	protected FetchCallback<TeachingActivity, Void> getFetchCallback(Filters<TeachingActivity> filters) {
		return query -> {
			return this.dataProvider.fetch(
					this.teachingService,
					VaadinSpringDataHelpers.toSpringPageRequest(query),
					filters).stream();
		};
	}

	@Override
	protected void addEntity() {
		openActivityEditor(new TeachingActivity(), getTranslation("views.teaching_activities.add_activity")); //$NON-NLS-1$
	}

	@Override
	protected void edit(TeachingActivity activity) {
		openActivityEditor(activity, getTranslation("views.teaching_activities.edit_activity", activity.getCodeOrTitle())); //$NON-NLS-1$
	}

	/** Show the editor of a teaching activity.
	 *
	 * @param activity the teaching activity to edit.
	 * @param title the title of the editor.
	 */
	protected void openActivityEditor(TeachingActivity activity, String title) {
		final var editor = new EmbeddedTeachingActivityEditor(
				this.teachingService.startEditing(activity),
				this.fileManager, this.personService, this.userService,
				this.organizationService, this.addressService, getAuthenticatedUser(), getMessageSourceAccessor());
		final var newEntity = editor.isNewEntity();
		final SerializableBiConsumer<Dialog, TeachingActivity> refreshAll = (dialog, entity) -> {
			// The person should be loaded because it was not loaded before
			this.teachingService.inSession(session -> {
				session.load(entity, Long.valueOf(entity.getId()));
				initializeEntityFromJPA(entity);
			});
			refreshGrid();
		};
		final SerializableBiConsumer<Dialog, TeachingActivity> refreshOne = (dialog, entity) -> {
			// The person should be loaded because it was not loaded before
			this.teachingService.inSession(session -> {
				session.load(entity, Long.valueOf(entity.getId()));
				initializeEntityFromJPA(entity);
			});
			refreshItem(entity);
		};
		ComponentFactory.openEditionModalDialog(title, editor, false,
				// Refresh the "old" item, even if its has been changed in the JPA database
				newEntity ? refreshAll : refreshOne,
				newEntity ? null : refreshAll);
	}

	@Override
	protected EntityDeletingContext<TeachingActivity> createDeletionContextFor(Set<TeachingActivity> entities) {
		return this.teachingService.startDeletion(entities);
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.titleColumn.setHeader(getTranslation("views.title")); //$NON-NLS-1$
		this.levelColumn.setHeader(getTranslation("views.type")); //$NON-NLS-1$
		this.universityColumn.setHeader(getTranslation("views.university")); //$NON-NLS-1$
		this.periodColumn.setHeader(getTranslation("views.period")); //$NON-NLS-1$
		this.teacherColumn.setHeader(getTranslation("views.teacher")); //$NON-NLS-1$
	}

	/** UI and JPA filters for {@link StandardTeachingActivitiesListView}.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class TeachingActivityFilters extends Filters<TeachingActivity> {

		private static final long serialVersionUID = -8070684808016589969L;

		private Checkbox includeCodesTitles;

		private Checkbox includeDegreesLevelsStudentTypes;

		private Checkbox includeUniversities;

		private Checkbox includePeriods;

		private Checkbox includePersons;

		/** Constructor.
		 *
		 * @param onSearch
		 */
		public TeachingActivityFilters(Runnable onSearch) {
			super(onSearch);
		}

		@Override
		protected Component getOptionsComponent() {
			this.includeCodesTitles = new Checkbox(true);
			this.includeDegreesLevelsStudentTypes = new Checkbox(true);
			this.includeUniversities = new Checkbox(true);
			this.includePeriods = new Checkbox(true);
			this.includePersons = new Checkbox(true);

			final var options = new HorizontalLayout();
			options.setSpacing(false);
			options.add(this.includeCodesTitles, this.includeDegreesLevelsStudentTypes, 
					this.includeUniversities, this.includePeriods, this.includePersons);

			return options;
		}

		@Override
		protected void resetFilters() {
			this.includeCodesTitles.setValue(Boolean.TRUE);
			this.includeDegreesLevelsStudentTypes.setValue(Boolean.TRUE);
			this.includeUniversities.setValue(Boolean.TRUE);
			this.includePeriods.setValue(Boolean.TRUE);
			this.includePersons.setValue(Boolean.TRUE);
		}

		@Override
		protected void buildQueryFor(String keywords, List<Predicate> predicates, Root<TeachingActivity> root,
				CriteriaBuilder criteriaBuilder) {
			if (this.includeCodesTitles.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), keywords)); //$NON-NLS-1$
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), keywords)); //$NON-NLS-1$
			}
			if (this.includeDegreesLevelsStudentTypes.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("level")), keywords)); //$NON-NLS-1$
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("degree")), keywords)); //$NON-NLS-1$
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("studentType")), keywords)); //$NON-NLS-1$
			}
			if (this.includeUniversities.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("university")), keywords)); //$NON-NLS-1$
			}
			if (this.includePeriods.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("startDate")), keywords)); //$NON-NLS-1$
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("endDate")), keywords)); //$NON-NLS-1$
			}
			if (this.includePersons.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("person")), keywords)); //$NON-NLS-1$
			}
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			this.includeCodesTitles.setLabel(getTranslation("views.filters.include_titles")); //$NON-NLS-1$
			this.includeDegreesLevelsStudentTypes.setLabel(getTranslation("views.filters.include_types")); //$NON-NLS-1$
			this.includeUniversities.setLabel(getTranslation("views.filters.include_universities")); //$NON-NLS-1$
			this.includePeriods.setLabel(getTranslation("views.filters.include_periods")); //$NON-NLS-1$
			this.includePersons.setLabel(getTranslation("views.filters.include_persons")); //$NON-NLS-1$
		}

	}


	/** Provider of data for teaching activities to be displayed in the list of activities view.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	@FunctionalInterface
	protected interface TeachingActivityDataProvider {

		/** Fetch teaching activity data.
		 *
		 * @param teachingService the service to have access to the JPA.
		 * @param pageRequest the request for paging the data.
		 * @param filters the filters to apply for selecting the data.
		 * @return the lazy data page.
		 */
		Page<TeachingActivity> fetch(TeachingService teachingService, PageRequest pageRequest, Filters<TeachingActivity> filters);

	}

}
