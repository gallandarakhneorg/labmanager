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

package fr.ciadlab.labmanager.service.assostructure;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.assostructure.AssociatedStructure;
import fr.ciadlab.labmanager.entities.assostructure.AssociatedStructureHolder;
import fr.ciadlab.labmanager.repository.assostructure.AssociatedStructureRepository;
import fr.ciadlab.labmanager.service.AbstractOrphanService;
import org.arakhne.afc.progress.Progression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for the managing the orphan associated structures.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Service
public class OrphanAssociatedStructureService extends AbstractOrphanService<AssociatedStructure> {

	private static final String MESSAGE_PREFIX = "orphanAssociatedStructureService."; //$NON-NLS-1$
	
	private AssociatedStructureRepository structureRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param structureRepository the repository for the associated structures.
	 */
	public OrphanAssociatedStructureService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired AssociatedStructureRepository structureRepository) {
		super(messages, constants);
		this.structureRepository = structureRepository;
	}

	@Override
	public void computeOrphans(ArrayNode receiver, Progression progress) {
		computeOrphansInJson(receiver, this.structureRepository, this,
				Constants.ASSOCIATED_STRUCTURE_EDITING_ENDPOINT, Constants.STRUCTURE_ENDPOINT_PARAMETER,
				Constants.ASSOCIATED_STRUCTURE_DELETING_ENDPOINT, Constants.STRUCTURE_ENDPOINT_PARAMETER,
				progress);
	}

	@Override
	public String getOrphanCriteria(AssociatedStructure structure) {
		if (structure.getHolders().isEmpty()) {
			return getMessage(MESSAGE_PREFIX + "EmptyHolderList"); //$NON-NLS-1$
		}
		if (structure.getCreationDate() == null) {
			return getMessage(MESSAGE_PREFIX + "NoCreationDate"); //$NON-NLS-1$
		}
		for (final AssociatedStructureHolder holder : structure.getHolders()) {
			if (holder.getPerson() == null) {
				return getMessage(MESSAGE_PREFIX + "NoPersonInHolder"); //$NON-NLS-1$
			}
			if (holder.getOrganization() == null) {
				return getMessage(MESSAGE_PREFIX + "NoOrganizationInHolder"); //$NON-NLS-1$
			}
		}
		return null;
	}

	@Override
	public String getOrphanEntityLabel(AssociatedStructure entity) {
		return entity.getAcronym() + " - " + entity.getName(); //$NON-NLS-1$
	}

	@Override
	public String getOrphanTypeLabel() {
		return getMessage(MESSAGE_PREFIX + "Name"); //$NON-NLS-1$
	}

}
