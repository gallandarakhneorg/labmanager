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

package fr.ciadlab.labmanager.service.member;

/** Member filtering.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum MemberFiltering {
	/** All members.
	 */
	ALL,
	/** Active members, who are currently members of the research organization.
	 */
	ACTIVES,
	/** Former members, who were members of the research organization and are outside.
	 */
	FORMERS,
	/** Future members, who are not yet members of the research organization.
	 */
	FUTURES;
}
