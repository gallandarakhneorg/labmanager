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

package fr.utbm.ciad.labmanager.tests.views.components.addons.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import fr.utbm.ciad.labmanager.views.components.addons.validators.OrcidValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for {@link OrcidValidator}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@SuppressWarnings("all")
public class OrcidValidatorTest {

	@Test
	@DisplayName("apply(null) null allowed")
	public void apply_0() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply(null, new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}
	
	@Test
	@DisplayName("apply(null) null not allowed")
	public void apply_1() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply(null, new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\"\") null allowed")
	public void apply_2() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply("", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"\") null not allowed")
	public void apply_3() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply("", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\"abc\") null allowed")
	public void apply_4() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply("abc", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"abc\") null not allowed")
	public void apply_5() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply("abc", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\"12.5\") null allowed")
	public void apply_6() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply("12.5", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"12.5\") null not allowed")
	public void apply_7() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply("12.5", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\"12.5,48.6\") null allowed")
	public void apply_8() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply("12.5,48.6", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"12.5,48.6\") null not allowed")
	public void apply_9() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply("12.5,48.6", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\"12.5,\") null allowed")
	public void apply_10() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply("12.5,", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"12.5,\") null not allowed")
	public void apply_11() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply("12.5,", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\",12.5\") null allowed")
	public void apply_12() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply(",12.5", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}
	
	@Test
	@DisplayName("apply(\",12.5\") null not allowed")
	public void apply_13() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply(",12.5", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\"1234-5678\") null allowed")
	public void apply_14() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply("1234-5678", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"01234-5678\") null not allowed")
	public void apply_15() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply("1234-5678", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\"1234-5678-9101\") null allowed")
	public void apply_16() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply("1234-5678-9101", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"1234-5678-9101\") null not allowed")
	public void apply_17() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply("1234-5678-9101", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\"1234-5678-9101-1121\") null allowed")
	public void apply_18() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply("1234-5678-9101-1121", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"1234-5678-9101-1121\") null not allowed")
	public void apply_19() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply("1234-5678-9101-1121", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}

	@Test
	@DisplayName("apply(\"1234-5678-9101-1121-3141\") null allowed")
	public void apply_20() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply("1234-5678-9101-1121-3141", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"1234-5678-9101-1121-3141\") null not allowed")
	public void apply_21() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply("1234-5678-9101-1121-3141", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\"1234-568-9101-1121\") null allowed")
	public void apply_22() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply("1234-568-9101-1121", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"1234-568-9101-1121\") null not allowed")
	public void apply_23() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply("1234-568-9101-1121", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\"     1234-5678-9101-1121     \") null allowed")
	public void apply_24() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply("     1234-5678-9101-1121     ", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"1234-5678-9101-1121\") null not allowed")
	public void apply_25() {
		OrcidValidator validator = new OrcidValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply("     1234-5678-9101-1121     ", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}

}
