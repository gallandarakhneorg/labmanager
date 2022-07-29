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
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.repository.journal;

import fr.ciadlab.labmanager.entities.journal.JournalQualityAnnualIndicators;
import org.springframework.data.jpa.repository.JpaRepository;

/** JPA repository for journal quality indicators.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface JournalQualityAnnualIndicatorsRepository extends JpaRepository<JournalQualityAnnualIndicators, Integer> {
	//
}
