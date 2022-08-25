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

import fr.ciadlab.labmanager.AbstractComponent;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationType;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.utils.CountryCodeUtils;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.util.CountryCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
public class ResearchOrganizationApiController extends AbstractComponent {

	private ResearchOrganizationService organizationService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param organizationService the research organization service.
	 */
	public ResearchOrganizationApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired ResearchOrganizationService organizationService) {
		super(messages);
		this.organizationService = organizationService;
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
	public ResearchOrganization getOrganizationData(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) Integer id,
			@RequestParam(required = false) String acronym) {
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

	/** Saving information of an organization. 
	 *
	 * @param organization the identifier of the organization. If the identifier is not provided, this endpoint is supposed to create
	 *     an organization in the database.
	 * @param acronym the acronym of the organization.
	 * @param name the name of the organization.
	 * @param description the description of the organization.
	 * @param type the type of the organization. It is a constant of {@link ResearchOrganizationType}.
	 * @param organizationURL the web-site of the organization.
	 * @param country the country of the organization. It is a constant of {@link CountryCode}.
	 * @param superOrganization the identifier of the super organization, or {@code 0} if none.
	 * @param username the login of the logged-in person.
	 * @throws Exception in case of problem for saving.
	 */
	@PostMapping(value = "/" + Constants.ORGANIZATION_SAVING_ENDPOINT)
	public void saveOrganization(
			@RequestParam(required = false) Integer organization,
			@RequestParam(required = true) String acronym,
			@RequestParam(required = true) String name,
			@RequestParam(required = false) String description,
			@RequestParam(required = true) String type,
			@RequestParam(required = false) String organizationURL,
			@RequestParam(required = false) String country,
			@RequestParam(required = false) Integer superOrganization,
			@CurrentSecurityContext(expression="authentication?.name") String username) throws Exception {
		if (isLoggedUser(username).booleanValue()) {
			try {
				final ResearchOrganizationType typeObj = ResearchOrganizationType.valueOfCaseInsensitive(type);
				final CountryCode countryObj = CountryCodeUtils.valueOfCaseInsensitive(country);
				//
				final Optional<ResearchOrganization> optOrganization;
				//
				if (organization == null) {
					optOrganization = this.organizationService.createResearchOrganization(
							acronym, name, description, typeObj, organizationURL, countryObj, superOrganization);
				} else {
					optOrganization = this.organizationService.updateResearchOrganization(organization.intValue(),
							acronym, name, description, typeObj, organizationURL, countryObj, superOrganization);
				}
				if (optOrganization.isEmpty()) {
					throw new IllegalStateException("Organization not found"); //$NON-NLS-1$
				}
			} catch (Throwable ex) {
				throw new IllegalStateException(ex.getLocalizedMessage(), ex);
			}
		} else {
			throw new IllegalAccessException(getMessage("all.notLogged")); //$NON-NLS-1$
		}
	}

	/** Delete a research organization from the database.
	 *
	 * @param organization the identifier of the organization.
	 * @param username the login of the logged-in person.
	 * @return the HTTP response.
	 */
	@DeleteMapping("/deleteOrganization")
	public ResponseEntity<Integer> deleteOrganization(
			@RequestParam Integer organization,
			@CurrentSecurityContext(expression="authentication?.name") String username) {
		if (isLoggedUser(username).booleanValue()) {
			try {
				if (organization == null || organization.intValue() == 0) {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				this.organizationService.removeResearchOrganization(organization.intValue());
				return new ResponseEntity<>(organization, HttpStatus.OK);
			} catch (Exception ex) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

}
