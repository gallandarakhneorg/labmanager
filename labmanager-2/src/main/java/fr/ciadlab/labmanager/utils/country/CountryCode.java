package fr.ciadlab.labmanager.utils.country;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Strings;
import org.eclipse.xtext.xbase.lib.Pure;
import org.springframework.context.i18n.LocaleContextHolder;

/** This enumeration gives the official codes of the countries
 * given by the ISO 3166-1.
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
	AFGHANISTAN("AF") { //$NON-NLS-1$
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
	ALAND_ISLANDS("AX") { //$NON-NLS-1$
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
	ALBANIA("AL") { //$NON-NLS-1$
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
	ALGERIA("DZ") { //$NON-NLS-1$
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
	AMERICAN_SAMOA("AS") { //$NON-NLS-1$
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
	ANDORRA("AD") { //$NON-NLS-1$
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
	ANGOLA("AO") { //$NON-NLS-1$
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
	ANGUILLA("AI") { //$NON-NLS-1$
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
	ANTARCTICA("AQ") { //$NON-NLS-1$
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
	ANTIGUA_AND_BARBUDA("AG") { //$NON-NLS-1$
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
	ARGENTINA("AR") { //$NON-NLS-1$
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
	ARMENIA("AM") { //$NON-NLS-1$
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
	ARUBA("AW") { //$NON-NLS-1$
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
	AUSTRALIA("AU") { //$NON-NLS-1$
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
	AUSTRIA("AT") { //$NON-NLS-1$
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
	AZERBAIJAN("AZ") { //$NON-NLS-1$
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
	BAHAMAS("BS") { //$NON-NLS-1$
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
	BAHRAIN("BH") { //$NON-NLS-1$
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
	BANGLADESH("BD") { //$NON-NLS-1$
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
	BARBADOS("BB") { //$NON-NLS-1$
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
	BELARUS("BY") { //$NON-NLS-1$
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
	BELGIUM("BE") { //$NON-NLS-1$
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
	BELIZE("BZ") { //$NON-NLS-1$
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
	BENIN("BJ") { //$NON-NLS-1$
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
	BERMUDA("BM") { //$NON-NLS-1$
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
	BHUTAN("BT") { //$NON-NLS-1$
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
	BOLIVIA("BO") { //$NON-NLS-1$
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
	SAINT_EUSTATIUS_AND_SABA_BONAIRE("BQ") { //$NON-NLS-1$
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
	BOSNIA_AND_HERZEGOVINA("BA") { //$NON-NLS-1$
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
	BOTSWANA("BW") { //$NON-NLS-1$
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
	BOUVET_ISLAND("BV") { //$NON-NLS-1$
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
	BRAZIL("BR") { //$NON-NLS-1$
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
	BRITISH_INDIAN_OCEAN_TERRITORY("IO") { //$NON-NLS-1$
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
	BRUNEI_DARUSSALAM("BN") { //$NON-NLS-1$
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
	BULGARIA("BG") { //$NON-NLS-1$
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
	BURKINA_FASO("BF") { //$NON-NLS-1$
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
	BURUNDI("BI") { //$NON-NLS-1$
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
	CAMBODIA("KH") { //$NON-NLS-1$
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
	CAMEROON("CM") { //$NON-NLS-1$
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
	CANADA("CA") { //$NON-NLS-1$
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
	CAPE_VERDE("CV") { //$NON-NLS-1$
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
	CAYMAN_ISLANDS("KY") { //$NON-NLS-1$
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
	CENTRAL_AFRICAN_REPUBLIC("CF") { //$NON-NLS-1$
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
	CHAD("TD") { //$NON-NLS-1$
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
	CHILE("CL") { //$NON-NLS-1$
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
	CHINA("CN") { //$NON-NLS-1$
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
	CHRISTMAS_ISLAND("CX") { //$NON-NLS-1$
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
	COCOS_ISLANDS("CC") { //$NON-NLS-1$
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
	COLOMBIA("CO") { //$NON-NLS-1$
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
	COMOROS("KM") { //$NON-NLS-1$
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
	CONGO("CG") { //$NON-NLS-1$
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
	CONGO_DEMOCRATIC_REPUBLIC("CD") { //$NON-NLS-1$
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
	COOK_ISLANDS("CK") { //$NON-NLS-1$
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
	COSTA_RICA("CR") { //$NON-NLS-1$
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
	COTE_D_IVOIRE("CI") { //$NON-NLS-1$
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
	CROATIA("HR") { //$NON-NLS-1$
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
	CUBA("CU") { //$NON-NLS-1$
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
	CURACAO("CW") { //$NON-NLS-1$
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
	CYPRUS("CY") { //$NON-NLS-1$
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
	CZECH_REPUBLIC("CZ") { //$NON-NLS-1$
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
	DENMARK("DK") { //$NON-NLS-1$
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
	DJIBOUTI("DJ") { //$NON-NLS-1$
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
	DOMINICA("DM") { //$NON-NLS-1$
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
	DOMINICAN_REPUBLIC("DO") { //$NON-NLS-1$
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
	ECUADOR("EC") { //$NON-NLS-1$
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
	EGYPT("EG") { //$NON-NLS-1$
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
	EL_SALVADOR("SV") { //$NON-NLS-1$
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
	EQUATORIAL_GUINEA("GQ") { //$NON-NLS-1$
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
	ERITREA("ER") { //$NON-NLS-1$
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
	ESTONIA("EE") { //$NON-NLS-1$
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
	ETHIOPIA("ET") { //$NON-NLS-1$
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
	FALKLAND_ISLANDS("FK") { //$NON-NLS-1$
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
	FAROE_ISLANDS("FO") { //$NON-NLS-1$
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
	FIJI("FJ") { //$NON-NLS-1$
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
	FINLAND("FI") { //$NON-NLS-1$
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
	FRANCE("FR") { //$NON-NLS-1$
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
	FRENCH_GUIANA("GF") { //$NON-NLS-1$
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
	FRENCH_POLYNESIA("PF") { //$NON-NLS-1$
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
	FRENCH_SOUTHERN_TERRITORIES("TF") { //$NON-NLS-1$
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
	GABON("GA") { //$NON-NLS-1$
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
	GAMBIA("GM") { //$NON-NLS-1$
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
	GEORGIA("GE") { //$NON-NLS-1$
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
	GERMANY("DE") { //$NON-NLS-1$
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
	GHANA("GH") { //$NON-NLS-1$
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
	GIBRALTAR("GI") { //$NON-NLS-1$
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
	GREECE("GR") { //$NON-NLS-1$
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
	GREENLAND("GL") { //$NON-NLS-1$
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
	GRENADA("GD") { //$NON-NLS-1$
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
	GUADELOUPE("GP") { //$NON-NLS-1$
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
	GUAM("GU") { //$NON-NLS-1$
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
	GUATEMALA("GT") { //$NON-NLS-1$
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
	GUERNSEY("GG") { //$NON-NLS-1$
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
	GUINEA("GN") { //$NON-NLS-1$
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
	GUINEA_BISSAU("GW") { //$NON-NLS-1$
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
	GUYANA("GY") { //$NON-NLS-1$
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
	HAITI("HT") { //$NON-NLS-1$
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
	HEARD_ISLAND_AND_MCDONALD_ISLANDS("HM") { //$NON-NLS-1$
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
	HONDURAS("HN") { //$NON-NLS-1$
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
	HONG_KONG("HK") { //$NON-NLS-1$
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
	HUNGARY("HU") { //$NON-NLS-1$
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
	ICELAND("IS") { //$NON-NLS-1$
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
	INDIA("IN") { //$NON-NLS-1$
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
	INDONESIA("ID") { //$NON-NLS-1$
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
	IRAN("IR") { //$NON-NLS-1$
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
	IRAQ("IQ") { //$NON-NLS-1$
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
	IRELAND("IE") { //$NON-NLS-1$
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
	ISLE_OF_MAN("IM") { //$NON-NLS-1$
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
	ISRAEL("IL") { //$NON-NLS-1$
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
	ITALY("IT") { //$NON-NLS-1$
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
	JAMAICA("JM") { //$NON-NLS-1$
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
	JAPAN("JP") { //$NON-NLS-1$
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
	JERSEY("JE") { //$NON-NLS-1$
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
	JORDAN("JO") { //$NON-NLS-1$
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
	KAZAKHSTAN("KZ") { //$NON-NLS-1$
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
	KENYA("KE") { //$NON-NLS-1$
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
	KIRIBATI("KI") { //$NON-NLS-1$
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
	KOREA_DEMOCRATIC_PEOPLE_REPUBLIC("KP") { //$NON-NLS-1$
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
	KOREA_REPUBLIC("KR") { //$NON-NLS-1$
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
	KUWAIT("KW") { //$NON-NLS-1$
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
	KYRGYZSTAN("KG") { //$NON-NLS-1$
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
	LAO("LA") { //$NON-NLS-1$
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
	LATVIA("LV") { //$NON-NLS-1$
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
	LEBANON("LB") { //$NON-NLS-1$
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
	LESOTHO("LS") { //$NON-NLS-1$
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
	LIBERIA("LR") { //$NON-NLS-1$
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
	LIBYAN_ARAB_JAMAHIRIYA("LY") { //$NON-NLS-1$
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
	LIECHTENSTEIN("LI") { //$NON-NLS-1$
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
	LITHUANIA("LT") { //$NON-NLS-1$
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
	LUXEMBOURG("LU") { //$NON-NLS-1$
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
	MACAO("MO") { //$NON-NLS-1$
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
	MACEDONIA("MK") { //$NON-NLS-1$
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
	MADAGASCAR("MG") { //$NON-NLS-1$
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
	MALAWI("MW") { //$NON-NLS-1$
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
	MALAYSIA("MY") { //$NON-NLS-1$
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
	MALDIVES("MV") { //$NON-NLS-1$
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
	MALI("ML") { //$NON-NLS-1$
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
	MALTA("MT") { //$NON-NLS-1$
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
	MARSHALL_ISLANDS("MH") { //$NON-NLS-1$
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
	MARTINIQUE("MQ") { //$NON-NLS-1$
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
	MAURITANIA("MR") { //$NON-NLS-1$
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
	MAURITIUS("MU") { //$NON-NLS-1$
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
	MAYOTTE("YT") { //$NON-NLS-1$
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
	MEXICO("MX") { //$NON-NLS-1$
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
	MICRONESIA("FM") { //$NON-NLS-1$
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
	MOLDOVA("MD") { //$NON-NLS-1$
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
	MONACO("MC") { //$NON-NLS-1$
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
	MONGOLIA("MN") { //$NON-NLS-1$
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
	MONTENEGRO("ME") { //$NON-NLS-1$
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
	MONTSERRAT("MS") { //$NON-NLS-1$
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
	MOROCCO("MA") { //$NON-NLS-1$
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
	MOZAMBIQUE("MZ") { //$NON-NLS-1$
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
	MYANMAR("MM") { //$NON-NLS-1$
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
	NAMIBIA("NA") { //$NON-NLS-1$
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
	NAURU("NR") { //$NON-NLS-1$
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
	NEPAL("NP") { //$NON-NLS-1$
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
	NETHERLANDS("NL") { //$NON-NLS-1$
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
	NEW_CALEDONIA("NC") { //$NON-NLS-1$
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
	NEW_ZEALAND("NZ") { //$NON-NLS-1$
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
	NICARAGUA("NI") { //$NON-NLS-1$
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
	NIGER("NE") { //$NON-NLS-1$
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
	NIGERIA("NG") { //$NON-NLS-1$
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
	NIUE("NU") { //$NON-NLS-1$
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
	NORFOLK_ISLAND("NF") { //$NON-NLS-1$
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
	NORTHERN_MARIANA_ISLANDS("MP") { //$NON-NLS-1$
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
	NORWAY("NO") { //$NON-NLS-1$
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
	OMAN("OM") { //$NON-NLS-1$
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
	PAKISTAN("PK") { //$NON-NLS-1$
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
	PALAU("PW") { //$NON-NLS-1$
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
	PALESTINIAN_TERRITORY("PS") { //$NON-NLS-1$
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
	PANAMA("PA") { //$NON-NLS-1$
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
	PAPUA_NEW_GUINEA("PG") { //$NON-NLS-1$
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
	PARAGUAY("PY") { //$NON-NLS-1$
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
	PERU("PE") { //$NON-NLS-1$
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
	PHILIPPINES("PH") { //$NON-NLS-1$
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
	/** PN.
	 */
	PITCAIRN("PN") { //$NON-NLS-1$
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
	POLAND("PL") { //$NON-NLS-1$
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
	PORTUGAL("PT") { //$NON-NLS-1$
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
	PUERTO_RICO("PR") { //$NON-NLS-1$
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
	QATAR("QA") { //$NON-NLS-1$
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
	REUNION("RE") { //$NON-NLS-1$
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
	ROMANIA("RO") { //$NON-NLS-1$
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
	RUSSIAN_FEDERATION("RU") { //$NON-NLS-1$
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
	RWANDA("RW") { //$NON-NLS-1$
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
	SAINT_BARTHELEMY("BL") { //$NON-NLS-1$
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
	SAINT_HELENA("SH") { //$NON-NLS-1$
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
	SAINT_KITTS_AND_NEVIS("KN") { //$NON-NLS-1$
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
	SAINT_LUCIA("LC") { //$NON-NLS-1$
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
	SAINT_PIERRE_AND_MIQUELON("PM") { //$NON-NLS-1$
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
	SAINT_VINCENT_AND_THE_GRENADINES("VC") { //$NON-NLS-1$
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
	SAMOA("WS") { //$NON-NLS-1$
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
	SAN_MARINO("SM") { //$NON-NLS-1$
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
	SAINT_MARTIN_FRENCH_SIDE("MF") { //$NON-NLS-1$
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
	SAO_TOME_AND_PRINCIPE("ST") { //$NON-NLS-1$
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
	SAUDI_ARABIA("SA") { //$NON-NLS-1$
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
	SENEGAL("SN") { //$NON-NLS-1$
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
	SERBIA("RS") { //$NON-NLS-1$
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
	SEYCHELLES("SC") { //$NON-NLS-1$
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
	SIERRA_LEONE("SL") { //$NON-NLS-1$
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
	SINGAPORE("SG") { //$NON-NLS-1$
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
	SINT_MAARTEN_DUTCH_SIDE("SX") { //$NON-NLS-1$
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
	SLOVAKIA("SK") { //$NON-NLS-1$
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
	SLOVENIA("SI") { //$NON-NLS-1$
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
	SOLOMON_ISLANDS("SB") { //$NON-NLS-1$
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
	SOMALIA("SO") { //$NON-NLS-1$
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
	SOUTH_AFRICA("ZA") { //$NON-NLS-1$
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
	SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS("GS") { //$NON-NLS-1$
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
	SPAIN("ES") { //$NON-NLS-1$
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
	SRI_LANKA("LK") { //$NON-NLS-1$
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
	SUDAN("SD") { //$NON-NLS-1$
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
	SURINAME("SR") { //$NON-NLS-1$
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
	SVALBARD_AND_JAN_MAYEN("SJ") { //$NON-NLS-1$
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
	SWAZILAND("SZ") { //$NON-NLS-1$
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
	SWEDEN("SE") { //$NON-NLS-1$
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
	SWITZERLAND("CH") { //$NON-NLS-1$
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
	SYRIAN_ARAB_REPUBLIC("SY") { //$NON-NLS-1$
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
	TAIWAN("TW") { //$NON-NLS-1$
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
	TAJIKISTAN("TJ") { //$NON-NLS-1$
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
	TANZANIA("TZ") { //$NON-NLS-1$
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
	THAILAND("TH") { //$NON-NLS-1$
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
	TIMOR_LESTE("TL") { //$NON-NLS-1$
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
	TOGO("TG") { //$NON-NLS-1$
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
	TOKELAU("TK") { //$NON-NLS-1$
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
	TONGA("TO") { //$NON-NLS-1$
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
	TRINIDAD_AND_TOBAGO("TT") { //$NON-NLS-1$
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
	TUNISIA("TN") { //$NON-NLS-1$
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
	TURKEY("TR") { //$NON-NLS-1$
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
	TURKMENISTAN("TM") { //$NON-NLS-1$
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
	TURKS_AND_CAICOS_ISLANDS("TC") { //$NON-NLS-1$
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
	TUVALU("TV") { //$NON-NLS-1$
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
	UGANDA("UG") { //$NON-NLS-1$
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
	UKRAINE("UA") { //$NON-NLS-1$
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
	UNITED_ARAB_EMIRATES("AE") { //$NON-NLS-1$
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
	UNITED_KINGDOM("GB") { //$NON-NLS-1$
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
	UNITED_STATES("US") { //$NON-NLS-1$
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
	/** UM.
	 */
	UNITED_STATES_MINOR_OUTLYING_ISLANDS("UM") { //$NON-NLS-1$
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
	URUGUAY("UY") { //$NON-NLS-1$
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
	UZBEKISTAN("UZ") { //$NON-NLS-1$
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
	VANUATU("VU") { //$NON-NLS-1$
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
	VATICAN_CITY_STATE("VA") { //$NON-NLS-1$
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
	VENEZUELA("VE") { //$NON-NLS-1$
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
	VIETNAM("VN") { //$NON-NLS-1$
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
	VIRGIN_ISLANDS_BRITISH_SIDE("VG") { //$NON-NLS-1$
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
	VIRGIN_ISLANDS_US_SIDE("VI") { //$NON-NLS-1$
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
	WALLIS_AND_FUTUNA("WF") { //$NON-NLS-1$
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
	WESTERN_SAHARA("EH") { //$NON-NLS-1$
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
	YEMEN("Y") { //$NON-NLS-1$
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
	ZAMBIA("ZM") { //$NON-NLS-1$
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
	ZIMBABWE("ZW") { //$NON-NLS-1$
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

	CountryCode(String isoCode) {
		this.code = isoCode.toLowerCase();
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
	 * @return the country code
	 */
	@Pure
	public static CountryCode fromLocale(Locale locale) {
		final String c = locale.getCountry();
		for (final CountryCode cc : CountryCode.values()) {
			if (cc.code.equalsIgnoreCase(c)) {
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
