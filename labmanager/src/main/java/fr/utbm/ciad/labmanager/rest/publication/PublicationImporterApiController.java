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

package fr.utbm.ciad.labmanager.rest.publication;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.publication.PublicationType;
import fr.utbm.ciad.labmanager.rest.AbstractApiController;
import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.utils.io.json.JsonUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
public class PublicationImporterApiController extends AbstractApiController {

	private PublicationService publicationService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param constants the constants of the app.
	 * @param publicationService the publication service.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public PublicationImporterApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PublicationService publicationService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.publicationService = publicationService;
	}

	private static Map<String, PublicationType> preparePublicationSaving(String changes) throws Exception {
		final ObjectMapper json = JsonUtils.createMapper();
		final String inChanges = inString(changes);
		final Map<String, PublicationType> expectedTypes = new TreeMap<>();
		if (inChanges != null) {
			final Map<String, Object> jsonChanges;
			try (final ByteArrayInputStream sis = new ByteArrayInputStream(inChanges.getBytes())) {
				jsonChanges = json.readerForMapOf(Map.class).readValue(sis);
			}
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
		}
		return expectedTypes;
	}

	/** Save a BibTeX file in the database.
	 *
	 * @param bibtexFile the uploaded BibTeX files.
	 * @param changes a JSON string that represents the changes. It is expected to be a map in which the keys are
	 *     the BibTeX keys, and the values are sub-maps with the key {@code import} indicates if an entry should be
	 *     imported or not (with boolean value), and the key {@code type} is the string representation of the type of
	 *     publication to be considered for the BibTeX entry. If this expected publication type does not corresponds
	 *     to the type of BibTeX entry, an exception is thrown.
	 * @param createMissedJournals indicates if the missed journals in the database should be created on-the-fly from
	 *     the BibTeX data.
	 * @param createMissedConferences indicates if the missed conferences in the database should be created on-the-fly from
	 *     the BibTeX data.
	 * @param username the name of the logged-in user.
	 * @throws Exception if it is impossible to import the BibTeX file in the database.
	 */
	@PostMapping(value = "/" + Constants.SAVE_BIBTEX_ENDPOINT)
	public void saveBibTeX(
			@RequestParam(required = false) MultipartFile bibtexFile,
			@RequestParam(required = false) String changes,
			@RequestParam(required = false, defaultValue = "true") boolean createMissedJournals,
			@RequestParam(required = false, defaultValue = "true") boolean createMissedConferences,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		//TODO ensureCredentials(username, Constants.SAVE_BIBTEX_ENDPOINT);
		try {
			// Pass the changes string as JSON to extract the expected types of publications. 
			final Map<String, PublicationType> expectedTypes = preparePublicationSaving(changes);
			// Import the publications that are specified in the map of expected types.
			try (final Reader reader = new InputStreamReader(bibtexFile.getInputStream())) {
				this.publicationService.importBibTeXPublications(reader, expectedTypes, createMissedJournals, createMissedConferences);
			}
		} catch (Throwable ex) {
			getLogger().error(ex.getLocalizedMessage(), ex);
			throw ex;
		}
	}

	/** Save a RIS file in the database.
	 *
	 * @param risFile the uploaded RIS files.
	 * @param changes a JSON string that represents the changes. It is expected to be a map in which the keys are
	 *     the RIS identifiers, and the values are sub-maps with the key {@code import} indicates if an entry should be
	 *     imported or not (with boolean value), and the key {@code type} is the string representation of the type of
	 *     publication to be considered for the BibTeX entry. If this expected publication type does not corresponds
	 *     to the type of RIS entry, an exception is thrown.
	 * @param createMissedJournals indicates if the missed journals in the database should be created on-the-fly from
	 *     the RIS data.
	 * @param createMissedConferences indicates if the missed conferences in the database should be created on-the-fly from
	 *     the RIS data.
	 * @param username the name of the logged-in user.
	 * @throws Exception if it is impossible to import the RIS file in the database.
	 */
	@PostMapping(value = "/" + Constants.SAVE_RIS_ENDPOINT)
	public void saveRIS(
			@RequestParam(required = false) MultipartFile risFile,
			@RequestParam(required = false) String changes,
			@RequestParam(required = false, defaultValue = "true") boolean createMissedJournals,
			@RequestParam(required = false, defaultValue = "true") boolean createMissedConferences,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		//TODO ensureCredentials(username, Constants.SAVE_RIS_ENDPOINT);
		try {
			// Pass the changes string as JSON to extract the expected types of publications. 
			final Map<String, PublicationType> expectedTypes = preparePublicationSaving(changes);
			// Import the publications that are specified in the map of expected types.
			try (final Reader reader = new InputStreamReader(risFile.getInputStream())) {
				this.publicationService.importRISPublications(reader, expectedTypes, createMissedJournals, createMissedConferences);
			}
		} catch (Throwable ex) {
			getLogger().error(ex.getLocalizedMessage(), ex);
			throw ex;
		}
	}

}