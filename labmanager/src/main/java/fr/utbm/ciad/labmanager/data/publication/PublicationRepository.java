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

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/** JPA repository for a publication.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PublicationRepository extends JpaRepository<Publication, Integer>, JpaSpecificationExecutor<Publication> {

	/** Replies the list of publications for the person with the given identifier.
	 *
	 * @param personId the identifier of the person.
	 * @return the list of publications.
	 */
	List<Publication> findAllByAuthorshipsPersonId(int personId);

	/** Replies the list of publications for the person with the given webpage identifier.
	 *
	 * @param webpageId the identifier of the webpage of the person.
	 * @return the list of publications.
	 */
	List<Publication> findAllByAuthorshipsPersonWebPageId(String webpageId);

	/** Replies the list of publications for the persons with the given identifiers.
	 *
	 * @param personIds the list of identifiers of the authors.
	 * @return the list of publications.
	 */
	Set<Publication> findAllByAuthorshipsPersonIdIn(Set<Integer> personIds);
	
	/** Replies the list of publictions for the given identifiers.
	 *
	 * @param identifiers the identifiers to search for.
	 * @return the publications.
	 * @since 2.5
	 */
	Set<Publication> findAllByIdIn(Collection<Integer> identifiers);

	/** Replies the list of publications with the given title.
	 *
	 * @param title the title to search for, with case insensitive test.
	 * @return the set of publications with the given title.
	 */
	List<Publication> findAllByTitleIgnoreCase(String title);

	/** Replies the list of publications for the given year.
	 *
	 * @param year the year of publication.
	 * @return the set of publications for the given year.
	 * @since 3.6
	 */
	List<Publication> findAllByPublicationYear(Integer year);

}