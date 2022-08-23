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

package fr.ciadlab.labmanager.service.publication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.Sets;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.publication.AuthorshipRepository;
import fr.ciadlab.labmanager.repository.publication.PublicationRepository;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.utils.names.DefaultPersonNameParser;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link AuthorshipService}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class AuthorshipServiceTest {

	private Person pers0;

	private Person pers1;

	private Person pers2;

	private Publication pub0;

	private Publication pub1;

	private Authorship a0;

	private Authorship a1;

	private Authorship a2;

	private MessageSourceAccessor messages;

	private PublicationRepository publicationRepository;

	private AuthorshipRepository authorshipRepository;

	private PersonRepository personRepository;

	private PersonService personService;

	private AuthorshipService test;

	@BeforeEach
	public void setUp() {
		this.messages = mock(MessageSourceAccessor.class);
		this.publicationRepository = mock(PublicationRepository.class);
		this.authorshipRepository = mock(AuthorshipRepository.class);
		this.personRepository = mock(PersonRepository.class);
		this.personService = mock(PersonService.class);
		this.test = new AuthorshipService(this.messages, this.publicationRepository, this.authorshipRepository,
				this.personRepository, this.personService);

		// Prepare some publications to be inside the repository
		// The lenient configuration is used to configure the mocks for all the tests
		// at the same code location for configuration simplicity
		this.pers0 = mock(Person.class, "pers0");
		lenient().when(this.pers0.getId()).thenReturn(12345);
		lenient().when(this.pers0.getFirstName()).thenReturn("F0");
		lenient().when(this.pers0.getLastName()).thenReturn("L0");
		this.pers1 = mock(Person.class, "pers1");
		lenient().when(this.pers1.getId()).thenReturn(23456);
		lenient().when(this.pers1.getFirstName()).thenReturn("F1");
		lenient().when(this.pers1.getLastName()).thenReturn("L1");
		this.pers2 = mock(Person.class, "pers2");
		lenient().when(this.pers2.getId()).thenReturn(34567);
		lenient().when(this.pers2.getFirstName()).thenReturn("F3");
		lenient().when(this.pers2.getLastName()).thenReturn("L3");

		this.pub0 = mock(Publication.class);
		lenient().when(this.pub0.getId()).thenReturn(1234);
		this.pub1 = mock(Publication.class);
		lenient().when(this.pub1.getId()).thenReturn(2345);

		this.a0 = mock(Authorship.class);
		lenient().when(this.a0.getId()).thenReturn(123);
		lenient().when(this.a0.getPublication()).thenReturn(this.pub0);
		lenient().when(this.a0.getPerson()).thenReturn(this.pers0);
		lenient().when(this.a0.getAuthorRank()).thenReturn(1);
		this.a1 = mock(Authorship.class);
		lenient().when(this.a1.getId()).thenReturn(234);
		lenient().when(this.a1.getPublication()).thenReturn(this.pub1);
		lenient().when(this.a1.getPerson()).thenReturn(this.pers1);
		lenient().when(this.a1.getAuthorRank()).thenReturn(0);
		this.a2 = mock(Authorship.class);
		lenient().when(this.a2.getId()).thenReturn(345);
		lenient().when(this.a2.getPublication()).thenReturn(this.pub0);
		lenient().when(this.a2.getPerson()).thenReturn(this.pers1);
		lenient().when(this.a2.getAuthorRank()).thenReturn(0);

		lenient().when(this.pub0.getAuthorships()).thenReturn(Arrays.asList(this.a2, this.a0));
		lenient().when(this.pub1.getAuthorships()).thenReturn(Arrays.asList(this.a1));

		lenient().when(this.pers0.getAuthorships()).thenReturn(Collections.singleton(this.a0));
		lenient().when(this.pers1.getAuthorships()).thenReturn(Sets.newHashSet(this.a1, this.a2));

		lenient().when(this.authorshipRepository.findAll()).thenReturn(
				Arrays.asList(this.a0, this.a1, this.a2));
		lenient().when(this.authorshipRepository.findByPersonIdAndPublicationId(anyInt(), anyInt())).then(it -> {
			final int personId = ((Integer) it.getArgument(0)).intValue();
			final int publicationId = ((Integer) it.getArgument(1)).intValue();
			switch (personId) {
			case 12345:
				switch (publicationId) {
				case 1234:
					return Optional.of(this.a0);
				}
				break;
			case 23456:
				switch (publicationId) {
				case 1234:
					return Optional.of(this.a2);
				case 2345:
					return Optional.of(this.a1);
				}
				break;
			}
			return Optional.empty();
		});
		lenient().when(this.personRepository.findByAuthorshipsPublicationIdOrderByAuthorshipsAuthorRank(anyInt())).thenAnswer(it -> {
			switch (((Integer) it.getArgument(0)).intValue()) {
			case 1234:
				return Arrays.asList(this.pers1, this.pers0);
			case 2345:
				return Collections.singletonList(this.pers1);
			}
			return Collections.emptyList();
		});
		lenient().when(this.personRepository.findById(anyInt())).thenAnswer(it -> {
			switch (((Integer) it.getArgument(0)).intValue()) {
			case 12345:
				return Optional.of(this.pers0);
			case 23456:
				return Optional.of(this.pers1);
			case 34567:
				return Optional.of(this.pers2);
			}
			return Optional.empty();
		});
		lenient().when(this.personRepository.findAllById(any(List.class))).thenAnswer(it -> {
			final List<Person> list = new ArrayList<>();
			for (final Integer id : (List<Integer>) it.getArgument(0)) {
				switch (id) {
				case 12345:
					list.add(this.pers0);
					break;
				case 23456:
					list.add(this.pers1);
					break;
				case 34567:
					list.add(this.pers2);
					break;
				}
			}
			return list;
		});
		lenient().when(this.personRepository.findByFirstNameAndLastName(anyString(), anyString())).thenAnswer(it -> {
			if ("F0".equals(it.getArgument(0).toString()) && "L0".equals(it.getArgument(1).toString())) {
				return Collections.singleton(this.pers0);
			}
			if ("F1".equals(it.getArgument(0).toString()) && "L1".equals(it.getArgument(1).toString())) {
				return Collections.singleton(this.pers1);
			}
			if ("F2".equals(it.getArgument(0).toString()) && "L2".equals(it.getArgument(1).toString())) {
				return Collections.singleton(this.pers2);
			}
			return Collections.emptySet();
		});
		lenient().when(this.publicationRepository.findAllByAuthorshipsPersonId(anyInt())).thenAnswer(it -> {
			switch (((Integer) it.getArgument(0)).intValue()) {
			case 12345:
				return Collections.singletonList(this.pub0);
			case 23456:
				return Arrays.asList(this.pub0, this.pub1);
			}
			return Collections.emptyList();
		});
		lenient().when(this.publicationRepository.findById(anyInt())).thenAnswer(it -> {
			switch (((Integer) it.getArgument(0)).intValue()) {
			case 1234:
				return Optional.of(this.pub0);
			case 2345:
				return Optional.of(this.pub1);
			}
			return Optional.empty();
		});
	}

	@Test
	public void getAuthorsFor() {
		final List<Person> list0 = this.test.getAuthorsFor(0);
		assertTrue(list0.isEmpty());

		final List<Person> list1 = this.test.getAuthorsFor(1234);
		assertEquals(2, list1.size());
		assertSame(this.pers1, list1.get(0));
		assertSame(this.pers0, list1.get(1));

		final List<Person> list2 = this.test.getAuthorsFor(2345);
		assertEquals(1, list2.size());
		assertTrue(list2.contains(this.pers1));
	}

	@Test
	public void addAuthorship_IntIntInt_oldAuthorship() {
		final Authorship autship = this.test.addAuthorship(12345, 1234, 1, true);
		assertNotNull(autship);
		final ArgumentCaptor<Authorship> arg = ArgumentCaptor.forClass(Authorship.class);
		verify(this.authorshipRepository, times(1)).save(arg.capture());
	}

	@Test
	public void addAuthorship_IntIntInt_newAuthorship_insertAsFirstAuthor() {
		final Authorship autship = this.test.addAuthorship(12345, 2345, 0, true);
		assertNotNull(autship);

		final ArgumentCaptor<Authorship> arg = ArgumentCaptor.forClass(Authorship.class);
		verify(this.authorshipRepository, atLeastOnce()).save(arg.capture());
		final Authorship actual = arg.getValue();
		assertNotNull(actual);
		assertSame(this.pers0, actual.getPerson());
		assertSame(this.pub1, actual.getPublication());
		assertEquals(0, actual.getAuthorRank());
	}

	@Test
	public void addAuthorship_IntIntInt_newAuthorship_insertAsSecondAuthor() {
		final Authorship autship = this.test.addAuthorship(12345, 2345, 1, true);
		assertNotNull(autship);

		final ArgumentCaptor<Authorship> arg = ArgumentCaptor.forClass(Authorship.class);
		verify(this.authorshipRepository, atLeastOnce()).save(arg.capture());
		final Authorship actual = arg.getValue();
		assertNotNull(actual);
		assertSame(this.pers0, actual.getPerson());
		assertSame(this.pub1, actual.getPublication());
		assertEquals(0, actual.getAuthorRank());
	}

	@Test
	public void addAuthorship_IntIntInt_newAuthorship_insertWithOutRangedRank() {
		final Authorship autship = this.test.addAuthorship(12345, 2345, 100, true);
		assertNotNull(autship);

		final ArgumentCaptor<Authorship> arg = ArgumentCaptor.forClass(Authorship.class);
		verify(this.authorshipRepository, atLeastOnce()).save(arg.capture());
		final Authorship actual = arg.getValue();
		assertNotNull(actual);
		assertSame(this.pers0, actual.getPerson());
		assertSame(this.pub1, actual.getPublication());
		assertEquals(0, actual.getAuthorRank());
	}

	@Test
	public void mergePersonsById_nullArguments() throws Exception {
		assertThrows(AssertionError.class, () -> {
			this.test.mergePersonsById(null, null);
		});
	}

	@Test
	public void mergePersonsById() throws Exception {
		Person pers3 = mock(Person.class, "pers3");
		lenient().when(pers3.getId()).thenReturn(34567548);
		lenient().when(pers3.getFirstName()).thenReturn("F3");
		lenient().when(pers3.getLastName()).thenReturn("L3");

		when(this.personRepository.findById(anyInt())).thenAnswer(it -> {
			switch (((Integer) it.getArgument(0)).intValue()) {
			case 12345:
				return Optional.of(this.pers0);
			case 34567548:
				return Optional.of(pers3);
			}
			return Optional.empty();
		});

		this.test.mergePersonsById(Arrays.asList(12345), 34567548);

		verify(this.pers0).deleteAllAuthorships();
		verify(this.personService).removePerson(eq(12345));

		final ArgumentCaptor<Authorship> arg0 = ArgumentCaptor.forClass(Authorship.class);
		verify(this.authorshipRepository, atLeastOnce()).save(arg0.capture());
		final Authorship newAuthorship = arg0.getValue();
		assertNotNull(newAuthorship);
		assertEquals(34567548, newAuthorship.getPerson().getId());
		assertEquals(1234, newAuthorship.getPublication().getId());
		assertEquals(0, newAuthorship.getAuthorRank());
	}

	@Test
	public void mergePersons_nullArguments() throws Exception {
		assertThrows(AssertionError.class, () -> {
			this.test.mergePersons(null, null);
		});
	}

	@Test
	public void mergePersons() throws Exception {
		Person pers3 = mock(Person.class, "pers3");
		lenient().when(pers3.getId()).thenReturn(34567548);
		lenient().when(pers3.getFirstName()).thenReturn("F3");
		lenient().when(pers3.getLastName()).thenReturn("L3");

		when(this.personRepository.findById(anyInt())).thenAnswer(it -> {
			switch (((Integer) it.getArgument(0)).intValue()) {
			case 12345:
				return Optional.of(this.pers0);
			case 34567548:
				return Optional.of(pers3);
			}
			return Optional.empty();
		});

		this.test.mergePersons(Arrays.asList(this.pers0), pers3);

		verify(this.pers0).deleteAllAuthorships();
		verify(this.personService).removePerson(eq(12345));

		final ArgumentCaptor<Authorship> arg0 = ArgumentCaptor.forClass(Authorship.class);
		verify(this.authorshipRepository, atLeastOnce()).save(arg0.capture());
		final Authorship newAuthorship = arg0.getValue();
		assertNotNull(newAuthorship);
		assertEquals(34567548, newAuthorship.getPerson().getId());
		assertEquals(1234, newAuthorship.getPublication().getId());
		assertEquals(0, newAuthorship.getAuthorRank());
	}

}
