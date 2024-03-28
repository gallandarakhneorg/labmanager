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
import fr.utbm.ciad.labmanager.data.teaching.PedagogicalPracticeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link PedagogicalPracticeType}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class PedagogicalPracticeTypeTest {

	private List<PedagogicalPracticeType> items;

	private MessageSourceAccessor messages;

	@BeforeEach
	public void setUp() {
		this.messages = BaseMessageSource.getGlobalMessageAccessor();
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(PedagogicalPracticeType.values()));
	}

	private PedagogicalPracticeType cons(PedagogicalPracticeType status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void valueOfCaseInsensitive_String_special() {
		assertThrows(IllegalArgumentException.class, () -> PedagogicalPracticeType.valueOfCaseInsensitive(null));
		assertThrows(IllegalArgumentException.class, () -> PedagogicalPracticeType.valueOfCaseInsensitive(""));
	}

	@Test
	public void valueOfCaseInsensitive_String_upperCase() {
		assertEquals(cons(PedagogicalPracticeType.COLLABORATIVE_WORKS), PedagogicalPracticeType.valueOfCaseInsensitive("COLLABORATIVE_WORKS"));
		assertEquals(cons(PedagogicalPracticeType.DISTANCE_LEARNING), PedagogicalPracticeType.valueOfCaseInsensitive("DISTANCE_LEARNING"));
		assertEquals(cons(PedagogicalPracticeType.HISTORICAL_METHOD), PedagogicalPracticeType.valueOfCaseInsensitive("HISTORICAL_METHOD"));
		assertEquals(cons(PedagogicalPracticeType.INVERTED_CLASSROOM), PedagogicalPracticeType.valueOfCaseInsensitive("INVERTED_CLASSROOM"));
		assertEquals(cons(PedagogicalPracticeType.MODERN_PRESENTATION_TOOLS), PedagogicalPracticeType.valueOfCaseInsensitive("MODERN_PRESENTATION_TOOLS"));
		assertEquals(cons(PedagogicalPracticeType.OTHER_METHOD), PedagogicalPracticeType.valueOfCaseInsensitive("OTHER_METHOD"));
		assertEquals(cons(PedagogicalPracticeType.PEDAGOGY_3_0), PedagogicalPracticeType.valueOfCaseInsensitive("PEDAGOGY_3_0"));
		assertEquals(cons(PedagogicalPracticeType.PROBLEM_BASED_LEARNING), PedagogicalPracticeType.valueOfCaseInsensitive("PROBLEM_BASED_LEARNING"));
		assertEquals(cons(PedagogicalPracticeType.PROJECT_ORIENTED), PedagogicalPracticeType.valueOfCaseInsensitive("PROJECT_ORIENTED"));
		assertEquals(cons(PedagogicalPracticeType.REVERSE_CLASS), PedagogicalPracticeType.valueOfCaseInsensitive("REVERSE_CLASS"));
		assertEquals(cons(PedagogicalPracticeType.SERIOUS_GAME), PedagogicalPracticeType.valueOfCaseInsensitive("SERIOUS_GAME"));
		assertEquals(cons(PedagogicalPracticeType.SOCIAL_NETWORK_LEARNING), PedagogicalPracticeType.valueOfCaseInsensitive("SOCIAL_NETWORK_LEARNING"));
		assertEquals(cons(PedagogicalPracticeType.VIRTUAL_CLASSROOM), PedagogicalPracticeType.valueOfCaseInsensitive("VIRTUAL_CLASSROOM"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> PedagogicalPracticeType.valueOfCaseInsensitive("XYZ"));
	}

	@Test
	public void valueOfCaseInsensitive_String_lowerCase() {
		assertEquals(cons(PedagogicalPracticeType.COLLABORATIVE_WORKS), PedagogicalPracticeType.valueOfCaseInsensitive("collaborative_works"));
		assertEquals(cons(PedagogicalPracticeType.DISTANCE_LEARNING), PedagogicalPracticeType.valueOfCaseInsensitive("distance_learning"));
		assertEquals(cons(PedagogicalPracticeType.HISTORICAL_METHOD), PedagogicalPracticeType.valueOfCaseInsensitive("historical_method"));
		assertEquals(cons(PedagogicalPracticeType.INVERTED_CLASSROOM), PedagogicalPracticeType.valueOfCaseInsensitive("inverted_classroom"));
		assertEquals(cons(PedagogicalPracticeType.MODERN_PRESENTATION_TOOLS), PedagogicalPracticeType.valueOfCaseInsensitive("modern_presentation_tools"));
		assertEquals(cons(PedagogicalPracticeType.OTHER_METHOD), PedagogicalPracticeType.valueOfCaseInsensitive("other_method"));
		assertEquals(cons(PedagogicalPracticeType.PEDAGOGY_3_0), PedagogicalPracticeType.valueOfCaseInsensitive("pedagogy_3_0"));
		assertEquals(cons(PedagogicalPracticeType.PROBLEM_BASED_LEARNING), PedagogicalPracticeType.valueOfCaseInsensitive("problem_based_learning"));
		assertEquals(cons(PedagogicalPracticeType.PROJECT_ORIENTED), PedagogicalPracticeType.valueOfCaseInsensitive("project_oriented"));
		assertEquals(cons(PedagogicalPracticeType.REVERSE_CLASS), PedagogicalPracticeType.valueOfCaseInsensitive("reverse_class"));
		assertEquals(cons(PedagogicalPracticeType.SERIOUS_GAME), PedagogicalPracticeType.valueOfCaseInsensitive("serious_game"));
		assertEquals(cons(PedagogicalPracticeType.SOCIAL_NETWORK_LEARNING), PedagogicalPracticeType.valueOfCaseInsensitive("social_network_learning"));
		assertEquals(cons(PedagogicalPracticeType.VIRTUAL_CLASSROOM), PedagogicalPracticeType.valueOfCaseInsensitive("virtual_classroom"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> PedagogicalPracticeType.valueOfCaseInsensitive("xyz"));
	}

	@Test
	public void getLabel_Locale_US() throws Exception {
		assertEquals("Collaborative works", cons(PedagogicalPracticeType.COLLABORATIVE_WORKS).getLabel(this.messages, Locale.US));
		assertEquals("Distance learning", cons(PedagogicalPracticeType.DISTANCE_LEARNING).getLabel(this.messages, Locale.US));
		assertEquals("Historical pedagogical method", cons(PedagogicalPracticeType.HISTORICAL_METHOD).getLabel(this.messages, Locale.US));
		assertEquals("Inverted classroom", cons(PedagogicalPracticeType.INVERTED_CLASSROOM).getLabel(this.messages, Locale.US));
		assertEquals("Use of modern presentation tools", cons(PedagogicalPracticeType.MODERN_PRESENTATION_TOOLS).getLabel(this.messages, Locale.US));
		assertEquals("Other method", cons(PedagogicalPracticeType.OTHER_METHOD).getLabel(this.messages, Locale.US));
		assertEquals("Pedagogy 3.0", cons(PedagogicalPracticeType.PEDAGOGY_3_0).getLabel(this.messages, Locale.US));
		assertEquals("Problem based learning", cons(PedagogicalPracticeType.PROBLEM_BASED_LEARNING).getLabel(this.messages, Locale.US));
		assertEquals("Project-oriented pedagogy", cons(PedagogicalPracticeType.PROJECT_ORIENTED).getLabel(this.messages, Locale.US));
		assertEquals("Reverse class", cons(PedagogicalPracticeType.REVERSE_CLASS).getLabel(this.messages, Locale.US));
		assertEquals("Serious game", cons(PedagogicalPracticeType.SERIOUS_GAME).getLabel(this.messages, Locale.US));
		assertEquals("Learning with social network", cons(PedagogicalPracticeType.SOCIAL_NETWORK_LEARNING).getLabel(this.messages, Locale.US));
		assertEquals("Virtual classroom", cons(PedagogicalPracticeType.VIRTUAL_CLASSROOM).getLabel(this.messages, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getDescription_Locale_US() throws Exception {
		assertNotNull(cons(PedagogicalPracticeType.COLLABORATIVE_WORKS).getDescription(this.messages, Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.DISTANCE_LEARNING).getDescription(this.messages, Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.HISTORICAL_METHOD).getDescription(this.messages, Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.INVERTED_CLASSROOM).getDescription(this.messages, Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.MODERN_PRESENTATION_TOOLS).getDescription(this.messages, Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.OTHER_METHOD).getDescription(this.messages, Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.PEDAGOGY_3_0).getDescription(this.messages, Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.PROBLEM_BASED_LEARNING).getDescription(this.messages, Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.PROJECT_ORIENTED).getDescription(this.messages, Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.REVERSE_CLASS).getDescription(this.messages, Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.SERIOUS_GAME).getDescription(this.messages, Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.SOCIAL_NETWORK_LEARNING).getDescription(this.messages, Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.VIRTUAL_CLASSROOM).getDescription(this.messages, Locale.US));
		assertAllTreated();
	}

}
