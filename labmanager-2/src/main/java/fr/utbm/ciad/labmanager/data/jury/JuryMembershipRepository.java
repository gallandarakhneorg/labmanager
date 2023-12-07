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

package fr.utbm.ciad.labmanager.data.jury;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/** JPA repository for jury memberships.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public interface JuryMembershipRepository extends JpaRepository<JuryMembership, Integer> {

	/** Replies the list of memberships that are associated to the person with the given identifier.
	 *
	 * @param personId the identifier of the person.
	 * @return the list of memberships.
	 */
	List<JuryMembership> findAllByPersonId(int personId);

	/** Replies the list of memberships that are associated to the candidate with the given identifier.
	 *
	 * @param candidateId the identifier of the candidate.
	 * @return the list of memberships.
	 */
	List<JuryMembership> findAllByCandidateId(int candidateId);

	/** Replies the list of memberships that are associated to the promoter with the given identifier.
	 *
	 * @param promoterId the identifier of the promoter.
	 * @return the list of memberships.
	 */
	List<JuryMembership> findAllByPromotersId(int promoterId);

	/** Replies a membership that corresponds to the person, candidate and type.
	 *
	 * @param personId the identifier of the person.
	 * @param candidateId the identifier of the candidate.
	 * @param type the type of membership.
	 * @return the membership from the database.
	 */
	Optional<JuryMembership> findByPersonIdAndCandidateIdAndType(int personId, int candidateId, JuryMembershipType type);

	/** Count the number of jury memberships for the participant with the given id.
	 *
	 * @param id the identifier of the participant.
	 * @return the count of supervisions.
	 */
	int countDistinctByPersonId(int id);

}
