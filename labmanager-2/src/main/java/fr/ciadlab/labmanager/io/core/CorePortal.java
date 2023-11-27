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

package fr.ciadlab.labmanager.io.core;

import java.io.Serializable;
import java.net.URL;

import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import org.arakhne.afc.progress.Progression;

/** Accessor to the online CORE Portal.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 * @see "http://portal.core.edu.au/"
 */
public interface CorePortal {

	/** Replies the URL of a conference on CORE.
	 *
	 * @param conferenceId the identifier of the conference.
	 * @return the URL for the conference.
	 */
	URL getConferenceUrl(String conferenceId);

	/** Replies the ranking descriptions for the conference with the given identifier.
	 *
	 * @param year the year for which the ranking should be retrieved.
	 * @param identifier the identifier of the conference on CORE Portal.
	 * @param progress progress monitor.
	 * @return the ranking descriptions for the conference, never {@code null}.
	 * @throws Exception if rankings cannot be read.
	 */
	CorePortalConference getConferenceRanking(int year, String identifier, Progression progress) throws Exception;

	/** Accessor to the online Core Portal.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 3.6
	 * @see "http://portal.core.edu.au/"
	 */
	class CorePortalConference implements Serializable {

		private static final long serialVersionUID = -6517131062597187166L;

		/** The CORE index of the conference.
		 */
		public final CoreRanking ranking;

		/** Constructor.
		 *
		 * @param index the CORE index of the conference.
		 */
		public CorePortalConference(CoreRanking index) {
			this.ranking = index;
		}

	}

}
