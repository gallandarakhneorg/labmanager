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

package fr.ciadlab.labmanager.utils;

import java.util.Locale;

import com.google.common.base.Strings;
import org.arakhne.afc.util.CountryCode;

/** Utilities for country codes.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public final class CountryCodeUtils {

	private CountryCodeUtils() {
		//
	}

	/** Replies the country code that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the code, to search for.
	 * @return the code.
	 * @throws IllegalArgumentException if the given name does not corresponds to a type.
	 */
	public static CountryCode valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final CountryCode code : CountryCode.values()) {
				if (name.equalsIgnoreCase(code.name())) {
					return code;
				}
			}
		}
		throw new IllegalArgumentException("Invalid country code: " + name); //$NON-NLS-1$
	}


	/** Replies the display name of the country.
	 *
	 * @param code the country code to use.
	 * @return the display name of the country.
	 */
	public static String getDisplayCountry(CountryCode code) {
		return new Locale(code.getCode(), code.getCode()).getDisplayCountry();
	}

}
