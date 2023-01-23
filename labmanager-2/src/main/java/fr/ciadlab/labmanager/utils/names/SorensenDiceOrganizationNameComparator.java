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

package fr.ciadlab.labmanager.utils.names;

import info.debatty.java.stringsimilarity.SorensenDice;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Utilities for comparing organization names using the Sorensen Dice algorithm.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.2
 */
@Component
@Primary
public class SorensenDiceOrganizationNameComparator extends AbstractOrganizationNameComparator {

	private static final double SIMILARITY_LEVEL = 0.7;

	/** Constructor.
	 */
	public SorensenDiceOrganizationNameComparator() {
		setSimilarityLevel(SIMILARITY_LEVEL);
	}

	@Override
	protected NormalizedStringSimilarity createStringSimilarityComputer() {
		return new SorensenDice();
	}

}