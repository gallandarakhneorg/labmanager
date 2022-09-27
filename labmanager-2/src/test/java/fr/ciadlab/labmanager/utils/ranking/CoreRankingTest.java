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

package fr.ciadlab.labmanager.utils.ranking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import org.junit.jupiter.api.Test;

/** Tests for {@link CoreRanking}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class CoreRankingTest {

	@Test
	public void toStringTest() {
		assertEquals("A**", CoreRanking.A_STAR_STAR.toString());
		assertEquals("A*", CoreRanking.A_STAR.toString());
		assertEquals("A", CoreRanking.A.toString());
		assertEquals("B", CoreRanking.B.toString());
		assertEquals("C", CoreRanking.C.toString());
		assertEquals("D", CoreRanking.D.toString());
	}

	@Test
	public void valueOfCaseInsensitive_String_special() {
		assertNull(CoreRanking.valueOfCaseInsensitive(null));
		assertNull(CoreRanking.valueOfCaseInsensitive(""));
	}

	@Test
	public void valueOfCaseInsensitive_String_upperCase() {
		assertEquals(CoreRanking.A_STAR_STAR, CoreRanking.valueOfCaseInsensitive("A**"));
		assertEquals(CoreRanking.A_STAR, CoreRanking.valueOfCaseInsensitive("A*"));
		assertEquals(CoreRanking.A, CoreRanking.valueOfCaseInsensitive("A"));
		assertEquals(CoreRanking.B, CoreRanking.valueOfCaseInsensitive("B"));
		assertEquals(CoreRanking.C, CoreRanking.valueOfCaseInsensitive("C"));
		assertEquals(CoreRanking.D, CoreRanking.valueOfCaseInsensitive("D"));
		assertNull(CoreRanking.valueOfCaseInsensitive("E"));
	}

	@Test
	public void valueOfCaseInsensitive_String_lowerCase() {
		assertEquals(CoreRanking.A_STAR_STAR, CoreRanking.valueOfCaseInsensitive("a**"));
		assertEquals(CoreRanking.A_STAR, CoreRanking.valueOfCaseInsensitive("a*"));
		assertEquals(CoreRanking.A, CoreRanking.valueOfCaseInsensitive("a"));
		assertEquals(CoreRanking.B, CoreRanking.valueOfCaseInsensitive("b"));
		assertEquals(CoreRanking.C, CoreRanking.valueOfCaseInsensitive("c"));
		assertEquals(CoreRanking.D, CoreRanking.valueOfCaseInsensitive("d"));
		assertNull(CoreRanking.valueOfCaseInsensitive("e"));
	}

}
