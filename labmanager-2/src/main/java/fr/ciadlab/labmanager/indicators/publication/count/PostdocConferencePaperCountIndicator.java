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

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.service.publication.type.ConferencePaperService;
import fr.ciadlab.labmanager.utils.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/** Count the number of conference papers written by a Postdoc for an organization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Component
public class PostdocConferencePaperCountIndicator extends AbstractConferencePaperCountIndicator {

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param conferencePaperService the service for accessing the conference papers.
	 */
	public PostdocConferencePaperCountIndicator(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ConferencePaperService conferencePaperService) {
		super(messages, constants, conferencePaperService, it -> EntityUtils.hasPostdocAuthor(it));
	}

	@Override
	public String getName() {
		return getMessage("postdocConferencePaperCountIndicator.name"); //$NON-NLS-1$
	}

	@Override
	public String getLabel(Unit unit) {
		return getLabelWithYears("postdocConferencePaperCountIndicator.label"); //$NON-NLS-1$
	}

}
