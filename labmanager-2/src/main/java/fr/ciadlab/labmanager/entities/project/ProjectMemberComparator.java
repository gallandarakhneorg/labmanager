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

package fr.ciadlab.labmanager.entities.project;

import java.util.Comparator;

import fr.ciadlab.labmanager.entities.member.PersonComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Comparator of project members.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.0
 */
@Component
@Primary
public class ProjectMemberComparator implements Comparator<ProjectMember> {

	private PersonComparator personComparator;

	/** Constructor.
	 *
	 * @param personComparator the comparator of persons names.
	 */
	public ProjectMemberComparator(@Autowired PersonComparator personComparator) {
		this.personComparator = personComparator;
	}

	@Override
	public int compare(ProjectMember o1, ProjectMember o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		int n = compareRoles(o1.getRole(), o2.getRole());
		if (n != 0) {
			return n;
		}
		n = this.personComparator.compare(o1.getPerson(), o2.getPerson());
		if (n != 0) {
			return n;
		}
		return Integer.compare(o1.getId(), o2.getId());
	}

	private static int compareRoles(Role r1, Role r2) {
		if (r1 == r2) {
			return 0;
		}
		if (r1 == null) {
			return Integer.MIN_VALUE;
		}
		if (r2 == null) {
			return Integer.MAX_VALUE;
		}
		return r1.compareTo(r2);
	}

}


