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

import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;
import org.apache.jena.ext.com.google.common.base.Strings;

/** Abstract implementation of utilities for comparing organization names.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.2
 */
public abstract class AbstractOrganizationNameComparator implements OrganizationNameComparator {

	private double similaritylevel;

	@Override
	public double getSimilarityLevel() {
		return this.similaritylevel;
	}

	@Override
	public void setSimilarityLevel(double similarityLevel) {
		if (similarityLevel < 0.0) {
			this.similaritylevel = 0.0;
		} else if (similarityLevel > 1.0) {
			this.similaritylevel = 1.0;
		} else {
			this.similaritylevel = similarityLevel;
		}
	}

	/** Create an instance of a string similarity computer.
	 * This is a factory method.
	 *
	 * @return the string similarity computer.
	 */
	protected abstract NormalizedStringSimilarity createStringSimilarityComputer();

	@Override
	public double getSimilarity(String acronym1, String name1, String acronym2, String name2) {
		final NormalizedStringSimilarity similarityComputer = createStringSimilarityComputer();
		final double s1 = similarityComputer.similarity(acronym1, acronym2);
		final double s2 = similarityComputer.similarity(name1, name2);
		return Math.max(s1, s2);
	}

	/** Replies the similarity of the two strings.
	 *
	 * @param matcher the string similarity computer to be used.
	 * @param str1 the first string to compare.
	 * @param str2 the second string to compare.
	 * @return the level of similarity. {@code 0} means that the strings are not
	 *     similar, and {@code 1} means that they are totally equal.
	 */
	@SuppressWarnings("static-method")
	protected double getSimilarity(NormalizedStringSimilarity matcher, String str1, String str2) {
		if (Strings.isNullOrEmpty(str1) || Strings.isNullOrEmpty(str2)) {
			return 1.0;
		}
		return matcher.similarity(str1, str2);
	}

}