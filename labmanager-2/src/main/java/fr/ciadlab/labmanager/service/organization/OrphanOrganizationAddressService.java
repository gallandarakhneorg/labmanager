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

package fr.ciadlab.labmanager.service.organization;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.organization.OrganizationAddress;
import fr.ciadlab.labmanager.repository.organization.OrganizationAddressRepository;
import fr.ciadlab.labmanager.service.AbstractOrphanService;
import org.arakhne.afc.progress.Progression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for orphan organizations' addresses.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Service
public class OrphanOrganizationAddressService extends AbstractOrphanService<OrganizationAddress> {

	private static final String MESSAGE_PREFIX = "orphanOrganizationAddressService."; //$NON-NLS-1$

	private final OrganizationAddressRepository addressRepository;

	private final ResearchOrganizationService organizationService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param addressRepository the address repository.
	 * @param organizationService the service for accessing the organizations.
	 */
	public OrphanOrganizationAddressService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired OrganizationAddressRepository addressRepository,
			@Autowired ResearchOrganizationService organizationService) {
		super(messages, constants);
		this.addressRepository = addressRepository;
		this.organizationService = organizationService;
	}

	@Override
	public void computeOrphans(ArrayNode receiver, Progression progress) {
		computeOrphansInJson(receiver, this.addressRepository, this,
				Constants.ORGANIZATION_ADDRESS_EDITING_ENDPOINT, Constants.ADDRESS_ENDPOINT_PARAMETER,
				Constants.ORGANIZATION_ADDRESS_DELETING_ENDPOINT, Constants.ADDRESS_ENDPOINT_PARAMETER,
				progress);
	}

	@Override
	public String getOrphanCriteria(OrganizationAddress address) {
		if (this.organizationService.getAllResearchOrganizations().stream()
				.anyMatch(it -> it.getAddresses().stream().anyMatch(it0 -> it0.getId() == address.getId()))) {
			return null;
		}
		return getMessage(MESSAGE_PREFIX + "NotUsed"); //$NON-NLS-1$
	}

	@Override
	public String getOrphanEntityLabel(OrganizationAddress entity) {
		return entity.getFullAddress();
	}

	@Override
	public String getOrphanTypeLabel() {
		return getMessage(MESSAGE_PREFIX + "Name"); //$NON-NLS-1$
	}

}
