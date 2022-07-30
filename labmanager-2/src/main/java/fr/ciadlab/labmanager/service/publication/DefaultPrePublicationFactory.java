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

package fr.ciadlab.labmanager.service.publication;

import java.sql.Date;

import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Factory of a publication that is temporary and defined for pre-initialization of a real publication.
 * This class is not supposed to be used intensively or stored into the database.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Component
@Primary
public class DefaultPrePublicationFactory implements PrePublicationFactory {

	@Override
	public Publication createPrePublication(PublicationType type, String title, String abstractText, String keywords,
			Date date, String isbn, String issn,
			String doi, String extraUrl, String videoUrl, String dblpUrl, String pdfPath,
			String awardPath, PublicationLanguage language) {
		return new PrePublication(type, title, abstractText, keywords, date, isbn, issn, doi, extraUrl,
				videoUrl, dblpUrl, pdfPath, awardPath, language);
	}

}
