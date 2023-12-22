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

package fr.utbm.ciad.labmanager.components.messages;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.vaadin.flow.i18n.I18NProvider;
import fr.utbm.ciad.labmanager.configuration.messages.BaseMessageSource;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Provider of I18N messages for Vaadin. This providers is linked to the {@link BaseMessageSource Spring message source}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Component
public class SpringBasedI18NProvider implements I18NProvider {

	private static final long serialVersionUID = 7109279618234895282L;

	private static final List<Locale> LOCALES;
		
	private final BaseMessageSource springMessageSource;
	
	static {
		LOCALES = Arrays.asList(Locale.US, Locale.FRANCE);
	}

	/** Constructor.
	 *
	 * @param springMessageSource the spring message source.
	 */
	public SpringBasedI18NProvider(@Autowired BaseMessageSource springMessageSource) {
		this.springMessageSource = springMessageSource;
	}
	
	@Override
	public List<Locale> getProvidedLocales() {
		return LOCALES;
	}

	@Override
	public String getTranslation(String key, Locale locale, Object... params) {
		assert !Strings.isNullOrEmpty(key);
		final var concreteLocale = locale == null ? Locale.getDefault() : locale;
		return this.springMessageSource.getMessageSource().getMessage(key, params, concreteLocale);
	}
	
}
