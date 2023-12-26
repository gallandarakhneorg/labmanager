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
import fr.utbm.ciad.labmanager.views.components.addons.validators.GpsCoordinatesValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for {@link GpsCoordinatesValidator}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@SuppressWarnings("all")
public class GpsCoordinatesValidatorTest {

	@Test
	@DisplayName("apply(null) null allowed")
	public void apply_0() {
		GpsCoordinatesValidator validator = new GpsCoordinatesValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply(null, new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}
	
	@Test
	@DisplayName("apply(null) null not allowed")
	public void apply_1() {
		GpsCoordinatesValidator validator = new GpsCoordinatesValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply(null, new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\"\") null allowed")
	public void apply_2() {
		GpsCoordinatesValidator validator = new GpsCoordinatesValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply("", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"\") null not allowed")
	public void apply_3() {
		GpsCoordinatesValidator validator = new GpsCoordinatesValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply("", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\"abc\") null allowed")
	public void apply_4() {
		GpsCoordinatesValidator validator = new GpsCoordinatesValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply("abc", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"abc\") null not allowed")
	public void apply_5() {
		GpsCoordinatesValidator validator = new GpsCoordinatesValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply("abc", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\"12.5\") null allowed")
	public void apply_6() {
		GpsCoordinatesValidator validator = new GpsCoordinatesValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply("12.5", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"12.5\") null not allowed")
	public void apply_7() {
		GpsCoordinatesValidator validator = new GpsCoordinatesValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply("12.5", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\"12.5,48.6\") null allowed")
	public void apply_8() {
		GpsCoordinatesValidator validator = new GpsCoordinatesValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply("12.5,48.6", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"12.5,48.6\") null not allowed")
	public void apply_9() {
		GpsCoordinatesValidator validator = new GpsCoordinatesValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply("12.5,48.6", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}

	@Test
	@DisplayName("apply(\"12.5,\") null allowed")
	public void apply_10() {
		GpsCoordinatesValidator validator = new GpsCoordinatesValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply("12.5,", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"12.5,\") null not allowed")
	public void apply_11() {
		GpsCoordinatesValidator validator = new GpsCoordinatesValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply("12.5,", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\",12.5\") null allowed")
	public void apply_12() {
		GpsCoordinatesValidator validator = new GpsCoordinatesValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply(",12.5", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}
	
	@Test
	@DisplayName("apply(\",12.5\") null not allowed")
	public void apply_13() {
		GpsCoordinatesValidator validator = new GpsCoordinatesValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply(",12.5", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\"   12.5 , 48.5   \") null allowed")
	public void apply_14() {
		GpsCoordinatesValidator validator = new GpsCoordinatesValidator("ERR", "WARN", true);
		ValidationResult result = validator.apply("   12.5 , 48.5   ", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}
	
	@Test
	@DisplayName("apply(\"   12.5 , 48.5   \") null not allowed")
	public void apply_15() {
		GpsCoordinatesValidator validator = new GpsCoordinatesValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply("   12.5 , 48.5   ", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}

	@Test
	@DisplayName("apply(\"47.64182213118393, 6.845054244315581\") null not allowed")
	public void apply_16() {
		GpsCoordinatesValidator validator = new GpsCoordinatesValidator("ERR", "WARN", false);
		ValidationResult result = validator.apply("47.64182213118393, 6.845054244315581", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}
	
}
