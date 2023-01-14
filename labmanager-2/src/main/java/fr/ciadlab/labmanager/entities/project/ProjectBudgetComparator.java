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

import fr.ciadlab.labmanager.utils.funding.FundingScheme;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Comparator of project budgets.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.0
 */
@Component
@Primary
public class ProjectBudgetComparator implements Comparator<ProjectBudget> {

	/** Constructor.
	 */
	public ProjectBudgetComparator() {
		//
	}

	@Override
	public int compare(ProjectBudget o1, ProjectBudget o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		int cmp = compareFundingScheme(o1.getFundingScheme(), o2.getFundingScheme());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Float.compare(o1.getBudget(), o2.getBudget());
		if (cmp != 0) {
			return cmp;
		}
		return Integer.compare(o1.getId(), o2.getId());
	}


	/** Null-safe comparison the two funding schemes.
	 * 
	 * @param v0 the first value.
	 * @param v1 the second value.
	 * @return the result of the comparison.
	 */
	protected static int compareFundingScheme(FundingScheme v0, FundingScheme v1) {
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

}
