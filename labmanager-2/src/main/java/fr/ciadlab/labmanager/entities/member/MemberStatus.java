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

import com.google.common.base.Strings;
import org.arakhne.afc.vmutil.locale.Locale;

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
	/** Researcher (LRU, etc.).
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
		final String label = Locale.getString(MemberStatus.class, name());
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

}
