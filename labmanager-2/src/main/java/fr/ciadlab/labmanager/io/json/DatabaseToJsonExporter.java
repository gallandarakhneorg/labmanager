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

package fr.ciadlab.labmanager.io.json;

import java.lang.reflect.Method;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.repository.journal.JournalQualityAnnualIndicatorsRepository;
import fr.ciadlab.labmanager.repository.journal.JournalRepository;
import fr.ciadlab.labmanager.repository.member.MembershipRepository;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.organization.ResearchOrganizationRepository;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Exporter of JSON data from the database.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Component
public class DatabaseToJsonExporter extends JsonTool {

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
	public DatabaseToJsonExporter(
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

	/** Run the exporter.
	 *
	 * @return the JSON content or {@code null} if empty.
	 * @throws Exception if there is problem for exporting.
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> exportFromDatabase() throws Exception {
		final JsonObject obj = exportFromDatabaseToJsonObject();
		if (obj != null) {
			final Gson gson = new Gson();
			return gson.fromJson(obj, Map.class);
		}
		return null;
	}

	/** Run the exporter for creating JSON objects.
	 *
	 * @return the JSON content.
	 * @throws Exception if there is problem for exporting.
	 */
	public JsonObject exportFromDatabaseToJsonObject() throws Exception {
		final JsonObject root = new JsonObject();
		final Map<Object, String> repository = new HashMap<>();
		exportOrganizations(root, repository);
		exportPersons(root, repository);
		exportMemberships(root, repository);
		exportJournals(root, repository);
		if (root.size() > 0) {
			root.addProperty(LAST_CHANGE_FIELDNAME, LocalDate.now().toString());
			return root;
		}
		return null;
	}

	private static String toLowerFirst(String name) {
		if (name.length() <= 0) {
			return name.toLowerCase();
		}
		final Pattern pattern = Pattern.compile("^[A-Z]+$"); //$NON-NLS-1$
		final Matcher matcher = pattern.matcher(name);
		if (matcher.matches()) {
			return name.toLowerCase();
		}
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

	private static Map<String, Method> findMethods(Class<?> source) {
		final Set<String> setters = new TreeSet<>();
		final Map<String, Method> getters = new HashMap<>();
		for (final Method meth : source.getMethods()) {
			final String name = meth.getName().toLowerCase();
			if (meth.getParameterCount() == 1
					&& (Number.class.equals(meth.getParameterTypes()[0])
						|| String.class.equals(meth.getParameterTypes()[0])
						|| Boolean.class.equals(meth.getParameterTypes()[0]))
					&& name.startsWith(SETTER_FUNCTION_PREFIX)) {
				final String attrName = toLowerFirst(meth.getName().substring(SETTER_FUNCTION_PREFIX.length()));
				setters.add(attrName);
			} else 	if (meth.getParameterCount() == 0
					&& (meth.getReturnType().isPrimitive()
							|| meth.getReturnType().isEnum()
							|| Number.class.isAssignableFrom(meth.getReturnType())
							|| String.class.isAssignableFrom(meth.getReturnType())
							|| Boolean.class.isAssignableFrom(meth.getReturnType())
							|| Date.class.isAssignableFrom(meth.getReturnType())
							|| LocalDate.class.isAssignableFrom(meth.getReturnType())
							|| Character.class.isAssignableFrom(meth.getReturnType()))
						&& name.startsWith(GETTER_FUNCTION_PREFIX)) {
				final String attrName = toLowerFirst(meth.getName().substring(GETTER_FUNCTION_PREFIX.length()));
				getters.put(attrName, meth);
			}
		}
		//
		final Iterator<Entry<String, Method>> iterator = getters.entrySet().iterator();
		while (iterator.hasNext()) {
			final Entry<String, Method> entry = iterator.next();
			if (!setters.contains(entry.getKey())) {
				iterator.remove();
			}
		}
		//
		return getters;
	}

	private static Object convertValue(Object value) {
		if (value != null) {
			final Class<?> type = value.getClass();
			if (value instanceof CharSequence) {
				return ((CharSequence) value).toString();
			}
			if (type.isPrimitive() || value instanceof Number || value instanceof Boolean) {
				return value;
			}
			if (value instanceof Enum) {
				return ((Enum<?>) value).name().toLowerCase();
			}
			if (value instanceof Date) {
				return ((Date) value).toString();
			}
			if (value instanceof LocalDate) {
				return ((LocalDate) value).toString();
			}
			if (value instanceof Character) {
				return ((Character) value).toString();
			}
		}
		return null;
	}
	
	/** Add entity reference to the given receiver.
	 *
	 * @param receiver the receiver of JSON.
	 * @param key the key that should receive the reference.
	 * @param id the identifier.
	 */
	@SuppressWarnings("static-method")
	protected void ref(JsonObject receiver, String key, String id) {
		final JsonObject ref = new JsonObject();
		ref.addProperty(ID_FIELDNAME, id);
		receiver.add(key, ref);
	}

	/** Export the given object to the receiver.
	 *
	 * @param receiver the receiver of JSON.
	 * @param id the identifier.
	 * @param object the object to export.
	 * @throws Exception if there is problem for exporting.
	 */
	@SuppressWarnings("static-method")
	protected void exportObject(JsonObject receiver, String id, Object object) throws Exception {
		if (object != null) {
			if (!Strings.isNullOrEmpty(id)) {
				receiver.addProperty(ID_FIELDNAME, id);
			}
			final Map<String, Method> meths = findMethods(object.getClass());
			for (final Entry<String, Method> entry : meths.entrySet()) {
				final Object objValue = convertValue(entry.getValue().invoke(object));
				if (objValue instanceof String) {
					receiver.addProperty(entry.getKey(), (String) objValue);
				} else if (objValue instanceof Number) {
					receiver.addProperty(entry.getKey(), (Number) objValue);
				} else if (objValue instanceof Boolean) {
					receiver.addProperty(entry.getKey(), (Boolean) objValue);
				} else if (objValue instanceof Character) {
					receiver.addProperty(entry.getKey(), (Character) objValue);
				}
			}
		}
	}
	
	/** Export the research organizations to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportOrganizations(JsonObject root, Map<Object, String> repository) throws Exception {
		final List<ResearchOrganization> organizations = this.organizationRepository.findAll();
		if (!organizations.isEmpty()) {
			final JsonArray array = new JsonArray();
			int i = 0;
			for (final ResearchOrganization organization : organizations) {
				final JsonObject jsonOrganization = new JsonObject();

				final String id = RESEARCHORGANIZATION_ID_PREFIX + i;

				exportObject(jsonOrganization, id, organization);

				if (jsonOrganization.size() > 0) {
					repository.put(organization, id);
					array.add(jsonOrganization);
					++i;
				}
			}
			if (array.size() > 0) {
				root.add(RESEARCHORGANIZATIONS_SECTION, array);
			}
		}
	}


	/** Export the persons to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportPersons(JsonObject root, Map<Object, String> repository) throws Exception {
		final List<Person> persons = this.personRepository.findAll();
		if (!persons.isEmpty()) {
			final JsonArray array = new JsonArray();
			int i = 0;
			for (final Person person : persons) {
				final JsonObject jsonPerson = new JsonObject();

				final String id = PERSON_ID_PREFIX + i;

				exportObject(jsonPerson, id, person);

				if (jsonPerson.size() > 0) {
					repository.put(person, id);
					array.add(jsonPerson);
					++i;
				}
			}
			if (array.size() > 0) {
				root.add(PERSONS_SECTION, array);
			}
		}
	}

	/** Export the memberships to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportMemberships(JsonObject root, Map<Object, String> repository) throws Exception {
		final List<Membership> memberships = this.membershipRepository.findAll();
		if (!memberships.isEmpty()) {
			final JsonArray array = new JsonArray();
			for (final Membership membership : memberships) {
				final String personId = repository.get(membership.getPerson());
				final String organizationId = repository.get(membership.getResearchOrganization());
				if (!Strings.isNullOrEmpty(personId) && !Strings.isNullOrEmpty(organizationId)) {
					final JsonObject jsonMembership = new JsonObject();

					exportObject(jsonMembership, null, membership);
					
					ref(jsonMembership, PERSON_KEY, personId);
					ref(jsonMembership, RESEARCHORGANIZATION_KEY, organizationId);

					if (jsonMembership.size() > 0) {
						array.add(jsonMembership);
					}
				}
			}
			if (array.size() > 0) {
				root.add(MEMBERSHIPS_SECTION, array);
			}
		}
	}

	/** Export the journals to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportJournals(JsonObject root, Map<Object, String> repository) throws Exception {
		final List<Journal> journals = this.journalRepository.findAll();
		if (!journals.isEmpty()) {
			final JsonArray array = new JsonArray();
			int i = 0;
			for (final Journal journal : journals) {
				final JsonObject jsonJournal = new JsonObject();

				final String id = JOURNAL_ID_PREFIX + i;

				exportObject(jsonJournal, id, journal);
				
				if (jsonJournal.size() > 0) {
					repository.put(journal, id);
					array.add(jsonJournal);
					++i;
				}
			}
			if (array.size() > 0) {
				root.add(JOURNALS_SECTION, array);
			}
		}
	}

}
