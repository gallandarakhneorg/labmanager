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

package fr.utbm.ciad.labmanager.data;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructure;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructureComparator;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructureHolderComparator;
import fr.utbm.ciad.labmanager.data.conference.ConferenceComparator;
import fr.utbm.ciad.labmanager.data.invitation.PersonInvitationComparator;
import fr.utbm.ciad.labmanager.data.journal.JournalComparator;
import fr.utbm.ciad.labmanager.data.jury.JuryMembershipComparator;
import fr.utbm.ciad.labmanager.data.member.MemberStatus;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.member.MembershipComparator;
import fr.utbm.ciad.labmanager.data.member.NameBasedMembershipComparator;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.member.PersonComparator;
import fr.utbm.ciad.labmanager.data.member.PersonListComparator;
import fr.utbm.ciad.labmanager.data.organization.OrganizationAddressComparator;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganizationComparator;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganizationType;
import fr.utbm.ciad.labmanager.data.project.Project;
import fr.utbm.ciad.labmanager.data.project.ProjectBudgetComparator;
import fr.utbm.ciad.labmanager.data.project.ProjectComparator;
import fr.utbm.ciad.labmanager.data.project.ProjectMemberComparator;
import fr.utbm.ciad.labmanager.data.publication.Production;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.data.publication.PublicationComparator;
import fr.utbm.ciad.labmanager.data.publication.SorensenDicePublicationComparator;
import fr.utbm.ciad.labmanager.data.scientificaxis.ScientificAxisComparator;
import fr.utbm.ciad.labmanager.data.supervision.Supervision;
import fr.utbm.ciad.labmanager.data.supervision.SupervisionComparator;
import fr.utbm.ciad.labmanager.data.supervision.SupervisorComparator;
import fr.utbm.ciad.labmanager.data.teaching.TeachingActivityComparator;
import info.debatty.java.stringsimilarity.SorensenDice;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ext.com.google.common.base.Strings;

