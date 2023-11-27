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

package fr.ciadlab.labmanager.service.publication;

import java.time.LocalDate;

import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.utils.doi.DoiTools;
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

	private final DoiTools doiTools;
	
	/** Constructor.
	 *
	 * @param doiTools the tools for manipulating the DOI.
	 */
	public DefaultPrePublicationFactory(DoiTools doiTools) {
		this.doiTools = doiTools;
	}
	
	@Override
	public Publication createPrePublication(PublicationType type, String title, String abstractText, String keywords,
			LocalDate date, int year, String isbn, String issn,
			String doi, String halId, String extraUrl, String videoUrl, String dblpUrl, String pdfPath,
			String awardPath, PublicationLanguage language) {
		return new PrePublication(type, title, abstractText, keywords, date, year, isbn, issn,
				this.doiTools.getDOINumberFromDOIUrlOrNull(doi),
				halId, extraUrl, videoUrl, dblpUrl, pdfPath, awardPath, language);
	}

}
