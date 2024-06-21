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
import fr.utbm.ciad.labmanager.Constants;
import fr.utbm.ciad.labmanager.views.appviews.login.AdaptiveLoginView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/** Configuration of the security for API and Vaadin.
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

	private static final String API_URL = "/api/v" + Constants.MANAGER_MAJOR_VERSION + "/**/*"; //$NON-NLS-1$ //$NON-NLS-2$

	/** The default password encoder when using local security system.
	 *
	 * @return the encoder.
	 */
	@SuppressWarnings("static-method")
	@Bean
	public PasswordEncoder passwordEncoder() {
		/* TODO
	   		return new BCryptPasswordEncoder();
		 */
		return NoOpPasswordEncoder.getInstance();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Access to API is allowed and anonymous
		http.authorizeHttpRequests(authorize -> authorize.requestMatchers(new AntPathRequestMatcher(API_URL)).anonymous());
		http.csrf(cfg -> cfg.ignoringRequestMatchers(new AntPathRequestMatcher(API_URL)));
		
        // Access to images is allowed
		http.authorizeHttpRequests(authorize -> authorize.requestMatchers(new AntPathRequestMatcher("/images/**/*")).permitAll()); //$NON-NLS-1$

		// Access to icons from the line-awesome addon
		http.authorizeHttpRequests(authorize -> authorize.requestMatchers(new AntPathRequestMatcher("/line-awesome/**/*.svg")).permitAll()); //$NON-NLS-1$

		super.configure(http);

		setLoginView(http, AdaptiveLoginView.class);
	}

}
