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
import fr.utbm.ciad.labmanager.data.teaching.TeachingActivityLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link TeachingActivityLevel}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class TeachingActivityLevelTest {

	private List<TeachingActivityLevel> items;

	private MessageSourceAccessor messages;

	@BeforeEach
	public void setUp() {
		this.messages = BaseMessageSource.getGlobalMessageAccessor();
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(TeachingActivityLevel.values()));
	}

	private TeachingActivityLevel cons(TeachingActivityLevel status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void valueOfCaseInsensitive_String_special() {
		assertThrows(IllegalArgumentException.class, () -> TeachingActivityLevel.valueOfCaseInsensitive(null));
		assertThrows(IllegalArgumentException.class, () -> TeachingActivityLevel.valueOfCaseInsensitive(""));
	}

	@Test
	public void valueOfCaseInsensitive_String_upperCase() {
		assertEquals(cons(TeachingActivityLevel.DOCTORAL_DEGREE), TeachingActivityLevel.valueOfCaseInsensitive("DOCTORAL_DEGREE"));
		assertEquals(cons(TeachingActivityLevel.MASTER_DEGREE), TeachingActivityLevel.valueOfCaseInsensitive("MASTER_DEGREE"));
		assertEquals(cons(TeachingActivityLevel.BACHELOR_DEGREE), TeachingActivityLevel.valueOfCaseInsensitive("BACHELOR_DEGREE"));
		assertEquals(cons(TeachingActivityLevel.HIGH_SCHOOL_DEGREE), TeachingActivityLevel.valueOfCaseInsensitive("HIGH_SCHOOL_DEGREE"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> TeachingActivityLevel.valueOfCaseInsensitive("XYZ"));
	}

	@Test
	public void valueOfCaseInsensitive_String_lowerCase() {
		assertEquals(cons(TeachingActivityLevel.DOCTORAL_DEGREE), TeachingActivityLevel.valueOfCaseInsensitive("doctoral_degree"));
		assertEquals(cons(TeachingActivityLevel.MASTER_DEGREE), TeachingActivityLevel.valueOfCaseInsensitive("master_degree"));
		assertEquals(cons(TeachingActivityLevel.BACHELOR_DEGREE), TeachingActivityLevel.valueOfCaseInsensitive("bachelor_degree"));
		assertEquals(cons(TeachingActivityLevel.HIGH_SCHOOL_DEGREE), TeachingActivityLevel.valueOfCaseInsensitive("high_school_degree"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> TeachingActivityLevel.valueOfCaseInsensitive("xyz"));
	}

	@Test
	public void getLabel_Locale_US() throws Exception {
		assertEquals("Doctoral Degree", cons(TeachingActivityLevel.DOCTORAL_DEGREE).getLabel(this.messages, Locale.US));
		assertEquals("Master Degree", cons(TeachingActivityLevel.MASTER_DEGREE).getLabel(this.messages, Locale.US));
		assertEquals("Bachelor Degree", cons(TeachingActivityLevel.BACHELOR_DEGREE).getLabel(this.messages, Locale.US));
		assertEquals("High-School Degree", cons(TeachingActivityLevel.HIGH_SCHOOL_DEGREE).getLabel(this.messages, Locale.US));
		assertAllTreated();
	}

}
