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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructureHolder;
import fr.utbm.ciad.labmanager.data.assostructure.HolderRole;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import org.junit.jupiter.api.BeforeEach;
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
	public void getPerson() {
		assertNull(this.test.getPerson());
	}

	@Test
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
	public void getRole() {
		assertSame(HolderRole.SCIENTIFIC_HEAD, this.test.getRole());
	}

	@Test
	public void setRole_Role_null() {
		this.test.setRole((HolderRole) null);
		assertSame(HolderRole.SCIENTIFIC_HEAD, this.test.getRole());
	}

	@Test
	public void setRole_Role() {
		this.test.setRole(HolderRole.STRUCTURE_HEAD);
		assertSame(HolderRole.STRUCTURE_HEAD, this.test.getRole());

		this.test.setRole(HolderRole.SCIENTIFIC_HEAD);
		assertSame(HolderRole.SCIENTIFIC_HEAD, this.test.getRole());
	}

	@Test
	public void setRole_String_null() {
		this.test.setRole(HolderRole.STRUCTURE_HEAD);
		this.test.setRole((String) null);
		assertSame(HolderRole.SCIENTIFIC_HEAD, this.test.getRole());
	}

	@Test
	public void setRole_String_empty() {
		this.test.setRole(HolderRole.STRUCTURE_HEAD);
		this.test.setRole("");
		assertSame(HolderRole.SCIENTIFIC_HEAD, this.test.getRole());
	}

	@Test
	public void setRole_String() {
		this.test.setRole("STRUCTURE_HEAD");
		assertSame(HolderRole.STRUCTURE_HEAD, this.test.getRole());

		this.test.setRole("scientific_head");
		assertSame(HolderRole.SCIENTIFIC_HEAD, this.test.getRole());
	}

	@Test
	public void getRoleDescription() {
		assertNull(this.test.getRoleDescription());
	}

	@Test
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
	public void getOrganization() {
		assertNull(this.test.getOrganization());
	}

	@Test
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
	public void getSuperOrganization() {
		assertNull(this.test.getSuperOrganization());
	}

	@Test
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

}
