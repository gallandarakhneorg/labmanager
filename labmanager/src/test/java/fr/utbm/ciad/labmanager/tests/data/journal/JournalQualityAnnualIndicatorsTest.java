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

package fr.utbm.ciad.labmanager.tests.data.journal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.utbm.ciad.labmanager.data.invitation.PersonInvitation;
import fr.utbm.ciad.labmanager.data.journal.JournalQualityAnnualIndicators;
import fr.utbm.ciad.labmanager.utils.ranking.QuartileRanking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for {@link JournalQualityAnnualIndicators}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class JournalQualityAnnualIndicatorsTest {

	private JournalQualityAnnualIndicators test;

	@BeforeEach
	public void setUp() {
		this.test = new JournalQualityAnnualIndicators();
	}

	@Test
	public void getReferenceYear() {
		assertEquals(0, this.test.getReferenceYear());
	}

	@Test
	public void setReferenceYear() {
		assertEquals(0, this.test.getReferenceYear());

		this.test.setReferenceYear(1234);
		assertEquals(1234, this.test.getReferenceYear());

		this.test.setReferenceYear(0);
		assertEquals(0, this.test.getReferenceYear());

		this.test.setReferenceYear(-56884);
		assertEquals(-56884, this.test.getReferenceYear());
	}

	@Test
	public void getScimagoQIndex() {
		assertNull(this.test.getScimagoQIndex());
	}

	@Test
	public void setScimagoQIndex_enum() {
		assertNull(this.test.getScimagoQIndex());

		this.test.setScimagoQIndex(QuartileRanking.Q2);
		assertSame(QuartileRanking.Q2, this.test.getScimagoQIndex());

		this.test.setScimagoQIndex((QuartileRanking) null);
		assertNull(this.test.getScimagoQIndex());
	}

	@Test
	public void setScimagoQIndex_string() {
		assertNull(this.test.getScimagoQIndex());

		this.test.setScimagoQIndex("Q2");
		assertSame(QuartileRanking.Q2, this.test.getScimagoQIndex());

		this.test.setScimagoQIndex((String) null);
		assertNull(this.test.getScimagoQIndex());
	}

	@Test
	public void getWosQIndex() {
		assertNull(this.test.getWosQIndex());
	}

	@Test
	public void setWosQIndex_enum() {
		assertNull(this.test.getWosQIndex());

		this.test.setWosQIndex(QuartileRanking.Q2);
		assertSame(QuartileRanking.Q2, this.test.getWosQIndex());

		this.test.setWosQIndex((QuartileRanking) null);
		assertNull(this.test.getWosQIndex());
	}

	@Test
	public void setWosQIndex_string() {
		assertNull(this.test.getWosQIndex());

		this.test.setWosQIndex("Q2");
		assertSame(QuartileRanking.Q2, this.test.getWosQIndex());

		this.test.setWosQIndex((String) null);
		assertNull(this.test.getWosQIndex());
	}

	@Test
	public void getImpactFactor() {
		assertEquals(0f, this.test.getImpactFactor());
	}

	@Test
	public void setImpactFactor_float() {
		assertEquals(0f, this.test.getImpactFactor());

		this.test.setImpactFactor(12.56f);
		assertEquals(12.56f, this.test.getImpactFactor());

		this.test.setImpactFactor(-458f);
		assertEquals(0f, this.test.getImpactFactor());
	}

	@Test
	public void setImpactFactor_string() {
		assertEquals(0f, this.test.getImpactFactor());

		this.test.setImpactFactor(Float.valueOf(12.56f));
		assertEquals(12.56f, this.test.getImpactFactor());

		this.test.setImpactFactor(Float.valueOf(-458f));
		assertEquals(0f, this.test.getImpactFactor());
	}

	private static JournalQualityAnnualIndicators createTransient1() {
		var e = new JournalQualityAnnualIndicators();
		e.setReferenceYear(2017);
		return e;
	}	

	private static JournalQualityAnnualIndicators createTransient2() {
		var e = new JournalQualityAnnualIndicators();
		e.setReferenceYear(2019);
		return e;
	}	

	private static JournalQualityAnnualIndicators createManaged1() {
		var e = createTransient1();
		e.setId(10l);
		return e;
	}	

	private static JournalQualityAnnualIndicators createManaged2() {
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
