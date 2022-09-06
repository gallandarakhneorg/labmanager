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

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import fr.ciadlab.labmanager.AbstractComponent;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.json.DatabaseToJsonExporter;
import fr.ciadlab.labmanager.io.json.SimilarPublicationProvider;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import info.debatty.java.stringsimilarity.SorensenDice;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;
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

	/** Export the JSON that is the result of the merging from the database and the given BibTeX file.
	 *
	 * @param bibtexFile the uploaded BibTeX file.
	 * @param username the login of the logged-in person.
	 * @return the result of the merging as a JSON data.
	 * @throws Exception in case of error.
	 */
	@PostMapping("/" + Constants.GET_JSON_FROM_DATABASE_AND_BIBTEX_ENDPOINT)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getJsonFromDatabaseAndBibTeX(
			@RequestParam(required = false) MultipartFile bibtexFile,
			@CurrentSecurityContext(expression="authentication?.name") String username) throws Exception {
		if (isLoggedUser(username).booleanValue()) {
			// Read the BibTeX file obtaining informations that could be injected into the JSON if needed.
			final SimilarPublicationProvider provider = new BibTeXSimilarPublicationProvider(this.publicationService, bibtexFile);
			// Export the content of the database, and complete the missed data
			final Map<String, Object> content = this.exporter.exportFromDatabase(provider);
			final BodyBuilder bb = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + Constants.DEFAULT_PUBLICATION_ATTACHMENT_BASENAME + ".json\""); //$NON-NLS-1$ //$NON-NLS-2$
			final ResponseEntity<Map<String, Object>> result = bb.body(content);
			getLogger().info("JSON was generated from the Database and the BibTeX file: " + bibtexFile.getOriginalFilename()); //$NON-NLS-1$
			return result;
		}
		throw new IllegalAccessException(getMessage("all.notLogged")); //$NON-NLS-1$
	}

	/** Implementation of a similar publication provider that is based on a BibTeX source.
	 * 
	 * @author sgalland
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 2.0.0
	 */
	private static class BibTeXSimilarPublicationProvider implements SimilarPublicationProvider {

		private static final double SIMILARITY = 0.95;
		
		private static final double SIMILARITY2 = 0.85;

		private final List<Publication> publications;

		private final NormalizedStringSimilarity similarity = new SorensenDice();
		
		/** Constructor that is reading the BibTeX file.
		 *
		 * @param publicationService the publication service that is able to read a BibTeX file.
		 * @param bibtexFile the BibTeX file to be read.
		 * @throws Exception in case of reading error.
		 */
		BibTeXSimilarPublicationProvider(PublicationService publicationService, MultipartFile bibtexFile) throws Exception {
			try (final Reader reader = new InputStreamReader(bibtexFile.getInputStream())) {
				this.publications = publicationService.readPublicationsFromBibTeX(reader, true, false);
			}
		}

		private static boolean isSimilar(int a, int b) {
			return Math.abs(a - b) <= 1;
		}

		private boolean isSimilar(String a, String b) {
			if (a == null || b == null) {
				return true;
			}
			return this.similarity.similarity(a, b) >= SIMILARITY;
		}
		
		private boolean isSimilar2(String a, String b) {
			if (a == null || b == null) {
				return true;
			}
			return this.similarity.similarity(a, b) >= SIMILARITY2;
		}

		private static String toLower(String a) {
			if (a == null) {
				return null;
			}
			return a.toLowerCase();
		}

		@Override
		public Publication get(Publication source) {
			final String baseTitle = toLower(source.getTitle());
			final String baseDOI = toLower(source.getDOI());
			final String baseWherePublished = toLower(source.getWherePublishedShortDescription().toLowerCase());
			final int baseYear = source.getPublicationYear();
			for (final Publication candidate : this.publications) {
				if (isSimilar(baseYear, candidate.getPublicationYear())
					&& isSimilar(baseTitle, toLower(candidate.getTitle()))
					&& isSimilar(baseDOI, toLower(candidate.getDOI()))
					&& isSimilar2(baseWherePublished, toLower(candidate.getWherePublishedShortDescription()))) {
					return candidate;
				}
			}
			return null;
		}

	}

}
