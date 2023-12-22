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

package fr.utbm.ciad.labmanager.tests.data.publication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.publication.Authorship;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.data.publication.PublicationCategory;
import fr.utbm.ciad.labmanager.data.publication.PublicationLanguage;
import fr.utbm.ciad.labmanager.data.publication.PublicationType;
import fr.utbm.ciad.labmanager.data.scientificaxis.ScientificAxis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

			@Override
			public String getWherePublishedShortDescription() {
				throw new UnsupportedOperationException();
			}

			@Override
			public String getPublicationTarget() {
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
		this.test.setPublicationDate(LocalDate.parse("2022-07-12"));
		assertEquals(LocalDate.parse("2022-07-12"), this.test.getPublicationDate());

		this.test.setPublicationDate((LocalDate) null);
		assertNull(this.test.getPublicationDate());
	}

	@Test
	public void setPublicationDate_string() {
		this.test.setPublicationDate(LocalDate.parse("2022-07-12"));
		assertEquals(LocalDate.parse("2022-07-12"), this.test.getPublicationDate());

		assertThrows(IllegalArgumentException.class, () -> {
			this.test.setPublicationDate((String) null);
		});
		assertEquals(LocalDate.parse("2022-07-12"), this.test.getPublicationDate());
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
	public void getHalId() {
		assertNull(this.test.getHalId());
	}

	@Test
	public void setHalId() {
		this.test.setHalId("xyz");
		assertEquals("xyz", this.test.getHalId());

		this.test.setHalId("");
		assertNull(this.test.getHalId());

		this.test.setHalId(null);
		assertNull(this.test.getHalId());
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
		when(pub.getId()).thenReturn(1234l);

		Person pers0 = mock(Person.class);
		when(pers0.getId()).thenReturn(7845l);

		Person pers1 = mock(Person.class);
		when(pers1.getId()).thenReturn(1230l);

		Person pers2 = mock(Person.class);
		when(pers2.getId()).thenReturn(4861l);

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
	public void getManualValidationForced() {
		assertFalse(this.test.getManualValidationForced());
	}

	@Test
	public void setManualValidationForced_boolean() {
		assertFalse(this.test.getManualValidationForced());
		this.test.setManualValidationForced(true);
		assertTrue(this.test.getManualValidationForced());
		this.test.setManualValidationForced(false);
		assertFalse(this.test.getManualValidationForced());
	}

	@Test
	public void setManualValidationForced_Boolean() {
		assertFalse(this.test.getManualValidationForced());
		this.test.setManualValidationForced(Boolean.TRUE);
		assertTrue(this.test.getManualValidationForced());
		this.test.setManualValidationForced(Boolean.FALSE);
		assertFalse(this.test.getManualValidationForced());
		this.test.setManualValidationForced(Boolean.TRUE);
		assertTrue(this.test.getManualValidationForced());
		this.test.setManualValidationForced(null);
		assertFalse(this.test.getManualValidationForced());
	}

	@Test
	public void getScientificAxes() {
		assertTrue(this.test.getScientificAxes().isEmpty());
	}

	@Test
	public void setScientificAxes() {
		Set<Publication> l0 = new HashSet<>();
		ScientificAxis a0 = mock(ScientificAxis.class);
		when(a0.getPublications()).thenReturn(l0);
		Set<Publication> l1 = new HashSet<>();
		ScientificAxis a1 = mock(ScientificAxis.class);
		when(a1.getPublications()).thenReturn(l1);
		List<ScientificAxis> axes = Arrays.asList(a0, a1);

		this.test.setScientificAxes(axes);

		Set<ScientificAxis> actual = this.test.getScientificAxes();
		assertNotNull(actual);
		assertEquals(2, actual.size());
		assertTrue(actual.contains(a0));
		assertTrue(actual.contains(a1));

		assertFalse(l0.contains(this.test));
		assertFalse(l1.contains(this.test));
	}

	private static Publication createTransient1() {
		var e = new Publication() {
			@Override
			public boolean isRanked() {
				throw new UnsupportedOperationException();
			}

			@Override
			public String getWherePublishedShortDescription() {
				throw new UnsupportedOperationException();
			}

			@Override
			public String getPublicationTarget() {
				throw new UnsupportedOperationException();
			}
		};
		e.setTitle("A");
		return e;
	}	

	private static Publication createTransient2() {
		var e = new Publication() {
			@Override
			public boolean isRanked() {
				throw new UnsupportedOperationException();
			}

			@Override
			public String getWherePublishedShortDescription() {
				throw new UnsupportedOperationException();
			}

			@Override
			public String getPublicationTarget() {
				throw new UnsupportedOperationException();
			}
		};
		e.setTitle("B");
		return e;
	}	

	private static Publication createManaged1() {
		var e = createTransient1();
		e.setId(10l);
		return e;
	}	

	private static Publication createManaged2() {
		var e = createTransient2();
		e.setId(20l);
		return e;
	}	

	@Test
	@DisplayName("t1.equals(null)")
	public void test_equals_0() {
		assertFalse(createTransient1().equals(null));
	}
	
	@Test
	@DisplayName("m1.equals(null)")
	public void test_equals_1() {
		assertFalse(createManaged1().equals(null));
	}

	@Test
	@DisplayName("t1.equals(t1)")
	public void test_equals_2() {
		var t1 = createTransient1();
		assertTrue(t1.equals(t1));
	}

	@Test
	@DisplayName("m1.equals(m1)")
	public void test_equals_3() {
		var m1 = createManaged1();
		assertTrue(m1.equals(m1));
	}

	@Test
	@DisplayName("t1.equals(t2)")
	public void test_equals_4() {
		// 2 transient entities need to be NOT equal
		var t1 = createTransient1();
		var t2 = createTransient2();
		assertFalse(t1.equals(t2));
	}

	@Test
	@DisplayName("t2.equals(t1)")
	public void test_equals_5() {
		// 2 transient entities need to be NOT equal
		var t1 = createTransient1();
		var t2 = createTransient2();
		assertFalse(t2.equals(t1));
	}

	@Test
	@DisplayName("m1.equals(m2)")
	public void test_equals_6() {
		// 2 managed entities that represent different records need to be NOT equal
		var m1 = createManaged1();
		var m2 = createManaged2();
		assertFalse(m1.equals(m2));
	}

	@Test
	@DisplayName("m2.equals(m1)")
	public void test_equals_7() {
		// 2 managed entities that represent different records need to be NOT equal
		var m1 = createManaged1();
		var m2 = createManaged2();
		assertFalse(m2.equals(m1));
	}

	@Test
	@DisplayName("m1.equals(m1')")
	public void test_equals_8() {
		// 2 managed entities that represent the same record need to be equal
		var m1 = createManaged1();
		var m1p = createManaged1();
		assertTrue(m1.equals(m1p));
	}

	@Test
	@DisplayName("m1'.equals(m1)")
	public void test_equals_9() {
		// 2 managed entities that represent the same record need to be equal
		var m1 = createManaged1();
		var m1p = createManaged1();
		assertTrue(m1p.equals(m1));
	}

	@Test
	@DisplayName("m1.equals(t1)")
	public void test_equals_10() {
		// a detached/transient and a managed entity object that represent the same record need to be equal
		var t1 = createTransient1();
		var m1 = createManaged1();
		assertTrue(m1.equals(t1));
	}

	@Test
	@DisplayName("t1.equals(m1)")
	public void test_equals_11() {
		// a detached/transient and a managed entity object that represent the same record need to be equal
		var t1 = createTransient1();
		var m1 = createManaged1();
		assertTrue(t1.equals(m1));
	}

	@Test
	@DisplayName("m1.equals(t2)")
	public void test_equals_12() {
		var t2 = createTransient2();
		var m1 = createManaged1();
		assertFalse(m1.equals(t2));
	}

	@Test
	@DisplayName("t2.equals(m1)")
	public void test_equals_13() {
		var t2 = createTransient2();
		var m1 = createManaged1();
		assertFalse(t2.equals(m1));
	}

	@Test
	@DisplayName("t1.hashCode == t1.hashCode")
	public void test_hashCode_0() {
		var t1 = createTransient1();
		assertEquals(t1.hashCode(), t1.hashCode());
	}

	@Test
	@DisplayName("t1.hashCode == t1p.hashCode")
	public void test_hashCode_1() {
		var t1 = createTransient1();
		var t1p = createTransient1();
		assertEquals(t1.hashCode(), t1p.hashCode());
	}

	@Test
	@DisplayName("t1.hashCode != m1.hashCode")
	public void test_hashCode_2() {
		var t1 = createTransient1();
		var m1 = createManaged1();
		assertNotEquals(t1.hashCode(), m1.hashCode());
	}

}
