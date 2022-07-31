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

package fr.ciadlab.labmanager.controller;

import java.net.URLEncoder;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Abstract implementation of a JEE Controller.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractController {

	/** Logger of the service. It is lazy loaded.
	 */
	private Logger logger;

	private final String majoutEndpointName;

	/** Constructor.
	 *
	 * @param defaultEndpointName the name of the endpoint that should be used by default, e.g., for displaying success
	 *     messages.
	 */
	public AbstractController(String defaultEndpointName) {
		this.majoutEndpointName = defaultEndpointName;
	}

	/** Replies the logger of this service.
	 *
	 * @return the logger.
	 */
	public Logger getLogger() {
		if (this.logger == null) {
			this.logger = createLogger();
		}
		return this.logger;
	}

	/** Change the logger of this service.
	 *
	 * @param logger the logger.
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/** Factory method for creating the service logger.
	 *
	 * @return the logger.
	 */
	protected Logger createLogger() {
		return LoggerFactory.getLogger(getClass());
	}

	/** Redirect the controller to the current tool page with a success.
	 *
	 * @param response the HTTP response.
	 * @param successReason the reason of the success to pass to the main tool.
	 * @throws Exception if the redirection has failed.
	 */
	protected void redirectSuccess(HttpServletResponse response, String successReason, String... additionalParameters) throws Exception {
		final StringBuilder msg = new StringBuilder();
		msg.append("/SpringRestHibernate/"); //$NON-NLS-1$
		msg.append(this.majoutEndpointName);
		msg.append("?success=true&message="); //$NON-NLS-1$
		msg.append(URLEncoder.encode(successReason, Charset.defaultCharset()));
		for (int i = 0; i < additionalParameters.length; i += 2) {
			msg.append("&"); //$NON-NLS-1$
			msg.append(additionalParameters[i]);
			msg.append("="); //$NON-NLS-1$
			msg.append(URLEncoder.encode(additionalParameters[i + 1], Charset.defaultCharset()));
		}
		response.sendRedirect(msg.toString());
	}

	/** Redirect the controller to the current tool page with a success.
	 *
	 * @param response the HTTP response.
	 * @param successReason the reason of the success to pass to the main tool.
	 * @throws Exception if the redirection has failed.
	 */
	protected void redirectCreated(HttpServletResponse response, String successReason) throws Exception {
		response.sendRedirect("/SpringRestHibernate/" + this.majoutEndpointName + "?created=true&message=" //$NON-NLS-1$ //$NON-NLS-2$
				+ URLEncoder.encode(successReason, Charset.defaultCharset()));
	}

	/** Redirect the controller to the current tool page with the updated status.
	 *
	 * @param response the HTTP response.
	 * @param successReason the reason of the success to pass to the main tool.
	 * @throws Exception if the redirection has failed.
	 */
	protected void redirectUpdated(HttpServletResponse response, String successReason) throws Exception {
		response.sendRedirect("/SpringRestHibernate/" + this.majoutEndpointName + "?updated=true&message=" //$NON-NLS-1$ //$NON-NLS-2$
				+ URLEncoder.encode(successReason, Charset.defaultCharset()));
	}

	/** Redirect the controller to the current tool page with the deletion status.
	 *
	 * @param response the HTTP response.
	 * @param successReason the reason of the success to pass to the main tool.
	 * @throws Exception if the redirection has failed.
	 */
	protected void redirectDeleted(HttpServletResponse response, String successReason) throws Exception {
		response.sendRedirect("/SpringRestHibernate/" + this.majoutEndpointName + "?deleted=true&message=" //$NON-NLS-1$ //$NON-NLS-2$
				+ URLEncoder.encode(successReason, Charset.defaultCharset()));
	}

	/** Redirect the controller to the current tool page with an error.
	 *
	 * @param response the HTTP response.
	 * @param exception the cause of the error.
	 * @throws Exception if the redirection has failed.
	 */
	protected void redirectError(HttpServletResponse response, Throwable exception) throws Exception {
		getLogger().error(exception.getLocalizedMessage(), exception);
		response.sendRedirect("/SpringRestHibernate/" + this.majoutEndpointName + "?error=1&message=" //$NON-NLS-1$ //$NON-NLS-2$
				+ URLEncoder.encode(exception.getLocalizedMessage(), Charset.defaultCharset()));
	}

	/** Redirect the controller to the current tool page with an error.
	 *
	 * @param response the HTTP response.
	 * @param errorMessage the cause of the error.
	 * @throws Exception if the redirection has failed.
	 */
	protected void redirectError(HttpServletResponse response, String errorMessage) throws Exception {
		getLogger().error(errorMessage);
		response.sendRedirect("/SpringRestHibernate/" + this.majoutEndpointName + "?error=1&message=" //$NON-NLS-1$ //$NON-NLS-2$
				+ URLEncoder.encode(errorMessage, Charset.defaultCharset()));
	}

}
