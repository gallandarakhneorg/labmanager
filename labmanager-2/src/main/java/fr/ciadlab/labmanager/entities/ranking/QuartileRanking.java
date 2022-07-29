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

package fr.ciadlab.labmanager.entities.ranking;

/** Quartile is one indicator for ranking scientific journals.
 * A quartile is the ranking of a journal or paper definite by any database based
 * on the impact factor (IF), citation, and indexing of that particular journal.
 * It can divide into four different quadrants starting with Q1, Q2, Q3, and Q4.
 * 
 * <p>Two major sources of ranking may be used:<ul>
 * <li>Scimajo Journal Ranking</li>
 * <li>Journal Citatio Reports of Web-of-Science</li>
 * </ul>
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @author $Author: lpascuzzi$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see "https://en.wikipedia.org/wiki/Journal_ranking"
 * @see "https://www.scimagojr.com/"
 * @see "https://jcr.clarivate.com/"
 */
public enum QuartileRanking {
	/** Fourth quartile.
	 */
	Q4,
	/** Third quartile.
	 */
	Q3,
	/** Second quartile.
	 */
	Q2,
	/** First quartile.
	 */
	Q1;
}
