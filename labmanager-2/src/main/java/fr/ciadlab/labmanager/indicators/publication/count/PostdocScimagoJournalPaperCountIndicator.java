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

package fr.ciadlab.labmanager.indicators.publication.count;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.service.publication.type.JournalPaperService;
import fr.ciadlab.labmanager.utils.Unit;
import fr.ciadlab.labmanager.utils.ranking.JournalRankingSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/** Count the number of journal papers ranked on Scimago with Postdoc as author.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Component
public class PostdocScimagoJournalPaperCountIndicator extends AbstractRankedJournalPaperCountIndicator {

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param journalPaperService the service for accessing the journal papers.
	 */
	public PostdocScimagoJournalPaperCountIndicator(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired JournalPaperService journalPaperService) {
		super(messages, constants, journalPaperService, it -> EntityUtils.hasPostdocAuthor(it));
	}

	@Override
	public JournalRankingSystem getJournalRankingSystem() {
		return JournalRankingSystem.SCIMAGO;
	}

	@Override
	public String getName() {
		return getMessage("postdocScimagoJournalPaperCountIndicator.name", getJournalRankingSystem().getLabel()); //$NON-NLS-1$
	}

	@Override
	public String getLabel(Unit unit) {
		return getLabelWithYears("postdocScimagoJournalPaperCountIndicator.label", getJournalRankingSystem().getLabel()); //$NON-NLS-1$
	}

}
