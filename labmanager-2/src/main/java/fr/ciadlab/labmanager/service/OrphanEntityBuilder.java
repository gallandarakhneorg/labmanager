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

package fr.ciadlab.labmanager.service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.arakhne.afc.progress.Progression;

/** Interface that represents a tool for computing the orphan entities.
 * 
 * @param <T> the type of the entities.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
public interface OrphanEntityBuilder<T> {

	/** Fill up the given JSON array with the list of the orphan entities.
	 * The content of the JSON nodes for each orphan entity depends on the implementation
	 * class of this interface.
	 *
	 * @param receiver the JSON node that receives the orphan entities.
	 * @param progress a progress indicator.
	 */
	void computeOrphans(ArrayNode receiver, Progression progress);

	/** Replies the reason why the given entity may be considered as orphan.
	 * An orphan entity is an entity that has no links to the existing
	 * other entities and that could be safely deleted from the system.
	 *
	 * @param entity the entity to check.
	 * @return the reason to be orphan; or {@code null} if the entity is not orphan.
	 */
	String getOrphanCriteria(T entity);

	/** Replies the name of the given entity.
	 *
	 * @param entity the entity to check.
	 * @return the name of the orphan entity.
	 */
	String getOrphanEntityLabel(T entity);

	/** Replies the name of the type of the orphan entities that is supported by this builder.
	 *
	 * @return the name of type of the orphan entities.
	 */
	String getOrphanTypeLabel();

}
