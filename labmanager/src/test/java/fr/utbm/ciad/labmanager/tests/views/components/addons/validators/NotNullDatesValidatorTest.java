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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotNullDateValidator;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotNullDatesValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for {@link NotNullDatesValidator}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@SuppressWarnings("all")
public class NotNullDatesValidatorTest {

	@Test
	@DisplayName("apply(null) + date")
	public void apply_0() {
		NotNullDatesValidator validator = new NotNullDatesValidator("ERR", () -> LocalDate.now());
		ValidationResult result = validator.apply(null, new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}

	@Test
	@DisplayName("apply(null) + null")
	public void apply_1() {
		NotNullDatesValidator validator = new NotNullDatesValidator("ERR", () -> null);
		ValidationResult result = validator.apply(null, new ValueContext());
		assertNotNull(result);
		assertTrue(result.isError());
	}

	@Test
	@DisplayName("apply(e) + date")
	public void apply_2() {
		NotNullDatesValidator validator = new NotNullDatesValidator("ERR", () -> LocalDate.now());
		ValidationResult result = validator.apply(LocalDate.of(2023, 12, 26), new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}

	@Test
	@DisplayName("apply(e) + null")
	public void apply_3() {
		NotNullDatesValidator validator = new NotNullDatesValidator("ERR", () -> null);
		ValidationResult result = validator.apply(LocalDate.of(2023, 12, 26), new ValueContext());
		assertNotNull(result);
		assertFalse(result.isError());
	}

}
