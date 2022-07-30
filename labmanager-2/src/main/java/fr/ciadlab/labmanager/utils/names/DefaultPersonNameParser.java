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

import java.text.Normalizer;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Utilities for parsing person names with the format {@code FIRST LAST} or {@code LAST, FIRST}.
 * In the first format, the two components are separated by white spaces.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Component
@Primary
public class DefaultPersonNameParser implements PersonNameParser {

	private static final char FORMAT_2_SEPARATOR = ',';
	
	private static final char FORMAT_1_SEPARATOR = ' ';

	private static final String NAME_FORMAT_0 = "([^,]+?)(?:\\s*,\\s*([^,]+?))?\\s*,\\s*([^,]+?)"; //$NON-NLS-1$

	private static final Pattern NAME_PATTERN_0 = Pattern.compile(NAME_FORMAT_0);

	private static final String NAME_SEPARATOR_0 = "\\s+AND\\s+"; //$NON-NLS-1$

	private static final Pattern NAME_SEPARATOR_PATTERN_0 = Pattern.compile(NAME_SEPARATOR_0, Pattern.CASE_INSENSITIVE);

	private static final String AND_SEPARATED_FORMAT = NAME_FORMAT_0 + "(?:" + NAME_SEPARATOR_0 + NAME_FORMAT_0 + ")*"; //$NON-NLS-1$ //$NON-NLS-2$
	
	private static final Pattern AND_SEPARATED_PATTERN = Pattern.compile(AND_SEPARATED_FORMAT, Pattern.CASE_INSENSITIVE);

	private static final String NAME_FORMAT_1 = "([^,]+?)\\s+([^,]+?)"; //$NON-NLS-1$
	
	private static final Pattern NAME_PATTERN_1 = Pattern.compile(NAME_FORMAT_1);

	private static final String NAME_SEPARATOR_1 = "\\s*,\\s*"; //$NON-NLS-1$

	private static final Pattern NAME_SEPARATOR_PATTERN_1 = Pattern.compile(NAME_SEPARATOR_1);

	private static final String COMMA_SEPARATED_FORMAT = NAME_FORMAT_1 + "(?:" + NAME_SEPARATOR_1 + NAME_FORMAT_1 + ")*"; //$NON-NLS-1$ //$NON-NLS-2$

	private static final Pattern COMMA_SEPARATED_PATTERN = Pattern.compile(COMMA_SEPARATED_FORMAT);

	private static int extractNames(String text, NameCallback callback, Pattern nameSeparator, Pattern pattern, int fnIndex, int vonIndex, int lnIndex) {
		int nb = 0;
		final String[] names = nameSeparator.split(text);
		for (final String fn : names) {
			final Matcher n0 = pattern.matcher(fn);
			if (n0.matches()) {
				final String lastname = n0.group(lnIndex);
				final String von = vonIndex > 0 ? n0.group(vonIndex) : null;
				final String firstname = n0.group(fnIndex);
				if (!Strings.isNullOrEmpty(firstname) && !Strings.isNullOrEmpty(lastname)) {
					callback.name(firstname, Strings.emptyToNull(von), lastname, nb);
					++nb;
				}
			}
		}
		return nb;
	}
	
	@Override
	public int parseNames(String text, NameCallback callback) {
		assert callback != null;
		int nb = 0;
		if (!Strings.isNullOrEmpty(text)) {
			final Matcher m0 = AND_SEPARATED_PATTERN.matcher(text);
			if (m0.matches()) {
				nb = extractNames(text, callback, NAME_SEPARATOR_PATTERN_0, NAME_PATTERN_0, 3, 2, 1);
			} else {
				final Matcher m1 = COMMA_SEPARATED_PATTERN.matcher(text);
				if (m1.matches()) {
					nb = extractNames(text, callback, NAME_SEPARATOR_PATTERN_1, NAME_PATTERN_1, 1, 0, 2);
				} else {
					throw new IllegalArgumentException("Invalid format of the list of names: " + text); //$NON-NLS-1$
				}
			}
		}
		return nb;
	}

