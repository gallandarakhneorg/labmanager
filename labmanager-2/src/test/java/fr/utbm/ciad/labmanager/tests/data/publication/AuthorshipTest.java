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

package fr.utbm.ciad.labmanager.tests.data.publication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.publication.Authorship;
import fr.utbm.ciad.labmanager.data.publication.Publication;
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

}
