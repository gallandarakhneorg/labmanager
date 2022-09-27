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

package fr.ciadlab.labmanager.service.member;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link PersonMergingService}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class PersonMergingServiceTest {

	private MessageSourceAccessor messages;

	private PersonRepository personRepository;

	private PersonService personService;

	private PersonMergingService test;
	
	@BeforeEach
	public void setUp() {
		this.messages = mock(MessageSourceAccessor.class);
		this.personRepository = mock(PersonRepository.class);
		this.personService = mock(PersonService.class);

		this.test = new PersonMergingService(this.messages, new Constants(), this.personRepository, this.personService);
	}

	@Test
	public void mergePersonsById_nullArguments() throws Exception {
		assertThrows(AssertionError.class, () -> {
			this.test.mergePersonsById(null, null);
		});
	}

	@Test
	public void mergePersonsById() throws Exception {
		Person pers0 = mock(Person.class, "pers0");
		when(pers0.getId()).thenReturn(12345);

		Set<Authorship> existingAuthorships0 = new HashSet<>();
		when(pers0.getAuthorships()).thenReturn(existingAuthorships0);
		
		Publication pub0 = mock(Publication.class);
		when(pub0.getAuthors()).thenReturn(Collections.singletonList(pers0));
		
		Authorship existingAuthorship0 = mock(Authorship.class);
		when(existingAuthorship0.getPublication()).thenReturn(pub0);
		existingAuthorships0.add(existingAuthorship0);

		Person pers3 = mock(Person.class, "pers3");
		when(pers3.getId()).thenReturn(34567548);

		Set<Authorship> existingAuthorships3 = new HashSet<>();
		when(pers3.getAuthorships()).thenReturn(existingAuthorships3);

		when(this.personRepository.findById(anyInt())).thenAnswer(it -> {
			switch (((Integer) it.getArgument(0)).intValue()) {
			case 12345:
				return Optional.of(pers0);
			case 34567548:
				return Optional.of(pers3);
			}
			return Optional.empty();
		});

		when(this.personRepository.findAllById(any(List.class))).thenAnswer(it -> {
			final List<Person> list = new ArrayList<>();
			for (final Integer id : (List<Integer>) it.getArgument(0)) {
				switch (id) {
				case 12345:
					list.add(pers0);
					break;
				}
			}
			return list;
		});
		
		this.test.mergePersonsById(Arrays.asList(12345), 34567548);

		verify(this.personService).removePerson(eq(12345));

		verify(this.personRepository).save(same(pers3));

		verify(existingAuthorship0).setPerson(same(pers3));
		
		assertTrue(existingAuthorships0.isEmpty());
		
		assertEquals(1, existingAuthorships3.size());
		assertSame(existingAuthorship0, existingAuthorships3.iterator().next());
	}

	@Test
	public void mergePersons_nullArguments() throws Exception {
		assertThrows(AssertionError.class, () -> {
			this.test.mergePersons(null, null);
		});
	}

	@Test
	public void mergePersons() throws Exception {
		Person pers0 = mock(Person.class, "pers0");
		when(pers0.getId()).thenReturn(12345);

		Set<Authorship> existingAuthorships0 = new HashSet<>();
		when(pers0.getAuthorships()).thenReturn(existingAuthorships0);
		
		Publication pub0 = mock(Publication.class);
		when(pub0.getAuthors()).thenReturn(Collections.singletonList(pers0));
		
		Authorship existingAuthorship0 = mock(Authorship.class);
		when(existingAuthorship0.getPublication()).thenReturn(pub0);
		existingAuthorships0.add(existingAuthorship0);
		
		Person pers3 = mock(Person.class, "pers3");
		when(pers3.getId()).thenReturn(34567548);

		Set<Authorship> existingAuthorships3 = new HashSet<>();
		when(pers3.getAuthorships()).thenReturn(existingAuthorships3);

		this.test.mergePersons(Arrays.asList(pers0), pers3);

		verify(this.personService).removePerson(eq(12345));

		verify(this.personRepository).save(same(pers3));

		verify(existingAuthorship0).setPerson(same(pers3));
		
		assertTrue(existingAuthorships0.isEmpty());
		
		assertEquals(1, existingAuthorships3.size());
		assertSame(existingAuthorship0, existingAuthorships3.iterator().next());
	}

}
