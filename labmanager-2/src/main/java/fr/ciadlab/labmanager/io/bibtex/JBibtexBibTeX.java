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

package fr.ciadlab.labmanager.io.bibtex;

import static org.jbibtex.BibTeXEntry.KEY_ADDRESS;
import static org.jbibtex.BibTeXEntry.KEY_AUTHOR;
import static org.jbibtex.BibTeXEntry.KEY_BOOKTITLE;
import static org.jbibtex.BibTeXEntry.KEY_CHAPTER;
import static org.jbibtex.BibTeXEntry.KEY_DOI;
import static org.jbibtex.BibTeXEntry.KEY_EDITION;
import static org.jbibtex.BibTeXEntry.KEY_EDITOR;
import static org.jbibtex.BibTeXEntry.KEY_HOWPUBLISHED;
import static org.jbibtex.BibTeXEntry.KEY_INSTITUTION;
import static org.jbibtex.BibTeXEntry.KEY_JOURNAL;
import static org.jbibtex.BibTeXEntry.KEY_MONTH;
import static org.jbibtex.BibTeXEntry.KEY_NOTE;
import static org.jbibtex.BibTeXEntry.KEY_NUMBER;
import static org.jbibtex.BibTeXEntry.KEY_ORGANIZATION;
import static org.jbibtex.BibTeXEntry.KEY_PAGES;
import static org.jbibtex.BibTeXEntry.KEY_PUBLISHER;
import static org.jbibtex.BibTeXEntry.KEY_SCHOOL;
import static org.jbibtex.BibTeXEntry.KEY_SERIES;
import static org.jbibtex.BibTeXEntry.KEY_TITLE;
import static org.jbibtex.BibTeXEntry.KEY_TYPE;
import static org.jbibtex.BibTeXEntry.KEY_URL;
import static org.jbibtex.BibTeXEntry.KEY_VOLUME;
import static org.jbibtex.BibTeXEntry.KEY_YEAR;
import static org.jbibtex.BibTeXEntry.TYPE_ARTICLE;
import static org.jbibtex.BibTeXEntry.TYPE_BOOK;
import static org.jbibtex.BibTeXEntry.TYPE_INCOLLECTION;
import static org.jbibtex.BibTeXEntry.TYPE_INPROCEEDINGS;
import static org.jbibtex.BibTeXEntry.TYPE_MANUAL;
import static org.jbibtex.BibTeXEntry.TYPE_MASTERSTHESIS;
import static org.jbibtex.BibTeXEntry.TYPE_MISC;
import static org.jbibtex.BibTeXEntry.TYPE_PHDTHESIS;
import static org.jbibtex.BibTeXEntry.TYPE_TECHREPORT;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

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
import fr.ciadlab.labmanager.io.bibtex.bugfix.BugfixLaTeXPrinter;
import fr.ciadlab.labmanager.service.journal.JournalService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.publication.PrePublicationFactory;
import fr.ciadlab.labmanager.service.publication.type.BookChapterService;
import fr.ciadlab.labmanager.service.publication.type.BookService;
import fr.ciadlab.labmanager.service.publication.type.ConferencePaperService;
import fr.ciadlab.labmanager.service.publication.type.JournalPaperService;
import fr.ciadlab.labmanager.service.publication.type.MiscDocumentService;
import fr.ciadlab.labmanager.service.publication.type.ReportService;
import fr.ciadlab.labmanager.service.publication.type.ThesisService;
import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXFormatter;
import org.jbibtex.BibTeXParser;
import org.jbibtex.CharacterFilterReader;
import org.jbibtex.DigitStringValue;
import org.jbibtex.Key;
import org.jbibtex.LaTeXObject;
import org.jbibtex.LaTeXParser;
import org.jbibtex.LaTeXPrinter;
import org.jbibtex.StringValue;
import org.jbibtex.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/** Implementation of the utilities for BibTeX based on the JBibtex library.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Component
@Primary
public class JBibtexBibTeX extends AbstractBibTeX {

	/** Field {@code abstract}.
	 */
	protected static final Key KEY_ABSTRACT = new Key(KEY_ABSTRACT_NAME);

	/** Field {@code keywords}.
	 */
	protected static final Key KEY_KEYWORD = new Key(KEY_KEYWORDS_NAME);

	/** Field {@code isbn}.
	 */
	protected static final Key KEY_ISBN = new Key(KEY_ISBN_NAME);

	/** Field {@code issn}.
	 */
	protected static final Key KEY_ISSN = new Key(KEY_ISSN_NAME);

	/** Field {@code halId}.
	 */
	protected static final Key KEY_HALID = new Key(KEY_HALID_NAME);

	/** Field {@code dblp}.
	 */
	protected static final Key KEY_DBLP = new Key(KEY_DBLP_NAME);

	/** Field {@code video}.
	 */
	protected static final Key KEY_VIDEO = new Key(KEY_VIDEO_NAME);

	/** Field {@code language}.
	 */
	protected static final Key KEY_LANGUAGE = new Key(KEY_LANGUAGE_NAME);

	/** Field {@code _publication_type}.
	 */
	protected static final Key KEY_PUBLICATION_TYPE = new Key("_publication_type"); //$NON-NLS-1$

	/** Field {@code _publication_category}.
	 */
	protected static final Key KEY_PUBLICATION_CATEGORY = new Key("_publication_category"); //$NON-NLS-1$

	/** Field {@code _scimago_qindex}.
	 */
	protected static final Key KEY_SCIMAGO_QINDEX = new Key("_scimago_qindex"); //$NON-NLS-1$

	/** Field {@code _wos_qindex}.
	 */
	protected static final Key KEY_WOS_QINDEX = new Key("_wos_qindex"); //$NON-NLS-1$

	/** Field {@code _impact_factor}.
	 */
	protected static final Key KEY_IMPACT_FACTOR = new Key("_impact_factor"); //$NON-NLS-1$

	/** Field {@code _core_ranking}.
	 */
	protected static final Key KEY_CORE_RANKING = new Key("_core_ranking"); //$NON-NLS-1$

	private static final String MESSAGE_PREFIX = "jBibtexBibTeX."; //$NON-NLS-1$

	private MessageSourceAccessor messages;

	private PrePublicationFactory prePublicationFactory;

	private JournalService journalService;

	private PersonService personService;

	private BookService bookService;

	private BookChapterService bookChapterService;

	private ConferencePaperService conferencePaperService;

	private JournalPaperService journalPaperService;

	private MiscDocumentService miscDocumentService;

	private ReportService reportService;

	private ThesisService thesisService;

	/** Constructor. This constructor is ready for injection.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param prePublicationFactory the factory of pre-publications.
	 * @param journalService the service for accessing the journals.
	 * @param personService the service for managing the persons.
	 * @param bookService the book service.
	 * @param bookChapterService the book chapter service.
	 * @param conferencePaperService the conference paper service.
	 * @param journalPaperService the journal paper service.
	 * @param miscDocumentService the service for misc documents.
	 * @param reportService the service for reports.
	 * @param thesisService the service for theses.
	 */
	public JBibtexBibTeX(
			@Autowired MessageSourceAccessor messages,
			@Autowired PrePublicationFactory prePublicationFactory,
			@Autowired JournalService journalService,
			@Autowired PersonService personService,
			@Autowired BookService bookService,
			@Autowired BookChapterService bookChapterService,
			@Autowired ConferencePaperService conferencePaperService,
			@Autowired JournalPaperService journalPaperService,
			@Autowired MiscDocumentService miscDocumentService,
			@Autowired ReportService reportService,
			@Autowired ThesisService thesisService) {
		this.messages = messages;
		this.prePublicationFactory = prePublicationFactory;
		this.journalService = journalService;
		this.personService = personService;
		this.bookService = bookService;
		this.bookChapterService = bookChapterService;
		this.conferencePaperService = conferencePaperService;
		this.journalPaperService = journalPaperService;
		this.miscDocumentService = miscDocumentService;
		this.reportService = reportService;
		this.thesisService = thesisService;
	}

	@Override
	public String parseTeXString(String texString) throws Exception {
		if (!Strings.isNullOrEmpty(texString)) {
			final LaTeXParser latexParser = new LaTeXParser();
			List<LaTeXObject> latexObjects = latexParser.parse(texString);
			final LaTeXPrinter latexPrinter = new BugfixLaTeXPrinter();
			final String plainTextString = latexPrinter.print(latexObjects);
			return plainTextString;
		}
		return null;
	}

	/** Add curly-braces around the upper-case words of the given text.
	 * This feature is usually applied in the titles of the BibTeX entries in
	 * order to avoid BibTeX tools to change the case of the words in the titles
	 * when it is rendered on a final document.
	 *
	 * @param text the text to change.
	 * @return the text with protected upper-case words.
	 */
	@Override
	public String protectAcronymsInText(String text) {
		if (!Strings.isNullOrEmpty(text)) {
			// We consider a word as an acronym when it is full capitalized with a minimum length of 2
			// and followed by a potential lower case 's'

			// Regex for acronyms in the middle of a sentence
			final String acronymRegex = "([^A-Za-z0-9{}])([A-Z0-9][A-Z0-9]+s?)([^A-Za-z0-9{}])"; //$NON-NLS-1$
			// Regex for an acronym as the first word in the sentence
			final String firstWordAcronymRegex = "^([A-Z0-9][A-Z0-9]+s?)([^A-Za-z0-9])"; //$NON-NLS-1$
			// Regex for an acronym as the last word in the sentence
			final String lastWordAcronymRegex = "([^A-Za-z0-9])([A-Z0-9][A-Z0-9]+s?)$"; //$NON-NLS-1$

			// We add braces to the acronyms that we found
			final String titleEncaps = text.replaceAll(acronymRegex, "$1{$2}$3") //$NON-NLS-1$
					.replaceAll(firstWordAcronymRegex, "{$1}$2") //$NON-NLS-1$
					.replaceAll(lastWordAcronymRegex, "$1{$2}"); //$NON-NLS-1$

			return titleEncaps;
		}
		return null;
	}

	@Override
	public Stream<Publication> getPublicationStreamFrom(Reader bibtex) throws Exception {
		try (Reader filteredReader = new CharacterFilterReader(bibtex)) {
			final BibTeXParser bibtexParser = new BibTeXParser();
			final BibTeXDatabase database = bibtexParser.parse(filteredReader);
			if (database != null) {
				return database.getEntries().entrySet().stream().map(it -> {
					return createPublicationFor(it.getKey(), it.getValue());
				});
			}
		}
		return Collections.<Publication>emptyList().stream();
	}


	/** Replies the publication type that could support the given BibTeX entry.
	 *
	 * @param entry the BibTeX entry.
	 * @return the publication type.
	 */
	@SuppressWarnings("static-method")
	protected PublicationType getPublicationTypeFor(BibTeXEntry entry) {
		if (BibTeXEntry.TYPE_ARTICLE.equals(entry.getType())
				|| BibTeXEntry.TYPE_INCOLLECTION.equals(entry.getType())) {
			return PublicationType.INTERNATIONAL_JOURNAL_PAPER;
		}
		if (BibTeXEntry.TYPE_INPROCEEDINGS.equals(entry.getType())
				|| BibTeXEntry.TYPE_CONFERENCE.equals(entry.getType())) {
			return PublicationType.INTERNATIONAL_CONFERENCE_PAPER;
		}
		if (BibTeXEntry.TYPE_BOOK.equals(entry.getType())) {
			return PublicationType.INTERNATIONAL_BOOK;
		}
		if (BibTeXEntry.TYPE_BOOKLET.equals(entry.getType())
				|| BibTeXEntry.TYPE_INBOOK.equals(entry.getType())) {
			return PublicationType.INTERNATIONAL_BOOK_CHAPTER;
		}
		if (BibTeXEntry.TYPE_PHDTHESIS.equals(entry.getType())) {
			return PublicationType.PHD_THESIS;
		} 
		if (BibTeXEntry.TYPE_MASTERSTHESIS.equals(entry.getType())) {
			return PublicationType.MASTER_THESIS;
		}
		if (BibTeXEntry.TYPE_TECHREPORT.equals(entry.getType())) {
			return PublicationType.TECHNICAL_REPORTS;
		}
		if (BibTeXEntry.TYPE_MANUAL.equals(entry.getType())) {
			return PublicationType.TUTORIAL_DOCUMENTATION;
		}
		if (BibTeXEntry.TYPE_MISC.equals(entry.getType())
				|| BibTeXEntry.TYPE_PROCEEDINGS.equals(entry.getType())
				|| BibTeXEntry.TYPE_UNPUBLISHED.equals(entry.getType())) {
			return PublicationType.OTHER;
		}
		throw new IllegalArgumentException("Unsupported type of the BibTeX entry: " + entry.getType()); //$NON-NLS-1$
	}

	private static String field(BibTeXEntry entry, Key key) {
		final Value value = entry.getField(key);
		if (value != null) {
			return Strings.emptyToNull(value.toUserString());
		}
		return null;
	}

	private static String field(BibTeXEntry entry, String key) {
		return field(entry, new Key(key));
	}

	private static String or(String... v) {
		for (final String value : v) {
			if (!Strings.isNullOrEmpty(value)) {
				return value;
			}
		}
		return null;
	}

	private static PublicationLanguage language(BibTeXEntry entry) {
		final String label = field(entry, new Key(KEY_LANGUAGE_NAME));
		return PublicationLanguage.valueOfCaseInsensitive(label);
	}

	private static int year(BibTeXEntry entry) {
		final String yearValue = field(entry, KEY_YEAR);
		if (Strings.isNullOrEmpty(yearValue)) {
			throw new IllegalArgumentException("Invalid year format for: " + entry.getKey().getValue()); //$NON-NLS-1$
		}
		final int year;
		try {
			year = Integer.parseUnsignedInt(yearValue);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Invalid year format for: " + entry.getKey().getValue(), ex); //$NON-NLS-1$
		}
		return year;
	}

	private static Date date(BibTeXEntry entry) {
		final int year = year(entry);
		final String monthValue = field(entry, KEY_MONTH);
		if (!Strings.isNullOrEmpty(monthValue)) {
			switch (monthValue) {
			case "01": //$NON-NLS-1$
			case "jan": //$NON-NLS-1$
			case "January": //$NON-NLS-1$
				return Date.valueOf(year + "-01-01"); //$NON-NLS-1$
			case "02": //$NON-NLS-1$
			case "feb": //$NON-NLS-1$
			case "February": //$NON-NLS-1$
				return Date.valueOf(year + "-02-01"); //$NON-NLS-1$
			case "03": //$NON-NLS-1$
			case "mar": //$NON-NLS-1$
			case "March": //$NON-NLS-1$
				return Date.valueOf(year + "-03-01"); //$NON-NLS-1$
			case "04": //$NON-NLS-1$
			case "apr": //$NON-NLS-1$
			case "April": //$NON-NLS-1$
				return Date.valueOf(year + "-04-01"); //$NON-NLS-1$
			case "05": //$NON-NLS-1$
			case "may": //$NON-NLS-1$
			case "May": //$NON-NLS-1$
				return Date.valueOf(year + "-05-01"); //$NON-NLS-1$
			case "06": //$NON-NLS-1$
			case "jun": //$NON-NLS-1$
			case "June": //$NON-NLS-1$
				return Date.valueOf(year + "-06-01"); //$NON-NLS-1$
			case "07": //$NON-NLS-1$
			case "jul": //$NON-NLS-1$
			case "July": //$NON-NLS-1$
				return Date.valueOf(year + "-07-01"); //$NON-NLS-1$
			case "08": //$NON-NLS-1$
			case "aug": //$NON-NLS-1$
			case "August": //$NON-NLS-1$
				return Date.valueOf(year + "-08-01"); //$NON-NLS-1$
			case "09": //$NON-NLS-1$
			case "sep": //$NON-NLS-1$
			case "September": //$NON-NLS-1$
				return Date.valueOf(year + "-09-01"); //$NON-NLS-1$
			case "10": //$NON-NLS-1$
			case "oct": //$NON-NLS-1$
			case "October": //$NON-NLS-1$
				return Date.valueOf(year + "-10-01"); //$NON-NLS-1$
			case "11": //$NON-NLS-1$
			case "nov": //$NON-NLS-1$
			case "November": //$NON-NLS-1$
				return Date.valueOf(year + "-11-01"); //$NON-NLS-1$
			case "12": //$NON-NLS-1$
			case "dec": //$NON-NLS-1$
			case "December": //$NON-NLS-1$
				return Date.valueOf(year + "-12-01"); //$NON-NLS-1$
			default:
				//
			}
		}
		return null;
	}

	/** Extract the publication from a BibTeX entry.
	 * This function does not save the publication in the database.
	 *
	 * @param key the key of the entry.
	 * @param entry the entry itself.
	 * @return the publication.
	 */
	protected Publication createPublicationFor(Key key, BibTeXEntry entry) {
		final PublicationType type = getPublicationTypeFor(entry);
		if (type != null) {
			// Create a generic publication
			final Publication genericPublication = this.prePublicationFactory.createPrePublication(
					type,
					field(entry, KEY_TITLE),
					field(entry, KEY_ABSTRACT_NAME),
					field(entry, KEY_KEYWORDS_NAME),
					date(entry),
					field(entry, KEY_ISBN_NAME),
					field(entry, KEY_ISSN_NAME),
					field(entry, KEY_DOI),
					field(entry, KEY_HALID),
					field(entry, KEY_URL),
					field(entry, KEY_VIDEO_NAME),
					field(entry, KEY_DBLP_NAME),
					null, // Ignore PDF file
					null, // Ignore award certificate file
					language(entry));
			genericPublication.setPublicationYear(year(entry));

			// Generate the publication instance
			final Publication finalPublication;
			switch (type) {
			case INTERNATIONAL_JOURNAL_PAPER:
				final JournalPaper journalPaper = this.journalPaperService.createJournalPaper(genericPublication,
						field(entry, KEY_VOLUME),
						field(entry, KEY_NUMBER),
						field(entry, KEY_PAGES),
						false);
				final String journalName = field(entry, KEY_JOURNAL);
				final Journal journal = this.journalService.getJournalByName(journalName);
				if (journal == null) {
					throw new IllegalArgumentException("Unknown journal for entry " + key.getValue() + ": " + journalName); //$NON-NLS-1$ //$NON-NLS-2$
				}
				journalPaper.setJournal(journal);
				finalPublication = journalPaper;
				break;
			case INTERNATIONAL_CONFERENCE_PAPER:
				finalPublication = this.conferencePaperService.createConferencePaper(genericPublication,
						field(entry, KEY_BOOKTITLE),
						field(entry, KEY_VOLUME),
						field(entry, KEY_NUMBER),
						field(entry, KEY_PAGES),
						field(entry, KEY_EDITOR),
						field(entry, KEY_SERIES),
						field(entry, KEY_ORGANIZATION),
						field(entry, KEY_ADDRESS),
						field(entry, KEY_PUBLISHER),
						false);
				break;
			case INTERNATIONAL_BOOK:
				finalPublication = this.bookService.createBook(genericPublication,
						field(entry, KEY_VOLUME),
						field(entry, KEY_NUMBER),
						field(entry, KEY_PAGES),
						field(entry, KEY_EDITION),
						field(entry, KEY_EDITOR),
						field(entry, KEY_SERIES),
						field(entry, KEY_PUBLISHER),
						field(entry, KEY_ADDRESS),
						false);
				break;
			case INTERNATIONAL_BOOK_CHAPTER:
				finalPublication = this.bookChapterService.createBookChapter(genericPublication,
						field(entry, KEY_BOOKTITLE),
						field(entry, KEY_CHAPTER),
						field(entry, KEY_EDITION),
						field(entry, KEY_VOLUME),
						field(entry, KEY_NUMBER),
						field(entry, KEY_PAGES),
						field(entry, KEY_EDITOR),
						field(entry, KEY_SERIES),
						field(entry, KEY_PUBLISHER),
						field(entry, KEY_ADDRESS),
						false);
				break;
			case PHD_THESIS:
			case MASTER_THESIS:
				finalPublication = this.thesisService.createThesis(genericPublication,
						or(field(entry, KEY_SCHOOL), field(entry, KEY_INSTITUTION)),
						field(entry, KEY_ADDRESS),
						false);
				break;
			case TECHNICAL_REPORTS:
				finalPublication = this.reportService.createReport(genericPublication,
						or(field(entry, KEY_NUMBER), field(entry, KEY_EDITION)),
						field(entry, KEY_TYPE),
						field(entry, KEY_INSTITUTION),
						field(entry, KEY_ADDRESS),
						false);
				break;
			case TUTORIAL_DOCUMENTATION:
				finalPublication = this.reportService.createReport(genericPublication,
						or(field(entry, KEY_NUMBER), field(entry, KEY_EDITION)),
						field(entry, KEY_TYPE),
						or(field(entry, KEY_ORGANIZATION), field(entry, KEY_PUBLISHER)),
						field(entry, KEY_ADDRESS),
						false);
				break;
			case OTHER:
				finalPublication = this.miscDocumentService.createMiscDocument(genericPublication,
						field(entry, KEY_NUMBER),
						field(entry, KEY_HOWPUBLISHED),
						field(entry, KEY_TYPE),
						field(entry, KEY_ORGANIZATION),
						field(entry, KEY_PUBLISHER),
						field(entry, KEY_ADDRESS),
						false);
				break;
			default:
				throw new IllegalArgumentException("Unsupported type of publication for BibTeX entry: " + key.getValue()); //$NON-NLS-1$
			}

			// Generate the author list
			final String authorField = or(field(entry, KEY_AUTHOR), field(entry, KEY_EDITOR));
			final List<Person> authors = this.personService.extractPersonsFrom(authorField);
			if (authors.isEmpty()) {
				throw new IllegalArgumentException("No author for the BibTeX entry: " + key.getValue()); //$NON-NLS-1$
			}
			finalPublication.setTemporaryAuthors(authors);

			return finalPublication;
		}
		throw new IllegalArgumentException("Unsupported type of BibTeX entry for: " + key.getValue()); //$NON-NLS-1$
	}

	@Override
	public void exportPublications(Writer output, Iterable<? extends Publication> publications) throws IOException {
		final BibTeXDatabase database = createDatabase(publications);
		final BibTeXFormatter bibtexFormatter = new BibTeXFormatter();
		bibtexFormatter.format(database, output);
	}

	/** Create a JBibTeX database with the given list of publications.
	 *
	 * @param publications the publications to put into the database.
	 * @return the JBibTeX database.
	 */
	protected BibTeXDatabase createDatabase(Iterable<? extends Publication> publications) {
		final BibTeXDatabase db = new BibTeXDatabase();
		for (final Publication publication : publications) {
			addPublication(db, publication);
		}
		return db;
	}

	/** Add a publication into a JBibTeX database.
	 *
	 * @param database the JBibTeX database.
	 * @param publication the publication to put into the database.
	 */
	protected void addPublication(BibTeXDatabase database, Publication publication) {
		final BibTeXEntry entry;
		switch (publication.getType()) {
		case INTERNATIONAL_JOURNAL_PAPER:
		case INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE:
		case NATIONAL_JOURNAL_PAPER:
		case NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE:
			entry = createBibTeXEntry((JournalPaper) publication);
			break;
		case INTERNATIONAL_CONFERENCE_PAPER:
		case INTERNATIONAL_ORAL_COMMUNICATION:
		case INTERNATIONAL_POSTER:
		case NATIONAL_CONFERENCE_PAPER:
		case NATIONAL_ORAL_COMMUNICATION:
		case NATIONAL_POSTER:
		case SCIENTIFIC_CULTURE_PAPER:
			entry = createBibTeXEntry((ConferencePaper) publication);
			break;
		case INTERNATIONAL_BOOK:
		case NATIONAL_BOOK:
		case SCIENTIFIC_CULTURE_BOOK:
			entry = createBibTeXEntry((Book) publication);
			break;
		case INTERNATIONAL_BOOK_CHAPTER:
		case NATIONAL_BOOK_CHAPTER:
		case SCIENTIFIC_CULTURE_BOOK_CHAPTER:
			entry = createBibTeXEntry((BookChapter) publication);
			break;
		case HDR_THESIS:
		case PHD_THESIS:
		case MASTER_THESIS:
			entry = createBibTeXEntry((Thesis) publication);
			break;
		case INTERNATIONAL_JOURNAL_EDITION:
		case NATIONAL_JOURNAL_EDITION:
			entry = createBibTeXEntry((JournalEdition) publication);
			break;
		case INTERNATIONAL_KEYNOTE:
		case NATIONAL_KEYNOTE:
			entry = createBibTeXEntry((KeyNote) publication);
			break;
		case TECHNICAL_REPORTS:
		case PROJECT_REPORTS:
		case RESEARCH_TRANSFERT_REPORT:
		case TEACHING_DOCUMENTS:
		case TUTORIAL_DOCUMENTATION:
			entry = createBibTeXEntry((Report) publication);
			break;
		case INTERNATIONAL_PATENT:
		case EUROPEAN_PATENT:
		case NATIONAL_PATENT:
			entry = createBibTeXEntry((Patent) publication);
			break;
		case ARTISTIC_PRODUCTION:
		case RESEARCH_TOOLS:
		case OTHER:
			entry = createBibTeXEntry((MiscDocument) publication);
			break;
		default:
			throw new IllegalArgumentException("Unsupported publication type for export: " + publication.getType()); //$NON-NLS-1$
		}
		if (entry != null) {
			database.addObject(entry);
		}
	}

	private static Key createBibTeXId(Publication publication) {
		return new Key(publication.getPreferredStringId());
	}

	private static void addField(BibTeXEntry entry, Key key, String value) {
		if (!Strings.isNullOrEmpty(value)) {
			entry.addField(key, new StringValue(value, StringValue.Style.BRACED));
		}
	}

	private static void addField(BibTeXEntry entry, Key key, QuartileRanking value) {
		if (value != null) {
			entry.addField(key, new StringValue(value.toString(), StringValue.Style.BRACED));
		}
	}

	private static void addField(BibTeXEntry entry, Key key, CoreRanking value) {
		if (value != null) {
			entry.addField(key, new StringValue(value.toString(), StringValue.Style.BRACED));
		}
	}

	private static void addField(BibTeXEntry entry, Key key, float value) {
		if (value > 0f) {
			final NumberFormat format = new DecimalFormat("#0.000"); //$NON-NLS-1$
			entry.addField(key, new StringValue(format.format(value), StringValue.Style.BRACED));
		}
	}

	private static void addField(BibTeXEntry entry, Key key, int value) {
		entry.addField(key, new DigitStringValue(Integer.toString(value)));
	}

	private static void addMonthField(BibTeXEntry entry, Date value) {
		if (value != null) {
			final String monthValue;
			switch (value.toLocalDate().getMonth()) {
			case APRIL:
				monthValue = "apr"; //$NON-NLS-1$
				break;
			case AUGUST:
				monthValue = "aug"; //$NON-NLS-1$
				break;
			case DECEMBER:
				monthValue = "dec"; //$NON-NLS-1$
				break;
			case FEBRUARY:
				monthValue = "feb"; //$NON-NLS-1$
				break;
			case JANUARY:
				monthValue = "jan"; //$NON-NLS-1$
				break;
			case JULY:
				monthValue = "jul"; //$NON-NLS-1$
				break;
			case JUNE:
				monthValue = "jun"; //$NON-NLS-1$
				break;
			case MARCH:
				monthValue = "mar"; //$NON-NLS-1$
				break;
			case MAY:
				monthValue = "may"; //$NON-NLS-1$
				break;
			case NOVEMBER:
				monthValue = "nov"; //$NON-NLS-1$
				break;
			case OCTOBER:
				monthValue = "oct"; //$NON-NLS-1$
				break;
			case SEPTEMBER:
				monthValue = "sep"; //$NON-NLS-1$
				break;
			default:
				return;
			}
			entry.addField(KEY_MONTH, new DigitStringValue(monthValue));
		}
	}

	private static void fillBibTeXEntry(BibTeXEntry entry, Publication publication, Key authorKey) {
		addField(entry, KEY_TITLE, publication.getTitle());

		final StringBuilder authorNames = new StringBuilder();
		for (final Person person : publication.getAuthors()) {
			if (authorNames.length() > 0) {
				authorNames.append(" and "); //$NON-NLS-1$
			}
			authorNames.append(person.getLastName());
			authorNames.append(", "); //$NON-NLS-1$
			authorNames.append(person.getFirstName());
		}
		addField(entry, authorKey, authorNames.toString());

		addField(entry, KEY_YEAR, publication.getPublicationYear());
		addMonthField(entry, publication.getPublicationDate());
		addField(entry, KEY_DOI, publication.getDOI());
		addField(entry, KEY_ISBN, publication.getISBN());
		addField(entry, KEY_ISSN, publication.getISSN());
		addField(entry, KEY_URL, publication.getExtraURL());
		addField(entry, KEY_DBLP, publication.getDblpURL());
		addField(entry, KEY_VIDEO, publication.getVideoURL());
		addField(entry, KEY_ABSTRACT, publication.getAbstractText());
		addField(entry, KEY_KEYWORD, publication.getKeywords());
		addField(entry, KEY_LANGUAGE, publication.getMajorLanguage().toString());
		final PublicationCategory cat = publication.getType().getCategory(publication.isRanked());
		// Force the Java locale to get the text that is corresponding to the language of the paper
		final java.util.Locale loc = java.util.Locale.getDefault();
		try {
			java.util.Locale.setDefault(publication.getMajorLanguage().getLocale());
			addField(entry, KEY_PUBLICATION_TYPE, publication.getType().getLabel());
			addField(entry, KEY_PUBLICATION_CATEGORY, cat.toString());
		} finally {
			java.util.Locale.setDefault(loc);
		}
	}

	private void addNoteForJournal(BibTeXEntry entry, PublicationLanguage language, 
			QuartileRanking scimago, QuartileRanking wos, float impactFactor) {
		// Force the Java locale to get the text that is corresponding to the language of the paper
		final java.util.Locale loc = java.util.Locale.getDefault();
		try {
			java.util.Locale.setDefault(language.getLocale());
			final StringBuilder note = new StringBuilder();
			if (scimago != null && wos != null) {
				if (scimago != wos) {
					note.append(this.messages.getMessage(MESSAGE_PREFIX + "SCIMAGO_WOS_QUARTILES", //$NON-NLS-1$
							new Object[] {scimago.toString(), wos.toString()}));
				} else {
					note.append(this.messages.getMessage(MESSAGE_PREFIX + "QUARTILES", //$NON-NLS-1$
							new Object[] {scimago.toString()}));
				}
			} else if (scimago != null) {
				note.append(this.messages.getMessage(MESSAGE_PREFIX + "SCIMAGO_QUARTILE", //$NON-NLS-1$
						new Object[] {scimago.toString()}));
			} else if (wos != null) {
				note.append(this.messages.getMessage(MESSAGE_PREFIX + "WOS_QUARTILE", //$NON-NLS-1$
						new Object[] {wos.toString()}));
			}
			if (impactFactor > 0f) {
				if (note.length() > 0) {
					note.append(", "); //$NON-NLS-1$
				}
				note.append(this.messages.getMessage(MESSAGE_PREFIX + "IMPACT_FACTOR", //$NON-NLS-1$
						new Object[] {Float.valueOf(impactFactor)}));
			}
			addField(entry, KEY_NOTE, note.toString());
		} finally {
			java.util.Locale.setDefault(loc);
		}
	}

	private void addNoteForConference(BibTeXEntry entry, PublicationLanguage language, CoreRanking core) {
		// Force the Java locale to get the text that is corresponding to the language of the paper
		final java.util.Locale loc = java.util.Locale.getDefault();
		try {
			java.util.Locale.setDefault(language.getLocale());
			if (core != null) {
				addField(entry, KEY_NOTE, this.messages.getMessage(MESSAGE_PREFIX + "CORE_RANKING", //$NON-NLS-1$
						new Object[] {core.toString()}));
			}
		} finally {
			java.util.Locale.setDefault(loc);
		}
	}

	/** Create a JBibTeX entry for a journal paper.
	 *
	 * @param paper the journal paper to put into the database.
	 * @return the JBibTeX entry.
	 */
	protected BibTeXEntry createBibTeXEntry(JournalPaper paper) {
		final BibTeXEntry entry = new BibTeXEntry(TYPE_ARTICLE, createBibTeXId(paper));
		fillBibTeXEntry(entry, paper, KEY_AUTHOR);
		final Journal journal = paper.getJournal();
		if (journal != null) {
			addField(entry, KEY_JOURNAL, journal.getJournalName());
			addField(entry, KEY_PUBLISHER, journal.getPublisher());
		}
		addField(entry, KEY_VOLUME, paper.getVolume());
		addField(entry, KEY_NUMBER, paper.getNumber());
		addField(entry, KEY_PAGES, paper.getPages());
		addField(entry, KEY_SCIMAGO_QINDEX, paper.getScimagoQIndex());
		addField(entry, KEY_WOS_QINDEX, paper.getWosQIndex());
		addField(entry, KEY_IMPACT_FACTOR, paper.getImpactFactor());
		addNoteForJournal(entry, paper.getMajorLanguage(), paper.getScimagoQIndex(),
				paper.getWosQIndex(), paper.getImpactFactor());
		return entry;
	}

	/** Create a JBibTeX entry for a conference paper.
	 *
	 * @param paper the conference paper to put into the database.
	 * @return the JBibTeX entry.
	 */
	protected BibTeXEntry createBibTeXEntry(ConferencePaper paper) {
		final BibTeXEntry entry = new BibTeXEntry(TYPE_INPROCEEDINGS, createBibTeXId(paper));
		fillBibTeXEntry(entry, paper, KEY_AUTHOR);
		addField(entry, KEY_BOOKTITLE, paper.getScientificEventName());
		addField(entry, KEY_VOLUME, paper.getVolume());
		addField(entry, KEY_NUMBER, paper.getNumber());
		addField(entry, KEY_PAGES, paper.getPages());
		addField(entry, KEY_SERIES, paper.getSeries());
		addField(entry, KEY_EDITOR, paper.getEditors());
		addField(entry, KEY_ORGANIZATION, paper.getOrganization());
		addField(entry, KEY_ADDRESS, paper.getAddress());
		addField(entry, KEY_CORE_RANKING, paper.getCoreRanking());
		addNoteForConference(entry, paper.getMajorLanguage(), paper.getCoreRanking());
		return entry;
	}

	/** Create a JBibTeX entry for a book.
	 *
	 * @param book the book to put into the database.
	 * @return the JBibTeX entry.
	 */
	@SuppressWarnings("static-method")
	protected BibTeXEntry createBibTeXEntry(Book book) {
		final BibTeXEntry entry = new BibTeXEntry(TYPE_BOOK, createBibTeXId(book));
		fillBibTeXEntry(entry, book, KEY_AUTHOR);
		addField(entry, KEY_EDITION, book.getEdition());
		addField(entry, KEY_SERIES, book.getSeries());
		addField(entry, KEY_VOLUME, book.getVolume());
		addField(entry, KEY_NUMBER, book.getNumber());
		addField(entry, KEY_PAGES, book.getPages());
		addField(entry, KEY_EDITOR, book.getEditors());
		addField(entry, KEY_PUBLISHER, book.getPublisher());
		addField(entry, KEY_ADDRESS, book.getAddress());
		return entry;
	}

	/** Create a JBibTeX entry for a book chapter.
	 *
	 * @param chapter the book chapter to put into the database.
	 * @return the JBibTeX entry.
	 */
	@SuppressWarnings("static-method")
	protected BibTeXEntry createBibTeXEntry(BookChapter chapter) {
		final BibTeXEntry entry = new BibTeXEntry(TYPE_INCOLLECTION, createBibTeXId(chapter));
		fillBibTeXEntry(entry, chapter, KEY_AUTHOR);
		addField(entry, KEY_BOOKTITLE, chapter.getBookTitle());
		addField(entry, KEY_CHAPTER, chapter.getChapterNumber());
		addField(entry, KEY_EDITION, chapter.getEdition());
		addField(entry, KEY_SERIES, chapter.getSeries());
		addField(entry, KEY_VOLUME, chapter.getVolume());
		addField(entry, KEY_NUMBER, chapter.getNumber());
		addField(entry, KEY_PAGES, chapter.getPages());
		addField(entry, KEY_EDITOR, chapter.getEditors());
		addField(entry, KEY_PUBLISHER, chapter.getPublisher());
		addField(entry, KEY_ADDRESS, chapter.getAddress());
		return entry;
	}

	/** Create a JBibTeX entry for a thesis.
	 *
	 * @param thesis the thesis to put into the database.
	 * @return the JBibTeX entry.
	 */
	@SuppressWarnings("static-method")
	protected BibTeXEntry createBibTeXEntry(Thesis thesis) {
		final Key pubType = thesis.getType() == PublicationType.MASTER_THESIS ? TYPE_MASTERSTHESIS : TYPE_PHDTHESIS;
		final BibTeXEntry entry = new BibTeXEntry(pubType, createBibTeXId(thesis));
		fillBibTeXEntry(entry, thesis, KEY_AUTHOR);
		addField(entry, KEY_SCHOOL, thesis.getInstitution());
		addField(entry, KEY_ADDRESS, thesis.getAddress());
		// Force the Java locale to get the text that is corresponding to the language of the paper
		final java.util.Locale loc = java.util.Locale.getDefault();
		try {
			java.util.Locale.setDefault(thesis.getMajorLanguage().getLocale());
			addField(entry, KEY_TYPE, thesis.getType().getLabel());
		} finally {
			java.util.Locale.setDefault(loc);
		}
		return entry;
	}

	/** Create a JBibTeX entry for a journal edition.
	 *
	 * @param edition the journal edition to put into the database.
	 * @return the JBibTeX entry.
	 */
	protected BibTeXEntry createBibTeXEntry(JournalEdition edition) {
		final BibTeXEntry entry = new BibTeXEntry(TYPE_BOOK, createBibTeXId(edition));
		fillBibTeXEntry(entry, edition, KEY_EDITOR);
		final Journal journal = edition.getJournal();
		if (journal != null) {
			addField(entry, KEY_JOURNAL, journal.getJournalName());
			addField(entry, KEY_PUBLISHER, journal.getPublisher());
		}
		addField(entry, KEY_VOLUME, edition.getVolume());
		addField(entry, KEY_NUMBER, edition.getNumber());
		addField(entry, KEY_PAGES, edition.getPages());
		addField(entry, KEY_SCIMAGO_QINDEX, edition.getScimagoQIndex());
		addField(entry, KEY_WOS_QINDEX, edition.getWosQIndex());
		addField(entry, KEY_IMPACT_FACTOR, edition.getImpactFactor());
		addNoteForJournal(entry, edition.getMajorLanguage(), edition.getScimagoQIndex(),
				edition.getWosQIndex(), edition.getImpactFactor());
		return entry;
	}

	/** Create a JBibTeX entry for a keynote.
	 *
	 * @param keynote the keynote to put into the database.
	 * @return the JBibTeX entry.
	 */
	@SuppressWarnings("static-method")
	protected BibTeXEntry createBibTeXEntry(KeyNote keynote) {
		final BibTeXEntry entry = new BibTeXEntry(TYPE_INPROCEEDINGS, createBibTeXId(keynote));
		fillBibTeXEntry(entry, keynote, KEY_AUTHOR);
		addField(entry, KEY_BOOKTITLE, keynote.getScientificEventName());
		addField(entry, KEY_EDITOR, keynote.getEditors());
		addField(entry, KEY_ORGANIZATION, keynote.getOrganization());
		addField(entry, KEY_ADDRESS, keynote.getAddress());
		// Force the Java locale to get the text that is corresponding to the language of the paper
		final java.util.Locale loc = java.util.Locale.getDefault();
		try {
			java.util.Locale.setDefault(keynote.getMajorLanguage().getLocale());
			addField(entry, KEY_NOTE, keynote.getType().getLabel());
		} finally {
			java.util.Locale.setDefault(loc);
		}
		return entry;
	}

	/** Create a JBibTeX entry for a report.
	 *
	 * @param report the report to put into the database.
	 * @return the JBibTeX entry.
	 */
	@SuppressWarnings("static-method")
	protected BibTeXEntry createBibTeXEntry(Report report) {
		final BibTeXEntry entry;
		if (report.getType() == PublicationType.TEACHING_DOCUMENTS
				|| report.getType() == PublicationType.TUTORIAL_DOCUMENTATION) {
			entry = new BibTeXEntry(TYPE_MANUAL, createBibTeXId(report));
			fillBibTeXEntry(entry, report, KEY_AUTHOR);
			addField(entry, KEY_EDITION, report.getReportNumber());
			addField(entry, KEY_ORGANIZATION, report.getInstitution());
			addField(entry, KEY_ADDRESS, report.getAddress());
			addField(entry, KEY_NOTE, report.getReportType());
		} else {
			entry = new BibTeXEntry(TYPE_TECHREPORT, createBibTeXId(report));
			fillBibTeXEntry(entry, report, KEY_AUTHOR);
			addField(entry, KEY_NUMBER, report.getReportNumber());
			addField(entry, KEY_INSTITUTION, report.getInstitution());
			addField(entry, KEY_ADDRESS, report.getAddress());
			addField(entry, KEY_NOTE, report.getReportType());
		}
		return entry;
	}

	/** Create a JBibTeX entry for a patent.
	 *
	 * @param patent the patent to put into the database.
	 * @return the JBibTeX entry.
	 */
	protected BibTeXEntry createBibTeXEntry(Patent patent) {
		final BibTeXEntry entry = new BibTeXEntry(TYPE_MISC, createBibTeXId(patent));
		fillBibTeXEntry(entry, patent, KEY_AUTHOR);
		addField(entry, KEY_ADDRESS, patent.getAddress());
		addField(entry, KEY_NOTE, patent.getPatentType());
		// Force the Java locale to get the text that is corresponding to the language of the patent
		final java.util.Locale loc = java.util.Locale.getDefault();
		try {
			java.util.Locale.setDefault(patent.getMajorLanguage().getLocale());
			final StringBuilder howPublished = new StringBuilder();
			if (!Strings.isNullOrEmpty(patent.getPatentNumber())
					&& !Strings.isNullOrEmpty(patent.getInstitution())) {
				howPublished.append(this.messages.getMessage(MESSAGE_PREFIX + "PATENT_NUMBER_INSTITUTION", //$NON-NLS-1$
						new Object[] {patent.getPatentNumber(), patent.getInstitution()}));
			} else if (!Strings.isNullOrEmpty(patent.getPatentNumber())) {
				howPublished.append(this.messages.getMessage(MESSAGE_PREFIX + "PATENT_NUMBER", //$NON-NLS-1$
						new Object[] {patent.getPatentNumber()}));
			} else if (!Strings.isNullOrEmpty(patent.getInstitution())) {
				howPublished.append(this.messages.getMessage(MESSAGE_PREFIX + "PATENT_INSTITUTION", //$NON-NLS-1$
						new Object[] {patent.getInstitution()}));
			} 
			addField(entry, KEY_HOWPUBLISHED, howPublished.toString());
		} finally {
			java.util.Locale.setDefault(loc);
		}
		return entry;
	}

	/** Create a JBibTeX entry for a misc document.
	 *
	 * @param document the misc document to put into the database.
	 * @return the JBibTeX entry.
	 */
	@SuppressWarnings("static-method")
	protected BibTeXEntry createBibTeXEntry(MiscDocument document) {
		final BibTeXEntry entry = new BibTeXEntry(TYPE_MISC, createBibTeXId(document));
		fillBibTeXEntry(entry, document, KEY_AUTHOR);
		addField(entry, KEY_HOWPUBLISHED, document.getHowPublished());
		addField(entry, KEY_TYPE, document.getDocumentType());
		addField(entry, KEY_NUMBER, document.getDocumentNumber());
		addField(entry, KEY_ORGANIZATION, document.getOrganization());
		addField(entry, KEY_PUBLISHER, document.getPublisher());
		addField(entry, KEY_ADDRESS, document.getAddress());
		return entry;
	}

}
