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

package fr.utbm.ciad.labmanager.services.conference;

import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.conference.Conference;
import fr.utbm.ciad.labmanager.data.conference.ConferenceRepository;
import fr.utbm.ciad.labmanager.data.publication.ConferenceBasedPublication;
import fr.utbm.ciad.labmanager.services.AbstractOrphanService;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import org.arakhne.afc.progress.Progression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service related to the orphan conferences.
 * 
 * @author $Author: sgalland$
 * @author $Author: anoubli$
 * @author $Author: bpdj$
 * @author $Author: pgoubet$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Service
public class OrphanConferenceService extends AbstractOrphanService<Conference> {

	private static final String MESSAGE_PREFIX = "orphanConferenceService."; //$NON-NLS-1$

	private final ConferenceRepository conferenceRepository;

	private final PublicationService publicationService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param conferenceRepository the journal repository.
	 * @param publicationService the publication service.
	 */
	public OrphanConferenceService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ConferenceRepository conferenceRepository,
			@Autowired PublicationService publicationService) {
		super(messages, constants);
		this.conferenceRepository = conferenceRepository;
		this.publicationService = publicationService;
	}

	@Override
	public void computeOrphans(ArrayNode receiver, Progression progress) {
		computeOrphansInJson(receiver, this.conferenceRepository, this,
				Constants.CONFERENCE_EDITING_ENDPOINT, Constants.CONFERENCE_ENDPOINT_PARAMETER,
				Constants.CONFERENCE_DELETING_ENDPOINT, Constants.CONFERENCE_ENDPOINT_PARAMETER,
				progress);
	}

	@Override
	public String getOrphanCriteria(Conference conference) {
		final List<ConferenceBasedPublication> pubs = this.publicationService.getPublicationsForConference(conference.getId());
		if (pubs.isEmpty()) {
			return getMessage(MESSAGE_PREFIX + "ConferenceWithoutPublication"); //$NON-NLS-1$
		}
		return null;
	}

	@Override
	public String getOrphanEntityLabel(Conference entity) {
		return entity.getAcronym() + " - " + entity.getName(); //$NON-NLS-1$
	}

	@Override
	public String getOrphanTypeLabel() {
		return getMessage(MESSAGE_PREFIX + "Name"); //$NON-NLS-1$
	}

}
