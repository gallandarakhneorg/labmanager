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

package fr.ciadlab.labmanager.service.organization;

import java.util.List;
import java.util.Optional;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.organization.OrganizationAddress;
import fr.ciadlab.labmanager.repository.organization.OrganizationAddressRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for organizations' addresses.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class OrganizationAddressService extends AbstractService {

	private final OrganizationAddressRepository addressRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param addressRepository the address repository.
	 */
	public OrganizationAddressService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired OrganizationAddressRepository addressRepository) {
		super(messages, constants);
		this.addressRepository = addressRepository;
	}

	/** Replies all the organizations' addresses.
	 *
	 * @return the address.
	 */
	public List<OrganizationAddress> getAllAddresses() {
		return this.addressRepository.findAll();
	}

	/** Replies the organization address with the given identifier.
	 *
	 * @param identifier the identifier of the address.
	 * @return the address or {@code null} if none.
	 */
	public OrganizationAddress getAddressById(int identifier) {
		final Optional<OrganizationAddress> adr = this.addressRepository.findById(Integer.valueOf(identifier));
		if (adr.isPresent()) {
			return adr.get();
		}
		return null;
	}

	/** Create a research organization address.
	 *
	 * @param name the name of the address.
	 * @param complement the complementary information that may appear before the rest of the address.
	 * @param street the building number and street name. 
	 * @param zipCode the postal code.
	 * @param city the name of the city.
	 * @param mapCoordinates the geo. coordinates of the location.
	 * @return the created address in the database.
	 */
	public Optional<OrganizationAddress> createAddress(String name, String complement, String street, String zipCode, String city,
			String mapCoordinates) {
		final OrganizationAddress adr = new OrganizationAddress();
		adr.setName(name);
		adr.setComplement(complement);
		adr.setStreet(street);
		adr.setZipCode(zipCode);
		adr.setCity(city);
		adr.setMapCoordinates(mapCoordinates);
		this.addressRepository.save(adr);
		return Optional.of(adr);
	}

	/** Update a research organization address.
	 *
	 * @param identifier the identifier in the database of the address to update.
	 * @param name the name of the address.
	 * @param complement the complementary information that may appear before the rest of the address.
	 * @param street the building number and street name. 
	 * @param zipCode the postal code.
	 * @param city the name of the city.
	 * @param mapCoordinates the geo. coordinates of the location.
	 * @return the created address in the database.
	 */
	public Optional<OrganizationAddress> updateAddress(int identifier, String name, String complement, String street, String zipCode,
			String city, String mapCoordinates) {
		final Optional<OrganizationAddress> res = this.addressRepository.findById(Integer.valueOf(identifier));
		if (res.isPresent()) {
			final OrganizationAddress address = res.get();
			if (!Strings.isNullOrEmpty(name)) {
				address.setName(name);
			}
			if (!Strings.isNullOrEmpty(complement)) {
				address.setComplement(complement);
			}
			if (!Strings.isNullOrEmpty(street)) {
				address.setStreet(street);
			}
			address.setZipCode(zipCode);
			if (!Strings.isNullOrEmpty(city)) {
				address.setCity(city);
			}
			address.setMapCoordinates(mapCoordinates);
			//
			this.addressRepository.save(address);
		}
		return res;
	}

	/** Remove from the database the organization address with the given database identifier.
	 *
	 * @param identifier the database identifier of the address.
	 */
	public void removeAddress(int identifier) {
		final Integer id = Integer.valueOf(identifier);
		this.addressRepository.deleteById(id);
	}

}
