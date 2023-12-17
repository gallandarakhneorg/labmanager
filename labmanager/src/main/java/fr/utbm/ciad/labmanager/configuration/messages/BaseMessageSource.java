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

package fr.utbm.ciad.labmanager.configuration.messages;

import java.util.EnumSet;
import java.util.Locale;

import fr.utbm.ciad.labmanager.data.jury.JuryMembershipType;
import fr.utbm.ciad.labmanager.data.jury.JuryType;
import fr.utbm.ciad.labmanager.data.member.Gender;
import fr.utbm.ciad.labmanager.data.member.MemberStatus;
import fr.utbm.ciad.labmanager.data.member.Responsibility;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganizationType;
import fr.utbm.ciad.labmanager.data.project.ProjectActivityType;
import fr.utbm.ciad.labmanager.data.project.ProjectCategory;
import fr.utbm.ciad.labmanager.data.project.ProjectStatus;
import fr.utbm.ciad.labmanager.data.project.Role;
import fr.utbm.ciad.labmanager.data.publication.PublicationCategory;
import fr.utbm.ciad.labmanager.data.publication.PublicationType;
import fr.utbm.ciad.labmanager.data.supervision.SupervisorType;
import fr.utbm.ciad.labmanager.data.teaching.PedagogicalPracticeType;
import fr.utbm.ciad.labmanager.data.teaching.StudentType;
import fr.utbm.ciad.labmanager.data.teaching.TeacherRole;
import fr.utbm.ciad.labmanager.data.teaching.TeachingActivityLevel;
import fr.utbm.ciad.labmanager.data.teaching.TeachingActivityType;
import fr.utbm.ciad.labmanager.data.user.UserRole;
import fr.utbm.ciad.labmanager.rest.member.GeneralMemberType;
import fr.utbm.ciad.labmanager.utils.bap.FrenchBap;
import fr.utbm.ciad.labmanager.utils.cnu.CnuSection;
import fr.utbm.ciad.labmanager.utils.conrs.ConrsSection;
import fr.utbm.ciad.labmanager.utils.funding.FundingScheme;
import fr.utbm.ciad.labmanager.utils.ranking.JournalRankingSystem;
import fr.utbm.ciad.labmanager.utils.trl.TRL;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

/** Base implementation of a message source accessor, outside Spring run.
 * This source is implemented for enabling the enumeration types to use
 * the localized messages.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Configuration
public class BaseMessageSource {

	private static final String TRANSLATION_BASEFILE = "classpath:labmanager-i18n/translations"; //$NON-NLS-1$

	private static MessageSource STATIC_SOURCE;

	private static MessageSourceAccessor STATIC_ACCESSOR;

	/** Get the message source that is lazily and statically loaded.
	 *
	 * @return the message source.
	 */
	public static MessageSource getStaticMessageSource() {
		if (STATIC_SOURCE == null) {
			STATIC_SOURCE = createMessageSource();
		}
		return STATIC_SOURCE;
	}

	/** Get the message source accessor that is lazily and statically loaded.
	 *
	 * @return the message source.
	 */
	public static MessageSourceAccessor getStaticMessageSourceAccessor() {
		if (STATIC_ACCESSOR == null) {
			STATIC_ACCESSOR = createMessageSourceAccessor(getStaticMessageSource());
		}
		return STATIC_ACCESSOR;
	}

	/** Create a well-configured message source.
	 *
	 * @return the message source.
	 */
	public static MessageSource createMessageSource() {
		final ReloadableResourceBundleMessageSource res = new ReloadableResourceBundleMessageSource();
		res.setFallbackToSystemLocale(false);
		res.addBasenames(TRANSLATION_BASEFILE);
		return res;
	}

	/** Create a well-configured message source accessor.
	 *
	 * @param source the message source.
	 * @return the message source accessor.
	 */
	public static MessageSourceAccessor createMessageSourceAccessor(MessageSource source) {
		return new MessageSourceAccessor(source, Locale.US);
	}

	/** Replies the source of the localized messages.
	 *
	 * @return the manager of the messages.
	 */
	@SuppressWarnings("static-method")
	@Bean(name = "messageSource")
	public MessageSource getMessageSource() {
		return createMessageSource();
	}

	/** Replies the tool for accessing to the localized source that is provided as argument.
	 *
	 * @param messageSource the source to embed.
	 * @return the accessor to the message source.
	 */
	@SuppressWarnings("static-method")
	@Bean
	public MessageSourceAccessor getMessageSourceAccessor(final MessageSource messageSource) {
		return createMessageSourceAccessor(messageSource);
	}

	/** Injectors of autowired elements in enumeration types.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	@Component
	public static class EnumerationInjector {

		@Autowired
		private MessageSourceAccessor messages;

		/**
		 * Invoked by Spring engine after the class is created in memory.
		 */
		@PostConstruct
		public void postConstruct() {
			for (UserRole item : EnumSet.allOf(UserRole.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (final ResearchOrganizationType item : EnumSet.allOf(ResearchOrganizationType.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (MemberStatus item : EnumSet.allOf(MemberStatus.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (Responsibility item : EnumSet.allOf(Responsibility.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (PublicationCategory item : EnumSet.allOf(PublicationCategory.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (PublicationType item : EnumSet.allOf(PublicationType.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (Gender item : EnumSet.allOf(Gender.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (CnuSection item : EnumSet.allOf(CnuSection.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (ConrsSection item : EnumSet.allOf(ConrsSection.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (FrenchBap item : EnumSet.allOf(FrenchBap.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (FundingScheme item : EnumSet.allOf(FundingScheme.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (GeneralMemberType item : EnumSet.allOf(GeneralMemberType.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (JuryMembershipType item : EnumSet.allOf(JuryMembershipType.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (JuryType item : EnumSet.allOf(JuryType.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (SupervisorType item : EnumSet.allOf(SupervisorType.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (JournalRankingSystem item : EnumSet.allOf(JournalRankingSystem.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (ProjectActivityType item : EnumSet.allOf(ProjectActivityType.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (ProjectCategory item : EnumSet.allOf(ProjectCategory.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (ProjectStatus item : EnumSet.allOf(ProjectStatus.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (Role item : EnumSet.allOf(Role.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (TRL item : EnumSet.allOf(TRL.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (PedagogicalPracticeType item : EnumSet.allOf(PedagogicalPracticeType.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (TeachingActivityType item : EnumSet.allOf(TeachingActivityType.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (TeachingActivityLevel item : EnumSet.allOf(TeachingActivityLevel.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (TeacherRole item : EnumSet.allOf(TeacherRole.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
			for (StudentType item : EnumSet.allOf(StudentType.class)) {
				item.setMessageSourceAccessor(this.messages);
			}
		}
	}

}
