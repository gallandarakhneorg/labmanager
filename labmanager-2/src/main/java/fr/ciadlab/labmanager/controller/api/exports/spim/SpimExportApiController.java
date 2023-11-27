/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
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

package fr.ciadlab.labmanager.controller.api.exports.spim;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.io.od.OdfSpreadsheetHelper;
import fr.ciadlab.labmanager.io.od.OdfSpreadsheetHelper.TableContentHelper;
import fr.ciadlab.labmanager.io.od.OdfSpreadsheetHelper.TableContentRowHelper;
import fr.ciadlab.labmanager.io.od.OdfSpreadsheetHelper.TableHeaderHelper;
import fr.ciadlab.labmanager.io.od.OdfSpreadsheetHelper.TableHelper;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
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

/** REST Controller for exporting elements to the SPIM doctoral school.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@RestController
@CrossOrigin
public class SpimExportApiController extends AbstractApiController {

	private static final String NAME_COLUMN = "Nom"; //$NON-NLS-1$

	private static final String FIRST_COLUMN = "Prénom"; //$NON-NLS-1$

	private static final String POSITION_COLUMN = "Corps"; //$NON-NLS-1$

	private static final String HDR_COLUMN = "HDR"; //$NON-NLS-1$

	private static final String UNIVERSITY_COLUMN = "Etablissement de tutelle"; //$NON-NLS-1$

	private static final String EMERITUS_COLUMN = "Emerite"; //$NON-NLS-1$

	private static final String RH_TABLE_NAME = "CIAD - RH"; //$NON-NLS-1$

	private ResearchOrganizationService organizationService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param constants the constants of the app.
	 * @param organizationService the service for accessing the organizations.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public SpimExportApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ResearchOrganizationService organizationService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.organizationService = organizationService;
	}

	/**
	 * Export annual indicators to the SPIM requirements.
	 *
	 * @param organization the identifier or the name of the organization for which the idicators must be extracted.
	 * @param year the reference year.
	 * @param username the name of the logged-in user.
	 * @return the document for the publications.
	 * @throws Exception if it is impossible to redirect to the error page.
	 * @since 3.5
	 */
	@GetMapping(value = "/exportSpimAnnualIndicators")
	@ResponseBody
	public ResponseEntity<byte[]> exportSpimAnnualPublications(
			@RequestParam(required = true) String organization,
			@RequestParam(required = true) int year,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		readCredentials(username, "exportSpimAnnualIndicators", organization, Integer.valueOf(year)); //$NON-NLS-1$
		final ResearchOrganization organizationObj = getOrganizationWith(organization, this.organizationService);
		if (organizationObj == null) {
			throw new IllegalArgumentException("Organization not found for: " + organization); //$NON-NLS-1$
		}
		//
		final byte[] content;
		final MediaType mediaType;
		final String filenameExtension;
		try (final OdfSpreadsheetHelper ods = new OdfSpreadsheetHelper()) {
			getLogger().info("Generating SPIM indicators' spreadsheet for human resources"); //$NON-NLS-1$
			exportHumanResources(ods, organizationObj, year);
			getLogger().info("Generating spreadsheet bytes"); //$NON-NLS-1$
			content = ods.toByteArray(true);
			mediaType = ods.getMediaType();
			filenameExtension = ods.getFileExtension();
		}
		//
		BodyBuilder bb = ResponseEntity.ok().contentType(mediaType);
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
		bb = bb.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" //$NON-NLS-1$
				+ Constants.DEFAULT_SPIM_INDICATORS_ATTACHMENT_BASENAME
				+ "_" + simpleDateFormat.format(new Date()) + "." + filenameExtension + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return bb.body(content);
	}

	/** Export the humn resources in a spreadsheet page that corresponds to the SPIM standard.
	 * The columns of the Excel are:<ul>
	 * <li>Name</li>
	 * <li>Firstname</li>
	 * <li>Position</li>
	 * <li>HDR</li>
	 * <li>University</li>
	 * <li>Emerite</li>
	 * </ul>
	 * 
	 * @param document the output document.
	 * @param organization the organization for which the publications must be extracted.
	 * @param year the reference year that is used for filtering the publications.
	 * @throws Exception if the Excel cannot be generated.
	 */
	@SuppressWarnings("static-method")
	protected void exportHumanResources(OdfSpreadsheetHelper document, ResearchOrganization organization, int year) throws Exception {
		final TableHelper output = document.newTable(MessageFormat.format(RH_TABLE_NAME, Integer.valueOf(year)));
		//
		final TableHeaderHelper header = output.getHeader();
		header.appendColumn(NAME_COLUMN);
		header.appendColumn(FIRST_COLUMN);
		header.appendColumn(POSITION_COLUMN);
		header.appendColumn(HDR_COLUMN);
		header.appendColumn(UNIVERSITY_COLUMN);
		header.appendColumn(EMERITUS_COLUMN);
		//
		final LocalDate startDate = LocalDate.of(year, 1, 1);
		final LocalDate endDate = LocalDate.of(year, 12, 31);
		final TableContentHelper content = output.getContent();
		organization.getMemberships().stream()
			.filter(it -> it.isActiveIn(startDate, endDate) && !it.getMemberStatus().isExternalPosition()
					&& it.isPermanentPosition() && (it.getMemberStatus().isResearcher() || it.getMemberStatus().isTechnicalStaff()))
			.forEach(it -> {
				final TableContentRowHelper row = content.appendRow();
				row.append(it.getPerson().getLastName());
				row.append(it.getPerson().getFirstName());
				row.append(it.getMemberStatus().getFrenchAcronym());
				row.append(Boolean.valueOf(it.getMemberStatus().isHdrOwner()));
				final Optional<ResearchOrganization> employer = it.getPerson().getMemberships().stream()
						.filter(it0 -> it.getId() != it0.getId() && it0.isActiveIn(startDate, endDate))
						.map(it0 -> it0.getResearchOrganization())
						.filter(it0 -> it0.getType().isEmployer())
						.findAny();
				row.append(employer.isPresent() ? employer.get().getAcronymOrName() : null);
				row.append(Boolean.valueOf(it.getMemberStatus().isEmeritus()));
			});
	}

}
