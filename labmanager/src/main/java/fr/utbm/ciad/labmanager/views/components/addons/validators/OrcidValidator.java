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

package fr.utbm.ciad.labmanager.views.components.addons.validators;

import com.google.common.base.Strings;
import com.vaadin.flow.data.binder.ErrorLevel;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.RegexpValidator;

/** A string validator for ORCID numbers.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class OrcidValidator extends RegexpValidator {

	private static final long serialVersionUID = -5751450579330516322L;

	private static final String PATTERN = "^\\s*[0-9a-zA-Z]{4}\\-[0-9a-zA-Z]{4}\\-[0-9a-zA-Z]{4}\\-[0-9a-zA-Z]{4}\\s*$"; //$NON-NLS-1$

	private final boolean allowEmptyValue;

	private final String warningMessageWhenEmpty;

	/**
	 * Creates a validator for checking that a string is a syntactically valid
	 * ORCID id.
	 * <p>
	 * This constructor creates a validator which does not accept an empty string
	 * as a valid ORCID id. Use {@link #OrcidValidator(String, String, boolean)}
	 * constructor with {@code true} as a value for the second argument to
	 * create a validator which accepts an empty string.
	 *
	 * @param errorMessage the message to display in case the value does not validate. Parameter {@code {0}} is replaced by the invalid entity in the message.
	 * @param warningMessage the message to display in case the value does validate but is empty. It could be
	 *     {@code null} to disable the warning.
	 * @see #OrcidValidator(String, String, boolean)
	 */
	public OrcidValidator(String errorMessage, String warningMessage) {
		this(errorMessage, warningMessage, false);
	}

	/**
	 * Creates a validator for checking that a string is a syntactically valid
	 * ORCID id.
	 *
	 * @param errorMessage the message to display in case the value does not validate. Must not be {@code null}. Parameter {@code {0}} is replaced by the invalid entity in the message.
	 * @param warningMessageWhenEmpty the message to display in case the value does validate but is empty. It could be
	 *     {@code null} to disable the warning.
	 * @param allowEmpty if {@code true} then an empty string passes the validation,
	 *     otherwise the validation fails.
	 */
	public OrcidValidator(String errorMessage, String warningMessageWhenEmpty, boolean allowEmpty) {
		super(errorMessage, PATTERN, true);
		this.allowEmptyValue = allowEmpty;
		this.warningMessageWhenEmpty = Strings.emptyToNull(warningMessageWhenEmpty);
	}

	@Override
	protected boolean isValid(String value) {
		if (Strings.isNullOrEmpty(value) || value.isBlank()) {
			return this.allowEmptyValue;
		}
		return super.isValid(value);
	}

	@Override
    public ValidationResult apply(String value, ValueContext context) {
		final var valid = isValid(value);
		if (valid) {
			if (this.warningMessageWhenEmpty != null && Strings.isNullOrEmpty(value)) {
				return ValidationResult.create(this.warningMessageWhenEmpty, ErrorLevel.WARNING);
			}
			return ValidationResult.ok();
		}
		// Replies the error status
        return toResult(value, valid);
    }

}
