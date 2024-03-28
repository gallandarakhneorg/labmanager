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

package fr.utbm.ciad.labmanager.tests.data.jury;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import fr.utbm.ciad.labmanager.data.journal.Journal;
import fr.utbm.ciad.labmanager.data.jury.JuryMembership;
import fr.utbm.ciad.labmanager.data.jury.JuryMembershipType;
import fr.utbm.ciad.labmanager.data.jury.JuryType;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for {@link JuryMembership}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@SuppressWarnings("all")
public class JuryMembershipTest {

	private JuryMembership test;

	@BeforeEach
	public void setUp() {
		this.test = new JuryMembership();
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
	public void getType() {
		assertNull(this.test.getType());
	}

	@Test
	public void setType_JuryType() {
		assertNull(this.test.getType());

		this.test.setType(JuryMembershipType.PRESIDENT);
		assertSame(JuryMembershipType.PRESIDENT, this.test.getType());

		this.test.setType((JuryMembershipType) null);
		assertNull(this.test.getType());
	}

	@Test
	public void setType_String() {
		assertNull(this.test.getType());

		this.test.setType("Examiner");
		assertSame(JuryMembershipType.EXAMINER, this.test.getType());

		this.test.setType((String) null);
		assertNull(this.test.getType());

		this.test.setType("presidenT");
		assertSame(JuryMembershipType.PRESIDENT, this.test.getType());

		this.test.setType("");
		assertNull(this.test.getType());
	}

	@Test
	public void getDefenseType() {
		assertNull(this.test.getDefenseType());
	}

	@Test
	public void setDefenseType_JuryType() {
		assertNull(this.test.getDefenseType());

		this.test.setDefenseType(JuryType.MASTER);
		assertSame(JuryType.MASTER, this.test.getDefenseType());

		this.test.setDefenseType((JuryType) null);
		assertNull(this.test.getDefenseType());
	}

	@Test
	public void setDefenseType_String() {
		assertNull(this.test.getDefenseType());

		this.test.setDefenseType("Master");
		assertSame(JuryType.MASTER, this.test.getDefenseType());

		this.test.setDefenseType((String) null);
		assertNull(this.test.getDefenseType());

		this.test.setDefenseType("phD");
		assertSame(JuryType.PHD, this.test.getDefenseType());

		this.test.setDefenseType("");
		assertNull(this.test.getDefenseType());
	}

	@Test
	public void getDate() {
		assertNull(this.test.getDate());
	}

	@Test
	public void setDate() {
		assertNull(this.test.getDate());

		this.test.setDate(LocalDate.parse("2022-10-14"));
		assertEquals(LocalDate.parse("2022-10-14"), this.test.getDate());
	}

	@Test
	public void getUniversity() {
		assertNull(this.test.getUniversity());
	}

	@Test
	public void setUniversity() {
		assertNull(this.test.getUniversity());

		this.test.setUniversity("abc");
		assertEquals("abc", this.test.getUniversity());

		this.test.setUniversity(null);
		assertNull(this.test.getUniversity());

		this.test.setUniversity("xyz");
		assertEquals("xyz", this.test.getUniversity());

		this.test.setUniversity("");
		assertNull(this.test.getUniversity());
	}

	@Test
	public void getCountry() {
		assertSame(CountryCode.FRANCE, this.test.getCountry());
	}

	@Test
	public void setCountry_code() {
		this.test.setCountry(CountryCode.ALGERIA);
		assertSame(CountryCode.ALGERIA, this.test.getCountry());

		this.test.setCountry((CountryCode) null);
		assertSame(CountryCode.FRANCE, this.test.getCountry());
	}

	@Test
	public void setCountry_string() {
		this.test.setCountry("AlGERIA");
		assertSame(CountryCode.ALGERIA, this.test.getCountry());

		this.test.setCountry((String) null);
		assertSame(CountryCode.FRANCE, this.test.getCountry());
	}

	@Test
	public void getCandidate() {
		assertNull(this.test.getCandidate());
	}

	@Test
	public void setCandidate() {
		assertNull(this.test.getCandidate());

		Person p = mock(Person.class);
		this.test.setCandidate(p);
		assertSame(p, this.test.getCandidate());

		this.test.setCandidate(null);
		assertNull(this.test.getCandidate());
	}

	@Test
	public void getTitle() {
		assertNull(this.test.getTitle());
	}

	@Test
	public void setTitle() {
		assertNull(this.test.getTitle());

		this.test.setTitle("abc");
		assertEquals("abc", this.test.getTitle());

		this.test.setTitle(null);
		assertNull(this.test.getTitle());

		this.test.setTitle("xyz");
		assertEquals("xyz", this.test.getTitle());

		this.test.setTitle("");
		assertNull(this.test.getTitle());
	}

	@Test
	public void getPromoters() {
		assertNotNull(this.test.getPromoters());
		assertTrue(this.test.getPromoters().isEmpty());
	}

	@Test
	public void setPromoters() {
		assertNotNull(this.test.getPromoters());
		assertTrue(this.test.getPromoters().isEmpty());
		
		Person p0 = mock(Person.class);
		Person p1 = mock(Person.class);
		List<Person> list = Arrays.asList(p0, p1);

		this.test.setPromoters(list);
		assertNotNull(this.test.getPromoters());
		assertNotSame(list, this.test.getPromoters());
		assertEquals(2, this.test.getPromoters().size());
		assertTrue(this.test.getPromoters().contains(p0));
		assertTrue(this.test.getPromoters().contains(p1));
	}
	
	private final Person c1 = mock(Person.class);
	private final Person p1 = mock(Person.class);
	private final Person c2 = mock(Person.class);
	private final Person p2 = mock(Person.class);

	private JuryMembership createTransient1() {
		var e = new JuryMembership();
		e.setCandidate(this.c1);
		e.setDefenseType(JuryType.MASTER);
		e.setPerson(this.p1);
		return e;
	}	

	private JuryMembership createTransient2() {
		var e = new JuryMembership();
		e.setCandidate(this.c2);
		e.setDefenseType(JuryType.MASTER);
		e.setPerson(this.p2);
		return e;
	}	

	private JuryMembership createManaged1() {
		var e = createTransient1();
		e.setId(10l);
		return e;
	}	

	private JuryMembership createManaged2() {
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
