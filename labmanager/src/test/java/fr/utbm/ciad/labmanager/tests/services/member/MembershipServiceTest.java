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

package fr.utbm.ciad.labmanager.tests.services.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Sets;
import fr.utbm.ciad.labmanager.configuration.ConfigurationConstants;
import fr.utbm.ciad.labmanager.data.member.MemberStatus;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.member.MembershipRepository;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.member.PersonRepository;
import fr.utbm.ciad.labmanager.data.member.Responsibility;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganizationRepository;
import fr.utbm.ciad.labmanager.data.scientificaxis.ScientificAxis;
import fr.utbm.ciad.labmanager.services.member.MemberFiltering;
import fr.utbm.ciad.labmanager.services.member.MembershipService;
import fr.utbm.ciad.labmanager.utils.bap.FrenchBap;
import fr.utbm.ciad.labmanager.utils.cnu.CnuSection;
import fr.utbm.ciad.labmanager.utils.conrs.ConrsSection;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link MembershipService}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {

	private Membership ms0;

	private Membership ms1;

	private Membership ms2;

	private Membership ms3;

	private ResearchOrganization o1;

	private ResearchOrganization o2;

	private Person p1;

	private Person p2;

	private Person p3;

	private MessageSourceAccessor messages;

	private ResearchOrganizationRepository organizationRepository;

	private MembershipRepository membershipRepository;

	private PersonRepository personRepository;

	private MembershipService test;

	private SessionFactory sessionFactory;

	@BeforeEach
	public void setUp() {
		this.messages = mock(MessageSourceAccessor.class);
		this.organizationRepository = mock(ResearchOrganizationRepository.class);
		this.membershipRepository = mock(MembershipRepository.class);
		this.personRepository = mock(PersonRepository.class);
		this.sessionFactory = mock(SessionFactory.class);
		this.test = new MembershipService(this.organizationRepository, this.membershipRepository, this.personRepository,
				this.messages, new ConfigurationConstants(), this.sessionFactory);

		// Prepare some memberships to be inside the repository
		// The lenient configuration is used to configure the mocks for all the tests
		// at the same code location for configuration simplicity
		this.o1 = mock(ResearchOrganization.class);
		lenient().when(this.o1.getId()).thenReturn(1234l);
		lenient().when(this.o1.getName()).thenReturn("o1");
		this.o2 = mock(ResearchOrganization.class);
		lenient().when(this.o2.getId()).thenReturn(2345l);
		lenient().when(this.o2.getName()).thenReturn("o2");

		this.p1 = mock(Person.class);
		lenient().when(this.p1.getId()).thenReturn(12345l);
		this.p2 = mock(Person.class);
		lenient().when(this.p2.getId()).thenReturn(23456l);
		this.p3 = mock(Person.class);
		lenient().when(this.p3.getId()).thenReturn(34567l);

		this.ms0 = mock(Membership.class);
		lenient().when(this.ms0.getId()).thenReturn(123l);
		lenient().when(this.ms0.getPerson()).thenReturn(this.p1);
		lenient().when(this.ms0.getDirectResearchOrganization()).thenReturn(this.o1);
		lenient().when(this.ms0.getMemberStatus()).thenReturn(MemberStatus.ASSOCIATE_PROFESSOR);
		this.ms1 = mock(Membership.class);
		lenient().when(this.ms1.getId()).thenReturn(234l);
		lenient().when(this.ms1.getPerson()).thenReturn(this.p2);
		lenient().when(this.ms1.getDirectResearchOrganization()).thenReturn(this.o1);
		lenient().when(this.ms1.getMemberStatus()).thenReturn(MemberStatus.FULL_PROFESSOR);
		this.ms2 = mock(Membership.class);
		lenient().when(this.ms2.getId()).thenReturn(345l);
		lenient().when(this.ms2.getPerson()).thenReturn(this.p1);
		lenient().when(this.ms2.getDirectResearchOrganization()).thenReturn(this.o2);
		lenient().when(this.ms2.getMemberStatus()).thenReturn(MemberStatus.ASSOCIATE_PROFESSOR);
		this.ms3 = mock(Membership.class);
		lenient().when(this.ms3.getId()).thenReturn(456l);
		lenient().when(this.ms3.getPerson()).thenReturn(this.p3);
		lenient().when(this.ms3.getDirectResearchOrganization()).thenReturn(this.o2);
		lenient().when(this.ms3.getMemberStatus()).thenReturn(MemberStatus.FULL_PROFESSOR);

		lenient().when(this.membershipRepository.findDistinctByResearchOrganizationIdAndPersonId(anyLong(), anyLong())).then(it -> {
			final long orgaId = ((Number) it.getArgument(0)).longValue();
			final long personId = ((Number) it.getArgument(1)).longValue();
			if (orgaId == 1234l) {
				if (personId == 12345l) {
					return Optional.of(this.ms0);
				} else if (personId == 23456l) {
					return Optional.of(this.ms1);
				}
			} else if (orgaId == 2345l) {
				if (personId == 12345l) {
					return Optional.of(this.ms2);
				} else if (personId == 34567l) {
					return Optional.of(this.ms3);
				}
			}
			return Optional.empty();
		});
		lenient().when(this.personRepository.findById(anyLong())).then(it -> {
			final long personId = ((Number) it.getArgument(0)).longValue();
			if (personId == 12345l) {
				return Optional.of(this.p1);
			} else if (personId == 23456l) {
				return Optional.of(this.p2);
			} else if (personId == 34567l) {
				return Optional.of(this.p3);
			}
			return Optional.empty();
		});
		lenient().when(this.personRepository.findDistinctByMembershipsResearchOrganizationId(anyLong())).then(it -> {
			final long orgaId = ((Number) it.getArgument(0)).longValue();
			if (orgaId == 1234l) {
				return Sets.newHashSet(this.p1, this.p2);
			} else if (orgaId == 2345l) {
				return Sets.newHashSet(this.p1, this.p3);
			}
			return Collections.emptySet();
		});
		lenient().when(this.personRepository.findDistinctByMembershipsResearchOrganizationNameAndMembershipsMemberStatus(anyString(), any())).then(it -> {
			final String orgaName = it.getArgument(0).toString();
			final MemberStatus status = (MemberStatus) it.getArgument(1);
			switch (orgaName) {
			case "o1":
				if (status == MemberStatus.ASSOCIATE_PROFESSOR) {
					return Sets.newHashSet(this.p1);
				}
				if (status == MemberStatus.FULL_PROFESSOR) {
					return Sets.newHashSet(this.p2);
				}
				break;
			case "o2":
				if (status == MemberStatus.ASSOCIATE_PROFESSOR) {
					return Sets.newHashSet(this.p1);
				}
				if (status == MemberStatus.FULL_PROFESSOR) {
					return Sets.newHashSet(this.p3);
				}
				break;
			}
			return Collections.emptySet();
		});
		lenient().when(this.personRepository.findDistinctByMembershipsResearchOrganizationAcronymAndMembershipsMemberStatus(anyString(), any())).then(it -> {
			final String orgaAcro = it.getArgument(0).toString();
			final MemberStatus status = (MemberStatus) it.getArgument(1);
			switch (orgaAcro) {
			case "-o1":
				if (status == MemberStatus.ASSOCIATE_PROFESSOR) {
					return Sets.newHashSet(this.p1);
				}
				if (status == MemberStatus.FULL_PROFESSOR) {
					return Sets.newHashSet(this.p2);
				}
				break;
			case "-o2":
				if (status == MemberStatus.ASSOCIATE_PROFESSOR) {
					return Sets.newHashSet(this.p1);
				}
				if (status == MemberStatus.FULL_PROFESSOR) {
					return Sets.newHashSet(this.p3);
				}
				break;
			}
			return Collections.emptySet();
		});
		lenient().when(this.organizationRepository.findById(anyLong())).then(it -> {
			final long orgaId = ((Number) it.getArgument(0)).longValue();
			if (orgaId == 1234l) {
				return Optional.of(this.o1);
			} else if (orgaId == 2345l) {
				return Optional.of(this.o2);
			}
			return Optional.empty();
		});
	}

	@Test
	public void getMembership() {
		final Membership ms0 = this.test.getMembership(1l, 1l);
		assertNull(ms0);

		final Membership ms1 = this.test.getMembership(1234l, 1l);
		assertNull(ms1);

		final Membership ms2 = this.test.getMembership(1l, 12345l);
		assertNull(ms2);

		final Membership ms3 = this.test.getMembership(2345l, 12345l);
		assertSame(this.ms2, ms3);
	}

	@Test
	public void getDirectMembersOf() {
		final Set<Person> pers0 = this.test.getDirectMembersOf(1l);
		assertTrue(pers0.isEmpty());

		final Set<Person> pers1 = this.test.getDirectMembersOf(1234l);
		assertEquals(2, pers1.size());
		assertTrue(pers1.contains(this.p1));
		assertTrue(pers1.contains(this.p2));
		assertFalse(pers1.contains(this.p3));

		final Set<Person> pers2 = this.test.getDirectMembersOf(2345l);
		assertEquals(2, pers2.size());
		assertTrue(pers2.contains(this.p1));
		assertFalse(pers2.contains(this.p2));
		assertTrue(pers2.contains(this.p3));
	}

	@Test
	public void getMembersOf_noHierarchy() {
		final Set<Person> pers0 = this.test.getMembersOf(1l);
		assertTrue(pers0.isEmpty());

		final Set<Person> pers1 = this.test.getMembersOf(1234l);
		assertEquals(2, pers1.size());
		assertTrue(pers1.contains(this.p1));
		assertTrue(pers1.contains(this.p2));
		assertFalse(pers1.contains(this.p3));

		final Set<Person> pers2 = this.test.getMembersOf(2345l);
		assertEquals(2, pers2.size());
		assertTrue(pers2.contains(this.p1));
		assertFalse(pers2.contains(this.p2));
		assertTrue(pers2.contains(this.p3));
	}

	@Test
	public void getMembersOf_hierarchy() {
		when(this.o1.getSubOrganizations()).thenReturn(Collections.singleton(this.o2));

		final Set<Person> pers0 = this.test.getMembersOf(1l);
		assertTrue(pers0.isEmpty());

		final Set<Person> pers1 = this.test.getMembersOf(1234l);
		assertEquals(3, pers1.size());
		assertTrue(pers1.contains(this.p1));
		assertTrue(pers1.contains(this.p2));
		assertTrue(pers1.contains(this.p3));

		final Set<Person> pers2 = this.test.getMembersOf(2345l);
		assertEquals(2, pers2.size());
		assertTrue(pers2.contains(this.p1));
		assertFalse(pers2.contains(this.p2));
		assertTrue(pers2.contains(this.p3));
	}

	@Test
	public void getPersonsByOrganizationNameStatus() {
		final Set<Person> pers0 = this.test.getPersonsByOrganizationNameStatus(null, null);
		assertTrue(pers0.isEmpty());

		final Set<Person> pers1 = this.test.getPersonsByOrganizationNameStatus("o1", null);
		assertTrue(pers1.isEmpty());

		final Set<Person> pers2 = this.test.getPersonsByOrganizationNameStatus("o2", null);
		assertTrue(pers2.isEmpty());

		final Set<Person> pers3 = this.test.getPersonsByOrganizationNameStatus(null, MemberStatus.FULL_PROFESSOR);
		assertTrue(pers3.isEmpty());

		final Set<Person> pers4 = this.test.getPersonsByOrganizationNameStatus("o3", MemberStatus.FULL_PROFESSOR);
		assertTrue(pers4.isEmpty());

		final Set<Person> pers5 = this.test.getPersonsByOrganizationNameStatus("o3", MemberStatus.FULL_PROFESSOR);
		assertTrue(pers5.isEmpty());

		final Set<Person> pers6 = this.test.getPersonsByOrganizationNameStatus("o1", MemberStatus.ADMIN);
		assertTrue(pers6.isEmpty());

		final Set<Person> pers7 = this.test.getPersonsByOrganizationNameStatus("o1", MemberStatus.FULL_PROFESSOR);
		assertEquals(1, pers7.size());
		assertTrue(pers7.contains(this.p2));

		final Set<Person> pers8 = this.test.getPersonsByOrganizationNameStatus("o1", MemberStatus.ASSOCIATE_PROFESSOR);
		assertEquals(1, pers8.size());
		assertTrue(pers8.contains(this.p1));

		final Set<Person> pers9 = this.test.getPersonsByOrganizationNameStatus("o2", MemberStatus.ADMIN);
		assertTrue(pers9.isEmpty());

		final Set<Person> pers10 = this.test.getPersonsByOrganizationNameStatus("o2", MemberStatus.FULL_PROFESSOR);
		assertEquals(1, pers10.size());
		assertTrue(pers10.contains(this.p3));

		final Set<Person> pers11 = this.test.getPersonsByOrganizationNameStatus("o2", MemberStatus.ASSOCIATE_PROFESSOR);
		assertEquals(1, pers11.size());
		assertTrue(pers11.contains(this.p1));
	}

	@Test
	public void getPersonsByOrganizationAcronymStatus() {
		final Set<Person> pers0 = this.test.getPersonsByOrganizationAcronymStatus(null, null);
		assertTrue(pers0.isEmpty());

		final Set<Person> pers1 = this.test.getPersonsByOrganizationAcronymStatus("-o1", null);
		assertTrue(pers1.isEmpty());

		final Set<Person> pers2 = this.test.getPersonsByOrganizationAcronymStatus("-o2", null);
		assertTrue(pers2.isEmpty());

		final Set<Person> pers3 = this.test.getPersonsByOrganizationAcronymStatus(null, MemberStatus.FULL_PROFESSOR);
		assertTrue(pers3.isEmpty());

		final Set<Person> pers4 = this.test.getPersonsByOrganizationAcronymStatus("-o3", MemberStatus.FULL_PROFESSOR);
		assertTrue(pers4.isEmpty());

		final Set<Person> pers5 = this.test.getPersonsByOrganizationAcronymStatus("-o3", MemberStatus.FULL_PROFESSOR);
		assertTrue(pers5.isEmpty());

		final Set<Person> pers6 = this.test.getPersonsByOrganizationAcronymStatus("-o1", MemberStatus.ADMIN);
		assertTrue(pers6.isEmpty());

		final Set<Person> pers7 = this.test.getPersonsByOrganizationAcronymStatus("-o1", MemberStatus.FULL_PROFESSOR);
		assertEquals(1, pers7.size());
		assertTrue(pers7.contains(this.p2));

		final Set<Person> pers8 = this.test.getPersonsByOrganizationAcronymStatus("-o1", MemberStatus.ASSOCIATE_PROFESSOR);
		assertEquals(1, pers8.size());
		assertTrue(pers8.contains(this.p1));

		final Set<Person> pers9 = this.test.getPersonsByOrganizationAcronymStatus("-o2", MemberStatus.ADMIN);
		assertTrue(pers9.isEmpty());

		final Set<Person> pers10 = this.test.getPersonsByOrganizationAcronymStatus("-o2", MemberStatus.FULL_PROFESSOR);
		assertEquals(1, pers10.size());
		assertTrue(pers10.contains(this.p3));

		final Set<Person> pers11 = this.test.getPersonsByOrganizationAcronymStatus("-o2", MemberStatus.ASSOCIATE_PROFESSOR);
		assertEquals(1, pers11.size());
		assertTrue(pers11.contains(this.p1));
	}

	@Test
	public void getOrganizationMembers_ResearchOrganizationMemberFilteringPredicate() {
		when(this.o1.getDirectOrganizationMemberships()).thenReturn(Sets.newHashSet(this.ms0, this.ms1));
		when(this.o2.getDirectOrganizationMemberships()).thenReturn(Sets.newHashSet(this.ms2, this.ms3));

		final List<Membership> mbrs0 = this.test.getOrganizationMembers(null, null, null);
		assertTrue(mbrs0.isEmpty());

		final List<Membership> mbrs1 = this.test.getOrganizationMembers(this.o1, null, null);
		assertEquals(2, mbrs1.size());
		assertTrue(mbrs1.contains(this.ms0));
		assertTrue(mbrs1.contains(this.ms1));
		assertFalse(mbrs1.contains(this.ms2));
		assertFalse(mbrs1.contains(this.ms3));

		final List<Membership> mbrs2 = this.test.getOrganizationMembers(this.o2, null, null);
		assertEquals(2, mbrs2.size());
		assertFalse(mbrs2.contains(this.ms0));
		assertFalse(mbrs2.contains(this.ms1));
		assertTrue(mbrs2.contains(this.ms2));
		assertTrue(mbrs2.contains(this.ms3));

		final List<Membership> mbrs3 = this.test.getOrganizationMembers(this.o1, MemberFiltering.FORMERS, null);
		assertTrue(mbrs3.isEmpty());

		final List<Membership> mbrs4 = this.test.getOrganizationMembers(this.o2, MemberFiltering.FORMERS, null);
		assertTrue(mbrs4.isEmpty());

		final List<Membership> mbrs5 = this.test.getOrganizationMembers(this.o1, null, it -> it == MemberStatus.FULL_PROFESSOR);
		assertEquals(1, mbrs5.size());
		assertFalse(mbrs5.contains(this.ms0));
		assertTrue(mbrs5.contains(this.ms1));
		assertFalse(mbrs5.contains(this.ms2));
		assertFalse(mbrs5.contains(this.ms3));

		final List<Membership> mbrs6 = this.test.getOrganizationMembers(this.o2, null, it -> it == MemberStatus.FULL_PROFESSOR);
		assertEquals(1, mbrs6.size());
		assertFalse(mbrs6.contains(this.ms0));
		assertFalse(mbrs6.contains(this.ms1));
		assertFalse(mbrs6.contains(this.ms2));
		assertTrue(mbrs6.contains(this.ms3));
	}

	@Test
	public void getOrganizationMembers_IntMemberFilteringPredicate() {
		when(this.o1.getDirectOrganizationMemberships()).thenReturn(Sets.newHashSet(this.ms0, this.ms1));
		when(this.o2.getDirectOrganizationMemberships()).thenReturn(Sets.newHashSet(this.ms2, this.ms3));

		final List<Membership> mbrs0 = this.test.getOrganizationMembers(0l, null, null);
		assertTrue(mbrs0.isEmpty());

		final List<Membership> mbrs1 = this.test.getOrganizationMembers(1234l, null, null);
		assertEquals(2, mbrs1.size());
		assertTrue(mbrs1.contains(this.ms0));
		assertTrue(mbrs1.contains(this.ms1));
		assertFalse(mbrs1.contains(this.ms2));
		assertFalse(mbrs1.contains(this.ms3));

		final List<Membership> mbrs2 = this.test.getOrganizationMembers(2345l, null, null);
		assertEquals(2, mbrs2.size());
		assertFalse(mbrs2.contains(this.ms0));
		assertFalse(mbrs2.contains(this.ms1));
		assertTrue(mbrs2.contains(this.ms2));
		assertTrue(mbrs2.contains(this.ms3));

		final List<Membership> mbrs3 = this.test.getOrganizationMembers(1234l, MemberFiltering.FORMERS, null);
		assertTrue(mbrs3.isEmpty());

		final List<Membership> mbrs4 = this.test.getOrganizationMembers(2345, MemberFiltering.FORMERS, null);
		assertTrue(mbrs4.isEmpty());

		final List<Membership> mbrs5 = this.test.getOrganizationMembers(1234, null, it -> it == MemberStatus.FULL_PROFESSOR);
		assertEquals(1, mbrs5.size());
		assertFalse(mbrs5.contains(this.ms0));
		assertTrue(mbrs5.contains(this.ms1));
		assertFalse(mbrs5.contains(this.ms2));
		assertFalse(mbrs5.contains(this.ms3));

		final List<Membership> mbrs6 = this.test.getOrganizationMembers(2345l, null, it -> it == MemberStatus.FULL_PROFESSOR);
		assertEquals(1, mbrs6.size());
		assertFalse(mbrs6.contains(this.ms0));
		assertFalse(mbrs6.contains(this.ms1));
		assertFalse(mbrs6.contains(this.ms2));
		assertTrue(mbrs6.contains(this.ms3));
	}

	@Test
	public void getOtherOrganizationsForMembers_CollectionString() {
		final Map<Long, List<ResearchOrganization>> grp0 = this.test.getOtherOrganizationsForMembers(null, null);
		assertTrue(grp0.isEmpty());

		final Map<Long, List<ResearchOrganization>> grp1 = this.test.getOtherOrganizationsForMembers(null, "o1");
		assertTrue(grp1.isEmpty());

		final Map<Long, List<ResearchOrganization>> grp2 = this.test.getOtherOrganizationsForMembers(null, "o3");
		assertTrue(grp2.isEmpty());

		final Map<Long, List<ResearchOrganization>> grp3 = this.test.getOtherOrganizationsForMembers(Sets.newHashSet(this.ms0, this.ms2), null);
		assertTrue(grp3.isEmpty());

		final Map<Long, List<ResearchOrganization>> grp4 = this.test.getOtherOrganizationsForMembers(Sets.newHashSet(this.ms0, this.ms2), "o3");
		assertEquals(1, grp4.size());
		final List<ResearchOrganization> orgs4 = grp4.get(12345l);
		assertEquals(2, orgs4.size());
		assertTrue(orgs4.contains(this.o1));
		assertTrue(orgs4.contains(this.o2));

		final Map<Long, List<ResearchOrganization>> grp5 = this.test.getOtherOrganizationsForMembers(Sets.newHashSet(this.ms0, this.ms2), "o1");
		assertEquals(1, grp5.size());
		final List<ResearchOrganization> orgs5 = grp5.get(12345l);
		assertEquals(1, orgs5.size());
		assertFalse(orgs5.contains(this.o1));
		assertTrue(orgs5.contains(this.o2));
	}

	@Test
	public void getOtherOrganizationsForMembers_CollectionInt() {
		final Map<Long, List<ResearchOrganization>> grp0 = this.test.getOtherOrganizationsForMembers(null, 0l);
		assertTrue(grp0.isEmpty());

		final Map<Long, List<ResearchOrganization>> grp1 = this.test.getOtherOrganizationsForMembers(null, 1234l);
		assertTrue(grp1.isEmpty());

		final Map<Long, List<ResearchOrganization>> grp2 = this.test.getOtherOrganizationsForMembers(null, 1l);
		assertTrue(grp2.isEmpty());

		final Map<Long, List<ResearchOrganization>> grp4 = this.test.getOtherOrganizationsForMembers(Sets.newHashSet(this.ms0, this.ms2), 1l);
		assertEquals(1, grp4.size());
		final List<ResearchOrganization> orgs4 = grp4.get(12345l);
		assertEquals(2, orgs4.size());
		assertTrue(orgs4.contains(this.o1));
		assertTrue(orgs4.contains(this.o2));

		final Map<Long, List<ResearchOrganization>> grp5 = this.test.getOtherOrganizationsForMembers(Sets.newHashSet(this.ms0, this.ms2), 1234l);
		assertEquals(1, grp5.size());
		final List<ResearchOrganization> orgs5 = grp5.get(12345l);
		assertEquals(1, orgs5.size());
		assertFalse(orgs5.contains(this.o1));
		assertTrue(orgs5.contains(this.o2));
	}

	@Test
	public void addMembership() throws Exception {
		final Pair<Membership, Boolean> m = this.test.addMembership(2345, null, null, 34567, LocalDate.parse("2022-07-12"),
				LocalDate.parse("2022-07-28"), MemberStatus.ENGINEER, true, Responsibility.DEAN,
				CnuSection.CNU_07, ConrsSection.CONRS_08, FrenchBap.BAP_E, false, new ArrayList<>(), false);
		assertNotNull(m);
		assertTrue(m.getRight());

		final ArgumentCaptor<Membership> arg = ArgumentCaptor.forClass(Membership.class);
		verify(this.membershipRepository, only()).save(arg.capture());
		final Membership actual = arg.getValue();
		assertNotNull(actual);
		assertSame(m.getLeft(), actual);
		assertEquals(LocalDate.parse("2022-07-12"), actual.getMemberSinceWhen());
		assertEquals(LocalDate.parse("2022-07-28"), actual.getMemberToWhen());
		assertSame(MemberStatus.ENGINEER, actual.getMemberStatus());
		assertTrue(actual.isPermanentPosition());
		assertSame(Responsibility.DEAN, actual.getResponsibility());
		assertSame(CnuSection.CNU_07, actual.getCnuSection());
		assertSame(ConrsSection.CONRS_08, actual.getConrsSection());
		assertSame(FrenchBap.BAP_E, actual.getFrenchBap());
		assertFalse(actual.isMainPosition());
		assertSame(this.p3, actual.getPerson());
		assertSame(this.o2, actual.getDirectResearchOrganization());
	}

	@Test
	public void updateMembershipById() throws Exception {
		when(this.membershipRepository.findById(anyLong())).then(it -> {
			final long id = ((Number) it.getArgument(0)).longValue();
			if (id == 234l) {
				return Optional.of(this.ms1);
			}
			return Optional.empty();
		});
		
		List<ScientificAxis> axes = mock(List.class);

		final Membership m = this.test.updateMembershipById(234l, 1234l, null, null, LocalDate.parse("2019-07-12"), LocalDate.parse("2019-07-28"),
				MemberStatus.MASTER_STUDENT, true, Responsibility.IT_RESPONSIBLE, CnuSection.CNU_05,
				ConrsSection.CONRS_06, FrenchBap.BAP_E, false, axes);
		assertSame(this.ms1, m);

		final ArgumentCaptor<Membership> arg0 = ArgumentCaptor.forClass(Membership.class);
		verify(this.membershipRepository, atLeastOnce()).save(same(this.ms1));
		verifyNoMoreInteractions(this.membershipRepository);

		verify(this.ms1).setDirectResearchOrganization(same(this.o1));
		verify(this.ms1).setOrganizationAddress(isNull());
		verify(this.ms1).setMemberSinceWhen(eq(LocalDate.parse("2019-07-12")));
		verify(this.ms1).setMemberToWhen(eq(LocalDate.parse("2019-07-28")));
		verify(this.ms1).setMemberStatus(same(MemberStatus.MASTER_STUDENT));
		verify(this.ms1).setPermanentPosition(eq(true));
		verify(this.ms1).setResponsibility(same(Responsibility.IT_RESPONSIBLE));
		verify(this.ms1).setCnuSection(same(CnuSection.CNU_05));
		verify(this.ms1).setConrsSection(same(ConrsSection.CONRS_06));
		verify(this.ms1).setFrenchBap(same(FrenchBap.BAP_E));
		verify(this.ms1).setMainPosition(eq(false));
		verify(this.ms1).setScientificAxes(same(axes));
		verifyNoMoreInteractions(this.ms1);
	}

	@Test
	public void removeMembership() throws Exception {
		when(this.membershipRepository.findById(anyLong())).then(it -> {
			final long personId = ((Number) it.getArgument(0)).longValue();
			if (personId == 234l) {
				return Optional.of(this.ms1);
			}
			return Optional.empty();
		});
		this.test.removeMembership(234);
		verify(this.membershipRepository, atLeastOnce()).findById(eq(234l));
		verify(this.membershipRepository, atLeastOnce()).deleteById(eq(234l));
	}

}
