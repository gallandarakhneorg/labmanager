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

package fr.ciadlab.labmanager.controller.journal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import fr.ciadlab.labmanager.configuration.BaseMessageSource;
import fr.ciadlab.labmanager.service.journal.JournalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.ModelAndView;

/** Tests for {@link JournalController}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class JournalControllerTest {

	private MessageSourceAccessor messages;

	private JournalService journalService;

	private JournalController test;

	@BeforeEach
	public void setUp() {
		this.messages = BaseMessageSource.getStaticMessageSourceAccessor();
		this.journalService = mock(JournalService.class);
		this.test = new JournalController(this.messages, this.journalService);
		this.test.setLogger(mock(Logger.class));
	}

	@Test
	public void showJournalList() {
		final ModelAndView mv = this.test.showJournalList();
		assertEquals("journalList", mv.getViewName());
	}

//	@Test
//	public void addJournal() throws Exception {
//		final HttpServletResponse response = mock(HttpServletResponse.class);
//		this.test.addJournal(response, "name0", "publisher0", "url0", "scimagoId0", "wosId0");
//		verify(this.journalService).createJournal("name0", "publisher0", "url0", "scimagoId0", "wosId0");
//		verify(response).sendRedirect("/SpringRestHibernate/journalList?created=true&message=name0");
//	}
//
//	@Test
//	public void getJournalData_unknownJournal() {
//		final Journal journal = this.test.getJournalData("xyz");
//		assertNull(journal);
//		verify(this.journalService).getJournalByName("xyz");
//	}
//
//	@Test
//	public void getJournalData_knownJournal() {
//		final Journal expected = mock(Journal.class);
//		when(this.journalService.getJournalByName("xyz")).thenReturn(expected);
//		final Journal journal = this.test.getJournalData("xyz");
//		assertSame(expected, journal);
//		verify(this.journalService).getJournalByName("xyz");
//	}
//
//	@Test
//	public void getJournalQualityIndicators_unknownJournal() {
//		final Map<String, String> indicators = this.test.getJournalQualityIndicators(2022, "xyz");
//		assertNull(indicators);
//		verify(this.journalService).getJournalByName("xyz");
//	}
//
//	@Test
//	public void getJournalQualityIndicators_knownJournal() {
//		final Journal expected = mock(Journal.class);
//		when(expected.getScimagoQIndexByYear(2022)).thenReturn(QuartileRanking.Q3);
//		when(expected.getWosQIndexByYear(2022)).thenReturn(QuartileRanking.Q2);
//		when(expected.getImpactFactorByYear(2022)).thenReturn(123.456f);
//		when(expected.hasQualityIndicatorsForYear(2022)).thenReturn(true);
//		when(this.journalService.getJournalByName("xyz")).thenReturn(expected);
//
//		final Map<String, String> indicators = this.test.getJournalQualityIndicators(2022, "xyz");
//
//		assertNotNull(indicators);
//		verify(this.journalService).getJournalByName("xyz");
//		verify(expected).getScimagoQIndexByYear(2022);
//		verify(expected).getWosQIndexByYear(2022);
//		verify(expected).getImpactFactorByYear(2022);
//
//		assertEquals(3, indicators.size());
//		assertEquals("Q3", indicators.get("scimagoQuartile"));
//		assertEquals("Q2", indicators.get("wosQuartile"));
//		assertEquals(123.456f, Float.parseFloat(indicators.get("impactFactor")));
//	}
//
//	@Test
//	public void editJournal_unknownJournal() throws Exception {
//		final HttpServletResponse response = mock(HttpServletResponse.class);
//		Locale.setDefault(Locale.US);
//
//		this.test.editJournal(response, "name0", "name1", "publisher1", "url1", "scimagoId1", "wosId1");
//
//		verify(this.journalService, never()).updateJournal(anyInt(), anyString(), anyString(), anyString(), anyString(), anyString());
//		verify(response).sendRedirect("/SpringRestHibernate/journalList?error=1&message=Journal+with+the+name+%27name0%27+was+not+found");
//	}
//
//	@Test
//	public void editJournal_knownJournal() throws Exception {
//		final HttpServletResponse response = mock(HttpServletResponse.class);
//		when(this.journalService.getJournalIdByName("name0")).thenReturn(123);
//
//		this.test.editJournal(response, "name0", "name1", "publisher1", "url1", "scimagoId1", "wosId1");
//
//		verify(this.journalService).updateJournal(123, "name1", "publisher1", "url1", "scimagoId1", "wosId1");
//		verify(response).sendRedirect("/SpringRestHibernate/journalList?updated=true&message=name0");
//	}

}
