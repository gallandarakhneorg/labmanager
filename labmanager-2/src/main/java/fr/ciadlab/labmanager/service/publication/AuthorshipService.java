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
	 * @param publicationRepository the publication repository.
	 * @param authorshipRepository the authorship repository.
	 * @param personRepository the person repository.
	 * @param personService the person service.
	 * @param nameParser the person's name parser.
	 */
	public AuthorshipService(@Autowired PublicationRepository publicationRepository,
			@Autowired AuthorshipRepository authorshipRepository,
			@Autowired PersonRepository personRepository,
			@Autowired PersonService personService,
			@Autowired PersonNameParser nameParser) {
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
		return this.personRepository.findByPublicationsPublicationIdOrderByPublicationsAuthorRank(publicationId);
	}

	/** Replies the publications of the person with the given identifier.
	 *
	 * @param personId the identifier of the person.
	 * @return the list of publications.
	 */
	public List<Publication> getPublicationsFor(int personId) {
		return this.publicationRepository.findAllByAuthorshipsPersonId(personId);
	}

	/** Link a person and a publication.
	 * The person is added at the end of the list of the authors.
	 *
	 * @param personId the identifier of the person.
	 * @param publicationId the identifier of the publication.
	 * @return {@code true} if the authorship is added.
	 */
	public boolean addAuthorship(int personId, int publicationId) {
		return addAuthorship(personId, publicationId, -1);
	}

	/** Link a person and a publication.
	 * The person is added at the given position in the list of the authors.
	 * If this list contains authors with a rank greater than or equals to the given rank,
	 * the ranks of these authors is incremented.
	 *
	 * @param personId the identifier of the person.
	 * @param publicationId the identifier of the publication.
	 * @param rank the position of the person in the list of authors. If the rank is negative, the person is
	 *     added at the end of the list of authors.
	 * @return {@code true} if the authorship is added.
	 */
	public boolean addAuthorship(int personId, int publicationId, int rank) {
		final Optional<Person> optPerson = this.personRepository.findById(Integer.valueOf(personId));
		if (optPerson.isPresent()) {
			final Optional<Publication> optPub = this.publicationRepository.findById(Integer.valueOf(publicationId));
			if (optPub.isPresent()) {
				final Publication publication = optPub.get();
				// No need to add the authorship if the person is already linked to the publication
				final List<Authorship> currentAuthors = publication.getAuthorships();
				final Optional<Authorship> ro = currentAuthors.stream().filter(
						it -> it.getPerson().getId() == personId).findAny();
				if (ro.isEmpty()) {
					final Person person = optPerson.get();
					final Authorship authorship = new Authorship();
					authorship.setPerson(person);
					authorship.setPublication(publication);
					final int realRank;
					if (rank < 0 || rank >= currentAuthors.size()) {
						realRank = currentAuthors.size();
					} else {
						// Need to be inserted
						realRank = rank;
						for (final Authorship currentAuthor : currentAuthors) {
							final int orank = currentAuthor.getAuthorRank();
							if (orank >= rank) {
								currentAuthor.setAuthorRank(orank + 1);
								this.authorshipRepository.save(currentAuthor);
							}
						}
					}
					authorship.setAuthorRank(realRank);
					this.authorshipRepository.save(authorship);
					return true;
				}
			}
		}
		return false;
	}

	/** Update the rank of an authorship.
	 *
	 * @param personId the identifier of the person.
	 * @param publicationId the identifier of the publication.
	 * @param rank the new position of the person in the list of authors. If the rank is negative, the person is
	 *     added at the end of the list of authors.
	 * @return {@code true} if the authorship is added.
	 */
	public boolean updateAuthorship(int personId, int publicationId, int rank) {
		final Optional<Authorship> optAut = this.authorshipRepository.findByPersonIdAndPublicationId(personId, publicationId);
		if (optAut.isPresent()) {
			final Authorship authorship = optAut.get();
			final int oldRank = authorship.getAuthorRank();
			if (oldRank != rank) {
				final List<Authorship> currentAuthors = authorship.getPublication().getAuthorships();
				final int newRank;
				if (rank < 0 || rank >= currentAuthors.size()) {
					// The author is moved to the end of the list.
					// The authors between the old position and the end of the list
					// change of rank by decrement.
					for (final Authorship currentAuthor : currentAuthors) {
						if (currentAuthor != authorship) {
							final int orank = currentAuthor.getAuthorRank();
							if (orank > rank) {
								currentAuthor.setAuthorRank(orank - 1);
								this.authorshipRepository.save(currentAuthor);
							}
						}
					}
					newRank = currentAuthors.size() - 1;
				} else if (rank <= oldRank) {
					// The author is moved before its current position.
					// The authors between the new position and the old position (new position <= old position)
					// change of rank by increment.
					for (final Authorship currentAuthor : currentAuthors) {
						if (currentAuthor != authorship) {
							final int orank = currentAuthor.getAuthorRank();
							if (orank >= rank && orank <= oldRank) {
								currentAuthor.setAuthorRank(orank + 1);
								this.authorshipRepository.save(currentAuthor);
							}
						}
					}
					newRank = rank;
				} else {
					// The author is moved after its current position.
					// The authors between the old position and the new position (old position <= new position)
					// change of rank by decrement.
					for (final Authorship currentAuthor : currentAuthors) {
						if (currentAuthor != authorship) {
							final int orank = currentAuthor.getAuthorRank();
							if (orank >= oldRank && orank <= rank) {
								currentAuthor.setAuthorRank(orank - 1);
								this.authorshipRepository.save(currentAuthor);
							}
						}
					}
					newRank = rank;
				}
				authorship.setAuthorRank(newRank);
				this.authorshipRepository.save(authorship);
				return true;
			}
		}
		return false;
	}

	/** Remove an authorship.
	 *
	 * @param personId the identifier of the person.
	 * @param publicationId the identifier of the publication.
	 * @return {@code true} if the authorship is removed.
	 */
	public boolean removeAuthorship(int personId, int publicationId) {
		final Optional<Authorship> optAut = this.authorshipRepository.findByPersonIdAndPublicationId(personId, publicationId);
		if (optAut.isPresent()) {
			final Optional<Publication> optPub = this.publicationRepository.findById(Integer.valueOf(publicationId));
			if (optPub.isPresent()) {
				final Publication publication = optPub.get();
				final Authorship authorship = optAut.get();

				// Update the ranks of the other authors
				final List<Authorship> currentAuthors = publication.getAuthorships();
				final int oldRank = authorship.getAuthorRank();
				for (final Authorship currentAuthor : currentAuthors) {
					if (currentAuthor.getId() != authorship.getId()) {
						final int crank = currentAuthor.getAuthorRank();
						if (crank >  oldRank) {
							currentAuthor.setAuthorRank(crank - 1);
							this.authorshipRepository.save(currentAuthor);
						}
					}
				}

				this.authorshipRepository.deleteById(Integer.valueOf(authorship.getId()));
				this.authorshipRepository.flush();
				return true;
			}
		}
		return false;
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
	public int mergeAuthors(String authorOldName, String authorNewName) {
		final String oldFirstName = this.nameParser.parseFirstName(authorOldName);
		final String oldLastName = this.nameParser.parseLastName(authorOldName);
		final String newFirstName = this.nameParser.parseFirstName(authorNewName);
		final String newLastName = this.nameParser.parseLastName(authorNewName);
		return mergeAuthors(oldFirstName, oldLastName, newFirstName, newLastName);
	}

	/** Merge the authorships by replacing those with an old author name by those with the new author name.
	 * This function enables to group the publications that are attached to two different author names
	 * and select one of the name as the final author name.
	 * <p>
	 * The function selects the target person as the first available person with the given first and last names.
	 * It is equivalent to a call to {@link #mergeAuthors(String, String, String, String, Function)} with
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
	public int mergeAuthors(String oldFirstName, String oldLastName, String newFirstName, String newLastName) {
		return mergeAuthors(oldFirstName, oldLastName, newFirstName, newLastName, null);
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
	public int mergeAuthors(String oldFirstName, String oldLastName,
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
					return mergeAuthors(oldAuthors, newAuthor);
				}
			}
		}
		return changes;
	}

	private int mergeAuthors(Iterable<Person> oldAuthors, Person newAuthor) {
		int changes = 0;
		for (final Person oldAuthor : oldAuthors) {
			if (oldAuthor.getId() != newAuthor.getId()) {
				final Set<Authorship> autPubs = oldAuthor.getPublications();
				// Reassign authorships
				Set<Integer> autorshipsToRemove = new HashSet<>();
				for (final Authorship authorship : autPubs) {
					final int pubId = authorship.getPublication().getId();
					final int rank = authorship.getAuthorRank();
					// Delete old authorship
					this.publicationRepository.findById(Integer.valueOf(pubId))
					.ifPresent(it -> it.deleteAuthorship(authorship));
					// Add new authorship
					addAuthorship(newAuthor.getId(), pubId, rank);
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
	 * It is equivalent to a call to {@link #mergeAuthors(List, String, String, Function)} with
	 * {@code null} as last argument.
	 *
	 * @param oldAuthorIds The list of all authors to merge because they are considered as duplicate of the new person.
	 * @param newFirstName new author first name
	 * @param newLastName new author last name
	 * @return number of affected publications
	 * @throws Exception if at least 2 of the authors have a page
	 */
	public int mergeAuthors(List<Integer> oldAuthorIds, String newFirstName, String newLastName) throws Exception {
		return mergeAuthors(oldAuthorIds, newFirstName, newLastName, null);
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
	public int mergeAuthors(List<Integer> oldAuthorIds, String newFirstName, String newLastName,
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
				final List<Person> oldAuthors = this.personRepository.findByIdIn(oldAuthorIds);
				return mergeAuthors(oldAuthors, newAuthor);
			}
		}
		return 0;
	}

}
