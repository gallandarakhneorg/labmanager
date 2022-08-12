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
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link Report}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ReportTest {

	private Report test;

	@BeforeEach
	public void setUp() {
		this.test = new Report();
	}

	@Test
	public void isRanked() {
		assertFalse(this.test.isRanked());
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
	public void getInstitution() {
		assertNull(this.test.getInstitution());
	}

	@Test
	public void setInstitution() {
		this.test.setInstitution("xyz");
		assertEquals("xyz", this.test.getInstitution());

		this.test.setInstitution("");
		assertNull(this.test.getInstitution());

		this.test.setInstitution(null);
		assertNull(this.test.getInstitution());
	}

	@Test
	public void getReportType() {
		assertNull(this.test.getReportType());
	}

	@Test
	public void setReportType() {
		this.test.setReportType("xyz");
		assertEquals("xyz", this.test.getReportType());

		this.test.setReportType("");
		assertNull(this.test.getReportType());

		this.test.setReportType(null);
		assertNull(this.test.getReportType());
	}

	@Test
	public void getReportNumber() {
		assertNull(this.test.getReportNumber());
	}

	@Test
	public void setReportNumber() {
		this.test.setReportNumber("xyz");
		assertEquals("xyz", this.test.getReportNumber());

		this.test.setReportNumber("");
		assertNull(this.test.getReportNumber());

		this.test.setReportNumber(null);
		assertNull(this.test.getReportNumber());
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

		Journal jour = mock(Journal.class);
		when(jour.getId()).thenReturn(658423);
		this.test.setAddress("adr0");
		this.test.setReportType("rtype0");
		this.test.setReportNumber("rnum0");
		this.test.setInstitution("inst0");

		this.test.serialize(generator, null);

		verify(generator, times(3)).writeStartObject();
		verify(generator).writeStringField(eq("abstractText"), eq("abs0"));
		verify(generator).writeStringField(eq("dblpURL"), eq("dblp0"));
		verify(generator).writeStringField(eq("doi"), eq("doi0"));
		verify(generator).writeStringField(eq("extraURL"), eq("url0"));
		verify(generator).writeStringField(eq("halId"), eq("hal0"));
		verify(generator).writeNumberField(eq("id"), eq(478));
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
		verify(generator).writeStringField(eq("reportType"), eq("rtype0"));
		verify(generator).writeStringField(eq("reportNumber"), eq("rnum0"));
		verify(generator).writeStringField(eq("institution"), eq("inst0"));

		verifyNoMoreInteractions(generator);
	}

}
