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

package fr.ciadlab.labmanager.utils.names;

import java.util.Objects;
import java.util.Set;

import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;

/** Abstract implementation of utilities for comparing person names.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public abstract class AbstractPersonNameComparator implements PersonNameComparator {

	private static final double SIMILARITY_LEVEL = 0.9;

	private final PersonNameParser nameParser;

	private double similaritylevel = SIMILARITY_LEVEL;

	/** Constructor.
	 *
	 * @param nameParser the parser for persons' names.
	 */
	public AbstractPersonNameComparator(@Autowired PersonNameParser nameParser) {
		this.nameParser = nameParser;
	}

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

	@Override
	public double getSimilarity(String fullName1, String fullName2) {
		final String first1 = this.nameParser.parseFirstName(fullName1);
		final String last1 = this.nameParser.parseLastName(fullName1);
		final String first2 = this.nameParser.parseFirstName(fullName2);
		final String last2 = this.nameParser.parseLastName(fullName2);
		return getSimilarity(first1, last1, first2, last2);
	}

	@Override
	public double getSimilarity(String firstName1, String lastName1, String firstName2, String lastName2) {
		if (Strings.isNullOrEmpty(firstName1) || Strings.isNullOrEmpty(lastName1)
			|| Strings.isNullOrEmpty(firstName2) || Strings.isNullOrEmpty(lastName2)) {
			return 0.0;
		}
		if ((Objects.equals(firstName1, firstName2) && Objects.equals(lastName1, lastName2))
			|| (Objects.equals(firstName1, lastName2) && Objects.equals(lastName1, firstName2))) {
			return getSimilarityLevel();
		}
		boolean enableShortNames = this.nameParser.isShortName(firstName1)
			|| this.nameParser.isShortName(firstName2)
			|| this.nameParser.isShortName(lastName2);
		final Set<String> firsts1 = this.nameParser.getNormalizedNamesFor(firstName1, enableShortNames);
		final String last1 = this.nameParser.normalizeName(lastName1);
		final String first2 = this.nameParser.normalizeName(firstName2);
		final Set<String> firsts2 = this.nameParser.getNormalizedNamesFor(firstName2, enableShortNames);
		final String last2 = this.nameParser.normalizeName(lastName2);
		final Set<String> lasts2 = this.nameParser.getNormalizedNamesFor(lastName2, enableShortNames);
		return getSimilarity(firsts1, last1, first2, firsts2, last2, lasts2);
	}

	/** Create an instance of a string similarity computer.
	 * This is a factory method.
	 *
	 * @return the string similarity computer.
	 */
	protected abstract NormalizedStringSimilarity createStringSimilarityComputer();

	/** Replies the similarity of the two names.
	 * This function test if the two first names and last names are similar, or
	 * if the first first name is similar to the second last name, and the
	 * first last name is similar to the second first name.
	 *
	 * @param firstNames1 the set of first names for the first name.
	 * @param lastName1 the last names for the first name.
	 * @param firstName2 the first names for the second name.
	 * @param firstNames2 the set of first names for the second name.
	 * @param lastName2 the last names for the second name.
	 * @param lastNames2 the set of last names for the second name.
	 * @return the level of similarity. {@code 0} means that the names are not
	 *     similar, and {@code 1} means that they are totally equal.
	 */
	protected double getSimilarity(Set<String> firstNames1, String lastName1, String firstName2,
			Set<String> firstNames2, String lastName2, Set<String> lastNames2) {
		final NormalizedStringSimilarity similarityComputer = createStringSimilarityComputer();
		final double s0_0 = getSimilarity(similarityComputer, firstNames1, firstNames2);
		final double s0_1 = getSimilarity(similarityComputer, lastName1, lastName2);
		final double s0 = (s0_0 + s0_1) / 2.0;
		final double s1_0 = getSimilarity(similarityComputer, firstNames1, lastNames2);
		final double s1_1 = getSimilarity(similarityComputer, lastName1, firstName2);
		final double s1 = (s1_0 + s1_1) / 2.0;
		if (s1 > s0) {
			return s1;
		}
		return s0;
	}

	/** Replies if the two sets of names are similar.
	 *
	 * @param matcher the string similarity computer to be used.
	 * @param name1 the first name to compare.
	 * @param name2 the second name to compare.
	 * @return the level of similarity. {@code 0} means that the names are not
	 *     similar, and {@code 1} means that they are totally equal.
	 */
	@SuppressWarnings("static-method")
	protected double getSimilarity(NormalizedStringSimilarity matcher, Set<String> name1, Set<String> name2) {
		double max = 0.0;
		for (final String n0 : name1) {
			assert !Strings.isNullOrEmpty(n0);
			for (final String n1 : name2) {
				assert !Strings.isNullOrEmpty(n1);
				final double similarity = matcher.similarity(n0, n1);
				if (similarity >= max) {
					max = similarity;
				}
			}
		}
		return max;
	}

	/** Replies the similarity of the two names.
	 *
	 * @param matcher the string similarity computer to be used.
	 * @param name1 the first name to compare.
	 * @param name2 the second name to compare.
	 * @return the level of similarity. {@code 0} means that the names are not
	 *     similar, and {@code 1} means that they are totally equal.
	 */
	@SuppressWarnings("static-method")
	protected double getSimilarity(NormalizedStringSimilarity matcher, String name1, String name2) {
		if (Strings.isNullOrEmpty(name1) || Strings.isNullOrEmpty(name2)) {
			return 0.0;
		}
		return matcher.similarity(name1, name2);
	}

}