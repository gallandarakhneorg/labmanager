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

package fr.ciadlab.labmanager.controller.publication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.service.member.MemberFiltering;
import fr.ciadlab.labmanager.service.member.MembershipService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.service.publication.AuthorshipService;
import fr.ciadlab.labmanager.utils.names.DefaultPersonNameParser;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.web.servlet.ModelAndView;

/** Tests for {@link AuthorController}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class AuthorControllerTest {

	private AuthorshipService authorshipService;

	private ResearchOrganizationService organizationService;

	private MembershipService memberService;

	private PersonService personService;

	private PersonNameParser nameParser;

	private AuthorController test;

	@BeforeEach
	public void setUp() {
		this.authorshipService = mock(AuthorshipService.class);
		this.organizationService = mock(ResearchOrganizationService.class);
		this.memberService = mock(MembershipService.class);
		this.personService = mock(PersonService.class);
		this.nameParser = new DefaultPersonNameParser();
		this.test = new AuthorController(this.authorshipService, this.personService,
				this.organizationService, this.memberService, this.nameParser);
		this.test.setLogger(mock(Logger.class));
	}

	@Test
	public void showAuthorTool() {
		final ModelAndView mv = this.test.showAuthorTool();
		assertEquals("authorTool", mv.getViewName());
	}

	@Test
	public void mergeAuthors() throws Exception {
		final HttpServletResponse response = mock(HttpServletResponse.class);
		when(this.authorshipService.mergeAuthors(anyString(), anyString())).thenReturn(123);
		this.test.mergeAuthors(response, "abc1 xyz1", "abc0 xyz0");
		verify(this.authorshipService).mergeAuthors("abc0 xyz0", "abc1 xyz1");
		verify(response).sendRedirect("/SpringRestHibernate/authorTool?success=true&message=abc0+xyz0&count=123");
	}

	@Test
	public void mergeMultipleAuthors() throws Exception {
		final HttpServletResponse response = mock(HttpServletResponse.class);
		final List<Integer> olds = mock(List.class);
		when(this.authorshipService.mergeAuthors(any(List.class), anyString(), anyString())).thenReturn(123);
		this.test.mergeMultipleAuthors(response, "abc1", "xyz1", olds);
		verify(this.authorshipService).mergeAuthors(olds, "abc1", "xyz1");
		verify(response).sendRedirect("/SpringRestHibernate/authorTool?success=true&message=abc1+xyz1&count=123");
	}

	@Test
	public void getAuthorsList_nullArgument() throws Exception {
		Person pers0 = mock(Person.class);
		when(pers0.getId()).thenReturn(1234);
		when(pers0.getFullName()).thenReturn("F1 L1");
		Person pers1 = mock(Person.class);
		when(pers1.getId()).thenReturn(2345);
		when(pers1.getFullName()).thenReturn("F2 L2");
		List<Person> persons = Arrays.asList(pers0, pers1);
		when(this.personService.getAllPersons()).thenReturn(persons);

		Map<Integer, String> result = this.test.getAuthorsList(null);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertSame("F1 L1", result.get(1234));
		assertSame("F2 L2", result.get(2345));
	}

	@Test
	public void getAuthorsList_emptyArgument() throws Exception {
		Person pers0 = mock(Person.class);
		when(pers0.getId()).thenReturn(1234);
		when(pers0.getFullName()).thenReturn("F1 L1");
		Person pers1 = mock(Person.class);
		when(pers1.getId()).thenReturn(2345);
		when(pers1.getFullName()).thenReturn("F2 L2");
		List<Person> persons = Arrays.asList(pers0, pers1);
		when(this.personService.getAllPersons()).thenReturn(persons);

		Map<Integer, String> result = this.test.getAuthorsList(new String[0]);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertSame("F1 L1", result.get(1234));
		assertSame("F2 L2", result.get(2345));
	}

	@Test
	public void getAuthorsList_noExcluded() throws Exception {
		Person pers0 = mock(Person.class);
		when(pers0.getId()).thenReturn(1234);
		when(pers0.getFullName()).thenReturn("F1 L1");
		Person pers1 = mock(Person.class);
		when(pers1.getId()).thenReturn(2345);
		when(pers1.getFullName()).thenReturn("F2 L2");
		List<Person> persons = Arrays.asList(pers0, pers1);
		when(this.personService.getAllPersons()).thenReturn(persons);
		when(this.personService.getPersonIdByName(anyString(), anyString())).thenAnswer(it -> {
			switch (it.getArgument(0).toString()) {
			case "F1":
				switch (it.getArgument(1).toString()) {
				case "L1":
					return 1234;
				}
				break;
			case "F2":
				switch (it.getArgument(1).toString()) {
				case "L2":
					return 2345;
				}
				break;
			}
			return null;
		});
		when(this.personService.getPerson(anyInt())).thenAnswer(it -> {
			switch (((Integer) it.getArgument(0)).intValue()) {
			case 1234:
				return pers0;
			case 2345:
				return pers1;
			}
			return null;
		});

		Map<Integer, String> result = this.test.getAuthorsList(new String[] { "L3, F3" });

		assertNotNull(result);
		assertEquals(2, result.size());
		assertSame("F1 L1", result.get(1234));
		assertSame("F2 L2", result.get(2345));
	}

	@Test
	public void getAuthorsList_excluded_format1() throws Exception {
		Person pers0 = mock(Person.class);
		when(pers0.getId()).thenReturn(1234);
		Person pers1 = mock(Person.class);
		when(pers1.getId()).thenReturn(2345);
		when(pers1.getFullName()).thenReturn("F2 L2");
		List<Person> persons = Arrays.asList(pers0, pers1);
		when(this.personService.getAllPersons()).thenReturn(persons);
		when(this.personService.getPersonIdByName(anyString(), anyString())).thenAnswer(it -> {
			switch (it.getArgument(0).toString()) {
			case "F1":
				switch (it.getArgument(1).toString()) {
				case "L1":
					return 1234;
				}
				break;
			case "F2":
				switch (it.getArgument(1).toString()) {
				case "L2":
					return 2345;
				}
				break;
			}
			return null;
		});
		when(this.personService.getPerson(anyInt())).thenAnswer(it -> {
			switch (((Integer) it.getArgument(0)).intValue()) {
			case 1234:
				return pers0;
			case 2345:
				return pers1;
			}
			return null;
		});

		Map<Integer, String> result = this.test.getAuthorsList(new String[] { "F1 L1" });

		assertNotNull(result);
		assertEquals(1, result.size());
		assertSame("F2 L2", result.get(2345));
	}

	@Test
	public void getAuthorsList_excluded_format2() throws Exception {
		Person pers0 = mock(Person.class);
		when(pers0.getId()).thenReturn(1234);
		Person pers1 = mock(Person.class);
		when(pers1.getId()).thenReturn(2345);
		when(pers1.getFullName()).thenReturn("F2 L2");
		List<Person> persons = Arrays.asList(pers0, pers1);
		when(this.personService.getAllPersons()).thenReturn(persons);
		when(this.personService.getPersonIdByName(anyString(), anyString())).thenAnswer(it -> {
			switch (it.getArgument(0).toString()) {
			case "F1":
				switch (it.getArgument(1).toString()) {
				case "L1":
					return 1234;
				}
				break;
			case "F2":
				switch (it.getArgument(1).toString()) {
				case "L2":
					return 2345;
				}
				break;
			}
			return null;
		});
		when(this.personService.getPerson(anyInt())).thenAnswer(it -> {
			switch (((Integer) it.getArgument(0)).intValue()) {
			case 1234:
				return pers0;
			case 2345:
				return pers1;
			}
			return null;
		});

		Map<Integer, String> result = this.test.getAuthorsList(new String[] { "L1, F1" });

		assertNotNull(result);
		assertEquals(1, result.size());
		assertSame("F2 L2", result.get(2345));
	}

	@Test
	public void showAuthorsList_allNull_invalidOrga() {
		Optional<ResearchOrganization> orga0 = Optional.empty();
		Optional<ResearchOrganization> orga1 = Optional.empty();
		when(this.organizationService.getResearchOrganizationByName(anyString())).thenReturn(orga0);
		when(this.organizationService.getResearchOrganizationByAcronym(anyString())).thenReturn(orga1);

		ModelAndView mv = this.test.showAuthorsList(null, null, null);

		verify(this.organizationService, atLeastOnce()).getResearchOrganizationByName(eq("CIAD"));
		verify(this.organizationService, atLeastOnce()).getResearchOrganizationByAcronym(eq("CIAD"));

		verify(this.memberService, never()).getOrganizationMembers(any(ResearchOrganization.class),
				any(MemberFiltering.class), any());
		verify(this.memberService, never()).getOtherOrganizationsForMembers(any(Collection.class), anyString());

		assertNotNull(mv);
		assertNull(mv.getModel().get("uuid"));
		assertNull(mv.getModel().get("otherOrganisationsForMembers"));
		assertNull(mv.getModel().get("members"));
	}

	@Test
	public void showAuthorsList_allNull_orgaAcronym() {
		Optional<ResearchOrganization> orga0 = Optional.empty();
		ResearchOrganization orga = mock(ResearchOrganization.class);
		Optional<ResearchOrganization> orga1 = Optional.of(orga);
		when(this.organizationService.getResearchOrganizationByName(anyString())).thenReturn(orga0);
		when(this.organizationService.getResearchOrganizationByAcronym(anyString())).thenReturn(orga1);

		List<Membership> members = mock(List.class);
		when(this.memberService.getOrganizationMembers(any(ResearchOrganization.class), any(MemberFiltering.class),
				any())).thenReturn(members);

		Map<Integer, List<ResearchOrganization>> other = mock(Map.class);
		when(this.memberService.getOtherOrganizationsForMembers(any(), anyString())).thenReturn(other);

		ModelAndView mv = this.test.showAuthorsList(null, null, null);

		verify(this.organizationService, atLeastOnce()).getResearchOrganizationByName(eq("CIAD"));
		verify(this.organizationService, atLeastOnce()).getResearchOrganizationByAcronym(eq("CIAD"));

		verify(this.memberService, atLeastOnce()).getOrganizationMembers(same(orga), same(MemberFiltering.ALL), isNull());
		verify(this.memberService, atLeastOnce()).getOtherOrganizationsForMembers(same(members), eq("CIAD"));

		assertNotNull(mv);
		assertTrue(mv.getModel().get("uuid") instanceof Integer);
		assertSame(other, mv.getModel().get("otherOrganisationsForMembers"));
		assertSame(members, mv.getModel().get("members"));
	}

}
