/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.repository.organization;

import java.util.Optional;

import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

/** JPA Repository for the research organizations.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see ResearchOrganization
 */
public interface ResearchOrganizationRepository extends JpaRepository<ResearchOrganization, Integer> {

	/** Find a research organization with the given acronm or name.
	 *
	 * @param acronym the acronym to search for.
	 * @param name the name to search for.
	 * @return the research organization.
	 */
	Optional<ResearchOrganization> findDistinctByAcronymOrName(String acronym, String name);

}
