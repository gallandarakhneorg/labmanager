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

package fr.utbm.ciad.labmanager.views.components.addons.entities;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.base.Strings;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider.FetchCallback;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.data.publication.PublicationType;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityDeletingContext;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.views.components.addons.badges.BadgeRenderer;
import fr.utbm.ciad.labmanager.views.components.addons.badges.BadgeState;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

/** Abstract implementation of a list all the publications whatever the type of publication.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public abstract class AbstractPublicationListView extends AbstractEntityListView<Publication> {
	
	private static final long serialVersionUID = 6591553988384909013L;

	private final String authorsColumnLabelKey;
	
	private PublicationDataProvider dataProvider;

	private PublicationService publicationService;

	private Column<Publication> titleColumn;

	private Column<Publication> authorsColumn;

	private Column<Publication> publishingColumn;

	private Column<Publication> typeColumn;

	private Column<Publication> yearColumn;

	private Column<Publication> fileColumn;

	private Column<Publication> validationColumn;

	private final DownloadableFileManager fileManager;

	/** Constructor.
	 *
	 * @param fileManager the manager of the filenames for the uploaded files.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param publicationService the service for accessing the publications.
	 * @param logger the logger to use.
	 * @param deletionTitleMessageKey the key in the localized messages for the dialog box title related to a deletion.
	 * @param deletionMessageKey the key in the localized messages for the message related to a deletion.
	 * @param deletionSuccessMessageKey the key in the localized messages for the messages related to a deletion success.
	 * @param deletionErrorMessageKey the key in the localized messages for the messages related to an error of deletion.
	 * @param authorsColumnLabelKey the key in the localized messages for the messages related to the label of the authors column.
	 */
	public AbstractPublicationListView(
			DownloadableFileManager fileManager,
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			PublicationService publicationService, Logger logger,
			String deletionTitleMessageKey, String deletionMessageKey,
			String deletionSuccessMessageKey, String deletionErrorMessageKey,
			String authorsColumnLabelKey) {
		super(Publication.class, authenticatedUser, messages, logger,
				deletionTitleMessageKey, deletionMessageKey, deletionSuccessMessageKey, deletionErrorMessageKey);
		this.fileManager = fileManager;
		this.authorsColumnLabelKey = authorsColumnLabelKey;
		this.publicationService = publicationService;
	}

	/** Change the data provider;.
	 *
	 * @param dataProvider the data provider.
	 */
	protected void setDataProvider(PublicationDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	/** Replies the types of publications that are supported by this view.
	 *
	 * @return the types of supported publications.
	 */
	protected abstract Stream<PublicationType> getSupportedPublicationTypes(); 

	/** Create filters that correspond to the supported types.
	 *
	 * @param filters the additional filters.
	 * @return the full filters.
	 * @see #getSupportedPublicationTypes()
	 */
	protected Specification<Publication> createJpaFilters(Specification<Publication> filters) {
		final var filters0 = new PublicationTypeSpecification();
		final var filters1 = filters == null ? filters0 : filters0.and(filters);
		return filters1;
	}

	@Override
	protected Filters<Publication> createFilters() {
		return new PublicationFilters(this::refreshGrid);
	}

	/** Initialize the given JPA entity for being displayed in the list.
	 *
	 * @param entity the entity.
	 */
	@SuppressWarnings("static-method")
	protected void initializeEntityFromJPA(Publication entity) {
		// Force the loaded of the lazy data that is needed for rendering the table
		Hibernate.initialize(entity.getAuthorships());
		// Force the loading of all the data that is required for obtaining the place where the publication was published
		entity.getWherePublishedShortDescription();
	}

	@Override
	protected Grid<Publication> createGridInstance() {
		final var grid = super.createGridInstance();
		grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
		return grid;
	}

	@Override
	protected boolean createGridColumns(Grid<Publication> grid) {
		this.titleColumn = grid.addColumn(it -> it.getTitle())
				.setAutoWidth(false)
				.setSortProperty("title"); //$NON-NLS-1$
		this.authorsColumn = grid.addColumn(new ComponentRenderer<>(this::getAuthorsComponent))
				.setAutoWidth(false);
		this.publishingColumn = grid.addColumn(new ComponentRenderer<>(this::getPublishingComponent))
				.setAutoWidth(false);
		this.typeColumn = grid.addColumn(new ComponentRenderer<>(this::getPublicationTypeComponent))
				.setAutoWidth(false)
				.setSortProperty("type"); //$NON-NLS-1$
		this.yearColumn = grid.addColumn(it -> Integer.toString(it.getPublicationYear()))
				.setAutoWidth(false)
				.setSortProperty("publicationYear"); //$NON-NLS-1$
		this.fileColumn = grid.addColumn(new BadgeRenderer<>((data, callback) -> {
			final var filename = data.getPathToDownloadablePDF();
			if (Strings.isNullOrEmpty(filename)) {
				callback.create(BadgeState.ERROR, getTranslation("views.publication.no_pdf"), null); //$NON-NLS-1$
			} else {
				callback.create(BadgeState.SUCCESS, getTranslation("views.publication.pdf"), filename); //$NON-NLS-1$
			}
		}))
				.setAutoWidth(false);
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

	/** Replies the authors for the given publication.
	 *
	 * @param publication the publication to read.
	 * @return the authors.
	 */
	protected Component getAuthorsComponent(Publication publication) {
		final var authors = publication.getAuthors();
		final var list = new StringBuilder();
		for (final var author : authors) {
			if (list.length() > 0) {
				list.append(", "); //$NON-NLS-1$
			}
			list.append(author.getFullName());
		}
		return new Span(list.toString());
	}

	/** Replies the publishing details for the given publication.
	 *
	 * @param publication the publication to read.
	 * @return the publishing details.
	 */
	protected Component getPublishingComponent(Publication publication) {
		final var details = publication.getWherePublishedShortDescription();
		return new Span(details);
	}

	/** Replies the type's label for the given publication.
	 *
	 * @param publication the publication to read.
	 * @return the type's label.
	 */
	protected Component getPublicationTypeComponent(Publication publication) {
		final var type = publication.getType();
		final var label = type.getLabel(getMessageSourceAccessor(), getLocale());
		return new Span(label);
	}

	@Override
	protected List<Column<Publication>> getInitialSortingColumns() {
		return Arrays.asList(this.yearColumn, this.typeColumn, this.titleColumn);
	}

	@Override
	protected SortDirection getInitialSortingDirection(Column<Publication> column) {
		if (column == this.yearColumn) {
			return SortDirection.DESCENDING;
		}
		return SortDirection.ASCENDING;
	}

	@Override
	protected FetchCallback<Publication, Void> getFetchCallback(Filters<Publication> filters) {
		return query -> {
			return this.dataProvider.fetch(
					this.publicationService,
					VaadinSpringDataHelpers.toSpringPageRequest(query),
					filters).stream();
		};
	}

	@Override
	protected void addEntity() {
		//TODO openPublicationEditor(new Publication(), getTranslation("views.publication.add_publication"));
	}

	@Override
	protected void edit(Publication publication) {
		//TODO openPublicationEditor(publication, getTranslation("views.publication.edit_publication", publication.getTitle()));
	}

	/** Show the editor of a publication.
	 *
	 * @param publication the publication to edit.
	 * @param title the title of the editor.
	 */
	protected void openPublicatioEditor(Publication publication, String title) {
// TODO		final var editor = new EmbeddedProjectEditor(
//				this.projectService.startEditing(project),
//				this.fileManager,
//				getAuthenticatedUser(), getMessageSourceAccessor());
//		final var newEntity = editor.isNewEntity();
//		final SerializableBiConsumer<Dialog, Project> refreshAll = (dialog, entity) -> refreshGrid();
//		final SerializableBiConsumer<Dialog, Project> refreshOne = (dialog, entity) -> refreshItem(entity);
//		ComponentFactory.openEditionModalDialog(title, editor, false,
//				// Refresh the "old" item, even if its has been changed in the JPA database
//				newEntity ? refreshAll : refreshOne,
//				newEntity ? null : refreshAll);
	}

	@Override
	protected EntityDeletingContext<Publication> createDeletionContextFor(Set<Publication> entities) {
		return null; // TODO this.publicationService.startDeletion(entities);
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.titleColumn.setHeader(getTranslation("views.title")); //$NON-NLS-1$
		this.authorsColumn.setHeader(getTranslation(this.authorsColumnLabelKey));
		this.publishingColumn.setHeader(getTranslation("views.where_published")); //$NON-NLS-1$
		this.typeColumn.setHeader(getTranslation("views.type")); //$NON-NLS-1$
		this.yearColumn.setHeader(getTranslation("views.year")); //$NON-NLS-1$
		this.fileColumn.setHeader(getTranslation("views.files")); //$NON-NLS-1$
		this.validationColumn.setHeader(getTranslation("views.validated")); //$NON-NLS-1$
		refreshGrid();
	}

	/** UI and JPA filters for {@link AbstractPublicationListView}.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class PublicationFilters extends Filters<Publication> {

		private static final long serialVersionUID = -9105749553511198435L;

		private Checkbox includeTitles;

		private Checkbox includeYears;

		/** Constructor.
		 *
		 * @param onSearch
		 */
		public PublicationFilters(Runnable onSearch) {
			super(onSearch);
		}

		@Override
		protected Component getOptionsComponent() {
			this.includeTitles = new Checkbox(true);
			this.includeYears = new Checkbox(true);

			final var options = new HorizontalLayout();
			options.setSpacing(false);
			options.add(this.includeTitles, this.includeYears);

			return options;
		}

		@Override
		protected void resetFilters() {
			this.includeTitles.setValue(Boolean.TRUE);
			this.includeYears.setValue(Boolean.TRUE);
		}

		@Override
		protected void buildQueryFor(String keywords, List<Predicate> predicates, Root<Publication> root,
				CriteriaBuilder criteriaBuilder) {
			if (this.includeTitles.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), keywords)); //$NON-NLS-1$
			}
			if (this.includeYears.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.toString(root.get("publicationYear")), keywords)); //$NON-NLS-1$
			}
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			this.includeTitles.setLabel(getTranslation("views.filters.include_titles")); //$NON-NLS-1$
			this.includeYears.setLabel(getTranslation("views.filters.include_years")); //$NON-NLS-1$
		}

	}

	/** Specification that is validating publications of specific type.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	private class PublicationTypeSpecification implements Specification<Publication> {

		private static final long serialVersionUID = -2002042407216268430L;

		@Override
		public Predicate toPredicate(Root<Publication> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			final var predicates = getSupportedPublicationTypes()
				.map(it -> criteriaBuilder.equal(root.get("type"), it)) //$NON-NLS-1$
				.toArray(size -> new Predicate[size]);
			return criteriaBuilder.or(predicates);
		}

	}

	/** Provider of data for publications to be displayed in the list of publications view.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	@FunctionalInterface
	protected interface PublicationDataProvider {

		/** Fetch publication data.
		 *
		 * @param publicationService the service to have access to the JPA.
		 * @param pageRequest the request for paging the data.
		 * @param filters the filters to apply for selecting the data.
		 * @return the lazy data page.
		 */
		Page<Publication> fetch(PublicationService publicationService, PageRequest pageRequest, Filters<Publication> filters);

	}

}
