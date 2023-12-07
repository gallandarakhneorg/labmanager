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

package fr.utbm.ciad.labmanager.views.tl.organization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.organization.OrganizationAddress;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganizationType;
import fr.utbm.ciad.labmanager.services.organization.OrganizationAddressService;
import fr.utbm.ciad.labmanager.services.organization.ResearchOrganizationService;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import fr.utbm.ciad.labmanager.views.tl.AbstractViewController;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for research organizations' views.
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
public class ResearchOrganizationViewController extends AbstractViewController {

	private OrganizationAddressService addressService;

	private ResearchOrganizationService organizationService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the live constants.
	 * @param addressService the service for manaing the addresses.
	 * @param organizationService the research organization service.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public ResearchOrganizationViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired OrganizationAddressService addressService,
			@Autowired ResearchOrganizationService organizationService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.addressService = addressService;
		this.organizationService = organizationService;
	}

	/** Replies the model-view component for listing the research organizations. It is the main endpoint for this controller.
	 *
	 * @param username the name of the logged-in user.
	 * @return the model-view component.
	 */
	@GetMapping("/" + Constants.ORGANIZATION_LIST_ENDPOINT)
	public ModelAndView showOrganizationList(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		readCredentials(username, Constants.ORGANIZATION_LIST_ENDPOINT);
		final ModelAndView modelAndView = new ModelAndView(Constants.ORGANIZATION_LIST_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView, false);
		initAdminTableButtons(modelAndView, endpoint(Constants.ORGANIZATION_EDITING_ENDPOINT, "organization")); //$NON-NLS-1$
		modelAndView.addObject("organizations", this.organizationService.getAllResearchOrganizations()); //$NON-NLS-1$
		return modelAndView;
	}

	/** Show the editor for an organization. This editor permits to create or to edit an organization.
	 *
	 * @param organization the identifier of the organization to edit. If it is {@code null}, the endpoint
	 *     is dedicated to the creation of an organization.
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 * @throws JsonProcessingException if the JSON for an address cannot be generated.
	 */
	@GetMapping(value = "/" + Constants.ORGANIZATION_EDITING_ENDPOINT)
	public ModelAndView showOrganizationEditor(
			@RequestParam(required = false) Integer organization,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws JsonProcessingException {
		ensureCredentials(username, Constants.ORGANIZATION_EDITING_ENDPOINT, organization);
		final ModelAndView modelAndView = new ModelAndView("organizationEditor"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, false);
		//
		final ResearchOrganization organizationObj;
		if (organization != null && organization.intValue() != 0) {
			Optional<ResearchOrganization> opt = this.organizationService.getResearchOrganizationById(organization.intValue());
			if (opt.isPresent()) {
				organizationObj = opt.get();
			} else {
				throw new IllegalArgumentException("Organization not found: " + organization); //$NON-NLS-1$
			}
		} else {
			organizationObj = null;
		}
		// Provide more information about uploaded logo
		if (organizationObj != null) {
			final String logoPath = organizationObj.getPathToLogo();
			if (!Strings.isNullOrEmpty(logoPath)) {
				modelAndView.addObject("pathToLogo_basename", FileSystem.largeBasename(logoPath)); //$NON-NLS-1$
				modelAndView.addObject("pathToLogo_picture", rootedThumbnail(logoPath, true)); //$NON-NLS-1$
			}
		}
		//
		List<ResearchOrganization> otherOrganizations = this.organizationService.getAllResearchOrganizations();
		if (organizationObj != null) {
			otherOrganizations = otherOrganizations.stream().filter(it -> it.getId() != organizationObj.getId())
					.sorted((a, b) -> a.getAcronym().compareToIgnoreCase(b.getAcronym())).collect(Collectors.toList());
		}
		//
		final List<OrganizationAddress> availableAddresses = new ArrayList<>();
		final List<OrganizationAddress> organizationAddresses = new ArrayList<>();
		for (final OrganizationAddress adr : this.addressService.getAllAddresses()) {
			if (organizationObj != null && organizationObj.getAddresses().contains(adr)) {
				organizationAddresses.add(adr);
			} else {
				availableAddresses.add(adr);
			}
		}
		//
		final List<ResearchOrganizationType> sortedOrganizationTypes = Arrays.asList(ResearchOrganizationType.values())
				.stream().sorted((a, b) -> a.getLabel().compareToIgnoreCase(b.getLabel())).collect(Collectors.toList());
		modelAndView.addObject("sortedOrganizationTypes", sortedOrganizationTypes); //$NON-NLS-1$
		//
		modelAndView.addObject("organization", organizationObj); //$NON-NLS-1$
		modelAndView.addObject("formActionUrl", rooted(Constants.ORGANIZATION_SAVING_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("formRedirectUrl", rooted(Constants.ORGANIZATION_LIST_ENDPOINT)); //$NON-NLS-1$
		modelAndView.addObject("defaultOrganizationType", ResearchOrganizationType.DEFAULT); //$NON-NLS-1$
		modelAndView.addObject("countryLabels", CountryCode.getAllDisplayCountries()); //$NON-NLS-1$
		modelAndView.addObject("defaultCountry", CountryCode.getDefault()); //$NON-NLS-1$
		modelAndView.addObject("otherOrganizations", otherOrganizations); //$NON-NLS-1$
		modelAndView.addObject("organizationAddresses", organizationAddresses); //$NON-NLS-1$
		modelAndView.addObject("availableAddresses", availableAddresses); //$NON-NLS-1$
		return modelAndView;
	}

}
