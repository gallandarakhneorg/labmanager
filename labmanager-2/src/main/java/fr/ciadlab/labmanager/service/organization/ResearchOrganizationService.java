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

package fr.ciadlab.labmanager.service.organization;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.repository.member.MembershipRepository;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.organization.ResearchOrganizationRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service for research organizations.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class ResearchOrganizationService extends AbstractService {

	private final ResearchOrganizationRepository organizationRepository;

	private final MembershipRepository membershipRepository;

	private final PersonRepository personRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param organizationRepository the organization repository.
	 * @param membershipRepository the membership repository.
	 * @param personRepository the person repository.
	 */
	public ResearchOrganizationService(@Autowired ResearchOrganizationRepository organizationRepository,
			@Autowired MembershipRepository membershipRepository, @Autowired PersonRepository personRepository) {
		this.organizationRepository = organizationRepository;
		this.membershipRepository = membershipRepository;
		this.personRepository = personRepository;
	}

	/** Replies all the research organizations.
	 *
	 * @return the research organizations.
	 */
	public List<ResearchOrganization> getAllResearchOrganizations() {
		return this.organizationRepository.findAll();
	}

	/** Replies the research organization with the given identifier.
	 *
	 * @param identifier the identifier to search for.
	 * @return the research organization.
	 */
	public Optional<ResearchOrganization> getResearchOrganizationById(int identifier) {
		return this.organizationRepository.findById(Integer.valueOf(identifier));
	}

	/** Replies the research organization with the given acronym.
	 *
	 * @param acronym the acronym to search for.
	 * @return the research organization.
	 */
	public Optional<ResearchOrganization> getResearchOrganizationByAcronym(String acronym) {
		final List<ResearchOrganization> allResearchOrganizations = getAllResearchOrganizations();
		final Optional<ResearchOrganization> ro = allResearchOrganizations.parallelStream()
				.filter(o -> Objects.equals(o.getAcronym(), acronym)).findFirst();
		return ro;
	}

	/** Replies the research organization with the given name.
	 *
	 * @param name the name to search for.
	 * @return the research organization.
	 */
	public Optional<ResearchOrganization> getResearchOrganizationByName(String name) {
		final List<ResearchOrganization> allResearchOrganizations = getAllResearchOrganizations();
		final Optional<ResearchOrganization> ro = allResearchOrganizations.parallelStream()
				.filter(o -> Objects.equals(o.getName(), name)).findFirst();
		return ro;
	}

	/** Create a research organization.
	 *
	 * @param acronym the acronum of the organization.
	 * @param name the name of the organization.
	 * @param description the description of the organization.
	 * @return the identifier of the organization in the database.
	 */
	public int createResearchOrganization(String acronym, String name, String description) {
		final ResearchOrganization res = new ResearchOrganization();
		res.setAcronym(acronym);
		res.setName(name);
		res.setDescription(description);
		this.organizationRepository.save(res);
		return res.getId();
	}

	/** Remove a research organization from the database.
	 * An research organization cannot be removed if it contains a suborganization.
	 *
	 * @param identifier the identifier of the organization to remove.
	 * @throws AttachedSubOrganizationException when the organization contains suborganizations.
	 */
	public void removeResearchOrganization(int identifier) throws AttachedSubOrganizationException {
		final Integer id = Integer.valueOf(identifier);
		final Optional<ResearchOrganization> res = this.organizationRepository.findById(id);
		if (res.isPresent()) {
			final ResearchOrganization organization = res.get();
			if (!organization.getSubOrganizations().isEmpty()) {
				throw new AttachedSubOrganizationException();
			}
			this.organizationRepository.deleteById(id);
		}
	}

	/** Change the information associated to a research organization.
	 *
	 * @param identifier the identifier of the research organization to be updated.
	 * @param acronym the new acronym for the research organization.
	 * @param name the new name for the research organization.
	 * @param description the new description for the research organization.
	 */
	public void updateResearchOrganization(int identifier, String acronym, String name, String description) {
		final Optional<ResearchOrganization> res = this.organizationRepository.findById(Integer.valueOf(identifier));
		if (res.isPresent()) {
			final ResearchOrganization organization = res.get();
			if (!Strings.isNullOrEmpty(acronym)) {
				organization.setAcronym(acronym);
			}
			if (!Strings.isNullOrEmpty(name)) {
				organization.setName(name);
			}
			organization.setDescription(Strings.emptyToNull(description));
			this.organizationRepository.save(organization);
		}
	}

	/** Link a suborganization to a super organization.
	 * 
	 * @param superIdentifier the identifier of the super organization.
	 * @param subIdentifier the identifier of the suborganization.
	 * @return {@code true} if the link is created; {@code false} if the link cannot be created.
	 */
	public boolean linkSubOrganization(int superIdentifier, int subIdentifier) {
		if (superIdentifier != subIdentifier) {
			final Optional<ResearchOrganization> superOrg = this.organizationRepository.findById(
					Integer.valueOf(superIdentifier));
			if (superOrg.isPresent()) {
				final Optional<ResearchOrganization> subOrg = this.organizationRepository.findById(
						Integer.valueOf(subIdentifier));
				if (subOrg.isPresent()) {
					final ResearchOrganization superOrganization = superOrg.get();
					final ResearchOrganization subOrganization = subOrg.get();
					if (superOrganization.getSubOrganizations().add(subOrganization)) {
						subOrganization.setSuperOrganization(superOrganization);
						this.organizationRepository.save(subOrganization);
						this.organizationRepository.save(superOrganization);
						return true;
					}
				}
			}
		}
		return false;
	}

	/** Unlink a suborganization from a super organization.
	 * 
	 * @param superIdentifier the identifier of the super organization.
	 * @param subIdentifier the identifier of the suborganization.
	 * @return {@code true} if the link is deleted; {@code false} if the link cannot be deleted.
	 */
	public boolean unlinkSubOrganization(int superIdentifier, int subIdentifier) {
		final Optional<ResearchOrganization> superOrg = this.organizationRepository.findById(
				Integer.valueOf(superIdentifier));
		if (superOrg.isPresent()) {
			final Optional<ResearchOrganization> subOrg = this.organizationRepository.findById(
					Integer.valueOf(subIdentifier));
			if (subOrg.isPresent()) {
				final ResearchOrganization superOrganization = superOrg.get();
				final ResearchOrganization subOrganization = subOrg.get();
				if (superOrganization.getSubOrganizations().remove(subOrganization)) {
					subOrganization.setSuperOrganization(null);
					this.organizationRepository.save(superOrganization);
					this.organizationRepository.save(subOrganization);
					return true;
				}
			}
		}
		return false;
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

}
