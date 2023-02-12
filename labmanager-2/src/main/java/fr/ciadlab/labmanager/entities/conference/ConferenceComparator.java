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

package fr.ciadlab.labmanager.entities.conference;

import java.util.Comparator;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Comparator of conferences.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Component
@Primary
public class ConferenceComparator implements Comparator<Conference> {

	/** Constructor.
	 */
	public ConferenceComparator() {
		//
	}

	@Override
	public int compare(Conference o1, Conference o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		int n = compareStr(o1.getAcronym(), o2.getAcronym());
		if (n != 0) {
			return n;
		}
		n = compareStr(o1.getName(), o2.getName());
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


