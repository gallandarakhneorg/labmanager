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
 * you entered into with the CIAD.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.utils.ranking;

import com.google.common.base.Strings;

/** The CORE Conference Ranking provides assessments of major conferences in
 * the computing disciplines. The rankings are managed by the CORE Executive Committee, 
 * with periodic rounds for submission of requests for addition or reranking of conferences. 
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see "http://portal.core.edu.au/conf-ranks/"
 */
public enum CoreRanking {
	/** Rank D.
	 */
    D,
	/** Rank C.
	 */
    C,
	/** Rank B.
	 */
    B,
	/** Rank A.
	 */
    A,
	/** Rank A*.
	 */
    A_STAR {
    	@Override
        public String toString() {
            return A_STAR_STRING;
        }
    },
	/** Rank A**.
	 */
    A_STAR_STAR {
    	@Override
        public String toString() {
            return A_STAR_STAR_STRING;
        }
    };

	private static final String A_STAR_STAR_STRING = "A**"; //$NON-NLS-1$
	
	private static final String A_STAR_STRING = "A*"; //$NON-NLS-1$

	/** Parse the given string for obtaining the CORE ranking.
	 * 
	 * @param stringCoreRanking the string representation of the code ranking.
	 * @return the core raking or {@code null} if the given string cannot match.
	 */
    public static CoreRanking valueOfCaseInsensitive(String stringCoreRanking) {
    	if (!Strings.isNullOrEmpty(stringCoreRanking)) {
	    	for (final CoreRanking candidate : values()) {
	    		if (candidate.toString().equalsIgnoreCase(stringCoreRanking)) {
	    			return candidate;
	    		}
	    	}
    	}
    	return null;
    }

}
