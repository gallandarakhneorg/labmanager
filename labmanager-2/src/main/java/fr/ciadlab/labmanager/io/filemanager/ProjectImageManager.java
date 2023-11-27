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

package fr.ciadlab.labmanager.io.filemanager;

import fr.ciadlab.labmanager.entities.project.Project;

/** Utilities for managing the project images.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
public interface ProjectImageManager extends FileManager {

	/** Replies the path to the thumbnail image for the given project.
	 * Depending on the implementation, the thumbnail image may be generated on the fly.
	 *
	 * @param project the project for which the thumbnail image must be generated.
	 * @return the path to the thumbnail image or {@code null} if there is no available thumbnail image.
	 */
	String getThumbnailPath(Project project);

}
