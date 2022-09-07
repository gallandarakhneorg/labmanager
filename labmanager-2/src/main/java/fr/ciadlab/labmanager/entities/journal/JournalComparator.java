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

package fr.ciadlab.labmanager.entities.journal;

import java.util.Comparator;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Comparator of journals. First the names are considered in the
 * sort; then, the publisher.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Component
@Primary
public class JournalComparator implements Comparator<Journal> {

	/** Constructor.
	 */
	public JournalComparator() {
		//
	}

	@Override
	public int compare(Journal o1, Journal o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		int n = compareStr(o1.getJournalName(), o2.getJournalName());
		if (n != 0) {
			return n;
		}
		n = compareStr(o1.getPublisher(), o2.getPublisher());
		if (n != 0) {
			return n;
		}
		n = compareStr(o1.getISBN(), o2.getISBN());
		if (n != 0) {
			return n;
		}
		n = compareStr(o1.getISSN(), o2.getISSN());
		if (n != 0) {
			return n;
		}
		return Integer.compare(o1.getId(), o2.getId());
	}

	private static int compareStr(String s0, String s1) {
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


