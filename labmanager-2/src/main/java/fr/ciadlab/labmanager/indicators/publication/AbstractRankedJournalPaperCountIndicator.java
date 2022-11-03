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

package fr.ciadlab.labmanager.indicators.publication;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import fr.ciadlab.labmanager.indicators.AbstractIndicator;
import fr.ciadlab.labmanager.service.publication.type.JournalPaperService;
import fr.ciadlab.labmanager.utils.ranking.JournalRankingSystem;
import org.springframework.context.support.MessageSourceAccessor;

/** Count the number of journal papers for an organization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
public abstract class AbstractRankedJournalPaperCountIndicator extends AbstractIndicator {

	private JournalPaperService journalPaperService;

	private int referenceDuration = 5;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param journalPaperService the service for accessing the journal papers.
	 */
	public AbstractRankedJournalPaperCountIndicator(
			MessageSourceAccessor messages,
			Constants constants,
			JournalPaperService journalPaperService) {
		super(messages, constants);
		this.journalPaperService = journalPaperService;
	}

	/** Replies the number of years for the reference period.
	 *
	 * @return the number of year for the reference period. 
	 */
	public int getReferencePeriodDuration() {
		return this.referenceDuration;
	}

	/** Change the number of years for the reference period.
	 *
	 * @param years the number of year for the reference period. 
	 */
	public void setReferencePeriodDuration(int years) {
		if (years > 1) {
			this.referenceDuration = years;
		} else {
			this.referenceDuration = 1;
		}
	}

	/** Replies the journal ranking system to be used.
	 *
	 * @return the journal ranking system to be used. 
	 */
	public abstract JournalRankingSystem getJournalRankingSystem();

	@Override
	public LocalDate getReferencePeriodStart() {
		return computeStartDate(getReferencePeriodDuration());
	}

	@Override
	public LocalDate getReferencePeriodEnd() {
		return computeEndDate(0);
	}

	@Override
	protected Number computeValue(ResearchOrganization organization) {
		final Set<JournalPaper> papers = this.journalPaperService.getJournalPapersByOrganizationId(organization.getId(), true);
		Stream<JournalPaper> stream = filterByTimeWindow(papers, it -> it.getPublicationDate());
		switch (getJournalRankingSystem()) {
		case SCIMAGO:
			stream = stream.filter(it -> it.getScimagoQIndex() != null);
			break;
		case WOS:
			stream = stream.filter(it -> it.getWosQIndex() != null);
			break;
		default:
			stream = stream.filter(it -> it.isRanked());
			break;
		}
		return Long.valueOf(stream.count());
	}

}
