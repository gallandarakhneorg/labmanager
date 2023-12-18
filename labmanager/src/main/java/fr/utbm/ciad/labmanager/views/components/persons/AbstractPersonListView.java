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

package fr.utbm.ciad.labmanager.views.components.persons;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.ibm.icu.text.Normalizer2;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridLazyDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.member.ChronoMembershipComparator;
import fr.utbm.ciad.labmanager.data.member.Gender;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.user.User;
import fr.utbm.ciad.labmanager.data.user.UserRole;
import fr.utbm.ciad.labmanager.services.member.MembershipService;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.user.UserService;
import fr.utbm.ciad.labmanager.views.ViewConstants;
import fr.utbm.ciad.labmanager.views.components.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.avatars.AvatarItem;
import fr.utbm.ciad.labmanager.views.components.badges.BadgeRenderer;
import fr.utbm.ciad.labmanager.views.components.badges.BadgeState;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.vaadin.lineawesome.LineAwesomeIcon;

/** Abstract implementation of a list of persons.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Uses(Icon.class)
@CssImport(value = "./themes/labmanager/components/grid-row-hover.css", themeFor = "vaadin-grid")
public abstract class AbstractPersonListView extends Composite<VerticalLayout> implements LocaleChangeObserver {

	private static final long serialVersionUID = -7781377605320634897L;

	/** Define the color of the border of the regular user avatars: red.
	 */
	private static final int ADMINISTRATOR_BORDER_COLOR = 0;
	
	/** Define the color of the border of the regular user avatars: dark blue.
	 */
	private static final int USER_BORDER_COLOR = 5;

	private final Logger logger;

	private final MessageSourceAccessor messages;

	private final AuthenticatedUser authenticatedUser;

	private final PersonService personService;

	private final UserService userService;

	private final MembershipService membershipService;

	private final ChronoMembershipComparator membershipComparator;

	private final PersonDataProvider dataProvider;
	
	private Map<Integer, User> personIdToUserMap = new HashMap<>();

	private Grid<Person> grid;

	private Column<Person> validationColumn;

	private Column<Person> nameColumn;

	private Column<Person> orcidColumn;

	private Column<Person> organizationColumn;

	private Filters filters;

	private MenuItem addButton;

	private MenuItem editButton;

	private MenuItem deleteButton;

	/** Constructor.
	 *
	 * @param personService the service for accessing to the persons.
	 * @param userService the service for accessing to the users.
	 * @param membershipService the service for accessing to the memberships.
	 * @param membershipComparator the comparator that must be used for comparing the memberships. It is assumed that
	 *     the memberships are sorted in reverse chronological order first.
	 * @param dataProvider the provider of lazy data.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param logger the logger to be used by this view.
	 */
	public AbstractPersonListView(
			PersonService personService, UserService userService, MembershipService membershipService,
			ChronoMembershipComparator membershipComparator, PersonDataProvider dataProvider,
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages, Logger logger) {
		this.logger = logger;
		this.messages = messages;
		this.authenticatedUser = authenticatedUser;
		this.personService = personService;
		this.userService = userService;
		this.membershipService = membershipService;
		this.membershipComparator = membershipComparator;
		this.dataProvider = dataProvider;

		refreshUsers();
		
		final VerticalLayout rootContainer = getContent();

		rootContainer.setSizeFull();
		rootContainer.setPadding(false);
		rootContainer.setSpacing(false);

		this.filters = createFilters();
		this.grid = createGrid();
		final MenuBar menu = createMenuBar();

		rootContainer.add(createMobileFilters(), this.filters, menu, this.grid);
	}

	/** Replies if the authenticated user has the admin role.
	 *
	 * @return {@code true} if there is an authenticated user with the admin role.
	 */
	protected boolean isAdminRole() {
		return this.authenticatedUser != null && this.authenticatedUser.get().isPresent()
				&& this.authenticatedUser.get().get().getRole() == UserRole.ADMIN;
	}

	/** Create the filters for mobile device.
	 *
	 * @return the filters
	 */
	protected HorizontalLayout createMobileFilters() {
		final HorizontalLayout mobileFilters = new HorizontalLayout();
		mobileFilters.setWidthFull();
		mobileFilters.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER, LumoUtility.AlignItems.CENTER);
		mobileFilters.addClassName("mobile-filters"); //$NON-NLS-1$

		Icon mobileIcon = LumoIcon.PLUS.create();
		Span filtersHeading = new Span(getTranslation("views.filters")); //$NON-NLS-1$
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
	protected Filters createFilters() {
		return new Filters(this::refreshGrid);
	}

	@SuppressWarnings("static-method")
	private Component createOrganizationComponent(Gender personGender, Iterator<Membership> memberships) {
		if (memberships.hasNext()) {
			final List<Span> spans = new ArrayList<>();
			while (memberships.hasNext()) {
				final Membership mbr = memberships.next();
				final ResearchOrganization organization = mbr.getResearchOrganization();
				final String name = organization.getAcronymOrName();
				Span span = new Span(name);
				if (mbr.isFormer()) {
					BadgeState.CONTRAST_PILL.assignTo(span);
				} else {
					BadgeState.SUCCESS_PILL.assignTo(span);
				}
				spans.add(span);
			}
			if (!spans.isEmpty()) {
				return new Span(spans.toArray(new Span[spans.size()]));
			}
		}
		return new Span();
	}

	private Component createOrganizationComponent(Person person) {
		return createOrganizationComponent(person.getGender(), new MembershipIterator(person));
	}

	private Component createNameComponent(Person person) {
		final String fullName = person.getFullNameWithLastNameFirst();
		final URL photo = person.getPhotoURL();

		String contactDetails = null;
		Integer avatarBorder = null;
		final User user = this.personIdToUserMap.get(Integer.valueOf(person.getId()));
		if (user != null) {
			final String login = user.getLogin();
			if (!Strings.isNullOrEmpty(login)) {
				final UserRole role = user.getRole();
				avatarBorder = Integer.valueOf(role == UserRole.ADMIN ? ADMINISTRATOR_BORDER_COLOR : USER_BORDER_COLOR);
				contactDetails = getTranslation("views.persons.person_details.login", //$NON-NLS-1$
						login,
						role.getLabel(this.messages, getLocale()));
			}
		}

		final AvatarItem avatar = new AvatarItem();
		avatar.setHeading(fullName);
		avatar.setAvatarBorderColor(avatarBorder);
		if (!Strings.isNullOrEmpty(contactDetails)) {
			avatar.setDescription(contactDetails);
		}
		if (photo != null) {
			avatar.setAvatarURL(photo.toExternalForm());
		}

		return avatar;
	}

	/** Create the list of tools.
	 *
	 * @return the menu bar, or {@code null} if the menu cannot be created because of access rights.
	 */
	protected MenuBar createMenuBar() {
		if (isAdminRole()) {
			final MenuBar menu = new MenuBar();
			menu.addThemeVariants(MenuBarVariant.LUMO_ICON);

			this.addButton = ComponentFactory.addIconItem(menu, LineAwesomeIcon.PLUS_SOLID, null, null);

			this.editButton = ComponentFactory.addIconItem(menu, LineAwesomeIcon.EDIT_SOLID, null, null, it -> editSelection());
			this.editButton.setEnabled(false);

			this.deleteButton = ComponentFactory.addIconItem(menu, LineAwesomeIcon.TRASH_SOLID, null, null, it -> deleteSelectionWithQuery());
			this.deleteButton.addThemeNames("error"); //$NON-NLS-1$
			this.deleteButton.setEnabled(false);

			return menu;
		}
		return null;
	}

	/** Delete the current selection without querying the user.
	 * 
	 * @see #deleteSelectionWithQuery()
	 */
	protected void deleteSelection() {
		try {
			// Get the selection again because this handler is run in another session than the one of the function
			int realSize = 0;
			for (final Integer personId : this.grid.getSelectedItems().stream().map(it0 -> Integer.valueOf(it0.getId())).collect(Collectors.toList())) {
				final Person deletedPerson = this.personService.removePerson(personId.intValue());
				if (deletedPerson != null) {
					final StringBuilder msg = new StringBuilder("Data for "); //$NON-NLS-1$
					msg.append(deletedPerson.getFullName());
					msg.append(" (id: "); //$NON-NLS-1$
					msg.append(personId);
					msg.append(") has been deleted by "); //$NON-NLS-1$
					msg.append(AuthenticatedUser.getUserName(this.authenticatedUser));
					this.logger.info(msg.toString());
					// Deselected the person
					this.grid.getSelectionModel().deselect(deletedPerson);
				}
				++realSize;
			}
			refreshGrid();
			notifyDeleted(realSize);
		} catch (Throwable ex) {
			refreshGrid();
			notifyDeletionError(ex);
		}
	}

	/** Query for the deletion the current selection and do the deletion if it is accepted.
	 *
	 * @see #deleteSelection()
	 */
	protected void deleteSelectionWithQuery() {
		deleteWithQuery(this.grid.getSelectedItems());
	}

	/** Query for the deletion the current selection and do the deletion if it is accepted.
	 *
	 * @param persons the person to be deleted.
	 * @see #deleteSelection()
	 */
	protected void deleteWithQuery(Set<Person> persons) {
		if (!persons.isEmpty()) {
			final int size = persons.size();
			ComponentFactory.createDeletionDialog(this,
					getTranslation("views.persons.delete.title", Integer.valueOf(size)), //$NON-NLS-1$
					getTranslation("views.persons.delete.message", Integer.valueOf(size)), //$NON-NLS-1$
					it ->  deleteSelection())
			.open();
		}
	}

	/** Notify the user that the persons were deleted.
	 *
	 * @param size the number of deleted persons
	 */
	protected void notifyDeleted(int size) {
		final Notification notification = Notification.show(getTranslation("views.persons.delete_success", //$NON-NLS-1$
				Integer.valueOf(size)));
		notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
	}

	/** Notify the user that the persons cannot be deleted.
	 */
	protected void notifyDeletionError(Throwable error) {
		final StringBuilder msg = new StringBuilder("Error when deleting data for the persons by "); //$NON-NLS-1$
		msg.append(AuthenticatedUser.getUserName(this.authenticatedUser));
		msg.append(": "); //$NON-NLS-1$
		msg.append(error.getLocalizedMessage());
		this.logger.info(msg.toString(), error);
		final Notification notification = Notification.show(getTranslation("views.persons.delete_error", //$NON-NLS-1$
				error.getLocalizedMessage()));
		notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
	}

	private void editSelection() {
		final Optional<Person> selection = this.grid.getSelectionModel().getFirstSelectedItem();
		if (selection.isPresent()) {
			final Person person = selection.get();
			edit(person);
		}
	}

	private void edit(Person person) {
		final Dialog dialog = new Dialog();
		dialog.setModal(true);
		dialog.setCloseOnEsc(true);
		dialog.setCloseOnOutsideClick(true);
		dialog.setDraggable(true);
		dialog.setResizable(true);			
		dialog.setHeaderTitle(getTranslation("views.persons.edit_person", person.getFullName())); //$NON-NLS-1$
		dialog.setWidthFull();

		final User user = this.userService.getUserFor(person);
		
		final EmbeddedPersonEditor editor = new EmbeddedPersonEditor(
				person, this.personService,
				user, this.userService,
				this.authenticatedUser, this.messages);
		dialog.add(editor);

		final Button saveButton = new Button(getTranslation("views.save"), e -> { //$NON-NLS-1$
			if (editor.save(this.personService, this.userService)) {
				dialog.close();
				refreshGrid();
			}
		});
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		saveButton.addClickShortcut(Key.ENTER);
		saveButton.setIcon(LineAwesomeIcon.SAVE_SOLID.create());

		final Button cancelButton = new Button(getTranslation("views.cancel"), e -> dialog.close()); //$NON-NLS-1$

		dialog.getFooter().add(cancelButton, saveButton);

		dialog.open();
	}

	/** Create the grid component.
	 *
	 * @return the grid component.
	 */
	protected Grid<Person> createGrid() {
		final Grid<Person> grid = new Grid<>(Person.class, false);

		this.nameColumn = grid.addColumn(it -> it.getFullNameWithLastNameFirst())
				.setRenderer(new ComponentRenderer<>(this::createNameComponent))
				.setAutoWidth(true)
				.setFrozen(true)
				.setSortProperty("lastName", "firstName"); //$NON-NLS-1$ //$NON-NLS-2$
		this.orcidColumn = grid.addColumn(person -> person.getORCID())
				.setAutoWidth(true)
				.setSortProperty("orcid"); //$NON-NLS-1$

		this.organizationColumn = grid.addColumn(person -> person)
				.setRenderer(new ComponentRenderer<>(this::createOrganizationComponent))
				.setAutoWidth(true)
				.setSortable(false);
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

		ComponentFactory.addGridCellHoverMenuBar(grid, (person, menuBar) -> {
			MenuItem item = menuBar.addItem(LineAwesomeIcon.EDIT.create(), event -> {
				edit(person);
			});
			item.setAriaLabel(getTranslation("views.edit")); //$NON-NLS-1$
			final SvgIcon deleteIcon = LineAwesomeIcon.TRASH_SOLID.create();
			deleteIcon.setColor("var(--lumo-error-color)"); //$NON-NLS-1$
			item = menuBar.addItem(deleteIcon, event -> {
				deleteWithQuery(Collections.singleton(person));
			});
			item.setAriaLabel(getTranslation("views.delete")); //$NON-NLS-1$
		});

		grid.setPageSize(ViewConstants.GRID_PAGE_SIZE);
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
		grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);
		grid.setSelectionMode(SelectionMode.MULTI);

		// Default sorting on names
		grid.sort(Collections.singletonList(new GridSortOrder<>(this.nameColumn, SortDirection.ASCENDING)));

		if (isAdminRole()) {
			grid.addSelectionListener(it -> {
				final Set<?> selection = it.getAllSelectedItems();
				final int size = selection.size();
				this.editButton.setEnabled(size == 1);
				this.deleteButton.setEnabled(size > 0);
			});
		}

		final GridLazyDataView<Person> dataView =  grid.setItems(query -> {
			return this.dataProvider.fetch(
					this.personService,
					VaadinSpringDataHelpers.toSpringPageRequest(query),
					this.filters).stream();
		});
		dataView.setItemCountEstimate(ViewConstants.GRID_PAGE_SIZE * 10); 
		dataView.setItemCountEstimateIncrease(ViewConstants.GRID_PAGE_SIZE); 

		// Edit on double click
		grid.addItemDoubleClickListener(it -> {
			executeDoubleClickAction(it.getItem());
		});

		return grid;
	}
	
	/** Run the double click action for the person. By default, it runs the editor.
	 * Subclass should override this function to change the double-click action.
	 *
	 * @param person the person with which the action must be run.
	 */
	protected void executeDoubleClickAction(Person person) {
		edit(person);
	}

	/** Refresh the user list.
	 */
	private void refreshUsers() {
		this.personIdToUserMap = new HashMap<>();
		for (final User user : this.userService.getAllUsers()) {
			final Person person = user.getPerson();
			this.personIdToUserMap.put(Integer.valueOf(person.getId()), user);
		}
	}

	/** Refresh the grid content.
	 */
	protected void refreshGrid() {
		refreshUsers();
		this.grid.getDataProvider().refreshAll();
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		this.validationColumn.setHeader(getTranslation("views.validated")); //$NON-NLS-1$
		this.nameColumn.setHeader(getTranslation("views.name")); //$NON-NLS-1$
		this.orcidColumn.setHeader(getTranslation("views.orcid")); //$NON-NLS-1$
		this.organizationColumn.setHeader(getTranslation("views.organizations")); //$NON-NLS-1$
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

	/** UI and JPA filters for {@link AbstractPersonListView}.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class Filters extends Div implements Specification<Person>, LocaleChangeObserver {

		private static final long serialVersionUID = -3722724440696178712L;

		private static final Pattern PATTERN = Pattern.compile(".[\\p{M}]"); //$NON-NLS-1$

		private static final String FOR_ONE = "_"; //$NON-NLS-1$

		private static final String FOR_MANY = "%"; //$NON-NLS-1$

		private static final Normalizer2 NORMALIZER = Normalizer2.getNFKDInstance();

		private final TextField keywords;

		private final Checkbox includeNames;

		private final Checkbox includeOrcids;

		private final Checkbox includeOrganizations;

		private final Button resetButton;

		private final Button searchButton;

		/** Constructor.
		 *
		 * @param onSearch
		 */
		public Filters(Runnable onSearch) {
			setWidthFull();
			addClassName("filter-layout"); //$NON-NLS-1$
			addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM, LumoUtility.BoxSizing.BORDER);

			this.keywords = new TextField();

			this.includeNames = new Checkbox(true);
			this.includeOrcids = new Checkbox(true);
			this.includeOrganizations = new Checkbox(false);

			final HorizontalLayout options = new HorizontalLayout();
			options.setSpacing(false);
			options.add(this.includeNames, this.includeOrcids, this.includeOrganizations);

			// Action buttons

			this.resetButton = new Button();
			this.resetButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
			this.resetButton.addClickListener(event -> {
				this.keywords.clear();
				this.includeNames.setValue(Boolean.TRUE);
				this.includeOrcids.setValue(Boolean.TRUE);
				this.includeOrganizations.setValue(Boolean.TRUE);
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

			final Div actions = new Div(this.resetButton, this.searchButton);
			actions.addClassName(LumoUtility.Gap.SMALL);
			actions.addClassName("actions"); //$NON-NLS-1$

			add(this.keywords, options, actions);
		}

		private static List<StringBuilder> buildCases(String filter) {
			final List<StringBuilder> allCases = new ArrayList<>();
			for (final String filterItem : filter.split("[ \n\r\t\f%]+")) { //$NON-NLS-1$
				final StringBuilder filter0 = new StringBuilder(FOR_MANY);
				String normedFilter0 = NORMALIZER.normalize(filterItem);
				normedFilter0 = normedFilter0.toLowerCase();
				final Matcher matcher = PATTERN.matcher(normedFilter0);
				normedFilter0 = matcher.replaceAll(FOR_ONE);
				filter0.append(normedFilter0);
				filter0.append(FOR_MANY);
				allCases.add(filter0);
			}
			return allCases;
		}

		/** Build the HQL query for the filtering.
		 * 
		 * @param cases the list of all the words to search for.
		 * @param root the root not for the search.
		 * @param criteriaBuilder the criteria builder. It is the Hibernate version in order to
		 *     have access to extra functions, e.g. {@code collate}.
		 * @return the selection predicate.
		 */
		private Predicate buildQuery(List<StringBuilder> cases, Root<Person> root, CriteriaBuilder criteriaBuilder) {
			final List<Predicate> predicates = new ArrayList<>();
			for (final StringBuilder acase : cases) {
				final List<Predicate> predicates0 = new ArrayList<>();
				final String lcf = acase.toString(); 
				if (this.includeNames.getValue() == Boolean.TRUE) {
					predicates0.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), lcf)); //$NON-NLS-1$
					predicates0.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), lcf)); //$NON-NLS-1$
				}

				if (this.includeOrcids.getValue() == Boolean.TRUE) {
					predicates0.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("orcid")), lcf)); //$NON-NLS-1$
				}
				if (this.includeOrganizations.getValue() == Boolean.TRUE) {
					final Predicate startPeriodPredicate = criteriaBuilder.or(
							criteriaBuilder.isNull(root.get("memberships").get("memberSinceWhen")), //$NON-NLS-1$ //$NON-NLS-2$
							criteriaBuilder.lessThanOrEqualTo(root.get("memberships").get("memberSinceWhen"), criteriaBuilder.localDate())); //$NON-NLS-1$ //$NON-NLS-2$

					final Predicate endPeriodPredicate = criteriaBuilder.or(
							criteriaBuilder.isNull(root.get("memberships").get("memberToWhen")), //$NON-NLS-1$ //$NON-NLS-2$
							criteriaBuilder.greaterThanOrEqualTo(root.get("memberships").get("memberToWhen"), criteriaBuilder.localDate())); //$NON-NLS-1$ //$NON-NLS-2$

					final Predicate textPredicate = criteriaBuilder.or(
							criteriaBuilder.like(criteriaBuilder.lower(root.get("memberships").get("researchOrganization").get("acronym")), lcf), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							criteriaBuilder.like(criteriaBuilder.lower(root.get("memberships").get("researchOrganization").get("name")), lcf)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

					final Predicate gpredicate = criteriaBuilder.and(startPeriodPredicate, endPeriodPredicate, textPredicate);

					predicates0.add(gpredicate);
				}
				predicates.add(criteriaBuilder.or(predicates0.toArray(new Predicate[predicates0.size()])));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
		}

		@Override
		public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			final String kws = this.keywords.getValue().trim();
			if (!Strings.isNullOrEmpty(kws)) {
				final List<StringBuilder> cases = buildCases(kws);
				return buildQuery(cases, root, criteriaBuilder);
			}
			return null;
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			this.keywords.setLabel(getTranslation("views.filters.keywords")); //$NON-NLS-1$
			this.includeNames.setLabel(getTranslation("views.filters.include_names")); //$NON-NLS-1$
			this.includeOrcids.setLabel(getTranslation("views.filters.include_orcids")); //$NON-NLS-1$
			this.includeOrganizations.setLabel(getTranslation("views.filters.include_organizations")); //$NON-NLS-1$
			this.resetButton.setText(getTranslation("views.filters.reset")); //$NON-NLS-1$
			this.searchButton.setText(getTranslation("views.filters.apply")); //$NON-NLS-1$
		}

	}

	/** Provider of data for persons to be displayed in the list of persons view.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	@FunctionalInterface
	protected interface PersonDataProvider {

		/** Fetch person data.
		 *
		 * @param personService the service to have access tothe JPA.
		 * @param pageRequest the request for paging the data.
		 * @param filters the filters to apply for selecting the data.
		 * @return the lazy data page.
		 */
		Page<Person> fetch(PersonService personService, PageRequest pageRequest, Filters filters);

	}

	/** Membership iterator for the person list view.
	 * This iterator assumes that the memberships are sorted according to a {@link ChronoMembershipComparator}
	 * and it stops as soon as all the active memberships are returned, or if there is none, when the first
	 * former memberships is returned. Future memberships are not considered.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	private class MembershipIterator implements Iterator<Membership> {

		private final Iterator<Membership> base;

		private boolean foundActive;

		private Membership next;

		private MembershipIterator(Person person) {
			this.base = AbstractPersonListView.this.membershipService.getMembershipsForPerson(person.getId()).stream()
					.filter(it -> !it.isFuture()).sorted(AbstractPersonListView.this.membershipComparator).iterator();
			searchNext();
		}

		private void searchNext() {
			this.next = null;
			if (this.base.hasNext()) {
				final Membership mbr = this.base.next();
				if (!mbr.isFormer() || !this.foundActive) {
					this.foundActive = true;
					this.next = mbr;
				}
			}
		}

		@Override
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public Membership next() {
			final Membership currentNext = this.next;
			searchNext();
			return currentNext;
		}

	}

}
