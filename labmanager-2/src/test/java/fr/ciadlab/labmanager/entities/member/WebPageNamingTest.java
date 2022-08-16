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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link WebPageNaming}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class WebPageNamingTest {

	private List<WebPageNaming> items;

	private Person person;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(WebPageNaming.values()));

		this.person = mock(Person.class);
		when(this.person.getId()).thenReturn(1456);
		when(this.person.getEmail()).thenReturn("myfirst.mylast@institution.org");
		when(this.person.getFirstName()).thenReturn("I am Stéphane");
		when(this.person.getLastName()).thenReturn("My Last Name");
	}

	private WebPageNaming cons(WebPageNaming status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void getWebpageURIFor() throws Exception {
		assertNull(cons(WebPageNaming.UNSPECIFIED).getWebpageURIFor(this.person));
		assertEquals(new URI("/author-1456"), cons(WebPageNaming.AUTHOR_ID).getWebpageURIFor(this.person));
		assertEquals(new URI("/myfirst.mylast"), cons(WebPageNaming.EMAIL_ID).getWebpageURIFor(this.person));
		assertEquals(new URI("/iamstephane_mylastname"), cons(WebPageNaming.FIRST_LAST).getWebpageURIFor(this.person));
		assertAllTreated();
	}

}
