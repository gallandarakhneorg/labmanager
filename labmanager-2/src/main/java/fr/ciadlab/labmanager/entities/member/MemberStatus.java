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

/** Status of a person in a research organization.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum MemberStatus {
	/** Full professor (professeur des universites).
	 */
	FULL_PROFESSOR {
		@Override
		public int getHierachicalLevel() {
			return 0;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return true;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
	},
	/** Research Director (Directeur de recherche).
	 */
	RESEARCH_DIRECTOR {
		@Override
		public int getHierachicalLevel() {
			return 0;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
	},
	/** Associate professor (maitre de conferences) with HDR.
	 */
	ASSOCIATE_PROFESSOR_HDR {
		@Override
		public int getHierachicalLevel() {
			return 1;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return true;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
	},
	/** Associate professor (maitre de conferences) without HDR.
	 */
	ASSOCIATE_PROFESSOR {
		@Override
		public int getHierachicalLevel() {
			return 2;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return true;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
	},
	/** Contractual teacher-researcher (enseignant chercheur contractuel).
	 */
	CONTRACTUAL_RESEARCHER_TEACHER {
		@Override
		public int getHierachicalLevel() {
			return 2;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return true;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
	},
	/** Researcher (Chargé de recherche, LRU, etc.).
	 */
	RESEARCHER {
		@Override
		public int getHierachicalLevel() {
			return 2;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
	},
	/** Postdoc.
	 */
	POSTDOC {
		@Override
		public int getHierachicalLevel() {
			return 3;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
	},
	/** Research engineer (IGR).
	 */
	RESEARCH_ENGINEER {
		@Override
		public int getHierachicalLevel() {
			return 3;
		}
		@Override
		public boolean isResearcher() {
			return false;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return true;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
	},
	/** PHD student.
	 */
	PHD_STUDENT {
		@Override
		public int getHierachicalLevel() {
			return 4;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
	},
	/** Engineer (IGE).
	 */
	ENGINEER {
		@Override
		public int getHierachicalLevel() {
			return 4;
		}
		@Override
		public boolean isResearcher() {
			return false;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return true;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
	},
	/** Administrative staff.
	 */
	ADMIN {
		@Override
		public int getHierachicalLevel() {
			return 4;
		}
		@Override
		public boolean isResearcher() {
			return false;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return true;
		}
	},
	/** Teacher (PRAG, etc.).
	 */
	TEACHER {
		@Override
		public int getHierachicalLevel() {
			return 4;
		}
		@Override
		public boolean isResearcher() {
			return false;
		}
		@Override
		public boolean isTeacher() {
			return true;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
	},
	/** Master student (internship).
	 */
	MASTER_STUDENT {
		@Override
		public int getHierachicalLevel() {
			return 5;
		}
		@Override
		public boolean isResearcher() {
			return false;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
	},
	/** Bachelor student (internship).
	 */
	OTHER_STUDENT {
		@Override
		public int getHierachicalLevel() {
			return 6;
		}
		@Override
		public boolean isResearcher() {
			return false;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
	},
	/** Associate member.
	 */
	ASSOCIATED_MEMBER {
		@Override
		public int getHierachicalLevel() {
			return 7;
		}
		@Override
		public boolean isResearcher() {
			return true;
		}
		@Override
		public boolean isTeacher() {
			return false;
		}
		@Override
		public boolean isTechnicalStaff() {
			return false;
		}
		@Override
		public boolean isAdministrativeStaff() {
			return false;
		}
	};

	private static final String MESSAGE_PREFIX = "memberStatus."; //$NON-NLS-1$

	private static final String TITLE_POSTFIX = "_title"; //$NON-NLS-1$

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

	/** Replies the hierarchical level.
	 *
	 * @return the level: {@code 0} for the higher level, higher is the value lower is the hierarchical position.
	 */
	public abstract int getHierachicalLevel();

	/** Replies if the status includes research activities.
	 *
	 * @return {@code true} if the status includes activities of research.
	 */
	public abstract boolean isResearcher();

	/** Replies if the status includes teaching activities.
	 *
	 * @return {@code true} if the status includes activities of teaching.
	 */
	public abstract boolean isTeacher();

	/** Replies if the status includes engineering or technical activities.
	 *
	 * @return {@code true} if the status includes engineering or technical activities.
	 */
	public abstract boolean isTechnicalStaff();

	/** Replies if the status includes administrative activities.
	 *
	 * @return {@code true} if the status includes administrative activities.
	 */
	public abstract boolean isAdministrativeStaff();

	/** Replies the label of the status in the current language.
	 *
	 * @return the label of the status in the current language.
	 */
	public String getLabel() {
		return getLabel((Gender) null);
	}

	/** Replies the label of the status in the current language.
	 *
	 * @param locale the locale to use.
	 * @return the label of the status in the current language.
	 */
	public String getLabel(Locale locale) {
		return getLabel(null, locale);
	}


	/** Replies the label of the status in the current language.
	 *
	 * @param gender the gender of the person concerned by the member status.
	 * @return the label of the status in the current language.
	 */
	public String getLabel(Gender gender) {
		final Gender gndr = gender == null || gender == Gender.NOT_SPECIFIED ? Gender.OTHER : gender;
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name() + "_" + gndr.name()); //$NON-NLS-1$
		return Strings.nullToEmpty(label);
	}

	/** Replies the label of the status in the current language.
	 *
	 * @param gender the gender of the person concerned by the member status.
	 * @param locale the locale to use.
	 * @return the label of the status in the current language.
	 */
	public String getLabel(Gender gender, Locale locale) {
		final Gender gndr = gender == null || gender == Gender.NOT_SPECIFIED ? Gender.OTHER : gender;
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name() + "_" + gndr.name(), locale); //$NON-NLS-1$
		return Strings.nullToEmpty(label);
	}

	/** Replies the member status that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the status, to search for.
	 * @return the status.
	 * @throws IllegalArgumentException if the given name does not corresponds to a type.
	 */
	public static MemberStatus valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final MemberStatus status : values()) {
				if (name.equalsIgnoreCase(status.name())) {
					return status;
				}
			}
		}
		throw new IllegalArgumentException("Invalid member status: " + name); //$NON-NLS-1$
	}

	/** Replies the civil title associated to this status, if it not a civil title based on gender.
	 *
	 * @return the civil title or {@code null} if none is defined.
	 * @see Gender
	 */
	public String getCivilTitle() {
		return Strings.emptyToNull(getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name() + TITLE_POSTFIX, (String) null));
	}

	/** Replies the civil title associated to this status, if it not a civil title based on gender.
	 *
	 * @param locale the locale that must be used for creating the localized civil title. 
	 * @return the civil title or {@code null} if none is defined.
	 * @see Gender
	 */
	public String getCivilTitle(Locale locale) {
		return Strings.emptyToNull(getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name() + TITLE_POSTFIX, (String) null, locale));
	}

}
