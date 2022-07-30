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

import org.springframework.web.servlet.view.RedirectView;

/** Factory of views.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public interface ViewFactory {

	/** Create a redirection view.
	 *
	 * @param url the URL to redirect to.
	 * @param contextRelative whether to interpret the given URL as relative to the current ServletContext.
	 * @return the view.
	 */
	RedirectView newRedirectView(String url, boolean contextRelative);

}
