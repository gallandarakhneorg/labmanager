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

package fr.ciadlab.labmanager.utils.bap;

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

/** Tests for {@link FrenchBap}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class FrenchBapTest {

	private List<FrenchBap> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(FrenchBap.values()));
	}

	private FrenchBap cons(FrenchBap status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void getLabel_FR() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		for (final FrenchBap section : FrenchBap.values()) {
			assertNotNull(section.getLabel(), section.name());
			assertNotEquals("", section.getLabel(), section.name());
		}
	}

	@Test
	public void getLabel_US() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.US);
		for (final FrenchBap section : FrenchBap.values()) {
			assertNotNull(section.getLabel(), section.name());
			assertNotEquals("", section.getLabel(), section.name());
		}
	}

	@Test
	public void getLabel_Locale_FR() throws Exception {
		for (final FrenchBap section : FrenchBap.values()) {
			assertNotNull(section.getLabel(java.util.Locale.FRANCE), section.name());
			assertNotEquals("", section.getLabel(java.util.Locale.FRANCE), section.name());
		}
	}

	@Test
	public void getLabel_Locale_US() throws Exception {
		for (final FrenchBap section : FrenchBap.values()) {
			assertNotNull(section.getLabel(java.util.Locale.US), section.name());
			assertNotEquals("", section.getLabel(java.util.Locale.US), section.name());
		}
	}

	@Test
	public void valueOfCaseInsensitive_letter() throws Exception {
		assertSame(cons(FrenchBap.BAP_A), FrenchBap.valueOfCaseInsensitive("a"));
		assertSame(cons(FrenchBap.BAP_B), FrenchBap.valueOfCaseInsensitive("b"));
		assertSame(cons(FrenchBap.BAP_C), FrenchBap.valueOfCaseInsensitive("c"));
		assertSame(cons(FrenchBap.BAP_D), FrenchBap.valueOfCaseInsensitive("d"));
		assertSame(cons(FrenchBap.BAP_E), FrenchBap.valueOfCaseInsensitive("e"));
		assertSame(cons(FrenchBap.BAP_F), FrenchBap.valueOfCaseInsensitive("f"));
		assertSame(cons(FrenchBap.BAP_G), FrenchBap.valueOfCaseInsensitive("g"));
		assertSame(cons(FrenchBap.BAP_J), FrenchBap.valueOfCaseInsensitive("j"));
		assertAllTreated();
	}

	@Test
	public void valueOfCaseInsensitive_int_invalid() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> {
			FrenchBap.valueOfCaseInsensitive("0");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			FrenchBap.valueOfCaseInsensitive("i");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			FrenchBap.valueOfCaseInsensitive("BAP_I");
		});
	}

}