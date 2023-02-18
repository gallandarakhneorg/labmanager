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

package fr.ciadlab.labmanager.controller.api;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.AbstractCredentialController;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;

/** Abstract implementation of a JEE Controller that provides regular API.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractApiController extends AbstractCredentialController {

	/** Name of the MIME type for CSV files.
	 *
	 * @since 3.6
	 * @see #MIME_TYPE_CSV
	 */
	public static final String MIME_TYPE_CSV_NAME = "text/csv;charset=UTF-8"; //$NON-NLS-1$
	
	/** Name of the MIME type for CSV files.
	 *
	 * @since 3.6
	 * @see #MIME_TYPE_CSV_NAME
	 */
	public static final MediaType MIME_TYPE_CSV = MediaType.valueOf(MIME_TYPE_CSV_NAME);

	/** Name of the MIME type for Excel (Open XML, xlsx).
	 *
	 * @since 3.6
	 * @see #MIME_TYPE_XLSX
	 */
	public static final String MIME_TYPE_XLSX_NAME = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"; //$NON-NLS-1$
	
	/** Name of the MIME type for Excel (Open XML, xlsx).
	 *
	 * @since 3.6
	 * @see #MIME_TYPE_XLSX_NAME
	 */
	public static final MediaType MIME_TYPE_XLSX = MediaType.valueOf(MIME_TYPE_XLSX_NAME);

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public AbstractApiController(MessageSourceAccessor messages, Constants constants, String usernameKey) {
		super(messages, constants, usernameKey);
	}

}
