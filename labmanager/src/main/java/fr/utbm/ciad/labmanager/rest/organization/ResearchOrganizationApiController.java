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

package fr.utbm.ciad.labmanager.rest.organization;

import java.util.List;
import java.util.Optional;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganizationType;
import fr.utbm.ciad.labmanager.rest.AbstractApiController;
import fr.utbm.ciad.labmanager.services.organization.ResearchOrganizationService;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
public class ResearchOrganizationApiController extends AbstractApiController {

	private ResearchOrganizationService organizationService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param constants the constants of the app.
	 * @param organizationService the research organization service.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public ResearchOrganizationApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ResearchOrganizationService organizationService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
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
		final String inName = inString(name);
		final String inAcronym = inString(acronym);
		if (id == null && inName == null && inAcronym == null) {
			throw new IllegalArgumentException("Name, identifier and acronym parameters are missed"); //$NON-NLS-1$
		}
		if (id != null) {
			final Optional<ResearchOrganization> opt = this.organizationService.getResearchOrganizationById(id.intValue());
			return opt.isPresent() ? opt.get() : null;
		}
		if (!Strings.isNullOrEmpty(inAcronym)) {
			final Optional<ResearchOrganization> opt = this.organizationService.getResearchOrganizationByAcronym(inAcronym);
			return opt.isPresent() ? opt.get() : null;
		}
		final Optional<ResearchOrganization> opt = this.organizationService.getResearchOrganizationByName(inName);
		return opt.isPresent() ? opt.get() : null;
	}

	/** Saving information of an organization. 
	 *
	 * @param organization the identifier of the organization. If the identifier is not provided, this endpoint is supposed to create
	 *     an organization in the database.
	 * @param acronym the acronym of the organization.
	 * @param name the name of the organization.
	 * @param major indicates if the organization is marked as major.
	 * @param rnsr the number of the organization in the RNSR.
	 * @param nationalIdentifier the identifier of the organization for the national ministry of research.
	 * @param description the description of the organization.
	 * @param type the type of the organization. It is a constant of {@link ResearchOrganizationType}.
	 * @param organizationURL the web-site of the organization.
	 * @param country the country of the organization. It is a constant of {@link CountryCode}.
	 * @param organizationAddress the list of the identifiers of the addresses that are associated to the organization.
	 * @param superOrganization the identifier of the super organization, or {@code 0} if none.
	 * @param pathToLogo the uploaded logo of the organization, if any.
	 * @param removePathToLogo indicates if the path to the logo in the database should be removed, possibly before saving a new logo.
	 * @param validated indicates if the journal is validated by a local authority.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of problem for saving.
	 */
	@PutMapping(value = "/" + Constants.ORGANIZATION_SAVING_ENDPOINT)
	public void saveOrganization(
			@RequestParam(required = false) Integer organization,
			@RequestParam(required = true) String acronym,
			@RequestParam(required = true) String name,
			@RequestParam(required = false, defaultValue = "false") boolean major,
			@RequestParam(required = false) String rnsr,
			@RequestParam(required = false) String nationalIdentifier,
			@RequestParam(required = false) String description,
			@RequestParam(required = true) String type,
			@RequestParam(required = false) String organizationURL,
			@RequestParam(required = false) String country,
			@RequestParam(required = false) List<Integer> organizationAddress,
			@RequestParam(required = false) Integer superOrganization,
			@RequestParam(required = false) MultipartFile pathToLogo,
			@RequestParam(required = false, defaultValue = "false", name = "@fileUpload_removed_pathToLogo") boolean removePathToLogo,
			@RequestParam(required = false, defaultValue = "false") boolean validated,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		//TODO ensureCredentials(username, Constants.ORGANIZATION_SAVING_ENDPOINT, organization);
		try {
			final ResearchOrganizationType typeObj = ResearchOrganizationType.valueOfCaseInsensitive(inString(type));
			final CountryCode countryObj = CountryCode.valueOfCaseInsensitive(inString(country));
			//
			final String inAcronym = inString(acronym);
			final String inName = inString(name);
			final String inRnsr = inString(rnsr);
			final String inNationalIdentifier = inString(nationalIdentifier);
			final String inDescription = inString(description);
			final String inOrganizationURL = inString(organizationURL);
			//
			final Optional<ResearchOrganization> optOrganization;
			if (organization == null) {
				optOrganization = this.organizationService.createResearchOrganization(
						validated, inAcronym, inName, major, inRnsr, inNationalIdentifier, inDescription, typeObj,
						inOrganizationURL, countryObj, organizationAddress, superOrganization,
						pathToLogo);
			} else {
				optOrganization = this.organizationService.updateResearchOrganization(organization.intValue(),
						validated, inAcronym, inName, major, inRnsr, inNationalIdentifier, inDescription, typeObj,
						inOrganizationURL, countryObj, organizationAddress, superOrganization,
						pathToLogo, removePathToLogo);
			}
			if (optOrganization.isEmpty()) {
				throw new IllegalStateException("Organization not found"); //$NON-NLS-1$
			}
		} catch (Throwable ex) {
			throw new IllegalStateException(ex.getLocalizedMessage(), ex);
		}
	}

	/** Delete a research organization from the database.
	 *
	 * @param organization the identifier of the organization.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@DeleteMapping("/" + Constants.ORGANIZATION_DELETING_ENDPOINT)
	public void deleteOrganization(
			@RequestParam(name = Constants.ORGANIZATION_ENDPOINT_PARAMETER) Integer organization,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		//TODO ensureCredentials(username, Constants.ORGANIZATION_DELETING_ENDPOINT, organization);
		if (organization == null || organization.intValue() == 0) {
			throw new IllegalStateException("Organization not found"); //$NON-NLS-1$
		}
		this.organizationService.removeResearchOrganization(organization.intValue());
	}

}
