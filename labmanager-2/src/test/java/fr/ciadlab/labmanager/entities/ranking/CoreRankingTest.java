/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.entities.ranking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
	public void getCoreRankingFromString_String_special() {
		assertNull(CoreRanking.getCoreRankingFromString(null));
		assertNull(CoreRanking.getCoreRankingFromString(""));
	}

	@Test
	public void getCoreRankingFromString_String_upperCase() {
		assertEquals(CoreRanking.A_STAR_STAR, CoreRanking.getCoreRankingFromString("A**"));
		assertEquals(CoreRanking.A_STAR, CoreRanking.getCoreRankingFromString("A*"));
		assertEquals(CoreRanking.A, CoreRanking.getCoreRankingFromString("A"));
		assertEquals(CoreRanking.B, CoreRanking.getCoreRankingFromString("B"));
		assertEquals(CoreRanking.C, CoreRanking.getCoreRankingFromString("C"));
		assertEquals(CoreRanking.D, CoreRanking.getCoreRankingFromString("D"));
		assertNull(CoreRanking.getCoreRankingFromString("E"));
	}

	@Test
	public void getCoreRankingFromString_String_lowerCase() {
		assertEquals(CoreRanking.A_STAR_STAR, CoreRanking.getCoreRankingFromString("a**"));
		assertEquals(CoreRanking.A_STAR, CoreRanking.getCoreRankingFromString("a*"));
		assertEquals(CoreRanking.A, CoreRanking.getCoreRankingFromString("a"));
		assertEquals(CoreRanking.B, CoreRanking.getCoreRankingFromString("b"));
		assertEquals(CoreRanking.C, CoreRanking.getCoreRankingFromString("c"));
		assertEquals(CoreRanking.D, CoreRanking.getCoreRankingFromString("d"));
		assertNull(CoreRanking.getCoreRankingFromString("e"));
	}

}
