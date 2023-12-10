/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.utbm.ciad.labmanager.utils.io.json;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructure;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructureHolder;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructureRepository;
import fr.utbm.ciad.labmanager.data.conference.Conference;
import fr.utbm.ciad.labmanager.data.conference.ConferenceQualityAnnualIndicators;
import fr.utbm.ciad.labmanager.data.conference.ConferenceRepository;
import fr.utbm.ciad.labmanager.data.indicator.GlobalIndicators;
import fr.utbm.ciad.labmanager.data.indicator.GlobalIndicatorsRepository;
import fr.utbm.ciad.labmanager.data.invitation.PersonInvitation;
import fr.utbm.ciad.labmanager.data.invitation.PersonInvitationRepository;
import fr.utbm.ciad.labmanager.data.journal.Journal;
import fr.utbm.ciad.labmanager.data.journal.JournalQualityAnnualIndicators;
import fr.utbm.ciad.labmanager.data.journal.JournalRepository;
import fr.utbm.ciad.labmanager.data.jury.JuryMembership;
import fr.utbm.ciad.labmanager.data.jury.JuryMembershipRepository;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.member.MembershipRepository;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.member.PersonRepository;
import fr.utbm.ciad.labmanager.data.organization.OrganizationAddress;
import fr.utbm.ciad.labmanager.data.organization.OrganizationAddressRepository;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganizationRepository;
import fr.utbm.ciad.labmanager.data.project.Project;
import fr.utbm.ciad.labmanager.data.project.ProjectBudget;
import fr.utbm.ciad.labmanager.data.project.ProjectMember;
import fr.utbm.ciad.labmanager.data.project.ProjectRepository;
import fr.utbm.ciad.labmanager.data.publication.ConferenceBasedPublication;
import fr.utbm.ciad.labmanager.data.publication.JournalBasedPublication;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.data.publication.PublicationRepository;
import fr.utbm.ciad.labmanager.data.scientificaxis.ScientificAxis;
import fr.utbm.ciad.labmanager.data.scientificaxis.ScientificAxisRepository;
import fr.utbm.ciad.labmanager.data.supervision.Supervision;
import fr.utbm.ciad.labmanager.data.supervision.SupervisionRepository;
import fr.utbm.ciad.labmanager.data.supervision.Supervisor;
import fr.utbm.ciad.labmanager.data.teaching.TeachingActivity;
import fr.utbm.ciad.labmanager.data.teaching.TeachingActivityRepository;
import fr.utbm.ciad.labmanager.data.teaching.TeachingActivityType;
import fr.utbm.ciad.labmanager.data.user.User;
import fr.utbm.ciad.labmanager.data.user.UserRepository;
import org.apache.commons.lang3.tuple.Pair;
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
 * @see DatabaseToZipExporter
 */
@Component
public class DatabaseToJsonExporter extends JsonTool {

	private OrganizationAddressRepository addressRepository;

	private ResearchOrganizationRepository organizationRepository;

	private PersonRepository personRepository;

	private MembershipRepository organizationMembershipRepository;

	private JournalRepository journalRepository;

	private ConferenceRepository conferenceRepository;

	private PublicationRepository publicationRepository;

	private JuryMembershipRepository juryMembershipRepository;

	private SupervisionRepository supervisionRepository;

	private PersonInvitationRepository invitationRepository;

	private GlobalIndicatorsRepository globalIndicatorsRepository;

	private ProjectRepository projectRepository;

	private AssociatedStructureRepository structureRepository;

	private TeachingActivityRepository teachingRepository;

	private ScientificAxisRepository scientificAxisRepository;

	private UserRepository userRepository;

	/** Constructor.
	 * 
	 * @param addressRepository the accessor to the organization address repository.
	 * @param organizationRepository the accessor to the organization repository.
	 * @param personRepository the accessor to the person repository.
	 * @param organizationMembershipRepository the accessor to the organization membership repository.
	 * @param journalRepository the accessor to the journal repository.
	 * @param conferenceRepository the accessor to the conference repository.
	 * @param publicationRepository the accessor to the repository of the publications.
	 * @param juryMembershipRepository the accessor to the jury membership repository.
	 * @param supervisionRepository the accessor to the supervision repository.
	 * @param invitationRepository the accessor to the invitation repository.
	 * @param globalIndicatorsRepository the accessor to the global indicators.
	 * @param projectRepository the accessor to the projects.
	 * @param structureRepository the accessor to the associated structures.
	 * @param teachingRepository the accessor to the teaching activities.
	 * @param scientificAxisRepository the accessor to the scientific axes.
	 * @param userRepository the accessor to the application users.
	 */
	public DatabaseToJsonExporter(
			@Autowired OrganizationAddressRepository addressRepository,
			@Autowired ResearchOrganizationRepository organizationRepository,
			@Autowired PersonRepository personRepository,
			@Autowired MembershipRepository organizationMembershipRepository,
			@Autowired JournalRepository journalRepository,
			@Autowired ConferenceRepository conferenceRepository,
			@Autowired PublicationRepository publicationRepository,
			@Autowired JuryMembershipRepository juryMembershipRepository,
			@Autowired SupervisionRepository supervisionRepository,
			@Autowired PersonInvitationRepository invitationRepository,
			@Autowired GlobalIndicatorsRepository globalIndicatorsRepository,
			@Autowired ProjectRepository projectRepository,
			@Autowired AssociatedStructureRepository structureRepository,
			@Autowired TeachingActivityRepository teachingRepository,
			@Autowired ScientificAxisRepository scientificAxisRepository,
			@Autowired UserRepository userRepository) {
		this.addressRepository = addressRepository;
		this.organizationRepository = organizationRepository;
		this.personRepository = personRepository;
		this.organizationMembershipRepository = organizationMembershipRepository;
		this.journalRepository = journalRepository;
		this.conferenceRepository = conferenceRepository;
		this.publicationRepository = publicationRepository;
		this.juryMembershipRepository = juryMembershipRepository;
		this.supervisionRepository = supervisionRepository;
		this.invitationRepository = invitationRepository;
		this.globalIndicatorsRepository = globalIndicatorsRepository;
		this.projectRepository = projectRepository;
		this.structureRepository = structureRepository;
		this.teachingRepository = teachingRepository;
		this.scientificAxisRepository = scientificAxisRepository;
		this.userRepository = userRepository;
	}

