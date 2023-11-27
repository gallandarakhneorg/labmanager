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

package fr.ciadlab.labmanager.io.scopus;

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
		final URL apiUrl = new URL("https://www.scopus.com/authid/detail.uri?authorId=" + personId); //$NON-NLS-1$
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
