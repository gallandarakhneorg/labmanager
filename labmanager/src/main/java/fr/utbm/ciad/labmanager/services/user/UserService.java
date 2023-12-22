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

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.user.User;
import fr.utbm.ciad.labmanager.data.user.UserRepository;
import fr.utbm.ciad.labmanager.services.AbstractService;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	/** Replies all the known users.
	 *
	 * @return all the users, never {@code null}.
	 */
	public List<User> getAllUsers() {
		return this.userRepository.findAll();
	}

	/** Replies all the known users.
	 *
	 * @param sortOrder the order specification to use for sorting the users.
	 * @return all the users, never {@code null}.
	 * @since 4.0
	 */
	public List<User> getAllUsers(Sort sortOrder) {
		return this.userRepository.findAll(sortOrder);
	}

	/** Replies all the known users.
	 *
	 * @param filter the filter to apply to the user list.
	 * @return all the users, never {@code null}.
	 * @since 4.0
	 */
	public List<User> getAllUsers(Specification<User> filter) {
       return this.userRepository.findAll(filter);
   }

	/** Replies all the known users.
	 *
	 * @param filter the filter to apply to the user list.
	 * @param sortOrder the order specification to use for sorting the users.
	 * @return all the users, never {@code null}.
	 * @since 4.0
	 */
	public List<User> getAllUsers(Specification<User> filter, Sort sortOrder) {
      return this.userRepository.findAll(filter, sortOrder);
  }

	/** Replies all the known users.
	 *
	 * @param pageable the manager of pagers.
	 * @return all the users, never {@code null}.
	 * @since 4.0
	 */
	public Page<User> getAllUsers(Pageable pageable) {
		return this.userRepository.findAll(pageable);
	}

	/** Replies all the known users.
	 *
	 * @param pageable the manager of pagers.
	 * @param filter the filter to apply to the user list.
	 * @return all the users, never {@code null}.
	 * @since 4.0
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

	/** Replies the application user that is associated to the given person.
	 *
	 * @param person the person.
	 * @return the user or {@code null} if the user cannot be found.
	 */
	public User getUserFor(Person person) {
		return this.userRepository.findByPersonId(person.getId()).orElse(null);
	}

	/** Start the editing of the given user.
	 *
	 * @param user the user to save. It may be {@code null} and, in this case, an user
	 *     is created and associated to the person in the given {@code personContext}.
	 * @param personContext the context used for editing the associated person in parallel.
	 *     This context is used for obtaining the edited person in the case
	 *     a fresh user instance is needed (after deletion).
	 * @return the editing context that enables to keep track of any information needed
	 *      for saving the user and its related resources.
	 */
	public EditingContext startEditing(User user,
			fr.utbm.ciad.labmanager.services.member.PersonService.EditingContext personContext) {
		final User userInstance;
		if (user == null) {
			userInstance = new User();
			userInstance.setPerson(personContext.getPerson());
		} else {
			userInstance = user;
		}
		return new EditingContext(userInstance, personContext);
	}

	/** Context for editing a {@link User}.
	 * This context is usually defined when the entity is associated to
	 * external resources in the server file system.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	public class EditingContext implements Serializable {

		private static final long serialVersionUID = 2325698605037680657L;

		private final fr.utbm.ciad.labmanager.services.member.PersonService.EditingContext personContext;

		private User user;

		/** Constructor.
		 *
		 * @param user the edited user.
		 * @param personContext the context used for editing the associated person in parallel.
		 *     This context is used for obtaining the edited person in the case
		 *     a fresh user instance is needed (after deletion).
		 */
		EditingContext(User user,
				fr.utbm.ciad.labmanager.services.member.PersonService.EditingContext personContext) {
			this.personContext = personContext;
			this.user = user;
			this.user.setPerson(personContext.getPerson());
		}

		/** Replies the user.
		 *
		 * @return the user.
		 */
		public User getUser() {
			return this.user;
		}

		/** Replies the context used for editing the person in parallel to this user..
		 *
		 * @return the editing context for the person associated to this user.
		 */
		public fr.utbm.ciad.labmanager.services.member.PersonService.EditingContext getPersonContext() {
			return this.personContext;
		}

		/** Save or delete the user and the person from the JPA database.
		 *
		 * <p>After calling this function, it is preferable to not use
		 * the user object that was provided before the saving.
		 * Invoke {@link #getUser()} for obtaining the new user
		 * instance, since the content of the saved object may have totally changed.
		 *
		 * @return {@code true} if the user was saved, and {@code false} if the user
		 *     was deleted.
		 */
		@Transactional
		public boolean savePersonAndSaveOrDeleteUser() {
			this.personContext.save();
			//
			if (Strings.isNullOrEmpty(this.user.getLogin())) {
				try {
					UserService.this.userRepository.delete(this.user);
				} catch (Throwable ex) {
					//
				}
				this.user = new User();
				this.user.setPerson(this.personContext.getPerson());
				return false;
			}
			this.user.setPerson(this.personContext.getPerson());
			this.user = UserService.this.userRepository.save(this.user);
			return true;
		}

	}

}
