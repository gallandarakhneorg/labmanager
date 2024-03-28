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

package fr.utbm.ciad.labmanager.tests.data.indicator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fr.utbm.ciad.labmanager.data.conference.Conference;
import fr.utbm.ciad.labmanager.data.indicator.GlobalIndicators;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests for {@link GlobalIndicators}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class GlobalIndicatorsTest {

	private GlobalIndicators test;

	@BeforeEach
	public void setUp() {
		this.test = new GlobalIndicators();
	}

	@Test
	public void getId() {
		assertEquals(0, this.test.getId());
	}

	@Test
	public void setId() {
		assertEquals(0, this.test.getId());

		this.test.setId(1478);
		assertEquals(1478, this.test.getId());

		this.test.setId(0);
		assertEquals(0, this.test.getId());

		this.test.setId(-568);
		assertEquals(-568, this.test.getId());
	}

	@Test
	public void getVisibleIndicatorKeys() {
		assertNull(this.test.getVisibleIndicatorKeys());
	}

	@Test
	public void setVisibleIndicatorKeys() {
		assertNull(this.test.getVisibleIndicatorKeys());

		this.test.setVisibleIndicatorKeys("abc,xyz");
		assertEquals("abc,xyz", this.test.getVisibleIndicatorKeys());

		this.test.setVisibleIndicatorKeys("");
		assertNull(this.test.getVisibleIndicatorKeys());

		this.test.setVisibleIndicatorKeys("uvw,nop");
		assertEquals("uvw,nop", this.test.getVisibleIndicatorKeys());

		this.test.setVisibleIndicatorKeys(null);
		assertNull(this.test.getVisibleIndicatorKeys());
	}

	@Test
	public void getVisibleIndicatorKeyList_noset() {
		assertTrue(this.test.getVisibleIndicatorKeyList().isEmpty());
	}

	@Test
	public void setVisibleIndicators_set() {
		this.test.setVisibleIndicatorKeys("abc,xyz");
		List<String> keys0 = this.test.getVisibleIndicatorKeyList();
		assertEquals(2, keys0.size());
		assertTrue(keys0.contains("xyz"));
		assertTrue(keys0.contains("abc"));

		this.test.setVisibleIndicatorKeys("");
		assertTrue(this.test.getVisibleIndicatorKeyList().isEmpty());

		this.test.setVisibleIndicatorKeys("uvw,nop");
		List<String> keys1 = this.test.getVisibleIndicatorKeyList();
		assertEquals(2, keys1.size());
		assertTrue(keys1.contains("nop"));
		assertTrue(keys1.contains("uvw"));

		this.test.setVisibleIndicatorKeys(null);
		assertTrue(this.test.getVisibleIndicatorKeyList().isEmpty());
	}

	private static GlobalIndicators createTransient1() {
		var e = new GlobalIndicators();
		return e;
	}	

	private static GlobalIndicators createTransient2() {
		var e = new GlobalIndicators();
		return e;
	}	

	private static GlobalIndicators createManaged1() {
		var e = createTransient1();
		e.setId(10l);
		return e;
	}	

	private static GlobalIndicators createManaged2() {
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
		assertTrue(t1.equals(t2));
	}

	@Test
	@DisplayName("t2.equals(t1)")
	public void test_equals_5() {
		// 2 transient entities need to be NOT equal
		var t1 = createTransient1();
		var t2 = createTransient2();
		assertTrue(t2.equals(t1));
	}

	@Test
	@DisplayName("m1.equals(m2)")
	public void test_equals_6() {
		// 2 managed entities that represent different records need to be NOT equal
		var m1 = createManaged1();
		var m2 = createManaged2();
		assertTrue(m1.equals(m2));
	}

	@Test
	@DisplayName("m2.equals(m1)")
	public void test_equals_7() {
		// 2 managed entities that represent different records need to be NOT equal
		var m1 = createManaged1();
		var m2 = createManaged2();
		assertTrue(m2.equals(m1));
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
		assertTrue(m1.equals(t2));
	}

	@Test
	@DisplayName("t2.equals(m1)")
	public void test_equals_13() {
		var t2 = createTransient2();
		var m1 = createManaged1();
		assertTrue(t2.equals(m1));
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
	@DisplayName("t1.hashCode == m1.hashCode")
	public void test_hashCode_2() {
		var t1 = createTransient1();
		var m1 = createManaged1();
		assertEquals(t1.hashCode(), m1.hashCode());
	}

}
