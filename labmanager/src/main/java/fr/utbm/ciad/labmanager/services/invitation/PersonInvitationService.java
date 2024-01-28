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

package fr.utbm.ciad.labmanager.services.invitation;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.invitation.PersonInvitation;
import fr.utbm.ciad.labmanager.data.invitation.PersonInvitationRepository;
import fr.utbm.ciad.labmanager.data.invitation.PersonInvitationType;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.services.AbstractEntityService;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.utils.HasAsynchronousUploadService;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import fr.utbm.ciad.labmanager.utils.names.PersonNameParser;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/** Service for the person invitations.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
@Service
public class PersonInvitationService extends AbstractEntityService<PersonInvitation> {

	private PersonInvitationRepository invitationRepository;

	private PersonService personService;

	private PersonNameParser nameParser;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param sessionFactory the Hibernate session factory.
	 * @param invitationRepository the person invitation repository.
	 * @param personService the service for accessing the person.
	 * @param nameParser the parser of person names.
	 */
	public PersonInvitationService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired SessionFactory sessionFactory,
			@Autowired PersonInvitationRepository invitationRepository,
			@Autowired PersonService personService,
			@Autowired PersonNameParser nameParser) {
		super(messages, constants, sessionFactory);
		this.invitationRepository = invitationRepository;
		this.personService = personService;
		this.nameParser = nameParser;
	}

	/** Replies the list of all the outgoing invitations.
	 *
	 * @return the list of all the outgoing invitations.
	 * @since 4.0
	 */
	public List<PersonInvitation> getAllOutgoingInvitations() {
		return this.invitationRepository.findAllDistinctByType(PersonInvitationType.OUTGOING_GUEST);
	}

	/** Replies the list of all the outgoing invitations.
	 *
	 * @param filter the filter of the invitations.
	 * @return the list of all the outgoing invitations.
	 * @since 4.0
	 */
	public List<PersonInvitation> getAllOutgoingIvitations(Specification<PersonInvitation> filter) {
		return this.invitationRepository.findAll(OutgoingInvitationSpecification.SINGLETON.and(filter));
	}

	/** Replies the list of all the outgoing invitations.
	 *
	 * @param filter the filter of the outgoing invitations.
	 * @param sortOrder the order specification to use for sorting the outgoing invitations.
	 * @return the list of all the outgoing invitations.
	 * @since 4.0
	 */
	public List<PersonInvitation> getAllOutgoingInvitations(Specification<PersonInvitation> filter, Sort sortOrder) {
		return this.invitationRepository.findAll(OutgoingInvitationSpecification.SINGLETON.and(filter), sortOrder);
	}

	/** Replies the list of all the outgoing invitations.
	 *
	 * @param sortOrder the order specification to use for sorting the outgoing invitations.
	 * @return the list of all the outgoing invitations.
	 * @since 4.0
	 */
	public List<PersonInvitation> getAllOutgoingInvitations(Sort sortOrder) {
		return this.invitationRepository.findAll(OutgoingInvitationSpecification.SINGLETON, sortOrder);
	}

	/** Replies the list of all the outgoing invitations.
	 *
	 * @param pageable the manager of pages.
	 * @return the list of all the outgoing invitations.
	 * @since 4.0
	 */
	public Page<PersonInvitation> getAllOutgoingInvitations(Pageable pageable) {
		return this.invitationRepository.findAll(OutgoingInvitationSpecification.SINGLETON, pageable);
	}

	/** Replies the list of all the outgoing invitations.
	 *
	 * @param pageable the manager of pages.
	 * @param filter the filter of the axes.
	 * @return the list of all the outgoing invitations.
	 * @since 4.0
	 */
	public Page<PersonInvitation> getAllOutgoingInvitations(Pageable pageable, Specification<PersonInvitation> filter) {
		return this.invitationRepository.findAll(OutgoingInvitationSpecification.SINGLETON.and(filter), pageable);
	}

	/** Replies the list of all the incoming invitations.
	 *
	 * @return the list of all the incoming invitations.
	 * @since 4.0
	 */
	public List<PersonInvitation> getAllIncomingInvitations() {
		return this.invitationRepository.findAll(IncomingInvitationSpecification.SINGLETON);
	}

	/** Replies the list of all the incoming invitations.
	 *
	 * @param filter the filter of the invitations.
	 * @return the list of all the incoming invitations.
	 * @since 4.0
	 */
	public List<PersonInvitation> getAllIncomingIvitations(Specification<PersonInvitation> filter) {
		return this.invitationRepository.findAll(IncomingInvitationSpecification.SINGLETON.and(filter));
	}

	/** Replies the list of all the incoming invitations.
	 *
	 * @param filter the filter of the incoming invitations.
	 * @param sortOrder the order specification to use for sorting the incoming invitations.
	 * @return the list of all the incoming invitations.
	 * @since 4.0
	 */
	public List<PersonInvitation> getAllIncomingInvitations(Specification<PersonInvitation> filter, Sort sortOrder) {
		return this.invitationRepository.findAll(IncomingInvitationSpecification.SINGLETON.and(filter), sortOrder);
	}

	/** Replies the list of all the incoming invitations.
	 *
	 * @param sortOrder the order specification to use for sorting the incoming invitations.
	 * @return the list of all the incoming invitations.
	 * @since 4.0
	 */
	public List<PersonInvitation> getAllIncomingInvitations(Sort sortOrder) {
		return this.invitationRepository.findAll(IncomingInvitationSpecification.SINGLETON, sortOrder);
	}

	/** Replies the list of all the incoming invitations.
	 *
	 * @param pageable the manager of pages.
	 * @return the list of all the incoming invitations.
	 * @since 4.0
	 */
	public Page<PersonInvitation> getAllIncomingInvitations(Pageable pageable) {
		return this.invitationRepository.findAll(IncomingInvitationSpecification.SINGLETON, pageable);
	}

	/** Replies the list of all the incoming invitations.
	 *
	 * @param pageable the manager of pages.
	 * @param filter the filter of the axes.
	 * @return the list of all the incoming invitations.
	 * @since 4.0
	 */
	public Page<PersonInvitation> getAllIncomingInvitations(Pageable pageable, Specification<PersonInvitation> filter) {
		return this.invitationRepository.findAll(IncomingInvitationSpecification.SINGLETON.and(filter), pageable);
	}

	/** Replies the invitations for the given person whatever if he/she is guest or inviter.
	 *
	 * @param personId the identifier of the person to search for.
	 * @return the invitations for the person.
	 */
	public List<PersonInvitation> getInvitationsForPerson(long personId) {
		return this.invitationRepository.findAllByGuestIdOrInviterId(personId, personId);
	}

	/** Add a person invitation.
	 *
	 * @param person the identifier of the person who is involved in invitation as guest or inviter.
	 * @param guest the name of the guest person.
	 * @param inviter the name of the inviting person.
	 * @param startDate the start date of the invitation. 
	 * @param endDate the end date of the invitation. 
	 * @param type the type of invitation.
	 * @param title the title of the works.
	 * @param university the name of the university of the partner person.
	 * @param country the country of the university.
	 * @return the created invitation.
	 */
	public PersonInvitation addPersonInvitation(long person,
			String guest, String inviter,
			LocalDate startDate, LocalDate endDate,
			PersonInvitationType type,
			String title, String university, CountryCode country) {
		final var personObj = this.personService.getPersonById(person);
		if (personObj != null) {
			final var inv = new PersonInvitation();
			updatePersonInvitationWithoutSaving(inv, personObj, guest, inviter, startDate, endDate, type, title, university, country);
			return this.invitationRepository.save(inv);
		}
		throw new IllegalArgumentException("Person not found with id: " + person); //$NON-NLS-1$
	}

	/** Update a person invitation.
	 *
	 * @param invitation the identifier of the invitation to be updated.
	 * @param person the identifier of the person who is involved in invitation as guest or inviter.
	 * @param guest the name of the guest person.
	 * @param inviter the name of the inviting person.
	 * @param startDate the start date of the invitation. 
	 * @param endDate the end date of the invitation. 
	 * @param type the type of invitation.
	 * @param title the title of the works.
	 * @param university the name of the university of the partner person.
	 * @param country the country of the university.
	 * @return the created invitation.
	 */
	public PersonInvitation updatePersonInvitation(
			long invitation,
			long person,
			String guest, String inviter,
			LocalDate startDate, LocalDate endDate,
			PersonInvitationType type,
			String title, String university, CountryCode country) {
		final var optInvitation = this.invitationRepository.findById(Long.valueOf(invitation));
		if (optInvitation.isEmpty()) {
			throw new IllegalArgumentException("Invitation not found with id: " + invitation); //$NON-NLS-1$
		}
		final var invitationObj = optInvitation.get();
		//
		final var personObj = this.personService.getPersonById(person);
		if (personObj == null) {
			throw new IllegalArgumentException("Person not found with id: " + person); //$NON-NLS-1$
		}
		//
		updatePersonInvitationWithoutSaving(invitationObj, personObj, guest, inviter, startDate, endDate, type, title, university, country);
		return this.invitationRepository.save(invitationObj);
	}

	/** Update a person invitation without saving the JPA.
	 *
	 * @param invitation the invitation to be updated.
	 * @param person the person who is involved in invitation as guest or inviter.
	 * @param guest the name of the guest person.
	 * @param inviter the name of the inviting person.
	 * @param startDate the start date of the invitation. 
	 * @param endDate the end date of the invitation. 
	 * @param type the type of invitation.
	 * @param title the title of the works.
	 * @param university the name of the university of the partner person.
	 * @param country the country of the university.
	 */
	public void updatePersonInvitationWithoutSaving(
			PersonInvitation invitation,
			Person person,
			String guest, String inviter,
			LocalDate startDate, LocalDate endDate,
			PersonInvitationType type,
			String title, String university, CountryCode country) {
		final var guestObj = extractPerson(guest, true, this.personService, this.nameParser);
		final var inviterObj = extractPerson(inviter, true, this.personService, this.nameParser);
		if (person.getId() != guestObj.getId() && person.getId() != inviterObj.getId()) {
			throw new IllegalArgumentException("Person with name '" + person.getFullName() + "' is neither guest nor inviter"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		//
		invitation.setType(type);
		invitation.setGuest(guestObj);
		invitation.setInviter(inviterObj);
		invitation.setStartDate(startDate);
		invitation.setEndDate(endDate);
		invitation.setTitle(title);
		invitation.setUniversity(university);
		invitation.setCountry(country);
	}

	/** Delete the person invitation with the given identifier.
	 *
	 * @param invitationId the identifier of the person invitation to be deleted.
	 * @throws Exception in case of error.
	 */
	@Transactional
	public void removePersonInvitation(long invitationId) {
		final var iid = Long.valueOf(invitationId);
		final var optInv = this.invitationRepository.findById(iid);
		if (optInv.isEmpty()) {
			throw new IllegalStateException("Person invitation not found with id: " + invitationId); //$NON-NLS-1$
		}
		this.invitationRepository.deleteById(iid);
	}

	/** Save the given invitation into the database.
	 *
	 * @param invitation the invitation to save.
	 */
	public void save(PersonInvitation invitation) {
		this.invitationRepository.save(invitation);
	}

	/** Replies if the given identifier is for a person who is associated to an invitation as inverter or invitee.
	 * 
	 * @param id the identifier of the person.
	 * @return {@code true} if the person is an inviter or an invitee.
	 * @since 3.6
	 */
	public boolean isAssociated(long id) {
		return !this.invitationRepository.findAllByGuestIdOrInviterId(id, id).isEmpty();
	}

	@Override
	public EntityEditingContext<PersonInvitation> startEditing(PersonInvitation invitation) {
		assert invitation != null;
		return new EditingContext(invitation);
	}

	@Override
	public EntityDeletingContext<PersonInvitation> startDeletion(Set<PersonInvitation> invitations) {
		assert invitations != null && !invitations.isEmpty();
		return new DeletingContext(invitations);
	}

	/** Context for editing a {@link PersonInvitation}.
	 * This context is usually defined when the entity is associated to
	 * external resources in the server file system.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected class EditingContext extends AbstractEntityEditingContext<PersonInvitation> {

		private static final long serialVersionUID = 44656900669005156L;

		/** Constructor.
		 *
		 * @param invitation the edited invitation.
		 */
		protected EditingContext(PersonInvitation invitation) {
			super(invitation);
		}

		@Override
		public void save(HasAsynchronousUploadService... components) throws IOException {
			this.entity = PersonInvitationService.this.invitationRepository.save(this.entity);
		}

		@Override
		public EntityDeletingContext<PersonInvitation> createDeletionContext() {
			return PersonInvitationService.this.startDeletion(Collections.singleton(this.entity));
		}

	}

	/** Context for deleting a {@link PersonInvitation}.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected class DeletingContext extends AbstractEntityDeletingContext<PersonInvitation> {

		private static final long serialVersionUID = 3488749520286411999L;

		/** Constructor.
		 *
		 * @param invitations the person invitations to delete.
		 */
		protected DeletingContext(Set<PersonInvitation> invitations) {
			super(invitations);
		}

		@Override
		protected void deleteEntities() throws Exception {
			PersonInvitationService.this.invitationRepository.deleteAllById(getDeletableEntityIdentifiers());
		}

	}

	/** Specification that is validating outgoing invitations.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	public static class OutgoingInvitationSpecification implements Specification<PersonInvitation> {

		private static final long serialVersionUID = -922262104471108626L;

		/** Singleton for this criteria.
		 */
		public static final OutgoingInvitationSpecification SINGLETON = new OutgoingInvitationSpecification();

		@Override
		public Predicate toPredicate(Root<PersonInvitation> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			return criteriaBuilder.equal(root.get("type"), PersonInvitationType.OUTGOING_GUEST); //$NON-NLS-1$
		}

	}

	/** Specification that is validating incoming invitations.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	public static class IncomingInvitationSpecification implements Specification<PersonInvitation> {

		private static final long serialVersionUID = 1354215003601539541L;

		/** Singleton for this criteria.
		 */
		public static final IncomingInvitationSpecification SINGLETON = new IncomingInvitationSpecification();

		@Override
		public Predicate toPredicate(Root<PersonInvitation> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			final Predicate p0 = criteriaBuilder.equal(root.get("type"), PersonInvitationType.INCOMING_GUEST_PHD_STUDENT); //$NON-NLS-1$
			final Predicate p1 = criteriaBuilder.equal(root.get("type"), PersonInvitationType.INCOMING_GUEST_PROFESSOR); //$NON-NLS-1$
			return criteriaBuilder.or(p0, p1);
		}

	}

}
