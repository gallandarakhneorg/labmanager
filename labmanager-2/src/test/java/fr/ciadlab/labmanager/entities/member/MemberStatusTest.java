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

package fr.ciadlab.labmanager.entities.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

	@BeforeEach
	public void setUp() {
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
		assertEquals(0, cons(MemberStatus.FULL_PROFESSOR).getHierachicalLevel());
		assertEquals(1, cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getHierachicalLevel());
		assertEquals(2, cons(MemberStatus.ASSOCIATE_PROFESSOR).getHierachicalLevel());
		assertEquals(2, cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getHierachicalLevel());
		assertEquals(2, cons(MemberStatus.RESEARCHER).getHierachicalLevel());
		assertEquals(3, cons(MemberStatus.RESEARCH_ENGINEER).getHierachicalLevel());
		assertEquals(3, cons(MemberStatus.POSTDOC).getHierachicalLevel());
		assertEquals(4, cons(MemberStatus.PHD_STUDENT).getHierachicalLevel());
		assertEquals(4, cons(MemberStatus.ENGINEER).getHierachicalLevel());
		assertEquals(4, cons(MemberStatus.ADMIN).getHierachicalLevel());
		assertEquals(4, cons(MemberStatus.TEACHER).getHierachicalLevel());
		assertEquals(5, cons(MemberStatus.MASTER_STUDENT).getHierachicalLevel());
		assertEquals(6, cons(MemberStatus.OTHER_STUDENT).getHierachicalLevel());
		assertEquals(7, cons(MemberStatus.ASSOCIATED_MEMBER).getHierachicalLevel());
		assertAllTreated();
	}

	@Test
	public void isResearcher() {
		assertTrue(cons(MemberStatus.FULL_PROFESSOR).isResearcher());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isResearcher());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR).isResearcher());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isResearcher());
		assertTrue(cons(MemberStatus.RESEARCHER).isResearcher());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER).isResearcher());
		assertTrue(cons(MemberStatus.POSTDOC).isResearcher());
		assertTrue(cons(MemberStatus.PHD_STUDENT).isResearcher());
		assertFalse(cons(MemberStatus.ENGINEER).isResearcher());
		assertFalse(cons(MemberStatus.ADMIN).isResearcher());
		assertFalse(cons(MemberStatus.TEACHER).isResearcher());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isResearcher());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isResearcher());
		assertTrue(cons(MemberStatus.ASSOCIATED_MEMBER).isResearcher());
		assertAllTreated();
	}

	@Test
	public void isTeacher() {
		assertTrue(cons(MemberStatus.FULL_PROFESSOR).isTeacher());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isTeacher());
		assertTrue(cons(MemberStatus.ASSOCIATE_PROFESSOR).isTeacher());
		assertTrue(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isTeacher());
		assertFalse(cons(MemberStatus.RESEARCHER).isTeacher());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER).isTeacher());
		assertFalse(cons(MemberStatus.POSTDOC).isTeacher());
		assertFalse(cons(MemberStatus.PHD_STUDENT).isTeacher());
		assertFalse(cons(MemberStatus.ENGINEER).isTeacher());
		assertFalse(cons(MemberStatus.ADMIN).isTeacher());
		assertTrue(cons(MemberStatus.TEACHER).isTeacher());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isTeacher());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isTeacher());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER).isTeacher());
		assertAllTreated();
	}

	@Test
	public void isTechnicalStaff() {
		assertFalse(cons(MemberStatus.FULL_PROFESSOR).isTechnicalStaff());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isTechnicalStaff());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR).isTechnicalStaff());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isTechnicalStaff());
		assertFalse(cons(MemberStatus.RESEARCHER).isTechnicalStaff());
		assertTrue(cons(MemberStatus.RESEARCH_ENGINEER).isTechnicalStaff());
		assertFalse(cons(MemberStatus.POSTDOC).isTechnicalStaff());
		assertFalse(cons(MemberStatus.PHD_STUDENT).isTechnicalStaff());
		assertTrue(cons(MemberStatus.ENGINEER).isTechnicalStaff());
		assertFalse(cons(MemberStatus.ADMIN).isTechnicalStaff());
		assertFalse(cons(MemberStatus.TEACHER).isTechnicalStaff());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isTechnicalStaff());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isTechnicalStaff());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER).isTechnicalStaff());
		assertAllTreated();
	}

	@Test
	public void isAdministrativeStaff() {
		assertFalse(cons(MemberStatus.FULL_PROFESSOR).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.ASSOCIATE_PROFESSOR).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.RESEARCHER).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.RESEARCH_ENGINEER).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.POSTDOC).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.PHD_STUDENT).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.ENGINEER).isAdministrativeStaff());
		assertTrue(cons(MemberStatus.ADMIN).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.TEACHER).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.MASTER_STUDENT).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.OTHER_STUDENT).isAdministrativeStaff());
		assertFalse(cons(MemberStatus.ASSOCIATED_MEMBER).isAdministrativeStaff());
		assertAllTreated();
	}

	@Test
	public void getLabel_US() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);

		assertEquals("Full Professor", cons(MemberStatus.FULL_PROFESSOR).getLabel());
		assertEquals("Associate Professor HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel());
		assertEquals("Associate Professor", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel());
		assertEquals("Contractual Teacher Researcher", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel());
		assertEquals("Researcher", cons(MemberStatus.RESEARCHER).getLabel());
		assertEquals("Research Engineer", cons(MemberStatus.RESEARCH_ENGINEER).getLabel());
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel());
		assertEquals("PhD Student", cons(MemberStatus.PHD_STUDENT).getLabel());
		assertEquals("Engineer", cons(MemberStatus.ENGINEER).getLabel());
		assertEquals("Administrative Staff", cons(MemberStatus.ADMIN).getLabel());
		assertEquals("Teacher", cons(MemberStatus.TEACHER).getLabel());
		assertEquals("Master Student", cons(MemberStatus.MASTER_STUDENT).getLabel());
		assertEquals("Student", cons(MemberStatus.OTHER_STUDENT).getLabel());
		assertEquals("Associated Member", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_FR() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);

		assertEquals("Professeur des universités", cons(MemberStatus.FULL_PROFESSOR).getLabel());
		assertEquals("Maître de conférences HDR", cons(MemberStatus.ASSOCIATE_PROFESSOR_HDR).getLabel());
		assertEquals("Maître de conférences", cons(MemberStatus.ASSOCIATE_PROFESSOR).getLabel());
		assertEquals("Enseignant chercheur contractuel", cons(MemberStatus.CONTRACTUAL_RESEARCHER_TEACHER).getLabel());
		assertEquals("Chercheur", cons(MemberStatus.RESEARCHER).getLabel());
		assertEquals("Ingénieur de recherche", cons(MemberStatus.RESEARCH_ENGINEER).getLabel());
		assertEquals("Postdoc", cons(MemberStatus.POSTDOC).getLabel());
		assertEquals("Doctorant", cons(MemberStatus.PHD_STUDENT).getLabel());
		assertEquals("Ingénieur", cons(MemberStatus.ENGINEER).getLabel());
		assertEquals("Personnel administratif", cons(MemberStatus.ADMIN).getLabel());
		assertEquals("Enseignant", cons(MemberStatus.TEACHER).getLabel());
		assertEquals("Etudiant master", cons(MemberStatus.MASTER_STUDENT).getLabel());
		assertEquals("Etudiant", cons(MemberStatus.OTHER_STUDENT).getLabel());
		assertEquals("Membre associé", cons(MemberStatus.ASSOCIATED_MEMBER).getLabel());
		assertAllTreated();
	}

}
