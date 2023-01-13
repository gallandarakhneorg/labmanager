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

	@BeforeEach
	public void setUp() {
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
	public void getLabel_FR() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Under preparation", cons(ProjectStatus.PREPARATION).getLabel());
		assertEquals("Under evaluation", cons(ProjectStatus.EVALUATION).getLabel());
		assertEquals("Canceled", cons(ProjectStatus.CANCELED).getLabel());
		assertEquals("Rejected", cons(ProjectStatus.REJECTED).getLabel());
		assertEquals("Accepted", cons(ProjectStatus.ACCEPTED).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_US() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Under preparation", cons(ProjectStatus.PREPARATION).getLabel());
		assertEquals("Under evaluation", cons(ProjectStatus.EVALUATION).getLabel());
		assertEquals("Canceled", cons(ProjectStatus.CANCELED).getLabel());
		assertEquals("Rejected", cons(ProjectStatus.REJECTED).getLabel());
		assertEquals("Accepted", cons(ProjectStatus.ACCEPTED).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR() throws Exception {
		assertEquals("En cours de préparation", cons(ProjectStatus.PREPARATION).getLabel(Locale.FRANCE));
		assertEquals("En cours d'évaluation", cons(ProjectStatus.EVALUATION).getLabel(Locale.FRANCE));
		assertEquals("Annulé", cons(ProjectStatus.CANCELED).getLabel(Locale.FRANCE));
		assertEquals("Rejeté", cons(ProjectStatus.REJECTED).getLabel(Locale.FRANCE));
		assertEquals("Accepté", cons(ProjectStatus.ACCEPTED).getLabel(Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() throws Exception {
		assertEquals("Under preparation", cons(ProjectStatus.PREPARATION).getLabel(Locale.US));
		assertEquals("Under evaluation", cons(ProjectStatus.EVALUATION).getLabel(Locale.US));
		assertEquals("Canceled", cons(ProjectStatus.CANCELED).getLabel(Locale.US));
		assertEquals("Rejected", cons(ProjectStatus.REJECTED).getLabel(Locale.US));
		assertEquals("Accepted", cons(ProjectStatus.ACCEPTED).getLabel(Locale.US));
		assertAllTreated();
	}

}
