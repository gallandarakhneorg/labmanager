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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationType;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.entities.publication.Publication;
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

	private ResearchOrganization mockOrg(String acronym) {
		ResearchOrganization o = mock(ResearchOrganization.class);
		when(o.getType()).thenReturn(ResearchOrganizationType.LABORATORY);
		when(o.getAcronym()).thenReturn(acronym);
		return o;
	}

	@Test
	public void getRecentMemberships() {
		ResearchOrganization o0 = mockOrg("o0");
		ResearchOrganization o1 = mockOrg("o1");

		Membership m0 = mock(Membership.class);
		when(m0.getResearchOrganization()).thenReturn(o0);
		when(m0.getPerson()).thenReturn(this.test);
		when(m0.getMemberStatus()).thenReturn(MemberStatus.PHD_STUDENT);
		when(m0.getMemberSinceWhen()).thenReturn(Date.valueOf("2021-07-22"));
		when(m0.getMemberToWhen()).thenReturn(Date.valueOf("2022-07-22"));

		Membership m1 = mock(Membership.class);
		when(m1.getResearchOrganization()).thenReturn(o1);
		when(m1.getPerson()).thenReturn(this.test);
		when(m1.getMemberStatus()).thenReturn(MemberStatus.ASSOCIATE_PROFESSOR);

		Membership m2 = mock(Membership.class);
		when(m2.getResearchOrganization()).thenReturn(o0);
		when(m2.getPerson()).thenReturn(this.test);
		when(m2.getMemberStatus()).thenReturn(MemberStatus.ASSOCIATE_PROFESSOR);
		when(m2.getMemberSinceWhen()).thenReturn(Date.valueOf("2022-07-23"));

		Membership m3 = mock(Membership.class);
		when(m3.getResearchOrganization()).thenReturn(o0);
		when(m3.getPerson()).thenReturn(this.test);
		when(m3.getMemberStatus()).thenReturn(MemberStatus.MASTER_STUDENT);
		when(m3.getMemberSinceWhen()).thenReturn(Date.valueOf("2021-01-01"));
		when(m0.getMemberToWhen()).thenReturn(Date.valueOf("2021-07-21"));

		Set<Membership> mbrs = new HashSet<>(Arrays.asList(m0, m1, m2));
		this.test.setResearchOrganizations(mbrs);

		Map<ResearchOrganization, Membership> map = this.test.getRecentMemberships();

		assertNotNull(map);
		assertEquals(2, map.size());
		assertSame(m2, map.get(o0));
		assertSame(m1, map.get(o1));
	}

	@Test
	public void getRecentMemberships_filter_0() {
		ResearchOrganization o0 = mockOrg("o0");
		ResearchOrganization o1 = mockOrg("o1");

		Membership m0 = mock(Membership.class);
		when(m0.getResearchOrganization()).thenReturn(o0);
		when(m0.getPerson()).thenReturn(this.test);
		when(m0.getMemberStatus()).thenReturn(MemberStatus.PHD_STUDENT);
		when(m0.getMemberSinceWhen()).thenReturn(Date.valueOf("2021-07-22"));
		when(m0.getMemberToWhen()).thenReturn(Date.valueOf("2022-07-22"));
		when(m0.isActiveAt(any())).thenReturn(false);

		Membership m1 = mock(Membership.class);
		when(m1.getResearchOrganization()).thenReturn(o1);
		when(m1.getPerson()).thenReturn(this.test);
		when(m1.getMemberStatus()).thenReturn(MemberStatus.ASSOCIATE_PROFESSOR);
		when(m1.isActiveAt(any())).thenReturn(true);

		Membership m2 = mock(Membership.class);
		when(m2.getResearchOrganization()).thenReturn(o0);
		when(m2.getPerson()).thenReturn(this.test);
		when(m2.getMemberStatus()).thenReturn(MemberStatus.ASSOCIATE_PROFESSOR);
		when(m2.getMemberSinceWhen()).thenReturn(Date.valueOf("2022-07-23"));
		when(m2.isActiveAt(any())).thenReturn(true);

		Membership m3 = mock(Membership.class);
		when(m3.getResearchOrganization()).thenReturn(o0);
		when(m3.getPerson()).thenReturn(this.test);
		when(m3.getMemberStatus()).thenReturn(MemberStatus.MASTER_STUDENT);
		when(m3.getMemberSinceWhen()).thenReturn(Date.valueOf("2021-01-01"));
		when(m3.getMemberToWhen()).thenReturn(Date.valueOf("2021-07-21"));
		when(m3.isActiveAt(any())).thenReturn(false);

		Set<Membership> mbrs = new HashSet<>(Arrays.asList(m0, m1, m2));
		this.test.setResearchOrganizations(mbrs);

		final LocalDate now = LocalDate.of(2022, 8, 1);
		Map<ResearchOrganization, Membership> map = this.test.getRecentMemberships(it -> it.isActiveAt(now));

		assertNotNull(map);
		assertEquals(2, map.size());
		assertSame(m2, map.get(o0));
		assertSame(m1, map.get(o1));
	}

	@Test
	public void getRecentMemberships_filter_1() {
		ResearchOrganization o0 = mockOrg("o0");
		ResearchOrganization o1 = mockOrg("o1");

		Membership m0 = mock(Membership.class);
		when(m0.getResearchOrganization()).thenReturn(o0);
		when(m0.getPerson()).thenReturn(this.test);
		when(m0.getMemberStatus()).thenReturn(MemberStatus.PHD_STUDENT);
		when(m0.getMemberSinceWhen()).thenReturn(Date.valueOf("2021-07-22"));
		when(m0.getMemberToWhen()).thenReturn(Date.valueOf("2022-07-22"));
		when(m0.isActiveAt(any())).thenReturn(true);

		Membership m1 = mock(Membership.class);
		when(m1.getResearchOrganization()).thenReturn(o1);
		when(m1.getPerson()).thenReturn(this.test);
		when(m1.getMemberStatus()).thenReturn(MemberStatus.ASSOCIATE_PROFESSOR);
		when(m1.isActiveAt(any())).thenReturn(true);

		Membership m2 = mock(Membership.class);
		when(m2.getResearchOrganization()).thenReturn(o0);
		when(m2.getPerson()).thenReturn(this.test);
		when(m2.getMemberStatus()).thenReturn(MemberStatus.ASSOCIATE_PROFESSOR);
		when(m2.getMemberSinceWhen()).thenReturn(Date.valueOf("2022-07-23"));
		when(m2.isActiveAt(any())).thenReturn(true);

		Membership m3 = mock(Membership.class);
		when(m3.getResearchOrganization()).thenReturn(o0);
		when(m3.getPerson()).thenReturn(this.test);
		when(m3.getMemberStatus()).thenReturn(MemberStatus.MASTER_STUDENT);
		when(m3.getMemberSinceWhen()).thenReturn(Date.valueOf("2021-01-01"));
		when(m3.getMemberToWhen()).thenReturn(Date.valueOf("2021-07-21"));
		when(m3.isActiveAt(any())).thenReturn(true);

		Set<Membership> mbrs = new HashSet<>(Arrays.asList(m0, m1, m2));
		this.test.setResearchOrganizations(mbrs);

		final LocalDate now = LocalDate.of(2022, 8, 1);
		Map<ResearchOrganization, Membership> map = this.test.getRecentMemberships(it -> it.isActiveAt(now));

		assertNotNull(map);
		assertEquals(2, map.size());
		assertSame(m2, map.get(o0));
		assertSame(m1, map.get(o1));
	}

	@Test
	public void getGoogleScholarHindex() {
		assertEquals(0, this.test.getGoogleScholarHindex());
	}

	@Test
	public void setGoogleScholarHindex_int() throws Exception {
		assertEquals(0, this.test.getGoogleScholarHindex());

		this.test.setGoogleScholarHindex(456);
		assertEquals(456, this.test.getGoogleScholarHindex());

		this.test.setGoogleScholarHindex(0);
		assertEquals(0, this.test.getGoogleScholarHindex());

		this.test.setGoogleScholarHindex(852);
		assertEquals(852, this.test.getGoogleScholarHindex());

		this.test.setGoogleScholarHindex(-580);
		assertEquals(0, this.test.getGoogleScholarHindex());
	}

	@Test
	public void setGoogleScholarHindex_Number() throws Exception {
		assertEquals(0, this.test.getGoogleScholarHindex());

		this.test.setGoogleScholarHindex(Integer.valueOf(456));
		assertEquals(456, this.test.getGoogleScholarHindex());

		this.test.setGoogleScholarHindex(null);
		assertEquals(0, this.test.getGoogleScholarHindex());

		this.test.setGoogleScholarHindex(Integer.valueOf(852));
		assertEquals(852, this.test.getGoogleScholarHindex());
	}

	@Test
	public void getWosHindex() {
		assertEquals(0, this.test.getWosHindex());
	}

	@Test
	public void setWosHindex_int() throws Exception {
		assertEquals(0, this.test.getWosHindex());

		this.test.setWosHindex(456);
		assertEquals(456, this.test.getWosHindex());

		this.test.setWosHindex(0);
		assertEquals(0, this.test.getWosHindex());

		this.test.setWosHindex(852);
		assertEquals(852, this.test.getWosHindex());

		this.test.setWosHindex(-580);
		assertEquals(0, this.test.getWosHindex());
	}

	@Test
	public void setWosHindex_Number() throws Exception {
		assertEquals(0, this.test.getWosHindex());

		this.test.setWosHindex(Integer.valueOf(456));
		assertEquals(456, this.test.getWosHindex());

		this.test.setWosHindex(null);
		assertEquals(0, this.test.getWosHindex());

		this.test.setWosHindex(Integer.valueOf(852));
		assertEquals(852, this.test.getWosHindex());
	}

	@Test
	public void getGravatarId() {
		assertNull(this.test.getGravatarId());
	}

	@Test
	public void setGravatarId() throws Exception {
		assertNull(this.test.getGravatarId());

		this.test.setGravatarId("xyz");
		assertEquals("xyz", this.test.getGravatarId());

		this.test.setGravatarId(null);
		assertNull(this.test.getGravatarId());

		this.test.setGravatarId("abc");
		assertEquals("abc", this.test.getGravatarId());

		this.test.setGravatarId(null);
		assertNull(this.test.getGravatarId());
	}

	@Test
	public void getGravatarURL() throws Exception {
		assertNull(this.test.getGravatarURL());

		this.test.setGravatarId("xyz");
		assertEquals(new URL("https://www.gravatar.com/avatar/xyz"), this.test.getGravatarURL());

		this.test.setGravatarId(null);
		assertNull(this.test.getGravatarURL());

		this.test.setGravatarId("abc");
		assertEquals(new URL("https://www.gravatar.com/avatar/abc"), this.test.getGravatarURL());

		this.test.setGravatarId(null);
		assertNull(this.test.getGravatarURL());
	}

	@Test
	public void getGravatarURL_size() throws Exception {
		assertNull(this.test.getGravatarURL(256));

		this.test.setGravatarId("xyz");
		assertEquals(new URL("https://www.gravatar.com/avatar/xyz?s=256"), this.test.getGravatarURL(256));

		this.test.setGravatarId(null);
		assertNull(this.test.getGravatarURL(256));

		this.test.setGravatarId("abc");
		assertEquals(new URL("https://www.gravatar.com/avatar/abc?s=256"), this.test.getGravatarURL(256));

		this.test.setGravatarId(null);
		assertNull(this.test.getGravatarURL(256));
	}

	@Test
	public void getOfficePhone() {
		assertNull(this.test.getOfficePhone());
	}

	@Test
	public void setOfficePhone() throws Exception {
		assertNull(this.test.getOfficePhone());

		this.test.setOfficePhone("xyz");
		assertEquals("xyz", this.test.getOfficePhone());

		this.test.setOfficePhone(null);
		assertNull(this.test.getOfficePhone());

		this.test.setOfficePhone("852");
		assertEquals("852", this.test.getOfficePhone());

		this.test.setOfficePhone("");
		assertNull(this.test.getOfficePhone());
	}

	@Test
	public void getMobilePhone() {
		assertNull(this.test.getMobilePhone());
	}

	@Test
	public void setMobilePhone() throws Exception {
		assertNull(this.test.getMobilePhone());

		this.test.setMobilePhone("xyz");
		assertEquals("xyz", this.test.getMobilePhone());

		this.test.setMobilePhone(null);
		assertNull(this.test.getMobilePhone());

		this.test.setMobilePhone("852");
		assertEquals("852", this.test.getMobilePhone());

		this.test.setMobilePhone("");
		assertNull(this.test.getMobilePhone());
	}

	@Test
	public void serialize() throws Exception {
		Publication pub0 = mock(Publication.class);
		when(pub0.getId()).thenReturn(457786);
		Authorship aut0 = mock(Authorship.class);
		when(aut0.getAuthorRank()).thenReturn(457);
		when(aut0.getPublication()).thenReturn(pub0);

		Publication pub1 = mock(Publication.class);
		when(pub1.getId()).thenReturn(457787);
		Authorship aut1 = mock(Authorship.class);
		when(aut1.getAuthorRank()).thenReturn(456);
		when(aut1.getPublication()).thenReturn(pub1);

		ResearchOrganization org0 = mock(ResearchOrganization.class);
		when(org0.getId()).thenReturn(877787);
		Membership mbr0 = mock(Membership.class);
		when(mbr0.getCnuSection()).thenReturn(27);
		when(mbr0.getMemberSinceWhen()).thenReturn(Date.valueOf("2022-07-22"));
		when(mbr0.getMemberToWhen()).thenReturn(Date.valueOf("2022-09-18"));
		when(mbr0.getMemberStatus()).thenReturn(MemberStatus.ENGINEER);
		when(mbr0.getResearchOrganization()).thenReturn(org0);

		this.test.setAcademiaURL("academia0");
		this.test.setCordisURL("cordis0");
		this.test.setDblpURL("dblp0");
		this.test.setEmail("email0");
		this.test.setFacebookId("fb0");
		this.test.setFirstName("first0");
		this.test.setGender(Gender.FEMALE);
		this.test.setGithubId("github0");
		this.test.setGoogleScholarHindex(456);
		this.test.setGravatarId("gravatar0");
		this.test.setId(951);
		this.test.setLastName("last0");
		this.test.setLinkedInId("ln0");
		this.test.setMobilePhone("mob0");
		this.test.setOfficePhone("tel0");
		this.test.setORCID("orcid0");
		this.test.setResearcherId("rid0");
		this.test.setResearchGateId("rg0");
		this.test.setWosHindex(789);
		this.test.setPublications(new HashSet<>(Arrays.asList(aut0, aut1)));
		this.test.setResearchOrganizations(new HashSet<>(Arrays.asList(mbr0)));
		JsonGenerator generator = mock(JsonGenerator.class);

		this.test.serialize(generator, null);

		verify(generator, times(4)).writeStartObject();
		verify(generator).writeStringField(eq("academiaURL"), eq("academia0"));
		verify(generator).writeStringField(eq("cordisURL"), eq("cordis0"));
		verify(generator).writeStringField(eq("dblpURL"), eq("dblp0"));
		verify(generator).writeStringField(eq("email"), eq("email0"));
		verify(generator).writeStringField(eq("facebookId"), eq("fb0"));
		verify(generator).writeStringField(eq("firstName"), eq("first0"));
		verify(generator).writeStringField(eq("gender"), eq("FEMALE"));
		verify(generator).writeStringField(eq("githubId"), eq("github0"));
		verify(generator).writeNumberField(eq("googleScholarHindex"), eq(456));
		verify(generator).writeStringField(eq("gravatarId"), eq("gravatar0"));
		verify(generator).writeNumberField(eq("id"), eq(951));
		verify(generator).writeStringField(eq("lastName"), eq("last0"));
		verify(generator).writeStringField(eq("linkedInId"), eq("ln0"));
		verify(generator).writeStringField(eq("mobilePhone"), eq("mob0"));
		verify(generator).writeStringField(eq("officePhone"), eq("tel0"));
		verify(generator).writeStringField(eq("orcid"), eq("orcid0"));
		verify(generator).writeStringField(eq("researcherId"), eq("rid0"));
		verify(generator).writeStringField(eq("researchGateId"), eq("rg0"));
		verify(generator).writeNumberField(eq("wosHindex"), eq(789));

		verify(generator).writeArrayFieldStart("publications");
		verify(generator).writeNumberField(eq("authorRank"), eq(456));
		verify(generator).writeNumberField(eq("authorRank"), eq(457));
		verify(generator).writeNumberField(eq("publication"), eq(457786));
		verify(generator).writeNumberField(eq("publication"), eq(457787));

		verify(generator).writeArrayFieldStart("researchOrganizations");
		verify(generator).writeNumberField(eq("cnuSection"), eq(27));
		verify(generator).writeStringField(eq("memberSinceWhen"), eq("2022-07-22"));
		verify(generator).writeStringField(eq("memberToWhen"), eq("2022-09-18"));
		verify(generator).writeStringField(eq("memberStatus"), eq("ENGINEER"));
		verify(generator).writeNumberField(eq("researchOrganization"), eq(877787));

		verify(generator, times(2)).writeEndArray();
		verify(generator, times(4)).writeEndObject();
		verifyNoMoreInteractions(generator);
	}

}
