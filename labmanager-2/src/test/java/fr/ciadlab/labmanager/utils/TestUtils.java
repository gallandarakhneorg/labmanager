/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.utils;

import java.net.URL;
import java.net.URLConnection;

/** Utilities for tests.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class TestUtils {

	private static Boolean networkEnable;

	/** Replies if the network connection is enable or note.
	 *
	 * @return {@code true} if the network is turned on.
	 */
	public static boolean isNetworkEnable() {
		synchronized (TestUtils.class) {
			if (networkEnable == null) {
				networkEnable = Boolean.FALSE;
				try {
					final URL url = new URL("http://www.google.com");
					final URLConnection connection = url.openConnection();
					connection.connect();
					networkEnable = Boolean.TRUE;
				} catch (Exception ex) {
					//
				}
			}
			return networkEnable.booleanValue();
		}
	}

}
