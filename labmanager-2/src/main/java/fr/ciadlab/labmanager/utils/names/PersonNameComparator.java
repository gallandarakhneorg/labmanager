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

/** Utilities for comparing person names.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public interface PersonNameComparator {

	/** Compute and replies the similarity between the names of two persons.
	 *
	 * This function supports the short names, e.g., initial for the first name.
	 * For example, {@code Stephane Galland} is similar to {@code S Galland} and
	 * {@code S. Galland}. And, {@code Jean-Pierre Dupont} is similar to
	 * {@code J P Dupont}, {@code J-P Dupont}, {@code J. P. Dupont}, {@code J.-P. Dupont},
	 * {@code J. P Dupont}, {@code J.-P Dupont}, {@code J P. Dupont} and {@code J-P. Dupont}.
	 * 
	 * @param fullName1 the first full name.
	 * @param fullName2 the second full name.
	 * @return the level of similarity. {@code 0} means that the names are not
	 *     similar, and {@code 1} means that they are totally equal.
	 */
	double getSimilarity(String fullName1, String fullName2);

	/** Compute and replies the similarity between the names of two persons.
	 *
	 * This function supports the short names, e.g., initial for the first name.
	 * For example, {@code Stephane Galland} is similar to {@code S Galland} and
	 * {@code S. Galland}. And, {@code Jean-Pierre Dupont} is similar to
	 * {@code J P Dupont}, {@code J-P Dupont}, {@code J. P. Dupont}, {@code J.-P. Dupont},
	 * {@code J. P Dupont}, {@code J.-P Dupont}, {@code J P. Dupont} and {@code J-P. Dupont}.
	 * 
	 * @param firstName1 the first name of the first person.
	 * @param lastName1 the last name of the first person.
	 * @param firstName2 the first name of the second person.
	 * @param lastName2 the last name of the second person.
	 * @return the level of similarity. {@code 0} means that the names are not
	 *     similar, and {@code 1} means that they are totally equal.
	 */
	double getSimilarity(String firstName1, String lastName1, String firstName2, String lastName2);

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

	/** Check name similarity between the names of two persons.
	 *
	 * This function supports the short names, e.g., initial for the first name.
	 * For example, {@code Stephane Galland} is similar to {@code S Galland} and
	 * {@code S. Galland}. And, {@code Jean-Pierre Dupont} is similar to
	 * {@code J P Dupont}, {@code J-P Dupont}, {@code J. P. Dupont}, {@code J.-P. Dupont},
	 * {@code J. P Dupont}, {@code J.-P Dupont}, {@code J P. Dupont} and {@code J-P. Dupont}.
	 * 
	 * @param fullName1 the first full name.
	 * @param fullName2 the second full name.
	 * @return {@code true} if the two given names are similar.
	 */
	default boolean isSimilar(String fullName1, String fullName2) {
		return getSimilarity(fullName1, fullName2) >= getSimilarityLevel();
	}

	/** Check name similarity between the names of two persons.
	 *
	 * This function supports the short names, e.g., initial for the first name.
	 * For example, {@code Stephane Galland} is similar to {@code S Galland} and
	 * {@code S. Galland}. And, {@code Jean-Pierre Dupont} is similar to
	 * {@code J P Dupont}, {@code J-P Dupont}, {@code J. P. Dupont}, {@code J.-P. Dupont},
	 * {@code J. P Dupont}, {@code J.-P Dupont}, {@code J P. Dupont} and {@code J-P. Dupont}.
	 * 
	 * @param firstName1 the first name of the first person.
	 * @param lastName1 the last name of the first person.
	 * @param firstName2 the first name of the second person.
	 * @param lastName2 the last name of the second person.
	 * @return {@code true} if the two given names are similar.
	 */
	default boolean isSimilar(String firstName1, String lastName1, String firstName2, String lastName2) {
		return getSimilarity(firstName1, lastName1, firstName2, lastName2) >= getSimilarityLevel();
	}

}
