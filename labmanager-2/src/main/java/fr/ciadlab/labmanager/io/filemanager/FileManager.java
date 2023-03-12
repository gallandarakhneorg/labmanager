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

package fr.ciadlab.labmanager.io.filemanager;

import java.io.File;

/** Utilities for managing the files.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
public interface FileManager {

	/** JPEG filename extension.
	 */
	String JPEG_FILE_EXTENSION = ".jpg"; //$NON-NLS-1$

	/** Default root directoy name for the downloadable files.
	 */
	String DOWNLOADABLE_FOLDER_NAME = "Downloadables"; //$NON-NLS-1$

	/** Normalize a relative filename to be absolute for the server.
	 *
	 * @param file the relative filename.
	 * @return the absolute filename.
	 */
	File normalizeForServerSide(File file);

}
