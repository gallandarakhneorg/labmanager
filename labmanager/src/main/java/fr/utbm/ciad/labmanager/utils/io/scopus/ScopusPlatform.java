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

package fr.utbm.ciad.labmanager.utils.io.scopus;

import java.io.Serializable;
import java.net.URL;

import org.arakhne.afc.progress.Progression;

/** Accessor to the online Elsevier Scopus platform.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.3
 * @see "https://www.scopus.com/"
 */
public interface ScopusPlatform {

	/** Replies the ranking descriptions for the person with the given URL.
	 *
	 * @param scProfile the URL to the profile of the person on Scopus.
	 * @param progress progress monitor.
	 * @return the ranking descriptions for the person, never {@code null}.
	 * @throws Exception if rankings cannot be read.
	 */
	ScopusPerson getPersonRanking(URL scProfile, Progression progress) throws Exception;

	/** Replies the ranking descriptions for the person with the given identifier.
	 *
	 * @param personId the identifier of the person on Scopus.
	 * @param progress progress monitor.
	 * @return the ranking descriptions for the person, never {@code null}.
	 * @throws Exception if rankings cannot be read.
	 */
	default ScopusPerson getPersonRanking(String personId, Progression progress) throws Exception {
		final var apiUrl = new URL("https://www.scopus.com/authid/detail.uri?authorId=" + personId); //$NON-NLS-1$
		return getPersonRanking(apiUrl, progress);
	}

	/** Accessor to the online Scopus platform.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 3.3
	 * @see "https://www.scopus.com/"
	 */
	public class ScopusPerson implements Serializable {

		private static final long serialVersionUID = 8357860848328947999L;

		/** The h-index of the person.
		 */
		public final int hindex;

		/** The number of citations for the person.
		 */
		public final int citations;

		/** Constructor.
		 *
		 * @param hindex the H-index of the person
		 * @param citations the number of citations for the person.
		 */
		public ScopusPerson(int hindex, int citations) {
			this.hindex = hindex;
			this.citations = citations;
		}

	}

}
