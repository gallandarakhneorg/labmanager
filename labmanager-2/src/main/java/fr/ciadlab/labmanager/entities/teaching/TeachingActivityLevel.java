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

package fr.ciadlab.labmanager.entities.teaching;

import java.util.Locale;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.BaseMessageSource;
import org.springframework.context.support.MessageSourceAccessor;

/** Describe the level of teaching activity.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.4
 */
public enum TeachingActivityLevel {

	/** Activities are at the doctoral level.
	 */
	DOCTORAL_DEGREE,

	/** Activities are at the master level.
	 */
	MASTER_DEGREE,

	/** Activities are at the bachelor level (Licence in French).
	 */
	BACHELOR_DEGREE,

	/** Activities are at the level of high schools (lycées en French).
	 */
	HIGH_SCHOOL_DEGREE;

	private static final String MESSAGE_PREFIX = "teachingActivityLevel."; //$NON-NLS-1$
	
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

	/** Replies the label of the activity level in the current language.
	 *
	 * @return the label of the activity level in the current language.
	 */
	public String getLabel() {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name());
		return Strings.nullToEmpty(label);
	}

	/** Replies the label of the activity level in the given language.
	 *
	 * @param locale the locale to use.
	 * @return the label of the activity level in the given  language.
	 */
	public String getLabel(Locale locale) {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name(), locale);
		return Strings.nullToEmpty(label);
	}

	/** Replies the level of activity that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the activity level, to search for.
	 * @return the level of activity.
	 * @throws IllegalArgumentException if the given name does not corresponds to a level of activity.
	 */
	public static TeachingActivityLevel valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final TeachingActivityLevel ranking : values()) {
				if (name.equalsIgnoreCase(ranking.name())) {
					return ranking;
				}
			}
		}
		throw new IllegalArgumentException("Invalid activity level: " + name); //$NON-NLS-1$
	}

}
