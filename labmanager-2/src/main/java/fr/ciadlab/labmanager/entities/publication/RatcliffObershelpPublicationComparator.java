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

package fr.ciadlab.labmanager.entities.publication;

import info.debatty.java.stringsimilarity.RatcliffObershelp;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;
import org.springframework.stereotype.Component;

/** Comparator of publications. For comparison, the order of the publication is based on the
 * type of publication, the year, the authors, the identifier.
 * For similarity, the Ratcliff/Obershelp algorithm is used.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Component
public class RatcliffObershelpPublicationComparator extends AbstractPublicationComparator {

	private static final double SIMILARITY_LEVEL = 0.9;

	/** Constructor.
	 */
	public RatcliffObershelpPublicationComparator() {
		super(SIMILARITY_LEVEL);
	}

	@Override
	protected NormalizedStringSimilarity createSimilarityComputer() {
		return new RatcliffObershelp();
	}

}
