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

package fr.ciadlab.labmanager.controller.api.scientificaxis;

import java.time.LocalDate;
import java.util.Optional;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.scientificaxis.ScientificAxis;
import fr.ciadlab.labmanager.service.scientificaxis.ScientificAxisService;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST Controller for scientific axis API.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.5
 */
@RestController
@CrossOrigin
public class ScientificAxisApiController extends AbstractApiController {

	private ScientificAxisService scientificAxisService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param scientificAxisService the service for managing the scientific axes.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public ScientificAxisApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ScientificAxisService scientificAxisService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.scientificAxisService = scientificAxisService;
	}

	/** Saving information of a scientific axis. 
	 *
	 * @param axis the identifier of the scientific axis. If the identifier is not provided, this endpoint is supposed to create
	 *     a scientific axis in the database.
	 * @param acronym the short name of acronym of the scientific axis.
	 * @param name the name of the scientific axis.
	 * @param startDate the start date of the scientific axis in format {@code YYY-MM-DD}.
	 * @param endDate the end date of the scientific axis in format {@code YYY-MM-DD}, or {@code null}.
	 * @param validated indicates if the project is validated by a local authority.
	 * @param username the name of the logged-in user.
	 * @throws Exception if the axis cannot be saved.
	 */
	@PutMapping(value = "/" + Constants.SCIENTIFIC_AXIS_SAVING_ENDPOINT)
	public void saveAssociatedStructure(
			@RequestParam(required = false) Integer axis,
			@RequestParam(required = true) String acronym,
			@RequestParam(required = true) String name,
			@RequestParam(required = true) String startDate,
			@RequestParam(required = false) String endDate,
			@RequestParam(required = false, defaultValue = "false") boolean validated,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.SCIENTIFIC_AXIS_SAVING_ENDPOINT, axis);

		// Analyze parameters
		final String inAcronym = inString(acronym);
		if (Strings.isNullOrEmpty(inAcronym)) {
			throw new IllegalArgumentException("Acronym is missed"); //$NON-NLS-1$
		}
		final String inName = inString(name);
		if (Strings.isNullOrEmpty(inName)) {
			throw new IllegalArgumentException("Name is missed"); //$NON-NLS-1$
		}
		final LocalDate inStartDate = LocalDate.parse(inString(startDate));
		final String inEndDateStr = inString(endDate);
		final LocalDate inEndDate = Strings.isNullOrEmpty(inEndDateStr) ? null : LocalDate.parse(inEndDateStr);

		// Create or update the structure
		Optional<ScientificAxis> axisOpt = Optional.empty();
		if (axis == null) {
			axisOpt = this.scientificAxisService.createScientificAxis(
					validated, inAcronym, inName, inStartDate, inEndDate);
		} else {
			axisOpt = this.scientificAxisService.updateScientificAxis(axis.intValue(),
					validated, inAcronym, inName, inStartDate, inEndDate);
		}
		if (axisOpt.isEmpty()) {
			throw new IllegalStateException("Scientific axis not found"); //$NON-NLS-1$
		}
	}

	/** Delete a scientific axis from the database.
	 *
	 * @param axis the identifier of the scientific axis.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@DeleteMapping("/deleteScientificAxis")
	public void deleteAssociatedStructure(
			@RequestParam(required = false, name = Constants.AXIS_ENDPOINT_PARAMETER) Integer axis,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, "deleteScientificAxis", axis); //$NON-NLS-1$
		if (axis == null || axis.intValue() == 0) {
			throw new IllegalStateException("Scientific axis not found"); //$NON-NLS-1$
		}
		this.scientificAxisService.removeScientificAxis(axis.intValue());
	}

}
