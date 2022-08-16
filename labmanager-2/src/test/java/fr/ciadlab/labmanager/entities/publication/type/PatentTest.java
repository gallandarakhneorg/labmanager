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

/** Tests for {@link Patent}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class PatentTest {

	private Patent test;

	@BeforeEach
	public void setUp() {
		this.test = new Patent();
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
	public void getPatentType() {
		assertNull(this.test.getPatentType());
	}

	@Test
	public void setPatentType() {
		this.test.setPatentType("xyz");
		assertEquals("xyz", this.test.getPatentType());

		this.test.setPatentType("");
		assertNull(this.test.getPatentType());

		this.test.setPatentType(null);
		assertNull(this.test.getPatentType());
	}

	@Test
	public void getPatentNumber() {
		assertNull(this.test.getPatentNumber());
	}

	@Test
	public void setPatentNumber() {
		this.test.setPatentNumber("xyz");
		assertEquals("xyz", this.test.getPatentNumber());

		this.test.setPatentNumber("");
		assertNull(this.test.getPatentNumber());

		this.test.setPatentNumber(null);
		assertNull(this.test.getPatentNumber());
	}

}
