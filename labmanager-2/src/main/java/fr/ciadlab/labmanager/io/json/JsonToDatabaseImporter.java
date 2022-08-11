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

package fr.ciadlab.labmanager.io.json;

import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.journal.JournalQualityAnnualIndicators;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.entities.publication.JournalBasedPublication;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationComparator;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.repository.journal.JournalQualityAnnualIndicatorsRepository;
import fr.ciadlab.labmanager.repository.journal.JournalRepository;
import fr.ciadlab.labmanager.repository.member.MembershipRepository;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.organization.ResearchOrganizationRepository;
import fr.ciadlab.labmanager.repository.publication.AuthorshipRepository;
import fr.ciadlab.labmanager.repository.publication.PublicationRepository;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Importer of JSON data into the database.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Component
public class JsonToDatabaseImporter extends JsonTool {

	private ResearchOrganizationRepository organizationRepository;

	private PersonRepository personRepository;

	private PersonService personService;

	private MembershipRepository membershipRepository;

	private JournalRepository journalRepository;

	private JournalQualityAnnualIndicatorsRepository journalIndicatorsRepository;

	private PublicationRepository publicationRepository;

	private PublicationComparator publicationComparator;

	private AuthorshipRepository authorshipRepository;

	private PersonNameParser personNameParser;

	private final Multimap<String, String> fieldAliases = LinkedListMultimap.create();

	private boolean fake;

	/** Constructor.
	 * 
	 * @param organizationRepository the accessor to the organization repository.
	 * @param personRepository the accessor to the person repository.
	 * @param personService the accessor to the high-level person services.
	 * @param membershipRepository the accessor to the membership repository.
	 * @param journalRepository the accessor to the journal repository.
	 * @param journalIndicatorsRepository the accessor to the repository of the journal quality annual indicators.
	 * @param publicationRepository the accessor to the repository of the publications.
	 * @param publicationComparator the comparator of publications.
	 * @param authorshipRepository the accessor to the authorships.
	 * @param personNameParser the parser of person names.
	 */
	public JsonToDatabaseImporter(
			@Autowired ResearchOrganizationRepository organizationRepository,
			@Autowired PersonRepository personRepository,
			@Autowired PersonService personService,
			@Autowired MembershipRepository membershipRepository,
			@Autowired JournalRepository journalRepository,
			@Autowired JournalQualityAnnualIndicatorsRepository journalIndicatorsRepository,
			@Autowired PublicationRepository publicationRepository,
			@Autowired PublicationComparator publicationComparator,
			@Autowired AuthorshipRepository authorshipRepository,
			@Autowired PersonNameParser personNameParser) {
		this.organizationRepository = organizationRepository;
		this.personRepository = personRepository;
		this.personService = personService;
		this.membershipRepository = membershipRepository;
		this.journalRepository = journalRepository;
		this.journalIndicatorsRepository = journalIndicatorsRepository;
		this.publicationRepository = publicationRepository;
		this.publicationComparator = publicationComparator;
		this.authorshipRepository = authorshipRepository;
		this.personNameParser = personNameParser;
		initializeFieldAliases();
	}

	/** Initialize the collection of standard and hard-coded field aliases. 
	 */
	protected void initializeFieldAliases() {
		addFieldAlias(YEAR_KEY, PUBLICATIONYEAR_KEY);
		addFieldAlias(LANGUAGE_KEY, MAJORLANGUAGE_KEY);
		addFieldAlias(URL_KEY, EXTRAURL_KEY);
	}

	/** Add a alias for a field name. The alias is used for retrieving the setter methods based on the
	 * field name. When a setter method is searched for the field {@code f}, the methods {@code setF} and
	 * @code{setAlias} are considered as candidates, where {@code alias} is defined as an alias of {@code f}.
	 *
	 * @param sourceName the name of the field to associated to an alias. It is equivalent to {@code f} in the example.
	 * @param aliasName the name of the alias. It is equivalent to {@code alias} in the example.
	 */
	public void addFieldAlias(String sourceName, String aliasName) {
		assert !Strings.isNullOrEmpty(sourceName);
		assert !Strings.isNullOrEmpty(aliasName);
		this.fieldAliases.put(sourceName, aliasName);
	}

