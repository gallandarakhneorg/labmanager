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

package fr.ciadlab.labmanager.service.supervision;

import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.supervision.Supervision;
import fr.ciadlab.labmanager.entities.supervision.Supervisor;
import fr.ciadlab.labmanager.repository.supervision.SupervisionRepository;
import fr.ciadlab.labmanager.service.AbstractOrphanService;
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
	public void computeOrphans(ArrayNode receiver, Progression progress) {
		computeOrphansInJson(receiver, this.supervisionRepository, this,
				Constants.SUPERVISION_EDITING_ENDPOINT, Constants.PERSON_ENDPOINT_PARAMETER,
				Constants.SUPERVISION_DELETION_ENDPOINT, Constants.ID_ENDPOINT_PARAMETER,
				progress);
	}

	@Override
	public String getOrphanCriteria(Supervision supervision) {
		if (supervision.getSupervisedPerson() == null) {
			return getMessage(MESSAGE_PREFIX + "NoSupervisedPerson"); //$NON-NLS-1$
		}
		if (supervision.getFunding() == null) {
			return getMessage(MESSAGE_PREFIX + "NoFunding"); //$NON-NLS-1$
		}
		if (supervision.getSupervisors().isEmpty()) {
			return getMessage(MESSAGE_PREFIX + "EmptySupervisorList"); //$NON-NLS-1$
		}
		for (final Supervisor supervisor : supervision.getSupervisors()) {
			if (supervisor.getSupervisor() == null) {
				return getMessage(MESSAGE_PREFIX + "NoPersonForSupervisor"); //$NON-NLS-1$
			}
		}
		return null;
	}

	@Override
	public String getOrphanEntityLabel(Supervision entity) {
		if (entity.getSupervisedPerson() != null) {
			return "? - " + join(entity.getSupervisors()) + " - " + entity.getYear(); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return entity.getSupervisedPerson().getPerson() + " - " + join(entity.getSupervisors()) + " - " + entity.getYear(); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private static String join(List<Supervisor> supervisors) {
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
	public String getOrphanTypeLabel() {
		return getMessage(MESSAGE_PREFIX + "Name"); //$NON-NLS-1$
	}

}
