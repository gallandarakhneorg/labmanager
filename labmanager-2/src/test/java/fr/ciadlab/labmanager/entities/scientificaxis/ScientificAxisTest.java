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

package fr.ciadlab.labmanager.entities.scientificaxis;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import fr.ciadlab.labmanager.entities.member.Membership;
import org.arakhne.afc.util.CountryCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link ScientificAxis}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ScientificAxisTest {

	private ScientificAxis test;

	@BeforeEach
	public void setUp() {
		this.test = new ScientificAxis();
	}

	@Test
	public void getId() {
		assertEquals(0, this.test.getId());
	}

	@Test
	public void setId() {
		assertEquals(0, this.test.getId());

		this.test.setId(-1234);
		assertEquals(-1234, this.test.getId());

		this.test.setId(0);
		assertEquals(0, this.test.getId());

		this.test.setId(4789);
		assertEquals(4789, this.test.getId());
	}

	@Test
	public void getAcronym() {
		assertNull(this.test.getAcronym());
	}

	@Test
	public void setAcronym() {
		this.test.setAcronym("xyz");
		assertEquals("xyz", this.test.getAcronym());

		this.test.setAcronym("");
		assertNull(this.test.getAcronym());

		this.test.setAcronym(null);
		assertNull(this.test.getAcronym());
	}

	@Test
	public void getName() {
		assertNull(this.test.getName());
	}

	@Test
	public void setName() {
		this.test.setName("xyz");
		assertEquals("xyz", this.test.getName());

		this.test.setName("");
		assertNull(this.test.getName());

		this.test.setName("abc");
		assertEquals("abc", this.test.getName());

		this.test.setName(null);
		assertNull(this.test.getName());
	}

	@Test
	public void getStartDate() {
		assertNull(this.test.getStartDate());
	}

	@Test
	public void setStartDate_LocalDate() {
		this.test.setStartDate(LocalDate.of(2023, 2, 6));
		assertEquals(LocalDate.of(2023, 2, 6), this.test.getStartDate());

		this.test.setStartDate((LocalDate) null);
		assertNull(this.test.getStartDate());

		this.test.setStartDate(LocalDate.of(2018, 12, 25));
		assertEquals(LocalDate.of(2018, 12, 25), this.test.getStartDate());
	}

	@Test
	public void setStartDate_String() {
		this.test.setStartDate("2023-02-06");
		assertEquals(LocalDate.of(2023, 2, 6), this.test.getStartDate());

		this.test.setStartDate("2018-12-25");
		assertEquals(LocalDate.of(2018, 12, 25), this.test.getStartDate());
	}

	@Test
	public void setStartDate_String_null() {
		this.test.setStartDate("2023-02-06");
		this.test.setStartDate((String) null);
		assertNull(this.test.getStartDate());
	}

	@Test
	public void setStartDate_String_empty() {
		this.test.setStartDate("2023-02-06");
		this.test.setStartDate("");
		assertNull(this.test.getStartDate());
	}

	@Test
	public void getEndDate() {
		assertNull(this.test.getEndDate());
	}

	@Test
	public void setEndDate_LocalDate() {
		this.test.setEndDate(LocalDate.of(2023, 2, 6));
		assertEquals(LocalDate.of(2023, 2, 6), this.test.getEndDate());

		this.test.setEndDate((LocalDate) null);
		assertNull(this.test.getEndDate());

		this.test.setEndDate(LocalDate.of(2018, 12, 25));
		assertEquals(LocalDate.of(2018, 12, 25), this.test.getEndDate());
	}

	@Test
	public void setEndDate_String() {
		this.test.setEndDate("2023-02-06");
		assertEquals(LocalDate.of(2023, 2, 6), this.test.getEndDate());

		this.test.setEndDate("2018-12-25");
		assertEquals(LocalDate.of(2018, 12, 25), this.test.getEndDate());
	}

	@Test
	public void setEndDate_String_null() {
		this.test.setEndDate("2023-02-06");
		this.test.setEndDate((String) null);
		assertNull(this.test.getEndDate());
	}

	@Test
	public void setEndDate_String_empty() {
		this.test.setEndDate("2023-02-06");
		this.test.setEndDate("");
		assertNull(this.test.getEndDate());
	}

}
