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

package fr.ciadlab.labmanager.service.publication;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.publication.AuthorshipRepository;
import fr.ciadlab.labmanager.repository.publication.PublicationRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import fr.ciadlab.labmanager.service.member.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for managing the authors of the publications.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class AuthorshipService extends AbstractService {

	private final PublicationRepository publicationRepository;

	private final AuthorshipRepository authorshipRepository;

	private final PersonRepository personRepository;

	private final PersonService personService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param publicationRepository the publication repository.
	 * @param authorshipRepository the authorship repository.
	 * @param personRepository the person repository.
	 * @param personService the person service.
	 */
	public AuthorshipService(
			@Autowired MessageSourceAccessor messages,
			@Autowired PublicationRepository publicationRepository,
			@Autowired AuthorshipRepository authorshipRepository,
			@Autowired PersonRepository personRepository,
			@Autowired PersonService personService) {
		super(messages);
		this.publicationRepository = publicationRepository;
		this.authorshipRepository = authorshipRepository;
		this.personRepository = personRepository;
		this.personService = personService;
	}

	/** Replies the authors of the publication with the given identifier.
	 * 
	 * @param publicationId the identifier of the publication.
	 * @return the authors.
	 */
	public List<Person> getAuthorsFor(int publicationId) {
		return this.personRepository.findByAuthorshipsPublicationIdOrderByAuthorshipsAuthorRank(publicationId);
	}

	/** Replies the authorships of the publication with the given identifier.
	 * 
	 * @param publicationId the identifier of the publication.
	 * @return the authorships.
	 */
	public List<Authorship> getAuthorshipsFor(int publicationId) {
		return this.authorshipRepository.findByPublicationId(publicationId);
	}

	/** Link a person and a publication.
	 * The person is added at the given position in the list of the authors.
	 * If this list contains authors with a rank greater than or equals to the given rank,
	 * the ranks of these authors is incremented.
	 *
	 * @param personId the identifier of the person.
	 * @param publicationId the identifier of the publication.
	 * @param rank the position of the person in the list of authors. To be sure to add the authorship at the end,
	 *     pass {@link Integer#MAX_VALUE}.
	 * @param updateOtherAuthorshipRanks indicates if the authorships ranks are re-arranged in order to be consistent.
	 *     If it is {@code false}, the given rank as argument is put into the authorship without change.
	 * @return the added authorship
	 */
	public Authorship addAuthorship(int personId, int publicationId, int rank, boolean updateOtherAuthorshipRanks) {
		final Optional<Person> optPerson = this.personRepository.findById(Integer.valueOf(personId));
		if (optPerson.isPresent()) {
			final Optional<Publication> optPub = this.publicationRepository.findById(Integer.valueOf(publicationId));
			if (optPub.isPresent()) {
				final Publication publication = optPub.get();
				// No need to add the authorship if the person is already linked to the publication
				final Set<Authorship> currentAuthors = publication.getAuthorshipsRaw();
				final Optional<Authorship> ro = currentAuthors.stream().filter(
						it -> it.getPerson().getId() == personId).findAny();
				if (ro.isEmpty()) {
					final Person person = optPerson.get();
					final Authorship authorship = new Authorship();
					authorship.setPerson(person);
					authorship.setPublication(publication);
					final int realRank;
					if (updateOtherAuthorshipRanks) {
						if (rank > currentAuthors.size()) {
							// Insert at the end
							realRank = currentAuthors.size();
						} else {
							// Need to be inserted
							realRank = rank < 0 ? 0 : rank;
							for (final Authorship currentAuthor : currentAuthors) {
								final int orank = currentAuthor.getAuthorRank();
								if (orank >= rank) {
									currentAuthor.setAuthorRank(orank + 1);
									this.authorshipRepository.save(currentAuthor);
								}
							}
						}
					} else {
						realRank = rank;
					}
					authorship.setAuthorRank(realRank);
					currentAuthors.add(authorship);
					publication.getAuthorshipsRaw().add(authorship);
					this.authorshipRepository.save(authorship);
					this.personRepository.save(person);
					this.publicationRepository.save(publication);
					return authorship;
				}
			}
		}
		return null;
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
		for (final Person source : sources) {
			if (source.getId() != target.getId()) {
				reassignPublications(source, target);
				//
				this.personService.removePerson(source.getId());
			}
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
