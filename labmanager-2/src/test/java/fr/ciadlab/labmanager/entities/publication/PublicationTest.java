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

package fr.ciadlab.labmanager.entities.publication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;

import com.google.gson.JsonObject;
import fr.ciadlab.labmanager.entities.member.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link Publication}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class PublicationTest {

	private Publication test;

	@BeforeEach
	public void setUp() {
		this.test = new Publication() {
			@Override
			public boolean isRanked() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Test
	public void getId() {
		assertEquals(0, this.test.getId());
	}

	@Test
	public void setId() {
		this.test.setId(-466);
		assertEquals(-466, this.test.getId());

		this.test.setId(0);
		assertEquals(0, this.test.getId());

		this.test.setId(478);
		assertEquals(478, this.test.getId());
	}

	@Test
	public void getType() {
		assertNull(this.test.getType());
	}

	@Test
	public void setType_type() {
		this.test.setType(PublicationType.INTERNATIONAL_CONFERENCE_PAPER);
		assertSame(PublicationType.INTERNATIONAL_CONFERENCE_PAPER, this.test.getType());

		this.test.setType(PublicationType.NATIONAL_POSTER);
		assertSame(PublicationType.NATIONAL_POSTER, this.test.getType());

		this.test.setType((PublicationType) null);
		assertNull(this.test.getType());
	}

	@Test
	public void setType_string() {
		this.test.setType(PublicationType.INTERNATIONAL_CONFERENCE_PAPER);
		assertSame(PublicationType.INTERNATIONAL_CONFERENCE_PAPER, this.test.getType());

		this.test.setType(PublicationType.NATIONAL_POSTER);
		assertSame(PublicationType.NATIONAL_POSTER, this.test.getType());

		this.test.setType((String) null);
		assertNull(this.test.getType());
	}

	@Test
	public void getCategory() {
		assertNull(this.test.getCategory());

		this.test.setType(PublicationType.INTERNATIONAL_CONFERENCE_PAPER);
		assertSame(PublicationCategory.C_ACTI, this.test.getCategory());

		this.test.setType(PublicationType.NATIONAL_POSTER);
		assertSame(PublicationCategory.C_AFF, this.test.getCategory());

		this.test.setType((PublicationType) null);
		assertNull(this.test.getCategory());
	}

	@Test
	public void getTitle() {
		assertNull(this.test.getTitle());
	}

	@Test
	public void setTitle() {
		this.test.setTitle("xyz");
		assertEquals("xyz", this.test.getTitle());

		this.test.setTitle("");
		assertNull(this.test.getTitle());

		this.test.setTitle(null);
		assertNull(this.test.getTitle());
	}

	@Test
	public void getAbstractText() {
		assertNull(this.test.getAbstractText());
	}

	@Test
	public void setAbstractText() {
		this.test.setAbstractText("xyz");
		assertEquals("xyz", this.test.getAbstractText());

		this.test.setAbstractText("");
		assertNull(this.test.getAbstractText());

		this.test.setAbstractText(null);
		assertNull(this.test.getAbstractText());
	}

	@Test
	public void getKeywords() {
		assertNull(this.test.getKeywords());
	}

	@Test
	public void setKeywords() {
		this.test.setKeywords("xyz");
		assertEquals("xyz", this.test.getKeywords());

		this.test.setKeywords("");
		assertNull(this.test.getKeywords());

		this.test.setKeywords(null);
		assertNull(this.test.getKeywords());
	}

	@Test
	public void getPublicationDate() {
		assertNull(this.test.getPublicationDate());
	}

	@Test
	public void setPublicationDate_date() {
		this.test.setPublicationDate(Date.valueOf("2022-07-12"));
		assertEquals(Date.valueOf("2022-07-12"), this.test.getPublicationDate());

		this.test.setPublicationDate((Date) null);
		assertNull(this.test.getPublicationDate());
	}

	@Test
	public void setPublicationDate_string() {
		this.test.setPublicationDate(Date.valueOf("2022-07-12"));
		assertEquals(Date.valueOf("2022-07-12"), this.test.getPublicationDate());

		assertThrows(IllegalArgumentException.class, () -> {
			this.test.setPublicationDate((String) null);
		});
		assertEquals(Date.valueOf("2022-07-12"), this.test.getPublicationDate());
	}

	@Test
	public void getPublicationYear() {
		assertEquals(LocalDate.now().getYear(), this.test.getPublicationYear());
	}

	@Test
	public void getDefaultPublicationYear() {
		assertEquals(LocalDate.now().getYear(), Publication.getDefaultPublicationYear());
	}

	@Test
	public void setPublicationYear() {
		this.test.setPublicationYear(-15);
		assertEquals(-15, this.test.getPublicationYear());

		this.test.setPublicationYear(0);
		assertEquals(0, this.test.getPublicationYear());

		this.test.setPublicationYear(2022);
		assertEquals(2022, this.test.getPublicationYear());
	}

	@Test
	public void getISBN() {
		assertNull(this.test.getISBN());
	}

	@Test
	public void setISBN() {
		this.test.setISBN("xyz");
		assertEquals("xyz", this.test.getISBN());

		this.test.setISBN("");
		assertNull(this.test.getISBN());

		this.test.setISBN(null);
		assertNull(this.test.getISBN());
	}

	@Test
	public void getISSN() {
		assertNull(this.test.getISSN());
	}

	@Test
	public void setISSN() {
		this.test.setISSN("xyz");
		assertEquals("xyz", this.test.getISSN());

		this.test.setISSN("");
		assertNull(this.test.getISSN());

		this.test.setISSN(null);
		assertNull(this.test.getISSN());
	}

	@Test
	public void getDOI() {
		assertNull(this.test.getDOI());
	}

	@Test
	public void setDOI() {
		this.test.setDOI("xyz");
		assertEquals("xyz", this.test.getDOI());

		this.test.setDOI("");
		assertNull(this.test.getDOI());

		this.test.setDOI(null);
		assertNull(this.test.getDOI());
	}

	@Test
	public void getExtraURLObject() {
		assertNull(this.test.getExtraURLObject());
	}

	@Test
	public void setExtraURL_URL() throws Exception {
		this.test.setExtraURL(new URL("http://abc.org"));
		assertEquals(new URL("http://abc.org"), this.test.getExtraURLObject());
		assertEquals("http://abc.org", this.test.getExtraURL());

		this.test.setExtraURL((URL) null);
		assertNull(this.test.getExtraURLObject());
		assertNull(this.test.getExtraURL());
	}

	@Test
	public void getExtraURL() {
		assertNull(this.test.getExtraURL());
	}

	@Test
	public void setExtraURL_String() throws Exception {
		this.test.setExtraURL("http://xyz.org");
		assertEquals("http://xyz.org", this.test.getExtraURL());
		assertEquals(new URL("http://xyz.org"), this.test.getExtraURLObject());

		this.test.setExtraURL("");
		assertNull(this.test.getExtraURL());
		assertNull(this.test.getExtraURLObject());

		this.test.setExtraURL((String) null);
		assertNull(this.test.getExtraURL());
		assertNull(this.test.getExtraURLObject());
	}

	@Test
	public void getVideoURLObject() {
		assertNull(this.test.getVideoURLObject());
	}

	@Test
	public void setVideoURL_URL() throws Exception {
		this.test.setVideoURL(new URL("http://xyz.com"));
		assertEquals("http://xyz.com", this.test.getVideoURL());
		assertEquals(new URL("http://xyz.com"), this.test.getVideoURLObject());

		this.test.setVideoURL((URL) null);
		assertNull(this.test.getVideoURL());
		assertNull(this.test.getVideoURLObject());
	}

	@Test
	public void getVideoURL() {
		assertNull(this.test.getVideoURL());
	}

	@Test
	public void setVideoURL_String() throws Exception {
		this.test.setVideoURL("http://xyz.com");
		assertEquals("http://xyz.com", this.test.getVideoURL());
		assertEquals(new URL("http://xyz.com"), this.test.getVideoURLObject());

		this.test.setVideoURL("");
		assertNull(this.test.getVideoURL());
		assertNull(this.test.getVideoURLObject());

		this.test.setVideoURL((String) null);
		assertNull(this.test.getVideoURL());
		assertNull(this.test.getVideoURLObject());
	}

	@Test
	public void getDblpURLObject() {
		assertNull(this.test.getDblpURLObject());
	}

	@Test
	public void setDblpURL_URL() throws Exception {
		this.test.setDblpURL(new URL("http://xyz.com"));
		assertEquals("http://xyz.com", this.test.getDblpURL());
		assertEquals(new URL("http://xyz.com"), this.test.getDblpURLObject());

		this.test.setDblpURL((URL) null);
		assertNull(this.test.getDblpURL());
		assertNull(this.test.getDblpURLObject());
	}

	@Test
	public void getDblpURL() {
		assertNull(this.test.getDblpURL());
	}

	@Test
	public void setDblpURL_String() throws Exception {
		this.test.setDblpURL("http://xyz.com");
		assertEquals("http://xyz.com", this.test.getDblpURL());
		assertEquals(new URL("http://xyz.com"), this.test.getDblpURLObject());

		this.test.setDblpURL("");
		assertNull(this.test.getDblpURL());
		assertNull(this.test.getDblpURLObject());

		this.test.setDblpURL((String) null);
		assertNull(this.test.getDblpURL());
		assertNull(this.test.getDblpURLObject());
	}

	@Test
	public void getPathToDownloadablePDF() {
		assertNull(this.test.getPathToDownloadablePDF());
	}

	@Test
	public void setPathToDownloadablePDF() {
		this.test.setPathToDownloadablePDF("xyz");
		assertEquals("xyz", this.test.getPathToDownloadablePDF());

		this.test.setPathToDownloadablePDF("");
		assertNull(this.test.getPathToDownloadablePDF());

		this.test.setPathToDownloadablePDF(null);
		assertNull(this.test.getPathToDownloadablePDF());
	}

	@Test
	public void getPathToDownloadableAwardCertificate() {
		assertNull(this.test.getPathToDownloadableAwardCertificate());
	}

	@Test
	public void setPathToDownloadableAwardCertificate() {
		this.test.setPathToDownloadableAwardCertificate("xyz");
		assertEquals("xyz", this.test.getPathToDownloadableAwardCertificate());

		this.test.setPathToDownloadableAwardCertificate("");
		assertNull(this.test.getPathToDownloadableAwardCertificate());

		this.test.setPathToDownloadableAwardCertificate(null);
		assertNull(this.test.getPathToDownloadableAwardCertificate());
	}

	@Test
	public void getMajorLanguage() {
		assertSame(PublicationLanguage.ENGLISH, this.test.getMajorLanguage());
	}

	@Test
	public void setMajorLanguage_language() {
		this.test.setMajorLanguage(PublicationLanguage.GERMAN);
		assertSame(PublicationLanguage.GERMAN, this.test.getMajorLanguage());

		this.test.setMajorLanguage((PublicationLanguage) null);
		assertSame(PublicationLanguage.ENGLISH, this.test.getMajorLanguage());
	}

	@Test
	public void setMajorLanguage_string() {
		this.test.setMajorLanguage(PublicationLanguage.GERMAN);
		assertSame(PublicationLanguage.GERMAN, this.test.getMajorLanguage());

		this.test.setMajorLanguage((String) null);
		assertSame(PublicationLanguage.ENGLISH, this.test.getMajorLanguage());
	}

	private Authorship[] createAuthorships() {
		Publication pub = mock(Publication.class);
		when(pub.getId()).thenReturn(1234);

		Person pers0 = mock(Person.class);
		when(pers0.getId()).thenReturn(7845);

		Person pers1 = mock(Person.class);
		when(pers1.getId()).thenReturn(1230);

		Person pers2 = mock(Person.class);
		when(pers2.getId()).thenReturn(4861);

		Authorship a0 = mock(Authorship.class);
		when(a0.getPublication()).thenReturn(pub);
		when(a0.getPerson()).thenReturn(pers0);
		when(a0.getAuthorRank()).thenReturn(1);
		this.test.addAuthorship(a0);

		Authorship a1 = mock(Authorship.class);
		when(a1.getPublication()).thenReturn(pub);
		when(a1.getPerson()).thenReturn(pers1);
		when(a1.getAuthorRank()).thenReturn(0);
		this.test.addAuthorship(a1);

		Authorship a2 = mock(Authorship.class);	
		when(a2.getPublication()).thenReturn(pub);
		when(a2.getPerson()).thenReturn(pers2);
		when(a2.getAuthorRank()).thenReturn(2);
		this.test.addAuthorship(a2);

		return new Authorship[] {a0, a1, a2};
	}

	private Person[] createAuthors() {
		final Authorship[] as = createAuthorships();
		Person[] ps = new Person[as.length];
		for (int i = 0; i < ps.length; ++i) {
			ps[i] = as[i].getPerson();
		}
		return ps;
	}

	@Test
	public void getAuthorships() {
		assertTrue(this.test.getAuthorships().isEmpty());

		Authorship[] as = createAuthorships();
		assertEquals(3, this.test.getAuthorships().size());
		assertTrue(this.test.getAuthorships().contains(as[0]));
		assertTrue(this.test.getAuthorships().contains(as[1]));
		assertTrue(this.test.getAuthorships().contains(as[2]));
	}

	@Test
	public void getAuthors() {
		assertTrue(this.test.getAuthors().isEmpty());

		assertTrue(this.test.getAuthors().isEmpty());

		Person[] as = createAuthors();
		assertEquals(3, this.test.getAuthors().size());
		assertTrue(this.test.getAuthors().contains(as[0]));
		assertTrue(this.test.getAuthors().contains(as[1]));
		assertTrue(this.test.getAuthors().contains(as[2]));
	}

	@Test
	public void deleteAuthorship() {
		Authorship[] as = createAuthorships();

		this.test.deleteAuthorship(as[1]);
		assertEquals(2, this.test.getAuthorships().size());
		assertTrue(this.test.getAuthorships().contains(as[0]));
		assertFalse(this.test.getAuthorships().contains(as[1]));
		assertTrue(this.test.getAuthorships().contains(as[2]));
	}

	@Test
	public void toJson() {
		this.test.setAbstractText("abs0");
		this.test.setDblpURL("dblp0");
		this.test.setDOI("doi0");
		this.test.setExtraURL("extra0");
		this.test.setId(123);
		this.test.setISBN("isbn0");
		this.test.setISSN("issn0");
		this.test.setKeywords("kw0");
		this.test.setMajorLanguage(PublicationLanguage.ITALIAN);
		this.test.setPathToDownloadableAwardCertificate("path/to/award0");
		this.test.setPathToDownloadablePDF("path/to/pdf0");
		this.test.setPublicationDate(Date.valueOf("2022-07-24"));
		this.test.setPublicationYear(2022);
		this.test.setTitle("title0");
		this.test.setType(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION);
		this.test.setVideoURL("video0");

		JsonObject obj = new JsonObject();

		this.test.toJson(obj);

		assertNotNull(obj.get("id"));
		assertNotNull(obj.get("type"));
		assertNotNull(obj.get("title"));
		assertNotNull(obj.get("abstractText"));
		assertNotNull(obj.get("keywords"));
		assertNotNull(obj.get("publicationDate"));
		assertNotNull(obj.get("publicationYear"));
		assertNotNull(obj.get("isbn"));
		assertNotNull(obj.get("issn"));
		assertNotNull(obj.get("doi"));
		assertNotNull(obj.get("dblpURL"));
		assertNotNull(obj.get("extraURL"));
		assertNotNull(obj.get("videoURL"));
		assertNotNull(obj.get("majorLanguage"));
		assertNotNull(obj.get("pathToDownloadablePDF"));
		assertNotNull(obj.get("pathToDownloadableAwardCertificate"));
		assertNull(obj.get("authors"));
	}

}
