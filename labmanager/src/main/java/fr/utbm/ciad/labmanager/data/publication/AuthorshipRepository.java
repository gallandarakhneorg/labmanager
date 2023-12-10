/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.utbm.ciad.labmanager.data.publication;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/** JPA repository for the authorship relations.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface AuthorshipRepository extends JpaRepository<Authorship, Integer>, JpaSpecificationExecutor<Authorship> {

	/** Find an authorship that corresponds tothe given person identifier and publication identifier.
	 *
	 * @param personId the identifier of the person.
	 * @param publicationId the publication of the publication.
	 * @return the authorship.
	 */
	Optional<Authorship> findByPersonIdAndPublicationId(int personId, int publicationId);

	/** Find the authorships that corresponds are linked to the publication with the given identifier.
	 *
	 * @param publicationId the publication of the publication.
	 * @return the authorships linked to the publication with the given id.
	 */
	List<Authorship> findByPublicationId(int publicationId);

	/** Count the number of authorships for the person with the given id.
	 *
	 * @param id the identifier of the person.
	 * @return the count of authorships.
	 */
	int countDistinctByPersonId(int id);

}