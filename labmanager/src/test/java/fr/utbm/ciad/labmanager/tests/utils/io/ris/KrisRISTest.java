/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the CIAD laboratory and the Universite de Technologie
 * de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD-UTBM.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.utbm.ciad.labmanager.tests.utils.io.ris;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.utbm.ciad.labmanager.data.conference.Conference;
import fr.utbm.ciad.labmanager.data.journal.Journal;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.publication.ConferenceBasedPublication;
import fr.utbm.ciad.labmanager.data.publication.JournalBasedPublication;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.data.publication.PublicationLanguage;
import fr.utbm.ciad.labmanager.data.publication.PublicationType;
import fr.utbm.ciad.labmanager.data.publication.type.Book;
import fr.utbm.ciad.labmanager.data.publication.type.BookChapter;
import fr.utbm.ciad.labmanager.data.publication.type.ConferencePaper;
import fr.utbm.ciad.labmanager.data.publication.type.JournalEdition;
import fr.utbm.ciad.labmanager.data.publication.type.JournalPaper;
import fr.utbm.ciad.labmanager.data.publication.type.KeyNote;
import fr.utbm.ciad.labmanager.data.publication.type.MiscDocument;
import fr.utbm.ciad.labmanager.data.publication.type.Patent;
import fr.utbm.ciad.labmanager.data.publication.type.Report;
import fr.utbm.ciad.labmanager.data.publication.type.Thesis;
import fr.utbm.ciad.labmanager.services.conference.ConferenceService;
import fr.utbm.ciad.labmanager.services.journal.JournalService;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.publication.PrePublicationFactory;
import fr.utbm.ciad.labmanager.services.publication.type.BookChapterService;
import fr.utbm.ciad.labmanager.services.publication.type.BookService;
import fr.utbm.ciad.labmanager.services.publication.type.ConferencePaperService;
import fr.utbm.ciad.labmanager.services.publication.type.JournalEditionService;
import fr.utbm.ciad.labmanager.services.publication.type.JournalPaperService;
import fr.utbm.ciad.labmanager.services.publication.type.KeyNoteService;
import fr.utbm.ciad.labmanager.services.publication.type.MiscDocumentService;
import fr.utbm.ciad.labmanager.services.publication.type.ReportService;
import fr.utbm.ciad.labmanager.services.publication.type.ThesisService;
import fr.utbm.ciad.labmanager.tests.utils.io.bibtex.JBibtexBibTeXTest;
import fr.utbm.ciad.labmanager.utils.doi.DefaultDoiTools;
import fr.utbm.ciad.labmanager.utils.doi.DoiTools;
import fr.utbm.ciad.labmanager.utils.io.ExporterConfigurator;
import fr.utbm.ciad.labmanager.utils.io.ris.KrisRIS;
import fr.utbm.ciad.labmanager.utils.ranking.QuartileRanking;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.Resources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests for {@link KrisRIS}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class KrisRISTest {

	private PrePublicationFactory prePublicationFactory;

	private JournalService journalService;

	private ConferenceService conferenceService;

	private PersonService personService;

	private BookService bookService;

	private BookChapterService bookChapterService;

	private ConferencePaperService conferencePaperService;

	private JournalPaperService journalPaperService;

	private MiscDocumentService miscDocumentService;

	private ReportService reportService;

	private ThesisService thesisService;

	private KeyNoteService keyNoteService;

	private JournalEditionService journalEditionService;

	private DoiTools doiTools;

	private KrisRIS test;

	@BeforeEach
	public void setUp() {
		this.prePublicationFactory = mock(PrePublicationFactory.class);
		this.journalService = mock(JournalService.class);
		this.conferenceService = mock(ConferenceService.class);
		this.personService = mock(PersonService.class);
		this.bookService = mock(BookService.class);
		this.bookChapterService = mock(BookChapterService.class);
		this.conferencePaperService = mock(ConferencePaperService.class);
		this.journalPaperService = mock(JournalPaperService.class);
		this.miscDocumentService = mock(MiscDocumentService.class);
		this.reportService = mock(ReportService.class);
		this.thesisService = mock(ThesisService.class);
		this.keyNoteService = mock(KeyNoteService.class);
		this.journalEditionService = mock(JournalEditionService.class);
		this.doiTools = new DefaultDoiTools();
		this.test = new KrisRIS(
				this.prePublicationFactory,
				this.journalService,
				this.conferenceService,
				this.personService,
				this.bookService,
				this.bookChapterService,
				this.conferencePaperService,
				this.journalPaperService,
				this.miscDocumentService,
				this.reportService,
				this.thesisService,
				this.keyNoteService,
				this.journalEditionService,
				this.doiTools);
	}

	private String lines(String... lines) {
		final StringBuilder b = new StringBuilder();
		for (final String line : lines) {
			if (b.length() > 0) {
				b.append("\n");
			}
			b.append(Strings.nullToEmpty(line));
		}
		return b.toString();
	}

	private void preparePublicationForExportTest(Publication pub, PublicationType type) {
		Person p0 = mock(Person.class);
		lenient().when(p0.getFirstName()).thenReturn("Firstname0");
		lenient().when(p0.getLastName()).thenReturn("Lastname0");

		Person p1 = mock(Person.class);
		lenient().when(p1.getFirstName()).thenReturn("Firstname1");
		lenient().when(p1.getLastName()).thenReturn("Lastname1");

		lenient().when(pub.getAbstractText()).thenReturn("Abs 1");
		lenient().when(pub.getAuthors()).thenReturn(Arrays.asList(p1, p0));
		lenient().when(pub.getCategory()).thenCallRealMethod();
		lenient().when(pub.getCategoryWithSupplier(any())).thenCallRealMethod();
		lenient().when(pub.getDblpURL()).thenReturn("DBLP/1");
		lenient().when(pub.getDOI()).thenReturn("doi/1");
		lenient().when(pub.getExtraURL()).thenReturn("url/1");
		if (!(pub instanceof JournalBasedPublication) && !(pub instanceof ConferenceBasedPublication)) {
			lenient().when(pub.getISBN()).thenReturn("isbn/1");
			lenient().when(pub.getISSN()).thenReturn("issn/1");
		}
		lenient().when(pub.getKeywords()).thenReturn("keyword 1, keyword 2");
		lenient().when(pub.getMajorLanguage()).thenReturn(PublicationLanguage.ENGLISH);
		lenient().when(pub.getPathToDownloadableAwardCertificate()).thenReturn("path/to/award");
		lenient().when(pub.getPathToDownloadablePDF()).thenReturn("path/to/pdf");
		lenient().when(pub.getPublicationDate()).thenReturn(LocalDate.parse("2022-07-24"));
		lenient().when(pub.getPublicationYear()).thenReturn(2022);
		lenient().when(pub.getTitle()).thenReturn("Title 1");
		lenient().when(pub.getType()).thenReturn(type);
		lenient().when(pub.getVideoURL()).thenReturn("video/1");
	}

	@Test
	@DisplayName("exportPublications(journal)")
	public void exportPublications_journalPaper() {
		Journal journal = mock(Journal.class);
		when(journal.getJournalName()).thenReturn("journal name//1");
		when(journal.getPublisher()).thenReturn("publisher//1");
		when(journal.getISBN()).thenReturn("isbn//1");
		when(journal.getISSN()).thenReturn("issn//1");
		when(journal.getAddress()).thenReturn("addr//1");
		JournalPaper pub = mock(JournalPaper.class);
		preparePublicationForExportTest(pub, PublicationType.INTERNATIONAL_JOURNAL_PAPER);
		when(pub.getImpactFactor()).thenReturn(123.456f);
		when(pub.getJournal()).thenReturn(journal);
		when(pub.getVolume()).thenReturn("vol/1");
		when(pub.getNumber()).thenReturn("nb/1");
		when(pub.getPages()).thenReturn("pages/1");
		when(pub.getScimagoQIndex()).thenReturn(QuartileRanking.Q2);

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"TY  - JOUR",
				"AB  - Abs 1",
				"AU  - Lastname1, Firstname1",
				"AU  - Lastname0, Firstname0",
				"C1  - ACLN",
				"C2  - Articles in international or national journals with selection committee and not ranked in international databases",
				"C3  - Q2",
				"C5  - 123.456",
				"DO  - doi/1",
				"JO  - journal name//1",
				"KW  - keyword 1",
				"KW  - keyword 2",
				"LA  - ENGLISH",
				"NV  - nb/1",
				"PB  - publisher//1",
				"PP  - addr//1",
				"PY  - 2022",
				"SN  - isbn//1",
				"TI  - Title 1",
				"UR  - url/1",
				"VL  - vol/1",
				"ER  - ",
				""), bibtex);
	}

	@Test
	@DisplayName("exportPublications(book)")
	public void exportPublications_book() {
		Book pub = mock(Book.class);
		preparePublicationForExportTest(pub, PublicationType.INTERNATIONAL_BOOK);
		when(pub.getVolume()).thenReturn("vol/1");
		when(pub.getNumber()).thenReturn("nb/1");
		when(pub.getPages()).thenReturn("pages/1");
		when(pub.getAddress()).thenReturn("adr/1");
		when(pub.getEdition()).thenReturn("edition/1");
		when(pub.getEditors()).thenReturn("Editor1, Editor2 and Editor3, Editor4");
		when(pub.getPublisher()).thenReturn("publisher/1");
		when(pub.getSeries()).thenReturn("series/1");

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"TY  - BOOK",
				"AB  - Abs 1",
				"AU  - Lastname1, Firstname1",
				"AU  - Lastname0, Firstname0",
				"C1  - OS",
				"C2  - Scientific books",
				"DO  - doi/1",
				"ED  - Editor1, Editor2 and Editor3, Editor4",
				"ET  - edition/1",
				"KW  - keyword 1",
				"KW  - keyword 2",
				"LA  - ENGLISH",
				"NV  - nb/1",
				"PB  - publisher/1",
				"PP  - adr/1",
				"PY  - 2022",
				"SE  - series/1",
				"SN  - isbn/1",
				"TI  - Title 1",
				"UR  - url/1",
				"VL  - vol/1",
				"ER  - ",
				""), bibtex);
	}

	@Test
	@DisplayName("exportPublications(bookChapter)")
	public void exportPublications_bookChapter() {
		BookChapter pub = mock(BookChapter.class);
		preparePublicationForExportTest(pub, PublicationType.INTERNATIONAL_BOOK_CHAPTER);
		when(pub.getVolume()).thenReturn("vol/1");
		when(pub.getNumber()).thenReturn("nb/1");
		when(pub.getPages()).thenReturn("pages/1");
		when(pub.getAddress()).thenReturn("adr/1");
		when(pub.getEdition()).thenReturn("edition/1");
		when(pub.getEditors()).thenReturn("Editor1, Editor2 and Editor3, Editor4");
		when(pub.getPublisher()).thenReturn("publisher/1");
		when(pub.getSeries()).thenReturn("series/1");

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"TY  - CHAP",
				"AB  - Abs 1",
				"AU  - Lastname1, Firstname1",
				"AU  - Lastname0, Firstname0",
				"C1  - COS",
				"C2  - Chapters in scientific books",
				"DO  - doi/1",
				"ED  - Editor1, Editor2 and Editor3, Editor4",
				"ET  - edition/1",
				"KW  - keyword 1",
				"KW  - keyword 2",
				"LA  - ENGLISH",
				"NV  - nb/1",
				"PB  - publisher/1",
				"PP  - adr/1",
				"PY  - 2022",
				"SN  - isbn/1",
				"T3  - series/1",
				"TI  - Title 1",
				"UR  - url/1",
				"VL  - vol/1",
				"ER  - ",
				""), bibtex);
	}

	@Test
	@DisplayName("exportPublications(PhDThesis)")
	public void exportPublications_phdthesis() {
		Thesis pub = mock(Thesis.class);
		preparePublicationForExportTest(pub, PublicationType.PHD_THESIS);
		when(pub.getInstitution()).thenReturn("inst/1");
		when(pub.getAddress()).thenReturn("adr/1");

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"TY  - THES",
				"AB  - Abs 1",
				"AU  - Lastname1, Firstname1",
				"AU  - Lastname0, Firstname0",
				"C1  - TH",
				"C2  - Theses (HDR, PHD, Master)",
				"C3  - PhD theses",
				"DO  - doi/1",
				"KW  - keyword 1",
				"KW  - keyword 2",
				"LA  - ENGLISH",
				"PB  - inst/1",
				"PP  - adr/1",
				"PY  - 2022",
				"SN  - isbn/1",
				"TI  - Title 1",
				"UR  - url/1",
				"ER  - ",
				""), bibtex);
	}

	@Test
	public void exportPublications_masterthesis() {
		Thesis pub = mock(Thesis.class);
		preparePublicationForExportTest(pub, PublicationType.MASTER_THESIS);
		when(pub.getInstitution()).thenReturn("inst/1");
		when(pub.getAddress()).thenReturn("adr/1");

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"TY  - THES",
				"AB  - Abs 1",
				"AU  - Lastname1, Firstname1",
				"AU  - Lastname0, Firstname0",
				"C1  - TH",
				"C2  - Theses (HDR, PHD, Master)",
				"C3  - Master theses",
				"DO  - doi/1",
				"KW  - keyword 1",
				"KW  - keyword 2",
				"LA  - ENGLISH",
				"PB  - inst/1",
				"PP  - adr/1",
				"PY  - 2022",
				"SN  - isbn/1",
				"TI  - Title 1",
				"UR  - url/1",
				"ER  - ",
				""), bibtex);
	}

	@Test
	@DisplayName("exportPublications(journalEdition)")
	public void exportPublications_journalEdition() {
		Journal journal = mock(Journal.class);
		when(journal.getJournalName()).thenReturn("journal name//1");
		when(journal.getISBN()).thenReturn("isbn//1");
		when(journal.getISSN()).thenReturn("issn//1");
		when(journal.getAddress()).thenReturn("adr//1");
		when(journal.getPublisher()).thenReturn("publisher//1");
		JournalEdition pub = mock(JournalEdition.class);
		preparePublicationForExportTest(pub, PublicationType.INTERNATIONAL_JOURNAL_EDITION);
		when(pub.getImpactFactor()).thenReturn(123.456f);
		when(pub.getJournal()).thenReturn(journal);
		when(pub.getVolume()).thenReturn("vol/1");
		when(pub.getNumber()).thenReturn("nb/1");
		when(pub.getPages()).thenReturn("pages/1");
		when(pub.getScimagoQIndex()).thenReturn(QuartileRanking.Q2);

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"TY  - EDBOOK",
				"AB  - Abs 1",
				"AU  - Lastname1, Firstname1",
				"AU  - Lastname0, Firstname0",
				"C1  - DO",
				"C2  - Editor of books or journals",
				"C3  - Q2",
				"C5  - 123.456",
				"DO  - doi/1",
				"JO  - journal name//1",
				"KW  - keyword 1",
				"KW  - keyword 2",
				"LA  - ENGLISH",
				"NV  - nb/1",
				"PB  - publisher//1",
				"PP  - adr//1",
				"PY  - 2022",
				"SN  - isbn//1",
				"TI  - Title 1",
				"UR  - url/1",
				"VL  - vol/1",
				"ER  - ",
				""), bibtex);
	}

	@Test
	@DisplayName("exportPublications(conferencePaper)")
	public void exportPublications_conferencePaper() {
		Conference conf = mock(Conference.class);
		when(conf.getAcronym()).thenReturn("ACR");
		when(conf.getName()).thenReturn("event//1");
		when(conf.getPublisher()).thenReturn("publisher//1");
		when(conf.getISBN()).thenReturn("isbn//1");
		when(conf.getISSN()).thenReturn("issn//1");
		
		ConferencePaper pub = mock(ConferencePaper.class);
		preparePublicationForExportTest(pub, PublicationType.INTERNATIONAL_CONFERENCE_PAPER);
		when(pub.getPublicationTarget()).thenCallRealMethod();
		when(pub.getConference()).thenReturn(conf);
		when(pub.getConferenceOccurrenceNumber()).thenReturn(1234);
		when(pub.getOrganization()).thenReturn("orga/1");
		when(pub.getVolume()).thenReturn("vol/1");
		when(pub.getNumber()).thenReturn("nb/1");
		when(pub.getAddress()).thenReturn("adr/1");
		when(pub.getEditors()).thenReturn("Editor1, Editor2 and Editor3, Editor4");

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"TY  - CPAPER",
				"AB  - Abs 1",
				"AU  - Lastname1, Firstname1",
				"AU  - Lastname0, Firstname0",
				"C1  - C_ACTI",
				"C2  - Papers in the proceedings of an international conference",
				"C4  - orga/1",
				"DO  - doi/1",
				"ED  - Editor1, Editor2 and Editor3, Editor4",
				"KW  - keyword 1",
				"KW  - keyword 2",
				"LA  - ENGLISH",
				"NV  - nb/1",
				"PB  - publisher//1",
				"PP  - adr/1",
				"PY  - 2022",
				"SN  - isbn//1",
				"T2  - 1234th event//1 (ACR-22)",
				"TI  - Title 1",
				"UR  - url/1",
				"VL  - vol/1",
				"ER  - ",
				""), bibtex);
	}

	@Test
	@DisplayName("exportPublications(keyNote)")
	public void exportPublications_keynote() {
		Conference conf = mock(Conference.class);
		when(conf.getAcronym()).thenReturn("ACR");
		when(conf.getName()).thenReturn("event//1");
		
		KeyNote pub = mock(KeyNote.class);
		preparePublicationForExportTest(pub, PublicationType.INTERNATIONAL_KEYNOTE);
		when(pub.getPublicationTarget()).thenCallRealMethod();
		when(pub.getConference()).thenReturn(conf);
		when(pub.getConferenceOccurrenceNumber()).thenReturn(1234);
		when(pub.getOrganization()).thenReturn("orga/1");
		when(pub.getAddress()).thenReturn("adr/1");
		when(pub.getEditors()).thenReturn("Editor1, Editor2 and Editor3, Editor4");

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"TY  - HEAR",
				"AB  - Abs 1",
				"AU  - Lastname1, Firstname1",
				"AU  - Lastname0, Firstname0",
				"C1  - C_INV",
				"C2  - Keynotes in international or national conference",
				"C3  - Keynotes in international conference",
				"DO  - doi/1",
				"ED  - Editor1, Editor2 and Editor3, Editor4",
				"JO  - 1234th event//1 (ACR-22)",
				"KW  - keyword 1",
				"KW  - keyword 2",
				"LA  - ENGLISH",
				"PB  - orga/1",
				"PP  - adr/1",
				"PY  - 2022",
				"TI  - Title 1",
				"UR  - url/1",
				"ER  - ",
				""), bibtex);
	}

	@Test
	@DisplayName("exportPublications(techReport)")
	public void exportPublications_techreport() {
		Report pub = mock(Report.class);
		preparePublicationForExportTest(pub, PublicationType.TECHNICAL_REPORT);
		when(pub.getInstitution()).thenReturn("inst/1");
		when(pub.getAddress()).thenReturn("adr/1");
		when(pub.getReportNumber()).thenReturn("num/1");
		when(pub.getReportType()).thenReturn("type/1");

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"TY  - RPRT",
				"AB  - Abs 1",
				"AU  - Lastname1, Firstname1",
				"AU  - Lastname0, Firstname0",
				"C1  - AP",
				"C2  - Other productions",
				"C3  - type/1",
				"DO  - doi/1",
				"KW  - keyword 1",
				"KW  - keyword 2",
				"LA  - ENGLISH",
				"PB  - inst/1",
				"PP  - adr/1",
				"PY  - 2022",
				"SN  - isbn/1",
				"TI  - Title 1",
				"UR  - url/1",
				"VL  - num/1",
				"ER  - ",
				""), bibtex);
	}

	@Test
	@DisplayName("exportPublications(manual)")
	public void exportPublications_manual() {
		Report pub = mock(Report.class);
		preparePublicationForExportTest(pub, PublicationType.TUTORIAL_DOCUMENTATION);
		when(pub.getInstitution()).thenReturn("inst/1");
		when(pub.getAddress()).thenReturn("adr/1");
		when(pub.getReportNumber()).thenReturn("num/1");
		when(pub.getReportType()).thenReturn("type/1");

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"TY  - RPRT",
				"AB  - Abs 1",
				"AU  - Lastname1, Firstname1",
				"AU  - Lastname0, Firstname0",
				"C1  - AP",
				"C2  - Other productions",
				"C3  - type/1",
				"DO  - doi/1",
				"KW  - keyword 1",
				"KW  - keyword 2",
				"LA  - ENGLISH",
				"PB  - inst/1",
				"PP  - adr/1",
				"PY  - 2022",
				"SN  - isbn/1",
				"TI  - Title 1",
				"UR  - url/1",
				"VL  - num/1",
				"ER  - ",
				""), bibtex);
	}

	@Test
	@DisplayName("exportPublications(patent)")
	public void exportPublications_patent() {
		Patent pub = mock(Patent.class);
		preparePublicationForExportTest(pub, PublicationType.INTERNATIONAL_PATENT);
		when(pub.getInstitution()).thenReturn("inst/1");
		when(pub.getAddress()).thenReturn("adr/1");
		when(pub.getPatentNumber()).thenReturn("bre/1");
		when(pub.getPatentType()).thenReturn("int./1");

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"TY  - PAT",
				"AB  - Abs 1",
				"AU  - Lastname1, Firstname1",
				"AU  - Lastname0, Firstname0",
				"C1  - BRE",
				"C2  - Patents",
				"C3  - int./1",
				"DO  - doi/1",
				"KW  - keyword 1",
				"KW  - keyword 2",
				"LA  - ENGLISH",
				"PB  - inst/1",
				"PP  - adr/1",
				"PY  - 2022",
				"SN  - isbn/1",
				"TI  - Title 1",
				"UR  - url/1",
				"VO  - bre/1",
				"ER  - ",
				""), bibtex);
	}

	@Test
	@DisplayName("exportPublications(misc artistic production)")
	public void exportPublications_misc() {
		MiscDocument pub = mock(MiscDocument.class);
		preparePublicationForExportTest(pub, PublicationType.ARTISTIC_PRODUCTION);
		when(pub.getDocumentNumber()).thenReturn("num/1");
		when(pub.getDocumentType()).thenReturn("type/1");
		when(pub.getAddress()).thenReturn("adr/1");
		when(pub.getHowPublished()).thenReturn("how/1");
		when(pub.getOrganization()).thenReturn("orga/1");
		when(pub.getPublisher()).thenReturn("publisher/1");

		final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

		assertEquals(lines(
				"TY  - ART",
				"AB  - Abs 1",
				"AU  - Lastname1, Firstname1",
				"AU  - Lastname0, Firstname0",
				"C1  - PAT",
				"C2  - Artistic research productions",
				"C3  - type/1",
				"DO  - doi/1",
				"ED  - orga/1",
				"KW  - keyword 1",
				"KW  - keyword 2",
				"LA  - ENGLISH",
				"PB  - publisher/1",
				"PP  - adr/1",
				"PY  - 2022",
				"SN  - isbn/1",
				"T2  - how/1",
				"TI  - Title 1",
				"UR  - url/1",
				"VL  - num/1",
				"ER  - ",
				""), bibtex);
	}

	@Test
	@DisplayName("exportPublications(x) / publication type")
	public void exportPublications_perPublicationType() {
		for (final PublicationType type : PublicationType.values()) {
			Publication pub = mock(type.getInstanceType());
			preparePublicationForExportTest(pub, type);
			lenient().when(pub.getPreferredStringId()).thenReturn("Pub/" + type.name());
			lenient().when(pub.getAbstractText()).thenReturn("abs/" + type.name());
			lenient().when(pub.getDblpURL()).thenReturn("dblp/" + type.name());
			lenient().when(pub.getDOI()).thenReturn("doi/" + type.name());
			lenient().when(pub.getExtraURL()).thenReturn("extra/" + type.name());
			lenient().when(pub.getHalId()).thenReturn("hal/" + type.name());
			lenient().when(pub.getISBN()).thenReturn("isbn/" + type.name());
			lenient().when(pub.getISSN()).thenReturn("issn/" + type.name());
			lenient().when(pub.getPublicationYear()).thenReturn(2022);
			lenient().when(pub.getTitle()).thenReturn("title/" + type.name());

			final String bibtex = this.test.exportPublications(Arrays.asList(pub), new ExporterConfigurator(null));

			assertFalse(Strings.isNullOrEmpty(bibtex));
		}
	}

	private Stream<Publication> getPublicationStreamFromTest(String filename) throws Exception {
		URL url = Resources.getResource(getClass().getPackageName().replaceAll("\\.", "/") + "/" + filename);
		try (Reader r = new InputStreamReader(url.openStream())) {
			return this.test.getPublicationStreamFrom(r, false, false, false, false, false);
		}
	}

	@Test
	public void getPublicationStreamFrom_0() throws Exception {
		Stream<Publication> pubs = getPublicationStreamFromTest("ris_0.ris");
		assertNotNull(pubs);
		List<Publication> list = pubs.collect(Collectors.toList());
		assertTrue(list.isEmpty());
	}

	@Test
	public void getPublicationStreamFrom_n() throws Exception {
		Publication prePublication = mock(Publication.class);
		when(this.prePublicationFactory.createPrePublication(
				any(), any(), any(), any(), any(), anyInt(), any(), any(), any(),
				any(), any(), any(), any(), any(), any(), any())).thenReturn(prePublication);

		JournalPaper jp = mock(JournalPaper.class);
		when(this.journalPaperService.createJournalPaper(
				any(), any(), any(), any(), any(), any(), anyBoolean())).thenReturn(jp);

		ConferencePaper cp = mock(ConferencePaper.class);
		when(this.conferencePaperService.createConferencePaper(
				any(), any(), anyInt(), any(), any(), any(), any(), any(), any(), any(), anyBoolean())).thenReturn(cp);

		Journal journal = mock(Journal.class);
		when(this.journalService.getJournalsByName(any())).thenReturn(Collections.singleton(journal));

		Conference conference = mock(Conference.class);
		when(this.conferenceService.getConferencesByName(any())).thenReturn(Collections.singleton(conference));

		Person p0 = mock(Person.class);
		Person p1 = mock(Person.class);
		when(this.personService.extractPersonsFrom(any(), anyBoolean(), anyBoolean(), anyBoolean())).thenReturn(Arrays.asList(p0, p1));

		Stream<Publication> pubs = getPublicationStreamFromTest("ris_n.ris");
		assertNotNull(pubs);
		List<Publication> list = pubs.collect(Collectors.toList());
		assertFalse(list.isEmpty());
		assertEquals(2, list.size());

		Publication p;

		p = list.get(0);
		assertTrue(p instanceof JournalPaper);
		verify(this.journalService).getJournalsByName(
				eq("European Journal of Immunology")); // journal name
		verify(this.prePublicationFactory, atLeastOnce()).createPrePublication(
				eq(PublicationType.INTERNATIONAL_JOURNAL_PAPER), //type
				eq("T-lymphocytes from normal human peritoneum are phenotypically different from their counterparts in peripheral blood and CD3- lymphocyte subsets contain mRNA for the recombination activating gene RAG-1"), // title
				eq("These findings are compatible with the hypothesis that the adult human peritoneum provides a microenvirinment capable of supporting a thymus-independent differentiation of T lymphocytes."), // abstract
				eq("Peritoneum; T cell; T lymphocyte; lymphocyte; immunology; CD3; human; Adult; blood"), // keywords
				isNull(), // date
				eq(1995), // year
				isNull(), // isbn
				isNull(), // issn
				isNull(), // doi
				isNull(), // halid
				isNull(), // extra url
				isNull(), // video url
				isNull(), // dblp url
				isNull(), // pdf path
				isNull(), // award path
				eq(PublicationLanguage.ENGLISH));
		ArgumentCaptor<Publication> actualPrePublication = ArgumentCaptor.forClass(Publication.class);
		verify(this.journalPaperService).createJournalPaper(
				actualPrePublication.capture(), // publication
				eq("25"), // volume
				isNull(), // number
				eq("2626-2631"), // pages
				isNull(), // series
				same(journal), // journal
				eq(false));
		assertNotNull(actualPrePublication.getValue());
		assertSame(prePublication, actualPrePublication.getValue());
		verify(p).setTemporaryAuthors(any());
		verify(this.personService).extractPersonsFrom(
				eq("Hartmann,J. and Maassen,V. and Rieber,P. and Fricke,H."), // authors
				eq(true),
				eq(false),
				eq(false));

		p = list.get(1);
		assertTrue(p instanceof ConferencePaper);
		verify(this.conferenceService).getConferencesByName(
				eq("Advances in Practical Applications of Agents, Multi-Agent Systems, and Cognitive Mimetics. The PAAMS Collection")); // conference name
		verify(this.prePublicationFactory, atLeastOnce()).createPrePublication(
				eq(PublicationType.INTERNATIONAL_CONFERENCE_PAPER), // type
				eq("Towards Exception Handling in the SARL Agent Platform"), // title
				eq("We demonstrate how exception handling can be realized in the SARL agent platform. We see exception handling as a mechanism binding some agents, entitled to raise given exceptions, to the ones entitled to handle them. To this end, we define dedicated exception spaces through which defining the agents’ behavior in presence of exceptions."), // abstract
				isNull(), // keywords
				isNull(), // date
				eq(2023), // year
				eq("978-3-031-37616-0"), // isbn
				isNull(), // issn
				eq("10.1007/978-3-031-37616-0_33"), // doi
				isNull(), // halid
				isNull(), // extra url
				isNull(), // video url
				isNull(), // dblp url
				isNull(), // pdf path
				isNull(), // award path
				eq(PublicationLanguage.ENGLISH));
		ArgumentCaptor<Conference> actualConference = ArgumentCaptor.forClass(Conference.class);
		actualPrePublication = ArgumentCaptor.forClass(Publication.class);
		verify(this.conferencePaperService).createConferencePaper(
				actualPrePublication.capture(),
				actualConference.capture(),
				eq(0),
				isNull(), // volume
				isNull(), // number
				eq("403-408"), // pages
				// TODO: Bug in Kris API, only the last editor field is read, multiple "ED" are ignored
				eq("De la Prieta, Fernando"), // editors
				isNull(), // series
				isNull(), // organization
				isNull(), // address
				eq(false));
		assertNotNull(actualPrePublication.getValue());
		assertSame(prePublication, actualPrePublication.getValue());
		assertNotNull(actualConference.getValue());
		assertSame(conference, actualConference.getValue());
		verify(p).setTemporaryAuthors(any());
		verify(this.personService).extractPersonsFrom(
				eq("Baldoni, Matteo and Baroglio, Cristina and Micalizio, Roberto and Tedeschi, Stefano"), // authors
				eq(true),
				eq(false),
				eq(false));
	}

}
