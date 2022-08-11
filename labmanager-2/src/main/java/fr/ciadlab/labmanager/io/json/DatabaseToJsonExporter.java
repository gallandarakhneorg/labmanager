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

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.journal.JournalQualityAnnualIndicators;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.publication.JournalBasedPublication;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.repository.journal.JournalRepository;
import fr.ciadlab.labmanager.repository.member.MembershipRepository;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.organization.ResearchOrganizationRepository;
import fr.ciadlab.labmanager.repository.publication.PublicationRepository;
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

	private PublicationRepository publicationRepository;

	/** Constructor.
	 * 
	 * @param organizationRepository the accessor to the organization repository.
	 * @param personRepository the accessor to the person repository.
	 * @param membershipRepository the accessor to the membership repository.
	 * @param journalRepository the accessor to the journal repository.
	 * @param publicationRepository the accessor to the repository of the publications.
	 */
	public DatabaseToJsonExporter(
			@Autowired ResearchOrganizationRepository organizationRepository,
			@Autowired PersonRepository personRepository,
			@Autowired MembershipRepository membershipRepository,
			@Autowired JournalRepository journalRepository,
			@Autowired PublicationRepository publicationRepository) {
		this.organizationRepository = organizationRepository;
		this.personRepository = personRepository;
		this.membershipRepository = membershipRepository;
		this.journalRepository = journalRepository;
		this.publicationRepository = publicationRepository;
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
		exportPublications(root, repository);
		if (root.size() > 0) {
			root.addProperty(LAST_CHANGE_FIELDNAME, LocalDate.now().toString());
			return root;
		}
		return null;
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
			final Map<String, Method> meths = findGetterMethods(object.getClass());
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
			int i = 0;
			for (final Membership membership : memberships) {
				final String personId = repository.get(membership.getPerson());
				final String organizationId = repository.get(membership.getResearchOrganization());
				if (!Strings.isNullOrEmpty(personId) && !Strings.isNullOrEmpty(organizationId)) {
					final JsonObject jsonMembership = new JsonObject();

					final String id = RESEARCHORGANIZATION_ID_PREFIX + i;
					exportObject(jsonMembership, id, membership);

					// Person and organization must be added explicitly because the "exportObject" function
					// ignore the getter functions for both.
					addReference(jsonMembership, PERSON_KEY, personId);
					addReference(jsonMembership, RESEARCHORGANIZATION_KEY, organizationId);

					if (jsonMembership.size() > 0) {
						array.add(jsonMembership);
						++i;
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

				// Add the publication indicators by hand because they are not exported implicitly by
				// the "exportObject" function
				final JsonObject indicatorMap = new JsonObject();
				for (final JournalQualityAnnualIndicators indicators : journal.getQualityIndicators().values()) {
					final JsonObject jsonIndicator = new JsonObject();
					exportObject(jsonIndicator, null, indicators);
					// Remove the year because it is not necessary into the JSON map as value and the year is the key.
					jsonIndicator.remove(REFERENCEYEAR_KEY);
					if (jsonIndicator.size() > 0) {
						indicatorMap.add(Integer.toString(indicators.getReferenceYear()), jsonIndicator);
					}
				}
				if (indicatorMap.size() > 0) {
					jsonJournal.add(QUALITYINDICATORSHISTORY_KEY, indicatorMap);
				}

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

	/** Export the publications to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportPublications(JsonObject root, Map<Object, String> repository) throws Exception {
		final List<Publication> publications = this.publicationRepository.findAll();
		if (!publications.isEmpty()) {
			final JsonArray array = new JsonArray();
			int i = 0;
			for (final Publication publication : publications) {
				final JsonObject jsonPublication = new JsonObject();

				final String id = PUBLICATION_ID_PREFIX + i;
				exportObject(jsonPublication, id, publication);

				// Add the authors by hand because they are not exported implicitly by
				// the "exportObject" function.
				// It is due to the reference to person entities.
				final JsonArray authorArray = new JsonArray();
				for (final Person author : publication.getAuthors()) {
					final String authorId = repository.get(author);
					if (Strings.isNullOrEmpty(authorId)) {
						// Author not found in the repository. It is an unexpected behavior but
						// the full name of the person is output to JSON
						authorArray.add(author.getFullName());
					} else {
						authorArray.add(createReference(authorId));
					}
				}
				if (authorArray.size() > 0) {
					jsonPublication.add(AUTHORS_KEY, authorArray);
				}

				// Add the journal by hand because they are not exported implicitly by
				// the "exportObject" function
				// It is due to the reference to journal entities.
				if (publication instanceof JournalBasedPublication) {
					final JournalBasedPublication jbp = (JournalBasedPublication) publication;
					final Journal journal = jbp.getJournal();
					if (journal != null) {
						final String journalId = repository.get(journal);
						if (Strings.isNullOrEmpty(journalId)) {
							// Journal not found in the repository. It is an unexpected behavior but
							// the name of the journal is output to JSON
							jsonPublication.addProperty(JOURNAL_KEY, journal.getJournalName());
						} else {
							jsonPublication.add(JOURNAL_KEY, createReference(journalId));
						}
					}
				}

				if (jsonPublication.size() > 0) {
					repository.put(publication, id);
					array.add(jsonPublication);
					++i;
				}
			}
			if (array.size() > 0) {
				root.add(PUBLICATIONS_SECTION, array);
			}
		}
	}

}
