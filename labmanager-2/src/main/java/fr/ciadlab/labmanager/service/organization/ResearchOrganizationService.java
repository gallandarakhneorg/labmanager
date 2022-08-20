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

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationType;
import fr.ciadlab.labmanager.repository.member.MembershipRepository;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.organization.ResearchOrganizationRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import org.arakhne.afc.util.CountryCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
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
	 * @param messages the provider of localized messages.
	 * @param organizationRepository the organization repository.
	 * @param membershipRepository the membership repository.
	 * @param personRepository the person repository.
	 */
	public ResearchOrganizationService(
			@Autowired MessageSourceAccessor messages,
			@Autowired ResearchOrganizationRepository organizationRepository,
			@Autowired MembershipRepository membershipRepository,
			@Autowired PersonRepository personRepository) {
		super(messages);
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
	 * @param acronym the new acronym for the research organization.
	 * @param name the new name for the research organization.
	 * @param description the new description for the research organization.
	 * @param type the type of the research organization.
	 * @param organizationURL the web-site URL of the research organization.
	 * @param country the country of the research organization.
	 * @param superOrganization the identifier of the super organization, or {@code null} or {@code 0} if none.
	 * @return the created organization in the database.
	 */
	public Optional<ResearchOrganization> createResearchOrganization(String acronym, String name, String description,
			ResearchOrganizationType type, String organizationURL, CountryCode country, Integer superOrganization) {
		final Optional<ResearchOrganization> sres;
		if (superOrganization != null && superOrganization.intValue() != 0) {
			sres = this.organizationRepository.findById(superOrganization);
			if (sres.isEmpty()) {
				throw new IllegalArgumentException("Research organization not found with id: " + superOrganization); //$NON-NLS-1$
			}
		} else {
			sres = Optional.empty();
		}
		final ResearchOrganization res = new ResearchOrganization();
		res.setAcronym(Strings.emptyToNull(acronym));
		res.setName(Strings.emptyToNull(name));
		res.setDescription(Strings.emptyToNull(description));
		res.setType(type);
		res.setOrganizationURL(Strings.emptyToNull(organizationURL));
		res.setCountry(country);
		if (sres.isPresent()) {
			res.setSuperOrganization(sres.get());
		}
		this.organizationRepository.save(res);
		return Optional.of(res);
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
	 * @param type the type of the research organization.
	 * @param organizationURL the web-site URL of the research organization.
	 * @param country the country of the research organization.
	 * @param superOrganization the identifier of the super organization, or {@code null} or {@code 0} if none.
	 * @return the organization object that was updated.
	 */
	public Optional<ResearchOrganization> updateResearchOrganization(int identifier, String acronym, String name, String description,
			ResearchOrganizationType type, String organizationURL, CountryCode country, Integer superOrganization) {
		final Optional<ResearchOrganization> res = this.organizationRepository.findById(Integer.valueOf(identifier));
		if (res.isPresent()) {
			final Optional<ResearchOrganization> sres;
			if (superOrganization != null && superOrganization.intValue() != 0) {
				sres = this.organizationRepository.findById(superOrganization);
				if (sres.isEmpty()) {
					throw new IllegalArgumentException("Research organization not found with id: " + superOrganization); //$NON-NLS-1$
				}
			} else {
				sres = Optional.empty();
			}
			//
			final ResearchOrganization organization = res.get();
			if (!Strings.isNullOrEmpty(acronym)) {
				organization.setAcronym(acronym);
			}
			if (!Strings.isNullOrEmpty(name)) {
				organization.setName(name);
			}
			organization.setDescription(Strings.emptyToNull(description));
			organization.setType(type);
			organization.setOrganizationURL(Strings.emptyToNull(organizationURL));
			organization.setCountry(country);
			if (sres.isPresent()) {
				organization.setSuperOrganization(sres.get());
			}
			//
			this.organizationRepository.save(organization);
		}
		return res;
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
	public boolean addMembership(int organizationId, int personId, LocalDate startDate, LocalDate endDate, MemberStatus memberStatus) {
		assert memberStatus != null;
		final Optional<ResearchOrganization> optOrg = this.organizationRepository.findById(Integer.valueOf(organizationId));
		if (optOrg.isPresent()) {
			final Optional<Person> optPerson = this.personRepository.findById(Integer.valueOf(personId));
			if (optPerson.isPresent()) {
				final Person person = optPerson.get();
				// We don't need to add the membership is the person is already involved in the organization
				final Optional<Membership> ro = person.getMemberships().stream().filter(
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
