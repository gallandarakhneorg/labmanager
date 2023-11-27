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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link ProjectMember}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ProjectMemberTest {

	private ProjectMember test;

	@BeforeEach
	public void setUp() {
		this.test = new ProjectMember();
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
		assertSame(Role.PARTICIPANT, this.test.getRole());
	}

	@Test
	public void setRole_Role_null() {
		this.test.setRole((Role) null);
		assertSame(Role.PARTICIPANT, this.test.getRole());
	}

	@Test
	public void setRole_Role() {
		this.test.setRole(Role.PROJECT_COORDINATOR);
		assertSame(Role.PROJECT_COORDINATOR, this.test.getRole());

		this.test.setRole(Role.SCIENTIFIC_HEAD);
		assertSame(Role.SCIENTIFIC_HEAD, this.test.getRole());
	}

	@Test
	public void setRole_String_null() {
		this.test.setRole(Role.PROJECT_COORDINATOR);
		this.test.setRole((String) null);
		assertSame(Role.PARTICIPANT, this.test.getRole());
	}

	@Test
	public void setRole_String_empty() {
		this.test.setRole(Role.PROJECT_COORDINATOR);
		this.test.setRole("");
		assertSame(Role.PARTICIPANT, this.test.getRole());
	}

	@Test
	public void setRole_String() {
		this.test.setRole("PROJECT_COORDINATOR");
		assertSame(Role.PROJECT_COORDINATOR, this.test.getRole());

		this.test.setRole("scientific_head");
		assertSame(Role.SCIENTIFIC_HEAD, this.test.getRole());
	}

}
