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

package fr.ciadlab.labmanager.entities.organization;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		assertSame(cons(ResearchOrganizationType.COMMUNITY), ResearchOrganizationType.valueOfCaseInsensitive("commUnitY"));
		assertSame(cons(ResearchOrganizationType.OTHER), ResearchOrganizationType.valueOfCaseInsensitive("oTher"));
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
		assertEquals("Community of universities", cons(ResearchOrganizationType.COMMUNITY).getLabel());
		assertEquals("Other type of organization", cons(ResearchOrganizationType.OTHER).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_FR() {
		// Force the local to be FR
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Equipe de recherche", cons(ResearchOrganizationType.RESEARCH_TEAM).getLabel());
		assertEquals("Département de recherche", cons(ResearchOrganizationType.LABORATORY_DEPARTMENT).getLabel());
		assertEquals("Laboratoire de recherche", cons(ResearchOrganizationType.LABORATORY).getLabel());
		assertEquals("Faculté", cons(ResearchOrganizationType.FACULTY).getLabel());
		assertEquals("Université", cons(ResearchOrganizationType.UNIVERSITY).getLabel());
		assertEquals("Communauté d'universités", cons(ResearchOrganizationType.COMMUNITY).getLabel());
		assertEquals("Autre type d'organisation", cons(ResearchOrganizationType.OTHER).getLabel());
		assertAllTreated();
	}

}
