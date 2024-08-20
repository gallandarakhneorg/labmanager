/*
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import com.google.common.base.Strings;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import fr.utbm.ciad.labmanager.data.IdentifiableEntity;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityDeletingContext;
import fr.utbm.ciad.labmanager.utils.io.filemanager.FileManager;
import fr.utbm.ciad.labmanager.views.ViewConstants;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.avatars.AvatarItem;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.arakhne.afc.vmutil.FileSystem;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.jpa.domain.Specification;
import org.vaadin.lineawesome.LineAwesomeIcon;

/** Abstract implementation of a list of entities based on a grid-based view.
 *
 * @param <T> the type of the entities in the grid, that must be {@link IdentifiableEntity} to be able to provide their id.
 * @param <F> the type of the filtered entities, that must be {@link IdentifiableEntity} to be able to provide their id.
 * @param <G> the type of the view component.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 * @see #AbstractEntityTreeListView
 */
@Uses(Icon.class)
@CssImport(value = "./themes/labmanager/components/grid-row-hover.css", themeFor = "vaadin-grid")
public abstract class AbstractGridBaseEntityListView<T extends IdentifiableEntity, F extends IdentifiableEntity, G extends Grid<T>>
		extends Composite<VerticalLayout> implements LocaleChangeObserver {

	private static final long serialVersionUID = -602512272488076268L;

	private final Logger logger;

	private final MessageSourceAccessor messages;

	private final AuthenticatedUser authenticatedUser;

	private final Class<T> entityType;

	private final String deletionTitleMessageKey;

	private final String deletionMessageKey;

	private final String deletionSuccessMessageKey;

	private final String deletionErrorMessageKey;

	private G grid;

	private AbstractFilters<F> filters;

	private MenuItem addButton;

	private MenuItem editButton;

	private MenuItem deleteButton;

	/** Constructor.
	 *
	 * @param entityType the type of the entities to be listed.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param logger the logger to be used by this view.
	 * @param deletionTitleMessageKey the key in the localized messages for the dialog box title related to a deletion.
	 * @param deletionMessageKey the key in the localized messages for the message related to a deletion.
	 * @param deletionSuccessMessageKey the key in the localized messages for the messages related to a deletion success.
	 * @param deletionErrorMessageKey the key in the localized messages for the messages related to an error of deletion.
	 */
	public AbstractGridBaseEntityListView(Class<T> entityType,
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages, Logger logger,
			String deletionTitleMessageKey, String deletionMessageKey,
			String deletionSuccessMessageKey, String deletionErrorMessageKey) {
		this.entityType = entityType;
		this.logger = logger;
		this.messages = messages;
		this.authenticatedUser = authenticatedUser;
		this.deletionTitleMessageKey = deletionTitleMessageKey;
		this.deletionMessageKey = deletionMessageKey;
		this.deletionSuccessMessageKey = deletionSuccessMessageKey;
		this.deletionErrorMessageKey = deletionErrorMessageKey;

		final var rootContainer = getContent();

		rootContainer.setSizeFull();
		rootContainer.setPadding(false);
		rootContainer.setSpacing(false);

		this.filters = createFilters();
		this.grid = createGrid();
		final var menu = createMenuBar();

		rootContainer.add(createMobileFilters(), this.filters, menu, this.grid);
	}

	/** Replies the accessor to the localized strings.
	 *
	 * @return the accessor.
	 */
	protected MessageSourceAccessor getMessageSourceAccessor() {
		return this.messages;
	}

	/** Replies the type of entity to be listed in this component.
	 *
	 * @return the entity type.
	 */
	protected Class<T> getEntityType() {
		return this.entityType;
	}

	/** Replies the authenticated user.
	 *
	 * @return the authenticated user.
	 */
	protected AuthenticatedUser getAuthenticatedUser() {
		return this.authenticatedUser;
	}

	/** Replies if the authenticated user has the admin role.
	 *
	 * @return {@code true} if there is an authenticated user with the admin role.
	 */
	protected boolean isAdminRole() {
		return this.authenticatedUser != null && this.authenticatedUser.get().isPresent()
				&& this.authenticatedUser.get().get().getRole().hasBaseAdministrationRights();
	}

	/** Create the filters for mobile device.
	 *
	 * @return the filters
	 */
	protected HorizontalLayout createMobileFilters() {
		final var mobileFilters = new HorizontalLayout();
		mobileFilters.setWidthFull();
		mobileFilters.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER, LumoUtility.AlignItems.CENTER);
		mobileFilters.addClassName("mobile-filters"); //$NON-NLS-1$

		var mobileIcon = LumoIcon.PLUS.create();
		var filtersHeading = new Span(getTranslation("views.filters")); //$NON-NLS-1$
		mobileFilters.add(mobileIcon, filtersHeading);
		mobileFilters.setFlexGrow(1, filtersHeading);
		mobileFilters.addClickListener(e -> {
			if (this.filters.getClassNames().contains("visible")) { //$NON-NLS-1$
				this.filters.removeClassName("visible"); //$NON-NLS-1$
				mobileIcon.getElement().setAttribute("icon", "lumo:plus"); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				this.filters.addClassName("visible"); //$NON-NLS-1$
				mobileIcon.getElement().setAttribute("icon", "lumo:minus"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		});
		return mobileFilters;
	}

	/** Create the filters for desktop device.
	 *
	 * @return the filters
	 */
	protected abstract AbstractFilters<F> createFilters();

	/** Replies the column to be used for the initial sorting.
	 *
	 * @return the column or {@code null}.
	 */
	protected abstract List<Column<T>> getInitialSortingColumns();

	/** Replies the order to be used for the initial sorting for the given column.
	 *
	 * @param column the column to check.
	 * @return the order, never {@code null}.
	 */
	protected SortDirection getInitialSortingDirection(Column<T> column) {
		return SortDirection.ASCENDING;
	}

	/** Create the list of tools.
	 *
	 * @return the menu bar, or {@code null} if the menu cannot be created because of access rights.
	 */
	protected MenuBar createMenuBar() {
		if (isAdminRole()) {
			final var menu = new MenuBar();
			menu.addThemeVariants(MenuBarVariant.LUMO_ICON);

			this.addButton = ComponentFactory.addIconItem(menu, LineAwesomeIcon.PLUS_SOLID, null, null, it -> addEntity());

			this.editButton = ComponentFactory.addIconItem(menu, LineAwesomeIcon.EDIT_SOLID, null, null, it -> editSelection());
			this.editButton.setEnabled(false);

			this.deleteButton = ComponentFactory.addIconItem(menu, LineAwesomeIcon.TRASH_SOLID, null, null, it -> deleteWithQuery(this.grid.getSelectedItems()));
			this.deleteButton.addThemeNames("error"); //$NON-NLS-1$
			this.deleteButton.setEnabled(false);

			return menu;
		}
		return null;
	}

	/** Replies the grid.
	 *
	 * @return the grid.
	 */
	protected G getGrid() {
		return this.grid;
	}

	/** Invoked to post-initialize the filters after all the associated components have been added.
	 * By default, this function initialize the filters if it is of type {@link AbstractDefaultOrganizationDataFilters}.
	 * Otherwise, it does nothing.
	 */
	protected void postInitializeFilters() {
		final var filters = getFilters();
		if (filters instanceof AbstractDefaultOrganizationDataFilters organizationFilters) {
			organizationFilters.updateDefaultOrganizationIconInToggleButton();
		}
	}

	/** Replies the filters.
	 *
	 * @return the filters.
	 */
	protected AbstractFilters<F> getFilters() {
		return this.filters;
	}
	
	/** Replies the logger.
	 *
	 * @return the logger.
	 */
	protected Logger getLogger() {
		return this.logger;
	}

	/** Add a new entity.
	 */
	protected abstract void addEntity();

	/** Query for the deletion the current selection and do the deletion if it is accepted.
	 *
	 * @param entities the entities to be deleted.
	 */
	protected final void deleteWithQuery(Set<T> entities) {
		final var context = createDeletionContextFor(entities);
		try {
			if (context.isDeletionPossible()) {
				final int size = entities.size();
				ComponentFactory.newDeletionDialog(this,
						getTranslation(this.deletionTitleMessageKey, Integer.valueOf(size)),
						getTranslation(this.deletionMessageKey, Integer.valueOf(size)),
						it ->  {
							try {
								context.delete();
								refreshGrid();
								notifyDeleted(size);
							} catch (Exception ex) {
								refreshGrid();
								notifyDeletionError(ex);
							}
						})
				.open();
			} else {
				notifyDeletionError(context.getDeletionStatus().getException(getMessageSourceAccessor(), getLocale()));
			}
		} catch (Throwable ex) {
			notifyDeletionError(ex);
		}
	}

	/** Create a context that is used for deleting entities and the associated files and entities.
	 *
	 * @param entities the entities to be deleted.
	 * @return the deletion context, never {@code null}.
	 */
	protected abstract EntityDeletingContext<T> createDeletionContextFor(Set<T> entities);

	/** Notify the user that the no entity was found for the requested operation.
	 */
	protected void notifyNotEntity() {
		ComponentFactory.showErrorNotification(getTranslation("views.no_entity")); //$NON-NLS-1$
	}

	/** Notify the user that the entities were deleted.
	 *
	 * @param size the number of deleted entities
	 * @see #notifyDeleted(int, String)
	 */
	protected final void notifyDeleted(int size) {
		notifyDeleted(size, this.deletionSuccessMessageKey);
	}

	/** Notify the user that the entities were deleted.
	 *
	 * @param size the number of deleted entities
	 * @param messageKey the key of the message to be displayed. Argument <code>{0}</code> is replaced by the {@code size}.
	 * @see #notifyDeleted(int)
	 */
	protected void notifyDeleted(int size, String messageKey) {
		ComponentFactory.showSuccessNotification(getTranslation(messageKey, Integer.valueOf(size)));
	}
	
	/** Notify the user that the entities cannot be deleted.
	 *
	 * @param error the error to be shown.
	 * @see #notifyDeletionError(Throwable, String)
	 */
	protected final void notifyDeletionError(Throwable error) {
		notifyDeletionError(error, this.deletionErrorMessageKey);
	}

	/** Notify the user that the entities cannot be deleted.
	 *
	 * @param error the error to be shown.
	 * @param messageKey the key of the message to be displayed. Argument <code>{0}</code> is replaced by
	 *    the string associated to the {@code error}.
	 * @see #notifyDeletionError(Throwable)
	 */
	protected void notifyDeletionError(Throwable error, String messageKey) {
		final var msg = new StringBuilder("Error when deleting data for the entity by "); //$NON-NLS-1$
		msg.append(AuthenticatedUser.getUserName(this.authenticatedUser));
		msg.append(": "); //$NON-NLS-1$
		msg.append(error.getLocalizedMessage());
		this.logger.warn(msg.toString(), error);
		ComponentFactory.showErrorNotification(getTranslation(messageKey, error.getLocalizedMessage()));
	}

	private void editSelection() {
		final var selection = this.grid.getSelectionModel().getFirstSelectedItem();
		if (selection.isPresent()) {
			final T entity = selection.get();
			edit(entity);
		}
	}

	/** Edit the given entity.
	 *
	 * @param entity the entity to be edited.
	 */
	protected abstract void edit(T entity);

	/** Create the grid component.
	 *
	 * @param grid the grid to build up.
	 * @return {@code true} if the hover menu bar must be automatically added.
	 */
	protected abstract boolean createGridColumns(G grid);

	/** Create the hover menu bar that is displayed for each grid row.
	 *
	 * @param entity the entity associated to the menu bar.
	 * @param menuBar the menu bar to build up.
	 */
	protected void createHoverMenuBar(T entity, MenuBar menuBar) {
		if (isAdminRole()) {
			var item = menuBar.addItem(LineAwesomeIcon.EDIT.create(), event -> {
				if (entity != null) {
					edit(entity);
				} else {
					notifyNotEntity();
				}
			});
			item.setAriaLabel(getTranslation("views.edit")); //$NON-NLS-1$

			final var deleteIcon = LineAwesomeIcon.TRASH_SOLID.create();
			deleteIcon.setColor("var(--lumo-error-color)"); //$NON-NLS-1$
			item = menuBar.addItem(deleteIcon, event -> {
				if (entity != null) {
					deleteWithQuery(Collections.singleton(entity));
				} else {
					notifyNotEntity();
				}
			});
			item.setAriaLabel(getTranslation("views.delete")); //$NON-NLS-1$
		}
	}
	
	/** Create the instance of the grid component.
	 *
	 * @return the instance of the grid component.
	 */
	protected abstract G createGridInstance();

	/** Create the grid component.
	 *
	 * @return the grid component.
	 */
	private G createGrid() {
		final var grid = createGridInstance();
		final var createHoverMenuBar = createGridColumns(grid);

		if (createHoverMenuBar) {
			ComponentFactory.addGridCellHoverMenuBar(grid, this::createHoverMenuBar);
		}

		grid.setPageSize(ViewConstants.GRID_PAGE_SIZE);
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
		grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);
		grid.setSelectionMode(SelectionMode.MULTI);

		// Default sorting on names
		final var initialSortingColumns = getInitialSortingColumns();
		if (initialSortingColumns != null && !initialSortingColumns.isEmpty()) {
			final var orders = new ArrayList<GridSortOrder<T>>();
			for (var column : initialSortingColumns) {
				final var initialSortingOrder = getInitialSortingDirection(column);
				orders.add(new GridSortOrder<>(column, initialSortingOrder));
			}
			if (!orders.isEmpty()) {
				grid.sort(orders);
			}
		}

		if (isAdminRole()) {
			grid.addSelectionListener(it -> {
				final Set<?> selection = it.getAllSelectedItems();
				onSelectionChange(selection);
			});
		}

		// Edit on double click
		grid.addItemDoubleClickListener(it -> {
			final var entity = it.getItem();
			if (entity != null) {
				executeDoubleClickAction(entity);
			} else {
				notifyNotEntity();
			}
		});

		return grid;
	}

	/** Invoked when the selection has changed in the list.
	 *
	 * @param selection the selected items.
	 */
	protected void onSelectionChange(Set<?> selection) {
		final int size = selection.size();
		this.editButton.setEnabled(size == 1);
		this.deleteButton.setEnabled(size > 0);
	}

	/** Initialize the data in the grid, preferably by using lay loading.
	 *
	 * @param grid the grid to fill up.
	 * @param filters the filters to be used in the grid.
	 */
	protected abstract void initializeDataInGrid(G grid, AbstractFilters<F> filters);

	/** Invoked for computing the identifier of an entity. This identifier is used by the Vaadin grid
	 * for comparing the items and detecting their equality.
	 *
	 * @param entity the entity for which an identifier must be computed. The entity is never {@code null} and
	 *     it must have an identifier different than zero replied by {@link IdentifiableEntity#getId()}.
	 * @return the identifier of the entity.
	 */
	protected Long computeIdForEntity(T entity) {
		assert entity != null;
		final var id = entity.getId();
		assert id != 0 : "Entity not in the JPA database"; //$NON-NLS-1$
		return Long.valueOf(id);
	}

	/** Run the double click action for the entity. By default, it runs the editor.
	 * Subclass should override this function to change the double-click action.
	 *
	 * @param entity the entity with which the action must be run.
	 */
	protected void executeDoubleClickAction(T entity) {
		edit(entity);
	}

	/** Refresh the grid content.
	 */
	protected void refreshGrid() {
		this.grid.deselectAll();
		this.grid.getDataProvider().refreshAll();
	}

	/** Refresh the grid content for the given item.
	 *
	 * @param item the item to refresh.
	 */
	protected void refreshItem(T item) {
		final var provider = this.grid.getDataProvider();
		if (item == null || item.getId() == 0) {
			provider.refreshAll();
		} else {
			provider.refreshItem(item, true);
		}
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		if (this.addButton != null) {
			ComponentFactory.setIconItemText(this.addButton, getTranslation("views.add")); //$NON-NLS-1$
		}
		if (this.editButton != null) {
			ComponentFactory.setIconItemText(this.editButton, getTranslation("views.edit")); //$NON-NLS-1$
		}
		if (this.deleteButton != null) {
			ComponentFactory.setIconItemText(this.deleteButton, getTranslation("views.delete")); //$NON-NLS-1$
		}
	}

	/** UI and JPA filters for {@link AbstractGridBaseEntityListView}.
	 * 
	 * @param <T> the type of the entities to be filtered.
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static abstract class AbstractFilters<T> extends Div implements Specification<T>, LocaleChangeObserver {

		private static final long serialVersionUID = 1052687254355760397L;

		private final TextField keywords;

		private final Button resetButton;

		private final Button searchButton;

		/** Constructor.
		 *
		 * @param onSearch the callback function for running the filtering.
		 */
		public AbstractFilters(Runnable onSearch) {
			setWidthFull();
			addClassName("filter-layout"); //$NON-NLS-1$
			addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM, LumoUtility.BoxSizing.BORDER);

			this.keywords = new TextField();
			
			this.resetButton = new Button();
			this.resetButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
			this.resetButton.addClickListener(event -> {
				this.keywords.clear();
				resetFilters();
				onSearch.run();
			});
			this.resetButton.addClickShortcut(Key.ESCAPE);
			this.resetButton.addClickShortcut(Key.CANCEL);
			this.resetButton.addClickShortcut(Key.CLEAR);

			this.searchButton = new Button();
			this.searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			this.searchButton.addClickListener(event -> onSearch.run());
			this.searchButton.addClickShortcut(Key.ENTER);
			this.searchButton.addClickShortcut(Key.FIND);
			this.searchButton.addClickShortcut(Key.EXECUTE);

			final var actions = new Div(this.resetButton, this.searchButton);
			actions.addClassName(LumoUtility.Gap.SMALL);
			actions.addClassName("actions"); //$NON-NLS-1$

			final var options = new HorizontalLayout();
			options.setSpacing(false);
			buildOptionsComponent(options);

			add(this.keywords, options, actions);
		}

		/** Build the component for filtering options.
		 *
		 * @param options the component that should receive the options, never {@code null}.
		 */
		protected abstract void buildOptionsComponent(HorizontalLayout options);
		
		/** Reset the filters.
		 */
		protected abstract void resetFilters();

		/** Build the HQL query for the filtering.
		 * 
		 * @param keywords the keywords to search for.
		 * @param predicates the list of filtering criteria with "or" semantic, being filled by this function.
		 * @param root the root not for the search.
		 * @param criteriaBuilder the criteria builder. It is the Hibernate version in order to
		 *     have access to extra functions, e.g. {@code collate}.
		 */
		protected abstract void buildQueryFor(String keywords, List<Predicate> predicates, Root<T> root, CriteriaBuilder criteriaBuilder);

		@Override
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			return ComponentFactory.newPredicateContainsOneOf(this.keywords.getValue(), root, query,
					criteriaBuilder, this::buildQueryFor);
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			this.keywords.setLabel(getTranslation("views.filters.keywords")); //$NON-NLS-1$
			this.resetButton.setText(getTranslation("views.filters.reset")); //$NON-NLS-1$
			this.searchButton.setText(getTranslation("views.filters.apply")); //$NON-NLS-1$
		}

	}

	/** Authenticated user filtering inside the standard JPA filters for {@link AbstractGridBaseEntityListView}.
	 * This filter panel adds the toggle button for enabling or disabling the filtering of data for the authenticated user.
	 * 
	 * @param <T> the type of the entities to be filtered.
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static abstract class AbstractAuthenticatedUserDataFilters<T> extends AbstractFilters<T> {

		private static final long serialVersionUID = 7953654886013901370L;

		private static final boolean DEFAULT_AUTHENTICATED_USER_FILTERING = true;

		private final Person user;
	
		private ToggleButton restrictToUser;

		private AvatarItem authenticatedUserAvatar;

		/** Constructor.
		 *
		 * @param user the connected user, or {@code null} if the filter does not care about a connected user.
		 * @param onSearch the callback function for running the filtering.
		 */
		public AbstractAuthenticatedUserDataFilters(AuthenticatedUser user, Runnable onSearch) {
			super(onSearch);
			Person pers = null;
			if (user != null) {
				final var optionalUser = user.get();
				if (optionalUser.isPresent()) {
					final var usr = optionalUser.get();
					if (usr != null) {
						pers = usr.getPerson();
					}
				}
			}
			this.user = pers;

			if (this.user != null) {
				final var session = VaadinService.getCurrentRequest().getWrappedSession();
				final var attr = session.getAttribute(buildPreferenceSectionKeyForAuthenticatedUserFiltering());
				var checked = DEFAULT_AUTHENTICATED_USER_FILTERING;
				if (attr != null) {
					if (attr instanceof Boolean bvalue) {
						checked = bvalue.booleanValue();
					} else if (attr instanceof String svalue) {
						checked = Boolean.parseBoolean(svalue);
					}
				}
				this.restrictToUser = new ToggleButton(checked);
				updateAuthenticatedUserToggleButton(this.restrictToUser, this.user);
				this.restrictToUser.addValueChangeListener(it -> onAuthenticatedUserFilteringChange(it.getValue() == null ? DEFAULT_AUTHENTICATED_USER_FILTERING : it.getValue().booleanValue(), onSearch));
			} else {
				this.restrictToUser = null;
			}

			if (this.restrictToUser != null) {
				addComponentAsFirst(this.restrictToUser);
			}
		}

		/** Invoked to update the toggle button for enabling or disabling the filtering dedicated to the authenticated user.
		 * The change listener for the button's value will be automatically added.
		 *
		 * @param button the button to update.
		 * @param person the authenticated user.
		 */
		protected void updateAuthenticatedUserToggleButton(ToggleButton button, Person person) {
			assert button != null;
			assert person != null;

			this.authenticatedUserAvatar = new AvatarItem();

			final var photo = person.getPhotoURL();
			if (photo != null) {
				this.authenticatedUserAvatar.setAvatarURL(photo.toExternalForm());
			}

			button.setLabelComponent(this.authenticatedUserAvatar);
		}

		private String buildPreferenceSectionKeyForAuthenticatedUserFiltering() {
			return new StringBuilder().append(ViewConstants.AUTHENTICATED_USER_FILTER_ROOT).append(getClass().getName()).toString();
		}

		/** Invoked when the filtering on the authenticated person has changed.
		 *
		 * @param enablePersonFilter indicates if the filtering is enable or disable.
		 * @param onSearch the callback function for running the filtering.
		 */
		protected void onAuthenticatedUserFilteringChange(boolean enablePersonFilter, Runnable onSearch) {
			final var session0 = VaadinService.getCurrentRequest().getWrappedSession();
			session0.setAttribute(buildPreferenceSectionKeyForAuthenticatedUserFiltering(), Boolean.valueOf(enablePersonFilter));
			if (onSearch != null) {
				onSearch.run();
			}
		}

		private boolean isRestrictedToAuthenticatedUser() {
			if (this.restrictToUser != null) {
				final var bvalue = this.restrictToUser.getValue();
				if (bvalue == null) {
					return DEFAULT_AUTHENTICATED_USER_FILTERING;
				}
				return bvalue.booleanValue();
			}
			return false;
		}
		
		/** Build the predicate for filtering the JPE entities that corresponds to the authenticated user with
		 * the given identifier.
		 * 
		 * @param root the root element to filter.
		 * @param query the top-level query.
		 * @param criteriaBuilder the tool for building a filtering criteria.
		 * @param user the user in the JPA database.
		 * @return the predicate or {@code null} if there is no need for this predicate.
		 */
		protected abstract Predicate buildPredicateForAuthenticatedUser(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, Person user);

		@Override
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			final var keywordFilter = super.toPredicate(root, query, criteriaBuilder);
			if (this.user != null && isRestrictedToAuthenticatedUser()) {
				final var userPredicate = buildPredicateForAuthenticatedUser(root, query, criteriaBuilder, this.user);
				if (userPredicate != null) {
					if (keywordFilter != null) {
						return criteriaBuilder.and(userPredicate, keywordFilter);
					}
					return userPredicate;
				}
			}
			return keywordFilter;
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			if (this.authenticatedUserAvatar != null) {
				this.authenticatedUserAvatar.setHeading(getTranslation("views.filters.authenticated_user_filter")); //$NON-NLS-1$
			}
		}

	}

	/** Default organization filtering inside the standard JPA filters for {@link AbstractGridBaseEntityListView}.
	 * This filter panel adds the toggle button for enabling or disabling the filtering of data for the default organization.
	 * 
	 * @param <T> the type of the entities to be filtered.
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static abstract class AbstractDefaultOrganizationDataFilters<T> extends AbstractFilters<T> {

		private static final long serialVersionUID = 7953654886013901370L;

		private static final boolean DEFAULT_ORGANIZATION_FILTERING = true;

		private final Supplier<ResearchOrganization> defaultOrganization;

		private final Supplier<FileManager> fileManager;

		private ToggleButton restrictToOrganization;

		private AvatarItem defaultOrganizationAvatar;


		/** Constructor.
		 *
		 * @param defaultOrganizationSupplier the provider of the default organization.
		 * @param fileManager the manager of files on the server.
		 * @param onSearch the callback function for running the filtering.
		 */
		public AbstractDefaultOrganizationDataFilters(Supplier<ResearchOrganization> defaultOrganizationSupplier, Supplier<FileManager> fileManager, Runnable onSearch) {
			super(onSearch);
			
			this.defaultOrganization = defaultOrganizationSupplier;
			this.fileManager = fileManager;

			final var session = VaadinService.getCurrentRequest().getWrappedSession();
			final var attr = session.getAttribute(buildPreferenceSectionKeyForDefaultOrganizationFiltering());
			var checked = DEFAULT_ORGANIZATION_FILTERING;
			if (attr != null) {
				if (attr instanceof Boolean bvalue) {
					checked = bvalue.booleanValue();
				} else if (attr instanceof String svalue) {
					checked = Boolean.parseBoolean(svalue);
				}
			}
			this.restrictToOrganization = new ToggleButton(checked);

			this.defaultOrganizationAvatar = new AvatarItem();
			this.restrictToOrganization.setLabelComponent(this.defaultOrganizationAvatar);
			this.restrictToOrganization.addValueChangeListener(it -> onDefaultOrganizationFilteringChange(it.getValue() == null ? DEFAULT_ORGANIZATION_FILTERING : it.getValue().booleanValue(), onSearch));

			addComponentAsFirst(this.restrictToOrganization);
		}

		/** Invoked to update the icon of the organization in the toggle button.
		 */
		final void updateDefaultOrganizationIconInToggleButton() {
			updateDefaultOrganizationIconInToggleButton(this.defaultOrganizationAvatar, this.defaultOrganization.get(), this.fileManager.get());
		}

		/** Invoked to update the icon of the organization in the toggle button.
		 *
		 * @param organizationAvatar the avatar of the research organization.
		 * @param defaultOrganization the default organization to be considered.
		 * @param fileManager the manager of files on the server.
		 */
		@SuppressWarnings("static-method")
		protected void updateDefaultOrganizationIconInToggleButton(AvatarItem organizationAvatar, ResearchOrganization defaultOrganization, FileManager fileManager) {
			final var logo = defaultOrganization.getPathToLogo();
			if (!Strings.isNullOrEmpty(logo)) {
				var logoFile = FileSystem.convertStringToFile(logo);
				if (logoFile != null) {
					logoFile = fileManager.normalizeForServerSide(logoFile);
					if (logoFile != null) {
						organizationAvatar.setAvatarResource(ComponentFactory.newStreamImage(logoFile));
					}
				}
			}
		}

		private String buildPreferenceSectionKeyForDefaultOrganizationFiltering() {
			return new StringBuilder().append(ViewConstants.DEFAULT_ORGANIZATION_FILTER_ROOT).append(getClass().getName()).toString();
		}

		/** Invoked when the filtering on the default organization has changed.
		 *
		 * @param enableOrganizationFilter indicates if the filtering is enable or disable.
		 * @param onSearch the callback function for running the filtering.
		 */
		protected void onDefaultOrganizationFilteringChange(boolean enableOrganizationFilter, Runnable onSearch) {
			final var session0 = VaadinService.getCurrentRequest().getWrappedSession();
			session0.setAttribute(buildPreferenceSectionKeyForDefaultOrganizationFiltering(), Boolean.valueOf(enableOrganizationFilter));
			if (onSearch != null) {
				onSearch.run();
			}
		}

		private boolean isRestrictedToDefaultOrganization() {
			if (this.restrictToOrganization != null) {
				final var bvalue = this.restrictToOrganization.getValue();
				if (bvalue == null) {
					return DEFAULT_ORGANIZATION_FILTERING;
				}
				return bvalue.booleanValue();
			}
			return false;
		}
		
		/** Build the predicate for filtering the JPE entities that corresponds to the default organization with
		 * the given identifier.
		 * 
		 * @param root the root element to filter.
		 * @param query the top-level query.
		 * @param criteriaBuilder the tool for building a filtering criteria.
		 * @param defaultOrganization the default organization.
		 * @return the predicate or {@code null} if there is no need for this predicate.
		 */
		protected abstract Predicate buildPredicateForDefaultOrganization(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, ResearchOrganization defaultOrganization);

		@Override
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			final var keywordFilter = super.toPredicate(root, query, criteriaBuilder);
			if (isRestrictedToDefaultOrganization()) {
				final var organizationPredicate = buildPredicateForDefaultOrganization(root, query, criteriaBuilder, this.defaultOrganization.get());
				if (organizationPredicate != null) {
					if (keywordFilter != null) {
						return criteriaBuilder.and(organizationPredicate, keywordFilter);
					}
					return organizationPredicate;
				}
			}
			return keywordFilter;
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			this.defaultOrganizationAvatar.setHeading(getTranslation("views.filters.default_organization_filter")); //$NON-NLS-1$
		}

	}

}
