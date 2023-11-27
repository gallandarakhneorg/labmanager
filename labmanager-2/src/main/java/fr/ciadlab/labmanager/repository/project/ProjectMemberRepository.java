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

package fr.ciadlab.labmanager.repository.project;

import java.util.List;

import fr.ciadlab.labmanager.entities.project.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

/** JPA repository for project members.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.0
 */
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Integer> {

	/** Replies the list of the project members that corresponds to the person with the given identifier.
	 *
	 * @param id the identifier of the person.
	 * @return the list of the project members.
	 * @since 3.2
	 */
	List<ProjectMember> findDistinctByPersonId(int id);

}
