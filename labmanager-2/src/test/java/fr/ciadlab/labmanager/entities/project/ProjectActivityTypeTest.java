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

/** Tests for {@link ProjectActivityType}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ProjectActivityTypeTest {

	private List<ProjectActivityType> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(ProjectActivityType.values()));
	}

	private ProjectActivityType cons(ProjectActivityType status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void valueOfCaseInsensitive_String_special() {
		assertThrows(IllegalArgumentException.class, () -> ProjectActivityType.valueOfCaseInsensitive(null));
		assertThrows(IllegalArgumentException.class, () -> ProjectActivityType.valueOfCaseInsensitive(""));
	}

	@Test
	public void valueOfCaseInsensitive_String_upperCase() {
		assertEquals(cons(ProjectActivityType.FUNDAMENTAL_RESEARCH), ProjectActivityType.valueOfCaseInsensitive("FUNDAMENTAL_RESEARCH"));
		assertEquals(cons(ProjectActivityType.APPLIED_RESEARCH), ProjectActivityType.valueOfCaseInsensitive("APPLIED_RESEARCH"));
		assertEquals(cons(ProjectActivityType.EXPERIMENTAL_DEVELOPMENT), ProjectActivityType.valueOfCaseInsensitive("EXPERIMENTAL_DEVELOPMENT"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> ProjectActivityType.valueOfCaseInsensitive("XYZ"));
	}

	@Test
	public void valueOfCaseInsensitive_String_lowerCase() {
		assertEquals(cons(ProjectActivityType.FUNDAMENTAL_RESEARCH), ProjectActivityType.valueOfCaseInsensitive("fundamental_research"));
		assertEquals(cons(ProjectActivityType.APPLIED_RESEARCH), ProjectActivityType.valueOfCaseInsensitive("applied_research"));
		assertEquals(cons(ProjectActivityType.EXPERIMENTAL_DEVELOPMENT), ProjectActivityType.valueOfCaseInsensitive("experimental_development"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> ProjectActivityType.valueOfCaseInsensitive("xyz"));
	}

	@Test
	public void getLabel_FR() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Fundamental Research", cons(ProjectActivityType.FUNDAMENTAL_RESEARCH).getLabel());
		assertEquals("Applied Research", cons(ProjectActivityType.APPLIED_RESEARCH).getLabel());
		assertEquals("Experimental Development", cons(ProjectActivityType.EXPERIMENTAL_DEVELOPMENT).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_US() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Fundamental Research", cons(ProjectActivityType.FUNDAMENTAL_RESEARCH).getLabel());
		assertEquals("Applied Research", cons(ProjectActivityType.APPLIED_RESEARCH).getLabel());
		assertEquals("Experimental Development", cons(ProjectActivityType.EXPERIMENTAL_DEVELOPMENT).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR() throws Exception {
		assertEquals("Recherche fondamentale", cons(ProjectActivityType.FUNDAMENTAL_RESEARCH).getLabel(Locale.FRANCE));
		assertEquals("Recherche appliquée", cons(ProjectActivityType.APPLIED_RESEARCH).getLabel(Locale.FRANCE));
		assertEquals("Développement expérimental", cons(ProjectActivityType.EXPERIMENTAL_DEVELOPMENT).getLabel(Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() throws Exception {
		assertEquals("Fundamental Research", cons(ProjectActivityType.FUNDAMENTAL_RESEARCH).getLabel(Locale.US));
		assertEquals("Applied Research", cons(ProjectActivityType.APPLIED_RESEARCH).getLabel(Locale.US));
		assertEquals("Experimental Development", cons(ProjectActivityType.EXPERIMENTAL_DEVELOPMENT).getLabel(Locale.US));
		assertAllTreated();
	}

}
