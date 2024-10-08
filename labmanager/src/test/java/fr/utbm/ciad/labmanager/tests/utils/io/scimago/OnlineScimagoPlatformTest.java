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

package fr.utbm.ciad.labmanager.tests.utils.io.scimago;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.util.Map;

import fr.utbm.ciad.labmanager.tests.utils.TestUtils;
import fr.utbm.ciad.labmanager.utils.io.scimago.OnlineScimagoPlatform;
import fr.utbm.ciad.labmanager.utils.io.scimago.ScimagoPlatform;
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

/** Tests for {@link OnlineScimagoPlatform}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class OnlineScimagoPlatformTest {

	private OnlineScimagoPlatform test;

	@BeforeEach
	public void setUp() {
		this.test = new OnlineScimagoPlatform();
	}

	@Test
	@DisplayName("getJournalPictureUrl(null)")
	public void getJournalPictureUrl_null() {
		assertNull(this.test.getJournalPictureUrl(null));
	}

	@Test
	@DisplayName("getJournalPictureUrl(\"\")")
	public void getJournalPictureUrl_empty() {
		assertNull(this.test.getJournalPictureUrl(""));
	}

	@Test
	@DisplayName("getJournalPictureUrl(id)")
	public void getJournalPictureUrl() {
		URL url = this.test.getJournalPictureUrl("xyz");
		assertNotNull(url);
		assertEquals("https://www.scimagojr.com/journal_img.php?id=xyz", url.toExternalForm());
	}

	@Test
	@DisplayName("getJournalUrl(null)")
	public void getJournalUrl_null() {
		assertNull(this.test.getJournalUrl(null));
	}

	@Test
	@DisplayName("getJournalUrl(\"\")")
	public void getJournalUrl_empty() {
		assertNull(this.test.getJournalUrl(""));
	}

	@Test
	@DisplayName("getJournalUrl(id)")
	public void getJournalUrl() {
		URL url = this.test.getJournalUrl("xyz");
		assertNotNull(url);
		assertEquals("https://www.scimagojr.com/journalsearch.php?tip=sid&q=xyz", url.toExternalForm());
	}

	@Test
	@DisplayName("getJournalCsvUrl")
	public void getJournalCsvUrl() {
		URL url = this.test.getJournalCsvUrl(2021);
		assertNotNull(url);
		assertEquals("https://www.scimagojr.com/journalrank.php?out=xls&year=2021", url.toExternalForm());
	}

	@Test
	@DisplayName("getJournalRanking w/ local resource")
	public void getJournalRanking_intUrl() throws Exception {
		URL resourceUrl = Resources.getResource(getClass(), "scimagojr_2021.csv");
		Assumptions.assumeTrue(resourceUrl != null);
		Progression progress = new DefaultProgression();
		//progress.addProgressionListener(new ProgressionConsoleMonitor());
		Map<String, Map<String, QuartileRanking>> data0 = this.test.getJournalRanking(2021, resourceUrl, progress);
		assertNotNull(data0);
		assertEquals(26701, data0.size());
		Map<String, QuartileRanking> data1 = data0.get("21100386856");
		assertNotNull(data1);
		assertEquals(3, data1.size());
		assertSame(QuartileRanking.Q4, data1.get(ScimagoPlatform.BEST));
		assertSame(QuartileRanking.Q4, data1.get("literature and literary theory"));
		assertSame(QuartileRanking.Q4, data1.get("linguistics and language"));
	}

	@Test
	@DisplayName("getJournalRanking(journal id) w/ local resource")
	public void getJournalRanking_intUrlString() throws Exception {
		URL resourceUrl = Resources.getResource(getClass(), "scimagojr_2021.csv");
		Assumptions.assumeTrue(resourceUrl != null);
		Progression progress = new DefaultProgression();
		//progress.addProgressionListener(new ProgressionConsoleMonitor());
		Map<String, QuartileRanking> data0 = this.test.getJournalRanking(2021, resourceUrl, "21100386856", progress);
		assertNotNull(data0);
		assertEquals(3, data0.size());
		assertSame(QuartileRanking.Q4, data0.get(ScimagoPlatform.BEST));
		assertSame(QuartileRanking.Q4, data0.get("literature and literary theory"));
		assertSame(QuartileRanking.Q4, data0.get("linguistics and language"));
	}

	private static boolean isNetworkEnable() {
		return TestUtils.isNetworkEnable();
	}

	@Test
	@EnabledIf("isNetworkEnable")
	@DisplayName("getJournalRanking w/ remote resource")
	public void getJournalRanking_intUrl_online() throws Exception {
		Progression progress = new DefaultProgression();
		//progress.addProgressionListener(new ProgressionConsoleMonitor());
		Map<String, Map<String, QuartileRanking>> data0 = this.test.getJournalRanking(2021, progress);
		assertNotNull(data0);
		assertTrue(data0.size() >= 0, "Expected positive size, but has " + data0.size());
		Map<String, QuartileRanking> data1 = data0.get("21100386856");
		assertNotNull(data1);
		assertEquals(3, data1.size());
		assertSame(QuartileRanking.Q4, data1.get(ScimagoPlatform.BEST));
		assertSame(QuartileRanking.Q4, data1.get("literature and literary theory"));
		assertSame(QuartileRanking.Q4, data1.get("linguistics and language"));
	}

	@Test
	@EnabledIf("isNetworkEnable")
	@DisplayName("getJournalRanking(journal id) w/ remote resource")
	public void getJournalRanking_intUrlString_online() throws Exception {
		Progression progress = new DefaultProgression();
		//progress.addProgressionListener(new ProgressionConsoleMonitor());
		Map<String, QuartileRanking> data0 = this.test.getJournalRanking(2021, "21100386856", progress);
		assertNotNull(data0);
		assertEquals(3, data0.size());
		assertSame(QuartileRanking.Q4, data0.get(ScimagoPlatform.BEST));
		assertSame(QuartileRanking.Q4, data0.get("literature and literary theory"));
		assertSame(QuartileRanking.Q4, data0.get("linguistics and language"));
	}

}