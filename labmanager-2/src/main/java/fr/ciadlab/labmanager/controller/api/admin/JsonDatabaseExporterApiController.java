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

package fr.ciadlab.labmanager.controller.api.admin;

import static fr.ciadlab.labmanager.entities.EntityUtils.isSimilarWithoutNormalization;
import static fr.ciadlab.labmanager.entities.EntityUtils.normalizeForSimularityTest;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.ciadlab.labmanager.AbstractComponent;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.json.DatabaseToJsonExporter;
import fr.ciadlab.labmanager.io.json.ExtraPublicationProvider;
import fr.ciadlab.labmanager.io.json.JsonTool;
import fr.ciadlab.labmanager.io.json.SimilarPublicationProvider;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** This controller provides a tool for exporting the content of the database according
 * to a specific JSON format that is independent of any database engine.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@RestController
@CrossOrigin
public class JsonDatabaseExporterApiController extends AbstractComponent {

	private static final String DUPLICATED_ENTRY_FIELD = "_duplicatedEntry"; //$NON-NLS-1$
	
	private static final int SIMILARE_ENTRY_VALUE = 0;
	
	private static final int SIMILARE_TITLE_VALUE = 1;

	private DatabaseToJsonExporter exporter;

	private PublicationService publicationService;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param exporter the exporter.
	 * @param publicationService the service for extracting publications from a BibTeX file.
	 */
	public JsonDatabaseExporterApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired DatabaseToJsonExporter exporter,
			@Autowired PublicationService publicationService) {
		super(messages);
		this.exporter = exporter;
		this.publicationService = publicationService;
	}

	/** Export the JSON.
	 *
	 * @param username the login of the logged-in person.
	 * @return The JSON data.
	 * @throws Exception in case of error.
	 */
	@GetMapping("/exportDatabaseToJson")
	public ResponseEntity<Map<String, Object>> exportDatabaseToJson(
			@CurrentSecurityContext(expression="authentication?.name") String username) throws Exception {
		if (isLoggedUser(username).booleanValue()) {
			final Map<String, Object> content = this.exporter.exportFromDatabase();
			final BodyBuilder bb = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + Constants.DEFAULT_PUBLICATION_ATTACHMENT_BASENAME + ".json\""); //$NON-NLS-1$ //$NON-NLS-2$
			final ResponseEntity<Map<String, Object>> result = bb.body(content);
			getLogger().info("JSON was generated from the Database only"); //$NON-NLS-1$
			return result;
		}
		throw new IllegalAccessException(getMessage("all.notLogged")); //$NON-NLS-1$
	}

	private static int getInt(Map<String, Object> source, String key) {
		final Object value = source.get(key);
		if (value instanceof Number) {
			try {
				return ((Number) value).intValue();
			} catch (Throwable ex) {
				//
			}
		}
		return 0;
	}

	private static boolean getBool(Map<String, Object> source, String key) {
		final Object value = source.get(key);
		if (value instanceof Boolean) {
			return ((Boolean) value).booleanValue();
		}
		if (value != null) {
			try {
				return Boolean.parseBoolean(value.toString());
			} catch (Throwable ex) {
				//
			}
		}
		return false;
	}

	private static String getString(Map<String, Object> source, String key) {
		final Object value = source.get(key);
		if (value != null) {
			return normalizeForSimularityTest(value.toString());
		}
		return null;
	}

	private static String getTarget(Map<String, Object> source) {
		final String scientificEventName = getString(source, JsonTool.SCIENTIFICEVENTNAME_KEY);
		if (!Strings.isNullOrEmpty(scientificEventName)) {
			return scientificEventName;
		}
		final String institution = getString(source, JsonTool.INSTITUTION_KEY);
		if (!Strings.isNullOrEmpty(institution)) {
			return institution;
		}
		final String journal = getString(source, JsonTool.JOURNAL_KEY);
		if (!Strings.isNullOrEmpty(journal)) {
			return journal;
		}
		final String publisher = getString(source, JsonTool.PUBLISHER_KEY);
		final String edition = getString(source, JsonTool.EDITION_KEY);
		if (!Strings.isNullOrEmpty(edition)) {
			return edition + (Strings.isNullOrEmpty(publisher) ? "" : (", " + publisher)); //$NON-NLS-1$ //$NON-NLS-2$
		}
		final String bookTitle = getString(source, JsonTool.BOOKTITLE_KEY);
		if (!Strings.isNullOrEmpty(bookTitle)) {
			return bookTitle + (Strings.isNullOrEmpty(publisher) ? "" : (", " + publisher)); //$NON-NLS-1$ //$NON-NLS-2$
		}
		final String howPublished = getString(source, JsonTool.HOWPUBLISHED_KEY);
		if (!Strings.isNullOrEmpty(howPublished)) {
			return howPublished + (Strings.isNullOrEmpty(publisher) ? "" : (", " + publisher)); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return null;
	}

	/** Export the JSON that is the result of the merging from the database and the given BibTeX file.
	 *
	 * @param bibtexFile the uploaded BibTeX file.
	 * @param addNewBibTeXEntries indicates if the entries from the BibTeX file that are not yet in the database
	 *     should be included into the created JSON. If this argument is {@code true}, the new BibTeX entries
	 *     are added. It this argument is {@code false}, the new BibTeX entries are not added into the JSON.
	 *     Default value is {@code true}.
	 * @param markDuplicateTitles indicates if the publications with the same title must be marked with a specific field named
	 *     {@code _duplicatedEntry}.
	 * @param username the login of the logged-in person.
	 * @return the result of the merging as a JSON data.
	 * @throws Exception in case of error.
	 */
	@PostMapping("/" + Constants.GET_JSON_FROM_DATABASE_AND_BIBTEX_ENDPOINT)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getJsonFromDatabaseAndBibTeX(
			@RequestParam(required = false) MultipartFile bibtexFile,
			@RequestParam(required = false, defaultValue = "true") boolean addNewBibTeXEntries,
			@RequestParam(required = false, defaultValue = "false") boolean markDuplicateTitles,
			@CurrentSecurityContext(expression="authentication?.name") String username) throws Exception {
		if (isLoggedUser(username).booleanValue()) {
			//
			// Read the BibTeX file obtaining informations that could be injected into the JSON if needed.
			final BibTeXSimilarPublicationProvider provider = new BibTeXSimilarPublicationProvider(this.publicationService, bibtexFile);
			//
			// Export the content of the database, and complete the missed data
			final int bibtexCount0 = provider.getPublications().size();
			final Map<String, Object> content = this.exporter.exportFromDatabase(
					provider,
					addNewBibTeXEntries ? provider : null);
			final int bibtexCount1 = provider.getPublications().size();
			getLogger().info("Number of publications that are merged into the database: " + (bibtexCount0 - bibtexCount1)); //$NON-NLS-1$
			getLogger().info("Number of publications that are added from BibTeX: " + bibtexCount1); //$NON-NLS-1$
			//
			// Try to mark the publications with duplicated titles
			if (markDuplicateTitles) {
				markDuplicates(content, provider);
			}
			//
			// Generate the answer
			final BodyBuilder bb = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + Constants.DEFAULT_PUBLICATION_ATTACHMENT_BASENAME + ".json\""); //$NON-NLS-1$ //$NON-NLS-2$
			final ResponseEntity<Map<String, Object>> result = bb.body(content);
			if (bibtexFile != null) {
				getLogger().info("JSON was generated from the Database and the BibTeX file: " + bibtexFile.getOriginalFilename()); //$NON-NLS-1$
			} else {
				getLogger().info("JSON was generated from the Database"); //$NON-NLS-1$
			}
			return result;
		}
		throw new IllegalAccessException(getMessage("all.notLogged")); //$NON-NLS-1$
	}

	private static void markDuplicates(Map<String, Object> content, BibTeXSimilarPublicationProvider provider) {
		if (content.containsKey(JsonTool.PUBLICATIONS_SECTION)) {
			@SuppressWarnings("unchecked")
			final List<Map<String, Object>> pubs = (List<Map<String, Object>>) content.get(JsonTool.PUBLICATIONS_SECTION);
			if (pubs != null && !pubs.isEmpty()) {
				final int max = pubs.size() - 1;
				for (int i = 0; i < max; ++i) {
					final Map<String, Object> publication0 = pubs.get(i);
					if (!publication0.containsKey(DUPLICATED_ENTRY_FIELD)) {
						final int year0 = getInt(publication0, JsonTool.PUBLICATIONYEAR_KEY);
						final String title0 = getString(publication0, JsonTool.TITLE_KEY);
						final String doi0 = getString(publication0, JsonTool.DOI_KEY);
						final String issn0 = getString(publication0, JsonTool.ISSN_KEY);
						final String target0 = getTarget(publication0);
						final boolean isNotValidated0 = !isValidated(publication0);
						int isDup = Integer.MAX_VALUE;
						for (int j = i + 1; j < pubs.size(); ++j) {
							final Map<String, Object> publication1 = pubs.get(j);
							if (!publication1.containsKey(DUPLICATED_ENTRY_FIELD)) {
								final int year1 = getInt(publication1, JsonTool.PUBLICATIONYEAR_KEY);
								final String title1 = getString(publication1, JsonTool.TITLE_KEY);
								final String doi1 = getString(publication1, JsonTool.DOI_KEY);
								final String issn1 = getString(publication1, JsonTool.ISSN_KEY);
								final String target1 = getTarget(publication1);
								if (isSimilarWithoutNormalization(year0, title0, doi0, issn0, target0,
										year1, title1, doi1, issn1, target1)) {
									// This case cannot be ignored
									isDup = SIMILARE_ENTRY_VALUE;
									publication1.put(DUPLICATED_ENTRY_FIELD, Integer.valueOf(SIMILARE_ENTRY_VALUE));
								} else if (isSimilarWithoutNormalization(title0, title1)) {
									// Ignore this case if one of the publications was validated.
									if (isNotValidated0) {
										isDup = Math.min(isDup, SIMILARE_TITLE_VALUE);
									}
									if (!isValidated(publication1)) {
										publication1.put(DUPLICATED_ENTRY_FIELD, Integer.valueOf(SIMILARE_TITLE_VALUE));
									}
								}
							}
						}
						if (isDup <= 10) {
							publication0.put(DUPLICATED_ENTRY_FIELD, Integer.valueOf(isDup));
						}
					}
				}
			}
		}
	}

	private static boolean isValidated(Map<String, Object> publication) {
		return getBool(publication, JsonTool.MANUALVALIDATIONFORCED_KEY);
	}

	/** Implementation of a similar publication provider that is based on a BibTeX source.
	 * 
	 * @author sgalland
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 2.0.0
	 */
	private static class BibTeXSimilarPublicationProvider implements SimilarPublicationProvider, ExtraPublicationProvider {

		private final List<Publication> publications;

		/** Constructor that is reading the BibTeX file.
		 *
		 * @param publicationService the publication service that is able to read a BibTeX file.
		 * @param bibtexFile the BibTeX file to be read.
		 * @throws Exception in case of reading error.
		 */
		BibTeXSimilarPublicationProvider(PublicationService publicationService, MultipartFile bibtexFile) throws Exception {
			if (bibtexFile != null) {
				try (final Reader reader = new InputStreamReader(bibtexFile.getInputStream())) {
					this.publications = publicationService.readPublicationsFromBibTeX(reader, true, false, true);
				}
			} else {
				this.publications = Collections.emptyList();
			}
		}

		@Override
		public List<Publication> getPublications() {
			return this.publications;
		}

		@Override
		public List<Publication> get(Publication source) {
			final int year0 = source.getPublicationYear();
			final String title0 = normalizeForSimularityTest(source.getTitle());
			final String doi0 = normalizeForSimularityTest(source.getDOI());
			final String issn0 = normalizeForSimularityTest(source.getISSN());
			final String target0 = normalizeForSimularityTest(source.getPublicationTarget());
			final List<Publication> similarPublications = new LinkedList<>();
			final Iterator<Publication> iterator = this.publications.iterator();
			while (iterator.hasNext()) {
				final Publication candidate = iterator.next();
				final int year1 = candidate.getPublicationYear();
				final String title1 = normalizeForSimularityTest(candidate.getTitle());
				final String doi1 = normalizeForSimularityTest(candidate.getDOI());
				final String issn1 = normalizeForSimularityTest(candidate.getISSN());
				final String target1 = normalizeForSimularityTest(candidate.getPublicationTarget());
				if (isSimilarWithoutNormalization(year0, title0, doi0, issn0, target0,
						year1, title1, doi1, issn1, target1)) {
					iterator.remove();
					similarPublications.add(candidate);
				}
			}
			return similarPublications;
		}

	}

}
