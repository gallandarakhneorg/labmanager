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

package fr.ciadlab.labmanager.controller.api.member;

import java.util.List;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.service.member.PersonMergingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST Controller for merging persons.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@RestController
@CrossOrigin
public class PersonMergingApiController extends AbstractApiController {

	private PersonMergingService mergingService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param constants the constants of the app.
	 * @param mergingService the service for merging persons.
	 */
	public PersonMergingApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PersonMergingService mergingService) {
		super(messages, constants);
		this.mergingService = mergingService;
	}

	/** Merge multiple persons into the database.
	 * Publications for a given list of authors is associated to a target author and
	 * unlinked from the old authors.
	 *
	 * @param target the identifier of the target person.
	 * @param sources the list of person identifiers that are considered as old persons.
	 * @param username the name of the logged-in user.
	 * @throws Exception if it is impossible to merge the persons.
	 */
	@PostMapping("/mergePersons")
	public void mergePersons(
			@RequestParam(required = true) Integer target,
			@RequestParam(required = true) List<Integer> sources,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) throws Exception {
		getLogger().info("Opening /mergePersons by " + username + " for target " + target); //$NON-NLS-1$ //$NON-NLS-2$
		ensureCredentials(username);
		try {
			this.mergingService.mergePersonsById(sources, target);
		} catch (Throwable ex) {
			getLogger().error(ex.getLocalizedMessage(), ex);
			throw ex;
		}
	}

}
