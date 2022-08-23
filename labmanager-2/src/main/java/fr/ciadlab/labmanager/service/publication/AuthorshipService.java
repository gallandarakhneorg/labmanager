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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.publication.AuthorshipRepository;
import fr.ciadlab.labmanager.repository.publication.PublicationRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.utils.names.DefaultPersonNameParser;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
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

	private final PersonNameParser nameParser;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param publicationRepository the publication repository.
	 * @param authorshipRepository the authorship repository.
	 * @param personRepository the person repository.
	 * @param personService the person service.
	 * @param nameParser the person's name parser.
	 */
	public AuthorshipService(
			@Autowired MessageSourceAccessor messages,
			@Autowired PublicationRepository publicationRepository,
			@Autowired AuthorshipRepository authorshipRepository,
			@Autowired PersonRepository personRepository,
			@Autowired PersonService personService,
			@Autowired PersonNameParser nameParser) {
		super(messages);
		this.publicationRepository = publicationRepository;
		this.authorshipRepository = authorshipRepository;
		this.personRepository = personRepository;
		this.personService = personService;
		this.nameParser = nameParser;
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

	/** Merge the authorships by replacing those with an old author name by those with the new author name.
	 * This function enables to group the publications that are attached to two different author names
	 * and select one of the name as the final author name.
	 *
	 * @param authorOldName the name of author that should deleted. The format of the name is {@code FIRST LAST} or {@code LAST, FIRST}.
	 * @param authorNewName the name of the author that should receive the publications. The format of the name is {@code FIRST LAST} or {@code LAST, FIRST}.
	 * @return the number of publications that have been changed.
	 * @see PersonNameParser
	 * @see DefaultPersonNameParser
	 */
	public int mergePersons(String authorOldName, String authorNewName) {
		final String oldFirstName = this.nameParser.parseFirstName(authorOldName);
		final String oldLastName = this.nameParser.parseLastName(authorOldName);
		final String newFirstName = this.nameParser.parseFirstName(authorNewName);
		final String newLastName = this.nameParser.parseLastName(authorNewName);
		return mergePersons(oldFirstName, oldLastName, newFirstName, newLastName);
	}

	/** Merge the authorships by replacing those with an old author name by those with the new author name.
	 * This function enables to group the publications that are attached to two different author names
	 * and select one of the name as the final author name.
	 * <p>
	 * The function selects the target person as the first available person with the given first and last names.
	 * It is equivalent to a call to {@link #mergePersons(String, String, String, String, Function)} with
	 * {@code null} as last argument.
	 *
	 * @param oldFirstName the first name of author that should deleted.
	 * @param oldLastName the last name of author that should deleted.
	 * @param newFirstName the first name of the author that should receive the publications.
	 * @param newLastName the last name of the author that should receive the publications.
	 * @return the number of authorships that have been changed.
	 * @see PersonNameParser
	 * @see DefaultPersonNameParser
	 */
	public int mergePersons(String oldFirstName, String oldLastName, String newFirstName, String newLastName) {
		return mergePersons(oldFirstName, oldLastName, newFirstName, newLastName, null);
	}

	/** Merge the authorships by replacing those with an old author name by those with the new author name.
	 * This function enables to group the publications that are attached to two different author names
	 * and select one of the name as the final author name.
	 *
	 * @param oldFirstName the first name of author that should deleted.
	 * @param oldLastName the last name of author that should deleted.
	 * @param newFirstName the first name of the author that should receive the publications.
	 * @param newLastName the last name of the author that should receive the publications.
	 * @param personSelector the function that permits to select the target person, i.e. the person who will receive
	 *      all the authorships. If it is {@code null}, the first person found is selected.
	 * @return the number of authorships that have been changed.
	 * @see PersonNameParser
	 * @see DefaultPersonNameParser
	 */
	public int mergePersons(String oldFirstName, String oldLastName,
			String newFirstName, String newLastName, Function<Set<Person>, Person> personSelector) {
		final Set<Person> oldAuthors = this.personRepository.findByFirstNameAndLastName(oldFirstName, oldLastName);
		int changes = 0;
		if (!oldAuthors.isEmpty()) {
			final Set<Person> newAuthors = this.personRepository.findByFirstNameAndLastName(newFirstName, newLastName);
			if (!newAuthors.isEmpty()) {
				final Person newAuthor;
				if (personSelector != null) {
					newAuthor = personSelector.apply(newAuthors);
				} else {
					newAuthor = newAuthors.iterator().next();
				}
				if (newAuthor != null) {
					return mergePersons(oldAuthors, newAuthor);
				}
			}
		}
		return changes;
	}

	private int mergePersons(Iterable<Person> oldAuthors, Person newAuthor) {
		int changes = 0;
		for (final Person oldAuthor : oldAuthors) {
			if (oldAuthor.getId() != newAuthor.getId()) {
				final Set<Authorship> autPubs = oldAuthor.getAuthorships();
				// Reassign authorships
				Set<Integer> autorshipsToRemove = new HashSet<>();
				for (final Authorship authorship : autPubs) {
					final int pubId = authorship.getPublication().getId();
					final int rank = authorship.getAuthorRank();
					// Delete old authorship
					this.publicationRepository.findById(Integer.valueOf(pubId))
					.ifPresent(it -> it.deleteAuthorship(authorship));
					// Add new authorship
					addAuthorship(newAuthor.getId(), pubId, rank, true);
					//
					autorshipsToRemove.add(Integer.valueOf(authorship.getId()));
				}
				// Remove the author and her/his authorships
				oldAuthor.deleteAllAuthorships();
				for (final Integer id : autorshipsToRemove) {
					this.authorshipRepository.deleteById(id);
				}
				this.personService.removePerson(oldAuthor.getId());
				changes += autPubs.size();
			}
		}
		return changes;
	}

	/** Merges all given authors to one and reset name.
	 * <p>
	 * The function selects the target person as the first available person with the given first and last names.
	 * It is equivalent to a call to {@link #mergePersons(List, String, String, Function)} with
	 * {@code null} as last argument.
	 *
	 * @param oldAuthorIds The list of all authors to merge because they are considered as duplicate of the new person.
	 * @param newFirstName new author first name
	 * @param newLastName new author last name
	 * @return number of affected publications
	 * @throws Exception if at least 2 of the authors have a page
	 */
	public int mergePersons(List<Integer> oldAuthorIds, String newFirstName, String newLastName) throws Exception {
		return mergePersons(oldAuthorIds, newFirstName, newLastName, null);
	}

	/** Merges all given authors to one and reset name.
	 *
	 * @param oldAuthorIds The list of all authors to merge because they are considered as duplicate of the new person.
	 * @param newFirstName new author first name
	 * @param newLastName new author last name
	 * @param personSelector the function that permits to select the target person, i.e. the person who will receive
	 *      all the authorships. If it is {@code null}, the first person found is selected.
	 * @return number of affected publications
	 */
	public int mergePersons(List<Integer> oldAuthorIds, String newFirstName, String newLastName,
			Function<Set<Person>, Person> personSelector) {
		final Set<Person> newAuthors = this.personRepository.findByFirstNameAndLastName(newFirstName, newLastName);
		if (!newAuthors.isEmpty()) {
			final Person newAuthor;
			if (personSelector == null) {
				newAuthor = newAuthors.iterator().next();
			} else {
				newAuthor = personSelector.apply(newAuthors);
			}
			if (newAuthor != null) {
				final List<Person> oldAuthors = this.personRepository.findAllById(oldAuthorIds);
				return mergePersons(oldAuthors, newAuthor);
			}
		}
		return 0;
	}

}
