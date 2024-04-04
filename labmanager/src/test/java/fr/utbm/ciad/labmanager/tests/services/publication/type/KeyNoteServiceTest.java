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
import fr.utbm.ciad.labmanager.data.publication.type.KeyNote;
import fr.utbm.ciad.labmanager.data.publication.type.KeyNoteRepository;
import fr.utbm.ciad.labmanager.services.publication.type.KeyNoteService;
import fr.utbm.ciad.labmanager.utils.doi.DefaultDoiTools;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.utils.io.hal.DefaultHalTools;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link KeyNoteService}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class KeyNoteServiceTest {

	private KeyNote pub0;

	private KeyNote pub1;

	private KeyNote pub2;

	private Publication base;

	private MessageSourceAccessor messages;

	private DownloadableFileManager downloadableFileManager;

	private KeyNoteRepository repository;

	private KeyNoteService test;

	@BeforeEach
	public void setUp() {
		this.messages = mock(MessageSourceAccessor.class);
		this.downloadableFileManager = mock(DownloadableFileManager.class);
		this.repository = mock(KeyNoteRepository.class);
		this.test = new KeyNoteService(this.downloadableFileManager, new DefaultDoiTools(), new DefaultHalTools(), this.repository, this.messages, new Constants(), mock(SessionFactory.class));

		// Prepare some publications to be inside the repository
		// The lenient configuration is used to configure the mocks for all the tests
		// at the same code location for configuration simplicity
		this.pub0 = mock(KeyNote.class);
		lenient().when(this.pub0.getId()).thenReturn(123l);
		this.pub1 = mock(KeyNote.class);
		lenient().when(this.pub1.getId()).thenReturn(234l);
		this.pub2 = mock(KeyNote.class);
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
	public void getAllKeyNotes() {
		final List<KeyNote> list = this.test.getAllKeyNotes();
		assertNotNull(list);
		assertEquals(3, list.size());
		assertSame(this.pub0, list.get(0));
		assertSame(this.pub1, list.get(1));
		assertSame(this.pub2, list.get(2));
	}

	@Test
	public void getKeyNote() {
		assertNull(this.test.getKeyNote(-4756));
		assertNull(this.test.getKeyNote(0));
		assertSame(this.pub0, this.test.getKeyNote(123));
		assertSame(this.pub1, this.test.getKeyNote(234));
		assertSame(this.pub2, this.test.getKeyNote(345));
		assertNull(this.test.getKeyNote(7896));
	}

	@Test
	public void createKeyNote() {
		Conference conf = mock(Conference.class);
		final KeyNote actual = this.test.createKeyNote(pub0,
				conf, 1234, "editors0", "orga0", "address0");

		assertSame(conf, actual.getConference());
		assertEquals(1234, actual.getConferenceOccurrenceNumber());
		assertEquals("editors0", actual.getEditors());
		assertEquals("orga0", actual.getOrganization());
		assertEquals("address0", actual.getAddress());

		verify(this.repository).save(actual);
	}

	@Test
	public void updateKeyNote() {
		Conference conf = mock(Conference.class);
		this.test.updateKeyNote(234,
				"title0", PublicationType.INTERNATIONAL_KEYNOTE, LocalDate.parse("2022-07-22"), 2022, "abstractText0",
				"keywords0", "doi:doi/0", "hal-123", "isbn0", "issn0", "dblpUrl0", "extraUrl0",
				PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0", "pathToVideo0",
				conf, 1234, "editors0", "orga0", "address0");

		verifyNoInteractions(this.pub0);

		verify(this.pub1).setConference(same(conf));
		verify(this.pub1).setConferenceOccurrenceNumber(eq(1234));
		verify(this.pub1).setEditors(eq("editors0"));
		verify(this.pub1).setOrganization(eq("orga0"));
		verify(this.pub1).setAddress(eq("address0"));

		verifyNoInteractions(this.pub2);
	}

	@Test
	public void removeKeyNote() {
		this.test.removeKeyNote(345);

		verify(this.repository).deleteById(345l);
	}

}
