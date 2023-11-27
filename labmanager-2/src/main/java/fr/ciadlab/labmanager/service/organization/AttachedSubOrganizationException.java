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

package fr.ciadlab.labmanager.service.organization;

/** Exception indicates than an operation if forbidden because an organization contains suborganizations.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AttachedSubOrganizationException extends Exception {

	private static final long serialVersionUID = -6687420153049435268L;

	/**
	 * Constructor.
	 */
	public AttachedSubOrganizationException() {
		super("Unable to do the action because the organization has associated sub-organizations"); //$NON-NLS-1$
	}

}
