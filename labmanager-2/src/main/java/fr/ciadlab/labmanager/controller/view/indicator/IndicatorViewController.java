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

package fr.ciadlab.labmanager.controller.view.indicator;

import java.util.List;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.indicators.Indicator;
import fr.ciadlab.labmanager.service.indicator.GlobalIndicatorsService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for viewing all the indicators.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.2
 */
@RestController
@CrossOrigin
public class IndicatorViewController extends AbstractViewController {

	private String defaultOrganizationName;

	private ResearchOrganizationService organizationService;

	private List<? extends Indicator> allIndicators;

	private GlobalIndicatorsService indicatorService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param organizationService the service to access to the organizations.
	 * @param allIndicators the list of all the indicators.
	 * @param indicatorService the service for accessing the global indicators.
	 * @param defaultOrganizationName the name of the default organization.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public IndicatorViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ResearchOrganizationService organizationService,
			@Autowired List<? extends Indicator> allIndicators,
			@Autowired GlobalIndicatorsService indicatorService,
			@Value("${labmanager.default-organization}") String defaultOrganizationName,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.defaultOrganizationName = defaultOrganizationName;
		this.organizationService = organizationService;
		this.allIndicators = allIndicators;
		this.indicatorService = indicatorService;
	}

	/** Replies the model-view component for listing the values of all of the indicators.
	 *
	 * @param organization the identifier of the organization for which the values must be computed.
	 *    If it is not provided, the default organization is used.
	 * @param username the name of the logged-in user.
	 * @return the model-view component.
	 */
	@GetMapping("/allIndicatorValueList")
	public ModelAndView allIndicatorValueList(
			@RequestParam(required = false) String organization,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		readCredentials(username, "/allIndicatorValueList"); //$NON-NLS-1$
		final ModelAndView modelAndView = new ModelAndView("allIndicatorValueList"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, false);
		//
		ResearchOrganization organizationObj = getOrganizationWith(organization, this.organizationService);
		if (organizationObj == null) {
			organizationObj = getOrganizationWith(this.defaultOrganizationName, this.organizationService);
		}
		if (organizationObj == null) {
			throw new IllegalArgumentException("Invalid organization identifier"); //$NON-NLS-1$
		}
		modelAndView.addObject("organization", organizationObj); //$NON-NLS-1$
		//
		final List<? extends Indicator> indicators = this.allIndicators.stream().sorted(
				(a, b) -> StringUtils.compare(a.getKey(), b.getKey())).collect(Collectors.toList());
		modelAndView.addObject("indicators", indicators); //$NON-NLS-1$
		//
		modelAndView.addObject("globalIndicators", this.indicatorService.getGlobalIndicatorsNeverNull()); //$NON-NLS-1$
		//
		modelAndView.addObject("resetIndicatorUrl", rooted(Constants.RESET_INDICATOR_CACHE_ENDPOINT)); //$NON-NLS-1$
		// 
		return modelAndView;
	}

}
