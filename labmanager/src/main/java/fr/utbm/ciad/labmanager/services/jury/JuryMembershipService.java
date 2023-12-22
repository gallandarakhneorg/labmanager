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

package fr.utbm.ciad.labmanager.services.jury;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.jury.JuryMembership;
import fr.utbm.ciad.labmanager.data.jury.JuryMembershipRepository;
import fr.utbm.ciad.labmanager.data.jury.JuryMembershipType;
import fr.utbm.ciad.labmanager.data.jury.JuryType;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.services.AbstractService;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import fr.utbm.ciad.labmanager.utils.names.PersonNameParser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for the jury memberships.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0
 */
@Service
public class JuryMembershipService extends AbstractService {

	private JuryMembershipRepository membershipRepository;

	private PersonService personService;

	private PersonNameParser nameParser;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param membershipRepository the jury membership repository.
	 * @param personService the service for accessing the persons.
	 * @param nameParser the parser of person names.
	 */
	public JuryMembershipService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired JuryMembershipRepository membershipRepository,
			@Autowired PersonService personService,
			@Autowired PersonNameParser nameParser) {
		super(messages, constants);
		this.membershipRepository = membershipRepository;
		this.personService = personService;
		this.nameParser = nameParser;
	}

	/** Replies the list of jury memberships for the person with the given identifier.
	 *
	 * @param personId the identifier of the person.
	 * @return the list of jury memberships.
	 */
	public List<JuryMembership> getMembershipsForPerson(long personId) {
		return this.membershipRepository.findAllByPersonId(personId);
	}

	/** Replies the list of jury memberships for the candidate with the given identifier.
	 *
	 * @param candidateId the identifier of the candidate.
	 * @return the list of jury memberships.
	 */
	public List<JuryMembership> getMembershipsForCandidate(long candidateId) {
		return this.membershipRepository.findAllByCandidateId(candidateId);
	}

	/** Replies the list of jury memberships for the promoter with the given identifier.
	 *
	 * @param promoterId the identifier of the promoter.
	 * @return the list of jury memberships.
	 */
	public List<JuryMembership> getMembershipsForPromoter(long promoterId) {
		return this.membershipRepository.findAllByPromotersId(promoterId);
	}

	/** Add a jury membership.
	 *
	 * @param person the identifier of the person who is involved in the jury.
	 * @param date the date of the defense. 
	 * @param membershipType the type of membership.
	 * @param defenseType the type of defense.
	 * @param title the title of the evaluated works.
	 * @param candidate the name of the candidate. It is a database identifier (for known person) or full name
	 *     (for unknown person).
	 * @param university the name of the university in which the defense was done.
	 * @param country the country of the university.
	 * @param promoters the list of promoters/directors. It is a list of database identifiers (for known persons) or full names
	 *     (for unknown persons).
	 * @return the created jury membership.
	 */
	public JuryMembership addJuryMembership(long person, LocalDate date, JuryMembershipType membershipType,
			JuryType defenseType, String title, String candidate, String university, CountryCode country,
			List<String> promoters) {
		final var personObj = this.personService.getPersonById(person);
		if (personObj != null) {
			final var mem = new JuryMembership();
			updateJuryMembershipWithoutSaving(mem, personObj, date, membershipType, defenseType, title, candidate, university, country, promoters);
			return this.membershipRepository.save(mem);
		}
		throw new IllegalArgumentException("Person not found with id: " + person); //$NON-NLS-1$
	}

	/** Update the jury membership with the given identifier.
	 *
	 * @param membershipId the identifier of the jury membership to be updated.
	 * @param person the identifier of the person who is involved in the jury.
	 * @param date the date of the defense. 
	 * @param membershipType the type of membership.
	 * @param defenseType the type of defense.
	 * @param title the title of the evaluated works.
	 * @param candidate the name of the candidate. It is a database identifier (for known person) or full name
	 *     (for unknown person).
	 * @param university the name of the university in which the defense was done.
	 * @param country the country of the university.
	 * @param promoters the list of promoters/directors. It is a list of database identifiers (for known persons) or full names
	 *     (for unknown persons).
	 * @return the updated jury membership.
	 */
	public JuryMembership updateJuryMembership(long membershipId, int person, LocalDate date, JuryMembershipType membershipType,
			JuryType defenseType, String title, String candidate, String university, CountryCode country,
			List<String> promoters) {
		final var optMembership = this.membershipRepository.findById(Long.valueOf(membershipId));
		if (optMembership.isEmpty()) {
			throw new IllegalArgumentException("Jury membership not found with id: " + membershipId); //$NON-NLS-1$
		}
		final var membership = optMembership.get();
		final var personObj = this.personService.getPersonById(person);
		if (personObj == null) {
			throw new IllegalArgumentException("Person not found with id: " + person); //$NON-NLS-1$
		}
		updateJuryMembershipWithoutSaving(membership, personObj, date, membershipType, defenseType, title, candidate, university, country, promoters);
		return this.membershipRepository.save(membership);
	}

	/** Update the jury membership with the given identifier.
	 *
	 * @param membership the jury membership to be updated.
	 * @param person the person who is involved in the jury.
	 * @param date the date of the defense. 
	 * @param membershipType the type of membership.
	 * @param defenseType the type of defense.
	 * @param title the title of the evaluated works.
	 * @param candidate the name of the candidate. It is a database identifier (for known person) or full name
	 *     (for unknown person).
	 * @param university the name of the university in which the defense was done.
	 * @param country the country of the university.
	 * @param promoters the list of promoters/directors. It is a list of database identifiers (for known persons) or full names
	 *     (for unknown persons).
	 */
	public void updateJuryMembershipWithoutSaving(JuryMembership membership, Person person, LocalDate date, JuryMembershipType membershipType,
			JuryType defenseType, String title, String candidate, String university, CountryCode country,
			List<String> promoters) {
		if (defenseType == JuryType.BAC && membershipType != JuryMembershipType.PRESIDENT) {
			throw new IllegalStateException("Unsupported type of jury membership for " + defenseType.name() + " and " + membershipType.name()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		//
		membership.setPerson(person);
		membership.setDate(date);
		membership.setType(membershipType);
		membership.setDefenseType(defenseType);
		membership.setTitle(title);
		membership.setCandidate(extractPerson(candidate, true, this.personService, this.nameParser));
		membership.setUniversity(university);
		membership.setCountry(country);
		final var promoterArray = new ArrayList<Person>();
		forEarchPerson(promoters, true, this.personService, this.nameParser, it -> promoterArray.add(it));
		membership.setPromoters(promoterArray);
	}

	/** Delete the jury membership with the given identifier.
	 *
	 * @param membershipId the identifier of the jury membership to be deleted.
	 * @throws Exception in case of error.
	 */
	@Transactional
	public void removeJuryMembership(long membershipId) throws Exception {
		final var mid = Long.valueOf(membershipId);
		final var optMbr = this.membershipRepository.findById(mid);
		if (optMbr.isEmpty()) {
			throw new IllegalStateException("Jury membership not found with id: " + membershipId); //$NON-NLS-1$
		}
		this.membershipRepository.deleteById(mid);
	}

	/** Save the given jury membership into the database.
	 *
	 * @param membership the jury membership to save.
	 */
	public void save(JuryMembership membership) {
		this.membershipRepository.save(membership);
	}

	/** Replies if the given identifier is for a person who is participating to a jury.
	 * This participation may be as candidate or member of the jury itself.
	 *
	 * @param id the identifier of the person.
	 * @return {@code true} if the person is candidate or member of a jury.
	 * @since 3.6
	 */
	public boolean isInvolved(long id) {
		return !this.membershipRepository.findAllByPersonId(id).isEmpty()
				|| !this.membershipRepository.findAllByCandidateId(id).isEmpty()
				|| !this.membershipRepository.findAllByPromotersId(id).isEmpty();
	}

}
