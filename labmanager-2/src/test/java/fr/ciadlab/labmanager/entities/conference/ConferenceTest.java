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

package fr.ciadlab.labmanager.entities.conference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.net.URL;
import java.util.Set;
import java.util.TreeSet;

import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link Conference}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ConferenceTest {

	private Conference test;

	@BeforeEach
	public void setUp() {
		this.test = new Conference();
	}

	@Test
	public void getId() {
		assertEquals(0, this.test.getId());
	}

	@Test
	public void setId() {
		assertEquals(0, this.test.getId());

		this.test.setId(1478);
		assertEquals(1478, this.test.getId());

		this.test.setId(0);
		assertEquals(0, this.test.getId());

		this.test.setId(-568);
		assertEquals(-568, this.test.getId());
	}

	@Test
	public void getAcronym() {
		assertNull(this.test.getAcronym());
	}

	@Test
	public void setAcronym() {
		assertNull(this.test.getAcronym());

		this.test.setAcronym("abc");
		assertEquals("abc", this.test.getAcronym());

		this.test.setAcronym("");
		assertNull(this.test.getAcronym());

		this.test.setAcronym("xyz");
		assertEquals("xyz", this.test.getAcronym());

		this.test.setAcronym(null);
		assertNull(this.test.getAcronym());
	}

	@Test
	public void getName() {
		assertNull(this.test.getName());
	}

	@Test
	public void setName() {
		assertNull(this.test.getName());

		this.test.setName("abc");
		assertEquals("abc", this.test.getName());

		this.test.setName("");
		assertNull(this.test.getName());

		this.test.setName("xyz");
		assertEquals("xyz", this.test.getName());

		this.test.setName(null);
		assertNull(this.test.getName());
	}

	@Test
	public void getPublisher() {
		assertNull(this.test.getPublisher());
	}

	@Test
	public void setPublisher() {
		assertNull(this.test.getPublisher());

		this.test.setPublisher("abc");
		assertEquals("abc", this.test.getPublisher());

		this.test.setPublisher("");
		assertNull(this.test.getPublisher());

		this.test.setPublisher("xyz");
		assertEquals("xyz", this.test.getPublisher());

		this.test.setPublisher(null);
		assertNull(this.test.getPublisher());
	}

	@Test
	public void getConferenceURL() {
		assertNull(this.test.getConferenceURL());
	}

	@Test
	public void setConferenceURL_String() {
		assertNull(this.test.getConferenceURL());

		this.test.setConferenceURL("abc");
		assertEquals("abc", this.test.getConferenceURL());

		this.test.setConferenceURL("");
		assertNull(this.test.getConferenceURL());

		this.test.setConferenceURL("xyz");
		assertEquals("xyz", this.test.getConferenceURL());

		this.test.setConferenceURL((String) null);
		assertNull(this.test.getConferenceURL());
	}

	@Test
	public void getConferenceURLObject() {
		assertNull(this.test.getConferenceURLObject());
	}

	@Test
	public void setConferenceURL_URL() throws Exception {
		assertNull(this.test.getConferenceURL());
		assertNull(this.test.getConferenceURLObject());

		this.test.setConferenceURL(new URL("http://abc.com"));
		assertEquals("http://abc.com", this.test.getConferenceURL());
		assertEquals(new URL("http://abc.com"), this.test.getConferenceURLObject());

		this.test.setConferenceURL((URL) null);
		assertNull(this.test.getConferenceURL());
		assertNull(this.test.getConferenceURLObject());

		this.test.setConferenceURL(new URL("http://xyz.org"));
		assertEquals("http://xyz.org", this.test.getConferenceURL());
		assertEquals(new URL("http://xyz.org"), this.test.getConferenceURLObject());
	}

	@Test
	public void getCoreId() {
		assertNull(this.test.getCoreId());
	}

	@Test
	public void setCoreId() {
		assertNull(this.test.getCoreId());

		this.test.setCoreId("abc");
		assertEquals("abc", this.test.getCoreId());

		this.test.setCoreId("");
		assertNull(this.test.getCoreId());

		this.test.setCoreId("xyz");
		assertEquals("xyz", this.test.getCoreId());

		this.test.setCoreId(null);
		assertNull(this.test.getCoreId());
	}

	@Test
	public void getQualityIndicators() {
		assertTrue(this.test.getQualityIndicators().isEmpty());
	}

	@Test
	public void getQualityIndicatorsForYear() {
		assertNull(this.test.getQualityIndicatorsForYear(-10));
		assertNull(this.test.getQualityIndicatorsForYear(0));
		assertNull(this.test.getQualityIndicatorsForYear(123));
		assertNull(this.test.getQualityIndicatorsForYear(2022));
	}

	@Test
	public void getQualityIndicatorsFor() {
		assertNull(this.test.getQualityIndicatorsFor(-10, it -> true));
		assertNull(this.test.getQualityIndicatorsFor(0, it -> true));
		assertNull(this.test.getQualityIndicatorsFor(123, it -> true));
		assertNull(this.test.getQualityIndicatorsFor(2022, it -> true));
	}

	@Test
	public void hasQualityIndicatorsForYear() {
		assertNull(this.test.getQualityIndicatorsForYear(-10));
		assertNull(this.test.getQualityIndicatorsForYear(0));
		assertNull(this.test.getQualityIndicatorsForYear(123));
		assertNull(this.test.getQualityIndicatorsForYear(2022));
	}

	@Test
	public void getCoreIndexByYear() {
		assertSame(CoreRanking.NR, this.test.getCoreIndexByYear(-1234));
		assertSame(CoreRanking.NR, this.test.getCoreIndexByYear(0));
		assertSame(CoreRanking.NR, this.test.getCoreIndexByYear(1234));
	}

	@Test
	public void setCoreIndexByYear() {
		assertSame(CoreRanking.NR, this.test.getCoreIndexByYear(512));
		assertSame(CoreRanking.NR, this.test.getCoreIndexByYear(1234));
		assertSame(CoreRanking.NR, this.test.getCoreIndexByYear(4567));

		assertNotNull(this.test.setCoreIndexByYear(1234, CoreRanking.A_STAR));
		assertSame(CoreRanking.NR, this.test.getCoreIndexByYear(512));
		assertSame(CoreRanking.A_STAR, this.test.getCoreIndexByYear(1234));
		assertSame(CoreRanking.A_STAR, this.test.getCoreIndexByYear(4567));

		assertNotNull(this.test.setCoreIndexByYear(1234, null));
		assertSame(CoreRanking.NR, this.test.getCoreIndexByYear(512));
		assertSame(CoreRanking.NR, this.test.getCoreIndexByYear(1234));
		assertSame(CoreRanking.NR, this.test.getCoreIndexByYear(4567));

		assertNotNull(this.test.setCoreIndexByYear(1234, CoreRanking.A_STAR));
		assertSame(CoreRanking.NR, this.test.getCoreIndexByYear(512));
		assertSame(CoreRanking.A_STAR, this.test.getCoreIndexByYear(1234));
		assertSame(CoreRanking.A_STAR, this.test.getCoreIndexByYear(4567));

		assertNotNull(this.test.setCoreIndexByYear(3, CoreRanking.B));
		assertSame(CoreRanking.B, this.test.getCoreIndexByYear(512));
		assertSame(CoreRanking.A_STAR, this.test.getCoreIndexByYear(1234));
		assertSame(CoreRanking.A_STAR, this.test.getCoreIndexByYear(4567));

		assertNotNull(this.test.setCoreIndexByYear(2500, CoreRanking.C));
		assertSame(CoreRanking.B, this.test.getCoreIndexByYear(512));
		assertSame(CoreRanking.A_STAR, this.test.getCoreIndexByYear(1234));
		assertSame(CoreRanking.C, this.test.getCoreIndexByYear(4567));
	}

	@Test
	public void hasCoreIndexForYear() {
		assertFalse(this.test.hasCoreIndexForYear(512));
		assertFalse(this.test.hasCoreIndexForYear(1234));
		assertFalse(this.test.hasCoreIndexForYear(4567));

		assertNotNull(this.test.setCoreIndexByYear(1234, CoreRanking.C));
		assertFalse(this.test.hasCoreIndexForYear(512));
		assertTrue(this.test.hasCoreIndexForYear(1234));
		assertTrue(this.test.hasCoreIndexForYear(4567));

		assertNotNull(this.test.setCoreIndexByYear(1234, null));
		assertFalse(this.test.hasCoreIndexForYear(512));
		assertFalse(this.test.hasCoreIndexForYear(1234));
		assertFalse(this.test.hasCoreIndexForYear(4567));

		assertNotNull(this.test.setCoreIndexByYear(1234, CoreRanking.A_STAR));
		assertFalse(this.test.hasCoreIndexForYear(512));
		assertTrue(this.test.hasCoreIndexForYear(1234));
		assertTrue(this.test.hasCoreIndexForYear(4567));

		assertNotNull(this.test.setCoreIndexByYear(3, CoreRanking.B));
		assertTrue(this.test.hasCoreIndexForYear(512));
		assertTrue(this.test.hasCoreIndexForYear(1234));
		assertTrue(this.test.hasCoreIndexForYear(4567));
	}

	@Test
	public void getOpenAcess() {
		assertNull(this.test.getOpenAccess());
	}

	@Test
	public void setOpenAccess() {
		assertNull(this.test.getOpenAccess());

		this.test.setOpenAccess(Boolean.TRUE);
		assertTrue(this.test.getOpenAccess());

		this.test.setOpenAccess(null);
		assertNull(this.test.getOpenAccess());

		this.test.setOpenAccess(Boolean.FALSE);
		assertFalse(this.test.getOpenAccess());
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
	public void getEnclosingConference() {
		assertNull(this.test.getEnclosingConference());
	}

	@Test
	public void setEnclosingConference() {
		Conference conf = mock(Conference.class);
		this.test.setEnclosingConference(conf);
		assertSame(conf, this.test.getEnclosingConference());
	}

}
