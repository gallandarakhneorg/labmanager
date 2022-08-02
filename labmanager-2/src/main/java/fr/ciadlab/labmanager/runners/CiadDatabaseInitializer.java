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
 * you entered into with the CIAD.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.runners;

import java.io.InputStream;
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
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.repository.member.MembershipRepository;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.organization.ResearchOrganizationRepository;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

/** This componet fill up the database with standard CIAD data.
 * It is searching for a file with the name {@code data-<platform>.json} at the
 * root folder of the class-path.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Component
public class CiadDatabaseInitializer implements ApplicationRunner {

	private static final String DATA_FILENAME = "/data.json"; //$NON-NLS-1$

	private static final String ID_FIELDNAME = "@id"; //$NON-NLS-1$

	private static final String PREFIX1 = "_"; //$NON-NLS-1$

	private static final String PREFIX2 = "@"; //$NON-NLS-1$

	private static final String PREFIX3 = "set"; //$NON-NLS-1$

	private static final String RESEARCHORGANIZATIONS_KEY = "researchOrganizations"; //$NON-NLS-1$

	private static final String SUPERORGANIZATION_KEY = "superOrganization"; //$NON-NLS-1$

	private static final String PERSONS_KEY = "persons"; //$NON-NLS-1$

	private static final String MEMBERSHIPS_KEY = "memberships"; //$NON-NLS-1$

	private static final String PERSON_KEY = "person"; //$NON-NLS-1$

	private static final String RESEARCHORGANIZATION_KEY = "researchOrganization"; //$NON-NLS-1$

	private ResearchOrganizationRepository organizationRepository;

	private PersonRepository personRepository;

	private MembershipRepository membershipRepository;

	/** Logger of the service. It is lazy loaded.
	 */
	private Logger logger;

	/** Constructor.
	 * 
	 * @param organizationRepository the accessor to the organization repository.
	 * @param personRepository the accessor to the person repository.
	 * @param membershipRepository the accessor to the membership repository.
	 */
	public CiadDatabaseInitializer(
			@Autowired ResearchOrganizationRepository organizationRepository,
			@Autowired PersonRepository personRepository,
			@Autowired MembershipRepository membershipRepository) {
		this.organizationRepository = organizationRepository;
		this.personRepository = personRepository;
		this.membershipRepository = membershipRepository;
	}

	/** Replies the logger of this service.
	 *
	 * @return the logger.
	 */
	public Logger getLogger() {
		if (this.logger == null) {
			this.logger = createLogger();
		}
		return this.logger;
	}

	/** Change the logger of this service.
	 *
	 * @param logger the logger.
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/** Factory method for creating the service logger.
	 *
	 * @return the logger.
	 */
	protected Logger createLogger() {
		return LoggerFactory.getLogger(getClass());
	}

	/** Replies the URL to the data script to use.
	 *
	 * @return the URL or {@code null} if none.
	 */
	@SuppressWarnings("static-method")
	protected URL getDataScriptURL() {
		try {
			final URL url = Resources.getResource(DATA_FILENAME);
			if (url != null) {
				try {
					@SuppressWarnings("resource")
					final InputStream is = url.openStream();
					if (is != null) {
						is.close();
					}
					return url;
				} catch (Throwable ex) {
					//
				}
			}
		} catch (Throwable ex) {
			//
		}
		return null;
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
			if (!Strings.isNullOrEmpty(entry.getKey()) && !entry.getKey().startsWith(PREFIX1)
					&& !entry.getKey().startsWith(PREFIX2)) {
				final Method method = findMethod(type, PREFIX3 + entry.getKey(), entry.getValue());
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

	@SuppressWarnings("unchecked")
	@Override
	public void run(ApplicationArguments args) throws Exception {
		final URL url = getDataScriptURL();
		if (url != null) {
			getLogger().info("Database initialization with: " + url.toExternalForm()); //$NON-NLS-1$
			final Gson gson = new Gson();
			final Map<Object, Object> content; 
			try (final InputStreamReader isr = new InputStreamReader(url.openStream())) {
				content = gson.fromJson(isr, Map.class);
			}
			if (content != null && !content.isEmpty()) {
				final Map<String, Object> repository = new TreeMap<>();
				insertOrganizations(get(content, RESEARCHORGANIZATIONS_KEY, List.class), repository);
				insertPersons(get(content, PERSONS_KEY, List.class), repository);
				insertMemberships(get(content, MEMBERSHIPS_KEY, List.class), repository);
			}
		}
	}

	/** Create the research organizations in the database.
	 *
	 * @param organizations the list of organizations in the Json source.
	 * @param repository the repository of the JSON elements with {@code "@id"} field.
	 * @throws Exception if an organization cannot be created.
	 */
	@SuppressWarnings("unchecked")
	protected void insertOrganizations(List<?> organizations, Map<String, Object> repository) throws Exception {
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
	}

	/** Create the persons in the database.
	 *
	 * @param persons the list of persons in the Json source.
	 * @param repository the repository of the JSON elements with {@code "@id"} field.
	 * @throws Exception if a person cannot be created.
	 */
	@SuppressWarnings("unchecked")
	protected void insertPersons(List<?> persons, Map<String, Object> repository) throws Exception {
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
	}

	/** Create the memberships in the database.
	 *
	 * @param memberships the list of memberships in the Json source.
	 * @param repository the repository of the JSON elements with {@code "@id"} field.
	 * @throws Exception if a membership cannot be created.
	 */
	@SuppressWarnings("unchecked")
	protected void insertMemberships(List<?> memberships, Map<String, Object> repository) throws Exception {
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
	}

}
