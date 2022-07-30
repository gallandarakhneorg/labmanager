/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.service.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.publication.AuthorshipRepository;
import fr.ciadlab.labmanager.repository.publication.PublicationRepository;
import fr.ciadlab.labmanager.utils.names.DefaultPersonNameParser;
import fr.ciadlab.labmanager.utils.names.PersonNameComparator;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import fr.ciadlab.labmanager.utils.names.SorensenDicePersonNameComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests for {@link PersonService}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

	private Person pers0;

	private Person pers1;

	private Person pers2;

	private Person pers3;

	private PublicationRepository publicationRepository;

	private AuthorshipRepository authorshipRepository;

	private PersonRepository personRepository;

	private PersonNameParser nameParser;

	private PersonNameComparator nameComparator;

	private PersonService test;

	@BeforeEach
	public void setUp() {
		this.publicationRepository = mock(PublicationRepository.class);
		this.authorshipRepository = mock(AuthorshipRepository.class);
		this.personRepository = mock(PersonRepository.class);
		// Create a real parser instance to be used in the test
		this.nameParser = new DefaultPersonNameParser();
		// Create a real comparator instance to be used in the test
		this.nameComparator = new SorensenDicePersonNameComparator(this.nameParser);
		this.test = new PersonService(this.publicationRepository, this.authorshipRepository, this.personRepository,
				this.nameParser, this.nameComparator);

		// Prepare some persons to be inside the repository
		// The lenient configuration is used to configure the mocks for all the tests
		// at the same code location for configuration simplicity
		this.pers0 = mock(Person.class);
		lenient().when(this.pers0.getId()).thenReturn(123);
		lenient().when(this.pers0.getFirstName()).thenReturn("F1");
		lenient().when(this.pers0.getLastName()).thenReturn("L1");
		this.pers1 = mock(Person.class);
		lenient().when(this.pers1.getId()).thenReturn(234);
		lenient().when(this.pers1.getFirstName()).thenReturn("F2");
		lenient().when(this.pers1.getLastName()).thenReturn("L2");
		this.pers2 = mock(Person.class);
		lenient().when(this.pers2.getId()).thenReturn(345);
		lenient().when(this.pers2.getFirstName()).thenReturn("F3");
		lenient().when(this.pers2.getLastName()).thenReturn("L3");
		this.pers3 = mock(Person.class);
		lenient().when(this.pers3.getId()).thenReturn(456);
		lenient().when(this.pers3.getFirstName()).thenReturn("F4");
		lenient().when(this.pers3.getLastName()).thenReturn("L4");

		lenient().when(this.personRepository.findAll()).thenReturn(
				Arrays.asList(this.pers0, this.pers1, this.pers2, this.pers3));
		lenient().when(this.personRepository.findById(anyInt())).then(it -> {
			switch (((Integer) it.getArgument(0)).intValue()) {
			case 123:
				return Optional.of(this.pers0);
			case 234:
				return Optional.of(this.pers1);
			case 345:
				return Optional.of(this.pers2);
			case 456:
				return Optional.of(this.pers3);
			}
			return Optional.empty();
		});
		lenient().when(this.personRepository.findByFirstNameAndLastName(anyString(), anyString())).then(it -> {
			if ("F1".equals(it.getArgument(0)) && "L1".equals(it.getArgument(1))) {
				return Collections.singleton(this.pers0);
			}
			if ("F2".equals(it.getArgument(0)) && "L2".equals(it.getArgument(1))) {
				return Collections.singleton(this.pers1);
			}
			if ("F3".equals(it.getArgument(0)) && "L3".equals(it.getArgument(1))) {
				return Collections.singleton(this.pers2);
			}
			if ("F4".equals(it.getArgument(0)) && "L4".equals(it.getArgument(1))) {
				return Collections.singleton(this.pers3);
			}
			return Collections.emptySet();
		});
	}

	@Test
	public void getAllPersons() {
		final List<Person> list = this.test.getAllPersons();
		assertNotNull(list);
		assertEquals(4, list.size());
		assertSame(this.pers0, list.get(0));
		assertSame(this.pers1, list.get(1));
		assertSame(this.pers2, list.get(2));
		assertSame(this.pers3, list.get(3));
	}

	@Test
	public void getPerson() {
		assertNull(this.test.getPerson(-4756));
		assertNull(this.test.getPerson(0));
		assertSame(this.pers0, this.test.getPerson(123));
		assertSame(this.pers1, this.test.getPerson(234));
		assertSame(this.pers2, this.test.getPerson(345));
		assertSame(this.pers3, this.test.getPerson(456));
		assertNull(this.test.getPerson(7896));
	}

	@Test
	public void getPersonIdByName() {
		assertEquals(0, this.test.getPersonIdByName(null, null));
		assertEquals(0, this.test.getPersonIdByName(null, ""));
		assertEquals(0, this.test.getPersonIdByName("", null));
		assertEquals(0, this.test.getPersonIdByName(null, "x"));
		assertEquals(0, this.test.getPersonIdByName("x", null));
		assertEquals(0, this.test.getPersonIdByName("", "x"));
		assertEquals(0, this.test.getPersonIdByName("x", ""));
		assertEquals(0, this.test.getPersonIdByName("", ""));
		assertEquals(0, this.test.getPersonIdByName("x", "y"));
		assertEquals(123, this.test.getPersonIdByName("F1", "L1"));
		assertEquals(234, this.test.getPersonIdByName("F2", "L2"));
		assertEquals(345, this.test.getPersonIdByName("F3", "L3"));
		assertEquals(456, this.test.getPersonIdByName("F4", "L4"));
	}

	@Test
	public void getPersonBySimilarName() {
		assertNull(this.test.getPersonBySimilarName(null, null));
		assertNull(this.test.getPersonBySimilarName(null, ""));
		assertNull(this.test.getPersonBySimilarName("", null));
		assertNull(this.test.getPersonBySimilarName(null, "x"));
		assertNull(this.test.getPersonBySimilarName("x", null));
		assertNull(this.test.getPersonBySimilarName("", "x"));
		assertNull(this.test.getPersonBySimilarName("x", ""));
		assertNull(this.test.getPersonBySimilarName("", ""));
		assertNull(this.test.getPersonBySimilarName("x", "y"));
		assertSame(this.pers0, this.test.getPersonBySimilarName("F1", "L1"));
		assertSame(this.pers1, this.test.getPersonBySimilarName("F2", "L2"));
		assertSame(this.pers2, this.test.getPersonBySimilarName("F3", "L3"));
		assertSame(this.pers3, this.test.getPersonBySimilarName("F4", "L4"));
		assertSame(this.pers0, this.test.getPersonBySimilarName("F.", "L1"));
		assertSame(this.pers1, this.test.getPersonBySimilarName("F.", "L2"));
		assertSame(this.pers2, this.test.getPersonBySimilarName("F.", "L3"));
		assertSame(this.pers3, this.test.getPersonBySimilarName("F.", "L4"));
		assertSame(this.pers0, this.test.getPersonBySimilarName("F", "L1"));
		assertSame(this.pers1, this.test.getPersonBySimilarName("F", "L2"));
		assertSame(this.pers2, this.test.getPersonBySimilarName("F", "L3"));
		assertSame(this.pers3, this.test.getPersonBySimilarName("F", "L4"));
	}

	@Test
	public void getPersonIdBySimilarName() {
		assertEquals(0, this.test.getPersonIdBySimilarName(null, null));
		assertEquals(0, this.test.getPersonIdBySimilarName(null, ""));
		assertEquals(0, this.test.getPersonIdBySimilarName("", null));
		assertEquals(0, this.test.getPersonIdBySimilarName(null, "x"));
		assertEquals(0, this.test.getPersonIdBySimilarName("x", null));
		assertEquals(0, this.test.getPersonIdBySimilarName("", "x"));
		assertEquals(0, this.test.getPersonIdBySimilarName("x", ""));
		assertEquals(0, this.test.getPersonIdBySimilarName("", ""));
		assertEquals(0, this.test.getPersonIdBySimilarName("x", "y"));
		assertEquals(123, this.test.getPersonIdBySimilarName("F1", "L1"));
		assertEquals(234, this.test.getPersonIdBySimilarName("F2", "L2"));
		assertEquals(345, this.test.getPersonIdBySimilarName("F3", "L3"));
		assertEquals(456, this.test.getPersonIdBySimilarName("F4", "L4"));
		assertEquals(123, this.test.getPersonIdBySimilarName("F.", "L1"));
		assertEquals(234, this.test.getPersonIdBySimilarName("F.", "L2"));
		assertEquals(345, this.test.getPersonIdBySimilarName("F.", "L3"));
		assertEquals(456, this.test.getPersonIdBySimilarName("F.", "L4"));
		assertEquals(123, this.test.getPersonIdBySimilarName("F", "L1"));
		assertEquals(234, this.test.getPersonIdBySimilarName("F", "L2"));
		assertEquals(345, this.test.getPersonIdBySimilarName("F", "L3"));
		assertEquals(456, this.test.getPersonIdBySimilarName("F", "L4"));
	}

	@Test
	public void createPerson() {
		final int id = this.test.createPerson("NFN", "NLN", "NE");

		final ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
		verify(this.personRepository, only()).save(arg.capture());
		final Person actual = arg.getValue();
		assertNotNull(actual);
		assertEquals(id, actual.getId());
		assertEquals("NFN", actual.getFirstName());
		assertEquals("NLN", actual.getLastName());
		assertEquals("NE", actual.getEmail());
	}

	@Test
	public void updatePerson() {
		this.test.updatePerson(234, "NFN", "NLN", "NE");

		final ArgumentCaptor<Integer> arg0 = ArgumentCaptor.forClass(Integer.class);
		verify(this.personRepository, atLeastOnce()).findById(arg0.capture());
		Integer actual0 = arg0.getValue();
		assertNotNull(actual0);
		assertEquals(234, actual0);

		final ArgumentCaptor<Person> arg1 = ArgumentCaptor.forClass(Person.class);
		verify(this.personRepository, atLeastOnce()).save(arg1.capture());
		final Person actual1 = arg1.getValue();
		assertSame(this.pers1, actual1);

		final ArgumentCaptor<String> arg2 = ArgumentCaptor.forClass(String.class);

		verify(this.pers1, atLeastOnce()).setFirstName(arg2.capture());
		assertEquals("NFN", arg2.getValue());

		verify(this.pers1, atLeastOnce()).setLastName(arg2.capture());
		assertEquals("NLN", arg2.getValue());

		verify(this.pers1, atLeastOnce()).setEmail(arg2.capture());
		assertEquals("NE", arg2.getValue());
	}

	@Test
	public void removePerson() {
		this.test.removePerson(234);

		final ArgumentCaptor<Integer> arg = ArgumentCaptor.forClass(Integer.class);

		verify(this.personRepository, atLeastOnce()).findById(arg.capture());
		Integer actual = arg.getValue();
		assertNotNull(actual);
		assertEquals(234, actual);

		verify(this.personRepository, atLeastOnce()).deleteById(arg.capture());
		actual = arg.getValue();
		assertNotNull(actual);
		assertEquals(234, actual);
	}

	@Test
	public void extractPersonsFrom_null() {
		List<Person> list = this.test.extractPersonsFrom(null);
		assertTrue(list.isEmpty());
	}

	@Test
	public void extractPersonsFrom_empty() {
		List<Person> list = this.test.extractPersonsFrom("");
		assertTrue(list.isEmpty());
	}

	@Test
	public void extractPersonsFrom_unkwownPersons() {
		List<Person> list = this.test.extractPersonsFrom("L1a, F1a and L2a, F2a and L3a, F3a");
		assertEquals(3, list.size());
		assertEquals(0, list.get(0).getId());
		assertEquals("F1a", list.get(0).getFirstName());
		assertEquals("L1a", list.get(0).getLastName());
		assertEquals(0, list.get(1).getId());
		assertEquals("F2a", list.get(1).getFirstName());
		assertEquals("L2a", list.get(1).getLastName());
		assertEquals(0, list.get(2).getId());
		assertEquals("F3a", list.get(2).getFirstName());
		assertEquals("L3a", list.get(2).getLastName());
	}

	@Test
	public void extractPersonsFrom_kwownPersons() {
		List<Person> list = this.test.extractPersonsFrom("L1, F1 and L2, F2 and L3, F3");
		assertEquals(3, list.size());
		assertEquals(123, list.get(0).getId());
		assertEquals("F1", list.get(0).getFirstName());
		assertEquals("L1", list.get(0).getLastName());
		assertEquals(234, list.get(1).getId());
		assertEquals("F2", list.get(1).getFirstName());
		assertEquals("L2", list.get(1).getLastName());
		assertEquals(345, list.get(2).getId());
		assertEquals("F3", list.get(2).getFirstName());
		assertEquals("L3", list.get(2).getLastName());
	}

	@Test
	public void extractPersonsFrom_kwownAndUknownPersons() {
		List<Person> list = this.test.extractPersonsFrom("L1, F1 and L2a, F2a and L3, F3");
		assertEquals(3, list.size());
		assertEquals(123, list.get(0).getId());
		assertEquals("F1", list.get(0).getFirstName());
		assertEquals("L1", list.get(0).getLastName());
		assertEquals(0, list.get(1).getId());
		assertEquals("F2a", list.get(1).getFirstName());
		assertEquals("L2a", list.get(1).getLastName());
		assertEquals(345, list.get(2).getId());
		assertEquals("F3", list.get(2).getFirstName());
		assertEquals("L3", list.get(2).getLastName());
	}

	@Test
	public void computePersonDuplicate_noDuplicate() {
		List<Set<Person>> duplicate = this.test.computePersonDuplicate();
		assertTrue(duplicate.isEmpty());
	}

	@Test
	public void computePersonDuplicate_oneDuplicate() {
		Person pers0b = mock(Person.class);
		when(pers0b.getFirstName()).thenReturn("F1");
		when(pers0b.getLastName()).thenReturn("L1");

		Person pers2b = mock(Person.class);
		when(pers2b.getFirstName()).thenReturn("F3");
		when(pers2b.getLastName()).thenReturn("L3");

		Person pers2c = mock(Person.class);
		when(pers2c.getFirstName()).thenReturn("F3");
		when(pers2c.getLastName()).thenReturn("L3");

		when(this.personRepository.findAll()).thenReturn(
				Arrays.asList(this.pers0, pers2c, this.pers1, pers0b, this.pers2, this.pers3, pers2b));

		List<Set<Person>> duplicate = this.test.computePersonDuplicate();

		assertEquals(2, duplicate.size());

		Set<Person> set1 = duplicate.get(0);
		assertSet(set1, this.pers0, pers0b);

		Set<Person> set2 = duplicate.get(1);
		assertSet(set2, this.pers2, pers2b, pers2c);
	}

	private void assertSet(Set<Person> actual, Person... expected) {
		final List<Person> exp = new ArrayList<>(Arrays.asList(expected));
		for (final Person p : actual) {
			assertTrue(exp.removeIf(it -> it == p), "Unexpected element: " + p);
		}
		assertTrue(exp.isEmpty(), "Missed elements: " + exp);
	}

}
