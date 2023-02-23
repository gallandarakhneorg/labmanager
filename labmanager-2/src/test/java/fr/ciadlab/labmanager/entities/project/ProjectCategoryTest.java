/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
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
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link ProjectCategory}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ProjectCategoryTest {

	private List<ProjectCategory> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(ProjectCategory.values()));
	}

	private ProjectCategory cons(ProjectCategory status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void valueOfCaseInsensitive_String_special() {
		assertThrows(IllegalArgumentException.class, () -> ProjectCategory.valueOfCaseInsensitive(null));
		assertThrows(IllegalArgumentException.class, () -> ProjectCategory.valueOfCaseInsensitive(""));
	}

	@Test
	public void valueOfCaseInsensitive_String_upperCase() {
		assertEquals(cons(ProjectCategory.COMPETITIVE_CALL_PROJECT), ProjectCategory.valueOfCaseInsensitive("COMPETITIVE_CALL_PROJECT"));
		assertEquals(cons(ProjectCategory.NOT_ACADEMIC_PROJECT), ProjectCategory.valueOfCaseInsensitive("NOT_ACADEMIC_PROJECT"));
		assertEquals(cons(ProjectCategory.OPEN_SOURCE), ProjectCategory.valueOfCaseInsensitive("OPEN_SOURCE"));
		assertEquals(cons(ProjectCategory.AUTO_FUNDING), ProjectCategory.valueOfCaseInsensitive("AUTO_FUNDING"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> ProjectCategory.valueOfCaseInsensitive("XYZ"));
	}

	@Test
	public void valueOfCaseInsensitive_String_lowerCase() {
		assertEquals(cons(ProjectCategory.COMPETITIVE_CALL_PROJECT), ProjectCategory.valueOfCaseInsensitive("competitive_call_project"));
		assertEquals(cons(ProjectCategory.NOT_ACADEMIC_PROJECT), ProjectCategory.valueOfCaseInsensitive("not_academic_project"));
		assertEquals(cons(ProjectCategory.OPEN_SOURCE), ProjectCategory.valueOfCaseInsensitive("open_source"));
		assertEquals(cons(ProjectCategory.AUTO_FUNDING), ProjectCategory.valueOfCaseInsensitive("auto_funding"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> ProjectCategory.valueOfCaseInsensitive("xyz"));
	}

	@Test
	public void getLabel_FR() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Projects in competitive calls or with public funding", cons(ProjectCategory.COMPETITIVE_CALL_PROJECT).getLabel());
		assertEquals("Projects with not academic or socio-economic partners", cons(ProjectCategory.NOT_ACADEMIC_PROJECT).getLabel());
		assertEquals("Open Source projects or freely available on Internet", cons(ProjectCategory.OPEN_SOURCE).getLabel());
		assertEquals("Auto-funding projects", cons(ProjectCategory.AUTO_FUNDING).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_US() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Projects in competitive calls or with public funding", cons(ProjectCategory.COMPETITIVE_CALL_PROJECT).getLabel());
		assertEquals("Projects with not academic or socio-economic partners", cons(ProjectCategory.NOT_ACADEMIC_PROJECT).getLabel());
		assertEquals("Open Source projects or freely available on Internet", cons(ProjectCategory.OPEN_SOURCE).getLabel());
		assertEquals("Auto-funding projects", cons(ProjectCategory.AUTO_FUNDING).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR() throws Exception {
		assertEquals("Projets dans des appels compétitifs ou avec financement public", cons(ProjectCategory.COMPETITIVE_CALL_PROJECT).getLabel(Locale.FRANCE));
		assertEquals("Projets avec des partenaires non académiques ou socio-économiques", cons(ProjectCategory.NOT_ACADEMIC_PROJECT).getLabel(Locale.FRANCE));
		assertEquals("Projets Open Source ou disponibles librement sur Internet", cons(ProjectCategory.OPEN_SOURCE).getLabel(Locale.FRANCE));
		assertEquals("Projets auto-financés", cons(ProjectCategory.AUTO_FUNDING).getLabel(Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() throws Exception {
		assertEquals("Projects in competitive calls or with public funding", cons(ProjectCategory.COMPETITIVE_CALL_PROJECT).getLabel(Locale.US));
		assertEquals("Projects with not academic or socio-economic partners", cons(ProjectCategory.NOT_ACADEMIC_PROJECT).getLabel(Locale.US));
		assertEquals("Open Source projects or freely available on Internet", cons(ProjectCategory.OPEN_SOURCE).getLabel(Locale.US));
		assertEquals("Auto-funding projects", cons(ProjectCategory.AUTO_FUNDING).getLabel(Locale.US));
		assertAllTreated();
	}

	@Test
	public void reverseOrdinal() throws Exception {
		assertEquals(0, cons(ProjectCategory.COMPETITIVE_CALL_PROJECT).reverseOrdinal());
		assertEquals(1, cons(ProjectCategory.NOT_ACADEMIC_PROJECT).reverseOrdinal());
		assertEquals(2, cons(ProjectCategory.AUTO_FUNDING).reverseOrdinal());
		assertEquals(3, cons(ProjectCategory.OPEN_SOURCE).reverseOrdinal());
		assertAllTreated();
	}

	@Test
	public void isContractualProject() throws Exception {
		assertTrue(cons(ProjectCategory.COMPETITIVE_CALL_PROJECT).isContractualProject());
		assertTrue(cons(ProjectCategory.NOT_ACADEMIC_PROJECT).isContractualProject());
		assertFalse(cons(ProjectCategory.AUTO_FUNDING).isContractualProject());
		assertFalse(cons(ProjectCategory.OPEN_SOURCE).isContractualProject());
		assertAllTreated();
	}

}
