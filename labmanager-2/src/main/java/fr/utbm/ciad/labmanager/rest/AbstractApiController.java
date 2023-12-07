/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.utbm.ciad.labmanager.rest;

import fr.utbm.ciad.labmanager.components.AbstractCredentialController;
import fr.utbm.ciad.labmanager.configuration.Constants;
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
