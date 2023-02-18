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

package fr.ciadlab.labmanager.controller.api.exports.utbm;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.conference.Conference;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.publication.ConferenceBasedPublication;
import fr.ciadlab.labmanager.entities.publication.JournalBasedPublication;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/** REST Controller for exporting elements to the UTBM organization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@RestController
@CrossOrigin
public class UtbmExportApiController extends AbstractApiController {

	private PublicationService publicationService;

	private ResearchOrganizationService organizationService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param constants the constants of the app.
	 * @param publicationService the publication service.
	 * @param organizationService the service for accessing the organizations.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public UtbmExportApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PublicationService publicationService,
			@Autowired ResearchOrganizationService organizationService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.publicationService = publicationService;
		this.organizationService = organizationService;
	}

	/**
	 * Export publications in a CSV document that corresponds to the UTBM requirements.
	 *
	 * @param organization the identifier or the name of the organization for which the publications must be extracted.
	 * @param year the reference year.
	 * @return the document for the publications.
	 * @throws Exception if it is impossible to redirect to the error page.
	 * @since 3.5
	 */
	@GetMapping(value = "/exportUtbmAnnualPublications")
	@ResponseBody
	public ResponseEntity<String> exportUtbmAnnualPublications(
			@RequestParam(required = true) String organization,
			@RequestParam(required = true) int year) throws Exception {
		final ResearchOrganization organizationObj = getOrganizationWith(organization, this.organizationService);
		if (organizationObj == null) {
			throw new IllegalArgumentException("Organization not found for: " + organization); //$NON-NLS-1$
		}
		final String content = exportPublicationCsv(organizationObj, year);
		BodyBuilder bb = ResponseEntity.ok().contentType(MIME_TYPE_CSV);
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
		bb = bb.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" //$NON-NLS-1$
				+ Constants.DEFAULT_PUBLICATIONS_ATTACHMENT_BASENAME
				+ "_" + simpleDateFormat.format(new Date()) + ".csv\""); //$NON-NLS-1$ //$NON-NLS-2$
		return bb.body(content);
	}

	/** Export the publications in a CSV file that corresponds to the UTBM standard.
	 * The columns of the CSV are:<ul>
	 * <li>Paper type</li>
	 * <li>Title</li>
	 * <li>Journal</li>
	 * <li>Conference</li>
	 * <li>Author 1</li>
	 * <li>Author 2</li>
	 * <li>Author 3</li>
	 * <li>Author 4</li>
	 * <li>Author 5</li>
	 * <li>Author 6</li>
	 * <li>Author 7</li>
	 * <li>Author 8</li>
	 * <li>Author 9</li>
	 * <li>Author 10</li>
	 * <li>Author 11</li>
	 * <li>Author 12</li>
	 * <li>Author 13</li>
	 * <li>Author 14</li>
	 * <li>Author 15</li>
	 * <li>DOI</li>
	 * <li>Keywords</li>
	 * <li>Research organization</li>
	 * </ul>
	 * 
	 * @param organization the organization for which the publications must be extracted.
	 * @param year the reference year that is used for filtering the publications.
	 * @return the CSV content.
	 * @throws IOException if the CSV cannot be generated.
	 */
	protected String exportPublicationCsv(ResearchOrganization organization, int year) throws IOException {
		final List<String[]> content = this.publicationService.getPublicationsByOrganizationId(organization.getId(), true, false).parallelStream()
				.filter(it -> it.getPublicationYear() == year)
				.map(it -> {
					final List<String> columns = new ArrayList<>(20);
					columns.add(it.getCategory().name());
					columns.add(Strings.nullToEmpty(it.getTitle()));
					if (it instanceof JournalBasedPublication) {
						final Journal journal = ((JournalBasedPublication) it).getJournal();
						columns.add(it.getPublicationTarget() + ", " + journal.getPublisher()); //$NON-NLS-1$
						columns.add(""); //$NON-NLS-1$
					} else if (it instanceof ConferenceBasedPublication) {
						final Conference conference = ((ConferenceBasedPublication) it).getConference();
						columns.add(it.getPublicationTarget() + ", " + conference.getPublisher()); //$NON-NLS-1$
						columns.add(""); //$NON-NLS-1$
					} else {
						columns.add(""); //$NON-NLS-1$
						columns.add(Strings.nullToEmpty(it.getWherePublishedShortDescription()));
					}
					final List<Person> authors = it.getAuthors();
					for (int i = 0; i < 15; ++i) {
						if (i < authors.size()) {
							columns.add(Strings.nullToEmpty(authors.get(i).getFullNameWithLastNameFirst()));
							
						} else {
							columns.add(""); //$NON-NLS-1$
						}
					}
					columns.add(Strings.nullToEmpty(it.getDOI()));
					columns.add(Strings.nullToEmpty(it.getKeywords()));
					columns.add(Strings.nullToEmpty(organization.getAcronymOrName()));
					final String[] array = new String[columns.size()];
					columns.toArray(array);
					return array;
				})
				.collect(Collectors.toList());
		//
		String csv = null;
		final StringWriter stream = new StringWriter();
		final CSVWriterBuilder builder = new CSVWriterBuilder(stream);
		try (final ICSVWriter writer = builder.build()) {
			writer.writeNext(new String[] {
					"Type d'article", //$NON-NLS-1$
					"Titre", //$NON-NLS-1$
					"Intitulé revue", //$NON-NLS-1$
					"Intitulé conférence", //$NON-NLS-1$
					"Auteur 1 - Nom", //$NON-NLS-1$
					"Auteur 2 - Nom", //$NON-NLS-1$
					"Auteur 3 - Nom", //$NON-NLS-1$
					"Auteur 4 - Nom", //$NON-NLS-1$
					"Auteur 5 - Nom", //$NON-NLS-1$
					"Auteur 6 - Nom", //$NON-NLS-1$
					"Auteur 7 - Nom", //$NON-NLS-1$
					"Auteur 8 - Nom", //$NON-NLS-1$
					"Auteur 9 - Nom", //$NON-NLS-1$
					"Auteur 10 - Nom", //$NON-NLS-1$
					"Auteur 11 - Nom", //$NON-NLS-1$
					"Auteur 12 - Nom", //$NON-NLS-1$
					"Auteur 13 - Nom", //$NON-NLS-1$
					"Auteur 14 - Nom", //$NON-NLS-1$
					"Auteur 15 - Nom", //$NON-NLS-1$
					"DOI", //$NON-NLS-1$
					"Mots-Clés", //$NON-NLS-1$
					"Laboratoire", //$NON-NLS-1$
			});
			writer.writeAll(content);
			csv = stream.toString();
		}
		return Strings.nullToEmpty(csv);
	}

}
