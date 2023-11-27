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

package fr.ciadlab.labmanager.utils.doi;

import java.net.URL;

/** Utilities for DOI number.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public interface DoiTools {

	/** Replies the DOI number that is specified inside the given DOI URL.
	 * A DOI URL is based on the standard {@link "https://doi.org"} to which
	 * the DOI number is added.
	 *
	 * @param url the DOI URL.
	 * @return the DOI number, never {@code null}.
	 */
	String getDOINumberFromDOIUrl(URL url);

	/** Replies the DOI number that is specified inside the given DOI URL.
	 * A DOI URL is based on the standard {@link "https://doi.org"} to which
	 * the DOI number is added.
	 *
	 * @param url the DOI URL.
	 * @return the DOI number, or {@code null} if the given URL does not corresponds to a DOI url.
	 * @since 3.6
	 */
	default String getDOINumberFromDOIUrlOrNull(URL url) {
		try {
			return getDOINumberFromDOIUrl(url);
		} catch (Throwable ex) {
			return null;
		}
	}

	/** Replies the DOI number that is specified inside the given DOI URL.
	 * A DOI URL is based on the standard {@link "https://doi.org"} to which
	 * the DOI number is added.
	 * If the input string is not a valid URL, the string is parsed a a DOI number.
	 *
	 * @param url the DOI URL.
	 * @return the DOI number, never {@code null}.
	 * @throws IllegalArgumentException if the given URL cannot be parsed.
	 */
	String getDOINumberFromDOIUrl(String url);

	/** Replies the DOI number that is specified inside the given DOI URL.
	 * A DOI URL is based on the standard {@link "https://doi.org"} to which
	 * the DOI number is added.
	 * If the input string is not a valid URL, the string is parsed a a DOI number.
	 *
	 * @param url the DOI URL.
	 * @return the DOI number, or {@code null} if the given URL does not corresponds to a DOI url.
	 * @since 3.6
	 */
	default String getDOINumberFromDOIUrlOrNull(String url) {
		try {
			return getDOINumberFromDOIUrl(url);
		} catch (Throwable ex) {
			return null;
		}
	}

	/** Replies the DOI URL from the given DOI number.
	 * A DOI URL is based on the standard {@link "https://doi.org"} to which
	 * the DOI number is added.
	 *
	 * @param number the DOI number.
	 * @return the DOI URL.
	 */
	URL getDOIUrlFromDOINumber(String number);

}
