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

package fr.utbm.ciad.labmanager.tests.data.assostructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructureHolder;
import fr.utbm.ciad.labmanager.data.assostructure.HolderRole;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for {@link AssociatedStructureHolder}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class AssociatedStructureHolderTest {

	private AssociatedStructureHolder test;

	@BeforeEach
	public void setUp() {
		this.test = new AssociatedStructureHolder();
	}

	@Test
	@DisplayName("getId")
	public void getId() {
		assertEquals(0, this.test.getId());
	}

	@Test
	@DisplayName("setId")
	public void setId() {
		this.test.setId(12547);
		assertEquals(12547, this.test.getId());
		this.test.setId(9251568);
		assertEquals(9251568, this.test.getId());
	}

	@Test
	@DisplayName("getPerson")
	public void getPerson() {
		assertNull(this.test.getPerson());
	}

	@Test
	@DisplayName("setPerson")
	public void setPerson() {
		Person expected0 = mock(Person.class);
		this.test.setPerson(expected0);
		assertSame(expected0, this.test.getPerson());

		this.test.setPerson(null);
		assertNull(this.test.getPerson());

		Person expected1 = mock(Person.class);
		this.test.setPerson(expected1);
		assertSame(expected1, this.test.getPerson());
	}

	@Test
	@DisplayName("getRole")
	public void getRole() {
		assertSame(HolderRole.SCIENTIFIC_HEAD, this.test.getRole());
	}

	@Test
	@DisplayName("setRole((HolderRole) null)")
	public void setRole_Role_null() {
		this.test.setRole((HolderRole) null);
		assertSame(HolderRole.SCIENTIFIC_HEAD, this.test.getRole());
	}

	@Test
	@DisplayName("setRole((HolderRole) r)")
	public void setRole_Role() {
		this.test.setRole(HolderRole.STRUCTURE_HEAD);
		assertSame(HolderRole.STRUCTURE_HEAD, this.test.getRole());

		this.test.setRole(HolderRole.SCIENTIFIC_HEAD);
		assertSame(HolderRole.SCIENTIFIC_HEAD, this.test.getRole());
	}

	@Test
	@DisplayName("setRole((String) null)")
	public void setRole_String_null() {
		this.test.setRole(HolderRole.STRUCTURE_HEAD);
		this.test.setRole((String) null);
		assertSame(HolderRole.SCIENTIFIC_HEAD, this.test.getRole());
	}

	@Test
	@DisplayName("setRole(\"\")")
	public void setRole_String_empty() {
		this.test.setRole(HolderRole.STRUCTURE_HEAD);
		this.test.setRole("");
		assertSame(HolderRole.SCIENTIFIC_HEAD, this.test.getRole());
	}

	@Test
	@DisplayName("setRole((String) r)")
	public void setRole_String() {
		this.test.setRole("STRUCTURE_HEAD");
		assertSame(HolderRole.STRUCTURE_HEAD, this.test.getRole());

		this.test.setRole("scientific_head");
		assertSame(HolderRole.SCIENTIFIC_HEAD, this.test.getRole());
	}

	@Test
	@DisplayName("getRoleDescription")
	public void getRoleDescription() {
		assertNull(this.test.getRoleDescription());
	}

	@Test
	@DisplayName("setRoleDescription")
	public void setRoleDescription() {
		this.test.setRoleDescription("xyz");
		assertEquals("xyz", this.test.getRoleDescription());

		this.test.setRoleDescription(null);
		assertNull(this.test.getRoleDescription());

		this.test.setRoleDescription("abc");
		assertEquals("abc", this.test.getRoleDescription());

		this.test.setRoleDescription("");
		assertNull(this.test.getRoleDescription());
	}

	@Test
	@DisplayName("getOrganization")
	public void getOrganization() {
		assertNull(this.test.getOrganization());
	}

	@Test
	@DisplayName("setOrganization")
	public void setOrganization() {
		ResearchOrganization expected0 = mock(ResearchOrganization.class);
		this.test.setOrganization(expected0);
		assertSame(expected0, this.test.getOrganization());

		this.test.setOrganization(null);
		assertNull(this.test.getOrganization());

		ResearchOrganization expected1 = mock(ResearchOrganization.class);
		this.test.setOrganization(expected1);
		assertSame(expected1, this.test.getOrganization());
	}

	@Test
	@DisplayName("getSuperOrganization")
	public void getSuperOrganization() {
		assertNull(this.test.getSuperOrganization());
	}

	@Test
	@DisplayName("setSuperOrganization")
	public void setSuperOrganization() {
		ResearchOrganization expected0 = mock(ResearchOrganization.class);
		this.test.setSuperOrganization(expected0);
		assertSame(expected0, this.test.getSuperOrganization());

		this.test.setSuperOrganization(null);
		assertNull(this.test.getSuperOrganization());

		ResearchOrganization expected1 = mock(ResearchOrganization.class);
		this.test.setSuperOrganization(expected1);
		assertSame(expected1, this.test.getSuperOrganization());
	}
	
	private final Person p1 = mock(Person.class);
	private final Person p2 = mock(Person.class);

	private AssociatedStructureHolder createTransient1() {
		var e = new AssociatedStructureHolder();
		e.setPerson(this.p1);
		return e;
	}	

	private AssociatedStructureHolder createTransient2() {
		var e = new AssociatedStructureHolder();
		e.setPerson(this.p2);
		return e;
	}	

	private AssociatedStructureHolder createManaged1() {
		var e = createTransient1();
		e.setId(10l);
		return e;
	}	

	private AssociatedStructureHolder createManaged2() {
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
