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

package fr.ciadlab.labmanager.controller.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.utils.names.DefaultPersonNameParser;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.web.servlet.ModelAndView;

/** Tests for {@link PersonController}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

	private PersonService personService;

	private PersonNameParser nameParser;

	private PersonController test;

	@BeforeEach
	public void setUp() {
		this.personService = mock(PersonService.class);
		// Force the use of the standard name parser in order to execute the functions of the controller.
		this.nameParser = new DefaultPersonNameParser();
		this.test = new PersonController(this.personService, this.nameParser);
		this.test.setLogger(mock(Logger.class));
	}

	@Test
	public void showPersonTool() {
		final ModelAndView mv = this.test.showPersonTool();
		assertEquals("personTool", mv.getViewName());
	}

	@Test
	public void addPerson() throws Exception {
		final HttpServletResponse response = mock(HttpServletResponse.class);
		this.test.addPerson(response, "first0", "last0", "gender0", "email0", "orcid0");
		verify(this.personService).createPerson("first0", "last0", "gender0", "email0", "orcid0");
		verify(response).sendRedirect("/SpringRestHibernate/personTool?created=true&message=first0+last0+%3Cemail0%3E");
	}

	@Test
	public void getPersonData_unknownPerson() {
		final Person person = this.test.getPersonData("abc xyz");
		assertNull(person);
		verify(this.personService).getPersonIdByName("abc", "xyz");
	}

	@Test
	public void getPersonData_knownPerson() {
		final Person expected = mock(Person.class);
		when(this.personService.getPersonIdByName("abc", "xyz")).thenReturn(123);
		when(this.personService.getPerson(123)).thenReturn(expected);
		final Person person = this.test.getPersonData("abc xyz");
		assertSame(expected, person);
		verify(this.personService).getPersonIdByName("abc", "xyz");
		verify(this.personService).getPerson(123);
	}

	@Test
	public void editPerson_unknownPerson() throws Exception {
		final HttpServletResponse response = mock(HttpServletResponse.class);
		Locale.setDefault(Locale.US);

		this.test.editPerson(response, "abc xyz", "abc1", "xyz1", "email1", "orcid1");

		verify(this.personService, never()).updatePerson(anyInt(), anyString(), anyString(), anyString(), anyString());
		verify(response).sendRedirect("/SpringRestHibernate/personTool?error=1&message=Person+with+the+name+%27abc+xyz%27+was+not+found");
	}

	@Test
	public void editPerson_knownPerson() throws Exception {
		final HttpServletResponse response = mock(HttpServletResponse.class);
		final Person expected = mock(Person.class);
		when(this.personService.getPersonIdByName("abc", "xyz")).thenReturn(123);

		this.test.editPerson(response, "abc xyz", "abc1", "xyz1", "email1", "orcid1");

		verify(this.personService).updatePerson(123, "abc1", "xyz1", "email1", "orcid1");
		verify(response).sendRedirect("/SpringRestHibernate/personTool?updated=true&message=abc+xyz");
	}

	@Test
	public void personDuplicate() {
		List<Set<Person>> list = mock(List.class);
		when(this.personService.computePersonDuplicate()).thenReturn(list);

		ModelAndView mv = this.test.personDuplicate();

		assertNotNull(mv);
		assertSame(list, mv.getModel().get("matchingAuthors"));
	}

}
