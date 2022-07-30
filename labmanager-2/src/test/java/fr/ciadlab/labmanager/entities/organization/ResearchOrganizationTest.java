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

package fr.ciadlab.labmanager.entities.organization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.Set;

import fr.ciadlab.labmanager.entities.member.Membership;
import org.arakhne.afc.util.CountryCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link ResearchOrganization}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ResearchOrganizationTest {

	private ResearchOrganization test;

	@BeforeEach
	public void setUp() {
		this.test = new ResearchOrganization();
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
	public void getMembers() {
		assertTrue(this.test.getMembers().isEmpty());
	}

	@Test
	public void setMembers() {
		Set<Membership> members = new HashSet<>();
		members.add(mock(Membership.class));
		members.add(mock(Membership.class));
		members.add(mock(Membership.class));
		//
		this.test.setMembers(members);
		assertSame(members, this.test.getMembers());
		//
		this.test.setMembers(null);
		assertTrue(this.test.getMembers().isEmpty());
	}

	@Test
	public void getSubOrganizations() {
		assertTrue(this.test.getSubOrganizations().isEmpty());
	}

	@Test
	public void setSubOrganizations() {
		Set<ResearchOrganization> orgas = new HashSet<>();
		orgas.add(mock(ResearchOrganization.class));
		orgas.add(mock(ResearchOrganization.class));
		orgas.add(mock(ResearchOrganization.class));
		//
		this.test.setSubOrganizations(orgas);
		assertSame(orgas, this.test.getSubOrganizations());
		//
		this.test.setSubOrganizations(null);
		assertTrue(this.test.getSubOrganizations().isEmpty());
	}

	@Test
	public void getSuperOrganization() {
		assertNull(this.test.getSuperOrganization());
	}

	@Test
	public void setSuperOrganization() {
		ResearchOrganization org = mock(ResearchOrganization.class);
		//
		this.test.setSuperOrganization(org);
		assertSame(org, this.test.getSuperOrganization());
		//
		this.test.setSuperOrganization(null);
		assertNull(this.test.getSuperOrganization());
	}

	@Test
	public void getAcronym() {
		assertNull(this.test.getAcronym());
	}

	@Test
	public void setAcronym() {
		this.test.setAcronym("xyz");
		assertEquals("xyz", this.test.getAcronym());

		this.test.setAcronym("");
		assertNull(this.test.getAcronym());

		this.test.setAcronym(null);
		assertNull(this.test.getAcronym());
	}

	@Test
	public void getName() {
		assertNull(this.test.getName());
	}

	@Test
	public void setName() {
		this.test.setName("xyz");
		assertEquals("xyz", this.test.getName());

		this.test.setName("");
		assertNull(this.test.getName());

		this.test.setName(null);
		assertNull(this.test.getName());
	}

	@Test
	public void getDescription() {
		assertNull(this.test.getDescription());
	}

	@Test
	public void setDescription() {
		this.test.setDescription("xyz");
		assertEquals("xyz", this.test.getDescription());

		this.test.setDescription("");
		assertNull(this.test.getDescription());

		this.test.setDescription(null);
		assertNull(this.test.getDescription());
	}

	@Test
	public void getCountry() {
		assertNull(this.test.getCountry());
	}

	@Test
	public void setCountry() {
		this.test.setCountry(CountryCode.ALGERIA);
		assertSame(CountryCode.ALGERIA, this.test.getCountry());

		this.test.setCountry(null);
		assertNull(this.test.getCountry());
	}

}
