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

package fr.ciadlab.labmanager.entities;

import java.text.Normalizer;

import fr.ciadlab.labmanager.entities.journal.JournalComparator;
import fr.ciadlab.labmanager.entities.member.MembershipComparator;
import fr.ciadlab.labmanager.entities.member.NameBasedMembershipComparator;
import fr.ciadlab.labmanager.entities.member.PersonComparator;
import fr.ciadlab.labmanager.entities.member.PersonListComparator;
import fr.ciadlab.labmanager.entities.organization.OrganizationAddressComparator;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationComparator;
import fr.ciadlab.labmanager.entities.publication.PublicationComparator;
import fr.ciadlab.labmanager.entities.publication.SorensenDicePublicationComparator;
import info.debatty.java.stringsimilarity.SorensenDice;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;

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

	private static OrganizationAddressComparator ORGANIZATION_ADDRESS_COMPARATOR; 

	private static PublicationComparator PUBLICATION_COMPARATOR; 

	private static JournalComparator JOURNAL_COMPARATOR; 

	private static final NormalizedStringSimilarity SIMILARITY_COMPUTER = new SorensenDice();

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

}
