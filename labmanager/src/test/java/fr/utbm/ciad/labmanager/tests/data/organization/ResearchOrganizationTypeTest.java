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

package fr.utbm.ciad.labmanager.tests.data.organization;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.utbm.ciad.labmanager.configuration.messages.BaseMessageSource;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganizationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link ResearchOrganizationType}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ResearchOrganizationTypeTest {

	private List<ResearchOrganizationType> items;

	private MessageSourceAccessor messages;

	@BeforeEach
	public void setUp() {
		this.messages = BaseMessageSource.getGlobalMessageAccessor();
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(ResearchOrganizationType.values()));
	}

	private ResearchOrganizationType cons(ResearchOrganizationType status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void valueOfCaseInsensitive() {
		assertSame(cons(ResearchOrganizationType.RESEARCH_TEAM), ResearchOrganizationType.valueOfCaseInsensitive("reSearch_teAm"));
		assertSame(cons(ResearchOrganizationType.LABORATORY_DEPARTMENT), ResearchOrganizationType.valueOfCaseInsensitive("lAbOratory_depaRtment"));
		assertSame(cons(ResearchOrganizationType.LABORATORY), ResearchOrganizationType.valueOfCaseInsensitive("lAbOratory"));
		assertSame(cons(ResearchOrganizationType.FACULTY), ResearchOrganizationType.valueOfCaseInsensitive("FaculTy"));
		assertSame(cons(ResearchOrganizationType.UNIVERSITY), ResearchOrganizationType.valueOfCaseInsensitive("UnivErsIty"));
		assertSame(cons(ResearchOrganizationType.HIGH_SCHOOL), ResearchOrganizationType.valueOfCaseInsensitive("High_school"));
		assertSame(cons(ResearchOrganizationType.COMMUNITY), ResearchOrganizationType.valueOfCaseInsensitive("commUnitY"));
		assertSame(cons(ResearchOrganizationType.OTHER), ResearchOrganizationType.valueOfCaseInsensitive("oTher"));
		assertSame(cons(ResearchOrganizationType.RESEARCH_INSTITUTE), ResearchOrganizationType.valueOfCaseInsensitive("reSearch_Institute"));
		assertSame(cons(ResearchOrganizationType.RESEARCH_INSTITUTION), ResearchOrganizationType.valueOfCaseInsensitive("Research_Institution"));
		assertSame(cons(ResearchOrganizationType.PUBLIC_ADMINISTRATION), ResearchOrganizationType.valueOfCaseInsensitive("Public_administration"));
		assertSame(cons(ResearchOrganizationType.PUBLIC_NON_PROFIT_ASSOCIATION), ResearchOrganizationType.valueOfCaseInsensitive("Public_non_profit_association"));
		assertSame(cons(ResearchOrganizationType.PRIVATE_NON_PROFIT_ASSOCIATION), ResearchOrganizationType.valueOfCaseInsensitive("Private_non_profit_Association"));
		assertSame(cons(ResearchOrganizationType.START_UP_COMPANY), ResearchOrganizationType.valueOfCaseInsensitive("Start_Up_Company"));
		assertSame(cons(ResearchOrganizationType.VERY_SMALL_SIZE_COMPANY), ResearchOrganizationType.valueOfCaseInsensitive("Very_small_size_Company"));
		assertSame(cons(ResearchOrganizationType.SMALL_SIZE_COMPANY), ResearchOrganizationType.valueOfCaseInsensitive("Small_size_Company"));
		assertSame(cons(ResearchOrganizationType.INTERMEDIATE_SIZE_COMPANY), ResearchOrganizationType.valueOfCaseInsensitive("Intermediate_size_Company"));
		assertSame(cons(ResearchOrganizationType.BIG_SIZE_COMPANY), ResearchOrganizationType.valueOfCaseInsensitive("Big_size_Company"));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() {
		assertEquals("Research Team", cons(ResearchOrganizationType.RESEARCH_TEAM).getLabel(this.messages, Locale.US));
		assertEquals("Research Department", cons(ResearchOrganizationType.LABORATORY_DEPARTMENT).getLabel(this.messages, Locale.US));
		assertEquals("Research Laboratory", cons(ResearchOrganizationType.LABORATORY).getLabel(this.messages, Locale.US));
		assertEquals("Faculty", cons(ResearchOrganizationType.FACULTY).getLabel(this.messages, Locale.US));
		assertEquals("University", cons(ResearchOrganizationType.UNIVERSITY).getLabel(this.messages, Locale.US));
		assertEquals("High School", cons(ResearchOrganizationType.HIGH_SCHOOL).getLabel(this.messages, Locale.US));
		assertEquals("Community of universities", cons(ResearchOrganizationType.COMMUNITY).getLabel(this.messages, Locale.US));
		assertEquals("Other type of organization", cons(ResearchOrganizationType.OTHER).getLabel(this.messages, Locale.US));
		assertEquals("Research institute", cons(ResearchOrganizationType.RESEARCH_INSTITUTE).getLabel(this.messages, Locale.US));
		assertEquals("Research organization", cons(ResearchOrganizationType.RESEARCH_INSTITUTION).getLabel(this.messages, Locale.US));
		assertEquals("Public administration", cons(ResearchOrganizationType.PUBLIC_ADMINISTRATION).getLabel(this.messages, Locale.US));
		assertEquals("Public non profit association", cons(ResearchOrganizationType.PUBLIC_NON_PROFIT_ASSOCIATION).getLabel(this.messages, Locale.US));
		assertEquals("Private non profit association", cons(ResearchOrganizationType.PRIVATE_NON_PROFIT_ASSOCIATION).getLabel(this.messages, Locale.US));
		assertEquals("Start-up company", cons(ResearchOrganizationType.START_UP_COMPANY).getLabel(this.messages, Locale.US));
		assertEquals("Very small size company (≤ 50 employees)", cons(ResearchOrganizationType.VERY_SMALL_SIZE_COMPANY).getLabel(this.messages, Locale.US));
		assertEquals("Small size company (≤ 250 employees)", cons(ResearchOrganizationType.SMALL_SIZE_COMPANY).getLabel(this.messages, Locale.US));
		assertEquals("Intermediate size company (≤ 5000 employees)", cons(ResearchOrganizationType.INTERMEDIATE_SIZE_COMPANY).getLabel(this.messages, Locale.US));
		assertEquals("Big size company (≥ 5000 employees)", cons(ResearchOrganizationType.BIG_SIZE_COMPANY).getLabel(this.messages, Locale.US));
		assertAllTreated();
	}

	@Test
	public void isAcademicType() {
		assertTrue(cons(ResearchOrganizationType.RESEARCH_TEAM).isAcademicType());
		assertTrue(cons(ResearchOrganizationType.LABORATORY_DEPARTMENT).isAcademicType());
		assertTrue(cons(ResearchOrganizationType.LABORATORY).isAcademicType());
		assertTrue(cons(ResearchOrganizationType.FACULTY).isAcademicType());
		assertTrue(cons(ResearchOrganizationType.UNIVERSITY).isAcademicType());
		assertTrue(cons(ResearchOrganizationType.HIGH_SCHOOL).isAcademicType());
		assertTrue(cons(ResearchOrganizationType.COMMUNITY).isAcademicType());
		assertFalse(cons(ResearchOrganizationType.OTHER).isAcademicType());
		assertTrue(cons(ResearchOrganizationType.RESEARCH_INSTITUTE).isAcademicType());
		assertTrue(cons(ResearchOrganizationType.RESEARCH_INSTITUTION).isAcademicType());
		assertFalse(cons(ResearchOrganizationType.PUBLIC_ADMINISTRATION).isAcademicType());
		assertFalse(cons(ResearchOrganizationType.PUBLIC_NON_PROFIT_ASSOCIATION).isAcademicType());
		assertFalse(cons(ResearchOrganizationType.PRIVATE_NON_PROFIT_ASSOCIATION).isAcademicType());
		assertFalse(cons(ResearchOrganizationType.START_UP_COMPANY).isAcademicType());
		assertFalse(cons(ResearchOrganizationType.VERY_SMALL_SIZE_COMPANY).isAcademicType());
		assertFalse(cons(ResearchOrganizationType.SMALL_SIZE_COMPANY).isAcademicType());
		assertFalse(cons(ResearchOrganizationType.INTERMEDIATE_SIZE_COMPANY).isAcademicType());
		assertFalse(cons(ResearchOrganizationType.BIG_SIZE_COMPANY).isAcademicType());
		assertAllTreated();
	}

}
