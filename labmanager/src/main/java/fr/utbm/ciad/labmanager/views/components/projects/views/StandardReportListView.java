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

package fr.utbm.ciad.labmanager.views.components.projects.views;

import java.util.Arrays;
import java.util.stream.Stream;

import fr.utbm.ciad.labmanager.data.publication.PublicationType;
import fr.utbm.ciad.labmanager.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.services.conference.ConferenceService;
import fr.utbm.ciad.labmanager.services.journal.JournalService;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.services.scientificaxis.ScientificAxisService;
import fr.utbm.ciad.labmanager.services.user.UserService;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.views.components.conferences.editors.ConferenceEditorFactory;
import fr.utbm.ciad.labmanager.views.components.journals.editors.JournalEditorFactory;
import fr.utbm.ciad.labmanager.views.components.persons.editors.PersonEditorFactory;
import fr.utbm.ciad.labmanager.views.components.publications.editors.PublicationEditorFactory;
import fr.utbm.ciad.labmanager.views.components.publications.views.AbstractPublicationListView;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;

/** List all the reports and expertises.
 * Reports and expertises:
	PROJECT_REPORT
	RESEARCH_TRANSFERT_REPORT
	TECHNICAL_REPORT
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class StandardReportListView extends AbstractPublicationListView {

	private static final long serialVersionUID = 2845937407065908352L;
	
	private static final PublicationType[] SUPPORTED_PUBLICATION_TYPES = {
			PublicationType.PROJECT_REPORT,
			PublicationType.RESEARCH_TRANSFERT_REPORT,
			PublicationType.TECHNICAL_REPORT
	};

	/** Constructor.
	 *
	 * @param fileManager the manager of the filenames for the uploaded files.
	 * @param personService the service for accessing the JPA entities for persons.
	 * @param personEditorFactory the factory for creating the person editors.
	 * @param userService the service for accessing the JPA entities for users.
	 * @param journalService the service for accessing the JPA entities for journal.
	 * @param journalEditorFactory the factory for creating the journal editors.
	 * @param conferenceService the service for accessing the JPA entities for conferences.
	 * @param conferenceEditorFactory the factory for creating the conference editors.
	 * @param axisService the service for accessing the JPA entities for scientific axes.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param publicationService the service for accessing the publications.
     * @param publicationEditorFactory the factory for creating publication editors.
	 * @param logger the logger to use.
	 */
	public StandardReportListView(
			DownloadableFileManager fileManager,
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			PublicationService publicationService, PublicationEditorFactory publicationEditorFactory, PersonService personService,
			PersonEditorFactory personEditorFactory, UserService userService, JournalService journalService,
			JournalEditorFactory journalEditorFactory, ConferenceService conferenceService,
			ConferenceEditorFactory conferenceEditorFactory, ScientificAxisService axisService, Logger logger) {
		super(fileManager, authenticatedUser, messages, publicationService, publicationEditorFactory, personService, personEditorFactory,
				userService, journalService, journalEditorFactory, conferenceService,
				conferenceEditorFactory, axisService, logger,
				"views.reports.delete.title", //$NON-NLS-1$
				"views.reports.delete.message", //$NON-NLS-1$
				"views.reports.delete.success_message", //$NON-NLS-1$
				"views.reports.delete.error_message", //$NON-NLS-1$
				"views.authors", //$NON-NLS-1$
				"views.publication.new_author", //$NON-NLS-1$
				"views.publication.authors", //$NON-NLS-1$
				"views.publication.authors.helper", //$NON-NLS-1$
				"views.publication.authors.error.null", //$NON-NLS-1$
				"views.publication.authors.error.duplicate"); //$NON-NLS-1$
		setDataProvider((service, pageRequest, filters) -> {
			return publicationService.getAllPublications(pageRequest, createJpaFilters(filters),
					this::initializeEntityFromJPA);
		});
		postInitializeFilters();
		initializeDataInGrid(getGrid(), getFilters());
	}
	
	@Override
	protected Stream<PublicationType> getSupportedPublicationTypes() {
		return Arrays.asList(SUPPORTED_PUBLICATION_TYPES).stream();
	}

}
