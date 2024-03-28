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

package fr.utbm.ciad.labmanager.tests.utils.io;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

import fr.utbm.ciad.labmanager.data.member.MemberStatus;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.services.journal.JournalService;
import fr.utbm.ciad.labmanager.utils.io.ExportedAuthorStatus;
import fr.utbm.ciad.labmanager.utils.io.ExporterConfigurator;
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
		this.test = new ExporterConfigurator(mock(JournalService.class), Locale.US);
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
		when(m0.getDirectResearchOrganization()).thenReturn(orga0);
		when(m0.isActiveIn(any(), any())).thenReturn(true);

		Membership m1 = mock(Membership.class);
		when(m1.getMemberStatus()).thenReturn(MemberStatus.POSTDOC);
		when(m1.getPerson()).thenReturn(person);
		when(m1.getDirectResearchOrganization()).thenReturn(orga1);
		when(m1.isActiveIn(any(), any())).thenReturn(true);

		when(person.getMemberships()).thenReturn(new HashSet<>(Arrays.asList(m0, m1)));

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
		when(m0.getDirectResearchOrganization()).thenReturn(orga0);
		when(m0.isActiveIn(any(), any())).thenReturn(false);

		Membership m1 = mock(Membership.class);
		when(m1.getMemberStatus()).thenReturn(MemberStatus.POSTDOC);
		when(m1.getPerson()).thenReturn(person);
		when(m1.getDirectResearchOrganization()).thenReturn(orga1);
		when(m1.isActiveIn(any(), any())).thenReturn(true);

		when(person.getMemberships()).thenReturn(new HashSet<>(Arrays.asList(m0, m1)));

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

	@Test
	public void disableFormattedAuthorList() {
		assertTrue(this.test.isFormattedAuthorList());
		this.test.disableFormattedAuthorList();
		assertFalse(this.test.isFormattedAuthorList());
	}

	@Test
	public void disableFormattedPublicationDetails() {
		assertTrue(this.test.isFormattedPublicationDetails());
		this.test.disableFormattedPublicationDetails();
		assertFalse(this.test.isFormattedPublicationDetails());
	}

	@Test
	public void disableFormattedLinks() {
		assertTrue(this.test.isFormattedLinks());
		this.test.disableFormattedLinks();
		assertFalse(this.test.isFormattedLinks());
	}

	@Test
	public void disableTypeAndCategoryLabels() {
		assertTrue(this.test.isTypeAndCategoryLabels());
		this.test.disableTypeAndCategoryLabels();
		assertFalse(this.test.isTypeAndCategoryLabels());
	}

}