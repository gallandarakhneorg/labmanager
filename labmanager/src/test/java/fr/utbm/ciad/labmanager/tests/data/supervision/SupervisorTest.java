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

package fr.utbm.ciad.labmanager.tests.data.supervision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.supervision.Supervision;
import fr.utbm.ciad.labmanager.data.supervision.Supervisor;
import fr.utbm.ciad.labmanager.data.supervision.SupervisorType;
import fr.utbm.ciad.labmanager.utils.funding.FundingScheme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for {@link Supervisor}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.1
 */@SuppressWarnings("all")
 public class SupervisorTest {

	 private Supervisor test;

	 @BeforeEach
	 public void setUp() {
		 this.test = new Supervisor();
	 }

	 @Test
	 public void getId() {
		 assertEquals(0, this.test.getId());
	 }

	 @Test
	 public void setId() {
		 this.test.setId(123456);
		 assertEquals(123456, this.test.getId());
	 }

	 @Test
	 public void getPercentage() {
		 assertEquals(0, this.test.getPercentage());
	 }

	 @Test
	 public void setPercentage() {
		 this.test.setPercentage((byte) -1);
		 assertEquals(0, this.test.getPercentage());

		 this.test.setPercentage((byte) 0);
		 assertEquals(0, this.test.getPercentage());

		 this.test.setPercentage((byte) 105);
		 assertEquals(100, this.test.getPercentage());

		 this.test.setPercentage((byte) 64);
		 assertEquals(64, this.test.getPercentage());
	 }

	 @Test
	 public void getType() {
		 assertSame(SupervisorType.SUPERVISOR, this.test.getType());
	 }

	 @Test
	 public void setType_SupervisorType() {
		 this.test.setType(SupervisorType.DIRECTOR);
		 assertSame(SupervisorType.DIRECTOR, this.test.getType());

		 this.test.setType((SupervisorType) null);
		 assertSame(SupervisorType.SUPERVISOR, this.test.getType());
	 }

	 @Test
	 public void setType_String() {
		 this.test.setType("Director");
		 assertSame(SupervisorType.DIRECTOR, this.test.getType());

		 this.test.setType((String) null);
		 assertSame(SupervisorType.SUPERVISOR, this.test.getType());

		 this.test.setType("DireCtor");
		 assertSame(SupervisorType.DIRECTOR, this.test.getType());

		 this.test.setType("");
		 assertSame(SupervisorType.SUPERVISOR, this.test.getType());
	 }

	 @Test
	 public void getSupervisor() {
		 assertNull(this.test.getSupervisor());
	 }

	 @Test
	 public void setSupervisor() {
		 assertNull(this.test.getSupervisor());

		 Person s0 = mock(Person.class);
		 this.test.setSupervisor(s0);
		 assertSame(s0, this.test.getSupervisor());
	 }

	 private final Person s1 = mock(Person.class);
	 private final Person s2 = mock(Person.class);
	 
	 private Supervisor createTransient1() {
		 var e = new Supervisor();
		 e.setSupervisor(this.s1);
		 return e;
	 }	

	 private Supervisor createTransient2() {
		 var e = new Supervisor();
		 e.setSupervisor(this.s2);
		 return e;
	 }	

	 private Supervisor createManaged1() {
		 var e = createTransient1();
		 e.setId(10l);
		 return e;
	 }	

	 private Supervisor createManaged2() {
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