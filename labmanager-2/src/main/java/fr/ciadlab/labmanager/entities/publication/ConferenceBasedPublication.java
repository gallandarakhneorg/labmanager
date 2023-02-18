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

import fr.ciadlab.labmanager.entities.IdentifiableEntity;
import fr.ciadlab.labmanager.entities.conference.Conference;
import fr.ciadlab.labmanager.utils.ranking.CoreRanking;

/** A publication that is associated to a conference.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
public interface ConferenceBasedPublication extends IdentifiableEntity {

	/** Replies the conference in which the publication was published.
	 *
	 * <p>In the example of the "14th International Conference on Systems", the conference is related to "International
	 * Conference on Systems" and the "14" is not considered by this function.
	 *
	 * @return the conference.
	 * @see #getConferenceOccurrenceNumber()
	 */
	Conference getConference();

	/** Change the conference in which the publication was published.
	 *
	 * <p>In the example of the "14th International Conference on Systems", the conference is related to "International
	 * Conference on Systems" and the "14" is not considered by this function.
	 *
	 * @param conference the conference.
	 * @see #setConferenceOccurrenceNumber(int)
	 */
	void setConference(Conference conference);

	/** Replies the number of the occurrence of the conference in which the publication was published.
	 * <p>
	 * In the example of the "14th International Conference on Systems", the occurrence number is "14".
	 *
	 * @return the conference occurrence number.
	 * @see #getConference()
	 */
	int getConferenceOccurrenceNumber();

	/** Change the number of the occurrence of the conference in which the publication was published.
	 * <p>
	 * In the example of the "14th International Conference on Systems", the occurrence number is "14".
	 *
	 * @param number the conference occurrence number.
	 * @see #setConference(Conference)
	 */
	void setConferenceOccurrenceNumber(int number);

	/** Replies the CORE index of the conference.
	 *
	 * @return the CORE ranking.
	 */
	CoreRanking getCoreRanking();

	/** Replies the decorator for a number depending on the given language.
	 *
	 * @param number the number to decorate.
	 * @param language the language.
	 * @return the decorator.
	 */
	static String getNumberDecorator(int number, PublicationLanguage language) {
		switch (language) {
		case FRENCH:
			switch (number) {
			case 1:
				return "er"; //$NON-NLS-1$
			default:
				return "ème"; //$NON-NLS-1$
			}
		case GERMAN:
		case ITALIAN:
		case OTHER:
		case ENGLISH:
		default:
			switch (number) {
			case 1:
				return "st"; //$NON-NLS-1$
			case 2:
				return "nd"; //$NON-NLS-1$
			case 3:
				return "rd"; //$NON-NLS-1$
			default:
				return "th"; //$NON-NLS-1$
			}
		}
	}

}
