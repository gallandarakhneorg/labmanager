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

package fr.utbm.ciad.labmanager.services.organization;

import java.util.Locale;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.organization.OrganizationAddress;
import fr.utbm.ciad.labmanager.data.organization.OrganizationAddressRepository;
import fr.utbm.ciad.labmanager.services.AbstractOrphanService;
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
	public void computeOrphans(ArrayNode receiver, Locale locale, Progression progress) {
		computeOrphansInJson(receiver, this.addressRepository, this,
				Constants.ORGANIZATION_ADDRESS_EDITING_ENDPOINT, Constants.ADDRESS_ENDPOINT_PARAMETER,
				Constants.ORGANIZATION_ADDRESS_DELETING_ENDPOINT, Constants.ADDRESS_ENDPOINT_PARAMETER,
				locale, progress);
	}

	@Override
	public String getOrphanCriteria(OrganizationAddress address, Locale locale) {
		if (this.organizationService.getAllResearchOrganizations().stream()
				.anyMatch(it -> it.getAddresses().stream().anyMatch(it0 -> it0.getId() == address.getId()))) {
			return null;
		}
		return getMessage(locale, MESSAGE_PREFIX + "NotUsed"); //$NON-NLS-1$
	}

	@Override
	public String getOrphanEntityLabel(OrganizationAddress entity, Locale locale) {
		return entity.getFullAddress();
	}

	@Override
	public String getOrphanTypeLabel(Locale locale) {
		return getMessage(locale, MESSAGE_PREFIX + "Name"); //$NON-NLS-1$
	}

}
