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

package fr.utbm.ciad.labmanager.tests.services.publication.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.conference.Conference;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.data.publication.PublicationLanguage;
import fr.utbm.ciad.labmanager.data.publication.PublicationType;
import fr.utbm.ciad.labmanager.data.publication.type.ConferencePaper;
import fr.utbm.ciad.labmanager.data.publication.type.ConferencePaperRepository;
import fr.utbm.ciad.labmanager.services.member.MembershipService;
import fr.utbm.ciad.labmanager.services.publication.type.ConferencePaperService;
import fr.utbm.ciad.labmanager.utils.doi.DefaultDoiTools;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.utils.io.hal.DefaultHalTools;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link ConferencePaperService}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class ConferencePaperServiceTest {

	private ConferencePaper pub0;

	private ConferencePaper pub1;

	private ConferencePaper pub2;

	private Publication base;

	private MessageSourceAccessor messages;

	private DownloadableFileManager downloadableFileManager;

	private ConferencePaperRepository repository;

	private MembershipService membershipService;

	private ConferencePaperService test;

	@BeforeEach
	public void setUp() {
		this.messages = mock(MessageSourceAccessor.class);
		this.downloadableFileManager = mock(DownloadableFileManager.class);
		this.repository = mock(ConferencePaperRepository.class);
		this.membershipService = mock(MembershipService.class);
		this.test = new ConferencePaperService(this.downloadableFileManager, new DefaultDoiTools(), new DefaultHalTools(), this.repository,
				this.membershipService, this.messages, new Constants(), mock(SessionFactory.class));

		// Prepare some publications to be inside the repository
		// The lenient configuration is used to configure the mocks for all the tests
		// at the same code location for configuration simplicity
		this.pub0 = mock(ConferencePaper.class);
		lenient().when(this.pub0.getId()).thenReturn(123l);
		this.pub1 = mock(ConferencePaper.class);
		lenient().when(this.pub1.getId()).thenReturn(234l);
		this.pub2 = mock(ConferencePaper.class);
		lenient().when(this.pub2.getId()).thenReturn(345l);

		lenient().when(this.repository.findAll()).thenReturn(
				Arrays.asList(this.pub0, this.pub1, this.pub2));
		lenient().when(this.repository.findById(anyLong())).thenAnswer(it -> {
			var n = ((Number) it.getArgument(0)).longValue();
			if (n == 123l) {
				return Optional.of(this.pub0);
			} else if (n == 234l) {
				return Optional.of(this.pub1);
			} else if (n == 345l) {
				return Optional.of(this.pub2);
			}
			return Optional.empty();
		});

		this.base = mock(Publication.class);
		lenient().when(this.base.getId()).thenReturn(4567l);
	}

	@Test
	public void getAllConferencePapers() {
		final List<ConferencePaper> list = this.test.getAllConferencePapers();
		assertNotNull(list);
		assertEquals(3, list.size());
		assertSame(this.pub0, list.get(0));
		assertSame(this.pub1, list.get(1));
		assertSame(this.pub2, list.get(2));
	}

	@Test
	public void getConferencePaper() {
		assertNull(this.test.getConferencePaper(-4756));
		assertNull(this.test.getConferencePaper(0));
		assertSame(this.pub0, this.test.getConferencePaper(123));
		assertSame(this.pub1, this.test.getConferencePaper(234));
		assertSame(this.pub2, this.test.getConferencePaper(345));
		assertNull(this.test.getConferencePaper(7896));
	}

	@Test
	public void createConferencePaper() {
		Conference conf = mock(Conference.class);
		final ConferencePaper actual = this.test.createConferencePaper(pub0,
				conf, 1234, "volume0", "number0", "pages0", "editors0",
				"series0", "orga0", "address0");

		assertSame(conf, actual.getConference());
		assertEquals(1234, actual.getConferenceOccurrenceNumber());
		assertEquals("volume0", actual.getVolume());
		assertEquals("number0", actual.getNumber());
		assertEquals("pages0", actual.getPages());
		assertEquals("editors0", actual.getEditors());
		assertEquals("address0", actual.getAddress());
		assertEquals("series0", actual.getSeries());
		assertEquals("orga0", actual.getOrganization());
		assertEquals("address0", actual.getAddress());

		verify(this.repository).save(actual);
	}

	@Test
	public void updateConferencePaper() {
		Conference conf = mock(Conference.class);
		this.test.updateConferencePaper(234,
				"title0", PublicationType.NATIONAL_CONFERENCE_PAPER, LocalDate.parse("2022-07-22"), 2022, "abstractText0",
				"keywords0", "doi:doi/0", "hal-123", "isbn0", "issn0", "dblpUrl0", "extraUrl0",
				PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0", "pathToVideo0",
				conf, 1234, "volume0", "number0", "pages0", "editors0",
				"series0", "orga0", "address0");

		verifyNoInteractions(this.pub0);

		verify(this.pub1).setConference(same(conf));
		verify(this.pub1).setConferenceOccurrenceNumber(eq(1234));
		verify(this.pub1).setVolume(eq("volume0"));
		verify(this.pub1).setNumber(eq("number0"));
		verify(this.pub1).setPages(eq("pages0"));
		verify(this.pub1).setEditors(eq("editors0"));
		verify(this.pub1).setSeries(eq("series0"));
		verify(this.pub1).setOrganization(eq("orga0"));
		verify(this.pub1).setAddress(eq("address0"));

		verifyNoInteractions(this.pub2);
	}

	@Test
	public void removeConferencePaper() {
		this.test.removeConferencePaper(345);

		verify(this.repository).deleteById(345l);
	}

}
