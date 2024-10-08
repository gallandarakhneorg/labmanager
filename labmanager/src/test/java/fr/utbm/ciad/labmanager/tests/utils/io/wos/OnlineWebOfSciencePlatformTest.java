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

package fr.utbm.ciad.labmanager.tests.utils.io.wos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.util.Map;

import fr.utbm.ciad.labmanager.tests.utils.TestUtils;
import fr.utbm.ciad.labmanager.utils.io.wos.OnlineWebOfSciencePlatform;
import fr.utbm.ciad.labmanager.utils.io.wos.WebOfSciencePlatform.WebOfScienceJournal;
import fr.utbm.ciad.labmanager.utils.io.wos.WebOfSciencePlatform.WebOfSciencePerson;
import fr.utbm.ciad.labmanager.utils.ranking.QuartileRanking;
import org.arakhne.afc.progress.DefaultProgression;
import org.arakhne.afc.progress.Progression;
import org.arakhne.afc.vmutil.Resources;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests for {@link OnlineWebOfSciencePlatform}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class OnlineWebOfSciencePlatformTest {

	private OnlineWebOfSciencePlatform test;

	private static boolean isNetworkEnable() {
		return TestUtils.isNetworkEnable();
	}

	@BeforeEach
	public void setUp() {
		this.test = new OnlineWebOfSciencePlatform();
	}

	private static void assertSimilar(float expected, float actual) {
		final int expected0 = (int) Math.floor(expected * 1000f);
		final int actual0 = (int) Math.floor(actual * 1000f);
		assertEquals(expected0, actual0);
	}

	@Test
	@DisplayName("getJournalRanking w/ local resource")
	public void getJournalRanking_intUrl() throws Exception {
		URL resourceUrl = Resources.getResource(getClass(), "wos_2021.csv");
		Assumptions.assumeTrue(resourceUrl != null);
		Progression progress = new DefaultProgression();
		//progress.addProgressionListener(new ProgressionConsoleMonitor());
		Map<String, WebOfScienceJournal> data0 = this.test.getJournalRanking(2021, resourceUrl, progress);
		assertNotNull(data0);
		assertEquals(22800, data0.size());
		WebOfScienceJournal data1 = data0.get("09521976");
		assertNotNull(data1);
		assertEquals(4, data1.quartiles.size());
		assertSame(QuartileRanking.Q1, data1.quartiles.get("engineering, multidisciplinary"));
		assertSame(QuartileRanking.Q1, data1.quartiles.get("automation & control systems"));
		assertSame(QuartileRanking.Q1, data1.quartiles.get("computer science, artificial intelligence"));
		assertSame(QuartileRanking.Q1, data1.quartiles.get("engineering, electrical & electronic"));
		assertSimilar(7.802f, data1.impactFactor);
	}

	@Test
	@DisplayName("getJournalRanking(journal id) w/ local resource")
	public void getJournalRanking_intUrlString() throws Exception {
		URL resourceUrl = Resources.getResource(getClass(), "wos_2021.csv");
		Assumptions.assumeTrue(resourceUrl != null);
		Progression progress = new DefaultProgression();
		//progress.addProgressionListener(new ProgressionConsoleMonitor());
		WebOfScienceJournal data0 = this.test.getJournalRanking(2021, resourceUrl, "09521976", progress);
		assertNotNull(data0);
		assertEquals(4, data0.quartiles.size());
		assertSame(QuartileRanking.Q1, data0.quartiles.get("engineering, multidisciplinary"));
		assertSame(QuartileRanking.Q1, data0.quartiles.get("automation & control systems"));
		assertSame(QuartileRanking.Q1, data0.quartiles.get("computer science, artificial intelligence"));
		assertSame(QuartileRanking.Q1, data0.quartiles.get("engineering, electrical & electronic"));
		assertSimilar(7.802f, data0.impactFactor);
	}

	@Test
	@EnabledIf("isNetworkEnable")
	@DisplayName("getPersonRanking")
	public void getPersonRanking() throws Exception {
		URL url = new URL("https://www.webofscience.com/wos/author/record/367073");
		WebOfSciencePerson person = this.test.getPersonRanking(url, null);
		assertNotNull(person);
		assertTrue(person.hindex >= 13, "Current Hindex=" + person.hindex);
		assertTrue(person.citations >= 651, "Current citations=" + person.citations);
	}

	@Test
	@DisplayName("getJournalUrl(\"2687\")")
	public void getJournalUrl() throws Exception {
		URL url = this.test.getJournalUrl("2687");
		assertNotNull(url);
		assertEquals("https://wos-journal.info/journalid/2687", url.toExternalForm());
	}

	@Test
	@DisplayName("getJournalUrl(null)")
	public void getJournalUrl_null() throws Exception {
		URL url = this.test.getJournalUrl(null);
		assertNull(url);
	}

	@Test
	@DisplayName("getJournalUrl(\"\")")
	public void getJournalUrl_empty() throws Exception {
		URL url = this.test.getJournalUrl("");
		assertNull(url);
	}

	@Test
	@EnabledIf("isNetworkEnable")
	@DisplayName("getJournalRanking  w/ remote resource")
	public void getJournalRanking_String_online() throws Exception {
		Progression progress = new DefaultProgression();
		//progress.addProgressionListener(new ProgressionConsoleMonitor());
		WebOfScienceJournal data0 = this.test.getJournalRanking("2687", progress);
		assertNotNull(data0);
		assertEquals(6.1f, data0.impactFactor);
		assertSame(QuartileRanking.Q1, data0.quartiles.get("computer science, artificial intelligence"));
		assertSame(QuartileRanking.Q1, data0.quartiles.get("engineering, biomedical"));
		assertSame(QuartileRanking.Q1, data0.quartiles.get("medical informatics"));
	}

}