	/** Run the exporter.
	 *
	 * @return the JSON content or {@code null} if empty.
	 * @throws Exception if there is problem for exporting.
	 */
	public Map<String, Object> exportFromDatabase() throws Exception {
		return exportFromDatabase(null, null);
	}

	/** Run the exporter.
	 *
	 * @param similarPublicationProvider a provider of a publication that is similar to a given publication. 
	 *      If this argument is not {@code null} and if it replies a similar publication, the information in this
	 *      similar publication is used to complete the JSON file that is initially filled up with the source publication.
	 * @param extraPublicationProvider this provider gives publications that must be exported into the JSON that are
	 *      not directly extracted from the database. If this argument is {@code null}, no extra publication is exported. 
	 * @return the JSON content or {@code null} if empty.
	 * @throws Exception if there is problem for exporting.
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> exportFromDatabase(SimilarPublicationProvider similarPublicationProvider,
			ExtraPublicationProvider extraPublicationProvider) throws Exception {
		final ObjectMapper mapper = JsonUtils.createMapper();
		final JsonNode obj = exportFromDatabaseToJsonObject(mapper.getNodeFactory(), similarPublicationProvider, extraPublicationProvider);
		if (obj != null) {
			return mapper.treeToValue(obj, Map.class);
		}
		return null;
	}

	/** Run the exporter for creating JSON objects.
	 *
	 * @param factory the factory of nodes.
	 * @return the JSON content.
	 * @throws Exception if there is problem for exporting.
	 */
	public JsonNode exportFromDatabaseToJsonObject(JsonNodeCreator factory) throws Exception {
		return exportFromDatabaseToJsonObject(factory, null, null);
	}

	/** Run the exporter for creating JSON objects.
	 *
	 * @param factory the factory of nodes.
	 * @param similarPublicationProvider a provider of a publication that is similar to a given publication. 
	 *      If this argument is not {@code null} and if it replies a similar publication, the information in this
	 *      similar publication is used to complete the JSON file that is initially filled up with the source publication.
	 * @param extraPublicationProvider this provider gives publications that must be exported into the JSON that are
	 *      not directly extracted from the database. If this argument is {@code null}, no extra publication is exported. 
	 * @return the JSON content.
	 * @throws Exception if there is problem for exporting.
	 */
	public JsonNode exportFromDatabaseToJsonObject(JsonNodeCreator factory, SimilarPublicationProvider similarPublicationProvider,
			ExtraPublicationProvider extraPublicationProvider) throws Exception {
		final ObjectNode root = factory.objectNode();
		final Map<Object, String> repository = new HashMap<>();
		exportGlobalIndicators(root, repository);
		exportAddresses(root, repository);
		exportOrganizations(root, repository);
		exportPersons(root, repository);
		exportOrganizationMemberships(root, repository);
		exportJournals(root, repository);
		exportConferences(root, repository);
		exportPublications(root, repository, similarPublicationProvider, extraPublicationProvider);
		exportJuryMemberships(root, repository);
		exportSupervisions(root, repository);
		exportInvitations(root, repository);
		exportProjects(root, repository);
		exportAssociatedStructures(root, repository);
		exportTeachingActivities(root, repository);
		exportScientificAxes(root, repository);
		exportApplicationUsers(root, repository);
		if (root.size() > 0) {
			root.set(LAST_CHANGE_FIELDNAME, factory.textNode(LocalDate.now().toString()));
			return root;
		}
		return null;
	}

	/** Export the given object to the receiver.
	 *
	 * @param receiver the receiver of JSON.
	 * @param id the identifier.
	 * @param object the object to export.
	 * @param factory the factory of nodes.
	 * @param complements the objects that are of the same type of {@code object} but that could be used for obtaining additional data
	 *     that is missed from the original object.
	 * @throws Exception if there is problem for exporting.
	 */
	@SuppressWarnings("static-method")
	protected void exportObject(JsonNode receiver, String id, Object object, JsonNodeCreator factory, List<?> complements) throws Exception {
		if (object != null) {
			final ObjectNode rec = (ObjectNode) receiver;
			if (!Strings.isNullOrEmpty(id)) {
				rec.set(ID_FIELDNAME, factory.textNode(id));
			}
			final Map<String, Method> meths = findGetterMethods(object.getClass());
			for (final Entry<String, Method> entry : meths.entrySet()) {
				final Method method = entry.getValue();
				Object objValue = convertValue(method.invoke(object));
				if (objValue == null && complements != null && !complements.isEmpty()) {
					final Iterator<?> iterator = complements.iterator();
					while (iterator.hasNext() && objValue == null) {
						final Object complement = iterator.next();
						objValue = convertValue(method.invoke(complement));
					}
				}
				if (objValue instanceof String) {
					rec.set(entry.getKey(), factory.textNode((String) objValue));
				} else if (objValue instanceof Byte) {
					rec.set(entry.getKey(), factory.numberNode((Byte) objValue));
				} else if (objValue instanceof Short) {
					rec.set(entry.getKey(), factory.numberNode((Short) objValue));
				} else if (objValue instanceof Integer) {
					rec.set(entry.getKey(), factory.numberNode((Integer) objValue));
				} else if (objValue instanceof Long) {
					rec.set(entry.getKey(), factory.numberNode((Long) objValue));
				} else if (objValue instanceof Float) {
					rec.set(entry.getKey(), factory.numberNode((Float) objValue));
				} else if (objValue instanceof BigDecimal) {
					rec.set(entry.getKey(), factory.numberNode((BigDecimal) objValue));
				} else if (objValue instanceof BigInteger) {
					rec.set(entry.getKey(), factory.numberNode((BigInteger) objValue));
				} else if (objValue instanceof Number) {
					rec.set(entry.getKey(), factory.numberNode(((Number) objValue).doubleValue()));
				} else if (objValue instanceof Boolean) {
					rec.set(entry.getKey(), factory.booleanNode(((Boolean) objValue).booleanValue()));
				} else if (objValue instanceof Character) {
					rec.set(entry.getKey(), factory.textNode(((Character) objValue).toString()));
				}
			}
		}
	}

