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

package fr.utbm.ciad.labmanager.tests.data.invitation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;

import fr.utbm.ciad.labmanager.data.indicator.GlobalIndicators;
import fr.utbm.ciad.labmanager.data.invitation.PersonInvitation;
import fr.utbm.ciad.labmanager.data.invitation.PersonInvitationType;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for {@link PersonInvitation}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
@SuppressWarnings("all")
public class PersonInvitationTest {

	private PersonInvitation test;

	@BeforeEach
	public void setUp() {
		this.test = new PersonInvitation();
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
	public void setType_PersonInvitationType() {
		assertNull(this.test.getType());

		this.test.setType(PersonInvitationType.OUTGOING_GUEST);
		assertSame(PersonInvitationType.OUTGOING_GUEST, this.test.getType());

		this.test.setType((PersonInvitationType) null);
		assertNull(this.test.getType());
	}

	@Test
	public void setType_String() {
		assertNull(this.test.getType());

		this.test.setType("incoming_guest_professor");
		assertSame(PersonInvitationType.INCOMING_GUEST_PROFESSOR, this.test.getType());

		this.test.setType((String) null);
		assertNull(this.test.getType());

		this.test.setType("outgoing_guest");
		assertSame(PersonInvitationType.OUTGOING_GUEST, this.test.getType());

		this.test.setType("");
		assertNull(this.test.getType());
	}

	@Test
	public void getStartDate() {
		assertNull(this.test.getStartDate());
	}

	@Test
	public void setStartDate() {
		assertNull(this.test.getStartDate());

		this.test.setStartDate(LocalDate.parse("2022-10-14"));
		assertEquals(LocalDate.parse("2022-10-14"), this.test.getStartDate());
	}

	@Test
	public void getEndDate() {
		assertNull(this.test.getEndDate());
	}

	@Test
	public void setEndDate() {
		assertNull(this.test.getEndDate());

		this.test.setEndDate(LocalDate.parse("2022-10-14"));
		assertEquals(LocalDate.parse("2022-10-14"), this.test.getEndDate());
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
	public void getGuest() {
		assertNull(this.test.getGuest());
	}

	@Test
	public void setGuest() {
		assertNull(this.test.getGuest());

		Person p = mock(Person.class);
		this.test.setGuest(p);
		assertSame(p, this.test.getGuest());

		this.test.setGuest(null);
		assertNull(this.test.getGuest());
	}

	@Test
	public void getInviter() {
		assertNull(this.test.getInviter());
	}

	@Test
	public void setInviter() {
		assertNull(this.test.getInviter());

		Person p = mock(Person.class);
		this.test.setInviter(p);
		assertSame(p, this.test.getInviter());

		this.test.setInviter(null);
		assertNull(this.test.getInviter());
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

	private final Person g1 = mock(Person.class);
	private final Person i1 = mock(Person.class);
	private final LocalDate d1 = LocalDate.of(2023, 12, 22);
	private final Person g2 = mock(Person.class);
	private final Person i2 = mock(Person.class);
	private final LocalDate d2 = LocalDate.of(2023, 12, 22);
	
	private PersonInvitation createTransient1() {
		var e = new PersonInvitation();
		e.setGuest(this.g1);
		e.setInviter(this.g1);
		e.setStartDate(this.d1);
		return e;
	}	

	private PersonInvitation createTransient2() {
		var e = new PersonInvitation();
		e.setGuest(this.g2);
		e.setInviter(this.g2);
		e.setStartDate(this.d2);
		return e;
	}	

	private PersonInvitation createManaged1() {
		var e = createTransient1();
		e.setId(10l);
		return e;
	}	

	private PersonInvitation createManaged2() {
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
