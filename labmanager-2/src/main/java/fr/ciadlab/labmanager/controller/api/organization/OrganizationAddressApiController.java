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

package fr.ciadlab.labmanager.controller.api.organization;

import java.util.Optional;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.organization.OrganizationAddress;
import fr.ciadlab.labmanager.service.organization.OrganizationAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST Controller for organization addresses.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@RestController
@CrossOrigin
public class OrganizationAddressApiController extends AbstractApiController {

	private OrganizationAddressService addressService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param constants the constants of the app.
	 * @param addressService the organization address service.
	 */
	public OrganizationAddressApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired OrganizationAddressService addressService) {
		super(messages, constants);
		this.addressService = addressService;
	}

	/** Saving information of an address. 
	 *
	 * @param address the identifier of the address. If the identifier is not provided, this endpoint is supposed to create
	 *     an address in the database.
	 * @param name the name of the address.
	 * @param complement the complementary information that may appear before the rest of the address.
	 * @param street the building number and street name. 
	 * @param zipCode the postal code.
	 * @param city the name of the city.
	 * @param mapCoordinates the geo. coordinates of the location.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of problem for saving.
	 */
	@PostMapping(value = "/" + Constants.ORGANIZATION_ADDRESS_SAVING_ENDPOINT)
	public void saveOrganization(
			@RequestParam(required = false) Integer address,
			@RequestParam(required = true) String name,
			@RequestParam(required = false) String complement,
			@RequestParam(required = true) String street,
			@RequestParam(required = false) String zipCode,
			@RequestParam(required = true) String city,
			@RequestParam(required = false) String mapCoordinates,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) throws Exception {
		getLogger().info("Opening /" + Constants.ORGANIZATION_ADDRESS_SAVING_ENDPOINT + " by " + username + " for address " + address); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ensureCredentials(username);
		try {
			final Optional<OrganizationAddress> optAddress;
			//
			if (address == null) {
				optAddress = this.addressService.createAddress(name, complement, street, zipCode, city, mapCoordinates);
			} else {
				optAddress = this.addressService.updateAddress(address.intValue(),
						name, complement, street, zipCode, city, mapCoordinates);
			}
			if (optAddress.isEmpty()) {
				throw new IllegalStateException("Address not found"); //$NON-NLS-1$
			}
		} catch (Throwable ex) {
			throw new IllegalStateException(ex.getLocalizedMessage(), ex);
		}
	}


	/** Delete an organization address from the database.
	 *
	 * @param address the identifier of the address.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@DeleteMapping("/deleteAddress")
	public void deleteOrganization(
			@RequestParam Integer address,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) throws Exception {
		getLogger().info("Opening /deleteAddress by " + username + " for address " + address); //$NON-NLS-1$ //$NON-NLS-2$
		ensureCredentials(username);
		if (address == null || address.intValue() == 0) {
			throw new IllegalStateException("Address not found"); //$NON-NLS-1$
		}
		this.addressService.removeAddress(address.intValue());
	}

}