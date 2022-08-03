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

package fr.ciadlab.labmanager.io;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;

import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link ExporterConfigurator}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ExporterConfiguratorTest {

	private ExporterConfigurator test;

	@BeforeEach
	public void setUp() {
		this.test = new ExporterConfigurator();
	}

	@Test
	public void hasPersonSelector() {
		assertFalse(this.test.hasPersonSelector());

		this.test.selectPerson(it -> false);
		assertTrue(this.test.hasPersonSelector());

		this.test.selectPerson(null);
		assertFalse(this.test.hasPersonSelector());
	}

	@Test
	public void hasOrganizationSelector() {
		assertFalse(this.test.hasOrganizationSelector());

		this.test.selectOrganization(it -> false);
		assertTrue(this.test.hasOrganizationSelector());

		this.test.selectOrganization(null);
		assertFalse(this.test.hasOrganizationSelector());
	}

	@Test
	public void getExportedAuthorStatusFor_null() {
		assertSame(ExportedAuthorStatus.OTHER, this.test.getExportedAuthorStatusFor(null, 2022));
	}

	@Test
	public void getExportedAuthorStatusFor_researcher_noPersonSelector_noOrganizationSelector() {
		ResearchOrganization orga0 = mock(ResearchOrganization.class);
		ResearchOrganization orga1 = mock(ResearchOrganization.class);

		Person person = mock(Person.class);

		Membership m0 = mock(Membership.class);
		when(m0.getMemberStatus()).thenReturn(MemberStatus.ASSOCIATE_PROFESSOR);
		when(m0.getPerson()).thenReturn(person);
		when(m0.getResearchOrganization()).thenReturn(orga0);
		when(m0.isActiveIn(any(), any())).thenReturn(true);

		Membership m1 = mock(Membership.class);
		when(m1.getMemberStatus()).thenReturn(MemberStatus.POSTDOC);
		when(m1.getPerson()).thenReturn(person);
		when(m1.getResearchOrganization()).thenReturn(orga1);
		when(m1.isActiveIn(any(), any())).thenReturn(true);

		when(person.getResearchOrganizations()).thenReturn(new HashSet<>(Arrays.asList(m0, m1)));

		assertSame(ExportedAuthorStatus.RESEARCHER, this.test.getExportedAuthorStatusFor(person, 2022));
	}

	@Test
	public void getExportedAuthorStatusFor_postdoc_noPersonSelector_noOrganizationSelector() {
		ResearchOrganization orga0 = mock(ResearchOrganization.class);
		ResearchOrganization orga1 = mock(ResearchOrganization.class);

		Person person = mock(Person.class);

		Membership m0 = mock(Membership.class);
		when(m0.getMemberStatus()).thenReturn(MemberStatus.ASSOCIATE_PROFESSOR);
		when(m0.getPerson()).thenReturn(person);
		when(m0.getResearchOrganization()).thenReturn(orga0);
		when(m0.isActiveIn(any(), any())).thenReturn(false);

		Membership m1 = mock(Membership.class);
		when(m1.getMemberStatus()).thenReturn(MemberStatus.POSTDOC);
		when(m1.getPerson()).thenReturn(person);
		when(m1.getResearchOrganization()).thenReturn(orga1);
		when(m1.isActiveIn(any(), any())).thenReturn(true);

		when(person.getResearchOrganizations()).thenReturn(new HashSet<>(Arrays.asList(m0, m1)));

		assertSame(ExportedAuthorStatus.POSTDOC_ENGINEER, this.test.getExportedAuthorStatusFor(person, 2022));
	}

	@Test
	public void disablePostdocEngineerFormat() {
		assertTrue(this.test.isPostdocEngineerNameFormatted());
		this.test.disablePostdocEngineerFormat();
		assertFalse(this.test.isPostdocEngineerNameFormatted());
	}

	@Test
	public void disablePhDStudentFormat() {
		assertTrue(this.test.isPhDStudentNameFormatted());
		this.test.disablePhDStudentFormat();
		assertFalse(this.test.isPhDStudentNameFormatted());
	}

	@Test
	public void disableResearcherFormat() {
		assertTrue(this.test.isResearcherNameFormatted());
		this.test.disableResearcherFormat();
		assertFalse(this.test.isResearcherNameFormatted());
	}

	@Test
	public void disableSelectedPersonFormat() {
		assertTrue(this.test.isSelectedPersonNameFormatted());
		this.test.disableSelectedPersonFormat();
		assertFalse(this.test.isSelectedPersonNameFormatted());
	}

	@Test
	public void disableTitleColor() {
		assertTrue(this.test.isColoredTitle());
		this.test.disableTitleColor();
		assertFalse(this.test.isColoredTitle());
	}

}