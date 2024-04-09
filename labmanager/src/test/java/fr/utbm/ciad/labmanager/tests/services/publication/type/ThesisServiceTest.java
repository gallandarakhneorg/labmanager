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
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.data.publication.PublicationLanguage;
import fr.utbm.ciad.labmanager.data.publication.PublicationType;
import fr.utbm.ciad.labmanager.data.publication.type.Thesis;
import fr.utbm.ciad.labmanager.data.publication.type.ThesisRepository;
import fr.utbm.ciad.labmanager.services.publication.type.ThesisService;
import fr.utbm.ciad.labmanager.utils.doi.DefaultDoiTools;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.utils.io.hal.DefaultHalTools;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link ThesisService}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class ThesisServiceTest {

	private Thesis pub0;

	private Thesis pub1;

	private Thesis pub2;

	private Publication base;

	private MessageSourceAccessor messages;

	private DownloadableFileManager downloadableFileManager;

	private ThesisRepository repository;

	private ThesisService test;

	@BeforeEach
	public void setUp() {
		this.messages = mock(MessageSourceAccessor.class);
		this.downloadableFileManager = mock(DownloadableFileManager.class);
		this.repository = mock(ThesisRepository.class);
		this.test = new ThesisService(this.downloadableFileManager, new DefaultDoiTools(), new DefaultHalTools(), this.repository, this.messages, new Constants(), mock(SessionFactory.class));

		// Prepare some publications to be inside the repository
		// The lenient configuration is used to configure the mocks for all the tests
		// at the same code location for configuration simplicity
		this.pub0 = mock(Thesis.class);
		lenient().when(this.pub0.getId()).thenReturn(123l);
		this.pub1 = mock(Thesis.class);
		lenient().when(this.pub1.getId()).thenReturn(234l);
		this.pub2 = mock(Thesis.class);
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
	public void getAllTheses() {
		final List<Thesis> list = this.test.getAllTheses();
		assertNotNull(list);
		assertEquals(3, list.size());
		assertSame(this.pub0, list.get(0));
		assertSame(this.pub1, list.get(1));
		assertSame(this.pub2, list.get(2));
	}

	@Test
	public void getThesis() {
		assertNull(this.test.getThesis(-4756));
		assertNull(this.test.getThesis(0));
		assertSame(this.pub0, this.test.getThesis(123));
		assertSame(this.pub1, this.test.getThesis(234));
		assertSame(this.pub2, this.test.getThesis(345));
		assertNull(this.test.getThesis(7896));
	}

	@Test
	public void createThesis() {
		final Thesis actual = this.test.createThesis(pub0,
				"inst0", "address0");

		assertEquals("inst0", actual.getInstitution());
		assertEquals("address0", actual.getAddress());

		verify(this.repository).save(actual);
	}

	@Test
	public void updateThesis() {
		this.test.updateThesis(234,
				"title0", PublicationType.PHD_THESIS, LocalDate.parse("2022-07-22"), 2022, "abstractText0",
				"keywords0", "doi:doi/0", "hal-123", "isbn0", "issn0", "dblpUrl0", "extraUrl0",
				PublicationLanguage.ITALIAN, "pdfContent0", "awardContent0", "pathToVideo0",
				"inst0", "address0");

		verifyNoInteractions(this.pub0);

		verify(this.pub1).setInstitution("inst0");
		verify(this.pub1).setAddress("address0");

		verifyNoInteractions(this.pub2);
	}

	@Test
	public void removeThesis() {
		this.test.removeThesis(345);

		verify(this.repository).deleteById(345l);
	}

}
