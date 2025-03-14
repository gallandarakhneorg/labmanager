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

package fr.utbm.ciad.labmanager.tests.data.scientificaxis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.project.Project;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.data.publication.type.BookChapter;
import fr.utbm.ciad.labmanager.data.scientificaxis.ScientificAxis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

	@Test
	public void isActiveAt_noStart_noEnd() {
		final LocalDate ld = LocalDate.of(2022, 5, 5);
		assertTrue(this.test.isActiveAt(ld));
	}

	@Test
	public void isActiveAt_noStart() {
		final LocalDate ld = LocalDate.of(2022, 5, 5);
		//
		this.test.setEndDate(LocalDate.of(2021, 5, 5));
		assertFalse(this.test.isActiveAt(ld));
		//
		this.test.setEndDate(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setEndDate(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
	}

	@Test
	public void isActiveAt_noEnd() {
		final LocalDate ld = LocalDate.of(2022, 5, 5);
		//
		this.test.setStartDate(LocalDate.of(2021, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setStartDate(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setStartDate(LocalDate.of(2023, 5, 5));
		assertFalse(this.test.isActiveAt(ld));
	}

	@Test
	public void isActiveAt() {
		final LocalDate ld = LocalDate.of(2022, 5, 5);
		//
		this.test.setStartDate(LocalDate.of(2021, 5, 5));
		this.test.setEndDate(LocalDate.of(2021, 5, 5));
		assertFalse(this.test.isActiveAt(ld));
		//
		this.test.setStartDate(LocalDate.of(2021, 5, 5));
		this.test.setEndDate(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setStartDate(LocalDate.of(2021, 5, 5));
		this.test.setEndDate(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setStartDate(LocalDate.of(2022, 5, 5));
		this.test.setEndDate(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setStartDate(LocalDate.of(2022, 5, 5));
		this.test.setEndDate(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setStartDate(LocalDate.of(2023, 5, 5));
		this.test.setEndDate(LocalDate.of(2023, 5, 5));
		assertFalse(this.test.isActiveAt(ld));
	}


	@Test
	public void isActiveIn_noStart_noEnd() {
		final LocalDate s = LocalDate.of(2022, 1, 1);
		final LocalDate e = LocalDate.of(2022, 12, 31);
		assertTrue(this.test.isActiveIn(s, e));
	}

	@Test
	public void isActiveIn_noStart() {
		final LocalDate s = LocalDate.of(2022, 1, 1);
		final LocalDate e = LocalDate.of(2022, 12, 31);
		//
		this.test.setEndDate(LocalDate.of(2021, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
		//
		this.test.setEndDate(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setEndDate(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setEndDate(LocalDate.of(2022, 1, 1));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setEndDate(LocalDate.of(2021, 12, 31));
		assertFalse(this.test.isActiveIn(s, e));
	}

	@Test
	public void isActiveIn_noEnd() {
		final LocalDate s = LocalDate.of(2022, 1, 1);
		final LocalDate e = LocalDate.of(2022, 12, 31);
		//
		this.test.setStartDate(LocalDate.of(2021, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setStartDate(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setStartDate(LocalDate.of(2023, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
		//
		this.test.setStartDate(LocalDate.of(2022, 12, 31));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setStartDate(LocalDate.of(2023, 01, 01));
		assertFalse(this.test.isActiveIn(s, e));
	}

	@Test
	public void isActiveIn() {
		final LocalDate s = LocalDate.of(2022, 1, 1);
		final LocalDate e = LocalDate.of(2022, 12, 31);
		//
		this.test.setStartDate(LocalDate.of(2021, 5, 5));
		this.test.setEndDate(LocalDate.of(2021, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
		//
		this.test.setStartDate(LocalDate.of(2021, 5, 5));
		this.test.setEndDate(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setStartDate(LocalDate.of(2021, 5, 5));
		this.test.setEndDate(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setStartDate(LocalDate.of(2022, 5, 5));
		this.test.setEndDate(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setStartDate(LocalDate.of(2022, 5, 5));
		this.test.setEndDate(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setStartDate(LocalDate.of(2023, 5, 5));
		this.test.setEndDate(LocalDate.of(2023, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
	}

	@Test
	public void getProjects() {
		assertTrue(this.test.getProjects().isEmpty());
	}

	@Test
	public void setProjects() {
		Set<ScientificAxis> l0 = new HashSet<>();
		Project p0 = mock(Project.class);
		when(p0.getScientificAxes()).thenReturn(l0);
		Set<ScientificAxis> l1 = new HashSet<>();
		Project p1 = mock(Project.class);
		when(p1.getScientificAxes()).thenReturn(l1);
		List<Project> prjs = Arrays.asList(p0, p1);

		this.test.setProjects(prjs);

		Set<Project> actual = this.test.getProjects();
		assertNotNull(actual);
		assertEquals(2, actual.size());
		assertTrue(actual.contains(p0));
		assertTrue(actual.contains(p1));

		assertFalse(l0.contains(this.test));
		assertFalse(l1.contains(this.test));
	}

	@Test
	public void getPublications() {
		assertTrue(this.test.getPublications().isEmpty());
	}

	@Test
	public void setPublications() {
		Set<ScientificAxis> l0 = new HashSet<>();
		Publication p0 = mock(Publication.class);
		when(p0.getScientificAxes()).thenReturn(l0);
		Set<ScientificAxis> l1 = new HashSet<>();
		Publication p1 = mock(Publication.class);
		when(p1.getScientificAxes()).thenReturn(l1);
		List<Publication> pubs = Arrays.asList(p0, p1);

		this.test.setPublications(pubs);

		Set<Publication> actual = this.test.getPublications();
		assertNotNull(actual);
		assertEquals(2, actual.size());
		assertTrue(actual.contains(p0));
		assertTrue(actual.contains(p1));

		assertFalse(l0.contains(this.test));
		assertFalse(l1.contains(this.test));
	}

	@Test
	public void getMemberships() {
		assertTrue(this.test.getMemberships().isEmpty());
	}

	@Test
	public void setMemberships() {
		Set<ScientificAxis> l0 = new HashSet<>();
		Membership m0 = mock(Membership.class);
		when(m0.getScientificAxes()).thenReturn(l0);
		Set<ScientificAxis> l1 = new HashSet<>();
		Membership m1 = mock(Membership.class);
		when(m1.getScientificAxes()).thenReturn(l1);
		List<Membership> mbrs = Arrays.asList(m0, m1);

		this.test.setMemberships(mbrs);

		Set<Membership> actual = this.test.getMemberships();
		assertNotNull(actual);
		assertEquals(2, actual.size());
		assertTrue(actual.contains(m0));
		assertTrue(actual.contains(m1));

		assertFalse(l0.contains(this.test));
		assertFalse(l1.contains(this.test));
	}

	private static ScientificAxis createTransient1() {
		var e = new ScientificAxis();
		e.setAcronym("A");
		return e;
	}	

	private static ScientificAxis createTransient2() {
		var e = new ScientificAxis();
		e.setAcronym("B");
		return e;
	}	

	private static ScientificAxis createManaged1() {
		var e = createTransient1();
		e.setId(10l);
		return e;
	}	

	private static ScientificAxis createManaged2() {
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
