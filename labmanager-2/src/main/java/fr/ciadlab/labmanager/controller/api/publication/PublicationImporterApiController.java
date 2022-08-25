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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ciadlab.labmanager.AbstractComponent;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** REST Controller for publications' importer.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@RestController
@CrossOrigin
public class PublicationImporterApiController extends AbstractComponent {

	private PublicationService publicationService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param publicationService the publication service.
	 */
	public PublicationImporterApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired PublicationService publicationService) {
		super(messages);
		this.publicationService = publicationService;
	}

	/** Read a BibTeX file and replies the publications as JSON.
	 *
	 * @param bibtexFile the uploaded BibTeX files.
	 * @return the list of publications from the BibTeX file.
	 * @throws Exception if the BibTeX file cannot be used.
	 */
	@PostMapping(value = "/" + Constants.GET_JSON_FROM_BIBTEX_ENDPOINT)
	@ResponseBody
	public List<Publication> getJsonFromBibTeX(
			@RequestParam(required = false) MultipartFile bibtexFile) throws Exception {
		if (bibtexFile == null || bibtexFile.isEmpty()) {
			throw new IllegalArgumentException(getMessage("publicationImporterApiController.NoBibTeXSource")); //$NON-NLS-1$
		}
		try (final InputStream inputStream = bibtexFile.getInputStream()) {
			try (final Reader reader = new InputStreamReader(inputStream)) {
				return this.publicationService.readPublicationsFromBibTeX(reader, true, true);
			}
		}
	}

	/** Save a BibTeX file in the database..
	 *
	 * @param bibtexFile the uploaded BibTeX files.
	 * @param changes a JSON string that represents the changes. It is expected to be a map in which the keys are
	 *     the BibTeX keys, and the values are sub-maps with the key {@code import} indicates if an entry should be
	 *     imported or not (with boolean value), and the key {@code type} is the string representation of the type of
	 *     publication to be considered for the BibTeX entry. If this expected publication type does not corresponds
	 *     to the type of BibTeX entry, an exception is thrown.
	 * @param username the login of the logged-in person.
	 * @throws Exception if it is impossible to import the BibTeX file in the database.
	 */
	@PostMapping(value = "/" + Constants.SAVE_BIBTEX_ENDPOINT)
	public void saveBibTeX(
			@RequestParam(required = false) MultipartFile bibtexFile,
			@RequestParam(required = false) String changes,
			@CurrentSecurityContext(expression="authentication?.name") String username) throws Exception {
		if (isLoggedUser(username).booleanValue()) {
			try {
				// Pass the changes string as JSON to extract the expected types of publications. 
				final ObjectMapper json = new ObjectMapper();
				final Map<String, Object> jsonChanges;
				try (final ByteArrayInputStream sis = new ByteArrayInputStream(changes.getBytes())) {
					jsonChanges = json.readerForMapOf(Map.class).readValue(sis);
				}
				final Map<String, PublicationType> expectedTypes = new TreeMap<>();
				for (final Entry<String, Object> entry : jsonChanges.entrySet()) {
					@SuppressWarnings("unchecked")
					final Map<String, Object> sub = (Map<String, Object>) entry.getValue();
					if (sub != null && BooleanUtils.toBoolean(sub.getOrDefault("import", Boolean.FALSE).toString())) { //$NON-NLS-1$
						final Object expectedTypeStr = sub.get("type"); //$NON-NLS-1$
						if (expectedTypeStr != null && !Strings.isNullOrEmpty(expectedTypeStr.toString())) {
							final PublicationType type = PublicationType.valueOfCaseInsensitive(expectedTypeStr.toString());
							expectedTypes.put(entry.getKey(), type);
						}
					}
				}
				// Import the publications that are specified in the map of expected types.
				try (final Reader reader = new InputStreamReader(bibtexFile.getInputStream())) {
					this.publicationService.importPublications(reader, expectedTypes);
				}
			} catch (Throwable ex) {
				getLogger().error(ex.getLocalizedMessage(), ex);
				throw ex;
			}
		} else {
			throw new IllegalAccessException(getMessage("all.notLogged")); //$NON-NLS-1$
		}
	}

}
