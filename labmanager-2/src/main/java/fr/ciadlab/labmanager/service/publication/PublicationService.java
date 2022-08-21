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

package fr.ciadlab.labmanager.service.publication;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.entities.publication.JournalBasedPublication;
import fr.ciadlab.labmanager.entities.publication.Publication;
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
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.io.bibtex.BibTeX;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.io.html.HtmlDocumentExporter;
import fr.ciadlab.labmanager.io.json.JsonExporter;
import fr.ciadlab.labmanager.io.od.OpenDocumentTextExporter;
import fr.ciadlab.labmanager.repository.journal.JournalRepository;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.publication.AuthorshipRepository;
import fr.ciadlab.labmanager.repository.publication.PublicationRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import fr.ciadlab.labmanager.service.member.PersonService;
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
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/** Service for managing the publications.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class PublicationService extends AbstractService {

	private PublicationRepository publicationRepository;

	private AuthorshipService authorshipService;

	private AuthorshipRepository authorshipRepository;

	private JournalRepository journalRepository;

	private PersonRepository personRepository;

	private PersonService personService;

	private PersonNameParser nameParser;

	private BibTeX bibtex;

	private HtmlDocumentExporter html;

	private OpenDocumentTextExporter odt;

	private JsonExporter json;

	private DownloadableFileManager fileManager;

	private PrePublicationFactory prePublicationFactory;

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
	 * @param messages the provider of localized messages.
	 * @param publicationRepository the publication repository.
	 * @param prePublicationFactory factory of pre-publications.
	 * @param authorshipService the service for managing the authorships.
	 * @param authorshipRepository authorshipRepository the repository of authorships.
	 * @param personService the service for managing the persons.
	 * @param personRepository the repository of the persons.
	 * @param journalRepository the repository of the journals.
	 * @param nameParser the parser of person names.
	 * @param bibtex the tool for managing BibTeX source.
	 * @param html the tool for exporting to HTML.
	 * @param odt the tool for exporting to Open Document Text.
	 * @param json the tool for exporting to JSON.
	 * @param fileManager the manager of downloadable files.
	 * @param bookService the service for books.
	 * @param bookChapterService the service for book chapters.
	 * @param conferencePaperService the service for conference papers.
	 * @param journalEditionService the service for journal editions.
	 * @param journalPaperService the service for journal papers.
	 * @param keyNoteService the service for key-notes.
	 * @param miscDocumentService the service for misc documents.
	 * @param patentService the service for patents.
	 * @param reportService the service for reports.
	 * @param thesisService the service for theses.
	 */
	public PublicationService(
			@Autowired MessageSourceAccessor messages,
			@Autowired PublicationRepository publicationRepository,
			@Autowired PrePublicationFactory prePublicationFactory,
			@Autowired AuthorshipService authorshipService,
			@Autowired AuthorshipRepository authorshipRepository,
			@Autowired PersonService personService, @Autowired PersonRepository personRepository,
			@Autowired JournalRepository journalRepository,
			@Autowired PersonNameParser nameParser,
			@Autowired BibTeX bibtex,
			@Autowired HtmlDocumentExporter html,
			@Autowired OpenDocumentTextExporter odt,
			@Autowired JsonExporter json,
			@Autowired DownloadableFileManager fileManager,
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
		super(messages);
		this.publicationRepository = publicationRepository;
		this.prePublicationFactory = prePublicationFactory;
		this.authorshipService = authorshipService;
		this.authorshipRepository = authorshipRepository;
		this.personRepository = personRepository;
		this.personService = personService;
		this.journalRepository = journalRepository;
		this.nameParser = nameParser;
		this.bibtex = bibtex;
		this.html = html;
		this.odt = odt;
		this.json = json;
		this.fileManager = fileManager;
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

	/** Replies all the publications from the database.
	 *
	 * @return the publications.
	 */
	public List<Publication> getAllPublications() {
		return this.publicationRepository.findAll();
	}

	/** Replies all the publications from the database that are attached to the given person.
	 *
	 * @param identifier the identifier of the person.
	 * @return the publications.
	 */
	public List<Publication> getPublicationsByPersonId(int identifier) {
		return this.publicationRepository.findAllByAuthorshipsPersonId(identifier);
	}

	/** Replies all the publications from the database that are attached to the given organization.
	 *
	 * @param identifier the identifier of the organization.
	 * @return the publications.
	 */
	public List<Publication> getPublicationsByOrganizationId(int identifier) {
		return this.publicationRepository.findAllByAuthorshipsPersonMembershipsResearchOrganizationId(identifier);
	}

	/** Replies the publication with the given identifier.
	 *
	 * @param identifier the identifier of the publication.
	 * @return the publication, or {@code null} if not found.
	 */
	public Publication getPublicationById(int identifier) {
		final Optional<Publication> byId = this.publicationRepository.findById(Integer.valueOf(identifier));
		return byId.orElse(null);
	}

	/** Replies the publications with the given identifiers.
	 *
	 * @param identifiers the identifiers of the publications.
	 * @return the publications.
	 */
	public List<Publication> getPublicationsByIds(List<Integer> identifiers) {
		return this.publicationRepository.findAllById(identifiers);
	}

	/** Replies the publications with the given title.
	 *
	 * @param title the title of the publications.
	 * @return the publications.
	 */
	public List<Publication> getPublicationsByTitle(String title) {
		if (Strings.isNullOrEmpty(title)) {
			return Collections.emptyList();
		}
		return this.publicationRepository.findAllByTitle(title);
	}

	/** Remove the publication with the given identifier.
	 *
	 * @param identifier the identifier of the publication to remove.
	 * @param removeAssociatedFiles indicates if the associated files (PDF, Award...) should be also deleted.
	 */
	public void removePublication(int identifier, boolean removeAssociatedFiles) {
		final Integer id = Integer.valueOf(identifier);
		final Optional<Publication> optPublication = this.publicationRepository.findById(Integer.valueOf(identifier));
		if (optPublication.isPresent()) {
			final Publication publication = optPublication.get();
			final Iterator<Authorship> iterator = publication.getAuthorships().iterator();
			while (iterator.hasNext()) {
				Authorship autship = iterator.next();
				final Person person = autship.getPerson();
				if (person != null) {
					person.getAuthorships().remove(autship);
					autship.setPerson(null);
					this.personRepository.save(person);
				}
				autship.setPublication(null);
				iterator.remove();
				this.authorshipRepository.save(autship);
			}
			publication.getAuthorshipsRaw().clear();
			this.publicationRepository.deleteById(id);
			if (removeAssociatedFiles) {
				try {
					this.fileManager.deleteDownloadablePublicationPdfFile(identifier);
				} catch (Throwable ex) {
					// Silent
				}
				try {
					this.fileManager.deleteDownloadableAwardPdfFile(identifier);
				} catch (Throwable ex) {
					// Silent
				}
			}
		}
	}

	/** Save the given publications into the database.
	 * If one publication has a temporary list of authors, the corresponding authors are
	 * explicitly created into the database.
	 *
	 * @param publications the list of publications to save in the database.
	 */
	public void save(Publication... publications) {
		save(Arrays.asList(publications));
	}

	/** Save the given publications into the database.
	 * If one publication has a temporary list of authors, the corresponding authors are
	 * explicitly created into the database.
	 *
	 * @param publications the list of publications to save in the database.
	 */
	public void save(List<? extends Publication> publications) {
		for (final Publication publication : publications) {
			final List<Person> authors = publication.getTemporaryAuthors();
			publication.setTemporaryAuthors(null);
			this.publicationRepository.save(publication);
			if (publication instanceof JournalBasedPublication) {
				final Journal jour = ((JournalBasedPublication) publication).getJournal();
				if (jour != null) {
					this.journalRepository.save(jour);
				}
			}
			if (authors != null) {
				// Create the list of authors from the temporary (not yet saved) list. 
				int rank = 0;
				for (final Person author : authors) {
					this.personRepository.save(author);
					this.authorshipService.addAuthorship(author.getId(), publication.getId(), rank, false);
					++rank;
				}
			}
		}
	}

	/** Import publications from a BibTeX string. The format of the BibTeX is a standard that is briefly described
	 * on {@link "https://en.wikipedia.org/wiki/BibTeX"}.
	 * If multiple BibTeX entries are defined into the given input string, each of them is subject
	 * of an importation tentative. If the import process is successful, the database identifier of the publication
	 * is replied.
	 *
	 * @param bibtex the string that contains the BibTeX description of the publications.
	 * @return the list of the identifiers of the publications that are successfully imported.
	 * @throws Exception if it is impossible to parse the given BibTeX source.
	 * @see BibTeX
	 * @see "https://en.wikipedia.org/wiki/BibTeX"
	 */
	public List<Integer> importPublications(String bibtex) throws Exception {
		// Holds the publications that we are trying to import.
		// The publications are not yet imported into the database.
		final List<Publication> importablePublications = this.bibtex.extractPublications(bibtex);

		//Holds the IDs of the successfully imported IDs. We'll need it for type differenciation later.
		final List<Integer> importedPublicationIdentifiers = new ArrayList<>();

		//We are going to try to import every publication in the list
		for (final Publication publication : importablePublications) {
			try {
				// Add the publication to the database and get the new assigned identifier
				this.publicationRepository.save(publication);
				final int publicationId = publication.getId();
				final Integer publicationIdObj = Integer.valueOf(publicationId);

				// Adding the id of the current publication to the list
				importedPublicationIdentifiers.add(publicationIdObj);

				// For every authors assigned to this publication, save them into the database
				final List<Person> authors = publication.getAuthors();
				publication.setTemporaryAuthors(null);
				int rank = 0;
				for (final Person author : authors) {
					try {
						// Search for a person with a "similar name"
						int personId = this.personService.getPersonIdBySimilarName(
								author.getFirstName(), author.getLastName());
						// Create new author if is not inside the database.
						// If we've already got the author with the abbreviated first name in DB, 
						// but the one parsed have the full version, it creates a new author
						if (personId == 0) {
							this.personRepository.save(author);
							personId = author.getId();
						}
						// Assigning authorship
						this.authorshipService.addAuthorship(personId, publicationId, rank, false);
						this.publicationRepository.save(publication);

						// Check if the newly imported pub has at least one authorship.
						// If not, it's a bad case and the pub have to be removed and marked as failed
						if (this.authorshipService.getAuthorsFor(publicationId).isEmpty()) {
							throw new IllegalArgumentException("No author for publication id=" + publicationId); //$NON-NLS-1$
						}
						++rank;
					} catch (Exception ex) {
						// Even if a larger try catch for exceptions exists, we need to delete
						// first the imported publication and linked authorship
						importedPublicationIdentifiers.remove(publicationIdObj);
						for (final Authorship toRemove : this.authorshipRepository.findByPublicationId(publicationId)) {
							this.authorshipRepository.deleteById(Integer.valueOf(toRemove.getId()));
						}
						this.publicationRepository.deleteById(publicationIdObj);
						throw ex;
					}
				}	
			} catch(Exception ex) {
				getLogger().error("Error while importing Bibtext publication\nData :\n" + publication + "\nException :", ex); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return importedPublicationIdentifiers;
	}

	/**
	 * Export function for BibTeX using a list of publication identifiers.
	 *
	 * @param publications the array of publications that should be exported.
	 * @param configurator the configurator of the exporter.
	 * @return the BibTeX description of the publications with the given identifiers.
	 */
	public String exportBibTeX(Iterable<? extends Publication> publications, ExporterConfigurator configurator) {
		if (publications == null) {
			return null;
		}
		return this.bibtex.exportPublications(publications, configurator);
	}

	/**
	 * Export function for HTML using a list of publication identifiers.
	 *
	 * @param publications the array of publications that should be exported.
	 * @param configurator the configurator of the exporter.
	 * @return the HTML description of the publications with the given identifiers.
	 * @throws Exception if it is impossible to generate the HTML for the publications.
	 */
	public String exportHtml(Iterable<? extends Publication> publications, ExporterConfigurator configurator) throws Exception {
		if (publications == null) {
			return null;
		}
		return this.html.exportPublications(publications, configurator);
	}

	/**
	 * Export function for Open Document Text using a list of publication identifiers.
	 *
	 * @param publications the array of publications that should be exported.
	 * @param configurator the configurator of the exporter.
	 * @return the ODT description of the publications with the given identifiers.
	 * @throws Exception if it is impossible to generate the ODT for the publications.
	 */
	public byte[] exportOdt(Iterable<? extends Publication> publications, ExporterConfigurator configurator) throws Exception {
		if (publications == null) {
			return null;
		}
		return this.odt.exportPublications(publications, configurator);
	}

	/**
	 * Export function for JSON using a list of publication identifiers.
	 *
	 * @param publications the array of publications that should be exported.
	 * @param configurator the configurator of the exporter.
	 * @param rootKeys the sequence of keys for building the root of the tree. The exported data is then
	 *     output into the last created node with the {@code rootKeys}.
	 * @return the JSON description of the publications with the given identifiers.
	 * @throws Exception if it is impossible to generate the JSON for the publications.
	 */
	public String exportJson(Iterable<? extends Publication> publications, ExporterConfigurator configurator, String... rootKeys) throws Exception {
		if (publications == null) {
			return null;
		}
		return this.json.exportPublicationsWithRootKeys(publications, configurator, rootKeys);
	}

	/** Get the journal instance that is corresponding to the identifier from the given map for an attribute with the given name.
	 * <p>This function generates an exception if the journal is {@code null}.
	 *
	 * @param attributes the set of attributes
	 * @param name the name to search for.
	 * @return the value
	 */
	protected Journal ensureJournalInstance(Map<String, String> attributes, String name) {
		final String journalIdStr = ensureString(attributes, name);
		if (Strings.isNullOrEmpty(journalIdStr)) {
			throw new IllegalArgumentException("Missed journal: " + name); //$NON-NLS-1$
		}
		int journalId;
		try {
			journalId = Integer.parseInt(journalIdStr);
		} catch (Throwable ex) {
			journalId = 0;
		}
		if (journalId == 0) {
			throw new IllegalArgumentException("Missed journal: " + name); //$NON-NLS-1$
		}
		final Optional<Journal> optJournal = this.journalRepository.findById(Integer.valueOf(journalId));
		if (optJournal.isEmpty()) {
			throw new IllegalArgumentException("Unknown journal: " + name); //$NON-NLS-1$
		}
		return optJournal.get();
	}

	/** Create a publication in the database from values stored in the given map.
	 * This function ignore the attributes related to uploaded files.
	 *
	 * @param attributes the values of the attributes for the publication's creation.
	 * @param authors the list of authors. It is a list of database identifiers (for known persons) and full name
	 *     (for unknown persons).
	 * @param downloadablePDF the uploaded PDF file for the publication.
	 * @param downloadableAwardCertificate the uploaded Award certificate for the publication.
	 * @return the created publication.
	 * @throws IOException if the uploaded files cannot be treated correctly.
	 */
	public Optional<Publication> createPublicationFromMap(Map<String, String> attributes,
			List<String> authors, MultipartFile downloadablePDF, MultipartFile downloadableAwardCertificate) throws IOException {
		final PublicationType typeEnum = PublicationType.valueOfCaseInsensitive(ensureString(attributes, "type")); //$NON-NLS-1$
		final PublicationLanguage languageEnum = PublicationLanguage.valueOfCaseInsensitive(ensureString(attributes, "majorLanguage")); //$NON-NLS-1$
		final LocalDate date = ensureDate(attributes, "publicationDate"); //$NON-NLS-1$

		// First step : create the publication
		final Publication publication = this.prePublicationFactory.createPrePublication(
				typeEnum,
				ensureString(attributes, "title"), //$NON-NLS-1$
				optionalString(attributes, "abstractText"), //$NON-NLS-1$
				optionalString(attributes, "keywords"), //$NON-NLS-1$
				date,
				optionalString(attributes, "isbn"), //$NON-NLS-1$
				optionalString(attributes, "issn"), //$NON-NLS-1$
				optionalString(attributes, "doi"), //$NON-NLS-1$
				optionalString(attributes, "halId"), //$NON-NLS-1$
				optionalString(attributes, "extraURL"), //$NON-NLS-1$
				optionalString(attributes, "videoURL"), //$NON-NLS-1$
				optionalString(attributes, "dblpURL"), //$NON-NLS-1$
				optionalString(attributes, "pathToDownloadablePDF"), //$NON-NLS-1$
				optionalString(attributes, "pathToDownloadableAwardCertificate"), //$NON-NLS-1$
				languageEnum);

		// Second step: save late attributes of the fake publication
		publication.setPublicationYear(date.getYear());

		// Third step : create the specific publication type
		final Class<? extends Publication> publicationClass = typeEnum.getInstanceType();
		final Publication createdPublication;

		if (publicationClass.equals(Book.class)) {
			createdPublication = this.bookService.createBook(publication,
					optionalString(attributes, "volume"), //$NON-NLS-1$
					optionalString(attributes, "number"), //$NON-NLS-1$
					optionalString(attributes, "pages"), //$NON-NLS-1$
					optionalString(attributes, "edition"), //$NON-NLS-1$
					optionalString(attributes, "editors"), //$NON-NLS-1$
					optionalString(attributes, "series"), //$NON-NLS-1$
					optionalString(attributes, "publisher"), //$NON-NLS-1$
					optionalString(attributes, "address")); //$NON-NLS-1$
		} else if (publicationClass.equals(BookChapter.class)) {
			createdPublication = this.bookChapterService.createBookChapter(publication,
					ensureString(attributes, "bookTitle"), //$NON-NLS-1$
					optionalString(attributes, "chapterNumber"), //$NON-NLS-1$
					optionalString(attributes, "edition"), //$NON-NLS-1$
					optionalString(attributes, "volume"), //$NON-NLS-1$
					optionalString(attributes, "number"), //$NON-NLS-1$
					optionalString(attributes, "pages"), //$NON-NLS-1$
					optionalString(attributes, "editors"), //$NON-NLS-1$
					optionalString(attributes, "series"), //$NON-NLS-1$
					optionalString(attributes, "publisher"), //$NON-NLS-1$
					optionalString(attributes, "address")); //$NON-NLS-1$
		} else if (publicationClass.equals(ConferencePaper.class)) {
			createdPublication = this.conferencePaperService.createConferencePaper(publication,
					ensureString(attributes, "scientificEventName"), //$NON-NLS-1$
					optionalString(attributes, "volume"), //$NON-NLS-1$
					optionalString(attributes, "number"), //$NON-NLS-1$
					optionalString(attributes, "pages"), //$NON-NLS-1$
					optionalString(attributes, "editors"), //$NON-NLS-1$
					optionalString(attributes, "series"), //$NON-NLS-1$
					optionalString(attributes, "organization"), //$NON-NLS-1$
					optionalString(attributes, "address"), //$NON-NLS-1$
					optionalString(attributes, "publisher")); //$NON-NLS-1$
		} else if (publicationClass.equals(JournalEdition.class)) {
			createdPublication = this.journalEditionService.createJournalEdition(publication,
					optionalString(attributes, "volume"), //$NON-NLS-1$
					optionalString(attributes, "number"), //$NON-NLS-1$
					optionalString(attributes, "pages"), //$NON-NLS-1$
					ensureJournalInstance(attributes, "journal")); //$NON-NLS-1$
		} else if (publicationClass.equals(JournalPaper.class)) {
			createdPublication = this.journalPaperService.createJournalPaper(publication,
					optionalString(attributes, "volume"), //$NON-NLS-1$
					optionalString(attributes, "number"), //$NON-NLS-1$
					optionalString(attributes, "pages"), //$NON-NLS-1$
					optionalString(attributes, "series"), //$NON-NLS-1$
					ensureJournalInstance(attributes, "journal")); //$NON-NLS-1$
		} else if (publicationClass.equals(KeyNote.class)) {
			createdPublication = this.keyNoteService.createKeyNote(publication,
					ensureString(attributes, "scientificEventName"), //$NON-NLS-1$
					optionalString(attributes, "editors"), //$NON-NLS-1$
					optionalString(attributes, "organization"), //$NON-NLS-1$
					optionalString(attributes, "address")); //$NON-NLS-1$
		} else if (publicationClass.equals(MiscDocument.class)) {
			createdPublication = this.miscDocumentService.createMiscDocument(publication,
					optionalString(attributes, "number"), //$NON-NLS-1$
					ensureString(attributes, "howPublished"), //$NON-NLS-1$
					optionalString(attributes, "documentType"), //$NON-NLS-1$
					optionalString(attributes, "organization"), //$NON-NLS-1$
					optionalString(attributes, "publisher"), //$NON-NLS-1$
					optionalString(attributes, "address")); //$NON-NLS-1$
		} else if (publicationClass.equals(Patent.class)) {
			createdPublication = this.patentService.createPatent(publication,
					optionalString(attributes, "number"), //$NON-NLS-1$
					optionalString(attributes, "documentType"), //$NON-NLS-1$
					ensureString(attributes, "institution"), //$NON-NLS-1$
					optionalString(attributes, "address")); //$NON-NLS-1$
		} else if (publicationClass.equals(Report.class)) {
			createdPublication = this.reportService.createReport(publication,
					optionalString(attributes, "number"), //$NON-NLS-1$
					optionalString(attributes, "documentType"), //$NON-NLS-1$
					ensureString(attributes, "institution"), //$NON-NLS-1$
					optionalString(attributes, "address")); //$NON-NLS-1$
		} else if (publicationClass.equals(Thesis.class)) {
			createdPublication = this.thesisService.createThesis(publication,
					ensureString(attributes, "institution"), //$NON-NLS-1$
					optionalString(attributes, "address")); //$NON-NLS-1$
		} else {
			throw new IllegalArgumentException("Unsupported publication type: " + typeEnum); //$NON-NLS-1$
		}

		// Fourth step: create the authors and link them to the publication
		updateAuthorList(true, createdPublication, authors);

		// Fifth step: update the associated PDF files
		updateUploadedPDFs(createdPublication, attributes, downloadablePDF, downloadableAwardCertificate, true);

		getLogger().info("Publication instance " + createdPublication.getId() + " created of type " + publicationClass.getSimpleName()); //$NON-NLS-1$ //$NON-NLS-2$

		return Optional.of(createdPublication);
	}

	/** Update an existing publication in the database from values stored in the given map.
	 *
	 * @param id the identifier of the publication.
	 * @param attributes the values of the attributes for the publication's creation.
	 * @param authors the list of authors. It is a list of database identifiers (for known persons) and full name
	 *     (for unknown persons).
	 * @param downloadablePDF the uploaded PDF file for the publication.
	 * @param downloadableAwardCertificate the uploaded Award certificate for the publication.
	 * @return the updated publication.
	 * @throws IOException if the uploaded files cannot be treated correctly.
	 */
	public Optional<Publication> updatePublicationFromMap(int id, Map<String, String> attributes,
			List<String> authors, MultipartFile downloadablePDF, MultipartFile downloadableAwardCertificate) throws IOException {
		final PublicationType typeEnum = PublicationType.valueOfCaseInsensitive(ensureString(attributes, "type")); //$NON-NLS-1$
		// First step : find the publication
		Optional<Publication> optPublication = this.publicationRepository.findById(Integer.valueOf(id));
		if (optPublication.isEmpty()) {
			throw new IllegalArgumentException("Publication not found with id: " + id); //$NON-NLS-1$
		}
		final Publication publication = optPublication.get();
		// Second step: check for any change of publication type
		if (isInstanceTypeChangeNeeded(publication, typeEnum)) {
			removePublication(id, false);
			optPublication = createPublicationFromMap(attributes, authors, downloadablePDF, downloadableAwardCertificate);
			if (optPublication.isPresent()) {
				final Publication newPublication = optPublication.get();
				final int newId = newPublication.getId();
				final MutableBoolean associatedFilesChanged = new MutableBoolean(false);
				this.fileManager.moveFiles(id, newId, (key, source, target) -> {
					switch (key) {
					case "pdf": //$NON-NLS-1$
						if (downloadablePDF != null && downloadablePDF.isEmpty()) {
							// PDF file was associated and no new one was provided in the POST query.
							// Re-associate the previously associated file.
							newPublication.setPathToDownloadablePDF(target);
							associatedFilesChanged.setTrue();
						}
						break;
					case "award": //$NON-NLS-1$
						if (downloadableAwardCertificate != null && downloadableAwardCertificate.isEmpty()) {
							// PDF file was associated and no new one was provided in the POST query.
							// Re-associate the previously associated file.
							newPublication.setPathToDownloadableAwardCertificate(target);
							associatedFilesChanged.setTrue();
						}
						break;
					default:
						// silent
					}
				});
				if (associatedFilesChanged.isTrue()) {
					save(newPublication);
				}
			}
			return optPublication;
		}
		// Third step: update of an existing publication
		updateExistingPublicationFromMap(publication, typeEnum, attributes, authors, downloadablePDF, downloadableAwardCertificate);
		return optPublication;
	}

	private static boolean isInstanceTypeChangeNeeded(Publication publication, PublicationType expectedType) {
		final Class<? extends Publication> clazz = expectedType.getInstanceType();
		return !clazz.isInstance(publication);
	}

	/** Update an existing publication in the database from values stored in the given map.
	 *
	 * @param publication the publication.
	 * @param type the type of the publication to be set-up.
	 * @param attributes the values of the attributes for the publication's creation.
	 * @param authors the list of authors. It is a list of database identifiers (for known persons) and full name
	 *     (for unknown persons).
	 * @param downloadablePDF the uploaded PDF file for the publication.
	 * @param downloadableAwardCertificate the uploaded Award certificate for the publication.
	 * @throws IOException if the uploaded files cannot be treated correctly.
	 */
	protected void updateExistingPublicationFromMap(Publication publication, PublicationType type, Map<String, String> attributes,
			List<String> authors, MultipartFile downloadablePDF, MultipartFile downloadableAwardCertificate) throws IOException {
		final PublicationLanguage languageEnum = PublicationLanguage.valueOfCaseInsensitive(ensureString(attributes, "majorLanguage")); //$NON-NLS-1$
		final LocalDate date = ensureDate(attributes, "publicationDate"); //$NON-NLS-1$


		// First step: Update the list of authors.
		updateAuthorList(false, publication, authors);

		// Second step: treat associated files
		updateUploadedPDFs(publication, attributes, downloadablePDF, downloadableAwardCertificate, false);

		// Third step : update the specific publication
		final Class<? extends Publication> publicationClass = type.getInstanceType();

		if (publicationClass.equals(Book.class)) {
			this.bookService.updateBook(
					publication.getId(),
					ensureString(attributes, "title"), //$NON-NLS-1$
					type,
					date,
					optionalString(attributes, "abstractText"), //$NON-NLS-1$
					optionalString(attributes, "keywords"), //$NON-NLS-1$
					optionalString(attributes, "doi"), //$NON-NLS-1$
					optionalString(attributes, "isbn"), //$NON-NLS-1$
					optionalString(attributes, "issn"), //$NON-NLS-1$
					optionalString(attributes, "dblpURL"), //$NON-NLS-1$
					optionalString(attributes, "extraURL"), //$NON-NLS-1$
					languageEnum,
					publication.getPathToDownloadablePDF(),
					publication.getPathToDownloadableAwardCertificate(),
					optionalString(attributes, "videoURL"), //$NON-NLS-1$
					optionalString(attributes, "volume"), //$NON-NLS-1$
					optionalString(attributes, "number"), //$NON-NLS-1$
					optionalString(attributes, "pages"), //$NON-NLS-1$
					optionalString(attributes, "edition"), //$NON-NLS-1$
					optionalString(attributes, "editors"), //$NON-NLS-1$
					optionalString(attributes, "series"), //$NON-NLS-1$
					optionalString(attributes, "publisher"), //$NON-NLS-1$
					optionalString(attributes, "address")); //$NON-NLS-1$
		} else if (publicationClass.equals(BookChapter.class)) {
			this.bookChapterService.updateBookChapter(
					publication.getId(),
					ensureString(attributes, "title"), //$NON-NLS-1$
					type,
					date,
					optionalString(attributes, "abstractText"), //$NON-NLS-1$
					optionalString(attributes, "keywords"), //$NON-NLS-1$
					optionalString(attributes, "doi"), //$NON-NLS-1$
					optionalString(attributes, "isbn"), //$NON-NLS-1$
					optionalString(attributes, "issn"), //$NON-NLS-1$
					optionalString(attributes, "dblpURL"), //$NON-NLS-1$
					optionalString(attributes, "extraURL"), //$NON-NLS-1$
					languageEnum,
					publication.getPathToDownloadablePDF(),
					publication.getPathToDownloadableAwardCertificate(),
					optionalString(attributes, "videoURL"), //$NON-NLS-1$
					ensureString(attributes, "bookTitle"), //$NON-NLS-1$
					optionalString(attributes, "chapterNumber"), //$NON-NLS-1$
					optionalString(attributes, "edition"), //$NON-NLS-1$
					optionalString(attributes, "volume"), //$NON-NLS-1$
					optionalString(attributes, "number"), //$NON-NLS-1$
					optionalString(attributes, "pages"), //$NON-NLS-1$
					optionalString(attributes, "editors"), //$NON-NLS-1$
					optionalString(attributes, "series"), //$NON-NLS-1$
					optionalString(attributes, "publisher"), //$NON-NLS-1$
					optionalString(attributes, "address")); //$NON-NLS-1$
		} else if (publicationClass.equals(ConferencePaper.class)) {
			this.conferencePaperService.updateConferencePaper(
					publication.getId(),
					ensureString(attributes, "title"), //$NON-NLS-1$
					type,
					date,
					optionalString(attributes, "abstractText"), //$NON-NLS-1$
					optionalString(attributes, "keywords"), //$NON-NLS-1$
					optionalString(attributes, "doi"), //$NON-NLS-1$
					optionalString(attributes, "isbn"), //$NON-NLS-1$
					optionalString(attributes, "issn"), //$NON-NLS-1$
					optionalString(attributes, "dblpURL"), //$NON-NLS-1$
					optionalString(attributes, "extraURL"), //$NON-NLS-1$
					languageEnum,
					publication.getPathToDownloadablePDF(),
					publication.getPathToDownloadableAwardCertificate(),
					optionalString(attributes, "videoURL"), //$NON-NLS-1$
					ensureString(attributes, "scientificEventName"), //$NON-NLS-1$
					optionalString(attributes, "volume"), //$NON-NLS-1$
					optionalString(attributes, "number"), //$NON-NLS-1$
					optionalString(attributes, "pages"), //$NON-NLS-1$
					optionalString(attributes, "editors"), //$NON-NLS-1$
					optionalString(attributes, "series"), //$NON-NLS-1$
					optionalString(attributes, "organizer"), //$NON-NLS-1$
					optionalString(attributes, "publisher"), //$NON-NLS-1$
					optionalString(attributes, "address")); //$NON-NLS-1$
		} else if (publicationClass.equals(JournalEdition.class)) {
			final Journal journal = ensureJournalInstance(attributes, "journal"); //$NON-NLS-1$
			this.journalEditionService.updateJournalEdition(
					publication.getId(),
					ensureString(attributes, "title"), //$NON-NLS-1$
					type,
					date,
					optionalString(attributes, "abstractText"), //$NON-NLS-1$
					optionalString(attributes, "keywords"), //$NON-NLS-1$
					optionalString(attributes, "doi"), //$NON-NLS-1$
					optionalString(attributes, "dblpURL"), //$NON-NLS-1$
					optionalString(attributes, "extraURL"), //$NON-NLS-1$
					languageEnum,
					publication.getPathToDownloadablePDF(),
					publication.getPathToDownloadableAwardCertificate(),
					optionalString(attributes, "videoURL"), //$NON-NLS-1$
					optionalString(attributes, "volume"), //$NON-NLS-1$
					optionalString(attributes, "number"), //$NON-NLS-1$
					optionalString(attributes, "pages"), //$NON-NLS-1$
					journal);
		} else if (publicationClass.equals(JournalPaper.class)) {
			final Journal journal = ensureJournalInstance(attributes, "journal"); //$NON-NLS-1$
			this.journalPaperService.updateJournalPaper(
					publication.getId(),
					ensureString(attributes, "title"), //$NON-NLS-1$
					type,
					date,
					optionalString(attributes, "abstractText"), //$NON-NLS-1$
					optionalString(attributes, "keywords"), //$NON-NLS-1$
					optionalString(attributes, "doi"), //$NON-NLS-1$
					optionalString(attributes, "dblpURL"), //$NON-NLS-1$
					optionalString(attributes, "extraURL"), //$NON-NLS-1$
					languageEnum,
					publication.getPathToDownloadablePDF(),
					publication.getPathToDownloadableAwardCertificate(),
					optionalString(attributes, "videoURL"), //$NON-NLS-1$
					optionalString(attributes, "volume"), //$NON-NLS-1$
					optionalString(attributes, "number"), //$NON-NLS-1$
					optionalString(attributes, "pages"), //$NON-NLS-1$
					optionalString(attributes, "series"), //$NON-NLS-1$
					journal);
		} else if (publicationClass.equals(KeyNote.class)) {
			this.keyNoteService.updateKeyNote(
					publication.getId(),
					ensureString(attributes, "title"), //$NON-NLS-1$
					type,
					date,
					optionalString(attributes, "abstractText"), //$NON-NLS-1$
					optionalString(attributes, "keywords"), //$NON-NLS-1$
					optionalString(attributes, "doi"), //$NON-NLS-1$
					optionalString(attributes, "isbn"), //$NON-NLS-1$
					optionalString(attributes, "issn"), //$NON-NLS-1$
					optionalString(attributes, "dblpURL"), //$NON-NLS-1$
					optionalString(attributes, "extraURL"), //$NON-NLS-1$
					languageEnum,
					publication.getPathToDownloadablePDF(),
					publication.getPathToDownloadableAwardCertificate(),
					optionalString(attributes, "videoURL"), //$NON-NLS-1$
					ensureString(attributes, "scientificEventName"), //$NON-NLS-1$
					optionalString(attributes, "editors"), //$NON-NLS-1$
					optionalString(attributes, "organization"), //$NON-NLS-1$
					optionalString(attributes, "organization")); //$NON-NLS-1$
		} else if (publicationClass.equals(MiscDocument.class)) {
			this.miscDocumentService.updateMiscDocument(
					publication.getId(),
					ensureString(attributes, "title"), //$NON-NLS-1$
					type,
					date,
					optionalString(attributes, "abstractText"), //$NON-NLS-1$
					optionalString(attributes, "keywords"), //$NON-NLS-1$
					optionalString(attributes, "doi"), //$NON-NLS-1$
					optionalString(attributes, "isbn"), //$NON-NLS-1$
					optionalString(attributes, "issn"), //$NON-NLS-1$
					optionalString(attributes, "dblpURL"), //$NON-NLS-1$
					optionalString(attributes, "extraURL"), //$NON-NLS-1$
					languageEnum,
					publication.getPathToDownloadablePDF(),
					publication.getPathToDownloadableAwardCertificate(),
					optionalString(attributes, "videoURL"), //$NON-NLS-1$
					optionalString(attributes, "number"), //$NON-NLS-1$
					ensureString(attributes, "howPublished"), //$NON-NLS-1$
					optionalString(attributes, "documentType"), //$NON-NLS-1$
					optionalString(attributes, "organization"), //$NON-NLS-1$
					optionalString(attributes, "publisher"), //$NON-NLS-1$
					optionalString(attributes, "address")); //$NON-NLS-1$
		} else if (publicationClass.equals(Patent.class)) {
			this.patentService.updatePatent(
					publication.getId(),
					ensureString(attributes, "title"), //$NON-NLS-1$
					type,
					date,
					optionalString(attributes, "abstractText"), //$NON-NLS-1$
					optionalString(attributes, "keywords"), //$NON-NLS-1$
					optionalString(attributes, "doi"), //$NON-NLS-1$
					optionalString(attributes, "isbn"), //$NON-NLS-1$
					optionalString(attributes, "issn"), //$NON-NLS-1$
					optionalString(attributes, "dblpURL"), //$NON-NLS-1$
					optionalString(attributes, "extraURL"), //$NON-NLS-1$
					languageEnum,
					publication.getPathToDownloadablePDF(),
					publication.getPathToDownloadableAwardCertificate(),
					optionalString(attributes, "videoURL"), //$NON-NLS-1$
					optionalString(attributes, "number"), //$NON-NLS-1$
					optionalString(attributes, "documentType"), //$NON-NLS-1$
					ensureString(attributes, "institution"), //$NON-NLS-1$
					optionalString(attributes, "address")); //$NON-NLS-1$
		} else if (publicationClass.equals(Report.class)) {
			this.reportService.updateReport(
					publication.getId(),
					ensureString(attributes, "title"), //$NON-NLS-1$
					type,
					date,
					optionalString(attributes, "abstractText"), //$NON-NLS-1$
					optionalString(attributes, "keywords"), //$NON-NLS-1$
					optionalString(attributes, "doi"), //$NON-NLS-1$
					optionalString(attributes, "isbn"), //$NON-NLS-1$
					optionalString(attributes, "issn"), //$NON-NLS-1$
					optionalString(attributes, "dblpURL"), //$NON-NLS-1$
					optionalString(attributes, "extraURL"), //$NON-NLS-1$
					languageEnum,
					publication.getPathToDownloadablePDF(),
					publication.getPathToDownloadableAwardCertificate(),
					optionalString(attributes, "videoURL"), //$NON-NLS-1$
					optionalString(attributes, "number"), //$NON-NLS-1$
					optionalString(attributes, "documentType"), //$NON-NLS-1$
					ensureString(attributes, "institution"), //$NON-NLS-1$
					optionalString(attributes, "address")); //$NON-NLS-1$
		} else if (publicationClass.equals(Thesis.class)) {
			this.thesisService.updateThesis(
					publication.getId(),
					ensureString(attributes, "title"), //$NON-NLS-1$
					type,
					date,
					optionalString(attributes, "abstractText"), //$NON-NLS-1$
					optionalString(attributes, "keywords"), //$NON-NLS-1$
					optionalString(attributes, "doi"), //$NON-NLS-1$
					optionalString(attributes, "isbn"), //$NON-NLS-1$
					optionalString(attributes, "issn"), //$NON-NLS-1$
					optionalString(attributes, "dblpURL"), //$NON-NLS-1$
					optionalString(attributes, "extraURL"), //$NON-NLS-1$
					languageEnum,
					publication.getPathToDownloadablePDF(),
					publication.getPathToDownloadableAwardCertificate(),
					optionalString(attributes, "videoURL"), //$NON-NLS-1$
					ensureString(attributes, "institution"), //$NON-NLS-1$
					optionalString(attributes, "address")); //$NON-NLS-1$
		} else {
			throw new IllegalArgumentException("Unsupported publication type: " + type); //$NON-NLS-1$
		}

		getLogger().info("Publication instance updated: " + publication.getId()); //$NON-NLS-1$
	}

	/** Update the references to the downloadable files for the given publication based on the 
	 * inputs.
	 * The just-uploaded files are given as argument.
	 * The attributes may contains two boolean files named {@code @pathToDownloadablePDF_previousValue}
	 * or {@code @pathToDownloadableAwardCertificate_previousValue} that indicates if the publication
	 * has associated files before the update query.
	 * 
	 * @param publication the publication to update.
	 * @param attributes the values of the attributes for the publication's creation.
	 * @param downloadablePDF the uploaded PDF file for the publication.
	 * @param downloadableAwardCertificate the uploaded Award certificate for the publication.
	 * @param saveInDb indicates if the changes must be saved in the database.
	 * @throws IOException if the uploaded files cannot be treated correctly.
	 */
	protected void updateUploadedPDFs(Publication publication, Map<String, String> attributes,
			MultipartFile downloadablePDF, MultipartFile downloadableAwardCertificate, boolean saveInDb) throws IOException {
		// Treat the uploaded files
		boolean hasUploaded = false;
		final boolean expliteRemove0 = optionalBoolean(attributes, "@pathToDownloadablePDF_explicitRemove"); //$NON-NLS-1$
		if (expliteRemove0) {
			try {
				this.fileManager.deleteDownloadablePublicationPdfFile(publication.getId());
			} catch (Throwable ex) {
				// Silent
			}
			publication.setPathToDownloadablePDF(null);
			hasUploaded = true;
		}
		if (downloadablePDF != null && !downloadablePDF.isEmpty()) {
			final File pdfFilename = this.fileManager.makePdfFilename(publication.getId());
			final File jpgFilename = this.fileManager.makePdfPictureFilename(publication.getId());
			this.fileManager.saveFiles(pdfFilename, jpgFilename, downloadablePDF);
			publication.setPathToDownloadablePDF(pdfFilename.getPath());
			hasUploaded = true;
			getLogger().info("PDF uploaded at: " + pdfFilename.getPath()); //$NON-NLS-1$
		}
		final boolean expliteRemove1 = optionalBoolean(attributes, "@pathToDownloadableAwardCertificate_explicitRemove"); //$NON-NLS-1$
		if (expliteRemove1) {
			try {
				this.fileManager.deleteDownloadableAwardPdfFile(publication.getId());
			} catch (Throwable ex) {
				// Silent
			}
			publication.setPathToDownloadableAwardCertificate(null);
			hasUploaded = true;
		}
		if (downloadableAwardCertificate != null && !downloadableAwardCertificate.isEmpty()) {
			final File pdfFilename = this.fileManager.makeAwardFilename(publication.getId());
			final File jpgFilename = this.fileManager.makeAwardPictureFilename(publication.getId());
			this.fileManager.saveFiles(pdfFilename, jpgFilename, downloadableAwardCertificate);
			publication.setPathToDownloadableAwardCertificate(pdfFilename.getPath());
			hasUploaded = true;
			getLogger().info("Award certificate uploaded at: " + pdfFilename.getPath()); //$NON-NLS-1$
		}
		if (saveInDb && hasUploaded) {
			save(publication);
		}
	}

	private void updateAuthorList(boolean creation, Publication publication, List<String> authors) {
		// First step: Update the list of authors.
		final List<Authorship> oldAuthorships = creation ? Collections.emptyList() : this.authorshipService.getAuthorshipsFor(publication.getId());
		Collector<Authorship, ?, Map<Integer, Authorship>> col = Collectors.toMap(
				it -> Integer.valueOf(it.getPerson().getId()),
				it -> it);
		final Map<Integer, Authorship> oldIds = oldAuthorships.stream().collect(col);
		final Pattern idPattern = Pattern.compile("\\d+"); //$NON-NLS-1$
		int rank = 0;
		for (final String author : authors) {
			Person person = null;
			int authorId = 0;
			if (idPattern.matcher(author).matches()) {
				// Numeric value means that the person is known.
				try {
					authorId = Integer.parseInt(author);
				} catch (Throwable ex) {
					// Silent
				}
			}
			if (authorId == 0) {
				// The author seems to be not in the database already. Check it based on the name.
				final String firstName = this.nameParser.parseFirstName(author);
				final String lastName = this.nameParser.parseLastName(author);
				authorId = this.personService.getPersonIdByName(firstName, lastName);
				if (authorId == 0) {
					// Now, it is sure that the person is unknown
					person = this.personService.createPerson(firstName, lastName);
					getLogger().info("New person \"" + author + "\" created with id: " + authorId); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					final Optional<Person> optPers = this.personRepository.findById(Integer.valueOf(authorId));
					if (optPers.isEmpty()) {
						throw new IllegalArgumentException("Unknown person with id: " + authorId); //$NON-NLS-1$
					}
					person = optPers.get();
				}
			} else {
				// Check if the given author identifier corresponds to a known person.
				final Optional<Person> optPers = this.personRepository.findById(Integer.valueOf(authorId));
				if (optPers.isEmpty()) {
					throw new IllegalArgumentException("Unknown person with id: " + authorId); //$NON-NLS-1$
				}
				person = optPers.get();
			}
			assert person != null;
			oldIds.remove(Integer.valueOf(person.getId()));
			final Person fperson = person;
			final Optional<Authorship> optAut = oldAuthorships.stream().filter(it -> it.getPerson().getId() == fperson.getId()).findFirst();
			if (optAut.isPresent()) {
				// Author is already present in the authorships
				final Authorship authorship = optAut.get();
				authorship.setPerson(person);
				authorship.setAuthorRank(rank);
				authorship.setPublication(publication);
				this.authorshipRepository.save(authorship);
				oldIds.remove(Integer.valueOf(authorship.getId()));
				getLogger().info("Author \"" + person.getFullName() //$NON-NLS-1$
						+ "\" updated for the publication with id " //$NON-NLS-1$
						+ publication.getId());
			} else {
				// Author was not associated yet
				this.authorshipService.addAuthorship(person.getId(), publication.getId(), rank, false);
				getLogger().info("Author \"" + person.getFullName()+ "\" added to publication with id " + publication.getId()); //$NON-NLS-1$ //$NON-NLS-2$
			}
			++rank;
		}
		// Remove the old author ships
		for (final Authorship oldAutshp : oldIds.values()) {
			publication.getAuthorshipsRaw().remove(oldAutshp);
			final Person oldAuthor = oldAutshp.getPerson();
			oldAuthor.getAuthorships().remove(oldAutshp);
			oldAutshp.setPerson(null);
			oldAutshp.setPublication(null);
			this.personRepository.save(oldAuthor);
			this.authorshipRepository.deleteById(Integer.valueOf(oldAutshp.getId()));
		}
		this.publicationRepository.save(publication);
		this.authorshipRepository.flush();
	}
	
}