	/** Replies the aliases for a field name. The alias is used for retrieving the setter methods based on the
	 * field name. When a setter method is searched for the field {@code f}, the methods {@code setF} and
	 * @code{setAlias} are considered as candidates, where {@code alias} is defined as an alias of {@code f}.
	 *
	 * @param sourceName the name of the field to associated to an alias. It is equivalent to {@code f} in the example.
	 * @return the list of aliases. It contains {@code alias} that is mentionned in the example.
	 */
	public Collection<String> getFieldAliases(String sourceName) {
		assert !Strings.isNullOrEmpty(sourceName);
		return this.fieldAliases.get(sourceName);
	}

	private static <T> T get(Object content, String key, Class<T> type) {
		if (content != null && !Strings.isNullOrEmpty(key) && content instanceof Map) {
			final Object value = ((Map<?, ?>) content).get(key);
			if (type.isInstance(value)) {
				return type.cast(value);
			}
		}
		return null;
	}

	private static <T extends Enum<T>> T getEnum(Object content, String key, Class<T> type) {
		if (content != null && !Strings.isNullOrEmpty(key) && content instanceof Map) {
			final Object value = ((Map<?, ?>) content).get(key);
			if (value != null) {
				try {
					final String name = value.toString();
					final T[] constants = type.getEnumConstants();
					if (constants != null) {
						for (final T cons : constants) {
							if (name.equalsIgnoreCase(cons.name())) {
								return cons;
							}
						}
					}
				} catch (Throwable ex) {
					//
				}
			}
		}
		return null;
	}

	/** Replies if the importer is using a fake saving in the database.
	 * If it replies {@code true}, the importer does not save into the database.
	 * If it replies {@code false}, the importer does save in the database.
	 *
	 * @return {@code true} for fake saving.
	 */
	public boolean isFake() {
		return this.fake;
	}

	/** Change if the importer is using a fake saving in the database.
	 * If it replies {@code true}, the importer does not save into the database.
	 * If it replies {@code false}, the importer does save in the database.
	 *
	 * @param fake {@code true} for fake saving.
	 */
	public void setFake(boolean fake) {
		this.fake = fake;
	}

	private static String getId(Object content) {
		return get(content, ID_FIELDNAME, String.class);
	}

	private static String getRef(Object content) {
		if (content != null && content instanceof Map) {
			final Object value = ((Map<?, ?>) content).get(ID_FIELDNAME);
			if (value != null) {
				return value.toString();
			}
		}
		return null;
	}

	private static <T> T get(Object value, Class<T> type) {
		if (value != null && type.isInstance(value)) {
			return type.cast(value);
		}
		return null;
	}

	/** Create the instance of an object that is initialized from the given JSON source.
	 *
	 * @param <T> the expected type of object.
	 * @param type the expected type of object.
	 * @param source the JSON source.
	 * @param aliasRepository the repository of aliases. This repository is filled up by this function.
	 * @param failIfNoSetter indicates if the function should fail if there is no setter method found for an attribute name in
	 *      the JSON file.
	 * @return the object or {@code null}.
	 * @throws Exception if the object cannot be created.
	 */
	protected <T> T createObject(Class<T> type, Map<String, Object> source,
			Map<String, Set<String>> aliasRepository,
			Predicate<Entry<String, Object>> failIfNoSetter) throws Exception {
		final T obj = type.getConstructor().newInstance();
		for (final Entry<String, Object> entry : source.entrySet()) {
			if (!Strings.isNullOrEmpty(entry.getKey()) && !entry.getKey().startsWith(HIDDEN_FIELD_PREFIX)
					&& !entry.getKey().startsWith(SPECIAL_FIELD_PREFIX)) {
				final Set<String> aliases = aliasRepository.computeIfAbsent(entry.getKey(), it -> {
					final TreeSet<String> set = new TreeSet<>();
					set.add(SETTER_FUNCTION_PREFIX + entry.getKey().toLowerCase());
					for (final String alias : getFieldAliases(entry.getKey())) {
						set.add(SETTER_FUNCTION_PREFIX + alias.toLowerCase());
					}
					return set;
				});
				final Method method = findSetterMethod(type, aliases, entry.getValue());
				if (method != null) {
					method.invoke(obj, entry.getValue());
				} else if (failIfNoSetter != null && failIfNoSetter.test(entry)) {
					throw new IllegalArgumentException("Setter function not found for the attribute: " + entry.getKey() //$NON-NLS-1$
					+ "; with a value of type: " + entry.getValue().getClass()); //$NON-NLS-1$
				}
			}
		}
		return obj;
	}

