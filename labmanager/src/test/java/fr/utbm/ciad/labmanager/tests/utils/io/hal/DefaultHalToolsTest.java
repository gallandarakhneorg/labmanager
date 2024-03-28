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

package fr.utbm.ciad.labmanager.tests.utils.io.hal;

import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.MalformedURLException;
import java.net.URL;

import fr.utbm.ciad.labmanager.utils.io.hal.DefaultHalTools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests for {@link DefaultHalTools}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class DefaultHalToolsTest {

	private DefaultHalTools test;

	@BeforeEach
	public void setUp() {
		this.test = new DefaultHalTools();
	}

	@Test
	public void getHALNumberFromHALUrlOrNull_String_null() throws Exception {
		assertNull(this.test.getHALNumberFromHALUrlOrNull((String) null));
	}

	@Test
	public void getHALNumberFromHALUrlOrNull_String() throws Exception {
		final String r = this.test.getHALNumberFromHALUrlOrNull("https://hal.science/hal-123");
		assertNotNull(r);
		assertEquals("hal-123", r);
	}

	@Test
	public void getHALNumberFromHALUrl_String_null() throws Exception {
		final IllegalArgumentException ex = catchThrowableOfType(
				() -> this.test.getHALNumberFromHALUrl((String) null),
				IllegalArgumentException.class);
		assertNotNull(ex);
	}

	@Test
	public void getHALNumberFromHALUrl_String_empty() throws Exception {
		final IllegalArgumentException ex = catchThrowableOfType(
				() -> this.test.getHALNumberFromHALUrl(""),
				IllegalArgumentException.class);
		assertNotNull(ex);
	}

	@Test
	public void getHALNumberFromHALUrl_String_malformedUrl() throws Exception {
		final IllegalArgumentException ex = catchThrowableOfType(
				() -> this.test.getHALNumberFromHALUrl("xyz/abc"),
				IllegalArgumentException.class);
		assertNotNull(ex);
	}

	@Test
	public void getHALNumberFromHALUrl_String() throws Exception {
		final String r = this.test.getHALNumberFromHALUrl("https://hal.science/hal-123");
		assertNotNull(r);
		assertEquals("hal-123", r);
	}

	@Test
	public void getHALNumberFromHALUrl_URL_null() throws Exception {
		final IllegalArgumentException ex = catchThrowableOfType(
				() -> this.test.getHALNumberFromHALUrl((URL) null),
				IllegalArgumentException.class);
		assertNotNull(ex);
	}

	@Test
	public void getHALNumberFromHALUrl_URL() throws Exception {
		final String r = this.test.getHALNumberFromHALUrl(new URL("https://hal.science/hal-123"));
		assertNotNull(r);
		assertEquals("hal-123", r);
	}

	@Test
	public void getHALNumberFromHALUrlOrNull_URL_null() throws Exception {
		assertNull(this.test.getHALNumberFromHALUrlOrNull((URL) null));
	}

	@Test
	public void getHALNumberFromHALUrlOrNull_URL() throws Exception {
		final String r = this.test.getHALNumberFromHALUrlOrNull(new URL("https://hal.science/hal-123"));
		assertNotNull(r);
		assertEquals("hal-123", r);
	}

	@Test
	public void getHALUrlFromHALNumber_null() {
		assertNull(this.test.getHALUrlFromHALNumber(null));
	}

	@Test
	public void getHALUrlFromHALNumber_empty() {
		assertNull(this.test.getHALUrlFromHALNumber(""));
	}

	@Test
	public void getHALUrlFromHALNumber_url0() {
		final URL url = this.test.getHALUrlFromHALNumber("a b c");
		assertNotNull(url);
		assertEquals("https://hal.science/a%20b%20c", url.toExternalForm());
	}

	@Test
	public void getHALUrlFromHALNumber_url1() {
		final URL url = this.test.getHALUrlFromHALNumber("xyz/abc");
		assertNotNull(url);
		assertEquals("https://hal.science/xyz/abc", url.toExternalForm());
	}

}