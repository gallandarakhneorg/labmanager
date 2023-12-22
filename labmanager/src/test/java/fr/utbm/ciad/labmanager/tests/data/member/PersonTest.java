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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.utbm.ciad.labmanager.data.member.Gender;
import fr.utbm.ciad.labmanager.data.member.MemberStatus;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.member.WebPageNaming;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganizationType;
import fr.utbm.ciad.labmanager.data.publication.Authorship;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import fr.utbm.ciad.labmanager.utils.phone.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
	public void getAuthorships() {
		assertTrue(this.test.getAuthorships().isEmpty());
	}

	@Test
	public void setAuthorships() {
		final Set<Authorship> authors = new HashSet<>();
		authors.add(mock(Authorship.class));
		authors.add(mock(Authorship.class));
		authors.add(mock(Authorship.class));
		//
		this.test.setAuthorships(authors);
		assertSame(authors, this.test.getAuthorships());
		//
		this.test.setAuthorships(null);
		assertTrue(this.test.getAuthorships().isEmpty());
	}

	@Test
	public void getMemberships() {
		assertTrue(this.test.getMemberships().isEmpty());
	}

	@Test
	public void setMemberships() {
		final Set<Membership> orgas = new HashSet<>();
		orgas.add(mock(Membership.class));
		orgas.add(mock(Membership.class));
		orgas.add(mock(Membership.class));
		//
		this.test.setMemberships(orgas);
		assertSame(orgas, this.test.getMemberships());
		//
		this.test.setMemberships(null);
		assertTrue(this.test.getMemberships().isEmpty());
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
		this.test.setAuthorships(authors);
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
		this.test.setAuthorships(authors);
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

		this.test.setGender(Gender.MALE.name());
		assertSame(Gender.MALE, this.test.getGender());

		this.test.setGender((String) null);
		assertSame(Gender.NOT_SPECIFIED, this.test.getGender());

		this.test.setGender(Gender.FEMALE.name());
		assertSame(Gender.FEMALE, this.test.getGender());
	}

	@Test
	public void getWebPageNaming() {
		assertSame(WebPageNaming.UNSPECIFIED, this.test.getWebPageNaming());
	}

	@Test
	public void setWebPageNaming_naming() {
		assertSame(WebPageNaming.UNSPECIFIED, this.test.getWebPageNaming());

		this.test.setWebPageNaming(WebPageNaming.AUTHOR_ID);
		assertSame(WebPageNaming.AUTHOR_ID, this.test.getWebPageNaming());

		this.test.setWebPageNaming((WebPageNaming) null);
		assertSame(WebPageNaming.UNSPECIFIED, this.test.getWebPageNaming());

		this.test.setWebPageNaming(WebPageNaming.EMAIL_ID);
		assertSame(WebPageNaming.EMAIL_ID, this.test.getWebPageNaming());
	}

	@Test
	public void setWebPageNaming_string() {
		assertSame(WebPageNaming.UNSPECIFIED, this.test.getWebPageNaming());

		this.test.setWebPageNaming(WebPageNaming.FIRST_LAST.name());
		assertSame(WebPageNaming.FIRST_LAST, this.test.getWebPageNaming());

		this.test.setWebPageNaming((String) null);
		assertSame(WebPageNaming.UNSPECIFIED, this.test.getWebPageNaming());

		this.test.setWebPageNaming(WebPageNaming.EMAIL_ID.name());
		assertSame(WebPageNaming.EMAIL_ID, this.test.getWebPageNaming());
	}

	@Test
	public void getWebPageId() {
		assertNull(this.test.getWebPageId());
	}

	@Test
	public void setWebPageId() {
		assertNull(this.test.getWebPageId());

		this.test.setWebPageId("xyz");
		assertEquals("xyz", this.test.getWebPageId());

		this.test.setWebPageId(null);
		assertNull(this.test.getWebPageId());

		this.test.setWebPageId("abc");
		assertEquals("abc", this.test.getWebPageId());

		this.test.setWebPageId("");
		assertNull(this.test.getWebPageId());
	}

	@Test
	public void getWebPageURI_onlyFirst() throws Exception {
		this.test.setWebPageNaming(WebPageNaming.FIRST_LAST);
		this.test.setFirstName("I am Stéphane");
		this.test.setLastName(null);
		assertEquals(new URI("/iamstephane"), this.test.getWebPageURI());
	}

	@Test
	public void getWebPageURI_onlyLast() throws Exception {
		this.test.setWebPageNaming(WebPageNaming.FIRST_LAST);
		this.test.setFirstName(null);
		this.test.setLastName("I am Stéphane");
		assertEquals(new URI("/iamstephane"), this.test.getWebPageURI());
	}

	@Test
	public void getWebPageURI() throws Exception {
		this.test.setWebPageNaming(WebPageNaming.FIRST_LAST);
		this.test.setFirstName("I am Stéphane");
		this.test.setLastName("My last name");
		assertEquals(new URI("/iamstephane_mylastname"), this.test.getWebPageURI());
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
	public void getGithubAvatarURL_size() {
		assertNull(this.test.getGithubAvatarURL(150));
	}

	@Test
	public void getGithubAvatarURL() {
		assertNull(this.test.getGithubAvatarURL());
	}

	@Test
	public void setGithubId() throws Exception {
		assertNull(this.test.getGithubId());

		this.test.setGithubId("xyz");
		assertEquals("xyz", this.test.getGithubId());
		assertEquals(new URL("https://www.github.com/xyz"), this.test.getGithubURL());
		assertEquals(new URL("https://avatars.githubusercontent.com/xyz"), this.test.getGithubAvatarURL());

		this.test.setGithubId(null);
		assertNull(this.test.getGithubId());
		assertNull(this.test.getGithubURL());
		assertNull(this.test.getGithubAvatarURL());

		this.test.setGithubId("xyz");
		assertEquals("xyz", this.test.getGithubId());
		assertEquals(new URL("https://www.github.com/xyz"), this.test.getGithubURL());
		assertEquals(new URL("https://avatars.githubusercontent.com/xyz"), this.test.getGithubAvatarURL());

		this.test.setGithubId("");
		assertNull(this.test.getGithubId());
		assertNull(this.test.getGithubURL());
		assertNull(this.test.getGithubAvatarURL());
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
		assertEquals(new URL("https://www.webofscience.com/wos/author/rid/xyz"), this.test.getResearcherIdURL());

		this.test.setResearcherId(null);
		assertNull(this.test.getResearcherId());
		assertNull(this.test.getResearcherIdURL());

		this.test.setResearcherId("xyz");
		assertEquals("xyz", this.test.getResearcherId());
		assertEquals(new URL("https://www.webofscience.com/wos/author/rid/xyz"), this.test.getResearcherIdURL());

		this.test.setResearcherId("");
		assertNull(this.test.getResearcherId());
		assertNull(this.test.getResearcherIdURL());
	}

	@Test
	public void getScopusId() {
		assertNull(this.test.getScopusId());
	}

	@Test
	public void setScopusId() throws Exception {
		assertNull(this.test.getScopusId());

		this.test.setScopusId("xyz");
		assertEquals("xyz", this.test.getScopusId());
		assertEquals(new URL("https://www.scopus.com/authid/detail.uri?authorId=xyz"), this.test.getScopusURL());

		this.test.setScopusId(null);
		assertNull(this.test.getScopusId());
		assertNull(this.test.getScopusURL());

		this.test.setScopusId("xyz");
		assertEquals("xyz", this.test.getScopusId());
		assertEquals(new URL("https://www.scopus.com/authid/detail.uri?authorId=xyz"), this.test.getScopusURL());

		this.test.setScopusId("");
		assertNull(this.test.getScopusId());
		assertNull(this.test.getScopusURL());
	}

	@Test
	public void getGoogleScholarId() {
		assertNull(this.test.getGoogleScholarId());
	}

	@Test
	public void getGoogleScholarAvatarURL() {
		assertNull(this.test.getGoogleScholarAvatarURL());
	}

	@Test
	public void setGoogleScholarId() throws Exception {
		assertNull(this.test.getGoogleScholarId());

		this.test.setGoogleScholarId("xyz");
		assertEquals("xyz", this.test.getGoogleScholarId());
		assertEquals(new URL("https://scholar.google.fr/citations?user=xyz"), this.test.getGoogleScholarURL());
		assertEquals(new URL("https://scholar.googleusercontent.com/citations?view_op=view_photo&user=xyz"), this.test.getGoogleScholarAvatarURL());

		this.test.setGoogleScholarId(null);
		assertNull(this.test.getGoogleScholarId());
		assertNull(this.test.getGoogleScholarURL());
		assertNull(this.test.getGoogleScholarAvatarURL());

		this.test.setGoogleScholarId("xyz");
		assertEquals("xyz", this.test.getGoogleScholarId());
		assertEquals(new URL("https://scholar.google.fr/citations?user=xyz"), this.test.getGoogleScholarURL());
		assertEquals(new URL("https://scholar.googleusercontent.com/citations?view_op=view_photo&user=xyz"), this.test.getGoogleScholarAvatarURL());

		this.test.setGoogleScholarId("");
		assertNull(this.test.getGoogleScholarId());
		assertNull(this.test.getGoogleScholarURL());
		assertNull(this.test.getGoogleScholarAvatarURL());
	}

	@Test
	public void getIdhal() {
		assertNull(this.test.getIdhal());
	}

	@Test
	public void getHalURL() {
		assertNull(this.test.getHalURL());
	}

	@Test
	public void setIdhal() throws Exception {
		assertNull(this.test.getIdhal());

		this.test.setIdhal("xyz");
		assertEquals("xyz", this.test.getIdhal());
		assertEquals(new URL("https://hal.science/search/index/?qa%5Bidentifiers_id%5D%5B%5D=xyz"), this.test.getHalURL());

		this.test.setIdhal(null);
		assertNull(this.test.getIdhal());
		assertNull(this.test.getHalURL());

		this.test.setIdhal("xyz");
		assertEquals("xyz", this.test.getIdhal());
		assertEquals(new URL("https://hal.science/search/index/?qa%5Bidentifiers_id%5D%5B%5D=xyz"), this.test.getHalURL());

		this.test.setIdhal("");
		assertNull(this.test.getIdhal());
		assertNull(this.test.getHalURL());
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

	@Test
	public void getAdScientificIndexId() {
		assertNull(this.test.getAdScientificIndexId());
	}

	@Test
	public void setAdScientificIndexId() throws Exception {
		assertNull(this.test.getAdScientificIndexId());

		this.test.setAdScientificIndexId("xyz");
		assertEquals("xyz", this.test.getAdScientificIndexId());
		assertEquals(new URL("https://www.adscientificindex.com/scientist/xyz"), this.test.getAdScientificIndexURL());

		this.test.setAdScientificIndexId(null);
		assertNull(this.test.getAdScientificIndexId());
		assertNull(this.test.getAdScientificIndexURL());

		this.test.setAdScientificIndexId("xyz");
		assertEquals("xyz", this.test.getAdScientificIndexId());
		assertEquals(new URL("https://www.adscientificindex.com/scientist/xyz"), this.test.getAdScientificIndexURL());

		this.test.setAdScientificIndexId("");
		assertNull(this.test.getAdScientificIndexId());
		assertNull(this.test.getAdScientificIndexURL());
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
		when(m0.getMemberSinceWhen()).thenReturn(LocalDate.parse("2021-07-22"));
		when(m0.getMemberToWhen()).thenReturn(LocalDate.parse("2022-07-22"));

		Membership m1 = mock(Membership.class);
		when(m1.getResearchOrganization()).thenReturn(o1);
		when(m1.getPerson()).thenReturn(this.test);
		when(m1.getMemberStatus()).thenReturn(MemberStatus.ASSOCIATE_PROFESSOR);

		Membership m2 = mock(Membership.class);
		when(m2.getResearchOrganization()).thenReturn(o0);
		when(m2.getPerson()).thenReturn(this.test);
		when(m2.getMemberStatus()).thenReturn(MemberStatus.ASSOCIATE_PROFESSOR);
		when(m2.getMemberSinceWhen()).thenReturn(LocalDate.parse("2022-07-23"));

		Membership m3 = mock(Membership.class);
		when(m3.getResearchOrganization()).thenReturn(o0);
		when(m3.getPerson()).thenReturn(this.test);
		when(m3.getMemberStatus()).thenReturn(MemberStatus.MASTER_STUDENT);
		when(m3.getMemberSinceWhen()).thenReturn(LocalDate.parse("2021-01-01"));
		when(m0.getMemberToWhen()).thenReturn(LocalDate.parse("2021-07-21"));

		Set<Membership> mbrs = new HashSet<>(Arrays.asList(m0, m1, m2));
		this.test.setMemberships(mbrs);

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
		when(m0.getMemberSinceWhen()).thenReturn(LocalDate.parse("2021-07-22"));
		when(m0.getMemberToWhen()).thenReturn(LocalDate.parse("2022-07-22"));
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
		when(m2.getMemberSinceWhen()).thenReturn(LocalDate.parse("2022-07-23"));
		when(m2.isActiveAt(any())).thenReturn(true);

		Membership m3 = mock(Membership.class);
		when(m3.getResearchOrganization()).thenReturn(o0);
		when(m3.getPerson()).thenReturn(this.test);
		when(m3.getMemberStatus()).thenReturn(MemberStatus.MASTER_STUDENT);
		when(m3.getMemberSinceWhen()).thenReturn(LocalDate.parse("2021-01-01"));
		when(m3.getMemberToWhen()).thenReturn(LocalDate.parse("2021-07-21"));
		when(m3.isActiveAt(any())).thenReturn(false);

		Set<Membership> mbrs = new HashSet<>(Arrays.asList(m0, m1, m2));
		this.test.setMemberships(mbrs);

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
		when(m0.getMemberSinceWhen()).thenReturn(LocalDate.parse("2021-07-22"));
		when(m0.getMemberToWhen()).thenReturn(LocalDate.parse("2022-07-22"));
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
		when(m2.getMemberSinceWhen()).thenReturn(LocalDate.parse("2022-07-23"));
		when(m2.isActiveAt(any())).thenReturn(true);

		Membership m3 = mock(Membership.class);
		when(m3.getResearchOrganization()).thenReturn(o0);
		when(m3.getPerson()).thenReturn(this.test);
		when(m3.getMemberStatus()).thenReturn(MemberStatus.MASTER_STUDENT);
		when(m3.getMemberSinceWhen()).thenReturn(LocalDate.parse("2021-01-01"));
		when(m3.getMemberToWhen()).thenReturn(LocalDate.parse("2021-07-21"));
		when(m3.isActiveAt(any())).thenReturn(true);

		Set<Membership> mbrs = new HashSet<>(Arrays.asList(m0, m1, m2));
		this.test.setMemberships(mbrs);

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
	public void getScopusHindex() {
		assertEquals(0, this.test.getScopusHindex());
	}

	@Test
	public void setScopusHindex_int() throws Exception {
		assertEquals(0, this.test.getScopusHindex());

		this.test.setScopusHindex(456);
		assertEquals(456, this.test.getScopusHindex());

		this.test.setScopusHindex(0);
		assertEquals(0, this.test.getScopusHindex());

		this.test.setScopusHindex(852);
		assertEquals(852, this.test.getScopusHindex());

		this.test.setScopusHindex(-580);
		assertEquals(0, this.test.getScopusHindex());
	}

	@Test
	public void setScopusHindex_Number() throws Exception {
		assertEquals(0, this.test.getScopusHindex());

		this.test.setScopusHindex(Integer.valueOf(456));
		assertEquals(456, this.test.getScopusHindex());

		this.test.setScopusHindex(null);
		assertEquals(0, this.test.getScopusHindex());

		this.test.setScopusHindex(Integer.valueOf(852));
		assertEquals(852, this.test.getScopusHindex());
	}

	@Test
	public void getGoogleScholarCitations() {
		assertEquals(0, this.test.getGoogleScholarCitations());
	}

	@Test
	public void setGoogleScholarCitations_int() throws Exception {
		assertEquals(0, this.test.getGoogleScholarCitations());

		this.test.setGoogleScholarCitations(456);
		assertEquals(456, this.test.getGoogleScholarCitations());

		this.test.setGoogleScholarCitations(0);
		assertEquals(0, this.test.getGoogleScholarCitations());

		this.test.setGoogleScholarCitations(852);
		assertEquals(852, this.test.getGoogleScholarCitations());

		this.test.setGoogleScholarCitations(-580);
		assertEquals(0, this.test.getGoogleScholarCitations());
	}

	@Test
	public void setGoogleScholarCitations_Number() throws Exception {
		assertEquals(0, this.test.getGoogleScholarCitations());

		this.test.setGoogleScholarCitations(Integer.valueOf(456));
		assertEquals(456, this.test.getGoogleScholarCitations());

		this.test.setGoogleScholarCitations(null);
		assertEquals(0, this.test.getGoogleScholarCitations());

		this.test.setGoogleScholarCitations(Integer.valueOf(852));
		assertEquals(852, this.test.getGoogleScholarCitations());
	}

	@Test
	public void getWosCitations() {
		assertEquals(0, this.test.getWosCitations());
	}

	@Test
	public void setWosCitations_int() throws Exception {
		assertEquals(0, this.test.getWosCitations());

		this.test.setWosCitations(456);
		assertEquals(456, this.test.getWosCitations());

		this.test.setWosCitations(0);
		assertEquals(0, this.test.getWosCitations());

		this.test.setWosCitations(852);
		assertEquals(852, this.test.getWosCitations());

		this.test.setWosCitations(-580);
		assertEquals(0, this.test.getWosCitations());
	}

	@Test
	public void setWosCitations_Number() throws Exception {
		assertEquals(0, this.test.getWosCitations());

		this.test.setWosCitations(Integer.valueOf(456));
		assertEquals(456, this.test.getWosCitations());

		this.test.setWosCitations(null);
		assertEquals(0, this.test.getWosCitations());

		this.test.setWosCitations(Integer.valueOf(852));
		assertEquals(852, this.test.getWosCitations());
	}

	@Test
	public void getScopusCitations() {
		assertEquals(0, this.test.getScopusCitations());
	}

	@Test
	public void setScopusCitations_int() throws Exception {
		assertEquals(0, this.test.getScopusCitations());

		this.test.setScopusCitations(456);
		assertEquals(456, this.test.getScopusCitations());

		this.test.setScopusCitations(0);
		assertEquals(0, this.test.getScopusCitations());

		this.test.setScopusCitations(852);
		assertEquals(852, this.test.getScopusCitations());

		this.test.setScopusCitations(-580);
		assertEquals(0, this.test.getScopusCitations());
	}

	@Test
	public void setScopusCitations_Number() throws Exception {
		assertEquals(0, this.test.getScopusCitations());

		this.test.setScopusCitations(Integer.valueOf(456));
		assertEquals(456, this.test.getScopusCitations());

		this.test.setScopusCitations(null);
		assertEquals(0, this.test.getScopusCitations());

		this.test.setScopusCitations(Integer.valueOf(852));
		assertEquals(852, this.test.getScopusCitations());
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

		this.test.setOfficePhone(new PhoneNumber(CountryCode.FRANCE, "123456789"));
		assertEquals(new PhoneNumber(CountryCode.FRANCE, "123456789"), this.test.getOfficePhone());

		this.test.setOfficePhone(null);
		assertNull(this.test.getOfficePhone());

		this.test.setOfficePhone(new PhoneNumber(CountryCode.AFGHANISTAN, "987654321"));
		assertEquals(new PhoneNumber(CountryCode.AFGHANISTAN, "987654321"), this.test.getOfficePhone());

		this.test.setOfficePhone(null);
		assertNull(this.test.getOfficePhone());
	}

	@Test
	public void getMobilePhone() {
		assertNull(this.test.getMobilePhone());
	}

	@Test
	public void setMobilePhone() throws Exception {
		assertNull(this.test.getMobilePhone());

		this.test.setMobilePhone(new PhoneNumber(CountryCode.FRANCE, "123456789"));
		assertEquals(new PhoneNumber(CountryCode.FRANCE, "123456789"), this.test.getMobilePhone());

		this.test.setMobilePhone(null);
		assertNull(this.test.getMobilePhone());

		this.test.setMobilePhone(new PhoneNumber(CountryCode.AFGHANISTAN, "987654321"));
		assertEquals(new PhoneNumber(CountryCode.AFGHANISTAN, "987654321"), this.test.getMobilePhone());

		this.test.setMobilePhone(null);
		assertNull(this.test.getMobilePhone());
	}

	@Test
	public void getPhotoURL_none() {
		assertNull(this.test.getPhotoURL());
	}

	@Test
	public void getPhotoURL_gravatar() throws Exception {
		this.test.setGravatarId("xyz");
		assertEquals(new URL("https://www.gravatar.com/avatar/xyz"), this.test.getPhotoURL());
	}

	@Test
	public void getPhotoURL_github() throws Exception {
		this.test.setGithubId("xyz");
		assertEquals(new URL("https://avatars.githubusercontent.com/xyz"), this.test.getPhotoURL());
	}

	@Test
	public void getPhotoURL_gravatar_github() throws Exception {
		this.test.setGravatarId("xyz");
		this.test.setGithubId("abc");
		assertEquals(new URL("https://www.gravatar.com/avatar/xyz"), this.test.getPhotoURL());
	}

	@Test
	public void getPhotoURL_size_none() {
		assertNull(this.test.getPhotoURL(150));
	}

	@Test
	public void getPhotoURL_size_gravatar() throws Exception {
		this.test.setGravatarId("xyz");
		assertEquals(new URL("https://www.gravatar.com/avatar/xyz?s=150"), this.test.getPhotoURL(150));
	}

	@Test
	public void getPhotoURL_size_github() throws Exception {
		this.test.setGithubId("xyz");
		assertEquals(new URL("https://avatars.githubusercontent.com/xyz?s=150"), this.test.getPhotoURL(150));
	}

	@Test
	public void getPhotoURL_size_gravatar_github() throws Exception {
		this.test.setGravatarId("xyz");
		this.test.setGithubId("abc");
		assertEquals(new URL("https://www.gravatar.com/avatar/xyz?s=150"), this.test.getPhotoURL(150));
	}

	private static Person createTransient1() {
		var e = new Person();
		e.setLastName("A");
		return e;
	}	

	private static Person createTransient2() {
		var e = new Person();
		e.setLastName("B");
		return e;
	}	

	private static Person createManaged1() {
		var e = createTransient1();
		e.setId(10l);
		return e;
	}	

	private static Person createManaged2() {
		var e = createTransient2();
		e.setId(20l);
		return e;
	}	

	@Test
	@DisplayName("t1.equals(null)")
	public void test_equals_0() {
		assertFalse(createTransient1().equals(null));
	}
	
	@Test
	@DisplayName("m1.equals(null)")
	public void test_equals_1() {
		assertFalse(createManaged1().equals(null));
	}

	@Test
	@DisplayName("t1.equals(t1)")
	public void test_equals_2() {
		var t1 = createTransient1();
		assertTrue(t1.equals(t1));
	}

	@Test
	@DisplayName("m1.equals(m1)")
	public void test_equals_3() {
		var m1 = createManaged1();
		assertTrue(m1.equals(m1));
	}

	@Test
	@DisplayName("t1.equals(t2)")
	public void test_equals_4() {
		// 2 transient entities need to be NOT equal
		var t1 = createTransient1();
		var t2 = createTransient2();
		assertFalse(t1.equals(t2));
	}

	@Test
	@DisplayName("t2.equals(t1)")
	public void test_equals_5() {
		// 2 transient entities need to be NOT equal
		var t1 = createTransient1();
		var t2 = createTransient2();
		assertFalse(t2.equals(t1));
	}

	@Test
	@DisplayName("m1.equals(m2)")
	public void test_equals_6() {
		// 2 managed entities that represent different records need to be NOT equal
		var m1 = createManaged1();
		var m2 = createManaged2();
		assertFalse(m1.equals(m2));
	}

	@Test
	@DisplayName("m2.equals(m1)")
	public void test_equals_7() {
		// 2 managed entities that represent different records need to be NOT equal
		var m1 = createManaged1();
		var m2 = createManaged2();
		assertFalse(m2.equals(m1));
	}

	@Test
	@DisplayName("m1.equals(m1')")
	public void test_equals_8() {
		// 2 managed entities that represent the same record need to be equal
		var m1 = createManaged1();
		var m1p = createManaged1();
		assertTrue(m1.equals(m1p));
	}

	@Test
	@DisplayName("m1'.equals(m1)")
	public void test_equals_9() {
		// 2 managed entities that represent the same record need to be equal
		var m1 = createManaged1();
		var m1p = createManaged1();
		assertTrue(m1p.equals(m1));
	}

	@Test
	@DisplayName("m1.equals(t1)")
	public void test_equals_10() {
		// a detached/transient and a managed entity object that represent the same record need to be equal
		var t1 = createTransient1();
		var m1 = createManaged1();
		assertTrue(m1.equals(t1));
	}

	@Test
	@DisplayName("t1.equals(m1)")
	public void test_equals_11() {
		// a detached/transient and a managed entity object that represent the same record need to be equal
		var t1 = createTransient1();
		var m1 = createManaged1();
		assertTrue(t1.equals(m1));
	}

	@Test
	@DisplayName("m1.equals(t2)")
	public void test_equals_12() {
		var t2 = createTransient2();
		var m1 = createManaged1();
		assertFalse(m1.equals(t2));
	}

	@Test
	@DisplayName("t2.equals(m1)")
	public void test_equals_13() {
		var t2 = createTransient2();
		var m1 = createManaged1();
		assertFalse(t2.equals(m1));
	}

	@Test
	@DisplayName("t1.hashCode == t1.hashCode")
	public void test_hashCode_0() {
		var t1 = createTransient1();
		assertEquals(t1.hashCode(), t1.hashCode());
	}

	@Test
	@DisplayName("t1.hashCode == t1p.hashCode")
	public void test_hashCode_1() {
		var t1 = createTransient1();
		var t1p = createTransient1();
		assertEquals(t1.hashCode(), t1p.hashCode());
	}

	@Test
	@DisplayName("t1.hashCode != m1.hashCode")
	public void test_hashCode_2() {
		var t1 = createTransient1();
		var m1 = createManaged1();
		assertNotEquals(t1.hashCode(), m1.hashCode());
	}

}
