/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the CIAD laboratory and the Université de Technologie
 * de Belfort-Montbéliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD-UTBM.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.repository.jury;

import java.util.List;

import fr.ciadlab.labmanager.entities.jury.JuryMembership;
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

}