/** Tools and configuration for the JPA entities.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class EntityUtils {

	/** Size of {@code VARCHAR} fields for large texts.
	 * By default, the length of columns is set to 255. This value
	 * permits to override this size for string-based columns.
	 * <p>This value is defined in order to be database independent for most of the {@code VARCHAR} specifications.
	 */
	public static final int LARGE_TEXT_SIZE = 32672;

	/** Size of {@code VARCHAR} fields for very small texts.
	 * By default, the length of columns is set to 32. This value
	 * permits to override this size for string-based columns.
	 * <p>This value is defined in order to be database independent for most of the {@code VARCHAR} specifications.
	 */
	public static final int VERY_SMALL_TEXT_SIZE = 32;

	private static final double SIMILARITY = 0.95;

	private static PersonComparator PERSON_COMPARATOR; 

	private static PersonListComparator PERSONLIST_COMPARATOR; 

	private static MembershipComparator MEMBERSHIP_COMPARATOR; 

	private static NameBasedMembershipComparator PERSON_NAME_MEMBERSHIP_COMPARATOR; 

	private static ResearchOrganizationComparator ORGANIZATION_COMPARATOR; 

	private static ScientificAxisComparator SCIENTFIC_AXIS_COMPARATOR; 

	private static ProjectComparator PROJECT_COMPARATOR; 

	private static TeachingActivityComparator TEACHING_ACTIVITY_COMPARATOR; 

	private static OrganizationAddressComparator ORGANIZATION_ADDRESS_COMPARATOR; 

	private static PublicationComparator PUBLICATION_COMPARATOR; 

	private static JournalComparator JOURNAL_COMPARATOR; 

	private static ConferenceComparator CONFERENCE_COMPARATOR; 

	private static JuryMembershipComparator JURY_MEMBERSHIP_COMPARATOR; 

	private static PersonInvitationComparator PERSON_INVITATION_COMPARATOR; 

	private static SupervisorComparator SUPERVISOR_COMPARATOR; 

	private static SupervisionComparator SUPERVISION_COMPARATOR; 

	private static ProjectMemberComparator PROJECT_MEMBER_COMPARATOR; 

	private static ProjectBudgetComparator PROJECT_BUDGET_COMPARATOR;

	private static AssociatedStructureHolderComparator ASSOCIATED_STRUCTURE_HOLDER_COMPARATOR; 

	private static AssociatedStructureComparator ASSOCIATED_STRUCTURE_COMPARATOR; 

	private static final NormalizedStringSimilarity SIMILARITY_COMPUTER = new SorensenDice();

	private static Comparator<? super Publication> DEFAULT_PUBLICATION_COMPARATOR = (a, b) -> {
		if (a == b) {
			return 0;
		}
		if (a == null) {
			return Integer.MIN_VALUE;
		}
		if (b == null) {
			return Integer.MAX_VALUE;
		}
		int cmp = - Integer.compare(a.getPublicationYear(), b.getPublicationYear());
		if (cmp != 0) {
			return cmp;
		}
		cmp = EntityUtils.getPreferredPersonListComparator().compare(a.getAuthors(), b.getAuthors());
		if (cmp != 0) {
			return cmp;
		}
		cmp = a.getType().compareTo(b.getType());
		if (cmp != 0) {
			return cmp;
		}
		cmp = StringUtils.compareIgnoreCase(a.getTitle(), b.getTitle(), true);
		if (cmp != 0) {
			return cmp;
		}
		return Integer.compare(a.getId(), b.getId());
	};

	private EntityUtils() {
		//
	}

	/** Replies the preferred comparator of persons.
	 *
	 * @return the comparator.
	 */
	public static PersonComparator getPreferredPersonComparator() {
		synchronized (EntityUtils.class) {
			if (PERSON_COMPARATOR == null) {
				PERSON_COMPARATOR = new PersonComparator();
			}
			return PERSON_COMPARATOR;
		}
	}

	/** Change the preferred comparator of persons.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredPersonComparator(PersonComparator comparator) {
		synchronized (EntityUtils.class) {
			PERSON_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of lists of persons.
	 *
	 * @return the comparator.
	 */
	public static PersonListComparator getPreferredPersonListComparator() {
		synchronized (EntityUtils.class) {
			if (PERSONLIST_COMPARATOR == null) {
				PERSONLIST_COMPARATOR = new PersonListComparator(getPreferredPersonComparator());
			}
			return PERSONLIST_COMPARATOR;
		}
	}

	/** Change the preferred comparator of lists of persons.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredPersonListComparator(PersonListComparator comparator) {
		synchronized (EntityUtils.class) {
			PERSONLIST_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of memberships.
	 *
	 * @return the comparator.
	 */
	public static MembershipComparator getPreferredMembershipComparator() {
		synchronized (EntityUtils.class) {
			if (MEMBERSHIP_COMPARATOR == null) {
				MEMBERSHIP_COMPARATOR = new MembershipComparator(
						getPreferredPersonComparator(), getPreferredResearchOrganizationComparator(),
						getPreferredOrganizationAddressComparator());
			}
			return MEMBERSHIP_COMPARATOR;
		}
	}

	/** Change the preferred comparator of memberships.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredMembershipComparator(MembershipComparator comparator) {
		synchronized (EntityUtils.class) {
			MEMBERSHIP_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of research organizations.
	 *
	 * @return the comparator.
	 */
	public static ResearchOrganizationComparator getPreferredResearchOrganizationComparator() {
		synchronized (EntityUtils.class) {
			if (ORGANIZATION_COMPARATOR == null) {
				ORGANIZATION_COMPARATOR = new ResearchOrganizationComparator();
			}
			return ORGANIZATION_COMPARATOR;
		}
	}

	/** Change the preferred comparator of research organizations.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredResearchOrganizationComparator(ResearchOrganizationComparator comparator) {
		synchronized (EntityUtils.class) {
			ORGANIZATION_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of scientific axes.
	 *
	 * @return the comparator.
	 */
	public static ScientificAxisComparator getPreferredScientificAxisComparator() {
		synchronized (EntityUtils.class) {
			if (SCIENTFIC_AXIS_COMPARATOR == null) {
				SCIENTFIC_AXIS_COMPARATOR = new ScientificAxisComparator();
			}
			return SCIENTFIC_AXIS_COMPARATOR;
		}
	}

	/** Change the preferred comparator of scientific axes.
	 *
	 * @param comparator the comparator.
	 * @since 3.5
	 */
	public static void setPreferredScientificAxisComparator(ScientificAxisComparator comparator) {
		synchronized (EntityUtils.class) {
			SCIENTFIC_AXIS_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of organization addresses.
	 *
	 * @return the comparator.
	 */
	public static OrganizationAddressComparator getPreferredOrganizationAddressComparator() {
		synchronized (EntityUtils.class) {
			if (ORGANIZATION_ADDRESS_COMPARATOR == null) {
				ORGANIZATION_ADDRESS_COMPARATOR = new OrganizationAddressComparator();
			}
			return ORGANIZATION_ADDRESS_COMPARATOR;
		}
	}

	/** Change the preferred comparator of organization addresses.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredOrganizationAddressComparator(OrganizationAddressComparator comparator) {
		synchronized (EntityUtils.class) {
			ORGANIZATION_ADDRESS_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of publications.
	 *
	 * @return the comparator.
	 */
	public static PublicationComparator getPreferredPublicationComparator() {
		synchronized (EntityUtils.class) {
			if (PUBLICATION_COMPARATOR == null) {
				PUBLICATION_COMPARATOR = new SorensenDicePublicationComparator();
			}
			return PUBLICATION_COMPARATOR;
		}
	}

	/** Replies the preferred comparator of projects.
	 *
	 * @return the comparator.
	 * @since 3.0
	 */
	public static ProjectComparator getPreferredProjectComparator() {
		synchronized (EntityUtils.class) {
			if (PROJECT_COMPARATOR == null) {
				PROJECT_COMPARATOR = new ProjectComparator();
			}
			return PROJECT_COMPARATOR;
		}
	}

	/** Change the preferred comparator of projects.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredProjectComparator(ProjectComparator comparator) {
		synchronized (EntityUtils.class) {
			PROJECT_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of publications that is used for sorting the list of publications.
	 *
	 * @return the comparator.
	 */
	public static Comparator<? super Publication> getPreferredPublicationComparatorInLists() {
		synchronized (EntityUtils.class) {
			return DEFAULT_PUBLICATION_COMPARATOR;
		}
	}

	/** Change the preferred comparator of publications.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredPublicationComparator(PublicationComparator comparator) {
		synchronized (EntityUtils.class) {
			PUBLICATION_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of memberships based on the person's names.
	 *
	 * @return the comparator.
	 */
	public static NameBasedMembershipComparator getPreferredPersonNameBasedMembershipComparator() {
		synchronized (EntityUtils.class) {
			if (PERSON_NAME_MEMBERSHIP_COMPARATOR == null) {
				PERSON_NAME_MEMBERSHIP_COMPARATOR = new NameBasedMembershipComparator(
						getPreferredPersonComparator(), getPreferredResearchOrganizationComparator());
			}
			return PERSON_NAME_MEMBERSHIP_COMPARATOR;
		}
	}

	/** Change the preferred comparator of memberships based on the person's names.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredPersonNameBasedMembershipComparator(NameBasedMembershipComparator comparator) {
		synchronized (EntityUtils.class) {
			PERSON_NAME_MEMBERSHIP_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of journals.
	 *
	 * @return the comparator.
	 */
	public static JournalComparator getPreferredJournalComparator() {
		synchronized (EntityUtils.class) {
			if (JOURNAL_COMPARATOR == null) {
				JOURNAL_COMPARATOR = new JournalComparator();
			}
			return JOURNAL_COMPARATOR;
		}
	}

	/** Change the preferred comparator of journals.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredJournalComparator(JournalComparator comparator) {
		synchronized (EntityUtils.class) {
			JOURNAL_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of conferences.
	 *
	 * @return the comparator.
	 */
	public static ConferenceComparator getPreferredConferenceComparator() {
		synchronized (EntityUtils.class) {
			if (CONFERENCE_COMPARATOR == null) {
				CONFERENCE_COMPARATOR = new ConferenceComparator();
			}
			return CONFERENCE_COMPARATOR;
		}
	}

	/** Change the preferred comparator of conferences.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredConferenceComparator(ConferenceComparator comparator) {
		synchronized (EntityUtils.class) {
			CONFERENCE_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of teaching activities.
	 *
	 * @return the comparator.
	 * @since 3.4
	 */
	public static TeachingActivityComparator getPreferredTeachingActivityComparator() {
		synchronized (EntityUtils.class) {
			if (TEACHING_ACTIVITY_COMPARATOR == null) {
				TEACHING_ACTIVITY_COMPARATOR = new TeachingActivityComparator(getPreferredPersonComparator());
			}
			return TEACHING_ACTIVITY_COMPARATOR;
		}
	}

	/** Change the preferred comparator of teaching activities.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredTeachingActivityComparator(TeachingActivityComparator comparator) {
		synchronized (EntityUtils.class) {
			TEACHING_ACTIVITY_COMPARATOR = comparator;
		}
	}

	/** Check if the information from two publications correspond to similar publications, WITHOUT
	 * normalization of the strings. It is assumed that the given strings are already normalized.
	 * 
	 * @param year0 the publication year of the first publication.
	 * @param title0 the title of the first publication.
	 * @param doi0 the DOI of the first publication.
	 * @param issn0 the ISSN of the first publication.
	 * @param target0 the target of the first publication.
	 * @param year1 the publication year of the second publication.
	 * @param title1 the title of the second publication.
	 * @param doi1 the DOI of the second publication.
	 * @param issn1 the ISSN of the second publication.
	 * @param target1 the target of the second publication.
	 * @return {@code true} if the two publications are similar.
	 * @see #normalizeForSimularityTest(String)
	 */
	public static boolean isSimilarWithoutNormalization(
			int year0, String title0, String doi0, String issn0, String target0,
			int year1, String title1, String doi1, String issn1, String target1) {
		return isSimilar(year0, year1)
				&& isSimilarWithoutNormalization(title0, title1)
				&& isSimilarWithoutNormalization(doi0, doi1)
				&& isSimilarWithoutNormalization(issn0, issn1)
				&& isSimilarWithoutNormalization(target0, target1);
	}

	/** Normalize a string for enabling its similarity computation.
	 *
	 * @param a the string to normalize.
	 * @return the normalized string.
	 * @see #isSimilarWithoutNormalization(int, String, String, String, String, int, String, String, String, String)
	 * @see #isSimilarWithoutNormalization(String, String)
	 */
	public static String normalizeForSimularityTest(String a) {
		if (a == null) {
			return null;
		}
		String str = a.toLowerCase().trim();
		str = Normalizer.normalize(str, Normalizer.Form.NFD);
		return str.replaceAll("\\P{InBasic_Latin}", ""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private static boolean isSimilar(int a, int b) {
		return Math.abs(a - b) <= 1;
	}

	/** Replies if the two given strings are considered as similar.
	 * The two given strings are supposed to be normalized with {@link #normalizeForSimularityTest(String)}.
	 *
	 * @param a the first string to test.
	 * @param b the second string to test.
	 * @return {@code true} if the two strings are similar.
	 * @see #normalizeForSimularityTest(String)
	 */
	public static boolean isSimilarWithoutNormalization(String a, String b) {
		if (a == null || b == null) {
			return true;
		}
		return SIMILARITY_COMPUTER.similarity(a, b) >= SIMILARITY;
	}

	/** Replies the preferred comparator of jury memberships.
	 *
	 * @return the comparator.
	 */
	public static JuryMembershipComparator getPreferredJuryMembershipComparator() {
		synchronized (EntityUtils.class) {
			if (JURY_MEMBERSHIP_COMPARATOR == null) {
				JURY_MEMBERSHIP_COMPARATOR = new JuryMembershipComparator(getPreferredPersonComparator());
			}
			return JURY_MEMBERSHIP_COMPARATOR;
		}
	}

	/** Change the preferred comparator of jury memberships.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredJuryMembershipComparator(JuryMembershipComparator comparator) {
		synchronized (EntityUtils.class) {
			JURY_MEMBERSHIP_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of person invitations.
	 *
	 * @return the comparator.
	 */
	public static PersonInvitationComparator getPreferredPersonInvitationComparator() {
		synchronized (EntityUtils.class) {
			if (PERSON_INVITATION_COMPARATOR == null) {
				PERSON_INVITATION_COMPARATOR = new PersonInvitationComparator(getPreferredPersonComparator());
			}
			return PERSON_INVITATION_COMPARATOR;
		}
	}

	/** Change the preferred comparator of person invitations.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredPersonInvitationComparator(PersonInvitationComparator comparator) {
		synchronized (EntityUtils.class) {
			PERSON_INVITATION_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of supervisors.
	 *
	 * @return the comparator.
	 */
	public static SupervisorComparator getPreferredSupervisorComparator() {
		synchronized (EntityUtils.class) {
			if (SUPERVISOR_COMPARATOR == null) {
				SUPERVISOR_COMPARATOR = new SupervisorComparator(getPreferredPersonComparator());
			}
			return SUPERVISOR_COMPARATOR;
		}
	}

	/** Change the preferred comparator of supervisors.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredSupervisorComparator(SupervisorComparator comparator) {
		synchronized (EntityUtils.class) {
			SUPERVISOR_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of supervisions.
	 *
	 * @return the comparator.
	 */
	public static SupervisionComparator getPreferredSupervisionComparator() {
		synchronized (EntityUtils.class) {
			if (SUPERVISION_COMPARATOR == null) {
				SUPERVISION_COMPARATOR = new SupervisionComparator(
						getPreferredMembershipComparator(),
						getPreferredSupervisorComparator());
			}
			return SUPERVISION_COMPARATOR;
		}
	}

	/** Change the preferred comparator of supervisions.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredSupervisionComparator(SupervisionComparator comparator) {
		synchronized (EntityUtils.class) {
			SUPERVISION_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of project members.
	 *
	 * @return the comparator.
	 */
	public static ProjectMemberComparator getPreferredProjectMemberComparator() {
		synchronized (EntityUtils.class) {
			if (PROJECT_MEMBER_COMPARATOR == null) {
				PROJECT_MEMBER_COMPARATOR = new ProjectMemberComparator(getPreferredPersonComparator());
			}
			return PROJECT_MEMBER_COMPARATOR;
		}
	}

	/** Change the preferred comparator of project members.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredProjectMemberComparator(ProjectMemberComparator comparator) {
		synchronized (EntityUtils.class) {
			PROJECT_MEMBER_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of project budgets.
	 *
	 * @return the comparator.
	 * @since 3.0
	 */
	public static ProjectBudgetComparator getPreferredProjectBudgetComparator() {
		synchronized (EntityUtils.class) {
			if (PROJECT_BUDGET_COMPARATOR == null) {
				PROJECT_BUDGET_COMPARATOR = new ProjectBudgetComparator();
			}
			return PROJECT_BUDGET_COMPARATOR;
		}
	}

	/** Change the preferred comparator of project budgets.
	 *
	 * @param comparator the comparator.
	 */
	public static void setPreferredProjectBudgetComparator(ProjectBudgetComparator comparator) {
		synchronized (EntityUtils.class) {
			PROJECT_BUDGET_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of associated structure holders.
	 *
	 * @return the comparator.
	 * @since 3.2
	 */
	public static AssociatedStructureHolderComparator getPreferredAssociatedStructureHolderComparator() {
		synchronized (EntityUtils.class) {
			if (ASSOCIATED_STRUCTURE_HOLDER_COMPARATOR == null) {
				ASSOCIATED_STRUCTURE_HOLDER_COMPARATOR = new AssociatedStructureHolderComparator(
						getPreferredPersonComparator(),
						getPreferredResearchOrganizationComparator());
			}
			return ASSOCIATED_STRUCTURE_HOLDER_COMPARATOR;
		}
	}

	/** Change the preferred comparator of associated structure holders.
	 *
	 * @param comparator the comparator.
	 * @since 3.2
	 */
	public static void setPreferredAssociatedStructureHolderComparator(AssociatedStructureHolderComparator comparator) {
		synchronized (EntityUtils.class) {
			ASSOCIATED_STRUCTURE_HOLDER_COMPARATOR = comparator;
		}
	}

	/** Replies the preferred comparator of associated structures.
	 *
	 * @return the comparator.
	 * @since 3.2
	 */
	public static AssociatedStructureComparator getPreferredAssociatedStructureComparator() {
		synchronized (EntityUtils.class) {
			if (ASSOCIATED_STRUCTURE_COMPARATOR == null) {
				ASSOCIATED_STRUCTURE_COMPARATOR = new AssociatedStructureComparator(
						getPreferredResearchOrganizationComparator());
			}
			return ASSOCIATED_STRUCTURE_COMPARATOR;
		}
	}

	/** Change the preferred comparator of associated structures.
	 *
	 * @param comparator the comparator.
	 * @since 3.2
	 */
	public static void setPreferredAssociatedStructureComparator(AssociatedStructureComparator comparator) {
		synchronized (EntityUtils.class) {
			ASSOCIATED_STRUCTURE_COMPARATOR = comparator;
		}
	}

	/** Replies the name of the research organization, and possibly of the enclosing organizations up to the university.
	 *
	 * @param supervision the source supervision.
	 * @param separator the text to put between the names of the organizations.
	 * @return the name.
	 * @since 2.2
	 */
	public static String getNameInUniversity(Supervision supervision, String separator) {
		final Membership membership = supervision.getSupervisedPerson();
		final ResearchOrganization organization = membership.getResearchOrganization();
		final StringBuilder outcome = new StringBuilder();
		outcome.append(organization.getNameOrAcronym());

		final StringBuilder univBuffer = new StringBuilder();
		final int univType = ResearchOrganizationType.UNIVERSITY.ordinal();
		boolean univFound = false;
		if (organization.getType().ordinal() <= univType) {
			ResearchOrganization org = organization.getSuperOrganization();
			while (org != null && org.getType().ordinal() <= univType) {
				univFound = org.getType().ordinal() == univType;
				univBuffer.append(separator);
				univBuffer.append(org.getNameOrAcronym());
				org = org.getSuperOrganization();
			}
		}

		if (!univFound) {
			final LocalDate timeStart = membership.getMemberSinceWhen() == null
					? LocalDate.of(supervision.getYear(), 1, 1) : membership.getMemberSinceWhen();
			final LocalDate timeEnd = membership.getMemberToWhen() == null
					? LocalDate.of(supervision.getYear(), 12, 31) : membership.getMemberToWhen();
			final Iterator<Membership> iterator = supervision.getSupervisedPerson().getPerson().getMemberships().stream()
					.filter(it -> it.getId() != membership.getId() && it.isActiveIn(timeStart, timeEnd)).iterator();
			while (iterator.hasNext()) {
				final Membership mbr = iterator.next();
				if (mbr.getResearchOrganization().getType() == ResearchOrganizationType.UNIVERSITY) {
					outcome.append(separator);
					outcome.append(mbr.getResearchOrganization().getNameOrAcronym());
					break;
				}
			}
		} else {
			outcome.append(univBuffer);
		}

		return outcome.toString();
	}

	/** Replies the university/school/company that corresponds to the given organization.
	 *
	 * @param organization the organization.
	 * @return the parent organization or the given organization if it is not an university/school/company.
	 * @since 2.2
	 */
	public static ResearchOrganization getUniversityOrSchoolOrCompany(ResearchOrganization organization) {
		ResearchOrganization current = organization;
		while (current != null && !current.getType().isEmployer()) {
			current = current.getSuperOrganization();
		}
		if (current != null && current.getType().isEmployer()) {
			return current;
		}
		return organization;
	}

	/** Replies the university/school/company that corresponds to the given organizations.
	 *
	 * @param organizations the organizations.
	 * @return the parent organization or the given organization if it is not an university/school/company.
	 * @since 3.6
	 */
	public static ResearchOrganization getUniversityOrSchoolOrCompany(ResearchOrganization... organizations) {
		for (final ResearchOrganization organization : organizations) {
			if (organization.getType().isEmployer()) {
				return organization;
			}
		}
		return null;
	}

	/** Replies the acronym of university/school/company that corresponds to the given organizations.
	 *
	 * @param organizations the organizations.
	 * @return the name of the parent organization or the given organization if it is not an university/school/company.
	 * @since 3.6
	 */
	public static String getUniversityOrSchoolOrCompanyName(ResearchOrganization... organizations) {
		final ResearchOrganization organization = getUniversityOrSchoolOrCompany(organizations);
		if (organization != null) {
			return organization.getAcronymOrName();
		}
		return null;
	}

	/** Replies the employing research organization for the person.
	 *
	 * @param person the person.
	 * @param selector the selector of memberships. If it is {@code null}, not filtering is done.
	 * @return the employer or {@code null}.
	 */
	public static ResearchOrganization getUniversityOrSchoolOrCompany(Person person, Predicate<? super Membership> selector) {
		Stream<Membership> stream = person.getMemberships().stream();
		if (selector != null) {
			stream = stream.filter(selector);
		}
		final Optional<ResearchOrganization> employer = stream
				.map(it0 -> it0.getResearchOrganization())
				.filter(it0 -> it0.getType().isEmployer())
				.findAny();
		if (employer.isPresent()) {
			return employer.get();
		}
		return null;
	}

	/** Replies the name of the university or company for the given person regarding the time windows of the given supervision.
	 *
	 * @param supervision the source supervision.
	 * @param person the person.
	 * @param prefix the text to put before the name of the university.
	 * @param postfix the text to put after the name of the university.
	 * @param acronym indicates if the acronyms of the organizations are preferred to the full names.
	 * @return the university name, or empty string
	 * @since 2.2
	 */
	public static String getUniversityOrSchoolOrCompany(Supervision supervision, Person person, String prefix, String postfix, boolean acronym) {
		final Membership membership = supervision.getSupervisedPerson();
		final LocalDate timeStart = membership.getMemberSinceWhen() == null
				? LocalDate.of(supervision.getYear(), 1, 1) : membership.getMemberSinceWhen();
		final LocalDate timeEnd = membership.getMemberToWhen() == null
				? LocalDate.of(supervision.getYear(), 12, 31) : membership.getMemberToWhen();
		final Iterator<Membership> iterator = person.getMemberships().stream()
				.filter(it -> it.isActiveIn(timeStart, timeEnd)).iterator();
		while (iterator.hasNext()) {
			final Membership mbr = iterator.next();
			final ResearchOrganization org = mbr.getResearchOrganization();
			final ResearchOrganizationType otype = org.getType();
			if (otype == ResearchOrganizationType.UNIVERSITY || otype == ResearchOrganizationType.HIGH_SCHOOL
					|| otype == ResearchOrganizationType.OTHER) {
				final StringBuilder b = new StringBuilder();
				if (prefix != null) {
					b.append(prefix);
				}
				if (acronym) {
					b.append(org.getAcronymOrName());
				} else {
					b.append(org.getNameOrAcronym());
				}
				b.append(", "); //$NON-NLS-1$
				b.append(org.getCountry().getDisplayCountry());
				if (postfix != null) {
					b.append(postfix);
				}
				return b.toString();
			}
		}
		return ""; //$NON-NLS-1$
	}

	/** Fixing the carriage-return characters in the address complements.
	 * Replace the CR characters by the equivalent protected string.
	 *
	 * @param complement the complement to fix.
	 * @return the fixed complement.
	 * @see #fixAddressComplementForBackend(String)
	 * @see #getAddressComplementComponents(String)
	 * @since 2.2
	 */
	public static String fixAddressComplementForEditor(String complement) {
		if (complement == null) {
			return null;
		}
		return complement.replace("\n", "\\n"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/** Fixing the carriage-return characters in the address complements.
	 * Replace the protected CR characters by the equivalent unicode characters.
	 *
	 * @param complement the complement to fix.
	 * @return the fixed complement.
	 * @see #fixAddressComplementForEditor(String)
	 * @see #getAddressComplementComponents(String)
	 * @since 2.2
	 */
	public static String fixAddressComplementForBackend(String complement) {
		if (complement == null) {
			return null;
		}
		return complement.replace("\\n", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/** Extract the components of an address complement. The components are separated by carriage return characters.
	 *
	 * @param complement the complement to analyze.
	 * @return the components of the complement.
	 * @see #fixAddressComplementForEditor(String)
	 * @see #fixAddressComplementForBackend(String)
	 * @since 2.2
	 */
	public static List<String> getAddressComplementComponents(String complement) {
		if (Strings.isNullOrEmpty(complement)) {
			return Collections.emptyList();
		}
		final String[] components = complement.split("[\n]+");  //$NON-NLS-1$
		return Arrays.asList(components);
	}

	/** Replies the list of local heads of the project.
	 *
	 * @param project the project to analyze.
	 * @return the local heads.
	 * @since 3.6
	 */
	public static List<Person> getProjectLocalHeads(Project project) {
		if (project != null) {
			return project.getParticipants().stream().filter(it -> it.getRole().isHead())
					.map(it -> it.getPerson()).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	/** Replies the list of local heads of the associated structure.
	 *
	 * @param structure the associated structure to analyze.
	 * @return the local heads.
	 * @since 3.6
	 */
	public static List<Person> getAssociatedStructureLocalHeads(AssociatedStructure structure) {
		if (structure != null) {
			return structure.getHoldersRaw().stream().filter(it -> it.getRole().isHead())
					.map(it -> it.getPerson()).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	/** Replies the research organizations that are partners in the given projects.
	 *
	 * @param projects the projects to analyze
	 * @return the partners.
	 * @since 3.6
	 */
	public static Set<ResearchOrganization> getPartnersForProjects(Iterable<Project> projects) {
		final Set<ResearchOrganization> partners = new HashSet<>();
		for (final Project project : projects) {
			partners.addAll(project.getOtherPartners());
		}
		return partners;
	}

	/** Replies if one of the organizations in the given collection is located outside europe.
	 * 
	 * @param organizations the organizations.
	 * @return {@code true} if one organization is outside EU.
	 * @since 3.6
	 */
	public static boolean hasOrganizationOutsideEurope(Set<ResearchOrganization> organizations) {
		for (final ResearchOrganization organization : organizations) {
			if (organization.isInternational()) {
				return true;
			}
		}
		return false;
	}

	/** Replies if the given production has an author who is a PhD student.
	 *
	 * @param production the production to test.
	 * @return {@code true} if one author is a PhD student.
	 * @since 3.6
	 */
	public static boolean hasPhDStudentAuthor(Production production) {
		final int year = production.getPublicationYear();
		final LocalDate d0 = LocalDate.of(year, 1, 1);
		final LocalDate d1 = LocalDate.of(year, 12, 31);
		return production.getAuthorships().stream().anyMatch(
				it -> it.getPerson() != null && hasPhdStudentMembership(it.getPerson().getMemberships(), d0, d1));
	}

	private static boolean hasPhdStudentMembership(Set<Membership> memberships, LocalDate d0, LocalDate d1) {
		return memberships.stream().anyMatch(it -> it.getMemberStatus() == MemberStatus.PHD_STUDENT && it.isActiveIn(d0, d1));
	}

	/** Replies if the given production has an author who is a Postdoc.
	 *
	 * @param production the production to test.
	 * @return {@code true} if one author is a Postdoc.
	 * @since 3.6
	 */
	public static boolean hasPostdocAuthor(Production production) {
		final int year = production.getPublicationYear();
		final LocalDate d0 = LocalDate.of(year, 1, 1);
		final LocalDate d1 = LocalDate.of(year, 12, 31);
		return production.getAuthorships().stream().anyMatch(
				it -> it.getPerson() != null && hasPostdocMembership(it.getPerson().getMemberships(), d0, d1));
	}

	private static boolean hasPostdocMembership(Set<Membership> memberships, LocalDate d0, LocalDate d1) {
		return memberships.stream().anyMatch(it -> it.getMemberStatus() == MemberStatus.POSTDOC && it.isActiveIn(d0, d1));
	}

}
