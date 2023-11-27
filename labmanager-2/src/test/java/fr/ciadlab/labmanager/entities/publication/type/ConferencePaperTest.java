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

package fr.ciadlab.labmanager.entities.publication.type;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import static org.mockito.Mockito.*;

import fr.ciadlab.labmanager.entities.conference.Conference;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link ConferencePaper}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ConferencePaperTest extends AbstractTypedPublicationTest<ConferencePaper> {

	@Override
	protected ConferencePaper createTest() {
		return new ConferencePaper();
	}

	@Override
	protected ConferencePaper createTest(Publication prePublication) {
		return new ConferencePaper(prePublication, null, 0, null, null, null, null, null, null, null);
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
	public void getConference() {
		assertNull(this.test.getConference());
	}

	@Test
	public void setConference() {
		Conference conf = mock(Conference.class);
		this.test.setConference(conf);
		assertSame(conf, this.test.getConference());
	}

	@Test
	public void getConferenceOccurrenceNumber() {
		assertEquals(0, this.test.getConferenceOccurrenceNumber());
	}

	@Test
	public void setConferenceOccurrenceNumber() {
		this.test.setConferenceOccurrenceNumber(1234);
		assertEquals(1234, this.test.getConferenceOccurrenceNumber());
	}

	@Test
	public void getOrganization() {
		assertNull(this.test.getOrganization());
	}

	@Test
	public void setOrganization() {
		this.test.setOrganization("xyz");
		assertEquals("xyz", this.test.getOrganization());

		this.test.setOrganization("");
		assertNull(this.test.getOrganization());

		this.test.setOrganization(null);
		assertNull(this.test.getOrganization());
	}

	@Test
	public void getCoreRanking() {
		assertSame(CoreRanking.NR, this.test.getCoreRanking());
	}

}
