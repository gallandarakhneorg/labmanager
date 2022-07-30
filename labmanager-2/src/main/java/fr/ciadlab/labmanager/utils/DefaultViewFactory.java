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
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.utils;

import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.RedirectView;

/** Factory of views.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Component
@Primary
public class DefaultViewFactory implements ViewFactory {

	/** Create a redirection view.
	 *
	 * @param url the URL to redirect to.
	 * @param contextRelative whether to interpret the given URL as relative to the current ServletContext.
	 * @return the view.
	 */
	@Override
	public RedirectView newRedirectView(String url, boolean contextRelative) {
		assert !Strings.isNullOrEmpty(url);
		return new RedirectView(url, contextRelative);
	}

}
