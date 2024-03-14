/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
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

package fr.utbm.ciad.labmanager.tests.services.publication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.time.LocalDate;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.data.publication.PublicationLanguage;
import fr.utbm.ciad.labmanager.data.publication.PublicationType;
import fr.utbm.ciad.labmanager.services.publication.AbstractPublicationTypeService;
import fr.utbm.ciad.labmanager.utils.doi.DefaultDoiTools;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.utils.io.hal.DefaultHalTools;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link AbstractPublicationTypeService}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class AbstractPublicationTypeServiceTest {

	private Publication pub0;

	private Publication pub1;

	private Publication pub2;

	private DownloadableFileManager downloadableFileManager;

	private MessageSourceAccessor messages;

	private PublicationTypeServiceMock test;

	@BeforeEach
	public void setUp() {
		this.messages = mock(MessageSourceAccessor.class);
		this.downloadableFileManager = mock(DownloadableFileManager.class);
		this.test = new PublicationTypeServiceMock();
	}

	@Test
	public void updatePublicationNoSave_0() throws Exception {
		Publication pub = mock(Publication.class);

		this.test.updatePublicationNoSave(pub, "title0", PublicationType.ARTISTIC_PRODUCTION,
				LocalDate.parse("2022-07-23"), 2022, "abs0", "kw0", "doi:doi/0", "hal-123", "isbn0", "issn0", "http://dblp.org",
				"http://extra.org", PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0",
				"http://video.org");

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, atLeastOnce()).setPublicationDate(LocalDate.parse("2022-07-23"));
		verify(pub, atLeastOnce()).setPublicationYear(2022);
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setHalId("hal-123");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF("pdfContent0");
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("awardContent0");
	}

	@Test
	public void updatePublicationNoSave_0_notitle() throws Exception {
		Publication pub = mock(Publication.class);

		this.test.updatePublicationNoSave(pub, null, PublicationType.ARTISTIC_PRODUCTION,
				LocalDate.parse("2022-07-23"), 2022, "abs0", "kw0", "doi:doi/0", "hal-123", "isbn0", "issn0", "http://dblp.org",
				"http://extra.org", PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0",
				"http://video.org");

		verify(pub, never()).setTitle(anyString());
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, atLeastOnce()).setPublicationDate(LocalDate.parse("2022-07-23"));
		verify(pub, atLeastOnce()).setPublicationYear(2022);
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setHalId("hal-123");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF("pdfContent0");
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("awardContent0");
	}

	@Test
	public void updatePublicationNoSave_0_notype() throws Exception {
		Publication pub = mock(Publication.class);

		this.test.updatePublicationNoSave(pub, "title0", null,
				LocalDate.parse("2022-07-23"), 2022, "abs0", "kw0", "doi:doi/0", "hal-123", "isbn0", "issn0", "http://dblp.org",
				"http://extra.org", PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0",
				"http://video.org");

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, never()).setType(any(PublicationType.class));
		verify(pub, atLeastOnce()).setPublicationDate(LocalDate.parse("2022-07-23"));
		verify(pub, atLeastOnce()).setPublicationYear(2022);
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setHalId("hal-123");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF("pdfContent0");
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("awardContent0");
	}

	@Test
	public void updatePublicationNoSave_0_nodate() throws Exception {
		Publication pub = mock(Publication.class);

		this.test.updatePublicationNoSave(pub, "title0", PublicationType.ARTISTIC_PRODUCTION,
				null, 2000, "abs0", "kw0", "doi:doi/0", "hal-123", "isbn0", "issn0", "http://dblp.org",
				"http://extra.org", PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0",
				"http://video.org");

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, never()).setPublicationDate(any(LocalDate.class));
		verify(pub, atLeastOnce()).setPublicationYear(2000);
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setHalId("hal-123");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF("pdfContent0");
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("awardContent0");
	}

	@Test
	public void updatePublicationNoSave_0_nopdfcontent() throws Exception {
		Publication pub = mock(Publication.class);
		when(pub.getId()).thenReturn(123l);

		this.test.updatePublicationNoSave(pub, "title0", PublicationType.ARTISTIC_PRODUCTION,
				LocalDate.parse("2022-07-23"), 2022, "abs0", "kw0", "doi:doi/0", "hal-123", "isbn0", "issn0", "http://dblp.org",
				"http://extra.org", PublicationLanguage.ITALIAN, null, "awardContent0",
				"http://video.org");

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, atLeastOnce()).setPublicationDate(LocalDate.parse("2022-07-23"));
		verify(pub, atLeastOnce()).setPublicationYear(2022);
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setHalId("hal-123");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF(null);
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("awardContent0");

		verify(this.downloadableFileManager, atLeastOnce()).deleteDownloadablePublicationPdfFile(123);
	}

	@Test
	public void updatePublicationNoSave_0_noaward() throws Exception {
		Publication pub = mock(Publication.class);
		when(pub.getId()).thenReturn(123l);

		this.test.updatePublicationNoSave(pub, "title0", PublicationType.ARTISTIC_PRODUCTION,
				LocalDate.parse("2022-07-23"), 2022, "abs0", "kw0", "doi:doi/0", "hal-123", "isbn0", "issn0", "http://dblp.org",
				"http://extra.org", PublicationLanguage.ITALIAN, "pdfContent0", null,
				"http://video.org");

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, atLeastOnce()).setPublicationDate(LocalDate.parse("2022-07-23"));
		verify(pub, atLeastOnce()).setPublicationYear(2022);
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setHalId("hal-123");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF("pdfContent0");
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate(null);

		verify(this.downloadableFileManager, atLeastOnce()).deleteDownloadableAwardPdfFile(123);
	}

	@Test
	public void updatePublicationNoSave_1() throws Exception {
		Publication pub = mock(Publication.class);

		this.test.updatePublicationNoSave(pub, "title0", PublicationType.ARTISTIC_PRODUCTION,
				LocalDate.parse("2022-07-23"), 2022,"abs0", "kw0", "doi:doi/0", "hal-123", "isbn0", "issn0", new URL("http://dblp.org"),
				new URL("http://extra.org"), PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0",
				new URL("http://video.org"));

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, atLeastOnce()).setPublicationDate(LocalDate.parse("2022-07-23"));
		verify(pub, atLeastOnce()).setPublicationYear(2022);
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setHalId("hal-123");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF("pdfContent0");
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("awardContent0");
	}

	@Test
	public void updatePublicationNoSave_1_notitle() throws Exception {
		Publication pub = mock(Publication.class);

		this.test.updatePublicationNoSave(pub, null, PublicationType.ARTISTIC_PRODUCTION,
				LocalDate.parse("2022-07-23"), 2022, "abs0", "kw0", "doi:doi/0", "hal-123", "isbn0", "issn0", new URL("http://dblp.org"),
				new URL("http://extra.org"), PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0",
				new URL("http://video.org"));

		verify(pub, never()).setTitle(anyString());
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, atLeastOnce()).setPublicationDate(LocalDate.parse("2022-07-23"));
		verify(pub, atLeastOnce()).setPublicationYear(2022);
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setHalId("hal-123");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF("pdfContent0");
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("awardContent0");
	}

	@Test
	public void updatePublicationNoSave_1_notype() throws Exception {
		Publication pub = mock(Publication.class);

		this.test.updatePublicationNoSave(pub, "title0", null,
				LocalDate.parse("2022-07-23"), 2022, "abs0", "kw0", "doi:doi/0", "hal-123", "isbn0", "issn0", new URL("http://dblp.org"),
				new URL("http://extra.org"), PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0",
				new URL("http://video.org"));

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, never()).setType(any(PublicationType.class));
		verify(pub, atLeastOnce()).setPublicationDate(LocalDate.parse("2022-07-23"));
		verify(pub, atLeastOnce()).setPublicationYear(2022);
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setHalId("hal-123");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");

		verify(pub, atLeastOnce()).setPathToDownloadablePDF("pdfContent0");

		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("awardContent0");
	}

	@Test
	public void updatePublicationNoSave_1_nodate() throws Exception {
		Publication pub = mock(Publication.class);

		this.test.updatePublicationNoSave(pub, "title0", PublicationType.ARTISTIC_PRODUCTION,
				null, 2001, "abs0", "kw0", "doi:doi/0", "hal-123", "isbn0", "issn0", new URL("http://dblp.org"),
				new URL("http://extra.org"), PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0",
				new URL("http://video.org"));

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, never()).setPublicationDate(any(LocalDate.class));
		verify(pub, atLeastOnce()).setPublicationYear(2001);
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF("pdfContent0");
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("awardContent0");
	}

	@Test
	public void updatePublicationNoSave_1_nopdfcontent() throws Exception {
		Publication pub = mock(Publication.class);
		when(pub.getId()).thenReturn(123l);

		this.test.updatePublicationNoSave(pub, "title0", PublicationType.ARTISTIC_PRODUCTION,
				LocalDate.parse("2022-07-23"), 2022, "abs0", "kw0", "doi:doi/0", "hal-123", "isbn0", "issn0", new URL("http://dblp.org"),
				new URL("http://extra.org"), PublicationLanguage.ITALIAN, null, "awardContent0",
				new URL("http://video.org"));

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, atLeastOnce()).setPublicationDate(LocalDate.parse("2022-07-23"));
		verify(pub, atLeastOnce()).setPublicationYear(2022);
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF(null);
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("awardContent0");

		verify(this.downloadableFileManager, atLeastOnce()).deleteDownloadablePublicationPdfFile(123);
	}

	@Test
	public void updatePublicationNoSave_1_noaward() throws Exception {
		Publication pub = mock(Publication.class);
		when(pub.getId()).thenReturn(123l);

		this.test.updatePublicationNoSave(pub, "title0", PublicationType.ARTISTIC_PRODUCTION,
				LocalDate.parse("2022-07-23"), 2022, "abs0", "kw0", "doi:doi/0", "hal-123", "isbn0", "issn0", new URL("http://dblp.org"),
				new URL("http://extra.org"), PublicationLanguage.ITALIAN, "pdfContent0", null,
				new URL("http://video.org"));

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, atLeastOnce()).setPublicationDate(LocalDate.parse("2022-07-23"));
		verify(pub, atLeastOnce()).setPublicationYear(2022);
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF("pdfContent0");
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate(null);

		verify(this.downloadableFileManager, atLeastOnce()).deleteDownloadableAwardPdfFile(123);
	}

	/** Mock for {@link AbstractPublicationTypeService}.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public class PublicationTypeServiceMock extends AbstractPublicationTypeService {

		public PublicationTypeServiceMock() {
			super(AbstractPublicationTypeServiceTest.this.messages,
					new Constants(),
					mock(SessionFactory.class),
					AbstractPublicationTypeServiceTest.this.downloadableFileManager,
					new DefaultDoiTools(),
					new DefaultHalTools());
		}

		@Override
		protected boolean isValidPublicationType(PublicationType type, Publication publication) {
			return true;
		}

		@Override
		public void updatePublicationNoSave(Publication publication, String title, PublicationType type,
				LocalDate date, int year, String abstractText, String keywords, String doi, String halId, String isbn, String issn,
				String dblpUrl, String extraUrl, PublicationLanguage language, String pdfContent,
				String awardContent, String pathToVideo) {
			super.updatePublicationNoSave(publication, title, type, date, year, abstractText, keywords, doi, halId, isbn, issn, dblpUrl,
					extraUrl, language, pdfContent, awardContent, pathToVideo);
		}
		@Override
		public void updatePublicationNoSave(Publication publication, String title, PublicationType type,
				LocalDate date, int year, String abstractText, String keywords, String doi, String halId, String isbn, String issn, URL dblpUrl,
				URL extraUrl, PublicationLanguage language, String pdfContent, String awardContent,
				URL pathToVideo) {
			super.updatePublicationNoSave(publication, title, type, date, year, abstractText, keywords, doi, halId, isbn, issn, dblpUrl,
					extraUrl, language, pdfContent, awardContent, pathToVideo);
		}
	}

}
