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

package fr.ciadlab.labmanager.entities.publication;

import java.util.Comparator;

import fr.ciadlab.labmanager.entities.member.PersonListComparator;

/** Comparator of publications. The order of the publication is based on the
 * type of publication, the year, the authors, the identifier.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PublicationComparator implements Comparator<Publication> {

	/** Default comparator of publication.
	 */
	public static final Comparator<Publication> DEFAULT = new PublicationComparator();

	@Override
	public int compare(Publication o1, Publication o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		int cmp = o1.getType().compareTo(o2.getType());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Integer.compare(o1.getPublicationYear(), o2.getPublicationYear());
		if (cmp != 0) {
			return cmp;
		}
		cmp = PersonListComparator.DEFAULT.compare(o1.getAuthors(), o2.getAuthors());
		if (cmp != 0) {
			return cmp;
		}
		return Integer.compare(o1.getId(), o2.getId());
	}

}
