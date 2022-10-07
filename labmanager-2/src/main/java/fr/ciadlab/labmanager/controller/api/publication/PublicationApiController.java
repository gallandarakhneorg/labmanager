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

package fr.ciadlab.labmanager.controller.api.publication;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** REST Controller for publications.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@RestController
@CrossOrigin
public class PublicationApiController extends AbstractApiController {

	private PublicationService publicationService;

	private PersonService personService;

	private DownloadableFileManager fileManager;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param constants the constants of the app.
	 * @param publicationService the publication service.
	 * @param personService the person service.
	 * @param fileManager the manager of local files.
	 */
	public PublicationApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PublicationService publicationService,
			@Autowired PersonService personService,
			@Autowired DownloadableFileManager fileManager) {
		super(messages, constants);
		this.publicationService = publicationService;
		this.personService = personService;
		this.fileManager = fileManager;
	}

	/** Replies data about a specific publication from the database.
	 * This endpoint accepts one of the two parameters: the title or the identifier of the publication.
	 * <p>This function and {@link #exportJson(HttpServletResponse, List, Integer, Integer, Integer)} differ
	 * from the structure of the JSON output. This function is a direct translation of the publication to
	 * JSON.
	 *
	 * @param title the title of the publication.
	 * @param id the identifier of the publication.
	 * @return the publication if a specific identifier is provided, or a list of publications that have the given title.
	 * @see #exportJson(HttpServletResponse, List, Integer, Integer, Integer)
	 */
	@GetMapping(value = "/getPublicationData", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Object getPublicationData(@RequestParam(required = false) String title, @RequestParam(required = false) Integer id) {
		if (id == null && Strings.isNullOrEmpty(title)) {
			throw new IllegalArgumentException("Title and identifier parameters are missed"); //$NON-NLS-1$
		}
		if (id != null) {
			return this.publicationService.getPublicationById(id.intValue());
		}
		return this.publicationService.getPublicationsByTitle(title);
	}

	/** Saving information of a publication. 
	 *
	 * @param publication the identifier of the publication. If the identifier is not provided, this endpoint is supposed to create
	 *     a publication in the database.
	 * @param pathToDownloadablePDF the uploaded PDF file for the publication.
	 * @param pathToDownloadableAwardCertificate the uploaded Award certificate for the publication.
	 * @param authors the list of authors. It is a list of database identifiers (for known persons) and full name
	 *     (for unknown persons).
	 * @param allParameters the map of all the request string-based parameters.
	 * @param username the name of the logged-in user.
	 * @throws Exception if the publication cannot be saved.
	 */
	@PostMapping(value = "/" + Constants.PUBLICATION_SAVING_ENDPOINT)
	public void savePublication(
			@RequestParam(required = false) Integer publication,
			@RequestParam(required = false) List<String> authors,
			@RequestParam(required = false) MultipartFile pathToDownloadablePDF,
			@RequestParam(required = false) MultipartFile pathToDownloadableAwardCertificate,
			@RequestParam Map<String, String> allParameters,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) throws Exception {
		ensureCredentials(username, Constants.PUBLICATION_SAVING_ENDPOINT, publication);
		// First check if the authors follows the contraints
		if (!this.personService.containsAMember(authors, true)) {
			throw new IllegalArgumentException("The list of authors does not contains a member of one of the known research organizations."); //$NON-NLS-1$
		}
		
		int uploadedPdfFile = 0;
		int uploadedAwardFile = 0;
		Optional<Publication> optPublication = Optional.empty();
		try {
			// The "type" parameter format is more complex than a simple enumeration constant.
			// It it the PublicationType constant followed by the category label.
			// We must reformat the type value to have only a enumeration constant.
			String typeValue = ensureString(allParameters, "type"); //$NON-NLS-1$
			typeValue = StringUtils.substringBefore(typeValue, "/"); //$NON-NLS-1$
			allParameters.put("type", typeValue); //$NON-NLS-1$
			//
			if (publication == null) {
				optPublication = this.publicationService.createPublicationFromMap(allParameters,
						authors, pathToDownloadablePDF, pathToDownloadableAwardCertificate);
			} else {
				optPublication = this.publicationService.updatePublicationFromMap(publication.intValue(), allParameters,
						authors, pathToDownloadablePDF, pathToDownloadableAwardCertificate);
			}
			if (optPublication.isEmpty()) {
				throw new IllegalStateException("Publication not found"); //$NON-NLS-1$
			}
		} catch (Throwable ex) {
			// Delete created publication
			if (optPublication.isPresent()) {
				try {
					this.publicationService.removePublication(optPublication.get().getId(), true);
				} catch (Throwable ex0) {
					// Silent
				}
			}
			// Delete any uploaded file
			if (uploadedPdfFile != 0) {
				try {
					this.fileManager.deleteDownloadablePublicationPdfFile(uploadedPdfFile);
				} catch (Throwable ex0) {
					// Silent
				}
			}
			if (uploadedAwardFile != 0) {
				try {
					this.fileManager.deleteDownloadableAwardPdfFile(uploadedAwardFile);
				} catch (Throwable ex0) {
					// Silent
				}
			}
			getLogger().error(ex.getLocalizedMessage(), ex);
			throw ex;
		}
	}

	/** Delete a publication from the database.
	 *
	 * @param publication the identifier of the publication.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@DeleteMapping("/deletePublication")
	public void deletePublication(
			@RequestParam Integer publication,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) String username) throws Exception {
		ensureCredentials(username, "deletePublication", publication); //$NON-NLS-1$
		if (publication == null || publication.intValue() == 0) {
			throw new IllegalStateException("Publication not found"); //$NON-NLS-1$
		}
		this.publicationService.removePublication(publication.intValue(), true);
	}

}
