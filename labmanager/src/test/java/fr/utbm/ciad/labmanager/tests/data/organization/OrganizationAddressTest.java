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

package fr.utbm.ciad.labmanager.tests.data.organization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.organization.OrganizationAddress;
import fr.utbm.ciad.labmanager.utils.ranking.QuartileRanking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for {@link OrganizationAddress}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class OrganizationAddressTest {

	private OrganizationAddress test;

	@BeforeEach
	public void setUp() {
		this.test = new OrganizationAddress();
	}

	@Test
	public void getName() {
		assertNull(this.test.getName());
	}

	@Test
	public void setName() {
		assertNull(this.test.getName());

		this.test.setName("xyz");
		assertEquals("xyz", this.test.getName());

		this.test.setName(null);
		assertNull(this.test.getName());

		this.test.setName("abc");
		assertEquals("abc", this.test.getName());

		this.test.setName("");
		assertNull(this.test.getName());
	}

	@Test
	public void getComplement() {
		assertNull(this.test.getComplement());
	}

	@Test
	public void setComplement() {
		assertNull(this.test.getComplement());

		this.test.setComplement("xyz");
		assertEquals("xyz", this.test.getComplement());

		this.test.setComplement(null);
		assertNull(this.test.getComplement());

		this.test.setComplement("abc");
		assertEquals("abc", this.test.getComplement());

		this.test.setComplement("");
		assertNull(this.test.getComplement());
	}

	@Test
	public void getStreet() {
		assertNull(this.test.getStreet());
	}

	@Test
	public void setStreet() {
		assertNull(this.test.getStreet());

		this.test.setStreet("xyz");
		assertEquals("xyz", this.test.getStreet());

		this.test.setStreet(null);
		assertNull(this.test.getStreet());

		this.test.setStreet("abc");
		assertEquals("abc", this.test.getStreet());

		this.test.setStreet("");
		assertNull(this.test.getStreet());
	}

	@Test
	public void getZipCode() {
		assertNull(this.test.getZipCode());
	}

	@Test
	public void setZipCode() {
		assertNull(this.test.getZipCode());

		this.test.setZipCode("xyz");
		assertEquals("xyz", this.test.getZipCode());

		this.test.setZipCode(null);
		assertNull(this.test.getZipCode());

		this.test.setZipCode("abc");
		assertEquals("abc", this.test.getZipCode());

		this.test.setZipCode("");
		assertNull(this.test.getZipCode());
	}

	@Test
	public void getCity() {
		assertNull(this.test.getCity());
	}

	@Test
	public void setCity() {
		assertNull(this.test.getCity());

		this.test.setCity("xyz");
		assertEquals("xyz", this.test.getCity());

		this.test.setCity(null);
		assertNull(this.test.getCity());

		this.test.setCity("abc");
		assertEquals("abc", this.test.getCity());

		this.test.setCity("");
		assertNull(this.test.getCity());
	}

	@Test
	public void getMapCoordinates() {
		assertNull(this.test.getMapCoordinates());
	}

	@Test
	public void setMapCoordinates() {
		assertNull(this.test.getMapCoordinates());

		this.test.setMapCoordinates("xyz");
		assertEquals("xyz", this.test.getMapCoordinates());

		this.test.setMapCoordinates(null);
		assertNull(this.test.getMapCoordinates());

		this.test.setMapCoordinates("abc");
		assertEquals("abc", this.test.getMapCoordinates());

		this.test.setMapCoordinates("");
		assertNull(this.test.getMapCoordinates());
	}

	@Test
	public void getGoogleMapLink() {
		assertNull(this.test.getGoogleMapLink());
	}

	@Test
	public void setGoogleMapLink() {
		assertNull(this.test.getGoogleMapLink());

		this.test.setGoogleMapLink("xyz");
		assertEquals("xyz", this.test.getGoogleMapLink());

		this.test.setGoogleMapLink(null);
		assertNull(this.test.getGoogleMapLink());

		this.test.setGoogleMapLink("abc");
		assertEquals("abc", this.test.getGoogleMapLink());

		this.test.setGoogleMapLink("");
		assertNull(this.test.getMapCoordinates());
	}

	@Test
	public void getPathToBackgroundImage() {
		assertNull(this.test.getPathToBackgroundImage());
	}

	@Test
	public void setPathToBackgroundImage() {
		assertNull(this.test.getPathToBackgroundImage());

		this.test.setPathToBackgroundImage("xyz");
		assertEquals("xyz", this.test.getPathToBackgroundImage());

		this.test.setPathToBackgroundImage(null);
		assertNull(this.test.getPathToBackgroundImage());

		this.test.setPathToBackgroundImage("abc");
		assertEquals("abc", this.test.getPathToBackgroundImage());

		this.test.setPathToBackgroundImage("");
		assertNull(this.test.getPathToBackgroundImage());
	}

	private static OrganizationAddress createTransient1() {
		var e = new OrganizationAddress();
		e.setName("A");
		return e;
	}	

	private static OrganizationAddress createTransient2() {
		var e = new OrganizationAddress();
		e.setName("B");
		return e;
	}	

	private static OrganizationAddress createManaged1() {
		var e = createTransient1();
		e.setId(10l);
		return e;
	}	

	private static OrganizationAddress createManaged2() {
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
