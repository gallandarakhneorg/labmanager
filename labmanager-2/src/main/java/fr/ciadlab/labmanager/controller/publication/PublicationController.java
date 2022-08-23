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

package fr.ciadlab.labmanager.controller.publication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ciadlab.labmanager.Constants;
import fr.ciadlab.labmanager.controller.AbstractController;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.member.PersonComparator;
import fr.ciadlab.labmanager.entities.publication.JournalBasedPublication;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.io.bibtex.BibTeXConstants;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.io.od.OpenDocumentConstants;
import fr.ciadlab.labmanager.service.journal.JournalService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import fr.ciadlab.labmanager.service.publication.type.JournalPaperService;
import fr.ciadlab.labmanager.utils.RequiredFieldInForm;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

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
public class PublicationController extends AbstractController {

	private static final String DEFAULT_ENDPOINT = "publicationList"; //$NON-NLS-1$

	private PublicationService publicationService;

	private PersonService personService;

	private PersonComparator personComparator;

	private DownloadableFileManager fileManager;

	private JournalService journalService;

	private JournalPaperService journalPaperService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param publicationService the publication service.
	 * @param personService the person service.
	 * @param personComparator the comparator of persons.
	 * @param fileManager the manager of local files.
	 * @param journalService the tools for manipulating journals.
	 * @param journalPaperService the journal paper service.
	 */
	public PublicationController(
			@Autowired MessageSourceAccessor messages,
			@Autowired PublicationService publicationService,
			@Autowired PersonService personService,
			@Autowired PersonComparator personComparator,
			@Autowired DownloadableFileManager fileManager,
			@Autowired JournalService journalService,
			@Autowired JournalPaperService journalPaperService) {
		super(DEFAULT_ENDPOINT, messages);
		this.publicationService = publicationService;
		this.personService = personService;
		this.personComparator = personComparator;
		this.fileManager = fileManager;
		this.journalService = journalService;
		this.journalPaperService = journalPaperService;
	}

	/** Replies the model-view component for managing the publications.
	 * This endpoint is designed for the database management.
	 *
	 * @param username the login of the logged-in person.
	 * @return the model-view component.
	 * @see #showFrontPublicationList(Integer, Integer, Integer, Boolean)
	 */
	@GetMapping("/" + DEFAULT_ENDPOINT)
	public ModelAndView showBackPublicationList(
			@CurrentSecurityContext(expression="authentication?.name") String username) {
		final ModelAndView modelAndView = new ModelAndView(DEFAULT_ENDPOINT);
		initModelViewProperties(modelAndView, username);
		modelAndView.addObject("publications", this.publicationService.getAllPublications()); //$NON-NLS-1$
		modelAndView.addObject("uuid", generateUUID()); //$NON-NLS-1$
		return modelAndView;
	}

	/** Replies the list of publications for the given author.
	 * This function differs to {@link #showBackPublicationList()} because it is dedicated to
	 * the public front-end of the research organization. The function {@link #showBackPublicationList()}
	 * is more dedicated to the administration of the data-set.
	 * <p> This function may provide to the front-end the map of the person identifiers to
	 * their full names. The type of the map is: {@code Map&lt;Integer, String&gt;}.
	 *
	 * @param organization the identifier of the organization for which the publications must be exported.
	 * @param author the identifier of the author for who the publications must be exported.
	 * @param journal the identifier of the journal for which the publications must be exported.
	 * @param provideNames indicates if the model provides to the front-end the map from the identifiers to the full names.
	 * @return the model-view of the list of publications.
	 * @param username the login of the logged-in person.
	 * @see #showBackPublicationList()
	 * @see #exportJson(HttpServletResponse, List, Integer, Integer, Integer)
	 */
	@GetMapping("/showPublications")
	public ModelAndView showFrontPublicationList(
			@RequestParam(required = false, name = Constants.ORGANIZATION_ENDPOINT_PARAMETER) Integer organization,
			@RequestParam(required = false, name = Constants.AUTHOR_ENDPOINT_PARAMETER) Integer author,
			@RequestParam(required = false, name = Constants.JOURNAL_ENDPOINT_PARAMETER) Integer journal,
			@RequestParam(required = false, defaultValue = "true") Boolean provideNames,
			@CurrentSecurityContext(expression="authentication?.name") String username) {
		final ModelAndView modelAndView = new ModelAndView("showPublications"); //$NON-NLS-1$
		initModelViewProperties(modelAndView, username);
		if (provideNames == null || provideNames.booleanValue()) {
			final List<Person> persons = this.personService.getAllPersons();
			modelAndView.addObject("authorsMap", persons.parallelStream() //$NON-NLS-1$
					.collect(Collectors.toConcurrentMap(
							it -> Integer.valueOf(it.getId()),
							it -> it.getFullName())));
		}
		addUrlToPublicationListEndPoint(modelAndView, organization, author, journal);
		//
		final UriBuilderFactory factory = new DefaultUriBuilderFactory();
		modelAndView.addObject("endpoint_export_bibtex", //$NON-NLS-1$
				buildUri(factory, organization, author, journal, Constants.EXPORT_BIBTEX_ENDPOINT));
		//
		modelAndView.addObject("endpoint_export_odt", //$NON-NLS-1$
				buildUri(factory, organization, author, journal, Constants.EXPORT_ODT_ENDPOINT));
		//
		modelAndView.addObject("endpoint_export_html", //$NON-NLS-1$
				buildUri(factory, organization, author, journal, Constants.EXPORT_HTML_ENDPOINT));
		return modelAndView;
	}

