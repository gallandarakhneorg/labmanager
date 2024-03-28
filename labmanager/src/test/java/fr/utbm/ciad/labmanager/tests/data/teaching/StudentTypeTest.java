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
import fr.utbm.ciad.labmanager.data.teaching.StudentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link StudentType}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class StudentTypeTest {

	private List<StudentType> items;

	private MessageSourceAccessor messages;

	@BeforeEach
	public void setUp() {
		this.messages = BaseMessageSource.getGlobalMessageAccessor();
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(StudentType.values()));
	}

	private StudentType cons(StudentType status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void valueOfCaseInsensitive_String_special() {
		assertThrows(IllegalArgumentException.class, () -> StudentType.valueOfCaseInsensitive(null));
		assertThrows(IllegalArgumentException.class, () -> StudentType.valueOfCaseInsensitive(""));
	}

	@Test
	public void valueOfCaseInsensitive_String_upperCase() {
		assertEquals(cons(StudentType.INITIAL_TRAINING), StudentType.valueOfCaseInsensitive("INITIAL_TRAINING"));
		assertEquals(cons(StudentType.APPRENTICESHIP), StudentType.valueOfCaseInsensitive("APPRENTICESHIP"));
		assertEquals(cons(StudentType.CONTINUOUS), StudentType.valueOfCaseInsensitive("CONTINUOUS"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> StudentType.valueOfCaseInsensitive("XYZ"));
	}

	@Test
	public void valueOfCaseInsensitive_String_lowerCase() {
		assertEquals(cons(StudentType.INITIAL_TRAINING), StudentType.valueOfCaseInsensitive("initial_training"));
		assertEquals(cons(StudentType.APPRENTICESHIP), StudentType.valueOfCaseInsensitive("apprenticeship"));
		assertEquals(cons(StudentType.CONTINUOUS), StudentType.valueOfCaseInsensitive("continuous"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> StudentType.valueOfCaseInsensitive("xyz"));
	}

	@Test
	public void getLabel_Locale_US() throws Exception {
		assertEquals("Initial Training", cons(StudentType.INITIAL_TRAINING).getLabel(this.messages, Locale.US));
		assertEquals("Apprenticeship or Dual Training", cons(StudentType.APPRENTICESHIP).getLabel(this.messages, Locale.US));
		assertEquals("Continuing Education", cons(StudentType.CONTINUOUS).getLabel(this.messages, Locale.US));
		assertAllTreated();
	}

}
