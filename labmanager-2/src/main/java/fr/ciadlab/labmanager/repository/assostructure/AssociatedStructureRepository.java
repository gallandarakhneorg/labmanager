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

package fr.ciadlab.labmanager.repository.assostructure;

import java.util.List;

import fr.ciadlab.labmanager.entities.assostructure.AssociatedStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** JPA repository for associated structure declaration.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.2
 */
public interface AssociatedStructureRepository extends JpaRepository<AssociatedStructure, Integer> {

	/** Replies all the associated structures that match the organization identifier, and the confidential
	 * flag.
	 *
	 * @param id the identifier for the organization.
	 * @param confidential indicates the expected confidentiality flag for the associated structures.
	 * @return the list of associated structures.
	 */
	@Query("SELECT DISTINCT s FROM AssociatedStructure s, AssociatedStructureHolder h WHERE s.fundingOrganization.id = :id OR ((h.organization.id = :id OR h.superOrganization.id = :id) AND h MEMBER OF s.holders) AND s.confidential = :confidential")
	List<AssociatedStructure> findDistinctOrganizationAssociatedStructures(
			@Param("confidential") Boolean confidential, @Param("id") Integer id);

	/** Replies all the associated structures that match the person identifier, and the confidential
	 * flag.
	 *
	 * @param confidential indicates the expected confidentiality flag for the associated structures.
	 * @param id the identifier for the person.
	 * @return the list of associated structures.
	 */
	@Query("SELECT DISTINCT s FROM AssociatedStructure s, AssociatedStructureHolder h WHERE h.person.id = :id AND h MEMBER OF s.holders AND s.confidential = :confidential")
	List<AssociatedStructure> findDistinctPersonAssociatedStructures(
			@Param("confidential") Boolean confidential, @Param("id") Integer id);

}
