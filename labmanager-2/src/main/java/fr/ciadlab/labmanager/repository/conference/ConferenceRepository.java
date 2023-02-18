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

package fr.ciadlab.labmanager.repository.conference;

import java.util.Optional;
import java.util.Set;

import fr.ciadlab.labmanager.entities.conference.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** JPA repository for conferences.
 * 
 * @author $Author: sgalland$
 * @author $Author: anoubli$
 * @author $Author: bpdj$
 * @author $Author: pgoubet$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
public interface ConferenceRepository extends JpaRepository<Conference, Integer> {

	/** Find the conference with the given acronym or name.
	 *
	 * @param conference the name or acronym.
	 * @return the conference, or nothing.
	 */
	@Query("SELECT DISTINCT c FROM Conference c WHERE c.acronym = :name OR c.name = :name")
	Optional<Conference> findByAcronymOrName(@Param("name") String conference);

	/** Find the conferences by their name.
	 *
	 * @param name the name of the conference to search for.
	 * @return the conferences with the given name.
	 */
	@Query("SELECT DISTINCT c FROM Conference c WHERE c.acronym = :name OR c.name = :name")
	Set<Conference> findDistinctByAcronymOrName(String name);

}
