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

package fr.ciadlab.labmanager.entities.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.utils.funding.FundingScheme;
import org.junit.jupiter.api.BeforeEach;
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

}
