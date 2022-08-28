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

package fr.ciadlab.labmanager.controller.view;

import java.io.File;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.AbstractComponent;
import fr.ciadlab.labmanager.configuration.Constants;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

/** Abstract implementation of a JEE Controller that provides views.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractViewController extends AbstractComponent {

	/** Factory of URI builder.
	 */
	protected final UriBuilderFactory uriBuilderFacory = new DefaultUriBuilderFactory();
	
	private Constants constants;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 */
	public AbstractViewController(MessageSourceAccessor messages, Constants constants) {
		super(messages);
		this.constants = constants;
	}

	/** Build the URL for accessing an endpoint with the given parameter name, but without setting the parameter value. 
	 *
	 * @param endpointName the name of the endpoint.
	 * @param parameterName the parameter name.
	 * @return the endpoint URL.
	 */
	protected String endpoint(String endpointName, String parameterName) {
		final UriBuilder b = this.uriBuilderFacory.builder();
		b.path("/" + this.constants.getServerName() + "/" + endpointName); //$NON-NLS-1$ //$NON-NLS-2$
		if (!Strings.isNullOrEmpty(parameterName)) {
			b.queryParam(parameterName, ""); //$NON-NLS-1$
		}
		return b.build().toASCIIString();
	}

	/** Build the URL from the root of the JPA server. 
	 *
	 * @param relativeFile the relative path to append to the server's root.
	 * @return the rooted URL.
	 */
	protected String rooted(File relativeFile) {
		final StringBuilder bb = new StringBuilder();
		File f = relativeFile;
		while (f != null) {
			bb.insert(0, f.getName()).insert(0, "/"); //$NON-NLS-1$
			f = f.getParentFile();
		}
		final UriBuilder b = this.uriBuilderFacory.builder();
		b.path("/" + this.constants.getServerName() + bb.toString()); //$NON-NLS-1$
		return b.build().toASCIIString();
	}

	/** Build the URL from the root of the JPA server. 
	 *
	 * @param relativeUrl the relative URL to append to the server's root.
	 * @return the rooted URL.
	 */
	protected String rooted(String relativeUrl) {
		final UriBuilder b = this.uriBuilderFacory.builder();
		b.path("/" + this.constants.getServerName() + "/" + relativeUrl); //$NON-NLS-1$ //$NON-NLS-2$
		return b.build().toASCIIString();
	}

	/** Fill the attributes of the given model-view with the standard properties.
	 * The added object in the model are: {@code uuid} and {@code changeEnabled}.
	 *
	 * @param modelAndView the model-view
	 * @param username the login of the logged-in person.
	 */
	protected void initModelViewProperties(ModelAndView modelAndView, String username) {
		modelAndView.addObject("uuid", generateUUID()); //$NON-NLS-1$
		modelAndView.addObject("changeEnabled", isLoggedUser(username)); //$NON-NLS-1$
	}

	/** Fill the attributes that are needed to build up the buttons in an admin table.
	 *
	 * @param modelAndView the model-view
	 * @param editUrl the URL for editing an entity. The id of the entity will be appended to the given URL.
	 */
	protected void initAdminTableButtons(ModelAndView modelAndView, String editUrl) {
		modelAndView.addObject("URLS_edit", editUrl); //$NON-NLS-1$
		modelAndView.addObject("URLS_exportBibTeX", endpoint(Constants.EXPORT_BIBTEX_ENDPOINT, null)); //$NON-NLS-1$
		modelAndView.addObject("URLS_exportOdt", endpoint(Constants.EXPORT_ODT_ENDPOINT, null)); //$NON-NLS-1$
		modelAndView.addObject("URLS_exportHtml", endpoint(Constants.EXPORT_HTML_ENDPOINT, null)); //$NON-NLS-1$
		modelAndView.addObject("URLS_exportJson", endpoint(Constants.EXPORT_JSON_ENDPOINT, null)); //$NON-NLS-1$
	}

}
