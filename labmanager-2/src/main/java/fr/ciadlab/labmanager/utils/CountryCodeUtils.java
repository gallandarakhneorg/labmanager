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

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.common.base.Strings;
import org.arakhne.afc.util.CountryCode;
import org.springframework.context.i18n.LocaleContextHolder;

/** Utilities for country codes.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public final class CountryCodeUtils {

	/** Default country.
	 */
	public static final CountryCode DEFAULT = CountryCode.FRANCE;

	private static Set<CountryCode> EUROPEAN_COUNTRIES;

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
	 * The target language is the one provided by {@link LocaleContextHolder}.
	 *
	 * @param code the country code to use.
	 * @return the display name of the country.
	 */
	public static String getDisplayCountry(CountryCode code) {
		final Locale targetLanguage = LocaleContextHolder.getLocale();
		final Locale countryLocale = new Locale(code.getCode(), code.getCode());
		if (targetLanguage == null) {
			return countryLocale.getDisplayCountry();
		}
		return countryLocale.getDisplayCountry(targetLanguage);
	}

	/** Replies the display name of the language associated to the country.
	 * The target language is the one provided by {@link LocaleContextHolder}.
	 *
	 * @param code the country code to use.
	 * @return the display name of the country.
	 * @since 3.4
	 */
	public static String getDisplayLanguage(CountryCode code) {
		final Locale targetLanguage = LocaleContextHolder.getLocale();
		final Locale countryLocale = new Locale(code.getCode(), code.getCode());
		if (targetLanguage == null) {
			return countryLocale.getDisplayLanguage();
		}
		return countryLocale.getDisplayLanguage(targetLanguage);
	}

	/** Replies all the display names of the countries.
	 * The target language is the one provided by {@link LocaleContextHolder}.
	 *
	 * @return the display names of the countries.
	 */
	public static Map<CountryCode, String> getAllDisplayCountries() {
		final Map<CountryCode, String> labels = new TreeMap<>();
		for (final CountryCode code : CountryCode.values()) {
			labels.put(code, getDisplayCountry(code));
		}
		return labels;
	}

	/** Replies if the given code corresponds to France or one of its associated territories.
	 *
	 * @param code the code to test.
	 * @return {@code true} if the code corresponds to a French territory.
	 */
	public static boolean isFrance(CountryCode code) {
		if (code != null) {
			return code == CountryCode.FRANCE || code == CountryCode.FRENCH_GUIANA
					|| code == CountryCode.FRENCH_POLYNESIA || code == CountryCode.FRENCH_SOUTHERN_TERRITORIES
					|| code == CountryCode.GUADELOUPE || code == CountryCode.MARTINIQUE
					|| code == CountryCode.MAYOTTE || code == CountryCode.NEW_CALEDONIA
					|| code == CountryCode.REUNION || code == CountryCode.SAINT_MARTIN_FRENCH_SIDE
					|| code == CountryCode.SAINT_PIERRE_AND_MIQUELON || code == CountryCode.WALLIS_AND_FUTUNA;
		}
		return false;
	}

	/** Replies if the given code corresponds to an european country or one of its associated territories.
	 * A country is european when it is located on the European continent.
	 *
	 * @param code the code to test.
	 * @return {@code true} if the code corresponds to a French territory.
	 */
	public static boolean isEuropean(CountryCode code) {
		if (code != null) {
			final Set<CountryCode> countries;
			synchronized (CountryCodeUtils.class) {
				if (EUROPEAN_COUNTRIES == null) {
					EUROPEAN_COUNTRIES = new HashSet<>();
					EUROPEAN_COUNTRIES.add(CountryCode.ALBANIA);
					EUROPEAN_COUNTRIES.add(CountryCode.ANDORRA);
					EUROPEAN_COUNTRIES.add(CountryCode.AUSTRIA);
					EUROPEAN_COUNTRIES.add(CountryCode.BELARUS);
					EUROPEAN_COUNTRIES.add(CountryCode.BELGIUM);
					EUROPEAN_COUNTRIES.add(CountryCode.BOSNIA_AND_HERZEGOVINA);
					EUROPEAN_COUNTRIES.add(CountryCode.BULGARIA);
					EUROPEAN_COUNTRIES.add(CountryCode.CROATIA);
					EUROPEAN_COUNTRIES.add(CountryCode.CYPRUS);
					EUROPEAN_COUNTRIES.add(CountryCode.CZECH_REPUBLIC);
					EUROPEAN_COUNTRIES.add(CountryCode.DENMARK);
					EUROPEAN_COUNTRIES.add(CountryCode.ESTONIA);
					EUROPEAN_COUNTRIES.add(CountryCode.FINLAND);
					EUROPEAN_COUNTRIES.add(CountryCode.FRANCE);
					EUROPEAN_COUNTRIES.add(CountryCode.FRENCH_GUIANA);
					EUROPEAN_COUNTRIES.add(CountryCode.FRENCH_POLYNESIA);
					EUROPEAN_COUNTRIES.add(CountryCode.FRENCH_SOUTHERN_TERRITORIES);
					EUROPEAN_COUNTRIES.add(CountryCode.GERMANY);
					EUROPEAN_COUNTRIES.add(CountryCode.GIBRALTAR);
					EUROPEAN_COUNTRIES.add(CountryCode.GREECE);
					EUROPEAN_COUNTRIES.add(CountryCode.GUERNSEY);
					EUROPEAN_COUNTRIES.add(CountryCode.HUNGARY);
					EUROPEAN_COUNTRIES.add(CountryCode.ICELAND);
					EUROPEAN_COUNTRIES.add(CountryCode.IRELAND);
					EUROPEAN_COUNTRIES.add(CountryCode.ISLE_OF_MAN);
					EUROPEAN_COUNTRIES.add(CountryCode.ITALY);
					EUROPEAN_COUNTRIES.add(CountryCode.JERSEY);
					EUROPEAN_COUNTRIES.add(CountryCode.LATVIA);
					EUROPEAN_COUNTRIES.add(CountryCode.LIECHTENSTEIN);
					EUROPEAN_COUNTRIES.add(CountryCode.LITHUANIA);
					EUROPEAN_COUNTRIES.add(CountryCode.LUXEMBOURG);
					EUROPEAN_COUNTRIES.add(CountryCode.MACEDONIA);
					EUROPEAN_COUNTRIES.add(CountryCode.MALTA);
					EUROPEAN_COUNTRIES.add(CountryCode.MARTINIQUE);
					EUROPEAN_COUNTRIES.add(CountryCode.MAYOTTE);
					EUROPEAN_COUNTRIES.add(CountryCode.MOLDOVA);
					EUROPEAN_COUNTRIES.add(CountryCode.MONACO);
					EUROPEAN_COUNTRIES.add(CountryCode.MONTENEGRO);
					EUROPEAN_COUNTRIES.add(CountryCode.NETHERLANDS);
					EUROPEAN_COUNTRIES.add(CountryCode.NEW_CALEDONIA);
					EUROPEAN_COUNTRIES.add(CountryCode.NORWAY);
					EUROPEAN_COUNTRIES.add(CountryCode.POLAND);
					EUROPEAN_COUNTRIES.add(CountryCode.PORTUGAL);
					EUROPEAN_COUNTRIES.add(CountryCode.REUNION);
					EUROPEAN_COUNTRIES.add(CountryCode.ROMANIA);
					EUROPEAN_COUNTRIES.add(CountryCode.ROMANIA);
					EUROPEAN_COUNTRIES.add(CountryCode.SAINT_MARTIN_FRENCH_SIDE);
					EUROPEAN_COUNTRIES.add(CountryCode.SAINT_PIERRE_AND_MIQUELON);
					EUROPEAN_COUNTRIES.add(CountryCode.SAN_MARINO);
					EUROPEAN_COUNTRIES.add(CountryCode.SERBIA);
					EUROPEAN_COUNTRIES.add(CountryCode.SLOVAKIA);
					EUROPEAN_COUNTRIES.add(CountryCode.SLOVENIA);
					EUROPEAN_COUNTRIES.add(CountryCode.SPAIN);
					EUROPEAN_COUNTRIES.add(CountryCode.SWEDEN);
					EUROPEAN_COUNTRIES.add(CountryCode.SWITZERLAND);
					EUROPEAN_COUNTRIES.add(CountryCode.UKRAINE);
					EUROPEAN_COUNTRIES.add(CountryCode.UNITED_KINGDOM);
					EUROPEAN_COUNTRIES.add(CountryCode.VATICAN_CITY_STATE);
					EUROPEAN_COUNTRIES.add(CountryCode.WALLIS_AND_FUTUNA);
				}
				countries = EUROPEAN_COUNTRIES;
			}
			return countries.contains(code);
		}
		return false;
	}

}
