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

package fr.utbm.ciad.labmanager.services.member;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.member.MemberStatus;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.member.MembershipRepository;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.member.PersonRepository;
import fr.utbm.ciad.labmanager.data.member.Responsibility;
import fr.utbm.ciad.labmanager.data.organization.OrganizationAddress;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganizationRepository;
import fr.utbm.ciad.labmanager.data.scientificaxis.ScientificAxis;
import fr.utbm.ciad.labmanager.services.AbstractService;
import fr.utbm.ciad.labmanager.utils.bap.FrenchBap;
import fr.utbm.ciad.labmanager.utils.cnu.CnuSection;
import fr.utbm.ciad.labmanager.utils.conrs.ConrsSection;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for the memberships to research organizations.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class MembershipService extends AbstractService {

	private ResearchOrganizationRepository organizationRepository;

	private MembershipRepository membershipRepository;

	private PersonRepository personRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param organizationRepository the organization repository.
	 * @param membershipRepository the membership repository.
	 * @param personRepository the person repository.
	 */
	public MembershipService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ResearchOrganizationRepository organizationRepository,
			@Autowired MembershipRepository membershipRepository,
			@Autowired PersonRepository personRepository) {
		super(messages, constants);
		this.organizationRepository = organizationRepository;
		this.membershipRepository = membershipRepository;
		this.personRepository = personRepository;
	}

	/** Replies the members of the organization.
	 * 
	 * @param organization the organization to use.
	 * @param memberFilter describes how members are filtered.
	 * @param statusFilter indicates how to filter according to the member status.
	 * @return the list of the members.
	 */
	@SuppressWarnings("static-method")
	public List<Membership> getOrganizationMembers(ResearchOrganization organization, MemberFiltering memberFilter,
			Predicate<MemberStatus> statusFilter) {
		if (organization != null) {
			var stream = organization.getMemberships().stream();
			if (memberFilter != null) {
				switch (memberFilter) {
				case ACTIVES:
					stream = stream.filter(it -> it.isActive());
					break;
				case FORMERS:
					stream = stream.filter(it -> it.isFormer());
					break;
				case FUTURES:
					stream = stream.filter(it -> it.isFuture());
					break;
				case ALL:
				default:
					//
				}
			}
			if (statusFilter != null) {
				stream = stream.filter(it -> statusFilter.test(it.getMemberStatus()));
			}
			return stream.collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	/** Replies the members of the organization.
	 * 
	 * @param identifier the identifier of the organization to explore.
	 * @param memberFilter describes how members are filtered.
	 * @param statusFilter indicates how to filter according to the member status.
	 * @return the list of the members.
	 */
	public List<Membership> getOrganizationMembers(long identifier, MemberFiltering memberFilter,
			Predicate<MemberStatus> statusFilter) {
		final var optOrg = this.organizationRepository.findById(Long.valueOf(identifier));
		if (optOrg.isPresent()) {
			return getOrganizationMembers(optOrg.get(), memberFilter, statusFilter);
		}
		return Collections.emptyList();
	}

	/** Replies the list of the organizations that are not those with the given name.
	 *
	 * @param members the members to find into the organizations.
	 * @param currentOrganizationName the current organization name in order to find *others* organizations.
	 * @return map with the member identifiers as keys and the list of the other organizations as values.
	 */
	@SuppressWarnings("static-method")
	public Map<Long, List<ResearchOrganization>> getOtherOrganizationsForMembers(Collection<Membership> members,
			String currentOrganizationName) {
		if (currentOrganizationName != null) {
			return getOtherOrganizationsForMembers(members,
					it -> !Objects.equals(currentOrganizationName, it.getResearchOrganization().getName()));
		}
		return Collections.emptyMap();
	}

	/** Replies the list of the organizations that are not those with the given name.
	 *
	 * @param members the members to find into the organizations.
	 * @param currentOrganizationId the current organization name in order to find *others* organizations.
	 * @return map with the member identifiers as keys and the list of the other organizations as values.
	 */
	@SuppressWarnings("static-method")
	public Map<Long, List<ResearchOrganization>> getOtherOrganizationsForMembers(Collection<Membership> members,
			long currentOrganizationId) {
		return getOtherOrganizationsForMembers(members,
				it -> currentOrganizationId != it.getResearchOrganization().getId());
	}

	private static Map<Long, List<ResearchOrganization>> getOtherOrganizationsForMembers(Collection<Membership> members,
			Predicate<? super Membership> organizationSelector) {
		if (members != null) {
			// Get the memberships in other organizations for the given members
			final var stream = members.stream().filter(organizationSelector);
			// Group by member id and extract only the research organizations
			final var map = stream.collect(Collectors.groupingBy(
					it -> Long.valueOf(it.getPerson().getId()),
					TreeMap::new,
					Collectors.mapping(Membership::getResearchOrganization, Collectors.toList())));
			return map;
		}
		return Collections.emptyMap();
	}

	/** Replies the membership that corresponds to the given organization and member identifiers.
	 * 
	 * @param organizationId the identifier of the organization.
	 * @param memberId the identifier of the member.
	 * @return the membership or {@code null}.
	 */
	public Membership getMembership(long organizationId, long memberId) {
		final var res = this.membershipRepository.findDistinctByResearchOrganizationIdAndPersonId(
				organizationId, memberId);
		if (res.isPresent()) {
			return res.get();
		}
		return null;
	}

	/** Replies the memberships that corresponds to the given organization and member identifiers.
	 * 
	 * @param organizationId the identifier of the organization.
	 * @param memberId the identifier of the member.
	 * @return the memberships, never {@code null}.
	 */
	public Set<Membership> getMemberships(long organizationId, long memberId) {
		return this.membershipRepository.findByResearchOrganizationIdAndPersonId(organizationId, memberId);
	}

	/** Replies the membership of the given person.
	 *
	 * @param memberId the identifier of the person.
	 * @return the memberships of the person.
	 */
	public List<Membership> getMembershipsForPerson(long memberId) {
		return this.membershipRepository.findAllByPersonId(memberId);
	}

	/** Create the membership that corresponds to the given organization and member identifiers.
	 * This function may test if an active membership already exists in the organization for the given
	 * person. In this case, and if the argument {@code forceCreation} is set to {@code false},
	 * then the function does not create the membership and replies the existing membership.
	 * 
	 * @param organizationId the identifier of the organization.
	 * @param organizationAddressId the identifier of the organization address, if known.
	 * @param personId the identifier of the member.
	 * @param startDate the beginning of the membership.
	 * @param endDate the end of the membership.
	 * @param memberStatus the status of the person in the membership.
	 * @param permanentPosition indicates if the position is permanent.
	 * @param responsibility the responsibility of the person during the membership period.
	 * @param cnuSection the section of the CNU to which this membership belongs to.
	 * @param conrsSection the section of the CoNRS to which this membership belongs to.
	 * @param frenchBap the type of job for a not-researcher staff.
	 * @param isMainPosition indicates if the membership is mark as a main position.
	 * @param axes the scientific axes to which the project is associated to.
	 * @param forceCreation indicates if the membership must be created even if there is an active membership
	 *     in the same organization.
	 * @return a pair that contains the membership (created or not) and a boolean flag indicating
	 *     if the replied membership is a new membership or not.
	 * @throws Exception if the creation cannot be done.
	 */
	public Pair<Membership, Boolean> addMembership(long organizationId, Long organizationAddressId, long personId,
			LocalDate startDate, LocalDate endDate,
			MemberStatus memberStatus, boolean permanentPosition,
			Responsibility responsibility, CnuSection cnuSection, ConrsSection conrsSection,
			FrenchBap frenchBap, boolean isMainPosition, List<ScientificAxis> axes, boolean forceCreation) throws Exception {
		assert memberStatus != null;
		final var optOrg = this.organizationRepository.findById(Long.valueOf(organizationId));
		if (optOrg.isPresent()) {
			final var optPerson = this.personRepository.findById(Long.valueOf(personId));
			if (optPerson.isPresent()) {
				final var person = optPerson.get();
				if (!forceCreation) {
					// We don't need to add the membership if the person is already involved in the organization
					final var ro = person.getMemberships().stream().filter(
							it -> it.isActiveIn(startDate, endDate) && it.getResearchOrganization().getId() == organizationId).findAny();
					if (ro.isPresent()) {
						final var activeMembership = ro.get();
						final var sd = activeMembership.getMemberSinceWhen();
						if (endDate == null || sd == null || !endDate.isBefore(sd)) {
							return Pair.of(ro.get(), Boolean.FALSE);
						}
					}
				}
				final var organization = optOrg.get();
				OrganizationAddress address = null;
				if (organizationAddressId != null && organizationAddressId.intValue() != 0) {
					final var optAdr = organization.getAddresses().stream().filter(it -> organizationAddressId.intValue() == it.getId()).findAny();
					if (optAdr.isPresent()) {
						address = optAdr.get();
					}
				}
				
				final var mem = new Membership();
				mem.setPerson(person);
				mem.setResearchOrganization(organization);
				mem.setOrganizationAddress(address);
				mem.setMemberSinceWhen(startDate);
				mem.setMemberToWhen(endDate);
				mem.setMemberStatus(memberStatus);
				mem.setPermanentPosition(permanentPosition);
				mem.setResponsibility(responsibility);
				mem.setCnuSection(cnuSection);
				mem.setConrsSection(conrsSection);
				mem.setFrenchBap(frenchBap);
				mem.setMainPosition(isMainPosition);
				mem.setScientificAxes(axes);
				this.membershipRepository.save(mem);
				return Pair.of(mem, Boolean.TRUE);
			}
			throw new IllegalArgumentException("Person not found with id: " + personId); //$NON-NLS-1$
		}
		throw new IllegalArgumentException("Organization not found with id: " + organizationId); //$NON-NLS-1$
	}

	/** Update the membership that corresponds to the given membership identifier identifiers.
	 * 
	 * @param membershipId the identifier of the membership to update.
	 * @param organizationId the identifier of the organization. If it is {@code null}, the organization should not change.
	 * @param organizationAddressId the identifier of the organization address, if known.
	 * @param startDate the new beginning of the membership.
	 * @param endDate the new end of the membership.
	 * @param memberStatus the new status of the person in the membership.
	 * @param permanentPosition indicates if the position is permanent or not.
	 * @param responsibility the responsibility of the person during the membership period.
	 * @param cnuSection the new CNU section, or {@code null} if unknown.
	 * @param conrsSection the section of the CoNRS to which this membership belongs to.
	 * @param frenchBap the type of job for a not-researcher staff.
	 * @param isMainPosition indicates if the membership is mark as a main position.
	 * @param axes the scientific axes to which the project is associated to.
	 * @return the updated membership.
	 * @throws Exception if the given identifiers cannot be resolved to JPA entities.
	 */
	public Membership updateMembershipById(long membershipId, Long organizationId, Long organizationAddressId,
			LocalDate startDate, LocalDate endDate,
			MemberStatus memberStatus, boolean permanentPosition, Responsibility responsibility,
			CnuSection cnuSection, ConrsSection conrsSection, FrenchBap frenchBap,
			boolean isMainPosition, List<ScientificAxis> axes) throws Exception {
		final var res = this.membershipRepository.findById(Long.valueOf(membershipId));
		if (res.isPresent()) {
			final var membership = res.get();
			if (organizationId != null) {
				final var res0 = this.organizationRepository.findById(organizationId);
				if (res0.isEmpty()) {
					throw new IllegalArgumentException("Cannot find organization with id: " + organizationId); //$NON-NLS-1$
				}
				membership.setResearchOrganization(res0.get());
				OrganizationAddress address = null;
				if (organizationAddressId != null && organizationAddressId.intValue() != 0) {
					final var optAdr = res0.get().getAddresses().stream().filter(it -> organizationAddressId.intValue() == it.getId()).findAny();
					if (optAdr.isPresent()) {
						address = optAdr.get();
					}
				}
				membership.setOrganizationAddress(address);
			}
			membership.setMemberSinceWhen(startDate);
			membership.setMemberToWhen(endDate);
			if (memberStatus != null) {
				membership.setMemberStatus(memberStatus);
			}
			membership.setPermanentPosition(permanentPosition);
			membership.setResponsibility(responsibility);
			membership.setCnuSection(cnuSection);
			membership.setConrsSection(conrsSection);
			membership.setFrenchBap(frenchBap);
			membership.setMainPosition(isMainPosition);
			membership.setScientificAxes(axes);
			this.membershipRepository.save(membership);
			return membership;
		}
		throw new IllegalArgumentException("Cannot find membership with id: " + membershipId); //$NON-NLS-1$
	}

	/** Delete the membership with the given identifier.
	 *
	 * @param membershipId the identifier of the membership to be deleted.
	 * @throws Exception in case of error.
	 */
	@Transactional
	public void removeMembership(long membershipId) throws Exception {
		final var mid = Long.valueOf(membershipId);
		final var optMbr = this.membershipRepository.findById(mid);
		if (optMbr.isEmpty()) {
			throw new IllegalStateException("Membership not found with id: " + membershipId); //$NON-NLS-1$
		}
		final var mbr = optMbr.get();
		final var person = mbr.getPerson();
		if (person != null) {
			person.getMemberships().remove(mbr);
			mbr.setPerson(null);
		}
		final var organization = mbr.getResearchOrganization();
		if (organization != null) {
			organization.getMemberships().remove(mbr);
			mbr.setResearchOrganization(null);
		}
		mbr.setScientificAxes(null);
		this.membershipRepository.deleteById(mid);
	}

	/** Replies the persons in the organization of the given identifier.
	 * This function does not consider the suborganizations.
	 * The function {@link #getMembersOf(int)} provides the members for an organization and the associated
	 * suborganizations.
	 * 
	 * @param organizationId the identifier of the organization.
	 * @return the persons.
	 * @see #getMembersOf(int)
	 */
	public Set<Person> getDirectMembersOf(long organizationId) {
		return this.personRepository.findDistinctByMembershipsResearchOrganizationId(organizationId);
	}

	/** Replies the persons in the organization of the given identifier and its suborganizations.
	 * The function {@link #getDirectMembersOf(int)} provides the members for an organization and not of the associated
	 * suborganizations.
	 * 
	 * @param organizationId the identifier of the organization.
	 * @return the persons.
	 * @see #getDirectMembersOf(int)
	 */
	public Set<Person> getMembersOf(long organizationId) {
		final var optOrg = this.organizationRepository.findById(Long.valueOf(organizationId));
		if (optOrg.isPresent()) {
			final var organization = optOrg.get();

			final var persons = new HashSet<Person>();

			final var organizationCandidates = new LinkedList<ResearchOrganization>();
			organizationCandidates.add(organization);

			do {
				final var currentOrganization = organizationCandidates.removeFirst();
				persons.addAll(getDirectMembersOf(currentOrganization.getId()));
				organizationCandidates.addAll(currentOrganization.getSubOrganizations());
			} while (!organizationCandidates.isEmpty());

			return persons;
		}
		return Collections.emptySet();
	}

	/** Replies the persons in the organization of the given name, with the given status.
	 * 
	 * @param organizationName the name of the organization.
	 * @param status the person status.
	 * @return the persons
	 */
	public Set<Person> getPersonsByOrganizationNameStatus(String organizationName, MemberStatus status) {
		return this.personRepository.findDistinctByMembershipsResearchOrganizationNameAndMembershipsMemberStatus(
				organizationName, status);
	}

	/** Replies the persons in the organization of the given acronym, with the given status.
	 * 
	 * @param organizationAcronym the acronym of the organization.
	 * @param status the person status.
	 * @return the persons
	 */
	public Set<Person> getPersonsByOrganizationAcronymStatus(String organizationAcronym, MemberStatus status) {
		return this.personRepository.findDistinctByMembershipsResearchOrganizationAcronymAndMembershipsMemberStatus(
				organizationAcronym, status);
	}

	/** Replies the memberships that have the given identifiers.
	 *
	 * @param identifiers the list of identifiers to search for.
	 * @return the list of memberships for the given identifiers.
	 * @since 3.5
	 */
	public List<Membership> getMembershipsByIds(List<Long> identifiers) {
		var memberships = this.membershipRepository.findAllById(identifiers);
		if (memberships.size() != identifiers.size()) {
			throw new IllegalArgumentException("Membership not found"); //$NON-NLS-1$
		}
		return memberships;
	}

	/** Replies the memberships with a maximum age whatever the organization.
	 *
	 * @param maxAge the maximum age in years.
	 * @return the memberships.
	 * @since 3.5
	 */
	public List<Membership> getMembershipsOfAge(int maxAge) {
		final var startDate = LocalDate.of(LocalDate.now().getYear() - maxAge, 1, 1);
		final var endDate = LocalDate.now();
		return this.membershipRepository.findAll().parallelStream()
				.filter(it -> it.isActiveIn(startDate, endDate))
				.collect(Collectors.toList());
	}

	/** Replies if the given identifier is the one of a member of an organization.
	 *
	 * @param id the identifier of a person.
	 * @return {@code true} if the person is member of an organization.
	 * @since 3.6
	 */
	public boolean isMember(long id) {
		return !this.membershipRepository.findAllByPersonId(id).isEmpty();
	}

}
