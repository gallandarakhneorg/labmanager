/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.utbm.ciad.labmanager.utils.country;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Strings;
import org.eclipse.xtext.xbase.lib.Pure;
import org.springframework.context.i18n.LocaleContextHolder;

/** This enumeration gives a list of countries with the official codes of the countries
 * given by the ISO 3166-1, and the phone prefix information that is provided by
 * International Telecommunication Union (ITU) and defined in the ITU-T ISO standards E.123 and E.164.
 *
 * @author <a target="_blank" href="http://www.arakhne.org/homes/galland.html">St&eacute;phane GALLAND</a>
 * @version 17.0 2020-01-04 14:51:19
 * @mavengroupid org.arakhne.afc.core
 * @mavenartifactid util
 * @since 14.0
 */
public enum CountryCode {

	/** AF.
	 */
	AFGHANISTAN("af", 93, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** AX.
	 */
	ALAND_ISLANDS("ax", 358, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return FINLAND;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** AL.
	 */
	ALBANIA("al", 355, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** DZ.
	 */
	ALGERIA("dz", 213, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** AS.
	 */
	AMERICAN_SAMOA("as", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_STATES;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** AD.
	 */
	ANDORRA("ad", 376, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** AO.
	 */
	ANGOLA("ao", 244, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** AI.
	 */
	ANGUILLA("ai", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** AQ.
	 */
	ANTARCTICA("aq", 0, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** AG.
	 */
	ANTIGUA_AND_BARBUDA("ag", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** AR.
	 */
	ARGENTINA("ar", 54, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** AM.
	 */
	ARMENIA("am", 374, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** AW.
	 */
	ARUBA("aw", 297, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return NETHERLANDS;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** AU.
	 */
	AUSTRALIA("au", 61, "0011", null) { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** AT.
	 */
	AUSTRIA("at", 43, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** AZ.
	 */
	AZERBAIJAN("az", 994, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},

	/** BS.
	 */
	BAHAMAS("bs", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** BH.
	 */
	BAHRAIN("bh", 973, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** BD.
	 */
	BANGLADESH("bd", 880, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** BB.
	 */
	BARBADOS("bb", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** BY.
	 */
	BELARUS("by", 375, "810", "8") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** BE.
	 */
	BELGIUM("be", 32, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** BZ.
	 */
	BELIZE("bz", 501, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** BJ.
	 */
	BENIN("bj", 229, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** BM.
	 */
	BERMUDA("bm", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** BT.
	 */
	BHUTAN("BT", 975, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** BO.
	 */
	BOLIVIA("bo", 591, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** BQ.
	 */
	SAINT_EUSTATIUS_AND_SABA_BONAIRE("bq", 599, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return NETHERLANDS;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** BA.
	 */
	BOSNIA_AND_HERZEGOVINA("ba", 387, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** BW.
	 */
	BOTSWANA("bw", 267, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** BV.
	 */
	BOUVET_ISLAND("BV", 47, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return NORWAY;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** BR.
	 */
	BRAZIL("br", 55, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** IO.
	 */
	BRITISH_INDIAN_OCEAN_TERRITORY("io", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** BN.
	 */
	BRUNEI_DARUSSALAM("bn", 673, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** BG.
	 */
	BULGARIA("bg", 359, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** BF.
	 */
	BURKINA_FASO("bf", 226, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** BI.
	 */
	BURUNDI("bi", 257, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},

	/** KH.
	 */
	CAMBODIA("kh", 855, "007", null) { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** CM.
	 */
	CAMEROON("cm", 237, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** CA.
	 */
	CANADA("ca", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** CV.
	 */
	CAPE_VERDE("cv", 238, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** KY.
	 */
	CAYMAN_ISLANDS("ky", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** CF.
	 */
	CENTRAL_AFRICAN_REPUBLIC("cf", 236, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** TD.
	 */
	CHAD("td", 235, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** CL.
	 */
	CHILE("cl", 56, "1YZ0", "1YZ") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** CN.
	 */
	CHINA("cn", 86, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** CX.
	 */
	CHRISTMAS_ISLAND("cx", 672, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return AUSTRALIA;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** CC.
	 */
	COCOS_ISLANDS("cc", 672, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return AUSTRALIA;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** CO.
	 */
	COLOMBIA("co", 57, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** KM.
	 */
	COMOROS("km", 269, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** CG.
	 */
	CONGO("cg", 242, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** CD.
	 */
	CONGO_DEMOCRATIC_REPUBLIC("cd", 243, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** CK.
	 */
	COOK_ISLANDS("ck", 682, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** CR.
	 */
	COSTA_RICA("cr", 506, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** CI.
	 */
	COTE_D_IVOIRE("ci", 225, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** HR.
	 */
	CROATIA("hr", 385, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** CU.
	 */
	CUBA("cu", 53, "119", null) { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** CW.
	 */
	CURACAO("cw", 599, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return NETHERLANDS;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** CY.
	 */
	CYPRUS("cy", 357, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** CZ.
	 */
	CZECH_REPUBLIC("cz", 420, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},

	/** DK.
	 */
	DENMARK("dk", 45, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** DJ.
	 */
	DJIBOUTI("dj", 253, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** DM.
	 */
	DOMINICA("dm", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** DO.
	 */
	DOMINICAN_REPUBLIC("do", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},

	/** EC.
	 */
	ECUADOR("ec", 593, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** EG.
	 */
	EGYPT("eg", 20, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** SV.
	 */
	EL_SALVADOR("sv", 503, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** GQ.
	 */
	EQUATORIAL_GUINEA("gq", 240, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** ER.
	 */
	ERITREA("er", 291, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** EE.
	 */
	ESTONIA("ee", 372, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** ET.
	 */
	ETHIOPIA("et", 251, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},

	/** FK.
	 */
	FALKLAND_ISLANDS("fk", 500, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** FO.
	 */
	FAROE_ISLANDS("fo", 298, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return DENMARK;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** FJ.
	 */
	FIJI("fj", 679, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** FI.
	 */
	FINLAND("fi", 358, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** FR.
	 */
	FRANCE("fr", 33, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** GF.
	 */
	FRENCH_GUIANA("gf", 594, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return FRANCE;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** PF.
	 */
	FRENCH_POLYNESIA("pf", 689, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return FRANCE;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** TF.
	 */
	FRENCH_SOUTHERN_TERRITORIES("tf", 262, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return FRANCE;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},

	/** GA.
	 */
	GABON("ga", 241, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** GM.
	 */
	GAMBIA("gm", 220, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** GE.
	 */
	GEORGIA("ge", 995, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** DE.
	 */
	GERMANY("de", 49, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** GH.
	 */
	GHANA("ghg", 233, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** GI.
	 */
	GIBRALTAR("gi", 350, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** GR.
	 */
	GREECE("gr", 30, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** GL.
	 */
	GREENLAND("gl", 299, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return DENMARK;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** GD.
	 */
	GRENADA("gd", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** GP.
	 */
	GUADELOUPE("gp", 590, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return FRANCE;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** GU.
	 */
	GUAM("gu", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_STATES;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** GT.
	 */
	GUATEMALA("gt", 502, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** GG.
	 */
	GUERNSEY("GG", 44, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** GN.
	 */
	GUINEA("gn", 224, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** GW.
	 */
	GUINEA_BISSAU("gw", 245, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** GY.
	 */
	GUYANA("gy", 592, "001", "") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},

	/** HT.
	 */
	HAITI("ht", 509, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** HM.
	 */
	HEARD_ISLAND_AND_MCDONALD_ISLANDS("hm", 672, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return AUSTRALIA;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** HN.
	 */
	HONDURAS("hn", 504, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** HK.
	 */
	HONG_KONG("hk", 852, "001", "") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return CHINA;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** HU.
	 */
	HUNGARY("hu", 36, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},

	/** IS.
	 */
	ICELAND("is", 354, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** IN.
	 */
	INDIA("in", 91, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** ID.
	 */
	INDONESIA("id", 62, "001", null) { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** IR.
	 */
	IRAN("ir", 98, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** IQ.
	 */
	IRAQ("iq", 964, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** IE.
	 */
	IRELAND("ie", 353, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** IM.
	 */
	ISLE_OF_MAN("im", 44, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** IL.
	 */
	ISRAEL("il", 972, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** IT.
	 */
	ITALY("it", 39, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},

	/** JM.
	 */
	JAMAICA("jm", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** JP.
	 */
	JAPAN("jp", 81, "010", null) { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** JE.
	 */
	JERSEY("je", 44, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** JO.
	 */
	JORDAN("jo", 962, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},

	/** KZ.
	 */
	KAZAKHSTAN("kz", 7, "810", "8") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** KE.
	 */
	KENYA("ke", 254, "000", null) { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** KI.
	 */
	KIRIBATI("ki", 686, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** KP.
	 */
	KOREA_DEMOCRATIC_PEOPLE_REPUBLIC("kp", 850, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** KR.
	 */
	KOREA_REPUBLIC("kr", 82, "001.002", "0.082") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** KW.
	 */
	KUWAIT("kw", 965, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** KG.
	 */
	KYRGYZSTAN("kg", 996, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},

	/** LA.
	 */
	LAO("la", 856, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** LV.
	 */
	LATVIA("lv", 371, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** LB.
	 */
	LEBANON("lb", 961, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** LS.
	 */
	LESOTHO("ls", 266, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** LR.
	 */
	LIBERIA("lr", 231, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** LY.
	 */
	LIBYAN_ARAB_JAMAHIRIYA("ly", 218, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** LI.
	 */
	LIECHTENSTEIN("li", 423, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** LT.
	 */
	LITHUANIA("lt", 370, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** LU.
	 */
	LUXEMBOURG("lu", 352, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},

	/** MO.
	 */
	MACAO("mo", 853, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return CHINA;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** MK.
	 */
	MACEDONIA("mk", 389, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** MG.
	 */
	MADAGASCAR("mg", 261, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** MW.
	 */
	MALAWI("mw", 265, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** MY.
	 */
	MALAYSIA("my", 60, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** MV.
	 */
	MALDIVES("mv", 960, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** ML.
	 */
	MALI("ml", 223, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** MT.
	 */
	MALTA("mt", 356, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** MH.
	 */
	MARSHALL_ISLANDS("mh", 692, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** MQ.
	 */
	MARTINIQUE("mq", 596, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return FRANCE;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** MR.
	 */
	MAURITANIA("mr", 222, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** MU.
	 */
	MAURITIUS("mu", 230, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** YT.
	 */
	MAYOTTE("yt", 262, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return FRANCE;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** MX.
	 */
	MEXICO("mx", 52, null, "01") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** FM.
	 */
	MICRONESIA("fm", 691, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** MD.
	 */
	MOLDOVA("md", 373, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** MC.
	 */
	MONACO("mc", 377, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** MN.
	 */
	MONGOLIA("mn", 976, "001", null) { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** ME.
	 */
	MONTENEGRO("me", 382, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** MS.
	 */
	MONTSERRAT("mq", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** MA.
	 */
	MOROCCO("ma", 212, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** MZ.
	 */
	MOZAMBIQUE("mz", 258, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** MM.
	 */
	MYANMAR("mm", 95, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},

	/** NA.
	 */
	NAMIBIA("na", 264, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** NR.
	 */
	NAURU("nr", 674, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** NP.
	 */
	NEPAL("np", 977, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** NL.
	 */
	NETHERLANDS("nl", 31, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** NC.
	 */
	NEW_CALEDONIA("nc", 687, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return FRANCE;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** NZ.
	 */
	NEW_ZEALAND("nz", 64, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** NI.
	 */
	NICARAGUA("no", 505, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** NE.
	 */
	NIGER("ne", 227, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** NG.
	 */
	NIGERIA("ng", 234, "009", null) { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** NU.
	 */
	NIUE("nu", 683, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** NF.
	 */
	NORFOLK_ISLAND("nf", 672, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return AUSTRALIA;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** MP.
	 */
	NORTHERN_MARIANA_ISLANDS("mp", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_STATES;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** NO.
	 */
	NORWAY("no", 47, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},

	/** OM.
	 */
	OMAN("om", 968, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},

	/** PK.
	 */
	PAKISTAN("pk", 92, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** PW.
	 */
	PALAU("pw", 680, "011", "") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** PS.
	 */
	PALESTINIAN_TERRITORY("ps", 970, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** PA.
	 */
	PANAMA("pa", 507, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** PG.
	 */
	PAPUA_NEW_GUINEA("pg", 675, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** PY.
	 */
	PARAGUAY("py", 595, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** PE.
	 */
	PERU("pe", 51, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** PH.
	 */
	PHILIPPINES("ph", 63, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** PN. TDOO
	 */
	PITCAIRN("pn", 0, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** PL.
	 */
	POLAND("pl", 48, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** PT.
	 */
	PORTUGAL("pt", 351, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** PR.
	 */
	PUERTO_RICO("pr", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_STATES;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},

	/** QA.
	 */
	QATAR("qa", 974, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},

	/** RE.
	 */
	REUNION("RE", 262, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return FRANCE;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** RO.
	 */
	ROMANIA("ro", 40, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** RU.
	 */
	RUSSIAN_FEDERATION("ru", 7, "810", "8") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** RW.
	 */
	RWANDA("rw", 250, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},

	/** BL.
	 */
	SAINT_BARTHELEMY("bl", 590, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return FRANCE;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** SH.
	 */
	SAINT_HELENA("sh", 247, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** KN.
	 */
	SAINT_KITTS_AND_NEVIS("kn", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** LC.
	 */
	SAINT_LUCIA("lc", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** PM.
	 */
	SAINT_PIERRE_AND_MIQUELON("pm", 508, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return FRANCE;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** VC.
	 */
	SAINT_VINCENT_AND_THE_GRENADINES("vc", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** WS.
	 */
	SAMOA("ws", 685, "0", "") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** SM.
	 */
	SAN_MARINO("sm", 378, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** MF.
	 */
	SAINT_MARTIN_FRENCH_SIDE("mf", 590, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return FRANCE;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** ST.
	 */
	SAO_TOME_AND_PRINCIPE("st", 239, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** SA.
	 */
	SAUDI_ARABIA("sa", 966, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** SN.
	 */
	SENEGAL("sn", 221, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** RS.
	 */
	SERBIA("rs", 381, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** SC.
	 */
	SEYCHELLES("sc", 248, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** SL.
	 */
	SIERRA_LEONE("sl", 232, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** SG.
	 */
	SINGAPORE("sg", 65, "001.008", "") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** SX.
	 */
	SINT_MAARTEN_DUTCH_SIDE("sx", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return NETHERLANDS;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** SK.
	 */
	SLOVAKIA("sk", 421, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** SI.
	 */
	SLOVENIA("si", 386, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** SB.
	 */
	SOLOMON_ISLANDS("sb", 677, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** SO.
	 */
	SOMALIA("so", 252, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** ZA.
	 */
	SOUTH_AFRICA("za", 27, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** GS.
	 */
	SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS("GS", 500, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** ES.
	 */
	SPAIN("es", 34, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** LK.
	 */
	SRI_LANKA("lk", 94, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** SD.
	 */
	SUDAN("sd", 249, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** SR.
	 */
	SURINAME("sr", 597, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** SJ.
	 */
	SVALBARD_AND_JAN_MAYEN("sj", 47, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return NORWAY;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** SZ.
	 */
	SWAZILAND("sz", 268, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** SE.
	 */
	SWEDEN("se", 46, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** CH.
	 */
	SWITZERLAND("ch", 41, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** SY.
	 */
	SYRIAN_ARAB_REPUBLIC("sy", 963, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},

	/** TW.
	 */
	TAIWAN("tw", 886, "002", null) { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** TJ.
	 */
	TAJIKISTAN("tj", 992, "810", "8") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** TZ.
	 */
	TANZANIA("tz", 255, "000", "") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** TH.
	 */
	THAILAND("th", 66, "001", null) { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** TL.
	 */
	TIMOR_LESTE("tl", 670, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** TG.
	 */
	TOGO("tg", 228, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** TK.
	 */
	TOKELAU("tk", 690, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return NEW_ZEALAND;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** TO.
	 */
	TONGA("to", 676, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** TT.
	 */
	TRINIDAD_AND_TOBAGO("tt", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** TN.
	 */
	TUNISIA("tn", 216, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** TR.
	 */
	TURKEY("tr", 90, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** TM.
	 */
	TURKMENISTAN("tm", 993, "810", "8") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** TC.
	 */
	TURKS_AND_CAICOS_ISLANDS("tc", 1, "0", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** TV.
	 */
	TUVALU("tv", 688, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},

	/** UG.
	 */
	UGANDA("ug", 256, "000", null) { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** UA.
	 */
	UKRAINE("ua", 380, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** AE.
	 */
	UNITED_ARAB_EMIRATES("ae", 971, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** GB.
	 */
	UNITED_KINGDOM("gb", 44, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** US.
	 */
	UNITED_STATES("us", 1, "011", "1") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** UM. TDOO
	 */
	UNITED_STATES_MINOR_OUTLYING_ISLANDS("um", 0, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_STATES;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** UY.
	 */
	URUGUAY("uy", 598, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** UZ.
	 */
	UZBEKISTAN("uz", 998, "810", "8") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},

	/** VU.
	 */
	VANUATU("vu", 678, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** VA.
	 */
	VATICAN_CITY_STATE("va", 379, "", "") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return true;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** VE.
	 */
	VENEZUELA("ve", 58, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** VN.
	 */
	VIETNAM("vn", 84, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** VG.
	 */
	VIRGIN_ISLANDS_BRITISH_SIDE("vg", 284, "011", "") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_KINGDOM;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** VI.
	 */
	VIRGIN_ISLANDS_US_SIDE("vi", 340, "011", "") { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		@Override
		public CountryCode getSovereignCountry() {
			return UNITED_STATES;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return true;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},

	/** WF.
	 */
	WALLIS_AND_FUTUNA("wf", 681, null, "") { //$NON-NLS-1$ //$NON-NLS-2$
		@Override
		public CountryCode getSovereignCountry() {
			return FRANCE;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return true;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},
	/** EH.
	 */
	WESTERN_SAHARA("eh", 212, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return MOROCCO;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},

	/** Y.
	 */
	YEMEN("y", 967, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return true;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return false;
		}
	},

	/** ZM.
	 */
	ZAMBIA("zm", 260, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	},
	/** ZW.
	 */
	ZIMBABWE("zw", 263, null, null) { //$NON-NLS-1$
		@Override
		public CountryCode getSovereignCountry() {
			return null;
		}

		@Override
		public boolean isEuropeanContinent() {
			return false;
		}

		@Override
		public boolean isNorthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isSouthAmericanContinent() {
			return false;
		}

		@Override
		public boolean isAsianContinent() {
			return false;
		}

		@Override
		public boolean isOceanicContinent() {
			return false;
		}

		@Override
		public boolean isAfricanContinent() {
			return true;
		}
	};

	private final String code;

	private final int callingCode;

	private final String internationalPhonePrefix;

	private final String nationalPhonePrefix;

	CountryCode(String isoCode, int callingCode, String internationalPhonePrefix, String nationalPhonePrefix) {
		this.code = isoCode;
		this.callingCode = callingCode;
		this.internationalPhonePrefix = internationalPhonePrefix;
		this.nationalPhonePrefix = nationalPhonePrefix;
	}

	/** Replies the country calling code of this country.
	 * The country calling code is a number prefix (usually two or three digit long), which help you dial
	 * a phone number located in another country from the one you are in. It is assigned and managed 
	 * by International Telecommunication Union (ITU) and defined in the ITU-T ISO standards: E.123 and E.164.
	 *
	 * @return the country calling code, or {@code 0} if there is no code for this country.
	 * @since 18.0
	 * @see #getInternationalPhonePrefix()
	 * @see #getNationalPhonePrefix()
	 */
	public int getCallingCode() {
		return this.callingCode;
	}

	/** Replies the international phone prefix. It is another representation of the
	 * {@link #getCallingCode()} country calling code} for this country.
	 * It is the part of the phone number when it is written using its international notation,
	 * but without the famous {@code +} sign followed by the {@link #getCallingCode() calling code}
	 * and the rest of number.
	 *
	 * <p>For example, the phone number {@code 0033123456789} is a french phone number because
	 * it has the international prefix {@code 0033} that corresponds to the calling code {@code 33}.
	 * This number could also be written {@code +33123456789} (another international number notation}
	 * or {@code 0123456789} (the national number notation}.
	 *
	 * @return the international phone prefix.
	 * @since 18.0
	 * @see #getCallingCode()
	 * @see #getNationalPhonePrefix()
	 */
	public String getInternationalPhonePrefix() {
		if (this.internationalPhonePrefix == null) {
			return "00"; //$NON-NLS-1$
		}
		return this.internationalPhonePrefix;
	}

	/** Replies the national phone prefix for this country.
	 * It is the part of the phone number when it is written using its national notation.
	 *
	 * <p>For example, the phone number {@code 0123456789} is a french phone number because
	 * it has the national prefix {@code 0}.
	 * This number could also be written {@code +33123456789} or {@code 0033123456789} that
	 * are using both the international number notation.
	 *
	 * @return the international phone prefix.
	 * @since 18.0
	 * @see #getCallingCode()
	 * @see #getInternationalPhonePrefix()
	 */
	public String getNationalPhonePrefix() {
		if (this.nationalPhonePrefix == null) {
			return "0"; //$NON-NLS-1$
		}
		return this.nationalPhonePrefix;
	}

	/** Replies the ISO 3166-1 code for the current country.
	 *
	 * @return the iso code for the country.
	 */
	@Pure
	public String getCode() {
		return this.code;
	}

	/** Replies the country code for the given locale.
	 *
	 * @param locale the locale.
	 * @return the country code, or {@code null} if there is no country code for the locale.
	 */
	@Pure
	public static CountryCode fromLocale(Locale locale) {
		if (locale != null) {
			final String c = locale.getCountry();
			for (final CountryCode cc : CountryCode.values()) {
				if (cc.code.equalsIgnoreCase(c)) {
					return cc;
				}
			}
		}
		return null;
	}


	/** Replies the country code for the given calling code.
	 * This function replies the first country that has the given
	 * calling code, in the case these is multiple country with the
	 * same calling code.
	 *
	 * @param code the calling code.
	 * @return the country code, or {@code null} if no country was found.
	 */
	@Pure
	public static CountryCode fromCallingCode(int code) {
		for (final CountryCode cc : CountryCode.values()) {
			if (cc.callingCode == code) {
				return cc;
			}
		}
		return null;
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

	/** Replies the default locale for the country.
	 *
	 * @return the default locale for the country.
	 */
	@Pure
	public Locale getLocale() {
		return new Locale(this.code);
	}

	@Override
	@Pure
	public String toString() {
		return this.code;
	}

	/** Replies the display name of the country.
	 * The target language is the one provided by {@link LocaleContextHolder}.
	 *
	 * @return the display name of the country.
	 */
	public String getDisplayCountry() {
		final Locale targetLanguage = LocaleContextHolder.getLocale();
		final String theCode = getCode();
		final Locale countryLocale = new Locale(theCode, theCode);
		if (targetLanguage == null) {
			return countryLocale.getDisplayCountry();
		}
		return countryLocale.getDisplayCountry(targetLanguage);
	}

	/** Replies the display name of the language associated to the country.
	 * The target language is the one provided by {@link LocaleContextHolder}.
	 *
	 * @return the display name of the country.
	 */
	public String getDisplayLanguage() {
		final Locale targetLanguage = LocaleContextHolder.getLocale();
		final String theCode = getCode();
		final Locale countryLocale = new Locale(theCode, theCode);
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
			labels.put(code, code.getDisplayCountry());
		}
		return labels;
	}

	/** Replies the country code of the sovereign/president/governor of the country with the current code.
	 *
	 * <p>The information related to the sovereign/president/governor country is obtained from Wikipedia.
	 *
	 * @return the country of the sovereign, or {@code null} if the sovereign is in the current country.
	 */
	public abstract CountryCode getSovereignCountry();

	/** Replies if the country code corresponds to a country with an European sovereign/president/governor.
	 *
	 * <p>The information related to the sovereign/president/governor country is obtained from Wikipedia.
	 *
	 * @return {@code true} if the sovereign/president/governor of the country is in Europe continent.
	 */
	public boolean isEuropeanSovereign() {
		final CountryCode sovereign = getSovereignCountry();
		if (sovereign == null) {
			return isEuropeanContinent();
		}
		return sovereign.isEuropeanContinent();
	}

	/** Replies if the country code corresponds to a country located in European continent.
	 *
	 * <p>This function implements the seven-contient approach provided on Wikipedia.
	 *
	 * @return {@code true} if the code corresponds to a European territory.
	 */
	public abstract boolean isEuropeanContinent();

	/** Replies if the country code corresponds to a country with an North-American sovereign/president/governor.
	 *
	 * <p>The information related to the sovereign/president/governor country is obtained from Wikipedia.
	 *
	 * @return {@code true} if the sovereign/president/governor of the country is in North-American continent.
	 */
	public boolean isNorthAmericanSovereign() {
		final CountryCode sovereign = getSovereignCountry();
		if (sovereign == null) {
			return isNorthAmericanContinent();
		}
		return sovereign.isNorthAmericanContinent();
	}

	/** Replies if the country code corresponds to a country located in South-American continent.
	 *
	 * <p>This function implements the seven-contient approach provided on Wikipedia.
	 *
	 * @return {@code true} if the code corresponds to a South-American territory.
	 */
	public abstract boolean isNorthAmericanContinent();

	/** Replies if the country code corresponds to a country with an South-American sovereign/president/governor.
	 *
	 * <p>The information related to the sovereign/president/governor country is obtained from Wikipedia.
	 *
	 * @return {@code true} if the sovereign/president/governor of the country is in South-American continent.
	 */
	public boolean isSouthAmericanSovereign() {
		final CountryCode sovereign = getSovereignCountry();
		if (sovereign == null) {
			return isSouthAmericanContinent();
		}
		return sovereign.isSouthAmericanContinent();
	}

	/** Replies if the country code corresponds to a country located in South-American continent.
	 *
	 * <p>This function implements the seven-contient approach provided on Wikipedia.
	 *
	 * @return {@code true} if the code corresponds to a South-American territory.
	 */
	public abstract boolean isSouthAmericanContinent();

	/** Replies if the country code corresponds to a country with an Asian sovereign/president/governor.
	 *
	 * <p>The information related to the sovereign/president/governor country is obtained from Wikipedia.
	 *
	 * @return {@code true} if the sovereign/president/governor of the country is in Asian continent.
	 */
	public boolean isAsianSovereign() {
		final CountryCode sovereign = getSovereignCountry();
		if (sovereign == null) {
			return isAsianContinent();
		}
		return sovereign.isAsianContinent();
	}

	/** Replies if the country code corresponds to a country located in Asian continent.
	 *
	 * <p>This function implements the seven-contient approach provided on Wikipedia.
	 *
	 * @return {@code true} if the code corresponds to a Asian territory.
	 */
	public abstract boolean isAsianContinent();

	/** Replies if the country code corresponds to a country with an Oceanic sovereign/president/governor.
	 *
	 * <p>The information related to the sovereign/president/governor country is obtained from Wikipedia.
	 *
	 * @return {@code true} if the sovereign/president/governor of the country is in Oceanic continent.
	 */
	public boolean isAustralianSovereign() {
		final CountryCode sovereign = getSovereignCountry();
		if (sovereign == null) {
			return isOceanicContinent();
		}
		return sovereign.isOceanicContinent();
	}

	/** Replies if the country code corresponds to a country located in Oceanic continent.
	 *
	 * <p>This function implements the seven-contient approach provided on Wikipedia.
	 *
	 * @return {@code true} if the code corresponds to a Oceanic territory.
	 */
	public abstract boolean isOceanicContinent();

	/** Replies if the country code corresponds to a country with an African sovereign/president/governor.
	 *
	 * <p>The information related to the sovereign/president/governor country is obtained from Wikipedia.
	 *
	 * @return {@code true} if the sovereign/president/governor of the country is in African continent.
	 */
	public boolean isAfricanSovereign() {
		final CountryCode sovereign = getSovereignCountry();
		if (sovereign == null) {
			return isAfricanContinent();
		}
		return sovereign.isAfricanContinent();
	}

	/** Replies if the country code corresponds to a country located in African continent.
	 *
	 * <p>This function implements the seven-contient approach provided on Wikipedia.
	 *
	 * @return {@code true} if the code corresponds to a African territory.
	 */
	public abstract boolean isAfricanContinent();

	/** Replies if the code corresponds to France or one of its associated territories.
	 *
	 * @return {@code true} if the code corresponds to a French territory.
	 */
	public boolean isFrance() {
		return this == FRANCE || getSovereignCountry() == FRANCE;
	}

	/** Replies the default country code.
	 *
	 * @return the default country code.
	 */
	public static CountryCode getDefault() {
		return FRANCE;
	}

	/** Replied the normalized country code for this code.
	 * The normalized country code is a code without a sovereign code. If the current code
	 * has a sovereign code, this sovereign code is used. This process is repeated until
	 * a normalized code is found.
	 * 
	 * @return the normalized code.
	 */
	public CountryCode normalize() {
		CountryCode current = this;
		CountryCode sovereign = getSovereignCountry();
		while (sovereign != null) {
			current = sovereign;
			sovereign = current.getSovereignCountry();
		}
		return current;
	}

}
