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

package fr.utbm.ciad.labmanager.data.member;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/** JPA Repository for the persons.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

	/** Replies a person who has the given first and last names.
	 * 
	 * @param firstName the first name of the person.
	 * @param lastName the last name of the person.
	 * @return the person.
	 */
	Optional<Person> findDistinctByFirstNameAndLastName(String firstName, String lastName);

	/** Replies a person who has the given identifier for her/his webpage.
	 * 
	 * @param identifier the expected identifier of the webpage of the person.
	 * @return the person.
	 */
	Optional<Person> findDistinctByWebPageId(String identifier);

	/** Replies the persons who have the given first and last names.
	 * 
	 * @param firstName the first name of the person.
	 * @param lastName the last name of the person.
	 * @return the persons.
	 */
	Set<Person> findByFirstNameAndLastName(String firstName, String lastName);

	/** Replies the persons in the organization with the given name and who have the given status.
	 *
	 * @param organizationName the name of the organization.
	 * @param status the member status
	 * @return the persons.
	 */
	Set<Person> findDistinctByMembershipsResearchOrganizationNameAndMembershipsMemberStatus(
			String organizationName, MemberStatus status);

	/** Replies the persons in the organization with the given acronym and who have the given status.
	 *
	 * @param organizationAcronym the acronym of the organization.
	 * @param status the member status
	 * @return the persons.
	 */
	Set<Person> findDistinctByMembershipsResearchOrganizationAcronymAndMembershipsMemberStatus(
			String organizationAcronym, MemberStatus status);

	/** Replies the persons in the organization with the given identifier.
	 *
	 * @param id the identifier of the organization.
	 * @return the persons.
	 */
	Set<Person> findDistinctByMembershipsResearchOrganizationId(long id);

	/** Replies the persons who authored the publication with the given identifier.
	 *
	 * @param id the identifier of the publication.
	 * @return the persons.
	 */
	List<Person> findByAuthorshipsPublicationIdOrderByAuthorshipsAuthorRank(long id);

	/** Replies the list of the persons who have the given name (first name or last name).
	 *
	 * @param name the name to search for.
	 * @param pageable the manager of pages.
	 * @return the list of the persons who fits the constraint.
	 * @since 4.0
	 */
	@Query("SELECT p FROM Person p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
	Page<Person> findByName(String name, Pageable pageable);

	/** Replies the list of the persons who have the given name (first name or last name) and associated to the organization with a similar acronym.
	 *
	 * @param name the name to search for.
	 * @param organization the acronym of the organization to search for.
	 * @param pageable the manager of pages.
	 * @return the list of the persons who fits the constraint.
	 * @since 4.0
	 */
	@Query("SELECT p FROM Person p JOIN p.memberships m WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%')) AND LOWER(m.researchOrganization.acronym) LIKE LOWER(CONCAT('%', :organization, '%'))")
	Page<Person> findByNameAndOrganizationAcronym(String name, String organization, Pageable pageable);

	/** Replies the number of persons who have the given name (first name or last name).
	 *
	 * @param name the name to search for.
	 * @return the number of persons who fits the constraint.
	 * @since 4.0
	 */
	@Query("SELECT count(p) FROM Person p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
	long countByName(String name);

	/** Replies the number of persons who have the given name (first name or last name) and associated to the organization with a similar acronym.
	 *
	 * @param name the name to search for.
	 * @param organization the acronym of the organization to search for.
	 * @return the number of persons who fits the constraint.
	 * @since 4.0
	 */
	@Query("SELECT count(p) FROM Person p JOIN p.memberships m WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%'))  AND LOWER(m.researchOrganization.acronym) LIKE LOWER(CONCAT('%', :organization, '%'))")
	long countByNameAndOrganizationAcronym(String name, String organization);

	/** Replies the list of the persons who have the given ORCID.
	 *
	 * @param orcid the ORCID to search for.
	 * @param pageable the manager of pages.
	 * @return the list of the persons who fits the constraint.
	 * @since 4.0
	 */
	@Query("SELECT p FROM Person p WHERE LOWER(p.orcid) LIKE LOWER(CONCAT('%', :orcid, '%'))")
	Page<Person> findByOrcid(String orcid, Pageable pageable);

	/** Replies the list of the persons who have the given ORCID and associated to the organization with a similar acronym.
	 *
	 * @param orcid the ORCID to search for.
	 * @param organization the acronym of the organization to search for.
	 * @param pageable the manager of pages.
	 * @return the list of the persons who fits the constraint.
	 * @since 4.0
	 */
	@Query("SELECT p FROM Person p JOIN p.memberships m WHERE LOWER(p.orcid) LIKE LOWER(CONCAT('%', :orcid, '%')) AND LOWER(m.researchOrganization.acronym) LIKE LOWER(CONCAT('%', :organization, '%'))")
	Page<Person> findByOrcidAndOrganizationAcronym(String orcid, String organization, Pageable pageable);

	/** Replies the number of persons who have the given ORCID.
	 *
	 * @param orcid the ORCID to search for.
	 * @return the number of persons who fits the constraint.
	 * @since 4.0
	 */
	@Query("SELECT count(p) FROM Person p WHERE LOWER(p.orcid) ILIKE LOWER(CONCAT('%', :orcid, '%'))")
	long countByOrcid(String orcid);

	/** Replies the number of persons who have the given ORCID and associated to the organization with a similar acronym.
	 *
	 * @param orcid the ORCID to search for.
	 * @param organization the acronym of the organization to search for.
	 * @return the number of persons who fits the constraint.
	 * @since 4.0
	 */
	@Query("SELECT count(p) FROM Person p JOIN p.memberships m WHERE LOWER(p.orcid) ILIKE LOWER(CONCAT('%', :orcid, '%')) AND LOWER(m.researchOrganization.acronym) LIKE LOWER(CONCAT('%', :organization, '%'))")
	long countByOrcidAndOrganizationAcronym(String orcid, String organization);

	/** Replies the list of the persons who are associated to an organization with the given acronym.
	 *
	 * @param acronym the organization acronym to search for.
	 * @param pageable the manager of pages.
	 * @return the list of the persons who fits the constraint.
	 * @since 4.0
	 */
	@Query("SELECT p FROM Person p JOIN p.memberships m WHERE LOWER(m.researchOrganization.acronym) LIKE LOWER(CONCAT('%', :acronym, '%'))")
	Page<Person> findByOrganizationAcronym(String acronym, Pageable pageable);

	/** Replies the list of the persons who are associated to an organization with acronyms similar to the given acronyms.
	 *
	 * @param acronym1 the organization acronym to search for.
	 * @param acronym2 the organization acronym to search for.
	 * @param pageable the manager of pages.
	 * @return the list of the persons who fits the constraint.
	 * @since 4.0
	 */
	@Query("SELECT p FROM Person p JOIN p.memberships m WHERE LOWER(m.researchOrganization.acronym) LIKE LOWER(CONCAT('%', :acronym1, '%')) AND LOWER(m.researchOrganization.acronym) LIKE LOWER(CONCAT('%', :acronym2, '%'))")
	Page<Person> findByOrganizationAcronyms(String acronym1, String acronym2, Pageable pageable);

	/** Replies the number of persons who are associated to an organization with the given acronym.
	 *
	 * @param acronym the organization acronym to search for.
	 * @return the number of persons who fits the constraint.
	 * @since 4.0
	 */
	@Query("SELECT count(p) FROM Person p JOIN p.memberships m WHERE LOWER(m.researchOrganization.acronym) LIKE LOWER(CONCAT('%', :acronym, '%'))")
	long countByOrganizationAcronym(String acronym);

	/** Replies the number of persons who are associated to an organization with acronyms similar to the given acronyms.
	 *
	 * @param acronym1 the organization acronym to search for.
	 * @param acronym2 the organization acronym to search for.
	 * @return the number of persons who fits the constraint.
	 * @since 4.0
	 */
	@Query("SELECT count(p) FROM Person p JOIN p.memberships m WHERE LOWER(m.researchOrganization.acronym) LIKE LOWER(CONCAT('%', :acronym1, '%')) AND LOWER(m.researchOrganization.acronym) LIKE LOWER(CONCAT('%', :acronym2, '%'))")
	long countByOrganizationAcronyms(String acronym1, String acronym2);

}
