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

package fr.ciadlab.labmanager.entities.assostructure;

import java.util.Locale;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.BaseMessageSource;
import org.springframework.context.support.MessageSourceAccessor;

/** Describe the type of associated structure.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.2
 */
public enum AssociatedStructureType {

	/** Private company, e.g., Sping-Off...
	 */
	PRIVATE_COMPANY,

	/** Industrial chair with a company.
	 */
	INDUSTRIAL_CHAIR,

	/** Reseach chair.
	 */
	RESEARCH_CHAIR,

	/** Shared international research lab.
	 */
	INTERNATIONAL_RESEARCH_LAB,

	/** Shared international group of scientific interest.
	 */
	INTERNATIONAL_SCIENTIFIC_INTEREST_GROUP,

	/** Shared national research lab.
	 */
	NATIONAL_RESEARCH_LAB,

	/** Shared national group of scientific interest.
	 */
	NATIONAL_SCIENTIFIC_INTEREST_GROUP;

	private static final String MESSAGE_PREFIX = "associatedStructureType."; //$NON-NLS-1$
	
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

	/** Replies the project status that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the project status, to search for.
	 * @return the project status.
	 * @throws IllegalArgumentException if the given name does not corresponds to a project status.
	 */
	public static AssociatedStructureType valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final AssociatedStructureType ranking : values()) {
				if (name.equalsIgnoreCase(ranking.name())) {
					return ranking;
				}
			}
		}
		throw new IllegalArgumentException("Invalid project status: " + name); //$NON-NLS-1$
	}

}
