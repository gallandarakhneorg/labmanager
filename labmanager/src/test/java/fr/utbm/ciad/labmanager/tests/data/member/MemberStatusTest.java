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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.utbm.ciad.labmanager.configuration.messages.BaseMessageSource;
import fr.utbm.ciad.labmanager.data.member.Gender;
import fr.utbm.ciad.labmanager.data.member.MemberStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link MemberStatus}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class MemberStatusTest {

	private List<MemberStatus> items;

	private MessageSourceAccessor messages;
	
	@BeforeEach
	public void setUp() {
		this.messages = BaseMessageSource.getGlobalMessageAccessor();
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(MemberStatus.values()));
	}

	private MemberStatus cons(MemberStatus status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void getHierachicalLevel() {
		assertEquals(0, cons(MemberStatus.EMERITUS_FULL_PROFESSOR).getHierachicalLevel());
		assertEquals(0, cons(MemberStatus.FULL_PROFESSOR).getHierachicalLevel());
		assertEquals(0, cons(MemberStatus.RESEARCH_DIRECTOR).getHierachicalLevel());
		assertEquals(1, cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).getHierachicalLevel());
		assertEquals(2, cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).getHierachicalLevel());
		assertEquals(1, cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getHierachicalLevel());
		assertEquals(2, cons(MemberStatus.ASSOCIATE_PROFESSOR).getHierachicalLevel());
		assertEquals(2, cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getHierachicalLevel());
		assertEquals(2, cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getHierachicalLevel());
		assertEquals(2, cons(MemberStatus.RESEARCHER_PHD).getHierachicalLevel());
		assertEquals(2, cons(MemberStatus.RESEARCHER).getHierachicalLevel());
		assertEquals(3, cons(MemberStatus.RESEARCH_ENGINEER_PHD).getHierachicalLevel());
		assertEquals(3, cons(MemberStatus.RESEARCH_ENGINEER).getHierachicalLevel());
		assertEquals(3, cons(MemberStatus.POSTDOC).getHierachicalLevel());
		assertEquals(4, cons(MemberStatus.PHD_STUDENT).getHierachicalLevel());
		assertEquals(4, cons(MemberStatus.ENGINEER_PHD).getHierachicalLevel());
		assertEquals(4, cons(MemberStatus.ENGINEER).getHierachicalLevel());
		assertEquals(4, cons(MemberStatus.ADMIN).getHierachicalLevel());
		assertEquals(4, cons(MemberStatus.TEACHER_PHD).getHierachicalLevel());
		assertEquals(4, cons(MemberStatus.TEACHER).getHierachicalLevel());
		assertEquals(5, cons(MemberStatus.MASTER_STUDENT).getHierachicalLevel());
		assertEquals(5, cons(MemberStatus.CONTRACT_MASTER_STUDENT).getHierachicalLevel());
		assertEquals(6, cons(MemberStatus.OTHER_CONTRACT_STUDENT).getHierachicalLevel());
		assertEquals(6, cons(MemberStatus.OTHER_STUDENT).getHierachicalLevel());
		assertEquals(7, cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getHierachicalLevel());
		assertEquals(7, cons(MemberStatus.ASSOCIATED_MEMBER).getHierachicalLevel());
		assertAllTreated();
	}

	@Test
	public void getUsualResearchFullTimeEquivalent() {
		assertEquals(0., cons(MemberStatus.EMERITUS_FULL_PROFESSOR).getUsualResearchFullTimeEquivalent());
		assertEquals(0.5, cons(MemberStatus.FULL_PROFESSOR).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.RESEARCH_DIRECTOR).getUsualResearchFullTimeEquivalent());
		assertEquals(0., cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).getUsualResearchFullTimeEquivalent());
		assertEquals(0., cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).getUsualResearchFullTimeEquivalent());
		assertEquals(0.5, cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getUsualResearchFullTimeEquivalent());
		assertEquals(0.5, cons(MemberStatus.ASSOCIATE_PROFESSOR).getUsualResearchFullTimeEquivalent());
		assertEquals(0.5, cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getUsualResearchFullTimeEquivalent());
		assertEquals(0.5, cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.RESEARCHER_PHD).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.RESEARCHER).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.RESEARCH_ENGINEER_PHD).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.RESEARCH_ENGINEER).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.POSTDOC).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.PHD_STUDENT).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.ENGINEER_PHD).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.ENGINEER).getUsualResearchFullTimeEquivalent());
		assertEquals(0., cons(MemberStatus.ADMIN).getUsualResearchFullTimeEquivalent());
		assertEquals(0., cons(MemberStatus.TEACHER_PHD).getUsualResearchFullTimeEquivalent());
		assertEquals(0., cons(MemberStatus.TEACHER).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.MASTER_STUDENT).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.CONTRACT_MASTER_STUDENT).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.OTHER_CONTRACT_STUDENT).getUsualResearchFullTimeEquivalent());
		assertEquals(1., cons(MemberStatus.OTHER_STUDENT).getUsualResearchFullTimeEquivalent());
		assertEquals(0., cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getUsualResearchFullTimeEquivalent());
		assertEquals(0., cons(MemberStatus.ASSOCIATED_MEMBER).getUsualResearchFullTimeEquivalent());
		assertAllTreated();
	}

	@Test
	public void isPermanentPositionAllowed() {
		assertFalse(cons(MemberStatus.EMERITUS_FULL_PROFESSOR).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.FULL_PROFESSOR).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.RESEARCH_DIRECTOR).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.RESEARCHER_PHD).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.RESEARCHER).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.RESEARCH_ENGINEER).isPermanentPositionAllowed());
		assertFalse(cons(MemberStatus.POSTDOC).isPermanentPositionAllowed());
		assertFalse(cons(MemberStatus.PHD_STUDENT).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.ENGINEER_PHD).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.ENGINEER).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.ADMIN).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.TEACHER_PHD).isPermanentPositionAllowed());
		assertTrue(cons(MemberStatus.TEACHER).isPermanentPositionAllowed());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isPermanentPositionAllowed());
		assertFalse(cons(MemberStatus.CONTRACT_MASTER_STUDENT).isPermanentPositionAllowed());
		assertFalse(cons(MemberStatus.OTHER_CONTRACT_STUDENT).isPermanentPositionAllowed());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isPermanentPositionAllowed());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isPermanentPositionAllowed());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER).isPermanentPositionAllowed());
		assertAllTreated();
	}

	@Test
	public void isResearcher() {
		assertTrue(cons(MemberStatus.EMERITUS_FULL_PROFESSOR).isResearcher());
		assertTrue(cons(MemberStatus.FULL_PROFESSOR).isResearcher());
		assertTrue(cons(MemberStatus.RESEARCH_DIRECTOR).isResearcher());
		assertTrue(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).isResearcher());
		assertTrue(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).isResearcher());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isResearcher());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR).isResearcher());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isResearcher());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isResearcher());
		assertTrue(cons(MemberStatus.RESEARCHER_PHD).isResearcher());
		assertTrue(cons(MemberStatus.RESEARCHER).isResearcher());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isResearcher());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER).isResearcher());
		assertTrue(cons(MemberStatus.POSTDOC).isResearcher());
		assertTrue(cons(MemberStatus.PHD_STUDENT).isResearcher());
		assertFalse(cons(MemberStatus.ENGINEER_PHD).isResearcher());
		assertFalse(cons(MemberStatus.ENGINEER).isResearcher());
		assertFalse(cons(MemberStatus.ADMIN).isResearcher());
		assertFalse(cons(MemberStatus.TEACHER_PHD).isResearcher());
		assertFalse(cons(MemberStatus.TEACHER).isResearcher());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isResearcher());
		assertFalse(cons(MemberStatus.CONTRACT_MASTER_STUDENT).isResearcher());
		assertFalse(cons(MemberStatus.OTHER_CONTRACT_STUDENT).isResearcher());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isResearcher());
		assertTrue(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isResearcher());
		assertTrue(cons(MemberStatus.ASSOCIATED_MEMBER).isResearcher());
		assertAllTreated();
	}

	@Test
	public void isTeacher() {
		assertTrue(cons(MemberStatus.EMERITUS_FULL_PROFESSOR).isTeacher());
		assertTrue(cons(MemberStatus.FULL_PROFESSOR).isTeacher());
		assertFalse(cons(MemberStatus.RESEARCH_DIRECTOR).isTeacher());
		assertTrue(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).isTeacher());
		assertTrue(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).isTeacher());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isTeacher());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR).isTeacher());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isTeacher());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isTeacher());
		assertFalse(cons(MemberStatus.RESEARCHER_PHD).isTeacher());
		assertFalse(cons(MemberStatus.RESEARCHER).isTeacher());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isTeacher());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER).isTeacher());
		assertFalse(cons(MemberStatus.POSTDOC).isTeacher());
		assertFalse(cons(MemberStatus.PHD_STUDENT).isTeacher());
		assertFalse(cons(MemberStatus.ENGINEER_PHD).isTeacher());
		assertFalse(cons(MemberStatus.ENGINEER).isTeacher());
		assertFalse(cons(MemberStatus.ADMIN).isTeacher());
		assertTrue(cons(MemberStatus.TEACHER_PHD).isTeacher());
		assertTrue(cons(MemberStatus.TEACHER).isTeacher());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isTeacher());
		assertFalse(cons(MemberStatus.CONTRACT_MASTER_STUDENT).isTeacher());
		assertFalse(cons(MemberStatus.OTHER_CONTRACT_STUDENT).isTeacher());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isTeacher());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isTeacher());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER).isTeacher());
		assertAllTreated();
	}

	@Test
	public void isTechnicalStaff() {
		assertFalse(cons(MemberStatus.EMERITUS_FULL_PROFESSOR).isTechnicalStaff());
		assertFalse(cons(MemberStatus.FULL_PROFESSOR).isTechnicalStaff());
		assertFalse(cons(MemberStatus.RESEARCH_DIRECTOR).isTechnicalStaff());
		assertFalse(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).isTechnicalStaff());
		assertFalse(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).isTechnicalStaff());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isTechnicalStaff());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR).isTechnicalStaff());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isTechnicalStaff());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isTechnicalStaff());
		assertFalse(cons(MemberStatus.RESEARCHER_PHD).isTechnicalStaff());
		assertFalse(cons(MemberStatus.RESEARCHER).isTechnicalStaff());
		assertTrue(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isTechnicalStaff());
		assertTrue(cons(MemberStatus.RESEARCH_ENGINEER).isTechnicalStaff());
		assertFalse(cons(MemberStatus.POSTDOC).isTechnicalStaff());
		assertFalse(cons(MemberStatus.PHD_STUDENT).isTechnicalStaff());
		assertTrue(cons(MemberStatus.ENGINEER_PHD).isTechnicalStaff());
		assertTrue(cons(MemberStatus.ENGINEER).isTechnicalStaff());
		assertFalse(cons(MemberStatus.ADMIN).isTechnicalStaff());
		assertFalse(cons(MemberStatus.TEACHER_PHD).isTechnicalStaff());
		assertFalse(cons(MemberStatus.TEACHER).isTechnicalStaff());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isTechnicalStaff());
		assertTrue(cons(MemberStatus.CONTRACT_MASTER_STUDENT).isTechnicalStaff());
		assertTrue(cons(MemberStatus.OTHER_CONTRACT_STUDENT).isTechnicalStaff());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isTechnicalStaff());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isTechnicalStaff());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER).isTechnicalStaff());
		assertAllTreated();
	}

	@Test
	public void isAdministrativeStaff() {
		assertFalse(cons(MemberStatus.EMERITUS_FULL_PROFESSOR).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.FULL_PROFESSOR).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.RESEARCH_DIRECTOR).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.RESEARCHER_PHD).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.RESEARCHER).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.POSTDOC).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.PHD_STUDENT).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.ENGINEER_PHD).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.ENGINEER).isAdministrativeStaff());
		assertTrue(cons(MemberStatus.ADMIN).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.TEACHER_PHD).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.TEACHER).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.CONTRACT_MASTER_STUDENT).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.OTHER_CONTRACT_STUDENT).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER).isAdministrativeStaff());
		assertAllTreated();
	}

	@Test
	public void isPhDOwner() {
		assertTrue(cons(MemberStatus.EMERITUS_FULL_PROFESSOR).isPhDOwner());
		assertTrue(cons(MemberStatus.FULL_PROFESSOR).isPhDOwner());
		assertTrue(cons(MemberStatus.RESEARCH_DIRECTOR).isPhDOwner());
		assertTrue(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).isPhDOwner());
		assertTrue(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).isPhDOwner());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isPhDOwner());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR).isPhDOwner());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isPhDOwner());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isPhDOwner());
		assertTrue(cons(MemberStatus.RESEARCHER_PHD).isPhDOwner());
		assertFalse(cons(MemberStatus.RESEARCHER).isPhDOwner());
		assertTrue(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isPhDOwner());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER).isPhDOwner());
		assertTrue(cons(MemberStatus.POSTDOC).isPhDOwner());
		assertFalse(cons(MemberStatus.PHD_STUDENT).isPhDOwner());
		assertTrue(cons(MemberStatus.ENGINEER_PHD).isPhDOwner());
		assertFalse(cons(MemberStatus.ENGINEER).isPhDOwner());
		assertFalse(cons(MemberStatus.ADMIN).isPhDOwner());
		assertTrue(cons(MemberStatus.TEACHER_PHD).isPhDOwner());
		assertFalse(cons(MemberStatus.TEACHER).isPhDOwner());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isPhDOwner());
		assertFalse(cons(MemberStatus.CONTRACT_MASTER_STUDENT).isPhDOwner());
		assertFalse(cons(MemberStatus.OTHER_CONTRACT_STUDENT).isPhDOwner());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isPhDOwner());
		assertTrue(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isPhDOwner());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER).isPhDOwner());
		assertAllTreated();
	}

	@Test
	public void isHdrOwner() {
		assertTrue(cons(MemberStatus.EMERITUS_FULL_PROFESSOR).isHdrOwner());
		assertTrue(cons(MemberStatus.FULL_PROFESSOR).isHdrOwner());
		assertTrue(cons(MemberStatus.RESEARCH_DIRECTOR).isHdrOwner());
		assertTrue(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).isHdrOwner());
		assertFalse(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).isHdrOwner());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isHdrOwner());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR).isHdrOwner());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isHdrOwner());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isHdrOwner());
		assertFalse(cons(MemberStatus.RESEARCHER_PHD).isHdrOwner());
		assertFalse(cons(MemberStatus.RESEARCHER).isHdrOwner());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isHdrOwner());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER).isHdrOwner());
		assertFalse(cons(MemberStatus.POSTDOC).isHdrOwner());
		assertFalse(cons(MemberStatus.PHD_STUDENT).isHdrOwner());
		assertFalse(cons(MemberStatus.ENGINEER_PHD).isHdrOwner());
		assertFalse(cons(MemberStatus.ENGINEER).isHdrOwner());
		assertFalse(cons(MemberStatus.ADMIN).isHdrOwner());
		assertFalse(cons(MemberStatus.TEACHER_PHD).isHdrOwner());
		assertFalse(cons(MemberStatus.TEACHER).isHdrOwner());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isHdrOwner());
		assertFalse(cons(MemberStatus.CONTRACT_MASTER_STUDENT).isHdrOwner());
		assertFalse(cons(MemberStatus.OTHER_CONTRACT_STUDENT).isHdrOwner());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isHdrOwner());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isHdrOwner());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER).isHdrOwner());
		assertAllTreated();
	}

	@Test
	public void isSupervisable() {
		assertFalse(cons(MemberStatus.EMERITUS_FULL_PROFESSOR).isSupervisable());
		assertFalse(cons(MemberStatus.FULL_PROFESSOR).isSupervisable());
		assertFalse(cons(MemberStatus.RESEARCH_DIRECTOR).isSupervisable());
		assertFalse(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).isSupervisable());
		assertFalse(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).isSupervisable());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isSupervisable());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR).isSupervisable());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isSupervisable());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isSupervisable());
		assertFalse(cons(MemberStatus.RESEARCHER_PHD).isSupervisable());
		assertFalse(cons(MemberStatus.RESEARCHER).isSupervisable());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isSupervisable());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER).isSupervisable());
		assertTrue(cons(MemberStatus.POSTDOC).isSupervisable());
		assertTrue(cons(MemberStatus.PHD_STUDENT).isSupervisable());
		assertFalse(cons(MemberStatus.ENGINEER_PHD).isSupervisable());
		assertFalse(cons(MemberStatus.ENGINEER).isSupervisable());
		assertFalse(cons(MemberStatus.ADMIN).isSupervisable());
		assertFalse(cons(MemberStatus.TEACHER_PHD).isSupervisable());
		assertFalse(cons(MemberStatus.TEACHER).isSupervisable());
		assertTrue(cons(MemberStatus.MASTER_STUDENT).isSupervisable());
		assertTrue(cons(MemberStatus.CONTRACT_MASTER_STUDENT).isSupervisable());
		assertTrue(cons(MemberStatus.OTHER_CONTRACT_STUDENT).isSupervisable());
		assertTrue(cons(MemberStatus.OTHER_STUDENT).isSupervisable());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isSupervisable());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER).isSupervisable());
		assertAllTreated();
	}

	@Test
	public void isSupervisor() {
		assertTrue(cons(MemberStatus.EMERITUS_FULL_PROFESSOR).isSupervisor());
		assertTrue(cons(MemberStatus.FULL_PROFESSOR).isSupervisor());
		assertTrue(cons(MemberStatus.RESEARCH_DIRECTOR).isSupervisor());
		assertTrue(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).isSupervisor());
		assertFalse(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).isSupervisable());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isSupervisor());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR).isSupervisable());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isSupervisor());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isSupervisor());
		assertTrue(cons(MemberStatus.RESEARCHER_PHD).isSupervisor());
		assertTrue(cons(MemberStatus.RESEARCHER).isSupervisor());
		assertTrue(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isSupervisor());
		assertTrue(cons(MemberStatus.RESEARCH_ENGINEER).isSupervisor());
		assertTrue(cons(MemberStatus.POSTDOC).isSupervisor());
		assertFalse(cons(MemberStatus.PHD_STUDENT).isSupervisor());
		assertTrue(cons(MemberStatus.ENGINEER_PHD).isSupervisor());
		assertFalse(cons(MemberStatus.ENGINEER).isSupervisor());
		assertFalse(cons(MemberStatus.ADMIN).isSupervisor());
		assertTrue(cons(MemberStatus.TEACHER_PHD).isSupervisor());
		assertFalse(cons(MemberStatus.TEACHER).isSupervisor());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isSupervisor());
		assertFalse(cons(MemberStatus.CONTRACT_MASTER_STUDENT).isSupervisor());
		assertFalse(cons(MemberStatus.OTHER_CONTRACT_STUDENT).isSupervisor());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isSupervisor());
		assertTrue(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isSupervisor());
		assertTrue(cons(MemberStatus.ASSOCIATED_MEMBER).isSupervisor());
		assertAllTreated();
	}

	@Test
	public void isExternalPosition() {
		assertFalse(cons(MemberStatus.EMERITUS_FULL_PROFESSOR).isExternalPosition());
		assertFalse(cons(MemberStatus.FULL_PROFESSOR).isExternalPosition());
		assertFalse(cons(MemberStatus.RESEARCH_DIRECTOR).isExternalPosition());
		assertFalse(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).isExternalPosition());
		assertFalse(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).isExternalPosition());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isExternalPosition());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR).isExternalPosition());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isExternalPosition());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isExternalPosition());
		assertFalse(cons(MemberStatus.RESEARCHER_PHD).isExternalPosition());
		assertFalse(cons(MemberStatus.RESEARCHER).isExternalPosition());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isExternalPosition());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER).isExternalPosition());
		assertFalse(cons(MemberStatus.POSTDOC).isExternalPosition());
		assertFalse(cons(MemberStatus.PHD_STUDENT).isExternalPosition());
		assertFalse(cons(MemberStatus.ENGINEER_PHD).isExternalPosition());
		assertFalse(cons(MemberStatus.ENGINEER).isExternalPosition());
		assertFalse(cons(MemberStatus.ADMIN).isExternalPosition());
		assertFalse(cons(MemberStatus.TEACHER_PHD).isExternalPosition());
		assertFalse(cons(MemberStatus.TEACHER).isExternalPosition());
		assertTrue(cons(MemberStatus.MASTER_STUDENT).isExternalPosition());
		assertFalse(cons(MemberStatus.CONTRACT_MASTER_STUDENT).isExternalPosition());
		assertFalse(cons(MemberStatus.OTHER_CONTRACT_STUDENT).isExternalPosition());
		assertTrue(cons(MemberStatus.OTHER_STUDENT).isExternalPosition());
		assertTrue(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isExternalPosition());
		assertTrue(cons(MemberStatus.ASSOCIATED_MEMBER).isExternalPosition());
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_FEMALE() {
		assertEquals("Emeritus Professor", cons(MemberStatus.EMERITUS_FULL_PROFESSOR).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Emeritus Associate Professor HDR", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Emeritus Associate Professor", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Contractual Master Student", cons(MemberStatus.CONTRACT_MASTER_STUDENT).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Contractual Student", cons(MemberStatus.OTHER_CONTRACT_STUDENT).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(this.messages, Gender.FEMALE, false, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_MALE() {
		assertEquals("Emeritus Professor", cons(MemberStatus.EMERITUS_FULL_PROFESSOR).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Emeritus Associate Professor HDR", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Emeritus Associate Professor", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Contractual Master Student", cons(MemberStatus.CONTRACT_MASTER_STUDENT).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Contractual Student", cons(MemberStatus.OTHER_CONTRACT_STUDENT).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(this.messages, Gender.MALE, false, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_OTHER() {
		assertEquals("Emeritus Professor", cons(MemberStatus.EMERITUS_FULL_PROFESSOR).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Emeritus Associate Professor HDR", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Emeritus Associate Professor", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Contractual Master Student", cons(MemberStatus.CONTRACT_MASTER_STUDENT).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Contractual Student", cons(MemberStatus.OTHER_CONTRACT_STUDENT).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(this.messages, Gender.OTHER, false, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_NOTSPECIFIED() {
		assertEquals("Emeritus Professor", cons(MemberStatus.EMERITUS_FULL_PROFESSOR).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Emeritus Associate Professor HDR", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Emeritus Associate Professor", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Contractual Master Student", cons(MemberStatus.CONTRACT_MASTER_STUDENT).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Contractual Student", cons(MemberStatus.OTHER_CONTRACT_STUDENT).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(this.messages, Gender.NOT_SPECIFIED, false, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_NULLGENDER() {
		assertEquals("Emeritus Professor", cons(MemberStatus.EMERITUS_FULL_PROFESSOR).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Emeritus Associate Professor HDR", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Emeritus Associate Professor", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Contractual Master Student", cons(MemberStatus.CONTRACT_MASTER_STUDENT).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Contractual Student", cons(MemberStatus.OTHER_CONTRACT_STUDENT).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(this.messages, (Gender) null, false, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_Locale_US_FEMALE() {
		assertEquals("Former Emeritus Professor", cons(MemberStatus.EMERITUS_FULL_PROFESSOR).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Emeritus Associate Professor HDR", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Emeritus Associate Professor", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Contractual Master Student", cons(MemberStatus.CONTRACT_MASTER_STUDENT).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Contractual Student", cons(MemberStatus.OTHER_CONTRACT_STUDENT).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(this.messages, Gender.FEMALE, true, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_Locale_US_MALE() {
		assertEquals("Former Emeritus Professor", cons(MemberStatus.EMERITUS_FULL_PROFESSOR).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Emeritus Associate Professor HDR", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Emeritus Associate Professor", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Contractual Master Student", cons(MemberStatus.CONTRACT_MASTER_STUDENT).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Contractual Student", cons(MemberStatus.OTHER_CONTRACT_STUDENT).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(this.messages, Gender.MALE, true, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_Locale_US_OTHER() {
		assertEquals("Former Emeritus Professor", cons(MemberStatus.EMERITUS_FULL_PROFESSOR).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Emeritus Associate Professor HDR", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Emeritus Associate Professor", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Contractual Master Student", cons(MemberStatus.CONTRACT_MASTER_STUDENT).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Contractual Student", cons(MemberStatus.OTHER_CONTRACT_STUDENT).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(this.messages, Gender.OTHER, true, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_Locale_US_NOTSPECIFIED() {
		assertEquals("Former Emeritus Professor", cons(MemberStatus.EMERITUS_FULL_PROFESSOR).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Emeritus Associate Professor HDR", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Emeritus Associate Professor", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Contractual Master Student", cons(MemberStatus.CONTRACT_MASTER_STUDENT).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Contractual Student", cons(MemberStatus.OTHER_CONTRACT_STUDENT).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(this.messages, Gender.NOT_SPECIFIED, true, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_former_Locale_US_NULLGENDER() {
		assertEquals("Former Emeritus Professor", cons(MemberStatus.EMERITUS_FULL_PROFESSOR).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Research Director", cons(MemberStatus.RESEARCH_DIRECTOR).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Emeritus Associate Professor HDR", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Emeritus Associate Professor", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER_PHD).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Researcher", cons(MemberStatus.RESEARCHER).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Postdoc", cons(MemberStatus.POSTDOC).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER_PHD).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Engineer", cons(MemberStatus.ENGINEER).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Administrative Staff", cons(MemberStatus.ADMIN).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER_PHD).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Teacher", cons(MemberStatus.TEACHER).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Contractual Master Student", cons(MemberStatus.CONTRACT_MASTER_STUDENT).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Contractual Student", cons(MemberStatus.OTHER_CONTRACT_STUDENT).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Student", cons(MemberStatus.OTHER_STUDENT).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getLabel(this.messages, null, true, Locale.US));
		assertEquals("Former Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel(this.messages, null, true, Locale.US));
		assertAllTreated();
	}

	@Test
	public void isEmeritus() {
		assertTrue(cons(MemberStatus.EMERITUS_FULL_PROFESSOR).isEmeritus());
		assertFalse(cons(MemberStatus.FULL_PROFESSOR).isEmeritus());
		assertFalse(cons(MemberStatus.RESEARCH_DIRECTOR).isEmeritus());
		assertTrue(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).isEmeritus());
		assertTrue(cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).isEmeritus());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isEmeritus());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR).isEmeritus());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).isEmeritus());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isEmeritus());
		assertFalse(cons(MemberStatus.RESEARCHER_PHD).isEmeritus());
		assertFalse(cons(MemberStatus.RESEARCHER).isEmeritus());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER_PHD).isEmeritus());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER).isEmeritus());
		assertFalse(cons(MemberStatus.POSTDOC).isEmeritus());
		assertFalse(cons(MemberStatus.PHD_STUDENT).isEmeritus());
		assertFalse(cons(MemberStatus.ENGINEER_PHD).isEmeritus());
		assertFalse(cons(MemberStatus.ENGINEER).isEmeritus());
		assertFalse(cons(MemberStatus.ADMIN).isEmeritus());
		assertFalse(cons(MemberStatus.TEACHER_PHD).isEmeritus());
		assertFalse(cons(MemberStatus.TEACHER).isEmeritus());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isEmeritus());
		assertFalse(cons(MemberStatus.CONTRACT_MASTER_STUDENT).isEmeritus());
		assertFalse(cons(MemberStatus.OTHER_CONTRACT_STUDENT).isEmeritus());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isEmeritus());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER_PHD).isEmeritus());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER).isEmeritus());
		assertAllTreated();
	}

	@Test
	public void getFrenchAcronym_US() {
		assertEquals("PR", cons(MemberStatus.EMERITUS_FULL_PROFESSOR).getFrenchAcronym(this.messages));
		assertEquals("PR", cons(MemberStatus.FULL_PROFESSOR).getFrenchAcronym(this.messages));
		assertEquals("DR", cons(MemberStatus.RESEARCH_DIRECTOR).getFrenchAcronym(this.messages));
		assertEquals("MCF", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR_HDR).getFrenchAcronym(this.messages));
		assertEquals("MCF", cons(MemberStatus.EMERITUS_ASSOCIATE_PROFESSOR).getFrenchAcronym(this.messages));
		assertEquals("MCF", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getFrenchAcronym(this.messages));
		assertEquals("MCF", cons(MemberStatus.ASSOCIATE_PROFESSOR).getFrenchAcronym(this.messages));
		assertEquals("ECC", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER_PHD).getFrenchAcronym(this.messages));
		assertEquals("ECC", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getFrenchAcronym(this.messages));
		assertEquals("CR", cons(MemberStatus.RESEARCHER_PHD).getFrenchAcronym(this.messages));
		assertEquals("CR", cons(MemberStatus.RESEARCHER).getFrenchAcronym(this.messages));
		assertEquals("IGR", cons(MemberStatus.RESEARCH_ENGINEER_PHD).getFrenchAcronym(this.messages));
		assertEquals("IGR", cons(MemberStatus.RESEARCH_ENGINEER).getFrenchAcronym(this.messages));
		assertEquals("POSTDOC", cons(MemberStatus.POSTDOC).getFrenchAcronym(this.messages));
		assertEquals("", cons(MemberStatus.PHD_STUDENT).getFrenchAcronym(this.messages));
		assertEquals("IGE", cons(MemberStatus.ENGINEER_PHD).getFrenchAcronym(this.messages));
		assertEquals("IGE", cons(MemberStatus.ENGINEER).getFrenchAcronym(this.messages));
		assertEquals("", cons(MemberStatus.ADMIN).getFrenchAcronym(this.messages));
		assertEquals("PAST", cons(MemberStatus.TEACHER_PHD).getFrenchAcronym(this.messages));
		assertEquals("PAST", cons(MemberStatus.TEACHER).getFrenchAcronym(this.messages));
		assertEquals("", cons(MemberStatus.MASTER_STUDENT).getFrenchAcronym(this.messages));
		assertEquals("APP", cons(MemberStatus.CONTRACT_MASTER_STUDENT).getFrenchAcronym(this.messages));
		assertEquals("APP", cons(MemberStatus.OTHER_CONTRACT_STUDENT).getFrenchAcronym(this.messages));
		assertEquals("", cons(MemberStatus.OTHER_STUDENT).getFrenchAcronym(this.messages));
		assertEquals("", cons(MemberStatus.ASSOCIATED_MEMBER_PHD).getFrenchAcronym(this.messages));
		assertEquals("", cons(MemberStatus.ASSOCIATED_MEMBER).getFrenchAcronym(this.messages));
		assertAllTreated();
	}

}
