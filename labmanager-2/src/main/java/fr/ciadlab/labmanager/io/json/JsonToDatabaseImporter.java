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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.assostructure.AssociatedStructure;
import fr.ciadlab.labmanager.entities.assostructure.AssociatedStructureHolder;
import fr.ciadlab.labmanager.entities.assostructure.HolderRole;
import fr.ciadlab.labmanager.entities.indicator.GlobalIndicators;
import fr.ciadlab.labmanager.entities.invitation.PersonInvitation;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.journal.JournalQualityAnnualIndicators;
import fr.ciadlab.labmanager.entities.jury.JuryMembership;
import fr.ciadlab.labmanager.entities.member.Gender;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.OrganizationAddress;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.entities.project.ProjectBudget;
import fr.ciadlab.labmanager.entities.project.ProjectMember;
import fr.ciadlab.labmanager.entities.project.Role;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.entities.publication.JournalBasedPublication;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationComparator;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.supervision.Supervision;
import fr.ciadlab.labmanager.entities.supervision.Supervisor;
import fr.ciadlab.labmanager.entities.supervision.SupervisorType;
import fr.ciadlab.labmanager.repository.assostructure.AssociatedStructureRepository;
import fr.ciadlab.labmanager.repository.indicator.GlobalIndicatorsRepository;
import fr.ciadlab.labmanager.repository.invitation.PersonInvitationRepository;
import fr.ciadlab.labmanager.repository.journal.JournalQualityAnnualIndicatorsRepository;
import fr.ciadlab.labmanager.repository.journal.JournalRepository;
import fr.ciadlab.labmanager.repository.jury.JuryMembershipRepository;
import fr.ciadlab.labmanager.repository.member.MembershipRepository;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.organization.OrganizationAddressRepository;
import fr.ciadlab.labmanager.repository.organization.ResearchOrganizationRepository;
import fr.ciadlab.labmanager.repository.project.ProjectRepository;
import fr.ciadlab.labmanager.repository.publication.AuthorshipRepository;
import fr.ciadlab.labmanager.repository.publication.PublicationRepository;
import fr.ciadlab.labmanager.repository.supervision.SupervisionRepository;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.publication.PublicationService;
import fr.ciadlab.labmanager.utils.funding.FundingScheme;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.eclipse.xtext.xbase.lib.Functions.Function3;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Importer of JSON data into the database.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 * @see ZipToDatabaseImporter
 */
@Component
public class JsonToDatabaseImporter extends JsonTool {

	private SessionFactory sessionFactory;

	private OrganizationAddressRepository addressRepository;

	private ResearchOrganizationRepository organizationRepository;

	private PersonRepository personRepository;

	private PersonService personService;

	private MembershipRepository organizationMembershipRepository;

	private JournalRepository journalRepository;

	private JournalQualityAnnualIndicatorsRepository journalIndicatorsRepository;

	private PublicationRepository publicationRepository;

	private PublicationService publicationService;

	private PublicationComparator publicationComparator;

	private AuthorshipRepository authorshipRepository;

	private PersonNameParser personNameParser;

	private JuryMembershipRepository juryMembershipRepository;

	private SupervisionRepository supervisionRepository;

	private PersonInvitationRepository invitationRepository;

	private GlobalIndicatorsRepository globalIndicatorsRepository;

	private ProjectRepository projectRepository;

	private AssociatedStructureRepository structureRepository;

	private final Multimap<String, String> fieldAliases = LinkedListMultimap.create();

	private boolean fake;

