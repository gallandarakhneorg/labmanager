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

import java.time.LocalDate;
import java.util.Comparator;

import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationComparator;
import fr.ciadlab.labmanager.utils.cnu.CnuSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Comparator of memberships. Memberships are sorted by date (from future to past), then by organization, person
 * and CNU section.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Component
@Primary
public class ChronoMembershipComparator implements Comparator<Membership> {

	private PersonComparator personComparator;

	private ResearchOrganizationComparator organizationComparator;

	/** Constructor.
	 *
	 * @param personComparator the comparator of persons names.
	 * @param organizationComparator the comparator of research organizations.
	 */
	public ChronoMembershipComparator(@Autowired PersonComparator personComparator, @Autowired ResearchOrganizationComparator organizationComparator) {
		this.personComparator = personComparator;
		this.organizationComparator = organizationComparator;
	}

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
		int n = Integer.compare(period(o1), period(o2));
		if (n != 0) {
			return n;
		}
		n = compareStartDate(o1.getMemberSinceWhen(), o2.getMemberSinceWhen());
		if (n != 0) {
			return n;
		}
		n = compareEndDate(o1.getMemberToWhen(), o2.getMemberToWhen());
		if (n != 0) {
			return n;
		}
		n = this.organizationComparator.compare(o1.getResearchOrganization(), o2.getResearchOrganization());
		if (n != 0) {
			return n;
		}
		n = o1.getMemberStatus().compareTo(o2.getMemberStatus());
		if (n != 0) {
			return n;
		}
		n = this.personComparator.compare(o1.getPerson(), o2.getPerson());
		if (n != 0) {
			return n;
		}
		n = compareCnuSection(o1.getCnuSection(), o2.getCnuSection());
		if (n != 0) {
			return n;
		}
		return Integer.compare(o1.getId(), o2.getId());
	}

	private static int period(Membership d) {
		if (d.isFormer()) {
			return 2;
		}
		if (d.isFuture()) {
			return 0;
		}
		return 1;
	}

	private static int compareStartDate(LocalDate d0, LocalDate d1) {
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

	private static int compareEndDate(LocalDate d0, LocalDate d1) {
		if (d0 == d1) {
			return 0;
		}
		if (d0 == null) {
			return Integer.MAX_VALUE;
		}
		if (d1 == null) {
			return Integer.MIN_VALUE;
		}
		return d1.compareTo(d0);
	}

	private static int compareCnuSection(CnuSection s0, CnuSection s1) {
		if (s0 == s1) {
			return 0;
		}
		if (s0 == null) {
			return Integer.MIN_VALUE;
		}
		if (s1 == null) {
			return Integer.MAX_VALUE;
		}
		return s0.compareTo(s1);
	}

}


