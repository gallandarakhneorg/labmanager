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

import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.utils.ranking.JournalRankingSystem;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;

/** A publication that is associated to a journal.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface JournalBasedPublication extends Production {

	/** Replies the journal in which the publication was published.
	 *
	 * @return the journal.
	 */
	Journal getJournal();

	/** Change the journal in which the publication was published.
	 *
	 * @param journal the journal.
	 */
	void setJournal(Journal journal);

	/** Replies the Scimago Q-index.
	 *
	 * @return the Scimago ranking, never {@code null}.
	 */
	QuartileRanking getScimagoQIndex();

	/** Replies the JCR/Web-of-Science Q-index.
	 *
	 * @return the JCR/WOS ranking, never {@code null}.
	 */
	QuartileRanking getWosQIndex();

	/** Replies the journal impact factor.
	 *
	 * @return the IF or zero.
	 */
	float getImpactFactor();

	/** Replies if the publication is ranked in at least one of the ranking systems for the year of publication.
	 *
	 * <p>Any ranking that is known for a year after the publication's year is ignored.
	 *
	 * @return {@code true} if a ranking system is ranking this publication.
	 * @since 3.6
	 */
	boolean isRanked();
	
	/** Replies if the publication is published into a ranked support (journal or conference).
	 *
	 * @param randkingSystem the ranking system to be used.
	 * @return {@code true} if the publication is in a ranked support.
	 * @since 3.6
	 */
	boolean isRanked(JournalRankingSystem randkingSystem);

	/** Replies the category of publication.
	 *
	 * @param rankingsystem indicated the type of ranking system that should be used for determining the category.
	 *     If it is {@code null}, the {@link JournalRankingSystem#getDefault() default ranking system} is used.
	 * @return the category, never {@code null}.
	 * @since 3.6
	 */
	PublicationCategory getCategory(JournalRankingSystem rankingsystem);

}
