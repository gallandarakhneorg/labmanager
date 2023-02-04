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

package fr.ciadlab.labmanager.entities.project;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.AttributeProvider;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.IdentifiableEntity;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationComparator;
import fr.ciadlab.labmanager.io.json.JsonUtils;
import fr.ciadlab.labmanager.io.json.JsonUtils.CachedGenerator;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import fr.ciadlab.labmanager.utils.funding.FundingScheme;
import fr.ciadlab.labmanager.utils.trl.TRL;

/** Representation of a research project that may be with academic or not-academic partners.
 * 
 * @author $Author: bpdj$
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.0
 */
@Entity
@Table(name = "Projects")
public class Project implements Serializable, JsonSerializable, Comparable<Project>, AttributeProvider, IdentifiableEntity {

	private static final long serialVersionUID = 69671923802965957L;

	/**
	 * Identifier of the project
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	/** Title of the project with highlight on scientific contribution.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String scientificTitle;

	/** Acronym of the project.
	 */
	@Column
	private String acronym;

	/** Start date of the project.
	 */
	@Column
	private LocalDate startDate;

	/** Duration of a project in months.
	 */
	@Column
	private int duration = 1;

	/** Short description of the project.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String description;

	/** Global budget for the entire the project, including all the partners.
	 */
	@Column
	private float globalBudget;

	@Transient
	private Float totalLocalOrganizationBudget;

	/** List of the local budgets for the budget.
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ProjectBudget> budgets;

	/** Type of the project activity.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private ProjectActivityType activityType;

	/** Type of the project contract.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private ProjectContractType contractType = ProjectContractType.NOT_SPECIFIED;

	/** Status of the project.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private ProjectStatus status;

	/** Expected TRL for the project.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private TRL trl; 

	/** URLs of the associated videos if the project has one.
	 */
	@ElementCollection
	@CollectionTable(name = "ProjectVideos", joinColumns = @JoinColumn(name="project_id"))
	@Column(name = "videos_urls")
	private List<String> videoURLs;

	/** Website URL of the project if it has one.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String projectURL;

	/** Name of the logo of the project if it has one.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String pathToLogo;

	/** Names of the images of the project if it has one.
	 */
	@ElementCollection
	@CollectionTable(name = "ProjectImages", joinColumns = @JoinColumn(name="project_id"))
	@Column(name = "paths_to_images")
	private List<String> pathsToImages;

	/** Name of the powerpoint of the project if it has one.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String pathToPowerpoint;

	/** Name of the scientific requirements of the project if it has one.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String pathToScientificRequirements;

	/** Name of the press document of the project if it has one.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String pathToPressDocument;

	/** If the project is confidential or not.
	 */
	@Column
	private boolean confidential;

	/** If the project is open source or not.
	 */
	@Column
	private boolean openSource;

	/** If the project is validated by a local authority.
	 */
	@Column
	private boolean validated;

	/** Naming convention for the webpage of the project.
	 */
	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private ProjectWebPageNaming webPageNaming = ProjectWebPageNaming.UNSPECIFIED;

