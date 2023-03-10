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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		super(buildMessage(mainKey, elementIdx, source), cause);
	}

	private static String buildMessage(String mainKey, int elementIdx, Object source) {
		final StringBuilder msg = new StringBuilder();
		msg.append("Unable to import JSON data in "); //$NON-NLS-1$
		msg.append(mainKey);
		msg.append("["); //$NON-NLS-1$
		msg.append(elementIdx);
		msg.append("]"); //$NON-NLS-1$
		try {
			final ObjectMapper mapper = JsonUtils.createMapper();
			String jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(source);
			msg.append(" = "); //$NON-NLS-1$
			msg.append(jsonResult);
		} catch (JsonProcessingException ex) {
			//
		}
		return msg.toString();
	}

}
