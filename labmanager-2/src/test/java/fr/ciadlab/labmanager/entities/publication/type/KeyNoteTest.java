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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link KeyNote}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class KeyNoteTest {

	private KeyNote test;

	@BeforeEach
	public void setUp() {
		this.test = new KeyNote();
	}

	@Test
	public void isRanked() {
		assertFalse(this.test.isRanked());
	}

	@Test
	public void getScientificEventName() {
		assertNull(this.test.getScientificEventName());
	}

	@Test
	public void setScientificEventName() {
		this.test.setScientificEventName("xyz");
		assertEquals("xyz", this.test.getScientificEventName());

		this.test.setScientificEventName("");
		assertNull(this.test.getScientificEventName());

		this.test.setScientificEventName(null);
		assertNull(this.test.getScientificEventName());
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

}