	/** Run the importer.
	 *
	 * @param url the URL of the JSON file to read.
	 * @throws Exception if there is problem for importing.
	 */
	@SuppressWarnings("unchecked")
	public void importToDatabase(URL url) throws Exception {
		final Gson gson = new Gson();
		final Map<Object, Object> content; 
		try (final InputStreamReader isr = new InputStreamReader(url.openStream())) {
			content = gson.fromJson(isr, Map.class);
		}
		if (content != null && !content.isEmpty()) {
			final Map<String, Object> objectRepository = new TreeMap<>();
			final Map<String, Set<String>> aliasRepository = new TreeMap<>();
			final int nb0 = insertOrganizations(get(content, RESEARCHORGANIZATIONS_SECTION, List.class), objectRepository, aliasRepository);
			final int nb1 = insertPersons(get(content, PERSONS_SECTION, List.class), objectRepository, aliasRepository);
			final int nb2 = insertMemberships(get(content, MEMBERSHIPS_SECTION, List.class), objectRepository, aliasRepository);
			final int nb3 = insertJournals(get(content, JOURNALS_SECTION, List.class), objectRepository, aliasRepository);
			final Pair<Integer, Integer> added = insertPublications(get(content, PUBLICATIONS_SECTION, List.class), objectRepository, aliasRepository);
			final int nb4 = added != null ? added.getLeft().intValue() : 0;
			final int nb5 = added != null ? added.getRight().intValue() : 0;
			getLogger().info("Summary of inserts: " //$NON-NLS-1$
					+ nb0 + " organizations; " //$NON-NLS-1$
					+ (nb1 + nb5) + " persons; " //$NON-NLS-1$
					+ nb2 + " memberships; " //$NON-NLS-1$
					+ nb3 + " journals; " //$NON-NLS-1$
					+ nb4 + " publications."); //$NON-NLS-1$
		}
	}