	private static String buildUri(UriBuilderFactory factory, Integer organization, Integer author,
			Integer journal, String endpoint) {
		UriBuilder uriBuilder = factory.builder();
		uriBuilder = uriBuilder.path("/" + Constants.DEFAULT_SERVER_NAME + "/" + endpoint); //$NON-NLS-1$ //$NON-NLS-2$
		uriBuilder.queryParam(Constants.INATTACHMENT_ENDPOINT_PARAMETER, Boolean.TRUE);
		if (organization != null) {
			uriBuilder = uriBuilder.queryParam(Constants.ORGANIZATION_ENDPOINT_PARAMETER, organization);
		}
		if (author != null) {
			uriBuilder = uriBuilder.queryParam(Constants.AUTHOR_ENDPOINT_PARAMETER, author);
		}
		if (journal != null) {
			uriBuilder = uriBuilder.queryParam(Constants.JOURNAL_ENDPOINT_PARAMETER, journal);
		}
		return uriBuilder.build().toString();
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
	 * @param inAttachment indicates if the JSON is provided as attached document or not. By default, the value is
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
			bb = bb.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + Constants.DEFAULT_ATTACHMENT_BASENAME + ".html\""); //$NON-NLS-1$ //$NON-NLS-2$
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
	 * @param inAttachment indicates if the JSON is provided as attached document or not. By default, the value is
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
			bb = bb.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + Constants.DEFAULT_ATTACHMENT_BASENAME + ".bib\""); //$NON-NLS-1$ //$NON-NLS-2$
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
	 * @param inAttachment indicates if the JSON is provided as attached document or not. By default, the value is
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
			bb = bb.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + Constants.DEFAULT_ATTACHMENT_BASENAME + ".odt\""); //$NON-NLS-1$ //$NON-NLS-2$
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
			bb = bb.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + Constants.DEFAULT_ATTACHMENT_BASENAME + ".json\""); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return bb.body(content);
	}

	/** Replies the statistics for the publications and for the author with the given identifier.
	 *
	 * @param identifier the identifier of the author. If it is not provided, all the publications are considered.
	 * @return the model-view with the statistics.
	 */
	@GetMapping("/publicationStats")
	public ModelAndView showPublicationsStats(@RequestParam(required = false, name = Constants.ID_ENDPOINT_PARAMETER) Integer identifier) {
		final ModelAndView modelAndView = new ModelAndView("publicationStats"); //$NON-NLS-1$

		final List<Publication> publications;
		if (identifier == null) {
			publications = this.publicationService.getAllPublications();
		} else {
			publications = this.publicationService.getPublicationsByPersonId(identifier.intValue());
		}

		final Map<Integer, PublicationsStat> statsPerYear = new TreeMap<>();
		final PublicationsStat globalStats = new PublicationsStat(Integer.MIN_VALUE);

		for (final Publication p : publications) {
			final Integer y = Integer.valueOf(p.getPublicationYear());
			final PublicationsStat stats = statsPerYear.computeIfAbsent(y,
					it -> new PublicationsStat(it.intValue()));
			stats.increment(p.getType(), p.isRanked(), 1);
			globalStats.increment(p.getType(), p.isRanked(), 1);
		}

		modelAndView.addObject("stats", statsPerYear); //$NON-NLS-1$
		modelAndView.addObject("globalStats", globalStats); //$NON-NLS-1$
		modelAndView.addObject("uuid", generateUUID()); //$NON-NLS-1$
		return modelAndView;
	}

	/** Show the editor for a publication. This editor permits to create or to edit apublication.
	 *
	 * @param publication the identifier of the publication to edit. If it is {@code null}, the endpoint
	 *     is dedicated to the creation of a publication.
	 * @param success flag that indicates the previous operation was a success.
	 * @param failure flag that indicates the previous operation was a failure.
	 * @param message the message that is associated to the state of the previous operation.
	 * @param username the login of the logged-in person.
	 * @return the model-view object.
	 * @throws IOException if there is some internal IO error when building the form's data.
	 */
	@GetMapping(value = "/" + Constants.PUBLICATION_EDITING_ENDPOINT)
	public ModelAndView showPublicationEditor(
			@RequestParam(required = false, name = "id") Integer publication,
			@RequestParam(required = false, defaultValue = "false") Boolean success,
			@RequestParam(required = false, defaultValue = "false") Boolean failure,
			@RequestParam(required = false) String message,
			@CurrentSecurityContext(expression="authentication?.name") String username) throws IOException {
		final ModelAndView modelAndView = new ModelAndView("publicationEditor"); //$NON-NLS-1$
		//
		final Publication publicationObj;
		if (publication != null && publication.intValue() != 0) {
			publicationObj = this.publicationService.getPublicationById(publication.intValue());
			if (publicationObj == null) {
				throw new IllegalArgumentException("Publication not found: " + publication); //$NON-NLS-1$
			}
		} else {
			publicationObj = null;
		}
		//
		initModelViewProperties(modelAndView, username, success, failure, message);
		if (publicationObj != null) {
			// Provide the attributes of the publication
			publicationObj.forEachAttribute((attrName, attrValue) -> {
				// Specific treatment of fields that are considered as shared among multiple publication types
				if ("reportNumber".equals(attrName) || "patentNumber".equals(attrName) || "documentNumber".equals(attrName)) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					modelAndView.addObject("shared_number", attrValue); //$NON-NLS-1$
				} else if ("reportType".equals(attrName) || "patentType".equals(attrName)) {  //$NON-NLS-1$//$NON-NLS-2$
					modelAndView.addObject("shared_documentType", attrValue); //$NON-NLS-1$
				} else {
					modelAndView.addObject("shared_" + attrName, attrValue); //$NON-NLS-1$
				}
			});
		}

		// Create the mapping from type name to input field
		final Map<String, String> requiredFields = new TreeMap<>();
		final Map<String, Set<String>> typeFieldMapping = new HashMap<>();
		for (final PublicationType ptype : PublicationType.values()) {
			final Class<?> ctype = ptype.getInstanceType();
			final String type = ctype.getSimpleName();
			typeFieldMapping.computeIfAbsent(type, it -> {
				return buildHtmlElementMapping(ctype, requiredFields);
			});
		}
		modelAndView.addObject("typeFieldMapping", typeFieldMapping); //$NON-NLS-1$
		modelAndView.addObject("requiredFields", requiredFields); //$NON-NLS-1$

		// Special injection of attributes
		if (publicationObj != null) {
			// Provide more information about uploaded files
			final Object pdfPath = modelAndView.getModel().get("shared_pathToDownloadablePDF"); //$NON-NLS-1$
			if (pdfPath != null && !Strings.isNullOrEmpty(pdfPath.toString())) {
				modelAndView.addObject("pathToDownloadablePDF_basename", FileSystem.largeBasename(pdfPath.toString())); //$NON-NLS-1$
				modelAndView.addObject("pathToDownloadablePDF_picture", this.fileManager.makePdfPictureFilename(publicationObj.getId())); //$NON-NLS-1$
			}

			final Object awardPath = modelAndView.getModel().get("shared_pathToDownloadableAwardCertificate"); //$NON-NLS-1$
			if (awardPath != null && !Strings.isNullOrEmpty(awardPath.toString())) {
				modelAndView.addObject("pathToDownloadableAwardCertificate_basename", FileSystem.largeBasename(awardPath.toString())); //$NON-NLS-1$
				modelAndView.addObject("pathToDownloadableAwardCertificate_picture", this.fileManager.makeAwardPictureFilename(publicationObj.getId())); //$NON-NLS-1$
			}

			// Provide a YEAR-MONTH publication date
			if (publicationObj.getPublicationDate() != null) {
				final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM"); //$NON-NLS-1$
				modelAndView.addObject("dateYearMonth", publicationObj.getPublicationDate().format(formatter)); //$NON-NLS-1$
			}
			if (publicationObj instanceof JournalBasedPublication) {
				final JournalBasedPublication jbp = (JournalBasedPublication) publicationObj;
				final Journal journal = jbp.getJournal();
				if (journal != null) {
					modelAndView.addObject("journalIdentifier", Integer.valueOf(journal.getId())); //$NON-NLS-1$
				}
			}
		}

		// List of all the authors
		modelAndView.addObject("allPersons", this.personService.getAllPersons().stream().sorted(this.personComparator).iterator()); //$NON-NLS-1$

		// Provide the list of journals
		modelAndView.addObject("journals", this.journalService.getAllJournals()); //$NON-NLS-1$
		modelAndView.addObject("publication", publicationObj); //$NON-NLS-1$
		modelAndView.addObject("defaultPublicationType", PublicationType.INTERNATIONAL_JOURNAL_PAPER); //$NON-NLS-1$
		modelAndView.addObject("formActionUrl", "/" + Constants.PUBLICATION_SAVING_ENDPOINT); //$NON-NLS-1$ //$NON-NLS-2$
		//
		return modelAndView;
	}

	private static Set<String> buildHtmlElementMapping(Class<?> jtype, Map<String, String> required) {
		final Set<String> elements = new TreeSet<>();
		final boolean isJournalType = JournalBasedPublication.class.isAssignableFrom(jtype);
		if (isJournalType) {
			elements.add("dynamic-form-group-journal"); //$NON-NLS-1$
		} else {
			elements.add("dynamic-form-group-isbn"); //$NON-NLS-1$
			elements.add("dynamic-form-group-issn"); //$NON-NLS-1$
		}
		for (final Method method : jtype.getDeclaredMethods()) {
			final String bname = method.getName();
			if (bname.startsWith("get") && method.getParameterCount() == 0 && String.class.equals(method.getReturnType())) { //$NON-NLS-1$
				try {
					String attrName = bname.substring(3);
					final String setterName = "set" + attrName; //$NON-NLS-1$
					jtype.getDeclaredMethod(setterName, String.class);
					final String fieldName = StringUtils.uncapitalize(attrName);
					attrName = attrName.replaceAll("([A-Z]+)", "-$1"); //$NON-NLS-1$ //$NON-NLS-2$
					attrName = attrName.toLowerCase();
					if (attrName.endsWith("-number") && !"-chapter-number".equals(attrName)) { //$NON-NLS-1$ //$NON-NLS-2$
						attrName = "-number"; //$NON-NLS-1$
					} else if (attrName.endsWith("-type") ) { //$NON-NLS-1$
						attrName = "-document-type"; //$NON-NLS-1$
					} else if ("-isbn".equals(attrName) || "-issn".equals(attrName)) { //$NON-NLS-1$ //$NON-NLS-2$
						attrName = null;
					}
					if (attrName != null) {
						final String htmlElement = "dynamic-form-group" + attrName; //$NON-NLS-1$
						elements.add(htmlElement);
						if (method.isAnnotationPresent(RequiredFieldInForm.class)) {
							required.put(htmlElement, fieldName);
						}
					}
				} catch (Throwable ex) {
					//
				}
			}
		}
		return elements;
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
	 * @param username the login of the logged-in person.
	 * @param response the HTTP response to the client.
	 */
	@PostMapping(value = "/" + Constants.PUBLICATION_SAVING_ENDPOINT)
	public void savePublication(
			@RequestParam(required = false) Integer publication,
			@RequestParam(required = false) List<String> authors,
			@RequestParam(required = false) MultipartFile pathToDownloadablePDF,
			@RequestParam(required = false) MultipartFile pathToDownloadableAwardCertificate,
			@RequestParam Map<String, String> allParameters,
			@CurrentSecurityContext(expression="authentication?.name") String username,
			HttpServletResponse response) {
		if (isLoggedUser(username).booleanValue()) {
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
				final boolean newPublication;
				if (publication == null) {
					newPublication = true;
					optPublication = this.publicationService.createPublicationFromMap(allParameters,
							authors, pathToDownloadablePDF, pathToDownloadableAwardCertificate);
				} else {
					newPublication = false;
					optPublication = this.publicationService.updatePublicationFromMap(publication.intValue(), allParameters,
							authors, pathToDownloadablePDF, pathToDownloadableAwardCertificate);
				}
				if (optPublication.isEmpty()) {
					throw new IllegalStateException("Publication not found"); //$NON-NLS-1$
				}
				//
				redirectSuccessToEndPoint(response, Constants.PUBLICATION_EDITING_ENDPOINT,
						getMessage(
								newPublication ? "publicationController.AdditionSuccess" //$NON-NLS-1$
										: "publicationController.EditionSuccess", //$NON-NLS-1$
										optPublication.get().getTitle(),	
										Integer.valueOf(optPublication.get().getId())));
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
				redirectFailureToEndPoint(response, Constants.PUBLICATION_EDITING_ENDPOINT, ex.getLocalizedMessage());
			}
		} else {
			redirectFailureToEndPoint(response, Constants.PUBLICATION_EDITING_ENDPOINT, getMessage("all.notLogged")); //$NON-NLS-1$
		}
	}

	/** Delete a publication from the database.
	 *
	 * @param publication the identifier of the publication.
	 * @param username the login of the logged-in person.
	 * @return the HTTP response.
	 */
	@DeleteMapping("/deletePublication")
	public ResponseEntity<Integer> deletePublication(
			@RequestParam Integer publication,
			@CurrentSecurityContext(expression="authentication?.name") String username) {
		if (isLoggedUser(username).booleanValue()) {
			try {
				if (publication == null || publication.intValue() == 0) {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				this.publicationService.removePublication(publication.intValue(), true);
				return new ResponseEntity<>(publication, HttpStatus.OK);
			} catch (Exception ex) {
				getLogger().error(ex.getLocalizedMessage(), ex);
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	/** Show the view for importing BibTeX files.
	 *
	 * @param username the login of the logged-in person.
	 * @param success flag that indicates the previous operation was a success.
	 * @param failure flag that indicates the previous operation was a failure.
	 * @param message the message that is associated to the state of the previous operation.
	 * @return the model-view object.
	 * @throws IOException if there is some internal IO error when building the form's data.
	 */
	@GetMapping(value = "/" + Constants.IMPORT_BIBTEX_VIEW_ENDPOINT)
	public ModelAndView showBibTeXImporter(
			@RequestParam(required = false, defaultValue = "false") Boolean success,
			@RequestParam(required = false, defaultValue = "false") Boolean failure,
			@RequestParam(required = false) String message,
			@CurrentSecurityContext(expression="authentication?.name") String username) throws IOException {
		final ModelAndView modelAndView = new ModelAndView("importBibTeX"); //$NON-NLS-1$
		//
		initModelViewProperties(modelAndView, username, success, failure, message);
		modelAndView.addObject("bibtexJsonActionUrl", "/" + Constants.GET_JSON_FROM_BIBTEX_ENDPOINT); //$NON-NLS-1$ //$NON-NLS-2$
		modelAndView.addObject("formActionUrl", "/" + Constants.SAVE_BIBTEX_ENDPOINT); //$NON-NLS-1$ //$NON-NLS-2$
		//
		return modelAndView;
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
			throw new IllegalArgumentException(getMessage("publicationController.NoBibTeXSource")); //$NON-NLS-1$
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
