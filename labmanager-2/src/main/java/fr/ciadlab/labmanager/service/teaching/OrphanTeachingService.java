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

package fr.ciadlab.labmanager.service.teaching;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.teaching.TeachingActivity;
import fr.ciadlab.labmanager.repository.teaching.TeachingActivityRepository;
import fr.ciadlab.labmanager.service.AbstractOrphanService;
import org.arakhne.afc.progress.Progression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for the teaching activities.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.4
 */
@Service
public class OrphanTeachingService extends AbstractOrphanService<TeachingActivity> {

	private static final String MESSAGE_PREFIX = "orphanTeachingService."; //$NON-NLS-1$

	private TeachingActivityRepository teachingActivityRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param teachingActivityRepository the repository for the teaching activities.
	 * @param fileManager the manager of the uploaded and downloadable files.
	 */
	public OrphanTeachingService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired TeachingActivityRepository teachingActivityRepository) {
		super(messages, constants);
		this.teachingActivityRepository = teachingActivityRepository;
	}

	@Override
	public void computeOrphans(ArrayNode receiver, Progression progress) {
		computeOrphansInJson(receiver, this.teachingActivityRepository, this,
				Constants.TEACHING_ACTIVITY_EDITING_ENDPOINT, Constants.ACTIVITY_ENDPOINT_PARAMETER,
				Constants.TEACHING_ACTIVITY_DELETING_ENDPOINT, Constants.ACTIVITY_ENDPOINT_PARAMETER,
				progress);
	}

	@Override
	public String getOrphanCriteria(TeachingActivity activity) {
		if (activity.getStartDate() == null) {
			return getMessage(MESSAGE_PREFIX + "NoStartDate"); //$NON-NLS-1$
		}
		if (activity.getPerson() == null) {
			return getMessage(MESSAGE_PREFIX + "NoPerson"); //$NON-NLS-1$
		}
		if (activity.getUniversity() == null) {
			return getMessage(MESSAGE_PREFIX + "NoUniversity"); //$NON-NLS-1$
		}
		return null;
	}

	@Override
	public String getOrphanEntityLabel(TeachingActivity entity) {
		if (entity.getPerson() != null) {
			return entity.getCodeOrTitle() + " - ?"; //$NON-NLS-1$
		}
		return entity.getCodeOrTitle() + " - " + entity.getPerson().getFullName(); //$NON-NLS-1$
	}

	@Override
	public String getOrphanTypeLabel() {
		return getMessage(MESSAGE_PREFIX + "Name"); //$NON-NLS-1$
	}


}
