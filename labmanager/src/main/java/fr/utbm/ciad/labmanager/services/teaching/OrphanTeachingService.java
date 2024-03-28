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

package fr.utbm.ciad.labmanager.services.teaching;

import java.util.Locale;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.teaching.TeachingActivity;
import fr.utbm.ciad.labmanager.data.teaching.TeachingActivityRepository;
import fr.utbm.ciad.labmanager.services.AbstractOrphanService;
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
	public void computeOrphans(ArrayNode receiver, Locale locale, Progression progress) {
		computeOrphansInJson(receiver, this.teachingActivityRepository, this,
				Constants.TEACHING_ACTIVITY_EDITING_ENDPOINT, Constants.ACTIVITY_ENDPOINT_PARAMETER,
				Constants.TEACHING_ACTIVITY_DELETING_ENDPOINT, Constants.ACTIVITY_ENDPOINT_PARAMETER,
				locale, progress);
	}

	@Override
	public String getOrphanCriteria(TeachingActivity activity, Locale locale) {
		if (activity.getStartDate() == null) {
			return getMessage(locale, MESSAGE_PREFIX + "NoStartDate"); //$NON-NLS-1$
		}
		if (activity.getPerson() == null) {
			return getMessage(locale, MESSAGE_PREFIX + "NoPerson"); //$NON-NLS-1$
		}
		if (activity.getUniversity() == null) {
			return getMessage(locale, MESSAGE_PREFIX + "NoUniversity"); //$NON-NLS-1$
		}
		return null;
	}

	@Override
	public String getOrphanEntityLabel(TeachingActivity entity, Locale locale) {
		if (entity.getPerson() != null) {
			return entity.getCodeOrTitle() + " - ?"; //$NON-NLS-1$
		}
		return entity.getCodeOrTitle() + " - " + entity.getPerson().getFullName(); //$NON-NLS-1$
	}

	@Override
	public String getOrphanTypeLabel(Locale locale) {
		return getMessage(locale, MESSAGE_PREFIX + "Name"); //$NON-NLS-1$
	}


}
