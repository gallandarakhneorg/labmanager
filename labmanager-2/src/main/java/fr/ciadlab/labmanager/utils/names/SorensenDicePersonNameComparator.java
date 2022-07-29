/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.utils.names;

import info.debatty.java.stringsimilarity.SorensenDice;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Utilities for comparing person names using the Sorensen Dice algorithm.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Component
@Primary
public class SorensenDicePersonNameComparator extends AbstractPersonNameComparator {

	/** Constructor.
	 *
	 * @param nameParser the parser for persons' names.
	 */
	public SorensenDicePersonNameComparator(@Autowired PersonNameParser nameParser) {
		super(nameParser);
	}

	@Override
	protected NormalizedStringSimilarity createStringSimilarityComputer() {
		return new SorensenDice();
	}

}