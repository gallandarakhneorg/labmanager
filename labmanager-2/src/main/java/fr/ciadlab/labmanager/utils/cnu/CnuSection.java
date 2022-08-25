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

package fr.ciadlab.labmanager.utils.cnu;

import java.util.Locale;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.BaseMessageSource;
import fr.ciadlab.labmanager.utils.bap.FrenchBap;
import fr.ciadlab.labmanager.utils.conrs.ConrsSection;
import org.springframework.context.support.MessageSourceAccessor;

/** The CNU section is the part of the French public research community that is dedicated to
 * a specific domain. CNU means "Conseil National des Universités".
 * Each section is associated to a number and a scientific topic.
 * Usually, a French research in public institution is associated to a major CNU section, and
 * may be associated to minor other sections.
 * <p>CNU section is one of the two major system used in France with the {@link ConrsSection CoNRS system}.
 * Not researcher staff is classified according to the {@link FrenchBap French BAP}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see "https://www.galaxie.enseignementsup-recherche.gouv.fr/ensup/pdf/qualification/sections.pdf"
 * @see ConrsSection
 * @see FrenchBAP
 */
public enum CnuSection {
	/** CNU 01.
	 */
	CNU_01 {
		@Override
		public int getGroup() {
			return 1;
		}
		@Override
		public int getNumber() {
			return 1;
		}
	},
	/** CNU 02.
	 */
	CNU_02 {
		@Override
		public int getGroup() {
			return 1;
		}
		@Override
		public int getNumber() {
			return 2;
		}
	},
	/** CNU 03.
	 */
	CNU_03 {
		@Override
		public int getGroup() {
			return 1;
		}
		@Override
		public int getNumber() {
			return 3;
		}
	},
	/** CNU 04.
	 */
	CNU_04 {
		@Override
		public int getGroup() {
			return 1;
		}
		@Override
		public int getNumber() {
			return 4;
		}
	},
	/** CNU 05.
	 */
	CNU_05 {
		@Override
		public int getGroup() {
			return 2;
		}
		@Override
		public int getNumber() {
			return 5;
		}
	},
	/** CNU 06.
	 */
	CNU_06 {
		@Override
		public int getGroup() {
			return 2;
		}
		@Override
		public int getNumber() {
			return 6;
		}
	},
	/** CNU 07.
	 */
	CNU_07 {
		@Override
		public int getGroup() {
			return 3;
		}
		@Override
		public int getNumber() {
			return 7;
		}
	},
	/** CNU 08.
	 */
	CNU_08 {
		@Override
		public int getGroup() {
			return 3;
		}
		@Override
		public int getNumber() {
			return 8;
		}
	},
	/** CNU 09.
	 */
	CNU_09 {
		@Override
		public int getGroup() {
			return 3;
		}
		@Override
		public int getNumber() {
			return 9;
		}
	},
	/** CNU 10.
	 */
	CNU_10 {
		@Override
		public int getGroup() {
			return 3;
		}
		@Override
		public int getNumber() {
			return 10;
		}
	},
	/** CNU 11.
	 */
	CNU_11 {
		@Override
		public int getGroup() {
			return 3;
		}
		@Override
		public int getNumber() {
			return 11;
		}
	},
	/** CNU 12.
	 */
	CNU_12 {
		@Override
		public int getGroup() {
			return 3;
		}
		@Override
		public int getNumber() {
			return 12;
		}
	},
	/** CNU 13.
	 */
	CNU_13 {
		@Override
		public int getGroup() {
			return 3;
		}
		@Override
		public int getNumber() {
			return 13;
		}
	},
	/** CNU 14.
	 */
	CNU_14 {
		@Override
		public int getGroup() {
			return 3;
		}
		@Override
		public int getNumber() {
			return 14;
		}
	},
	/** CNU 15.
	 */
	CNU_15 {
		@Override
		public int getGroup() {
			return 3;
		}
		@Override
		public int getNumber() {
			return 15;
		}
	},
	/** CNU 16.
	 */
	CNU_16 {
		@Override
		public int getGroup() {
			return 4;
		}
		@Override
		public int getNumber() {
			return 16;
		}
	},
	/** CNU 17.
	 */
	CNU_17 {
		@Override
		public int getGroup() {
			return 4;
		}
		@Override
		public int getNumber() {
			return 17;
		}
	},
	/** CNU 18.
	 */
	CNU_18 {
		@Override
		public int getGroup() {
			return 4;
		}
		@Override
		public int getNumber() {
			return 18;
		}
	},
	/** CNU 19.
	 */
	CNU_19 {
		@Override
		public int getGroup() {
			return 4;
		}
		@Override
		public int getNumber() {
			return 19;
		}
	},
	/** CNU 20.
	 */
	CNU_20 {
		@Override
		public int getGroup() {
			return 4;
		}
		@Override
		public int getNumber() {
			return 20;
		}
	},
	/** CNU 21.
	 */
	CNU_21 {
		@Override
		public int getGroup() {
			return 4;
		}
		@Override
		public int getNumber() {
			return 21;
		}
	},
	/** CNU 22.
	 */
	CNU_22 {
		@Override
		public int getGroup() {
			return 4;
		}
		@Override
		public int getNumber() {
			return 22;
		}
	},
	/** CNU 23.
	 */
	CNU_23 {
		@Override
		public int getGroup() {
			return 4;
		}
		@Override
		public int getNumber() {
			return 23;
		}
	},
	/** CNU 24.
	 */
	CNU_24 {
		@Override
		public int getGroup() {
			return 4;
		}
		@Override
		public int getNumber() {
			return 24;
		}
	},
	/** CNU 25.
	 */
	CNU_25 {
		@Override
		public int getGroup() {
			return 5;
		}
		@Override
		public int getNumber() {
			return 25;
		}
	},
	/** CNU 26.
	 */
	CNU_26 {
		@Override
		public int getGroup() {
			return 5;
		}
		@Override
		public int getNumber() {
			return 26;
		}
	},
	/** CNU 27.
	 */
	CNU_27 {
		@Override
		public int getGroup() {
			return 5;
		}
		@Override
		public int getNumber() {
			return 27;
		}
	},
	/** CNU 28.
	 */
	CNU_28 {
		@Override
		public int getGroup() {
			return 6;
		}
		@Override
		public int getNumber() {
			return 28;
		}
	},
	/** CNU 29.
	 */
	CNU_29 {
		@Override
		public int getGroup() {
			return 6;
		}
		@Override
		public int getNumber() {
			return 29;
		}
	},
	/** CNU 30.
	 */
	CNU_30 {
		@Override
		public int getGroup() {
			return 6;
		}
		@Override
		public int getNumber() {
			return 30;
		}
	},
	/** CNU 31.
	 */
	CNU_31 {
		@Override
		public int getGroup() {
			return 7;
		}
		@Override
		public int getNumber() {
			return 31;
		}
	},
	/** CNU 32.
	 */
	CNU_32 {
		@Override
		public int getGroup() {
			return 7;
		}
		@Override
		public int getNumber() {
			return 32;
		}
	},
	/** CNU 33.
	 */
	CNU_33 {
		@Override
		public int getGroup() {
			return 7;
		}
		@Override
		public int getNumber() {
			return 33;
		}
	},
	/** CNU 34.
	 */
	CNU_34 {
		@Override
		public int getGroup() {
			return 8;
		}
		@Override
		public int getNumber() {
			return 34;
		}
	},
	/** CNU 35.
	 */
	CNU_35 {
		@Override
		public int getGroup() {
			return 8;
		}
		@Override
		public int getNumber() {
			return 35;
		}
	},
	/** CNU 36.
	 */
	CNU_36 {
		@Override
		public int getGroup() {
			return 8;
		}
		@Override
		public int getNumber() {
			return 36;
		}
	},
	/** CNU 37.
	 */
	CNU_37 {
		@Override
		public int getGroup() {
			return 8;
		}
		@Override
		public int getNumber() {
			return 37;
		}
	},
	/** CNU 60.
	 */
	CNU_60 {
		@Override
		public int getGroup() {
			return 9;
		}
		@Override
		public int getNumber() {
			return 60;
		}
	},
	/** CNU 61.
	 */
	CNU_61 {
		@Override
		public int getGroup() {
			return 9;
		}
		@Override
		public int getNumber() {
			return 61;
		}
	},
	/** CNU 62.
	 */
	CNU_62 {
		@Override
		public int getGroup() {
			return 9;
		}
		@Override
		public int getNumber() {
			return 62;
		}
	},
	/** CNU 63.
	 */
	CNU_63 {
		@Override
		public int getGroup() {
			return 9;
		}
		@Override
		public int getNumber() {
			return 63;
		}
	},
	/** CNU 64.
	 */
	CNU_64 {
		@Override
		public int getGroup() {
			return 10;
		}
		@Override
		public int getNumber() {
			return 64;
		}
	},
	/** CNU 65.
	 */
	CNU_65 {
		@Override
		public int getGroup() {
			return 10;
		}
		@Override
		public int getNumber() {
			return 65;
		}
	},
	/** CNU 66.
	 */
	CNU_66 {
		@Override
		public int getGroup() {
			return 10;
		}
		@Override
		public int getNumber() {
			return 66;
		}
	},
	/** CNU 67.
	 */
	CNU_67 {
		@Override
		public int getGroup() {
			return 10;
		}
		@Override
		public int getNumber() {
			return 67;
		}
	},
	/** CNU 68.
	 */
	CNU_68 {
		@Override
		public int getGroup() {
			return 10;
		}
		@Override
		public int getNumber() {
			return 68;
		}
	},
	/** CNU 69.
	 */
	CNU_69 {
		@Override
		public int getGroup() {
			return 10;
		}
		@Override
		public int getNumber() {
			return 69;
		}
	},
	/** CNU 70.
	 */
	CNU_70 {
		@Override
		public int getGroup() {
			return 12;
		}
		@Override
		public int getNumber() {
			return 70;
		}
	},
	/** CNU 71.
	 */
	CNU_71 {
		@Override
		public int getGroup() {
			return 12;
		}
		@Override
		public int getNumber() {
			return 71;
		}
	},
	/** CNU 72.
	 */
	CNU_72 {
		@Override
		public int getGroup() {
			return 12;
		}
		@Override
		public int getNumber() {
			return 72;
		}
	},
	/** CNU 73.
	 */
	CNU_73 {
		@Override
		public int getGroup() {
			return 12;
		}
		@Override
		public int getNumber() {
			return 73;
		}
	},
	/** CNU 74.
	 */
	CNU_74 {
		@Override
		public int getGroup() {
			return 12;
		}
		@Override
		public int getNumber() {
			return 74;
		}
	},
	/** CNU 76.
	 */
	CNU_76 {
		@Override
		public int getGroup() {
			return 0;
		}
		@Override
		public int getNumber() {
			return 76;
		}
	},
	/** CNU 77.
	 */
	CNU_77 {
		@Override
		public int getGroup() {
			return 0;
		}
		@Override
		public int getNumber() {
			return 77;
		}
	},
	/** CNU 85.
	 */
	CNU_85 {
		@Override
		public int getGroup() {
			return 0;
		}
		@Override
		public int getNumber() {
			return 85;
		}
	},
	/** CNU 86.
	 */
	CNU_86 {
		@Override
		public int getGroup() {
			return 0;
		}
		@Override
		public int getNumber() {
			return 86;
		}
	},
	/** CNU 87.
	 */
	CNU_87 {
		@Override
		public int getGroup() {
			return 0;
		}
		@Override
		public int getNumber() {
			return 87;
		}
	};

