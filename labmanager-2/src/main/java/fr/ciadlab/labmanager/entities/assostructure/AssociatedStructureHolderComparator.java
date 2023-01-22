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

package fr.ciadlab.labmanager.entities.assostructure;

import java.util.Comparator;

import fr.ciadlab.labmanager.entities.member.PersonComparator;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationComparator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Comparator of associated structure holders.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.2
 */
@Component
@Primary
public class AssociatedStructureHolderComparator implements Comparator<AssociatedStructureHolder> {

	private final PersonComparator personComparator;

	private final ResearchOrganizationComparator organizationComparator;

	/** Constructor.
	 *
	 * @param personComparator the comparator of persons that is used by this holder comparator.
	 * @param organizationComparator the comparator of organizations that is used by this holder comparator.
	 */
	public AssociatedStructureHolderComparator(PersonComparator personComparator, ResearchOrganizationComparator organizationComparator) {
		this.personComparator = personComparator;
		this.organizationComparator = organizationComparator;
	}

	@Override
	public int compare(AssociatedStructureHolder o1, AssociatedStructureHolder o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		int cmp = this.personComparator.compare(o1.getPerson(), o2.getPerson());
		if (cmp != 0) {
			return cmp;
		}
		cmp = compareRole(o1.getRole(), o2.getRole());
		if (cmp != 0) {
			return cmp;
		}
		cmp = this.organizationComparator.compare(o1.getOrganization(), o2.getOrganization());
		if (cmp != 0) {
			return cmp;
		}
		cmp = this.organizationComparator.compare(o1.getSuperOrganization(), o2.getSuperOrganization());
		if (cmp != 0) {
			return cmp;
		}
		cmp = compareString(o1.getRoleDescription(), o2.getRoleDescription());
		if (cmp != 0) {
			return cmp;
		}
		return Integer.compare(o1.getId(), o2.getId());
	}

	/** Null-safe comparison the two strings.
	 * 
	 * @param v0 the first value.
	 * @param v1 the second value.
	 * @return the result of the comparison.
	 */
	protected static int compareString(String v0, String v1) {
		if (v0 == v1) {
			return 0;
		}
		if (v0 == null) {
			return Integer.MIN_VALUE;
		}
		if (v1 == null) {
			return Integer.MAX_VALUE;
		}
		return v0.compareTo(v1);
	}

	/** Null-safe comparison of the two roles.
	 *
	 * @param r0 the first value.
	 * @param r1 the second value.
	 * @return the numeric result of the comparison.
	 */
	private static int compareRole(HolderRole r0, HolderRole r1) {
		if (r0 == r1) {
			return 0;
		}
		if (r0 == null) {
			return Integer.MIN_VALUE;
		}
		if (r1 == null) {
			return Integer.MAX_VALUE;
		}
		return r0.compareTo(r1);
	}

}