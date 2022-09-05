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

import fr.ciadlab.labmanager.entities.member.MembershipComparator;
import fr.ciadlab.labmanager.entities.member.NameBasedMembershipComparator;
import fr.ciadlab.labmanager.entities.member.PersonComparator;
import fr.ciadlab.labmanager.entities.member.PersonListComparator;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationComparator;
import fr.ciadlab.labmanager.entities.publication.PublicationComparator;
import fr.ciadlab.labmanager.entities.publication.SorensenDicePublicationComparator;

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

	private static PersonComparator PERSON_COMPARATOR; 

	private static PersonListComparator PERSONLIST_COMPARATOR; 

	private static MembershipComparator MEMBERSHIP_COMPARATOR; 

	private static NameBasedMembershipComparator PERSON_NAME_MEMBERSHIP_COMPARATOR; 

	private static ResearchOrganizationComparator ORGANIZATION_COMPARATOR; 

	private static PublicationComparator PUBLICATION_COMPARATOR; 

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
						getPreferredPersonComparator(), getPreferredResearchOrganizationComparator());
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

}
