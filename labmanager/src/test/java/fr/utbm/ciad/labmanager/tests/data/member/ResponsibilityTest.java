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
import fr.utbm.ciad.labmanager.data.member.Responsibility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link Responsibility}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ResponsibilityTest {

	private List<Responsibility> items;

	private MessageSourceAccessor messages;

	@BeforeEach
	public void setUp() {
		this.messages = BaseMessageSource.getGlobalMessageAccessor();
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(Responsibility.values()));
	}

	private Responsibility cons(Responsibility type) {
		assertTrue(this.items.remove(type), "Expecting enumeration item: " + type.toString());
		return type;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void getLabel_Locale_US_MALE() {
		assertEquals("Vice-President of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("President of the Restricted Administration Council", cons(Responsibility.RESTRICTED_ADMINISTRATION_COUNCIL_PRESIDENT).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Member of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_MEMBER).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Responsible of the business unit", cons(Responsibility.BUSINESS_UNIT_RESPONSIBLE).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Director of Communication", cons(Responsibility.COMMUNICATION_DIRECTOR).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Responsible of communication", cons(Responsibility.COMMUNICATION_RESPONSIBLE).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Vice-President of Communication", cons(Responsibility.COMMUNICATION_VICE_PRESIDENT).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Dean", cons(Responsibility.DEAN).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Department Director", cons(Responsibility.DEPARTMENT_DIRECTOR).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Deputy Director", cons(Responsibility.DEPUTY_DIRECTOR).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Direction Secretary", cons(Responsibility.DIRECTION_SECRETARY).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Director", cons(Responsibility.DIRECTOR).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Executive Director", cons(Responsibility.EXECUTIVE_DIRECTOR).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Faculty Dean", cons(Responsibility.FACULTY_DEAN).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Faculty Director", cons(Responsibility.FACULTY_DIRECTOR).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Responsible of gender equality", cons(Responsibility.GENDER_EQUALITY_RESPONSIBLE).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Responsible of human resources", cons(Responsibility.HUMAN_RESOURCE_RESPONSIBLE).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Responsible of IT", cons(Responsibility.IT_RESPONSIBLE).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Member of the Numeric Council", cons(Responsibility.NUMERIC_COUNCIL_MEMBER).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Director of Numeric", cons(Responsibility.NUMERIC_SERVICE_DIRECTOR).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Vice-President of Numeric", cons(Responsibility.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Responsible of Open Science activities", cons(Responsibility.OPEN_SCIENCE_RESPONSIBLE).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Member of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_MEMBER).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Vice-President of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Director of Pedagogy", cons(Responsibility.PEDAGOGY_DIRECTOR).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("President", cons(Responsibility.PRESIDENT).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Director of Research", cons(Responsibility.RESEARCH_DIRECTOR).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Vice-President of Research", cons(Responsibility.RESEARCH_VICE_PRESIDENT).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Member of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_MEMBER).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Vice-President of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Responsible of the conferences and seminars", cons(Responsibility.SEMINAR_RESPONSIBLE).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Vice-President", cons(Responsibility.VICE_PRESIDENT).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Responsible of a scientific team", cons(Responsibility.TEAM_SCIENTFIC_RESPONSIBLE).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Responsible of a scientific axis", cons(Responsibility.SCIENTIFIC_AXIS_RESPONSIBLE).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Responsible of an application axis", cons(Responsibility.APPLICATION_AXIS_RESPONSIBLE).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Responsible of a technological platform", cons(Responsibility.PLATFORM_RESPONSIBLE).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Responsible of a transverse axis", cons(Responsibility.TRANSVERSE_AXIS_RESPONSIBLE).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Director of the Computer Science department of engineering training under student status", cons(Responsibility.CS_FISE_DIRECTOR).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Director of the Computer Science department of engineering training under apprentice status", cons(Responsibility.CS_FISA_DIRECTOR).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Director of the Center of Energy and Computer Science", cons(Responsibility.ENERGY_CS_CENTER_DIRECTOR).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Director of the Center of Transport and Mobility", cons(Responsibility.TRANSPORT_MOBILITY_CENTER_DIRECTOR).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Director of the international relation service", cons(Responsibility.INTERNATIONAL_SERVICE_DIRECTOR).getLabel(this.messages, Gender.MALE, Locale.US));
		assertEquals("Member of the Laboratory Council", cons(Responsibility.LABORATORY_COUNCIL_MEMBER).getLabel(this.messages, Gender.MALE, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_FEMALE() {
		assertEquals("Vice-President of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("President of the Restricted Administration Council", cons(Responsibility.RESTRICTED_ADMINISTRATION_COUNCIL_PRESIDENT).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Member of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_MEMBER).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Responsible of the business unit", cons(Responsibility.BUSINESS_UNIT_RESPONSIBLE).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Director of Communication", cons(Responsibility.COMMUNICATION_DIRECTOR).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Responsible of communication", cons(Responsibility.COMMUNICATION_RESPONSIBLE).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Vice-President of Communication", cons(Responsibility.COMMUNICATION_VICE_PRESIDENT).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Dean", cons(Responsibility.DEAN).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Department Director", cons(Responsibility.DEPARTMENT_DIRECTOR).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Deputy Director", cons(Responsibility.DEPUTY_DIRECTOR).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Direction Secretary", cons(Responsibility.DIRECTION_SECRETARY).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Director", cons(Responsibility.DIRECTOR).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Executive Director", cons(Responsibility.EXECUTIVE_DIRECTOR).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Faculty Dean", cons(Responsibility.FACULTY_DEAN).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Faculty Director", cons(Responsibility.FACULTY_DIRECTOR).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Responsible of gender equality", cons(Responsibility.GENDER_EQUALITY_RESPONSIBLE).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Responsible of human resources", cons(Responsibility.HUMAN_RESOURCE_RESPONSIBLE).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Responsible of IT", cons(Responsibility.IT_RESPONSIBLE).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Member of the Numeric Council", cons(Responsibility.NUMERIC_COUNCIL_MEMBER).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Director of Numeric", cons(Responsibility.NUMERIC_SERVICE_DIRECTOR).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Vice-President of Numeric", cons(Responsibility.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Responsible of Open Science activities", cons(Responsibility.OPEN_SCIENCE_RESPONSIBLE).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Member of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_MEMBER).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Vice-President of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Director of Pedagogy", cons(Responsibility.PEDAGOGY_DIRECTOR).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("President", cons(Responsibility.PRESIDENT).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Director of Research", cons(Responsibility.RESEARCH_DIRECTOR).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Vice-President of Research", cons(Responsibility.RESEARCH_VICE_PRESIDENT).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Member of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_MEMBER).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Vice-President of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Responsible of the conferences and seminars", cons(Responsibility.SEMINAR_RESPONSIBLE).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Vice-President", cons(Responsibility.VICE_PRESIDENT).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Responsible of a scientific team", cons(Responsibility.TEAM_SCIENTFIC_RESPONSIBLE).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Responsible of a scientific axis", cons(Responsibility.SCIENTIFIC_AXIS_RESPONSIBLE).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Responsible of an application axis", cons(Responsibility.APPLICATION_AXIS_RESPONSIBLE).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Responsible of a technological platform", cons(Responsibility.PLATFORM_RESPONSIBLE).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Responsible of a transverse axis", cons(Responsibility.TRANSVERSE_AXIS_RESPONSIBLE).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Director of the Computer Science department of engineering training under student status", cons(Responsibility.CS_FISE_DIRECTOR).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Director of the Computer Science department of engineering training under apprentice status", cons(Responsibility.CS_FISA_DIRECTOR).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Director of the Center of Energy and Computer Science", cons(Responsibility.ENERGY_CS_CENTER_DIRECTOR).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Director of the Center of Transport and Mobility", cons(Responsibility.TRANSPORT_MOBILITY_CENTER_DIRECTOR).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Director of the international relation service", cons(Responsibility.INTERNATIONAL_SERVICE_DIRECTOR).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertEquals("Member of the Laboratory Council", cons(Responsibility.LABORATORY_COUNCIL_MEMBER).getLabel(this.messages, Gender.FEMALE, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US_OTHER() {
		assertEquals("Vice-President of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_VICE_PRESIDENT).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("President of the Restricted Administration Council", cons(Responsibility.RESTRICTED_ADMINISTRATION_COUNCIL_PRESIDENT).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Member of the Administration Council", cons(Responsibility.ADMINISTRATION_COUNCIL_MEMBER).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Responsible of the business unit", cons(Responsibility.BUSINESS_UNIT_RESPONSIBLE).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Director of Communication", cons(Responsibility.COMMUNICATION_DIRECTOR).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Responsible of communication", cons(Responsibility.COMMUNICATION_RESPONSIBLE).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Vice-President of Communication", cons(Responsibility.COMMUNICATION_VICE_PRESIDENT).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Dean", cons(Responsibility.DEAN).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Department Director", cons(Responsibility.DEPARTMENT_DIRECTOR).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Deputy Director", cons(Responsibility.DEPUTY_DIRECTOR).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Direction Secretary", cons(Responsibility.DIRECTION_SECRETARY).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Director", cons(Responsibility.DIRECTOR).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Executive Director", cons(Responsibility.EXECUTIVE_DIRECTOR).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Faculty Dean", cons(Responsibility.FACULTY_DEAN).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Faculty Director", cons(Responsibility.FACULTY_DIRECTOR).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Responsible of gender equality", cons(Responsibility.GENDER_EQUALITY_RESPONSIBLE).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Responsible of human resources", cons(Responsibility.HUMAN_RESOURCE_RESPONSIBLE).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Responsible of IT", cons(Responsibility.IT_RESPONSIBLE).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Member of the Numeric Council", cons(Responsibility.NUMERIC_COUNCIL_MEMBER).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Director of Numeric", cons(Responsibility.NUMERIC_SERVICE_DIRECTOR).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Vice-President of Numeric", cons(Responsibility.NUMERIC_SERVICE_VICE_PRESIDENT).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Responsible of Open Science activities", cons(Responsibility.OPEN_SCIENCE_RESPONSIBLE).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Member of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_MEMBER).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Vice-President of the Pedagogy Council", cons(Responsibility.PEDAGOGY_COUNCIL_VICE_PRESIDENT).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Director of Pedagogy", cons(Responsibility.PEDAGOGY_DIRECTOR).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("President", cons(Responsibility.PRESIDENT).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Director of Research", cons(Responsibility.RESEARCH_DIRECTOR).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Vice-President of Research", cons(Responsibility.RESEARCH_VICE_PRESIDENT).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Member of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_MEMBER).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Vice-President of the Scientific Council", cons(Responsibility.SCIENTIFIC_COUNCIL_VICE_PRESIDENT).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Responsible of the conferences and seminars", cons(Responsibility.SEMINAR_RESPONSIBLE).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Vice-President", cons(Responsibility.VICE_PRESIDENT).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Responsible of a scientific team", cons(Responsibility.TEAM_SCIENTFIC_RESPONSIBLE).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Responsible of a scientific axis", cons(Responsibility.SCIENTIFIC_AXIS_RESPONSIBLE).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Responsible of an application axis", cons(Responsibility.APPLICATION_AXIS_RESPONSIBLE).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Responsible of a technological platform", cons(Responsibility.PLATFORM_RESPONSIBLE).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Responsible of a transverse axis", cons(Responsibility.TRANSVERSE_AXIS_RESPONSIBLE).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Director of the Computer Science department of engineering training under student status", cons(Responsibility.CS_FISE_DIRECTOR).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Director of the Computer Science department of engineering training under apprentice status", cons(Responsibility.CS_FISA_DIRECTOR).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Director of the Center of Energy and Computer Science", cons(Responsibility.ENERGY_CS_CENTER_DIRECTOR).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Director of the Center of Transport and Mobility", cons(Responsibility.TRANSPORT_MOBILITY_CENTER_DIRECTOR).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Director of the international relation service", cons(Responsibility.INTERNATIONAL_SERVICE_DIRECTOR).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertEquals("Member of the Laboratory Council", cons(Responsibility.LABORATORY_COUNCIL_MEMBER).getLabel(this.messages, Gender.OTHER, Locale.US));
		assertAllTreated();
	}

}
