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

package fr.ciadlab.labmanager.entities.publication.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link MiscDocument}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class MiscDocumentTest {

	private MiscDocument test;

	@BeforeEach
	public void setUp() {
		this.test = new MiscDocument();
	}

	@Test
	public void isRanked() {
		assertFalse(this.test.isRanked());
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
	public void getDocumentType() {
		assertNull(this.test.getDocumentType());
	}

	@Test
	public void setDocumentType() {
		this.test.setDocumentType("xyz");
		assertEquals("xyz", this.test.getDocumentType());

		this.test.setDocumentType("");
		assertNull(this.test.getDocumentType());

		this.test.setDocumentType(null);
		assertNull(this.test.getDocumentType());
	}

	@Test
	public void getDocumentNumber() {
		assertNull(this.test.getDocumentNumber());
	}

	@Test
	public void setDocumentNumber() {
		this.test.setDocumentNumber("xyz");
		assertEquals("xyz", this.test.getDocumentNumber());

		this.test.setDocumentNumber("");
		assertNull(this.test.getDocumentNumber());

		this.test.setDocumentNumber(null);
		assertNull(this.test.getDocumentNumber());
	}

	@Test
	public void getHowPublished() {
		assertNull(this.test.getHowPublished());
	}

	@Test
	public void setHowPublished() {
		this.test.setHowPublished("xyz");
		assertEquals("xyz", this.test.getHowPublished());

		this.test.setHowPublished("");
		assertNull(this.test.getHowPublished());

		this.test.setHowPublished(null);
		assertNull(this.test.getHowPublished());
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

}
