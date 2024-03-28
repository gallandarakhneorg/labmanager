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

package fr.utbm.ciad.labmanager.tests.data.teaching;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.utbm.ciad.labmanager.configuration.messages.BaseMessageSource;
import fr.utbm.ciad.labmanager.data.teaching.TeacherRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link TeacherRole}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class TeacherRoleTest {

	private List<TeacherRole> items;
	
	private MessageSourceAccessor messages;

	@BeforeEach
	public void setUp() {
		this.messages = BaseMessageSource.getGlobalMessageAccessor();
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(TeacherRole.values()));
	}

	private TeacherRole cons(TeacherRole status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void valueOfCaseInsensitive_String_special() {
		assertThrows(IllegalArgumentException.class, () -> TeacherRole.valueOfCaseInsensitive(null));
		assertThrows(IllegalArgumentException.class, () -> TeacherRole.valueOfCaseInsensitive(""));
	}

	@Test
	public void valueOfCaseInsensitive_String_upperCase() {
		assertEquals(cons(TeacherRole.CREATOR), TeacherRole.valueOfCaseInsensitive("CREATOR"));
		assertEquals(cons(TeacherRole.SUPERVISOR), TeacherRole.valueOfCaseInsensitive("SUPERVISOR"));
		assertEquals(cons(TeacherRole.PARTICIPANT), TeacherRole.valueOfCaseInsensitive("PARTICIPANT"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> TeacherRole.valueOfCaseInsensitive("XYZ"));
	}

	@Test
	public void valueOfCaseInsensitive_String_lowerCase() {
		assertEquals(cons(TeacherRole.CREATOR), TeacherRole.valueOfCaseInsensitive("creator"));
		assertEquals(cons(TeacherRole.SUPERVISOR), TeacherRole.valueOfCaseInsensitive("supervisor"));
		assertEquals(cons(TeacherRole.PARTICIPANT), TeacherRole.valueOfCaseInsensitive("participant"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> TeacherRole.valueOfCaseInsensitive("xyz"));
	}

	@Test
	public void getLabel_Locale_US() throws Exception {
		assertEquals("Creator and supervisor", cons(TeacherRole.CREATOR).getLabel(this.messages, Locale.US));
		assertEquals("Supervisor", cons(TeacherRole.SUPERVISOR).getLabel(this.messages, Locale.US));
		assertEquals("Participant", cons(TeacherRole.PARTICIPANT).getLabel(this.messages, Locale.US));
		assertAllTreated();
	}

}
