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

package fr.ciadlab.labmanager.repository.member;

import java.util.Optional;
import java.util.Set;

import fr.ciadlab.labmanager.entities.member.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

/** JPA repository for the membership relations.
 *
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface MembershipRepository extends JpaRepository<Membership, Integer> {

	/** Find the membership by their organization and member identifiers.
	 *
	 * @param researchoOrganizationId the identifier of the organization.
	 * @param personId the identifier of the member.
	 * @return the membership.
	 */
	Optional<Membership> findDistinctByResearchOrganizationIdAndPersonId(int researchoOrganizationId, int personId);

	/** Delete the membership between the organization and the person with the given identifiers.
	 *
	 * @param researchoOrganizationId the identifier of the organization.
	 * @param personId the identifier of the member.
	 */
	void deleteByResearchOrganizationIdAndPersonId(int researchoOrganizationId, int personId);

	/** Find the memberships by their organization and member identifiers.
	 *
	 * @param researchoOrganizationId the identifier of the organization.
	 * @param personId the identifier of the member.
	 * @return the memberships.
	 */
	Set<Membership> findByResearchOrganizationIdAndPersonId(int researchoOrganizationId, int personId);

}
