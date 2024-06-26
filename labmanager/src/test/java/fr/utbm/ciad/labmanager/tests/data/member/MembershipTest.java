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

package fr.utbm.ciad.labmanager.tests.data.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.utbm.ciad.labmanager.data.jury.JuryMembership;
import fr.utbm.ciad.labmanager.data.member.MemberStatus;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.member.Responsibility;
import fr.utbm.ciad.labmanager.data.organization.OrganizationAddress;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.project.Project;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.data.scientificaxis.ScientificAxis;
import fr.utbm.ciad.labmanager.utils.bap.FrenchBap;
import fr.utbm.ciad.labmanager.utils.cnu.CnuSection;
import fr.utbm.ciad.labmanager.utils.conrs.ConrsSection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for {@link Membership}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class MembershipTest {

	private Membership test;

	private LocalDate now;

	private LocalDate past;

	private LocalDate future;

	private List<MemberStatus> membershipStatusItems;

	@BeforeEach
	public void setUp() {
		this.membershipStatusItems = new ArrayList<>();
		this.membershipStatusItems.addAll(Arrays.asList(MemberStatus.values()));
		//
		final LocalDate ld = LocalDate.now();
		this.now = LocalDate.of(ld.getYear(), ld.getMonthValue(), ld.getDayOfMonth());
		this.past = LocalDate.of(ld.getYear() - 1, ld.getMonthValue(), ld.getDayOfMonth());
		this.future = LocalDate.of(ld.getYear() + 1, ld.getMonthValue(), ld.getDayOfMonth());
		//
		this.test = new Membership();
	}

	private MemberStatus cons(MemberStatus status) {
		assertTrue(this.membershipStatusItems.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.membershipStatusItems.isEmpty(), "Missing enumeration items: " + this.membershipStatusItems.toString());
	}

	@Test
	public void getId() {
		assertEquals(0, this.test.getId());
	}

	@Test
	public void setId() {
		assertEquals(0, this.test.getId());

		this.test.setId(456);
		assertEquals(456, this.test.getId());

		this.test.setId(0);
		assertEquals(0, this.test.getId());
	}

	@Test
	public void getPerson() {
		assertNull(this.test.getPerson());
	}

	@Test
	public void setPerson() {
		assertNull(this.test.getPerson());
		final Person p0 = mock(Person.class);
		this.test.setPerson(p0);
		assertSame(p0, this.test.getPerson());
		this.test.setPerson(null);
		assertNull(this.test.getPerson());
	}

	@Test
	public void getDirectResearchOrganization() {
		assertNull(this.test.getDirectResearchOrganization());
	}

	@Test
	public void setDirectResearchOrganization() {
		assertNull(this.test.getDirectResearchOrganization());
		final ResearchOrganization r0 = mock(ResearchOrganization.class);
		this.test.setDirectResearchOrganization(r0);
		assertSame(r0, this.test.getDirectResearchOrganization());
		this.test.setDirectResearchOrganization(null);
		assertNull(this.test.getDirectResearchOrganization());
	}

	@Test
	public void getSuperResearchOrganization() {
		assertNull(this.test.getSuperResearchOrganization());
	}

	@Test
	public void setSuperResearchOrganization() {
		assertNull(this.test.getSuperResearchOrganization());
		final ResearchOrganization r0 = mock(ResearchOrganization.class);
		this.test.setSuperResearchOrganization(r0);
		assertSame(r0, this.test.getSuperResearchOrganization());
		this.test.setSuperResearchOrganization(null);
		assertNull(this.test.getSuperResearchOrganization());
	}

	@Test
	public void getOrganizationAddress() {
		assertNull(this.test.getOrganizationAddress());
	}

	@Test
	public void setOrganizationAddress_notNull_valid() {
		OrganizationAddress adr = mock(OrganizationAddress.class);
		ResearchOrganization orga = mock(ResearchOrganization.class);
		when(orga.getAddresses()).thenReturn(Collections.singleton(adr));
		this.test.setDirectResearchOrganization(orga);

		this.test.setOrganizationAddress(adr);

		assertSame(adr, this.test.getOrganizationAddress());
	}

	@Test
	public void setOrganizationAddress_notNull_invalid0() {
		OrganizationAddress adr = mock(OrganizationAddress.class);
		ResearchOrganization orga = mock(ResearchOrganization.class);
		when(orga.getAddresses()).thenReturn(Collections.emptySet());
		this.test.setDirectResearchOrganization(orga);

		this.test.setOrganizationAddress(adr);

		assertNull(this.test.getOrganizationAddress());
	}

	@Test
	public void setOrganizationAddress_notNull_invalid1() {
		OrganizationAddress adr = mock(OrganizationAddress.class);
		OrganizationAddress adr1 = mock(OrganizationAddress.class);
		ResearchOrganization orga = mock(ResearchOrganization.class);
		when(orga.getAddresses()).thenReturn(Collections.singleton(adr1));
		this.test.setDirectResearchOrganization(orga);

		this.test.setOrganizationAddress(adr);

		assertNull(this.test.getOrganizationAddress());
	}

	@Test
	public void setOrganizationAddress_null() {
		this.test.setOrganizationAddress(null);
		assertNull(this.test.getOrganizationAddress());
	}

	@Test
	public void getMemberSinceWhen() {
		assertNull(this.test.getMemberSinceWhen());
	}

	@Test
	public void setMemberSinceWhen_LocalDate() {
		assertNull(this.test.getMemberSinceWhen());
		final LocalDate d0 = LocalDate.parse("2022-07-22");
		this.test.setMemberSinceWhen(d0);
		assertSame(d0, this.test.getMemberSinceWhen());
		this.test.setMemberSinceWhen((LocalDate) null);
		assertNull(this.test.getMemberSinceWhen());
	}

	@Test
	public void setMemberSinceWhen_string() {
		assertNull(this.test.getMemberSinceWhen());
		final LocalDate d0 = LocalDate.parse("2022-07-22");
		this.test.setMemberSinceWhen(d0);
		assertSame(d0, this.test.getMemberSinceWhen());
		this.test.setMemberSinceWhen((String) null);
		assertNull(this.test.getMemberSinceWhen());
	}

	@Test
	public void getMemberToWhen() {
		assertNull(this.test.getMemberToWhen());
	}

	@Test
	public void setMemberToWhen_LocalDate() {
		assertNull(this.test.getMemberToWhen());
		final LocalDate d0 = LocalDate.parse("2022-07-22");
		this.test.setMemberToWhen(d0);
		assertSame(d0, this.test.getMemberToWhen());
		this.test.setMemberToWhen((LocalDate) null);
		assertNull(this.test.getMemberToWhen());
	}

	@Test
	public void setMemberToWhen_string() {
		assertNull(this.test.getMemberToWhen());
		final LocalDate d0 = LocalDate.parse("2022-07-22");
		this.test.setMemberToWhen(d0);
		assertSame(d0, this.test.getMemberToWhen());
		this.test.setMemberToWhen((String) null);
		assertNull(this.test.getMemberToWhen());
	}

	@Test
	public void getMemberStatus() {
		assertNull(this.test.getMemberStatus());
	}

	@Test
	public void setMemberStatus_MemberStatus() {
		assertNull(this.test.getMemberStatus());
		final MemberStatus s0 = MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER;
		this.test.setMemberStatus(s0);
		assertSame(s0, this.test.getMemberStatus());
		this.test.setMemberStatus((MemberStatus) null);
		assertNull(this.test.getMemberStatus());
	}

	@Test
	public void setMemberStatus_String() {
		assertNull(this.test.getMemberStatus());
		final MemberStatus s0 = MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER;
		this.test.setMemberStatus(s0.name().toLowerCase());
		assertSame(s0, this.test.getMemberStatus());
		this.test.setMemberStatus((String) null);
		assertNull(this.test.getMemberStatus());
	}

	private void doIsPermanentPositionTest(MemberStatus status) {
		this.test.setMemberStatus(cons(status));
		//
		this.test.setPermanentPosition(true);
		assertEquals(status.isPermanentPositionAllowed(), this.test.isPermanentPosition());
		//
		this.test.setPermanentPosition(false);
		assertFalse(this.test.isPermanentPosition());
	}
	
	@Test
	public void isPermanentPosition() {
		assertFalse(this.test.isPermanentPosition());
		doIsPermanentPositionTest(MemberStatus.ADMIN);
		doIsPermanentPositionTest(MemberStatus.ASSOCIATE_PROFESSOR);
		doIsPermanentPositionTest(MemberStatus.ASSOCIATE_PROFESSOR_HDR);
		doIsPermanentPositionTest(MemberStatus.ASSOCIATED_MEMBER);
		doIsPermanentPositionTest(MemberStatus.ASSOCIATED_MEMBER_PHD);
		doIsPermanentPositionTest(MemberStatus.CONTRACT_MASTER_STUDENT);
		doIsPermanentPositionTest(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER);
		doIsPermanentPositionTest(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD);
		doIsPermanentPositionTest(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR);
		doIsPermanentPositionTest(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR);
		doIsPermanentPositionTest(MemberStatus.EMERITUS_FULL_PROFESSOR);
		doIsPermanentPositionTest(MemberStatus.ENGINEER);
		doIsPermanentPositionTest(MemberStatus.ENGINEER_PHD);
		doIsPermanentPositionTest(MemberStatus.FULL_PROFESSOR);
		doIsPermanentPositionTest(MemberStatus.MASTER_STUDENT);
		doIsPermanentPositionTest(MemberStatus.OTHER_CONTRACT_STUDENT);
		doIsPermanentPositionTest(MemberStatus.OTHER_STUDENT);
		doIsPermanentPositionTest(MemberStatus.PHD_STUDENT);
		doIsPermanentPositionTest(MemberStatus.POSTDOC);
		doIsPermanentPositionTest(MemberStatus.RESEARCH_DIRECTOR);
		doIsPermanentPositionTest(MemberStatus.RESEARCH_ENGINEER);
		doIsPermanentPositionTest(MemberStatus.RESEARCH_ENGINEER_PHD);
		doIsPermanentPositionTest(MemberStatus.RESEARCHER);
		doIsPermanentPositionTest(MemberStatus.RESEARCHER_PHD);
		doIsPermanentPositionTest(MemberStatus.TEACHER);
		doIsPermanentPositionTest(MemberStatus.TEACHER_PHD);
		assertAllTreated();
	}

	@Test
	public void setPermanentPosition() {
		assertFalse(this.test.isPermanentPosition());

	}

	@Test
	public void getCnuSection() {
		assertNull(this.test.getCnuSection());
	}

	@Test
	public void setCnuSection() {
		assertNull(this.test.getCnuSection());
		this.test.setCnuSection(61);
		assertSame(CnuSection.CNU_61, this.test.getCnuSection());
		this.test.setCnuSection(0);
		assertNull(this.test.getCnuSection());
		this.test.setCnuSection(-1);
		assertNull(this.test.getCnuSection());
	}

	@Test
	public void getConrsSection() {
		assertNull(this.test.getConrsSection());
	}

	@Test
	public void setConrsSection() {
		assertNull(this.test.getConrsSection());
		this.test.setConrsSection(31);
		assertSame(ConrsSection.CONRS_31, this.test.getConrsSection());
		this.test.setConrsSection(0);
		assertNull(this.test.getConrsSection());
		this.test.setConrsSection(-1);
		assertNull(this.test.getConrsSection());
	}

	@Test
	public void getFrenchBap() {
		assertNull(this.test.getFrenchBap());
	}

	@Test
	public void setFrenchBap() {
		assertNull(this.test.getFrenchBap());
		this.test.setFrenchBap("bap_b");
		assertSame(FrenchBap.BAP_B, this.test.getFrenchBap());
		this.test.setFrenchBap("c");
		assertSame(FrenchBap.BAP_C, this.test.getFrenchBap());
		this.test.setFrenchBap((FrenchBap) null);
		assertNull(this.test.getFrenchBap());
	}

	@Test
	public void isActive_noStart_noEnd() {
		assertTrue(this.test.isActive());
	}

	@Test
	public void isActive_noStart() {
		this.test.setMemberToWhen(this.past);
		assertFalse(this.test.isActive());
		//
		this.test.setMemberToWhen(this.now);
		assertTrue(this.test.isActive());
		//
		this.test.setMemberToWhen(this.future);
		assertTrue(this.test.isActive());
	}

	@Test
	public void isActive_noEnd() {
		this.test.setMemberSinceWhen(this.past);
		assertTrue(this.test.isActive());
		//
		this.test.setMemberSinceWhen(this.now);
		assertTrue(this.test.isActive());
		//
		this.test.setMemberSinceWhen(this.future);
		assertFalse(this.test.isActive());
	}

	@Test
	public void isActive() {
		this.test.setMemberSinceWhen(this.past);
		this.test.setMemberToWhen(this.past);
		assertFalse(this.test.isActive());
		//
		this.test.setMemberSinceWhen(this.past);
		this.test.setMemberToWhen(this.now);
		assertTrue(this.test.isActive());
		//
		this.test.setMemberSinceWhen(this.past);
		this.test.setMemberToWhen(this.future);
		assertTrue(this.test.isActive());
		//
		this.test.setMemberSinceWhen(this.now);
		this.test.setMemberToWhen(this.now);
		assertTrue(this.test.isActive());
		//
		this.test.setMemberSinceWhen(this.now);
		this.test.setMemberToWhen(this.future);
		assertTrue(this.test.isActive());
		//
		this.test.setMemberSinceWhen(this.future);
		this.test.setMemberToWhen(this.future);
		assertFalse(this.test.isActive());
	}

	@Test
	public void isFormer_noStart_noEnd() {
		assertFalse(this.test.isFormer());
	}

	@Test
	public void isFormer_noStart() {
		this.test.setMemberToWhen(this.past);
		assertTrue(this.test.isFormer());
		//
		this.test.setMemberToWhen(this.now);
		assertFalse(this.test.isFormer());
		//
		this.test.setMemberToWhen(this.future);
		assertFalse(this.test.isFormer());
	}

	@Test
	public void isFormer_noEnd() {
		this.test.setMemberSinceWhen(this.past);
		assertFalse(this.test.isFormer());
		//
		this.test.setMemberSinceWhen(this.now);
		assertFalse(this.test.isFormer());
		//
		this.test.setMemberSinceWhen(this.future);
		assertFalse(this.test.isFormer());
	}

	@Test
	public void isFormer() {
		this.test.setMemberSinceWhen(this.past);
		this.test.setMemberToWhen(this.past);
		assertTrue(this.test.isFormer());
		//
		this.test.setMemberSinceWhen(this.past);
		this.test.setMemberToWhen(this.now);
		assertFalse(this.test.isFormer());
		//
		this.test.setMemberSinceWhen(this.past);
		this.test.setMemberToWhen(this.future);
		assertFalse(this.test.isFormer());
		//
		this.test.setMemberSinceWhen(this.now);
		this.test.setMemberToWhen(this.now);
		assertFalse(this.test.isFormer());
		//
		this.test.setMemberSinceWhen(this.now);
		this.test.setMemberToWhen(this.future);
		assertFalse(this.test.isFormer());
		//
		this.test.setMemberSinceWhen(this.future);
		this.test.setMemberToWhen(this.future);
		assertFalse(this.test.isFormer());
	}

	@Test
	public void isFuture_noStart_noEnd() {
		assertFalse(this.test.isFuture());
	}

	@Test
	public void isFuture_noStart() {
		this.test.setMemberToWhen(this.past);
		assertFalse(this.test.isFuture());
		//
		this.test.setMemberToWhen(this.now);
		assertFalse(this.test.isFuture());
		//
		this.test.setMemberToWhen(this.future);
		assertFalse(this.test.isFuture());
	}

	@Test
	public void isFuture_noEnd() {
		this.test.setMemberSinceWhen(this.past);
		assertFalse(this.test.isFuture());
		//
		this.test.setMemberSinceWhen(this.now);
		assertFalse(this.test.isFuture());
		//
		this.test.setMemberSinceWhen(this.future);
		assertTrue(this.test.isFuture());
	}

	@Test
	public void isFuture() {
		this.test.setMemberSinceWhen(this.past);
		this.test.setMemberToWhen(this.past);
		assertFalse(this.test.isFuture());
		//
		this.test.setMemberSinceWhen(this.past);
		this.test.setMemberToWhen(this.now);
		assertFalse(this.test.isFuture());
		//
		this.test.setMemberSinceWhen(this.past);
		this.test.setMemberToWhen(this.future);
		assertFalse(this.test.isFuture());
		//
		this.test.setMemberSinceWhen(this.now);
		this.test.setMemberToWhen(this.now);
		assertFalse(this.test.isFuture());
		//
		this.test.setMemberSinceWhen(this.now);
		this.test.setMemberToWhen(this.future);
		assertFalse(this.test.isFuture());
		//
		this.test.setMemberSinceWhen(this.future);
		this.test.setMemberToWhen(this.future);
		assertTrue(this.test.isFuture());
	}

	@Test
	public void isActiveAt_noStart_noEnd() {
		final LocalDate ld = LocalDate.of(2022, 5, 5);
		assertTrue(this.test.isActiveAt(ld));
	}

	@Test
	public void isActiveAt_noStart() {
		final LocalDate ld = LocalDate.of(2022, 5, 5);
		//
		this.test.setMemberToWhen(LocalDate.of(2021, 5, 5));
		assertFalse(this.test.isActiveAt(ld));
		//
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
	}

	@Test
	public void isActiveAt_noEnd() {
		final LocalDate ld = LocalDate.of(2022, 5, 5);
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 5, 5));
		assertFalse(this.test.isActiveAt(ld));
	}

	@Test
	public void isActiveAt() {
		final LocalDate ld = LocalDate.of(2022, 5, 5);
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2021, 5, 5));
		assertFalse(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveAt(ld));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertFalse(this.test.isActiveAt(ld));
	}

	@Test
	@DisplayName("isActiveIn(w1, w2) with [null, null]")
	public void isActiveIn_notNull_notNull_noStart_noEnd() {
		final LocalDate s = LocalDate.of(2022, 1, 1);
		final LocalDate e = LocalDate.of(2022, 12, 31);
		assertTrue(this.test.isActiveIn(s, e));
	}

	@Test
	@DisplayName("isActiveIn(null, null) with [null, null]")
	public void isActiveIn_null_null_noStart_noEnd() {
		final LocalDate s = null;
		final LocalDate e = null;
		assertTrue(this.test.isActiveIn(s, e));
	}

	@Test
	@DisplayName("isActiveIn(w1, null) with [null, null]")
	public void isActiveIn_notNull_null_noStart_noEnd() {
		final LocalDate s = LocalDate.of(2022, 1, 1);
		final LocalDate e = null;
		assertTrue(this.test.isActiveIn(s, e));
	}

	@Test
	@DisplayName("isActiveIn(null, w2) with [null, null]")
	public void isActiveIn_null_notNull_noStart_noEnd() {
		final LocalDate s = null;
		final LocalDate e = LocalDate.of(2022, 12, 31);
		assertTrue(this.test.isActiveIn(s, e));
	}

	@Test
	@DisplayName("isActiveIn(w1, w2) with [null, d2]")
	public void isActiveIn_notNull_notNull_noStart() {
		final LocalDate s = LocalDate.of(2022, 1, 1);
		final LocalDate e = LocalDate.of(2022, 12, 31);
		//
		this.test.setMemberToWhen(LocalDate.of(2021, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2022, 1, 1));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2021, 12, 31));
		assertFalse(this.test.isActiveIn(s, e));
	}

	@Test
	@DisplayName("isActiveIn(null, null) with [null, d2]")
	public void isActiveIn_null_null_noStart() {
		final LocalDate s = null;
		final LocalDate e = null;
		//
		this.test.setMemberToWhen(LocalDate.of(2021, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2022, 1, 1));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2021, 12, 31));
		assertTrue(this.test.isActiveIn(s, e));
	}

	@Test
	@DisplayName("isActiveIn(w1, null) with [null, d2]")
	public void isActiveIn_notNull_null_noStart() {
		final LocalDate s = LocalDate.of(2022, 1, 1);
		final LocalDate e = null;
		//
		this.test.setMemberToWhen(LocalDate.of(2021, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2022, 1, 1));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2021, 12, 31));
		assertFalse(this.test.isActiveIn(s, e));
	}

	@Test
	@DisplayName("isActiveIn(null, w2) with [null, d2]")
	public void isActiveIn_null_notNull_noStart() {
		final LocalDate s = null;
		final LocalDate e = LocalDate.of(2022, 12, 31);
		//
		this.test.setMemberToWhen(LocalDate.of(2021, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2022, 1, 1));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberToWhen(LocalDate.of(2021, 12, 31));
		assertTrue(this.test.isActiveIn(s, e));
	}

	@Test
	@DisplayName("isActiveIn(w1, w2) with [d1, null]")
	public void isActiveIn_notNull_notNull_noEnd() {
		final LocalDate s = LocalDate.of(2022, 1, 1);
		final LocalDate e = LocalDate.of(2022, 12, 31);
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 12, 31));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 01, 01));
		assertFalse(this.test.isActiveIn(s, e));
	}

	@Test
	@DisplayName("isActiveIn(null, null) with [d1, null]")
	public void isActiveIn_null_null_noEnd() {
		final LocalDate s = null;
		final LocalDate e = null;
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 12, 31));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 01, 01));
		assertTrue(this.test.isActiveIn(s, e));
	}

	@Test
	@DisplayName("isActiveIn(w1, null) with [d1, null]")
	public void isActiveIn_notNull_null_noEnd() {
		final LocalDate s = LocalDate.of(2022, 1, 1);
		final LocalDate e = null;
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 12, 31));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 01, 01));
		assertTrue(this.test.isActiveIn(s, e));
	}

	@Test
	@DisplayName("isActiveIn(null, w2) with [d1, null]")
	public void isActiveIn_null_notNull_noEnd() {
		final LocalDate s = null;
		final LocalDate e = LocalDate.of(2022, 12, 31);
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 12, 31));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 01, 01));
		assertFalse(this.test.isActiveIn(s, e));
	}

	@Test
	@DisplayName("isActiveIn(w1, w2) with [d1, d2]")
	public void isActiveIn_notNull_notNull() {
		final LocalDate s = LocalDate.of(2022, 1, 1);
		final LocalDate e = LocalDate.of(2022, 12, 31);
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2021, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
	}

	@Test
	@DisplayName("isActiveIn(null, null) with [d1, d2]")
	public void isActiveIn_null_null() {
		final LocalDate s = null;
		final LocalDate e = null;
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2021, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
	}

	@Test
	@DisplayName("isActiveIn(w1, null) with [d1, d2]")
	public void isActiveIn_notNull_null() {
		final LocalDate s = LocalDate.of(2022, 1, 1);
		final LocalDate e = null;
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2021, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
	}

	@Test
	@DisplayName("isActiveIn(null, w2) with [d1, d2]")
	public void isActiveIn_null_notNull() {
		final LocalDate s = null;
		final LocalDate e = LocalDate.of(2022, 12, 31);
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2021, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2022, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertTrue(this.test.isActiveIn(s, e));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 5, 5));
		this.test.setMemberToWhen(LocalDate.of(2023, 5, 5));
		assertFalse(this.test.isActiveIn(s, e));
	}

	@Test
	public void isMainPosition() {
		assertTrue(this.test.isMainPosition());
	}

	@Test
	public void setMainPosition() {
		assertTrue(this.test.isMainPosition());
		this.test.setMainPosition(false);
		assertFalse(this.test.isMainPosition());
		this.test.setMainPosition(true);
		assertTrue(this.test.isMainPosition());
	}

	@Test
	public void setMainPosition_Boolean() {
		assertTrue(this.test.isMainPosition());
		this.test.setMainPosition(Boolean.FALSE);
		assertFalse(this.test.isMainPosition());
		this.test.setMainPosition(Boolean.TRUE);
		assertTrue(this.test.isMainPosition());
		this.test.setMainPosition(Boolean.FALSE);
		assertFalse(this.test.isMainPosition());
		this.test.setMainPosition((Boolean) null);
		assertTrue(this.test.isMainPosition());
	}

	@Test
	public void getResponsibility() {
		assertNull(this.test.getResponsibility());
	}

	@Test
	public void setResponsibility_Responsibility() {
		assertNull(this.test.getResponsibility());
		final Responsibility r0 = Responsibility.COMMUNICATION_VICE_PRESIDENT;
		this.test.setResponsibility(r0);
		assertSame(r0, this.test.getResponsibility());
		this.test.setResponsibility((Responsibility) null);
		assertNull(this.test.getResponsibility());
	}

	@Test
	public void setResponsibility_String() {
		assertNull(this.test.getResponsibility());
		final Responsibility r0 = Responsibility.BUSINESS_UNIT_RESPONSIBLE;
		this.test.setResponsibility(r0.name().toLowerCase());
		assertSame(r0, this.test.getResponsibility());
		this.test.setResponsibility((String) null);
		assertNull(this.test.getResponsibility());
	}

	@Test
	public void getScientificAxes() {
		assertTrue(this.test.getScientificAxes().isEmpty());
	}

	@Test
	public void setScientificAxes() {
		Set<Membership> l0 = new HashSet<>();
		ScientificAxis a0 = mock(ScientificAxis.class);
		when(a0.getMemberships()).thenReturn(l0);
		Set<Membership> l1 = new HashSet<>();
		ScientificAxis a1 = mock(ScientificAxis.class);
		when(a1.getMemberships()).thenReturn(l1);
		List<ScientificAxis> axes = Arrays.asList(a0, a1);

		this.test.setScientificAxes(axes);

		Set<ScientificAxis> actual = this.test.getScientificAxes();
		assertNotNull(actual);
		assertEquals(2, actual.size());
		assertTrue(actual.contains(a0));
		assertTrue(actual.contains(a1));

		assertFalse(l0.contains(this.test));
		assertFalse(l1.contains(this.test));
	}

	@Test
	public void daysInYear_noStart_noEnd() {
		assertEquals(365, this.test.daysInYear(2022));
	}

	@Test
	public void daysInYear_noStart() {
		this.test.setMemberToWhen(LocalDate.of(2021, 7, 1));
		assertEquals(0, this.test.daysInYear(2022));
		//
		this.test.setMemberToWhen(LocalDate.of(2022, 7, 1));
		assertEquals(182, this.test.daysInYear(2022));
		//
		this.test.setMemberToWhen(LocalDate.of(2023, 7, 1));
		assertEquals(365, this.test.daysInYear(2022));
	}

	@Test
	public void daysInYear_noEnd() {
		this.test.setMemberSinceWhen(LocalDate.of(2021, 7, 1));
		assertEquals(365, this.test.daysInYear(2022));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 7, 1));
		assertEquals(184, this.test.daysInYear(2022));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 7, 1));
		assertEquals(0, this.test.daysInYear(2022));
	}

	@Test
	public void daysInYear() {
		this.test.setMemberSinceWhen(LocalDate.of(2021, 7, 1));
		this.test.setMemberToWhen(LocalDate.of(2021, 12, 1));
		assertEquals(0, this.test.daysInYear(2022));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 7, 1));
		this.test.setMemberToWhen(LocalDate.of(2022, 7, 1));
		assertEquals(182, this.test.daysInYear(2022));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2021, 7, 1));
		this.test.setMemberToWhen(LocalDate.of(2023, 7, 1));
		assertEquals(365, this.test.daysInYear(2022));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 7, 1));
		this.test.setMemberToWhen(LocalDate.of(2022, 12, 1));
		assertEquals(154, this.test.daysInYear(2022));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2022, 7, 1));
		this.test.setMemberToWhen(LocalDate.of(2023, 7, 1));
		assertEquals(184, this.test.daysInYear(2022));
		//
		this.test.setMemberSinceWhen(LocalDate.of(2023, 7, 1));
		this.test.setMemberToWhen(LocalDate.of(2024, 7, 1));
		assertEquals(0, this.test.daysInYear(2022));
	}

	private final Person p1 = mock(Person.class);
	private final ResearchOrganization o1 = mock(ResearchOrganization.class);
	private final LocalDate s1 = LocalDate.of(2023, 12, 22);
	private final LocalDate e1 = LocalDate.of(2023, 12, 24);
	private final Person p2 = mock(Person.class);
	private final ResearchOrganization o2 = mock(ResearchOrganization.class);
	private final LocalDate s2 = LocalDate.of(2023, 12, 22);
	private final LocalDate e2 = LocalDate.of(2023, 12, 24);

	private Membership createTransient1() {
		var e = new Membership();
		e.setPerson(this.p1);
		e.setDirectResearchOrganization(this.o1);
		e.setMemberSinceWhen(this.s1);
		e.setMemberToWhen(this.e1);
		return e;
	}	

	private Membership createTransient2() {
		var e = new Membership();
		e.setPerson(this.p2);
		e.setDirectResearchOrganization(this.o2);
		e.setMemberSinceWhen(this.s2);
		e.setMemberToWhen(this.e2);
		return e;
	}	

	private Membership createManaged1() {
		var e = createTransient1();
		e.setId(10l);
		return e;
	}	

	private Membership createManaged2() {
		var e = createTransient2();
		e.setId(20l);
		return e;
	}	

	@Test
	@DisplayName("t1.equals(null)")
	public void test_equals_0() {
		assertFalse(createTransient1().equals(null));
	}
	
	@Test
	@DisplayName("m1.equals(null)")
	public void test_equals_1() {
		assertFalse(createManaged1().equals(null));
	}

	@Test
	@DisplayName("t1.equals(t1)")
	public void test_equals_2() {
		var t1 = createTransient1();
		assertTrue(t1.equals(t1));
	}

	@Test
	@DisplayName("m1.equals(m1)")
	public void test_equals_3() {
		var m1 = createManaged1();
		assertTrue(m1.equals(m1));
	}

	@Test
	@DisplayName("t1.equals(t2)")
	public void test_equals_4() {
		// 2 transient entities need to be NOT equal
		var t1 = createTransient1();
		var t2 = createTransient2();
		assertFalse(t1.equals(t2));
	}

	@Test
	@DisplayName("t2.equals(t1)")
	public void test_equals_5() {
		// 2 transient entities need to be NOT equal
		var t1 = createTransient1();
		var t2 = createTransient2();
		assertFalse(t2.equals(t1));
	}

	@Test
	@DisplayName("m1.equals(m2)")
	public void test_equals_6() {
		// 2 managed entities that represent different records need to be NOT equal
		var m1 = createManaged1();
		var m2 = createManaged2();
		assertFalse(m1.equals(m2));
	}

	@Test
	@DisplayName("m2.equals(m1)")
	public void test_equals_7() {
		// 2 managed entities that represent different records need to be NOT equal
		var m1 = createManaged1();
		var m2 = createManaged2();
		assertFalse(m2.equals(m1));
	}

	@Test
	@DisplayName("m1.equals(m1')")
	public void test_equals_8() {
		// 2 managed entities that represent the same record need to be equal
		var m1 = createManaged1();
		var m1p = createManaged1();
		assertTrue(m1.equals(m1p));
	}

	@Test
	@DisplayName("m1'.equals(m1)")
	public void test_equals_9() {
		// 2 managed entities that represent the same record need to be equal
		var m1 = createManaged1();
		var m1p = createManaged1();
		assertTrue(m1p.equals(m1));
	}

	@Test
	@DisplayName("m1.equals(t1)")
	public void test_equals_10() {
		// a detached/transient and a managed entity object that represent the same record need to be equal
		var t1 = createTransient1();
		var m1 = createManaged1();
		assertTrue(m1.equals(t1));
	}

	@Test
	@DisplayName("t1.equals(m1)")
	public void test_equals_11() {
		// a detached/transient and a managed entity object that represent the same record need to be equal
		var t1 = createTransient1();
		var m1 = createManaged1();
		assertTrue(t1.equals(m1));
	}

	@Test
	@DisplayName("m1.equals(t2)")
	public void test_equals_12() {
		var t2 = createTransient2();
		var m1 = createManaged1();
		assertFalse(m1.equals(t2));
	}

	@Test
	@DisplayName("t2.equals(m1)")
	public void test_equals_13() {
		var t2 = createTransient2();
		var m1 = createManaged1();
		assertFalse(t2.equals(m1));
	}

	@Test
	@DisplayName("t1.hashCode == t1.hashCode")
	public void test_hashCode_0() {
		var t1 = createTransient1();
		assertEquals(t1.hashCode(), t1.hashCode());
	}

	@Test
	@DisplayName("t1.hashCode == t1p.hashCode")
	public void test_hashCode_1() {
		var t1 = createTransient1();
		var t1p = createTransient1();
		assertEquals(t1.hashCode(), t1p.hashCode());
	}

	@Test
	@DisplayName("t1.hashCode != m1.hashCode")
	public void test_hashCode_2() {
		var t1 = createTransient1();
		var m1 = createManaged1();
		assertNotEquals(t1.hashCode(), m1.hashCode());
	}

}
