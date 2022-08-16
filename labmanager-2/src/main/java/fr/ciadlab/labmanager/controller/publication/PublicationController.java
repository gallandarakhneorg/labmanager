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

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import fr.ciadlab.labmanager.Constants;
import fr.ciadlab.labmanager.controller.AbstractController;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.io.bibtex.BibTeX;
import fr.ciadlab.labmanager.io.bibtex.BibTeXConstants;
import fr.ciadlab.labmanager.io.od.OpenDocumentConstants;
import fr.ciadlab.labmanager.service.journal.JournalService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.publication.PrePublicationFactory;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import fr.ciadlab.labmanager.service.publication.type.JournalPaperService;
import fr.ciadlab.labmanager.utils.ViewFactory;
import fr.ciadlab.labmanager.utils.files.DownloadableFileManager;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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

	private PrePublicationFactory prePublicationFactory;

	private PublicationService publicationService;

	private PersonService personService;

	private DownloadableFileManager fileManager;

	private PersonNameParser nameParser;

	private BibTeX bibtex;

	private JournalService journalService;

	private ViewFactory viewFactory;

	private JournalPaperService journalPaperService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param prePublicationFactory the factory of pre-publications.
	 * @param publicationService the publication service.
	 * @param personService the person service.
	 * @param fileManager the manager of local files.
	 * @param nameParser the parser of a person name.
	 * @param bibtex the tools for manipulating BibTeX data.
	 * @param viewFactory the factory of view.
	 * @param journalService the tools for manipulating journals.
	 * @param bookService the book service.
	 * @param bookChapterService the book chapter service.
	 * @param conferencePaperService the conference paper service.
	 * @param journalEditionService the journal edition service.
	 * @param journalPaperService the journal paper service.
	 * @param keyNoteService the service for keynotes.
	 * @param miscDocumentService the service for misc documents.
	 * @param patentService the service for patents.
	 * @param reportService the service for reports.
	 * @param thesisService the service for theses.
	 */
	public PublicationController(
			@Autowired PrePublicationFactory prePublicationFactory,
			@Autowired PublicationService publicationService,
			@Autowired PersonService personService,
			@Autowired DownloadableFileManager fileManager,
			@Autowired PersonNameParser nameParser,
			@Autowired BibTeX bibtex,
			@Autowired ViewFactory viewFactory,
			@Autowired JournalService journalService,
			@Autowired JournalPaperService journalPaperService) {
		super(DEFAULT_ENDPOINT);
		this.prePublicationFactory = prePublicationFactory;
		this.publicationService = publicationService;
		this.personService = personService;
		this.fileManager = fileManager;
		this.nameParser = nameParser;
		this.bibtex = bibtex;
		this.viewFactory = viewFactory;
		this.journalService = journalService;
		this.journalPaperService = journalPaperService;
	}

	/** Replies the model-view component for managing the publications.
	 * This endpoint is designed for the database management.
	 *
	 * @return the model-view component.
	 * @see #showFrontPublicationList(Integer, Integer, Integer, Boolean)
	 */
	@GetMapping("/" + DEFAULT_ENDPOINT)
	public ModelAndView showBackPublicationList() {
		final ModelAndView modelAndView = new ModelAndView(DEFAULT_ENDPOINT);
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
	 * @see #showBackPublicationList()
	 * @see #exportJson(HttpServletResponse, List, Integer, Integer, Integer)
	 */
	@GetMapping("/showPublications")
	public ModelAndView showFrontPublicationList(
			@RequestParam(required = false, name = Constants.ORGANIZATION_ENDPOINT_PARAMETER) Integer organization,
			@RequestParam(required = false, name = Constants.AUTHOR_ENDPOINT_PARAMETER) Integer author,
			@RequestParam(required = false, name = Constants.JOURNAL_ENDPOINT_PARAMETER) Integer journal,
			@RequestParam(required = false, defaultValue = "true") Boolean provideNames) {
		final ModelAndView modelAndView = new ModelAndView("showPublications"); //$NON-NLS-1$
		if (provideNames == null || provideNames.booleanValue()) {
			final List<Person> persons = this.personService.getAllPersons();
			modelAndView.addObject("authorsMap", persons.parallelStream() //$NON-NLS-1$
					.collect(Collectors.toConcurrentMap(
							it -> Integer.valueOf(it.getId()),
							it -> it.getFullName())));
		}
		addUrlToPublicationListEndPoint(modelAndView, organization, author, journal);
		modelAndView.addObject("uuid", generateUUID()); //$NON-NLS-1$
		modelAndView.addObject("endpoint_export_bibtex", "/" + Constants.DEFAULT_SERVER_NAME + "/" + Constants.EXPORT_BIBTEX_ENDPOINT ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		modelAndView.addObject("endpoint_export_odt", "/" + Constants.DEFAULT_SERVER_NAME + "/" + Constants.EXPORT_ODT_ENDPOINT ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		modelAndView.addObject("endpoint_export_html", "/" + Constants.DEFAULT_SERVER_NAME + "/" + Constants.EXPORT_HTML_ENDPOINT ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return modelAndView;
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
		final ExporterConfigurator configurator = new ExporterConfigurator();
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
		if (organization != null) {
			configurator.selectOrganization(it -> it.getId() == organization.intValue());
		}
		if (author != null) {
			configurator.selectPerson(it -> it.getId() == author.intValue());
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
			@RequestParam(required = false, defaultValue = "false") Boolean inAttachment) throws Exception {
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
			@RequestParam(required = false, defaultValue = "false") Boolean inAttachment) throws Exception {
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
			@RequestParam(required = false, defaultValue = "false") Boolean inAttachment) throws Exception {
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
			@RequestParam(required = false, defaultValue = "false") Boolean inAttachment) throws Exception {
		final boolean isAjax = forAjax != null && forAjax.booleanValue();
		final Boolean isAjaxObj = Boolean.valueOf(isAjax);
		final boolean isAttachment = !isAjax && inAttachment != null && inAttachment.booleanValue();
		final ExporterCallback<String> cb = (pubs, configurator) -> {
			if (isAjax) {
				return this.publicationService.exportJson(pubs, configurator, "data"); //$NON-NLS-1$
			}
			return this.publicationService.exportJson(pubs, configurator);
		};
		final String content = export(identifiers, organization, author, journal, isAjaxObj, Boolean.FALSE,
				isAjaxObj, isAjaxObj, isAjaxObj, isAjaxObj, isAjaxObj, isAjaxObj, isAjaxObj, cb);
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

	//	/** Redirect to the publication list with a "success" state.
	//	 * This function is usually invoked after the success of an operation.
	//	 *
	//	 * @param response the HTTP response.
	//	 * @throws Exception if the redirection cannot be done.
	//	 */
	//	@SuppressWarnings("static-method")
	//	protected void redirectToPublicationListWithSuccessState(HttpServletResponse response) throws Exception {
	//		response.sendRedirect("/SpringRestHibernate/publicationsListPrivate?success=1"); //$NON-NLS-1$
	//	}
	//
	//	/** Redirect to the publication list with a "failure" state.
	//	 * This function is usually invoked after the success of an operation.
	//	 *
	//	 * @param response the HTTP response.
	//	 * @throws Exception if the redirection cannot be done.
	//	 */
	//	@SuppressWarnings("static-method")
	//	protected void redirectToPublicationListWithFailureState(HttpServletResponse response) throws Exception {
	//		response.sendRedirect("/SpringRestHibernate/publicationsListPrivate?success=0"); //$NON-NLS-1$
	//	}
	//
	//	/** Redirect to the "add publication" page with a "success" state.
	//	 * This function is usually invoked after the success of an operation.
	//	 *
	//	 * @param response the HTTP response.
	//	 * @throws Exception if the redirection cannot be done.
	//	 */
	//	@SuppressWarnings("static-method")
	//	protected void redirectToAddPublicationWithSuccessState(HttpServletResponse response) throws Exception {
	//		response.sendRedirect("/SpringRestHibernate/addPublication?success=1"); //$NON-NLS-1$
	//	}
	//
	//	/** Redirect to the "add publication" page with a "success" state and a number of successfully imoprted publications.
	//	 * This function is usually invoked after the success of an operation.
	//	 *
	//	 * @param response the HTTP response.
	//	 * @param nbImported number of publications that were successfully imported.
	//	 * @throws Exception if the redirection cannot be done.
	//	 */
	//	@SuppressWarnings("static-method")
	//	protected void redirectToAddPublicationWithSuccessState(HttpServletResponse response, int nbImported) throws Exception {
	//		response.sendRedirect("/SpringRestHibernate/addPublication?success=1&importedPubs=" + nbImported); //$NON-NLS-1$
	//	}
	//
	//	/** Redirect to the "add publication" page with a "edition" state.
	//	 * This function is usually invoked for editing a publication.
	//	 *
	//	 * @param response the HTTP response.
	//	 * @param publicationId the identifier of the publication to edit.
	//	 * @throws Exception if the redirection cannot be done.
	//	 */
	//	@SuppressWarnings("static-method")
	//	protected void redirectToAddPublicationWithEditState(HttpServletResponse response, int publicationId) throws Exception {
	//		response.sendRedirect("/SpringRestHibernate/addPublication?edit=1&publicationId=" + publicationId); //$NON-NLS-1$
	//	}
	//
	//	/** Delete a publication.
	//	 *
	//	 * @param response the HTTP response.
	//	 * @param publicationId the identifier of the publication.
	//	 * @throws Exception if the redirection cannot be done.
	//	 */
	//	@GetMapping("/deletePublication")
	//	public void deletePublication(HttpServletResponse response,
	//			@RequestParam Integer publicationId) throws Exception {
	//		if (publicationId != null) {
	//			this.publicationService.removePublication(publicationId.intValue());
	//			redirectToPublicationListWithSuccessState(response);
	//		} else {
	//			redirectToPublicationListWithFailureState(response);
	//		}
	//	}
	//
	//	/** Create a publication entry into the database.
	//	 * This function supports the first step for the creation of a publication.
	//	 * It creates the publication into the database according to the variable parameters
	//	 * of the function. Then is redirect to the end-point of
	//	 * {@link #addPublication(HttpServletRequest, HttpServletResponse, boolean, Integer)}
	//	 * for finalizing the process of publication creation.
	//	 *
	//	 * @param response the HTTP response.
	//	 * @param publicationType the type of publication to be created.
	//	 * @param publicationTitle the title of the publication.
	//	 * @param publicationAuthors the ordered list of the authors. This list contains the names of the persons
	//	 *     with a syntax that is supported by {@link PersonNameParser}, e.g., {@code "FIRST LAST"} or
	//	 *     {@code "LAST, FIRST"}.
	//	 * @param publicationYear the year of the publication.
	//	 * @param publicationDate the full date of the publication in format {@code "YYYY-MM-DD"}.
	//	 * @param publicationAbstract the text of the publication abstract.
	//	 * @param publicationKeywords the keywords of the publication.
	//	 * @param publicationDOI the DOI number of the publication.
	//	 * @param publicationISBN the ISBN number of the publication.
	//	 * @param publicationISSN the ISSN numver of the publication.
	//	 * @param publicationURL an URL to a page that is associated to the publication.
	//	 * @param publicationVideoURL an URL to a video associated to the publication.
	//	 * @param publicationDblpURL the URL to the page of the publication on DBLP website.
	//	 * @param publicationLanguage the major language with which the publication is written. By default,
	//	 *     it is {@link PublicationLanguage#ENGLISH}.
	//	 * @param publicationPdf the content of the PDF file of the publication that must be uploaded.
	//	 * @param publicationAward the content of a document that represents an award certificate that must be uploaded.
	//	 * @param volume the volume of the publication (common to most of the publication types).
	//	 * @param number the number of the publication (common to most of the publication types).
	//	 * @param pages the page range of the publication in its container (common to most of the publication types).
	//	 * @param editors the list of the names of the editos of the publication's container (common to most of the publication types).
	//	 * @param address the geographical location of the event in which the publication is published  (common to most of the publication types).
	//	 *     It is usually a city and/or a country.
	//	 * @param series the name or the number of series in which the publication is committed.
	//	 * @param publisher the name of the publisher.
	//	 * @param edition the number of name of the edition (or version) of the publication.
	//	 * @param bookTitle the name of the book that contains the publication (usually for {@link PublicationCategory#COS} or
	//	 *     {@link PublicationCategory#COV}).
	//	 * @param chapterNumber the number or the name of the chapter that corresponds to the publication (usually for {@link PublicationCategory#COS} or
	//	 *     {@link PublicationCategory#COV}).
	//	 * @param scientificEventName the name of the scientific event for which the publication was committed. Usually,
	//	 *     this event generates proceedings that contain the publication (see {@link PublicationCategory#C_ACTI},
	//	 *     {@link PublicationCategory#C_ACTN}, {@link PublicationCategory#C_COM} or {@link PublicationCategory#C_INV}).
	//	 * @param organization the name of the institution which have organized the scientific event in which the
	//	 *     publication is published  (see {@link PublicationCategory#C_ACTI},
	//	 *     {@link PublicationCategory#C_ACTN}, {@link PublicationCategory#C_COM} or {@link PublicationCategory#C_INV}).
	//	 * @param institution the name of the institution that has published the publication (see {@link PublicationCategory#TH}).
	//	 * @param howPublished a description of the method of publication of a document (usually for {@link PublicationCategory#AP}).
	//	 * @param documentType the name of a type of document  (usually for {@link PublicationCategory#AP} or
	//	 *      {@link PublicationCategory#BRE}).
	//	 * @throws Exception if the redirection to the page that is suporting the second stage has failed.
	//	 * @see #addPublication(HttpServletRequest, HttpServletResponse, boolean, Integer)
	//	 */
	//	@PostMapping(value = "/createPublication", headers = "Accept=application/json")
	//	public void createPublication(HttpServletResponse response,
	//			@RequestParam String publicationType,
	//			@RequestParam String publicationTitle,
	//			@RequestParam String[] publicationAuthors,
	//			@RequestParam int publicationYear,
	//			@RequestParam(required = false) String publicationDate,
	//			@RequestParam(required = false) String publicationAbstract,
	//			@RequestParam(required = false) String publicationKeywords,
	//			@RequestParam(required = false) String publicationDOI,
	//			@RequestParam(required = false) String publicationISBN,
	//			@RequestParam(required = false) String publicationISSN,
	//			@RequestParam(required = false) String publicationURL,
	//			@RequestParam(required = false) String publicationVideoURL,
	//			@RequestParam(required = false) String publicationDblpURL,
	//			@RequestParam(required = false) String publicationLanguage,
	//			@RequestParam(required = false) MultipartFile publicationPdf,
	//			@RequestParam(required = false) MultipartFile publicationAward,
	//			@RequestParam(required = false) String volume,
	//			@RequestParam(required = false) String number,
	//			@RequestParam(required = false) String pages,
	//			@RequestParam(required = false) String editors,
	//			@RequestParam(required = false) String address,
	//			@RequestParam(required = false) String series,
	//			@RequestParam(required = false) String publisher,
	//			@RequestParam(required = false) String edition,
	//			@RequestParam(required = false) String bookTitle,
	//			@RequestParam(required = false) String chapterNumber,
	//			@RequestParam(required = false) String scientificEventName,
	//			@RequestParam(required = false) String organization,
	//			@RequestParam(required = false) String howPublished,
	//			@RequestParam(required = false) String documentType,
	//			@RequestParam(required = false) String institution) throws Exception {
	//		try {
	//			if (publicationAuthors == null) {
	//				throw new IllegalArgumentException("You must specify at least one author"); //$NON-NLS-1$
	//			}
	//			final PublicationType publicationTypeEnum = PublicationType.valueOfCaseInsensitive(publicationType);
	//			final Date publicationDateObj;
	//			if (Strings.isNullOrEmpty(publicationDate)) {
	//				publicationDateObj = null;
	//			} else {
	//				publicationDateObj = Date.valueOf(publicationDate);
	//			}
	//			final PublicationLanguage publicationLanguageEnum = PublicationLanguage.valueOfCaseInsensitive(publicationLanguage);
	//
	//			// First step : create the publication
	//			final Publication publication = this.prePublicationFactory.createPrePublication(
	//					publicationTypeEnum,
	//					publicationTitle,
	//					publicationAbstract,
	//					publicationKeywords,
	//					publicationDateObj,
	//					publicationISBN,
	//					publicationISSN,
	//					publicationDOI,
	//					publicationURL,
	//					publicationVideoURL,
	//					publicationDblpURL,
	//					null,
	//					null,
	//					publicationLanguageEnum);
	//			final int publicationId = publication.getId();
	//
	//			// Second step: Store PDFs
	//			String concretePdfString = null;
	//			if (publicationPdf != null && !publicationPdf.isEmpty()) {
	//				final File filename = this.fileManager.makePdfFilename(publicationId);
	//				final File folder = filename.getParentFile().getAbsoluteFile();
	//				this.fileManager.saveFile(folder, filename.getName(), publicationPdf);
	//				concretePdfString = filename.getPath();
	//				getLogger().info("PDF uploaded at: " + concretePdfString); //$NON-NLS-1$
	//			}
	//
	//			String concreteAwardString = null;
	//			if (publicationAward != null && !publicationAward.isEmpty()) {
	//				final File filename = this.fileManager.makeAwardFilename(publicationId);
	//				final File folder = filename.getParentFile().getAbsoluteFile();
	//				this.fileManager.saveFile(folder, filename.getName(), publicationPdf);
	//				concreteAwardString = filename.getPath();
	//				getLogger().info("Award certificate uploaded at: " + concreteAwardString); //$NON-NLS-1$
	//			}
	//
	//			// Third step: save late attributes of the fake publication
	//			publication.setPublicationYear(publicationYear);
	//			publication.setPathToDownloadablePDF(concretePdfString);
	//			publication.setPathToDownloadableAwardCertificate(concreteAwardString);
	//
	//			// Fourth step : create the specific publication type
	//			final Class<? extends Publication> publicationClass = publicationTypeEnum.getInstanceType();
	//
	//			if (publicationClass.equals(Book.class)) {
	//				this.bookService.createBook(publication, volume, number, pages, edition,
	//						editors, series, publisher, address);
	//			} else if (publicationClass.equals(BookChapter.class)) {
	//				this.bookChapterService.createBookChapter(publication, bookTitle, chapterNumber, edition,
	//						volume, number, pages, editors, series, publisher, address);
	//			} else if (publicationClass.equals(ConferencePaper.class)) {
	//				this.conferencePaperService.createConferencePaper(publication, scientificEventName,
	//						volume, number, pages, editors, series, organization, address);
	//			} else if (publicationClass.equals(JournalEdition.class)) {
	//				this.journalEditionService.createJournalEdition(publication, volume, number, pages);
	//			} else if (publicationClass.equals(JournalPaper.class)) {
	//				this.journalPaperService.createJournalPaper(publication, volume, number, pages);
	//			} else if (publicationClass.equals(KeyNote.class)) {
	//				this.keyNoteService.createKeyNote(publication, scientificEventName, editors, organization, address);
	//			} else if (publicationClass.equals(MiscDocument.class)) {
	//				this.miscDocumentService.createMiscDocument(publication, number, howPublished, documentType,
	//						organization, publisher, address);
	//			} else if (publicationClass.equals(Patent.class)) {
	//				this.patentService.createPatent(publication, number, documentType, institution, address);
	//			} else if (publicationClass.equals(Report.class)) {
	//				this.reportService.createReport(publication, number, documentType, institution, address);
	//			} else if (publicationClass.equals(Thesis.class)) {
	//				this.thesisService.createThesis(publication, institution, address);
	//			} else {
	//				throw new IllegalArgumentException("Unsupported publication type: " + publicationType); //$NON-NLS-1$
	//			}
	//			getLogger().info("Publication instance created: " + publicationClass.getSimpleName()); //$NON-NLS-1$
	//
	//			// Fifth step: create the authors and link them to the publication
	//			int i = 0;
	//			for (final String publicationAuthor : publicationAuthors) {
	//				final String firstName = this.nameParser.parseFirstName(publicationAuthor);
	//				final String lastName = this.nameParser.parseLastName(publicationAuthor);
	//				int authorId = this.personService.getPersonIdByName(firstName, lastName);
	//				if (authorId == 0) {
	//					// The author does not exist yet
	//					authorId = this.personService.createPerson(firstName, lastName, null, null, null);
	//					getLogger().info("New person \"" + publicationAuthor + "\" created with id: " + authorId); //$NON-NLS-1$ //$NON-NLS-2$
	//				}
	//				this.authorshipService.addAuthorship(authorId, publicationId, i);
	//				getLogger().info("Author \"" + publicationAuthor + "\" added to publication with id " + publicationId); //$NON-NLS-1$ //$NON-NLS-2$
	//				i++;
	//			}
	//
	//			redirectToAddPublicationWithSuccessState(response);
	//		} catch (Exception ex) {
	//			redirectError(response, ex);
	//		}
	//	}
	//
	//	/** Create a model-view form that enables to edit a publication.
	//	 * This function supports the second step for the creation of a publication.
	//	 * It creates an edition form for the publication.
	//	 * <p>
	//	 * The "flash map" attributes may contains the {@code "bibtex"} fields that is a 
	//	 * regular BibTeX description of the fields for the publication.
	//	 * These BibTeX values are used for pre-filling the publication form.
	//	 *
	//	 * @param request the HTTP request.
	//	 * @param response the HTTP response.
	//	 * @param filling {@code true} to fill up the model-view form with the values of the publication's fields.
	//	 *     If it is {@code false}, the fields of the form are not filled up.
	//	 *     If the filling is turned on, the field values are obtained from a BibTeX description in the
	//	 *     "flash map" of the HTTP request.
	//	 * @param publicationId the identifier of the publication to be edited if it is provided.
	//	 * @return the model-view object.
	//	 * @throws Exception if the model-view cannot be created.
	//	 * @see #createPublication(HttpServletResponse, String, String, String[], int, String, String, String, String, String, String, String, String, String, String, MultipartFile, MultipartFile, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String)
	//	 */
	//	@GetMapping("/addPublication")
	//	public ModelAndView addPublication(HttpServletRequest request, HttpServletResponse response,
	//			@RequestParam(required = false) boolean filling,
	//			@RequestParam(required = false) Integer publicationId) throws Exception  {
	//		try {
	//			Publication publication = null;
	//			if (filling) {
	//				final Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
	//				if (inputFlashMap != null) {
	//					String bibtex = (String)inputFlashMap.get("bibtex"); //$NON-NLS-1$
	//					final List<Publication> pubs = this.bibtex.extractPublications(bibtex);
	//					// TODO for now, we just take the first bibtex
	//					publication = pubs.get(0);
	//				} else {
	//					throw new Exception("This bibtex does not fit with the publication filling."); //$NON-NLS-1$
	//				}
	//			} else if (publicationId != null) {
	//				publication = this.publicationService.getPublication(publicationId.intValue());
	//			}
	//
	//			final ModelAndView modelView = new ModelAndView("addPublication"); //$NON-NLS-1$
	//
	//			modelView.addObject("_journalService", this.journalService); //$NON-NLS-1$
	//			modelView.addObject("_edit", Boolean.FALSE); //$NON-NLS-1$
	//
	//			final List<Journal> journals = this.journalService.getAllJournals();
	//			modelView.addObject("_journals", journals); //$NON-NLS-1$
	//
	//			final List<Person> authors = this.personService.getAllPersons();
	//			modelView.addObject("_authors", authors); //$NON-NLS-1$
	//
	//			final List<PublicationType> publicationsTypes = Arrays.asList(PublicationType.values());
	//			modelView.addObject("_publicationsTypes", publicationsTypes); //$NON-NLS-1$
	//
	//			final List<QuartileRanking> publicationsQuartiles = Arrays.asList(QuartileRanking.values());
	//			modelView.addObject("_publicationsQuartiles", publicationsQuartiles); //$NON-NLS-1$
	//
	//			final List<CoreRanking> jCoreRankings = Arrays.asList(CoreRanking.values());
	//			modelView.addObject("_journalCoreRankings", jCoreRankings); //$NON-NLS-1$
	//
	//			// IF edit mode
	//			if (publication != null && (publicationId != null || filling)) {
	//				if (publicationId != null) {
	//					modelView.addObject("authors", this.authorshipService.getAuthorsFor(publicationId.intValue())); //$NON-NLS-1$
	//					modelView.addObject("_edit", Boolean.TRUE); //$NON-NLS-1$
	//				} else if (filling) {
	//					modelView.addObject("authors", publication.getAuthors()); //$NON-NLS-1$
	//				}
	//				modelView.addObject("_publication", publication); //$NON-NLS-1$
	//
	//				publication.forEachAttribute((name, value) -> {
	//					modelView.addObject(name, value);
	//				});
	//			}
	//
	//			return modelView;
	//		} catch(Exception ex) {
	//			redirectError(response,  ex);
	//		}
	//		return null;
	//	}
	//
	//	/** Update the fields of a publication.
	//	 *
	//	 * @param response the HTTP response.
	//	 * @param publicationId the identifier of the publication to be edited.
	//	 * @param publicationType the type of publication to be created.
	//	 * @param publicationTitle the title of the publication.
	//	 * @param publicationAuthors the ordered list of the authors. This list contains the names of the persons
	//	 *     with a syntax that is supported by {@link PersonNameParser}, e.g., {@code "FIRST LAST"} or
	//	 *     {@code "LAST, FIRST"}.
	//	 * @param publicationYear the year of the publication.
	//	 * @param publicationDate the full date of the publication in format {@code "YYYY-MM-DD"}.
	//	 * @param publicationAbstract the text of the publication abstract.
	//	 * @param publicationKeywords the keywords of the publication.
	//	 * @param publicationDOI the DOI number of the publication.
	//	 * @param publicationISBN the ISBN number of the publication.
	//	 * @param publicationISSN the ISSN numver of the publication.
	//	 * @param publicationURL an URL to a page that is associated to the publication.
	//	 * @param publicationVideoURL an URL to a video associated to the publication.
	//	 * @param publicationDblpURL the URL to the page of the publication on DBLP website.
	//	 * @param publicationLanguage the major language with which the publication is written. By default,
	//	 *     it is {@link PublicationLanguage#ENGLISH}.
	//	 * @param publicationPdf the content of the PDF file of the publication that must be uploaded.
	//	 * @param publicationAward the content of a document that represents an award certificate that must be uploaded.
	//	 * @param volume the volume of the publication (common to most of the publication types).
	//	 * @param number the number of the publication (common to most of the publication types).
	//	 * @param pages the page range of the publication in its container (common to most of the publication types).
	//	 * @param editors the list of the names of the editos of the publication's container (common to most of the publication types).
	//	 * @param address the geographical location of the event in which the publication is published  (common to most of the publication types).
	//	 *     It is usually a city and/or a country.
	//	 * @param series the name or the number of series in which the publication is committed.
	//	 * @param publisher the name of the publisher.
	//	 * @param edition the number of name of the edition (or version) of the publication.
	//	 * @param bookTitle the name of the book that contains the publication (usually for {@link PublicationCategory#COS} or
	//	 *     {@link PublicationCategory#COV}).
	//	 * @param chapterNumber the number or the name of the chapter that corresponds to the publication (usually for {@link PublicationCategory#COS} or
	//	 *     {@link PublicationCategory#COV}).
	//	 * @param scientificEventName the name of the scientific event for which the publication was committed. Usually,
	//	 *     this event generates proceedings that contain the publication (see {@link PublicationCategory#C_ACTI},
	//	 *     {@link PublicationCategory#C_ACTN}, {@link PublicationCategory#C_COM} or {@link PublicationCategory#C_INV}).
	//	 * @param organization the name of the institution which have organized the scientific event in which the
	//	 *     publication is published  (see {@link PublicationCategory#C_ACTI},
	//	 *     {@link PublicationCategory#C_ACTN}, {@link PublicationCategory#C_COM} or {@link PublicationCategory#C_INV}).
	//	 * @param institution the name of the institution that has published the publication (see {@link PublicationCategory#TH}).
	//	 * @param howPublished a description of the method of publication of a document (usually for {@link PublicationCategory#AP}).
	//	 * @param documentType the name of a type of document  (usually for {@link PublicationCategory#AP} or
	//	 *      {@link PublicationCategory#BRE}).
	//	 * @throws Exception if the redirection to the page that is suporting the second stage has failed.
	//	 */
	//	@PostMapping(value = "/editPublication", headers = "Accept=application/json")
	//	public void editPublication(HttpServletResponse response,
	//			@RequestParam Integer publicationId,
	//			@RequestParam String publicationType,
	//			@RequestParam String publicationTitle,
	//			@RequestParam String[] publicationAuthors,
	//			@RequestParam int publicationYear,
	//			@RequestParam(required = false) String publicationDate,
	//			@RequestParam(required = false) String publicationAbstract,
	//			@RequestParam(required = false) String publicationKeywords,
	//			@RequestParam(required = false) String publicationDOI,
	//			@RequestParam(required = false) String publicationISBN,
	//			@RequestParam(required = false) String publicationISSN,
	//			@RequestParam(required = false) String publicationURL,
	//			@RequestParam(required = false) String publicationVideoURL,
	//			@RequestParam(required = false) String publicationDblpURL,
	//			@RequestParam(required = false) String publicationLanguage,
	//			@RequestParam(required = false) MultipartFile publicationPdf,
	//			@RequestParam(required = false) MultipartFile publicationAward,
	//			@RequestParam(required = false) String volume,
	//			@RequestParam(required = false) String number,
	//			@RequestParam(required = false) String pages,
	//			@RequestParam(required = false) String editors,
	//			@RequestParam(required = false) String address,
	//			@RequestParam(required = false) String series,
	//			@RequestParam(required = false) String publisher,
	//			@RequestParam(required = false) String edition,
	//			@RequestParam(required = false) String bookTitle,
	//			@RequestParam(required = false) String chapterNumber,
	//			@RequestParam(required = false) String scientificEventName,
	//			@RequestParam(required = false) String organization,
	//			@RequestParam(required = false) String howPublished,
	//			@RequestParam(required = false) String documentType,
	//			@RequestParam(required = false) String institution) throws Exception {
	//		try {
	//			if (publicationId == null) {
	//				throw new IllegalArgumentException("null publication identifier"); //$NON-NLS-1$
	//			}
	//			if (publicationAuthors == null) {
	//				throw new IllegalArgumentException("You must specify at least one author"); //$NON-NLS-1$
	//			}
	//			final PublicationType publicationTypeEnum = PublicationType.valueOfCaseInsensitive(publicationType);
	//			final Date publicationDateObj;
	//			if (Strings.isNullOrEmpty(publicationDate)) {
	//				publicationDateObj = null;
	//			} else {
	//				publicationDateObj = Date.valueOf(publicationDate);
	//			}
	//			final PublicationLanguage publicationLanguageEnum = PublicationLanguage.valueOfCaseInsensitive(publicationLanguage);
	//
	//			final Publication pub = this.publicationService.getPublication(publicationId.intValue());
	//			if (pub != null) {
	//
	//				// First step: Store PDFs
	//				String concretePdfString = null;
	//				if (publicationPdf != null && !publicationPdf.isEmpty()) {
	//					final File filename = this.fileManager.makePdfFilename(publicationId.intValue());
	//					final File folder = filename.getParentFile().getAbsoluteFile();
	//					this.fileManager.saveFile(folder, filename.getName(), publicationPdf);
	//					concretePdfString = filename.getPath();
	//					getLogger().info("PDF uploaded at: " + concretePdfString); //$NON-NLS-1$
	//				}
	//
	//				String concreteAwardString = null;
	//				if (publicationAward != null && !publicationAward.isEmpty()) {
	//					final File filename = this.fileManager.makeAwardFilename(publicationId.intValue());
	//					final File folder = filename.getParentFile().getAbsoluteFile();
	//					this.fileManager.saveFile(folder, filename.getName(), publicationPdf);
	//					concreteAwardString = filename.getPath();
	//					getLogger().info("Award certificate uploaded at: " + concreteAwardString); //$NON-NLS-1$
	//				}
	//
	//				// Second step: Update the list of authors.
	//
	//				final List<Person> oldAuthors = this.authorshipService.getAuthorsFor(publicationId.intValue());
	//				final List<Integer> oldAuthorIds = oldAuthors.stream().map(it -> Integer.valueOf(it.getId())).collect(Collectors.toList());
	//
	//				int i = 0;
	//				for (final String publicationAuthor : publicationAuthors) {
	//					final String firstName = this.nameParser.parseFirstName(publicationAuthor);
	//					final String lastName = this.nameParser.parseLastName(publicationAuthor);
	//					int authorId = this.personService.getPersonIdByName(firstName, lastName);
	//					if (authorId == 0) {
	//						// The author does not exist yet
	//						authorId = this.personService.createPerson(firstName, lastName, null, null, null);
	//						getLogger().info("New person \"" + publicationAuthor + "\" created with id: " + authorId); //$NON-NLS-1$ //$NON-NLS-2$
	//					} else {
	//						oldAuthorIds.remove(Integer.valueOf(authorId));
	//					}
	//					final int finalAuthorId = authorId;
	//					final Optional<Person> optPerson = oldAuthors.stream().filter(it -> it.getId() == finalAuthorId).findFirst();
	//					if (optPerson.isPresent()) {
	//						// Author is already present
	//						this.authorshipService.updateAuthorship(authorId, publicationId.intValue(), i);
	//						getLogger().info("Author \"" + publicationAuthor + "\" updated for the publication with id " + publicationId); //$NON-NLS-1$ //$NON-NLS-2$
	//					} else {
	//						// Author was not associated yet
	//						this.authorshipService.addAuthorship(authorId, publicationId.intValue(), i);
	//						getLogger().info("Author \"" + publicationAuthor + "\" added to publication with id " + publicationId); //$NON-NLS-1$ //$NON-NLS-2$
	//					}
	//					i++;
	//				}
	//
	//				// Remove the old author ships
	//				for (final Integer id : oldAuthorIds) {
	//					this.authorshipService.removeAuthorship(id.intValue(), publicationId.intValue());
	//				}
	//
	//				// Third step : update the specific publication
	//				final Class<? extends Publication> publicationClass = publicationTypeEnum.getInstanceType();
	//
	//				if (publicationClass.equals(Book.class)) {
	//					this.bookService.updateBook(
	//							publicationId.intValue(),
	//							publicationTitle, publicationTypeEnum,
	//							publicationDateObj, publicationAbstract, publicationKeywords,
	//							publicationDOI, publicationISBN, publicationISSN,
	//							publicationDblpURL, publicationURL, publicationLanguageEnum,
	//							concretePdfString, concreteAwardString, publicationVideoURL,
	//							volume, number, pages, edition, editors,
	//							series, publisher, address);
	//				} else if (publicationClass.equals(BookChapter.class)) {
	//					this.bookChapterService.updateBookChapter(
	//							publicationId.intValue(), 
	//							publicationTitle, publicationTypeEnum, publicationDateObj,
	//							publicationAbstract, publicationKeywords,
	//							publicationDOI, publicationISBN, publicationISSN,
	//							publicationDblpURL, publicationURL,
	//							publicationLanguageEnum,
	//							concretePdfString, concreteAwardString, publicationVideoURL,
	//							bookTitle, chapterNumber, edition,
	//							volume, number, pages, editors, series,
	//							publisher, address);
	//				} else if (publicationClass.equals(ConferencePaper.class)) {
	//					this.conferencePaperService.updateConferencePaper(
	//							publicationId.intValue(),
	//							publicationTitle, publicationTypeEnum, publicationDateObj,
	//							publicationAbstract, publicationKeywords,
	//							publicationDOI, publicationISBN, publicationISSN,
	//							publicationDblpURL, publicationURL,
	//							publicationLanguageEnum, concretePdfString, concreteAwardString,
	//							publicationVideoURL, scientificEventName, volume, number,
	//							pages, editors, series, organization, address);
	//				} else if (publicationClass.equals(JournalEdition.class)) {
	//					this.journalEditionService.updateJournalEdition(
	//							publicationId.intValue(),
	//							publicationTitle, publicationTypeEnum, publicationDateObj,
	//							publicationAbstract, publicationKeywords,
	//							publicationDOI, publicationISBN, publicationISSN,
	//							publicationDblpURL, publicationURL,
	//							publicationLanguageEnum, concretePdfString, concreteAwardString,
	//							publicationVideoURL, volume, number, pages);
	//				} else if (publicationClass.equals(JournalPaper.class)) {
	//					this.journalPaperService.updateJournalPaper(
	//							publicationId.intValue(),
	//							publicationTitle, publicationTypeEnum, publicationDateObj,
	//							publicationAbstract, publicationKeywords,
	//							publicationDOI, publicationISBN, publicationISSN,
	//							publicationDblpURL, publicationURL,
	//							publicationLanguageEnum, concretePdfString, concreteAwardString,
	//							publicationVideoURL, volume, number, pages);
	//				} else if (publicationClass.equals(KeyNote.class)) {
	//					this.keyNoteService.updateKeyNote(
	//							publicationId.intValue(),
	//							publicationTitle, publicationTypeEnum, publicationDateObj,
	//							publicationAbstract, publicationKeywords,
	//							publicationDOI, publicationISBN, publicationISSN,
	//							publicationDblpURL, publicationURL,
	//							publicationLanguageEnum, concretePdfString, concreteAwardString,
	//							publicationVideoURL, scientificEventName, editors, organization, address);
	//				} else if (publicationClass.equals(MiscDocument.class)) {
	//					this.miscDocumentService.updateMiscDocument(
	//							publicationId.intValue(),
	//							publicationTitle, publicationTypeEnum, publicationDateObj,
	//							publicationAbstract, publicationKeywords,
	//							publicationDOI, publicationISBN, publicationISSN,
	//							publicationDblpURL, publicationURL,
	//							publicationLanguageEnum, concretePdfString, concreteAwardString,
	//							publicationVideoURL, number, howPublished, documentType,
	//							organization, publisher, address);
	//				} else if (publicationClass.equals(Patent.class)) {
	//					this.patentService.updatePatent(
	//							publicationId.intValue(),
	//							publicationTitle, publicationTypeEnum, publicationDateObj,
	//							publicationAbstract, publicationKeywords,
	//							publicationDOI, publicationISBN, publicationISSN,
	//							publicationDblpURL, publicationURL,
	//							publicationLanguageEnum, concretePdfString, concreteAwardString,
	//							publicationVideoURL, number, documentType, institution, address);
	//				} else if (publicationClass.equals(Report.class)) {
	//					this.reportService.updateReport(
	//							publicationId.intValue(),
	//							publicationTitle, publicationTypeEnum, publicationDateObj,
	//							publicationAbstract, publicationKeywords,
	//							publicationDOI, publicationISBN, publicationISSN,
	//							publicationDblpURL, publicationURL,
	//							publicationLanguageEnum, concretePdfString, concreteAwardString,
	//							publicationVideoURL, number, documentType, institution, address);
	//				} else if (publicationClass.equals(Thesis.class)) {
	//					this.thesisService.updateThesis(
	//							publicationId.intValue(),
	//							publicationTitle, publicationTypeEnum, publicationDateObj,
	//							publicationAbstract, publicationKeywords,
	//							publicationDOI, publicationISBN, publicationISSN,
	//							publicationDblpURL, publicationURL,
	//							publicationLanguageEnum, concretePdfString, concreteAwardString,
	//							publicationVideoURL, institution, address);
	//				} else {
	//					throw new IllegalArgumentException("Unsupported publication type: " + publicationType); //$NON-NLS-1$
	//				}
	//				getLogger().info("Publication instance updated: " + publicationId); //$NON-NLS-1$
	//			}
	//			redirectToAddPublicationWithEditState(response, publicationId.intValue());
	//		} catch (Exception ex) {
	//			redirectError(response, ex);
	//		}
	//	}
	//
	//	/** Import publications from a BibTeX string. The format of the BibTeX is a standard that is briefly described
	//	 * on {@link "https://en.wikipedia.org/wiki/BibTeX"}.
	//	 * If multiple BibTeX entries are defined into the given input string, each of them is subject
	//	 * of an importation tentative. If the import process is successful, the database identifier of the publication
	//	 * is replied.
	//	 *
	//	 * @param response the HTTP response.
	//	 * @param bibtex the string that contains the BibTeX description of the publications.
	//	 * @return the list of the identifiers of the publications that are successfully imported.
	//	 * @throws Exception if it is impossible to redirect to the error page.
	//	 * @see BibTeX
	//	 * @see "https://en.wikipedia.org/wiki/BibTeX"
	//	 */
	//	@PostMapping(value = "/importPublications", headers = "Accept=application/json")
	//	public List<Integer> importPublications(HttpServletResponse response, String bibtex) throws Exception {
	//		try {
	//			return this.publicationService.importPublications(bibtex);
	//		} catch (Exception ex) {
	//			redirectError(response, ex);
	//			return Collections.emptyList();
	//		}
	//	}
	//
	//	/** Import publications from a BibTeX string. The format of the BibTeX is a standard that is briefly described
	//	 * on {@link "https://en.wikipedia.org/wiki/BibTeX"}.
	//	 * If multiple BibTeX entries are defined into the given input string, each of them is subject
	//	 * of an importation tentative. If the import process is successful, the database identifier of the publication
	//	 * is replied.
	//	 *
	//	 * @param response the HTTP response.
	//	 * @param bibtexFile the file that contains the BibTeX description of the publications.
	//	 * @throws Exception if it is impossible to import the BibTeX data from the given source file.
	//	 * @see BibTeX
	//	 * @see "https://en.wikipedia.org/wiki/BibTeX"
	//	 */
	//	@PostMapping(value = "/importBibTeX", headers = "Accept=application/json")
	//	public void importBibTeX(HttpServletResponse response, MultipartFile bibtexFile) throws Exception {
	//		try {
	//			if (bibtexFile != null && !bibtexFile.isEmpty()) {
	//				final String bibtexContent = this.fileManager.readTextFile(bibtexFile);
	//				getLogger().debug("BibTeX file read : \n" + bibtexContent); //$NON-NLS-1$
	//				final List<Integer> publications = importPublications(response, bibtexContent);
	//				redirectToAddPublicationWithSuccessState(response, publications.size());
	//			} else {
	//				throw new Exception("BibTeX input file not provided"); //$NON-NLS-1$
	//			}
	//		} catch (Exception ex) {
	//			redirectError(response, ex);
	//		}
	//	}
	//
	//	/** Read a BibTeX source and redirect to the publication addition form.
	//	 *
	//	 * @param response the HTTP response
	//	 * @param bibtexFile the BibTeX file to read.
	//	 * @param redirectAttributes definition of the redirection attributes.
	//	 * @return the redirection view.
	//	 * @throws Exception if the BibTeX source cannot be read.
	//	 */
	//	@PostMapping(value = "/bibTeXToAddPublication", headers = "Accept=application/json")
	//	public RedirectView bibTeXToAddPublication(HttpServletResponse response, MultipartFile bibtexFile, RedirectAttributes redirectAttributes) throws Exception {
	//		try {
	//			if (bibtexFile != null && !bibtexFile.isEmpty()) {
	//				final String bibtexContent = this.fileManager.readTextFile(bibtexFile);
	//				redirectAttributes.addFlashAttribute("bibtex", bibtexContent); //$NON-NLS-1$
	//				redirectAttributes.addFlashAttribute("publicationService", this.publicationService); //$NON-NLS-1$
	//				return this.viewFactory.newRedirectView("/addPublication?filling=true", true); //$NON-NLS-1$
	//			}
	//			throw new Exception("BibTeX input file not provided"); //$NON-NLS-1$
	//		} catch (Exception ex) {
	//			redirectError(response, ex);
	//			return null;
	//		}
	//	}

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
