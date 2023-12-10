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

import java.util.List;
import java.util.Optional;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.user.User;
import fr.utbm.ciad.labmanager.data.user.UserRepository;
import fr.utbm.ciad.labmanager.services.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/** Service for the application users.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Service
public class UserService extends AbstractService {

	private final UserRepository userRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param userRepository the repository for the application users.
	 */
	public UserService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired UserRepository userRepository) {
		super(messages, constants);
		this.userRepository = userRepository;
	}

//	/** Replies the application user for the person with the given identifier.
//	 *
//	 * @param id the identifier of the person.
//	 * @return the user.
//	 */
//	public Optional<User> getUserByPersonId(int id) {
//		return this.userRepository.findByPersonId(Integer.valueOf(id));
//	}

	/** Replies all the known users.
	 *
	 * @return all the users, never {@code null}.
	 */
	public List<User> getAllUsers() {
		return this.userRepository.findAll();
	}

	/** Replies all the known users.
	 *
	 * @param pageable the manager of pagers.
	 * @return all the users, never {@code null}.
	 */
	public Page<User> getAllUsers(Pageable pageable) {
		return this.userRepository.findAll(pageable);
	}

	/** Replies all the known users.
	 *
	 * @param pageable the manager of pagers.
	 * @param filter the filter to apply to the user list.
	 * @return all the users, never {@code null}.
	 */
	public Page<User> getAllUsers(Pageable pageable, Specification<User> filter) {
        return this.userRepository.findAll(filter, pageable);
    }

    /** Replies the application user that has the given identifier.
	 *
	 * @param id the identifier.
	 * @return the user or {@code null} if the user cannot be found.
	 */
	public User getUserById(int id) {
		final Optional<User> opt = this.userRepository.findById(Integer.valueOf(id));
		if (opt.isPresent()) {
			return opt.get();
		}
		return null;
	}

}
