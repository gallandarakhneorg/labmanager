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

package fr.ciadlab.labmanager.controller.organization;

import java.util.Optional;

import fr.ciadlab.labmanager.controller.AbstractController;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriBuilderFactory;

/** REST Controller for research organizations.
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
public class ResearchOrganizationController extends AbstractController {

	private static final String DEFAULT_ENDPOINT = "organizationList"; //$NON-NLS-1$

	private ResearchOrganizationService organizationService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param organizationService the research organizationservice.
	 */
	public ResearchOrganizationController(
			@Autowired ResearchOrganizationService organizationService) {
		super(DEFAULT_ENDPOINT);
		this.organizationService = organizationService;
	}

	/** Replies the model-view component for listing the research organizations. It is the main endpoint for this controller.
	 *
	 * @return the model-view component.
	 */
	@GetMapping("/" + DEFAULT_ENDPOINT)
	public ModelAndView showOrganizationList() {
		final ModelAndView modelAndView = new ModelAndView(DEFAULT_ENDPOINT);
		modelAndView.addObject("organizations", this.organizationService.getAllResearchOrganizations()); //$NON-NLS-1$
		modelAndView.addObject("uuid", generateUUID()); //$NON-NLS-1$
		return modelAndView;
	}

	/** Replies data about a specific research organization from the database.
	 * This endpoint accepts one of the three parameters: the name, the identifier or the acronym of the organization.
	 *
	 * @param name the name of the organization.
	 * @param id the identifier of the organization.
	 * @param acronym the acronym of the organization.
	 * @return the organization.
	 */
	@GetMapping(value = "/getOrganizationData", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ResearchOrganization getOrganizationData(@RequestParam(required = false) String name,
			@RequestParam(required = false) Integer id, @RequestParam(required = false) String acronym) {
		if (id == null && Strings.isNullOrEmpty(name) && Strings.isNullOrEmpty(acronym)) {
			throw new IllegalArgumentException("Name, identifier and acronym parameters are missed"); //$NON-NLS-1$
		}
		if (id != null) {
			final Optional<ResearchOrganization> opt = this.organizationService.getResearchOrganizationById(id.intValue());
			return opt.isPresent() ? opt.get() : null;
		}
		if (!Strings.isNullOrEmpty(acronym)) {
			final Optional<ResearchOrganization> opt = this.organizationService.getResearchOrganizationByAcronym(acronym);
			return opt.isPresent() ? opt.get() : null;
		}
		final Optional<ResearchOrganization> opt = this.organizationService.getResearchOrganizationByName(name);
		return opt.isPresent() ? opt.get() : null;
	}

}
