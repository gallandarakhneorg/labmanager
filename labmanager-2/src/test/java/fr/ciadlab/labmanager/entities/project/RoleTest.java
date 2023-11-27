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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link Role}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class RoleTest {

	private List<Role> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(Role.values()));
	}

	private Role cons(Role status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void valueOfCaseInsensitive_String_special() {
		assertThrows(IllegalArgumentException.class, () -> Role.valueOfCaseInsensitive(null));
		assertThrows(IllegalArgumentException.class, () -> Role.valueOfCaseInsensitive(""));
	}

	@Test
	public void valueOfCaseInsensitive_String_upperCase() {
		assertEquals(cons(Role.PROJECT_COORDINATOR), Role.valueOfCaseInsensitive("PROJECT_COORDINATOR"));
		assertEquals(cons(Role.SCIENTIFIC_HEAD), Role.valueOfCaseInsensitive("SCIENTIFIC_HEAD"));
		assertEquals(cons(Role.WORK_PACKAGE_LEADER), Role.valueOfCaseInsensitive("WORK_PACKAGE_LEADER"));
		assertEquals(cons(Role.TASK_LEADER), Role.valueOfCaseInsensitive("TASK_LEADER"));
		assertEquals(cons(Role.PARTICIPANT), Role.valueOfCaseInsensitive("PARTICIPANT"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> Role.valueOfCaseInsensitive("XYZ"));
	}

	@Test
	public void valueOfCaseInsensitive_String_lowerCase() {
		assertEquals(cons(Role.PROJECT_COORDINATOR), Role.valueOfCaseInsensitive("project_coordinator"));
		assertEquals(cons(Role.SCIENTIFIC_HEAD), Role.valueOfCaseInsensitive("scientific_head"));
		assertEquals(cons(Role.WORK_PACKAGE_LEADER), Role.valueOfCaseInsensitive("work_package_leader"));
		assertEquals(cons(Role.TASK_LEADER), Role.valueOfCaseInsensitive("task_leader"));
		assertEquals(cons(Role.PARTICIPANT), Role.valueOfCaseInsensitive("participant"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> Role.valueOfCaseInsensitive("xyz"));
	}

	@Test
	public void getLabel_FR() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Project coordinator", cons(Role.PROJECT_COORDINATOR).getLabel());
		assertEquals("Scientific head", cons(Role.SCIENTIFIC_HEAD).getLabel());
		assertEquals("Work package leader", cons(Role.WORK_PACKAGE_LEADER).getLabel());
		assertEquals("Task leader", cons(Role.TASK_LEADER).getLabel());
		assertEquals("Participant", cons(Role.PARTICIPANT).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_US() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Project coordinator", cons(Role.PROJECT_COORDINATOR).getLabel());
		assertEquals("Scientific head", cons(Role.SCIENTIFIC_HEAD).getLabel());
		assertEquals("Work package leader", cons(Role.WORK_PACKAGE_LEADER).getLabel());
		assertEquals("Task leader", cons(Role.TASK_LEADER).getLabel());
		assertEquals("Participant", cons(Role.PARTICIPANT).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR() throws Exception {
		assertEquals("Porteur", cons(Role.PROJECT_COORDINATOR).getLabel(Locale.FRANCE));
		assertEquals("Responsable scientifique", cons(Role.SCIENTIFIC_HEAD).getLabel(Locale.FRANCE));
		assertEquals("Responsable de lot", cons(Role.WORK_PACKAGE_LEADER).getLabel(Locale.FRANCE));
		assertEquals("Responsable de tâche", cons(Role.TASK_LEADER).getLabel(Locale.FRANCE));
		assertEquals("Participant", cons(Role.PARTICIPANT).getLabel(Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() throws Exception {
		assertEquals("Project coordinator", cons(Role.PROJECT_COORDINATOR).getLabel(Locale.US));
		assertEquals("Scientific head", cons(Role.SCIENTIFIC_HEAD).getLabel(Locale.US));
		assertEquals("Work package leader", cons(Role.WORK_PACKAGE_LEADER).getLabel(Locale.US));
		assertEquals("Task leader", cons(Role.TASK_LEADER).getLabel(Locale.US));
		assertEquals("Participant", cons(Role.PARTICIPANT).getLabel(Locale.US));
		assertAllTreated();
	}

	@Test
	public void isHead() throws Exception {
		assertTrue(cons(Role.PROJECT_COORDINATOR).isHead());
		assertTrue(cons(Role.SCIENTIFIC_HEAD).isHead());
		assertTrue(cons(Role.WORK_PACKAGE_LEADER).isHead());
		assertTrue(cons(Role.TASK_LEADER).isHead());
		assertFalse(cons(Role.PARTICIPANT).isHead());
		assertAllTreated();
	}

}
