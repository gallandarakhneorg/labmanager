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

package fr.ciadlab.labmanager.utils.cnu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link CnuSection}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class CnuSectionTest {

	private List<CnuSection> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(CnuSection.values()));
	}

	private CnuSection cons(CnuSection status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void getNumber() throws Exception {
		for (final CnuSection section : CnuSection.values()) {
			int index = section.name().indexOf('_');
			int expected = Integer.parseInt(section.name().substring(index + 1));
			assertEquals(expected, section.getNumber());
		}
	}

	@Test
	public void getLabel_FR() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		for (final CnuSection section : CnuSection.values()) {
			assertNotNull(section.getLabel(), "Section " + section.name());
			assertNotEquals("", section.getLabel(), "Section " + section.name());
		}
	}

	@Test
	public void getLabel_US() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.US);
		for (final CnuSection section : CnuSection.values()) {
			assertNotNull(section.getLabel(), "Section " + section.name());
			assertNotEquals("", section.getLabel(), "Section " + section.name());
		}
	}

	@Test
	public void getLabel_Locale_FR() throws Exception {
		for (final CnuSection section : CnuSection.values()) {
			assertNotNull(section.getLabel(java.util.Locale.FRANCE), "Section " + section.name());
			assertNotEquals("", section.getLabel(java.util.Locale.FRANCE), "Section " + section.name());
		}
	}

	@Test
	public void getLabel_Locale_US() throws Exception {
		for (final CnuSection section : CnuSection.values()) {
			assertNotNull(section.getLabel(java.util.Locale.US), "Section " + section.name());
			assertNotEquals("", section.getLabel(java.util.Locale.US), "Section " + section.name());
		}
	}

	@Test
	public void valueOf_int() throws Exception {
		assertSame(cons(CnuSection.CNU_01), CnuSection.valueOf(1));
		assertSame(cons(CnuSection.CNU_02), CnuSection.valueOf(2));
		assertSame(cons(CnuSection.CNU_03), CnuSection.valueOf(3));
		assertSame(cons(CnuSection.CNU_04), CnuSection.valueOf(4));
		assertSame(cons(CnuSection.CNU_05), CnuSection.valueOf(5));
		assertSame(cons(CnuSection.CNU_06), CnuSection.valueOf(6));
		assertSame(cons(CnuSection.CNU_07), CnuSection.valueOf(7));
		assertSame(cons(CnuSection.CNU_08), CnuSection.valueOf(8));
		assertSame(cons(CnuSection.CNU_09), CnuSection.valueOf(9));
		assertSame(cons(CnuSection.CNU_10), CnuSection.valueOf(10));
		assertSame(cons(CnuSection.CNU_11), CnuSection.valueOf(11));
		assertSame(cons(CnuSection.CNU_12), CnuSection.valueOf(12));
		assertSame(cons(CnuSection.CNU_13), CnuSection.valueOf(13));
		assertSame(cons(CnuSection.CNU_14), CnuSection.valueOf(14));
		assertSame(cons(CnuSection.CNU_15), CnuSection.valueOf(15));
		assertSame(cons(CnuSection.CNU_16), CnuSection.valueOf(16));
		assertSame(cons(CnuSection.CNU_17), CnuSection.valueOf(17));
		assertSame(cons(CnuSection.CNU_18), CnuSection.valueOf(18));
		assertSame(cons(CnuSection.CNU_19), CnuSection.valueOf(19));
		assertSame(cons(CnuSection.CNU_20), CnuSection.valueOf(20));
		assertSame(cons(CnuSection.CNU_21), CnuSection.valueOf(21));
		assertSame(cons(CnuSection.CNU_22), CnuSection.valueOf(22));
		assertSame(cons(CnuSection.CNU_23), CnuSection.valueOf(23));
		assertSame(cons(CnuSection.CNU_24), CnuSection.valueOf(24));
		assertSame(cons(CnuSection.CNU_25), CnuSection.valueOf(25));
		assertSame(cons(CnuSection.CNU_26), CnuSection.valueOf(26));
		assertSame(cons(CnuSection.CNU_27), CnuSection.valueOf(27));
		assertSame(cons(CnuSection.CNU_28), CnuSection.valueOf(28));
		assertSame(cons(CnuSection.CNU_29), CnuSection.valueOf(29));
		assertSame(cons(CnuSection.CNU_30), CnuSection.valueOf(30));
		assertSame(cons(CnuSection.CNU_31), CnuSection.valueOf(31));
		assertSame(cons(CnuSection.CNU_32), CnuSection.valueOf(32));
		assertSame(cons(CnuSection.CNU_33), CnuSection.valueOf(33));
		assertSame(cons(CnuSection.CNU_34), CnuSection.valueOf(34));
		assertSame(cons(CnuSection.CNU_35), CnuSection.valueOf(35));
		assertSame(cons(CnuSection.CNU_36), CnuSection.valueOf(36));
		assertSame(cons(CnuSection.CNU_37), CnuSection.valueOf(37));
		assertSame(cons(CnuSection.CNU_60), CnuSection.valueOf(60));
		assertSame(cons(CnuSection.CNU_61), CnuSection.valueOf(61));
		assertSame(cons(CnuSection.CNU_62), CnuSection.valueOf(62));
		assertSame(cons(CnuSection.CNU_63), CnuSection.valueOf(63));
		assertSame(cons(CnuSection.CNU_64), CnuSection.valueOf(64));
		assertSame(cons(CnuSection.CNU_65), CnuSection.valueOf(65));
		assertSame(cons(CnuSection.CNU_66), CnuSection.valueOf(66));
		assertSame(cons(CnuSection.CNU_67), CnuSection.valueOf(67));
		assertSame(cons(CnuSection.CNU_68), CnuSection.valueOf(68));
		assertSame(cons(CnuSection.CNU_69), CnuSection.valueOf(69));
		assertSame(cons(CnuSection.CNU_70), CnuSection.valueOf(70));
		assertSame(cons(CnuSection.CNU_71), CnuSection.valueOf(71));
		assertSame(cons(CnuSection.CNU_72), CnuSection.valueOf(72));
		assertSame(cons(CnuSection.CNU_73), CnuSection.valueOf(73));
		assertSame(cons(CnuSection.CNU_74), CnuSection.valueOf(74));
		assertSame(cons(CnuSection.CNU_76), CnuSection.valueOf(76));
		assertSame(cons(CnuSection.CNU_77), CnuSection.valueOf(77));
		assertSame(cons(CnuSection.CNU_85), CnuSection.valueOf(85));
		assertSame(cons(CnuSection.CNU_86), CnuSection.valueOf(86));
		assertSame(cons(CnuSection.CNU_87), CnuSection.valueOf(87));
		assertAllTreated();
	}

	@Test
	public void valueOf_int_invalid() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> {
			CnuSection.valueOf(0);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			CnuSection.valueOf(38);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			CnuSection.valueOf(75);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			CnuSection.valueOf(80);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			CnuSection.valueOf(88);
		});
	}

}