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

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.conference.Conference;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.publication.ConferenceBasedPublication;
import fr.ciadlab.labmanager.entities.publication.JournalBasedPublication;
import fr.ciadlab.labmanager.io.od.OdfSpreadsheetHelper;
import fr.ciadlab.labmanager.io.od.OdfSpreadsheetHelper.TableContentHelper;
import fr.ciadlab.labmanager.io.od.OdfSpreadsheetHelper.TableContentRowHelper;
import fr.ciadlab.labmanager.io.od.OdfSpreadsheetHelper.TableHeaderHelper;
import fr.ciadlab.labmanager.io.od.OdfSpreadsheetHelper.TableHelper;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.CookieValue;
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

	private static final String PAPER_TYPE_COLUMN = "Type d'article"; //$NON-NLS-1$

	private static final String TITLE_COLUMN = "Titre"; //$NON-NLS-1$

	private static final String JOURNAL_COLUMN = "Intitulé revue"; //$NON-NLS-1$

	private static final String CONFERENCE_COLUMN = "Intitulé conférence"; //$NON-NLS-1$

	private static final String AUTHOR_0_COLUMN = "Auteur 1 - Nom"; //$NON-NLS-1$

	private static final String AUTHOR_1_COLUMN = "Auteur 2 - Nom"; //$NON-NLS-1$

	private static final String AUTHOR_2_COLUMN = "Auteur 3 - Nom"; //$NON-NLS-1$

	private static final String AUTHOR_3_COLUMN = "Auteur 4 - Nom"; //$NON-NLS-1$

	private static final String AUTHOR_4_COLUMN = "Auteur 5 - Nom"; //$NON-NLS-1$

	private static final String AUTHOR_5_COLUMN = "Auteur 6 - Nom"; //$NON-NLS-1$

	private static final String AUTHOR_6_COLUMN = "Auteur 7 - Nom"; //$NON-NLS-1$

	private static final String AUTHOR_7_COLUMN = "Auteur 8 - Nom"; //$NON-NLS-1$

	private static final String AUTHOR_8_COLUMN = "Auteur 9 - Nom"; //$NON-NLS-1$

	private static final String AUTHOR_9_COLUMN = "Auteur 10 - Nom"; //$NON-NLS-1$

	private static final String AUTHOR_10_COLUMN = "Auteur 11 - Nom"; //$NON-NLS-1$

	private static final String AUTHOR_11_COLUMN = "Auteur 12 - Nom"; //$NON-NLS-1$

	private static final String AUTHOR_12_COLUMN = "Auteur 13 - Nom"; //$NON-NLS-1$

	private static final String AUTHOR_13_COLUMN = 	"Auteur 14 - Nom"; //$NON-NLS-1$

	private static final String AUTHOR_14_COLUMN = "Auteur 15 - Nom"; //$NON-NLS-1$

	private static final String DOI_COLUMN = "DOI"; //$NON-NLS-1$

	private static final String KEYWORDS_COLUMN = "Mots-Clés"; //$NON-NLS-1$

	private static final String LAB_COLUMN = "Laboratoire"; //$NON-NLS-1$

	private static final String RANK_COLUMN = "Classement WoS ou CORE"; //$NON-NLS-1$

	private static final String IMPACT_FACTOR_COLUMN = "Facteur d'impact"; //$NON-NLS-1$

	private static final String PUBLICATIONS_TABLE_NAME = "Publications {0,number,0000}"; //$NON-NLS-1$

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
	 * Export annual indicators to the UTBM requirements.
	 *
	 * @param organization the identifier or the name of the organization for which the publications must be extracted.
	 * @param year the reference year.
	 * @param username the name of the logged-in user.
	 * @return the document for the publications.
	 * @throws Exception if it is impossible to redirect to the error page.
	 * @since 3.5
	 */
	@GetMapping(value = "/exportUtbmAnnualIndicators")
	@ResponseBody
	public ResponseEntity<byte[]> exportUtbmAnnualPublications(
			@RequestParam(required = true) String organization,
			@RequestParam(required = true) int year,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		readCredentials(username, "exportUtbmAnnualIndicators", organization, Integer.valueOf(year)); //$NON-NLS-1$
		final ResearchOrganization organizationObj = getOrganizationWith(organization, this.organizationService);
		if (organizationObj == null) {
			throw new IllegalArgumentException("Organization not found for: " + organization); //$NON-NLS-1$
		}
		//
		final byte[] content;
		final MediaType mediaType;
		final String filenameExtension;
		try (final OdfSpreadsheetHelper ods = new OdfSpreadsheetHelper()) {
			getLogger().info("Generating UTBM indicators' spreadsheet for publications"); //$NON-NLS-1$
			exportPublications(ods, organizationObj, year);
			getLogger().info("Generating spreadsheet bytes"); //$NON-NLS-1$
			content = ods.toByteArray(true);
			mediaType = ods.getMediaType();
			filenameExtension = ods.getFileExtension();
		}
		//
		BodyBuilder bb = ResponseEntity.ok().contentType(mediaType);
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
		bb = bb.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" //$NON-NLS-1$
				+ Constants.DEFAULT_UTBM_INDICATORS_ATTACHMENT_BASENAME
				+ "_" + simpleDateFormat.format(new Date()) + "." + filenameExtension + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return bb.body(content);
	}

	/** Export the publications in a spreadsheet page that corresponds to the UTBM standard.
	 * The columns of the Excel are:<ul>
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
	 * <li>WoS/CORE Ranking</li>
	 * <li>Impact factor</li>
	 * </ul>
	 * 
	 * @param document the output document.
	 * @param organization the organization for which the publications must be extracted.
	 * @param year the reference year that is used for filtering the publications.
	 * @throws Exception if the Excel cannot be generated.
	 */
	protected void exportPublications(OdfSpreadsheetHelper document, ResearchOrganization organization, int year) throws Exception {
		final TableHelper output = document.newTable(MessageFormat.format(PUBLICATIONS_TABLE_NAME, Integer.valueOf(year)));
		//
		final TableHeaderHelper header = output.getHeader();
		header.appendColumn(PAPER_TYPE_COLUMN);
		header.appendColumn(TITLE_COLUMN);
		header.appendColumn(JOURNAL_COLUMN);
		header.appendColumn(CONFERENCE_COLUMN);
		header.appendColumn(AUTHOR_0_COLUMN);
		header.appendColumn(AUTHOR_1_COLUMN);
		header.appendColumn(AUTHOR_2_COLUMN);
		header.appendColumn(AUTHOR_3_COLUMN);
		header.appendColumn(AUTHOR_4_COLUMN);
		header.appendColumn(AUTHOR_5_COLUMN);
		header.appendColumn(AUTHOR_6_COLUMN);
		header.appendColumn(AUTHOR_7_COLUMN);
		header.appendColumn(AUTHOR_8_COLUMN);
		header.appendColumn(AUTHOR_9_COLUMN);
		header.appendColumn(AUTHOR_10_COLUMN);
		header.appendColumn(AUTHOR_11_COLUMN);
		header.appendColumn(AUTHOR_12_COLUMN);
		header.appendColumn(AUTHOR_13_COLUMN);
		header.appendColumn(AUTHOR_14_COLUMN);
		header.appendColumn(DOI_COLUMN);
		header.appendColumn(KEYWORDS_COLUMN);
		header.appendColumn(LAB_COLUMN);
		header.appendColumn(RANK_COLUMN);
		header.appendColumn(IMPACT_FACTOR_COLUMN);
		//
		final TableContentHelper content = output.getContent();
		this.publicationService.getPublicationsByOrganizationId(organization.getId(), true, false).stream()
				.filter(it -> it.getPublicationYear() == year)
				.forEach(it -> {
					final TableContentRowHelper row = content.appendRow();
					row.append(it.getCategory().name());
					row.append(it.getTitle());
					if (it instanceof JournalBasedPublication) {
						final Journal journal = ((JournalBasedPublication) it).getJournal();
						row.append(it.getPublicationTarget() + ", " + journal.getPublisher()); //$NON-NLS-1$
						row.append((String) null);
					} else if (it instanceof ConferenceBasedPublication) {
						row.append((String) null);
						final Conference conference = ((ConferenceBasedPublication) it).getConference();
						if (conference != null) {
							row.append(it.getPublicationTarget() + ", " + conference.getPublisher()); //$NON-NLS-1$
						} else {
							row.append(it.getWherePublishedShortDescription());
						}
					} else {
						row.append((String) null);
						row.append(it.getWherePublishedShortDescription());
					}
					final List<Person> authors = it.getAuthors();
					for (int i = 0; i < 15; ++i) {
						if (i < authors.size()) {
							row.append(authors.get(i).getFullNameWithLastNameFirst());
							
						} else {
							row.append((String) null);
						}
					}
					row.append(it.getDOI());
					row.append(it.getKeywords());
					row.append(organization.getAcronymOrName());
					boolean hasRank = false;
					boolean hasImpactFactor = false;
					if (it instanceof JournalBasedPublication) {
						final Journal journal = ((JournalBasedPublication) it).getJournal();
						final QuartileRanking quartile = journal.getWosQIndexByYear(year);
						if (quartile != null && quartile != QuartileRanking.NR) {
							hasRank = true;
							row.append(quartile.name());
						}
						final float impactFactor = journal.getImpactFactorByYear(year);
						if (impactFactor > 0f) {
							hasImpactFactor = true;
							row.append(Double.valueOf(impactFactor));
						}
					} else if (it instanceof ConferenceBasedPublication) {
						final Conference conference = ((ConferenceBasedPublication) it).getConference();
						if (conference != null) {
							final CoreRanking ranking = conference.getCoreIndexByYear(year);
							if (ranking != null && ranking != CoreRanking.NR) {
								hasRank = true;
								row.append(ranking.toString());
							}
						}
					}
					if (!hasRank) {
						row.append((String) null);
					}
					if (!hasImpactFactor) {
						row.append((String) null);
					}
				});
	}

}