	/**
	 * Organization which is coordinating or leading the project.
	 * The coordinator may be the local organization.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private ResearchOrganization coordinator;

	/**
	 * Local organization which is involved in the project.
	 * This organization may be a sub-organization of the legal local organization.
	 *
	 * @see #superOrganization
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private ResearchOrganization localOrganization;

	/**
	 * Super organization which is involved in the project. This organization is 
	 * the legal super organization of the local organization.
	 * 
	 * @see #localOrganization
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private ResearchOrganization superOrganization;

	/**
	 * Organization of the Legal Entity Appointed Representative for the project.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private ResearchOrganization learOrganization;

	/**
	 * List of organizations involved in the project that are neither the {@link #coordinator}
	 * nor the {@link #localOrganization} nor the {@link #superOrganization}.
	 */
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
			name = "RESEARCH_ORGS_PARTNER_PROJECTS",
			joinColumns = @JoinColumn(name = "idProjects"),
			inverseJoinColumns = @JoinColumn( name = "idResearchOrgs"))
	private Set<ResearchOrganization> otherPartners;

	/** List of the participants in the local organization.
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ProjectMember> participants;

	/**
	 * Construct an empty project.
	 */
	public Project() {
		//
	}

	/** Constructor.
	 *
	 * @param id the identifier of the project in the database.
	 * @param scientificTitle the title of the project with highlight of the scientific contribution. 
	 * @param acronym the acronym of the project.
	 * @param description the full description of the project.
	 * @param startDate the date of start.
	 * @param duration the duration in months.
	 * @param globalBudget the global budget, including all the partners.
	 * @param trl indicates the TRL for the project.
	 * @param contractType the type of contract for the project.
	 * @param activityType the type of activity for the project.
	 * @param status the status of the project.
	 * @param confidential indicates if the project information is confidential or not.
	 * @param openSource indicates if the project is open source or not.
	 * @param projectUrl the official website of the project (usually outside the local organization website).
	 * @param webPageNaming the naming convention for the project webpage.
	 * @param videoUrls the list of URLs to videos related to the project.
	 * @param pathToScientificRequirements the path to an upload document that describes the scientific requirements.
	 * @param pathToPressDocument the path to an upload press document.
	 * @param pathToLogo the path to the logo that may be used for representing the project.
	 * @param pathsToImages the paths to the public images that may be used for representing the project.
	 * @param pathToPowerpoint the path to an upload PowerPoint document that describes the project.
	 * @param coordinator the organization that is coordinating the project.
	 * @param localOrganization the local organization. It may be the same as the {@code coordinator}.
	 * @param otherPartners the list of the other partners that are involved in the project and that are neither
	 *     the coordinator nor the local organization.
	 * @param participants the list of the participants in the local organization and their roles in the project.
	 */
	public Project(int id, String scientificTitle, String acronym, String description,
			LocalDate startDate, int duration,
			float globalBudget, TRL trl, ProjectContractType contractType, ProjectActivityType activityType, 
			ProjectStatus status, boolean confidential, boolean openSource,
			String projectUrl, ProjectWebPageNaming webPageNaming, List<String> videoUrls,
			String pathToScientificRequirements, String pathToPressDocument, String pathToLogo, List<String> pathsToImages, String pathToPowerpoint,
			ResearchOrganization coordinator, ResearchOrganization localOrganization, Set<ResearchOrganization> otherPartners,
			List<ProjectMember> participants) {
		assert activityType != null;
		this.id = id;
		this.scientificTitle = scientificTitle;
		this.acronym = acronym;
		this.description = description;
		this.startDate = startDate;
		this.duration = duration;
		this.globalBudget = globalBudget;
		this.status = status;
		this.contractType = contractType == null ? ProjectContractType.NOT_SPECIFIED : contractType;
		this.activityType = activityType;
		this.trl = trl;
		this.confidential = confidential;
		this.openSource = openSource;
		this.participants = participants;
		this.localOrganization = localOrganization;
		this.otherPartners = otherPartners;
		this.coordinator = coordinator;
		this.videoURLs = videoUrls;
		this.projectURL = projectUrl;
		this.webPageNaming = webPageNaming;
		this.pathToScientificRequirements = pathToScientificRequirements;
		this.pathToPressDocument = pathToPressDocument;
		this.pathToLogo = pathToLogo;
		this.pathsToImages = pathsToImages;
		this.pathToPowerpoint = pathToPowerpoint;
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.scientificTitle);
		h = HashCodeUtils.add(h, this.acronym);
		h = HashCodeUtils.add(h, this.description);
		h = HashCodeUtils.add(h, this.startDate);
		h = HashCodeUtils.add(h, this.duration);
		h = HashCodeUtils.add(h, this.globalBudget);
		h = HashCodeUtils.add(h, this.contractType);
		h = HashCodeUtils.add(h, this.activityType);
		h = HashCodeUtils.add(h, this.status);
		h = HashCodeUtils.add(h, this.trl);
		h = HashCodeUtils.add(h, this.confidential);
		h = HashCodeUtils.add(h, this.openSource);
		h = HashCodeUtils.add(h, this.projectURL);
		h = HashCodeUtils.add(h, this.webPageNaming);
		h = HashCodeUtils.add(h, this.videoURLs);
		h = HashCodeUtils.add(h, this.pathToScientificRequirements);
		h = HashCodeUtils.add(h, this.pathToPressDocument);
		h = HashCodeUtils.add(h, this.pathToLogo);
		h = HashCodeUtils.add(h, this.videoURLs);
		h = HashCodeUtils.add(h, this.pathsToImages);
		h = HashCodeUtils.add(h, this.pathToPowerpoint);
		h = HashCodeUtils.add(h, this.coordinator);
		h = HashCodeUtils.add(h, this.localOrganization);
		h = HashCodeUtils.add(h, this.superOrganization);
		h = HashCodeUtils.add(h, this.learOrganization);
		h = HashCodeUtils.add(h, this.otherPartners);
		h = HashCodeUtils.add(h, this.participants);
		h = HashCodeUtils.add(h, this.validated);
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Project other = (Project) obj;
		if (!Objects.equals(this.scientificTitle, other.scientificTitle)) {
			return false;
		}
		if (!Objects.equals(this.acronym, other.acronym)) {
			return false;
		}
		if (!Objects.equals(this.startDate, other.startDate)) {
			return false;
		}
		if (this.duration != other.duration) {
			return false;
		}
		if (!Objects.equals(this.description, other.description)) {
			return false;
		}
		if (this.globalBudget != other.globalBudget) {
			return false;
		}
		if (this.contractType != other.contractType) {
			return false;
		}
		if (this.activityType != other.activityType) {
			return false;
		}
		if (this.status != other.status) {
			return false;
		}
		if (this.trl != other.trl) {
			return false;
		}
		if (this.confidential != other.confidential) {
			return false;
		}
		if (this.openSource != other.openSource) {
			return false;
		}
		if (!Objects.equals(this.projectURL, other.projectURL)) {
			return false;
		}
		if (!Objects.equals(this.webPageNaming, other.webPageNaming)) {
			return false;
		}
		if (!Objects.equals(this.pathToScientificRequirements, other.pathToScientificRequirements)) {
			return false;
		}
		if (!Objects.equals(this.pathToPressDocument, other.pathToPressDocument)) {
			return false;
		}
		if (!Objects.equals(this.pathToLogo, other.pathToLogo)) {
			return false;
		}
		if (!Objects.equals(this.pathsToImages, other.pathsToImages)) {
			return false;
		}
		if (!Objects.equals(this.pathToPowerpoint, other.pathToPowerpoint)) {
			return false;
		}
		if (!Objects.equals(this.videoURLs, other.videoURLs)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Project o) {
		return EntityUtils.getPreferredProjectComparator().compare(this, o);
	}

	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		assert consumer != null : "How to consume an attribute if the consumer is null?"; //$NON-NLS-1$
		if (getId() != 0) {
			consumer.accept("id", Integer.valueOf(getId())); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getScientificTitle())) {
			consumer.accept("scientificTitle", getScientificTitle()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getAcronym())) {
			consumer.accept("acronym", getAcronym()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getDescription())) {
			consumer.accept("description", getDescription()); //$NON-NLS-1$
		}
		if (getStartDate() != null) {
			consumer.accept("startDate", getStartDate()); //$NON-NLS-1$
		}
		if (getDuration() > 0) {
			consumer.accept("duration", Integer.valueOf(getDuration())); //$NON-NLS-1$
		}
		if (getGlobalBudget() > 0f) {
			consumer.accept("globalBudget", Float.valueOf(getGlobalBudget())); //$NON-NLS-1$
		}
		if (getContractType() != null) {
			consumer.accept("contractType", getContractType()); //$NON-NLS-1$
		}
		if (getActivityType() != null) {
			consumer.accept("activityType", getActivityType()); //$NON-NLS-1$
		}
		if (getStatus() != null) {
			consumer.accept("status", getStatus()); //$NON-NLS-1$
		}
		if (getTRL() != null) {
			consumer.accept("trl", getTRL()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getProjectURL())) {
			consumer.accept("projectURL", getProjectURL()); //$NON-NLS-1$
		}
		if (getWebPageNaming() != null) {
			consumer.accept("webPageNaming", getWebPageNaming()); //$NON-NLS-1$
		}
		if (!getVideoURLs().isEmpty()) {
			consumer.accept("videoURLs", getVideoURLs()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getPathToScientificRequirements())) {
			consumer.accept("pathToScientificRequirements", getPathToScientificRequirements()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getPathToPressDocument())) {
			consumer.accept("pathToPressDocument", getPathToPressDocument()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getPathToLogo())) {
			consumer.accept("pathToLogo", getPathToLogo()); //$NON-NLS-1$
		}
		if (!getPathsToImages().isEmpty()) {
			consumer.accept("pathsToImages", getPathsToImages()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getPathToPowerpoint())) {
			consumer.accept("pathToPowerpoint", getPathToPowerpoint()); //$NON-NLS-1$
		}
		consumer.accept("confidential", Boolean.valueOf(isConfidential())); //$NON-NLS-1$
		consumer.accept("openSource", Boolean.valueOf(isOpenSource())); //$NON-NLS-1$
		consumer.accept("validated", Boolean.valueOf(isValidated())); //$NON-NLS-1$
	}

	@Override
	public void serialize(JsonGenerator generator, SerializerProvider serializers) throws IOException {
		generator.writeStartObject();
		forEachAttribute((attrName, attrValue) -> {
			JsonUtils.writeField(generator, attrName, attrValue);
		});
		final CachedGenerator organizations = JsonUtils.cache(generator);
		//
		final ResearchOrganization coordinator = getCoordinator();
		organizations.writeReferenceOrObjectField("coordinator", coordinator, () -> { //$NON-NLS-1$
			JsonUtils.writeObjectAndAttributes(generator, coordinator);
		});
		//
		final ResearchOrganization localOrganization = getLocalOrganization();
		organizations.writeReferenceOrObjectField("localOrganization", localOrganization, () -> { //$NON-NLS-1$
			JsonUtils.writeObjectAndAttributes(generator, localOrganization);
		});
		//
		final ResearchOrganization superOrganization = getSuperOrganization();
		organizations.writeReferenceOrObjectField("superOrganization", superOrganization, () -> { //$NON-NLS-1$
			JsonUtils.writeObjectAndAttributes(generator, superOrganization);
		});
		//
		final ResearchOrganization learOrganization = getLearOrganization();
		organizations.writeReferenceOrObjectField("learOrganization", learOrganization, () -> { //$NON-NLS-1$
			JsonUtils.writeObjectAndAttributes(generator, learOrganization);
		});
		//
		generator.writeArrayFieldStart("otherPartners"); //$NON-NLS-1$
		for (final ResearchOrganization otherPartner : getOtherPartners()) {
			organizations.writeReferenceOrObject(otherPartner, () -> {
				JsonUtils.writeObjectAndAttributes(generator, otherPartner);
			});
		}
		generator.writeEndArray();
		//
		generator.writeArrayFieldStart("participants"); //$NON-NLS-1$
		for (final ProjectMember participant : getParticipants()) {
			generator.writeStartObject();
			final Person person = participant.getPerson();
			organizations.writeReferenceOrObjectField("person", person, () -> { //$NON-NLS-1$
				JsonUtils.writeObjectAndAttributes(generator, person);
			});
			final Role role = participant.getRole();
			JsonUtils.writeField(generator, "role", role); //$NON-NLS-1$
			generator.writeEndObject();
		}
		generator.writeEndArray();
		//
		generator.writeArrayFieldStart("budgets"); //$NON-NLS-1$
		for (final ProjectBudget budget : getBudgets()) {
			generator.writeStartObject();
			final FundingScheme scheme = budget.getFundingScheme();
			if (scheme != null) {
				JsonUtils.writeField(generator, "fundingScheme", scheme); //$NON-NLS-1$
			}
			final float budgetValue = budget.getBudget();
			if (budgetValue > 0f) {
				JsonUtils.writeField(generator, "budget", Float.valueOf(budgetValue)); //$NON-NLS-1$
			}
			final String grant = budget.getGrant();
			if (!Strings.isNullOrEmpty(grant)) {
				JsonUtils.writeField(generator, "grant", grant); //$NON-NLS-1$
			}
			generator.writeEndObject();
		}
		generator.writeEndArray();
		//
		generator.writeEndObject();
	}

	@Override
	public void serializeWithType(JsonGenerator generator, SerializerProvider serializers, TypeSerializer typeSer)
			throws IOException {
		serialize(generator, serializers);
	}

	@Override
	public int getId() {
		return this.id;
	}

	/** Change the identifier of the research organization.
	 *
	 * @param id the identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/** Replies the title of the project with highlighting of the scientific contribution.
	 *
	 * @return the scientific title.
	 */
	public String getScientificTitle() {
		return this.scientificTitle;
	}

	/** Change the title of the project with highlighting of the scientific contribution.
	 *
	 * @param scientificTitle the scientific title.
	 */
	public void setScientificTitle(String scientificTitle) {
		this.scientificTitle = Strings.emptyToNull(scientificTitle);
	}

	/** Replies the acronym of the project.
	 *
	 * @return the acronym.
	 */
	public String getAcronym() {
		return this.acronym;
	}

	/** Change the acronym of the project.
	 *
	 * @param acronym the acronym.
	 */
	public void setAcronym(String acronym) {
		this.acronym = Strings.emptyToNull(acronym);
	}

	/** Replies the acronym or the scientific title of the project, that order.
	 *
	 * @return the acronym or name.
	 * @see #getNameOrAcronym()
	 * @see #getAcronym()
	 * @see #getScientificTitle()
	 */
	public String getAcronymOrScientificTitle() {
		return Strings.isNullOrEmpty(this.acronym) ? this.scientificTitle : this.acronym;
	}

	/** Replies the scientific title or the acronym of the project, that order.
	 *
	 * @return the name or acronym.
	 * @see #getAcronymOrName()
	 * @see #getAcronym()
	 * @see #getScientificTitle()
	 */
	public String getScientificTitleOrAcronym() {
		return Strings.isNullOrEmpty(this.scientificTitle) ? this.acronym: this.scientificTitle;
	}

	/** Replies the description of the project.
	 *
	 * @return the description.
	 */
	public String getDescription() {
		return this.description;
	}

	/** Change the description of the project.
	 *
	 * @param description the description.
	 */
	public void setDescription(String description) {
		this.description = Strings.emptyToNull(description);
	}

	/** Replies the global budget of the project in Kilo euros.
	 *
	 * @return the budget in kilo euros.
	 */
	public float getGlobalBudget() {
		return this.globalBudget;
	}

	/** Set the global budget of the project in Kilo euros.
	 *
	 * @param globalBudget the budget in kilo euros.
	 */
	public void setGlobalBudget(float globalBudget) {
		if (globalBudget > 0f) {
			this.globalBudget = globalBudget;
		} else {
			this.globalBudget = 0f;
		}
	}

	/** Set the global budget of the project in Kilo euros.
	 *
	 * @param globalBudget the budget in kilo euros.
	 */
	public final void setGlobalBudget(Number globalBudget) {
		if (globalBudget != null) {
			setGlobalBudget(globalBudget.floatValue());
		} else {
			setGlobalBudget(0f);
		}
	}

	/** Replies the sum of the budgets for the local organizations
	 *
	 * @return the sum of the budgets.
	 * @see #getBudgets()
	 */
	public float getTotalLocalOrganizationBudget() {
		if (this.totalLocalOrganizationBudget == null) {
			float sum = 0f;
			for (final ProjectBudget budget : getBudgets()) {
				sum += budget.getBudget();
			}
			this.totalLocalOrganizationBudget = Float.valueOf(sum);
		}
		return this.totalLocalOrganizationBudget.floatValue();
	}

	/** Replies the type of contract for this project.
	 *
	 * @return contract type.
	 */
	public ProjectContractType getContractType() {
		return this.contractType;
	}

	/** Change the type of contract for this project.
	 *
	 * @param type contract type.
	 */
	public void setContractType(ProjectContractType type) {
		if (type == null) {
			this.contractType = ProjectContractType.NOT_SPECIFIED;
		} else {
			this.contractType = type;
		}
	}

	/** Change the type of contract for this project.
	 *
	 * @param type contract type.
	 */
	public final void setContractType(String type) {
		try {
			setContractType(ProjectContractType.valueOfCaseInsensitive(type));
		} catch (Throwable ex) {
			setContractType((ProjectContractType) null);
		}
	}

	/** Replies the type of research activity for this project.
	 *
	 * @return activity type.
	 */
	public ProjectActivityType getActivityType() {
		return this.activityType;
	}

	/** Change the type of research activity for this project.
	 *
	 * @param type activity type.
	 */
	public void setActivityType(ProjectActivityType type) {
		this.activityType = type;
	}

	/** Change the type of research activity for this project.
	 *
	 * @param type activity type.
	 */
	public final void setActivityType(String type) {
		try {
			setActivityType(ProjectActivityType.valueOfCaseInsensitive(type));
		} catch (Throwable ex) {
			setActivityType((ProjectActivityType) null);
		}
	}

	/** Replies the category of this project.
	 *
	 * @return category.
	 */
	public ProjectCategory getCategory() {
		switch (getContractType()) {
		case RCO:
			return ProjectCategory.COMPETITIVE_CALL_PROJECT;
		case RCD:
		case PR:
		case PI:
			return ProjectCategory.NOT_ACADEMIC_PROJECT;
		case NOT_SPECIFIED:
		default:
			// See below
		}
		if (isOpenSource()) {
			return ProjectCategory.OPEN_SOURCE;
		}
		return ProjectCategory.AUTO_FUNDING;
	}

	/** Replies the status of this project.
	 *
	 * @return status.
	 */
	public ProjectStatus getStatus() {
		return this.status;
	}

	/** Change the status of this project.
	 *
	 * @param status the status
	 */
	public void setStatus(ProjectStatus status) {
		this.status = status;
	}

	/** Change the status of this project.
	 *
	 * @param status the status
	 */
	public final void setStatus(String status) {
		try {
			setStatus(ProjectStatus.valueOfCaseInsensitive(status));
		} catch (Throwable ex) {
			setStatus((ProjectStatus) null);
		}
	}

	/** Replies the URL of the website related to this project.
	 *
	 * @return the URL
	 */
	public String getProjectURL() {
		return this.projectURL;
	}

	/** Replies the URL of the website related to this project.
	 *
	 * @return the URL, or {@code null}.
	 */
	public URL getProjectURLObject() {
		try {
			return new URL(getProjectURL());
		} catch (MalformedURLException ex) {
			return null;
		}
	}

	/** Change the URL of a website related to this project.
	 *
	 * @param url the URL.
	 */
	public void setProjectURL(URL url) {
		if (url != null) {
			setProjectURL(url.toExternalForm());
		} else {
			setProjectURL((String) null);
		}
	}

	/** Change the URL of a website related to this project.
	 *
	 * @param url the URL.
	 */
	public void setProjectURL(String url) {
		this.projectURL = Strings.emptyToNull(url);
	}


	/** Replies the naming convention for the project webpage.
	 *
	 * @return the naming convention.
	 */
	public ProjectWebPageNaming getWebPageNaming() {
		return this.webPageNaming;
	}

	/** Change the naming convention for the project webpage.
	 *
	 * @param convention the naming convention.
	 */
	public void setWebPageNaming(ProjectWebPageNaming convention) {
		if (convention == null) {
			this.webPageNaming = ProjectWebPageNaming.UNSPECIFIED;
		} else {
			this.webPageNaming = convention;
		}
	}

	/** Change the naming convention for the project webpage.
	 *
	 * @param convention the naming convention.
	 */
	public final void setWebPageNaming(String convention) {
		try {
			setWebPageNaming(ProjectWebPageNaming.valueOfCaseInsensitive(convention));
		} catch (Throwable ex) {
			setWebPageNaming((ProjectWebPageNaming) null);
		}
	}

	/** Replies the URI of the webpage of the project on the institution website.
	 *
	 * @return the URI or {@code null} if none.
	 */
	public URI getWebPageURI() {
		return getWebPageNaming().getWebpageURIFor(this);
	}

	/** Replies the URLs of the videos related to this project.
	 *
	 * @return the URLs, never {@code null}.
	 */
	public List<String> getVideoURLs() {
		if (this.videoURLs == null) {
			this.videoURLs = new ArrayList<>();
		}
		return this.videoURLs;
	}

	/** Replies the URLs of the videos related to this project.
	 *
	 * @return the URLs.
	 */
	public final List<URL> getVideoURLsObject() {
		return getVideoURLs().stream().map(it -> {
			try {
				return new URL(it);
			} catch (RuntimeException ex) {
				throw ex;
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}).collect(Collectors.toList());
	}

	/** Change the URLs of the videos related to this project.
	 *
	 * @param urls the URLs.
	 */
	protected void setVideoURLs(Stream<String> urls) {
		if (this.videoURLs == null) {
			this.videoURLs = new ArrayList<>();
		} else {
			this.videoURLs.clear();
		}
		if (urls != null) {
			urls.forEach(it -> this.videoURLs.add(it));
		}
	}

	/** Change the URLs of the videos related to this project.
	 *
	 * @param urls the URLs.
	 */
	public void setVideoURLs(List<String> urls) {
		if (urls != null) {
			setVideoURLs(urls.stream());
		} else {
			setVideoURLs((Stream<String>) null);
		}
	}

	/** Change the URLs of the videos related to this project.
	 *
	 * @param urls the URLs.
	 */
	public void setVideoURLsObject(List<URL> urls) {
		if (urls != null) {
			setVideoURLs(urls.stream().map(it -> it.toExternalForm()));
		} else {
			setVideoURLs((Stream<String>) null);
		}
	}

	/** Replies a path to a downloadable document that is the scientific requirements of the project.
	 *
	 * @return the path or {@code null}.
	 */
	public String getPathToScientificRequirements() {
		return this.pathToScientificRequirements;
	}

	/** Change a path to a downloadable document that is the scientific requirements of the project.
	 *
	 * @param path the path or {@code null}.
	 */
	public void setPathToScientificRequirements(String path) {
		this.pathToScientificRequirements = Strings.emptyToNull(path);
	}

	/** Replies a path to a downloadable press document of the project.
	 *
	 * @return the path or {@code null}.
	 */
	public String getPathToPressDocument() {
		return this.pathToPressDocument;
	}

	/** Change a path to a downloadable press document of the project.
	 *
	 * @param path the path or {@code null}.
	 */
	public void setPathToPressDocument(String path) {
		this.pathToPressDocument = Strings.emptyToNull(path);
	}

	/** Replies a path to a downloadable logo that represents the project.
	 *
	 * @return the path or {@code null}.
	 */
	public String getPathToLogo() {
		return this.pathToLogo;
	}

	/** Change the path to a downloadable logo that represents the project.
	 *
	 * @param path the path or {@code null}.
	 */
	public void setPathToLogo(String path) {
		this.pathToLogo = Strings.emptyToNull(path);
	}

	/** Replies the paths to the downloadable images that represents the project.
	 *
	 * @return the paths never {@code null}.
	 */
	public List<String> getPathsToImages() {
		if (this.pathsToImages == null) {
			this.pathsToImages = new ArrayList<>();
		}
		return this.pathsToImages;
	}

	/** Change the paths to a downloadable images that represents the project.
	 *
	 * @param paths the paths.
	 */
	public void setPathsToImages(List<String> paths) {
		if (this.pathsToImages == null) {
			this.pathsToImages = new ArrayList<>();
		} else {
			this.pathsToImages.clear();
		}
		if (paths != null) {
			this.pathsToImages.addAll(paths);
		}
	}

	/** Replies a path to a downloadable powerpoint for the project.
	 *
	 * @return the path or {@code null}.
	 */
	public String getPathToPowerpoint() {
		return this.pathToPowerpoint;
	}

	/** Change the path to a downloadable powerpoint for the project.
	 *
	 * @param path the path or {@code null}.
	 */
	public void setPathToPowerpoint(String path) {
		this.pathToPowerpoint = Strings.emptyToNull(path);
	}

	/** Replies the TRL for the project.
	 *
	 * @return the TRL.
	 */
	public TRL getTRL() {
		return this.trl;
	}

	/** Change the TRL for the project.
	 *
	 * @param trl the TRL.
	 */
	public void setTRL(TRL trl) {
		this.trl = trl;
	}

	/** Change the TRL for the project.
	 *
	 * @param trl the TRL.
	 */
	public final void setTRL(String trl) {
		try {
			setTRL(TRL.valueOfCaseInsensitive(trl));
		} catch (Throwable ex) {
			setTRL((TRL) null);
		}
	}

	/** Replies if this project is marked as confidential or not.
	 *
	 * @return {@code true} if the project is confidential.
	 */
	public boolean isConfidential() {
		return this.confidential;
	}

	/** Change the flag that indicates if this project is marked as confidential or not.
	 *
	 * @param confidential {@code true} if the project is confidential.
	 */
	public void setConfidential(boolean confidential) {
		this.confidential = confidential;
	}

	/** Change the flag that indicates if this project is marked as confidential or not.
	 *
	 * @param confidential {@code true} if the project is confidential.
	 */
	public final void setConfidential(Boolean confidential) {
		if (confidential != null) {
			setConfidential(confidential.booleanValue());
		} else {
			setConfidential(false);
		}
	}

	/** Replies if this project is open source or not.
	 *
	 * @return {@code true} if the project is open source.
	 */
	public boolean isOpenSource() {
		return this.openSource;
	}

	/** Change the flag that indicates if this project is open source or not.
	 *
	 * @param openSource {@code true} if the project is open source.
	 */
	public void setOpenSource(boolean openSource) {
		this.openSource = openSource;
	}

	/** Change the flag that indicates if this project is open source or not.
	 *
	 * @param openSource {@code true} if the project is open source.
	 */
	public final void setOpenSource(Boolean openSource) {
		if (openSource != null) {
			setOpenSource(openSource.booleanValue());
		} else {
			setOpenSource(false);
		}
	}

	/** Replies if this journal was validated by an authority.
	 *
	 * @return {@code true} if the journal is validated.
	 */
	public boolean isValidated() {
		return this.validated;
	}

	/** Change the flag that indicates if this journal was validated by an authority.
	 *
	 * @param validated {@code true} if the journal is validated.
	 */
	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	/** Change the flag that indicates if this journal was validated by an authority.
	 *
	 * @param validated {@code true} if the journal is validated.
	 */
	public final void setValidated(Boolean validated) {
		if (validated == null) {
			setValidated(false);
		} else {
			setValidated(validated.booleanValue());
		}
	}

	/** Replies the date of start.
	 *
	 * @return the date, or {@code null}.
	 */
	public LocalDate getStartDate() {
		return this.startDate;
	}

	/** Change the date of start. 
	 *
	 * @param date the date, or {@code null}.
	 */
	public void setStartDate(LocalDate date) {
		this.startDate = date;
	}

	/** Change the date of start. 
	 *
	 * @param date the date, or {@code null}.
	 */
	public final void setStartDate(String date) {
		if (Strings.isNullOrEmpty(date)) {
			throw new IllegalArgumentException();
		}
		setStartDate(LocalDate.parse(date));
	}

	/** Replies the year of start. It is computed {@link #getStartDate()}.
	 *
	 * @return the year, or {@code 0}.
	 */
	public int getStartYear() {
		final LocalDate sd = getStartDate();
		if (sd != null) {
			return sd.getYear();
		}
		return 0;
	}

	/** Replies the duration of the project in months.
	 *
	 * @return the duration in months.
	 */
	public int getDuration() {
		return this.duration;
	}

	/** Change the duration of the project in months.
	 *
	 * @param duration the duration in months.
	 */
	public void setDuration(int duration) {
		if (duration < 0) {
			this.duration = 0;
		} else {
			this.duration = duration;
		}
	}

	/** Change the duration of the project in months.
	 *
	 * @param duration the duration in months.
	 */
	public final void setDuration(Number duration) {
		if (duration != null) {
			setDuration(duration.intValue());
		} else {
			setDuration(0);
		}
	}

	/** Replies the date of end. It is computed by adding the results of
	 * {@link #getStartDate()} and {@link #getDuration()}.
	 *
	 * @return the date, or {@code null} if there is no ending date
	 */
	public LocalDate getEndDate() {
		final LocalDate sd = getStartDate();
		if (sd != null) {
			final int duration = getDuration();
			if (duration > 0) {
				return sd.plusMonths(duration);
			}
		}
		return null;
	}

	/** Replies the year of end. It is computed {@link #getEndDate()}.
	 *
	 * @return the year, or {@code 0}.
	 */
	public int getEndYear() {
		final LocalDate sd = getEndDate();
		if (sd != null) {
			return sd.getYear();
		}
		return 0;
	}

	/** Replies the coordinator of the project.
	 *
	 * @return the coordinator, or {@code null} if the coordinator is unknown.
	 */
	public ResearchOrganization getCoordinator() {
		return this.coordinator;
	}

	/** Change the coordinator of the project.
	 *
	 * @param coordinator the coordinator, or {@code null} if the coordinator is unknown.
	 */
	public void setCoordinator(ResearchOrganization coordinator) {
		this.coordinator = coordinator;
	}

	/** Replies the local organization involved in the project.
	 *
	 * @return the local organization, or {@code null} if the local organization is unknown.
	 */
	public ResearchOrganization getLocalOrganization() {
		return this.localOrganization;
	}

	/** Change the local organization involved in the project.
	 *
	 * @param organization the local organization, or {@code null} if the local organization is unknown.
	 */
	public void setLocalOrganization(ResearchOrganization organization) {
		this.localOrganization = organization;
	}

	/** Replies the legal super organization involved in the project.
	 *
	 * @return the legal super organization, or {@code null} if the legal super is unknown.
	 */
	public ResearchOrganization getSuperOrganization() {
		return this.superOrganization;
	}

	/** Change the super organization of the local organization.
	 *
	 * @param organization the super organization, or {@code null} if the legal super organization is unknown.
	 */
	public void setSuperOrganization(ResearchOrganization organization) {
		this.superOrganization = organization;
	}

	/** Replies the organization of the Legal Entity Appointed Representative for the project.
	 *
	 * @return the LEAR's organization, or {@code null} if the organization is unknown.
	 */
	public ResearchOrganization getLearOrganization() {
		return this.learOrganization;
	}

	/** Change the organization of the Legal Entity Appointed Representative for the project.
	 *
	 * @param lear the LEAR's organization, or {@code null} if the organization is unknown.
	 */
	public void setLearOrganization(ResearchOrganization lear) {
		this.learOrganization = lear;
	}

	/** Replies the set of other partners. The other partners are replied in the alpha order.
	 *
	 * @return the other partners.
	 */
	public List<ResearchOrganization> getOtherPartners() {
		final Set<ResearchOrganization> raw = getOtherPartnersRaw();
		assert raw != null;
		final List<ResearchOrganization> sortedList = raw.stream().sorted(ResearchOrganizationComparator.DEFAULT).collect(Collectors.toList());
		return sortedList;
	}

	/** Replies the reference to the storage area without any change or filtering.
	 *
	 * @return the other partners.
	 */
	public Set<ResearchOrganization> getOtherPartnersRaw() {
		if (this.otherPartners == null) {
			this.otherPartners = new HashSet<>();
		}
		return this.otherPartners;
	}

	/** Change the set of other partners.
	 * You are not supposed to invoke this function yourself because the other-partner set
	 * is managed by the API framework.
	 *
	 * @param otherPartners the other partners.
	 */
	public void setOtherPartners(Set<? extends ResearchOrganization> otherPartners) {
		if (this.otherPartners == null) {
			this.otherPartners = new HashSet<>();
		} else {
			this.otherPartners.clear();
		}
		if (otherPartners != null) {
			this.otherPartners.addAll(otherPartners);
		}
	}

	/** Replies the list of the persons from the local organization who are participating to this project, and their
	 * respective roles in the project.
	 *
	 * @return the list of participants, never {@code null}
	 */
	public List<ProjectMember> getParticipants() {
		if (this.participants == null) {
			return Collections.emptyList();
		}
		return this.participants;
	}

	/** Change the list of the persons from the local organization who are participating to this project, and their
	 * respective roles in the project.
	 *
	 * @param participants the list of participants, never {@code null}
	 */
	public void setParticipants(List<? extends ProjectMember> participants) {
		if (this.participants == null) {
			this.participants = new ArrayList<>();
		} else {
			this.participants.clear();
		}
		if (participants != null) {
			this.participants.addAll(participants);
		}
	}

	/** Add a project participant to the list of the persons from the local organization who are participating to this project, and the
	 * role of this person in the project.
	 *
	 * @param participant the new participant.
	 */
	public void addParticipant(ProjectMember participant) {
		if (participant != null) {
			if (this.participants == null) {
				this.participants = new ArrayList<>();
			}
			this.participants.add(participant);
		}
	}

	/** Remove a project participant from the list of the persons from the local organization who are participating to this project.
	 *
	 * @param participant the participant to remove.
	 */
	public void removeParticipant(ProjectMember participant) {
		if (participant != null && this.participants != null) {
			this.participants.remove(participant);
		}
	}

	/** Replies the list of local budgets associated to the project.
	 *
	 * @return the list of budget.
	 * @see #getTotalLocalOrganizationBudget()
	 */
	public List<ProjectBudget> getBudgets() {
		if (this.budgets == null) {
			this.budgets = new ArrayList<>();
		}
		return this.budgets;
	}

	/** Change the list of local budgets associated to the project.
	 *
	 * @param budgets the list of budget.
	 */
	public void setBudgets(List<? extends ProjectBudget> budgets) {
		if (this.budgets == null) {
			this.budgets = new ArrayList<>();
		} else {
			this.budgets.clear();
		}
		if (budgets != null) {
			this.budgets.addAll(budgets);
		}
		this.totalLocalOrganizationBudget = null;
	}

	/** Replies the funding scheme that is the most important from the type of funding.
	 *
	 * @return the major funding scheme.
	 */
	public FundingScheme getMajorFundingScheme() {
		// Assume "NOT_FUNDED" is the lowest important scheme
		FundingScheme scheme = FundingScheme.NOT_FUNDED;
		for (final ProjectBudget budget : getBudgets()) {
			final FundingScheme sch = budget.getFundingScheme();
			if (sch != null && scheme.compareTo(sch) < 0) {
				scheme = sch;
			}
		}
		return scheme;
	}

}
