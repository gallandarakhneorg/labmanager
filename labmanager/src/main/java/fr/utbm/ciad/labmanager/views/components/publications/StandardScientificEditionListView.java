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

package fr.utbm.ciad.labmanager.views.components.publications;

import java.util.Arrays;
import java.util.stream.Stream;

import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.publication.PublicationType;
import fr.utbm.ciad.labmanager.services.conference.ConferenceService;
import fr.utbm.ciad.labmanager.services.journal.JournalService;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.services.scientificaxis.ScientificAxisService;
import fr.utbm.ciad.labmanager.services.user.UserService;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;

/** List all the scientific editions.
 * Edition:
	INTERNATIONAL_JOURNAL_EDITION
	NATIONAL_JOURNAL_EDITION
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class StandardScientificEditionListView extends AbstractPublicationListView {

	private static final long serialVersionUID = -6005063592673684168L;

	private static final PublicationType[] SUPPORTED_PUBLICATION_TYPES = {
			PublicationType.INTERNATIONAL_JOURNAL_EDITION,
			PublicationType.NATIONAL_JOURNAL_EDITION,
	};

	/** Constructor.
	 *
	 * @param fileManager the manager of the filenames for the uploaded files.
	 * @param personService the service for accessing the JPA entities for persons.
	 * @param userService the service for accessing the JPA entities for users.
	 * @param journalService the service for accessing the JPA entities for journal.
	 * @param conferenceService the service for accessing the JPA entities for conferences.
	 * @param axisService the service for accessing the JPA entities for scientific axes.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param publicationService the service for accessing the publications.
	 * @param logger the logger to use.
	 */
	public StandardScientificEditionListView(
			DownloadableFileManager fileManager,
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			PublicationService publicationService, PersonService personService,
			UserService userService, JournalService journalService,
			ConferenceService conferenceService,
			ScientificAxisService axisService, Logger logger) {
		super(fileManager, authenticatedUser, messages, publicationService, personService,
				userService, journalService, conferenceService, axisService, logger,
				"views.edition.delete.title", //$NON-NLS-1$
				"views.edition.delete.message", //$NON-NLS-1$
				"views.edition.delete.success_message", //$NON-NLS-1$
				"views.edition.delete.error_message", //$NON-NLS-1$
				"views.editors", //$NON-NLS-1$
				"views.publication.new_editor", //$NON-NLS-1$
				"views.publication.editors", //$NON-NLS-1$
				"views.publication.editors.helper", //$NON-NLS-1$
				"views.publication.editors.error.null", //$NON-NLS-1$
				"views.publication.editors.error.duplicate"); //$NON-NLS-1$
		setDataProvider((service, pageRequest, filters) -> {
			return publicationService.getAllPublications(pageRequest, createJpaFilters(filters),
					this::initializeEntityFromJPA);
		});
		initializeDataInGrid(getGrid(), getFilters());
	}
	
	@Override
	protected Stream<PublicationType> getSupportedPublicationTypes() {
		return Arrays.asList(SUPPORTED_PUBLICATION_TYPES).stream();
	}

}
