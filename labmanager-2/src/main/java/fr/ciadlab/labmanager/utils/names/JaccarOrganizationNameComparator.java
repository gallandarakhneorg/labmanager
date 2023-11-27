/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
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

package fr.ciadlab.labmanager.utils.names;

import info.debatty.java.stringsimilarity.Jaccard;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;
import org.springframework.stereotype.Component;

/** Utilities for comparing organization names using the Jaccar algorithm.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.2
 */
@Component
public class JaccarOrganizationNameComparator extends AbstractOrganizationNameComparator {

	private static final double SIMILARITY_LEVEL = 0.65;

	/** Constructor.
	 */
	public JaccarOrganizationNameComparator() {
		setSimilarityLevel(SIMILARITY_LEVEL);
	}

	@Override
	protected NormalizedStringSimilarity createStringSimilarityComputer() {
		return new Jaccard();
	}

}