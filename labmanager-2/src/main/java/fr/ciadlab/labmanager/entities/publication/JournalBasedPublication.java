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
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;

/** A publication that is associated to a journal.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface JournalBasedPublication extends IdentifiableEntity {

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
	 * @return the Scimago ranking.
	 */
	QuartileRanking getScimagoQIndex();

	/** Replies the JCR/Web-of-Science Q-index.
	 *
	 * @return the JCR/WOS ranking.
	 */
	QuartileRanking getWosQIndex();

	/** Replies the journal impact factor.
	 *
	 * @return the IF or zero.
	 */
	float getImpactFactor();

}
