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

package fr.ciadlab.labmanager.entities.publication;

import java.util.Comparator;

/** Comparator of publications. The order of the publication depends on the implementation
 * of this interface.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PublicationComparator extends Comparator<Publication> {

	/** Compute and replies the similarity between the publications.
	 * 
	 * @param publication1 the first publication.
	 * @param publication2 the second publication.
	 * @return the level of similarity. {@code 0} means that the publications are not
	 *     similar, and {@code 1} means that they are totally equal.
	 */
	double getSimilarity(Publication publication1, Publication publication2);

	/** Replies the similarity level to consider for assuming that two publications are similar.
	 *
	 * @return the minimum level of similarity. {@code 0} means that the publication are not
	 *     similar, and {@code 1} means that they are totally equal.
	 */
	double getSimilarityLevel();

	/** Replies the similarity level to consider for assuming that two publications are similar.
	 *
	 * @param similarityLevel the minimum level of similarity. {@code 0} means that the publications are not
	 *     similar, and {@code 1} means that they are totally equal.
	 */
	void setSimilarityLevel(double similarityLevel);

	/** Check publication similarity between the publications.
	 * 
	 * @param publication1 the first publication.
	 * @param publication2 the second publication.
	 * @return {@code true} if the two given publications are similar.
	 */
	default boolean isSimilar(Publication publication1, Publication publication2) {
		final double similarity = getSimilarity(publication1, publication2);
		return similarity > getSimilarityLevel();
	}

}
