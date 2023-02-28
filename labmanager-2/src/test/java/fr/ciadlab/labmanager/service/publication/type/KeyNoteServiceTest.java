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

package fr.ciadlab.labmanager.service.publication.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.conference.Conference;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.publication.type.KeyNote;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.repository.publication.type.KeyNoteRepository;
import fr.ciadlab.labmanager.utils.doi.DefaultDoiTools;
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
		this.test = new KeyNoteService(this.messages, new Constants(), this.downloadableFileManager, new DefaultDoiTools(), this.repository);

		// Prepare some publications to be inside the repository
		// The lenient configuration is used to configure the mocks for all the tests
		// at the same code location for configuration simplicity
		this.pub0 = mock(KeyNote.class);
		lenient().when(this.pub0.getId()).thenReturn(123);
		this.pub1 = mock(KeyNote.class);
		lenient().when(this.pub1.getId()).thenReturn(234);
		this.pub2 = mock(KeyNote.class);
		lenient().when(this.pub2.getId()).thenReturn(345);

		lenient().when(this.repository.findAll()).thenReturn(
				Arrays.asList(this.pub0, this.pub1, this.pub2));
		lenient().when(this.repository.findById(anyInt())).thenAnswer(it -> {
			switch (((Integer) it.getArgument(0)).intValue()) {
			case 123:
				return Optional.of(this.pub0);
			case 234:
				return Optional.of(this.pub1);
			case 345:
				return Optional.of(this.pub2);
			}
			return Optional.empty();
		});

		this.base = mock(Publication.class);
		lenient().when(this.base.getId()).thenReturn(4567);
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
				"keywords0", "doi:doi/0", "isbn0", "issn0", "dblpUrl0", "extraUrl0",
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

		verify(this.repository).deleteById(345);
	}

}
