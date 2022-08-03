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

package fr.ciadlab.labmanager.entities.publication;

import java.util.Comparator;

/** Comparator of authorships.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AuthorshipComparator implements Comparator<Authorship> {

	/** Singleton of a comparator of authorships.
	 */
	public static final Comparator<Authorship> DEFAULT = new AuthorshipComparator();

	@Override
	public int compare(Authorship o1, Authorship o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		int n = Integer.compare(o1.getPublication().getId(), o2.getPublication().getId());
		if (n != 0) {
			return n;
		}
		n = Integer.compare(o1.getAuthorRank(), o2.getAuthorRank());
		if (n != 0) {
			return n;
		}
		return Integer.compare(o1.getPerson().getId(), o2.getPerson().getId());
	}

}


