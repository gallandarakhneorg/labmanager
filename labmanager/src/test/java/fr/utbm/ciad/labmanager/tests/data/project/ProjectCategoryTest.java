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
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.utbm.ciad.labmanager.configuration.messages.BaseMessageSource;
import fr.utbm.ciad.labmanager.data.project.ProjectCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

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

	private MessageSourceAccessor messages;

	@BeforeEach
	public void setUp() {
		this.messages = BaseMessageSource.getGlobalMessageAccessor();
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
	public void getLabel_Locale_US() throws Exception {
		assertEquals("Projects in competitive calls or with public funding", cons(ProjectCategory.COMPETITIVE_CALL_PROJECT).getLabel(this.messages, Locale.US));
		assertEquals("Projects with not academic or socio-economic partners", cons(ProjectCategory.NOT_ACADEMIC_PROJECT).getLabel(this.messages, Locale.US));
		assertEquals("Open Source projects or freely available on Internet", cons(ProjectCategory.OPEN_SOURCE).getLabel(this.messages, Locale.US));
		assertEquals("Auto-funding projects", cons(ProjectCategory.AUTO_FUNDING).getLabel(this.messages, Locale.US));
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
