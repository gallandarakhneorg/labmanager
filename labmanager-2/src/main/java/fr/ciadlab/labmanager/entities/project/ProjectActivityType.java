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

package fr.ciadlab.labmanager.entities.project;

import java.util.Locale;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.BaseMessageSource;
import org.springframework.context.support.MessageSourceAccessor;

/** Describe the type of activity that is related to a project.
 * 
 * @author $Author: sgalland$
 * @author $Author: anoubli$
 * @author $Author: bpdj$
 * @author $Author: pgoubet$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.0
 * @see "https://anr.fr/fileadmin/documents/2017/guide-CIR-2017.pdf"
 */
public enum ProjectActivityType {

	/** Activity is fundamental research for acquiring new knowledge.
	 */
	FUNDAMENTAL_RESEARCH,

	/** Activity is applied research for acquiring new knowledge in an application domain.
	 */
	APPLIED_RESEARCH,

	/** Activity is experimental development that applies technical elements for: (i) the creation
	 * of new software product, (ii) the creation of new methods, processes, services or systems,
	 * (iii) the substantial improvement of existing elements. 
	 */
	EXPERIMENTAL_DEVELOPMENT;

	private static final String MESSAGE_PREFIX = "projectActivityType."; //$NON-NLS-1$
	
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
	public static ProjectActivityType valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final ProjectActivityType ranking : values()) {
				if (name.equalsIgnoreCase(ranking.name())) {
					return ranking;
				}
			}
		}
		throw new IllegalArgumentException("Invalid activity type: " + name); //$NON-NLS-1$
	}

}