	/** Constructor.
	 * 
	 * @param sessionFactory the factory of an hibernate session.
	 * @param addressRepository the accessor to the address repository.
	 * @param organizationRepository the accessor to the organization repository.
	 * @param personRepository the accessor to the person repository.
	 * @param personService the accessor to the high-level person services.
	 * @param organizationMembershipRepository the accessor to the organization membership repository.
	 * @param journalRepository the accessor to the journal repository.
	 * @param journalIndicatorsRepository the accessor to the repository of the journal quality annual indicators.
	 * @param publicationRepository the accessor to the repository of the publications.
	 * @param publicationService the service related to the publications.
	 * @param publicationComparator the comparator of publications.
	 * @param authorshipRepository the accessor to the authorships.
	 * @param personNameParser the parser of person names.
	 * @param juryMembershipRepository the repository of jury memberships.
	 * @param supervisionRepository the repository of supervisions.
	 * @param invitationRepository the repository of invitations.
	 * @param globalIndicatorsRepository the repository of the global indicators.
	 * @param projectRepository the repository of the projects.
	 * @param structureRepository the repository of the projects.
	 */
	public JsonToDatabaseImporter(
			@Autowired SessionFactory sessionFactory,
			@Autowired OrganizationAddressRepository addressRepository,
			@Autowired ResearchOrganizationRepository organizationRepository,
			@Autowired PersonRepository personRepository,
			@Autowired PersonService personService,
			@Autowired MembershipRepository organizationMembershipRepository,
			@Autowired JournalRepository journalRepository,
			@Autowired JournalQualityAnnualIndicatorsRepository journalIndicatorsRepository,
			@Autowired PublicationRepository publicationRepository,
			@Autowired PublicationService publicationService,
			@Autowired PublicationComparator publicationComparator,
			@Autowired AuthorshipRepository authorshipRepository,
			@Autowired PersonNameParser personNameParser,
			@Autowired JuryMembershipRepository juryMembershipRepository,
			@Autowired SupervisionRepository supervisionRepository,
			@Autowired PersonInvitationRepository invitationRepository,
			@Autowired GlobalIndicatorsRepository globalIndicatorsRepository,
			@Autowired ProjectRepository projectRepository,
			@Autowired AssociatedStructureRepository structureRepository) {
		this.sessionFactory = sessionFactory;
		this.addressRepository = addressRepository;
		this.organizationRepository = organizationRepository;
		this.personRepository = personRepository;
		this.personService = personService;
		this.organizationMembershipRepository = organizationMembershipRepository;
		this.journalRepository = journalRepository;
		this.journalIndicatorsRepository = journalIndicatorsRepository;
		this.publicationRepository = publicationRepository;
		this.publicationService = publicationService;
		this.publicationComparator = publicationComparator;
		this.authorshipRepository = authorshipRepository;
		this.personNameParser = personNameParser;
		this.juryMembershipRepository = juryMembershipRepository;
		this.supervisionRepository = supervisionRepository;
		this.invitationRepository = invitationRepository;
		this.globalIndicatorsRepository = globalIndicatorsRepository;
		this.projectRepository = projectRepository;
		this.structureRepository = structureRepository;
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

	private static <T extends Enum<T>> T getEnum(JsonNode content, String key, Class<T> type) {
		if (content != null && !Strings.isNullOrEmpty(key) && content.isObject()) {
			final JsonNode value = content.get(key);
			if (value != null) {
				try {
					final String name = value.asText();
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

	private static int getInt(JsonNode content, String key, int defaultValue) {
		if (content != null && !Strings.isNullOrEmpty(key) && content.isObject()) {
			final JsonNode value = content.get(key);
			if (value != null) {
				try {
					final String strValue = value.asText();
					if (!Strings.isNullOrEmpty(strValue)) {
						return Integer.parseInt(strValue);
					}
				} catch (Throwable ex) {
					//
				}
			}
		}
		return defaultValue;
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

	private static String getId(JsonNode content) {
		final JsonNode child = content.get(ID_FIELDNAME);
		if (child != null) {
			return Strings.emptyToNull(child.asText());
		}
		return null;
	}

	private static String getRef(JsonNode content) {
		if (content != null && content.isObject()) {
			final JsonNode value = content.get(ID_FIELDNAME);
			if (value != null) {
				return value.asText();
			}
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
	 *      the JSON file. The first argument is the attribute name, the second argument is the raw value for
	 *      the attribute, the third argument is the JSON node of the value.
	 * @return the object or {@code null}.
	 * @throws Exception if the object cannot be created.
	 */
	protected <T> T createObject(Class<T> type, JsonNode source,
			Map<String, Set<String>> aliasRepository,
			Function3<String, Object, JsonNode, Boolean> failIfNoSetter) throws Exception {
		if (!source.isObject()) {
			throw new IllegalArgumentException("Source node for an object must be a Json map."); //$NON-NLS-1$
		}
		final T obj = type.getConstructor().newInstance();
		final Iterator<Entry<String, JsonNode>> iterator = source.fields();
		while (iterator.hasNext()) {
			final Entry<String, JsonNode> entry = iterator.next();
			final String key = entry.getKey();
			final JsonNode jsonValue = entry.getValue();
			if (!Strings.isNullOrEmpty(key) && !key.startsWith(HIDDEN_FIELD_PREFIX)
					&& !key.startsWith(SPECIAL_FIELD_PREFIX) &&!key.equalsIgnoreCase("id")) { //$NON-NLS-1$
				final Set<String> aliases = aliasRepository.computeIfAbsent(key, it -> {
					final TreeSet<String> set = new TreeSet<>();
					set.add(SETTER_FUNCTION_PREFIX + key.toLowerCase());
					if (key.startsWith(IS_GETTER_FUNCTION_PREFIX)) {
						set.add(SETTER_FUNCTION_PREFIX + key.substring(IS_GETTER_FUNCTION_PREFIX.length()).toLowerCase());
					}
					for (final String alias : getFieldAliases(key)) {
						set.add(SETTER_FUNCTION_PREFIX + alias.toLowerCase());
					}
					return set;
				});
				final Object rawValue = getRawValue(jsonValue);
				final Method method;
				if (rawValue != null) {
					method = findSetterMethod(type, aliases, rawValue);
				} else {
					method = null;
				}
				if (method != null) {
					method.invoke(obj, rawValue);
				} else if (failIfNoSetter != null && failIfNoSetter.apply(key, rawValue, jsonValue).booleanValue()) {
					throw new IllegalArgumentException("Setter function not found for the attribute: " + key //$NON-NLS-1$
							+ "; with a value of type: " + jsonValue); //$NON-NLS-1$
				}
			}
		}
		return obj;
	}

	/** Run the importer for JSON data source only.
	 * This function calls {@link #importJsonFileToDatabase(URL)} and displays an information
	 * message.
	 *
	 * @param url the URL of the JSON file to read.
	 * @throws Exception if there is problem for importing.
	 * @see #importJsonFileToDatabase(URL)
	 */
	public void importDataFileToDatabase(URL url) throws Exception {
		final Stats stats = importJsonFileToDatabase(url);
		getLogger().info("Summary of inserts:\n" //$NON-NLS-1$
				+ stats.addresses + " addresses;\n" //$NON-NLS-1$
				+ stats.organizations + " organizations;\n" //$NON-NLS-1$
				+ stats.journals + " journals;\n" //$NON-NLS-1$
				+ stats.persons + " explicit persons;\n" //$NON-NLS-1$
				+ stats.authors + " external authors;\n" //$NON-NLS-1$
				+ stats.organizationMemberships + " organization memberships;\n" //$NON-NLS-1$
				+ stats.publications + " publications;\n" //$NON-NLS-1$
				+ stats.juryMemberships + " jury memberships;\n" //$NON-NLS-1$
				+ stats.supervisions + " supervisions;\n" //$NON-NLS-1$
				+ stats.invitations + " invitations;\n" //$NON-NLS-1$
				+ stats.projects + " projects;\n" //$NON-NLS-1$
				+ stats.associatedStructures + " associated structures.\n"); //$NON-NLS-1$
	}

	/** Run the importer for JSON data source only.
	 *
	 * @param url the URL of the JSON file to read.
	 * @return the import stats.
	 * @throws Exception if there is problem for importing.
	 * @see #importDataFileToDatabase(URL)
	 */
	public Stats importJsonFileToDatabase(URL url) throws Exception {
		try (final InputStream is = url.openStream()) {
			return importJsonFileToDatabase(is);
		}
	}

	/** Run the importer for JSON data source only.
	 *
	 * @param inputStream the input stream of the JSON file to read.
	 * @return the import stats.
	 * @throws Exception if there is problem for importing.
	 * @see #importDataFileToDatabase(URL)
	 */
	public Stats importJsonFileToDatabase(InputStream inputStream) throws Exception {
		final JsonNode content;
		try (final InputStreamReader isr = new InputStreamReader(inputStream)) {
			final ObjectMapper mapper = new ObjectMapper();
			content = mapper.readTree(isr);
		}
		return importJsonFileToDatabase(content, false, null);
	}

	/** Run the importer for JSON data source only.
	 *
	 * @param content the input node of the JSON file to read.
	 * @param clearDatabase indicates if the database is clear before importing.
	 * @param fileCallback a tool that is invoked when associated file is detected. It could be {@code null}.
	 * @return the import stats.
	 * @throws Exception if there is problem for importing.
	 * @see #importDataFileToDatabase(URL)
	 */
	public Stats importJsonFileToDatabase(JsonNode content, boolean clearDatabase, FileCallback fileCallback) throws Exception {
		if (content != null && !content.isEmpty()) {
			final Map<String, Integer> objectRepository = new TreeMap<>();
			final Map<String, Set<String>> aliasRepository = new TreeMap<>();

			try (final Session session = this.sessionFactory.openSession()) {
				if (clearDatabase) {
					clearDatabase(session);
				}
				insertGlobalIndicators(session, content.get(GLOBALINDICATORS_SECTION), objectRepository, aliasRepository);
				final int nb6 = insertAddresses(session, content.get(ORGANIZATIONADDRESSES_SECTION), objectRepository, aliasRepository, fileCallback);
				final int nb0 = insertOrganizations(session, content.get(RESEARCHORGANIZATIONS_SECTION), objectRepository, aliasRepository);
				final int nb1 = insertPersons(session, content.get(PERSONS_SECTION), objectRepository, aliasRepository);
				final int nb2 = insertJournals(session, content.get(JOURNALS_SECTION), objectRepository, aliasRepository);
				final int nb3 = insertOrganizationMemberships(session, content.get(ORGANIZATION_MEMBERSHIPS_SECTION), objectRepository, aliasRepository);
				final Pair<Integer, Integer> added = insertPublications(session, content.get(PUBLICATIONS_SECTION), objectRepository, aliasRepository, fileCallback);
				final int nb4 = added != null ? added.getLeft().intValue() : 0;
				final int nb5 = added != null ? added.getRight().intValue() : 0;
				final int nb7 = insertJuryMemberships(session, content.get(JURY_MEMBERSHIPS_SECTION), objectRepository, aliasRepository);
				final int nb8 = insertSupervisions(session, content.get(SUPERVISIONS_SECTION), objectRepository, aliasRepository);
				final int nb9 = insertInvitations(session, content.get(INVITATIONS_SECTION), objectRepository, aliasRepository);
				final int nb10 = insertProjects(session, content.get(PROJECTS_SECTION), objectRepository, aliasRepository, fileCallback);
				final int nb11 = insertAssociatedStructures(session, content.get(ASSOCIATED_STRUCTURES_SECTION), objectRepository, aliasRepository, fileCallback);
				return new Stats(nb6, nb0, nb2, nb1, nb5, nb3, nb4, nb7, nb8, nb9, nb10, nb11);
			}
		}
		return new Stats();
	}

	/** Clear the database content.
	 *
	 * @param session the database session.
	 */
	protected void clearDatabase(Session session) {
		getLogger().info("Clearing database content..."); //$NON-NLS-1$
		this.authorshipRepository.deleteAll();
		this.publicationRepository.deleteAll();
		this.organizationMembershipRepository.deleteAll();
		this.journalRepository.deleteAll();
		this.journalIndicatorsRepository.deleteAll();
		this.personRepository.deleteAll();
		this.organizationRepository.deleteAll();
		this.addressRepository.deleteAll();
	}

	/** Create the global indicators in the database.
	 *
	 * @param session the JPA session for managing transactions.
	 * @param globalIndicators the global indicators.
	 * @param objectIdRepository the mapping from JSON {@code @id} field and the JPA database identifier.
	 * @param aliasRepository the repository of field aliases.
	 * @throws Exception if an address cannot be created.
	 */
	protected void insertGlobalIndicators(Session session, JsonNode globalIndicators, Map<String, Integer> objectIdRepository,
			Map<String, Set<String>> aliasRepository) throws Exception {
		if (globalIndicators != null && !globalIndicators.isEmpty()) {
			getLogger().info("Inserting global indicators..."); //$NON-NLS-1$
			final JsonNode visibleIndicators = globalIndicators.get(VISIBLEGLOBALINDICATORS_KEY);
			if (visibleIndicators != null && visibleIndicators.isArray()) {
				final List<String> keys = new ArrayList<>();
				for (final JsonNode valueNode : visibleIndicators) {
					keys.add(valueNode.asText());
				}
				if (!keys.isEmpty()) {
					final GlobalIndicators gi = new GlobalIndicators();
					gi.setVisibleIndicators(keys);
					gi.setValues(null);
					this.globalIndicatorsRepository.save(gi);
				}
			}
		}
	}

	/** Create the organization addresses in the database.
	 *
	 * @param session the JPA session for managing transactions.
	 * @param addresses the list of addresses in the Json source.
	 * @param objectIdRepository the mapping from JSON {@code @id} field and the JPA database identifier.
	 * @param aliasRepository the repository of field aliases.
	 * @param fileCallback a tool that is invoked when associated file is detected. It could be {@code null}.
	 * @return the number of new addresses in the database.
	 * @throws Exception if an address cannot be created.
	 */
	protected int insertAddresses(Session session, JsonNode addresses, Map<String, Integer> objectIdRepository,
			Map<String, Set<String>> aliasRepository, FileCallback fileCallback) throws Exception {
		int nbNew = 0;
		if (addresses != null && !addresses.isEmpty()) {
			getLogger().info("Inserting " + addresses.size() + " addresses..."); //$NON-NLS-1$ //$NON-NLS-2$
			int i = 0;
			for (final JsonNode adrObject : addresses) {
				getLogger().info("> Address " + (i + 1) + "/" + addresses.size()); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					final String id = getId(adrObject);
					session.beginTransaction();
					OrganizationAddress adr = createObject(OrganizationAddress.class, adrObject,
							aliasRepository, null);
					if (adr != null) {
						final Optional<OrganizationAddress> existing = this.addressRepository.findDistinctByName(adr.getName());
						if (existing.isEmpty()) {
							if (!isFake()) {
								adr = this.addressRepository.save(adr);
							}
							// Ensure that attached files are correct
							if (fileCallback != null) {
								boolean publicationChanged = false;
								if (!Strings.isNullOrEmpty(adr.getPathToBackgroundImage())) {
									final String ofn = adr.getPathToBackgroundImage();
									final String fn = fileCallback.addressBackgroundImageFile(adr.getId(), ofn);
									if (!Objects.equals(ofn, fn)) {
										adr.setPathToBackgroundImage(fn);
										publicationChanged = true;
									}
								}
								if (publicationChanged && !isFake()) {
									this.addressRepository.save(adr);
								}
							}
							++nbNew;
							getLogger().info("  + " + adr.getName() + " (id: " + adr.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							if (!Strings.isNullOrEmpty(id)) {
								objectIdRepository.put(id, Integer.valueOf(adr.getId()));
							}
						} else {
							getLogger().info("  X " + existing.get().getName() + " (id: " + existing.get().getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							if (!Strings.isNullOrEmpty(id)) {
								objectIdRepository.put(id, Integer.valueOf(existing.get().getId()));
							}
							if (fileCallback != null) {
								if (!Strings.isNullOrEmpty(existing.get().getPathToBackgroundImage())) {
									fileCallback.addressBackgroundImageFile(existing.get().getId(), existing.get().getPathToBackgroundImage());
								}
							}
						}
					}
					session.getTransaction().commit();
				} catch (Throwable ex) {
					throw new UnableToImportJsonException(ORGANIZATIONADDRESSES_SECTION, i, adrObject,ex);
				}
				++i;
			}
		}
		return nbNew;
	}

	/** Create the research organizations in the database.
	 *
	 * @param session the JPA session for managing transactions.
	 * @param organizations the list of organizations in the Json source.
	 * @param objectIdRepository the mapping from JSON {@code @id} field and the JPA database identifier.
	 * @param aliasRepository the repository of field aliases.
	 * @return the number of new organizations in the database.
	 * @throws Exception if an organization cannot be created.
	 */
	protected int insertOrganizations(Session session, JsonNode organizations, Map<String, Integer> objectIdRepository,
			Map<String, Set<String>> aliasRepository) throws Exception {
		int nbNew = 0;
		if (organizations != null && !organizations.isEmpty()) {
			final Map<String, ResearchOrganization> objectInstanceRepository = new TreeMap<>();
			getLogger().info("Inserting " + organizations.size() + " organizations..."); //$NON-NLS-1$ //$NON-NLS-2$
			int i = 0;
			final List<Pair<ResearchOrganization, String>> superOrgas = new ArrayList<>();
			for (final JsonNode orgaObject : organizations) {
				getLogger().info("> Organization " + (i + 1) + "/" + organizations.size()); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					final String id = getId(orgaObject);
					session.beginTransaction();
					ResearchOrganization orga = createObject(ResearchOrganization.class, orgaObject,
							aliasRepository, null);
					if (orga != null) {
						final Optional<ResearchOrganization> existing = this.organizationRepository.findDistinctByAcronymOrName(orga.getAcronym(), orga.getName());
						if (existing.isEmpty()) {
							// Save addresses
							final JsonNode addressesNode = orgaObject.get(ADDRESSES_KEY);
							if (addressesNode != null) {
								final Set<OrganizationAddress> addrs = new TreeSet<>(EntityUtils.getPreferredOrganizationAddressComparator());
								for (final JsonNode addressRefNode : addressesNode) {
									final String addressRef = getRef(addressRefNode);
									if (Strings.isNullOrEmpty(addressRef)) {
										throw new IllegalArgumentException("Invalid address reference for organization with id: " + id); //$NON-NLS-1$
									}
									final Integer addressDbId = objectIdRepository.get(addressRef);
									if (addressDbId == null || addressDbId.intValue() == 0) {
										throw new IllegalArgumentException("Invalid address reference for organization with id: " + id); //$NON-NLS-1$
									}
									final Optional<OrganizationAddress> addressObj = this.addressRepository.findById(addressDbId);
									if (addressObj.isEmpty()) {
										throw new IllegalArgumentException("Invalid address reference for organization with id: " + id); //$NON-NLS-1$
									}
									addrs.add(addressObj.get());
								}
								orga.setAddresses(addrs);
							}
							if (!isFake()) {
								orga = this.organizationRepository.save(orga);
							}
							++nbNew;
							getLogger().info("  + " + orga.getAcronymOrName() + " (id: " + orga.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							if (!Strings.isNullOrEmpty(id)) {
								objectInstanceRepository.put(id, orga);
								objectIdRepository.put(id, Integer.valueOf(orga.getId()));
							}
							// Save hierarchical relation with other organization
							final String superOrga = getRef(orgaObject.get(SUPER_ORGANIZATION_KEY));
							if (!Strings.isNullOrEmpty(superOrga)) {
								superOrgas.add(Pair.of(orga, superOrga));
							}
						} else {
							getLogger().info("  X " + existing.get().getAcronymOrName() + " (id: " + existing.get().getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							if (!Strings.isNullOrEmpty(id)) {
								objectInstanceRepository.put(id, existing.get());
								objectIdRepository.put(id, Integer.valueOf(existing.get().getId()));
							}
						}
					}
					session.getTransaction().commit();
				} catch (Throwable ex) {
					throw new UnableToImportJsonException(RESEARCHORGANIZATIONS_SECTION, i, orgaObject,ex);
				}
				++i;
			}
			// Save hierarchical relations between organizations
			for (final Pair<ResearchOrganization, String> entry : superOrgas) {
				final ResearchOrganization sup = objectInstanceRepository.get(entry.getRight());
				if (sup == null) {
					throw new IllegalArgumentException("Invalid reference to Json element with id: " + entry.getRight()); //$NON-NLS-1$
				}
				session.beginTransaction();
				entry.getLeft().setSuperOrganization(sup);
				sup.getSubOrganizations().add(entry.getLeft());
				if (!isFake()) {
					this.organizationRepository.save(entry.getLeft());
					this.organizationRepository.save(sup);
				}
				session.getTransaction().commit();
			}
		}
		return nbNew;
	}

	/** Create the persons in the database.
	 *
	 * @param session the JPA session for managing transactions.
	 * @param persons the list of persons in the Json source.
	 * @param objectIdRepository the mapping from JSON {@code @id} field and the JPA database identifier.
	 * @param aliasRepository the repository of field aliases.
	 * @return the number of new persons in the database.
	 * @throws Exception if a person cannot be created.
	 */
	protected int insertPersons(Session session, JsonNode persons, Map<String, Integer> objectIdRepository,
			Map<String, Set<String>> aliasRepository) throws Exception {
		int nbNew = 0;
		if (persons != null && !persons.isEmpty()) {
			getLogger().info("Inserting " + persons.size() + " persons..."); //$NON-NLS-1$ //$NON-NLS-2$
			int i = 0;
			for (JsonNode personObject : persons) {
				getLogger().info("> Person " + (i + 1) + "/" + persons.size()); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					final String id = getId(personObject);
					Person person = createObject(Person.class, personObject, aliasRepository, null);
					if (person != null) {
						session.beginTransaction();
						final Optional<Person> existing = this.personRepository.findDistinctByFirstNameAndLastName(person.getFirstName(), person.getLastName());
						if (existing.isEmpty()) {
							if (!isFake()) {
								person = this.personRepository.save(person);
							}
							++nbNew;
							getLogger().info("  + " + person.getFullName() + " (id: " + person.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							if (!Strings.isNullOrEmpty(id)) {
								objectIdRepository.put(id, Integer.valueOf(person.getId()));
							}
						} else {
							getLogger().info("  X " + existing.get().getFullName() + " (id: " + existing.get().getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							if (!Strings.isNullOrEmpty(id)) {
								objectIdRepository.put(id, Integer.valueOf(existing.get().getId()));
							}
						}
						session.getTransaction().commit();
					}
				} catch (Throwable ex) {
					throw new UnableToImportJsonException(PERSONS_SECTION, i, personObject, ex);
				}
				++i;
			}
		}
		return nbNew;
	}

	/** Create the organization memberships in the database.
	 *
	 * @param session the JPA session for managing transactions.
	 * @param memberships the list of memberships in the Json source.
	 * @param objectIdRepository the mapping from JSON {@code @id} field and the JPA database identifier.
	 * @param aliasRepository the repository of field aliases.
	 * @return the number of new memberships in the database.
	 * @throws Exception if a membership cannot be created.
	 */
	protected int insertOrganizationMemberships(Session session, JsonNode memberships, Map<String, Integer> objectIdRepository,
			Map<String, Set<String>> aliasRepository) throws Exception {
		int nbNew = 0;
		if (memberships != null && !memberships.isEmpty()) {
			getLogger().info("Inserting " + memberships.size() + " organization memberships..."); //$NON-NLS-1$ //$NON-NLS-2$
			final List<Pair<Membership, Integer>> addressPostProcessing = new ArrayList<>();
			int i = 0;
			for (JsonNode membershipObject : memberships) {
				getLogger().info("> Organization membership " + (i + 1) + "/" + memberships.size()); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					final String id = getId(membershipObject);
					Membership membership = createObject(Membership.class, membershipObject, aliasRepository, null);
					if (membership != null) {
						session.beginTransaction();
						final String adrId = getRef(membershipObject.get(ADDRESS_KEY));
						if (!Strings.isNullOrEmpty(adrId)) {
							final Integer adrDbId = objectIdRepository.get(adrId);
							if (adrDbId == null || adrDbId.intValue() == 0) {
								throw new IllegalArgumentException("Invalid address reference for organization membership with id: " + id); //$NON-NLS-1$
							}
							addressPostProcessing.add(Pair.of(membership, adrDbId));
						}
						//
						final String personId = getRef(membershipObject.get(PERSON_KEY));
						if (Strings.isNullOrEmpty(personId)) {
							throw new IllegalArgumentException("Invalid person reference for organization membership with id: " + id); //$NON-NLS-1$
						}
						final Integer personDbId = objectIdRepository.get(personId);
						if (personDbId == null || personDbId.intValue() == 0) {
							throw new IllegalArgumentException("Invalid person reference for organizationm embership with id: " + id); //$NON-NLS-1$
						}
						final Optional<Person> targetPerson = this.personRepository.findById(personDbId);
						if (targetPerson.isEmpty()) {
							throw new IllegalArgumentException("Invalid person reference for organization membership with id: " + id); //$NON-NLS-1$
						}
						//
						final String orgaId = getRef(membershipObject.get(RESEARCHORGANIZATION_KEY));
						if (Strings.isNullOrEmpty(orgaId)) {
							throw new IllegalArgumentException("Invalid organization reference for organization membership with id: " + id); //$NON-NLS-1$
						}
						final Integer orgaDbId = objectIdRepository.get(orgaId);
						if (orgaDbId == null || orgaDbId.intValue() == 0) {
							throw new IllegalArgumentException("Invalid organization reference for organization membership with id: " + id); //$NON-NLS-1$
						}
						final Optional<ResearchOrganization> targetOrganization = this.organizationRepository.findById(orgaDbId);
						if (targetOrganization.isEmpty()) {
							throw new IllegalArgumentException("Invalid organization reference for organization membership with id: " + id); //$NON-NLS-1$
						}
						//
						final Set<Membership> existings = this.organizationMembershipRepository.findByResearchOrganizationIdAndPersonId(
								targetOrganization.get().getId(), targetPerson.get().getId());
						final Membership finalmbr0 = membership;
						final Stream<Membership> existing0 = existings.stream().filter(it -> {
							assert it.getPerson().getId() == targetPerson.get().getId();
							assert it.getResearchOrganization().getId() == targetOrganization.get().getId();
							return Objects.equals(it.getMemberStatus(), finalmbr0.getMemberStatus())
									&& Objects.equals(it.getResponsibility(), finalmbr0.getResponsibility())
									&& Objects.equals(it.getMemberSinceWhen(), finalmbr0.getMemberSinceWhen())
									&& Objects.equals(it.getMemberToWhen(), finalmbr0.getMemberToWhen());
						});
						final Optional<Membership> existing = existing0.findAny();
						if (existing.isEmpty()) {
							membership.setPerson(targetPerson.get());
							membership.setResearchOrganization(targetOrganization.get());
							if (!isFake()) {
								membership = this.organizationMembershipRepository.save(membership);
								this.personRepository.save(targetPerson.get());
								this.organizationRepository.save(targetOrganization.get());
							}
							++nbNew;
							getLogger().info("  + " + targetOrganization.get().getAcronymOrName() //$NON-NLS-1$
									+ " - " + targetPerson.get().getFullName() //$NON-NLS-1$
									+ " (id: " + membership.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
							if (!Strings.isNullOrEmpty(id)) {
								objectIdRepository.put(id, Integer.valueOf(membership.getId()));
							}
						} else {
							getLogger().info("  X " + targetOrganization.get().getAcronymOrName() //$NON-NLS-1$
									+ " - " + targetPerson.get().getFullName() //$NON-NLS-1$
									+ " (id: " + existing.get().getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
							if (!Strings.isNullOrEmpty(id)) {
								objectIdRepository.put(id, Integer.valueOf(existing.get().getId()));
							}
						}
						session.getTransaction().commit();
					}
				} catch (Throwable ex) {
					throw new UnableToImportJsonException(ORGANIZATION_MEMBERSHIPS_SECTION, i, membershipObject, ex);
				}
				++i;
			}
			// Post processing of the addresses for avoiding lazy loading errors
			if (!addressPostProcessing.isEmpty()) {
				session.beginTransaction();
				for (final Pair<Membership, Integer> pair : addressPostProcessing) {
					final Membership membership = pair.getLeft();
					final Optional<OrganizationAddress> targetAddress = this.addressRepository.findById(pair.getRight());
					if (targetAddress.isEmpty()) {
						throw new IllegalArgumentException("Invalid address reference for organization membership with id: " + pair.getRight()); //$NON-NLS-1$
					}
					membership.setOrganizationAddress(targetAddress.get(), false);
					this.organizationMembershipRepository.save(membership);
				}
				session.getTransaction().commit();
			}
		}
		return nbNew;
	}

	/** Create the journals in the database.
	 *
	 * @param session the JPA session for managing transactions.
	 * @param journals the list of journals in the Json source.
	 * @param objectIdRepository the mapping from JSON {@code @id} field and the JPA database identifier.
	 * @param aliasRepository the repository of field aliases.
	 * @return the number of new journals in the database.
	 * @throws Exception if a membership cannot be created.
	 */
	protected int insertJournals(Session session, JsonNode journals, Map<String, Integer> objectIdRepository,
			Map<String, Set<String>> aliasRepository) throws Exception {
		int nbNew = 0;
		if (journals != null && !journals.isEmpty()) {
			getLogger().info("Inserting " + journals.size() + " journals..."); //$NON-NLS-1$ //$NON-NLS-2$
			int i = 0;
			for (JsonNode journalObject : journals) {
				getLogger().info("> Journal " + (i + 1) + "/" + journals.size()); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					final String id = getId(journalObject);
					Journal journal = createObject(Journal.class, journalObject, aliasRepository, null);
					if (journal != null) {
						session.beginTransaction();
						final Optional<Journal> existing = this.journalRepository.findByJournalName(journal.getJournalName());
						if (existing.isEmpty()) {
							if (!isFake()) {
								journal = this.journalRepository.save(journal);
							}
							// Create the quality indicators
							final JsonNode history = journalObject.get(QUALITYINDICATORSHISTORY_KEY);
							if (history != null && !history.isEmpty()) {
								final Iterator<Entry<String, JsonNode>> iterator = history.fields();
								while (iterator.hasNext()) {
									final Entry<String, JsonNode> historyEntry = iterator.next();
									final int year = Integer.parseInt(historyEntry.getKey());
									String str = null;
									if (historyEntry.getValue() != null) {
										final JsonNode n = historyEntry.getValue().get(SCIMAGOQINDEX_KEY);
										if (n != null) {
											str = n.asText();
										}
									}
									JournalQualityAnnualIndicators indicators = null; 
									if (!Strings.isNullOrEmpty(str) ) {
										final QuartileRanking scimago = QuartileRanking.valueOfCaseInsensitive(str);
										if (scimago != null) {
											indicators = journal.setScimagoQIndexByYear(year, scimago);
										}
									}
									str = null;
									if (historyEntry.getValue() != null) {
										final JsonNode n = historyEntry.getValue().get(WOSQINDEX_KEY);
										if (n != null) {
											str = n.asText();
										}
									}
									if (!Strings.isNullOrEmpty(str)) {
										final QuartileRanking wos = QuartileRanking.valueOfCaseInsensitive(str);
										if (wos != null) {
											final JournalQualityAnnualIndicators oindicators = indicators;
											indicators = journal.setWosQIndexByYear(year, wos);
											assert oindicators == null || oindicators == indicators;
										}
									}
									Number flt = null;
									if (historyEntry.getValue() != null) {
										final JsonNode n = historyEntry.getValue().get(IMPACTFACTOR_KEY);
										if (n != null) {
											flt = Double.valueOf(n.asDouble());
										}
									}
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
								objectIdRepository.put(id, Integer.valueOf(journal.getId()));
							}
						} else {
							getLogger().info("  X " + existing.get().getJournalName() + " (id: " + existing.get().getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							if (!Strings.isNullOrEmpty(id)) {
								objectIdRepository.put(id, Integer.valueOf(existing.get().getId()));
							}
						}
						session.getTransaction().commit();
					}
				} catch (Throwable ex) {
					throw new UnableToImportJsonException(JOURNALS_SECTION, i, journalObject, ex);
				}
				++i;
			}
		}
		return nbNew;
	}

	private static int parseMonthField(JsonNode value) {
		if (value != null) {
			final String text = value.asText();
			if (!Strings.isNullOrEmpty(text)) {
				switch (text.toLowerCase()) {
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
		}
		return 0;
	}

	/** Create the publications (and additional authors) in the database.
	 *
	 * @param session the JPA session for managing transactions.
	 * @param publications the list of publications in the Json source.
	 * @param objectIdRepository the repository of the JSON elements with {@code "@id"} field.
	 * @param aliasRepository the repository of field aliases.
	 * @param fileCallback a tool that is invoked when associated file is detected. It could be {@code null}.
	 * @return the pair of numbers, never {@code null}. The first number is the number of added publication; the
	 *     second number is the is the number of added persons.
	 * @throws Exception if a membership cannot be created.
	 */
	protected Pair<Integer, Integer> insertPublications(Session session, JsonNode publications, Map<String, Integer> objectIdRepository,
			Map<String, Set<String>> aliasRepository, FileCallback fileCallback) throws Exception {
		int nbNewPublications = 0;
		final MutableInt nbNewPersons = new MutableInt();
		if (publications != null && !publications.isEmpty()) {
			getLogger().info("Retreiving the existing publications..."); //$NON-NLS-1$
			final List<Publication> allPublications = this.publicationRepository.findAll();
			getLogger().info("Inserting " + publications.size() + " publications..."); //$NON-NLS-1$ //$NON-NLS-2$
			int i = 0;
			for (JsonNode publicationObject : publications) {
				getLogger().info("> Publication " + (i + 1) + "/" + publications.size()); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					final String id = getId(publicationObject);
					final Publication publication = createPublicationInstance(id,
							publicationObject, objectIdRepository, aliasRepository);
					// Test if the publication is already inside the database
					final Publication readOnlyPublication = publication;
					final Optional<Publication> existing = allPublications.stream().filter(
							it -> this.publicationComparator.isSimilar(it, readOnlyPublication)).findAny();
					if (existing.isEmpty()) {
						session.beginTransaction();
						// Save the publication
						if (!isFake()) {
							this.publicationService.save(publication);
						}
						// Ensure that attached files are correct
						if (fileCallback != null) {
							boolean publicationChanged = false;
							if (!Strings.isNullOrEmpty(publication.getPathToDownloadablePDF())) {
								final String ofn = publication.getPathToDownloadablePDF();
								final String fn = fileCallback.publicationPdfFile(publication.getId(), ofn);
								if (!Objects.equals(ofn, fn)) {
									publication.setPathToDownloadablePDF(fn);
									publicationChanged = true;
								}
							}
							if (!Strings.isNullOrEmpty(publication.getPathToDownloadableAwardCertificate())) {
								final String ofn = publication.getPathToDownloadableAwardCertificate();
								final String fn = fileCallback.publicationAwardFile(publication.getId(), ofn);
								if (!Objects.equals(ofn, fn)) {
									publication.setPathToDownloadableAwardCertificate(fn);
									publicationChanged = true;
								}
							}
							if (publicationChanged && !isFake()) {
								this.publicationService.save(publication);
							}
						}
						++nbNewPublications;
						if (!Strings.isNullOrEmpty(id)) {
							objectIdRepository.put(id, Integer.valueOf(publication.getId()));
						}
						//
						getLogger().info("  + " + publication.getTitle() + " (id: " + publication.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

						// Attach authors
						final JsonNode authors = publicationObject.get(AUTHORS_KEY);
						if (authors == null || authors.isEmpty()) {
							throw new IllegalArgumentException("No author for publication with id: " + id); //$NON-NLS-1$
						}
						int authorRank = 0;
						final Iterator<JsonNode> iterator = authors.elements();
						while (iterator.hasNext()) {
							final JsonNode authorObject = iterator.next();
							final Person targetAuthor = findOrCreateAuthor(authorObject, objectIdRepository, nbNewPersons);
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
						session.getTransaction().commit();
					} else {
						// Publication is already in the database
						getLogger().info("  X " + existing.get().getTitle() + " (id: " + existing.get().getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						if (!Strings.isNullOrEmpty(id)) {
							objectIdRepository.put(id, Integer.valueOf(existing.get().getId()));
						}
						if (fileCallback != null) {
							if (!Strings.isNullOrEmpty(existing.get().getPathToDownloadablePDF())) {
								fileCallback.publicationPdfFile(existing.get().getId(), existing.get().getPathToDownloadablePDF());
							}
							if (!Strings.isNullOrEmpty(existing.get().getPathToDownloadableAwardCertificate())) {
								fileCallback.publicationAwardFile(existing.get().getId(), existing.get().getPathToDownloadableAwardCertificate());
							}
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

	private Publication createPublicationInstance(String id, JsonNode publicationObject, Map<String, Integer> objectIdRepository,
			Map<String, Set<String>> aliasRepository) throws Exception {
		// Retrieve the elements that characterize the type of the publication
		final PublicationType type = getEnum(publicationObject, TYPE_KEY, PublicationType.class);
		if (type == null) {
			throw new IllegalArgumentException("Missing publication type"); //$NON-NLS-1$
		}
		final Class<? extends Publication> publicationClass = type.getInstanceType();
		assert publicationClass != null;

		// Create the publication
		Publication publication = createObject(publicationClass, publicationObject,
				aliasRepository, (attrName, attrValue, attrNode) -> {
					// Keys "authors" and "journal" are not directly set. They have a specific
					// code for associating authors and journals to the publication
					return Boolean.valueOf(!AUTHORS_KEY.equalsIgnoreCase(attrName) && !JOURNAL_KEY.equalsIgnoreCase(attrName)
							&& !MONTH_KEY.equalsIgnoreCase(attrName));
				});
		if (publication == null) {
			throw new IllegalArgumentException("Unable to create the instance of the publication of type: " + publicationClass); //$NON-NLS-1$
		}

		// Attach month if it is provided
		final int month = parseMonthField(publicationObject.get(MONTH_KEY));
		if (month > 0 && month <= 12) {
			final int year = publication.getPublicationYear();
			if (year != 0) {
				final LocalDate localDate = LocalDate.of(year, month, 1);
				publication.setPublicationDate(localDate);
			}
		}

		// Attach journal if needed for the type of publication
		final Journal targetJournal;
		if (publication instanceof JournalBasedPublication) {
			final String journalId = getRef(publicationObject.get(JOURNAL_KEY));
			if (Strings.isNullOrEmpty(journalId)) {
				throw new IllegalArgumentException("Invalid journal reference for publication with id: " + id); //$NON-NLS-1$
			}
			final Integer journalDbId = objectIdRepository.get(journalId);
			if (journalDbId == null || journalDbId.intValue() == 0) {
				throw new IllegalArgumentException("Invalid journal reference for publication with id: " + id); //$NON-NLS-1$
			}
			final Optional<Journal> optJournal = this.journalRepository.findById(journalDbId);
			if (optJournal.isEmpty()) {
				throw new IllegalArgumentException("Invalid journal reference for publication with id: " + id); //$NON-NLS-1$
			}
			targetJournal = optJournal.get();
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

		return publication;
	}

	private Person findOrCreateAuthor(JsonNode authorObject, Map<String, Integer> objectIdRepository, MutableInt nbNewPersons) {
		assert authorObject != null;
		Person targetAuthor = null;
		final String authorId = getRef(authorObject);
		if (!Strings.isNullOrEmpty(authorId)) {
			final Integer personId = objectIdRepository.get(authorId);
			if (personId != null && personId.intValue() != 0) {
				targetAuthor = this.personService.getPersonById(personId.intValue());
			}
		}
		if (targetAuthor == null) {
			// The author is not a reference to a defined person
			final String authorName = authorObject.asText();
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
		}
		return targetAuthor;
	}

	/** Create the jury memberships in the database.
	 *
	 * @param session the JPA session for managing transactions.
	 * @param memberships the list of memberships in the Json source.
	 * @param objectIdRepository the mapping from JSON {@code @id} field and the JPA database identifier.
	 * @param aliasRepository the repository of field aliases.
	 * @return the number of new memberships in the database.
	 * @throws Exception if a membership cannot be created.
	 */
	protected int insertJuryMemberships(Session session, JsonNode memberships, Map<String, Integer> objectIdRepository,
			Map<String, Set<String>> aliasRepository) throws Exception {
		int nbNew = 0;
		if (memberships != null && !memberships.isEmpty()) {
			getLogger().info("Inserting " + memberships.size() + " jury memberships..."); //$NON-NLS-1$ //$NON-NLS-2$
			int i = 0;
			for (JsonNode membershipObject : memberships) {
				getLogger().info("> Jury membership " + (i + 1) + "/" + memberships.size()); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					final String id = getId(membershipObject);
					JuryMembership membership = createObject(JuryMembership.class, membershipObject, aliasRepository, null);
					if (membership != null) {
						session.beginTransaction();
						// Person
						final String personId = getRef(membershipObject.get(PERSON_KEY));
						if (Strings.isNullOrEmpty(personId)) {
							throw new IllegalArgumentException("Invalid person reference for jury membership with id: " + id); //$NON-NLS-1$
						}
						final Integer personDbId = objectIdRepository.get(personId);
						if (personDbId == null || personDbId.intValue() == 0) {
							throw new IllegalArgumentException("Invalid person reference for jury membership with id: " + id); //$NON-NLS-1$
						}
						final Optional<Person> targetPerson = this.personRepository.findById(personDbId);
						if (targetPerson.isEmpty()) {
							throw new IllegalArgumentException("Invalid person reference for jury membership with id: " + id); //$NON-NLS-1$
						}
						membership.setPerson(targetPerson.get());
						// Candidate
						final String candidateId = getRef(membershipObject.get(CANDIDATE_KEY));
						if (Strings.isNullOrEmpty(candidateId)) {
							throw new IllegalArgumentException("Invalid candidate reference for jury membership with id: " + id); //$NON-NLS-1$
						}
						final Integer candidateDbId = objectIdRepository.get(candidateId);
						if (candidateDbId == null || candidateDbId.intValue() == 0) {
							throw new IllegalArgumentException("Invalid candidate reference for jury membership with id: " + id); //$NON-NLS-1$
						}
						final Optional<Person> targetCandidate = this.personRepository.findById(candidateDbId);
						if (targetCandidate.isEmpty()) {
							throw new IllegalArgumentException("Invalid candidate reference for jury membership with id: " + id); //$NON-NLS-1$
						}
						membership.setCandidate(targetCandidate.get());
						// Promoters
						final JsonNode promotersNode = membershipObject.get(PROMOTERS_KEY);
						if (promotersNode != null && promotersNode.isArray()) {
							final List<Person> promoters = new ArrayList<>();
							for (final JsonNode promoterNode : promotersNode) {
								final String promoterId = getRef(promoterNode);
								if (Strings.isNullOrEmpty(promoterId)) {
									throw new IllegalArgumentException("Invalid promoter reference for jury membership with id: " + id); //$NON-NLS-1$
								}
								final Integer promoterDbId = objectIdRepository.get(promoterId);
								if (promoterDbId == null || promoterDbId.intValue() == 0) {
									throw new IllegalArgumentException("Invalid promoter reference for jury membership with id: " + id); //$NON-NLS-1$
								}
								final Optional<Person> targetPromoter = this.personRepository.findById(promoterDbId);
								if (targetPromoter.isEmpty()) {
									throw new IllegalArgumentException("Invalid promoter reference for jury membership with id: " + id); //$NON-NLS-1$
								}
								promoters.add(targetPromoter.get());
							}
							membership.setPromoters(promoters);
						}
						final Optional<JuryMembership> existing = this.juryMembershipRepository.findByPersonIdAndCandidateIdAndType(
								targetPerson.get().getId(), targetCandidate.get().getId(), membership.getType());
						if (existing.isEmpty()) {
							if (!isFake()) {
								membership = this.juryMembershipRepository.save(membership);
							}
							++nbNew;
							getLogger().info("  + " + targetPerson.get().getFullName() //$NON-NLS-1$
									+ " - " + targetCandidate.get().getFullName() //$NON-NLS-1$
									+ " - " + membership.getType().getLabel(Gender.NOT_SPECIFIED) //$NON-NLS-1$
									+ " (id: " + membership.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
							if (!Strings.isNullOrEmpty(id)) {
								objectIdRepository.put(id, Integer.valueOf(membership.getId()));
							}
						} else {
							getLogger().info("  X " + targetPerson.get().getFullName() //$NON-NLS-1$
									+ " - " + targetCandidate.get().getFullName() //$NON-NLS-1$
									+ " - " + membership.getType().getLabel(Gender.NOT_SPECIFIED) //$NON-NLS-1$
									+ " (id: " + membership.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
							if (!Strings.isNullOrEmpty(id)) {
								objectIdRepository.put(id, Integer.valueOf(existing.get().getId()));
							}
						}
						session.getTransaction().commit();
					}
				} catch (Throwable ex) {
					throw new UnableToImportJsonException(JURY_MEMBERSHIPS_SECTION, i, membershipObject, ex);
				}
				++i;
			}
		}
		return nbNew;
	}

	/** Create the supervisions in the database.
	 *
	 * @param session the JPA session for managing transactions.
	 * @param supervisions the list of supervisions in the Json source.
	 * @param objectIdRepository the mapping from JSON {@code @id} field and the JPA database identifier.
	 * @param aliasRepository the repository of field aliases.
	 * @return the number of new supervisions in the database.
	 * @throws Exception if a supervision cannot be created.
	 */
	protected int insertSupervisions(Session session, JsonNode supervisions, Map<String, Integer> objectIdRepository,
			Map<String, Set<String>> aliasRepository) throws Exception {
		int nbNew = 0;
		if (supervisions != null && !supervisions.isEmpty()) {
			getLogger().info("Inserting " + supervisions.size() + " supervisions..."); //$NON-NLS-1$ //$NON-NLS-2$
			int i = 0;
			for (JsonNode supervisionObject : supervisions) {
				getLogger().info("> Supervision " + (i + 1) + "/" + supervisions.size()); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					final String id = getId(supervisionObject);
					Supervision supervision = createObject(Supervision.class, supervisionObject, aliasRepository, null);
					if (supervision != null) {
						session.beginTransaction();
						// Supervised Person
						final String mbrId = getRef(supervisionObject.get(PERSON_KEY));
						if (Strings.isNullOrEmpty(mbrId)) {
							throw new IllegalArgumentException("Invalid membership reference for supervision with id: " + id); //$NON-NLS-1$
						}
						final Integer mbrDbId = objectIdRepository.get(mbrId);
						if (mbrDbId == null || mbrDbId.intValue() == 0) {
							throw new IllegalArgumentException("Invalid membership reference for supervision with id: " + id); //$NON-NLS-1$
						}
						final Optional<Membership> targetMembership = this.organizationMembershipRepository.findById(mbrDbId);
						if (targetMembership.isEmpty()) {
							throw new IllegalArgumentException("Invalid membership reference for supervision with id: " + id); //$NON-NLS-1$
						}
						supervision.setSupervisedPerson(targetMembership.get());
						if (!isFake()) {
							this.supervisionRepository.save(supervision);
						}
						// Directors
						final JsonNode supervisorsNode = supervisionObject.get(SUPERVISORS_KEY);
						if (supervisorsNode != null && supervisorsNode.isArray()) {
							final List<Supervisor> supervisors = new ArrayList<>();
							for (final JsonNode supervisorNode : supervisorsNode) {
								final Supervisor supervisorObj = new Supervisor();
								final String supervisorId = getRef(supervisorNode.get(PERSON_KEY));
								if (Strings.isNullOrEmpty(supervisorId)) {
									throw new IllegalArgumentException("Invalid supervisor reference for supervision with id: " + id); //$NON-NLS-1$
								}
								final Integer supervisorDbId = objectIdRepository.get(supervisorId);
								if (supervisorDbId == null || supervisorDbId.intValue() == 0) {
									throw new IllegalArgumentException("Invalid supervisor reference for supervision with id: " + id); //$NON-NLS-1$
								}
								final Optional<Person> targetSupervisor = this.personRepository.findById(supervisorDbId);
								if (targetSupervisor.isEmpty()) {
									throw new IllegalArgumentException("Invalid supervisor reference for jury membership with id: " + id); //$NON-NLS-1$
								}
								supervisorObj.setSupervisor(targetSupervisor.get());
								supervisorObj.setType(getEnum(supervisorNode, TYPE_KEY, SupervisorType.class));
								supervisorObj.setPercentage(getInt(supervisorNode, PERCENT_KEY, 0));
								supervisors.add(supervisorObj);
							}
							supervision.setSupervisors(supervisors);
							if (!isFake()) {
								this.supervisionRepository.save(supervision);
							}
						}
						++nbNew;
						getLogger().info("  + " + supervision.getSupervisedPerson().getPerson().getFullName() //$NON-NLS-1$
								+ " - " + supervision.getSupervisedPerson().getShortDescription() //$NON-NLS-1$
								+ " (id: " + supervision.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
						if (!Strings.isNullOrEmpty(id)) {
							objectIdRepository.put(id, Integer.valueOf(supervision.getId()));
						}
						session.getTransaction().commit();
					}
				} catch (Throwable ex) {
					throw new UnableToImportJsonException(SUPERVISIONS_SECTION, i, supervisionObject, ex);
				}
				++i;
			}
		}
		return nbNew;
	}

	/** Create the invitations in the database.
	 *
	 * @param session the JPA session for managing transactions.
	 * @param invitations the list of invitations in the Json source.
	 * @param objectIdRepository the mapping from JSON {@code @id} field and the JPA database identifier.
	 * @param aliasRepository the repository of field aliases.
	 * @return the number of new supervisions in the database.
	 * @throws Exception if a supervision cannot be created.
	 */
	protected int insertInvitations(Session session, JsonNode invitations, Map<String, Integer> objectIdRepository,
			Map<String, Set<String>> aliasRepository) throws Exception {
		int nbNew = 0;
		if (invitations != null && !invitations.isEmpty()) {
			getLogger().info("Inserting " + invitations.size() + " invitations..."); //$NON-NLS-1$ //$NON-NLS-2$
			int i = 0;
			for (JsonNode invitationObject : invitations) {
				getLogger().info("> Invitation " + (i + 1) + "/" + invitations.size()); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					final String id = getId(invitationObject);
					PersonInvitation invitation = createObject(PersonInvitation.class, invitationObject, aliasRepository, null);
					if (invitation != null) {
						session.beginTransaction();
						// Guest
						final String guestId = getRef(invitationObject.get(GUEST_KEY));
						if (Strings.isNullOrEmpty(guestId)) {
							throw new IllegalArgumentException("Invalid guest reference for invitation with id: " + id); //$NON-NLS-1$
						}
						final Integer guestDbId = objectIdRepository.get(guestId);
						if (guestDbId == null || guestDbId.intValue() == 0) {
							throw new IllegalArgumentException("Invalid guest reference for invitation with id: " + id); //$NON-NLS-1$
						}
						final Optional<Person> targetGuest = this.personRepository.findById(guestDbId);
						if (targetGuest.isEmpty()) {
							throw new IllegalArgumentException("Invalid guest reference for invitation with id: " + id); //$NON-NLS-1$
						}
						invitation.setGuest(targetGuest.get());
						// Inviter
						final String inviterId = getRef(invitationObject.get(INVITER_KEY));
						if (Strings.isNullOrEmpty(inviterId)) {
							throw new IllegalArgumentException("Invalid inviter reference for invitation with id: " + id); //$NON-NLS-1$
						}
						final Integer inviterDbId = objectIdRepository.get(inviterId);
						if (inviterDbId == null || inviterDbId.intValue() == 0) {
							throw new IllegalArgumentException("Invalid inviter reference for invitation with id: " + id); //$NON-NLS-1$
						}
						final Optional<Person> targetInviter = this.personRepository.findById(inviterDbId);
						if (targetInviter.isEmpty()) {
							throw new IllegalArgumentException("Invalid inviter reference for invitation with id: " + id); //$NON-NLS-1$
						}
						invitation.setInviter(targetInviter.get());
						//
						if (!isFake()) {
							this.invitationRepository.save(invitation);
						}
						++nbNew;
						getLogger().info("  + " + invitation.getGuest().getFullName() //$NON-NLS-1$
								+ " - " + invitation.getInviter().getFullName() //$NON-NLS-1$
								+ " (id: " + invitation.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
						if (!Strings.isNullOrEmpty(id)) {
							objectIdRepository.put(id, Integer.valueOf(invitation.getId()));
						}
						session.getTransaction().commit();
					}
				} catch (Throwable ex) {
					throw new UnableToImportJsonException(INVITATIONS_SECTION, i, invitationObject, ex);
				}
				++i;
			}
		}
		return nbNew;
	}

	/** Create the projects in the database.
	 *
	 * @param session the JPA session for managing transactions.
	 * @param projects the list of projects in the Json source.
	 * @param objectIdRepository the mapping from JSON {@code @id} field and the JPA database identifier.
	 * @param aliasRepository the repository of field aliases.
	 * @param fileCallback a tool that is invoked when associated file is detected. It could be {@code null}.
	 * @return the number of new projects in the database.
	 * @throws Exception if a project cannot be created.
	 */
	protected int insertProjects(Session session, JsonNode projects, Map<String, Integer> objectIdRepository,
			Map<String, Set<String>> aliasRepository, FileCallback fileCallback) throws Exception {
		int nbNew = 0;
		if (projects != null && !projects.isEmpty()) {
			getLogger().info("Inserting " + projects.size() + " projects..."); //$NON-NLS-1$ //$NON-NLS-2$
			int i = 0;
			for (JsonNode projectObject : projects) {
				getLogger().info("> Project " + (i + 1) + "/" + projects.size()); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					final String id = getId(projectObject);
					Project project = createObject(Project.class, projectObject, aliasRepository, null);
					if (project != null) {
						session.beginTransaction();

						// Budgets
						final JsonNode budgetsNode = projectObject.get(BUDGETS_KEY);
						if (budgetsNode != null) {
							final List<ProjectBudget> budgetList = new ArrayList<>();
							budgetsNode.forEach(it -> {
								final ProjectBudget budgetObject = new ProjectBudget();
								final String fundingValue = getStringValue(it.get(FUNDING_KEY));
								if (fundingValue != null) {
									try {
										final FundingScheme scheme = FundingScheme.valueOfCaseInsensitive(fundingValue.toString());
										budgetObject.setFundingScheme(scheme);
									} catch (Throwable ex) {
										budgetObject.setFundingScheme(FundingScheme.NOT_FUNDED);
									}
								}
								final Number budgetValue = getNumberValue(it.get(BUDGET_KEY));
								if (budgetValue != null) {
									budgetObject.setBudget(budgetValue.floatValue());
								}
								final String grantValue = getStringValue(it.get(GRANT_KEY));
								if (grantValue != null) {
									budgetObject.setGrant(grantValue);
								}
								budgetList.add(budgetObject);
							});
							project.setBudgets(budgetList);
						}

						if (!isFake()) {
							this.projectRepository.save(project);
						}

						// Video URLs
						final List<String> videoURLs = getStringList(projectObject, VIDEO_URLS_KEY);
						project.setVideoURLs(videoURLs);

						// Paths to images
						final List<String> pathsToImages = getStringList(projectObject, PATHS_TO_IMAGES_KEY);
						project.setPathsToImages(pathsToImages);

						if (!isFake()) {
							this.projectRepository.save(project);
						}

						// Ensure that attached files are correct
						if (fileCallback != null) {
							boolean projectChanged = false;
							if (!Strings.isNullOrEmpty(project.getPathToLogo())) {
								final String ofn = project.getPathToLogo();
								final String fn = fileCallback.projectLogoFile(project.getId(), ofn);
								if (!Objects.equals(ofn, fn)) {
									project.setPathToLogo(fn);
									projectChanged = true;
								}
							}
							if (!Strings.isNullOrEmpty(project.getPathToPowerpoint())) {
								final String ofn = project.getPathToPowerpoint();
								final String fn = fileCallback.projectPowerpointFile(project.getId(), ofn);
								if (!Objects.equals(ofn, fn)) {
									project.setPathToPowerpoint(fn);
									projectChanged = true;
								}
							}
							if (!Strings.isNullOrEmpty(project.getPathToPressDocument())) {
								final String ofn = project.getPathToPressDocument();
								final String fn = fileCallback.projectPressDocumentFile(project.getId(), ofn);
								if (!Objects.equals(ofn, fn)) {
									project.setPathToPressDocument(fn);
									projectChanged = true;
								}
							}
							if (!Strings.isNullOrEmpty(project.getPathToScientificRequirements())) {
								final String ofn = project.getPathToScientificRequirements();
								final String fn = fileCallback.projectScientificRequirementsFile(project.getId(), ofn);
								if (!Objects.equals(ofn, fn)) {
									project.setPathToScientificRequirements(fn);
									projectChanged = true;
								}
							}
							if (!project.getPathsToImages().isEmpty()) {
								final List<String> newPaths = new ArrayList<>();
								int imageIndex = 0;
								for (final String path : project.getPathsToImages()) {
									final String fn = fileCallback.projectImageFile(project.getId(), imageIndex, path);
									if (!Objects.equals(path, fn)) {
										newPaths.add(fn);
										projectChanged = true;
									} else {
										newPaths.add(path);
									}
									++imageIndex;
								}
								project.setPathsToImages(newPaths);
							}
							if (projectChanged && !isFake()) {
								this.projectRepository.save(project);
							}
						}
						
						// Coordinator
						final String coordinatorId = getRef(projectObject.get(COORDINATOR_KEY));
						if (Strings.isNullOrEmpty(coordinatorId)) {
							throw new IllegalArgumentException("Invalid coordinator reference for project with id: " + id); //$NON-NLS-1$
						}
						final Integer coordinatorDbId = objectIdRepository.get(coordinatorId);
						if (coordinatorDbId == null || coordinatorDbId.intValue() == 0) {
							throw new IllegalArgumentException("Invalid coordinator reference for project with id: " + id); //$NON-NLS-1$
						}
						final Optional<ResearchOrganization> targetCoordinator = this.organizationRepository.findById(coordinatorDbId);
						if (targetCoordinator.isEmpty()) {
							throw new IllegalArgumentException("Invalid coordinator reference for project with id: " + id); //$NON-NLS-1$
						}
						project.setCoordinator(targetCoordinator.get());
						if (!isFake()) {
							this.projectRepository.save(project);
						}

						// Local organization
						final String localOrganizationId = getRef(projectObject.get(LOCAL_ORGANIZATION_KEY));
						if (Strings.isNullOrEmpty(localOrganizationId)) {
							throw new IllegalArgumentException("Invalid local organization reference for project with id: " + id); //$NON-NLS-1$
						}
						final Integer localOrganizationDbId = objectIdRepository.get(localOrganizationId);
						if (localOrganizationDbId == null || localOrganizationDbId.intValue() == 0) {
							throw new IllegalArgumentException("Invalid local organization reference for project with id: " + id); //$NON-NLS-1$
						}
						final Optional<ResearchOrganization> targetLocalOrganization = this.organizationRepository.findById(localOrganizationDbId);
						if (targetLocalOrganization.isEmpty()) {
							throw new IllegalArgumentException("Invalid local organization reference for project with id: " + id); //$NON-NLS-1$
						}
						project.setLocalOrganization(targetLocalOrganization.get());
						if (!isFake()) {
							this.projectRepository.save(project);
						}

						// Super organization
						final String superOrganizationId = getRef(projectObject.get(SUPER_ORGANIZATION_KEY));
						if (Strings.isNullOrEmpty(superOrganizationId)) {
							throw new IllegalArgumentException("Invalid super organization reference for project with id: " + id); //$NON-NLS-1$
						}
						final Integer superOrganizationDbId = objectIdRepository.get(superOrganizationId);
						if (superOrganizationDbId == null || superOrganizationDbId.intValue() == 0) {
							throw new IllegalArgumentException("Invalid super organization reference for project with id: " + id); //$NON-NLS-1$
						}
						final Optional<ResearchOrganization> targetSuperOrganization = this.organizationRepository.findById(superOrganizationDbId);
						if (targetSuperOrganization.isEmpty()) {
							throw new IllegalArgumentException("Invalid super organization reference for project with id: " + id); //$NON-NLS-1$
						}
						project.setSuperOrganization(targetSuperOrganization.get());
						if (!isFake()) {
							this.projectRepository.save(project);
						}

						// LEAR organization
						final String learOrganizationId = getRef(projectObject.get(LEAR_ORGANIZATION_KEY));
						if (Strings.isNullOrEmpty(learOrganizationId)) {
							throw new IllegalArgumentException("Invalid LEAR organization reference for project with id: " + id); //$NON-NLS-1$
						}
						final Integer learOrganizationDbId = objectIdRepository.get(learOrganizationId);
						if (learOrganizationDbId == null || learOrganizationDbId.intValue() == 0) {
							throw new IllegalArgumentException("Invalid LEAR organization reference for project with id: " + id); //$NON-NLS-1$
						}
						final Optional<ResearchOrganization> targetLearOrganization = this.organizationRepository.findById(learOrganizationDbId);
						if (targetLearOrganization.isEmpty()) {
							throw new IllegalArgumentException("Invalid LEAR organization reference for project with id: " + id); //$NON-NLS-1$
						}
						project.setLearOrganization(targetLearOrganization.get());
						if (!isFake()) {
							this.projectRepository.save(project);
						}

						// Other partners
						final Set<ResearchOrganization> otherPartners = new HashSet<>();
						final JsonNode otherPartnersNode = projectObject.get(OTHER_PARTNERS_KEY);
						if (otherPartnersNode != null) {
							otherPartnersNode.forEach(otherPartnerNode -> {
								final String partnerId = getRef(otherPartnerNode);
								if (Strings.isNullOrEmpty(partnerId)) {
									throw new IllegalArgumentException("Invalid partner reference for project with id: " + id); //$NON-NLS-1$
								}
								final Integer partnerDbId = objectIdRepository.get(partnerId);
								if (partnerDbId == null || partnerDbId.intValue() == 0) {
									throw new IllegalArgumentException("Invalid partner reference for project with id: " + id); //$NON-NLS-1$
								}
								final Optional<ResearchOrganization> targetPartnerOrganization = this.organizationRepository.findById(partnerDbId);
								if (targetPartnerOrganization.isEmpty()) {
									throw new IllegalArgumentException("Invalid partner reference for project with id: " + id); //$NON-NLS-1$
								}
								otherPartners.add(targetPartnerOrganization.get());
							});
						}
						project.setOtherPartners(otherPartners);
						if (!isFake()) {
							this.projectRepository.save(project);
						}

						// Participants
						final List<ProjectMember> participants = new ArrayList<>();
						final JsonNode participantsNode = projectObject.get(PARTICIPANTS_KEY);
						if (participantsNode != null) {
							participantsNode.forEach(participantNode -> {
								final String participantId = getRef(participantNode.get(PERSON_KEY));
								if (Strings.isNullOrEmpty(participantId)) {
									throw new IllegalArgumentException("Invalid parcticipant reference for project with id: " + id); //$NON-NLS-1$
								}
								final Integer participantDbId = objectIdRepository.get(participantId);
								if (participantDbId == null || participantDbId.intValue() == 0) {
									throw new IllegalArgumentException("Invalid participant reference for project with id: " + id); //$NON-NLS-1$
								}
								final Optional<Person> targetParticipant = this.personRepository.findById(participantDbId);
								if (targetParticipant.isEmpty()) {
									throw new IllegalArgumentException("Invalid participant reference for project with id: " + id); //$NON-NLS-1$
								}
								final Role role = Role.valueOfCaseInsensitive(participantNode.get(ROLE_KEY).asText());
								final ProjectMember member = new ProjectMember();
								member.setPerson(targetParticipant.get());
								member.setRole(role);
								participants.add(member);
							});
						}
						project.setParticipants(participants);
						if (!isFake()) {
							this.projectRepository.save(project);
						}

						++nbNew;
						getLogger().info("  + " + project.getAcronymOrScientificTitle() //$NON-NLS-1$
								+ " (id: " + project.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
						if (!Strings.isNullOrEmpty(id)) {
							objectIdRepository.put(id, Integer.valueOf(project.getId()));
						}
						session.getTransaction().commit();
					}
				} catch (Throwable ex) {
					throw new UnableToImportJsonException(PROJECTS_SECTION, i, projectObject, ex);
				}
				++i;
			}
		}
		return nbNew;
	}

	private static  List<String> getStringList(JsonNode node, String fieldName) {
		final List<String> content = new ArrayList<>();
		if (node != null && !Strings.isNullOrEmpty(fieldName)) {
			final JsonNode fieldNode = node.get(fieldName);
			if (fieldNode != null && fieldNode.isArray()) {
				fieldNode.forEach(it -> {
					content.add(it.asText());
				});
			}
		}
		return content;
	}

	/** Create the associated structures in the database.
	 *
	 * @param session the JPA session for managing transactions.
	 * @param structures the list of associated structures in the Json source.
	 * @param objectIdRepository the mapping from JSON {@code @id} field and the JPA database identifier.
	 * @param aliasRepository the repository of field aliases.
	 * @param fileCallback a tool that is invoked when associated file is detected. It could be {@code null}.
	 * @return the number of new associated structures in the database.
	 * @throws Exception if an associated structure cannot be created.
	 */
	protected int insertAssociatedStructures(Session session, JsonNode structures, Map<String, Integer> objectIdRepository,
			Map<String, Set<String>> aliasRepository, FileCallback fileCallback) throws Exception {
		int nbNew = 0;
		if (structures != null && !structures.isEmpty()) {
			getLogger().info("Inserting " + structures.size() + " associated structures..."); //$NON-NLS-1$ //$NON-NLS-2$
			int i = 0;
			for (JsonNode structureObject : structures) {
				getLogger().info("> Associated Structure " + (i + 1) + "/" + structures.size()); //$NON-NLS-1$ //$NON-NLS-2$
				try {
					final String id = getId(structureObject);
					AssociatedStructure structure = createObject(AssociatedStructure.class, structureObject, aliasRepository, null);
					if (structure != null) {
						session.beginTransaction();

						final String fundingOrganizationId = getRef(structureObject.get(FUNDING_KEY));
						if (Strings.isNullOrEmpty(fundingOrganizationId)) {
							throw new IllegalArgumentException("Invalid funding organization reference for associated structure with id: " + id); //$NON-NLS-1$
						}
						final Integer fundingOrganizationDbId = objectIdRepository.get(fundingOrganizationId);
						if (fundingOrganizationDbId == null || fundingOrganizationDbId.intValue() == 0) {
							throw new IllegalArgumentException("Invalid funding organization reference for associated structure with id: " + id); //$NON-NLS-1$
						}
						final Optional<ResearchOrganization> fundingOrganization = this.organizationRepository.findById(fundingOrganizationDbId);
						if (fundingOrganization.isEmpty()) {
							throw new IllegalArgumentException("Invalid funding organization reference for associated structure with id: " + id); //$NON-NLS-1$
						}
						structure.setFundingOrganization(fundingOrganization.get());
						if (!isFake()) {
							this.structureRepository.save(structure);
						}

						final List<AssociatedStructureHolder> holders = new ArrayList<>();
						final JsonNode holdersNode = structureObject.get(HOLDERS_KEY);
						if (holdersNode != null) {
							holdersNode.forEach(holderNode -> {
								final AssociatedStructureHolder holderObj = new AssociatedStructureHolder();
								// Person
								final String personId = getRef(holderNode.get(PERSON_KEY));
								if (Strings.isNullOrEmpty(personId)) {
									throw new IllegalArgumentException("Invalid holding person reference for associated structure with id: " + id); //$NON-NLS-1$
								}
								final Integer personDbId = objectIdRepository.get(personId);
								if (personDbId == null || personDbId.intValue() == 0) {
									throw new IllegalArgumentException("Invalid holding person reference for associated structure with id: " + id); //$NON-NLS-1$
								}
								final Optional<Person> person = this.personRepository.findById(personDbId);
								if (person.isEmpty()) {
									throw new IllegalArgumentException("Invalid holding person reference for associated structure with id: " + id); //$NON-NLS-1$
								}
								holderObj.setPerson(person.get());
								// Role
								final HolderRole role = getEnum(holderNode, ROLE_KEY, HolderRole.class);
								if (role == null) {
									throw new IllegalArgumentException("Invalid holder role for associated structure with id: " + id); //$NON-NLS-1$
								}
								holderObj.setRole(role);
								// Role description
								final String roleDescription = getStringValue(holderNode.get(ROLE_DESCRIPTION_KEY));
								if (!Strings.isNullOrEmpty(roleDescription)) {
									holderObj.setRoleDescription(roleDescription);
								}
								// Organization
								final String organizationId = getRef(holderNode.get(ORGANIZATION_KEY));
								if (Strings.isNullOrEmpty(organizationId)) {
									throw new IllegalArgumentException("Invalid holder organization reference for associated structure with id: " + id); //$NON-NLS-1$
								}
								final Integer organizationDbId = objectIdRepository.get(organizationId);
								if (organizationDbId == null || organizationDbId.intValue() == 0) {
									throw new IllegalArgumentException("Invalid holder organization reference for associated structure with id: " + id); //$NON-NLS-1$
								}
								final Optional<ResearchOrganization> organization = this.organizationRepository.findById(organizationDbId);
								if (organization.isEmpty()) {
									throw new IllegalArgumentException("Invalid holder organization reference for associated structure with id: " + id); //$NON-NLS-1$
								}
								holderObj.setOrganization(organization.get());
								// Super organization
								final String superOrganizationId = getRef(holderNode.get(SUPER_ORGANIZATION_KEY));
								if (!Strings.isNullOrEmpty(superOrganizationId)) {
									final Integer superOrganizationDbId = objectIdRepository.get(superOrganizationId);
									if (superOrganizationDbId != null && superOrganizationDbId.intValue() != 0) {
										final Optional<ResearchOrganization> superOrganization = this.organizationRepository.findById(superOrganizationDbId);
										if (superOrganization.isPresent()) {
											holderObj.setSuperOrganization(superOrganization.get());
										}
									}
								}
								holders.add(holderObj);
							});
						}
						structure.setHolders(holders);
						if (!isFake()) {
							this.structureRepository.save(structure);
						}

						final List<Project> projects = new ArrayList<>();
						final JsonNode projectsNode = structureObject.get(PROJECTS_KEY);
						if (projectsNode != null) {
							projectsNode.forEach(projectNode -> {
								final String projectId = getRef(projectNode);
								if (Strings.isNullOrEmpty(projectId)) {
									throw new IllegalArgumentException("Invalid project reference for associated structure with id: " + id); //$NON-NLS-1$
								}
								final Integer projectDbId = objectIdRepository.get(projectId);
								if (projectDbId == null || projectDbId.intValue() == 0) {
									throw new IllegalArgumentException("Invalid project reference for associated structure with id: " + id); //$NON-NLS-1$
								}
								final Optional<Project> project = this.projectRepository.findById(projectDbId);
								if (project.isEmpty()) {
									throw new IllegalArgumentException("Invalid project reference for associated structure with id: " + id); //$NON-NLS-1$
								}
								projects.add(project.get());
							});
						}
						structure.setProjects(projects);
						if (!isFake()) {
							this.structureRepository.save(structure);
						}

						++nbNew;
						getLogger().info("  + " + structure.getAcronymOrName() //$NON-NLS-1$
								+ " (id: " + structure.getId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
						if (!Strings.isNullOrEmpty(id)) {
							objectIdRepository.put(id, Integer.valueOf(structure.getId()));
						}
						session.getTransaction().commit();
					}
				} catch (Throwable ex) {
					throw new UnableToImportJsonException(ASSOCIATED_STRUCTURES_SECTION, i, structureObject, ex);
				}
				++i;
			}
		}
		return nbNew;
	}

	/** Callback for files that are associated to elements from the JSON.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 2.0.0
	 */
	public interface FileCallback {

		/** A PDF file was attached to a publication.
		 *
		 * @param dbId the identifier of the publication in the database.
		 * @param filename the filename that is specified in the JSON file.
		 * @return the fixed filename.
		 */
		String publicationPdfFile(int dbId, String filename);

		/** An award file was attached to a publication.
		 *
		 * @param dbId the identifier of the publication in the database.
		 * @param filename the filename that is specified in the JSON file.
		 * @return the fixed filename.
		 */
		String publicationAwardFile(int dbId, String filename);

		/** A background image file was attached to an address.
		 *
		 * @param dbId the identifier of the address in the database.
		 * @param filename the filename that is specified in the JSON file.
		 * @return the fixed filename.
		 */
		String addressBackgroundImageFile(int dbId, String filename);

		/** A logo file was attached to a project.
		 *
		 * @param dbId the identifier of the project in the database.
		 * @param filename the filename that is specified in the JSON file.
		 * @return the fixed filename.
		 * @since 3.0
		 */
		String projectLogoFile(int dbId, String filename);

		/** An image file was attached to a project.
		 *
		 * @param dbId the identifier of the project in the database.
		 * @param index the index of the image in the project.
		 * @param filename the filename that is specified in the JSON file.
		 * @return the fixed filename.
		 * @since 3.0
		 */
		String projectImageFile(int dbId, int index, String filename);

		/** A scientific requirement file was attached to a project.
		 *
		 * @param dbId the identifier of the project in the database.
		 * @param filename the filename that is specified in the JSON file.
		 * @return the fixed filename.
		 * @since 3.0
		 */
		String projectScientificRequirementsFile(int dbId, String filename);

		/** A press document file was attached to a project.
		 *
		 * @param dbId the identifier of the project in the database.
		 * @param filename the filename that is specified in the JSON file.
		 * @return the fixed filename.
		 * @since 3.0
		 */
		String projectPressDocumentFile(int dbId, String filename);

		/** A PowerPoint file was attached to a project.
		 *
		 * @param dbId the identifier of the project in the database.
		 * @param filename the filename that is specified in the JSON file.
		 * @return the fixed filename.
		 * @since 3.0
		 */
		String projectPowerpointFile(int dbId, String filename);

	}

	/** Stats on the import.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 2.0.0
	 */
	public static class Stats {

		/** Number of created addresses.
		 */
		public final int addresses;

		/** Number of created organizations.
		 */
		public final int organizations;

		/** Number of created journals.
		 */
		public final int journals;

		/** Number of created persons.
		 */
		public final int persons;

		/** Number of created authors.
		 */
		public final int authors;

		/** Number of created organization memberships.
		 */
		public final int organizationMemberships;

		/** Number of created publications.
		 */
		public final int publications;

		/** Number of created jury memberships.
		 */
		public final int juryMemberships;

		/** Number of created supervisions.
		 */
		public final int supervisions;

		/** Number of created invitations.
		 */
		public final int invitations;

		/** Number of created projects.
		 * 
		 * @since 3.0
		 */
		public final int projects;

		/** Number of created associated structures.
		 * 
		 * @since 3.2
		 */
		public final int associatedStructures;

		/** Constructor.
		 *
		 * @param addresses the number of created addresses.
		 * @param organizations the number of created organizations.
		 * @param journals the number of created journals.
		 * @param persons the number of created persons.
		 * @param authors the number of created authors.
		 * @param memberships the number of created organization memberships.
		 * @param publications the number of created publications.
		 * @param juryMemberships the number of created jury memberships.
		 * @param supervisions the number of supervisions.
		 * @param invitations the number of invitations.
		 * @param projects the number of projects.
		 * @param associatedStructures the number of associated structures.
		 */
		Stats(int addresses, int organizations, int journals, int persons, int authors, int memberships, int publications,
				int juryMemberships, int supervisions, int invitations, int projects, int associatedStructures) {
			this.addresses = addresses;
			this.organizations = organizations;
			this.journals = journals;
			this.persons = persons;
			this.authors = authors;
			this.organizationMemberships = memberships;
			this.publications = publications;
			this.juryMemberships = juryMemberships;
			this.supervisions = supervisions;
			this.invitations = invitations;
			this.projects = projects;
			this.associatedStructures = associatedStructures;
		}

		/** Constructor.
		 */
		Stats() {
			this(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		}

	}

}