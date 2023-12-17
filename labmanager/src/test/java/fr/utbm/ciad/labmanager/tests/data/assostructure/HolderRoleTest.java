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

package fr.utbm.ciad.labmanager.tests.data.assostructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.utbm.ciad.labmanager.configuration.messages.BaseMessageSource;
import fr.utbm.ciad.labmanager.data.assostructure.HolderRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

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

	private MessageSourceAccessor messages;

	@BeforeEach
	public void setUp() {
		this.messages = BaseMessageSource.getGlobalMessageAccessor();
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
	public void getLabel() throws Exception {
		assertEquals("Structure head", cons(HolderRole.STRUCTURE_HEAD).getLabel(this.messages, Locale.US));
		assertEquals("Scientific head", cons(HolderRole.SCIENTIFIC_HEAD).getLabel(this.messages, Locale.US));
		assertEquals("Participant", cons(HolderRole.PARTICIPANT).getLabel(this.messages, Locale.US));
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
