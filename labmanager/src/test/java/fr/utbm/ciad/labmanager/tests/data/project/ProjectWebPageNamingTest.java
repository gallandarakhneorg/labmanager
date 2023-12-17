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

package fr.utbm.ciad.labmanager.tests.data.project;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.utbm.ciad.labmanager.configuration.messages.BaseMessageSource;
import fr.utbm.ciad.labmanager.data.member.WebPageNaming;
import fr.utbm.ciad.labmanager.data.project.Project;
import fr.utbm.ciad.labmanager.data.project.ProjectWebPageNaming;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link ProjectWebPageNaming}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.0
 */
@SuppressWarnings("all")
public class ProjectWebPageNamingTest {

	private List<ProjectWebPageNaming> items;
	
	private MessageSourceAccessor messages;

	private Project project;

	@BeforeEach
	public void setUp() {
		this.messages = BaseMessageSource.getGlobalMessageAccessor();
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(ProjectWebPageNaming.values()));

		this.project = mock(Project.class);
		when(this.project.getId()).thenReturn(1456);
		when(this.project.getAcronym()).thenReturn("myacronym");
	}

	private ProjectWebPageNaming cons(ProjectWebPageNaming status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void getWebpageURIFor() throws Exception {
		assertNull(cons(ProjectWebPageNaming.UNSPECIFIED).getWebpageURIFor(this.project));
		assertEquals(new URI("/project-1456"), cons(ProjectWebPageNaming.PROJECT_ID).getWebpageURIFor(this.project));
		assertEquals(new URI("/myacronym"), cons(ProjectWebPageNaming.ACRONYM).getWebpageURIFor(this.project));
		assertEquals(new URI("/project/myacronym"), cons(ProjectWebPageNaming.PROJECT_ACRONYM).getWebpageURIFor(this.project));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() {
		assertEquals("project/<ACRONYM>", cons(ProjectWebPageNaming.PROJECT_ACRONYM).getLabel(this.messages, Locale.US));
		assertEquals("Acronym of the project", cons(ProjectWebPageNaming.ACRONYM).getLabel(this.messages, Locale.US));
		assertEquals("project-<ID>", cons(ProjectWebPageNaming.PROJECT_ID).getLabel(this.messages, Locale.US));
		assertEquals("Not specified", cons(ProjectWebPageNaming.UNSPECIFIED).getLabel(this.messages, Locale.US));
		assertAllTreated();
	}

}
