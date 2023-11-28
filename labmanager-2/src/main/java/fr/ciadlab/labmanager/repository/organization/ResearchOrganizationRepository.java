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

package fr.ciadlab.labmanager.repository.organization;

import java.util.Optional;

import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

/** JPA Repository for the research organizations.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see ResearchOrganization
 */
public interface ResearchOrganizationRepository extends JpaRepository<ResearchOrganization, Integer> {

	/** Find a research organization with the given acronym or name. This function is case sensitive.
	 *
	 * @param acronym the acronym to search for.
	 * @param name the name to search for.
	 * @return the research organization.
	 */
	Optional<ResearchOrganization> findDistinctByAcronymOrName(String acronym, String name);

	/** Find a research organization with the given acronym. This function is case sensitive.
	 *
	 * @param acronym the acronym to search for.
	 * @return the research organization.
	 */
	Optional<ResearchOrganization> findDistinctByAcronym(String acronym);

	/** Find a research organization with the given name. This function is case sensitive.
	 *
	 * @param name the name to search for.
	 * @return the research organization.
	 */
	Optional<ResearchOrganization> findDistinctByName(String name);

}
