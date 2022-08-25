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

package fr.ciadlab.labmanager.controller.view.publication;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.member.PersonComparator;
import fr.ciadlab.labmanager.entities.publication.JournalBasedPublication;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.service.journal.JournalService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import fr.ciadlab.labmanager.utils.RequiredFieldInForm;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

/** REST Controller for publications' views.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@RestController
@CrossOrigin
public class PublicationViewController extends AbstractViewController {

	private static final String DEFAULT_ENDPOINT = "publicationList"; //$NON-NLS-1$

	private PublicationService publicationService;

	private PersonService personService;

	private PersonComparator personComparator;

	private DownloadableFileManager fileManager;

	private JournalService journalService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of messages.
	 * @param publicationService the publication service.
	 * @param personService the person service.
	 * @param personComparator the comparator of persons.
	 * @param fileManager the manager of local files.
	 * @param journalService the tools for manipulating journals.
	 */
	public PublicationViewController(
			@Autowired MessageSourceAccessor messages,
			@Autowired PublicationService publicationService,
			@Autowired PersonService personService,
			@Autowired PersonComparator personComparator,
			@Autowired DownloadableFileManager fileManager,
			@Autowired JournalService journalService) {
		super(messages);
		this.publicationService = publicationService;
		this.personService = personService;
		this.personComparator = personComparator;
		this.fileManager = fileManager;
		this.journalService = journalService;
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

	private String buildUri(UriBuilderFactory factory, Integer organization, Integer author,
			Integer journal, String endpoint) {
		UriBuilder uriBuilder = factory.builder();
		uriBuilder = uriBuilder.path("/" + getApplicationConstants().getServerName() + "/" + endpoint); //$NON-NLS-1$ //$NON-NLS-2$
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

}
