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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.utbm.ciad.labmanager.configuration.messages.BaseMessageSource;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.member.WebPageNaming;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

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
	
	private MessageSourceAccessor messages;

	private Person person;

	@BeforeEach
	public void setUp() {
		this.messages = BaseMessageSource.getGlobalMessageAccessor();
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(WebPageNaming.values()));

		this.person = mock(Person.class);
		when(this.person.getId()).thenReturn(1456l);
		when(this.person.getPrimaryEmail()).thenReturn("myfirst.mylast@institution.org");
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

	@Test
	public void getLabel_Locale_US() {
		assertEquals("author-<ID>", cons(WebPageNaming.AUTHOR_ID).getLabel(this.messages, Locale.US));
		assertEquals("Email without domain part", cons(WebPageNaming.EMAIL_ID).getLabel(this.messages, Locale.US));
		assertEquals("First and last names", cons(WebPageNaming.FIRST_LAST).getLabel(this.messages, Locale.US));
		assertEquals("Not specified", cons(WebPageNaming.UNSPECIFIED).getLabel(this.messages, Locale.US));
		assertAllTreated();
	}

}
