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

/** Describe the type of teaching activity.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.4
 */
public enum TeachingActivityType {

	/** Lectures (or Cours Magistral in French) are, in most cases, theoretical courses in an amphitheater which bring together all the
	 * students of a class.
	 */
	LECTURES(1.5f, 1.5f),

	/** Integrated courses (or Cours Intégrés in French) are courses in which the exercises are immediately integrated
	 * by way of explanation. Knowledge is implemented alongside learning.
	 */
	INTEGRATED_COURSES(1.25f, 1.25f),

	/** Turotials (or Travaux dirigés in Frencj) are an opportunity to meet in small groups and to deepen the
	 * notions covered in {@link #LECTURE lectures}, to put into practice the methods that will be required
	 * for the exam and above all to ask questions.
	 */
	TUTORIALS(1f, 1f),

	/** Practical works (or Travaux pratiques in French) are made up of a small number of students. They are an opportunity
	 * to carry out experiments and put into practice the theory learned in class.
	 */
	PRACTICAL_WORKS(1f, 2f/3f),

	/** Supervision of student groups.
	 */
	GROUP_SUPERVISION(1f, 1f),

	/** Supervision of student projects.
	 */
	PROJECT_SUPERVISION(1f, 2f/3f);

	private static final String MESSAGE_PREFIX = "teachingActivityType."; //$NON-NLS-1$
	
	private MessageSourceAccessor messages;

	private final float internalContractHetdFactor;
	
	private final float externalContractHetdFactor;

	private TeachingActivityType(float permanentFactor, float externalFactor) {
		this.internalContractHetdFactor = permanentFactor;
		this.externalContractHetdFactor = externalFactor;
	}
	
	/** Replies the number of hETD (equivalent number of hours for tutorials) for a single hour of this type of activity.
	 *
	 * @param differentHetdFactors indicates if the contract for the person implies that his/her has different factors for
	 *     computing hETD from the real teaching hours.
	 * @return the hETD factor.
	 */
	public float getHetdFactor(boolean differentHetdFactors) {
		return differentHetdFactors ? this.externalContractHetdFactor : this.internalContractHetdFactor;
	}
	
	/** Convert a given number of hours to its equivalent number of hETD.
	 *
	 * @param hours the number of hours to convert.
	 * @param differentHetdFactors indicates if the contract for the person implies that his/her has different factors for
	 *     computing hETD from the real teaching hours.
	 * @return the hETD number.
	 */
	public float convertHoursToHetd(float hours, boolean differentHetdFactors) {
		return getHetdFactor(differentHetdFactors) * hours;
	}

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

	/** Replies the label of the activity type in the current language.
	 *
	 * @return the label of the activity type in the current language.
	 */
	public String getLabel() {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name());
		return Strings.nullToEmpty(label);
	}

	/** Replies the label of the activity type in the given language.
	 *
	 * @param locale the locale to use.
	 * @return the label of the activity type in the given  language.
	 */
	public String getLabel(Locale locale) {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name(), locale);
		return Strings.nullToEmpty(label);
	}

	/** Replies the type of activity that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the activity type, to search for.
	 * @return the type of activity.
	 * @throws IllegalArgumentException if the given name does not corresponds to a type of activity.
	 */
	public static TeachingActivityType valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final TeachingActivityType ranking : values()) {
				if (name.equalsIgnoreCase(ranking.name())) {
					return ranking;
				}
			}
		}
		throw new IllegalArgumentException("Invalid activity type: " + name); //$NON-NLS-1$
	}

}
