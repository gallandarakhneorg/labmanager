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

import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import fr.ciadlab.labmanager.AbstractComponent;
import fr.ciadlab.labmanager.configuration.Constants;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Value;
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

	/** Hash for encoding the username.
	 */
	@Value("labmanager.security.username-hash")
	private String usernameHash;

	/** IV for encoding the username.
	 */
	@Value("labmanager.security.username-iv")
	private String usernameIv;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param usernameHash the hash string for encrypting the usernames.
	 * @param usernameIv the hash string for encrypting the usernames.
	 */
	public AbstractCredentialController(MessageSourceAccessor messages, Constants constants, String usernameHash, String usernameIv) {
		super(messages, constants);
		this.usernameHash = usernameHash;
		this.usernameIv = usernameIv;
	}

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
		return !Strings.isNullOrEmpty(this.username);
	}

	/** Validate the credentials for the given username.
	 * <p>This function always validates if the backend is in debug mode.
	 *
	 * @param username the encrypted username of the logged-in user.
	 * @param serviceName the name of the service that is accessed.
	 * @param serviceParams the significant parameters that are passed to the service. They are used for building the log messages.
	 * @return the uncrypted username.
	 * @throws IllegalAccessError if there is no logged user.
	 */
	protected String ensureCredentials(String username, String serviceName, Object... serviceParams) {
		final String uname = readCredentials(username, serviceName, serviceParams);
		if (!isLoggedIn()) {
			throw new IllegalAccessError(getMessage("all.notLogged")); //$NON-NLS-1$
		}
		return uname;
	}

	/** Read the credentials for the given username without validating them.
	 * <p>This function always validates if the backend is in debug mode.
	 *
	 * @param username the encrypted username of the logged-in user.
	 * @param serviceName the name of the service that is accessed.
	 * @param serviceParams the significant parameters that are passed to the service. They are used for building the log messages.
	 * @return the uncrypted username.
	 * @throws IllegalAccessError if there is no logged user.
	 */
	protected String readCredentials(String username, String serviceName, Object... serviceParams) {
		if (this.debugVersion) {
			this.username = Constants.DEVELOPER;
		} else {
			String decodedUsername = null;
			if (!Strings.isNullOrEmpty(username)) {
				try {
					decodedUsername = decode(username);
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
		if (!Strings.isNullOrEmpty(this.username)) {
			getLogger().info("Opening /" + serviceName + " by " + this.username + " for parameters: " + Arrays.toString(serviceParams)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} else {
			getLogger().info("Opening /" + serviceName + " without login with parameters: " + Arrays.toString(serviceParams)); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return this.username;
	}

	private String decode(String username) throws Exception {
		try {
			final String[] parts = username.split(":"); //$NON-NLS-1$
            final IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(parts[1]));
            final SecretKeySpec skeySpec = new SecretKeySpec(this.usernameHash.getBytes("UTF-8"), "AES"); //$NON-NLS-1$ //$NON-NLS-2$
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); //$NON-NLS-1$
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            final byte[] decodedEncryptedData = Base64.getDecoder().decode(parts[0]);
            final byte[] original = cipher.doFinal(decodedEncryptedData);
            return new String(original, "UTF-8"); //$NON-NLS-1$
		} catch (Throwable ex) {
			return null;
		}
	}

}
