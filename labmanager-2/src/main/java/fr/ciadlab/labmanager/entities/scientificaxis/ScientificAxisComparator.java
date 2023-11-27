/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
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

package fr.ciadlab.labmanager.entities.scientificaxis;

import java.time.LocalDate;
import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Comparator of scientific axes.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.5
 */
@Component
@Primary
public class ScientificAxisComparator implements Comparator<ScientificAxis> {

	/** Constructor.
	 */
	public ScientificAxisComparator() {
		//
	}

	@Override
	public int compare(ScientificAxis o1, ScientificAxis o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		int n = compareDate(o1.getEndDate(), o2.getEndDate());
		if (n != 0) {
			return n;
		}
		n = compareDate(o1.getStartDate(), o2.getStartDate());
		if (n != 0) {
			return n;
		}
		n = StringUtils.compareIgnoreCase(o1.getAcronym(), o2.getAcronym());
		if (n != 0) {
			return n;
		}
		n = StringUtils.compareIgnoreCase(o1.getName(), o2.getName());
		if (n != 0) {
			return n;
		}
		return Integer.compare(o1.getId(), o2.getId());
	}

	/** Null-safe comparison the two dates.
	 * 
	 * @param s0 the first value.
	 * @param s1 the second value.
	 * @return the result of the comparison.
	 */
	protected static int compareDate(LocalDate d0, LocalDate d1) {
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


