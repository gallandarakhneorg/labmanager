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

package fr.utbm.ciad.labmanager.services.supervision;

import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.supervision.Supervision;
import fr.utbm.ciad.labmanager.data.supervision.SupervisionRepository;
import fr.utbm.ciad.labmanager.data.supervision.Supervisor;
import fr.utbm.ciad.labmanager.services.AbstractOrphanService;
import org.arakhne.afc.progress.Progression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for the person supervisions.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Service
public class OrphanSupervisionService extends AbstractOrphanService<Supervision> {

	private static final String MESSAGE_PREFIX = "orphanSupervisionService."; //$NON-NLS-1$

	private SupervisionRepository supervisionRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param supervisionRepository the repository for person supervisions.
	 */
	public OrphanSupervisionService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired SupervisionRepository supervisionRepository) {
		super(messages, constants);
		this.supervisionRepository = supervisionRepository;
	}

	@Override
	public void computeOrphans(ArrayNode receiver, Locale locale, Progression progress) {
		computeOrphansInJson(receiver, this.supervisionRepository, this,
				Constants.SUPERVISION_EDITING_ENDPOINT, Constants.PERSON_ENDPOINT_PARAMETER,
				Constants.SUPERVISION_DELETION_ENDPOINT, Constants.ID_ENDPOINT_PARAMETER,
				locale, progress);
	}

	@Override
	public String getOrphanCriteria(Supervision supervision, Locale locale) {
		if (supervision.getSupervisedPerson() == null) {
			return getMessage(locale, MESSAGE_PREFIX + "NoSupervisedPerson"); //$NON-NLS-1$
		}
		if (supervision.getFunding() == null) {
			return getMessage(locale, MESSAGE_PREFIX + "NoFunding"); //$NON-NLS-1$
		}
		if (supervision.getSupervisors().isEmpty()) {
			return getMessage(locale, MESSAGE_PREFIX + "EmptySupervisorList"); //$NON-NLS-1$
		}
		for (final Supervisor supervisor : supervision.getSupervisors()) {
			if (supervisor.getSupervisor() == null) {
				return getMessage(locale, MESSAGE_PREFIX + "NoPersonForSupervisor"); //$NON-NLS-1$
			}
		}
		return null;
	}

	@Override
	public String getOrphanEntityLabel(Supervision entity, Locale locale) {
		if (entity.getSupervisedPerson() != null) {
			return "? - " + join(entity.getSupervisors(), locale) + " - " + entity.getYear(); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return entity.getSupervisedPerson().getPerson() + " - " + join(entity.getSupervisors(), locale) + " - " + entity.getYear(); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private static String join(List<Supervisor> supervisors, Locale locale) {
		final StringBuffer buffer = new StringBuffer();
		for (final Supervisor supervisor : supervisors) {
			if (buffer.length() > 0) {
				buffer.append(", "); //$NON-NLS-1$
			}
			buffer.append(supervisor.getSupervisor().getFullName());
		}
		return buffer.toString();
	}


	@Override
	public String getOrphanTypeLabel(Locale locale) {
		return getMessage(locale, MESSAGE_PREFIX + "Name"); //$NON-NLS-1$
	}

}
