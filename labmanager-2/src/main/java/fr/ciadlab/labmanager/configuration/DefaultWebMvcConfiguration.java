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

package fr.ciadlab.labmanager.configuration;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

/** Override of the MVC configuration for forcing the locale definition.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
public class DefaultWebMvcConfiguration extends DelegatingWebMvcConfiguration {

	/** Replies the locale resolver that must be used by this application.
	 *
	 * @return the locale resolver.
	 */
	@Bean
	@Override
	public LocaleResolver localeResolver() {
		//final SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		final FixedLocaleResolver localeResolver = new FixedLocaleResolver();
		localeResolver.setDefaultLocale(defaultLocale());
		return localeResolver;
	}

	/** Replies the default locale.
	 *
	 * @return the default locale.
	 */
	@SuppressWarnings("static-method")
	@Bean
	public Locale defaultLocale() {
		return Locale.ENGLISH;
	}

}
