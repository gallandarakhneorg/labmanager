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

package fr.ciadlab.labmanager.entities.assostructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.ciadlab.labmanager.entities.assostructure.HolderRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link HolderRole}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.2
 */
@SuppressWarnings("all")
public class HolderRoleTest {

	private List<HolderRole> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(HolderRole.values()));
	}

	private HolderRole cons(HolderRole status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void valueOfCaseInsensitive_String_special() {
		assertThrows(IllegalArgumentException.class, () -> HolderRole.valueOfCaseInsensitive(null));
		assertThrows(IllegalArgumentException.class, () -> HolderRole.valueOfCaseInsensitive(""));
	}

	@Test
	public void valueOfCaseInsensitive_String_upperCase() {
		assertEquals(cons(HolderRole.STRUCTURE_HEAD), HolderRole.valueOfCaseInsensitive("STRUCTURE_HEAD"));
		assertEquals(cons(HolderRole.SCIENTIFIC_HEAD), HolderRole.valueOfCaseInsensitive("SCIENTIFIC_HEAD"));
		assertEquals(cons(HolderRole.PARTICIPANT), HolderRole.valueOfCaseInsensitive("PARTICIPANT"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> HolderRole.valueOfCaseInsensitive("XYZ"));
	}

	@Test
	public void valueOfCaseInsensitive_String_lowerCase() {
		assertEquals(cons(HolderRole.STRUCTURE_HEAD), HolderRole.valueOfCaseInsensitive("structure_head"));
		assertEquals(cons(HolderRole.SCIENTIFIC_HEAD), HolderRole.valueOfCaseInsensitive("scientific_head"));
		assertEquals(cons(HolderRole.PARTICIPANT), HolderRole.valueOfCaseInsensitive("participant"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> HolderRole.valueOfCaseInsensitive("xyz"));
	}

	@Test
	public void getLabel_FR() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Structure head", cons(HolderRole.STRUCTURE_HEAD).getLabel());
		assertEquals("Scientific head", cons(HolderRole.SCIENTIFIC_HEAD).getLabel());
		assertEquals("Participant", cons(HolderRole.PARTICIPANT).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_US() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Structure head", cons(HolderRole.STRUCTURE_HEAD).getLabel());
		assertEquals("Scientific head", cons(HolderRole.SCIENTIFIC_HEAD).getLabel());
		assertEquals("Participant", cons(HolderRole.PARTICIPANT).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR() throws Exception {
		assertEquals("Dirigeant", cons(HolderRole.STRUCTURE_HEAD).getLabel(Locale.FRANCE));
		assertEquals("Responsable scientifique", cons(HolderRole.SCIENTIFIC_HEAD).getLabel(Locale.FRANCE));
		assertEquals("Participant", cons(HolderRole.PARTICIPANT).getLabel(Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() throws Exception {
		assertEquals("Structure head", cons(HolderRole.STRUCTURE_HEAD).getLabel(Locale.US));
		assertEquals("Scientific head", cons(HolderRole.SCIENTIFIC_HEAD).getLabel(Locale.US));
		assertEquals("Participant", cons(HolderRole.PARTICIPANT).getLabel(Locale.US));
		assertAllTreated();
	}

	@Test
	public void isHead() throws Exception {
		assertTrue(cons(HolderRole.STRUCTURE_HEAD).isHead());
		assertTrue(cons(HolderRole.SCIENTIFIC_HEAD).isHead());
		assertFalse(cons(HolderRole.PARTICIPANT).isHead());
		assertAllTreated();
	}

}
