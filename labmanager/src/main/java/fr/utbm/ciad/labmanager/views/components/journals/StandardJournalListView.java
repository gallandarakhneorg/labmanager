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

package fr.utbm.ciad.labmanager.views.components.journals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider.FetchCallback;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.journal.Journal;
import fr.utbm.ciad.labmanager.services.journal.JournalService;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.badges.BadgeRenderer;
import fr.utbm.ciad.labmanager.views.components.addons.badges.BadgeState;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityListView;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/** List all the journals.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class StandardJournalListView extends AbstractEntityListView<Journal> {

	private static final long serialVersionUID = -284476504810879812L;

	private final JournalDataProvider dataProvider;

	private JournalService journalService;

	private Column<Journal> nameColumn;

	private Column<Journal> publisherColumn;

	private Column<Journal> scimagoRankingColumn;

	private Column<Journal> wosRankingColumn;

	private Column<Journal> paperCountColumn;

	private Column<Journal> validationColumn;

	/** Constructor.
	 *
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param journalService the service for accessing the journals.
	 * @param logger the logger to use.
	 */
	public StandardJournalListView(
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			JournalService journalService, Logger logger) {
		super(Journal.class, authenticatedUser, messages, logger);
		this.journalService = journalService;
		this.dataProvider = (ps, query, filters) -> ps.getAllJournals(query, filters);
		initializeDataInGrid(getGrid(), getFilters());
	}

	@Override
	protected Filters<Journal> createFilters() {
		return new JournalFilters(this::refreshGrid);
	}

	@Override
	protected boolean createGridColumns(Grid<Journal> grid) {
		final var currentYear = LocalDate.now().getYear();
		this.nameColumn = grid.addColumn(journal -> journal.getJournalName())
				.setAutoWidth(true)
				.setFrozen(true)
				.setSortProperty("journalName"); //$NON-NLS-1$
		this.publisherColumn = grid.addColumn(journal -> journal.getPublisher())
				.setAutoWidth(true)
				.setSortProperty("publisher"); //$NON-NLS-1$
		this.scimagoRankingColumn = grid.addColumn(journal -> getScimagoQuartile(journal, currentYear))
				.setAutoWidth(true)
				.setSortProperty("scimagoRanking"); //$NON-NLS-1$
		this.wosRankingColumn = grid.addColumn(journal -> getWosQuartile(journal, currentYear))
				.setAutoWidth(true)
				.setSortProperty("wosRanking"); //$NON-NLS-1$
		this.paperCountColumn = grid.addColumn(journal -> getPaperCount(journal, currentYear))
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

	private static String getScimagoQuartile(Journal journal, int year) {
//		final QuartileRanking rank = QuartileRanking.normalize(journal.getScimagoQIndexByYear(year));
//		if (rank == QuartileRanking.NR) {
//			return ""; //$NON-NLS-1$
//		}
//		return rank.name();
		return "";
	}

	private static String getWosQuartile(Journal journal, int year) {
//		final QuartileRanking rank = QuartileRanking.normalize(journal.getWosQIndexByYear(year));
//		if (rank == QuartileRanking.NR) {
//			return ""; //$NON-NLS-1$
//		}
//		return rank.name();
		return "";
	}

	private static Integer getPaperCount(Journal journal, int year) {
		return Integer.valueOf(0);
	}

	@Override
	protected Column<Journal> getInitialSortingColumn() {
		return this.nameColumn;
	}

	@Override
	protected FetchCallback<Journal, Void> getFetchCallback(Filters<Journal> filters) {
		return query -> {
			return this.dataProvider.fetch(
					this.journalService,
					VaadinSpringDataHelpers.toSpringPageRequest(query),
					filters).stream();
		};
	}

	@Override
	protected void deleteWithQuery(Set<Journal> journals) {
		if (!journals.isEmpty()) {
			final var size = journals.size();
			ComponentFactory.createDeletionDialog(this,
					getTranslation("views.journals.delete.title", Integer.valueOf(size)), //$NON-NLS-1$
					getTranslation("views.journals.delete.message", Integer.valueOf(size)), //$NON-NLS-1$
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
			for (final var journal : new ArrayList<>(grd.getSelectedItems())) {
				this.journalService.removeJournal(journal.getId());
				final var msg = new StringBuilder("Journal: "); //$NON-NLS-1$
				msg.append(journal.getJournalName());
				msg.append(" (id: "); //$NON-NLS-1$
				msg.append(journal.getId());
				msg.append(") has been deleted by "); //$NON-NLS-1$
				msg.append(userName);
				log.info(msg.toString());
				// Deselected the address
				grd.getSelectionModel().deselect(journal);
				++realSize;
			}
			refreshGrid();
			notifyDeleted(realSize);
		} catch (Throwable ex) {
			refreshGrid();
			notifyDeletionError(ex);
		}
	}

	/** Notify the user that the journals were deleted.
	 *
	 * @param size the number of deleted journals
	 */
	protected void notifyDeleted(int size) {
		notifyDeleted(size, "views.journals.delete_success"); //$NON-NLS-1$
	}

	/** Notify the user that the journals cannot be deleted.
	 */
	protected void notifyDeletionError(Throwable error) {
		notifyDeletionError(error, "views.journals.delete_error"); //$NON-NLS-1$
	}

	@Override
	protected void addEntity() {
		openJournalEditor(new Journal(), getTranslation("views.journals.add_journal")); //$NON-NLS-1$
	}

	@Override
	protected void edit(Journal journal) {
		openJournalEditor(journal, getTranslation("views.journals.edit_journal", journal.getJournalName())); //$NON-NLS-1$
	}

	/** Show the editor of an journal.
	 *
	 * @param journal the journal to edit.
	 * @param title the title of the editor.
	 */
	protected void openJournalEditor(Journal journal, String title) {
		final var editor = new EmbeddedJournalEditor(
				this.journalService.startEditing(journal),
				getAuthenticatedUser(), getMessageSourceAccessor());
		ComponentFactory.openEditionModalDialog(title, editor, false,
				// Refresh the "old" item, even if its has been changed in the JPA database
				dialog -> refreshItem(journal));
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.nameColumn.setHeader(getTranslation("views.name")); //$NON-NLS-1$
		this.publisherColumn.setHeader(getTranslation("views.publisher")); //$NON-NLS-1$
		this.scimagoRankingColumn.setHeader(getTranslation("views.journals.scimagoRanking")); //$NON-NLS-1$
		this.wosRankingColumn.setHeader(getTranslation("views.journals.wosRanking")); //$NON-NLS-1$
		this.paperCountColumn.setHeader(getTranslation("views.journals.paperCount")); //$NON-NLS-1$
		this.validationColumn.setHeader(getTranslation("views.validated")); //$NON-NLS-1$
	}

	/** UI and JPA filters for {@link StandardJournalListView}.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class JournalFilters extends Filters<Journal> {

		private static final long serialVersionUID = -2926094704107561306L;

		private Checkbox includeNames;

		private Checkbox includePublishers;

		private Checkbox includeRankings;

		/** Constructor.
		 *
		 * @param onSearch
		 */
		public JournalFilters(Runnable onSearch) {
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
		protected void buildQueryFor(String keywords, List<Predicate> predicates, Root<Journal> root,
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


	/** Provider of data for journals to be displayed in the list of journals view.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	@FunctionalInterface
	protected interface JournalDataProvider {

		/** Fetch address data.
		 *
		 * @param journalService the service to have access to the JPA.
		 * @param pageRequest the request for paging the data.
		 * @param filters the filters to apply for selecting the data.
		 * @return the lazy data page.
		 */
		Page<Journal> fetch(JournalService journalService, PageRequest pageRequest, Filters<Journal> filters);

	}

}
