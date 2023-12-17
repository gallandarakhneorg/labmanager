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
import fr.utbm.ciad.labmanager.data.project.ProjectStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link ProjectStatus}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ProjectStatusTest {

	private List<ProjectStatus> items;

	private MessageSourceAccessor messages;

	@BeforeEach
	public void setUp() {
		this.messages = BaseMessageSource.getGlobalMessageAccessor();
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(ProjectStatus.values()));
	}

	private ProjectStatus cons(ProjectStatus status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void valueOfCaseInsensitive_String_special() {
		assertThrows(IllegalArgumentException.class, () -> ProjectStatus.valueOfCaseInsensitive(null));
		assertThrows(IllegalArgumentException.class, () -> ProjectStatus.valueOfCaseInsensitive(""));
	}

	@Test
	public void valueOfCaseInsensitive_String_upperCase() {
		assertEquals(cons(ProjectStatus.PREPARATION), ProjectStatus.valueOfCaseInsensitive("PREPARATION"));
		assertEquals(cons(ProjectStatus.EVALUATION), ProjectStatus.valueOfCaseInsensitive("EVALUATION"));
		assertEquals(cons(ProjectStatus.CANCELED), ProjectStatus.valueOfCaseInsensitive("CANCELED"));
		assertEquals(cons(ProjectStatus.REJECTED), ProjectStatus.valueOfCaseInsensitive("REJECTED"));
		assertEquals(cons(ProjectStatus.ACCEPTED), ProjectStatus.valueOfCaseInsensitive("ACCEPTED"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> ProjectStatus.valueOfCaseInsensitive("XYZ"));
	}

	@Test
	public void valueOfCaseInsensitive_String_lowerCase() {
		assertEquals(cons(ProjectStatus.PREPARATION), ProjectStatus.valueOfCaseInsensitive("preparation"));
		assertEquals(cons(ProjectStatus.EVALUATION), ProjectStatus.valueOfCaseInsensitive("evaluation"));
		assertEquals(cons(ProjectStatus.CANCELED), ProjectStatus.valueOfCaseInsensitive("canceled"));
		assertEquals(cons(ProjectStatus.REJECTED), ProjectStatus.valueOfCaseInsensitive("rejected"));
		assertEquals(cons(ProjectStatus.ACCEPTED), ProjectStatus.valueOfCaseInsensitive("accepted"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> ProjectStatus.valueOfCaseInsensitive("xyz"));
	}

	@Test
	public void getLabel_Locale_US() throws Exception {
		assertEquals("Under preparation", cons(ProjectStatus.PREPARATION).getLabel(this.messages, Locale.US));
		assertEquals("Under evaluation", cons(ProjectStatus.EVALUATION).getLabel(this.messages, Locale.US));
		assertEquals("Canceled", cons(ProjectStatus.CANCELED).getLabel(this.messages, Locale.US));
		assertEquals("Rejected", cons(ProjectStatus.REJECTED).getLabel(this.messages, Locale.US));
		assertEquals("Accepted", cons(ProjectStatus.ACCEPTED).getLabel(this.messages, Locale.US));
		assertAllTreated();
	}

}
