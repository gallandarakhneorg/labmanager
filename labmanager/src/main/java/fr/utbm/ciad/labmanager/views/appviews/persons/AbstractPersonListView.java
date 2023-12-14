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

package fr.utbm.ciad.labmanager.views.appviews.persons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.icu.text.Normalizer2;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridLazyDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import fr.utbm.ciad.labmanager.data.member.ChronoMembershipComparator;
import fr.utbm.ciad.labmanager.data.member.Gender;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.services.member.MembershipService;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.views.ViewConstants;
import fr.utbm.ciad.labmanager.views.components.BadgeRenderer;
import fr.utbm.ciad.labmanager.views.components.BadgeState;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

/** Abstract implementation of a list of persons.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Uses(Icon.class)
public abstract class AbstractPersonListView extends Composite<VerticalLayout> implements LocaleChangeObserver {

	private static final long serialVersionUID = -7781377605320634897L;

	private final PersonService personService;

	private final MembershipService membershipService;

	private final ChronoMembershipComparator membershipComparator;

	private Grid<Person> grid;

	private Column<Person> validationColumn;

	private Column<Person> nameColumn;

	private Column<Person> orcidColumn;

	private Column<Person> organizationColumn;

	private Filters filters;

	/** Constructor.
	 *
	 * @param personService the service for accessing to the persons.
	 * @param membershipService the service for accessing to the memberships.
	 * @param membershipComparator the comparator that must be used for comparing the memberships. It is assumed that
	 *     the memberships are sorted in reverse chronological order first.
	 */
	public AbstractPersonListView(PersonService personService, MembershipService membershipService, ChronoMembershipComparator membershipComparator) {
		this.personService = personService;
		this.membershipService = membershipService;
		this.membershipComparator = membershipComparator;

		final VerticalLayout rootContainer = getContent();

		rootContainer.setSizeFull();
		rootContainer.setPadding(false);
		rootContainer.setSpacing(false);

		this.filters = createFilters();
		this.grid = createGrid();

		rootContainer.add(createMobileFilters(), this.filters, this.grid);
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

		Icon mobileIcon = new Icon("lumo", "plus"); //$NON-NLS-1$ //$NON-NLS-2$
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
		return new Filters(() -> refreshGrid());
	}

	private static Component createOrganizationComponent(Gender personGender, Iterator<Membership> memberships) {
		if (memberships.hasNext()) {
			final List<Span> spans = new ArrayList<>();
			while (memberships.hasNext()) {
				final Membership mbr = memberships.next();
				final ResearchOrganization organization = mbr.getResearchOrganization();
				final String name = organization.getAcronymOrName();
				Span span = new Span(name);
				span.setTitle(mbr.getMemberStatus().getLabel(personGender));
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

	/** Create the grid component.
	 *
	 * @return the grid component.
	 */
	protected Grid<Person> createGrid() {
		final Grid<Person> grid = new Grid<>(Person.class, false);

		this.nameColumn = grid.addColumn(it -> it.getFullNameWithLastNameFirst())
				.setAutoWidth(true)
				.setSortable(true);
		this.orcidColumn = grid.addColumn(person -> person.getORCID())
				.setAutoWidth(true)
				.setSortable(true);
		this.organizationColumn = grid.addColumn(person -> person)
				.setRenderer(new ComponentRenderer<>(this::createOrganizationComponent))
				.setAutoWidth(true)
				.setSortable(true);
		this.validationColumn = grid.addColumn(new BadgeRenderer<>((data, callback) -> {
			if (data.isValidated()) {
				callback.create(BadgeState.SUCCESS, null, getTranslation("views.validated")); //$NON-NLS-1$
			} else {
				callback.create(BadgeState.ERROR, null, getTranslation("views.validable")); //$NON-NLS-1$
			}
		})).setAutoWidth(false).setSortable(true).setWidth("0%"); //$NON-NLS-1$

		grid.setPageSize(ViewConstants.GRID_PAGE_SIZE);
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
		grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

		final GridLazyDataView<Person> dataView =  grid.setItems(query -> { 
			return this.personService.getAllPersons(
					VaadinSpringDataHelpers.toSpringPageRequest(query),
					this.filters).stream();
		});
		dataView.setItemCountEstimate(ViewConstants.GRID_PAGE_SIZE * 10); 
		dataView.setItemCountEstimateIncrease(ViewConstants.GRID_PAGE_SIZE); 

		return grid;
	}

	/** Refresh the grid content.
	 */
	protected void refreshGrid() {
		this.grid.getDataProvider().refreshAll();
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		this.validationColumn.setHeader(getTranslation("views.validated")); //$NON-NLS-1$
		this.nameColumn.setHeader(getTranslation("views.name")); //$NON-NLS-1$
		this.orcidColumn.setHeader(getTranslation("views.orcid")); //$NON-NLS-1$
		this.organizationColumn.setHeader(getTranslation("views.organizations")); //$NON-NLS-1$
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
			this.includeOrganizations = new Checkbox(true);

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
					//predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("orcid")), lcf)); //$NON-NLS-1$
				}
				predicates.add(criteriaBuilder.or(predicates0.toArray(new Predicate[predicates0.size()])));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
		}

		@Override
		public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			if (!this.keywords.isEmpty()) {
				final List<StringBuilder> cases = buildCases(this.keywords.getValue());
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
