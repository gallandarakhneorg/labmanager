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

package fr.ciadlab.labmanager.utils.trl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.ciadlab.labmanager.entities.project.ProjectStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link TRL}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class TRLTest {

	private List<TRL> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(TRL.values()));
	}

	private TRL cons(TRL status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void valueOfCaseInsensitive_String_special() {
		assertNull(TRL.valueOfCaseInsensitive(null));
		assertNull(TRL.valueOfCaseInsensitive(""));
	}

	@Test
	public void valueOfCaseInsensitive_String_upperCase() {
		assertEquals(cons(TRL.TRL1), TRL.valueOfCaseInsensitive("TRL1"));
		assertEquals(cons(TRL.TRL2), TRL.valueOfCaseInsensitive("TRL2"));
		assertEquals(cons(TRL.TRL3), TRL.valueOfCaseInsensitive("TRL3"));
		assertEquals(cons(TRL.TRL4), TRL.valueOfCaseInsensitive("TRL4"));
		assertEquals(cons(TRL.TRL5), TRL.valueOfCaseInsensitive("TRL5"));
		assertEquals(cons(TRL.TRL6), TRL.valueOfCaseInsensitive("TRL6"));
		assertEquals(cons(TRL.TRL7), TRL.valueOfCaseInsensitive("TRL7"));
		assertEquals(cons(TRL.TRL8), TRL.valueOfCaseInsensitive("TRL8"));
		assertEquals(cons(TRL.TRL9), TRL.valueOfCaseInsensitive("TRL9"));
		assertAllTreated();
		assertNull(TRL.valueOfCaseInsensitive("XYZ"));
	}

	@Test
	public void valueOfCaseInsensitive_String_lowerCase() {
		assertEquals(cons(TRL.TRL1), TRL.valueOfCaseInsensitive("trl1"));
		assertEquals(cons(TRL.TRL2), TRL.valueOfCaseInsensitive("trl2"));
		assertEquals(cons(TRL.TRL3), TRL.valueOfCaseInsensitive("trl3"));
		assertEquals(cons(TRL.TRL4), TRL.valueOfCaseInsensitive("trl4"));
		assertEquals(cons(TRL.TRL5), TRL.valueOfCaseInsensitive("trl5"));
		assertEquals(cons(TRL.TRL6), TRL.valueOfCaseInsensitive("trl6"));
		assertEquals(cons(TRL.TRL7), TRL.valueOfCaseInsensitive("trl7"));
		assertEquals(cons(TRL.TRL8), TRL.valueOfCaseInsensitive("trl8"));
		assertEquals(cons(TRL.TRL9), TRL.valueOfCaseInsensitive("trl9"));
		assertAllTreated();
		assertNull(TRL.valueOfCaseInsensitive("xyz"));
	}

	@Test
	public void valueOfCaseInsensitive_String_number() {
		assertEquals(cons(TRL.TRL1), TRL.valueOfCaseInsensitive("1"));
		assertEquals(cons(TRL.TRL2), TRL.valueOfCaseInsensitive("2"));
		assertEquals(cons(TRL.TRL3), TRL.valueOfCaseInsensitive("3"));
		assertEquals(cons(TRL.TRL4), TRL.valueOfCaseInsensitive("4"));
		assertEquals(cons(TRL.TRL5), TRL.valueOfCaseInsensitive("5"));
		assertEquals(cons(TRL.TRL6), TRL.valueOfCaseInsensitive("6"));
		assertEquals(cons(TRL.TRL7), TRL.valueOfCaseInsensitive("7"));
		assertEquals(cons(TRL.TRL8), TRL.valueOfCaseInsensitive("8"));
		assertEquals(cons(TRL.TRL9), TRL.valueOfCaseInsensitive("9"));
		assertAllTreated();
		assertNull(TRL.valueOfCaseInsensitive("0"));
		assertNull(TRL.valueOfCaseInsensitive("10"));
	}

	@Test
	public void getLabel_FR() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Observation of basic principles", cons(TRL.TRL1).getLabel());
		assertEquals("Formulation of concept", cons(TRL.TRL2).getLabel());
		assertEquals("Experimental proof of concept", cons(TRL.TRL3).getLabel());
		assertEquals("Validation in lab", cons(TRL.TRL4).getLabel());
		assertEquals("Validation in relevant environment", cons(TRL.TRL5).getLabel());
		assertEquals("Demonstration in relevant environment", cons(TRL.TRL6).getLabel());
		assertEquals("Demonstration of system prototype in operational environment", cons(TRL.TRL7).getLabel());
		assertEquals("System complete and qualified", cons(TRL.TRL8).getLabel());
		assertEquals("Actual system proven in operational environment", cons(TRL.TRL9).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_US() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Observation of basic principles", cons(TRL.TRL1).getLabel());
		assertEquals("Formulation of concept", cons(TRL.TRL2).getLabel());
		assertEquals("Experimental proof of concept", cons(TRL.TRL3).getLabel());
		assertEquals("Validation in lab", cons(TRL.TRL4).getLabel());
		assertEquals("Validation in relevant environment", cons(TRL.TRL5).getLabel());
		assertEquals("Demonstration in relevant environment", cons(TRL.TRL6).getLabel());
		assertEquals("Demonstration of system prototype in operational environment", cons(TRL.TRL7).getLabel());
		assertEquals("System complete and qualified", cons(TRL.TRL8).getLabel());
		assertEquals("Actual system proven in operational environment", cons(TRL.TRL9).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR() throws Exception {
		assertEquals("Observation de principes de base", cons(TRL.TRL1).getLabel(Locale.FRANCE));
		assertEquals("Formulation du concept", cons(TRL.TRL2).getLabel(Locale.FRANCE));
		assertEquals("Preuve expérimentale de concept", cons(TRL.TRL3).getLabel(Locale.FRANCE));
		assertEquals("Validation en laboratoire", cons(TRL.TRL4).getLabel(Locale.FRANCE));
		assertEquals("Validation dans un environnement significatif", cons(TRL.TRL5).getLabel(Locale.FRANCE));
		assertEquals("Démonstration dans un environnement significatif", cons(TRL.TRL6).getLabel(Locale.FRANCE));
		assertEquals("Démonstration d'un prototype dans un environnement opérationnel", cons(TRL.TRL7).getLabel(Locale.FRANCE));
		assertEquals("Système complet et qualifié", cons(TRL.TRL8).getLabel(Locale.FRANCE));
		assertEquals("Système complet fonctionnant dans l'environnement opérationnel", cons(TRL.TRL9).getLabel(Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() throws Exception {
		assertEquals("Observation of basic principles", cons(TRL.TRL1).getLabel(Locale.US));
		assertEquals("Formulation of concept", cons(TRL.TRL2).getLabel(Locale.US));
		assertEquals("Experimental proof of concept", cons(TRL.TRL3).getLabel(Locale.US));
		assertEquals("Validation in lab", cons(TRL.TRL4).getLabel(Locale.US));
		assertEquals("Validation in relevant environment", cons(TRL.TRL5).getLabel(Locale.US));
		assertEquals("Demonstration in relevant environment", cons(TRL.TRL6).getLabel(Locale.US));
		assertEquals("Demonstration of system prototype in operational environment", cons(TRL.TRL7).getLabel(Locale.US));
		assertEquals("System complete and qualified", cons(TRL.TRL8).getLabel(Locale.US));
		assertEquals("Actual system proven in operational environment", cons(TRL.TRL9).getLabel(Locale.US));
		assertAllTreated();
	}

}
