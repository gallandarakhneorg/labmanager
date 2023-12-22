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

package fr.utbm.ciad.labmanager.tests.views.components.converters;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import fr.utbm.ciad.labmanager.views.components.converters.StringTrimer;
import fr.utbm.ciad.labmanager.views.components.validators.OrcidValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for {@link StringTrimer}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@SuppressWarnings("all")
public class StringTrimerTest {

	@Test
	@DisplayName("convertToModel(null)")
	public void convertToModel_0() {
		StringTrimer converter = new StringTrimer();
		Result<String> result = converter.convertToModel(null, new ValueContext());
		assertNotNull(result);
		result.handle(
				it -> assertEquals("", it),
				it -> fail("Unexpected error detected"));
	}

	@Test
	@DisplayName("convertToModel(\"\")")
	public void convertToModel_1() {
		StringTrimer converter = new StringTrimer();
		Result<String> result = converter.convertToModel("", new ValueContext());
		assertNotNull(result);
		result.handle(
				it -> assertEquals("", it),
				it -> fail("Unexpected error detected"));
	}

	@Test
	@DisplayName("convertToModel(\"     \")")
	public void convertToModel_2() {
		StringTrimer converter = new StringTrimer();
		Result<String> result = converter.convertToModel("    ", new ValueContext());
		assertNotNull(result);
		result.handle(
				it -> assertEquals("", it),
				it -> fail("Unexpected error detected"));
	}

	@Test
	@DisplayName("convertToModel(\"abc\")")
	public void convertToModel_3() {
		StringTrimer converter = new StringTrimer();
		Result<String> result = converter.convertToModel("abc", new ValueContext());
		assertNotNull(result);
		result.handle(
				it -> assertEquals("abc", it),
				it -> fail("Unexpected error detected"));
	}

	@Test
	@DisplayName("convertToModel(\"   abc\")")
	public void convertToModel_4() {
		StringTrimer converter = new StringTrimer();
		Result<String> result = converter.convertToModel("   abc", new ValueContext());
		assertNotNull(result);
		result.handle(
				it -> assertEquals("abc", it),
				it -> fail("Unexpected error detected"));
	}

	@Test
	@DisplayName("convertToModel(\"abc   \")")
	public void convertToModel_5() {
		StringTrimer converter = new StringTrimer();
		Result<String> result = converter.convertToModel("abc   ", new ValueContext());
		assertNotNull(result);
		result.handle(
				it -> assertEquals("abc", it),
				it -> fail("Unexpected error detected"));
	}

	@Test
	@DisplayName("convertToModel(\"   abc  \")")
	public void convertToModel_6() {
		StringTrimer converter = new StringTrimer();
		Result<String> result = converter.convertToModel("   abc  ", new ValueContext());
		assertNotNull(result);
		result.handle(
				it -> assertEquals("abc", it),
				it -> fail("Unexpected error detected"));
	}

	@Test
	@DisplayName("convertToModel(\"   ab c  \")")
	public void convertToModel_7() {
		StringTrimer converter = new StringTrimer();
		Result<String> result = converter.convertToModel("   ab c  ", new ValueContext());
		assertNotNull(result);
		result.handle(
				it -> assertEquals("ab c", it),
				it -> fail("Unexpected error detected"));
	}

	@Test
	@DisplayName("convertToPresentation(null)")
	public void convertToPresentation_0() {
		StringTrimer converter = new StringTrimer();
		String result = converter.convertToPresentation(null, new ValueContext());
		assertEquals("", result);
	}

	@Test
	@DisplayName("convertToPresentation(\"\")")
	public void convertToPresentation_1() {
		StringTrimer converter = new StringTrimer();
		String result = converter.convertToPresentation("", new ValueContext());
		assertNotNull(result);
		assertEquals("", result);
	}

	@Test
	@DisplayName("convertToPresentation(\"     \")")
	public void convertToPresentation_2() {
		StringTrimer converter = new StringTrimer();
		String result = converter.convertToPresentation("    ", new ValueContext());
		assertNotNull(result);
		assertEquals("", result);
	}

	@Test
	@DisplayName("convertToPresentation(\"abc\")")
	public void convertToPresentation_3() {
		StringTrimer converter = new StringTrimer();
		String result = converter.convertToPresentation("abc", new ValueContext());
		assertNotNull(result);
		assertEquals("abc", result);
	}

	@Test
	@DisplayName("convertToPresentation(\"   abc\")")
	public void convertToPresentation_4() {
		StringTrimer converter = new StringTrimer();
		String result = converter.convertToPresentation("   abc", new ValueContext());
		assertNotNull(result);
		assertEquals("abc", result);
	}

	@Test
	@DisplayName("convertToPresentation(\"abc   \")")
	public void convertToPresentation_5() {
		StringTrimer converter = new StringTrimer();
		String result = converter.convertToPresentation("abc   ", new ValueContext());
		assertNotNull(result);
		assertEquals("abc", result);
	}

	@Test
	@DisplayName("convertToPresentation(\"   abc  \")")
	public void convertToPresentation_6() {
		StringTrimer converter = new StringTrimer();
		String result = converter.convertToPresentation("   abc  ", new ValueContext());
		assertNotNull(result);
		assertEquals("abc", result);
	}

	@Test
	@DisplayName("convertToPresentation(\"   ab c  \")")
	public void convertToPresentation_7() {
		StringTrimer converter = new StringTrimer();
		String result = converter.convertToPresentation("   ab c  ", new ValueContext());
		assertNotNull(result);
		assertEquals("ab c", result);
	}

}
