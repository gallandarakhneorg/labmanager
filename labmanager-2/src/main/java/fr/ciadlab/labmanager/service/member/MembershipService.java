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

package fr.ciadlab.labmanager.service.member;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.repository.member.MembershipRepository;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.organization.ResearchOrganizationRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;

/** Service for the memberships to research organizations.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MembershipService extends AbstractService {

	private ResearchOrganizationRepository organizationRepository;

	private MembershipRepository membershipRepository;

	private PersonRepository personRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param organizationRepository the organization repository.
	 * @param membershipRepository the membership repository.
	 * @param personRepository the person repository.
	 */
	public MembershipService(@Autowired ResearchOrganizationRepository organizationRepository,
			@Autowired MembershipRepository membershipRepository,
			@Autowired PersonRepository personRepository) {
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
			Stream<Membership> stream = organization.getMembers().stream();
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
	public List<Membership> getOrganizationMembers(int identifier, MemberFiltering memberFilter,
			Predicate<MemberStatus> statusFilter) {
		final Optional<ResearchOrganization> optOrg = this.organizationRepository.findById(Integer.valueOf(identifier));
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
	public Map<Integer, List<ResearchOrganization>> getOtherOrganizationsForMembers(Collection<Membership> members,
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
	public Map<Integer, List<ResearchOrganization>> getOtherOrganizationsForMembers(Collection<Membership> members,
			int currentOrganizationId) {
		return getOtherOrganizationsForMembers(members,
				it -> currentOrganizationId != it.getResearchOrganization().getId());
	}

	private static Map<Integer, List<ResearchOrganization>> getOtherOrganizationsForMembers(Collection<Membership> members,
			Predicate<? super Membership> organizationSelector) {
		if (members != null) {
			// Get the memberships in other organizations for the given members
			final Stream<Membership> stream = members.stream().filter(organizationSelector);
			// Group by member id and extract only the research organizations
			final Map<Integer, List<ResearchOrganization>> map = stream.collect(Collectors.groupingBy(
					it -> Integer.valueOf(it.getPerson().getId()),
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
	public Membership getMembership(int organizationId, int memberId) {
		final Optional<Membership> res = this.membershipRepository.findDistinctByResearchOrganizationIdAndPersonId(
				organizationId, memberId);
		if (res.isPresent()) {
			return res.get();
		}
		return null;
	}

	/** Create the membership that corresponds to the given organization and member identifiers.
	 * 
	 * @param organizationId the identifier of the organization.
	 * @param personId the identifier of the member.
	 * @param startDate the beginning of the membership.
	 * @param endDate the end of the membership.
	 * @param memberStatus the status of the person in the membership.
	 * @return {@code true} if the link is created; {@code false} if the link cannot be created.
	 */
	public boolean addMembership(int organizationId, int personId, java.sql.Date startDate, java.sql.Date endDate, MemberStatus memberStatus) {
		assert memberStatus != null;
		final Optional<ResearchOrganization> optOrg = this.organizationRepository.findById(Integer.valueOf(organizationId));
		if (optOrg.isPresent()) {
			final Optional<Person> optPerson = this.personRepository.findById(Integer.valueOf(personId));
			if (optPerson.isPresent()) {
				final Person person = optPerson.get();
				// We don't need to add the membership is the person is already involved in the organization
				final Optional<Membership> ro = person.getResearchOrganizations().stream().filter(
						it -> it.getResearchOrganization().getId() == organizationId).findAny();
				if (ro.isEmpty()) {
					final ResearchOrganization organization = optOrg.get();
					final Membership mem = new Membership();
					mem.setPerson(person);
					mem.setResearchOrganization(organization);
					mem.setMemberSinceWhen(startDate);
					mem.setMemberToWhen(endDate);
					mem.setMemberStatus(memberStatus);
					this.membershipRepository.save(mem);
					return true;
				}
			}
		}
		return false;
	}

	/** Update the membership that corresponds to the given organization and member identifiers.
	 * 
	 * @param organizationId the identifier of the organization.
	 * @param personId the identifier of the member.
	 * @param startDate the new beginning of the membership.
	 * @param endDate the new end of the membership.
	 * @param memberStatus the new status of the person in the membership.
	 * @return {@code true} if the link has changed; {@code false} if the link has not changed.
	 */
	public boolean updateMembership(int organizationId, int personId, java.sql.Date startDate, java.sql.Date endDate, MemberStatus memberStatus) {
		final Optional<Membership> res = this.membershipRepository.findDistinctByResearchOrganizationIdAndPersonId(organizationId, personId);
		if (res.isPresent()) {
			final Membership membership = res.get();
			membership.setMemberSinceWhen(startDate);
			membership.setMemberToWhen(endDate);
			if (memberStatus != null) {
				membership.setMemberStatus(memberStatus);
			}
			this.membershipRepository.save(membership);
			return true;
		}
		return false;
	}

	/** Delete the membership between the organization with the given identifier and the person
	 * with the given identifier.
	 *
	 * @param organizationId the identifier of the organization.
	 * @param personId the identifier of the person.
	 */
	@Transactional
	public void removeMembership(int organizationId, int personId) {
		this.membershipRepository.deleteByResearchOrganizationIdAndPersonId(organizationId, personId);
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
	public Set<Person> getDirectMembersOf(int organizationId) {
		return this.personRepository.findDistinctByResearchOrganizationsResearchOrganizationId(organizationId);
	}

	/** Replies the persons in the organization of the given identifier and its suborganizations.
	 * The function {@link #getDirectMembersOf(int)} provides the members for an organization and not of the associated
	 * suborganizations.
	 * 
	 * @param organizationId the identifier of the organization.
	 * @return the persons.
	 * @see #getDirectMembersOf(int)
	 */
	public Set<Person> getMembersOf(int organizationId) {
		final Optional<ResearchOrganization> optOrg = this.organizationRepository.findById(Integer.valueOf(organizationId));
		if (optOrg.isPresent()) {
			final ResearchOrganization organization = optOrg.get();

			final Set<Person> persons = new HashSet<>();

			final Deque<ResearchOrganization> organizationCandidates = new LinkedList<>();
			organizationCandidates.add(organization);

			do {
				final ResearchOrganization currentOrganization = organizationCandidates.removeFirst();
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
		return this.personRepository.findDistinctByResearchOrganizationsResearchOrganizationNameAndResearchOrganizationsMemberStatus(
				organizationName, status);
	}

	/** Replies the persons in the organization of the given acronym, with the given status.
	 * 
	 * @param organizationAcronym the acronym of the organization.
	 * @param status the person status.
	 * @return the persons
	 */
	public Set<Person> getPersonsByOrganizationAcronymStatus(String organizationAcronym, MemberStatus status) {
		return this.personRepository.findDistinctByResearchOrganizationsResearchOrganizationAcronymAndResearchOrganizationsMemberStatus(
				organizationAcronym, status);
	}

}
