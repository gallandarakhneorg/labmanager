/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.controller.publication;

import java.io.File;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.ciadlab.labmanager.controller.AbstractController;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationCategory;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.publication.type.Book;
import fr.ciadlab.labmanager.entities.publication.type.BookChapter;
import fr.ciadlab.labmanager.entities.publication.type.ConferencePaper;
import fr.ciadlab.labmanager.entities.publication.type.JournalEdition;
import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import fr.ciadlab.labmanager.entities.publication.type.KeyNote;
import fr.ciadlab.labmanager.entities.publication.type.MiscDocument;
import fr.ciadlab.labmanager.entities.publication.type.Patent;
import fr.ciadlab.labmanager.entities.publication.type.Report;
import fr.ciadlab.labmanager.entities.publication.type.Thesis;
import fr.ciadlab.labmanager.entities.ranking.CoreRanking;
import fr.ciadlab.labmanager.entities.ranking.QuartileRanking;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.io.bibtex.BibTeX;
import fr.ciadlab.labmanager.io.html.HtmlPageExporter;
import fr.ciadlab.labmanager.service.journal.JournalService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.publication.AuthorshipService;
import fr.ciadlab.labmanager.service.publication.PrePublicationFactory;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import fr.ciadlab.labmanager.service.publication.type.BookChapterService;
import fr.ciadlab.labmanager.service.publication.type.BookService;
import fr.ciadlab.labmanager.service.publication.type.ConferencePaperService;
import fr.ciadlab.labmanager.service.publication.type.JournalEditionService;
import fr.ciadlab.labmanager.service.publication.type.JournalPaperService;
import fr.ciadlab.labmanager.service.publication.type.KeyNoteService;
import fr.ciadlab.labmanager.service.publication.type.MiscDocumentService;
import fr.ciadlab.labmanager.service.publication.type.PatentService;
import fr.ciadlab.labmanager.service.publication.type.ReportService;
import fr.ciadlab.labmanager.service.publication.type.ThesisService;
import fr.ciadlab.labmanager.utils.ViewFactory;
import fr.ciadlab.labmanager.utils.files.DownloadableFileManager;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

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

	private static final String TOOL_NAME = "publicationTool"; //$NON-NLS-1$

	private PrePublicationFactory prePublicationFactory;

	private PublicationService publicationService;

	private AuthorshipService authorshipService;

	private PersonService personService;

	private HtmlPageExporter htmlPageExporter;

	private DownloadableFileManager fileManager;

	private PersonNameParser nameParser;

	private BibTeX bibtex;

	private JournalService journalService;

	private ViewFactory viewFactory;

	private final Random random = new Random();

	private BookService bookService;

	private BookChapterService bookChapterService;

	private ConferencePaperService conferencePaperService;

	private JournalEditionService journalEditionService;

	private JournalPaperService journalPaperService;

	private KeyNoteService keyNoteService;

	private MiscDocumentService miscDocumentService;

	private PatentService patentService;

	private ReportService reportService;

	private ThesisService thesisService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param prePublicationFactory the factory of pre-publications.
	 * @param publicationService the publication service.
	 * @param authorshipService the authorship service.
	 * @param personService the person service.
	 * @param htmlPageExporter the tool for exporting to HTML page.
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
			@Autowired AuthorshipService authorshipService,
			@Autowired PersonService personService,
			@Autowired HtmlPageExporter htmlPageExporter,
			@Autowired DownloadableFileManager fileManager,
			@Autowired PersonNameParser nameParser,
			@Autowired BibTeX bibtex,
			@Autowired ViewFactory viewFactory,
			@Autowired JournalService journalService,
			@Autowired BookService bookService,
			@Autowired BookChapterService bookChapterService,
			@Autowired ConferencePaperService conferencePaperService,
			@Autowired JournalEditionService journalEditionService,
			@Autowired JournalPaperService journalPaperService,
			@Autowired KeyNoteService keyNoteService,
			@Autowired MiscDocumentService miscDocumentService,
			@Autowired PatentService patentService,
			@Autowired ReportService reportService,
			@Autowired ThesisService thesisService) {
		super(TOOL_NAME);
		this.prePublicationFactory = prePublicationFactory;
		this.publicationService = publicationService;
		this.authorshipService = authorshipService;
		this.personService = personService;
		this.htmlPageExporter = htmlPageExporter;
		this.fileManager = fileManager;
		this.nameParser = nameParser;
		this.bibtex = bibtex;
		this.viewFactory = viewFactory;
		this.journalService = journalService;
		this.bookService = bookService;
		this.bookChapterService = bookChapterService;
		this.conferencePaperService = conferencePaperService;
		this.journalEditionService = journalEditionService;
		this.journalPaperService = journalPaperService;
		this.keyNoteService = keyNoteService;
		this.miscDocumentService = miscDocumentService;
		this.patentService = patentService;
		this.reportService = reportService;
		this.thesisService = thesisService;
	}

	/** Replies the model-view component for managing the publications.
	 *
	 * @return the model-view component.
	 */
	@GetMapping("/" + TOOL_NAME)
	public ModelAndView showPublicationTool() {
		final ModelAndView modelAndView = new ModelAndView(TOOL_NAME);
		modelAndView.addObject("publications", this.publicationService.getAllPublications()); //$NON-NLS-1$
		return modelAndView;
	}

	/** Build a list of publication as a Json string.
	 * 
	 * @param authorId the identifier of the person for who the publications must be replied.
	 *     If it is not provided or equal to {@code null}, all the publications are considered.
	 * @param onlyValid indicates if only the valid publications, i.e., those with at least one author,
	 *     are replied. If this argument is not provided or equal to {@code null}, the default value is
	 *     {@code true}.
	 * @return the JSON of the list of publications. THe format of the JSON is the following:
	 *     {@code data} field contains the whole data and with the value as an array of JSON elements.
	 *     Each of these elements is:<ul>
	 *     <li>{@code data} : the map of the publication's fields. The list of fields depends on
	 *         on the type of the publication;</li>
	 *     <li>{@code html} : the map of the HTML buttons for the publication. Three keys are
	 *         provided: {@code downloads}, {@code exports}, {@code edit}.</li>
	 *     </ul>
	 */
	@GetMapping("/getPublicationsList")
	@ResponseBody
	public String getPublicationList(
			@RequestParam(required = false) Integer authorId,
			@RequestParam(required = false) Boolean onlyValid) {
		List<Publication> publications;
		if (authorId == null) {
			publications = this.publicationService.getAllPublications();
		} else {
			publications = this.authorshipService.getPublicationsFor(authorId.intValue());
		}
		if (onlyValid == null || onlyValid.booleanValue()) {
			publications = publications.stream().filter(it -> it.getAuthorships() != null
					&& !it.getAuthorships().isEmpty()).collect(Collectors.toList());
		}

		final JsonArray publicationArrayJson = new JsonArray();

		for (Publication publication : publications) {
			final JsonObject publicationData = new JsonObject();
			publication.toJson(publicationData);

			final JsonObject publicationHtml = new JsonObject();

			final StringBuilder downloads = new StringBuilder();
			boolean htmlAdded = false;
			if (publication.getPathToDownloadablePDF() != null) {
				final String html = this.htmlPageExporter.getButtonToDownloadPublicationPDF(publication.getPathToDownloadablePDF());
				if (!Strings.isNullOrEmpty(html)) {
					downloads.append(html);
					htmlAdded = true;
				}
			}
			if (publication.getPathToDownloadableAwardCertificate() != null) {
				final String html = this.htmlPageExporter.getButtonToDownloadPublicationAwardCertificate(publication.getPathToDownloadableAwardCertificate());
				if (!Strings.isNullOrEmpty(html)) {
					if (htmlAdded) {
						downloads.append(this.htmlPageExporter.doubleSeparator());
					}
					downloads.append(html);
				}
			}
			if (downloads.length() > 0) {
				publicationHtml.addProperty("downloads", downloads.toString()); //$NON-NLS-1$
			}

			final StringBuilder exports = new StringBuilder();
			String html = this.htmlPageExporter.getButtonToExportPublicationToBibTeX(publication.getId());
			htmlAdded = false;
			if (!Strings.isNullOrEmpty(html)) {
				exports.append(html);
				htmlAdded = true;
			}
			html = this.htmlPageExporter.getButtonToExportPublicationToHtml(publication.getId());
			if (!Strings.isNullOrEmpty(html)) {
				if (htmlAdded) {
					exports.append(this.htmlPageExporter.doubleSeparator());
				}
				exports.append(html);
				htmlAdded = true;
			}
			html = this.htmlPageExporter.getButtonToExportPublicationToOpenDocument(publication.getId());
			if (!Strings.isNullOrEmpty(html)) {
				if (htmlAdded) {
					exports.append(this.htmlPageExporter.doubleSeparator());
				}
				exports.append(html);
			}
			if (exports.length() > 0) {
				publicationHtml.addProperty("exports", exports.toString()); //$NON-NLS-1$
			}

			final StringBuilder edit = new StringBuilder();
			html = this.htmlPageExporter.getButtonToEditPublication(publication.getId());
			htmlAdded = false;
			if (!Strings.isNullOrEmpty(html)) {
				exports.append(html);
				htmlAdded = true;
			}
			html = this.htmlPageExporter.getButtonToDeletePublication(publication.getId());
			if (!Strings.isNullOrEmpty(html)) {
				if (htmlAdded) {
					exports.append(this.htmlPageExporter.doubleSeparator());
				}
				exports.append(html);
			}
			if (edit.length() > 0) {
				publicationHtml.addProperty("edit", edit.toString()); //$NON-NLS-1$
			}

			final JsonObject container = new JsonObject();
			if (publicationData.size() > 0) {
				container.add("data", publicationData); //$NON-NLS-1$
			}
			if (publicationHtml.size() > 0) {
				container.add("html", publicationHtml); //$NON-NLS-1$
			}
			if (container.size() > 0) {
				publicationArrayJson.add(container);
			}
		}

		final JsonObject dataJson = new JsonObject();
		dataJson.add("data", publicationArrayJson); //$NON-NLS-1$
		final Gson gson = new Gson();
		return gson.toJson(dataJson);
	}

	/** Redirect to the publication list.
	 *
	 * @param modelAndView the model-view to configure for redirection.
	 * @param authorId the identifier of the author to get the list, or {@code null} if no
	 *     author selection.
	 * @param onlyValid indicates if only the valid authorships are considered. If {@code null},
	 *     the value {@code true} is assumed.
	 */
	protected void redirectToPublicationList(ModelAndView modelAndView, Integer authorId, Boolean onlyValid) {
		final StringBuilder url = new StringBuilder();
		url.append("/SpringRestHibernate/getPublicationsList"); //$NON-NLS-1$
		if (authorId != null && onlyValid != null) {
			url.append("?authorId="); //$NON-NLS-1$
			url.append(authorId.intValue());
			url.append("&onlyValid="); //$NON-NLS-1$
			url.append(onlyValid.booleanValue());
		}
		else if (authorId != null) {
			url.append("?authorId="); //$NON-NLS-1$
			url.append(authorId.intValue());
		}
		else if (onlyValid != null) {
			url.append("?onlyValid="); //$NON-NLS-1$
			url.append(onlyValid.booleanValue());
		}
		modelAndView.addObject("url", url.toString()); //$NON-NLS-1$
		// UUID to generate unique html elements
		modelAndView.addObject("uuid", Integer.valueOf(Math.abs(this.random.nextInt()))); //$NON-NLS-1$
	}

	/** Provide the map of the authors to the front-end.
	 * The provided map is given to {@code "authorsMap"} attribute and is a map
	 * with the person identifiers as keys and the full names as values.
	 * The type of the map is: {@code Map&lt;Integer, String&gt;}.
	 *
	 * @param modelAndView the model-view to configure for redirection.
	 */
	protected void provideAuthorMapToFontEnd(ModelAndView modelAndView) {
		final List<Person> persons = this.personService.getAllPersons();
		modelAndView.addObject("authorsMap", persons.parallelStream() //$NON-NLS-1$
				.collect(Collectors.toConcurrentMap(
						it -> Integer.valueOf(it.getId()),
						it -> it.getFullName())));
	}

	/** Replies the list of publications for the given author.
	 * This function provides to the front-end the map of the person identifiers to
	 * their full names.
	 * The type of the map is: {@code Map&lt;Integer, String&gt;}.
	 *
	 * @param authorId the identifier of the author.
	 * @return the model-view of the list of publications.
	 * @see #publicationListLight(Integer)
	 */
	@GetMapping("/publicationList")
	public ModelAndView publicationList(
			@RequestParam Integer authorId) {
		final ModelAndView modelAndView = new ModelAndView("publicationsList"); //$NON-NLS-1$
		provideAuthorMapToFontEnd(modelAndView);
		redirectToPublicationList(modelAndView, authorId, null);
		return modelAndView;
	}

	/** Replies the list of publications for the given author.
	 * This function does not provides the authors' map to the front-end.
	 *
	 * @param authorId the identifier of the author.
	 * @return the model-view of the list of publications.
	 * @see #publicationList(Integer)
	 */
	@GetMapping("/publicationListLight")
	public ModelAndView publicationListLight(
			@RequestParam Integer authorId) {
		final ModelAndView modelAndView = new ModelAndView("publicationsListLight"); //$NON-NLS-1$
		redirectToPublicationList(modelAndView, authorId, null);
		return modelAndView;
	}

	/** Replies the list of publications for the given author and even if the publications are not
	 * considered as valid.
	 * This function provides to the front-end the map of the person identifiers to
	 * their full names.
	 * The type of the map is: {@code Map&lt;Integer, String&gt;}.
	 * A valid publication is one that has the author as authorship.
	 *
	 * @param authorId the identifier of the author.
	 * @return the model-view of the list of publications.
	 */
	@GetMapping("/publicationListPrivate")
	public ModelAndView publicationListPrivate(
			@RequestParam(required = false) Integer authorId) {
		final ModelAndView modelAndView = new ModelAndView("publicationsListPrivate"); //$NON-NLS-1$
		provideAuthorMapToFontEnd(modelAndView);
		redirectToPublicationList(modelAndView, authorId, Boolean.FALSE);
		return modelAndView;
	}

	/** Redirect to the publication list with a "success" state.
	 * This function is usually invoked after the success of an operation.
	 *
	 * @param response the HTTP response.
	 * @throws Exception if the redirection cannot be done.
	 */
	@SuppressWarnings("static-method")
	protected void redirectToPublicationListWithSuccessState(HttpServletResponse response) throws Exception {
		response.sendRedirect("/SpringRestHibernate/publicationsListPrivate?success=1"); //$NON-NLS-1$
	}

	/** Redirect to the publication list with a "failure" state.
	 * This function is usually invoked after the success of an operation.
	 *
	 * @param response the HTTP response.
	 * @throws Exception if the redirection cannot be done.
	 */
	@SuppressWarnings("static-method")
	protected void redirectToPublicationListWithFailureState(HttpServletResponse response) throws Exception {
		response.sendRedirect("/SpringRestHibernate/publicationsListPrivate?success=0"); //$NON-NLS-1$
	}

	/** Redirect to the "add publication" page with a "success" state.
	 * This function is usually invoked after the success of an operation.
	 *
	 * @param response the HTTP response.
	 * @throws Exception if the redirection cannot be done.
	 */
	@SuppressWarnings("static-method")
	protected void redirectToAddPublicationWithSuccessState(HttpServletResponse response) throws Exception {
		response.sendRedirect("/SpringRestHibernate/addPublication?success=1"); //$NON-NLS-1$
	}

	/** Redirect to the "add publication" page with a "success" state and a number of successfully imoprted publications.
	 * This function is usually invoked after the success of an operation.
	 *
	 * @param response the HTTP response.
	 * @param nbImported number of publications that were successfully imported.
	 * @throws Exception if the redirection cannot be done.
	 */
	@SuppressWarnings("static-method")
	protected void redirectToAddPublicationWithSuccessState(HttpServletResponse response, int nbImported) throws Exception {
		response.sendRedirect("/SpringRestHibernate/addPublication?success=1&importedPubs=" + nbImported); //$NON-NLS-1$
	}

	/** Redirect to the "add publication" page with a "edition" state.
	 * This function is usually invoked for editing a publication.
	 *
	 * @param response the HTTP response.
	 * @param publicationId the identifier of the publication to edit.
	 * @throws Exception if the redirection cannot be done.
	 */
	@SuppressWarnings("static-method")
	protected void redirectToAddPublicationWithEditState(HttpServletResponse response, int publicationId) throws Exception {
		response.sendRedirect("/SpringRestHibernate/addPublication?edit=1&publicationId=" + publicationId); //$NON-NLS-1$
	}

	/** Delete a publication.
	 *
	 * @param response the HTTP response.
	 * @param publicationId the identifier of the publication.
	 * @throws Exception if the redirection cannot be done.
	 */
	@GetMapping("/deletePublication")
	public void deletePublication(HttpServletResponse response,
			@RequestParam Integer publicationId) throws Exception {
		if (publicationId != null) {
			this.publicationService.removePublication(publicationId.intValue());
			redirectToPublicationListWithSuccessState(response);
		} else {
			redirectToPublicationListWithFailureState(response);
		}
	}

	/** Create a publication entry into the database.
	 * This function supports the first step for the creation of a publication.
	 * It creates the publication into the database according to the variable parameters
	 * of the function. Then is redirect to the end-point of
	 * {@link #addPublication(HttpServletRequest, HttpServletResponse, boolean, Integer)}
	 * for finalizing the process of publication creation.
	 *
	 * @param response the HTTP response.
	 * @param publicationType the type of publication to be created.
	 * @param publicationTitle the title of the publication.
	 * @param publicationAuthors the ordered list of the authors. This list contains the names of the persons
	 *     with a syntax that is supported by {@link PersonNameParser}, e.g., {@code "FIRST LAST"} or
	 *     {@code "LAST, FIRST"}.
	 * @param publicationYear the year of the publication.
	 * @param publicationDate the full date of the publication in format {@code "YYYY-MM-DD"}.
	 * @param publicationAbstract the text of the publication abstract.
	 * @param publicationKeywords the keywords of the publication.
	 * @param publicationDOI the DOI number of the publication.
	 * @param publicationISBN the ISBN number of the publication.
	 * @param publicationISSN the ISSN numver of the publication.
	 * @param publicationURL an URL to a page that is associated to the publication.
	 * @param publicationVideoURL an URL to a video associated to the publication.
	 * @param publicationDblpURL the URL to the page of the publication on DBLP website.
	 * @param publicationLanguage the major language with which the publication is written. By default,
	 *     it is {@link PublicationLanguage#ENGLISH}.
	 * @param publicationPdf the content of the PDF file of the publication that must be uploaded.
	 * @param publicationAward the content of a document that represents an award certificate that must be uploaded.
	 * @param volume the volume of the publication (common to most of the publication types).
	 * @param number the number of the publication (common to most of the publication types).
	 * @param pages the page range of the publication in its container (common to most of the publication types).
	 * @param editors the list of the names of the editos of the publication's container (common to most of the publication types).
	 * @param address the geographical location of the event in which the publication is published  (common to most of the publication types).
	 *     It is usually a city and/or a country.
	 * @param series the name or the number of series in which the publication is committed.
	 * @param publisher the name of the publisher.
	 * @param edition the number of name of the edition (or version) of the publication.
	 * @param bookTitle the name of the book that contains the publication (usually for {@link PublicationCategory#COS} or
	 *     {@link PublicationCategory#COV}).
	 * @param chapterNumber the number or the name of the chapter that corresponds to the publication (usually for {@link PublicationCategory#COS} or
	 *     {@link PublicationCategory#COV}).
	 * @param scientificEventName the name of the scientific event for which the publication was committed. Usually,
	 *     this event generates proceedings that contain the publication (see {@link PublicationCategory#C_ACTI},
	 *     {@link PublicationCategory#C_ACTN}, {@link PublicationCategory#C_COM} or {@link PublicationCategory#C_INV}).
	 * @param organization the name of the institution which have organized the scientific event in which the
	 *     publication is published  (see {@link PublicationCategory#C_ACTI},
	 *     {@link PublicationCategory#C_ACTN}, {@link PublicationCategory#C_COM} or {@link PublicationCategory#C_INV}).
	 * @param institution the name of the institution that has published the publication (see {@link PublicationCategory#TH}).
	 * @param howPublished a description of the method of publication of a document (usually for {@link PublicationCategory#AP}).
	 * @param documentType the name of a type of document  (usually for {@link PublicationCategory#AP} or
	 *      {@link PublicationCategory#BRE}).
	 * @throws Exception if the redirection to the page that is suporting the second stage has failed.
	 * @see #addPublication(HttpServletRequest, HttpServletResponse, boolean, Integer)
	 */
	@PostMapping(value = "/createPublication", headers = "Accept=application/json")
	public void createPublication(HttpServletResponse response,
			@RequestParam String publicationType,
			@RequestParam String publicationTitle,
			@RequestParam String[] publicationAuthors,
			@RequestParam int publicationYear,
			@RequestParam(required = false) String publicationDate,
			@RequestParam(required = false) String publicationAbstract,
			@RequestParam(required = false) String publicationKeywords,
			@RequestParam(required = false) String publicationDOI,
			@RequestParam(required = false) String publicationISBN,
			@RequestParam(required = false) String publicationISSN,
			@RequestParam(required = false) String publicationURL,
			@RequestParam(required = false) String publicationVideoURL,
			@RequestParam(required = false) String publicationDblpURL,
			@RequestParam(required = false) String publicationLanguage,
			@RequestParam(required = false) MultipartFile publicationPdf,
			@RequestParam(required = false) MultipartFile publicationAward,
			@RequestParam(required = false) String volume,
			@RequestParam(required = false) String number,
			@RequestParam(required = false) String pages,
			@RequestParam(required = false) String editors,
			@RequestParam(required = false) String address,
			@RequestParam(required = false) String series,
			@RequestParam(required = false) String publisher,
			@RequestParam(required = false) String edition,
			@RequestParam(required = false) String bookTitle,
			@RequestParam(required = false) String chapterNumber,
			@RequestParam(required = false) String scientificEventName,
			@RequestParam(required = false) String organization,
			@RequestParam(required = false) String howPublished,
			@RequestParam(required = false) String documentType,
			@RequestParam(required = false) String institution) throws Exception {
		try {
			if (publicationAuthors == null) {
				throw new IllegalArgumentException("You must specify at least one author"); //$NON-NLS-1$
			}
			final PublicationType publicationTypeEnum = PublicationType.valueOfCaseInsensitive(publicationType);
			final Date publicationDateObj;
			if (Strings.isNullOrEmpty(publicationDate)) {
				publicationDateObj = null;
			} else {
				publicationDateObj = Date.valueOf(publicationDate);
			}
			final PublicationLanguage publicationLanguageEnum = PublicationLanguage.valueOfCaseInsensitive(publicationLanguage);

			// First step : create the publication
			final Publication publication = this.prePublicationFactory.createPrePublication(
					publicationTypeEnum,
					publicationTitle,
					publicationAbstract,
					publicationKeywords,
					publicationDateObj,
					publicationISBN,
					publicationISSN,
					publicationDOI,
					publicationURL,
					publicationVideoURL,
					publicationDblpURL,
					null,
					null,
					publicationLanguageEnum);
			final int publicationId = publication.getId();

			// Second step: Store PDFs
			String concretePdfString = null;
			if (publicationPdf != null && !publicationPdf.isEmpty()) {
				final File filename = this.fileManager.makePdfFilename(publicationId);
				final File folder = filename.getParentFile().getAbsoluteFile();
				this.fileManager.saveFile(folder, filename.getName(), publicationPdf);
				concretePdfString = filename.getPath();
				getLogger().info("PDF uploaded at: " + concretePdfString); //$NON-NLS-1$
			}

			String concreteAwardString = null;
			if (publicationAward != null && !publicationAward.isEmpty()) {
				final File filename = this.fileManager.makeAwardFilename(publicationId);
				final File folder = filename.getParentFile().getAbsoluteFile();
				this.fileManager.saveFile(folder, filename.getName(), publicationPdf);
				concreteAwardString = filename.getPath();
				getLogger().info("Award certificate uploaded at: " + concreteAwardString); //$NON-NLS-1$
			}

			// Third step: save late attributes of the fake publication
			publication.setPublicationYear(publicationYear);
			publication.setPathToDownloadablePDF(concretePdfString);
			publication.setPathToDownloadableAwardCertificate(concreteAwardString);

			// Fourth step : create the specific publication type
			final Class<? extends Publication> publicationClass = publicationTypeEnum.getInstanceType();

			if (publicationClass.equals(Book.class)) {
				this.bookService.createBook(publication, volume, number, pages, edition,
						editors, series, publisher, address);
			} else if (publicationClass.equals(BookChapter.class)) {
				this.bookChapterService.createBookChapter(publication, bookTitle, chapterNumber, edition,
						volume, number, pages, editors, series, publisher, address);
			} else if (publicationClass.equals(ConferencePaper.class)) {
				this.conferencePaperService.createConferencePaper(publication, scientificEventName,
						volume, number, pages, editors, series, organization, address);
			} else if (publicationClass.equals(JournalEdition.class)) {
				this.journalEditionService.createJournalEdition(publication, volume, number, pages);
			} else if (publicationClass.equals(JournalPaper.class)) {
				this.journalPaperService.createJournalPaper(publication, volume, number, pages);
			} else if (publicationClass.equals(KeyNote.class)) {
				this.keyNoteService.createKeyNote(publication, scientificEventName, editors, organization, address);
			} else if (publicationClass.equals(MiscDocument.class)) {
				this.miscDocumentService.createMiscDocument(publication, number, howPublished, documentType,
						organization, publisher, address);
			} else if (publicationClass.equals(Patent.class)) {
				this.patentService.createPatent(publication, number, documentType, institution, address);
			} else if (publicationClass.equals(Report.class)) {
				this.reportService.createReport(publication, number, documentType, institution, address);
			} else if (publicationClass.equals(Thesis.class)) {
				this.thesisService.createThesis(publication, institution, address);
			} else {
				throw new IllegalArgumentException("Unsupported publication type: " + publicationType); //$NON-NLS-1$
			}
			getLogger().info("Publication instance created: " + publicationClass.getSimpleName()); //$NON-NLS-1$

			// Fifth step: create the authors and link them to the publication
			int i = 0;
			for (final String publicationAuthor : publicationAuthors) {
				final String firstName = this.nameParser.parseFirstName(publicationAuthor);
				final String lastName = this.nameParser.parseLastName(publicationAuthor);
				int authorId = this.personService.getPersonIdByName(firstName, lastName);
				if (authorId == 0) {
					// The author does not exist yet
					authorId = this.personService.createPerson(firstName, lastName, null);
					getLogger().info("New person \"" + publicationAuthor + "\" created with id: " + authorId); //$NON-NLS-1$ //$NON-NLS-2$
				}
				this.authorshipService.addAuthorship(authorId, publicationId, i);
				getLogger().info("Author \"" + publicationAuthor + "\" added to publication with id " + publicationId); //$NON-NLS-1$ //$NON-NLS-2$
				i++;
			}

			redirectToAddPublicationWithSuccessState(response);
		} catch (Exception ex) {
			redirectError(response, ex);
		}
	}

	/** Create a model-view form that enables to edit a publication.
	 * This function supports the second step for the creation of a publication.
	 * It creates an edition form for the publication.
	 * <p>
	 * The "flash map" attributes may contains the {@code "bibtex"} fields that is a 
	 * regular BibTeX description of the fields for the publication.
	 * These BibTeX values are used for pre-filling the publication form.
	 *
	 * @param request the HTTP request.
	 * @param response the HTTP response.
	 * @param filling {@code true} to fill up the model-view form with the values of the publication's fields.
	 *     If it is {@code false}, the fields of the form are not filled up.
	 *     If the filling is turned on, the field values are obtained from a BibTeX description in the
	 *     "flash map" of the HTTP request.
	 * @param publicationId the identifier of the publication to be edited if it is provided.
	 * @return the model-view object.
	 * @throws Exception if the model-view cannot be created.
	 * @see #createPublication(HttpServletResponse, String, String, String[], int, String, String, String, String, String, String, String, String, String, String, MultipartFile, MultipartFile, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String)
	 */
	@GetMapping("/addPublication")
	public ModelAndView addPublication(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) boolean filling,
			@RequestParam(required = false) Integer publicationId) throws Exception  {
		try {
			Publication publication = null;
			if (filling) {
				final Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
				if (inputFlashMap != null) {
					String bibtex = (String)inputFlashMap.get("bibtex"); //$NON-NLS-1$
					final List<Publication> pubs = this.bibtex.extractPublications(bibtex);
					// TODO for now, we just take the first bibtex
					publication = pubs.get(0);
				} else {
					throw new Exception("This bibtex does not fit with the publication filling."); //$NON-NLS-1$
				}
			} else if (publicationId != null) {
				publication = this.publicationService.getPublication(publicationId.intValue());
			}

			final ModelAndView modelView = new ModelAndView("addPublication"); //$NON-NLS-1$

			modelView.addObject("_journalService", this.journalService); //$NON-NLS-1$
			modelView.addObject("_edit", Boolean.FALSE); //$NON-NLS-1$

			final List<Journal> journals = this.journalService.getAllJournals();
			modelView.addObject("_journals", journals); //$NON-NLS-1$

			final List<Person> authors = this.personService.getAllPersons();
			modelView.addObject("_authors", authors); //$NON-NLS-1$

			final List<PublicationType> publicationsTypes = Arrays.asList(PublicationType.values());
			modelView.addObject("_publicationsTypes", publicationsTypes); //$NON-NLS-1$

			final List<QuartileRanking> publicationsQuartiles = Arrays.asList(QuartileRanking.values());
			modelView.addObject("_publicationsQuartiles", publicationsQuartiles); //$NON-NLS-1$

			final List<CoreRanking> jCoreRankings = Arrays.asList(CoreRanking.values());
			modelView.addObject("_journalCoreRankings", jCoreRankings); //$NON-NLS-1$

			// IF edit mode
			if (publication != null && (publicationId != null || filling)) {
				if (publicationId != null) {
					modelView.addObject("authors", this.authorshipService.getAuthorsFor(publicationId.intValue())); //$NON-NLS-1$
					modelView.addObject("_edit", Boolean.TRUE); //$NON-NLS-1$
				} else if (filling) {
					modelView.addObject("authors", publication.getAuthors()); //$NON-NLS-1$
				}
				modelView.addObject("_publication", publication); //$NON-NLS-1$

				publication.forEachAttribute((name, value) -> {
					modelView.addObject(name, value);
				});
			}

			return modelView;
		} catch(Exception ex) {
			redirectError(response,  ex);
		}
		return null;
	}

	/** Update the fields of a publication.
	 *
	 * @param response the HTTP response.
	 * @param publicationId the identifier of the publication to be edited.
	 * @param publicationType the type of publication to be created.
	 * @param publicationTitle the title of the publication.
	 * @param publicationAuthors the ordered list of the authors. This list contains the names of the persons
	 *     with a syntax that is supported by {@link PersonNameParser}, e.g., {@code "FIRST LAST"} or
	 *     {@code "LAST, FIRST"}.
	 * @param publicationYear the year of the publication.
	 * @param publicationDate the full date of the publication in format {@code "YYYY-MM-DD"}.
	 * @param publicationAbstract the text of the publication abstract.
	 * @param publicationKeywords the keywords of the publication.
	 * @param publicationDOI the DOI number of the publication.
	 * @param publicationISBN the ISBN number of the publication.
	 * @param publicationISSN the ISSN numver of the publication.
	 * @param publicationURL an URL to a page that is associated to the publication.
	 * @param publicationVideoURL an URL to a video associated to the publication.
	 * @param publicationDblpURL the URL to the page of the publication on DBLP website.
	 * @param publicationLanguage the major language with which the publication is written. By default,
	 *     it is {@link PublicationLanguage#ENGLISH}.
	 * @param publicationPdf the content of the PDF file of the publication that must be uploaded.
	 * @param publicationAward the content of a document that represents an award certificate that must be uploaded.
	 * @param volume the volume of the publication (common to most of the publication types).
	 * @param number the number of the publication (common to most of the publication types).
	 * @param pages the page range of the publication in its container (common to most of the publication types).
	 * @param editors the list of the names of the editos of the publication's container (common to most of the publication types).
	 * @param address the geographical location of the event in which the publication is published  (common to most of the publication types).
	 *     It is usually a city and/or a country.
	 * @param series the name or the number of series in which the publication is committed.
	 * @param publisher the name of the publisher.
	 * @param edition the number of name of the edition (or version) of the publication.
	 * @param bookTitle the name of the book that contains the publication (usually for {@link PublicationCategory#COS} or
	 *     {@link PublicationCategory#COV}).
	 * @param chapterNumber the number or the name of the chapter that corresponds to the publication (usually for {@link PublicationCategory#COS} or
	 *     {@link PublicationCategory#COV}).
	 * @param scientificEventName the name of the scientific event for which the publication was committed. Usually,
	 *     this event generates proceedings that contain the publication (see {@link PublicationCategory#C_ACTI},
	 *     {@link PublicationCategory#C_ACTN}, {@link PublicationCategory#C_COM} or {@link PublicationCategory#C_INV}).
	 * @param organization the name of the institution which have organized the scientific event in which the
	 *     publication is published  (see {@link PublicationCategory#C_ACTI},
	 *     {@link PublicationCategory#C_ACTN}, {@link PublicationCategory#C_COM} or {@link PublicationCategory#C_INV}).
	 * @param institution the name of the institution that has published the publication (see {@link PublicationCategory#TH}).
	 * @param howPublished a description of the method of publication of a document (usually for {@link PublicationCategory#AP}).
	 * @param documentType the name of a type of document  (usually for {@link PublicationCategory#AP} or
	 *      {@link PublicationCategory#BRE}).
	 * @throws Exception if the redirection to the page that is suporting the second stage has failed.
	 */
	@PostMapping(value = "/editPublication", headers = "Accept=application/json")
	public void editPublication(HttpServletResponse response,
			@RequestParam Integer publicationId,
			@RequestParam String publicationType,
			@RequestParam String publicationTitle,
			@RequestParam String[] publicationAuthors,
			@RequestParam int publicationYear,
			@RequestParam(required = false) String publicationDate,
			@RequestParam(required = false) String publicationAbstract,
			@RequestParam(required = false) String publicationKeywords,
			@RequestParam(required = false) String publicationDOI,
			@RequestParam(required = false) String publicationISBN,
			@RequestParam(required = false) String publicationISSN,
			@RequestParam(required = false) String publicationURL,
			@RequestParam(required = false) String publicationVideoURL,
			@RequestParam(required = false) String publicationDblpURL,
			@RequestParam(required = false) String publicationLanguage,
			@RequestParam(required = false) MultipartFile publicationPdf,
			@RequestParam(required = false) MultipartFile publicationAward,
			@RequestParam(required = false) String volume,
			@RequestParam(required = false) String number,
			@RequestParam(required = false) String pages,
			@RequestParam(required = false) String editors,
			@RequestParam(required = false) String address,
			@RequestParam(required = false) String series,
			@RequestParam(required = false) String publisher,
			@RequestParam(required = false) String edition,
			@RequestParam(required = false) String bookTitle,
			@RequestParam(required = false) String chapterNumber,
			@RequestParam(required = false) String scientificEventName,
			@RequestParam(required = false) String organization,
			@RequestParam(required = false) String howPublished,
			@RequestParam(required = false) String documentType,
			@RequestParam(required = false) String institution) throws Exception {
		try {
			if (publicationId == null) {
				throw new IllegalArgumentException("null publication identifier"); //$NON-NLS-1$
			}
			if (publicationAuthors == null) {
				throw new IllegalArgumentException("You must specify at least one author"); //$NON-NLS-1$
			}
			final PublicationType publicationTypeEnum = PublicationType.valueOfCaseInsensitive(publicationType);
			final Date publicationDateObj;
			if (Strings.isNullOrEmpty(publicationDate)) {
				publicationDateObj = null;
			} else {
				publicationDateObj = Date.valueOf(publicationDate);
			}
			final PublicationLanguage publicationLanguageEnum = PublicationLanguage.valueOfCaseInsensitive(publicationLanguage);

			final Publication pub = this.publicationService.getPublication(publicationId.intValue());
			if (pub != null) {

				// First step: Store PDFs
				String concretePdfString = null;
				if (publicationPdf != null && !publicationPdf.isEmpty()) {
					final File filename = this.fileManager.makePdfFilename(publicationId.intValue());
					final File folder = filename.getParentFile().getAbsoluteFile();
					this.fileManager.saveFile(folder, filename.getName(), publicationPdf);
					concretePdfString = filename.getPath();
					getLogger().info("PDF uploaded at: " + concretePdfString); //$NON-NLS-1$
				}

				String concreteAwardString = null;
				if (publicationAward != null && !publicationAward.isEmpty()) {
					final File filename = this.fileManager.makeAwardFilename(publicationId.intValue());
					final File folder = filename.getParentFile().getAbsoluteFile();
					this.fileManager.saveFile(folder, filename.getName(), publicationPdf);
					concreteAwardString = filename.getPath();
					getLogger().info("Award certificate uploaded at: " + concreteAwardString); //$NON-NLS-1$
				}

				// Second step: Update the list of authors.

				final List<Person> oldAuthors = this.authorshipService.getAuthorsFor(publicationId.intValue());
				final List<Integer> oldAuthorIds = oldAuthors.stream().map(it -> Integer.valueOf(it.getId())).collect(Collectors.toList());

				int i = 0;
				for (final String publicationAuthor : publicationAuthors) {
					final String firstName = this.nameParser.parseFirstName(publicationAuthor);
					final String lastName = this.nameParser.parseLastName(publicationAuthor);
					int authorId = this.personService.getPersonIdByName(firstName, lastName);
					if (authorId == 0) {
						// The author does not exist yet
						authorId = this.personService.createPerson(firstName, lastName, null);
						getLogger().info("New person \"" + publicationAuthor + "\" created with id: " + authorId); //$NON-NLS-1$ //$NON-NLS-2$
					} else {
						oldAuthorIds.remove(Integer.valueOf(authorId));
					}
					final int finalAuthorId = authorId;
					final Optional<Person> optPerson = oldAuthors.stream().filter(it -> it.getId() == finalAuthorId).findFirst();
					if (optPerson.isPresent()) {
						// Author is already present
						this.authorshipService.updateAuthorship(authorId, publicationId.intValue(), i);
						getLogger().info("Author \"" + publicationAuthor + "\" updated for the publication with id " + publicationId); //$NON-NLS-1$ //$NON-NLS-2$
					} else {
						// Author was not associated yet
						this.authorshipService.addAuthorship(authorId, publicationId.intValue(), i);
						getLogger().info("Author \"" + publicationAuthor + "\" added to publication with id " + publicationId); //$NON-NLS-1$ //$NON-NLS-2$
					}
					i++;
				}

				// Remove the old author ships
				for (final Integer id : oldAuthorIds) {
					this.authorshipService.removeAuthorship(id.intValue(), publicationId.intValue());
				}

				// Third step : update the specific publication
				final Class<? extends Publication> publicationClass = publicationTypeEnum.getInstanceType();

				if (publicationClass.equals(Book.class)) {
					this.bookService.updateBook(
							publicationId.intValue(),
							publicationTitle, publicationTypeEnum,
							publicationDateObj, publicationAbstract, publicationKeywords,
							publicationDOI, publicationISBN, publicationISSN,
							publicationDblpURL, publicationURL, publicationLanguageEnum,
							concretePdfString, concreteAwardString, publicationVideoURL,
							volume, number, pages, edition, editors,
							series, publisher, address);
				} else if (publicationClass.equals(BookChapter.class)) {
					this.bookChapterService.updateBookChapter(
							publicationId.intValue(), 
							publicationTitle, publicationTypeEnum, publicationDateObj,
							publicationAbstract, publicationKeywords,
							publicationDOI, publicationISBN, publicationISSN,
							publicationDblpURL, publicationURL,
							publicationLanguageEnum,
							concretePdfString, concreteAwardString, publicationVideoURL,
							bookTitle, chapterNumber, edition,
							volume, number, pages, editors, series,
							publisher, address);
				} else if (publicationClass.equals(ConferencePaper.class)) {
					this.conferencePaperService.updateConferencePaper(
							publicationId.intValue(),
							publicationTitle, publicationTypeEnum, publicationDateObj,
							publicationAbstract, publicationKeywords,
							publicationDOI, publicationISBN, publicationISSN,
							publicationDblpURL, publicationURL,
							publicationLanguageEnum, concretePdfString, concreteAwardString,
							publicationVideoURL, scientificEventName, volume, number,
							pages, editors, series, organization, address);
				} else if (publicationClass.equals(JournalEdition.class)) {
					this.journalEditionService.updateJournalEdition(
							publicationId.intValue(),
							publicationTitle, publicationTypeEnum, publicationDateObj,
							publicationAbstract, publicationKeywords,
							publicationDOI, publicationISBN, publicationISSN,
							publicationDblpURL, publicationURL,
							publicationLanguageEnum, concretePdfString, concreteAwardString,
							publicationVideoURL, volume, number, pages);
				} else if (publicationClass.equals(JournalPaper.class)) {
					this.journalPaperService.updateJournalPaper(
							publicationId.intValue(),
							publicationTitle, publicationTypeEnum, publicationDateObj,
							publicationAbstract, publicationKeywords,
							publicationDOI, publicationISBN, publicationISSN,
							publicationDblpURL, publicationURL,
							publicationLanguageEnum, concretePdfString, concreteAwardString,
							publicationVideoURL, volume, number, pages);
				} else if (publicationClass.equals(KeyNote.class)) {
					this.keyNoteService.updateKeyNote(
							publicationId.intValue(),
							publicationTitle, publicationTypeEnum, publicationDateObj,
							publicationAbstract, publicationKeywords,
							publicationDOI, publicationISBN, publicationISSN,
							publicationDblpURL, publicationURL,
							publicationLanguageEnum, concretePdfString, concreteAwardString,
							publicationVideoURL, scientificEventName, editors, organization, address);
				} else if (publicationClass.equals(MiscDocument.class)) {
					this.miscDocumentService.updateMiscDocument(
							publicationId.intValue(),
							publicationTitle, publicationTypeEnum, publicationDateObj,
							publicationAbstract, publicationKeywords,
							publicationDOI, publicationISBN, publicationISSN,
							publicationDblpURL, publicationURL,
							publicationLanguageEnum, concretePdfString, concreteAwardString,
							publicationVideoURL, number, howPublished, documentType,
							organization, publisher, address);
				} else if (publicationClass.equals(Patent.class)) {
					this.patentService.updatePatent(
							publicationId.intValue(),
							publicationTitle, publicationTypeEnum, publicationDateObj,
							publicationAbstract, publicationKeywords,
							publicationDOI, publicationISBN, publicationISSN,
							publicationDblpURL, publicationURL,
							publicationLanguageEnum, concretePdfString, concreteAwardString,
							publicationVideoURL, number, documentType, institution, address);
				} else if (publicationClass.equals(Report.class)) {
					this.reportService.updateReport(
							publicationId.intValue(),
							publicationTitle, publicationTypeEnum, publicationDateObj,
							publicationAbstract, publicationKeywords,
							publicationDOI, publicationISBN, publicationISSN,
							publicationDblpURL, publicationURL,
							publicationLanguageEnum, concretePdfString, concreteAwardString,
							publicationVideoURL, number, documentType, institution, address);
				} else if (publicationClass.equals(Thesis.class)) {
					this.thesisService.updateThesis(
							publicationId.intValue(),
							publicationTitle, publicationTypeEnum, publicationDateObj,
							publicationAbstract, publicationKeywords,
							publicationDOI, publicationISBN, publicationISSN,
							publicationDblpURL, publicationURL,
							publicationLanguageEnum, concretePdfString, concreteAwardString,
							publicationVideoURL, institution, address);
				} else {
					throw new IllegalArgumentException("Unsupported publication type: " + publicationType); //$NON-NLS-1$
				}
				getLogger().info("Publication instance updated: " + publicationId); //$NON-NLS-1$
			}
			redirectToAddPublicationWithEditState(response, publicationId.intValue());
		} catch (Exception ex) {
			redirectError(response, ex);
		}
	}

	/** Import publications from a BibTeX string. The format of the BibTeX is a standard that is briefly described
	 * on {@link "https://en.wikipedia.org/wiki/BibTeX"}.
	 * If multiple BibTeX entries are defined into the given input string, each of them is subject
	 * of an importation tentative. If the import process is successful, the database identifier of the publication
	 * is replied.
	 *
	 * @param response the HTTP response.
	 * @param bibtex the string that contains the BibTeX description of the publications.
	 * @return the list of the identifiers of the publications that are successfully imported.
	 * @throws Exception if it is impossible to redirect to the error page.
	 * @see BibTeX
	 * @see "https://en.wikipedia.org/wiki/BibTeX"
	 */
	@PostMapping(value = "/importPublications", headers = "Accept=application/json")
	public List<Integer> importPublications(HttpServletResponse response, String bibtex) throws Exception {
		try {
			return this.publicationService.importPublications(bibtex);
		} catch (Exception ex) {
			redirectError(response, ex);
			return Collections.emptyList();
		}
	}

	/** Import publications from a BibTeX string. The format of the BibTeX is a standard that is briefly described
	 * on {@link "https://en.wikipedia.org/wiki/BibTeX"}.
	 * If multiple BibTeX entries are defined into the given input string, each of them is subject
	 * of an importation tentative. If the import process is successful, the database identifier of the publication
	 * is replied.
	 *
	 * @param response the HTTP response.
	 * @param bibtexFile the file that contains the BibTeX description of the publications.
	 * @throws Exception if it is impossible to import the BibTeX data from the given source file.
	 * @see BibTeX
	 * @see "https://en.wikipedia.org/wiki/BibTeX"
	 */
	@PostMapping(value = "/importBibTeX", headers = "Accept=application/json")
	public void importBibTeX(HttpServletResponse response, MultipartFile bibtexFile) throws Exception {
		try {
			if (bibtexFile != null && !bibtexFile.isEmpty()) {
				final String bibtexContent = this.fileManager.readTextFile(bibtexFile);
				getLogger().debug("BibTeX file read : \n" + bibtexContent); //$NON-NLS-1$
				final List<Integer> publications = importPublications(response, bibtexContent);
				redirectToAddPublicationWithSuccessState(response, publications.size());
			} else {
				throw new Exception("BibTeX input file not provided"); //$NON-NLS-1$
			}
		} catch (Exception ex) {
			redirectError(response, ex);
		}
	}

	/** Read a BibTeX source and redirect to the publication addition form.
	 *
	 * @param response the HTTP response
	 * @param bibtexFile the BibTeX file to read.
	 * @param redirectAttributes definition of the redirection attributes.
	 * @return the redirection view.
	 * @throws Exception if the BibTeX source cannot be read.
	 */
	@PostMapping(value = "/bibTeXToAddPublication", headers = "Accept=application/json")
	public RedirectView bibTeXToAddPublication(HttpServletResponse response, MultipartFile bibtexFile, RedirectAttributes redirectAttributes) throws Exception {
		try {
			if (bibtexFile != null && !bibtexFile.isEmpty()) {
				final String bibtexContent = this.fileManager.readTextFile(bibtexFile);
				redirectAttributes.addFlashAttribute("bibtex", bibtexContent); //$NON-NLS-1$
				redirectAttributes.addFlashAttribute("publicationService", this.publicationService); //$NON-NLS-1$
				return this.viewFactory.newRedirectView("/addPublication?filling=true", true); //$NON-NLS-1$
			}
			throw new Exception("BibTeX input file not provided"); //$NON-NLS-1$
		} catch (Exception ex) {
			redirectError(response, ex);
			return null;
		}
	}

	/**
	 * Export function for BibTeX using a list of publication identifiers.
	 *
	 * @param identifiers the array of publication identifiers that should be exported.
	 * @return the BibTeX description of the publications with the given identifiers, or {@code null}
	 *      if there is no publication to export.
	 */
	@PostMapping(value = "/exportBibTeX", headers = "Accept=application/json")
	public String exportBibTeX(Integer[] identifiers) {
		if (identifiers == null) {
			return null;
		}
		return this.publicationService.exportBibTeX(Arrays.asList(identifiers).stream());
	}

	/**
	 * Export function for HTML using a list of publication identifiers.
	 *
	 * @param response the HTTP response.
	 * @param identifiers the array of publication identifiers that should be exported.
	 * @param organizationId the identifier of the organization for which the publications must be exported.
	 *     Providing this identifier will have an effect on the formatting of the authors' names.
	 * @param authorId the identifier of the author for who the publications must be exported.
	 *     Providing this identifier will have an effect on the formatting of the authors' names.
	 * @return the HTML description of the publications with the given identifiers, or {@code null}
	 *      if there is no publication to export.
	 * @throws Exception if it is impossible to redirect to the error page.
	 */
	@PostMapping(value = "/exportHtml", headers = "Accept=application/json")
	public String exportHtml(HttpServletResponse response, Integer[] identifiers,
			@RequestParam(required = false) Integer organizationId,
			@RequestParam(required = false) Integer authorId) throws Exception {
		if (identifiers == null) {
			return null;
		}
		final ExporterConfigurator configurator = new ExporterConfigurator();
		if (organizationId != null) {
			configurator.selectOrganization(it -> it.getId() == organizationId.intValue());
		}
		if (authorId != null) {
			configurator.selectPerson(it -> it.getId() == authorId.intValue());
		}
		try {
			return this.publicationService.exportHtml(Arrays.asList(identifiers).stream(), configurator);
		} catch (Exception ex) {
			redirectError(response, ex);
			return null;
		}
	}

	/**
	 * Export function for Open Document Text (ODT) using a list of publication identifiers.
	 *
	 * @param response the HTTP response.
	 * @param identifiers the array of publication identifiers that should be exported.
	 * @param organizationId the identifier of the organization for which the publications must be exported.
	 *     Providing this identifier will have an effect on the formatting of the authors' names.
	 * @param authorId the identifier of the author for who the publications must be exported.
	 *     Providing this identifier will have an effect on the formatting of the authors' names.
	 * @return the ODT description of the publications with the given identifiers, or {@code null}
	 *      if there is no publication to export.
	 * @throws Exception if it is impossible to redirect to the error page.
	 */
	@PostMapping(value = "/exportOdt", headers = "Accept=application/vnd.oasis.opendocument.text")
	public byte[] exportOdt(HttpServletResponse response, Integer[] identifiers,
			@RequestParam(required = false) Integer organizationId,
			@RequestParam(required = false) Integer authorId) throws Exception {
		if (identifiers == null) {
			return null;
		}
		final ExporterConfigurator configurator = new ExporterConfigurator();
		if (organizationId != null) {
			configurator.selectOrganization(it -> it.getId() == organizationId.intValue());
		}
		if (authorId != null) {
			configurator.selectPerson(it -> it.getId() == authorId.intValue());
		}
		try {
			return this.publicationService.exportOdt(Arrays.asList(identifiers).stream(), configurator);
		} catch (Exception ex) {
			redirectError(response, ex);
			return null;
		}
	}

	/** Replies the statistics for the publications and for the author with the given identifier.
	 *
	 * @param identifier the identifier of the author. If it is not provided, all the publications are considered.
	 * @return the model-view with the statistics.
	 */
	@GetMapping("/publicationsStats")
	public ModelAndView showPublicationsStats(
			@RequestParam(required = false) Integer identifier) {
		final ModelAndView modelAndView = new ModelAndView("publicationsStats"); //$NON-NLS-1$

		final List<Publication> publications;
		if (identifier == null) {
			publications = this.publicationService.getAllPublications();
		} else {
			publications = this.authorshipService.getPublicationsFor(identifier.intValue());
		}

		final Map<Integer, PublicationsStat> statsPerYear = new TreeMap<>();
		final PublicationsStat globalStats = new PublicationsStat(Integer.MIN_VALUE);

		for (final Publication p : publications) {
			final Integer y = Integer.valueOf(p.getPublicationYear());
			final PublicationsStat stats = statsPerYear.computeIfAbsent(y,
					it -> new PublicationsStat(it.intValue()));
			stats.incrementCountForType(p.getType(), p.isRanked(), 1);
			globalStats.incrementCountForType(p.getType(), p.isRanked(), 1);
		}

		modelAndView.addObject("stats", statsPerYear); //$NON-NLS-1$
		modelAndView.addObject("globalStats", globalStats); //$NON-NLS-1$
		return modelAndView;
	}

}
