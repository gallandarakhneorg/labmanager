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

package fr.utbm.ciad.labmanager.utils.names;

import info.debatty.java.stringsimilarity.Jaccard;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Utilities for comparing person names using the Jaccar algorithm.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
@Component
public class JaccarPersonNameComparator extends AbstractPersonNameComparator {

	private static final double SIMILARITY_LEVEL = 0.65;

	/** Constructor.
	 *
	 * @param nameParser the parser for persons' names.
	 */
	public JaccarPersonNameComparator(@Autowired PersonNameParser nameParser) {
		super(nameParser);
		setSimilarityLevel(SIMILARITY_LEVEL);
	}

	@Override
	protected NormalizedStringSimilarity createStringSimilarityComputer() {
		return new Jaccard();
	}

}
