/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
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

package fr.ciadlab.labmanager.repository.teaching;

import java.util.List;

import fr.ciadlab.labmanager.entities.teaching.TeachingActivity;
import org.springframework.data.jpa.repository.JpaRepository;

/** JPA repository for teaching activity declaration.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.4
 */
public interface TeachingActivityRepository extends JpaRepository<TeachingActivity, Integer> {

	/** Replies the list of activities that associated to the person with the given id.
	 *
	 * @param id the identifier.
	 * @return the list of activities.
	 */
	List<TeachingActivity> findDistinctByPersonId(Integer id);

}
