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

package fr.ciadlab.labmanager.entities.publication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;

import com.fasterxml.jackson.core.JsonGenerator;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationType;
import org.arakhne.afc.util.CountryCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link Authorship}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class AuthorshipTest {

	private Authorship test;

	@BeforeEach
	public void setUp() {
		this.test = new Authorship();
	}

	@Test
	public void getId() {
		assertEquals(0, this.test.getId());
	}

	@Test
	public void setId() {
		this.test.setId(-1245);
		assertEquals(-1245, this.test.getId());

		this.test.setId(0);
		assertEquals(0, this.test.getId());

		this.test.setId(5689);
		assertEquals(5689, this.test.getId());
	}

	@Test
	public void getPublication() {
		assertNull(this.test.getPublication());
	}

	@Test
	public void setPublication() {
		final Publication pub = mock(Publication.class);
		this.test.setPublication(pub);
		assertSame(pub, this.test.getPublication());

		this.test.setPublication(null);
		assertNull(this.test.getPublication());
	}

	@Test
	public void getPerson() {
		assertNull(this.test.getPerson());
	}

	@Test
	public void setPerson() {
		final Person pers = mock(Person.class);
		this.test.setPerson(pers);
		assertSame(pers, this.test.getPerson());

		this.test.setPerson(null);
		assertNull(this.test.getPerson());
	}

	@Test
	public void getAuthorRank() {
		assertEquals(0, this.test.getAuthorRank());
	}

	@Test
	public void setAuthorRank() {
		this.test.setAuthorRank(-125);
		assertEquals(-125, this.test.getAuthorRank());

		this.test.setAuthorRank(-1);
		assertEquals(-1, this.test.getAuthorRank());

		this.test.setAuthorRank(0);
		assertEquals(0, this.test.getAuthorRank());

		this.test.setAuthorRank(1);
		assertEquals(1, this.test.getAuthorRank());

		this.test.setAuthorRank(2);
		assertEquals(2, this.test.getAuthorRank());

		this.test.setAuthorRank(125);
		assertEquals(125, this.test.getAuthorRank());
	}

	@Test
	public void serialize() throws Exception {
		Person person = mock(Person.class);
		when(person.getId()).thenReturn(4789);
		Publication pub = mock(Publication.class);
		when(pub.getId()).thenReturn(3789);
		this.test.setAuthorRank(456);
		this.test.setId(753);
		this.test.setPerson(person);
		this.test.setPublication(pub);
		JsonGenerator generator = mock(JsonGenerator.class);

		this.test.serialize(generator, null);

		verify(generator).writeStartObject();
		verify(generator).writeNumberField(eq("authorRank"), eq(456));
		verify(generator).writeNumberField(eq("id"), eq(753));
		verify(generator).writeNumberField(eq("person"), eq(4789));
		verify(generator).writeNumberField(eq("publication"), eq(3789));
		verify(generator).writeEndObject();
	}

}
