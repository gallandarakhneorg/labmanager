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

package fr.ciadlab.labmanager.entities.publication;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link PublicationLanguage}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class PublicationLanguageTest {

	private static final Random RANDOM = new Random();

	private List<PublicationLanguage> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(PublicationLanguage.values()));
	}

	private PublicationLanguage cons(PublicationLanguage lang) {
		assertTrue(this.items.remove(lang), "Expecting enumeration item: " + lang.toString());
		return lang;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	private static String randomString(String text) {
		final StringBuilder buf = new StringBuilder();
		for (int i = 0; i < text.length(); ++i) {
			final String s = Character.toString(text.charAt(i));
			if (RANDOM.nextBoolean()) {
				buf.append(s.toLowerCase());
			} else {
				buf.append(s.toUpperCase());
			}
		}
		return buf.toString();
	}

	@Test
	public void getDefault() {
		assertSame(PublicationLanguage.ENGLISH, PublicationLanguage.getDefault());
	}

	@Test
	public void valueOfCaseInsensitive_null() {
		assertSame(PublicationLanguage.ENGLISH, PublicationLanguage.valueOfCaseInsensitive(null));
	}

	@Test
	public void valueOfCaseInsensitive_empty() {
		assertSame(PublicationLanguage.ENGLISH, PublicationLanguage.valueOfCaseInsensitive(""));
	}

	@Test
	public void valueOfCaseInsensitive_invalid() {
		assertSame(PublicationLanguage.ENGLISH, PublicationLanguage.valueOfCaseInsensitive("xvz"));
	}

	@Test
	public void valueOfCaseInsensitive_notNull() {
		for (PublicationLanguage lang : PublicationLanguage.values()) {
			assertSame(
					cons(lang),
					PublicationLanguage.valueOfCaseInsensitive(randomString(lang.name())),
					"valueOfCaseInsensitive: "+ lang.name());
		}
		assertAllTreated();
	}

}
