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

package fr.ciadlab.labmanager.utils.conrs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link ConrsSection}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ConrsSectionTest {

	private List<ConrsSection> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(ConrsSection.values()));
	}

	private ConrsSection cons(ConrsSection status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void getNumber() throws Exception {
		for (final ConrsSection section : ConrsSection.values()) {
			int index = section.name().indexOf('_');
			int expected = Integer.parseInt(section.name().substring(index + 1));
			assertEquals(expected, section.getNumber());
		}
	}

	@Test
	public void getLabel_FR() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		for (final ConrsSection section : ConrsSection.values()) {
			assertNotNull(section.getLabel(), "Section " + section.name());
			assertNotEquals("", section.getLabel(), "Section " + section.name());
		}
	}

	@Test
	public void getLabel_US() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.US);
		for (final ConrsSection section : ConrsSection.values()) {
			assertNotNull(section.getLabel(), "Section " + section.name());
			assertNotEquals("", section.getLabel(), "Section " + section.name());
		}
	}

	@Test
	public void getLabel_Locale_FR() throws Exception {
		for (final ConrsSection section : ConrsSection.values()) {
			assertNotNull(section.getLabel(java.util.Locale.FRANCE), "Section " + section.name());
			assertNotEquals("", section.getLabel(java.util.Locale.FRANCE), "Section " + section.name());
		}
	}

	@Test
	public void getLabel_Locale_US() throws Exception {
		for (final ConrsSection section : ConrsSection.values()) {
			assertNotNull(section.getLabel(java.util.Locale.US), "Section " + section.name());
			assertNotEquals("", section.getLabel(java.util.Locale.US), "Section " + section.name());
		}
	}

	@Test
	public void valueOf_int() throws Exception {
		assertSame(cons(ConrsSection.CONRS_01), ConrsSection.valueOf(1));
		assertSame(cons(ConrsSection.CONRS_02), ConrsSection.valueOf(2));
		assertSame(cons(ConrsSection.CONRS_03), ConrsSection.valueOf(3));
		assertSame(cons(ConrsSection.CONRS_04), ConrsSection.valueOf(4));
		assertSame(cons(ConrsSection.CONRS_05), ConrsSection.valueOf(5));
		assertSame(cons(ConrsSection.CONRS_06), ConrsSection.valueOf(6));
		assertSame(cons(ConrsSection.CONRS_07), ConrsSection.valueOf(7));
		assertSame(cons(ConrsSection.CONRS_08), ConrsSection.valueOf(8));
		assertSame(cons(ConrsSection.CONRS_09), ConrsSection.valueOf(9));
		assertSame(cons(ConrsSection.CONRS_10), ConrsSection.valueOf(10));
		assertSame(cons(ConrsSection.CONRS_11), ConrsSection.valueOf(11));
		assertSame(cons(ConrsSection.CONRS_12), ConrsSection.valueOf(12));
		assertSame(cons(ConrsSection.CONRS_13), ConrsSection.valueOf(13));
		assertSame(cons(ConrsSection.CONRS_14), ConrsSection.valueOf(14));
		assertSame(cons(ConrsSection.CONRS_15), ConrsSection.valueOf(15));
		assertSame(cons(ConrsSection.CONRS_16), ConrsSection.valueOf(16));
		assertSame(cons(ConrsSection.CONRS_17), ConrsSection.valueOf(17));
		assertSame(cons(ConrsSection.CONRS_18), ConrsSection.valueOf(18));
		assertSame(cons(ConrsSection.CONRS_19), ConrsSection.valueOf(19));
		assertSame(cons(ConrsSection.CONRS_20), ConrsSection.valueOf(20));
		assertSame(cons(ConrsSection.CONRS_21), ConrsSection.valueOf(21));
		assertSame(cons(ConrsSection.CONRS_22), ConrsSection.valueOf(22));
		assertSame(cons(ConrsSection.CONRS_23), ConrsSection.valueOf(23));
		assertSame(cons(ConrsSection.CONRS_24), ConrsSection.valueOf(24));
		assertSame(cons(ConrsSection.CONRS_25), ConrsSection.valueOf(25));
		assertSame(cons(ConrsSection.CONRS_26), ConrsSection.valueOf(26));
		assertSame(cons(ConrsSection.CONRS_27), ConrsSection.valueOf(27));
		assertSame(cons(ConrsSection.CONRS_28), ConrsSection.valueOf(28));
		assertSame(cons(ConrsSection.CONRS_29), ConrsSection.valueOf(29));
		assertSame(cons(ConrsSection.CONRS_30), ConrsSection.valueOf(30));
		assertSame(cons(ConrsSection.CONRS_31), ConrsSection.valueOf(31));
		assertSame(cons(ConrsSection.CONRS_32), ConrsSection.valueOf(32));
		assertSame(cons(ConrsSection.CONRS_33), ConrsSection.valueOf(33));
		assertSame(cons(ConrsSection.CONRS_34), ConrsSection.valueOf(34));
		assertSame(cons(ConrsSection.CONRS_35), ConrsSection.valueOf(35));
		assertSame(cons(ConrsSection.CONRS_36), ConrsSection.valueOf(36));
		assertSame(cons(ConrsSection.CONRS_37), ConrsSection.valueOf(37));
		assertSame(cons(ConrsSection.CONRS_38), ConrsSection.valueOf(38));
		assertSame(cons(ConrsSection.CONRS_39), ConrsSection.valueOf(39));
		assertSame(cons(ConrsSection.CONRS_40), ConrsSection.valueOf(40));
		assertSame(cons(ConrsSection.CONRS_41), ConrsSection.valueOf(41));
		assertAllTreated();
	}

	@Test
	public void valueOf_int_invalid() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> {
			ConrsSection.valueOf(0);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			ConrsSection.valueOf(42);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			ConrsSection.valueOf(75);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			ConrsSection.valueOf(80);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			ConrsSection.valueOf(88);
		});
	}

}