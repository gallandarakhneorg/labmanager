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
import fr.utbm.ciad.labmanager.data.project.ProjectContractType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link ProjectContractType}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ProjectContractTypeTest {

	private List<ProjectContractType> items;

	private MessageSourceAccessor messages;

	@BeforeEach
	public void setUp() {
		this.messages = BaseMessageSource.getGlobalMessageAccessor();
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(ProjectContractType.values()));
	}

	private ProjectContractType cons(ProjectContractType status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void valueOfCaseInsensitive_String_special() {
		assertThrows(IllegalArgumentException.class, () -> ProjectContractType.valueOfCaseInsensitive(null));
		assertThrows(IllegalArgumentException.class, () -> ProjectContractType.valueOfCaseInsensitive(""));
	}

	@Test
	public void valueOfCaseInsensitive_String_upperCase() {
		assertEquals(cons(ProjectContractType.NOT_SPECIFIED), ProjectContractType.valueOfCaseInsensitive("NOT_SPECIFIED"));
		assertEquals(cons(ProjectContractType.RCO), ProjectContractType.valueOfCaseInsensitive("RCO"));
		assertEquals(cons(ProjectContractType.RCD), ProjectContractType.valueOfCaseInsensitive("RCD"));
		assertEquals(cons(ProjectContractType.PR), ProjectContractType.valueOfCaseInsensitive("PR"));
		assertEquals(cons(ProjectContractType.PI), ProjectContractType.valueOfCaseInsensitive("PI"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> ProjectContractType.valueOfCaseInsensitive("XYZ"));
	}

	@Test
	public void valueOfCaseInsensitive_String_lowerCase() {
		assertEquals(cons(ProjectContractType.NOT_SPECIFIED), ProjectContractType.valueOfCaseInsensitive("not_specified"));
		assertEquals(cons(ProjectContractType.RCO), ProjectContractType.valueOfCaseInsensitive("rco"));
		assertEquals(cons(ProjectContractType.RCD), ProjectContractType.valueOfCaseInsensitive("rcd"));
		assertEquals(cons(ProjectContractType.PR), ProjectContractType.valueOfCaseInsensitive("pr"));
		assertEquals(cons(ProjectContractType.PI), ProjectContractType.valueOfCaseInsensitive("pi"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> ProjectContractType.valueOfCaseInsensitive("xyz"));
	}

	@Test
	public void getLabel_Locale_US() throws Exception {
		assertEquals("Unspecified type", cons(ProjectContractType.NOT_SPECIFIED).getLabel(this.messages, Locale.US));
		assertEquals("Contract for collaborative contractual research, funded by public bodies", cons(ProjectContractType.RCO).getLabel(this.messages, Locale.US));
		assertEquals("Contract for direct contractual research, with shared IP or CIFRE", cons(ProjectContractType.RCD).getLabel(this.messages, Locale.US));
		assertEquals("Contract for contractual services, with transfer of IP", cons(ProjectContractType.PR).getLabel(this.messages, Locale.US));
		assertEquals("Contract dedicated to IP transfer", cons(ProjectContractType.PI).getLabel(this.messages, Locale.US));
		assertAllTreated();
	}

}
