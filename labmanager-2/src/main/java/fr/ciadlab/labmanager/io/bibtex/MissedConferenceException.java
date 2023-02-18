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

package fr.ciadlab.labmanager.io.bibtex;

/** Exception that indicates a conference is missed.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
public class MissedConferenceException extends RuntimeException {

	private static final long serialVersionUID = 1235523142208197014L;

	/** Constructor.
	 *
	 * @param entryKey the BibTeX key.
	 * @param conferenceName the name of the missed conference.
	 */
	public MissedConferenceException(String entryKey, String conferenceName) {
		super("Unknown conference for entry " + entryKey + ": " + conferenceName); //$NON-NLS-1$ //$NON-NLS-2$);
	}

}
