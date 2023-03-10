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

package fr.ciadlab.labmanager.service.scientificaxis;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.scientificaxis.ScientificAxis;
import fr.ciadlab.labmanager.repository.scientificaxis.ScientificAxisRepository;
import fr.ciadlab.labmanager.service.AbstractOrphanService;
import org.arakhne.afc.progress.Progression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for orphan scientific axes.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Service
public class OrphanScientificAxisService extends AbstractOrphanService<ScientificAxis> {

	private static final String MESSAGE_PREFIX = "orphanScientificAxisService."; //$NON-NLS-1$

	private final ScientificAxisRepository scientificAxisRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param scientificAxisRepository the repository for accessing the scientific axes.
	 */
	public OrphanScientificAxisService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ScientificAxisRepository scientificAxisRepository) {
		super(messages, constants);
		this.scientificAxisRepository = scientificAxisRepository;
	}

	@Override
	public void computeOrphans(ArrayNode receiver, Progression progress) {
		computeOrphansInJson(receiver, this.scientificAxisRepository, this,
				Constants.SCIENTIFIC_AXIS_EDITING_ENDPOINT, Constants.AXIS_ENDPOINT_PARAMETER,
				Constants.SCIENTIFIC_AXIS_DELETING_ENDPOINT, Constants.AXIS_ENDPOINT_PARAMETER,
				progress);
	}

	@Override
	public String getOrphanCriteria(ScientificAxis axis) {
		if (!axis.getProjects().isEmpty()) {
			return null;
		}
		if (!axis.getPublications().isEmpty()) {
			return null;
		}
		if (!axis.getMemberships().isEmpty()) {
			return null;
		}
		return getMessage(MESSAGE_PREFIX + "NoLink"); //$NON-NLS-1$
	}

	@Override
	public String getOrphanEntityLabel(ScientificAxis entity) {
		return entity.getAcronymOrName();
	}

	@Override
	public String getOrphanTypeLabel() {
		return getMessage(MESSAGE_PREFIX + "Name"); //$NON-NLS-1$
	}

}
