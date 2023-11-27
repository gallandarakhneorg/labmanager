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

package fr.ciadlab.labmanager.utils.net;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/** Wrapper for the network connection that supports proxy configuration or not depending on the local configuration.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0
 */
public interface NetConnection {

	/** Read the image that it referenced by the given URL.
	 *
	 * @param url the url of the image to be read.
	 * @return the image.
	 * @throws IOException if the image cannot be read. The exception gives details on the reason of the failure.
	 */
	BufferedImage getImageFromURL(URL url) throws IOException;

}