	/** Create the research organizations in the database.
	 *
	 * @param organizations the list of organizations in the Json source.
	 * @param objectRepository the repository of the JSON elements with {@code "@id"} field.
	 * @param aliasRepository the repository of field aliases.
	 * @return the number of new organizations in the database.
	 * @throws Exception if an organization cannot be created.
	 */
	@SuppressWarnings("unchecked")
	protected int insertOrganizations(List<?> organizations, Map<String, Object> objectRepository,
			Map<String, Set<String>> aliasRepository) throws Exception {
		int nbNew = 0;
		if (organizations != null && !organizations.isEmpty()) {
			getLogger().info("Inserting " + organizations.size() + " organizations..."); //$NON-NLS-1$ //$NON-NLS-2$
			int i = 0;
			final List<Pair<ResearchOrganization, String>> superOrgas = new ArrayList<>();
			for (Object orgaObject : organizations) {
				getLogger().info("> Organization " + (i + 1) + "/" + organizations.size()); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					final String id = getId(orgaObject);
					ResearchOrganization orga = createObject(ResearchOrganization.class, get(orgaObject, Map.class),
							aliasRepository, null);
					if (orga != null) {
						final Optional<ResearchOrganization> existing = this.organizationRepository.findDistinctByAcronymOrName(orga.getAcronym(), orga.getName());
						if (existing.isEmpty()) {
							if (!isFake()) {
								orga = this.organizationRepository.save(orga);
							}
							++nbNew;
							getLogger().info("  + " + orga.getAcronymOrName() + " (id: " + orga.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							if (!Strings.isNullOrEmpty(id)) {
								objectRepository.put(id, orga);
							}
							final String superOrga = getRef(get(orgaObject, SUPERORGANIZATION_KEY, Object.class));
							if (!Strings.isNullOrEmpty(superOrga)) {
								superOrgas.add(Pair.of(orga, superOrga));
							}
						} else {
							getLogger().info("  X " + existing.get().getAcronymOrName() + " (id: " + existing.get().getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							if (!Strings.isNullOrEmpty(id)) {
								objectRepository.put(id, existing.get());
							}
						}
					}
				} catch (Throwable ex) {
					throw new UnableToImportJsonException(RESEARCHORGANIZATIONS_SECTION, i, orgaObject,ex);
				}
				++i;
			}
			for (final Pair<ResearchOrganization, String> entry : superOrgas) {
				final ResearchOrganization sup = get(objectRepository, entry.getRight(), ResearchOrganization.class);
				if (sup == null) {
					throw new IllegalArgumentException("Invalid reference to Json element with id: " + entry.getRight()); //$NON-NLS-1$
				}
				entry.getLeft().setSuperOrganization(sup);
				sup.getSubOrganizations().add(entry.getLeft());
				if (!isFake()) {
					this.organizationRepository.save(entry.getLeft());
					this.organizationRepository.save(sup);
				}
			}
		}
		return nbNew;
	}

	/** Create the persons in the database.
	 *
	 * @param persons the list of persons in the Json source.
	 * @param objectRepository the repository of the JSON elements with {@code "@id"} field.
	 * @param aliasRepository the repository of field aliases.
	 * @return the number of new persons in the database.
	 * @throws Exception if a person cannot be created.
	 */
	@SuppressWarnings("unchecked")
	protected int insertPersons(List<?> persons, Map<String, Object> objectRepository,
			Map<String, Set<String>> aliasRepository) throws Exception {
		int nbNew = 0;
		if (persons != null && !persons.isEmpty()) {
			getLogger().info("Inserting " + persons.size() + " persons..."); //$NON-NLS-1$ //$NON-NLS-2$
			int i = 0;
			for (Object personObject : persons) {
				getLogger().info("> Person " + (i + 1) + "/" + persons.size()); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					final String id = getId(personObject);
					Person person = createObject(Person.class, get(personObject, Map.class), aliasRepository, null);
					if (person != null) {
						final Optional<Person> existing = this.personRepository.findDistinctByFirstNameAndLastName(person.getFirstName(), person.getLastName());
						if (existing.isEmpty()) {
							if (!isFake()) {
								person = this.personRepository.save(person);
							}
							++nbNew;
							getLogger().info("  + " + person.getFullName() + " (id: " + person.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							if (!Strings.isNullOrEmpty(id)) {
								objectRepository.put(id, person);
							}
						} else {
							getLogger().info("  X " + existing.get().getFullName() + " (id: " + existing.get().getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							if (!Strings.isNullOrEmpty(id)) {
								objectRepository.put(id, existing.get());
							}
						}
					}
				} catch (Throwable ex) {
					throw new UnableToImportJsonException(PERSONS_SECTION, i, personObject, ex);
				}
				++i;
			}
		}
		return nbNew;
	}

	/** Create the memberships in the database.
	 *
	 * @param memberships the list of memberships in the Json source.
	 * @param objectRepository the repository of the JSON elements with {@code "@id"} field.
	 * @param aliasRepository the repository of field aliases.
	 * @return the number of new memberships in the database.
	 * @throws Exception if a membership cannot be created.
	 */
	@SuppressWarnings("unchecked")
	protected int insertMemberships(List<?> memberships, Map<String, Object> objectRepository,
			Map<String, Set<String>> aliasRepository) throws Exception {
		int nbNew = 0;
		if (memberships != null && !memberships.isEmpty()) {
			getLogger().info("Inserting " + memberships.size() + " memberships..."); //$NON-NLS-1$ //$NON-NLS-2$
			int i = 0;
			for (Object membershipObject : memberships) {
				getLogger().info("> Membership " + (i + 1) + "/" + memberships.size()); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					final String id = getId(membershipObject);
					Membership membership = createObject(Membership.class, get(membershipObject, Map.class),
							aliasRepository, null);
					if (membership != null) {
						final String personId = getRef(get(membershipObject, PERSON_KEY, Object.class));
						if (Strings.isNullOrEmpty(personId)) {
							throw new IllegalArgumentException("Invalid person reference for membership with id: " + id); //$NON-NLS-1$
						}
						final Person targetPerson = get(objectRepository, personId, Person.class);
						if (targetPerson == null) {
							throw new IllegalArgumentException("Invalid person reference for membership with id: " + id); //$NON-NLS-1$
						}
						//
						final String orgaId = getRef(get(membershipObject, RESEARCHORGANIZATION_KEY, Object.class));
						if (Strings.isNullOrEmpty(orgaId)) {
							throw new IllegalArgumentException("Invalid organization reference for membership with id: " + id); //$NON-NLS-1$
						}
						final ResearchOrganization targetOrganization = get(objectRepository, orgaId, ResearchOrganization.class);
						if (targetOrganization == null) {
							throw new IllegalArgumentException("Invalid organization reference for membership with id: " + id); //$NON-NLS-1$
						}
						//
						final Set<Membership> existings = this.membershipRepository.findByResearchOrganizationIdAndPersonId(
								targetOrganization.getId(), targetPerson.getId());
						final Membership finalmbr0 = membership;
						final Stream<Membership> existing0 = existings.stream().filter(it -> {
							assert it.getPerson().getId() == targetPerson.getId();
							assert it.getResearchOrganization().getId() == targetOrganization.getId();
							return Objects.equals(it.getMemberStatus(), finalmbr0.getMemberStatus())
									&& Objects.equals(it.getMemberSinceWhen(), finalmbr0.getMemberSinceWhen())
									&& Objects.equals(it.getMemberToWhen(), finalmbr0.getMemberToWhen());
						});
						final Optional<Membership> existing = existing0.findAny();
						if (existing.isEmpty()) {
							membership.setPerson(targetPerson);
							membership.setResearchOrganization(targetOrganization);
							targetPerson.getResearchOrganizations().add(membership);
							targetOrganization.getMembers().add(membership);
							if (!isFake()) {
								membership = this.membershipRepository.save(membership);
							}
							++nbNew;
							getLogger().info("  + " + targetOrganization.getAcronymOrName() //$NON-NLS-1$
							+ " - " + targetPerson.getFullName() //$NON-NLS-1$
							+ " (id: " + membership.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
							if (!Strings.isNullOrEmpty(id)) {
								objectRepository.put(id, membership);
							}
						} else {
							getLogger().info("  X " + targetOrganization.getAcronymOrName() //$NON-NLS-1$
							+ " - " + targetPerson.getFullName() //$NON-NLS-1$
							+ " (id: " + existing.get().getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
							if (!Strings.isNullOrEmpty(id)) {
								objectRepository.put(id, existing.get());
							}
						}
					}
				} catch (Throwable ex) {
					throw new UnableToImportJsonException(MEMBERSHIPS_SECTION, i, membershipObject, ex);
				}
				++i;
			}
		}
		return nbNew;
	}

	/** Create the journals in the database.
	 *
	 * @param journals the list of journals in the Json source.
	 * @param objectRepository the repository of the JSON elements with {@code "@id"} field.
	 * @param aliasRepository the repository of field aliases.
	 * @return the number of new journals in the database.
	 * @throws Exception if a membership cannot be created.
	 */
	@SuppressWarnings("unchecked")
	protected int insertJournals(List<?> journals, Map<String, Object> objectRepository,
			Map<String, Set<String>> aliasRepository) throws Exception {
		int nbNew = 0;
		if (journals != null && !journals.isEmpty()) {
			getLogger().info("Inserting " + journals.size() + " journals..."); //$NON-NLS-1$ //$NON-NLS-2$
			int i = 0;
			for (Object journalObject : journals) {
				getLogger().info("> Journal " + (i + 1) + "/" + journals.size()); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					final String id = getId(journalObject);
					Journal journal = createObject(Journal.class, get(journalObject, Map.class), aliasRepository, null);
					if (journal != null) {
						final Optional<Journal> existing = this.journalRepository.findByJournalName(journal.getJournalName());
						if (existing.isEmpty()) {
							if (!isFake()) {
								journal = this.journalRepository.save(journal);
							}
							// Create the quality indicators
							final Map<String, Object> history = get(journalObject, QUALITYINDICATORSHISTORY_KEY, Map.class);
							if (history != null && !history.isEmpty()) {
								for (final Entry<String, Object> historyEntry : history.entrySet()) {
									final int year = Integer.parseInt(historyEntry.getKey());
									String str = get(historyEntry.getValue(), SCIMAGOQINDEX_KEY, String.class);
									JournalQualityAnnualIndicators indicators = null; 
									if (!Strings.isNullOrEmpty(str) ) {
										final QuartileRanking scimago = QuartileRanking.valueOfCaseInsensitive(str);
										if (scimago != null) {
											indicators = journal.setScimagoQIndexByYear(year, scimago);
										}
									}
									str = get(historyEntry.getValue(), WOSQINDEX_KEY, String.class);
									if (!Strings.isNullOrEmpty(str)) {
										final QuartileRanking wos = QuartileRanking.valueOfCaseInsensitive(str);
										if (wos != null) {
											final JournalQualityAnnualIndicators oindicators = indicators;
											indicators = journal.setWosQIndexByYear(year, wos);
											assert oindicators == null || oindicators == indicators;
										}
									}
									Number flt = get(historyEntry.getValue(), IMPACTFACTOR_KEY, Number.class);
									if (flt != null) {
										final float impactFactor = flt.floatValue();
										if (impactFactor > 0) {
											final JournalQualityAnnualIndicators oindicators = indicators;
											indicators = journal.setImpactFactorByYear(year, impactFactor);
											assert oindicators == null || oindicators == indicators;
										}
									}
									if (indicators != null && !isFake()) {
										this.journalIndicatorsRepository.save(indicators);
									}
								}
							}
							// Save again the journal for saving the links to the quality indicators
							if (!isFake()) {
								journal = this.journalRepository.save(journal);
							}
							++nbNew;
							//
							getLogger().info("  + " + journal.getJournalName() + " (id: " + journal.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							if (!Strings.isNullOrEmpty(id)) {
								objectRepository.put(id, journal);
							}
						} else {
							getLogger().info("  X " + existing.get().getJournalName() + " (id: " + existing.get().getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							if (!Strings.isNullOrEmpty(id)) {
								objectRepository.put(id, existing.get());
							}
						}
					}
				} catch (Throwable ex) {
					throw new UnableToImportJsonException(JOURNALS_SECTION, i, journalObject, ex);
				}
				++i;
			}
		}
		return nbNew;
	}

	private static int parseMonthField(String value) {
		if (!Strings.isNullOrEmpty(value)) {
			switch (value.toLowerCase()) {
			case "jan": //$NON-NLS-1$
				return 1;
			case "feb": //$NON-NLS-1$
				return 2;
			case "mar": //$NON-NLS-1$
				return 3;
			case "apr": //$NON-NLS-1$
				return 4;
			case "may": //$NON-NLS-1$
				return 5;
			case "jun": //$NON-NLS-1$
				return 6;
			case "jul": //$NON-NLS-1$
				return 7;
			case "aug": //$NON-NLS-1$
				return 8;
			case "sep": //$NON-NLS-1$
				return 9;
			case "oct": //$NON-NLS-1$
				return 10;
			case "nov": //$NON-NLS-1$
				return 11;
			case "dec": //$NON-NLS-1$
				return 12;
			default:
				//
			}
		}
		return 0;
	}

	/** Create the publications (and additional authors) in the database.
	 *
	 * @param publications the list of publications in the Json source.
	 * @param objectRepository the repository of the JSON elements with {@code "@id"} field.
	 * @param aliasRepository the repository of field aliases.
	 * @return the pair of numbers, never {@code null}. The first number is the number of added publication; the
	 *     second number is the is the number of added persons.
	 * @throws Exception if a membership cannot be created.
	 */
	protected Pair<Integer, Integer> insertPublications(List<?> publications, Map<String, Object> objectRepository,
			Map<String, Set<String>> aliasRepository) throws Exception {
		int nbNewPublications = 0;
		final MutableInt nbNewPersons = new MutableInt();
		if (publications != null && !publications.isEmpty()) {
			getLogger().info("Retreiving the existing publications..."); //$NON-NLS-1$
			final List<Publication> allPublications = this.publicationRepository.findAll();
			getLogger().info("Inserting " + publications.size() + " publications..."); //$NON-NLS-1$ //$NON-NLS-2$
			int i = 0;
			for (Object publicationObject : publications) {
				getLogger().info("> Publication " + (i + 1) + "/" + publications.size()); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					final String id = getId(publicationObject);
					final Pair<Publication, Journal> pair = createPublicationInstance(id,
							publicationObject, objectRepository, aliasRepository);
					// Test if the publication is already inside the database
					Publication publication = pair.getLeft();
					final Publication readOnlyPublication = publication;
					final Optional<Publication> existing = allPublications.stream().filter(
							it -> this.publicationComparator.isSimilar(it, readOnlyPublication)).findAny();
					if (existing.isEmpty()) {
						// Save the publication
						if (!isFake()) {
							publication = this.publicationRepository.save(publication);
							if (pair.getRight() != null) {
								this.journalRepository.save(pair.getRight());
							}
						}
						++nbNewPublications;
						if (!Strings.isNullOrEmpty(id)) {
							objectRepository.put(id, publication);
						}
						//
						getLogger().info("  + " + publication.getTitle() + " (id: " + publication.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

						// Attach authors
						final Iterable<?> authors = get(publicationObject, AUTHORS_KEY, Iterable.class);
						int authorRank = 0;
						for (final Object authorObject : authors) {
							final Person targetAuthor = findOrCreateAuthor(authorObject, objectRepository, nbNewPersons);
							if (targetAuthor == null) {
								throw new IllegalArgumentException("Invalid author reference for publication with id: " + id); //$NON-NLS-1$
							}
							//
							Authorship authorship = new Authorship();
							authorship.setPerson(targetAuthor);
							authorship.setPublication(publication);
							authorship.setAuthorRank(authorRank);
							if (!isFake()) {
								authorship = this.authorshipRepository.save(authorship);
							}
							++authorRank;
						}
					} else {
						// Publication is already in the database
						getLogger().info("  X " + existing.get().getTitle() + " (id: " + existing.get().getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						if (!Strings.isNullOrEmpty(id)) {
							objectRepository.put(id, existing.get());
						}
					}
				} catch (Throwable ex) {
					throw new UnableToImportJsonException(PUBLICATIONS_SECTION, i, publicationObject, ex);
				}
				++i;
			}
		}
		return Pair.of(Integer.valueOf(nbNewPublications), nbNewPersons.toInteger());
	}

	@SuppressWarnings("unchecked")
	private Pair<Publication, Journal> createPublicationInstance(String id, Object publicationObject, Map<String, Object> objectRepository,
			Map<String, Set<String>> aliasRepository) throws Exception {
		// Retrieve the elements that characterize the type of the publication
		final PublicationType type = getEnum(publicationObject, TYPE_KEY, PublicationType.class);
		if (type == null) {
			throw new IllegalArgumentException("Missing publication type"); //$NON-NLS-1$
		}
		final Class<? extends Publication> publicationClass = type.getInstanceType();
		assert publicationClass != null;

		// Create the publication
		Publication publication = createObject(publicationClass, get(publicationObject, Map.class),
				aliasRepository, it -> {
					final String k = it.getKey();
					// Keys "authors" and "journal" are not directly set. They have a specific
					// code for associating authors and journals to the publication
					return !AUTHORS_KEY.equalsIgnoreCase(k) && !JOURNAL_KEY.equalsIgnoreCase(k)
							&& !MONTH_KEY.equalsIgnoreCase(k);
				});
		if (publication == null) {
			throw new IllegalArgumentException("Unable to create the instance of the publication of type: " + publicationClass); //$NON-NLS-1$
		}

		// Attach month if it is provided
		final int month = parseMonthField(get(publicationObject, MONTH_KEY, String.class));
		if (month > 0 && month <= 12) {
			final int year = publication.getPublicationYear();
			if (year != 0) {
				final LocalDate localDate = LocalDate.of(year, month, 1);
				final Date dt = Date.valueOf(localDate);
				publication.setPublicationDate(dt);
			}
		}

		// Attach journal if needed for the type of publication
		final Journal targetJournal;
		if (publication instanceof JournalBasedPublication) {
			final String journalId = getRef(get(publicationObject, JOURNAL_KEY, Object.class));
			if (Strings.isNullOrEmpty(journalId)) {
				throw new IllegalArgumentException("Invalid journal reference for publication with id: " + id); //$NON-NLS-1$
			}
			targetJournal = get(objectRepository, journalId, Journal.class);
			if (targetJournal == null) {
				throw new IllegalArgumentException("Invalid journal reference for publication with id: " + id); //$NON-NLS-1$
			}
			final JournalBasedPublication journalPaper = (JournalBasedPublication) publication;
			journalPaper.setJournal(targetJournal);
		} else {
			targetJournal = null;
		}

		// Check the minimum set of fields
		if (Strings.isNullOrEmpty(publication.getTitle())) {
			throw new IllegalArgumentException("A publication must have a title"); //$NON-NLS-1$
		}
		if (publication.getPublicationYear() <= 1980) {
			throw new IllegalArgumentException("A publication must have a year of publishing greather to 1980 (value: " //$NON-NLS-1$
					+ publication.getPublicationYear() + ")"); //$NON-NLS-1$
		}
		if (publication.getType() == null) {
			throw new IllegalArgumentException("A publication must have a type"); //$NON-NLS-1$
		}

		return Pair.of(publication, targetJournal);
	}

	private Person findOrCreateAuthor(Object authorObject, Map<String, Object> objectRepository, MutableInt nbNewPersons) {
		assert authorObject != null;
		final String authorId = getRef(authorObject);
		Person targetAuthor = null;
		if (Strings.isNullOrEmpty(authorId)) {
			// The author is not a reference to a defined person
			final String authorName = authorObject.toString();
			final String firstName = this.personNameParser.parseFirstName(authorName);
			final String lastName = this.personNameParser.parseLastName(authorName);
			final Person optPerson = this.personService.getPersonBySimilarName(firstName, lastName);
			if (optPerson == null) {
				// This is a new person in the database
				Person newAuthor = new Person();
				newAuthor.setFirstName(this.personNameParser.formatNameForDisplay(firstName));
				newAuthor.setLastName(this.personNameParser.formatNameForDisplay(lastName));
				if (!isFake()) {
					newAuthor = this.personRepository.save(newAuthor);
				}
				nbNewPersons.increment();
				targetAuthor = newAuthor;
			} else {
				targetAuthor = optPerson;
			}
		} else {
			// The author is a referenced to a defined person
			targetAuthor = get(objectRepository, authorId, Person.class);
		}
		return targetAuthor;
	}

}