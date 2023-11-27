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

package fr.ciadlab.labmanager.io.json;

import java.util.List;

import fr.ciadlab.labmanager.entities.publication.Publication;

/** Provider of publication that could be used for completing a given publication.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public interface SimilarPublicationProvider {

	/** Replies the publications that corresponds to the given source. The replied publication is
	 * assumed to be different than the source. The similarity between the source publication and
	 * the replied publication depends on the implementation of the {@code SimilarPublicationProvider}.
	 *
	 * @param source the publication source.
	 * @return the similar publications.
	 */
	List<Publication> get(Publication source);

}
