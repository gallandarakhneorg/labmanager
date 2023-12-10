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

package fr.utbm.ciad.labmanager.configuration.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import fr.utbm.ciad.labmanager.views.login.LoginView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/** Configuration of the security login.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

	/** The default password encoder when using local security system.
	 *
	 * @return the encoder.
	 */
    @Bean
    public PasswordEncoder passwordEncoder() {
        //TODO return new BCryptPasswordEncoder();
    	return NoOpPasswordEncoder.getInstance();
    }

    @Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				authorize -> authorize.requestMatchers(new AntPathRequestMatcher("/images/**/*")).permitAll()); //$NON-NLS-1$

		// Icons from the line-awesome addon
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(new AntPathRequestMatcher("/line-awesome/**/*.svg")).permitAll()); //$NON-NLS-1$

		super.configure(http);
		//http.authenticationProvider(null)
		setLoginView(http, LoginView.class);
	}

// TODO	/**
//	 * Allows access to static resources, bypassing Spring Security.
//	 */
//	@Override
//	public void configure(WebSecurity web) {
//		web.ignoring().requestMatchers(
//				// Client-side JS
//				"/VAADIN/**", //$NON-NLS-1$
//				// the standard favicon URI
//				"/favicon.ico", //$NON-NLS-1$
//				// the robots exclusion standard
//				"/robots.txt", //$NON-NLS-1$
//				// web application manifest
//				"/manifest.webmanifest", //$NON-NLS-1$
//				"/sw.js", //$NON-NLS-1$
//				// icons and images
//				"/icons/**", //$NON-NLS-1$
//				"/images/**", //$NON-NLS-1$
//				"/styles/**"); //$NON-NLS-1$
//	}    

}
