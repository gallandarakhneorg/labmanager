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

package fr.utbm.ciad.labmanager.tests.data.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.project.ProjectBudget;
import fr.utbm.ciad.labmanager.utils.funding.FundingScheme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for {@link ProjectBudget}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ProjectBudgetTest {

	private ProjectBudget test;

	@BeforeEach
	public void setUp() {
		this.test = new ProjectBudget();
	}

	@Test
	public void getId() {
		assertEquals(0, this.test.getId());
	}

	@Test
	public void setId() {
		this.test.setId(12547);
		assertEquals(12547, this.test.getId());
		this.test.setId(9251568);
		assertEquals(9251568, this.test.getId());
	}

	@Test
	public void getBudget() {
		assertEquals(0f, this.test.getBudget());
	}

	@Test
	public void setBudget_float() {
		this.test.setBudget(123.456f);
		assertEquals(123.456f, this.test.getBudget());

		this.test.setBudget(-123.456f);
		assertEquals(0f, this.test.getBudget());
	}

	@Test
	public void setBudget_Float_null() {
		this.test.setBudget(null);
		assertEquals(0f, this.test.getBudget());
	}

	@Test
	public void setBudget_Float() {
		this.test.setBudget(Float.valueOf(123.456f));
		assertEquals(123.456f, this.test.getBudget());

		this.test.setBudget(Float.valueOf(-123.456f));
		assertEquals(0f, this.test.getBudget());
	}

	@Test
	public void getFundingScheme() {
		assertSame(FundingScheme.NOT_FUNDED, this.test.getFundingScheme());
	}

	@Test
	public void setFundingScheme_FundingScheme_null() {
		this.test.setFundingScheme(FundingScheme.CIFRE);
		this.test.setFundingScheme((FundingScheme) null);
		assertSame(FundingScheme.NOT_FUNDED, this.test.getFundingScheme());
	}

	@Test
	public void setFundingScheme_FundingScheme() {
		this.test.setFundingScheme(FundingScheme.CIFRE);
		assertSame(FundingScheme.CIFRE, this.test.getFundingScheme());
	}

	@Test
	public void setFundingScheme_String_null() {
		this.test.setFundingScheme(FundingScheme.CIFRE);
		this.test.setFundingScheme((String) null);
		assertSame(FundingScheme.NOT_FUNDED, this.test.getFundingScheme());
	}

	@Test
	public void setFundingScheme_String_empty() {
		this.test.setFundingScheme(FundingScheme.CIFRE);
		this.test.setFundingScheme("");
		assertSame(FundingScheme.NOT_FUNDED, this.test.getFundingScheme());
	}

	@Test
	public void setFundingScheme_String_valid() {
		this.test.setFundingScheme("cifre");
		assertSame(FundingScheme.CIFRE, this.test.getFundingScheme());
	}

	@Test
	public void setFundingScheme_String_invalid() {
		this.test.setFundingScheme("xyz");
		assertSame(FundingScheme.NOT_FUNDED, this.test.getFundingScheme());
	}

	@Test
	public void getGrant() {
		assertNull(this.test.getGrant());
	}

	@Test
	public void setGrant_null() {
		this.test.setGrant("xyz");
		this.test.setGrant(null);
		assertNull(this.test.getGrant());
	}

	@Test
	public void setGrant_empty() {
		this.test.setGrant("xyz");
		this.test.setGrant("");
		assertNull(this.test.getGrant());
	}

	@Test
	public void setGrant() {
		this.test.setGrant("xyz");
		assertEquals("xyz", this.test.getGrant());
	}

	private static ProjectBudget createTransient1() {
		var e = new ProjectBudget();
		e.setFundingScheme(FundingScheme.ANR);
		return e;
	}	

	private static ProjectBudget createTransient2() {
		var e = new ProjectBudget();
		e.setFundingScheme(FundingScheme.CARNOT);
		return e;
	}	

	private static ProjectBudget createManaged1() {
		var e = createTransient1();
		e.setId(10l);
		return e;
	}	

	private static ProjectBudget createManaged2() {
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
