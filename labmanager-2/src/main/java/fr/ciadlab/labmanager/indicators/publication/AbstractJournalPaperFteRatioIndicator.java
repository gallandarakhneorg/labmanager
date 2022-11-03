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

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.indicators.AbstractIndicator;
import fr.ciadlab.labmanager.indicators.members.PermanentResearchFteIndicator;
import fr.ciadlab.labmanager.service.publication.type.JournalPaperService;
import org.springframework.context.support.MessageSourceAccessor;

/** Calculate the number of ranked papers per full-time equivalent (FTE). 
 * 
 * @param <T> the type of journal paper counter.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
public abstract class AbstractJournalPaperFteRatioIndicator<T extends AbstractRankedJournalPaperCountIndicator> extends AbstractIndicator {

	private int referenceDuration = 5;

	private final PermanentResearchFteIndicator fteIndicator;

	/** Counter of journal papers.
	 */
	protected final T countIndicator;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param journalPaperService the service for accessing the journal papers.
	 * @param countIndicator the counter of ranked journals.
	 */
	public AbstractJournalPaperFteRatioIndicator(
			MessageSourceAccessor messages,
			Constants constants,
			JournalPaperService journalPaperService,
			T countIndicator) {
		super(messages, constants);
		this.fteIndicator = new PermanentResearchFteIndicator(messages, constants);
		this.fteIndicator.setReferencePeriodDuration(this.referenceDuration);
		this.countIndicator = countIndicator;
		this.countIndicator.setReferencePeriodDuration(this.referenceDuration);
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
		this.fteIndicator.setReferencePeriodDuration(this.referenceDuration);
		this.countIndicator.setReferencePeriodDuration(this.referenceDuration);
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
		final float fte = this.fteIndicator.getNumericValue(organization).floatValue();
		final int paperCount = this.countIndicator.getNumericValue(organization).intValue();
		final float ratio = paperCount / fte / getReferencePeriodDuration();
		return Float.valueOf(ratio);
	}

}
