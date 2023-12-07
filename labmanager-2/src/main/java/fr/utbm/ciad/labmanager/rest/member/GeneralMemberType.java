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

package fr.utbm.ciad.labmanager.rest.member;

import java.util.Locale;

import com.google.common.base.Strings;
import fr.utbm.ciad.labmanager.configuration.BaseMessageSource;
import fr.utbm.ciad.labmanager.data.member.MemberStatus;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.member.Responsibility;
import org.springframework.context.support.MessageSourceAccessor;

/** Describe the type of a member for the member list front end.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum GeneralMemberType {
	/** Laboratory Direction.
	 */
	DIRECTION,

	/** Researchers.
	 */
	RESEARCHERS,

	/** Associated members.
	 */
	ASSOCIATED_MEMBERS,

	/** Post-Docs.
	 */
	POSTDOCS,

	/** PhD students.
	 */
	PHDS,

	/** Members that are engineers.
	 */
	ENGINEERS,

	/** Members that are not inside in one of the other general member types.
	 */
	OTHER_MEMBERS,

	/** Master students.
	 */
	MASTER_STUDENTS,

	/** Former members.
	 */
	FORMER_MEMBERS;

	private static final String MESSAGE_PREFIX = "generalMemberType."; //$NON-NLS-1$

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

	/** Replies the label of the general member type in the current language.
	 *
	 * @return the label of the general member type in the current language.
	 */
	public String getLabel() {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name());
		return Strings.nullToEmpty(label);
	}

	/** Replies the label of the general member type in the current language.
	 *
	 * @param locale the locale to use.
	 * @return the label of the general member type in the current language.
	 */
	public String getLabel(Locale locale) {
		final String label = getMessageSourceAccessor().getMessage(MESSAGE_PREFIX + name(), locale);
		return Strings.nullToEmpty(label);
	}

	/** Replies the general member type that corresponds to the given membership.
	 *
	 * @param membership the membership to analyze.
	 * @return the type.
	 */
	public static GeneralMemberType fromMembership(Membership membership) {
		assert membership != null;
		if (membership.isFuture()) {
			return null;
		}
		// Classification on the responsabilities
		Responsibility res = membership.getResponsibility();
		if (res != null && res.isDirectionLevel()) {
			return GeneralMemberType.DIRECTION;
		}
		// Classification on member status
		final MemberStatus ms = membership.getMemberStatus();
		if (membership.isActive()) {
			if (ms.isResearcher()) {
				if (ms == MemberStatus.ASSOCIATED_MEMBER || ms == MemberStatus.ASSOCIATED_MEMBER_PHD) {
					return GeneralMemberType.ASSOCIATED_MEMBERS;
				}
				if (ms == MemberStatus.POSTDOC) {
					return GeneralMemberType.POSTDOCS;
				}
				if (ms == MemberStatus.PHD_STUDENT) {
					return GeneralMemberType.PHDS;
				}
				return GeneralMemberType.RESEARCHERS;
			}
			if (ms.isTechnicalStaff()) {
				return GeneralMemberType.ENGINEERS;
			}
			if (ms == MemberStatus.MASTER_STUDENT) {
				return GeneralMemberType.MASTER_STUDENTS;
			}
			if (ms.isAdministrativeStaff()) {
				return GeneralMemberType.OTHER_MEMBERS;
			}
			return null;
		}
		return GeneralMemberType.FORMER_MEMBERS;
	}

}


