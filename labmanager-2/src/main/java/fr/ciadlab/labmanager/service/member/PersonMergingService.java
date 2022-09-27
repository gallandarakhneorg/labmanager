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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.service.AbstractService;
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

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param personRepository the person repository.
	 * @param personService the person service.
	 */
	public PersonMergingService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PersonRepository personRepository,
			@Autowired PersonService personService) {
		super(messages, constants);
		this.personRepository = personRepository;
		this.personService = personService;
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
				reassignPublications(source, target);
				//
				this.personService.removePerson(source.getId());
				changed = true;
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
	 * @throws Exception if the change cannot be completed.
	 */
	@SuppressWarnings("static-method")
	protected void reassignPublications(Person source, Person target) throws Exception {
		final Set<Authorship> autPubs = source.getAuthorships();
		final Iterator<Authorship> iterator = autPubs.iterator();
		while (iterator.hasNext()) {
			final Authorship authorship = iterator.next();
			// Test if the target is co-author. If yes, don't do re-assignment to avoid
			// the same person multiple times as author.
			if (!authorship.getPublication().getAuthors().contains(target)) {
				authorship.setPerson(target);
				iterator.remove();
				target.getAuthorships().add(authorship);
			}
		}
	}

}
