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
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.entities.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonObject;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link Person}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class PersonTest {

	private Person test;

	@BeforeEach
	public void setUp() {
		this.test = new Person();
	}

	@Test
	public void getId() {
		assertEquals(0, this.test.getId());
	}

	@Test
	public void setId() {
		assertEquals(0, this.test.getId());

		this.test.setId(-1234);
		assertEquals(-1234, this.test.getId());

		this.test.setId(0);
		assertEquals(0, this.test.getId());

		this.test.setId(4789);
		assertEquals(4789, this.test.getId());
	}

	@Test
	public void getFirstName() {
		assertNull(this.test.getFirstName());
	}

	@Test
	public void setFirstName() {
		assertNull(this.test.getFirstName());

		this.test.setFirstName(null);
		assertNull(this.test.getFirstName());

		this.test.setFirstName("");
		assertNull(this.test.getFirstName());

		this.test.setFirstName("xyz");
		assertEquals("xyz", this.test.getFirstName());
	}

	@Test
	public void getLastName() {
		assertNull(this.test.getLastName());
	}

	@Test
	public void setLastName() {
		assertNull(this.test.getLastName());

		this.test.setLastName(null);
		assertNull(this.test.getLastName());

		this.test.setLastName("");
		assertNull(this.test.getLastName());

		this.test.setLastName("xyz");
		assertEquals("xyz", this.test.getLastName());
	}

	@Test
	public void getFullName() {
		this.test.setFirstName("F1");
		this.test.setLastName("L1");
		assertEquals("F1 L1", this.test.getFullName());
	}

	@Test
	public void getEmail() {
		assertNull(this.test.getEmail());
	}

	@Test
	public void setEmail() {
		assertNull(this.test.getEmail());

		this.test.setEmail(null);
		assertNull(this.test.getEmail());

		this.test.setEmail("");
		assertNull(this.test.getEmail());

		this.test.setEmail("xyz");
		assertEquals("xyz", this.test.getEmail());
	}

	@Test
	public void getPublications() {
		assertTrue(this.test.getPublications().isEmpty());
	}

	@Test
	public void setPublications() {
		final Set<Authorship> authors = new HashSet<>();
		authors.add(mock(Authorship.class));
		authors.add(mock(Authorship.class));
		authors.add(mock(Authorship.class));
		//
		this.test.setPublications(authors);
		assertSame(authors, this.test.getPublications());
		//
		this.test.setPublications(null);
		assertTrue(this.test.getPublications().isEmpty());
	}

	@Test
	public void getResearchOrganizations() {
		assertTrue(this.test.getResearchOrganizations().isEmpty());
	}

	@Test
	public void setResearchOrganizations() {
		final Set<Membership> orgas = new HashSet<>();
		orgas.add(mock(Membership.class));
		orgas.add(mock(Membership.class));
		orgas.add(mock(Membership.class));
		//
		this.test.setResearchOrganizations(orgas);
		assertSame(orgas, this.test.getResearchOrganizations());
		//
		this.test.setResearchOrganizations(null);
		assertTrue(this.test.getResearchOrganizations().isEmpty());
	}

	@Test
	public void deleteAuthorship() {
		final Authorship a0 = mock(Authorship.class);
		final Authorship a1 = mock(Authorship.class);
		final Authorship a2 = mock(Authorship.class);
		final Set<Authorship> authors = new HashSet<>();
		authors.add(a0);
		authors.add(a1);
		authors.add(a2);
		this.test.setPublications(authors);
		//
		this.test.deleteAuthorship(a1);
		//
		assertTrue(authors.contains(a0));
		assertFalse(authors.contains(a1));
		assertTrue(authors.contains(a2));
	}

	@Test
	public void deleteAllAuthorships() {
		final Authorship a0 = mock(Authorship.class);
		final Authorship a1 = mock(Authorship.class);
		final Authorship a2 = mock(Authorship.class);
		final Set<Authorship> authors = new HashSet<>();
		authors.add(a0);
		authors.add(a1);
		authors.add(a2);
		this.test.setPublications(authors);
		//
		this.test.deleteAllAuthorships();
		//
		assertTrue(authors.isEmpty());
	}

	@Test
	public void toJson() {
		this.test.setFirstName("fn0");
		this.test.setLastName("ln0");
		this.test.setEmail("@0");

		JsonObject obj = new JsonObject();

		this.test.toJson(obj);

		assertNotNull(obj.get("firstName"));
		assertNotNull(obj.get("lastName"));
		assertNotNull(obj.get("email"));
	}

}
