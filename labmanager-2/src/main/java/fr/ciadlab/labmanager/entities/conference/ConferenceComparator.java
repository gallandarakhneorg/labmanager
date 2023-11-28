/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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


