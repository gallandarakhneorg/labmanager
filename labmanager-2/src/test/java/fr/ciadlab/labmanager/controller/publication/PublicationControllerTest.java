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

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import fr.ciadlab.labmanager.io.bibtex.BibTeX;
import fr.ciadlab.labmanager.io.od.OpenDocumentTextExporter;
import fr.ciadlab.labmanager.service.journal.JournalService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.publication.PrePublicationFactory;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import fr.ciadlab.labmanager.service.publication.type.JournalPaperService;
import fr.ciadlab.labmanager.utils.ViewFactory;
import fr.ciadlab.labmanager.utils.files.DownloadableFileManager;
import fr.ciadlab.labmanager.utils.names.DefaultPersonNameParser;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

/** Tests for {@link PublicationController}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class PublicationControllerTest {

	private PrePublicationFactory prePublicationFactory;

	private PublicationService publicationService;

	private PersonService personService;

	private OpenDocumentTextExporter odtExporter;

	private DownloadableFileManager fileManager;

	private PersonNameParser nameParser;

	private BibTeX bibtex;

	private ViewFactory viewFactory;

	private JournalService journalService;

	private JournalPaperService journalPaperService;

	private PublicationController test;

	@BeforeEach
	public void setUp() {
		this.prePublicationFactory = mock(PrePublicationFactory.class);
		this.publicationService = mock(PublicationService.class);
		this.odtExporter = mock(OpenDocumentTextExporter.class);
		this.personService = mock(PersonService.class);
		this.fileManager = mock(DownloadableFileManager.class);
		this.nameParser = new DefaultPersonNameParser();
		this.bibtex = mock(BibTeX.class);
		this.viewFactory = mock(ViewFactory.class);
		this.journalService = mock(JournalService.class);
		this.journalPaperService = mock(JournalPaperService.class);
		this.test = new PublicationController(
				this.prePublicationFactory, this.publicationService,
				this.personService, this.fileManager,
				this.nameParser, this.bibtex, this.viewFactory, this.journalService,
				this.journalPaperService);
		this.test.setLogger(mock(Logger.class));
	}

	private List<Person> mockAuthors(int id) {
		final Person p0 = mock(Person.class);
		final int pid0 = 123000 + id;
		lenient().when(p0.getId()).thenReturn(pid0);
		lenient().when(p0.getFirstName()).thenReturn("FN0_" + pid0);
		lenient().when(p0.getLastName()).thenReturn("LN0_" + pid0);
		lenient().when(p0.getFullName()).thenReturn("FN0_" + pid0 + " LN0_" + pid0);
		lenient().when(p0.getEmail()).thenReturn("0@" + pid0);
		final Person p1 = mock(Person.class);
		final int pid1 = 234000 + id;
		lenient().when(p1.getId()).thenReturn(pid1);
		lenient().when(p1.getFirstName()).thenReturn("FN1_" + pid1);
		lenient().when(p1.getLastName()).thenReturn("LN1_" + pid1);
		lenient().when(p1.getFullName()).thenReturn("FN1_" + pid1 + " LN1_" + pid1);
		lenient().when(p1.getEmail()).thenReturn("1@" + pid1);
		return Arrays.asList(p1, p0);
	}

	private Publication mockPublication(int id) {
		final JournalPaper pub = mock(JournalPaper.class);
		lenient().when(pub.getId()).thenReturn(id);
		lenient().when(pub.getType()).thenReturn(PublicationType.INTERNATIONAL_JOURNAL_PAPER);
		lenient().when(pub.getTitle()).thenReturn("title" + id);
		lenient().doReturn(mockAuthors(id)).when(pub).getAuthors();
		lenient().when(pub.getPublicationDate()).thenReturn(LocalDate.parse("2022-07-28"));
		lenient().when(pub.getPublicationYear()).thenReturn(2022);
		lenient().when(pub.getAbstractText()).thenReturn("abs" + id);
		lenient().when(pub.getKeywords()).thenReturn("kw" + id);
		lenient().when(pub.getDblpURL()).thenReturn("dblp" + id);
		lenient().when(pub.getDOI()).thenReturn("doi" + id);
		lenient().when(pub.getISBN()).thenReturn("isbn" + id);
		lenient().when(pub.getISSN()).thenReturn("issn" + id);
		lenient().when(pub.getExtraURL()).thenReturn("extra" + id);
		lenient().when(pub.getVideoURL()).thenReturn("video" + id);
		lenient().when(pub.getPathToDownloadablePDF()).thenReturn("path/to/pdf" + id);
		lenient().when(pub.getPathToDownloadableAwardCertificate()).thenReturn("path/to/award" + id);
		lenient().when(pub.getMajorLanguage()).thenReturn(PublicationLanguage.ITALIAN);
		return pub;
	}

//	@Test
//	public void showPublicationTool() {
//		final ModelAndView mv = this.test.showPublicationTool();
//		assertEquals("publicationTool", mv.getViewName());
//	}
//
//	@Test
//	public void getPublicationList_noAuthorId() {
//		Publication p0 = mockPublication(1234);
//		Publication p1 = mockPublication(2345);
//		when(this.publicationService.getAllPublications()).thenReturn(Arrays.asList(p0, p1));
//
//		final String actual = this.test.getPublicationList(null, false);
//
//		assertEquals("{\"data\":[{\"data\":{\"id\":1234,\"type\":\"INTERNATIONAL_JOURNAL_PAPER\",\"publicationYear\":2022}},{\"data\":{\"id\":2345,\"type\":\"INTERNATIONAL_JOURNAL_PAPER\",\"publicationYear\":2022}}]}", actual);
//
//		verify(this.htmlExporter, atLeastOnce()).getButtonToDownloadPublicationPDF("path/to/pdf1234");
//		verify(this.htmlExporter, atLeastOnce()).getButtonToDownloadPublicationPDF("path/to/pdf2345");
//		verify(this.htmlExporter, atLeastOnce()).getButtonToDownloadPublicationAwardCertificate("path/to/award1234");
//		verify(this.htmlExporter, atLeastOnce()).getButtonToDownloadPublicationAwardCertificate("path/to/award2345");
//		verify(this.htmlExporter, atLeastOnce()).getButtonToExportPublicationToBibTeX(1234);
//		verify(this.htmlExporter, atLeastOnce()).getButtonToExportPublicationToBibTeX(2345);
//		verify(this.htmlExporter, atLeastOnce()).getButtonToExportPublicationToHtml(1234);
//		verify(this.htmlExporter, atLeastOnce()).getButtonToExportPublicationToHtml(2345);
//		verify(this.htmlExporter, atLeastOnce()).getButtonToExportPublicationToOpenDocument(1234);
//		verify(this.htmlExporter, atLeastOnce()).getButtonToExportPublicationToOpenDocument(2345);
//		verify(this.htmlExporter, atLeastOnce()).getButtonToEditPublication(1234);
//		verify(this.htmlExporter, atLeastOnce()).getButtonToEditPublication(2345);
//		verify(this.htmlExporter, atLeastOnce()).getButtonToDeletePublication(1234);
//		verify(this.htmlExporter, atLeastOnce()).getButtonToDeletePublication(2345);
//
//		assertNotNull(actual);
//	}
//
//	@Test
//	public void getPublicationList_authorId() {
//		Publication p0 = mockPublication(1234);
//		Publication p1 = mockPublication(2345);
//		when(this.authorshipService.getPublicationsFor(anyInt())).thenReturn(Arrays.asList(p1, p0));
//
//		final String actual = this.test.getPublicationList(123, false);
//
//
//		assertEquals("{\"data\":[{\"data\":{\"id\":2345,\"type\":\"INTERNATIONAL_JOURNAL_PAPER\",\"publicationYear\":2022}},{\"data\":{\"id\":1234,\"type\":\"INTERNATIONAL_JOURNAL_PAPER\",\"publicationYear\":2022}}]}", actual);
//
//		verify(this.htmlExporter, atLeastOnce()).getButtonToDownloadPublicationPDF("path/to/pdf1234");
//		verify(this.htmlExporter, atLeastOnce()).getButtonToDownloadPublicationPDF("path/to/pdf2345");
//		verify(this.htmlExporter, atLeastOnce()).getButtonToDownloadPublicationAwardCertificate("path/to/award1234");
//		verify(this.htmlExporter, atLeastOnce()).getButtonToDownloadPublicationAwardCertificate("path/to/award2345");
//		verify(this.htmlExporter, atLeastOnce()).getButtonToExportPublicationToBibTeX(1234);
//		verify(this.htmlExporter, atLeastOnce()).getButtonToExportPublicationToBibTeX(2345);
//		verify(this.htmlExporter, atLeastOnce()).getButtonToExportPublicationToHtml(1234);
//		verify(this.htmlExporter, atLeastOnce()).getButtonToExportPublicationToHtml(2345);
//		verify(this.htmlExporter, atLeastOnce()).getButtonToExportPublicationToOpenDocument(1234);
//		verify(this.htmlExporter, atLeastOnce()).getButtonToExportPublicationToOpenDocument(2345);
//		verify(this.htmlExporter, atLeastOnce()).getButtonToEditPublication(1234);
//		verify(this.htmlExporter, atLeastOnce()).getButtonToEditPublication(2345);
//		verify(this.htmlExporter, atLeastOnce()).getButtonToDeletePublication(1234);
//		verify(this.htmlExporter, atLeastOnce()).getButtonToDeletePublication(2345);
//
//		assertNotNull(actual);
//	}
//
//	@Test
//	public void publicationList_nullId() {
//		final List<Person> persons = mockAuthors(123);
//		when(this.personService.getAllPersons()).thenReturn(persons);
//
//		final ModelAndView mv = this.test.publicationList(null);
//
//		assertNotNull(mv);
//		final Map<String, Object> model = mv.getModel();
//		assertNotNull(model);
//
//		assertTrue(model.containsKey("uuid"));
//		assertFalse(Strings.isNullOrEmpty(model.get("uuid").toString()));
//
//		assertTrue(model.containsKey("url"));
//		assertEquals("/SpringRestHibernate/getPublicationsList", model.get("url"));
//
//		assertTrue(model.containsKey("authorsMap"));
//		assertTrue(model.get("authorsMap") instanceof Map);
//		final Map<Integer, String> authors = (Map<Integer, String>) model.get("authorsMap");
//		assertEquals("FN0_123123 LN0_123123", authors.get(123123));
//		assertEquals("FN1_234123 LN1_234123", authors.get(234123));
//	}
//
//	@Test
//	public void publicationList_id() {
//		final List<Person> persons = mockAuthors(123);
//		when(this.personService.getAllPersons()).thenReturn(persons);
//
//		final ModelAndView mv = this.test.publicationList(123);
//
//		assertNotNull(mv);
//		final Map<String, Object> model = mv.getModel();
//		assertNotNull(model);
//
//		assertTrue(model.containsKey("uuid"));
//		assertFalse(Strings.isNullOrEmpty(model.get("uuid").toString()));
//
//		assertTrue(model.containsKey("url"));
//		assertEquals("/SpringRestHibernate/getPublicationsList?authorId=123", model.get("url"));
//
//		assertTrue(model.containsKey("authorsMap"));
//		assertTrue(model.get("authorsMap") instanceof Map);
//		final Map<Integer, String> authors = (Map<Integer, String>) model.get("authorsMap");
//		assertEquals("FN0_123123 LN0_123123", authors.get(123123));
//		assertEquals("FN1_234123 LN1_234123", authors.get(234123));
//	}
//
//	@Test
//	public void publicationListLight_nullId() {
//		final ModelAndView mv = this.test.publicationListLight(null);
//
//		assertNotNull(mv);
//		final Map<String, Object> model = mv.getModel();
//		assertNotNull(model);
//
//		assertTrue(model.containsKey("uuid"));
//		assertFalse(Strings.isNullOrEmpty(model.get("uuid").toString()));
//
//		assertTrue(model.containsKey("url"));
//		assertEquals("/SpringRestHibernate/getPublicationsList", model.get("url"));
//
//		assertFalse(model.containsKey("authorsMap"));
//	}
//
//	@Test
//	public void publicationListLight_id() {
//		final ModelAndView mv = this.test.publicationListLight(123);
//
//		assertNotNull(mv);
//		final Map<String, Object> model = mv.getModel();
//		assertNotNull(model);
//
//		assertTrue(model.containsKey("uuid"));
//		assertFalse(Strings.isNullOrEmpty(model.get("uuid").toString()));
//
//		assertTrue(model.containsKey("url"));
//		assertEquals("/SpringRestHibernate/getPublicationsList?authorId=123", model.get("url"));
//
//		assertFalse(model.containsKey("authorsMap"));
//	}
//
//	@Test
//	public void publicationListPrivate_nullId() {
//		final List<Person> persons = mockAuthors(123);
//		when(this.personService.getAllPersons()).thenReturn(persons);
//
//		final ModelAndView mv = this.test.publicationListPrivate(null);
//
//		assertNotNull(mv);
//		final Map<String, Object> model = mv.getModel();
//		assertNotNull(model);
//
//		assertTrue(model.containsKey("uuid"));
//		assertFalse(Strings.isNullOrEmpty(model.get("uuid").toString()));
//
//		assertTrue(model.containsKey("url"));
//		assertEquals("/SpringRestHibernate/getPublicationsList?onlyValid=false", model.get("url"));
//
//		assertTrue(model.containsKey("authorsMap"));
//		assertTrue(model.get("authorsMap") instanceof Map);
//		final Map<Integer, String> authors = (Map<Integer, String>) model.get("authorsMap");
//		assertEquals("FN0_123123 LN0_123123", authors.get(123123));
//		assertEquals("FN1_234123 LN1_234123", authors.get(234123));
//	}
//
//	@Test
//	public void publicationListPrivate_id() {
//		final List<Person> persons = mockAuthors(123);
//		when(this.personService.getAllPersons()).thenReturn(persons);
//
//		final ModelAndView mv = this.test.publicationListPrivate(123);
//
//		assertNotNull(mv);
//		final Map<String, Object> model = mv.getModel();
//		assertNotNull(model);
//
//		assertTrue(model.containsKey("uuid"));
//		assertFalse(Strings.isNullOrEmpty(model.get("uuid").toString()));
//
//		assertTrue(model.containsKey("url"));
//		assertEquals("/SpringRestHibernate/getPublicationsList?authorId=123&onlyValid=false", model.get("url"));
//
//		assertTrue(model.containsKey("authorsMap"));
//		assertTrue(model.get("authorsMap") instanceof Map);
//		final Map<Integer, String> authors = (Map<Integer, String>) model.get("authorsMap");
//		assertEquals("FN0_123123 LN0_123123", authors.get(123123));
//		assertEquals("FN1_234123 LN1_234123", authors.get(234123));
//	}
//
//	@Test
//	public void deletePublication_nullId() throws Exception {
//		final HttpServletResponse response = mock(HttpServletResponse.class);
//		this.test.deletePublication(response, null);
//		verify(this.publicationService, never()).removePublication(anyInt());
//		verify(response).sendRedirect("/SpringRestHibernate/publicationsListPrivate?success=0");
//	}
//
//	@Test
//	public void deletePublication_id() throws Exception {
//		final HttpServletResponse response = mock(HttpServletResponse.class);
//		this.test.deletePublication(response, 123);
//		verify(this.publicationService).removePublication(123);
//		verify(response).sendRedirect("/SpringRestHibernate/publicationsListPrivate?success=1");
//	}
//
//	@Test
//	public void createPublication() throws Exception {
//		HttpServletResponse response = mock(HttpServletResponse.class);
//		when(this.personService.getPersonIdByName(eq("F0"), eq("L0"))).thenReturn(123);
//		when(this.personService.createPerson(eq("F1"), eq("L1"), isNull(), isNull(), isNull())).thenReturn(234);
//		Publication fake = mock(Publication.class);
//		when(fake.getId()).thenReturn(1234);
//		doReturn(fake).when(this.prePublicationFactory).createPrePublication(
//				any(), anyString(),
//				anyString(), anyString(), isNull(), anyString(), anyString(),
//				anyString(), anyString(), anyString(), anyString(), isNull(), isNull(),
//				any());
//		ConferencePaper paper = mock(ConferencePaper.class);
//		when(this.conferencePaperService.createConferencePaper(any(), anyString(),
//				anyString(), anyString(), anyString(), anyString(), anyString(),
//				anyString(), anyString())).thenReturn(paper);
//		String[] authors = new String[] {
//				"F0 L0",
//				"F1 L1"
//		};
//
//		this.test.createPublication(response,
//				PublicationType.INTERNATIONAL_CONFERENCE_PAPER.name(),
//				"title0",
//				authors,
//				2022,
//				null,
//				"abs0",
//				"kw0",
//				"doi0",
//				"isbn0",
//				"issn0",
//				"url0",
//				"video0",
//				"dblp0",
//				PublicationLanguage.ENGLISH.name(),
//				null,
//				null,
//				"vol0",
//				"nb0",
//				"0-xx",
//				"editors0",
//				"adr0",
//				"series0",
//				"publisher0",
//				"edition0",
//				"book0",
//				"chapter0",
//				"conference0",
//				"orga0",
//				"how0",
//				"doctype0",
//				"inst0");
//
//		ArgumentCaptor<Publication> pub = ArgumentCaptor.forClass(Publication.class);
//		ArgumentCaptor<String> arg0 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg2 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg3 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg4 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg5 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg6 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg7 = ArgumentCaptor.forClass(String.class);
//		verify(this.conferencePaperService).createConferencePaper(pub.capture(), arg0.capture(), arg1.capture(),
//				arg2.capture(), arg3.capture(), arg4.capture(), arg5.capture(), arg6.capture(),
//				arg7.capture());
//		assertEquals("conference0", arg0.getValue());
//		assertEquals("vol0", arg1.getValue());
//		assertEquals("nb0", arg2.getValue());
//		assertEquals("0-xx", arg3.getValue());
//		assertEquals("editors0", arg4.getValue());
//		assertEquals("series0", arg5.getValue());
//		assertEquals("orga0", arg6.getValue());
//		assertEquals("adr0", arg7.getValue());
//
//		assertSame(fake, pub.getValue());
//
//		verify(this.personService).createPerson("F1", "L1", null, null, null);
//		verify(this.authorshipService, atLeastOnce()).addAuthorship(123, 1234, 0);
//		verify(this.authorshipService, atLeastOnce()).addAuthorship(234, 1234, 1);
//
//		verify(response).sendRedirect("/SpringRestHibernate/addPublication?success=1");
//	}
//
//	@Test
//	public void addPublication_publicationId_noFilling_noBibtexAsInput() throws Exception {
//		HttpServletRequest request = mock(HttpServletRequest.class);
//		HttpServletResponse response = mock(HttpServletResponse.class);
//		Person pers0 = mock(Person.class);
//		List<Person> allAuthors = Collections.singletonList(pers0);
//		when(this.personService.getAllPersons()).thenReturn(allAuthors);
//		when(this.authorshipService.getAuthorsFor(anyInt())).thenReturn(allAuthors);
//		Publication p0 = mock(Publication.class);
//		when(this.publicationService.getPublication(anyInt())).thenReturn(p0);
//		List<Journal> allJournals = Collections.singletonList(mock(Journal.class));
//		when(this.journalService.getAllJournals()).thenReturn(allJournals);
//
//		final ModelAndView mv = this.test.addPublication(request, response, false, 123);
//
//		assertNotNull(mv);
//		assertSame(this.journalService, mv.getModel().get("_journalService"));
//		assertEquals(Boolean.TRUE, mv.getModel().get("_edit"));
//		assertSame(allJournals, mv.getModel().get("_journals"));
//		assertSame(allAuthors, mv.getModel().get("_authors"));
//		assertEquals(Arrays.asList(PublicationType.values()), mv.getModel().get("_publicationsTypes"));
//		assertEquals(Arrays.asList(QuartileRanking.values()), mv.getModel().get("_publicationsQuartiles"));
//		assertEquals(Arrays.asList(CoreRanking.values()), mv.getModel().get("_journalCoreRankings"));
//
//		assertSame(allAuthors, mv.getModel().get("authors"));
//		assertSame(p0, mv.getModel().get("_publication"));
//	}
//
//	@Test
//	public void addPublication_noPublicationId_noFilling_noBibtexAsInput() throws Exception {
//		HttpServletRequest request = mock(HttpServletRequest.class);
//		HttpServletResponse response = mock(HttpServletResponse.class);
//		Person pers0 = mock(Person.class);
//		List<Person> allAuthors = Collections.singletonList(pers0);
//		when(this.personService.getAllPersons()).thenReturn(allAuthors);
//		List<Journal> allJournals = Collections.singletonList(mock(Journal.class));
//		when(this.journalService.getAllJournals()).thenReturn(allJournals);
//
//		final ModelAndView mv = this.test.addPublication(request, response, false, null);
//
//		assertNotNull(mv);
//		assertSame(this.journalService, mv.getModel().get("_journalService"));
//		assertEquals(Boolean.FALSE, mv.getModel().get("_edit"));
//		assertSame(allJournals, mv.getModel().get("_journals"));
//		assertSame(allAuthors, mv.getModel().get("_authors"));
//		assertEquals(Arrays.asList(PublicationType.values()), mv.getModel().get("_publicationsTypes"));
//		assertEquals(Arrays.asList(QuartileRanking.values()), mv.getModel().get("_publicationsQuartiles"));
//		assertEquals(Arrays.asList(CoreRanking.values()), mv.getModel().get("_journalCoreRankings"));
//
//		assertNull(mv.getModel().get("authors"));
//		assertNull(mv.getModel().get("_publication"));
//	}
//
//	@Test
//	public void editPublication_noAuthorChange() throws Exception {
//		HttpServletResponse response = mock(HttpServletResponse.class);
//		Person p1 = mock(Person.class);
//		when(p1.getId()).thenReturn(1234);
//		Person p2 = mock(Person.class);
//		when(p2.getId()).thenReturn(2345);
//		when(this.personService.getPersonIdByName(eq("F0"), eq("L0"))).thenReturn(1234);
//		when(this.personService.getPersonIdByName(eq("F1"), eq("L1"))).thenReturn(2345);
//		Publication publi = mock(Publication.class);
//		when(this.publicationService.getPublication(anyInt())).thenReturn(publi);
//		when(this.authorshipService.getAuthorsFor(eq(123456))).thenReturn(Arrays.asList(p1, p2));
//		String[] authors = new String[] {
//				"F0 L0",
//				"F1 L1"
//		};
//
//		this.test.editPublication(response,
//				123456,
//				PublicationType.INTERNATIONAL_CONFERENCE_PAPER.name(),
//				"title0",
//				authors,
//				2022,
//				null,
//				"abs0",
//				"kw0",
//				"doi0",
//				"isbn0",
//				"issn0",
//				"url0",
//				"video0",
//				"dblp0",
//				PublicationLanguage.ENGLISH.name(),
//				null,
//				null,
//				"vol0",
//				"nb0",
//				"0-xx",
//				"editors0",
//				"adr0",
//				"series0",
//				"publisher0",
//				"edition0",
//				"book0",
//				"chapter0",
//				"conference0",
//				"orga0",
//				"how0",
//				"doctype0",
//				"inst0");
//
//		ArgumentCaptor<Integer> arg0 = ArgumentCaptor.forClass(Integer.class);
//		ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<PublicationType> arg2 = ArgumentCaptor.forClass(PublicationType.class);
//		ArgumentCaptor<Date> arg3 = ArgumentCaptor.forClass(Date.class);
//		ArgumentCaptor<String> arg4 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg5 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg6 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg7 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg8 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg9 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg10 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<PublicationLanguage> arg11 = ArgumentCaptor.forClass(PublicationLanguage.class);
//		ArgumentCaptor<String> arg12 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg13 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg14 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg15 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg16 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg17 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg18 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg19 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg20 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg21 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg22 = ArgumentCaptor.forClass(String.class);
//		verify(this.conferencePaperService).updateConferencePaper(
//				arg0.capture(), arg1.capture(), arg2.capture(), arg3.capture(),
//				arg4.capture(), arg5.capture(), arg6.capture(), arg7.capture(),
//				arg8.capture(), arg9.capture(), arg10.capture(),
//				arg11.capture(), arg12.capture(), arg13.capture(), arg14.capture(),
//				arg15.capture(), arg16.capture(), arg17.capture(), arg18.capture(),
//				arg19.capture(), arg20.capture(), arg21.capture(), arg22.capture());
//		assertEquals(123456, arg0.getValue());
//		assertEquals("title0", arg1.getValue());
//		assertEquals(PublicationType.INTERNATIONAL_CONFERENCE_PAPER, arg2.getValue());
//		assertNull(arg3.getValue());
//		assertEquals("abs0", arg4.getValue());
//		assertEquals("kw0", arg5.getValue());
//		assertEquals("doi0", arg6.getValue());
//		assertEquals("isbn0", arg7.getValue());
//		assertEquals("issn0", arg8.getValue());
//		assertEquals("dblp0", arg9.getValue());
//		assertEquals("url0", arg10.getValue());
//		assertEquals(PublicationLanguage.ENGLISH, arg11.getValue());
//		assertNull(arg12.getValue());
//		assertNull(arg13.getValue());
//		assertEquals("video0", arg14.getValue());
//		assertEquals("conference0", arg15.getValue());
//		assertEquals("vol0", arg16.getValue());
//		assertEquals("nb0", arg17.getValue());
//		assertEquals("0-xx", arg18.getValue());
//		assertEquals("editors0", arg19.getValue());
//		assertEquals("series0", arg20.getValue());
//		assertEquals("orga0", arg21.getValue());
//		assertEquals("adr0", arg22.getValue());
//
//		verify(this.personService, never()).createPerson(anyString(), anyString(), any(), any(), any());
//		verify(this.authorshipService, atLeastOnce()).updateAuthorship(1234, 123456, 0);
//		verify(this.authorshipService, atLeastOnce()).updateAuthorship(2345, 123456, 1);
//		verify(this.authorshipService, never()).addAuthorship(anyInt(), anyInt(), anyInt());
//		verify(this.authorshipService, never()).removeAuthorship(anyInt(), anyInt());
//
//		verify(response).sendRedirect("/SpringRestHibernate/addPublication?edit=1&publicationId=123456");
//	}
//
//	@Test
//	public void editPublication_authorRemoved() throws Exception {
//		HttpServletResponse response = mock(HttpServletResponse.class);
//		Person p1 = mock(Person.class);
//		when(p1.getId()).thenReturn(1234);
//		Person p2 = mock(Person.class);
//		when(p2.getId()).thenReturn(2345);
//		when(this.personService.getPersonIdByName(eq("F1"), eq("L1"))).thenReturn(2345);
//		Publication publi = mock(Publication.class);
//		when(this.publicationService.getPublication(anyInt())).thenReturn(publi);
//		when(this.authorshipService.getAuthorsFor(eq(123456))).thenReturn(Arrays.asList(p1, p2));
//		String[] authors = new String[] {
//				"F1 L1"
//		};
//
//		this.test.editPublication(response,
//				123456,
//				PublicationType.INTERNATIONAL_CONFERENCE_PAPER.name(),
//				"title0",
//				authors,
//				2022,
//				null,
//				"abs0",
//				"kw0",
//				"doi0",
//				"isbn0",
//				"issn0",
//				"url0",
//				"video0",
//				"dblp0",
//				PublicationLanguage.ENGLISH.name(),
//				null,
//				null,
//				"vol0",
//				"nb0",
//				"0-xx",
//				"editors0",
//				"adr0",
//				"series0",
//				"publisher0",
//				"edition0",
//				"book0",
//				"chapter0",
//				"conference0",
//				"orga0",
//				"how0",
//				"doctype0",
//				"inst0");
//
//		ArgumentCaptor<Integer> arg0 = ArgumentCaptor.forClass(Integer.class);
//		ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<PublicationType> arg2 = ArgumentCaptor.forClass(PublicationType.class);
//		ArgumentCaptor<Date> arg3 = ArgumentCaptor.forClass(Date.class);
//		ArgumentCaptor<String> arg4 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg5 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg6 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg7 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg8 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg9 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg10 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<PublicationLanguage> arg11 = ArgumentCaptor.forClass(PublicationLanguage.class);
//		ArgumentCaptor<String> arg12 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg13 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg14 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg15 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg16 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg17 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg18 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg19 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg20 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg21 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg22 = ArgumentCaptor.forClass(String.class);
//		verify(this.conferencePaperService).updateConferencePaper(
//				arg0.capture(), arg1.capture(), arg2.capture(), arg3.capture(),
//				arg4.capture(), arg5.capture(), arg6.capture(), arg7.capture(),
//				arg8.capture(), arg9.capture(), arg10.capture(),
//				arg11.capture(), arg12.capture(), arg13.capture(), arg14.capture(),
//				arg15.capture(), arg16.capture(), arg17.capture(), arg18.capture(),
//				arg19.capture(), arg20.capture(), arg21.capture(), arg22.capture());
//		assertEquals(123456, arg0.getValue());
//		assertEquals("title0", arg1.getValue());
//		assertEquals(PublicationType.INTERNATIONAL_CONFERENCE_PAPER, arg2.getValue());
//		assertNull(arg3.getValue());
//		assertEquals("abs0", arg4.getValue());
//		assertEquals("kw0", arg5.getValue());
//		assertEquals("doi0", arg6.getValue());
//		assertEquals("isbn0", arg7.getValue());
//		assertEquals("issn0", arg8.getValue());
//		assertEquals("dblp0", arg9.getValue());
//		assertEquals("url0", arg10.getValue());
//		assertEquals(PublicationLanguage.ENGLISH, arg11.getValue());
//		assertNull(arg12.getValue());
//		assertNull(arg13.getValue());
//		assertEquals("video0", arg14.getValue());
//		assertEquals("conference0", arg15.getValue());
//		assertEquals("vol0", arg16.getValue());
//		assertEquals("nb0", arg17.getValue());
//		assertEquals("0-xx", arg18.getValue());
//		assertEquals("editors0", arg19.getValue());
//		assertEquals("series0", arg20.getValue());
//		assertEquals("orga0", arg21.getValue());
//		assertEquals("adr0", arg22.getValue());
//
//		verify(this.personService, never()).createPerson(anyString(), anyString(), any(), any(), any());
//		verify(this.authorshipService, atLeastOnce()).updateAuthorship(2345, 123456, 0);
//		verify(this.authorshipService, never()).addAuthorship(anyInt(), anyInt(), anyInt());
//		verify(this.authorshipService, atLeastOnce()).removeAuthorship(1234, 123456);
//
//		verify(response).sendRedirect("/SpringRestHibernate/addPublication?edit=1&publicationId=123456");
//	}
//
//	@Test
//	public void editPublication_authorAdded() throws Exception {
//		HttpServletResponse response = mock(HttpServletResponse.class);
//		Person p1 = mock(Person.class);
//		when(p1.getId()).thenReturn(1234);
//		Person p2 = mock(Person.class);
//		when(p2.getId()).thenReturn(2345);
//		when(this.personService.getPersonIdByName(eq("F0"), eq("L0"))).thenReturn(1234);
//		when(this.personService.getPersonIdByName(eq("F1"), eq("L1"))).thenReturn(2345);
//		when(this.personService.getPersonIdByName(eq("F2"), eq("L2"))).thenReturn(3456);
//		Publication publi = mock(Publication.class);
//		when(this.publicationService.getPublication(anyInt())).thenReturn(publi);
//		when(this.authorshipService.getAuthorsFor(eq(123456))).thenReturn(Arrays.asList(p1, p2));
//		String[] authors = new String[] {
//				"F0 L0",
//				"F1 L1",
//				"F2 L2"
//		};
//
//		this.test.editPublication(response,
//				123456,
//				PublicationType.INTERNATIONAL_CONFERENCE_PAPER.name(),
//				"title0",
//				authors,
//				2022,
//				null,
//				"abs0",
//				"kw0",
//				"doi0",
//				"isbn0",
//				"issn0",
//				"url0",
//				"video0",
//				"dblp0",
//				PublicationLanguage.ENGLISH.name(),
//				null,
//				null,
//				"vol0",
//				"nb0",
//				"0-xx",
//				"editors0",
//				"adr0",
//				"series0",
//				"publisher0",
//				"edition0",
//				"book0",
//				"chapter0",
//				"conference0",
//				"orga0",
//				"how0",
//				"doctype0",
//				"inst0");
//
//		ArgumentCaptor<Integer> arg0 = ArgumentCaptor.forClass(Integer.class);
//		ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<PublicationType> arg2 = ArgumentCaptor.forClass(PublicationType.class);
//		ArgumentCaptor<Date> arg3 = ArgumentCaptor.forClass(Date.class);
//		ArgumentCaptor<String> arg4 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg5 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg6 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg7 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg8 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg9 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg10 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<PublicationLanguage> arg11 = ArgumentCaptor.forClass(PublicationLanguage.class);
//		ArgumentCaptor<String> arg12 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg13 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg14 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg15 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg16 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg17 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg18 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg19 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg20 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg21 = ArgumentCaptor.forClass(String.class);
//		ArgumentCaptor<String> arg22 = ArgumentCaptor.forClass(String.class);
//		verify(this.conferencePaperService).updateConferencePaper(
//				arg0.capture(), arg1.capture(), arg2.capture(), arg3.capture(),
//				arg4.capture(), arg5.capture(), arg6.capture(), arg7.capture(),
//				arg8.capture(), arg9.capture(), arg10.capture(),
//				arg11.capture(), arg12.capture(), arg13.capture(), arg14.capture(),
//				arg15.capture(), arg16.capture(), arg17.capture(), arg18.capture(),
//				arg19.capture(), arg20.capture(), arg21.capture(), arg22.capture());
//		assertEquals(123456, arg0.getValue());
//		assertEquals("title0", arg1.getValue());
//		assertEquals(PublicationType.INTERNATIONAL_CONFERENCE_PAPER, arg2.getValue());
//		assertNull(arg3.getValue());
//		assertEquals("abs0", arg4.getValue());
//		assertEquals("kw0", arg5.getValue());
//		assertEquals("doi0", arg6.getValue());
//		assertEquals("isbn0", arg7.getValue());
//		assertEquals("issn0", arg8.getValue());
//		assertEquals("dblp0", arg9.getValue());
//		assertEquals("url0", arg10.getValue());
//		assertEquals(PublicationLanguage.ENGLISH, arg11.getValue());
//		assertNull(arg12.getValue());
//		assertNull(arg13.getValue());
//		assertEquals("video0", arg14.getValue());
//		assertEquals("conference0", arg15.getValue());
//		assertEquals("vol0", arg16.getValue());
//		assertEquals("nb0", arg17.getValue());
//		assertEquals("0-xx", arg18.getValue());
//		assertEquals("editors0", arg19.getValue());
//		assertEquals("series0", arg20.getValue());
//		assertEquals("orga0", arg21.getValue());
//		assertEquals("adr0", arg22.getValue());
//
//		verify(this.personService, never()).createPerson(anyString(), anyString(), any(), any(), any());
//		verify(this.authorshipService, atLeastOnce()).updateAuthorship(1234, 123456, 0);
//		verify(this.authorshipService, atLeastOnce()).updateAuthorship(2345, 123456, 1);
//		verify(this.authorshipService, atLeastOnce()).addAuthorship(3456, 123456, 2);
//		verify(this.authorshipService, never()).removeAuthorship(1234, 123456);
//
//		verify(response).sendRedirect("/SpringRestHibernate/addPublication?edit=1&publicationId=123456");
//	}
//
//	@Test
//	public void importPublications_nullBibTeX() throws Exception {
//		HttpServletResponse response = mock(HttpServletResponse.class);
//		List<Integer> ids = mock(List.class);
//		when(this.publicationService.importPublications(any())).thenReturn(ids);
//
//		List<Integer> actual = this.test.importPublications(response, null);
//
//		assertSame(ids, actual);
//
//		ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
//		verify(this.publicationService, only()).importPublications(arg.capture());
//		assertNull(arg.getValue());
//	}
//
//
//	@Test
//	public void importPublications_emptyBibTeX() throws Exception {
//		HttpServletResponse response = mock(HttpServletResponse.class);
//		List<Integer> ids = mock(List.class);
//		when(this.publicationService.importPublications(any())).thenReturn(ids);
//
//		List<Integer> actual = this.test.importPublications(response, "");
//
//		assertSame(ids, actual);
//
//		ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
//		verify(this.publicationService, only()).importPublications(arg.capture());
//		assertEquals("", arg.getValue());
//	}
//
//	@Test
//	public void importPublications() throws Exception {
//		HttpServletResponse response = mock(HttpServletResponse.class);
//		List<Integer> ids = mock(List.class);
//		when(this.publicationService.importPublications(any())).thenReturn(ids);
//
//		List<Integer> actual = this.test.importPublications(response, "abc");
//
//		assertSame(ids, actual);
//
//		ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
//		verify(this.publicationService, only()).importPublications(arg.capture());
//		assertEquals("abc", arg.getValue());
//	}
//
//	@Test
//	public void importBibTeX() throws Exception {
//		HttpServletResponse response = mock(HttpServletResponse.class);
//		MultipartFile file = mock(MultipartFile.class);
//		when(this.fileManager.readTextFile(any())).thenReturn("abc");
//		List<Integer> ids = mock(List.class);
//		when(this.publicationService.importPublications(any())).thenReturn(ids);
//		when(ids.size()).thenReturn(2);
//
//		this.test.importBibTeX(response, file);
//
//		ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
//		verify(this.publicationService, only()).importPublications(arg.capture());
//		assertEquals("abc", arg.getValue());
//
//		verify(response).sendRedirect("/SpringRestHibernate/addPublication?success=1&importedPubs=2");
//	}
//
//	@Test
//	public void bibTeXToAddPublication() throws Exception {
//		HttpServletResponse response = mock(HttpServletResponse.class);
//		MultipartFile file = mock(MultipartFile.class);
//		when(this.fileManager.readTextFile(any())).thenReturn("abc");
//		RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
//		RedirectView expectedView = mock(RedirectView.class);
//		when(this.viewFactory.newRedirectView(any(), anyBoolean())).thenReturn(expectedView);
//
//		RedirectView view = this.test.bibTeXToAddPublication(response, file, redirectAttributes);
//
//		assertSame(expectedView, view);
//
//		verify(redirectAttributes, atLeastOnce()).addFlashAttribute(eq("bibtex"), eq("abc"));
//		verify(redirectAttributes, atLeastOnce()).addFlashAttribute(eq("publicationService"), same(this.publicationService));
//	}
//
//	@Test
//	public void exportBibTeX_null() {
//		assertNull(this.test.exportBibTeX(null));
//	}
//
//	@Test
//	public void exportBibTeX() {
//		when(this.publicationService.exportBibTeX(any(Stream.class))).thenReturn("abc");
//		Integer[] identifiers = new Integer[] {123, 345};
//
//		String bibtex = this.test.exportBibTeX(identifiers);
//
//		assertEquals("abc", bibtex);
//
//		ArgumentCaptor<Stream<Integer>> arg = ArgumentCaptor.forClass(Stream.class);
//		verify(this.publicationService, only()).exportBibTeX(arg.capture());
//		Stream<Integer> stream = arg.getValue();
//		assertNotNull(stream);
//		Object[] content = stream.toArray();
//		assertEquals(2, content.length);
//		assertEquals(123, content[0]);
//		assertEquals(345, content[1]);
//	}
//
//	@Test
//	public void exportHtml_null() throws Exception {
//		HttpServletResponse response = mock(HttpServletResponse.class);
//		assertNull(this.test.exportHtml(response, null, null, null));
//	}
//
//	@Test
//	public void exportHtml() throws Exception {
//		HttpServletResponse response = mock(HttpServletResponse.class);
//		when(this.publicationService.exportHtml(any(Stream.class), any())).thenReturn("abc");
//		Integer[] identifiers = new Integer[] {123, 345};
//
//		String html = this.test.exportHtml(response, identifiers, null, null);
//
//		assertEquals("abc", html);
//
//		ArgumentCaptor<Stream<Integer>> arg0 = ArgumentCaptor.forClass(Stream.class);
//		ArgumentCaptor<ExporterConfigurator> arg1 = ArgumentCaptor.forClass(ExporterConfigurator.class);
//		verify(this.publicationService, only()).exportHtml(arg0.capture(), arg1.capture());
//		Stream<Integer> stream = arg0.getValue();
//		assertNotNull(stream);
//		Object[] content = stream.toArray();
//		assertEquals(2, content.length);
//		assertEquals(123, content[0]);
//		assertEquals(345, content[1]);
//		assertNotNull(arg1.getValue());
//	}
//
//	@Test
//	public void exportOdt_null() throws Exception {
//		HttpServletResponse response = mock(HttpServletResponse.class);
//		assertNull(this.test.exportOdt(response, null, null, null));
//	}
//
//	@Test
//	public void exportOdt() throws Exception {
//		HttpServletResponse response = mock(HttpServletResponse.class);
//		when(this.publicationService.exportOdt(any(Stream.class), any())).thenReturn("abc".getBytes());
//		Integer[] identifiers = new Integer[] {123, 345};
//
//		byte[] odt = this.test.exportOdt(response, identifiers, null, null);
//
//		assertEquals("abc", new String(odt));
//
//		ArgumentCaptor<Stream<Integer>> arg0 = ArgumentCaptor.forClass(Stream.class);
//		ArgumentCaptor<ExporterConfigurator> arg1 = ArgumentCaptor.forClass(ExporterConfigurator.class);
//		verify(this.publicationService, only()).exportOdt(arg0.capture(), arg1.capture());
//		Stream<Integer> stream = arg0.getValue();
//		assertNotNull(stream);
//		Object[] content = stream.toArray();
//		assertEquals(2, content.length);
//		assertEquals(123, content[0]);
//		assertEquals(345, content[1]);
//		assertNotNull(arg1.getValue());
//	}
//
//	@Test
//	public void showPublicationsStats_null_noPub() {
//		when(this.publicationService.getAllPublications()).thenReturn(Collections.emptyList());
//
//		ModelAndView mv = this.test.showPublicationsStats(null);
//
//		assertNotNull(mv);
//
//		PublicationsStat global = (PublicationsStat) mv.getModel().get("globalStats");
//		assertNotNull(global);
//		assertEquals(0, global.getTotal());
//
//		Map<Integer, PublicationsStat> stats = (Map<Integer, PublicationsStat>) mv.getModel().get("stats");
//		assertNotNull(stats);
//		assertEquals(0, stats.size());
//	}
//
//	@Test
//	public void showPublicationsStats_null_pub() {
//		Publication p0 = mock(Publication.class);
//		when(p0.getPublicationYear()).thenReturn(2022);
//		when(p0.getType()).thenReturn(PublicationType.INTERNATIONAL_JOURNAL_PAPER);
//		when(p0.isRanked()).thenReturn(true);
//
//		Publication p1 = mock(Publication.class);
//		when(p1.getPublicationYear()).thenReturn(2022);
//		when(p1.getType()).thenReturn(PublicationType.INTERNATIONAL_CONFERENCE_PAPER);
//		when(p1.isRanked()).thenReturn(false);
//
//		Publication p2 = mock(Publication.class);
//		when(p2.getPublicationYear()).thenReturn(2019);
//		when(p2.getType()).thenReturn(PublicationType.NATIONAL_BOOK);
//		when(p2.isRanked()).thenReturn(false);
//
//		Publication p3 = mock(Publication.class);
//		when(p3.getPublicationYear()).thenReturn(2020);
//		when(p3.getType()).thenReturn(PublicationType.INTERNATIONAL_CONFERENCE_PAPER);
//		when(p3.isRanked()).thenReturn(false);
//
//		when(this.publicationService.getAllPublications()).thenReturn(Arrays.asList(p0, p1, p2, p3));
//
//		ModelAndView mv = this.test.showPublicationsStats(null);
//
//		assertNotNull(mv);
//
//		PublicationsStat global = (PublicationsStat) mv.getModel().get("globalStats");
//		assertNotNull(global);
//		assertEquals(4, global.getTotal());
//
//		Map<Integer, PublicationsStat> stats = (Map<Integer, PublicationsStat>) mv.getModel().get("stats");
//		assertNotNull(stats);
//		assertEquals(3, stats.size());
//
//		PublicationsStat stats2019 = (PublicationsStat) stats.get(2019);
//		assertEquals(1, stats2019.getTotal());
//		assertEquals(1, stats2019.getCountForType(PublicationType.NATIONAL_BOOK));
//		assertEquals(1, stats2019.getCountForCategory(PublicationCategory.OS));
//
//		PublicationsStat stats2020 = (PublicationsStat) stats.get(2020);
//		assertEquals(1, stats2020.getTotal());
//		assertEquals(1, stats2020.getCountForType(PublicationType.INTERNATIONAL_CONFERENCE_PAPER));
//		assertEquals(1, stats2020.getCountForCategory(PublicationCategory.C_ACTI));
//
//		PublicationsStat stats2022 = (PublicationsStat) stats.get(2022);
//		assertEquals(2, stats2022.getTotal());
//		assertEquals(1, stats2022.getCountForType(PublicationType.INTERNATIONAL_JOURNAL_PAPER));
//		assertEquals(1, stats2022.getCountForType(PublicationType.INTERNATIONAL_CONFERENCE_PAPER));
//		assertEquals(1, stats2022.getCountForCategory(PublicationCategory.ACL));
//		assertEquals(1, stats2022.getCountForCategory(PublicationCategory.C_ACTI));
//	}
//
//	@Test
//	public void showPublicationsStats_id_noPub() {
//		List<Publication> pubs0 = mock(List.class);
//		when(this.authorshipService.getPublicationsFor(anyInt())).thenReturn(Collections.emptyList());
//
//		ModelAndView mv = this.test.showPublicationsStats(123);
//
//		assertNotNull(mv);
//
//		PublicationsStat global = (PublicationsStat) mv.getModel().get("globalStats");
//		assertNotNull(global);
//		assertEquals(0, global.getTotal());
//
//		Map<Integer, PublicationsStat> stats = (Map<Integer, PublicationsStat>) mv.getModel().get("stats");
//		assertNotNull(stats);
//		assertEquals(0, stats.size());
//	}
//
//	@Test
//	public void showPublicationsStats_id_pub() {
//		Publication p0 = mock(Publication.class);
//		when(p0.getPublicationYear()).thenReturn(2022);
//		when(p0.getType()).thenReturn(PublicationType.INTERNATIONAL_JOURNAL_PAPER);
//		when(p0.isRanked()).thenReturn(true);
//
//		Publication p1 = mock(Publication.class);
//		when(p1.getPublicationYear()).thenReturn(2022);
//		when(p1.getType()).thenReturn(PublicationType.INTERNATIONAL_CONFERENCE_PAPER);
//		when(p1.isRanked()).thenReturn(false);
//
//		Publication p2 = mock(Publication.class);
//		when(p2.getPublicationYear()).thenReturn(2019);
//		when(p2.getType()).thenReturn(PublicationType.NATIONAL_BOOK);
//		when(p2.isRanked()).thenReturn(false);
//
//		Publication p3 = mock(Publication.class);
//		when(p3.getPublicationYear()).thenReturn(2020);
//		when(p3.getType()).thenReturn(PublicationType.INTERNATIONAL_CONFERENCE_PAPER);
//		when(p3.isRanked()).thenReturn(false);
//
//		when(this.authorshipService.getPublicationsFor(anyInt())).thenReturn(Arrays.asList(p0, p1, p2, p3));
//
//		ModelAndView mv = this.test.showPublicationsStats(123);
//
//		assertNotNull(mv);
//
//		PublicationsStat global = (PublicationsStat) mv.getModel().get("globalStats");
//		assertNotNull(global);
//		assertEquals(4, global.getTotal());
//
//		Map<Integer, PublicationsStat> stats = (Map<Integer, PublicationsStat>) mv.getModel().get("stats");
//		assertNotNull(stats);
//		assertEquals(3, stats.size());
//
//		PublicationsStat stats2019 = (PublicationsStat) stats.get(2019);
//		assertEquals(1, stats2019.getTotal());
//		assertEquals(1, stats2019.getCountForType(PublicationType.NATIONAL_BOOK));
//		assertEquals(1, stats2019.getCountForCategory(PublicationCategory.OS));
//
//		PublicationsStat stats2020 = (PublicationsStat) stats.get(2020);
//		assertEquals(1, stats2020.getTotal());
//		assertEquals(1, stats2020.getCountForType(PublicationType.INTERNATIONAL_CONFERENCE_PAPER));
//		assertEquals(1, stats2020.getCountForCategory(PublicationCategory.C_ACTI));
//
//		PublicationsStat stats2022 = (PublicationsStat) stats.get(2022);
//		assertEquals(2, stats2022.getTotal());
//		assertEquals(1, stats2022.getCountForType(PublicationType.INTERNATIONAL_JOURNAL_PAPER));
//		assertEquals(1, stats2022.getCountForType(PublicationType.INTERNATIONAL_CONFERENCE_PAPER));
//		assertEquals(1, stats2022.getCountForCategory(PublicationCategory.ACL));
//		assertEquals(1, stats2022.getCountForCategory(PublicationCategory.C_ACTI));
//	}

}
