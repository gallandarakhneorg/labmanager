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

import fr.utbm.ciad.labmanager.data.teaching.PedagogicalPracticeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

	@BeforeEach
	public void setUp() {
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
	public void getLabel_FR() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Collaborative works", cons(PedagogicalPracticeType.COLLABORATIVE_WORKS).getLabel());
		assertEquals("Distance learning", cons(PedagogicalPracticeType.DISTANCE_LEARNING).getLabel());
		assertEquals("Historical pedagogical method", cons(PedagogicalPracticeType.HISTORICAL_METHOD).getLabel());
		assertEquals("Inverted classroom", cons(PedagogicalPracticeType.INVERTED_CLASSROOM).getLabel());
		assertEquals("Use of modern presentation tools", cons(PedagogicalPracticeType.MODERN_PRESENTATION_TOOLS).getLabel());
		assertEquals("Other method", cons(PedagogicalPracticeType.OTHER_METHOD).getLabel());
		assertEquals("Pedagogy 3.0", cons(PedagogicalPracticeType.PEDAGOGY_3_0).getLabel());
		assertEquals("Problem based learning", cons(PedagogicalPracticeType.PROBLEM_BASED_LEARNING).getLabel());
		assertEquals("Project-oriented pedagogy", cons(PedagogicalPracticeType.PROJECT_ORIENTED).getLabel());
		assertEquals("Reverse class", cons(PedagogicalPracticeType.REVERSE_CLASS).getLabel());
		assertEquals("Serious game", cons(PedagogicalPracticeType.SERIOUS_GAME).getLabel());
		assertEquals("Learning with social network", cons(PedagogicalPracticeType.SOCIAL_NETWORK_LEARNING).getLabel());
		assertEquals("Virtual classroom", cons(PedagogicalPracticeType.VIRTUAL_CLASSROOM).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_US() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Collaborative works", cons(PedagogicalPracticeType.COLLABORATIVE_WORKS).getLabel());
		assertEquals("Distance learning", cons(PedagogicalPracticeType.DISTANCE_LEARNING).getLabel());
		assertEquals("Historical pedagogical method", cons(PedagogicalPracticeType.HISTORICAL_METHOD).getLabel());
		assertEquals("Inverted classroom", cons(PedagogicalPracticeType.INVERTED_CLASSROOM).getLabel());
		assertEquals("Use of modern presentation tools", cons(PedagogicalPracticeType.MODERN_PRESENTATION_TOOLS).getLabel());
		assertEquals("Other method", cons(PedagogicalPracticeType.OTHER_METHOD).getLabel());
		assertEquals("Pedagogy 3.0", cons(PedagogicalPracticeType.PEDAGOGY_3_0).getLabel());
		assertEquals("Problem based learning", cons(PedagogicalPracticeType.PROBLEM_BASED_LEARNING).getLabel());
		assertEquals("Project-oriented pedagogy", cons(PedagogicalPracticeType.PROJECT_ORIENTED).getLabel());
		assertEquals("Reverse class", cons(PedagogicalPracticeType.REVERSE_CLASS).getLabel());
		assertEquals("Serious game", cons(PedagogicalPracticeType.SERIOUS_GAME).getLabel());
		assertEquals("Learning with social network", cons(PedagogicalPracticeType.SOCIAL_NETWORK_LEARNING).getLabel());
		assertEquals("Virtual classroom", cons(PedagogicalPracticeType.VIRTUAL_CLASSROOM).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR() throws Exception {
		assertEquals("Travail collaboratif", cons(PedagogicalPracticeType.COLLABORATIVE_WORKS).getLabel(Locale.FRANCE));
		assertEquals("Formation à distance", cons(PedagogicalPracticeType.DISTANCE_LEARNING).getLabel(Locale.FRANCE));
		assertEquals("Méthode historique", cons(PedagogicalPracticeType.HISTORICAL_METHOD).getLabel(Locale.FRANCE));
		assertEquals("Classe inversée", cons(PedagogicalPracticeType.INVERTED_CLASSROOM).getLabel(Locale.FRANCE));
		assertEquals("Utilisation d'outils récents de présentation", cons(PedagogicalPracticeType.MODERN_PRESENTATION_TOOLS).getLabel(Locale.FRANCE));
		assertEquals("Authe méthode", cons(PedagogicalPracticeType.OTHER_METHOD).getLabel(Locale.FRANCE));
		assertEquals("Pédagogie 3.0", cons(PedagogicalPracticeType.PEDAGOGY_3_0).getLabel(Locale.FRANCE));
		assertEquals("Apprentissage par problème", cons(PedagogicalPracticeType.PROBLEM_BASED_LEARNING).getLabel(Locale.FRANCE));
		assertEquals("Pédagogie orientée projet", cons(PedagogicalPracticeType.PROJECT_ORIENTED).getLabel(Locale.FRANCE));
		assertEquals("Classe renversée", cons(PedagogicalPracticeType.REVERSE_CLASS).getLabel(Locale.FRANCE));
		assertEquals("Pédagogie basée sur un jeu sérieux", cons(PedagogicalPracticeType.SERIOUS_GAME).getLabel(Locale.FRANCE));
		assertEquals("Usage pédagogique des réseaux sociaux", cons(PedagogicalPracticeType.SOCIAL_NETWORK_LEARNING).getLabel(Locale.FRANCE));
		assertEquals("Classe virtuelle", cons(PedagogicalPracticeType.VIRTUAL_CLASSROOM).getLabel(Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() throws Exception {
		assertEquals("Collaborative works", cons(PedagogicalPracticeType.COLLABORATIVE_WORKS).getLabel(Locale.US));
		assertEquals("Distance learning", cons(PedagogicalPracticeType.DISTANCE_LEARNING).getLabel(Locale.US));
		assertEquals("Historical pedagogical method", cons(PedagogicalPracticeType.HISTORICAL_METHOD).getLabel(Locale.US));
		assertEquals("Inverted classroom", cons(PedagogicalPracticeType.INVERTED_CLASSROOM).getLabel(Locale.US));
		assertEquals("Use of modern presentation tools", cons(PedagogicalPracticeType.MODERN_PRESENTATION_TOOLS).getLabel(Locale.US));
		assertEquals("Other method", cons(PedagogicalPracticeType.OTHER_METHOD).getLabel(Locale.US));
		assertEquals("Pedagogy 3.0", cons(PedagogicalPracticeType.PEDAGOGY_3_0).getLabel(Locale.US));
		assertEquals("Problem based learning", cons(PedagogicalPracticeType.PROBLEM_BASED_LEARNING).getLabel(Locale.US));
		assertEquals("Project-oriented pedagogy", cons(PedagogicalPracticeType.PROJECT_ORIENTED).getLabel(Locale.US));
		assertEquals("Reverse class", cons(PedagogicalPracticeType.REVERSE_CLASS).getLabel(Locale.US));
		assertEquals("Serious game", cons(PedagogicalPracticeType.SERIOUS_GAME).getLabel(Locale.US));
		assertEquals("Learning with social network", cons(PedagogicalPracticeType.SOCIAL_NETWORK_LEARNING).getLabel(Locale.US));
		assertEquals("Virtual classroom", cons(PedagogicalPracticeType.VIRTUAL_CLASSROOM).getLabel(Locale.US));
		assertAllTreated();
	}

	@Test
	public void getDescription_FR() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertNotNull(cons(PedagogicalPracticeType.COLLABORATIVE_WORKS).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.DISTANCE_LEARNING).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.HISTORICAL_METHOD).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.INVERTED_CLASSROOM).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.MODERN_PRESENTATION_TOOLS).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.OTHER_METHOD).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.PEDAGOGY_3_0).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.PROBLEM_BASED_LEARNING).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.PROJECT_ORIENTED).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.REVERSE_CLASS).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.SERIOUS_GAME).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.SOCIAL_NETWORK_LEARNING).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.VIRTUAL_CLASSROOM).getDescription());
		assertAllTreated();
	}

	@Test
	public void getDescrition_US() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.US);
		assertNotNull(cons(PedagogicalPracticeType.COLLABORATIVE_WORKS).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.DISTANCE_LEARNING).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.HISTORICAL_METHOD).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.INVERTED_CLASSROOM).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.MODERN_PRESENTATION_TOOLS).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.OTHER_METHOD).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.PEDAGOGY_3_0).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.PROBLEM_BASED_LEARNING).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.PROJECT_ORIENTED).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.REVERSE_CLASS).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.SERIOUS_GAME).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.SOCIAL_NETWORK_LEARNING).getDescription());
		assertNotNull(cons(PedagogicalPracticeType.VIRTUAL_CLASSROOM).getDescription());
		assertAllTreated();
	}

	@Test
	public void getDescription_Locale_FR() throws Exception {
		assertNotNull(cons(PedagogicalPracticeType.COLLABORATIVE_WORKS).getDescription(Locale.FRANCE));
		assertNotNull(cons(PedagogicalPracticeType.DISTANCE_LEARNING).getDescription(Locale.FRANCE));
		assertNotNull(cons(PedagogicalPracticeType.HISTORICAL_METHOD).getDescription(Locale.FRANCE));
		assertNotNull(cons(PedagogicalPracticeType.INVERTED_CLASSROOM).getDescription(Locale.FRANCE));
		assertNotNull(cons(PedagogicalPracticeType.MODERN_PRESENTATION_TOOLS).getDescription(Locale.FRANCE));
		assertNotNull(cons(PedagogicalPracticeType.OTHER_METHOD).getDescription(Locale.FRANCE));
		assertNotNull(cons(PedagogicalPracticeType.PEDAGOGY_3_0).getDescription(Locale.FRANCE));
		assertNotNull(cons(PedagogicalPracticeType.PROBLEM_BASED_LEARNING).getDescription(Locale.FRANCE));
		assertNotNull(cons(PedagogicalPracticeType.PROJECT_ORIENTED).getDescription(Locale.FRANCE));
		assertNotNull(cons(PedagogicalPracticeType.REVERSE_CLASS).getDescription(Locale.FRANCE));
		assertNotNull(cons(PedagogicalPracticeType.SERIOUS_GAME).getDescription(Locale.FRANCE));
		assertNotNull(cons(PedagogicalPracticeType.SOCIAL_NETWORK_LEARNING).getDescription(Locale.FRANCE));
		assertNotNull(cons(PedagogicalPracticeType.VIRTUAL_CLASSROOM).getDescription(Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getDescription_Locale_US() throws Exception {
		assertNotNull(cons(PedagogicalPracticeType.COLLABORATIVE_WORKS).getDescription(Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.DISTANCE_LEARNING).getDescription(Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.HISTORICAL_METHOD).getDescription(Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.INVERTED_CLASSROOM).getDescription(Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.MODERN_PRESENTATION_TOOLS).getDescription(Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.OTHER_METHOD).getDescription(Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.PEDAGOGY_3_0).getDescription(Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.PROBLEM_BASED_LEARNING).getDescription(Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.PROJECT_ORIENTED).getDescription(Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.REVERSE_CLASS).getDescription(Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.SERIOUS_GAME).getDescription(Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.SOCIAL_NETWORK_LEARNING).getDescription(Locale.US));
		assertNotNull(cons(PedagogicalPracticeType.VIRTUAL_CLASSROOM).getDescription(Locale.US));
		assertAllTreated();
	}

}
