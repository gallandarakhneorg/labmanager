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

package fr.ciadlab.labmanager.service.project;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.entities.project.ProjectMember;
import fr.ciadlab.labmanager.repository.project.ProjectRepository;
import fr.ciadlab.labmanager.service.AbstractOrphanService;
import org.arakhne.afc.progress.Progression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for the orphan research projects.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Service
public class OrphanProjectService extends AbstractOrphanService<Project> {

	private static final String MESSAGE_PREFIX = "orphanProjectService."; //$NON-NLS-1$

	private ProjectRepository projectRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param projectRepository the repository for the research projects.
	 * @param scientificAxisService the service for managing scientific axes.
	 * @param structureService the service for accessing the associated structures.
	 */
	public OrphanProjectService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ProjectRepository projectRepository) {
		super(messages, constants);
		this.projectRepository = projectRepository;
	}

	@Override
	public void computeOrphans(ArrayNode receiver, Progression progress) {
		computeOrphansInJson(receiver, this.projectRepository, this,
				Constants.PROJECT_EDITING_ENDPOINT, Constants.PROJECT_ENDPOINT_PARAMETER,
				Constants.PROJECT_DELETING_ENDPOINT, Constants.PROJECT_ENDPOINT_PARAMETER,
				progress);
	}

	@Override
	public String getOrphanCriteria(Project project) {
		if (project.getCoordinator() == null) {
			return getMessage(MESSAGE_PREFIX + "NoCoordinator"); //$NON-NLS-1$
		}
		if (project.getBudgets().isEmpty()) {
			return getMessage(MESSAGE_PREFIX + "EmptyBudgetList"); //$NON-NLS-1$
		}
		if (project.getStartDate() == null) {
			return getMessage(MESSAGE_PREFIX + "NoStartDate"); //$NON-NLS-1$
		}
		if (project.getLocalOrganization() == null) {
			return getMessage(MESSAGE_PREFIX + "NoLocalOrganization"); //$NON-NLS-1$
		}
		if (project.getLearOrganization() == null) {
			return getMessage(MESSAGE_PREFIX + "NoLearOrganization"); //$NON-NLS-1$
		}
		if (project.getParticipants().isEmpty()) {
			return getMessage(MESSAGE_PREFIX + "EmptyParticipantList"); //$NON-NLS-1$
		}
		for (final ProjectMember participant : project.getParticipants()) {
			if (participant.getPerson() == null) {
				return getMessage(MESSAGE_PREFIX + "MissedPersonForParticipant"); //$NON-NLS-1$
			}
		}
		return null;
	}

	@Override
	public String getOrphanEntityLabel(Project entity) {
		return entity.getAcronymOrScientificTitle() + " - " + entity.getStartYear(); //$NON-NLS-1$
	}

	@Override
	public String getOrphanTypeLabel() {
		return getMessage(MESSAGE_PREFIX + "Name"); //$NON-NLS-1$
	}

}
