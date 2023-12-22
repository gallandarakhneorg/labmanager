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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructure;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructureHolder;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructureType;
import fr.utbm.ciad.labmanager.data.assostructure.HolderRole;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.project.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for {@link AssociatedStructure}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class AssociatedStructureTest {

	private AssociatedStructure test;

	@BeforeEach
	public void setUp() {
		this.test = new AssociatedStructure();
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
	public void getAcronym() {
		assertNull(this.test.getAcronym());
	}

	@Test
	public void setAcronym_null() {
		this.test.setAcronym("xyz");
		this.test.setAcronym(null);
		assertNull(this.test.getAcronym());
	}

	@Test
	public void setAcronym_empty() {
		this.test.setAcronym("xyz");
		this.test.setAcronym("");
		assertNull(this.test.getAcronym());
	}

	@Test
	public void setAcronym() {
		this.test.setAcronym("xyz");
		assertEquals("xyz", this.test.getAcronym());
	}

	@Test
	public void getName() {
		assertNull(this.test.getName());
	}

	@Test
	public void setName_null() {
		this.test.setName("xyz");
		this.test.setName(null);
		assertNull(this.test.getName());
	}

	@Test
	public void setName_empty() {
		this.test.setName("xyz");
		this.test.setName("");
		assertNull(this.test.getName());
	}

	@Test
	public void setName() {
		this.test.setName("xyz");
		assertEquals("xyz", this.test.getName());
	}

	@Test
	public void getDescription() {
		assertNull(this.test.getDescription());
	}

	@Test
	public void setDescription_null() {
		this.test.setDescription("xyz");
		this.test.setDescription(null);
		assertNull(this.test.getDescription());
	}

	@Test
	public void setDescription_empty() {
		this.test.setDescription("xyz");
		this.test.setDescription("");
		assertNull(this.test.getDescription());
	}

	@Test
	public void setDescription() {
		this.test.setDescription("xyz");
		assertEquals("xyz", this.test.getDescription());
	}

	@Test
	public void getCreationDate() {
		assertNull(this.test.getCreationDate());
	}

	@Test
	public void setCreationDate_LocalDate_null() {
		this.test.setCreationDate(LocalDate.of(2023, 7, 18));
		this.test.setCreationDate((LocalDate) null);
		assertNull(this.test.getCreationDate());
	}

	@Test
	public void setCreationDate_LocalDate() {
		this.test.setCreationDate(LocalDate.of(2023, 7, 18));
		assertEquals(LocalDate.of(2023, 7, 18), this.test.getCreationDate());
	}

	@Test
	public void setCreationDate_String_null() {
		this.test.setCreationDate(LocalDate.of(2023, 7, 18));
		this.test.setCreationDate((String) null);
		assertNull(this.test.getCreationDate());
	}

	@Test
	public void setCreationDate_String_empty() {
		this.test.setCreationDate(LocalDate.of(2023, 7, 18));
		this.test.setCreationDate((String) null);
		assertNull(this.test.getCreationDate());
	}

	@Test
	public void setCreationDate_String() {
		this.test.setCreationDate("2023-07-18");
		assertEquals(LocalDate.of(2023, 7, 18), this.test.getCreationDate());
	}

	@Test
	public void setCreationDate_String_invalid() {
		this.test.setCreationDate("xyz");
		assertNull(this.test.getCreationDate());
	}

	@Test
	public void getBudget() {
		assertEquals(0f, this.test.getBudget());
	}

	@Test
	public void setBudget_float() {
		this.test.setBudget(123.587f);
		assertEquals(123.587f, this.test.getBudget());

		this.test.setBudget(-159f);
		assertEquals(0f, this.test.getBudget());
	}

	@Test
	public void setBudget_Number_null() {
		this.test.setBudget(Float.valueOf(123.587f));
		this.test.setBudget(null);
		assertEquals(0f, this.test.getBudget());
	}

	@Test
	public void setBudget_Number() {
		this.test.setBudget(Float.valueOf(123.587f));
		assertEquals(123.587f, this.test.getBudget());

		this.test.setBudget(Float.valueOf(-159f));
		assertEquals(0f, this.test.getBudget());
	}

	@Test
	public void getType() {
		assertNull(this.test.getType());
	}

	@Test
	public void setType_AssociatedStructureType_null() {
		this.test.setType(AssociatedStructureType.NATIONAL_RESEARCH_LAB);
		this.test.setType((AssociatedStructureType) null);
		assertNull(this.test.getType());
	}

	@Test
	public void setType_AssociatedStructureType() {
		this.test.setType(AssociatedStructureType.NATIONAL_RESEARCH_LAB);
		assertSame(AssociatedStructureType.NATIONAL_RESEARCH_LAB, this.test.getType());

		this.test.setType(AssociatedStructureType.PRIVATE_COMPANY);
		assertSame(AssociatedStructureType.PRIVATE_COMPANY, this.test.getType());
	}

	@Test
	public void setType_String_null() {
		this.test.setType(AssociatedStructureType.NATIONAL_RESEARCH_LAB);
		this.test.setType((String) null);
		assertNull(this.test.getType());
	}

	@Test
	public void setType_String_empty() {
		this.test.setType(AssociatedStructureType.NATIONAL_RESEARCH_LAB);
		this.test.setType("");
		assertNull(this.test.getType());
	}

	@Test
	public void setType_String() {
		this.test.setType("national_research_lab");
		assertSame(AssociatedStructureType.NATIONAL_RESEARCH_LAB, this.test.getType());

		this.test.setType("private_company");
		assertSame(AssociatedStructureType.PRIVATE_COMPANY, this.test.getType());
	}

	@Test
	public void isConfidential() {
		assertFalse(this.test.isConfidential());
	}

	@Test
	public void setConfidential_boolean() {
		this.test.setConfidential(true);
		assertTrue(this.test.isConfidential());

		this.test.setConfidential(false);
		assertFalse(this.test.isConfidential());
	}

	@Test
	public void setConfidential_Boolean_null() {
		this.test.setConfidential(true);
		this.test.setConfidential(null);
		assertFalse(this.test.isConfidential());
	}

	@Test
	public void setConfidential_Boolean() {
		this.test.setConfidential(Boolean.TRUE);
		assertTrue(this.test.isConfidential());

		this.test.setConfidential(Boolean.FALSE);
		assertFalse(this.test.isConfidential());
	}

	@Test
	public void isValidated() {
		assertFalse(this.test.isValidated());
	}

	@Test
	public void setValidated_boolean() {
		this.test.setValidated(true);
		assertTrue(this.test.isValidated());

		this.test.setValidated(false);
		assertFalse(this.test.isValidated());
	}

	@Test
	public void setValidated_Boolean_null() {
		this.test.setValidated(true);
		this.test.setValidated(null);
		assertFalse(this.test.isValidated());
	}

	@Test
	public void setValidated_Boolean() {
		this.test.setValidated(Boolean.TRUE);
		assertTrue(this.test.isValidated());

		this.test.setValidated(Boolean.FALSE);
		assertFalse(this.test.isValidated());
	}

	@Test
	public void getFundingOrganization() {
		assertNull(this.test.getFundingOrganization());
	}

	@Test
	public void setFundingOrganization_null() {
		ResearchOrganization org0 = mock(ResearchOrganization.class);
		this.test.setFundingOrganization(org0);
		this.test.setFundingOrganization(null);
		assertNull(this.test.getFundingOrganization());
	}

	@Test
	public void setFundingOrganization() {
		ResearchOrganization org0 = mock(ResearchOrganization.class);
		this.test.setFundingOrganization(org0);
		assertSame(org0, this.test.getFundingOrganization());
	}

	@Test
	public void getHoldersRaw() {
		Set<AssociatedStructureHolder> actual = this.test.getHoldersRaw();
		assertTrue(actual.isEmpty());
	}

	@Test
	public void getHolders() {
		List<AssociatedStructureHolder> actual = this.test.getHolders();
		assertTrue(actual.isEmpty());
	}

	@Test
	public void setHolders_null() {
		AssociatedStructureHolder h0 = mock(AssociatedStructureHolder.class);
		AssociatedStructureHolder h1 = mock(AssociatedStructureHolder.class);
		List<AssociatedStructureHolder> holders = Arrays.asList(h0, h1);
		this.test.setHolders(holders);
		
		this.test.setHolders(null);

		List<AssociatedStructureHolder> actual = this.test.getHolders();
		assertNotSame(holders, actual);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void setHolders() {
		AssociatedStructureHolder h0 = mock(AssociatedStructureHolder.class);
		AssociatedStructureHolder h1 = mock(AssociatedStructureHolder.class);
		List<AssociatedStructureHolder> holders = Arrays.asList(h0, h1);
		this.test.setHolders(holders);
		
		List<AssociatedStructureHolder> actual = this.test.getHolders();
		assertNotSame(holders, actual);
		assertEquals(2, actual.size());
		assertTrue(actual.contains(h0));
		assertTrue(actual.contains(h1));
	}

	@Test
	public void getProjects() {
		assertTrue(this.test.getProjects().isEmpty());
	}

	@Test
	public void setProjects() {
		Project p0 = mock(Project.class);
		Project p1 = mock(Project.class);
		List<Project> projects = Arrays.asList(p0, p1);
		
		this.test.setProjects(projects);

		List<Project> actual = this.test.getProjects();
		assertEquals(2, actual.size());
		assertTrue(actual.contains(p0));
		assertTrue(actual.contains(p1));
	}
	
	private static AssociatedStructure createTransient1() {
		var e = new AssociatedStructure();
		e.setAcronym("A");
		return e;
	}	

	private static AssociatedStructure createTransient2() {
		var e = new AssociatedStructure();
		e.setAcronym("B");
		return e;
	}	

	private static AssociatedStructure createManaged1() {
		var e = createTransient1();
		e.setId(10l);
		return e;
	}	

	private static AssociatedStructure createManaged2() {
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
