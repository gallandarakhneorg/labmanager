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

package fr.ciadlab.labmanager.entities.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonObject;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationType;
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

		this.test.setEmail("xyz");
		assertEquals("xyz", this.test.getEmail());

		this.test.setEmail(null);
		assertNull(this.test.getEmail());

		this.test.setEmail("xyz");
		assertEquals("xyz", this.test.getEmail());

		this.test.setEmail("");
		assertNull(this.test.getEmail());
	}

	@Test
	public void getORCID() {
		assertNull(this.test.getORCID());
	}

	@Test
	public void setORCID() {
		assertNull(this.test.getORCID());

		this.test.setORCID("xyz");
		assertEquals("xyz", this.test.getORCID());

		this.test.setORCID(null);
		assertNull(this.test.getORCID());

		this.test.setORCID("xyz");
		assertEquals("xyz", this.test.getORCID());

		this.test.setORCID("");
		assertNull(this.test.getORCID());
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

	@Test
	public void getGender() {
		assertSame(Gender.NOT_SPECIFIED, this.test.getGender());
	}

	@Test
	public void setGender_gender() {
		assertSame(Gender.NOT_SPECIFIED, this.test.getGender());

		this.test.setGender(Gender.MALE);
		assertSame(Gender.MALE, this.test.getGender());

		this.test.setGender((Gender) null);
		assertSame(Gender.NOT_SPECIFIED, this.test.getGender());

		this.test.setGender(Gender.FEMALE);
		assertSame(Gender.FEMALE, this.test.getGender());
	}

	@Test
	public void setGender_string() {
		assertSame(Gender.NOT_SPECIFIED, this.test.getGender());

		this.test.setGender(Gender.MALE);
		assertSame(Gender.MALE, this.test.getGender());

		this.test.setGender((String) null);
		assertSame(Gender.NOT_SPECIFIED, this.test.getGender());

		this.test.setGender(Gender.FEMALE);
		assertSame(Gender.FEMALE, this.test.getGender());
	}

	@Test
	public void getAcademiaURL() {
		assertNull(this.test.getAcademiaURL());
	}

	@Test
	public void getAcademiaURLObject() {
		assertNull(this.test.getAcademiaURLObject());
	}

	@Test
	public void setAcademiaURL() {
		assertNull(this.test.getAcademiaURL());

		this.test.setAcademiaURL("xyz");
		assertEquals("xyz", this.test.getAcademiaURL());

		this.test.setAcademiaURL(null);
		assertNull(this.test.getAcademiaURL());

		this.test.setAcademiaURL("xyz");
		assertEquals("xyz", this.test.getAcademiaURL());

		this.test.setAcademiaURL("");
		assertNull(this.test.getAcademiaURL());
	}

	@Test
	public void getCordisURL() {
		assertNull(this.test.getCordisURL());
	}

	@Test
	public void getCordisURLObject() {
		assertNull(this.test.getCordisURLObject());
	}

	@Test
	public void setCordisURL() {
		assertNull(this.test.getCordisURL());

		this.test.setCordisURL("xyz");
		assertEquals("xyz", this.test.getCordisURL());

		this.test.setCordisURL(null);
		assertNull(this.test.getCordisURL());

		this.test.setCordisURL("xyz");
		assertEquals("xyz", this.test.getCordisURL());

		this.test.setCordisURL("");
		assertNull(this.test.getCordisURL());
	}

	@Test
	public void getDblpURL() {
		assertNull(this.test.getDblpURL());
	}

	@Test
	public void getDblpURLObject() {
		assertNull(this.test.getDblpURLObject());
	}

	@Test
	public void setDblpURL() {
		assertNull(this.test.getDblpURL());

		this.test.setDblpURL("xyz");
		assertEquals("xyz", this.test.getDblpURL());

		this.test.setDblpURL(null);
		assertNull(this.test.getDblpURL());

		this.test.setDblpURL("xyz");
		assertEquals("xyz", this.test.getDblpURL());

		this.test.setDblpURL("");
		assertNull(this.test.getDblpURL());
	}

	@Test
	public void getFacebookId() {
		assertNull(this.test.getFacebookId());
	}

	@Test
	public void getFacebookURL() {
		assertNull(this.test.getFacebookURL());
	}

	@Test
	public void setFacebookId() throws Exception {
		assertNull(this.test.getFacebookId());

		this.test.setFacebookId("xyz");
		assertEquals("xyz", this.test.getFacebookId());
		assertEquals(new URL("https://www.facebook.com/xyz"), this.test.getFacebookURL());

		this.test.setFacebookId(null);
		assertNull(this.test.getFacebookId());
		assertNull(this.test.getFacebookURL());

		this.test.setFacebookId("xyz");
		assertEquals("xyz", this.test.getFacebookId());
		assertEquals(new URL("https://www.facebook.com/xyz"), this.test.getFacebookURL());

		this.test.setFacebookId("");
		assertNull(this.test.getFacebookId());
		assertNull(this.test.getFacebookURL());
	}

	@Test
	public void getGithubId() {
		assertNull(this.test.getGithubId());
	}

	@Test
	public void getGithubURL() {
		assertNull(this.test.getGithubURL());
	}

	@Test
	public void setGithubId() throws Exception {
		assertNull(this.test.getGithubId());

		this.test.setGithubId("xyz");
		assertEquals("xyz", this.test.getGithubId());
		assertEquals(new URL("https://www.github.com/xyz"), this.test.getGithubURL());

		this.test.setGithubId(null);
		assertNull(this.test.getGithubId());
		assertNull(this.test.getGithubURL());

		this.test.setGithubId("xyz");
		assertEquals("xyz", this.test.getGithubId());
		assertEquals(new URL("https://www.github.com/xyz"), this.test.getGithubURL());

		this.test.setGithubId("");
		assertNull(this.test.getGithubId());
		assertNull(this.test.getGithubURL());
	}

	@Test
	public void getLinkedInURL() {
		assertNull(this.test.getLinkedInURL());
	}

	@Test
	public void setLinkedInId() throws Exception {
		assertNull(this.test.getLinkedInId());

		this.test.setLinkedInId("xyz");
		assertEquals("xyz", this.test.getLinkedInId());
		assertEquals(new URL("http://linkedin.com/in/xyz"), this.test.getLinkedInURL());

		this.test.setLinkedInId(null);
		assertNull(this.test.getLinkedInId());
		assertNull(this.test.getLinkedInURL());

		this.test.setLinkedInId("xyz");
		assertEquals("xyz", this.test.getLinkedInId());
		assertEquals(new URL("http://linkedin.com/in/xyz"), this.test.getLinkedInURL());

		this.test.setLinkedInId("");
		assertNull(this.test.getLinkedInId());
		assertNull(this.test.getLinkedInURL());
	}

	@Test
	public void getResearcherId() {
		assertNull(this.test.getResearcherId());
	}

	@Test
	public void setResearcherId() throws Exception {
		assertNull(this.test.getResearcherId());

		this.test.setResearcherId("xyz");
		assertEquals("xyz", this.test.getResearcherId());
		assertEquals(new URL("http://www.researcherid.com/rid/xyz"), this.test.getResearcherIdURL());

		this.test.setResearcherId(null);
		assertNull(this.test.getResearcherId());
		assertNull(this.test.getResearcherIdURL());

		this.test.setResearcherId("xyz");
		assertEquals("xyz", this.test.getResearcherId());
		assertEquals(new URL("http://www.researcherid.com/rid/xyz"), this.test.getResearcherIdURL());

		this.test.setResearcherId("");
		assertNull(this.test.getResearcherId());
		assertNull(this.test.getResearcherIdURL());
	}

	@Test
	public void getResearchGateId() {
		assertNull(this.test.getResearchGateId());
	}

	@Test
	public void setResearchGateId() throws Exception {
		assertNull(this.test.getResearchGateId());

		this.test.setResearchGateId("xyz");
		assertEquals("xyz", this.test.getResearchGateId());
		assertEquals(new URL("http://www.researchgate.net/profile/xyz"), this.test.getResearchGateURL());

		this.test.setResearchGateId(null);
		assertNull(this.test.getResearchGateId());
		assertNull(this.test.getResearchGateURL());

		this.test.setResearchGateId("xyz");
		assertEquals("xyz", this.test.getResearchGateId());
		assertEquals(new URL("http://www.researchgate.net/profile/xyz"), this.test.getResearchGateURL());

		this.test.setResearchGateId("");
		assertNull(this.test.getResearchGateId());
		assertNull(this.test.getResearchGateURL());
	}

}
