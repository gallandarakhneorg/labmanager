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

package fr.ciadlab.labmanager.indicators.publication.fte;

import java.util.Map;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.indicators.AbstractAnnualIndicator;
import fr.ciadlab.labmanager.indicators.members.fte.PhdStudentFteIndicator;
import fr.ciadlab.labmanager.indicators.publication.count.PhdConferencePaperCountIndicator;
import fr.ciadlab.labmanager.utils.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/** Count the number of conference papers per PhD student per year.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Component
public class ConferencePaperPhdRatioIndicator extends AbstractAnnualIndicator {

	private PhdConferencePaperCountIndicator paperCount;

	private PhdStudentFteIndicator fteIndicator;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param fteIndicator the indicator that counts the PhD students.
	 * @param paperCount the indicator for counting the papers.
	 */
	public ConferencePaperPhdRatioIndicator(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PhdStudentFteIndicator fteIndicator,
			@Autowired PhdConferencePaperCountIndicator paperCount) {
		super(messages, constants, AbstractAnnualIndicator::average);
		this.fteIndicator = fteIndicator;
		this.paperCount = paperCount;
	}

	@Override
	public Map<Integer, Number> getValuesPerYear(ResearchOrganization organization, int startYear, int endYear) {
		final Map<Integer, Number> rankedPapers = this.paperCount.getValuesPerYear(organization, startYear, endYear);
		final Map<Integer, Number> ftes = this.fteIndicator.getValuesPerYear(organization, startYear, endYear);
		final Map<Integer, Number> ratios = rankedPapers.entrySet().parallelStream().collect(Collectors.toConcurrentMap(
			Map.Entry::getKey,
			it -> {
				final Number count = it.getValue();
				final Number fte = ftes.get(it.getKey());
				if (fte != null && count != null) {
					return Float.valueOf(count.floatValue() / fte.floatValue());
				}
				return Float.valueOf(0f);
			}));
		//
		setComputationDetails(ratios);
		return ratios;
	}

	@Override
	public String getName() {
		return getMessage("conferencePaperPhdRatioIndicator.name"); //$NON-NLS-1$
	}

	@Override
	public String getLabel(Unit unit) {
		return getMessage("conferencePaperPhdRatioIndicator.label"); //$NON-NLS-1$
	}

}
