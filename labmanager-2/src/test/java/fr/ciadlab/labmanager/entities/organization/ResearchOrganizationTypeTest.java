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

package fr.ciadlab.labmanager.entities.organization;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.ciadlab.labmanager.configuration.BaseMessageSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

	@BeforeEach
	public void setUp() {
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
	public void getLabel_US() {
		// Force the local to be US
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Research Team", cons(ResearchOrganizationType.RESEARCH_TEAM).getLabel());
		assertEquals("Research Department", cons(ResearchOrganizationType.LABORATORY_DEPARTMENT).getLabel());
		assertEquals("Research Laboratory", cons(ResearchOrganizationType.LABORATORY).getLabel());
		assertEquals("Faculty", cons(ResearchOrganizationType.FACULTY).getLabel());
		assertEquals("University", cons(ResearchOrganizationType.UNIVERSITY).getLabel());
		assertEquals("High School", cons(ResearchOrganizationType.HIGH_SCHOOL).getLabel());
		assertEquals("Community of universities", cons(ResearchOrganizationType.COMMUNITY).getLabel());
		assertEquals("Other type of organization", cons(ResearchOrganizationType.OTHER).getLabel());
		assertEquals("Research institute", cons(ResearchOrganizationType.RESEARCH_INSTITUTE).getLabel());
		assertEquals("Research organization", cons(ResearchOrganizationType.RESEARCH_INSTITUTION).getLabel());
		assertEquals("Public administration", cons(ResearchOrganizationType.PUBLIC_ADMINISTRATION).getLabel());
		assertEquals("Public non profit association", cons(ResearchOrganizationType.PUBLIC_NON_PROFIT_ASSOCIATION).getLabel());
		assertEquals("Private non profit association", cons(ResearchOrganizationType.PRIVATE_NON_PROFIT_ASSOCIATION).getLabel());
		assertEquals("Start-up company", cons(ResearchOrganizationType.START_UP_COMPANY).getLabel());
		assertEquals("Very small size company (≤ 50 employees)", cons(ResearchOrganizationType.VERY_SMALL_SIZE_COMPANY).getLabel());
		assertEquals("Small size company (≤ 250 employees)", cons(ResearchOrganizationType.SMALL_SIZE_COMPANY).getLabel());
		assertEquals("Intermediate size company (≤ 5000 employees)", cons(ResearchOrganizationType.INTERMEDIATE_SIZE_COMPANY).getLabel());
		assertEquals("Big size company (≥ 5000 employees)", cons(ResearchOrganizationType.BIG_SIZE_COMPANY).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_FR() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Research Team", cons(ResearchOrganizationType.RESEARCH_TEAM).getLabel());
		assertEquals("Research Department", cons(ResearchOrganizationType.LABORATORY_DEPARTMENT).getLabel());
		assertEquals("Research Laboratory", cons(ResearchOrganizationType.LABORATORY).getLabel());
		assertEquals("Faculty", cons(ResearchOrganizationType.FACULTY).getLabel());
		assertEquals("University", cons(ResearchOrganizationType.UNIVERSITY).getLabel());
		assertEquals("High School", cons(ResearchOrganizationType.HIGH_SCHOOL).getLabel());
		assertEquals("Community of universities", cons(ResearchOrganizationType.COMMUNITY).getLabel());
		assertEquals("Other type of organization", cons(ResearchOrganizationType.OTHER).getLabel());
		assertEquals("Research institute", cons(ResearchOrganizationType.RESEARCH_INSTITUTE).getLabel());
		assertEquals("Research organization", cons(ResearchOrganizationType.RESEARCH_INSTITUTION).getLabel());
		assertEquals("Public administration", cons(ResearchOrganizationType.PUBLIC_ADMINISTRATION).getLabel());
		assertEquals("Public non profit association", cons(ResearchOrganizationType.PUBLIC_NON_PROFIT_ASSOCIATION).getLabel());
		assertEquals("Private non profit association", cons(ResearchOrganizationType.PRIVATE_NON_PROFIT_ASSOCIATION).getLabel());
		assertEquals("Start-up company", cons(ResearchOrganizationType.START_UP_COMPANY).getLabel());
		assertEquals("Very small size company (≤ 50 employees)", cons(ResearchOrganizationType.VERY_SMALL_SIZE_COMPANY).getLabel());
		assertEquals("Small size company (≤ 250 employees)", cons(ResearchOrganizationType.SMALL_SIZE_COMPANY).getLabel());
		assertEquals("Intermediate size company (≤ 5000 employees)", cons(ResearchOrganizationType.INTERMEDIATE_SIZE_COMPANY).getLabel());
		assertEquals("Big size company (≥ 5000 employees)", cons(ResearchOrganizationType.BIG_SIZE_COMPANY).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() {
		assertEquals("Research Team", cons(ResearchOrganizationType.RESEARCH_TEAM).getLabel(Locale.US));
		assertEquals("Research Department", cons(ResearchOrganizationType.LABORATORY_DEPARTMENT).getLabel(Locale.US));
		assertEquals("Research Laboratory", cons(ResearchOrganizationType.LABORATORY).getLabel(Locale.US));
		assertEquals("Faculty", cons(ResearchOrganizationType.FACULTY).getLabel(Locale.US));
		assertEquals("University", cons(ResearchOrganizationType.UNIVERSITY).getLabel(Locale.US));
		assertEquals("High School", cons(ResearchOrganizationType.HIGH_SCHOOL).getLabel(Locale.US));
		assertEquals("Community of universities", cons(ResearchOrganizationType.COMMUNITY).getLabel(Locale.US));
		assertEquals("Other type of organization", cons(ResearchOrganizationType.OTHER).getLabel(Locale.US));
		assertEquals("Research institute", cons(ResearchOrganizationType.RESEARCH_INSTITUTE).getLabel(Locale.US));
		assertEquals("Research organization", cons(ResearchOrganizationType.RESEARCH_INSTITUTION).getLabel(Locale.US));
		assertEquals("Public administration", cons(ResearchOrganizationType.PUBLIC_ADMINISTRATION).getLabel(Locale.US));
		assertEquals("Public non profit association", cons(ResearchOrganizationType.PUBLIC_NON_PROFIT_ASSOCIATION).getLabel(Locale.US));
		assertEquals("Private non profit association", cons(ResearchOrganizationType.PRIVATE_NON_PROFIT_ASSOCIATION).getLabel(Locale.US));
		assertEquals("Start-up company", cons(ResearchOrganizationType.START_UP_COMPANY).getLabel(Locale.US));
		assertEquals("Very small size company (≤ 50 employees)", cons(ResearchOrganizationType.VERY_SMALL_SIZE_COMPANY).getLabel(Locale.US));
		assertEquals("Small size company (≤ 250 employees)", cons(ResearchOrganizationType.SMALL_SIZE_COMPANY).getLabel(Locale.US));
		assertEquals("Intermediate size company (≤ 5000 employees)", cons(ResearchOrganizationType.INTERMEDIATE_SIZE_COMPANY).getLabel(Locale.US));
		assertEquals("Big size company (≥ 5000 employees)", cons(ResearchOrganizationType.BIG_SIZE_COMPANY).getLabel(Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR() {
		assertEquals("\u00C9quipe de recherche", cons(ResearchOrganizationType.RESEARCH_TEAM).getLabel(Locale.FRANCE));
		assertEquals("Département de recherche", cons(ResearchOrganizationType.LABORATORY_DEPARTMENT).getLabel(Locale.FRANCE));
		assertEquals("Laboratoire de recherche", cons(ResearchOrganizationType.LABORATORY).getLabel(Locale.FRANCE));
		assertEquals("Faculté", cons(ResearchOrganizationType.FACULTY).getLabel(Locale.FRANCE));
		assertEquals("Université", cons(ResearchOrganizationType.UNIVERSITY).getLabel(Locale.FRANCE));
		assertEquals("Collège ou Lycée", cons(ResearchOrganizationType.HIGH_SCHOOL).getLabel(Locale.FRANCE));
		assertEquals("Communauté d'universités", cons(ResearchOrganizationType.COMMUNITY).getLabel(Locale.FRANCE));
		assertEquals("Autre type d'organisation", cons(ResearchOrganizationType.OTHER).getLabel(Locale.FRANCE));
		assertEquals("Institut de recherche", cons(ResearchOrganizationType.RESEARCH_INSTITUTE).getLabel(Locale.FRANCE));
		assertEquals("\u00C9tablissement de recherche", cons(ResearchOrganizationType.RESEARCH_INSTITUTION).getLabel(Locale.FRANCE));
		assertEquals("Administration publique", cons(ResearchOrganizationType.PUBLIC_ADMINISTRATION).getLabel(Locale.FRANCE));
		assertEquals("Association publique à but non lucratif", cons(ResearchOrganizationType.PUBLIC_NON_PROFIT_ASSOCIATION).getLabel(Locale.FRANCE));
		assertEquals("Association privée à but non lucratif", cons(ResearchOrganizationType.PRIVATE_NON_PROFIT_ASSOCIATION).getLabel(Locale.FRANCE));
		assertEquals("Entreprise start-up", cons(ResearchOrganizationType.START_UP_COMPANY).getLabel(Locale.FRANCE));
		assertEquals("Très petite entreprise (≤ 50 employés)", cons(ResearchOrganizationType.VERY_SMALL_SIZE_COMPANY).getLabel(Locale.FRANCE));
		assertEquals("Petite ou moyenne entreprise (≤ 250 employés)", cons(ResearchOrganizationType.SMALL_SIZE_COMPANY).getLabel(Locale.FRANCE));
		assertEquals("Entreprise de taille intermédiaire (≤ 5000 employés)", cons(ResearchOrganizationType.INTERMEDIATE_SIZE_COMPANY).getLabel(Locale.FRANCE));
		assertEquals("Grande entreprise (≥ 5000 employés)", cons(ResearchOrganizationType.BIG_SIZE_COMPANY).getLabel(Locale.FRANCE));
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
