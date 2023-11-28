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

package fr.ciadlab.labmanager.utils;

import java.io.Serializable;

/** API error.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2.0
 */
public class ApiError implements Serializable {

	private static final long serialVersionUID = 3392972706564054457L;

	private final int code;

	private final String reason;

	/** Constructor.
	 *
	 * @param code the HTTP code of the error.
	 * @param reason the reseaon.
	 */
	public ApiError(int code, String reason) {
		this.code = code;
		this.reason = reason;
	}

	/** Replies the error code.
	 *
	 * @return the code.
	 */
	public int getCode() {
		return this.code;
	}

	/** Replies the reason of the error.
	 *
	 * @return the error message.
	 */
	public String getReason() {
		return this.reason;
	}

}
