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

package fr.ciadlab.labmanager.repository.assostructure;

import java.util.List;

import fr.ciadlab.labmanager.entities.assostructure.AssociatedStructureHolder;
import org.springframework.data.jpa.repository.JpaRepository;

/** JPA repository for the declarations of the holders of associated structures.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.2
 */
public interface AssociatedStructureHolderRepository extends JpaRepository<AssociatedStructureHolder, Integer> {

	/** Replies the list of holders that corresponds to the persons with the give identifier.
	 *
	 * @param id the identifier of the person.
	 * @return the list of associate structures' holders.
	 */
	List<AssociatedStructureHolder> findDistinctByPersonId(int id);

}
