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

package fr.ciadlab.labmanager.service.member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.jury.JuryMembership;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.repository.member.MembershipRepository;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import fr.ciadlab.labmanager.service.jury.JuryMembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for the merging persons.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class PersonMergingService extends AbstractService {

	private final PersonRepository personRepository;

	private final PersonService personService;

	private final MembershipService organizationMembershipService;

	private final MembershipRepository organizationMembershipRepository;

	private final JuryMembershipService juryMembershipService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param personRepository the person repository.
	 * @param personService the person service.
	 * @param organizationMembershipService the service for managing the organization memberships.
	 * @param organizationMembershipRepository the repository for managing the organization memberships.
	 * @param juryMembershipService the service for managing the jury memberships.
	 */
	public PersonMergingService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PersonRepository personRepository,
			@Autowired PersonService personService,
			@Autowired MembershipService organizationMembershipService,
			@Autowired MembershipRepository organizationMembershipRepository,
			@Autowired JuryMembershipService juryMembershipService) {
		super(messages, constants);
		this.personRepository = personRepository;
		this.personService = personService;
		this.organizationMembershipService = organizationMembershipService;
		this.organizationMembershipRepository = organizationMembershipRepository;
		this.juryMembershipService = juryMembershipService;
	}

	/** Merge the persons and authorships by replacing those with an old author name by those with the new author name.
	 * This function enables to group the publications that are attached to two different author names
	 * and select one of the name as the final author name.
	 *
	 * @param source the list of the identifiers of the persons to remove and replace by the target person.
	 * @param target the identifier of the target person who should replace the source persons.
	 * @throws Exception if the merging cannot be completed.
	 */
	public void mergePersonsById(Collection<Integer> source, Integer target) throws Exception {
		assert target != null;
		assert source != null;
		final Optional<Person> optTarget = this.personRepository.findById(target);
		if (optTarget.isPresent()) {
			final Person targetPerson = optTarget.get();
			final List<Person> sourcePersons = this.personRepository.findAllById(source);
			if (sourcePersons.size() != source.size()) {
				for (final Person sp : sourcePersons) {
					if (!source.contains(Integer.valueOf(sp.getId()))) {
						throw new IllegalArgumentException("Source person not found with identifier: " + sp.getId()); //$NON-NLS-1$
					}
				}
				throw new IllegalArgumentException("Source person not found"); //$NON-NLS-1$
			}
			mergePersons(sourcePersons, targetPerson);
		} else {
			throw new IllegalArgumentException("Target person not found with identifier: " + target); //$NON-NLS-1$
		}
	}

	/** Merge the persons and authorships by replacing those with an old author name by those with the new author name.
	 * This function enables to group the publications that are attached to two different author names
	 * and select one of the name as the final author name.
	 *
	 * @param sources the list of persons to remove and replace by the target person.
	 * @param target the target person who should replace the source persons.
	 * @throws Exception if the merging cannot be completed.
	 */
	public void mergePersons(Iterable<Person> sources, Person target) throws Exception {
		assert target != null;
		assert sources != null;
		boolean changed = false;
		for (final Person source : sources) {
			if (source.getId() != target.getId()) {
				getLogger().info("Reassign to " + target.getFullName() + " the elements of " + source.getFullName()); //$NON-NLS-1$ //$NON-NLS-2$
				boolean lchange = reassignPublications(source, target);
				lchange = reassignOrganizationMemberships(source, target) || lchange;
				lchange = reassignJuryMemberships(source, target) || lchange;
				//
				this.personService.removePerson(source.getId());
				changed = changed || lchange;
			}
		}
		if (changed) {
			this.personRepository.save(target);
		}
	}

	/** Re-assign the publication attached to the source person to the target person.
	 * 
	 * @param sources the person to remove and replace by the target person.
	 * @param target the target person who should replace the source persons.
	 * @return {@code true} if publication has changed.
	 * @throws Exception if the change cannot be completed.
	 */
	@SuppressWarnings("static-method")
	protected boolean reassignPublications(Person source, Person target) throws Exception {
		final Set<Authorship> autPubs = source.getAuthorships();
		final Iterator<Authorship> iterator = autPubs.iterator();
		boolean changed = false;
		while (iterator.hasNext()) {
			final Authorship authorship = iterator.next();
			// Test if the target is co-author. If yes, don't do re-assignment to avoid
			// the same person multiple times as author.
			if (!authorship.getPublication().getAuthors().contains(target)) {
				authorship.setPerson(target);
				iterator.remove();
				target.getAuthorships().add(authorship);
				changed = true;
			}
		}
		return changed;
	}

	/** Re-assign the organization memberships attached to the source person to the target person.
	 * 
	 * @param sources the person to remove and replace by the target person.
	 * @param target the target person who should replace the source persons.
	 * @return {@code true} if organization membership has changed.
	 * @throws Exception if the change cannot be completed.
	 */
	protected boolean reassignOrganizationMemberships(Person source, Person target) throws Exception {
		boolean changed = false;
		for (final Membership membership : this.organizationMembershipService.getMembershipsForPerson(source.getId())) {
			source.getMemberships().remove(membership);
			membership.setPerson(target);
			target.getMemberships().add(membership);
			this.organizationMembershipRepository.save(membership);
			changed = true;
		}
		return changed;
	}

	/** Re-assign the jury memberships attached to the source person to the target person.
	 * 
	 * @param sources the person to remove and replace by the target person.
	 * @param target the target person who should replace the source persons.
	 * @return {@code true} if jury membership has changed.
	 * @throws Exception if the change cannot be completed.
	 */
	protected boolean reassignJuryMemberships(Person source, Person target) throws Exception {
		final Set<JuryMembership> changed = new TreeSet<>(EntityUtils.getPreferredJuryMembershipComparator());
		for (final JuryMembership jmembership : this.juryMembershipService.getMembershipsForPerson(source.getId())) {
			jmembership.setPerson(target);
			changed.add(jmembership);
		}
		for (final JuryMembership jmembership : this.juryMembershipService.getMembershipsForCandidate(source.getId())) {
			jmembership.setCandidate(target);
			changed.add(jmembership);
		}
		for (final JuryMembership jmembership : this.juryMembershipService.getMembershipsForPromoter(source.getId())) {
			final List<Person> list = new ArrayList<>(jmembership.getPromoters());
			list.remove(source);
			list.add(target);
			jmembership.setPromoters(list);
			changed.add(jmembership);
		}
		if (changed.isEmpty()) {
			return false;
		}
		for (final JuryMembership mbr : changed) {
			this.juryMembershipService.save(mbr);
		}
		return true;
	}

}
