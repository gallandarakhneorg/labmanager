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

package fr.ciadlab.labmanager.repository.project;

import java.util.List;

import fr.ciadlab.labmanager.entities.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/** JPA repository for project declaration.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.0
 */
public interface ProjectRepository extends JpaRepository<Project, Integer> {

	/** Replies all the projects that mactch the different organization identifiers.
	 *
	 * @param coordinator the identifier for the coordinator.
	 * @param localOrganization the identifier for the local organization.
	 * @param superOrganization the identifier for the super organization.
	 * @param otherPartner the identifier in the other partners.
	 * @return the list of projects.
	 */
	List<Project> findAllDistinctByCoordinatorIdOrLocalOrganizationIdOrSuperOrganizationIdOrOtherPartnersId(
			Integer coordinator, Integer localOrganization, Integer superOrganization, Integer otherPartner);

	/** Replies all the projects in which the person with the given identifier is involved.
	 *
	 * @param id the identifier of the person.
	 * @return the list of projects.
	 */
	List<Project> findAllDistinctByParticipantsPersonId(int id);

}
