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
import fr.ciadlab.labmanager.utils.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/** Count the number of journal papers for an organization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
@Component
public class RankedJournalPaperCountIndicator extends AbstractIndicator {

	private JournalPaperService journalPaperService;

	private int referenceDuration = 5;
	
	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param journalPaperService the service for accessing the journal papers.
	 */
	public RankedJournalPaperCountIndicator(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired JournalPaperService journalPaperService) {
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

	@Override
	public String getName() {
		return getMessage("rankedJournalPaperCountIndicator.name"); //$NON-NLS-1$
	}

	@Override
	public String getLabel(Unit unit) {
		return getLabelWithYears("rankedJournalPaperCountIndicator.label"); //$NON-NLS-1$
	}

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
		final Stream<JournalPaper> stream = filterByTimeWindow(papers, it -> it.getPublicationDate());
		return Long.valueOf(stream.count());
	}

}
