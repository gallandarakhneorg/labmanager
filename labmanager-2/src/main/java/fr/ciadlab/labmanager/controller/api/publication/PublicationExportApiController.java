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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import fr.ciadlab.labmanager.AbstractComponent;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.io.bibtex.BibTeXConstants;
import fr.ciadlab.labmanager.io.od.OpenDocumentConstants;
import fr.ciadlab.labmanager.service.journal.JournalService;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import fr.ciadlab.labmanager.service.publication.type.JournalPaperService;
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

/** REST Controller for exports of publications.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@RestController
@CrossOrigin
public class PublicationExportApiController extends AbstractComponent {

	private PublicationService publicationService;

	private JournalService journalService;

	private JournalPaperService journalPaperService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param publicationService the publication service.
	 * @param journalService the tools for manipulating journals.
	 * @param journalPaperService the journal paper service.
	 */
	public PublicationExportApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired PublicationService publicationService,
			@Autowired JournalService journalService,
			@Autowired JournalPaperService journalPaperService) {
		super(messages);
		this.publicationService = publicationService;
		this.journalService = journalService;
		this.journalPaperService = journalPaperService;
	}

	private <T> T export(List<Integer> identifiers, Integer organization, Integer author,
			Integer journal, Boolean nameHighlight, Boolean color, Boolean downloadButtons, Boolean exportButtons, 
			Boolean editButtons, Boolean deleteButtons, Boolean htmlAuthors, Boolean htmlPublicationDetails,
			Boolean htmlTypeAndCategory, ExporterCallback<T> callback) throws Exception {
		// Prepare the exporter
		final ExporterConfigurator configurator = new ExporterConfigurator(this.journalService);
		if (nameHighlight != null && !nameHighlight.booleanValue()) {
			configurator.disableSelectedPersonFormat();
			configurator.disableResearcherFormat();
			configurator.disablePostdocEngineerFormat();
			configurator.disablePhDStudentFormat();
		}
		if (color != null && !color.booleanValue()) {
			configurator.disableTitleColor();
		}
		if (downloadButtons != null && !downloadButtons.booleanValue()) {
			configurator.disableDownloadButtons();
		}
		if (exportButtons != null && !exportButtons.booleanValue()) {
			configurator.disableExportButtons();
		}
		if (editButtons != null && !editButtons.booleanValue()) {
			configurator.disableEditButtons();
		}
		if (deleteButtons != null && !deleteButtons.booleanValue()) {
			configurator.disableDeleteButtons();
		}
		if (htmlAuthors != null && !htmlAuthors.booleanValue()) {
			configurator.disableFormattedAuthorList();
		}
		if (htmlPublicationDetails != null && !htmlPublicationDetails.booleanValue()) {
			configurator.disableFormattedPublicationDetails();
		}
		if (htmlTypeAndCategory != null && !htmlTypeAndCategory.booleanValue()) {
			configurator.disableTypeAndCategoryLabels();
		}
		//
		if (organization != null) {
			configurator.selectOrganization(it -> it.getId() == organization.intValue());
			configurator.addUriQueryParam(Constants.ORGANIZATION_ENDPOINT_PARAMETER, organization);
		}
		if (author != null) {
			configurator.selectPerson(it -> it.getId() == author.intValue());
			configurator.addUriQueryParam(Constants.AUTHOR_ENDPOINT_PARAMETER, author);
		}
		if (journal != null) {
			configurator.addUriQueryParam(Constants.JOURNAL_ENDPOINT_PARAMETER, journal);
		}
		// Get the list of publications
		final List<Publication> pubs;
		if (identifiers == null || identifiers.isEmpty()) {
			if (author != null) {
				pubs = this.publicationService.getPublicationsByPersonId(author.intValue());
			} else if (organization != null) {
				pubs = this.publicationService.getPublicationsByOrganizationId(organization.intValue());
			} else if (journal != null) {
				pubs = this.journalPaperService.getJournalPapersByJournalId(journal.intValue());
			} else {
				pubs = this.publicationService.getAllPublications();
			}
		} else {
			pubs = this.publicationService.getPublicationsByIds(identifiers);
		}
		// Export
		return callback.export(pubs, configurator);
	}

	/**
	 * Export publications to HTML.
	 * This function takes one of the following parameters:<ul>
	 * <li>{@code identifiers}: a list of publication identifiers to export.</li>
	 * <li>{@code organization}: the identifier of a research organization for which the publications should be exported.</li>
	 * <li>{@code author}: the identifier of an author.</li>
	 * <li>{@code journal}: the identifier of a journal.</li>
	 * </ul>
	 * <p>If both author and organization identifiers are provided, the publications of the authors are prioritized.
	 *
	 * @param identifiers the array of publication identifiers that should be exported.
	 * @param organization the identifier of the organization for which the publications must be exported.
	 * @param author the identifier of the author for who the publications must be exported.
	 * @param journal the identifier of the journal for which the publications must be exported.
	 * @param nameHighlight indicates if the names of the authors should be highlighted depending on their status in the organization. 
	 *     Providing this identifier will have an effect on the formatting of the authors' names.
	 * @param color indicates if the colors are enabled for producing the HTML output. 
	 * @param inAttachment indicates if the HTML is provided as attached document or not. By default, the value is
	 *     {@code false}.
	 * @return the HTML description of the publications.
	 * @throws Exception if it is impossible to redirect to the error page.
	 */
	@GetMapping(value = "/" + Constants.EXPORT_HTML_ENDPOINT)
	@ResponseBody
	public ResponseEntity<String> exportHtml(
			@RequestParam(name = Constants.ID_ENDPOINT_PARAMETER, required = false) List<Integer> identifiers,
			@RequestParam(required = false, name = Constants.ORGANIZATION_ENDPOINT_PARAMETER) Integer organization,
			@RequestParam(required = false, name = Constants.AUTHOR_ENDPOINT_PARAMETER) Integer author,
			@RequestParam(required = false, name = Constants.JOURNAL_ENDPOINT_PARAMETER) Integer journal,
			@RequestParam(required = false, defaultValue = "true") Boolean nameHighlight,
			@RequestParam(required = false, defaultValue = "true") Boolean color,
			@RequestParam(required = false, defaultValue = "false", name = Constants.INATTACHMENT_ENDPOINT_PARAMETER) Boolean inAttachment) throws Exception {
		final ExporterCallback<String> cb = (pubs, configurator) -> this.publicationService.exportHtml(pubs, configurator);
		final String content = export(identifiers, organization, author, journal, nameHighlight, color,
				Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, cb);
		BodyBuilder bb = ResponseEntity.ok().contentType(MediaType.TEXT_HTML);
		if (inAttachment != null && inAttachment.booleanValue()) {
			bb = bb.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + Constants.DEFAULT_PUBLICATION_ATTACHMENT_BASENAME + ".html\""); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return bb.body(content);
	}

	/**
	 * Export publications to BibTeX.
	 * This function takes one of the following parameters:<ul>
	 * <li>{@code identifiers}: a list of publication identifiers to export.</li>
	 * <li>{@code organization}: the identifier of a research organization for which the publications should be exported.</li>
	 * <li>{@code author}: the identifier of an author.</li>
	 * <li>{@code journal}: the identifier of a journal.</li>
	 * </ul>
	 * <p>If both author and organization identifiers are provided, the publications of the authors are prioritized.
	 *
	 * @param identifiers the array of publication identifiers that should be exported.
	 * @param organization the identifier of the organization for which the publications must be exported.
	 * @param author the identifier of the author for who the publications must be exported.
	 * @param journal the identifier of the journal for which the publications must be exported.
	 * @param inAttachment indicates if the BibTeX is provided as attached document or not. By default, the value is
	 *     {@code false}.
	 * @return the BibTeX description of the publications.
	 * @throws Exception if it is impossible to redirect to the error page.
	 */
	@GetMapping(value = "/" + Constants.EXPORT_BIBTEX_ENDPOINT)
	@ResponseBody
	public ResponseEntity<String> exportBibTeX(
			@RequestParam(name = Constants.ID_ENDPOINT_PARAMETER, required = false) List<Integer> identifiers,
			@RequestParam(required = false, name = Constants.ORGANIZATION_ENDPOINT_PARAMETER) Integer organization,
			@RequestParam(required = false, name = Constants.AUTHOR_ENDPOINT_PARAMETER) Integer author,
			@RequestParam(required = false, name = Constants.JOURNAL_ENDPOINT_PARAMETER) Integer journal,
			@RequestParam(required = false, defaultValue = "false", name = Constants.INATTACHMENT_ENDPOINT_PARAMETER) Boolean inAttachment) throws Exception {
		final ExporterCallback<String> cb = (pubs, configurator) -> this.publicationService.exportBibTeX(pubs, configurator);
		final String content = export(identifiers, organization, author, journal, Boolean.FALSE, Boolean.FALSE,
				Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, cb);
		BodyBuilder bb = ResponseEntity.ok().contentType(BibTeXConstants.MIME_TYPE);
		if (inAttachment != null && inAttachment.booleanValue()) {
			bb = bb.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + Constants.DEFAULT_PUBLICATION_ATTACHMENT_BASENAME + ".bib\""); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return bb.body(content);
	}

	/**
	 * Export publications to Open Document Text (ODT).
	 * This function takes one of the following parameters:<ul>
	 * <li>{@code identifiers}: a list of publication identifiers to export.</li>
	 * <li>{@code organization}: the identifier of a research organization for which the publications should be exported.</li>
	 * <li>{@code author}: the identifier of an author.</li>
	 * <li>{@code journal}: the identifier of a journal.</li>
	 * </ul>
	 * <p>If both author and organization identifiers are provided, the publications of the authors are prioritized.
	 *
	 * @param identifiers the array of publication identifiers that should be exported.
	 * @param organization the identifier of the organization for which the publications must be exported.
	 * @param author the identifier of the author for who the publications must be exported.
	 * @param journal the identifier of the journal for which the publications must be exported.
	 * @param nameHighlight indicates if the names of the authors should be highlighted depending on their status in the organization. 
	 *     Providing this identifier will have an effect on the formatting of the authors' names.
	 * @param color indicates if the colors are enabled for producing the ODT output. 
	 * @param inAttachment indicates if the ODT is provided as attached document or not. By default, the value is
	 *     {@code false}.
	 * @return the OpenDocument description of the publications.
	 * @throws Exception if it is impossible to redirect to the error page.
	 */
	@GetMapping(value = "/" + Constants.EXPORT_ODT_ENDPOINT)
	@ResponseBody
	public ResponseEntity<byte[]> exportOpenDocumentText(
			@RequestParam(name = Constants.ID_ENDPOINT_PARAMETER, required = false) List<Integer> identifiers,
			@RequestParam(required = false, name = Constants.ORGANIZATION_ENDPOINT_PARAMETER) Integer organization,
			@RequestParam(required = false, name = Constants.AUTHOR_ENDPOINT_PARAMETER) Integer author,
			@RequestParam(required = false, name = Constants.JOURNAL_ENDPOINT_PARAMETER) Integer journal,
			@RequestParam(required = false, defaultValue = "true") Boolean nameHighlight,
			@RequestParam(required = false, defaultValue = "true") Boolean color,
			@RequestParam(required = false, defaultValue = "false", name = Constants.INATTACHMENT_ENDPOINT_PARAMETER) Boolean inAttachment) throws Exception {
		final ExporterCallback<byte[]> cb = (pubs, configurator) -> this.publicationService.exportOdt(pubs, configurator);
		final byte[] content = export(identifiers, organization, author, journal, nameHighlight, color,
				Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, cb);
		BodyBuilder bb = ResponseEntity.ok().contentType(OpenDocumentConstants.ODT_MIME_TYPE);
		if (inAttachment != null && inAttachment.booleanValue()) {
			bb = bb.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + Constants.DEFAULT_PUBLICATION_ATTACHMENT_BASENAME + ".odt\""); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return bb.body(content);
	}

	/**
	 * Export publications to JSON.
	 * This function takes one of the following parameters:<ul>
	 * <li>{@code identifiers}: a list of publication identifiers to export.</li>
	 * <li>{@code organization}: the identifier of a research organization for which the publications should be exported.</li>
	 * <li>{@code author}: the identifier of an author.</li>
	 * <li>{@code journal}: the identifier of a journal.</li>
	 * </ul>
	 * <p>If both author and organization identifiers are provided, the publications of the authors are prioritized.
	 * <p>This function and {@link #getPublicationData(String, Integer)} differ
	 * from the structure of the JSON output. This function provides for each publication a map with the given pairs:<ul>
	 * <li>{@code "data"}: the value is the JSON representation of the publication.</li>
	 * <li>{@code "html/download"}: an array of HTML codes that enable to download things related to the publication.</li>
	 * <li>{@code "html/export"}: an array of HTML codes that enable to export things related to the publication.</li>
	 * <li>{@code "html/edit"}: an HTML code that enable to edit the publication.</li>
	 * <li>{@code "html/delete"}: an HTML code that enable to delete the publication.</li>
	 * </ul>
	 *
	 * @param identifiers the array of publication identifiers that should be exported.
	 * @param organization the identifier of the organization for which the publications must be exported.
	 * @param author the identifier of the author for who the publications must be exported.
	 * @param journal the identifier of the journal for which the publications must be exported.
	 * @param forAjax indicates if the JSON is provided to AJAX. By default, the value is
	 *     {@code false}. If the JSON is provided to AJAX, the data is included into the root key {@code data} that is expected by AJAX.
	 *     If this parameter is evaluated to {@code true}, the parameter {@code inAttachment} is ignored.
	 * @param inAttachment indicates if the JSON is provided as attached document or not. By default, the value is
	 *     {@code false}.
	 *     If the parameter {@code forAjax} is evaluated to {@code true}, this parameter is ignored.
	 * @param username the name of the logged-in user.
	 * @return the JSON description of the publications.
	 * @throws Exception if it is impossible to redirect to the error page.
	 * @see #getPublicationData(String, Integer)
	 */
	@GetMapping(value = "/" + Constants.EXPORT_JSON_ENDPOINT)
	@ResponseBody
	public ResponseEntity<String> exportJson(
			@RequestParam(name = Constants.ID_ENDPOINT_PARAMETER, required = false) List<Integer> identifiers,
			@RequestParam(required = false, name = Constants.ORGANIZATION_ENDPOINT_PARAMETER) Integer organization,
			@RequestParam(required = false, name = Constants.AUTHOR_ENDPOINT_PARAMETER) Integer author,
			@RequestParam(required = false, name = Constants.JOURNAL_ENDPOINT_PARAMETER) Integer journal,
			@RequestParam(required = false, defaultValue = "false", name = Constants.FORAJAX_ENDPOINT_PARAMETER) Boolean forAjax,
			@RequestParam(required = false, defaultValue = "false", name = Constants.INATTACHMENT_ENDPOINT_PARAMETER) Boolean inAttachment,
			@CurrentSecurityContext(expression="authentication?.name") String username) throws Exception {
		final boolean isAjax = forAjax != null && forAjax.booleanValue();
		final Boolean isAjaxObj = Boolean.valueOf(isAjax);
		final Boolean isLoggingIn = Boolean.valueOf(isAjax && !Strings.isNullOrEmpty(username));
		final boolean isAttachment = !isAjax && inAttachment != null && inAttachment.booleanValue();
		final ExporterCallback<String> cb = (pubs, configurator) -> {
			if (isAjax) {
				return this.publicationService.exportJson(pubs, configurator, "data"); //$NON-NLS-1$
			}
			return this.publicationService.exportJson(pubs, configurator);
		};
		final String content = export(identifiers, organization, author, journal, isAjaxObj, Boolean.FALSE,
				isAjaxObj, isAjaxObj, isLoggingIn, isLoggingIn, isAjaxObj, isAjaxObj, isAjaxObj, cb);
		BodyBuilder bb = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON);
		if (isAttachment) {
			bb = bb.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + Constants.DEFAULT_PUBLICATION_ATTACHMENT_BASENAME + ".json\""); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return bb.body(content);
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

	/** Exporter callback.
	 * 
	 * @param <T> the type of data that is the result of an export.
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private interface ExporterCallback<T> {

		/** Do the export.
		 * 
		 * @param identifiers the identifiers.
		 * @param configurator the exporter configuration.
		 * @return the export result.
		 * @throws Exception if the export cannot be done.
		 */
		T export(Iterable<? extends Publication> identifiers, ExporterConfigurator configurator) throws Exception;

	}

}
