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

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import fr.ciadlab.labmanager.AbstractComponent;
import fr.ciadlab.labmanager.configuration.Constants;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation of a JEE Controller that provides support for logged users.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractCredentialController extends AbstractComponent {

	/** Current username provided by {@link #ensureLoggedUser(HttpServletRequest)}.
	 *
	 * @see #ensureLoggedUser(HttpServletRequest)
	 */
	protected String username;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 */
	public AbstractCredentialController(MessageSourceAccessor messages, Constants constants) {
		super(messages, constants);
	}

	/** Replies if a user is logged-in.
	 * <p>This function always validates if the backend is in debug mode.
	 *
	 * @return {@code true} if a user is logged in.
	 */
	protected boolean isLoggedIn() {
		return this.debugVersion || !Strings.isNullOrEmpty(this.username);
	}

	/** Validate the credentials for the given username.
	 * <p>This function always validates if the backend is in debug mode.
	 *
	 * @param username the username of the logged-in user.
	 * @throws IllegalAccessError if there is no logged user.
	 */
	protected void ensureCredentials(String username) {
		readCredentials(username);
		if (!isLoggedIn()) {
			throw new IllegalAccessError(getMessage("all.notLogged")); //$NON-NLS-1$
		}
	}

	/** Read the credentials for the given username without validating them.
	 * <p>This function always validates if the backend is in debug mode.
	 *
	 * @param username the username of the logged-in user.
	 * @throws IllegalAccessError if there is no logged user.
	 */
	protected void readCredentials(String username) {
		String decodedUsername = null;
		if (!Strings.isNullOrEmpty(username)) {
			try {
				final byte[] bytes = Base64.getDecoder().decode(username);
				if (bytes != null && bytes.length > 0) {
					decodedUsername = new String(bytes);
				}
			} catch (Throwable ex) {
				decodedUsername = null;
			}
		}
		if (!Strings.isNullOrEmpty(decodedUsername) && !Constants.ANONYMOUS.equalsIgnoreCase(decodedUsername)) {
			this.username = decodedUsername;
		} else {
			this.username = null;
		}
	}

}