	@Override
	public String parseFirstName(String fullName) {
		final String fn = Strings.nullToEmpty(fullName).trim();
		if (!Strings.isNullOrEmpty(fn)) {
			int index = fn.indexOf(FORMAT_2_SEPARATOR);
			if (index >= 0) {
				final String n = fn.substring(index + 1).trim();
				return Strings.emptyToNull(n.trim());
			}
			index = fn.indexOf(FORMAT_1_SEPARATOR);
			if (index > 0) {
				final String n = fn.substring(0, index).trim();
				return Strings.emptyToNull(n.trim());
			}
			return Strings.emptyToNull(fn);
		}
		return null;
	}

	@Override
	public String parseLastName(String fullName) {
		final String fn = Strings.nullToEmpty(fullName).trim();
		if (!Strings.isNullOrEmpty(fn)) {
			int index = fn.indexOf(FORMAT_2_SEPARATOR);
			if (index >= 0) {
				final String n = fn.substring(0, index).trim();
				return Strings.emptyToNull(n.trim());
			}
			index = fn.indexOf(FORMAT_1_SEPARATOR);
			if (index > 0) {
				final String n = fn.substring(index + 1).trim();
				return Strings.emptyToNull(n.trim());
			}
		}
		return null;
	}
	
	@Override
	public String normalizeName(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			final String normalizedName0 = name.trim();
			if (!Strings.isNullOrEmpty(normalizedName0)) {
				// Remove dashes, e.g., "A-B" -> "A B"
				final String normalizedName1 = normalizedName0.replaceAll("\\s*\\-\\s*", " "); //$NON-NLS-1$ //$NON-NLS-2$
				// Remove the dots, e.g., "N." -> "N"
				final String normalizedName2 = normalizedName1.replaceAll("(\\S)\\.", "$1 "); //$NON-NLS-1$ //$NON-NLS-2$
				// Normalize white-spaces, e.g., "    " -> " "
				final String normalizedName3 = normalizedName2.replaceAll("\\s+", " "); //$NON-NLS-1$ //$NON-NLS-2$
				// Remove accents and special characters
				final String normalizedName4 = Normalizer.normalize(normalizedName3, Normalizer.Form.NFD)
						.replaceAll("[^\\p{ASCII}]", ""); //$NON-NLS-1$ //$NON-NLS-2$
				// Upper-case the name
				return normalizedName4.toUpperCase();
			}
		}
		return null;
	}

	@Override
	public boolean isShortName(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			final String normalizedName = normalizeName(name);
			if (!Strings.isNullOrEmpty(normalizedName)) {
				final String[] words = normalizedName.split("\\s+"); //$NON-NLS-1$
				if (words.length > 0) {
					for (final String word : words) {
						if (word.length() > 1) {
							return false;
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Set<String> getNormalizedNamesFor(String name, boolean enableShortNames) {
		Set<String> cases = new TreeSet<>();
		final String normalizedName = normalizeName(name);
		if (!Strings.isNullOrEmpty(normalizedName)) {
			// Generate the cases
			final String[] words = normalizedName.split("\\s+"); //$NON-NLS-1$
			if (words.length > 0) {
				cases.add(words[0]);
				if (enableShortNames) {
					cases.add(Character.toString(words[0].charAt(0)));
				}
				for (int i = 1; i < words.length; ++i) {
					cases = fillWithWords(cases, words[i], enableShortNames);
				}
			}
		}
		return cases;
	}

	private static Set<String> fillWithWords(Set<String> cases, String word, boolean enableShortNames) {
		assert !Strings.isNullOrEmpty(word);
		final Set<String> result = new TreeSet<>();
		for (final String candidate : cases) {
			result.add(candidate + " " + word); //$NON-NLS-1$
			if (enableShortNames) {
				result.add(candidate + " " + Character.toString(word.charAt(0))); //$NON-NLS-1$
			}
		}
		return result;
	}

}
