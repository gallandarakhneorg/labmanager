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

package fr.utbm.ciad.labmanager.views.components;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.stereotype.Component;

/** Factory of Vaadin components.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Component
public final class ComponentFactory {

	private ComponentFactory() {
		//
	}

	/** Create a form layout with multiple columns.
	 *
	 * @param columns the number of columns between 1 and 2.
	 * @return the layout.
	 */
	public static FormLayout newColumnForm(int columns) {
		assert columns >= 1 && columns <= 2;
		final FormLayout content = new FormLayout();
		switch (columns) {
		case 1:
			// No specific configuration for a single column
			break;
		case 2:
			content.setResponsiveSteps(
					new FormLayout.ResponsiveStep("0", 1), //$NON-NLS-1$
					new FormLayout.ResponsiveStep("20em", 2)); //$NON-NLS-1$
			break;
		default:
			throw new IllegalArgumentException();
		}
		return content;
	}

	/** Create a text field for phone numbers, with international prefix.
	 *
	 * @param region the code of the region; never {@code null}.
	 * @return the field.
	 * @see #newPhoneNumberField(CountryCode)
	 */
	public static TextField newPhoneNumberField(String region) {
		assert !Strings.isNullOrEmpty(region);
		final TextField content = new TextField();
		//final PhoneI18nFieldFormatter formatter = new PhoneI18nFieldFormatter(region);
		//formatter.extend(content);
		return content;
	}

	/** Create a text field for phone numbers, with international prefix.
	 *
	 * @param region the code of the region; never {@code null}.
	 * @return the field.
	 * @see #newPhoneNumberField(String)
	 */
	public static TextField newPhoneNumberField(CountryCode region) {
		assert region != null;
		return newPhoneNumberField(region.getCode());
	}

}
