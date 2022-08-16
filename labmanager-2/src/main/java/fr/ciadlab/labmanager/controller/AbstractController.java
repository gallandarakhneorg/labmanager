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

package fr.ciadlab.labmanager.controller;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import fr.ciadlab.labmanager.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

/** Abstract implementation of a JEE Controller.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractController {

	private Logger logger;

	private final UriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();

	private final Random random = new Random();

	private final String majorEndpointName;

	/** Constructor.
	 *
	 * @param defaultEndpointName the name of the endpoint that should be used by default, e.g., for displaying success
	 *     messages.
	 */
	public AbstractController(String defaultEndpointName) {
		this.majorEndpointName = defaultEndpointName;
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
		msg.append(this.majorEndpointName);
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
		response.sendRedirect("/SpringRestHibernate/" + this.majorEndpointName + "?created=true&message=" //$NON-NLS-1$ //$NON-NLS-2$
				+ URLEncoder.encode(successReason, Charset.defaultCharset()));
	}

	/** Redirect the controller to the current tool page with the updated status.
	 *
	 * @param response the HTTP response.
	 * @param successReason the reason of the success to pass to the main tool.
	 * @throws Exception if the redirection has failed.
	 */
	protected void redirectUpdated(HttpServletResponse response, String successReason) throws Exception {
		response.sendRedirect("/SpringRestHibernate/" + this.majorEndpointName + "?updated=true&message=" //$NON-NLS-1$ //$NON-NLS-2$
				+ URLEncoder.encode(successReason, Charset.defaultCharset()));
	}

	/** Redirect the controller to the current tool page with the deletion status.
	 *
	 * @param response the HTTP response.
	 * @param successReason the reason of the success to pass to the main tool.
	 * @throws Exception if the redirection has failed.
	 */
	protected void redirectDeleted(HttpServletResponse response, String successReason) throws Exception {
		response.sendRedirect("/SpringRestHibernate/" + this.majorEndpointName + "?deleted=true&message=" //$NON-NLS-1$ //$NON-NLS-2$
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
		response.sendRedirect("/SpringRestHibernate/" + this.majorEndpointName + "?error=1&message=" //$NON-NLS-1$ //$NON-NLS-2$
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
		response.sendRedirect("/SpringRestHibernate/" + this.majorEndpointName + "?error=1&message=" //$NON-NLS-1$ //$NON-NLS-2$
				+ URLEncoder.encode(errorMessage, Charset.defaultCharset()));
	}

	/** Generate an UUID.
	 *
	 * @return the UUID.
	 */
	protected Integer generateUUID() {
		return Integer.valueOf(Math.abs(this.random.nextInt()));
	}

	/** Add the URL to model that permits to retrieve the publication list.
	 *
	 * @param modelAndView the model-view to configure for redirection.
	 * @param organization the identifier of the organization for which the publications must be exported.
	 * @param author the identifier of the author for who the publications must be exported.
	 * @param journal the identifier of the journal for which the publications must be exported.
	 */
	protected void addUrlToPublicationListEndPoint(ModelAndView modelAndView, Integer organization, Integer author,
			Integer journal) {
		final StringBuilder path = new StringBuilder();
		path.append("/").append(Constants.DEFAULT_SERVER_NAME).append("/").append(Constants.EXPORT_JSON_ENDPOINT); //$NON-NLS-1$ //$NON-NLS-2$
		UriBuilder uriBuilder = this.uriBuilderFactory.builder();
		uriBuilder = uriBuilder.path(path.toString());
		uriBuilder = uriBuilder.queryParam(Constants.FORAJAX_ENDPOINT_PARAMETER, Boolean.TRUE);
		if (organization != null && organization.intValue() != 0) {
			uriBuilder = uriBuilder.queryParam(Constants.ORGANIZATION_ENDPOINT_PARAMETER, organization);
		}
		if (author != null && author.intValue() != 0) {
			uriBuilder = uriBuilder.queryParam(Constants.AUTHOR_ENDPOINT_PARAMETER, author);
		}
		if (journal != null && journal.intValue() != 0) {
			uriBuilder = uriBuilder.queryParam(Constants.JOURNAL_ENDPOINT_PARAMETER, journal);
		}
		final String url = uriBuilder.build().toString();
		modelAndView.addObject("url", url); //$NON-NLS-1$
	}

}
