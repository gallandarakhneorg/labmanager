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

import fr.ciadlab.labmanager.entities.conference.Conference;

/** Fake of a conference.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.4
 */
public class ConferenceFake extends Conference {

	private static final long serialVersionUID = -7666146265221068147L;

	/** Constructor.
	 *
	 * @param name the name of the conference.
	 * @param publisher the name of the conference proceedings' publisher.
	 * @param isbn the ISBN number of the conference.
	 * @param issn the ISSN number of the conference.
	 */
	public ConferenceFake(String name, String publisher, String isbn, String issn) {
		setName(name);
		setPublisher(publisher);
		setISBN(isbn);
		setISSN(issn);
	}

	@Override
	public boolean isFakeEntity() {
		return true;
	}

}
