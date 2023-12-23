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

package fr.utbm.ciad.labmanager.views.components.organizations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider.FetchCallback;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.services.organization.ResearchOrganizationService;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.views.components.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.countryflag.CountryFlag;
import fr.utbm.ciad.labmanager.views.components.avatars.AvatarItem;
import fr.utbm.ciad.labmanager.views.components.badges.BadgeRenderer;
import fr.utbm.ciad.labmanager.views.components.badges.BadgeState;
import fr.utbm.ciad.labmanager.views.components.entities.AbstractEntityListView;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/** List all the organizations.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class StandardOrganizationListView extends AbstractEntityListView<ResearchOrganization> {

	private static final long serialVersionUID = -2162467602125180982L;

	private final OrganizationDataProvider dataProvider;

	private ResearchOrganizationService organizationService;

	private Column<ResearchOrganization> nameColumn;

	private Column<ResearchOrganization> typeColumn;

	private Column<ResearchOrganization> countryColumn;

	private Column<ResearchOrganization> validationColumn;

	private final DownloadableFileManager fileManager;

	/** Constructor.
	 *
	 * @param fileManager the manager of the filenames for the uploaded files.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param organizationService the service for accessing the organizations.
	 * @param logger the logger to use.
	 */
	public StandardOrganizationListView(
			DownloadableFileManager fileManager,
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			ResearchOrganizationService organizationService, Logger logger) {
		super(ResearchOrganization.class, authenticatedUser, messages, logger);
		this.fileManager = fileManager;
		this.organizationService = organizationService;
		this.dataProvider = (ps, query, filters) -> ps.getAllResearchOrganizations(query, filters);
	}

	@Override
	protected Filters<ResearchOrganization> createFilters() {
		return new OrganizationFilters(this::refreshGrid);
	}

	@Override
	protected boolean createGridColumns(Grid<ResearchOrganization> grid) {
		this.nameColumn = grid.addColumn(new ComponentRenderer<>(this::createNameComponent))
				.setAutoWidth(true)
				.setSortProperty("acronym", "name"); //$NON-NLS-1$ //$NON-NLS-2$
		this.typeColumn = grid.addColumn(organization -> organization.getType().getLabel(getMessageSourceAccessor(), getLocale()))
				.setAutoWidth(true)
				.setSortProperty("type"); //$NON-NLS-1$
		this.countryColumn = grid.addColumn(new ComponentRenderer<>(this::createCountryComponent))
				.setAutoWidth(true)
				.setSortProperty("country"); //$NON-NLS-1$

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
	
	private Component createCountryComponent(ResearchOrganization organization) {
		final var country = organization.getCountry();

		final var name = new Span(country.getDisplayCountry(getLocale()));
		name.getStyle().set("margin-left", "var(--lumo-space-s)"); //$NON-NLS-1$ //$NON-NLS-2$
		
		final var flag = new CountryFlag(country);
		flag.setSizeFromHeight(1, Unit.REM);
		
		final var layout = new HorizontalLayout(flag, name);
		layout.setSpacing(false);
		layout.setAlignItems(Alignment.CENTER);

		return layout;
	}

	private Component createNameComponent(ResearchOrganization organization) {
		final var acronym = organization.getAcronym();
		final var name = organization.getName();
		final var logo = organization.getPathToLogo();
		final var identifier = organization.getNationalIdentifier();
		final var rnsr = organization.getRnsr();

		final var details = new StringBuilder();
		if (!Strings.isNullOrEmpty(acronym)) {
			details.append(acronym);
		}
		if (!Strings.isNullOrEmpty(identifier)) {
			if (details.length() > 0) {
				details.append(' ');
			}
			details.append(identifier);
		}
		if (!Strings.isNullOrEmpty(rnsr)) {
			if (details.length() > 0) {
				details.append(" - "); //$NON-NLS-1$
			}
			details.append("RNSR ").append(rnsr); //$NON-NLS-1$
		}

		final var avatar = new AvatarItem();
		avatar.setHeading(name);
		if (details.length() > 0) {
			avatar.setDescription(details.toString());
		}
		if (organization.isMajorOrganization()) {
			avatar.setAvatarBorderColor(Integer.valueOf(3));
		}
		var logoFile = FileSystem.convertStringToFile(logo);
		if (logoFile != null) {
			logoFile = this.fileManager.normalizeForServerSide(logoFile);
			if (logoFile != null) {
				avatar.setAvatarResource(ComponentFactory.newStreamImage(logoFile));
			}
		}
		return avatar;
	}

	@Override
	protected Column<ResearchOrganization> getInitialSortingColumn() {
		return this.nameColumn;
	}

	@Override
	protected FetchCallback<ResearchOrganization, Void> getFetchCallback(Filters<ResearchOrganization> filters) {
		return query -> {
			return this.dataProvider.fetch(
					this.organizationService,
					VaadinSpringDataHelpers.toSpringPageRequest(query),
					filters).stream();
		};
	}

	@Override
	protected void deleteWithQuery(Set<ResearchOrganization> addresses) {
		if (!addresses.isEmpty()) {
			final var size = addresses.size();
			ComponentFactory.createDeletionDialog(this,
					getTranslation("views.organization.delete.title", Integer.valueOf(size)), //$NON-NLS-1$
					getTranslation("views.organization.delete.message", Integer.valueOf(size)), //$NON-NLS-1$
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
			for (final var adr : new ArrayList<>(grd.getSelectedItems())) {
				this.organizationService.removeResearchOrganization(adr.getId());
				final var msg = new StringBuilder("Organization: "); //$NON-NLS-1$
				msg.append(adr.getAcronymOrName());
				msg.append(" (id: "); //$NON-NLS-1$
				msg.append(adr.getId());
				msg.append(") has been deleted by "); //$NON-NLS-1$
				msg.append(userName);
				log.info(msg.toString());
				// Deselected the address
				grd.getSelectionModel().deselect(adr);
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
		notifyDeleted(size, "views.organizations.delete_success"); //$NON-NLS-1$
	}

	/** Notify the user that the addresses cannot be deleted.
	 */
	protected void notifyDeletionError(Throwable error) {
		notifyDeletionError(error, "views.organizations.delete_error"); //$NON-NLS-1$
	}

	@Override
	protected void addEntity() {
		openOrganizationEditor(new ResearchOrganization(), getTranslation("views.organizations.add_organization")); //$NON-NLS-1$
	}

	@Override
	protected void edit(ResearchOrganization organisation) {
		openOrganizationEditor(organisation, getTranslation("views.organizations.edit_organization", organisation.getAcronymOrName())); //$NON-NLS-1$
	}

	/** Show the editor of an address.
	 *
	 * @param address the address to edit.
	 * @param title the title of the editor.
	 */
	protected void openOrganizationEditor(ResearchOrganization organization, String title) {
		final var editor = new EmbeddedOrganizationEditor(
				this.organizationService.startEditing(organization),
				this.fileManager,
				getAuthenticatedUser(), getMessageSourceAccessor());
		ComponentFactory.openEditionModalDialog(title, editor, false,
				// Refresh the "old" item, even if its has been changed in the JPA database
				dialog -> refreshItem(organization));
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.nameColumn.setHeader(getTranslation("views.name")); //$NON-NLS-1$
		this.typeColumn.setHeader(getTranslation("views.type")); //$NON-NLS-1$
		this.countryColumn.setHeader(getTranslation("views.country")); //$NON-NLS-1$
		this.validationColumn.setHeader(getTranslation("views.validated")); //$NON-NLS-1$
		refreshGrid();
	}

	/** UI and JPA filters for {@link StandardOrganizationListView}.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class OrganizationFilters extends Filters<ResearchOrganization> {

		private static final long serialVersionUID = 5584210266375969212L;

		private Checkbox includeNames;

		private Checkbox includeTypes;

		private Checkbox includeCountries;

		/** Constructor.
		 *
		 * @param onSearch
		 */
		public OrganizationFilters(Runnable onSearch) {
			super(onSearch);
		}

		@Override
		protected Component getOptionsComponent() {
			this.includeNames = new Checkbox(true);
			this.includeTypes = new Checkbox(true);
			this.includeCountries = new Checkbox(true);

			final var options = new HorizontalLayout();
			options.setSpacing(false);
			options.add(this.includeNames);
			options.add(this.includeTypes);
			options.add(this.includeCountries);

			return options;
		}

		@Override
		protected void resetFilters() {
			this.includeNames.setValue(Boolean.TRUE);
			this.includeTypes.setValue(Boolean.TRUE);
			this.includeCountries.setValue(Boolean.TRUE);
		}

		@Override
		protected void buildQueryFor(String keywords, List<Predicate> predicates, Root<ResearchOrganization> root,
				CriteriaBuilder criteriaBuilder) {
			if (this.includeNames.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("acronym")), keywords)); //$NON-NLS-1$
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), keywords)); //$NON-NLS-1$
			}
			if (this.includeTypes.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("type")), keywords)); //$NON-NLS-1$
			}
			if (this.includeNames.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("country")), keywords)); //$NON-NLS-1$
			}
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			this.includeNames.setLabel(getTranslation("views.filters.include_names")); //$NON-NLS-1$
			this.includeTypes.setLabel(getTranslation("views.filters.include_types")); //$NON-NLS-1$
			this.includeCountries.setLabel(getTranslation("views.filters.include_countries")); //$NON-NLS-1$
		}

	}


	/** Provider of data for organizations to be displayed in the list of organizations view.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	@FunctionalInterface
	protected interface OrganizationDataProvider {

		/** Fetch organization data.
		 *
		 * @param organizationService the service to have access to the JPA.
		 * @param pageRequest the request for paging the data.
		 * @param filters the filters to apply for selecting the data.
		 * @return the lazy data page.
		 */
		Page<ResearchOrganization> fetch(ResearchOrganizationService organizationService, PageRequest pageRequest, Filters<ResearchOrganization> filters);

	}

}
