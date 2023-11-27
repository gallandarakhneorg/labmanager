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

package fr.ciadlab.labmanager.io.gscholar;

import java.io.Serializable;
import java.net.URL;

import org.arakhne.afc.progress.Progression;

/** Accessor to the online Google Scholar platform.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.3
 * @see "https://scholar.google.com/"
 */
public interface GoogleScholarPlatform {

	/** Replies the ranking descriptions for the person with the given URL.
	 *
	 * @param gsProfile the URL to the profile of the person on GS.
	 * @param progress progress monitor.
	 * @return the ranking descriptions for the person.
	 * @throws Exception if rankings cannot be read.
	 * @since 3.3
	 */
	GoogleScholarPerson getPersonRanking(URL gsProfile, Progression progress) throws Exception;

	/** Accessor to the online Google Scholar platform.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 3.3
	 * @see "https://scholar.google.com/"
	 */
	public class GoogleScholarPerson implements Serializable {

		private static final long serialVersionUID = 5779940249749841338L;

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
		public GoogleScholarPerson(int hindex, int citations) {
			this.hindex = hindex;
			this.citations = citations;
		}

	}

}
