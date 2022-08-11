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

package fr.ciadlab.labmanager.io.bibtex;

import org.apache.jena.ext.com.google.common.base.Strings;

/** Abstract implementation of the utilities for BibTeX.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public abstract class AbstractBibTeX implements BibTeX {

	/** Field {@code abstract}.
	 */
	protected static final String KEY_ABSTRACT_NAME = "abstract"; //$NON-NLS-1$

	/** Field {@code keywords}.
	 */
	protected static final String KEY_KEYWORDS_NAME = "keywords"; //$NON-NLS-1$

	/** Field {@code isbn}.
	 */
	protected static final String KEY_ISBN_NAME = "isbn"; //$NON-NLS-1$

	/** Field {@code issn}.
	 */
	protected static final String KEY_ISSN_NAME = "issn"; //$NON-NLS-1$

	/** Field {@code halid}.
	 */
	protected static final String KEY_HALID_NAME = "halid"; //$NON-NLS-1$

	/** Field {@code dblp}.
	 */
	protected static final String KEY_DBLP_NAME = "dblp"; //$NON-NLS-1$

	/** Field {@code video}.
	 */
	protected static final String KEY_VIDEO_NAME = "video"; //$NON-NLS-1$

	/** Field {@code language}.
	 */
	protected static final String KEY_LANGUAGE_NAME = "language"; //$NON-NLS-1$

	/** Add curly-braces around the upper-case words of the given text.
	 * This feature is usually applied in the titles of the BibTeX entries in
	 * order to avoid BibTeX tools to change the case of the words in the titles
	 * when it is rendered on a final document.
	 *
	 * @param text the text to change.
	 * @return the text with protected upper-case words.
	 */
	@SuppressWarnings("static-method")
	protected String protectAcronymsInText(String text) {
		if (!Strings.isNullOrEmpty(text)) {
			// We consider a word as an acronym when it is full capitalized with a minimum length of 2
			// and followed by a potential lower case 's'

			// Regex for acronyms in the middle of a sentence
			final String acronymRegex = "([^A-Za-z0-9{}])([A-Z0-9][A-Z0-9]+s?)([^A-Za-z0-9{}])"; //$NON-NLS-1$
			// Regex for an acronym as the first word in the sentence
			final String firstWordAcronymRegex = "^([A-Z0-9][A-Z0-9]+s?)([^A-Za-z0-9])"; //$NON-NLS-1$
			// Regex for an acronym as the last word in the sentence
			final String lastWordAcronymRegex = "([^A-Za-z0-9])([A-Z0-9][A-Z0-9]+s?)$"; //$NON-NLS-1$

			// We add braces to the acronyms that we found
			final String titleEncaps = text.replaceAll(acronymRegex, "$1{$2}$3") //$NON-NLS-1$
					.replaceAll(firstWordAcronymRegex, "{$1}$2") //$NON-NLS-1$
					.replaceAll(lastWordAcronymRegex, "$1{$2}"); //$NON-NLS-1$

			return titleEncaps;
		}
		return null;
	}

}
