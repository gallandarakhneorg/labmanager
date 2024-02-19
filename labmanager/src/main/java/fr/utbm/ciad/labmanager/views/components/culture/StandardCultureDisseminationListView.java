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

package fr.utbm.ciad.labmanager.views.components.culture;

import java.util.Arrays;
import java.util.stream.Stream;

import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.publication.PublicationType;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractPublicationListView;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;

/** List all the scientific culture dissemination.
 * Culture dissemination:
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class StandardCultureDisseminationListView extends AbstractPublicationListView {

	private static final long serialVersionUID = 5540919427209464921L;

	private static final PublicationType[] SUPPORTED_PUBLICATION_TYPES = {
			PublicationType.ARTISTIC_PRODUCTION,
			PublicationType.INTERNATIONAL_SCIENTIFIC_CULTURE_PRESENTATION,
			PublicationType.NATIONAL_SCIENTIFIC_CULTURE_PRESENTATION,
			PublicationType.SCIENTIFIC_CULTURE_BOOK,
			PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER,
			PublicationType.SCIENTIFIC_CULTURE_PAPER,
			PublicationType.OTHER
	};

	/** Constructor.
	 *
	 * @param fileManager the manager of the filenames for the uploaded files.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer).
	 * @param publicationService the service for accessing the publications.
	 * @param logger the logger to use.
	 */
	public StandardCultureDisseminationListView(
			DownloadableFileManager fileManager,
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages,
			PublicationService publicationService, Logger logger) {
		super(fileManager, authenticatedUser, messages, publicationService, logger,
				"views.scientific_culture_actions.delete.title", //$NON-NLS-1$
				"views.scientific_culture_actions.delete.message", //$NON-NLS-1$
				"views.scientific_culture_actions.delete.success_message", //$NON-NLS-1$
				"views.scientific_culture_actions.delete.error_message", //$NON-NLS-1$
				"views.authors"); //$NON-NLS-1$
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
