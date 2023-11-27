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

package fr.ciadlab.labmanager.io.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.util.Map;

import fr.ciadlab.labmanager.io.core.CorePortal.CorePortalConference;
import fr.ciadlab.labmanager.io.scopus.ScopusPlatform.ScopusPerson;
import fr.ciadlab.labmanager.utils.TestUtils;
import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.arakhne.afc.progress.DefaultProgression;
import org.arakhne.afc.progress.Progression;
import org.arakhne.afc.vmutil.Resources;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests for {@link OnlineCorePortal}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class OnlineCorePortalTest {

	private OnlineCorePortal test;

	private static boolean isNetworkEnable() {
		return TestUtils.isNetworkEnable();
	}

	@BeforeEach
	public void setUp() {
		this.test = new OnlineCorePortal();
	}

	@Test
	public void getJournalUrl_null() {
		assertNull(this.test.getConferenceUrl(null));
	}

	@Test
	public void getJournalUrl_empty() {
		assertNull(this.test.getConferenceUrl(""));
	}

	@Test
	public void getJournalUrl() {
		URL url = this.test.getConferenceUrl("xyz");
		assertNotNull(url);
		assertEquals("http://portal.core.edu.au/conf-ranks/xyz", url.toExternalForm());
	}

	@Test
	@EnabledIf("isNetworkEnable")
	public void getConferenceRanking() throws Exception {
		CorePortalConference conference = this.test.getConferenceRanking(2017, "922", null);
		assertNotNull(conference);
		assertSame(CoreRanking.A_STAR, conference.ranking);
	}

}