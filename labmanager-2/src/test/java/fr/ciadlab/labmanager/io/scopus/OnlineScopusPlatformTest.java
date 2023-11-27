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

package fr.ciadlab.labmanager.io.scopus;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URL;

import fr.ciadlab.labmanager.io.scopus.ScopusPlatform.ScopusPerson;
import fr.ciadlab.labmanager.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests for {@link OnlineScopusPlatform}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class OnlineScopusPlatformTest {

	private OnlineScopusPlatform test;

	private static boolean isNetworkEnable() {
		return TestUtils.isNetworkEnable();
	}

	@BeforeEach
	public void setUp() {
		this.test = new OnlineScopusPlatform();
	}

	@Test
	@EnabledIf("isNetworkEnable")
	public void getPersonRanking() throws Exception {
		URL url = new URL("https://www.scopus.com/authid/detail.uri?authorId=23008496500");
		ScopusPerson person = this.test.getPersonRanking(url, null);
		assertNotNull(person);
		// use 17 that was the h-index when this unit test is written
		assertGreaterEquals(17, person.hindex);
		// use 992 that was the number of citations when this unit test is written
		assertGreaterEquals(992, person.citations);
	}

	private static void assertGreaterEquals(int expected, int actual) {
		if (actual < expected) {
			fail("Invalid value: " + actual + ". It should be >= " + expected);
		}
	}

}