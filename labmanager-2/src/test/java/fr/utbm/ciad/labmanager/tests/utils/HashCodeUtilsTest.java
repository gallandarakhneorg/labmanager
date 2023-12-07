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

package fr.utbm.ciad.labmanager.tests.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import fr.utbm.ciad.labmanager.utils.HashCodeUtils;
import org.junit.jupiter.api.Test;

/** Tests for {@link HashCodeUtils}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class HashCodeUtilsTest {

	@Test
	public void start() {
		assertEquals(1, HashCodeUtils.start());
	}

	@Test
	public void add_byte() {
		assertEquals(3819, HashCodeUtils.add(123, (byte) 6));
	}

	@Test
	public void add_short() {
		assertEquals(3819, HashCodeUtils.add(123, (short) 6));
	}

	@Test
	public void add_int() {
		assertEquals(3819, HashCodeUtils.add(123, 6));
	}

	@Test
	public void add_long() {
		assertEquals(3819, HashCodeUtils.add(123, (long) 6));
	}

	@Test
	public void add_float() {
		assertEquals(1086328549, HashCodeUtils.add(123, 6f));
	}

	@Test
	public void add_double() {
		assertEquals(1075318501, HashCodeUtils.add(123, 6.0));
	}

	@Test
	public void add_boolean() {
		assertEquals(5044, HashCodeUtils.add(123, true));
	}

	@Test
	public void add_object() {
		assertEquals(100167, HashCodeUtils.add(123, "abc"));
	}

}
