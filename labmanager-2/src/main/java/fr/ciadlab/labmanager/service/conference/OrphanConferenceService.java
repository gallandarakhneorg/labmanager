/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the CIAD laboratory and the Université de Technologie
 * de Belfort-Montbéliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD-UTBM.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.service.conference;

import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.conference.Conference;
import fr.ciadlab.labmanager.entities.publication.ConferenceBasedPublication;
import fr.ciadlab.labmanager.repository.conference.ConferenceRepository;
import fr.ciadlab.labmanager.service.AbstractOrphanService;
import fr.ciadlab.labmanager.service.publication.PublicationService;
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
