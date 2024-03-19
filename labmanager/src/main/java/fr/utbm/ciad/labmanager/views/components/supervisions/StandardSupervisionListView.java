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

import java.util.Arrays;
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
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.supervision.Supervision;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityDeletingContext;
import fr.utbm.ciad.labmanager.services.member.MembershipService;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.organization.OrganizationAddressService;
import fr.utbm.ciad.labmanager.services.organization.ResearchOrganizationService;
import fr.utbm.ciad.labmanager.services.scientificaxis.ScientificAxisService;
import fr.utbm.ciad.labmanager.services.supervision.SupervisionService;
import fr.utbm.ciad.labmanager.services.user.UserService;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityListView;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Hibernate;
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

	private final SupervisionService supervisionService;

	private final MembershipService membershipService;
	
	private final PersonService personService;
	
	private final UserService userService;
	
	private final ResearchOrganizationService organizationService;
	
	private final OrganizationAddressService addressService;

	private final ScientificAxisService axisService;

	private Column<Supervision> supervisedPersonColumn;

	private Column<Supervision> typeColumn;

	private Column<Supervision> dateColumn;

	private Column<Supervision> supervisorsColumn;

	/** Constructor.
	 *
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param supervisionService the service for accessing the supervisions.
	 * @param membershipService the service for accessing the membership JPA entities.
	 * @param personService the service for accessing the person JPA entities.
	 * @param userService the service for accessing the connected user JPA entities.
	 * @param organizationService the service for accessing the organization JPA entities.
	 * @param addressService the service for accessing the organization address JPA entities.
	 * @param axisService the service for accessing the scientific axis JPA entities.
	 * @param logger the logger to use.
	 */
	public StandardSupervisionListView(
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			SupervisionService supervisionService, MembershipService membershipService, PersonService personService, UserService userService,
			ResearchOrganizationService organizationService, OrganizationAddressService addressService, ScientificAxisService axisService,
			Logger logger) {
		super(Supervision.class, authenticatedUser, messages, logger,
				"views.supervisions.delete.title", //$NON-NLS-1$
				"views.supervisions.delete.message", //$NON-NLS-1$
				"views.supervision.delete_success", //$NON-NLS-1$
				"views.supervision.delete_error"); //$NON-NLS-1$
		this.supervisionService = supervisionService;
		this.membershipService = membershipService;
		this.personService = personService;
		this.userService = userService;
		this.organizationService = organizationService;
		this.addressService = addressService;
		this.axisService = axisService;
		this.dataProvider = (ps, query, filters) -> ps.getAllSupervisions(query, filters, this::initializeEntityFromJPA);
		initializeDataInGrid(getGrid(), getFilters());
	}

	private void initializeEntityFromJPA(Supervision entity) {
		// Force the loaded of the lazy data that is needed for rendering the table
		Hibernate.initialize(entity.getSupervisedPerson());
		Hibernate.initialize(entity.getSupervisors());
		entity.getSupervisors().forEach(it -> {
			Hibernate.initialize(it.getSupervisor());
		});
	}

	@Override
	protected Filters<Supervision> createFilters() {
		return new SupervisionFilters(this::refreshGrid);
	}

	@Override
	protected boolean createGridColumns(Grid<Supervision> grid) {
		this.supervisedPersonColumn = grid.addColumn(new ComponentRenderer<>(this::createSupervisedPersonComponent))
				.setAutoWidth(true);
		this.typeColumn = grid.addColumn(this::getTypeLabel)
				.setAutoWidth(false);
		this.dateColumn = grid.addColumn(this::getDateLabel)
				.setAutoWidth(false)
				.setSortProperty("supervisedPerson.memberToWhen", "defenseDate"); //$NON-NLS-1$ //$NON-NLS-2$
		this.supervisorsColumn = grid.addColumn(new ComponentRenderer<>(this::createSupervisorsComponent))
				.setAutoWidth(true);
		return isAdminRole();
	}

	private Component createSupervisedPersonComponent(Supervision supervision) {
		final var membership = supervision.getSupervisedPerson();
		if (membership != null) {
			final var person = membership.getPerson();
			if (person != null) {
				return ComponentFactory.newPersonAvatar(person);
			}
		}
		return new Span();
	}

	private String getTypeLabel(Supervision supervision) {
		final var membership = supervision.getSupervisedPerson();
		if (membership != null) {
			final var person = membership.getPerson();
			final var gender = person == null ? null : person.getGender();
			return membership.getMemberStatus().getLabel(getMessageSourceAccessor(), gender, false, getLocale());
		}
		return ""; //$NON-NLS-1$
	}

	private String getDateLabel(Supervision supervision) {
		return supervision.getYearRange().toString();
	}

	private Component createSupervisorsComponent(Supervision supervision) {
		final var supervisors = supervision.getSupervisors();
		if (supervisors != null && !supervisors.isEmpty()) {
			final var layout = new HorizontalLayout();
			layout.setSpacing(false);
			layout.setPadding(false);
			for (final var supervisor : supervisors) {
				final var supervisorComponent =  ComponentFactory.newPersonAvatar(supervisor.getSupervisor());
				layout.add(supervisorComponent);
			}
			return layout;
		}
		return new Span();
	}

	@Override
	protected List<Column<Supervision>> getInitialSortingColumns() {
		return Arrays.asList(this.dateColumn, this.supervisedPersonColumn);
	}

	@Override
	protected SortDirection getInitialSortingDirection(Column<Supervision> column) {
		if (column == this.dateColumn) {
			return SortDirection.DESCENDING;
		}
		return SortDirection.ASCENDING;
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
				this.membershipService, this.personService, this.userService,
				this.organizationService, this.addressService, this.axisService,
				getAuthenticatedUser(), getMessageSourceAccessor());
		final var newEntity = editor.isNewEntity();
		final SerializableBiConsumer<Dialog, Supervision> refreshAll = (dialog, entity) -> {
			// The person should be loaded because it was not loaded before
			this.supervisionService.inSession(session -> {
				session.load(entity, Long.valueOf(entity.getId()));
				initializeEntityFromJPA(entity);
			});
			refreshGrid();
		};
		final SerializableBiConsumer<Dialog, Supervision> refreshOne = (dialog, entity) -> {
			// The person should be loaded because it was not loaded before
			this.supervisionService.inSession(session -> {
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
	protected EntityDeletingContext<Supervision> createDeletionContextFor(Set<Supervision> entities) {
		return this.supervisionService.startDeletion(entities);
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.supervisedPersonColumn.setHeader(getTranslation("views.supervised_person")); //$NON-NLS-1$
		this.typeColumn.setHeader(getTranslation("views.type")); //$NON-NLS-1$
		this.dateColumn.setHeader(getTranslation("views.period")); //$NON-NLS-1$
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

		private Checkbox includeTypes;

		/** Constructor.
		 *
		 * @param onSearch
		 */
		public SupervisionFilters(Runnable onSearch) {
			super(onSearch);
		}

		@Override
		protected Component getOptionsComponent() {
			this.includeTypes = new Checkbox(true);

			final var options = new HorizontalLayout();
			options.setSpacing(false);
			options.add(this.includeTypes);

			return options;
		}

		@Override
		protected void resetFilters() {
			this.includeTypes.setValue(Boolean.TRUE);
		}

		@Override
		protected void buildQueryFor(String keywords, List<Predicate> predicates, Root<Supervision> root,
				CriteriaBuilder criteriaBuilder) {
			if (this.includeTypes.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("types")), keywords)); //$NON-NLS-1$
			}
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			this.includeTypes.setLabel(getTranslation("views.filters.include_types")); //$NON-NLS-1$
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
