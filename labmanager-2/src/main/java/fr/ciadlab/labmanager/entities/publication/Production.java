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

import java.io.Serializable;
import java.util.List;

import fr.ciadlab.labmanager.entities.IdentifiableEntity;
import fr.ciadlab.labmanager.entities.member.Person;

/** Represents all the productions (publications, etc.) of the orgnaization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
public interface Production extends Serializable, IdentifiableEntity {

	/** Replies the category of publication.
	 *
	 * @return the category, never {@code null}.
	 */
	PublicationCategory getCategory();

	/** Replies the ordered list of authors.
	 * The authors are replied in the order provided in the paper.
	 *
	 * @return the list of authors.
	 */
	List<Person> getAuthors();

	/** Replies the set of authorships. The authorships are replied in the order provided in the paper.
	 *
	 * @return the authorships.
	 */
	List<Authorship> getAuthorships();

	/** Replies the DOI reference number that is associated to this publication.
	 * Usually, the DOI number should not be prefixed by the {@code http://doi.org} prefix.
	 *
	 * @return the DOI reference or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/Digital_object_identifier"
	 */
	String getDOI();

	/** Replies the year of publication.
	 * If the publication date is specified, the year is extracted from this date.
	 * If the publication date is not specified, the current year is replied.
	 *
	 * @return the year.
	 */
	int getPublicationYear();

	/** Replies the title of the publication.
	 *
	 * @return the title.
	 */
	String getTitle();

}
