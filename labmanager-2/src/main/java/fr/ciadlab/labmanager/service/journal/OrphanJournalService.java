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

package fr.ciadlab.labmanager.service.journal;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.repository.journal.JournalRepository;
import fr.ciadlab.labmanager.repository.publication.type.JournalPaperRepository;
import fr.ciadlab.labmanager.service.AbstractOrphanService;
import org.arakhne.afc.progress.Progression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service related to the orphan journals.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Service
public class OrphanJournalService extends AbstractOrphanService<Journal> {

	private static final String MESSAGE_PREFIX = "orphanJournalService."; //$NON-NLS-1$

	private final JournalRepository journalRepository;

	private final JournalPaperRepository publicationRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param journalRepository the journal repository.
	 * @param publicationRepository the publication repository.
	 */
	public OrphanJournalService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired JournalRepository journalRepository,
			@Autowired JournalPaperRepository publicationRepository) {
		super(messages, constants);
		this.journalRepository = journalRepository;
		this.publicationRepository = publicationRepository;
	}

	@Override
	public void computeOrphans(ArrayNode receiver, Progression progress) {
		computeOrphansInJson(receiver, this.journalRepository, this,
				Constants.JOURNAL_EDITING_ENDPOINT, Constants.JOURNAL_ENDPOINT_PARAMETER,
				Constants.JOURNAL_DELETING_ENDPOINT, Constants.JOURNAL_ENDPOINT_PARAMETER,
				progress);
	}

	@Override
	public String getOrphanCriteria(Journal journal) {
		if (this.publicationRepository.findAll().stream()
				.noneMatch(it -> it.getJournal() != null && it.getJournal().getId() == journal.getId())) {
			return getMessage(MESSAGE_PREFIX + "NoPublication"); //$NON-NLS-1$
		}
		return null;
	}

	@Override
	public String getOrphanEntityLabel(Journal entity) {
		return entity.getJournalName() + " - " + entity.getPublisher(); //$NON-NLS-1$
	}

	@Override
	public String getOrphanTypeLabel() {
		return getMessage(MESSAGE_PREFIX + "Name"); //$NON-NLS-1$
	}

}
