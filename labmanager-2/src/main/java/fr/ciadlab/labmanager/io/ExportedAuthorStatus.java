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

package fr.ciadlab.labmanager.io;

/** Type of status for an author.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public enum ExportedAuthorStatus {
	/** The person is active and explicitly selected.
	 */
	SELECTED_PERSON,
	/** The person is an active researcher.
	 */
	RESEARCHER,
	/** The person is an active PhD student.
	 */
	PHD_STUDENT,
	/** The person is an active Postdoc or engineer.
	 */
	POSTDOC_ENGINEER,
	/** The person is in a case that is not supported by the other enum constants.
	 */
	OTHER;
}
