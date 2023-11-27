/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
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

package fr.ciadlab.labmanager.indicators.publication.count;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import fr.ciadlab.labmanager.indicators.AbstractAnnualIndicator;
import fr.ciadlab.labmanager.service.publication.type.JournalPaperService;
import fr.ciadlab.labmanager.utils.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/** Count the number of unranked journal papers for an organization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Component
public class UnrankedJournalPaperCountIndicator extends AbstractAnnualIndicator {

	private JournalPaperService journalPaperService;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param journalPaperService the service for accessing the journal papers.
	 */
	public UnrankedJournalPaperCountIndicator(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired JournalPaperService journalPaperService) {
		super(messages, constants, AbstractAnnualIndicator::sum);
		this.journalPaperService = journalPaperService;
	}

	@Override
	public Map<Integer, Number> getValuesPerYear(ResearchOrganization organization, int startYear, int endYear) {
		final Set<JournalPaper> papers = this.journalPaperService.getJournalPapersByOrganizationId(organization.getId(), true, true);
		//
		final Map<Integer, Number> rankedPapers = filterByYearWindow(papers, it -> Integer.valueOf(it.getPublicationYear()))
				.filter(it -> !it.isRanked())
				.collect(Collectors.toConcurrentMap(
					it -> Integer.valueOf(it.getPublicationYear()),
					it -> Integer.valueOf(1),
					(a, b) -> Integer.valueOf(a.intValue() + b.intValue())));
		//
		setComputationDetails(rankedPapers);
		return rankedPapers;
	}

	@Override
	public String getName() {
		return getMessage("unrankedJournalPaperCountIndicator.name"); //$NON-NLS-1$
	}

	@Override
	public String getLabel(Unit unit) {
		return getMessage("unrankedJournalPaperCountIndicator.label"); //$NON-NLS-1$
	}

}
