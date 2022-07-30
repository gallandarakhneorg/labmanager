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

package fr.ciadlab.labmanager.entities.publication.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link BookChapter}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class BookChapterTest {

	private BookChapter test;

	@BeforeEach
	public void setUp() {
		this.test = new BookChapter();
	}

	@Test
	public void isRanked() {
		assertFalse(this.test.isRanked());
	}

	@Test
	public void getVolume() {
		assertNull(this.test.getVolume());
	}

	@Test
	public void setVolume() {
		this.test.setVolume("xyz");
		assertEquals("xyz", this.test.getVolume());

		this.test.setVolume("");
		assertNull(this.test.getVolume());

		this.test.setVolume(null);
		assertNull(this.test.getVolume());
	}

	@Test
	public void getNumber() {
		assertNull(this.test.getNumber());
	}

	@Test
	public void setNumber() {
		this.test.setNumber("xyz");
		assertEquals("xyz", this.test.getNumber());

		this.test.setNumber("");
		assertNull(this.test.getNumber());

		this.test.setNumber(null);
		assertNull(this.test.getNumber());
	}

	@Test
	public void getPages() {
		assertNull(this.test.getPages());
	}

	@Test
	public void setPages() {
		this.test.setPages("xyz");
		assertEquals("xyz", this.test.getPages());

		this.test.setPages("");
		assertNull(this.test.getPages());

		this.test.setPages(null);
		assertNull(this.test.getPages());
	}

	@Test
	public void getEditors() {
		assertNull(this.test.getEditors());
	}

	@Test
	public void setEditors() {
		this.test.setEditors("xyz");
		assertEquals("xyz", this.test.getEditors());

		this.test.setEditors("");
		assertNull(this.test.getEditors());

		this.test.setEditors(null);
		assertNull(this.test.getEditors());
	}

	@Test
	public void getAddress() {
		assertNull(this.test.getAddress());
	}

	@Test
	public void setAddress() {
		this.test.setAddress("xyz");
		assertEquals("xyz", this.test.getAddress());

		this.test.setAddress("");
		assertNull(this.test.getAddress());

		this.test.setAddress(null);
		assertNull(this.test.getAddress());
	}

	@Test
	public void getSeries() {
		assertNull(this.test.getSeries());
	}

	@Test
	public void setSeries() {
		this.test.setSeries("xyz");
		assertEquals("xyz", this.test.getSeries());

		this.test.setSeries("");
		assertNull(this.test.getSeries());

		this.test.setSeries(null);
		assertNull(this.test.getSeries());
	}

	@Test
	public void getPublisher() {
		assertNull(this.test.getPublisher());
	}

	@Test
	public void setPublisher() {
		this.test.setPublisher("xyz");
		assertEquals("xyz", this.test.getPublisher());

		this.test.setPublisher("");
		assertNull(this.test.getPublisher());

		this.test.setPublisher(null);
		assertNull(this.test.getPublisher());
	}

	@Test
	public void getEdition() {
		assertNull(this.test.getEdition());
	}

	@Test
	public void setEdition() {
		this.test.setEdition("xyz");
		assertEquals("xyz", this.test.getEdition());

		this.test.setEdition("");
		assertNull(this.test.getEdition());

		this.test.setEdition(null);
		assertNull(this.test.getEdition());
	}

	@Test
	public void getBookTitle() {
		assertNull(this.test.getBookTitle());
	}

	@Test
	public void setBookTitle() {
		this.test.setBookTitle("xyz");
		assertEquals("xyz", this.test.getBookTitle());

		this.test.setBookTitle("");
		assertNull(this.test.getBookTitle());

		this.test.setBookTitle(null);
		assertNull(this.test.getBookTitle());
	}

	@Test
	public void getChapterNumber() {
		assertNull(this.test.getChapterNumber());
	}

	@Test
	public void setChapterNumber() {
		this.test.setChapterNumber("xyz");
		assertEquals("xyz", this.test.getChapterNumber());

		this.test.setChapterNumber("");
		assertNull(this.test.getChapterNumber());

		this.test.setChapterNumber(null);
		assertNull(this.test.getChapterNumber());
	}

}
