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

package fr.utbm.ciad.labmanager.utils.trl;

import java.util.Locale;

import com.google.common.base.Strings;
import fr.utbm.ciad.labmanager.configuration.BaseMessageSource;
import org.springframework.context.support.MessageSourceAccessor;

/** Technology readiness levels (TRLs) are a method for estimating the maturity of technologies during the acquisition phase
 * of a program. TRLs enable consistent and uniform discussions of technical maturity across different types of technology.
 * 
 * @author $Author: sgalland$
 * @author $Author: anoubli$
 * @author $Author: bpdj$
 * @author $Author: pgoubet$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.0
 * @see "https://en.wikipedia.org/wiki/Technology_readiness_level"
 */
public enum TRL {

	/**
	 * Basic principles observed.
	 */
	TRL1,

	/**
	 * Technology concept formulated.
	 */
	TRL2,

	/**
	 * Experimental proof of concept.
	 */
	TRL3,

	/**
	 * Technology validated in lab.
	 */
	TRL4,

	/**
	 * Technology validated in relevant environment (industrially relevant environment in the case of key enabling technologies).
	 */
	TRL5,

	/**
	 * Technology demonstrated in relevant environment (industrially relevant environment in the case of key enabling technologies).
	 */
	TRL6,
	
	/**
	 * System prototype demonstration in operational environment.
	 */
	TRL7,

	/**
	 * System complete and qualified.
	 */
	TRL8,

	/** Actual system proven in operational environment (competitive manufacturing in the case of key enabling technologies; or in space).
	 */
	TRL9;
	
	private static final String MESSAGE_PREFIX = "trl."; //$NON-NLS-1$
	
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

	/** Replies the label of the project status in the current language.
	 *
	 * @return the label of the project status in the current language.
	 */
	public String getLabel() {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name());
		return Strings.nullToEmpty(label);
	}

	/** Replies the label of the project status in the given language.
	 *
	 * @param locale the locale to use.
	 * @return the label of the project status in the given  language.
	 */
	public String getLabel(Locale locale) {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name(), locale);
		return Strings.nullToEmpty(label);
	}

	/** Replies the TRL level. It is the ordinal number plus one.
	 *
	 * @return the TRL level.
	 */
	public int getLevel() {
		return ordinal() + 1;
	}

	/** Parse the given string for obtaining the TRL.
	 * 
	 * @param stringTrl the string representation of the TRL.
	 * @return the TRL or {@code null} if the given string cannot match.
	 */
    public static TRL valueOfCaseInsensitive(String stringTrl) {
    	if (!Strings.isNullOrEmpty(stringTrl)) {
	    	for (final TRL candidate : values()) {
	    		if (candidate.toString().equalsIgnoreCase(stringTrl)
	    				|| Integer.toString(candidate.getLevel()).equals(stringTrl)) {
	    			return candidate;
	    		}
	    	}
    	}
    	return null;
    }

}
