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

package fr.utbm.ciad.labmanager.rest.organization;

import java.util.Optional;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.EntityUtils;
import fr.utbm.ciad.labmanager.data.organization.OrganizationAddress;
import fr.utbm.ciad.labmanager.rest.AbstractApiController;
import fr.utbm.ciad.labmanager.services.organization.OrganizationAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public OrganizationAddressApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired OrganizationAddressService addressService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
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
	 * @param googleMapLink the link to the Google Map.
	 * @param pathToBackgroundImage the path to the background image.
	 * @param backgroundImage the background image.
	 * @param removedBackgroundImage indicates if the background image should be removed.
	 * @param validated indicates if the journal is validated by a local authority.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of problem for saving.
	 */
	@PutMapping(value = "/" + Constants.ORGANIZATION_ADDRESS_SAVING_ENDPOINT)
	public void saveOrganizationAddress(
			@RequestParam(required = false) Integer address,
			@RequestParam(required = true) String name,
			@RequestParam(required = false) String complement,
			@RequestParam(required = true) String street,
			@RequestParam(required = false) String zipCode,
			@RequestParam(required = true) String city,
			@RequestParam(required = false) String mapCoordinates,
			@RequestParam(required = false) String googleMapLink,
			@RequestParam(required = false) MultipartFile pathToBackgroundImage,
			@RequestParam(required = false, name = "@fileUpload_removed_pathToBackgroundImage", defaultValue = "false") boolean removedBackgroundImage,
			@RequestParam(required = false, defaultValue = "false") boolean validated,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		//TODO ensureCredentials(username, Constants.ORGANIZATION_ADDRESS_SAVING_ENDPOINT, address);
		try {
			final String inName = inString(name);
			final String inComplement = EntityUtils.fixAddressComplementForBackend(inString(complement));
			final String inStreet = inString(street);
			final String inZipCode = inString(zipCode);
			final String inCity = inString(city);
			final String inMapCoordinates = inString(mapCoordinates);
			final String inGoogleMapLink = inString(googleMapLink);
			//
			final Optional<OrganizationAddress> optAddress;
			//
			if (address == null) {
				optAddress = this.addressService.createAddress(validated, inName, inComplement, inStreet, inZipCode, inCity, inMapCoordinates,
						inGoogleMapLink, pathToBackgroundImage);
			} else {
				optAddress = this.addressService.updateAddress(address.intValue(), validated,
						inName, inComplement, inStreet, inZipCode, inCity, inMapCoordinates,
						inGoogleMapLink, pathToBackgroundImage, removedBackgroundImage);
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
	@DeleteMapping("/" + Constants.ORGANIZATION_ADDRESS_DELETING_ENDPOINT)
	public void deleteOrganizationAddress(
			@RequestParam(name = Constants.ADDRESS_ENDPOINT_PARAMETER) Integer address,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		//TODO ensureCredentials(username, Constants.ORGANIZATION_ADDRESS_DELETING_ENDPOINT, address);
		if (address == null || address.intValue() == 0) {
			throw new IllegalStateException("Address not found"); //$NON-NLS-1$
		}
		this.addressService.removeAddress(address.intValue());
	}

}