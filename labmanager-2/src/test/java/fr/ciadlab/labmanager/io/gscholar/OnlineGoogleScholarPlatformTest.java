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

package fr.ciadlab.labmanager.io.gscholar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;

import fr.ciadlab.labmanager.io.gscholar.GoogleScholarPlatform.GoogleScholarPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests for {@link OnlineGoogleScholarPlatform}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class OnlineGoogleScholarPlatformTest {

	private OnlineGoogleScholarPlatform test;

	@BeforeEach
	public void setUp() {
		this.test = new OnlineGoogleScholarPlatform();
	}

	@Test
	public void getPersonRanking() throws Exception {
		URL url = new URL("https://scholar.google.fr/citations?user=2Xita5IAAAAJ");
		GoogleScholarPerson person = this.test.getPersonRanking(url, null);
		assertNotNull(person);
		// use 26 that was the h-index when this unit test is written
		assertTrue(person.hindex >= 26);
		// use 2278 that was the number of citations when this unit test is written
		assertTrue(person.citations >= 2278);
	}

}