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

package fr.utbm.ciad.labmanager.services.user;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.vaadin.flow.server.VaadinService;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.user.User;
import fr.utbm.ciad.labmanager.data.user.UserRepository;
import fr.utbm.ciad.labmanager.services.AbstractService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service managing the security access to the application.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Service
public class UserSecurityService extends AbstractService implements UserDetailsService {

	private static final String DEVELOPMENT_MODE_PASSWORD = "x"; //$NON-NLS-1$
	
	private final UserRepository userRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param userRepository the repository for the application users.
	 */
	public UserSecurityService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired UserRepository userRepository) {
		super(messages, constants);
		this.userRepository = userRepository;
	}

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> optUser = this.userRepository.findByLogin(username);
        if (optUser.isEmpty()) {
            throw new UsernameNotFoundException("Invalid user with login: " + username); //$NON-NLS-1$
        }
        final User user = optUser.get();
        final String login = user.getLogin();
        if (Strings.isBlank(login)) {
            throw new UsernameNotFoundException("No institutional login specified for the user with login: " + username); //$NON-NLS-1$
        }
        final String password;
        //if (VaadinService.getCurrent().getDeploymentConfiguration().isProductionMode()) {
        //TODO pwd
        password = DEVELOPMENT_MODE_PASSWORD;
        //} else {
        //	password = DEVELOPMENT_MODE_PASSWORD;
        //}
        return new org.springframework.security.core.userdetails.User(login, password, getAuthorities(user));
    }

    private static List<GrantedAuthority> getAuthorities(User user) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())); //$NON-NLS-1$
    }

}
