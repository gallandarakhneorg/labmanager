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
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link PositionType}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class PositionTypeTest {

	private List<PositionType> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(PositionType.values()));
	}

	private PositionType cons(PositionType type) {
		assertTrue(this.items.remove(type), "Expecting enumeration item: " + type.toString());
		return type;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void getLabel_US_MALE() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Vice-President of the Administration Council", cons(PositionType.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Member of the Administration Council", cons(PositionType.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.MALE));
		assertEquals("Responsible of the business unit", cons(PositionType.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Director of Communication", cons(PositionType.COMMUNICATION_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Responsible of communication", cons(PositionType.COMMUNICATION_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Vice-President of Communication", cons(PositionType.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Dean", cons(PositionType.DEAN).getLabel(Gender.MALE));
		assertEquals("Department Director", cons(PositionType.DEPARTMENT_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Deputy-Director", cons(PositionType.DEPUTY_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Direction Secretary", cons(PositionType.DIRECTION_SECRETARY).getLabel(Gender.MALE));
		assertEquals("Director", cons(PositionType.DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Executive Director", cons(PositionType.EXECUTIVE_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Faculty Dean", cons(PositionType.FACULTY_DEAN).getLabel(Gender.MALE));
		assertEquals("Faculty Director", cons(PositionType.FACULTY_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Responsible of gender equality", cons(PositionType.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Responsible of human resources", cons(PositionType.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Responsible of IT", cons(PositionType.IT_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Member of the Numeric Council", cons(PositionType.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.MALE));
		assertEquals("Director of Numeric", cons(PositionType.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Vice-President of Numeric", cons(PositionType.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Responsible of Open Science activities", cons(PositionType.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Member of the Pedagogy Council", cons(PositionType.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.MALE));
		assertEquals("Vice-President of the Pedagogy Council", cons(PositionType.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Director of Pedagogy", cons(PositionType.PEDAGOGY_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("President", cons(PositionType.PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Director of Research", cons(PositionType.RESEARCH_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Vice-President of Research", cons(PositionType.RESEARCH_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Member of the Scientific Council", cons(PositionType.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.MALE));
		assertEquals("Vice-President of the Scientific Council", cons(PositionType.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Responsible of the conferences and seminars", cons(PositionType.SEMINAR_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Vice-President", cons(PositionType.VICE_PRESIDENT).getLabel(Gender.MALE));
		assertAllTreated();
	}

	@Test
	public void getLabel_US_FEMALE() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Vice-President of the Administration Council", cons(PositionType.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Member of the Administration Council", cons(PositionType.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.FEMALE));
		assertEquals("Responsible of the business unit", cons(PositionType.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Director of Communication", cons(PositionType.COMMUNICATION_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Responsible of communication", cons(PositionType.COMMUNICATION_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of Communication", cons(PositionType.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Dean", cons(PositionType.DEAN).getLabel(Gender.FEMALE));
		assertEquals("Department Director", cons(PositionType.DEPARTMENT_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Deputy-Director", cons(PositionType.DEPUTY_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Direction Secretary", cons(PositionType.DIRECTION_SECRETARY).getLabel(Gender.FEMALE));
		assertEquals("Director", cons(PositionType.DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Executive Director", cons(PositionType.EXECUTIVE_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Faculty Dean", cons(PositionType.FACULTY_DEAN).getLabel(Gender.FEMALE));
		assertEquals("Faculty Director", cons(PositionType.FACULTY_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Responsible of gender equality", cons(PositionType.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Responsible of human resources", cons(PositionType.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Responsible of IT", cons(PositionType.IT_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Member of the Numeric Council", cons(PositionType.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.FEMALE));
		assertEquals("Director of Numeric", cons(PositionType.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of Numeric", cons(PositionType.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Responsible of Open Science activities", cons(PositionType.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Member of the Pedagogy Council", cons(PositionType.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of the Pedagogy Council", cons(PositionType.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Director of Pedagogy", cons(PositionType.PEDAGOGY_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("President", cons(PositionType.PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Director of Research", cons(PositionType.RESEARCH_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of Research", cons(PositionType.RESEARCH_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Member of the Scientific Council", cons(PositionType.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of the Scientific Council", cons(PositionType.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Responsible of the conferences and seminars", cons(PositionType.SEMINAR_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Vice-President", cons(PositionType.VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertAllTreated();
	}

	@Test
	public void getLabel_US_OTHER() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Vice-President of the Administration Council", cons(PositionType.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Member of the Administration Council", cons(PositionType.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.OTHER));
		assertEquals("Responsible of the business unit", cons(PositionType.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Director of Communication", cons(PositionType.COMMUNICATION_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Responsible of communication", cons(PositionType.COMMUNICATION_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Vice-President of Communication", cons(PositionType.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Dean", cons(PositionType.DEAN).getLabel(Gender.OTHER));
		assertEquals("Department Director", cons(PositionType.DEPARTMENT_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Deputy-Director", cons(PositionType.DEPUTY_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Direction Secretary", cons(PositionType.DIRECTION_SECRETARY).getLabel(Gender.OTHER));
		assertEquals("Director", cons(PositionType.DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Executive Director", cons(PositionType.EXECUTIVE_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Faculty Dean", cons(PositionType.FACULTY_DEAN).getLabel(Gender.OTHER));
		assertEquals("Faculty Director", cons(PositionType.FACULTY_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Responsible of gender equality", cons(PositionType.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Responsible of human resources", cons(PositionType.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Responsible of IT", cons(PositionType.IT_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Member of the Numeric Council", cons(PositionType.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.OTHER));
		assertEquals("Director of Numeric", cons(PositionType.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Vice-President of Numeric", cons(PositionType.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Responsible of Open Science activities", cons(PositionType.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Member of the Pedagogy Council", cons(PositionType.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.OTHER));
		assertEquals("Vice-President of the Pedagogy Council", cons(PositionType.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Director of Pedagogy", cons(PositionType.PEDAGOGY_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("President", cons(PositionType.PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Director of Research", cons(PositionType.RESEARCH_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Vice-President of Research", cons(PositionType.RESEARCH_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Member of the Scientific Council", cons(PositionType.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.OTHER));
		assertEquals("Vice-President of the Scientific Council", cons(PositionType.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Responsible of the conferences and seminars", cons(PositionType.SEMINAR_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Vice-President", cons(PositionType.VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertAllTreated();
	}

	@Test
	public void getLabel_FR_MALE() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Vice-President of the Administration Council", cons(PositionType.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Member of the Administration Council", cons(PositionType.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.MALE));
		assertEquals("Responsible of the business unit", cons(PositionType.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Director of Communication", cons(PositionType.COMMUNICATION_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Responsible of communication", cons(PositionType.COMMUNICATION_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Vice-President of Communication", cons(PositionType.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Dean", cons(PositionType.DEAN).getLabel(Gender.MALE));
		assertEquals("Department Director", cons(PositionType.DEPARTMENT_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Deputy-Director", cons(PositionType.DEPUTY_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Direction Secretary", cons(PositionType.DIRECTION_SECRETARY).getLabel(Gender.MALE));
		assertEquals("Director", cons(PositionType.DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Executive Director", cons(PositionType.EXECUTIVE_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Faculty Dean", cons(PositionType.FACULTY_DEAN).getLabel(Gender.MALE));
		assertEquals("Faculty Director", cons(PositionType.FACULTY_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Responsible of gender equality", cons(PositionType.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Responsible of human resources", cons(PositionType.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Responsible of IT", cons(PositionType.IT_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Member of the Numeric Council", cons(PositionType.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.MALE));
		assertEquals("Director of Numeric", cons(PositionType.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Vice-President of Numeric", cons(PositionType.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Responsible of Open Science activities", cons(PositionType.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Member of the Pedagogy Council", cons(PositionType.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.MALE));
		assertEquals("Vice-President of the Pedagogy Council", cons(PositionType.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Director of Pedagogy", cons(PositionType.PEDAGOGY_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("President", cons(PositionType.PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Director of Research", cons(PositionType.RESEARCH_DIRECTOR).getLabel(Gender.MALE));
		assertEquals("Vice-President of Research", cons(PositionType.RESEARCH_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Member of the Scientific Council", cons(PositionType.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.MALE));
		assertEquals("Vice-President of the Scientific Council", cons(PositionType.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE));
		assertEquals("Responsible of the conferences and seminars", cons(PositionType.SEMINAR_RESPONSIBLE).getLabel(Gender.MALE));
		assertEquals("Vice-President", cons(PositionType.VICE_PRESIDENT).getLabel(Gender.MALE));
		assertAllTreated();
	}

	@Test
	public void getLabel_FR_FEMALE() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Vice-President of the Administration Council", cons(PositionType.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Member of the Administration Council", cons(PositionType.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.FEMALE));
		assertEquals("Responsible of the business unit", cons(PositionType.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Director of Communication", cons(PositionType.COMMUNICATION_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Responsible of communication", cons(PositionType.COMMUNICATION_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of Communication", cons(PositionType.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Dean", cons(PositionType.DEAN).getLabel(Gender.FEMALE));
		assertEquals("Department Director", cons(PositionType.DEPARTMENT_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Deputy-Director", cons(PositionType.DEPUTY_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Direction Secretary", cons(PositionType.DIRECTION_SECRETARY).getLabel(Gender.FEMALE));
		assertEquals("Director", cons(PositionType.DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Executive Director", cons(PositionType.EXECUTIVE_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Faculty Dean", cons(PositionType.FACULTY_DEAN).getLabel(Gender.FEMALE));
		assertEquals("Faculty Director", cons(PositionType.FACULTY_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Responsible of gender equality", cons(PositionType.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Responsible of human resources", cons(PositionType.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Responsible of IT", cons(PositionType.IT_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Member of the Numeric Council", cons(PositionType.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.FEMALE));
		assertEquals("Director of Numeric", cons(PositionType.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of Numeric", cons(PositionType.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Responsible of Open Science activities", cons(PositionType.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Member of the Pedagogy Council", cons(PositionType.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of the Pedagogy Council", cons(PositionType.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Director of Pedagogy", cons(PositionType.PEDAGOGY_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("President", cons(PositionType.PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Director of Research", cons(PositionType.RESEARCH_DIRECTOR).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of Research", cons(PositionType.RESEARCH_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Member of the Scientific Council", cons(PositionType.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.FEMALE));
		assertEquals("Vice-President of the Scientific Council", cons(PositionType.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertEquals("Responsible of the conferences and seminars", cons(PositionType.SEMINAR_RESPONSIBLE).getLabel(Gender.FEMALE));
		assertEquals("Vice-President", cons(PositionType.VICE_PRESIDENT).getLabel(Gender.FEMALE));
		assertAllTreated();
	}

	@Test
	public void getLabel_FR_OTHER() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Vice-President of the Administration Council", cons(PositionType.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Member of the Administration Council", cons(PositionType.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.OTHER));
		assertEquals("Responsible of the business unit", cons(PositionType.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Director of Communication", cons(PositionType.COMMUNICATION_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Responsible of communication", cons(PositionType.COMMUNICATION_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Vice-President of Communication", cons(PositionType.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Dean", cons(PositionType.DEAN).getLabel(Gender.OTHER));
		assertEquals("Department Director", cons(PositionType.DEPARTMENT_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Deputy-Director", cons(PositionType.DEPUTY_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Direction Secretary", cons(PositionType.DIRECTION_SECRETARY).getLabel(Gender.OTHER));
		assertEquals("Director", cons(PositionType.DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Executive Director", cons(PositionType.EXECUTIVE_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Faculty Dean", cons(PositionType.FACULTY_DEAN).getLabel(Gender.OTHER));
		assertEquals("Faculty Director", cons(PositionType.FACULTY_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Responsible of gender equality", cons(PositionType.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Responsible of human resources", cons(PositionType.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Responsible of IT", cons(PositionType.IT_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Member of the Numeric Council", cons(PositionType.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.OTHER));
		assertEquals("Director of Numeric", cons(PositionType.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Vice-President of Numeric", cons(PositionType.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Responsible of Open Science activities", cons(PositionType.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Member of the Pedagogy Council", cons(PositionType.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.OTHER));
		assertEquals("Vice-President of the Pedagogy Council", cons(PositionType.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Director of Pedagogy", cons(PositionType.PEDAGOGY_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("President", cons(PositionType.PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Director of Research", cons(PositionType.RESEARCH_DIRECTOR).getLabel(Gender.OTHER));
		assertEquals("Vice-President of Research", cons(PositionType.RESEARCH_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Member of the Scientific Council", cons(PositionType.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.OTHER));
		assertEquals("Vice-President of the Scientific Council", cons(PositionType.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertEquals("Responsible of the conferences and seminars", cons(PositionType.SEMINAR_RESPONSIBLE).getLabel(Gender.OTHER));
		assertEquals("Vice-President", cons(PositionType.VICE_PRESIDENT).getLabel(Gender.OTHER));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_MALE() {
		assertEquals("Vice-President of the Administration Council", cons(PositionType.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.US));
		assertEquals("Member of the Administration Council", cons(PositionType.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.MALE, Locale.US));
		assertEquals("Responsible of the business unit", cons(PositionType.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.MALE, Locale.US));
		assertEquals("Director of Communication", cons(PositionType.COMMUNICATION_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Responsible of communication", cons(PositionType.COMMUNICATION_RESPONSIBLE).getLabel(Gender.MALE, Locale.US));
		assertEquals("Vice-President of Communication", cons(PositionType.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.US));
		assertEquals("Dean", cons(PositionType.DEAN).getLabel(Gender.MALE, Locale.US));
		assertEquals("Department Director", cons(PositionType.DEPARTMENT_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Deputy-Director", cons(PositionType.DEPUTY_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Direction Secretary", cons(PositionType.DIRECTION_SECRETARY).getLabel(Gender.MALE, Locale.US));
		assertEquals("Director", cons(PositionType.DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Executive Director", cons(PositionType.EXECUTIVE_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Faculty Dean", cons(PositionType.FACULTY_DEAN).getLabel(Gender.MALE, Locale.US));
		assertEquals("Faculty Director", cons(PositionType.FACULTY_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Responsible of gender equality", cons(PositionType.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.MALE, Locale.US));
		assertEquals("Responsible of human resources", cons(PositionType.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.MALE, Locale.US));
		assertEquals("Responsible of IT", cons(PositionType.IT_RESPONSIBLE).getLabel(Gender.MALE, Locale.US));
		assertEquals("Member of the Numeric Council", cons(PositionType.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.MALE, Locale.US));
		assertEquals("Director of Numeric", cons(PositionType.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Vice-President of Numeric", cons(PositionType.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.US));
		assertEquals("Responsible of Open Science activities", cons(PositionType.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.MALE, Locale.US));
		assertEquals("Member of the Pedagogy Council", cons(PositionType.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.MALE, Locale.US));
		assertEquals("Vice-President of the Pedagogy Council", cons(PositionType.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.US));
		assertEquals("Director of Pedagogy", cons(PositionType.PEDAGOGY_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("President", cons(PositionType.PRESIDENT).getLabel(Gender.MALE, Locale.US));
		assertEquals("Director of Research", cons(PositionType.RESEARCH_DIRECTOR).getLabel(Gender.MALE, Locale.US));
		assertEquals("Vice-President of Research", cons(PositionType.RESEARCH_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.US));
		assertEquals("Member of the Scientific Council", cons(PositionType.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.MALE, Locale.US));
		assertEquals("Vice-President of the Scientific Council", cons(PositionType.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.US));
		assertEquals("Responsible of the conferences and seminars", cons(PositionType.SEMINAR_RESPONSIBLE).getLabel(Gender.MALE, Locale.US));
		assertEquals("Vice-President", cons(PositionType.VICE_PRESIDENT).getLabel(Gender.MALE, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_FEMALE() {
		assertEquals("Vice-President of the Administration Council", cons(PositionType.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Member of the Administration Council", cons(PositionType.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Responsible of the business unit", cons(PositionType.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Director of Communication", cons(PositionType.COMMUNICATION_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Responsible of communication", cons(PositionType.COMMUNICATION_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Vice-President of Communication", cons(PositionType.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Dean", cons(PositionType.DEAN).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Department Director", cons(PositionType.DEPARTMENT_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Deputy-Director", cons(PositionType.DEPUTY_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Direction Secretary", cons(PositionType.DIRECTION_SECRETARY).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Director", cons(PositionType.DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Executive Director", cons(PositionType.EXECUTIVE_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Faculty Dean", cons(PositionType.FACULTY_DEAN).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Faculty Director", cons(PositionType.FACULTY_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Responsible of gender equality", cons(PositionType.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Responsible of human resources", cons(PositionType.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Responsible of IT", cons(PositionType.IT_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Member of the Numeric Council", cons(PositionType.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Director of Numeric", cons(PositionType.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Vice-President of Numeric", cons(PositionType.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Responsible of Open Science activities", cons(PositionType.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Member of the Pedagogy Council", cons(PositionType.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Vice-President of the Pedagogy Council", cons(PositionType.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Director of Pedagogy", cons(PositionType.PEDAGOGY_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("President", cons(PositionType.PRESIDENT).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Director of Research", cons(PositionType.RESEARCH_DIRECTOR).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Vice-President of Research", cons(PositionType.RESEARCH_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Member of the Scientific Council", cons(PositionType.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Vice-President of the Scientific Council", cons(PositionType.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Responsible of the conferences and seminars", cons(PositionType.SEMINAR_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.US));
		assertEquals("Vice-President", cons(PositionType.VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_OTHER() {
		assertEquals("Vice-President of the Administration Council", cons(PositionType.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Member of the Administration Council", cons(PositionType.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Responsible of the business unit", cons(PositionType.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Director of Communication", cons(PositionType.COMMUNICATION_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Responsible of communication", cons(PositionType.COMMUNICATION_RESPONSIBLE).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Vice-President of Communication", cons(PositionType.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Dean", cons(PositionType.DEAN).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Department Director", cons(PositionType.DEPARTMENT_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Deputy-Director", cons(PositionType.DEPUTY_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Direction Secretary", cons(PositionType.DIRECTION_SECRETARY).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Director", cons(PositionType.DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Executive Director", cons(PositionType.EXECUTIVE_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Faculty Dean", cons(PositionType.FACULTY_DEAN).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Faculty Director", cons(PositionType.FACULTY_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Responsible of gender equality", cons(PositionType.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Responsible of human resources", cons(PositionType.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Responsible of IT", cons(PositionType.IT_RESPONSIBLE).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Member of the Numeric Council", cons(PositionType.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Director of Numeric", cons(PositionType.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Vice-President of Numeric", cons(PositionType.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Responsible of Open Science activities", cons(PositionType.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Member of the Pedagogy Council", cons(PositionType.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Vice-President of the Pedagogy Council", cons(PositionType.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Director of Pedagogy", cons(PositionType.PEDAGOGY_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("President", cons(PositionType.PRESIDENT).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Director of Research", cons(PositionType.RESEARCH_DIRECTOR).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Vice-President of Research", cons(PositionType.RESEARCH_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Member of the Scientific Council", cons(PositionType.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Vice-President of the Scientific Council", cons(PositionType.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Responsible of the conferences and seminars", cons(PositionType.SEMINAR_RESPONSIBLE).getLabel(Gender.OTHER, Locale.US));
		assertEquals("Vice-President", cons(PositionType.VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR_MALE() {
		assertEquals("Vice-président du Conseil d'Administration", cons(PositionType.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Membre du Conseil d'Administration", cons(PositionType.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Responsable de la valorisation", cons(PositionType.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur de la communication", cons(PositionType.COMMUNICATION_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Responsable de la communication", cons(PositionType.COMMUNICATION_RESPONSIBLE).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Vice-président à la communication", cons(PositionType.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Doyen", cons(PositionType.DEAN).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur de département", cons(PositionType.DEPARTMENT_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur-adjoint", cons(PositionType.DEPUTY_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Secrétaire de direction", cons(PositionType.DIRECTION_SECRETARY).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur", cons(PositionType.DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur exécutif", cons(PositionType.EXECUTIVE_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Doyen de Faculté", cons(PositionType.FACULTY_DEAN).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur de Faculté", cons(PositionType.FACULTY_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Responsable à l'égalité des genres", cons(PositionType.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Responsable des ressources humaines", cons(PositionType.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Responsable au numérique", cons(PositionType.IT_RESPONSIBLE).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Membre du Conseil au numérique", cons(PositionType.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur au numérique", cons(PositionType.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Vice-président au numérique", cons(PositionType.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Responsable des activités de Science Ouverte", cons(PositionType.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Membre du Conseil de la Pédagogie", cons(PositionType.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Vice-président du Conseil de la Pédagogie", cons(PositionType.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur de la pédagogie", cons(PositionType.PEDAGOGY_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Président", cons(PositionType.PRESIDENT).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Directeur à la recherche et à la valorisation", cons(PositionType.RESEARCH_DIRECTOR).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Vice-président à la recherche et à la valorisation", cons(PositionType.RESEARCH_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Membre du Conseil Scientifique", cons(PositionType.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Vice-président du Conseil Scientifique", cons(PositionType.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Responsable des conférences et des séminaires", cons(PositionType.SEMINAR_RESPONSIBLE).getLabel(Gender.MALE, Locale.FRANCE));
		assertEquals("Vice-président", cons(PositionType.VICE_PRESIDENT).getLabel(Gender.MALE, Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR_FEMALE() {
		assertEquals("Vice-présidente du Conseil d'Administration", cons(PositionType.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Membre du Conseil d'Administration", cons(PositionType.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Responsable de la valorisation", cons(PositionType.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice de la communication", cons(PositionType.COMMUNICATION_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Responsable de la communication", cons(PositionType.COMMUNICATION_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Vice-présidente à la communication", cons(PositionType.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Doyenne", cons(PositionType.DEAN).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice de département", cons(PositionType.DEPARTMENT_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice-adjointe", cons(PositionType.DEPUTY_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Secrétaire de direction", cons(PositionType.DIRECTION_SECRETARY).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice", cons(PositionType.DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice exécutive", cons(PositionType.EXECUTIVE_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Doyenne de Faculté", cons(PositionType.FACULTY_DEAN).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice de Faculté", cons(PositionType.FACULTY_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Responsable à l'égalité des genres", cons(PositionType.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Responsable des ressources humaines", cons(PositionType.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Responsable au numérique", cons(PositionType.IT_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Membre du Conseil au numérique", cons(PositionType.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice au numérique", cons(PositionType.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Vice-présidente au numérique", cons(PositionType.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Responsable des activités de Science Ouverte", cons(PositionType.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Membre du Conseil de la Pédagogie", cons(PositionType.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Vice-présidente du Conseil de la Pédagogie", cons(PositionType.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice de la pédagogie", cons(PositionType.PEDAGOGY_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Présidente", cons(PositionType.PRESIDENT).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Directrice à la recherche et à la valorisation", cons(PositionType.RESEARCH_DIRECTOR).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Vice-présidente à la recherche et à la valorisation", cons(PositionType.RESEARCH_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Membre du Conseil Scientifique", cons(PositionType.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Vice-présidente du Conseil Scientifique", cons(PositionType.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Responsable des conférences et des séminaires", cons(PositionType.SEMINAR_RESPONSIBLE).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertEquals("Vice-présidente", cons(PositionType.VICE_PRESIDENT).getLabel(Gender.FEMALE, Locale.FRANCE));
		assertAllTreated();
	}


	@Test
	public void getLabel_Locale_FR_OTHER() {
		assertEquals("Vice-président.e du Conseil d'Administration", cons(PositionType.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Membre du Conseil d'Administration", cons(PositionType.ADMINISTRATION_COUNCIL_MEMBER).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Responsable de la valorisation", cons(PositionType.BUSINESS_UNIT_RESPONSIBLE).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice de la communication", cons(PositionType.COMMUNICATION_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Responsable de la communication", cons(PositionType.COMMUNICATION_RESPONSIBLE).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Vice-président.e à la communication", cons(PositionType.COMMUNICATION_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Doyen.ne", cons(PositionType.DEAN).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice de département", cons(PositionType.DEPARTMENT_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice-adjoint.e", cons(PositionType.DEPUTY_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Secrétaire de direction", cons(PositionType.DIRECTION_SECRETARY).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice", cons(PositionType.DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice exécutif.ve", cons(PositionType.EXECUTIVE_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Doyen.ne de Faculté", cons(PositionType.FACULTY_DEAN).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice de Faculté", cons(PositionType.FACULTY_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Responsable à l'égalité des genres", cons(PositionType.GENDER_EQUALITY_RESPONSIBLE).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Responsable des ressources humaines", cons(PositionType.HUMAN_RESOURCE_RESPONSIBLE).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Responsable au numérique", cons(PositionType.IT_RESPONSIBLE).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Membre du Conseil au numérique", cons(PositionType.NUMERIC_COUNCIL_MEMBER).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice au numérique", cons(PositionType.NUMERIC_SERVICE_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Vice-président.e au numérique", cons(PositionType.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Responsable des activités de Science Ouverte", cons(PositionType.OPEN_SCIENCE_RESPONSIBLE).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Membre du Conseil de la Pédagogie", cons(PositionType.PEDAGOGY_COUNCIL_MEMBER).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Vice-président.e du Conseil de la Pédagogie", cons(PositionType.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice de la pédagogie", cons(PositionType.PEDAGOGY_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Président.e", cons(PositionType.PRESIDENT).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Directeur.trice à la recherche et à la valorisation", cons(PositionType.RESEARCH_DIRECTOR).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Vice-président.e à la recherche et à la valorisation", cons(PositionType.RESEARCH_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Membre du Conseil Scientifique", cons(PositionType.SCIENTIFIC_COUNCIL_MEMBER).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Vice-président.e du Conseil Scientifique", cons(PositionType.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Responsable des conférences et des séminaires", cons(PositionType.SEMINAR_RESPONSIBLE).getLabel(Gender.OTHER, Locale.FRANCE));
		assertEquals("Vice-président.e", cons(PositionType.VICE_PRESIDENT).getLabel(Gender.OTHER, Locale.FRANCE));
		assertAllTreated();
	}

}
