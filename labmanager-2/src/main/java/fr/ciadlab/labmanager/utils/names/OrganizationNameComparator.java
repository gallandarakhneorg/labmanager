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

/** Utilities for comparing organization names.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.2
 */
public interface OrganizationNameComparator {

	/** Compute and replies the similarity between the acronyms and names of two organizations.
	 *
	 * 
	 * @param acronym1 the first acronym.
	 * @param name1 the first name.
	 * @param acronym2 the second acronym.
	 * @param name2 the second name.
	 * @return the level of similarity. {@code 0} means that the names are not
	 *     similar, and {@code 1} means that they are totally equal.
	 */
	double getSimilarity(String acronym1, String name1, String acronym2, String name2);

	/** Replies the similarity level to consider for assuming that two names are similar.
	 *
	 * @return the minimum level of similarity. {@code 0} means that the names are not
	 *     similar, and {@code 1} means that they are totally equal.
	 */
	double getSimilarityLevel();

	/** Change the similarity level to consider for assuming that two names are similar.
	 *
	 * @param similarityLevel the minimum level of similarity. {@code 0} means that the names are not
	 *     similar, and {@code 1} means that they are totally equal.
	 */
	void setSimilarityLevel(double similarityLevel);

	/** Check name similarity between the names of two organizations.
	 * 
	 * @param acronym1 the first acronym.
	 * @param name1 the first name.
	 * @param acronym2 the second acronym.
	 * @param name2 the second name.
	 * @return {@code true} if the two given names are similar.
	 */
	default boolean isSimilar(String acronym1, String name1, String acronym2, String name2) {
		return getSimilarity(acronym1, name1, acronym2, name2) >= getSimilarityLevel();
	}

}
