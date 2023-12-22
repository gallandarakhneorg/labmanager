/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.utbm.ciad.labmanager.tests.views.components.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import fr.utbm.ciad.labmanager.views.components.validators.UrlValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for {@link UrlValidator}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@SuppressWarnings("all")
public class UrlValidatorTest {

	@Test
	@DisplayName("apply(null) null allowed")
	public void apply_0() {
		UrlValidator validator = new UrlValidator("ERR", true);
		ValidationResult result = validator.apply(null, new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}
	
	@Test
	@DisplayName("apply(null) null not allowed")
	public void apply_1() {
		UrlValidator validator = new UrlValidator("ERR", false);
		ValidationResult result = validator.apply(null, new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\"\") null allowed")
	public void apply_2() {
		UrlValidator validator = new UrlValidator("ERR", true);
		ValidationResult result = validator.apply("", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"\") null not allowed")
	public void apply_3() {
		UrlValidator validator = new UrlValidator("ERR", false);
		ValidationResult result = validator.apply("", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\"abc\") null allowed")
	public void apply_4() {
		UrlValidator validator = new UrlValidator("ERR", true);
		ValidationResult result = validator.apply("abc", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"abc\") null not allowed")
	public void apply_5() {
		UrlValidator validator = new UrlValidator("ERR", false);
		ValidationResult result = validator.apply("abc", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\"http://www.utbm.fr/abc\") null allowed")
	public void apply_6() {
		UrlValidator validator = new UrlValidator("ERR", true);
		ValidationResult result = validator.apply("http://www.utbm.fr/abc", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"http://www.utbm.fr/abc\") null not allowed")
	public void apply_7() {
		UrlValidator validator = new UrlValidator("ERR", false);
		ValidationResult result = validator.apply("http://www.utbm.fr/abc", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}

	@Test
	@DisplayName("apply(\"http://www.utbm.fr/abc?a=4\") null allowed")
	public void apply_8() {
		UrlValidator validator = new UrlValidator("ERR", true);
		ValidationResult result = validator.apply("http://www.utbm.fr/abc?a=4", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"http://www.utbm.fr/abc?a=4\") null not allowed")
	public void apply_9() {
		UrlValidator validator = new UrlValidator("ERR", false);
		ValidationResult result = validator.apply("http://www.utbm.fr/abc?a=4", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}

	@Test
	@DisplayName("apply(\"htp://www.utbm.fr/abc?a=4\") null allowed")
	public void apply_10() {
		UrlValidator validator = new UrlValidator("ERR", true);
		ValidationResult result = validator.apply("htp://www.utbm.fr/abc?a=4", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"htp://www.utbm.fr/abc?a=4\") null not allowed")
	public void apply_11() {
		UrlValidator validator = new UrlValidator("ERR", false);
		ValidationResult result = validator.apply("htp://www.utbm.fr/abc?a=4", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

}
