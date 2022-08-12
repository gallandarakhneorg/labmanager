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

package fr.ciadlab.labmanager.entities.publication.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashSet;

import com.fasterxml.jackson.core.JsonGenerator;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link Book}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class BookTest {

	private Book test;

	@BeforeEach
	public void setUp() {
		this.test = new Book();
	}

	@Test
	public void isRanked() {
		assertFalse(this.test.isRanked());
	}

	@Test
	public void getVolume() {
		assertNull(this.test.getVolume());
	}

	@Test
	public void setVolume() {
		this.test.setVolume("xyz");
		assertEquals("xyz", this.test.getVolume());

		this.test.setVolume("");
		assertNull(this.test.getVolume());

		this.test.setVolume(null);
		assertNull(this.test.getVolume());
	}

	@Test
	public void getNumber() {
		assertNull(this.test.getNumber());
	}

	@Test
	public void setNumber() {
		this.test.setNumber("xyz");
		assertEquals("xyz", this.test.getNumber());

		this.test.setNumber("");
		assertNull(this.test.getNumber());

		this.test.setNumber(null);
		assertNull(this.test.getNumber());
	}

	@Test
	public void getPages() {
		assertNull(this.test.getPages());
	}

	@Test
	public void setPages() {
		this.test.setPages("xyz");
		assertEquals("xyz", this.test.getPages());

		this.test.setPages("");
		assertNull(this.test.getPages());

		this.test.setPages(null);
		assertNull(this.test.getPages());
	}

	@Test
	public void getEditors() {
		assertNull(this.test.getEditors());
	}

	@Test
	public void setEditors() {
		this.test.setEditors("xyz");
		assertEquals("xyz", this.test.getEditors());

		this.test.setEditors("");
		assertNull(this.test.getEditors());

		this.test.setEditors(null);
		assertNull(this.test.getEditors());
	}

	@Test
	public void getAddress() {
		assertNull(this.test.getAddress());
	}

	@Test
	public void setAddress() {
		this.test.setAddress("xyz");
		assertEquals("xyz", this.test.getAddress());

		this.test.setAddress("");
		assertNull(this.test.getAddress());

		this.test.setAddress(null);
		assertNull(this.test.getAddress());
	}

	@Test
	public void getSeries() {
		assertNull(this.test.getSeries());
	}

	@Test
	public void setSeries() {
		this.test.setSeries("xyz");
		assertEquals("xyz", this.test.getSeries());

		this.test.setSeries("");
		assertNull(this.test.getSeries());

		this.test.setSeries(null);
		assertNull(this.test.getSeries());
	}

	@Test
	public void getPublisher() {
		assertNull(this.test.getPublisher());
	}

	@Test
	public void setPublisher() {
		this.test.setPublisher("xyz");
		assertEquals("xyz", this.test.getPublisher());

		this.test.setPublisher("");
		assertNull(this.test.getPublisher());

		this.test.setPublisher(null);
		assertNull(this.test.getPublisher());
	}

	@Test
	public void getEdition() {
		assertNull(this.test.getEdition());
	}

	@Test
	public void setEdition() {
		this.test.setEdition("xyz");
		assertEquals("xyz", this.test.getEdition());

		this.test.setEdition("");
		assertNull(this.test.getEdition());

		this.test.setEdition(null);
		assertNull(this.test.getEdition());
	}

	@Test
	public void serialize() throws Exception {
		Person pers0 = mock(Person.class);
		when(pers0.getId()).thenReturn(45875);
		Authorship aut0 = mock(Authorship.class);
		when(aut0.getAuthorRank()).thenReturn(4789);
		when(aut0.getPerson()).thenReturn(pers0);
		when(aut0.getPublication()).thenReturn(this.test);

		Person pers1 = mock(Person.class);
		when(pers1.getId()).thenReturn(68875);
		Authorship aut1 = mock(Authorship.class);
		when(aut1.getAuthorRank()).thenReturn(6989);
		when(aut1.getPerson()).thenReturn(pers1);
		when(aut1.getPublication()).thenReturn(this.test);

		this.test.setAbstractText("abs0");
		this.test.setDblpURL("dblp0");
		this.test.setDOI("doi0");
		this.test.setExtraURL("url0");
		this.test.setHalId("hal0");
		this.test.setId(478);
		this.test.setISBN("isbn0");
		this.test.setISSN("issn0");
		this.test.setKeywords("kw0");
		this.test.setMajorLanguage(PublicationLanguage.GERMAN);
		this.test.setPathToDownloadableAwardCertificate("path0");
		this.test.setPathToDownloadablePDF("path1");
		this.test.setPublicationDate(Date.valueOf("2022-07-23"));
		this.test.setPublicationYear(2022);
		this.test.setTitle("title0");
		this.test.setType(PublicationType.EUROPEAN_PATENT);
		this.test.setVideoURL("video0");
		this.test.setAuthorships(new HashSet<>(Arrays.asList(aut0, aut1)));
		JsonGenerator generator = mock(JsonGenerator.class);

		this.test.setAddress("adr0");
		this.test.setEdition("edition0");
		this.test.setEditors("editor0");
		this.test.setNumber("number0");
		this.test.setPages("pages0");
		this.test.setPublisher("publisher0");
		this.test.setSeries("series0");
		this.test.setVolume("vol0");

		this.test.serialize(generator, null);

		verify(generator, times(3)).writeStartObject();
		verify(generator).writeStringField(eq("abstractText"), eq("abs0"));
		verify(generator).writeStringField(eq("dblpURL"), eq("dblp0"));
		verify(generator).writeStringField(eq("doi"), eq("doi0"));
		verify(generator).writeStringField(eq("extraURL"), eq("url0"));
		verify(generator).writeStringField(eq("halId"), eq("hal0"));
		verify(generator).writeNumberField(eq("id"), eq(478));
		verify(generator).writeStringField(eq("isbn"), eq("isbn0"));
		verify(generator).writeStringField(eq("issn"), eq("issn0"));
		verify(generator).writeStringField(eq("keywords"), eq("kw0"));
		verify(generator).writeStringField(eq("majorLanguage"), eq("GERMAN"));
		verify(generator).writeStringField(eq("pathToDownloadableAwardCertificate"), eq("path0"));
		verify(generator).writeStringField(eq("pathToDownloadablePDF"), eq("path1"));
		verify(generator).writeStringField(eq("publicationDate"), eq("2022-07-23"));
		verify(generator).writeNumberField(eq("publicationYear"), eq(2022));
		verify(generator).writeStringField(eq("title"), eq("title0"));
		verify(generator).writeStringField(eq("type"), eq("EUROPEAN_PATENT"));
		verify(generator).writeStringField(eq("videoURL"), eq("video0"));

		verify(generator).writeArrayFieldStart(eq("authors"));
		verify(generator).writeNumber(eq(45875));
		verify(generator).writeNumber(eq(68875));

		verify(generator).writeArrayFieldStart(eq("authorships"));
		verify(generator).writeNumberField(eq("authorRank"), eq(4789));
		verify(generator).writeNumberField(eq("authorRank"), eq(6989));
		verify(generator).writeNumberField(eq("person"), eq(45875));
		verify(generator).writeNumberField(eq("person"), eq(68875));

		verify(generator, times(2)).writeEndArray();

		verify(generator, times(3)).writeEndObject();

		verify(generator).writeStringField(eq("address"), eq("adr0"));
		verify(generator).writeStringField(eq("edition"), eq("edition0"));
		verify(generator).writeStringField(eq("editors"), eq("editor0"));
		verify(generator).writeStringField(eq("number"), eq("number0"));
		verify(generator).writeStringField(eq("pages"), eq("pages0"));
		verify(generator).writeStringField(eq("publisher"), eq("publisher0"));
		verify(generator).writeStringField(eq("series"), eq("series0"));
		verify(generator).writeStringField(eq("volume"), eq("vol0"));

		verifyNoMoreInteractions(generator);
	}

}
