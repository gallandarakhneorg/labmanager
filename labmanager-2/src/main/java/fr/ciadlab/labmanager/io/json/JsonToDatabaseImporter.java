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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

import com.google.gson.Gson;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.journal.JournalQualityAnnualIndicators;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.repository.journal.JournalQualityAnnualIndicatorsRepository;
import fr.ciadlab.labmanager.repository.journal.JournalRepository;
import fr.ciadlab.labmanager.repository.member.MembershipRepository;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.organization.ResearchOrganizationRepository;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
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

	private MembershipRepository membershipRepository;

	private JournalRepository journalRepository;

	private JournalQualityAnnualIndicatorsRepository journalIndicatorsRepository;

	/** Constructor.
	 * 
	 * @param organizationRepository the accessor to the organization repository.
	 * @param personRepository the accessor to the person repository.
	 * @param membershipRepository the accessor to the membership repository.
	 * @param journalRepository the accessor to the journal repository.
	 * @param journalIndicatorsRepository the accessor to the repository of the journal quality annual indicators.
	 */
	public JsonToDatabaseImporter(
			@Autowired ResearchOrganizationRepository organizationRepository,
			@Autowired PersonRepository personRepository,
			@Autowired MembershipRepository membershipRepository,
			@Autowired JournalRepository journalRepository,
			@Autowired JournalQualityAnnualIndicatorsRepository journalIndicatorsRepository) {
		this.organizationRepository = organizationRepository;
		this.personRepository = personRepository;
		this.membershipRepository = membershipRepository;
		this.journalRepository = journalRepository;
		this.journalIndicatorsRepository = journalIndicatorsRepository;
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

	private static Method findMethod(Class<?> type, String name, Object value) {
		for (final Method meth : type.getMethods()) {
			try {
				if (meth.getParameterCount() == 1 && name.equalsIgnoreCase(meth.getName())
						&& meth.getParameterTypes()[0].isInstance(value)) {
					return meth;
				}
			} catch (Throwable ex) {
				//
			}
		}
		return null;
	}

	/** Create the instance of an object that is initialized from the given JSON source.
	 *
	 * @param <T> the expected type of object.
	 * @param type the expected type of object.
	 * @param source the JSON source.
	 * @return the object or {@code null}.
	 * @throws Exception if the object cannot be created.
	 */
	protected <T> T createObject(Class<T> type, Map<String, Object> source) throws Exception {
		final T obj = type.getConstructor().newInstance();
		for (final Entry<String, Object> entry : source.entrySet()) {
			if (!Strings.isNullOrEmpty(entry.getKey()) && !entry.getKey().startsWith(HIDDEN_FIELD_PREFIX)
					&& !entry.getKey().startsWith(SPECIAL_FIELD_PREFIX)) {
				final Method method = findMethod(type, SETTER_FUNCTION_PREFIX + entry.getKey(), entry.getValue());
				if (method != null) {
					try {
						method.invoke(obj, entry.getValue());
					} catch (Throwable ex) {
						getLogger().error(ex.getLocalizedMessage(), ex);
					}
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
			final Map<String, Object> repository = new TreeMap<>();
			final int nb0 = insertOrganizations(get(content, RESEARCHORGANIZATIONS_SECTION, List.class), repository);
			final int nb1 = insertPersons(get(content, PERSONS_SECTION, List.class), repository);
			final int nb2 = insertMemberships(get(content, MEMBERSHIPS_SECTION, List.class), repository);
			final int nb3 = insertJournals(get(content, JOURNALS_SECTION, List.class), repository);
			getLogger().info("Summary of inserts: " //$NON-NLS-1$
					+ nb0 + " organizations; " //$NON-NLS-1$
					+ nb1 + " persons; " //$NON-NLS-1$
					+ nb2 + " memberships; " //$NON-NLS-1$
					+ nb3 + " journals."); //$NON-NLS-1$
		}
	}

	/** Create the research organizations in the database.
	 *
	 * @param organizations the list of organizations in the Json source.
	 * @param repository the repository of the JSON elements with {@code "@id"} field.
	 * @return the number of new organizations in the database.
	 * @throws Exception if an organization cannot be created.
	 */
	@SuppressWarnings("unchecked")
	protected int insertOrganizations(List<?> organizations, Map<String, Object> repository) throws Exception {
		int nbNew = 0;
		if (organizations != null && !organizations.isEmpty()) {
			getLogger().info("Inserting " + organizations.size() + " organizations..."); //$NON-NLS-1$ //$NON-NLS-2$
			int i = 0;
			final List<Pair<ResearchOrganization, String>> superOrgas = new ArrayList<>();
			for (Object orgaObject : organizations) {
				getLogger().info("> Organization " + (i + 1) + "/" + organizations.size()); //$NON-NLS-1$ //$NON-NLS-2$
				final String id = getId(orgaObject);
				ResearchOrganization orga = createObject(ResearchOrganization.class, get(orgaObject, Map.class));
				if (orga != null) {
					final Optional<ResearchOrganization> existing = this.organizationRepository.findDistinctByAcronymOrName(orga.getAcronym(), orga.getName());
					if (existing.isEmpty()) {
						orga = this.organizationRepository.saveAndFlush(orga);
						++nbNew;
						getLogger().info("  + " + orga.getAcronymOrName() + " (id: " + orga.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						if (!Strings.isNullOrEmpty(id)) {
							repository.put(id, orga);
						}
						final String superOrga = getRef(get(orgaObject, SUPERORGANIZATION_KEY, Object.class));
						if (!Strings.isNullOrEmpty(superOrga)) {
							superOrgas.add(Pair.of(orga, superOrga));
						}
					} else {
						getLogger().info("  X " + existing.get().getAcronymOrName() + " (id: " + existing.get().getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						if (!Strings.isNullOrEmpty(id)) {
							repository.put(id, existing.get());
						}
					}
				}
				++i;
			}
			for (final Pair<ResearchOrganization, String> entry : superOrgas) {
				final ResearchOrganization sup = get(repository, entry.getSecond(), ResearchOrganization.class);
				if (sup == null) {
					throw new IllegalArgumentException("Invalid reference to Json element with id: " + entry.getSecond()); //$NON-NLS-1$
				}
				entry.getFirst().setSuperOrganization(sup);
				sup.getSubOrganizations().add(entry.getFirst());
				this.organizationRepository.save(entry.getFirst());
				this.organizationRepository.save(sup);
			}
		}
		return nbNew;
	}

	/** Create the persons in the database.
	 *
	 * @param persons the list of persons in the Json source.
	 * @param repository the repository of the JSON elements with {@code "@id"} field.
	 * @return the number of new persons in the database.
	 * @throws Exception if a person cannot be created.
	 */
	@SuppressWarnings("unchecked")
	protected int insertPersons(List<?> persons, Map<String, Object> repository) throws Exception {
		int nbNew = 0;
		if (persons != null && !persons.isEmpty()) {
			getLogger().info("Inserting " + persons.size() + " persons..."); //$NON-NLS-1$ //$NON-NLS-2$
			int i = 0;
			for (Object personObject : persons) {
				getLogger().info("> Person " + (i + 1) + "/" + persons.size()); //$NON-NLS-1$ //$NON-NLS-2$
				final String id = getId(personObject);
				Person person = createObject(Person.class, get(personObject, Map.class));
				if (person != null) {
					final Optional<Person> existing = this.personRepository.findDistinctByFirstNameAndLastName(person.getFirstName(), person.getLastName());
					if (existing.isEmpty()) {
						person = this.personRepository.saveAndFlush(person);
						++nbNew;
						getLogger().info("  + " + person.getFullName() + " (id: " + person.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						if (!Strings.isNullOrEmpty(id)) {
							repository.put(id, person);
						}
					} else {
						getLogger().info("  X " + existing.get().getFullName() + " (id: " + existing.get().getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						if (!Strings.isNullOrEmpty(id)) {
							repository.put(id, existing.get());
						}
					}
				}
				++i;
			}
		}
		return nbNew;
	}

	/** Create the memberships in the database.
	 *
	 * @param memberships the list of memberships in the Json source.
	 * @param repository the repository of the JSON elements with {@code "@id"} field.
	 * @return the number of new memberships in the database.
	 * @throws Exception if a membership cannot be created.
	 */
	@SuppressWarnings("unchecked")
	protected int insertMemberships(List<?> memberships, Map<String, Object> repository) throws Exception {
		int nbNew = 0;
		if (memberships != null && !memberships.isEmpty()) {
			getLogger().info("Inserting " + memberships.size() + " memberships..."); //$NON-NLS-1$ //$NON-NLS-2$
			int i = 0;
			for (Object membershipObject : memberships) {
				getLogger().info("> Membership " + (i + 1) + "/" + memberships.size()); //$NON-NLS-1$ //$NON-NLS-2$
				final String id = getId(membershipObject);
				Membership membership = createObject(Membership.class, get(membershipObject, Map.class));
				if (membership != null) {
					final String personId = getRef(get(membershipObject, PERSON_KEY, Object.class));
					if (Strings.isNullOrEmpty(personId)) {
						throw new IllegalArgumentException("Invalid person reference for membership with id: " + id); //$NON-NLS-1$
					}
					final Person targetPerson = get(repository, personId, Person.class);
					if (targetPerson == null) {
						throw new IllegalArgumentException("Invalid person reference for membership with id: " + id); //$NON-NLS-1$
					}
					//
					final String orgaId = getRef(get(membershipObject, RESEARCHORGANIZATION_KEY, Object.class));
					if (Strings.isNullOrEmpty(orgaId)) {
						throw new IllegalArgumentException("Invalid organization reference for membership with id: " + id); //$NON-NLS-1$
					}
					final ResearchOrganization targetOrganization = get(repository, orgaId, ResearchOrganization.class);
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
						membership = this.membershipRepository.saveAndFlush(membership);
						++nbNew;
						getLogger().info("  + " + targetOrganization.getAcronymOrName() //$NON-NLS-1$
						+ " - " + targetPerson.getFullName() //$NON-NLS-1$
						+ " (id: " + membership.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
						if (!Strings.isNullOrEmpty(id)) {
							repository.put(id, membership);
						}
					} else {
						getLogger().info("  X " + targetOrganization.getAcronymOrName() //$NON-NLS-1$
						+ " - " + targetPerson.getFullName() //$NON-NLS-1$
						+ " (id: " + existing.get().getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
						if (!Strings.isNullOrEmpty(id)) {
							repository.put(id, existing.get());
						}
					}
				}
				++i;
			}
		}
		return nbNew;
	}


	/** Create the journals in the database.
	 *
	 * @param journals the list of journals in the Json source.
	 * @param repository the repository of the JSON elements with {@code "@id"} field.
	 * @return the number of new journals in the database.
	 * @throws Exception if a membership cannot be created.
	 */
	@SuppressWarnings("unchecked")
	protected int insertJournals(List<?> journals, Map<String, Object> repository) throws Exception {
		int nbNew = 0;
		if (journals != null && !journals.isEmpty()) {
			getLogger().info("Inserting " + journals.size() + " journal..."); //$NON-NLS-1$ //$NON-NLS-2$
			int i = 0;
			for (Object journalObject : journals) {
				getLogger().info("> Journal " + (i + 1) + "/" + journals.size()); //$NON-NLS-1$ //$NON-NLS-2$
				final String id = getId(journalObject);
				Journal journal = createObject(Journal.class, get(journalObject, Map.class));
				if (journal != null) {
					final Optional<Journal> existing = this.journalRepository.findByJournalName(journal.getJournalName());
					if (existing.isEmpty()) {
						journal = this.journalRepository.saveAndFlush(journal);
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
								if (indicators != null) {
									this.journalIndicatorsRepository.saveAndFlush(indicators);
								}
							}
						}
						// Save again the journal for saving the links to the quality indicators
						journal = this.journalRepository.saveAndFlush(journal);
						++nbNew;
						//
						getLogger().info("  + " + journal.getJournalName() + " (id: " + journal.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						if (!Strings.isNullOrEmpty(id)) {
							repository.put(id, journal);
						}
					} else {
						getLogger().info("  X " + existing.get().getJournalName() + " (id: " + existing.get().getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						if (!Strings.isNullOrEmpty(id)) {
							repository.put(id, existing.get());
						}
					}
				}
				++i;
			}
		}
		return nbNew;
	}

}
