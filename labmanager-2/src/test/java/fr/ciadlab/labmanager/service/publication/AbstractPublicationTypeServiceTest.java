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
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.service.publication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.sql.Date;

import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.utils.files.DownloadableFileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

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

	private AbstractPublicationTypeService test;

	@BeforeEach
	public void setUp() {
		this.downloadableFileManager = mock(DownloadableFileManager.class);
		this.test = new AbstractPublicationTypeService(this.downloadableFileManager) {
			@Override
			public void updatePublicationNoSave(Publication publication, String title, PublicationType type,
					Date date, String abstractText, String keywords, String doi, String isbn, String issn,
					String dblpUrl, String extraUrl, PublicationLanguage language, String pdfContent,
					String awardContent, String pathToVideo) {
				super.updatePublicationNoSave(publication, title, type, date, abstractText, keywords, doi, isbn, issn, dblpUrl,
						extraUrl, language, pdfContent, awardContent, pathToVideo);
			}
			@Override
			public void updatePublicationNoSave(Publication publication, String title, PublicationType type,
					Date date, String abstractText, String keywords, String doi, String isbn, String issn, URL dblpUrl,
					URL extraUrl, PublicationLanguage language, String pdfContent, String awardContent,
					URL pathToVideo) {
				super.updatePublicationNoSave(publication, title, type, date, abstractText, keywords, doi, isbn, issn, dblpUrl,
						extraUrl, language, pdfContent, awardContent, pathToVideo);
			}
		};
	}

	@Test
	public void updatePublicationNoSave_0() throws Exception {
		when(this.downloadableFileManager.saveDownloadablePublicationPdfFile(anyInt(), anyString())).thenReturn("path/to/pdf");
		when(this.downloadableFileManager.saveDownloadableAwardPdfFile(anyInt(), anyString())).thenReturn("path/to/award");

		Publication pub = mock(Publication.class);
		when(pub.getId()).thenReturn(123);

		this.test.updatePublicationNoSave(pub, "title0", PublicationType.ARTISTIC_PRODUCTION,
				Date.valueOf("2022-07-23"), "abs0", "kw0", "doi/0", "isbn0", "issn0", "http://dblp.org",
				"http://extra.org", PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0",
				"http://video.org");

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, atLeastOnce()).setPublicationDate(Date.valueOf("2022-07-23"));
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadablePublicationPdfFile(123, "pdfContent0");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF("path/to/pdf");

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadableAwardPdfFile(123, "awardContent0");
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("path/to/award");
	}

	@Test
	public void updatePublicationNoSave_0_notitle() throws Exception {
		when(this.downloadableFileManager.saveDownloadablePublicationPdfFile(anyInt(), anyString())).thenReturn("path/to/pdf");
		when(this.downloadableFileManager.saveDownloadableAwardPdfFile(anyInt(), anyString())).thenReturn("path/to/award");

		Publication pub = mock(Publication.class);
		when(pub.getId()).thenReturn(123);

		this.test.updatePublicationNoSave(pub, null, PublicationType.ARTISTIC_PRODUCTION,
				Date.valueOf("2022-07-23"), "abs0", "kw0", "doi/0", "isbn0", "issn0", "http://dblp.org",
				"http://extra.org", PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0",
				"http://video.org");

		verify(pub, never()).setTitle(anyString());
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, atLeastOnce()).setPublicationDate(Date.valueOf("2022-07-23"));
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadablePublicationPdfFile(123, "pdfContent0");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF("path/to/pdf");

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadableAwardPdfFile(123, "awardContent0");
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("path/to/award");
	}

	@Test
	public void updatePublicationNoSave_0_notype() throws Exception {
		when(this.downloadableFileManager.saveDownloadablePublicationPdfFile(anyInt(), anyString())).thenReturn("path/to/pdf");
		when(this.downloadableFileManager.saveDownloadableAwardPdfFile(anyInt(), anyString())).thenReturn("path/to/award");

		Publication pub = mock(Publication.class);
		when(pub.getId()).thenReturn(123);

		this.test.updatePublicationNoSave(pub, "title0", null,
				Date.valueOf("2022-07-23"), "abs0", "kw0", "doi/0", "isbn0", "issn0", "http://dblp.org",
				"http://extra.org", PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0",
				"http://video.org");

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, never()).setType(any());
		verify(pub, atLeastOnce()).setPublicationDate(Date.valueOf("2022-07-23"));
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadablePublicationPdfFile(123, "pdfContent0");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF("path/to/pdf");

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadableAwardPdfFile(123, "awardContent0");
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("path/to/award");
	}

	@Test
	public void updatePublicationNoSave_0_nodate() throws Exception {
		when(this.downloadableFileManager.saveDownloadablePublicationPdfFile(anyInt(), anyString())).thenReturn("path/to/pdf");
		when(this.downloadableFileManager.saveDownloadableAwardPdfFile(anyInt(), anyString())).thenReturn("path/to/award");

		Publication pub = mock(Publication.class);
		when(pub.getId()).thenReturn(123);

		this.test.updatePublicationNoSave(pub, "title0", PublicationType.ARTISTIC_PRODUCTION,
				null, "abs0", "kw0", "doi/0", "isbn0", "issn0", "http://dblp.org",
				"http://extra.org", PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0",
				"http://video.org");

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, never()).setPublicationDate(any());
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadablePublicationPdfFile(123, "pdfContent0");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF("path/to/pdf");

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadableAwardPdfFile(123, "awardContent0");
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("path/to/award");
	}

	@Test
	public void updatePublicationNoSave_0_nopdfcontent() throws Exception {
		when(this.downloadableFileManager.saveDownloadableAwardPdfFile(anyInt(), anyString())).thenReturn("path/to/award");

		Publication pub = mock(Publication.class);
		when(pub.getId()).thenReturn(123);

		this.test.updatePublicationNoSave(pub, "title0", PublicationType.ARTISTIC_PRODUCTION,
				Date.valueOf("2022-07-23"), "abs0", "kw0", "doi/0", "isbn0", "issn0", "http://dblp.org",
				"http://extra.org", PublicationLanguage.ITALIAN, null, "awardContent0",
				"http://video.org");

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, atLeastOnce()).setPublicationDate(Date.valueOf("2022-07-23"));
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");

		verify(this.downloadableFileManager, never()).saveDownloadablePublicationPdfFile(anyInt(), anyString());
		verify(this.downloadableFileManager, atLeastOnce()).deleteDownloadablePublicationPdfFile(123);
		verify(pub, atLeastOnce()).setPathToDownloadablePDF(null);

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadableAwardPdfFile(123, "awardContent0");
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("path/to/award");
	}

	@Test
	public void updatePublicationNoSave_0_noaward() throws Exception {
		when(this.downloadableFileManager.saveDownloadablePublicationPdfFile(anyInt(), anyString())).thenReturn("path/to/pdf");

		Publication pub = mock(Publication.class);
		when(pub.getId()).thenReturn(123);

		this.test.updatePublicationNoSave(pub, "title0", PublicationType.ARTISTIC_PRODUCTION,
				Date.valueOf("2022-07-23"), "abs0", "kw0", "doi/0", "isbn0", "issn0", "http://dblp.org",
				"http://extra.org", PublicationLanguage.ITALIAN, "pdfContent0", null,
				"http://video.org");

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, atLeastOnce()).setPublicationDate(Date.valueOf("2022-07-23"));
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadablePublicationPdfFile(123, "pdfContent0");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF("path/to/pdf");

		verify(this.downloadableFileManager, never()).saveDownloadableAwardPdfFile(anyInt(), anyString());
		verify(this.downloadableFileManager, atLeastOnce()).deleteDownloadableAwardPdfFile(123);
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate(null);
	}

	@Test
	public void updatePublicationNoSave_1() throws Exception {
		when(this.downloadableFileManager.saveDownloadablePublicationPdfFile(anyInt(), anyString())).thenReturn("path/to/pdf");
		when(this.downloadableFileManager.saveDownloadableAwardPdfFile(anyInt(), anyString())).thenReturn("path/to/award");

		Publication pub = mock(Publication.class);
		when(pub.getId()).thenReturn(123);

		this.test.updatePublicationNoSave(pub, "title0", PublicationType.ARTISTIC_PRODUCTION,
				Date.valueOf("2022-07-23"), "abs0", "kw0", "doi/0", "isbn0", "issn0", new URL("http://dblp.org"),
				new URL("http://extra.org"), PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0",
				new URL("http://video.org"));

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, atLeastOnce()).setPublicationDate(Date.valueOf("2022-07-23"));
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadablePublicationPdfFile(123, "pdfContent0");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF("path/to/pdf");

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadableAwardPdfFile(123, "awardContent0");
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("path/to/award");
	}

	@Test
	public void updatePublicationNoSave_1_notitle() throws Exception {
		when(this.downloadableFileManager.saveDownloadablePublicationPdfFile(anyInt(), anyString())).thenReturn("path/to/pdf");
		when(this.downloadableFileManager.saveDownloadableAwardPdfFile(anyInt(), anyString())).thenReturn("path/to/award");

		Publication pub = mock(Publication.class);
		when(pub.getId()).thenReturn(123);

		this.test.updatePublicationNoSave(pub, null, PublicationType.ARTISTIC_PRODUCTION,
				Date.valueOf("2022-07-23"), "abs0", "kw0", "doi/0", "isbn0", "issn0", new URL("http://dblp.org"),
				new URL("http://extra.org"), PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0",
				new URL("http://video.org"));

		verify(pub, never()).setTitle(anyString());
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, atLeastOnce()).setPublicationDate(Date.valueOf("2022-07-23"));
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadablePublicationPdfFile(123, "pdfContent0");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF("path/to/pdf");

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadableAwardPdfFile(123, "awardContent0");
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("path/to/award");
	}

	@Test
	public void updatePublicationNoSave_1_notype() throws Exception {
		when(this.downloadableFileManager.saveDownloadablePublicationPdfFile(anyInt(), anyString())).thenReturn("path/to/pdf");
		when(this.downloadableFileManager.saveDownloadableAwardPdfFile(anyInt(), anyString())).thenReturn("path/to/award");

		Publication pub = mock(Publication.class);
		when(pub.getId()).thenReturn(123);

		this.test.updatePublicationNoSave(pub, "title0", null,
				Date.valueOf("2022-07-23"), "abs0", "kw0", "doi/0", "isbn0", "issn0", new URL("http://dblp.org"),
				new URL("http://extra.org"), PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0",
				new URL("http://video.org"));

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, never()).setType(any());
		verify(pub, atLeastOnce()).setPublicationDate(Date.valueOf("2022-07-23"));
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadablePublicationPdfFile(123, "pdfContent0");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF("path/to/pdf");

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadableAwardPdfFile(123, "awardContent0");
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("path/to/award");
	}

	@Test
	public void updatePublicationNoSave_1_nodate() throws Exception {
		when(this.downloadableFileManager.saveDownloadablePublicationPdfFile(anyInt(), anyString())).thenReturn("path/to/pdf");
		when(this.downloadableFileManager.saveDownloadableAwardPdfFile(anyInt(), anyString())).thenReturn("path/to/award");

		Publication pub = mock(Publication.class);
		when(pub.getId()).thenReturn(123);

		this.test.updatePublicationNoSave(pub, "title0", PublicationType.ARTISTIC_PRODUCTION,
				null, "abs0", "kw0", "doi/0", "isbn0", "issn0", new URL("http://dblp.org"),
				new URL("http://extra.org"), PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0",
				new URL("http://video.org"));

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, never()).setPublicationDate(any());
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadablePublicationPdfFile(123, "pdfContent0");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF("path/to/pdf");

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadableAwardPdfFile(123, "awardContent0");
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("path/to/award");
	}

	@Test
	public void updatePublicationNoSave_1_nopdfcontent() throws Exception {
		when(this.downloadableFileManager.saveDownloadableAwardPdfFile(anyInt(), anyString())).thenReturn("path/to/award");

		Publication pub = mock(Publication.class);
		when(pub.getId()).thenReturn(123);

		this.test.updatePublicationNoSave(pub, "title0", PublicationType.ARTISTIC_PRODUCTION,
				Date.valueOf("2022-07-23"), "abs0", "kw0", "doi/0", "isbn0", "issn0", new URL("http://dblp.org"),
				new URL("http://extra.org"), PublicationLanguage.ITALIAN, null, "awardContent0",
				new URL("http://video.org"));

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, atLeastOnce()).setPublicationDate(Date.valueOf("2022-07-23"));
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");

		verify(this.downloadableFileManager, never()).saveDownloadablePublicationPdfFile(anyInt(), anyString());
		verify(this.downloadableFileManager, atLeastOnce()).deleteDownloadablePublicationPdfFile(123);
		verify(pub, atLeastOnce()).setPathToDownloadablePDF(null);

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadableAwardPdfFile(123, "awardContent0");
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate("path/to/award");
	}

	@Test
	public void updatePublicationNoSave_1_noaward() throws Exception {
		when(this.downloadableFileManager.saveDownloadablePublicationPdfFile(anyInt(), anyString())).thenReturn("path/to/pdf");

		Publication pub = mock(Publication.class);
		when(pub.getId()).thenReturn(123);

		this.test.updatePublicationNoSave(pub, "title0", PublicationType.ARTISTIC_PRODUCTION,
				Date.valueOf("2022-07-23"), "abs0", "kw0", "doi/0", "isbn0", "issn0", new URL("http://dblp.org"),
				new URL("http://extra.org"), PublicationLanguage.ITALIAN, "pdfContent0", null,
				new URL("http://video.org"));

		verify(pub, atLeastOnce()).setTitle("title0");
		verify(pub, atLeastOnce()).setType(PublicationType.ARTISTIC_PRODUCTION);
		verify(pub, atLeastOnce()).setPublicationDate(Date.valueOf("2022-07-23"));
		verify(pub, atLeastOnce()).setAbstractText("abs0");
		verify(pub, atLeastOnce()).setKeywords("kw0");
		verify(pub, atLeastOnce()).setDOI("doi/0");
		verify(pub, atLeastOnce()).setISBN("isbn0");
		verify(pub, atLeastOnce()).setISSN("issn0");
		verify(pub, atLeastOnce()).setDblpURL("http://dblp.org");
		verify(pub, atLeastOnce()).setExtraURL("http://extra.org");
		verify(pub, atLeastOnce()).setMajorLanguage(PublicationLanguage.ITALIAN);
		verify(pub, atLeastOnce()).setVideoURL("http://video.org");

		verify(this.downloadableFileManager, atLeastOnce()).saveDownloadablePublicationPdfFile(123, "pdfContent0");
		verify(pub, atLeastOnce()).setPathToDownloadablePDF("path/to/pdf");

		verify(this.downloadableFileManager, never()).saveDownloadableAwardPdfFile(anyInt(), anyString());
		verify(this.downloadableFileManager, atLeastOnce()).deleteDownloadableAwardPdfFile(123);
		verify(pub, atLeastOnce()).setPathToDownloadableAwardCertificate(null);
	}

}
