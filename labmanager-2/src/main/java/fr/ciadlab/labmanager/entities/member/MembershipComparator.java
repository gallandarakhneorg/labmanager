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

import java.sql.Date;
import java.util.Comparator;

import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;

/** Comparator of memberships. Dates are sorted from the highest to the lowest.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MembershipComparator implements Comparator<Membership> {

	/** Default comparator of memberships.
	 * Dates are sorted from the highest to the lowest.
	 */
	public static final Comparator<Membership> DEFAULT = new MembershipComparator();

	@Override
	public int compare(Membership o1, Membership o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		int n = ResearchOrganization.DEFAULT_COMPARATOR.compare(o1.getResearchOrganization(), o2.getResearchOrganization());
		if (n != 0) {
			return n;
		}
		n = o1.getMemberStatus().compareTo(o2.getMemberStatus());
		if (n != 0) {
			return n;
		}
		n = compareDate(o1.getMemberSinceWhen(), o2.getMemberSinceWhen());
		if (n != 0) {
			return n;
		}
		n = compareDate(o1.getMemberToWhen(), o2.getMemberToWhen());
		if (n != 0) {
			return n;
		}
		n = PersonComparator.DEFAULT.compare(o1.getPerson(), o2.getPerson());
		if (n != 0) {
			return n;
		}
		n = Integer.compare(o1.getCnuSection(), o2.getCnuSection());
		if (n != 0) {
			return n;
		}
		return Integer.compare(o1.getId(), o2.getId());
	}

	private static int compareDate(Date d0, Date d1) {
		if (d0 == d1) {
			return 0;
		}
		if (d0 == null) {
			return Integer.MIN_VALUE;
		}
		if (d1 == null) {
			return Integer.MAX_VALUE;
		}
		return d1.compareTo(d0);
	}

}


