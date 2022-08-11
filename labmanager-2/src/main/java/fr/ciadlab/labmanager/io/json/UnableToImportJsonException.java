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

package fr.ciadlab.labmanager.io.json;

import com.google.gson.Gson;

/** Exception thrown when it is impossible to import a JSON.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public class UnableToImportJsonException extends Exception {

	private static final long serialVersionUID = 8962180458951007213L;

	/** Constructor.
	 *
	 * @param mainKey the name of the main key in the JSON for which the exception is thrown.
	 * @param elementIdx the index of the element in the main key for which the exception is thrown
	 * @param source the source object that corresponds to the data extract from the JSON.
	 * @param cause the original cause.
	 */
	public UnableToImportJsonException(String mainKey, int elementIdx, Object source, Throwable cause) {
		super("Unable to import JSON data in " + mainKey + "[" + elementIdx + "] = " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ (new Gson().toJson(source)), cause);
	}

}
