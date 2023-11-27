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

package fr.ciadlab.labmanager.io.ris;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.ciadlab.labmanager.configuration.BaseMessageSource;
import fr.ciadlab.labmanager.entities.conference.Conference;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.ConferenceBasedPublication;
import fr.ciadlab.labmanager.entities.publication.JournalBasedPublication;
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
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.io.bibtex.JBibtexBibTeX.ConferenceNameComponents;
import fr.ciadlab.labmanager.io.bibtex.bugfix.BugfixLaTeXPrinter;
import fr.ciadlab.labmanager.service.conference.ConferenceService;
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
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.Resources;
import org.jbibtex.LaTeXObject;
import org.jbibtex.LaTeXParser;
import org.jbibtex.LaTeXPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

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

	private KrisRIS test;

	@BeforeEach
	public void setUp() {
		this.test = new KrisRIS();
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
				"TY  - GEN",
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

}
