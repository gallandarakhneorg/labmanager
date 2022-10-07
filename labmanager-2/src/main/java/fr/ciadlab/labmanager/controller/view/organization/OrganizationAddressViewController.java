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

package fr.ciadlab.labmanager.controller.view.organization;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.organization.OrganizationAddress;
import fr.ciadlab.labmanager.service.organization.OrganizationAddressService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for organization addresses' views.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see ResearchOrganizationService
 * @since 2.0.0
 */
@RestController
@CrossOrigin
public class OrganizationAddressViewController extends AbstractViewController {

	private OrganizationAddressService addressService;
	
	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the live constants.
	 * @param addressService the service for managing the addresses.
	 */
	public OrganizationAddressViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired OrganizationAddressService addressService) {
		super(messages, constants);
		this.addressService = addressService;
	}

	/** Replies the model-view component for listing the research organizations' addresses. It is the main endpoint for this controller.
	 *
	 * @param username the name of the logged-in user.
	 * @return the model-view component.
	 */
	@GetMapping("/" + Constants.ORGANIZATION_ADDRESS_LIST_ENDPOINT)
	public ModelAndView showAddressList(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) {
		getLogger().info("Opening /" + Constants.ORGANIZATION_ADDRESS_LIST_ENDPOINT + " by " + username); //$NON-NLS-1$ //$NON-NLS-2$
		readCredentials(username);
		final ModelAndView modelAndView = new ModelAndView(Constants.ORGANIZATION_ADDRESS_LIST_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView, false);
		initAdminTableButtons(modelAndView, endpoint(Constants.ORGANIZATION_ADDRESS_EDITING_ENDPOINT, "address")); //$NON-NLS-1$
		modelAndView.addObject("addresses", this.addressService.getAllAddresses()); //$NON-NLS-1$
		return modelAndView;
	}

	/** Show the editor for an organization address. This editor permits to create or to edit an address.
	 *
	 * @param address the identifier of the address to edit. If it is {@code null}, the endpoint
	 *     is dedicated to the creation of an address.
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 */
	@GetMapping(value = "/" + Constants.ORGANIZATION_ADDRESS_EDITING_ENDPOINT)
	public ModelAndView showAddressEditor(
			@RequestParam(required = false) Integer address,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) {
		getLogger().info("Opening /" + Constants.ORGANIZATION_ADDRESS_EDITING_ENDPOINT + " by " + username + " for address " + address); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ensureCredentials(username);
		final ModelAndView modelAndView = new ModelAndView("addressEditor"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, false);
		//
		final OrganizationAddress addressObj;
		if (address != null && address.intValue() != 0) {
			addressObj = this.addressService.getAddressById(address.intValue());
			if (addressObj == null) {
				throw new IllegalArgumentException("Address not found: " + address); //$NON-NLS-1$
			}
		} else {
			addressObj = null;
		}
		//
		modelAndView.addObject("address", addressObj); //$NON-NLS-1$
		modelAndView.addObject("formActionUrl", rooted(Constants.ORGANIZATION_ADDRESS_SAVING_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("formRedirectUrl", rooted(Constants.ORGANIZATION_ADDRESS_LIST_ENDPOINT)); //$NON-NLS-1$
		return modelAndView;
	}

}
