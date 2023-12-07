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

package fr.utbm.ciad.labmanager.tests.utils.doi;

import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.MalformedURLException;
import java.net.URL;

import fr.utbm.ciad.labmanager.utils.doi.DefaultDoiTools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests for {@link DefaultDoiTools}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class DefaultDoiToolsTest {

	private DefaultDoiTools test;

	@BeforeEach
	public void setUp() {
		this.test = new DefaultDoiTools();
	}

	@Test
	public void getDOINumberFromDOIUrl_String_null() throws Exception {
		final IllegalArgumentException ex = catchThrowableOfType(
			() -> this.test.getDOINumberFromDOIUrl((String) null),
			IllegalArgumentException.class);
		assertNotNull(ex);
	}

	@Test
	public void getDOINumberFromDOIUrl_String_empty() throws Exception {
		final IllegalArgumentException ex = catchThrowableOfType(
			() -> this.test.getDOINumberFromDOIUrl(""),
			IllegalArgumentException.class);
		assertNotNull(ex);
	}

	@Test
	public void getDOINumberFromDOIUrl_String_malformedDoi_01() throws Exception {
		final IllegalArgumentException ex = catchThrowableOfType(
				() -> this.test.getDOINumberFromDOIUrl("http://doi.org/xyz"),
				IllegalArgumentException.class);
		assertNotNull(ex);
	}

	@Test
	public void getDOINumberFromDOIUrl_String_malformedDoi_02() throws Exception {
		final IllegalArgumentException ex = catchThrowableOfType(
				() -> this.test.getDOINumberFromDOIUrl("doi:xyz"),
				IllegalArgumentException.class);
		assertNotNull(ex);
	}

	@Test
	public void getDOINumberFromDOIUrl_String_malformedDoi_03() throws Exception {
		final IllegalArgumentException ex = catchThrowableOfType(
				() -> this.test.getDOINumberFromDOIUrl("doi:xyz/"),
				IllegalArgumentException.class);
		assertNotNull(ex);
	}

	@Test
	public void getDOINumberFromDOIUrl_String_malformedDoi_04() throws Exception {
		final IllegalArgumentException ex = catchThrowableOfType(
				() -> this.test.getDOINumberFromDOIUrl("doi:/xyz"),
				IllegalArgumentException.class);
		assertNotNull(ex);
	}

	@Test
	public void getDOINumberFromDOIUrl_String_malformedDoi_05() throws Exception {
		final IllegalArgumentException ex = catchThrowableOfType(
				() -> this.test.getDOINumberFromDOIUrl("http://doi.org/xyz/"),
				IllegalArgumentException.class);
		assertNotNull(ex);
	}

	@Test
	public void getDOINumberFromDOIUrl_String_malformedDoi_06() throws Exception {
		final IllegalArgumentException ex = catchThrowableOfType(
				() -> this.test.getDOINumberFromDOIUrl("http://doi.org//xyz"),
				IllegalArgumentException.class);
		assertNotNull(ex);
	}

	@Test
	public void getDOINumberFromDOIUrl_String_malformedDoi_07() throws Exception {
		final IllegalArgumentException ex = catchThrowableOfType(
				() -> this.test.getDOINumberFromDOIUrl("http://doi.org//xyz/abc/def"),
				IllegalArgumentException.class);
		assertNotNull(ex);
	}

	@Test
	public void getDOINumberFromDOIUrl_String_01() throws Exception {
		final String r = this.test.getDOINumberFromDOIUrl("http://doi.org/xyz/abc");
		assertNotNull(r);
		assertEquals("xyz/abc", r);
	}

	@Test
	public void getDOINumberFromDOIUrl_String_02() throws Exception {
		final String r = this.test.getDOINumberFromDOIUrl("doi:xyz/abc");
		assertNotNull(r);
		assertEquals("xyz/abc", r);
	}

	@Test
	public void getDOINumberFromDOIUrl_URL_null() throws Exception {
		final IllegalArgumentException ex = catchThrowableOfType(
				() -> this.test.getDOINumberFromDOIUrl((URL) null),
				IllegalArgumentException.class);
		assertNotNull(ex);
	}

	@Test
	public void getDOINumberFromDOIUrlOrNull_String_null() throws Exception {
		assertNull(this.test.getDOINumberFromDOIUrlOrNull((String) null));
	}

	@Test
	public void getDOINumberFromDOIUrlOrNull_String() throws Exception {
		final String r = this.test.getDOINumberFromDOIUrlOrNull("doi:xyz/abc");
		assertNotNull(r);
		assertEquals("xyz/abc", r);
	}

	@Test
	public void getDOINumberFromDOIUrl_URL() throws Exception {
		final String r = this.test.getDOINumberFromDOIUrl(new URL("https://doi.org/xyz/abc"));
		assertNotNull(r);
		assertEquals("xyz/abc", r);
	}

	@Test
	public void getDOINumberFromDOIUrlOrNull_URL_null() throws Exception {
		assertNull(this.test.getDOINumberFromDOIUrlOrNull((URL) null));
	}

	@Test
	public void getDOINumberFromDOIUrlOrNull_URL() throws Exception {
		final String r = this.test.getDOINumberFromDOIUrlOrNull(new URL("https://doi.org/xyz/abc"));
		assertNotNull(r);
		assertEquals("xyz/abc", r);
	}

	@Test
	public void getDOIUrlFromDOINumber_null() {
		final URL url = this.test.getDOIUrlFromDOINumber(null);
		assertNull(url);
	}

	@Test
	public void getDOIUrlFromDOINumber_empty() {
		final URL url = this.test.getDOIUrlFromDOINumber("");
		assertNull(url);
	}

	@Test
	public void getDOIUrlFromDOINumber_url0() {
		final URL url = this.test.getDOIUrlFromDOINumber("a b c");
		assertNotNull(url);
		assertEquals("https://doi.org/a%20b%20c", url.toExternalForm());
	}

	@Test
	public void getDOIUrlFromDOINumber_url1() {
		final URL url = this.test.getDOIUrlFromDOINumber("xyz/abc");
		assertNotNull(url);
		assertEquals("https://doi.org/xyz/abc", url.toExternalForm());
	}

}