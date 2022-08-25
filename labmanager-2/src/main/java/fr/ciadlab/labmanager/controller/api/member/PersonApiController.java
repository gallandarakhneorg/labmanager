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

import fr.ciadlab.labmanager.AbstractComponent;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.publication.PublicationApiController;
import fr.ciadlab.labmanager.entities.member.Gender;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.member.WebPageNaming;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.utils.names.PersonNameComparator;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.apache.jena.ext.com.google.common.base.Strings;
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

/** REST Controller for persons.
 * <p>
 * This controller does not manage the memberships' or authorships' databases themselves.
 * You must use {@link MembershipApiController} for managing the memberships, and
 * {@link PublicationApiController} for authorships.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see MembershipApiController
 * @see PublicationApiController
 */
@RestController
@CrossOrigin
public class PersonApiController extends AbstractComponent {

	private PersonService personService;

	private PersonNameParser nameParser;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param personService the person service.
	 * @param nameParser the parser of person names.
	 */
	public PersonApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired PersonService personService, @Autowired PersonNameParser nameParser) {
		super(messages);
		this.personService = personService;
		this.nameParser = nameParser;
	}

	/** Replies the information about a person as a JSON stream.
	 * This endpoint accepts one of the two parameters: the name or the identifier of the person.
	 * The name test is based on
	 * {@link PersonNameComparator#isSimilar(String, String) name similarity}, and not on strict equality.
	 *
	 * @param name the name of the person. The format should be recognized by a {@link PersonNameParser}.
	 *     Usually, it is {@code "FIRST LAST"} or {@code "LAST, FIRST"}.
	 * @param id the identifier of the person.
	 * @param strictName indicates if the name test must be strict (equality test) or not (similarity test).
	 *     By default, this parameter has the value {@code false}.
	 * @return the person, or {@code null} if the person with the given name was not found.
	 * @see PersonNameComparator
	 */
	@GetMapping(value = "/getPersonData", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Person getPersonData(@RequestParam(required = false) String name,
			@RequestParam(required = false) Integer id,
			@RequestParam(defaultValue = "false", required = false) boolean strictName) {
		if (id == null && Strings.isNullOrEmpty(name)) {
			throw new IllegalArgumentException("Name and identifier parameters are missed"); //$NON-NLS-1$
		}
		int pid = 0;
		if (Strings.isNullOrEmpty(name)) {
			if (id != null) {
				pid = id.intValue();
			}
		} else {
			final String firstName = this.nameParser.parseFirstName(name);
			final String lastName = this.nameParser.parseLastName(name);
			if (strictName) {
				pid = this.personService.getPersonIdByName(firstName, lastName);
			} else {
				pid = this.personService.getPersonIdBySimilarName(firstName, lastName);
			}
		}
		if (pid != 0) {
			return this.personService.getPersonById(pid);
		}
		return null;
	}

	/** Saving information of a person. 
	 *
	 * @param person the identifier of the person. If the identifier is not provided, this endpoint is supposed to create
	 *     a person in the database.
	 * @param firstName the first name of the person.
	 * @param lastName the last name of the person.
	 * @param gender the gender.
	 * @param email the email of the person.
	 * @param officePhone the phone number at office.
	 * @param mobilePhone the mobile phone number.
	 * @param gravatarId the identifier for obtaining a photo on Gravatar.
	 * @param orcid the ORCID of the person.
	 * @param researcherId the identifier of the person on ResearchId/WOS/Publon.
	 * @param linkedInId the identifier of the person on LinkedIn.
	 * @param githubId the identifier of the person on Github.
	 * @param researchGateId the identifier of the person on ResearchGate.
	 * @param facebookId the identifier of the person on Facebook.
	 * @param dblpURL the URL of the person's page on DBLP.
	 * @param academiaURL the URL of the person's page on Academia.edu.
	 * @param cordisURL the URL of the person's page on European Commission's Cordis.
	 * @param webPageNaming the type of naming for the person's webpage on the organization server.
	 * @param scholarHindex the Hindex of the person on Google Scholar.
	 * @param wosHindex the Hindex of the person on WOS.
	 * @param username the login of the logged-in person.
	 * @throws Exception if the person cannot be saved.
	 */
	@PostMapping(value = "/" + Constants.PERSON_SAVING_ENDPOINT)
	public void savePerson(
			@RequestParam(required = false) Integer person,
			@RequestParam(required = true) String firstName,
			@RequestParam(required = true) String lastName,
			@RequestParam(required = false) String gender,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String officePhone,
			@RequestParam(required = false) String mobilePhone,
			@RequestParam(required = false) String gravatarId,
			@RequestParam(required = false) String orcid,
			@RequestParam(required = false) String researcherId,
			@RequestParam(required = false) String linkedInId,
			@RequestParam(required = false) String githubId,
			@RequestParam(required = false) String researchGateId,
			@RequestParam(required = false) String facebookId,
			@RequestParam(required = false) String dblpURL,
			@RequestParam(required = false) String academiaURL,
			@RequestParam(required = false) String cordisURL,
			@RequestParam(required = false) String webPageNaming,
			@RequestParam(required = false) Integer scholarHindex,
			@RequestParam(required = false) Integer wosHindex,
			@CurrentSecurityContext(expression="authentication?.name") String username) throws Exception {
		if (isLoggedUser(username).booleanValue()) {
			final Gender genderObj = Strings.isNullOrEmpty(gender) ? Gender.NOT_SPECIFIED : Gender.valueOfCaseInsensitive(gender);
			final WebPageNaming webPageNamingObj = Strings.isNullOrEmpty(webPageNaming) ? WebPageNaming.UNSPECIFIED : WebPageNaming.valueOfCaseInsensitive(webPageNaming);
			final int shindex = scholarHindex == null ? 0 : scholarHindex.intValue();
			final int whindex = wosHindex == null ? 0 : wosHindex.intValue();
			//
			final Person optPerson;
			//
			if (person == null) {
				optPerson = this.personService.createPerson(
						firstName, lastName, genderObj, email, officePhone, mobilePhone,
						gravatarId, orcid, researcherId, linkedInId, githubId, researchGateId,
						facebookId, dblpURL, academiaURL, cordisURL, webPageNamingObj,
						shindex, whindex);
			} else {
				optPerson = this.personService.updatePerson(person.intValue(),
						firstName, lastName, genderObj, email, officePhone, mobilePhone,
						gravatarId, orcid, researcherId, linkedInId, githubId, researchGateId,
						facebookId, dblpURL, academiaURL, cordisURL, webPageNamingObj,
						shindex, whindex);
			}
			if (optPerson == null) {
				throw new IllegalStateException("Person not found"); //$NON-NLS-1$
			}
		} else {
			throw new IllegalAccessException(getMessage("all.notLogged")); //$NON-NLS-1$
		}
	}

	/** Delete a person from the database.
	 *
	 * @param person the identifier of the person.
	 * @param username the login of the logged-in person.
	 * @return the HTTP response.
	 */
	@DeleteMapping("/deletePerson")
	public ResponseEntity<Integer> deletePerson(
			@RequestParam Integer person,
			@CurrentSecurityContext(expression="authentication?.name") String username) {
		if (isLoggedUser(username).booleanValue()) {
			try {
				if (person == null || person.intValue() == 0) {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				this.personService.removePerson(person.intValue());
				return new ResponseEntity<>(person, HttpStatus.OK);
			} catch (Exception ex) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

}