	/** Export the configuration for global indicatorsto the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportGlobalIndicators(ObjectNode root, Map<Object, String> repository) throws Exception {
		final List<GlobalIndicators> indicators = this.globalIndicatorsRepository.findAll();
		if (!indicators.isEmpty()) {
			final ObjectNode node = root.objectNode();
			final GlobalIndicators ind = indicators.get(0);
			final ArrayNode array = node.arrayNode();
			for (final String key : ind.getVisibleIndicatorKeyList()) {
				array.add(key);
			}
			if (array.size() > 0) {
				node.set(VISIBLEGLOBALINDICATORS_KEY, array);
			}
			if (node.size() > 0) {
				root.set(GLOBALINDICATORS_SECTION, node);
			}
		}
	}

	/** Export the organization addresses to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportAddresses(ObjectNode root, Map<Object, String> repository) throws Exception {
		final List<OrganizationAddress> addresses = this.addressRepository.findAll();
		if (!addresses.isEmpty()) {
			final ArrayNode array = root.arrayNode();
			int i = 0;
			final Map<Integer, ObjectNode> nodes = new TreeMap<>();
			for (final OrganizationAddress address : addresses) {
				final ObjectNode jsonAddress = array.objectNode();

				final String id = ORGANIZATIONADDRESS_ID_PREFIX + i;
				exportObject(jsonAddress, id, address, jsonAddress, null);

				if (jsonAddress.size() > 0) {
					repository.put(address, id);
					nodes.put(Integer.valueOf(address.getId()), jsonAddress);
					array.add(jsonAddress);
					++i;
				}
			}
			if (array.size() > 0) {
				root.set(ORGANIZATIONADDRESSES_SECTION, array);
			}
		}
	}

	/** Export the research organizations to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportOrganizations(ObjectNode root, Map<Object, String> repository) throws Exception {
		final List<ResearchOrganization> organizations = this.organizationRepository.findAll();
		if (!organizations.isEmpty()) {
			final ArrayNode array = root.arrayNode();
			int i = 0;
			final Map<Integer, ObjectNode> nodes = new TreeMap<>();
			for (final ResearchOrganization organization : organizations) {
				final ObjectNode jsonOrganization = array.objectNode();

				final String id = RESEARCHORGANIZATION_ID_PREFIX + i;
				exportObject(jsonOrganization, id, organization, jsonOrganization, null);

				if (jsonOrganization.size() > 0) {
					repository.put(organization, id);
					nodes.put(Integer.valueOf(organization.getId()), jsonOrganization);
					array.add(jsonOrganization);
					++i;
				}
			}
			// Export the addresses of the organization
			for (final ResearchOrganization organization : organizations) {
				if (!organization.getAddresses().isEmpty()) {
					final ArrayNode jsonAddresses = array.arrayNode();
					for (final OrganizationAddress adr : organization.getAddresses()) {
						final String id = repository.get(adr);
						if (Strings.isNullOrEmpty(id)) {
							throw new IllegalStateException("Address not found: " + adr.getName()); //$NON-NLS-1$
						}
						final JsonNode ref = createReference(id, jsonAddresses);
						if (ref != null) {
							jsonAddresses.add(ref);
						}
					}
					if (!jsonAddresses.isEmpty()) {
						final ObjectNode objNode = nodes.get(Integer.valueOf(organization.getId()));
						if (objNode == null) {
							throw new IllegalStateException("No JSON node created for organization: " + organization.getAcronymOrName()); //$NON-NLS-1$
						}
						objNode.set(ADDRESSES_KEY, jsonAddresses);
					}
				}
			}
			// Export the super organization for enabling the building of the organization hierarchy
			// This loop is externalized to be sure all the organizations are in the repository.
			for (final ResearchOrganization organization : organizations) {
				if (organization.getSuperOrganization() != null) {
					final String id = repository.get(organization.getSuperOrganization());
					if (Strings.isNullOrEmpty(id)) {
						throw new IllegalStateException("Organization not found: " + organization.getAcronymOrName()); //$NON-NLS-1$
					}
					final ObjectNode objNode = nodes.get(Integer.valueOf(organization.getId()));
					if (objNode == null) {
						throw new IllegalStateException("No JSON node created for organization: " + organization.getAcronymOrName()); //$NON-NLS-1$
					}
					addReference(objNode, SUPER_ORGANIZATION_KEY, id);
				}
			}
			if (array.size() > 0) {
				root.set(RESEARCHORGANIZATIONS_SECTION, array);
			}
		}
	}

	/** Export the persons to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportPersons(ObjectNode root, Map<Object, String> repository) throws Exception {
		final List<Person> persons = this.personRepository.findAll();
		if (!persons.isEmpty()) {
			final ArrayNode array = root.arrayNode();
			int i = 0;
			for (final Person person : persons) {
				final ObjectNode jsonPerson = array.objectNode();

				final String id = PERSON_ID_PREFIX + i;
				exportObject(jsonPerson, id, person, jsonPerson, null);

				if (jsonPerson.size() > 0) {
					repository.put(person, id);
					array.add(jsonPerson);
					++i;
				}
			}
			if (array.size() > 0) {
				root.set(PERSONS_SECTION, array);
			}
		}
	}

	/** Export the organization memberships to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportOrganizationMemberships(ObjectNode root, Map<Object, String> repository) throws Exception {
		final List<Membership> memberships = this.organizationMembershipRepository.findAll();
		if (!memberships.isEmpty()) {
			final ArrayNode array = root.arrayNode();
			int i = 0;
			for (final Membership membership : memberships) {
				final String adrId;
				if (membership.getOrganizationAddress() != null) {
					adrId = repository.get(membership.getOrganizationAddress());
				} else {
					adrId = null;
				}
				final String personId = repository.get(membership.getPerson());
				final String organizationId = repository.get(membership.getResearchOrganization());
				if (!Strings.isNullOrEmpty(personId) && !Strings.isNullOrEmpty(organizationId)) {
					final ObjectNode jsonMembership = array.objectNode();

					final String id = MEMBERSHIP_ID_PREFIX + i;
					exportObject(jsonMembership, id, membership, jsonMembership, null);

					// Address, person and organization must be added explicitly because the "exportObject" function
					// ignore the getter functions for all.
					if (!Strings.isNullOrEmpty(adrId)) {
						addReference(jsonMembership, ADDRESS_KEY, adrId);
					}
					addReference(jsonMembership, PERSON_KEY, personId);
					addReference(jsonMembership, RESEARCHORGANIZATION_KEY, organizationId);

					if (jsonMembership.size() > 0) {
						repository.put(membership, id);
						array.add(jsonMembership);
						++i;
					}
				}
			}
			if (array.size() > 0) {
				root.set(ORGANIZATION_MEMBERSHIPS_SECTION, array);
			}
		}
	}

	/** Export the journals to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportJournals(ObjectNode root, Map<Object, String> repository) throws Exception {
		final List<Journal> journals = this.journalRepository.findAll();
		if (!journals.isEmpty()) {
			final ArrayNode array = root.arrayNode();
			int i = 0;
			for (final Journal journal : journals) {
				final ObjectNode jsonJournal = array.objectNode();

				final String id = JOURNAL_ID_PREFIX + i;
				exportObject(jsonJournal, id, journal, jsonJournal, null);

				// Add the publication indicators by hand because they are not exported implicitly by
				// the "exportObject" function
				final ObjectNode indicatorMap = jsonJournal.objectNode();
				for (final JournalQualityAnnualIndicators indicators : journal.getQualityIndicators().values()) {
					final ObjectNode jsonIndicator = indicatorMap.objectNode();
					exportObject(jsonIndicator, null, indicators, jsonIndicator, null);
					// Remove the year because it is not necessary into the JSON map as value and the year is the key.
					jsonIndicator.remove(REFERENCEYEAR_KEY);
					if (jsonIndicator.size() > 0) {
						indicatorMap.set(Integer.toString(indicators.getReferenceYear()), jsonIndicator);
					}
				}
				if (indicatorMap.size() > 0) {
					jsonJournal.set(QUALITYINDICATORSHISTORY_KEY, indicatorMap);
				}

				if (jsonJournal.size() > 0) {
					repository.put(journal, id);
					array.add(jsonJournal);
					++i;
				}
			}
			if (array.size() > 0) {
				root.set(JOURNALS_SECTION, array);
			}
		}
	}

	/** Export the conferences to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportConferences(ObjectNode root, Map<Object, String> repository) throws Exception {
		final List<Conference> conferences = this.conferenceRepository.findAll();
		if (!conferences.isEmpty()) {
			final ArrayNode array = root.arrayNode();
			int i = 0;
			final List<Pair<ObjectNode, Conference>> enclosingConferences = new ArrayList<>();
			for (final Conference conference : conferences) {
				final ObjectNode jsonConference = array.objectNode();

				final String id = CONFERENCE_ID_PREFIX + i;
				exportObject(jsonConference, id, conference, jsonConference, null);

				// Add the quality indicators by hand because they are not exported implicitly by
				// the "exportObject" function
				final ObjectNode indicatorMap = jsonConference.objectNode();
				for (final ConferenceQualityAnnualIndicators indicators : conference.getQualityIndicators().values()) {
					final ObjectNode jsonIndicator = indicatorMap.objectNode();
					exportObject(jsonIndicator, null, indicators, jsonIndicator, null);
					// Remove the year because it is not necessary into the JSON map as value and the year is the key.
					jsonIndicator.remove(REFERENCEYEAR_KEY);
					if (jsonIndicator.size() > 0) {
						indicatorMap.set(Integer.toString(indicators.getReferenceYear()), jsonIndicator);
					}
				}
				if (indicatorMap.size() > 0) {
					jsonConference.set(QUALITYINDICATORSHISTORY_KEY, indicatorMap);
				}

				// Differ the creation of the reference to the enclosing conference to
				// be sure that all the conference nodes are created before creating the links
				if (conference.getEnclosingConference() != null) {
					enclosingConferences.add(Pair.of(jsonConference, conference.getEnclosingConference()));
				}

				if (jsonConference.size() > 0) {
					repository.put(conference, id);
					array.add(jsonConference);
					++i;
				}
			}

			// Export the enclosing conferences for enabling the building of the conference hierarchy.
			for (final Pair<ObjectNode, Conference> pair : enclosingConferences) {
				final ObjectNode node = pair.getLeft();
				final Conference enclosingConference = pair.getRight();
				final String id = repository.get(enclosingConference);
				if (Strings.isNullOrEmpty(id)) {
					throw new IllegalStateException("Conference not found: " + enclosingConference.getAcronymOrName()); //$NON-NLS-1$
				}
				addReference(node, ENCLOSING_CONFERENCE_KEY, id);
			}

			if (array.size() > 0) {
				root.set(CONFERENCES_SECTION, array);
			}
		}
	}

	/** Export the publications to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @param similarPublicationProvider a provider of a publication that is similar to a given publication. 
	 *      If this argument is not {@code null} and if it replies a similar publication, the information in this
	 *      similar publication is used to complete the JSON file that is initially filled up with the source publication.
	 * @param extraPublicationProvider this provider gives publications that must be exported into the JSON that are
	 *      not directly extracted from the database. If this argument is {@code null}, no extra publication is exported. 
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportPublications(ObjectNode root, Map<Object, String> repository, SimilarPublicationProvider similarPublicationProvider,
			ExtraPublicationProvider extraPublicationProvider) throws Exception {
		final List<Publication> publications = this.publicationRepository.findAll();
		final ArrayNode array = root.arrayNode();
		int i = 0;
		if (!publications.isEmpty()) {
			for (final Publication publication : publications) {
				final ObjectNode jsonPublication = array.objectNode();
				final String id = exportPublication(i, publication, root, jsonPublication, repository, similarPublicationProvider);
				if (jsonPublication.size() > 0) {
					jsonPublication.set(HIDDEN_INTERNAL_DATA_SOURCE_KEY, jsonPublication.textNode(HIDDEN_INTERNAL_DATABASE_SOURCE_VALUE));
					repository.put(publication, id);
					array.add(jsonPublication);
					++i;
				}
			}
		}
		getLogger().info("Exporting " + array.size() + " publications from the database."); //$NON-NLS-1$ //$NON-NLS-2$
		if (extraPublicationProvider != null) {
			for (final Publication publication : extraPublicationProvider.getPublications()) {
				final ObjectNode jsonPublication = array.objectNode();
				final String id = exportPublication(i, publication, root, jsonPublication, repository, null);
				if (jsonPublication.size() > 0) {
					jsonPublication.set(HIDDEN_INTERNAL_DATA_SOURCE_KEY, jsonPublication.textNode(HIDDEN_INTERNAL_EXTERNAL_SOURCE_VALUE));
					repository.put(publication, id);
					array.add(jsonPublication);
					++i;
				}
			}
			getLogger().info("Exporting " + extraPublicationProvider.getPublications().size() + " extra publications from the BibTeX."); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (array.size() > 0) {
			root.set(PUBLICATIONS_SECTION, array);
		}
	}

	/** Export the publications to the given JSON root element.
	 *
	 * @param index the index of the publication in the list of publications.
	 * @param publication the publication to export.
	 * @param root the JSON root.
	 * @param jsonPublication the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @param similarPublicationProvider a provider of a publication that is similar to a given publication. 
	 *      If this argument is not {@code null} and if it replies a similar publication, the information in this
	 *      similar publication is used to complete the JSON file that is initially filled up with the source publication.
	 * @return the identifier of the exported publication.
	 * @throws Exception if there is problem for exporting.
	 */
	protected String exportPublication(int index, Publication publication, ObjectNode root, ObjectNode jsonPublication, 
			Map<Object, String> repository, SimilarPublicationProvider similarPublicationProvider) throws Exception {
		// Add missed information from any similar publication
		final List<Publication> similarPublications;
		if (similarPublicationProvider != null) {
			similarPublications = similarPublicationProvider.get(publication);
			if (similarPublications != null && !similarPublications.isEmpty()) {
				getLogger().info("Found similar publication(s) for: " + publication.getTitle()); //$NON-NLS-1$
			}
		} else {
			similarPublications = Collections.emptyList();
		}

		final String id = PUBLICATION_ID_PREFIX + index;
		exportObject(jsonPublication, id, publication, jsonPublication, similarPublications);

		// Add the database identifier for information
		if (publication.getId() > 0) {
			jsonPublication.set(DATABASE_ID_FIELDNAME, jsonPublication.numberNode(publication.getId()));
		}

		// Add the authors by hand because they are not exported implicitly by
		// the "exportObject" function.
		// It is due to the reference to person entities.
		final ArrayNode authorArray = jsonPublication.arrayNode();
		for (final Person author : publication.getAuthors()) {
			String authorId = repository.get(author);
			if (Strings.isNullOrEmpty(authorId)) {
				// Author not found in the repository. It is a behavior that may
				// occur when the authors are provided by a BibTeX source and the
				// person is not yet known.
				authorId = createPerson(root, repository, author);
				if (Strings.isNullOrEmpty(authorId)) {
					// Unexpected behavior. But add the name as text to have it inside the output.
					authorArray.add(author.getFullName());
				} else {
					authorArray.add(createReference(authorId, authorArray));
				}
			} else {
				authorArray.add(createReference(authorId, authorArray));
			}
		}
		if (authorArray.size() > 0) {
			jsonPublication.set(AUTHORS_KEY, authorArray);
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
					jsonPublication.set(JOURNAL_KEY, jsonPublication.textNode(journal.getJournalName()));
				} else {
					jsonPublication.set(JOURNAL_KEY, createReference(journalId, jsonPublication));
				}
			}
		}

		// Add the conference by hand because they are not exported implicitly by
		// the "exportObject" function
		// It is due to the reference to journal entities.
		if (publication instanceof ConferenceBasedPublication) {
			final ConferenceBasedPublication cbp = (ConferenceBasedPublication) publication;
			final Conference conference = cbp.getConference();
			if (conference != null) {
				final String conferenceId = repository.get(conference);
				if (Strings.isNullOrEmpty(conferenceId)) {
					// Conference not found in the repository. It is an unexpected behavior but
					// the name of the conference is output to JSON
					jsonPublication.set(CONFERENCE_KEY, jsonPublication.textNode(conference.getName()));
				} else {
					jsonPublication.set(CONFERENCE_KEY, createReference(conferenceId, jsonPublication));
				}
			}
		}

		return id;
	}

	private String createPerson(ObjectNode root, Map<Object, String> repository, Person person) throws Exception {
		final ArrayNode personNode = (ArrayNode) root.get(PERSONS_SECTION);
		if (personNode != null) {
			final ObjectNode jsonPerson = personNode.objectNode();
			final String id = PERSON_ID_PREFIX + personNode.size();
			exportObject(jsonPerson, id, person, jsonPerson, null);
			repository.put(person, id);
			personNode.add(jsonPerson);
			return id;
		}
		return null;
	}

	/** Export the jury memberships to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportJuryMemberships(ObjectNode root, Map<Object, String> repository) throws Exception {
		final List<JuryMembership> memberships = this.juryMembershipRepository.findAll();
		if (!memberships.isEmpty()) {
			final ArrayNode array = root.arrayNode();
			int i = 0;
			for (final JuryMembership membership : memberships) {
				final ObjectNode jsonMembership = array.objectNode();

				final String id = JURY_MEMBERSHIP_ID_PREFIX + i;
				exportObject(jsonMembership, id, membership, jsonMembership, null);

				// Persons must be added explicitly because the "exportObject" function
				// ignore the getter functions for all.
				final String personId = repository.get(membership.getPerson());
				if (!Strings.isNullOrEmpty(personId)) {
					addReference(jsonMembership, PERSON_KEY, personId);
				}
				final String candidateId = repository.get(membership.getCandidate());
				if (!Strings.isNullOrEmpty(candidateId)) {
					addReference(jsonMembership, CANDIDATE_KEY, candidateId);
				}
				final ArrayNode promoterArray = jsonMembership.arrayNode();
				for (final Person promoter : membership.getPromoters()) {
					final String promoterId = repository.get(promoter);
					promoterArray.add(createReference(promoterId, jsonMembership));
				}
				if (promoterArray.size() > 0) {
					jsonMembership.set(PROMOTERS_KEY, promoterArray);
				}
				if (jsonMembership.size() > 0) {
					array.add(jsonMembership);
					++i;
				}
			}
			if (array.size() > 0) {
				root.set(JURY_MEMBERSHIPS_SECTION, array);
			}
		}
	}

	/** Export the supervisions to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportSupervisions(ObjectNode root, Map<Object, String> repository) throws Exception {
		final List<Supervision> supervisions = this.supervisionRepository.findAll();
		if (!supervisions.isEmpty()) {
			final ArrayNode array = root.arrayNode();
			int i = 0;
			for (final Supervision supervision : supervisions) {
				final ObjectNode jsonSupervision = array.objectNode();

				final String id = SUPERVISION_ID_PREFIX + i;
				exportObject(jsonSupervision, id, supervision, jsonSupervision, null);

				// Persons must be added explicitly because the "exportObject" function
				// ignore the getter functions for all.
				final String personId = repository.get(supervision.getSupervisedPerson());
				if (!Strings.isNullOrEmpty(personId)) {
					addReference(jsonSupervision, PERSON_KEY, personId);
				}
				final ArrayNode supervisorsArray = jsonSupervision.arrayNode();
				for (final Supervisor supervisorDesc : supervision.getSupervisors()) {
					final ObjectNode supervisorDescNode = supervisorsArray.objectNode();
					final String supervisorId = repository.get(supervisorDesc.getSupervisor());
					supervisorDescNode.set(PERSON_KEY, createReference(supervisorId, supervisorDescNode));
					supervisorDescNode.set(PERCENT_KEY, supervisorDescNode.numberNode(supervisorDesc.getPercentage())); 
					supervisorDescNode.set(TYPE_KEY, supervisorDescNode.textNode(supervisorDesc.getType().name()));
					supervisorsArray.add(supervisorDescNode);
				}
				if (supervisorsArray.size() > 0) {
					jsonSupervision.set(SUPERVISORS_KEY, supervisorsArray);
				}
				if (jsonSupervision.size() > 0) {
					array.add(jsonSupervision);
					++i;
				}
			}
			if (array.size() > 0) {
				root.set(SUPERVISIONS_SECTION, array);
			}
		}
	}

	/** Export the invitations to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportInvitations(ObjectNode root, Map<Object, String> repository) throws Exception {
		final List<PersonInvitation> invitations = this.invitationRepository.findAll();
		if (!invitations.isEmpty()) {
			final ArrayNode array = root.arrayNode();
			int i = 0;
			for (final PersonInvitation invitation : invitations) {
				final ObjectNode jsonInvitation = array.objectNode();

				final String id = INVITATION_ID_PREFIX + i;
				exportObject(jsonInvitation, id, invitation, jsonInvitation, null);

				// Persons must be added explicitly because the "exportObject" function
				// ignore the getter functions for all.
				String personId = repository.get(invitation.getGuest());
				if (!Strings.isNullOrEmpty(personId)) {
					addReference(jsonInvitation, GUEST_KEY, personId);
				}
				personId = repository.get(invitation.getInviter());
				if (!Strings.isNullOrEmpty(personId)) {
					addReference(jsonInvitation, INVITER_KEY, personId);
				}
				if (jsonInvitation.size() > 0) {
					array.add(jsonInvitation);
					++i;
				}
			}
			if (array.size() > 0) {
				root.set(INVITATIONS_SECTION, array);
			}
		}
	}

	/** Export the projects to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportProjects(ObjectNode root, Map<Object, String> repository) throws Exception {
		final List<Project> projects = this.projectRepository.findAll();
		if (!projects.isEmpty()) {
			final ArrayNode array = root.arrayNode();
			int i = 0;
			for (final Project project : projects) {
				final ObjectNode jsonProject = array.objectNode();

				final String id = PROJECT_ID_PREFIX + i;
				exportObject(jsonProject, id, project, jsonProject, null);

				// Organizations and persons must be added explicitly because the "exportObject" function
				// ignore the getter functions for all.
				final List<ProjectBudget> budgets = project.getBudgets();
				if (budgets != null && !budgets.isEmpty()) {
					final ArrayNode budgetNode = jsonProject.arrayNode();
					for (final ProjectBudget budget : budgets) {
						final ObjectNode node = budgetNode.addObject();
						node.put(FUNDING_KEY, budget.getFundingScheme().name());
						if (budget.getBudget() > 0f) {
							node.put(BUDGET_KEY, Float.valueOf(budget.getBudget()));
						}
						if (!Strings.isNullOrEmpty(budget.getGrant())) {
							node.put(GRANT_KEY, budget.getGrant());
						}
					}
					if (!budgetNode.isEmpty()) {
						jsonProject.set(BUDGETS_KEY, budgetNode);
					}
				}

				final String coordinatorId = repository.get(project.getCoordinator());
				if (!Strings.isNullOrEmpty(coordinatorId)) {
					addReference(jsonProject, COORDINATOR_KEY, coordinatorId);
				}
				final String localOrganizationId = repository.get(project.getLocalOrganization());
				if (!Strings.isNullOrEmpty(localOrganizationId)) {
					addReference(jsonProject, LOCAL_ORGANIZATION_KEY, localOrganizationId);
				}
				final String superOrganizationId = repository.get(project.getSuperOrganization());
				if (!Strings.isNullOrEmpty(superOrganizationId)) {
					addReference(jsonProject, SUPER_ORGANIZATION_KEY, superOrganizationId);
				}
				final String learOrganizationId = repository.get(project.getLearOrganization());
				if (!Strings.isNullOrEmpty(learOrganizationId)) {
					addReference(jsonProject, LEAR_ORGANIZATION_KEY, learOrganizationId);
				}
				final List<ResearchOrganization> otherPartners = project.getOtherPartners();
				if (!otherPartners.isEmpty()) {
					final ArrayNode jsonPartners = jsonProject.arrayNode();
					for (final ResearchOrganization partner : otherPartners) {
						final String partnerId = repository.get(partner);
						if (!Strings.isNullOrEmpty(partnerId)) {
							jsonPartners.add(createReference(partnerId, jsonPartners));
						}
					}
					if (!jsonPartners.isEmpty()) {
						jsonProject.set(OTHER_PARTNERS_KEY, jsonPartners);
					}
				}
				final List<ProjectMember> participants = project.getParticipants();
				if (!participants.isEmpty()) {
					final ArrayNode jsonParticipants = jsonProject.arrayNode();
					for (final ProjectMember participant : participants) {
						final String participantId = repository.get(participant.getPerson());
						if (!Strings.isNullOrEmpty(participantId)) {
							final ObjectNode jsonParticipant = jsonParticipants.objectNode();
							jsonParticipant.set(PERSON_KEY, createReference(participantId, jsonParticipant));
							jsonParticipant.put(ROLE_KEY, participant.getRole().name());
							jsonParticipants.add(jsonParticipant);
						}
					}
					if (!jsonParticipants.isEmpty()) {
						jsonProject.set(PARTICIPANTS_KEY, jsonParticipants);
					}
				}
				exportStringList(jsonProject, VIDEO_URLS_KEY, project.getVideoURLs());
				exportStringList(jsonProject, PATHS_TO_IMAGES_KEY, project.getPathsToImages());
				if (jsonProject.size() > 0) {
					repository.put(project, id);
					array.add(jsonProject);
					++i;
				}
			}
			if (array.size() > 0) {
				root.set(PROJECTS_SECTION, array);
			}
		}
	}

	/** Export the given list of string into a JSON field.
	 *
	 * @param receiver the JSON receiver.
	 * @param fieldName the field name.
	 * @param data the data to export.
	 */
	@SuppressWarnings("static-method")
	protected void exportStringList(ObjectNode receiver, String fieldName, List<String> data) {
		if (data != null && !data.isEmpty()) {
			final ArrayNode jsonArray = receiver.arrayNode();
			for (final String element : data) {
				if (!Strings.isNullOrEmpty(element)) {
					jsonArray.add(element);
				}
			}
			if (!jsonArray.isEmpty()) {
				receiver.set(fieldName, jsonArray);
			}
		}
	}

	/** Export the associated structures to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 * @since 3.2
	 */
	protected void exportAssociatedStructures(ObjectNode root, Map<Object, String> repository) throws Exception {
		final List<AssociatedStructure> structures = this.structureRepository.findAll();
		if (!structures.isEmpty()) {
			final ArrayNode array = root.arrayNode();
			int i = 0;
			for (final AssociatedStructure structure : structures) {
				final ObjectNode jsonStructure = array.objectNode();

				final String id = ASSOCIATED_STRUCTURE_ID_PREFIX + i;
				exportObject(jsonStructure, id, structure, jsonStructure, null);

				// Several fields must be added explicitly because the "exportObject" function
				// ignore the getter functions for all.

				final String fundingOrganizationId = repository.get(structure.getFundingOrganization());
				if (!Strings.isNullOrEmpty(fundingOrganizationId)) {
					addReference(jsonStructure, FUNDING_KEY, fundingOrganizationId);
				}

				final List<AssociatedStructureHolder> holders = structure.getHolders();
				if (holders != null && !holders.isEmpty()) {
					final ArrayNode holderNode = jsonStructure.arrayNode();
					for (final AssociatedStructureHolder holder : holders) {
						final ObjectNode node = holderNode.addObject();
						final String personId = repository.get(holder.getPerson());
						if (!Strings.isNullOrEmpty(personId)) {
							addReference(node, PERSON_KEY, personId);
						}
						node.put(ROLE_KEY, holder.getRole().name());
						if (!Strings.isNullOrEmpty(holder.getRoleDescription())) {
							node.put(ROLE_DESCRIPTION_KEY, holder.getRoleDescription());
						}
						final String organizationId = repository.get(holder.getOrganization());
						if (!Strings.isNullOrEmpty(organizationId)) {
							addReference(node, ORGANIZATION_KEY, organizationId);
						}
						final String superOrganizationId = repository.get(holder.getSuperOrganization());
						if (!Strings.isNullOrEmpty(superOrganizationId)) {
							addReference(node, SUPER_ORGANIZATION_KEY, superOrganizationId);
						}
					}
					if (!holderNode.isEmpty()) {
						jsonStructure.set(HOLDERS_KEY, holderNode);
					}
				}

				final List<Project> projects = structure.getProjects();
				if (projects != null && !projects.isEmpty()) {
					final ArrayNode projectNode = jsonStructure.arrayNode();
					for (final Project project : projects) {
						final String projectId = repository.get(project);
						addReference(projectNode, projectId);
					}
					if (!projectNode.isEmpty()) {
						jsonStructure.set(PROJECTS_KEY, projectNode);
					}
				}
				if (jsonStructure.size() > 0) {
					array.add(jsonStructure);
					++i;
				}
			}
			if (array.size() > 0) {
				root.set(ASSOCIATED_STRUCTURES_SECTION, array);
			}
		}
	}

	/** Export the teaching activities to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 * @since 3.4
	 */
	protected void exportTeachingActivities(ObjectNode root, Map<Object, String> repository) throws Exception {
		final List<TeachingActivity> activities = this.teachingRepository.findAll();
		if (!activities.isEmpty()) {
			final ArrayNode array = root.arrayNode();
			int i = 0;
			for (final TeachingActivity activity : activities) {
				final ObjectNode jsonActivity = array.objectNode();

				final String id = TEACHING_ACTIVITY_ID_PREFIX + i;
				exportObject(jsonActivity, id, activity, jsonActivity, null);

				// Organizations and persons must be added explicitly because the "exportObject" function
				// ignore the getter functions for all.
				final String personId = repository.get(activity.getPerson());
				if (!Strings.isNullOrEmpty(personId)) {
					addReference(jsonActivity, PERSON_KEY, personId);
				}

				final String universityId = repository.get(activity.getUniversity());
				if (!Strings.isNullOrEmpty(universityId)) {
					addReference(jsonActivity, UNIVERSITY_KEY, universityId);
				}

				final Map<TeachingActivityType, Float> annualWorkPerType = activity.getAnnualWorkPerType();
				if (annualWorkPerType != null && !annualWorkPerType.isEmpty()) {
					final ArrayNode hoursNode = jsonActivity.arrayNode();
					for (final Entry<TeachingActivityType, Float> hours : annualWorkPerType.entrySet()) {
						final ObjectNode node = hoursNode.addObject();
						final Float value = hours.getValue();
						if (value != null && value.floatValue() > 0f) {
							node.put(TYPE_KEY, hours.getKey().name());
							node.put(HOURS_KEY, value);
						}
					}
					if (!hoursNode.isEmpty()) {
						jsonActivity.set(ANNUAL_HOURS_KEY, hoursNode);
					}
				}
				if (jsonActivity.size() > 0) {
					repository.put(activity, id);
					array.add(jsonActivity);
					++i;
				}
			}
			if (array.size() > 0) {
				root.set(TEACHING_ACTIVITY_SECTION, array);
			}
		}
	}

	/** Export the scientific axes to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 * @since 3.4
	 */
	protected void exportScientificAxes(ObjectNode root, Map<Object, String> repository) throws Exception {
		final List<ScientificAxis> axes = this.scientificAxisRepository.findAll();
		if (!axes.isEmpty()) {
			final ArrayNode array = root.arrayNode();
			int i = 0;
			for (final ScientificAxis axis : axes) {
				final ObjectNode jsonAxis = array.objectNode();

				final String id = SCIENTFIC_AXIS_ID_PREFIX + i;
				exportObject(jsonAxis, id, axis, jsonAxis, null);

				// Other JPA entities must be added explicitly because the "exportObject" function
				// ignore the getter functions for all.

				final Set<Project> projects = axis.getProjects();
				if (projects != null && !projects.isEmpty()) {
					final ArrayNode projectArray = jsonAxis.putArray(PROJECTS_KEY);
					for (final Project project : projects) {
						final String projectId = repository.get(project);
						if (!Strings.isNullOrEmpty(projectId)) {
							addReference(projectArray, projectId);
						}
					}
				}

				final Set<Publication> publications = axis.getPublications();
				if (publications != null && !publications.isEmpty()) {
					final ArrayNode publicationArray = jsonAxis.putArray(PUBLICATIONS_KEY);
					for (final Publication publication : publications) {
						final String publicationId = repository.get(publication);
						if (!Strings.isNullOrEmpty(publicationId)) {
							addReference(publicationArray, publicationId);
						}
					}
				}

				final Set<Membership> memberships = axis.getMemberships();
				if (memberships != null && !memberships.isEmpty()) {
					final ArrayNode membershipArray = jsonAxis.putArray(MEMBERSHIPS_KEY);
					for (final Membership membership : memberships) {
						final String membershipId = repository.get(membership);
						if (!Strings.isNullOrEmpty(membershipId)) {
							addReference(membershipArray, membershipId);
						}
					}
				}

				if (jsonAxis.size() > 0) {
					repository.put(axis, id);
					array.add(jsonAxis);
					++i;
				}
			}
			if (array.size() > 0) {
				root.set(SCIENTIFIC_AXIS_SECTION, array);
			}
		}
	}

	/** Export the application users to the given JSON root element.
	 *
	 * @param root the receiver of the JSON elements.
	 * @param repository the repository of elements that maps an object to its JSON id.
	 * @throws Exception if there is problem for exporting.
	 */
	protected void exportApplicationUsers(ObjectNode root, Map<Object, String> repository) throws Exception {
		final List<User> users = this.userRepository.findAll();
		if (!users.isEmpty()) {
			final ArrayNode array = root.arrayNode();
			int i = 0;
			for (final User user : users) {
				final ObjectNode jsonUser = array.objectNode();

				final String id = APPLICATION_USER_ID_PREFIX + i;
				exportObject(jsonUser, id, user, jsonUser, null);

				// Persons must be added explicitly because the "exportObject" function
				// ignore the getter functions for all.
				final String personId = repository.get(user.getPerson());
				if (!Strings.isNullOrEmpty(personId)) {
					addReference(jsonUser, PERSON_KEY, personId);
				}

				if (jsonUser.size() > 0) {
					repository.put(user, id);
					array.add(jsonUser);
					++i;
				}
			}
			if (array.size() > 0) {
				root.set(APPLICATION_USERS_SECTION, array);
			}
		}
	}

}