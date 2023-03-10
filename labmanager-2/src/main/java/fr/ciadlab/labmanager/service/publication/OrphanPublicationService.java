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

package fr.ciadlab.labmanager.service.publication;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.publication.ConferenceBasedPublication;
import fr.ciadlab.labmanager.entities.publication.JournalBasedPublication;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.repository.publication.PublicationRepository;
import fr.ciadlab.labmanager.service.AbstractOrphanService;
import org.arakhne.afc.progress.Progression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for managing the orphan publications.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Service
public class OrphanPublicationService extends AbstractOrphanService<Publication> {

	private static final String MESSAGE_PREFIX = "orphanPublicationService."; //$NON-NLS-1$

	private PublicationRepository publicationRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param publicationRepository the publication repository.
	 */
	public OrphanPublicationService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PublicationRepository publicationRepository) {
		super(messages, constants);
		this.publicationRepository = publicationRepository;
	}

	@Override
	public void computeOrphans(ArrayNode receiver, Progression progress) {
		computeOrphansInJson(receiver, this.publicationRepository, this,
				Constants.PUBLICATION_EDITING_ENDPOINT, Constants.PUBLICATION_ENDPOINT_PARAMETER,
				Constants.PUBLICATION_DELETING_ENDPOINT, Constants.PUBLICATION_ENDPOINT_PARAMETER,
				progress);
	}

	@Override
	public String getOrphanCriteria(Publication publication) {
		if (publication.getAuthors().isEmpty()) {
			return getMessage(MESSAGE_PREFIX + "EmptyAuthorList"); //$NON-NLS-1$
		}
		if (publication.getPublicationDate() == null && publication.getPublicationYear() == 0) {
			return getMessage(MESSAGE_PREFIX + "NoDate"); //$NON-NLS-1$
		}
		if (publication instanceof JournalBasedPublication) {
			final JournalBasedPublication journalPub = (JournalBasedPublication) publication;
			if (journalPub.getJournal() == null) {
				return getMessage(MESSAGE_PREFIX + "MissedJournal"); //$NON-NLS-1$
			}
		}
		if (publication instanceof ConferenceBasedPublication) {
			final ConferenceBasedPublication conferencePub = (ConferenceBasedPublication) publication;
			if (conferencePub.getConference() == null) {
				return getMessage(MESSAGE_PREFIX + "MissedConference"); //$NON-NLS-1$
			}
		}
		return null;
	}

	@Override
	public String getOrphanEntityLabel(Publication entity) {
		return entity.getTitle() + ". " + entity.getWherePublishedShortDescription(); //$NON-NLS-1$
	}

	@Override
	public String getOrphanTypeLabel() {
		return getMessage(MESSAGE_PREFIX + "Name"); //$NON-NLS-1$
	}

}
