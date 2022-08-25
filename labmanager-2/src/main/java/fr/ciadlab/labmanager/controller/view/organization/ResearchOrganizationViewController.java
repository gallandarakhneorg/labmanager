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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationType;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.utils.CountryCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.annotation.CurrentSecurityContext;
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

	private static final String DEFAULT_ENDPOINT = "organizationList"; //$NON-NLS-1$

	private ResearchOrganizationService organizationService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param organizationService the research organization service.
	 */
	public ResearchOrganizationViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired ResearchOrganizationService organizationService) {
		super(messages);
		this.organizationService = organizationService;
	}

	/** Replies the model-view component for listing the research organizations. It is the main endpoint for this controller.
	 *
	 * @param username the login of the logged-in person.
	 * @return the model-view component.
	 */
	@GetMapping("/" + DEFAULT_ENDPOINT)
	public ModelAndView showOrganizationList(
			@CurrentSecurityContext(expression="authentication?.name") String username) {
		final ModelAndView modelAndView = new ModelAndView(DEFAULT_ENDPOINT);
		initModelViewProperties(modelAndView, username);
		modelAndView.addObject("organizations", this.organizationService.getAllResearchOrganizations()); //$NON-NLS-1$
		modelAndView.addObject("uuid", generateUUID()); //$NON-NLS-1$
		return modelAndView;
	}

	/** Show the editor for an organization. This editor permits to create or to edit an organization.
	 *
	 * @param organization the identifier of the organization to edit. If it is {@code null}, the endpoint
	 *     is dedicated to the creation of an organization.
	 * @param success flag that indicates the previous operation was a success.
	 * @param failure flag that indicates the previous operation was a failure.
	 * @param message the message that is associated to the state of the previous operation.
	 * @param username the login of the logged-in person.
	 * @return the model-view object.
	 */
	@GetMapping(value = "/" + Constants.ORGANIZATION_EDITING_ENDPOINT)
	public ModelAndView showOrganizationEditor(
			@RequestParam(required = false) Integer organization,
			@RequestParam(required = false, defaultValue = "false") Boolean success,
			@RequestParam(required = false, defaultValue = "false") Boolean failure,
			@RequestParam(required = false) String message,
			@CurrentSecurityContext(expression="authentication?.name") String username) {
		final ModelAndView modelAndView = new ModelAndView("organizationEditor"); //$NON-NLS-1$
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
		//
		List<ResearchOrganization> otherOrganizations = this.organizationService.getAllResearchOrganizations();
		if (organizationObj != null) {
			otherOrganizations = otherOrganizations.stream().filter(it -> it.getId() != organizationObj.getId()).collect(Collectors.toList());
		}
		//
		initModelViewProperties(modelAndView, username, success, failure, message);
		modelAndView.addObject("organization", organizationObj); //$NON-NLS-1$
		modelAndView.addObject("otherOrganizations", otherOrganizations); //$NON-NLS-1$
		modelAndView.addObject("defaultOrganizationType", ResearchOrganizationType.DEFAULT); //$NON-NLS-1$
		modelAndView.addObject("defaultCountry", CountryCodeUtils.DEFAULT); //$NON-NLS-1$
		modelAndView.addObject("countryLabels", CountryCodeUtils.getAllDisplayCountries()); //$NON-NLS-1$
		modelAndView.addObject("formActionUrl", "/" + Constants.ORGANIZATION_SAVING_ENDPOINT); //$NON-NLS-1$ //$NON-NLS-2$
		//
		return modelAndView;
	}

}
