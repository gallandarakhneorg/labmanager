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

/** Type of responsibility taken by a member of an organization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum Responsibility {

	/** President or chief executive officer of the organization.
	 */
	PRESIDENT,

	/** Dean of the organization.
	 */
	DEAN,

	/** Executive Director of the organization. A executive director is usually an employee of the organization.
	 */
	EXECUTIVE_DIRECTOR,

	/** Director of the organization. A director is usually not an employee of the organization.
	 */
	DIRECTOR,

	/** Vice-president of the organization.
	 */
	VICE_PRESIDENT,

	/** Deputy director of the organization.
	 */
	DEPUTY_DIRECTOR,

	/** Research vice-president of the organization.
	 */
	RESEARCH_VICE_PRESIDENT,

	/** Research director of the organization.
	 */
	RESEARCH_DIRECTOR,

	/** Numeric service vice-president of the organization.
	 */
	NUMERIC_SERVICE_VICE_PRESIDENT,

	/** Numeric service director of the organization.
	 */
	NUMERIC_SERVICE_DIRECTOR,

	/** Director for pedagogy of the organization.
	 */
	PEDAGOGY_DIRECTOR,

	/** Communication vice-president of the organization.
	 */
	COMMUNICATION_VICE_PRESIDENT,

	/** Communication director of the organization.
	 */
	COMMUNICATION_DIRECTOR,

	/** Faculty director of the organization.
	 */
	FACULTY_DIRECTOR,

	/** Faculty dean of the organization.
	 */
	FACULTY_DEAN,

	/** Faculty director of the organization.
	 */
	DEPARTMENT_DIRECTOR,

	/** Administration council vice-president of the organization.
	 */
	ADMINISTRATION_COUNCIL_VICE_PRESIDENT,

	/** Scientific council vice-president of the organization.
	 */
	SCIENTIFIC_COUNCIL_VICE_PRESIDENT,

	/** Teaching council vice-president of the organization.
	 */
	PEDAGOGY_COUNCIL_VICE_PRESIDENT,

	/** Member of the administration council of the organization.
	 */
	ADMINISTRATION_COUNCIL_MEMBER,

	/** Member of the scientific council of the organization.
	 */
	SCIENTIFIC_COUNCIL_MEMBER,

	/** Member of the pedagogy council of the organization.
	 */
	PEDAGOGY_COUNCIL_MEMBER,

	/** Member of the numeric council of the organization.
	 */
	NUMERIC_COUNCIL_MEMBER,

	/** Responsible of business unit of the organization.
	 */
	BUSINESS_UNIT_RESPONSIBLE,

	/** Leader of a team of the organization.
	 */
	TEAM_SCIENTFIC_RESPONSIBLE,

	/** Leader of a scientific axis of the organization.
	 */
	SCIENTIFIC_AXIS_RESPONSIBLE,

	/** Leader of a application axis of the organization.
	 */
	APPLICATION_AXIS_RESPONSIBLE,

	/** Head of a platform of the organization.
	 */
	PLATFORM_RESPONSIBLE,

	/** Head of a transverse axis of the organization.
	 */
	TRANSVERSE_AXIS_RESPONSIBLE,

	/** Direction secretary of the organization.
	 */
	DIRECTION_SECRETARY,

	/** Responsible of IT of the organization.
	 */
	IT_RESPONSIBLE,

	/** Seminar and conference responsible of the organization.
	 */
	SEMINAR_RESPONSIBLE,

	/** Gender-equality responsible of the organization.
	 */
	GENDER_EQUALITY_RESPONSIBLE,

	/** Human-resource responsible of the organization.
	 */
	HUMAN_RESOURCE_RESPONSIBLE,

	/** Responsible of Open Science of the organization.
	 */
	OPEN_SCIENCE_RESPONSIBLE,

	/** Communication responsible of the organization.
	 */
	COMMUNICATION_RESPONSIBLE;

	private static final String MESSAGE_PREFIX = "positionType."; //$NON-NLS-1$

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

	/** Replies the label of the status in the current language.
	 *
	 * @param gender the gender of the person who has the position of this type.
	 * @return the label of the status in the current language.
	 */
	public String getLabel(Gender gender) {
		Gender g = gender;
		if (g == null || g == Gender.NOT_SPECIFIED) {
			g = Gender.MALE;
		}
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name() + "_" + g.name()); //$NON-NLS-1$
		return Strings.nullToEmpty(label);
	}

	/** Replies the label of the status in the current language.
	 *
	 * @param gender the gender of the person who has the position of this type.
	 * @param locale the locale to use.
	 * @return the label of the status in the current language.
	 */
	public String getLabel(Gender gender, Locale locale) {
		Gender g = gender;
		if (g == null || g == Gender.NOT_SPECIFIED) {
			g = Gender.MALE;
		}
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name() + "_" + g.name(), locale); //$NON-NLS-1$
		return Strings.nullToEmpty(label);
	}

	/** Replies the member status that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the status, to search for.
	 * @return the status.
	 * @throws IllegalArgumentException if the given name does not corresponds to a type.
	 */
	public static Responsibility valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final Responsibility status : values()) {
				if (name.equalsIgnoreCase(status.name())) {
					return status;
				}
			}
		}
		throw new IllegalArgumentException("Invalid position type: " + name); //$NON-NLS-1$
	}

}
