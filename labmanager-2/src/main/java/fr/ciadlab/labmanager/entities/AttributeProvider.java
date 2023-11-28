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
