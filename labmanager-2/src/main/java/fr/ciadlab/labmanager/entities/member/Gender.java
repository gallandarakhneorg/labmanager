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

package fr.ciadlab.labmanager.entities.member;

import java.util.Locale;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.BaseMessageSource;
import org.springframework.context.support.MessageSourceAccessor;

/** Gender of a person.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public enum Gender {
	/** Unknown gender.
	 */
	NOT_SPECIFIED,
	/** Male.
	 */
	MALE,
	/** Female.
	 */
	FEMALE,
	/** Other gender.
	 */
	OTHER;

	private static final String MESSAGE_PREFIX = "gender."; //$NON-NLS-1$

	private static final String CIVIL_TITLE_POSTFIX = "_title"; //$NON-NLS-1$

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

	/** Replies the label of the gender in the current language.
	 *
	 * @return the label of the gender in the current language.
	 */
	public String getLabel() {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name());
		return Strings.nullToEmpty(label);
	}

	/** Replies the label of the gender in the current language.
	 *
	 * @param locale the locale to use.
	 * @return the label of the gender in the current language.
	 */
	public String getLabel(Locale locale) {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name(), locale);
		return Strings.nullToEmpty(label);
	}

	/** Replies the civil title of the gender in the current language.
	 *
	 * @return the civil title or {@code null} if none is applicable.
	 */
	public String getCivilTitle() {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name() + CIVIL_TITLE_POSTFIX, (String) null);
		return Strings.nullToEmpty(label);
	}

	/** Replies the civil title of the gender in the current language.
	 *
	 * @param locale the locale to use.
	 * @return the civil title or {@code null} if none is applicable.
	 */
	public String getCivilTitle(Locale locale) {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name() + CIVIL_TITLE_POSTFIX, (String) null, locale);
		return Strings.nullToEmpty(label);
	}

	/** Replies the gender that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the gender, to search for.
	 * @return the status.
	 * @throws IllegalArgumentException if the given name does not corresponds to a type.
	 */
	public static Gender valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final Gender gender : values()) {
				if (name.equalsIgnoreCase(gender.name())) {
					return gender;
				}
			}
		}
		throw new IllegalArgumentException("Invalid gender: " + name); //$NON-NLS-1$
	}

}
