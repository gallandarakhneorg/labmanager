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
 * you entered into with the CIAD.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.entities;

/** Configuration for the JPA and the types of fields in the database.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface EntityFieldConfig {

	/** Size of {@code VARCHAR} fields for large texts.
	 * By default, the length of coulmns is set to 255. This value
	 * permits to override this size for string-based columns.
	 * <p>This value is defined in order to be database independent for most of the {@code VARCHAR} specifications.
	 */
	static final int LARGE_TEXT_SIZE = 32672;

}
