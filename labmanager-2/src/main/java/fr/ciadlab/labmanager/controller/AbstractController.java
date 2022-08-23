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

import java.io.IOError;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import fr.ciadlab.labmanager.AbstractComponent;
import fr.ciadlab.labmanager.configuration.Constants;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.context.support.MessageSourceAccessor;
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
public abstract class AbstractController extends AbstractComponent {

	private final UriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 */
	public AbstractController(MessageSourceAccessor messages) {
		super(messages);
	}

	/** Fill the attributes of the given model-view with the standard properties.
	 *
	 * @param modelAndView the model-view
	 * @param username the login of the logged-in person.
	 * @param success flag that indicates the previous operation was a success.
	 * @param failure flag that indicates the previous operation was a failure.
	 * @param message the message that is associated to the state of the previous operation.
	 */
	protected void initModelViewProperties(ModelAndView modelAndView, String username, Boolean success, Boolean failure, String message) {
		initModelViewProperties(modelAndView, username);
		if (success != null && success.booleanValue()) {
			modelAndView.addObject("success", success); //$NON-NLS-1$
		}
		if (failure != null && failure.booleanValue()) {
			modelAndView.addObject("failure", failure); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(message)) {
			modelAndView.addObject("message", message); //$NON-NLS-1$
		}
	}

	/** Fill the attributes of the given model-view with the standard properties.
	 *
	 * @param modelAndView the model-view
	 * @param username the login of the logged-in person.
	 */
	protected void initModelViewProperties(ModelAndView modelAndView, String username) {
		modelAndView.addObject("uuid", generateUUID()); //$NON-NLS-1$
		modelAndView.addObject("changeEnabled", isLoggedUser(username)); //$NON-NLS-1$
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
		path.append("/").append(getApplicationConstants().getServerName()).append("/").append(Constants.EXPORT_JSON_ENDPOINT); //$NON-NLS-1$ //$NON-NLS-2$
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

	private void redirectToEndPoint(HttpServletResponse response, String endpoint, String message, String stateName) {
		UriBuilder builder = new DefaultUriBuilderFactory().builder();
		builder = builder.path("/" + getApplicationConstants().getServerName() + "/" + endpoint); //$NON-NLS-1$ //$NON-NLS-2$
		builder = builder.queryParam(stateName, Boolean.TRUE);
		if (!Strings.isNullOrEmpty(message)) {
			builder = builder.queryParam(Constants.MESSAGE_ENDPOINT_PARAMETER, message);
		}
		try {
			response.sendRedirect(builder.build().toString());
		} catch (IOException ex) {
			throw new IOError(ex);
		}
	}

	/** Redirect the controller to the given endpoint with a success state.
	 *
	 * @param response the HTTP response.
	 * @param endpoint the name of the endpoint.
	 * @param message additional message to provide to the target endpoint.
	 */
	protected void redirectSuccessToEndPoint(HttpServletResponse response, String endpoint, String message) {
		redirectToEndPoint(response, endpoint, message, Constants.SUCCESS_ENDPOINT_PARAMETER);
	}

	/** Redirect the controller to the given endpoint with a failure state.
	 *
	 * @param response the HTTP response.
	 * @param endpoint the name of the endpoint.
	 * @param message additional message to provide to the target endpoint.
	 */
	protected void redirectFailureToEndPoint(HttpServletResponse response, String endpoint, String message) {
		redirectToEndPoint(response, endpoint, message, Constants.FAILURE_ENDPOINT_PARAMETER);
	}

}