	private static final String MESSAGE_PREFIX = "cnuSection."; //$NON-NLS-1$

	private MessageSourceAccessor messages;

	/** Replies the message accessor to be used.
	 *
	 * @return the accessor.
	 */
	public MessageSourceAccessor getMessageSourceAccessor() {
		if (this.messages == null) {
			this.messages = BaseMessageSource.getStaticMessageSourceAccessor();
		}
		return this.messages;
	}

	/** Change the message accessor to be used.
	 *
	 * @param messages the accessor.
	 */
	public void setMessageSourceAccessor(MessageSourceAccessor messages) {
		this.messages = messages;
	}

	/** Replies the label of the CNU section in the current language.
	 *
	 * @return the label of the CNU section in the current language.
	 */
	public String getLabel() {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name());
		return Strings.nullToEmpty(label);
	}

	/** Replies the label of the status in the current language.
	 *
	 * @param locale the locale to use.
	 * @return the label of the status in the current language.
	 */
	public String getLabel(Locale locale) {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name(), locale);
		return Strings.nullToEmpty(label);
	}

	/** Replies the CNU section  that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the CNU section, to search for.
	 * @return the CNU section.
	 * @throws IllegalArgumentException if the given name does not corresponds to a CNU section.
	 */
	public static CnuSection valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final CnuSection section : values()) {
				if (name.equalsIgnoreCase(section.name())) {
					return section;
				}
			}
		}
		throw new IllegalArgumentException("Invalid CNU section: " + name); //$NON-NLS-1$
	}

	/** Replies the CNU section that corresponds to the given number.
	 *
	 * @param number the number of the CNU section, to search for.
	 * @return the CNU section.
	 * @throws IllegalArgumentException if the given number does not corresponds to a CNU section.
	 */
	public static CnuSection valueOf(Number number) {
		if (number != null) {
			final int n = number.intValue();
			return valueOf(n);
		}
		throw new IllegalArgumentException("Invalid CNU section: " + number); //$NON-NLS-1$
	}

	/** Replies the CNU section that corresponds to the given number.
	 *
	 * @param number the number of the CNU section, to search for.
	 * @return the CNU section.
	 * @throws IllegalArgumentException if the given number does not corresponds to a CNU section.
	 */
	public static CnuSection valueOf(int number) {
		for (final CnuSection section : values()) {
			if (number == section.getNumber()) {
				return section;
			}
		}
		throw new IllegalArgumentException("Invalid CNU section: " + number); //$NON-NLS-1$
	}

	/** Replies the CNU group in which this section is is. 
	 *
	 * @return the CNU group, or {@code 0} if the group is undefined.
	 */
	public abstract int getGroup();

	/** Replies the number of the CNU section. 
	 *
	 * @return the CNU section number, or {@code 0} if the number is undefined.
	 */
	public abstract int getNumber();

}
