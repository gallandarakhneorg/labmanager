/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.entities.member;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/** Comparator of lists of persons.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PersonListComparator implements Comparator<List<Person>> {

	/** Default comparator of lists of persons. The order of the persons in the list is based on
	 * {@link #DEFAULT}.
	 */
	public static final Comparator<List<Person>> DEFAULT = new PersonListComparator();

	@Override
	public int compare(List<Person> o1, List<Person> o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		final int max = Integer.max(o1.size(), o2.size());
		final Iterator<Person> it1 = o1.iterator();
		final Iterator<Person> it2 = o2.iterator();
		for (int i = 0; i < max; ++i) {
			final Person p1 = it1.next();
			final Person p2 = it2.next();
			final int cmp = PersonComparator.DEFAULT.compare(p1, p2);
			if (cmp != 0) {
				return cmp;
			}
		}
		return Integer.compare(o1.size(), o2.size());
	}

}
