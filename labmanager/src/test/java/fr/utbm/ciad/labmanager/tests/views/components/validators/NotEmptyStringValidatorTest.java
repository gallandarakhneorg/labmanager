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

import static org.junit.jupiter.api.Assertions.*;

import com.vaadin.flow.data.binder.ErrorLevel;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.RegexpValidator;
import fr.utbm.ciad.labmanager.views.components.validators.NotEmptyStringValidator;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for {@link NotEmptyStringValidator}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@SuppressWarnings("all")
public class NotEmptyStringValidatorTest {

	@Test
	@DisplayName("apply(null)")
	public void apply_0() {
		NotEmptyStringValidator validator = new NotEmptyStringValidator("ERR");
		ValidationResult result = validator.apply(null, new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}

	@Test
	@DisplayName("apply(\"\")")
	public void apply_1() {
		NotEmptyStringValidator validator = new NotEmptyStringValidator("ERR");
		ValidationResult result = validator.apply("", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(\"abc\")")
	public void apply_2() {
		NotEmptyStringValidator validator = new NotEmptyStringValidator("ERR");
		ValidationResult result = validator.apply("abc", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}

	@Test
	@DisplayName("apply(\"   abc\")")
	public void apply_3() {
		NotEmptyStringValidator validator = new NotEmptyStringValidator("ERR");
		ValidationResult result = validator.apply("   abc", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}

	@Test
	@DisplayName("apply(\"   abc   \")")
	public void apply_4() {
		NotEmptyStringValidator validator = new NotEmptyStringValidator("ERR");
		ValidationResult result = validator.apply("   abc   ", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}

	@Test
	@DisplayName("apply(\"abc   \")")
	public void apply_5() {
		NotEmptyStringValidator validator = new NotEmptyStringValidator("ERR");
		ValidationResult result = validator.apply("abc   ", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}

	@Test
	@DisplayName("apply(\"   ab c  \")")
	public void apply_6() {
		NotEmptyStringValidator validator = new NotEmptyStringValidator("ERR");
		ValidationResult result = validator.apply("   ab c  ", new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}

	@Test
	@DisplayName("apply(\"   \")")
	public void apply_7() {
		NotEmptyStringValidator validator = new NotEmptyStringValidator("ERR");
		ValidationResult result = validator.apply("   ", new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

}
