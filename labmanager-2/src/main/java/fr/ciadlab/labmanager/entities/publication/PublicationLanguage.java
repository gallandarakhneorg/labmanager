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

import java.util.Locale;

import com.google.common.base.Strings;

/** Name of the language in which a research publication is published.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0
 */
public enum PublicationLanguage {
	/** Publication is mostly written in English.
	 */
	ENGLISH {
		@Override
		public Locale getLocale() {
			return Locale.US;
		}
	},
	/** Publication is mostly written in French.
	 */
	FRENCH {
		@Override
		public Locale getLocale() {
			return Locale.FRENCH;
		}
	},
	/** Publication is mostly written in German.
	 */
	GERMAN {
		@Override
		public Locale getLocale() {
			return Locale.GERMAN;
		}
	},
	/** Publication is mostly written in Italian.
	 */
	ITALIAN {
		@Override
		public Locale getLocale() {
			return Locale.ITALIAN;
		}
	},
	/** Publication is mostly written in a language that is not specified.
	 */
	OTHER {
		@Override
		public Locale getLocale() {
			assert getDefault() != this;
			return getDefault().getLocale();
		}
		@Override
		public String getLabel() {
			return "?"; //$NON-NLS-1$
		}
	};

	/** Replies the default publication language.
	 *
	 * @return the default publication language, i.e. English.
	 */
	public static PublicationLanguage getDefault() {
		return ENGLISH;
	}

	/** Replies the publication language that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the language, to search for.
	 * @return the type, or the value replied by {@link #getDefault()} if the name does not corresponds to
	 *     a known language.
	 */
	public static PublicationLanguage valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final PublicationLanguage lang : values()) {
				if (name.equalsIgnoreCase(lang.name())) {
					return lang;
				}
			}
		}
		return getDefault();
	}

	/** Replies the Java locale that corresponds to the publication language.
	 *
	 * @return the locale.
	 */
	public abstract Locale getLocale();

	/** Replies the label of the current language.
	 *
	 * @return the label of the current language.
	 */
	public String getLabel() {
		return getLocale().getDisplayLanguage();
	}

}
