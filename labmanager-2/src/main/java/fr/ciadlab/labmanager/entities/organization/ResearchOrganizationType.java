/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
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

package fr.ciadlab.labmanager.entities.organization;

import java.util.Locale;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.BaseMessageSource;
import org.springframework.context.support.MessageSourceAccessor;

/** Type of research organization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum ResearchOrganizationType {
	/** Research team.
	 */
	RESEARCH_TEAM {
		@Override
		public boolean isAcademicType() {
			return true;
		}
		@Override
		public boolean isEmployer() {
			return false;
		}
	},

	/** Research department.
	 */
	LABORATORY_DEPARTMENT {
		@Override
		public boolean isAcademicType() {
			return true;
		}
		@Override
		public boolean isEmployer() {
			return false;
		}
	},

	/** Research laboratory or institute.
	 */
	LABORATORY {
		@Override
		public boolean isAcademicType() {
			return true;
		}
		@Override
		public boolean isEmployer() {
			return false;
		}
	},

	/** Faculty.
	 */
	FACULTY {
		@Override
		public boolean isAcademicType() {
			return true;
		}
		@Override
		public boolean isEmployer() {
			return false;
		}
	},

	/** Research institute that is part of a research institution.
	 *
	 * @since 3.1
	 */
	RESEARCH_INSTITUTE {
		@Override
		public boolean isAcademicType() {
			return true;
		}
		@Override
		public boolean isEmployer() {
			return false;
		}
	},

	/** Research organization/institution, e.g., National Research Center.
	 *
	 * @since 3.1
	 */
	RESEARCH_INSTITUTION {
		@Override
		public boolean isAcademicType() {
			return true;
		}
		@Override
		public boolean isEmployer() {
			return true;
		}
	},

	/** University or college.
	 */
	UNIVERSITY {
		@Override
		public boolean isAcademicType() {
			return true;
		}
		@Override
		public boolean isEmployer() {
			return true;
		}
	},

	/** High school.
	 */
	HIGH_SCHOOL {
		@Override
		public boolean isAcademicType() {
			return true;
		}
		@Override
		public boolean isEmployer() {
			return true;
		}
	},

	/** University community or network.
	 */
	COMMUNITY {
		@Override
		public boolean isAcademicType() {
			return true;
		}
		@Override
		public boolean isEmployer() {
			return true;
		}
	},

	/** Any public administration or service that depends from the government or any local public authority.
	 *
	 * @since 3.1
	 */
	PUBLIC_ADMINISTRATION {
		@Override
		public boolean isAcademicType() {
			return false;
		}
		@Override
		public boolean isEmployer() {
			return true;
		}
	},

	/** Public association or non-profit organization.
	 *
	 * @since 3.1
	 */
	PUBLIC_NON_PROFIT_ASSOCIATION {
		@Override
		public boolean isAcademicType() {
			return false;
		}
		@Override
		public boolean isEmployer() {
			return true;
		}
	},

	/** Private association or non-profit organization.
	 *
	 * @since 3.1
	 */
	PRIVATE_NON_PROFIT_ASSOCIATION {
		@Override
		public boolean isAcademicType() {
			return false;
		}
		@Override
		public boolean isEmployer() {
			return true;
		}
	},

	/** A company that is just created or incubated.
	 *
	 * @since 3.1
	 */
	START_UP_COMPANY {
		@Override
		public boolean isAcademicType() {
			return false;
		}
		@Override
		public boolean isEmployer() {
			return true;
		}
	},

	/** Very small company, e.g., less than 10 employees.
	 *
	 * @since 3.1
	 */
	VERY_SMALL_SIZE_COMPANY {
		@Override
		public boolean isAcademicType() {
			return false;
		}
		@Override
		public boolean isEmployer() {
			return true;
		}
	},

	/** Small or middle-size company, e.g., less than 250 employees.
	 *
	 * @since 3.1
	 */
	SMALL_SIZE_COMPANY {
		@Override
		public boolean isAcademicType() {
			return false;
		}
		@Override
		public boolean isEmployer() {
			return true;
		}
	},

	/** Intermediate-size company, e.g., less than 5000 employees.
	 *
	 * @since 3.1
	 */
	INTERMEDIATE_SIZE_COMPANY {
		@Override
		public boolean isAcademicType() {
			return false;
		}
		@Override
		public boolean isEmployer() {
			return true;
		}
	},

	/** Big-size company, e.g., equal to or more than 5000 employees.
	 *
	 * @since 3.1
	 */
	BIG_SIZE_COMPANY {
		@Override
		public boolean isAcademicType() {
			return false;
		}
		@Override
		public boolean isEmployer() {
			return true;
		}
	},

	/** Other type of organization.
	 */
	OTHER {
		@Override
		public boolean isAcademicType() {
			return false;
		}
		@Override
		public boolean isEmployer() {
			return true;
		}
	};

	/** Default organization type.
	 */
	public static final ResearchOrganizationType DEFAULT = LABORATORY;

	private static final String MESSAGE_PREFIX = "researchOrganizationType."; //$NON-NLS-1$

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

	/** Replies the label of the type of organization.
	 *
	 * @return the label of the type of organization.
	 */
	public String getLabel() {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name());
		return Strings.nullToEmpty(label);
	}

	/** Replies the label of the type of organization.
	 *
	 * @param locale the locale to use.
	 * @return the label of the type of organization.
	 */
	public String getLabel(Locale locale) {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name(), locale);
		return Strings.nullToEmpty(label);
	}

	/** Replies the type of organization that corresponds to the given name, with a case-insensitive
	 * test of the name.
	 *
	 * @param name the name of the type of organization, to search for.
	 * @return the status.
	 * @throws IllegalArgumentException if the given name does not corresponds to a type.
	 */
	public static ResearchOrganizationType valueOfCaseInsensitive(String name) {
		if (!Strings.isNullOrEmpty(name)) {
			for (final ResearchOrganizationType status : values()) {
				if (name.equalsIgnoreCase(status.name())) {
					return status;
				}
			}
		}
		throw new IllegalArgumentException("Invalid type of organization: " + name); //$NON-NLS-1$
	}

	/** Replies if this type of organization is for academic environment.
	 *
	 * @return {@code true} if the type is for academic organization.
	 * @since 3.1
	 */
	public abstract boolean isAcademicType();

	/** Replies if this type of organization corresponds to an employer.
	 *
	 * @return {@code true} if the type is for an employer organization.
	 * @since 3.6
	 */
	public abstract boolean isEmployer();

}
