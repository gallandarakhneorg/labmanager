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

package fr.utbm.ciad.labmanager.views.components.conferences;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider.FetchCallback;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.conference.Conference;
import fr.utbm.ciad.labmanager.services.conference.ConferenceService;
import fr.utbm.ciad.labmanager.views.components.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.badges.BadgeRenderer;
import fr.utbm.ciad.labmanager.views.components.badges.BadgeState;
import fr.utbm.ciad.labmanager.views.components.entities.AbstractEntityListView;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/** List all the conferences.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class StandardConferenceListView extends AbstractEntityListView<Conference> {

	private static final long serialVersionUID = 7576767630790475598L;

	private final ConferenceDataProvider dataProvider;

	private ConferenceService conferenceService;

	private Column<Conference> nameColumn;

	private Column<Conference> publisherColumn;

	private Column<Conference> coreRankingColumn;

	private Column<Conference> paperCountColumn;

	private Column<Conference> validationColumn;

	/** Constructor.
	 *
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param conferenceService the service for accessing the sonferences.
	 * @param logger the logger to use.
	 */
	public StandardConferenceListView(
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			ConferenceService conferenceService, Logger logger) {
		super(Conference.class, authenticatedUser, messages, logger);
		this.conferenceService = conferenceService;
		this.dataProvider = (ps, query, filters) -> ps.getAllConferences(query, filters);
	}

	@Override
	protected Filters<Conference> createFilters() {
		return new ConferenceFilters(this::refreshGrid);
	}

	private String getConferenceName(Conference conference) {
		return new StringBuilder().append(conference.getAcronym()).append(" - ").append(conference.getName()).toString(); //$NON-NLS-1$
	}

	@Override
	protected boolean createGridColumns(Grid<Conference> grid) {
		final var currentYear = LocalDate.now().getYear();
		this.nameColumn = grid.addColumn(this::getConferenceName)
				.setAutoWidth(true)
				.setFrozen(true)
				.setSortProperty("name"); //$NON-NLS-1$
		this.publisherColumn = grid.addColumn(conference -> conference.getPublisher())
				.setAutoWidth(true)
				.setSortProperty("publisher"); //$NON-NLS-1$
		this.coreRankingColumn = grid.addColumn(conference -> getCoreRank(conference, currentYear))
				.setAutoWidth(true)
				.setSortProperty("coreRanking"); //$NON-NLS-1$
		this.paperCountColumn = grid.addColumn(conference -> getPaperCount(conference, currentYear))
				.setAutoWidth(true)
				.setSortProperty("paperCount"); //$NON-NLS-1$
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

	private static String getCoreRank(Conference conference, int year) {
//		final QuartileRanking rank = QuartileRanking.normalize(journal.getScimagoQIndexByYear(year));
//		if (rank == QuartileRanking.NR) {
//			return ""; //$NON-NLS-1$
//		}
//		return rank.name();
		return "";
	}

	private static Integer getPaperCount(Conference conference, int year) {
		return Integer.valueOf(0);
	}

	@Override
	protected Column<Conference> getInitialSortingColumn() {
		return this.nameColumn;
	}

	@Override
	protected FetchCallback<Conference, Void> getFetchCallback(Filters<Conference> filters) {
		return query -> {
			return this.dataProvider.fetch(
					this.conferenceService,
					VaadinSpringDataHelpers.toSpringPageRequest(query),
					filters).stream();
		};
	}

	@Override
	protected void deleteWithQuery(Set<Conference> conferences) {
		if (!conferences.isEmpty()) {
			final int size = conferences.size();
			ComponentFactory.createDeletionDialog(this,
					getTranslation("views.conferences.delete.title", Integer.valueOf(size)), //$NON-NLS-1$
					getTranslation("views.conferences.delete.message", Integer.valueOf(size)), //$NON-NLS-1$
					it ->  deleteCurrentSelection())
			.open();
		}
	}

	@Override
	protected void deleteCurrentSelection() {
		try {
			// Get the selection again because this handler is run in another session than the one of the function
			int realSize = 0;
			final var grd = getGrid();
			final var log = getLogger();
			final var userName = AuthenticatedUser.getUserName(getAuthenticatedUser());
			for (final var conference : new ArrayList<>(grd.getSelectedItems())) {
				this.conferenceService.removeConference(conference.getId());
				final StringBuilder msg = new StringBuilder("Conference: "); //$NON-NLS-1$
				msg.append(conference.getName());
				msg.append(" (id: "); //$NON-NLS-1$
				msg.append(conference.getId());
				msg.append(") has been deleted by "); //$NON-NLS-1$
				msg.append(userName);
				log.info(msg.toString());
				// Deselected the conference
				grd.getSelectionModel().deselect(conference);
				++realSize;
			}
			refreshGrid();
			notifyDeleted(realSize);
		} catch (Throwable ex) {
			refreshGrid();
			notifyDeletionError(ex);
		}
	}

	/** Notify the user that the conferences were deleted.
	 *
	 * @param size the number of deleted conferences
	 */
	protected void notifyDeleted(int size) {
		notifyDeleted(size, "views.conferences.delete_success"); //$NON-NLS-1$
	}

	/** Notify the user that the conferences cannot be deleted.
	 */
	protected void notifyDeletionError(Throwable error) {
		notifyDeletionError(error, "views.conferences.delete_error"); //$NON-NLS-1$
	}

	@Override
	protected void addEntity() {
		openConferenceEditor(new Conference(), getTranslation("views.conferences.add_conference")); //$NON-NLS-1$
	}

	@Override
	protected void edit(Conference conference) {
		openConferenceEditor(conference, getTranslation("views.conferences.edit_conference", conference.getName())); //$NON-NLS-1$
	}

	/** Show the editor of a conference.
	 *
	 * @param journal the conference to edit.
	 * @param title the title of the editor.
	 */
	protected void openConferenceEditor(Conference conference, String title) {
		final var editor = new EmbeddedConferenceEditor(
				this.conferenceService.startEditing(conference),
				getAuthenticatedUser(), getMessageSourceAccessor());
		ComponentFactory.openEditionModalDialog(title, editor, false,
				// Refresh the "old" item, even if its has been changed in the JPA database
				dialog -> refreshItem(conference));
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.nameColumn.setHeader(getTranslation("views.name")); //$NON-NLS-1$
		this.publisherColumn.setHeader(getTranslation("views.publisher")); //$NON-NLS-1$
		this.coreRankingColumn.setHeader(getTranslation("views.conferences.coreRanking")); //$NON-NLS-1$
		this.paperCountColumn.setHeader(getTranslation("views.conferences.paperCount")); //$NON-NLS-1$
		this.validationColumn.setHeader(getTranslation("views.validated")); //$NON-NLS-1$
	}

	/** UI and JPA filters for {@link StandardConferenceListView}.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class ConferenceFilters extends Filters<Conference> {

		private static final long serialVersionUID = -5029775320994118621L;

		private Checkbox includeNames;

		private Checkbox includePublishers;

		private Checkbox includeRankings;

		/** Constructor.
		 *
		 * @param onSearch
		 */
		public ConferenceFilters(Runnable onSearch) {
			super(onSearch);
		}

		@Override
		protected Component getOptionsComponent() {
			this.includeNames = new Checkbox(true);
			this.includePublishers = new Checkbox(true);
			this.includeRankings = new Checkbox(true);

			final var options = new HorizontalLayout();
			options.setSpacing(false);
			options.add(this.includeNames, this.includePublishers, this.includeRankings);

			return options;
		}

		@Override
		protected void resetFilters() {
			this.includeNames.setValue(Boolean.TRUE);
			this.includePublishers.setValue(Boolean.TRUE);
			this.includeRankings.setValue(Boolean.TRUE);
		}

		@Override
		protected void buildQueryFor(String keywords, List<Predicate> predicates, Root<Conference> root,
				CriteriaBuilder criteriaBuilder) {
			if (this.includeNames.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("journalName")), keywords)); //$NON-NLS-1$
			}
			if (this.includePublishers.getValue() == Boolean.TRUE) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("publisher")), keywords)); //$NON-NLS-1$
			}
			if (this.includeRankings.getValue() == Boolean.TRUE) {
//				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("scimagoRank")), keywords)); //$NON-NLS-1$
//				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("wosRank")), keywords)); //$NON-NLS-1$
			}
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			this.includeNames.setLabel(getTranslation("views.filters.include_names")); //$NON-NLS-1$
			this.includePublishers.setLabel(getTranslation("views.filters.include_publishers")); //$NON-NLS-1$
			this.includeRankings.setLabel(getTranslation("views.filters.include_rankings")); //$NON-NLS-1$
		}

	}


	/** Provider of data for conferences to be displayed in the list of conferences view.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	@FunctionalInterface
	protected interface ConferenceDataProvider {

		/** Fetch conference data.
		 *
		 * @param conferenceService the service to have access to the JPA.
		 * @param pageRequest the request for paging the data.
		 * @param filters the filters to apply for selecting the data.
		 * @return the lazy data page.
		 */
		Page<Conference> fetch(ConferenceService conferenceService, PageRequest pageRequest, Filters<Conference> filters);

	}

}
