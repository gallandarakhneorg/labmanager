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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.IdentifiableEntityComparator;
import fr.ciadlab.labmanager.entities.member.Gender;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.member.WebPageNaming;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.gscholar.GoogleScholarPlatform;
import fr.ciadlab.labmanager.io.gscholar.GoogleScholarPlatform.GoogleScholarPerson;
import fr.ciadlab.labmanager.io.scopus.ScopusPlatform;
import fr.ciadlab.labmanager.io.scopus.ScopusPlatform.ScopusPerson;
import fr.ciadlab.labmanager.io.wos.WebOfSciencePlatform;
import fr.ciadlab.labmanager.io.wos.WebOfSciencePlatform.WebOfSciencePerson;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.publication.AuthorshipRepository;
import fr.ciadlab.labmanager.repository.publication.PublicationRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import fr.ciadlab.labmanager.utils.names.PersonNameComparator;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.apache.commons.lang3.mutable.MutableInt;
import org.arakhne.afc.progress.DefaultProgression;
import org.arakhne.afc.progress.Progression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for managing the persons.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class PersonService extends AbstractService {

	private PublicationRepository publicationRepository;

	private AuthorshipRepository authorshipRepository;

	private PersonRepository personRepository;

	private PersonNameParser nameParser;

	private GoogleScholarPlatform googlePlatfom;

	private ScopusPlatform scopusPlatfom;

	private WebOfSciencePlatform wosPlatfom;

	private PersonNameComparator nameComparator;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param publicationRepository the publication repository.
	 * @param authorshipRepository the authorship repository.
	 * @param personRepository the person repository.
	 * @param googlePlatfom the tool for accessing the remote Google Scholar platform.
	 * @param scopusPlatfom the tool for accessing the remote Scopus platform.
	 * @param wosPlatfom the tool for accessing the remote WoS platform.
	 * @param nameParser the parser of person names.
	 * @param nameComparator the comparator of person names.
	 * @param structureService the service for accessing the associated structures.
	 * @param invitationService the service for accessing the person invitations.
	 * @param juryMembershipService the service for accessing the jury memberships.
	 * @param membershipService the service for accessing the organization memberships.
	 * @param projectService the service for accessing the projects.
	 * @param supervisionService the service for accessing the student supervisions.
	 * @param teachingService the service for accessing the teaching activities.
	 */
	public PersonService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired PublicationRepository publicationRepository,
			@Autowired AuthorshipRepository authorshipRepository,
			@Autowired PersonRepository personRepository,
			@Autowired GoogleScholarPlatform googlePlatfom,
			@Autowired ScopusPlatform scopusPlatfom,
			@Autowired WebOfSciencePlatform wosPlatfom,
			@Autowired PersonNameParser nameParser,
			@Autowired PersonNameComparator nameComparator) {
		super(messages, constants);
		this.publicationRepository = publicationRepository;
		this.authorshipRepository = authorshipRepository;
		this.personRepository = personRepository;
		this.googlePlatfom = googlePlatfom;
		this.scopusPlatfom = scopusPlatfom;
		this.wosPlatfom = wosPlatfom;
		this.nameParser = nameParser;
		this.nameComparator = nameComparator;
	}

	/** Replies the list of all the persons from the database.
	 *
	 * @return all the persons.
	 */
	public List<Person> getAllPersons() {
		return this.personRepository.findAll();
	}

	/** Replies the person with the given identifier.
	 *
	 * @param identifier the identifier of the person.
	 * @return the person, or {@code null} if none.
	 */
	public Person getPersonById(int identifier) {
		final Optional<Person> byId = this.personRepository.findById(Integer.valueOf(identifier));
		return byId.orElse(null);
	}

	/** Replies the persons with the given identifiers.
	 *
	 * @param identifiers the identifiers of the persons.
	 * @return the persons.
	 */
	public List<Person> getPersonsByIds(List<Integer> identifiers) {
		return this.personRepository.findAllById(identifiers);
	}

	/** Replies the person with the given webpage identifier.
	 *
	 * @param identifier the identifier of the webpage of the person.
	 * @return the person, or {@code null} if none.
	 */
	public Person getPersonByWebPageId(String identifier) {
		final Optional<Person> byId = this.personRepository.findDistinctByWebPageId(identifier);
		return byId.orElse(null);
	}

	/** Create a person in the database by providing only the name of the person.
	 *
	 * @param firstName the first name of the person.
	 * @param lastName the last name of the person.
	 * @return the person in the database.
	 * @see #createPerson(String, String, Gender, String, String, String, String, String, String, String, String, String, String, String, String, String, WebPageNaming, int, int)
	 */
	public Person createPerson(String firstName, String lastName) {
		return createPerson(
				false,
				firstName, lastName,
				Gender.NOT_SPECIFIED,
				null, // email
				null, // office phone
				null, // mobile phone
				null, // office room
				null, // Gravatar Id
				null, // Orcid
				null, // ResearcherId
				null, // AD Scientific Index
				null, // Scopus Id
				null, // Google Scholar
				null, // Linked-In Id
				null, // Github Id
				null, // Research Gate Id
				null, // Facebook Id
				null, // DBLP URL
				null, // Academia.edu URL
				null, // EU Cordis URL
				WebPageNaming.UNSPECIFIED,
				0, // Google Scholar H-index
				0, // WoS H-index
				0, // Scopus H-index
				0, // Google Scholar citations
				0, // WoS citations
				0); // Scopus citations
	}

	/** Create a person in the database.
	 *
	 * @param validated indicates if the journal is validated by a local authority.
	 * @param firstName the first name of the person.
	 * @param lastName the last name of the person.
	 * @param gender the gender.
	 * @param email the email of the person.
	 * @param officePhone the phone number at office.
	 * @param mobilePhone the mobile phone number.
	 * @param officeRoom the number of the office room.
	 * @param gravatarId the identifier for obtaining a photo on Gravatar.
	 * @param orcid the ORCID of the person.
	 * @param researcherId the identifier of the person on ResearchId/WOS/Publon.
	 * @param scopusId the identifier of the person on Scopus.
	 * @param scholarId the identifier of the person on Google Scholar.
	 * @param linkedInId the identifier of the person on LinkedIn.
	 * @param githubId the identifier of the person on Github.
	 * @param researchGateId the identifier of the person on ResearchGate.
	 * @param adScientificIndexId the identifier of the person on AD Scientific Index.
	 * @param facebookId the identifier of the person on Facebook.
	 * @param dblpURL the URL of the person's page on DBLP.
	 * @param academiaURL the URL of the person's page on Academia.edu.
	 * @param cordisURL the URL of the person's page on European Commission's Cordis.
	 * @param webPageNaming the type of naming for the person's webpage on the organization server.
	 * @param scholarHindex the Hindex of the person on Google Scholar.
	 * @param wosHindex the Hindex of the person on WOS.
	 * @param scopusHindex the Hindex of the person on Scopus.
	 * @param scholarCitations the number of citations for the person on Google Scholar.
	 * @param wosCitations the number of citations for the person on ResearchId/WOS/Publon.
	 * @param scopusCitations the number of citations for the person on Scopus.
	 * @return the person in the database.
	 * @see #createPerson(String, String)
	 * @see Gender
	 */
	public Person createPerson(boolean validated, String firstName, String lastName, Gender gender, String email, String officePhone,
			String mobilePhone, String officeRoom, String gravatarId, String orcid, String researcherId, String scopusId,
			String scholarId, String linkedInId, String githubId, String researchGateId, String adScientificIndexId, String facebookId,
			String dblpURL, String academiaURL, String cordisURL, WebPageNaming webPageNaming,
			int scholarHindex, int wosHindex, int scopusHindex, int scholarCitations, int wosCitations, int scopusCitations) {
		final Person res = new Person();
		res.setFirstName(firstName);
		res.setLastName(lastName);
		res.setGender(gender);
		res.setEmail(email);
		res.setOfficePhone(officePhone);
		res.setMobilePhone(mobilePhone);
		res.setOfficeRoom(officeRoom);
		res.setGravatarId(gravatarId);
		res.setORCID(orcid);
		res.setResearcherId(researcherId);
		res.setScopusId(scopusId);
		res.setGoogleScholarId(scholarId);
		res.setLinkedInId(linkedInId);
		res.setGithubId(githubId);
		res.setResearchGateId(researchGateId);
		res.setAdScientificIndexId(adScientificIndexId);
		res.setFacebookId(facebookId);
		res.setDblpURL(dblpURL);
		res.setAcademiaURL(academiaURL);
		res.setCordisURL(cordisURL);
		res.setWebPageNaming(webPageNaming);
		res.setGoogleScholarHindex(scholarHindex);
		res.setWosHindex(wosHindex);
		res.setScopusHindex(scopusHindex);
		res.setGoogleScholarCitations(scholarCitations);
		res.setWosCitations(wosCitations);
		res.setScopusCitations(scopusCitations);
		res.setValidated(validated);
		this.personRepository.save(res);
		return res;
	}

	/** Change the attributes of a person in the database.
	 *
	 * @param identifier the identifier of the person in the database.
	 * @param validated indicates if the journal is validated by a local authority.
	 * @param firstName the first name of the person.
	 * @param lastName the last name of the person.
	 * @param gender the gender.
	 * @param email the email of the person.
	 * @param officePhone the phone number at office.
	 * @param mobilePhone the mobile phone number.
	 * @param officeRoom the number of the office room.
	 * @param gravatarId the identifier for obtaining a photo on Gravatar.
	 * @param orcid the ORCID of the person.
	 * @param researcherId the identifier of the person on ResearchId/WOS/Publon.
	 * @param scopusId the identifier of the person on Scopus.
	 * @param scholarId the identifier of the person on Google Scholar.
	 * @param linkedInId the identifier of the person on LinkedIn.
	 * @param githubId the identifier of the person on Github.
	 * @param researchGateId the identifier of the person on ResearchGate.
	 * @param adScientificIndexId the identifier of the person on AD Scientific Index.
	 * @param facebookId the identifier of the person on Facebook.
	 * @param dblpURL the URL of the person's page on DBLP.
	 * @param academiaURL the URL of the person's page on Academia.edu.
	 * @param cordisURL the URL of the person's page on European Commission's Cordis.
	 * @param webPageNaming the type of naming for the person's webpage on the organization server.
	 * @param scholarHindex the Hindex of the person on Google Scholar.
	 * @param wosHindex the Hindex of the person on WOS.
	 * @param scopusHindex the Hindex of the person on Scopus.
	 * @param scholarCitations the number of citations for the person on Google Scholar.
	 * @param wosCitations the number of citations for the person on ResearchId/WOS/Publon.
	 * @param scopusCitations the number of citations for the person on Scopus.
	 * @return the updated person.
	 */
	public Person updatePerson(int identifier, boolean validated, String firstName, String lastName, Gender gender, String email, String officePhone,
			String mobilePhone, String officeRoom, String gravatarId, String orcid, String researcherId, String scopusId, String scholarId,
			String linkedInId, String githubId, String researchGateId, String adScientificIndexId, String facebookId, String dblpURL, String academiaURL,
			String cordisURL, WebPageNaming webPageNaming, int scholarHindex, int wosHindex, int scopusHindex,
			int scholarCitations, int wosCitations, int scopusCitations) {
		final Optional<Person> res = this.personRepository.findById(Integer.valueOf(identifier));
		if (res.isPresent()) {
			final Person person = res.get();
			if (!Strings.isNullOrEmpty(firstName)) {
				person.setFirstName(firstName);
			}
			if (!Strings.isNullOrEmpty(lastName)) {
				person.setLastName(lastName);
			}
			person.setGender(gender);
			person.setEmail(email);
			person.setOfficePhone(officePhone);
			person.setMobilePhone(mobilePhone);
			person.setOfficeRoom(officeRoom);
			person.setGravatarId(gravatarId);
			person.setORCID(orcid);
			person.setResearcherId(researcherId);
			person.setScopusId(scopusId);
			person.setGoogleScholarId(scholarId);
			person.setLinkedInId(linkedInId);
			person.setGithubId(githubId);
			person.setResearchGateId(researchGateId);
			person.setAdScientificIndexId(adScientificIndexId);
			person.setFacebookId(facebookId);
			person.setDblpURL(dblpURL);
			person.setAcademiaURL(academiaURL);
			person.setCordisURL(cordisURL);
			person.setWebPageNaming(webPageNaming);
			person.setGoogleScholarHindex(scholarHindex);
			person.setWosHindex(wosHindex);
			person.setScopusHindex(scopusHindex);
			person.setGoogleScholarCitations(scholarCitations);
			person.setWosCitations(wosCitations);
			person.setScopusCitations(scopusCitations);
			person.setValidated(validated);
			this.personRepository.save(person);
			return person;
		}
		return null;
	}

	/** Remove the person with the given identifier from the database.
	 *
	 * @param identifier the identifier of the person in the database.
	 */
	public void removePerson(int identifier) {
		final Integer id = Integer.valueOf(identifier);
		final Optional<Person> optPerson = this.personRepository.findById(id);
		if (optPerson.isPresent()) {
			final Person person = optPerson.get();
			for (final Authorship authorship : person.getAuthorships()) {
				final int rank = authorship.getAuthorRank();
				final Publication publication = authorship.getPublication();
				authorship.setPerson(null);
				authorship.setPublication(null);
				//When removing an author, reduce the ranks of the other authorships for the pubs he made.
				final Iterator<Authorship> iterator = publication.getAuthorshipsRaw().iterator();
				while (iterator.hasNext()) {
					final Authorship otherAuthorsip = iterator.next();
					if (authorship.getId() != otherAuthorsip.getId()) {
						final int oRank = otherAuthorsip.getAuthorRank();
						if (oRank > rank) {
							otherAuthorsip.setAuthorRank(oRank - 1);
							this.authorshipRepository.save(otherAuthorsip);
						}
					} else {
						iterator.remove();
					}
				}
				this.publicationRepository.save(publication);
			}
			// membership and autorship are deleted by cascade
			person.deleteAllAuthorships();
			this.personRepository.deleteById(id);
		}
	}

	/** Replies the database identifier for the person with the last name and first name.
	 * If there is multiple persons with the same last name and first name, one is replied.
	 * <p>The name matching is exact, i.e., the equality test is used for selecting the persons.
	 * For using a similarity test on the names, see {@link #getPersonIdBySimilarName(String, String)}.
	 *
	 * @param firstName the first name of the person.
	 * @param lastName the last name of the person.
	 * @return the identifier, or {@code 0} if no person has the given name.
	 * @see #getPersonIdBySimilarName(String, String)
	 */
	public int getPersonIdByName(String firstName, String lastName) {
		final Set<Person> res = this.personRepository.findByFirstNameAndLastName(firstName, lastName);
		if (res.size() > 0) {
			return res.iterator().next().getId();
		}
		return 0;
	}

	/** Replies the database identifier for the person with a similar name to the givan last name and givan first name.
	 * If there is multiple persons with similar last name and first name, one is replied.
	 * <p>The name matching is based on similarity of names.
	 * For using a strict equality test on the names, see {@link #getPersonIdByName(String, String)}.
	 *
	 * @param firstName the first name of the person.
	 * @param lastName the last name of the person.
	 * @return the identifier, or {@code 0} if no person has the given name.
	 * @see #getPersonIdByName(String, String)
	 */
	public int getPersonIdBySimilarName(String firstName, String lastName) {
		final Person person = getPersonBySimilarName(firstName, lastName);
		if (person != null) {
			return person.getId();
		}
		return 0;
	}

	/** Replies the person with a similar name to the givan last name and givan first name.
	 * If there is multiple persons with similar last name and first name, one is replied.
	 * <p>The name matching is based on similarity of names.
	 * For using a strict equality test on the names, see {@link #getPersonIdByName(String, String)}.
	 *
	 * @param firstName the first name of the person.
	 * @param lastName the last name of the person.
	 * @return the person, or {@code null} if no person has the given name.
	 * @see #getPersonIdBySimilarName(String, String)
	 */
	public Person getPersonBySimilarName(String firstName, String lastName) {
		if (!Strings.isNullOrEmpty(firstName) || !Strings.isNullOrEmpty(lastName)) {
			for (final Person person : this.personRepository.findAll()) {
				if (this.nameComparator.isSimilar(firstName, lastName, person.getFirstName(), person.getLastName())) {
					return person;
				}
			}
		}
		return null;
	}

	/** Extract the list of the authors.
	 * <p>The format of the list of authors follows the rules of {@link PersonNameParser}.
	 * <p>The returned authors are not saved into the database. It means that if the given
	 * list of authors contains a known author, this person is read from the database.
	 * If the author is unknown, a {@link Person} object is created but not saved into the
	 * database.
	 * 
	 * @param authorText the list of authors to parse.
	 * @param useNameSimilarity indicates of the member search from their name is based on similar names, if {@code true};
	 *     or on exact names, if {@code false}.
	 * @param assignRandomId indicates if a random identifier will be assigned to the created entities.
	 *     If this argument is {@code true}, a numeric id will be computed and assign to all the JPA entities.
	 *     If this argument is {@code false}, the ids of the JPA entities will be the default values, i.e., {@code 0}.
	 * @param ensureAtLeastOneMember if {@code true}, at least one member of a research organization is required from the
	 *     the list of the persons. If {@code false}, the list of persons could contain no organization member.
	 * @return the list of authors.
	 * @see #containsAMember(List)
	 */
	public List<Person> extractPersonsFrom(String authorText, boolean useNameSimilarity, boolean assignRandomId, boolean ensureAtLeastOneMember) {
		final MutableInt memberCount = new MutableInt();
		final List<Person> persons = new ArrayList<>();
		this.nameParser.parseNames(authorText, (fn, von, ln, pos) -> {
			// Build last name
			final StringBuilder firstname = new StringBuilder();
			firstname.append(fn);
			if (!Strings.isNullOrEmpty(von)) {
				firstname.append(" "); //$NON-NLS-1$
				firstname.append(von);
			}
			//
			final int id;
			if (useNameSimilarity) {
				id = getPersonIdBySimilarName(firstname.toString(), ln);
			} else {
				id = getPersonIdByName(firstname.toString(), ln);
			}
			Person person = null;
			if (id != 0) {
				person = getPersonById(id);
			}
			if (person == null) {
				person = new Person();
				person.setFirstName(firstname.toString());
				person.setLastName(ln);
				if (assignRandomId) {
					person.setId(generateUUID().intValue());
				}
			} else {
				memberCount.increment();
			}
			persons.add(person);
		});
		if (ensureAtLeastOneMember && memberCount.intValue() <= 0) {
			throw new IllegalArgumentException("The list of the authors does not contain a member of a known research organization."); //$NON-NLS-1$
		}
		return persons;
	}

	/** Replies if the given list of authors contains at least one person who is associated to a research organization,
	 * i.e., with a membership. The name similarity is used for searching the members based on their names.
	 *
	 * @param authors the list of authors. It is a list of database identifiers (for known persons) and full name
	 *     (for unknown persons).
	 * @param useNameSimilarity indicates of the member search from their name is based on similar names, if {@code true};
	 *     or on exact names, if {@code false}.
	 * @return {@code true} if one person with a membership was found.
	 */
	public boolean containsAMember(List<String> authors, boolean useNameSimilarity) {
		final Pattern idPattern = Pattern.compile("\\d+"); //$NON-NLS-1$
		try {
			for (final String author : authors) {
				if (author != null) {
					int authorId = 0;
					if (idPattern.matcher(author).matches()) {
						// Numeric value means that the person is known.
						try {
							authorId = Integer.parseInt(author);
						} catch (Throwable ex) {
							// Silent
						}
					}
					if (authorId == 0) {
						// The author seems to be not in the database already. Check it based on the name.
						final String firstName = this.nameParser.parseFirstName(author);
						final String lastName = this.nameParser.parseLastName(author);
						if (useNameSimilarity) {
							authorId = getPersonIdBySimilarName(firstName, lastName);
						} else {
							authorId = getPersonIdByName(firstName, lastName);
						}
					}
					if (authorId != 0) {
						// Check if the given author identifier corresponds to a known person with memberships.
						final Optional<Person> optPers = this.personRepository.findById(Integer.valueOf(authorId));
						if (optPers.isPresent() && !optPers.get().getMemberships().isEmpty()) {
							throw new SuccessException();
						}
					}
				}
			}
		} catch (SuccessException ex) {
			return true;
		}
		return false;
	}

	/** Compute and replies the updates of the ranking indicators for all the persons in the given organization.
	 *
	 * <p>CAUTION: The person ranking is considered only if the person is already associated to a positive H-index or
	 * number of citations.
	 *
	 * @param organization the organization in which the persons to be considered are involved.
	 * @param progression the progression indicator. 
	 * @param callback the callback for consuming the the map that maps persons to the maps of the updates.
	 *      THe keys are: {@code id}, {@code name}, {@code wosHindex}, {@code currentWosHindex},
	 *      {@code scopusHindex}, {@code currentScopusHindex}, {@code scholarHindex}, {@code currentScholarHindex}.
	 * @throws Exception if is it impossible to access to a remote resource.
	 */
	public void computePersonRankingIndicatorUpdates(ResearchOrganization organization,
			Progression progression,
			BiConsumer<Person, Map<String, Object>> callback) throws Exception {
		final Set<Person> treatedIdentifiers = new TreeSet<>(new IdentifiableEntityComparator());
		final Set<Membership> memberships = organization.getMemberships();
		final Progression progress = progression == null ? new DefaultProgression() : progression;
		progress.setProperties(0, 0, memberships.size(), false);
		for (final Membership membership : memberships) {
			final Person person = membership.getPerson();
			final Progression subTasks = progress.subTask(1, 0, 3);
			if (treatedIdentifiers.add(person)) {
				progress.setComment(getMessage("personService.GetPersonIndicatorUpdatesFor", person.getFullNameWithLastNameFirst())); //$NON-NLS-1$
				final Map<String, Object> newPersonIndicators = new HashMap<>();
				readGoogleScholarIndicators(person, newPersonIndicators);
				subTasks.increment();
				readScopusIndicators(person, newPersonIndicators);
				subTasks.increment();
				readWosIndicators(person, newPersonIndicators);
				if (!newPersonIndicators.isEmpty()) {
					callback.accept(person, newPersonIndicators);
				}
			}
			progress.increment();
		}
		progress.end();
	}

	private void readGoogleScholarIndicators(Person person, Map<String, Object> newIndicators) {
		getLogger().info("Get Google Scholar indicators for " + person.getFullName()); //$NON-NLS-1$
		final boolean hindexEnabled = person.getGoogleScholarHindex() > 0;
		final boolean citationsEnabled = person.getGoogleScholarCitations() > 0;
		if (!Strings.isNullOrEmpty(person.getGoogleScholarId()) && (hindexEnabled || citationsEnabled)) {
			final URL url = person.getGoogleScholarURL();
			if (url != null) {
				try {
					final GoogleScholarPerson indicators = this.googlePlatfom.getPersonRanking(url, null);
					if (indicators != null) {
						if (hindexEnabled) {
							if (indicators.hindex > 0) {
								newIndicators.put("scholarHindex", Integer.valueOf(indicators.hindex)); //$NON-NLS-1$
							}
							newIndicators.put("currentScholarHindex", Integer.valueOf(person.getGoogleScholarHindex())); //$NON-NLS-1$
						}
						if (citationsEnabled) {
							if (indicators.citations > 0) {
								newIndicators.put("scholarCitations", Integer.valueOf(indicators.citations)); //$NON-NLS-1$
							}
							newIndicators.put("currentScholarCitations", Integer.valueOf(person.getGoogleScholarCitations())); //$NON-NLS-1$
						}
					}
				} catch (Throwable ex) {
					//
				}
			}
		}
	}

	private void readScopusIndicators(Person person, Map<String, Object> newIndicators) {
		getLogger().info("Get Scopus indicators for " + person.getFullName()); //$NON-NLS-1$
		final boolean hindexEnabled = person.getScopusHindex() > 0;
		final boolean citationsEnabled = person.getScopusCitations() > 0;
		if (!Strings.isNullOrEmpty(person.getScopusId()) && (hindexEnabled || citationsEnabled)) {
			final URL url = person.getScopusURL();
			if (url != null) {
				try {
					final ScopusPerson indicators = this.scopusPlatfom.getPersonRanking(url, null);
					if (indicators != null) {
						if (hindexEnabled) {
							if (indicators.hindex > 0) {
								newIndicators.put("scopusHindex", Integer.valueOf(indicators.hindex)); //$NON-NLS-1$
							}
							newIndicators.put("currentScopusHindex", Integer.valueOf(person.getScopusHindex())); //$NON-NLS-1$
						}
						if (citationsEnabled) {
							if (indicators.citations > 0) {
								newIndicators.put("scopusCitations", Integer.valueOf(indicators.citations)); //$NON-NLS-1$
							}
							newIndicators.put("currentScopusCitations", Integer.valueOf(person.getScopusCitations())); //$NON-NLS-1$
						}
					}
				} catch (Throwable ex) {
					//
				}
			}
		}
	}

	private void readWosIndicators(Person person, Map<String, Object> newIndicators) {
		getLogger().info("Get WoS indicators for " + person.getFullName()); //$NON-NLS-1$
		final boolean hindexEnabled = person.getWosHindex() > 0;
		final boolean citationsEnabled = person.getWosCitations() > 0;
		if (!Strings.isNullOrEmpty(person.getResearcherId()) && (hindexEnabled || citationsEnabled)) {
			final URL url = person.getResearcherIdURL();
			if (url != null) {
				try {
					final WebOfSciencePerson indicators = this.wosPlatfom.getPersonRanking(url, null);
					if (indicators != null) {
						if (hindexEnabled) {
							if (indicators.hindex > 0) {
								newIndicators.put("wosHindex", Integer.valueOf(indicators.hindex)); //$NON-NLS-1$
							}
							newIndicators.put("currentWosHindex", Integer.valueOf(person.getWosHindex())); //$NON-NLS-1$
						}
						if (citationsEnabled) {
							if (indicators.citations > 0) {
								newIndicators.put("wosCitations", Integer.valueOf(indicators.citations)); //$NON-NLS-1$
							}
							newIndicators.put("currentWosCitations", Integer.valueOf(person.getWosCitations())); //$NON-NLS-1$
						}
					}
				} catch (Throwable ex) {
					//
				}
			}
		}
	}

	/** Save person indicators. If a person is not mentionned in the given map, her/his associated indicators will be not changed.
	 *
	 * @param changes the changes to apply.
	 */
	public void setPersonIndicators(Map<Integer, PersonIndicators> changes) {
		if (changes != null) {
			for (final Entry<Integer, PersonIndicators> entry : changes.entrySet()) {
				final Optional<Person> person = this.personRepository.findById(entry.getKey());
				if (person.isEmpty()) {
					throw new IllegalArgumentException("Person not found: " + entry.getKey()); //$NON-NLS-1$
				}
				final PersonIndicators indicators = entry.getValue();
				if (indicators != null) {
					boolean change = false;
					final Person pers = person.get();
					if (indicators.wos.hindex >= 0) {
						pers.setWosHindex(indicators.wos.hindex);
						change = true;
					}
					if (indicators.wos.citations >= 0) {
						pers.setWosCitations(indicators.wos.citations);
						change = true;
					}
					if (indicators.scopus.hindex >= 0) {
						pers.setScopusHindex(indicators.scopus.hindex);
						change = true;
					}
					if (indicators.scopus.citations >= 0) {
						pers.setScopusCitations(indicators.scopus.citations);
						change = true;
					}
					if (indicators.scholar.hindex >= 0) {
						pers.setGoogleScholarHindex(indicators.scholar.hindex);
						change = true;
					}
					if (indicators.scholar.citations >= 0) {
						pers.setGoogleScholarCitations(indicators.scholar.citations);
						change = true;
					}
					if (change) {
						this.personRepository.save(pers);
					}
				}
			}
		}
	}

	/** Person indicators
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 5.4
	 */
	public static class PersonIndicators {

		/** WoS indicators.
		 */
		public final WebOfSciencePerson wos;
		
		/** Scopus indicators.
		 */
		public final ScopusPerson scopus;

		/** Google Scholar indicators.
		 */
		public final GoogleScholarPerson scholar;

		/** Constructor.
		 *
		 * @param wosHindex H-index from WoS, or {@code -1} if unknown.
		 * @param wosCitations Citations from WoS, or {@code -1} if unknown.
		 * @param scopusHindex H-index from Scopus, or {@code -1} if unknown.
		 * @param scopusCitations Citations from Scopus, or {@code -1} if unknown.
		 * @param scholarHindex H-index from Google Scholar, or {@code -1} if unknown.
		 * @param scholarCitations Citations from Google Scholar, or {@code -1} if unknown.
		 */
		public PersonIndicators(int wosHindex, int wosCitations, int scopusHindex, int scopusCitations, int scholarHindex, int scholarCitations) {
			this.wos = new WebOfSciencePerson(wosHindex, wosCitations);
			this.scopus = new ScopusPerson(scopusHindex, scopusCitations);
			this.scholar = new GoogleScholarPerson(scholarHindex, scholarCitations);
		}

	}

	/** Internal exception
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class SuccessException extends RuntimeException {

		private static final long serialVersionUID = -2814067417193916087L;

		SuccessException() {
			//
		}

	}

}
