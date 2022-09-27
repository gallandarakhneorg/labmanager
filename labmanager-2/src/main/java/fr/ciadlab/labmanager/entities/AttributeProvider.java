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

package fr.ciadlab.labmanager.entities;

import java.io.IOException;

/** Interface that represents an object that could provides attributes.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public interface AttributeProvider {

	/** Invoke the given consumer on each attribute of this object.
	 * This function is usually invoked by the functions that need to generate
	 * a map of attributes for this object.
	 * <p>Several attributes are not considered by this function. They
	 * are listed in the documentation of the implementation class.
	 *
	 * @param consumer the consumer of the publication's attributes
		 * @throws IOException on error.
	 */
	void forEachAttribute(AttributeConsumer consumer) throws IOException ;

	/** Interface that represents an object that could provides attributes.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 2.0.0
	 */
	public interface AttributeConsumer {

		/**
		 * Performs the operation on the given attribute.
		 *
		 * @param name the attribute name.
		 * @param value the attribute value.
		 * @throws IOException on error.
		 */
		void accept(String name, Object value) throws IOException;

	}

}
