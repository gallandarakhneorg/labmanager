/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
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

package fr.ciadlab.labmanager.entities.scientificaxis;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.AttributeProvider;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.IdentifiableEntity;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.json.JsonUtils;
import fr.ciadlab.labmanager.io.json.JsonUtils.CachedGenerator;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/** A scientific axis that represents a transversal research activity
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.5
 */
@Entity
@Table(name = "ScientificAxes")
public class ScientificAxis implements Serializable, JsonSerializable, Comparable<ScientificAxis>, AttributeProvider, IdentifiableEntity {

	private static final long serialVersionUID = -5032257098515820096L;

	/** Identifier of the axis.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	/** Acronym of the scientific axis.
	 */
	@Column
	private String acronym;

	/** Name of the scientific axis.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String name;

	/** Creation date of the axis.
	 */
	@Column
	private LocalDate startDate;

	/** Termination date of the axis.
	 */
	@Column
	private LocalDate endDate;

	/** Indicates if the axis was validated by an authority.
	 */
	@Column(nullable = false)
	private boolean validated;

	/** Projects which are associated to the scientific axis.
	 */
	@ManyToMany(mappedBy = "scientificAxes", fetch = FetchType.LAZY)
	private Set<Project> projects = new HashSet<>();

	/** Publications which are associated to the scientific axis.
	 */
	@ManyToMany(mappedBy = "scientificAxes", fetch = FetchType.LAZY)
	private Set<Publication> publications = new HashSet<>();

	/** Persons who are associated to the research axis.
	 */
	@ManyToMany(mappedBy = "scientificAxes", fetch = FetchType.LAZY)
	private Set<Membership> memberships = new HashSet<>();

	/** Construct a scientific axis from the given values.
	 * 
	 * @param id the identifier of the axis.
	 * @param acronym the acronym of the axis.
	 * @param name the name of the axis.
	 * @param creationDate the date of creation of the axis.
	 * @param description the textual description of the axis.
	 */
	public ScientificAxis(int id, String acronym, String name, LocalDate creationDate) {
		this.id = id;
		this.acronym = acronym;
		this.name = name;
		this.startDate = creationDate;
	}

	/** Construct an empty scientific axis.
	 */
	public ScientificAxis() {
		//
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.acronym);
		h = HashCodeUtils.add(h, this.name);
		h = HashCodeUtils.add(h, this.startDate);
		h = HashCodeUtils.add(h, this.endDate);
		h = HashCodeUtils.add(h, this.validated);
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final ScientificAxis other = (ScientificAxis) obj;
		if (this.id != other.id) {
			return false;
		}
		if (!Objects.equals(this.acronym, other.acronym)) {
			return false;
		}
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.startDate, other.startDate)) {
			return false;
		}
		if (!Objects.equals(this.endDate, other.endDate)) {
			return false;
		}
		if (this.validated != other.validated) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(ScientificAxis o) {
		return EntityUtils.getPreferredScientificAxisComparator().compare(this, o);
	}

	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		assert consumer != null : "How to consume an attribute if the consumer is null?"; //$NON-NLS-1$
		if (getId() != 0) {
			consumer.accept("id", Integer.valueOf(getId())); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getAcronym())) {
			consumer.accept("acronym", getAcronym()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getName())) {
			consumer.accept("name", getName()); //$NON-NLS-1$
		}
		if (getStartDate() != null) {
			consumer.accept("startDate", getStartDate()); //$NON-NLS-1$
		}
		if (getEndDate() != null) {
			consumer.accept("endDate", getEndDate()); //$NON-NLS-1$
		}
		consumer.accept("validated", Boolean.valueOf(isValidated())); //$NON-NLS-1$
	}

	@Override
	public void serialize(JsonGenerator generator, SerializerProvider serializers) throws IOException {
		generator.writeStartObject();
		forEachAttribute((attrName, attrValue) -> {
			JsonUtils.writeField(generator, attrName, attrValue);
		});
		//
		final CachedGenerator projects = JsonUtils.cache(generator);
		final CachedGenerator publications = JsonUtils.cache(generator);
		final CachedGenerator memberships = JsonUtils.cache(generator);
		//
		generator.writeArrayFieldStart("projects"); //$NON-NLS-1$
		for (final Project project : getProjects()) {
			projects.writeReferenceOrObject(project, () -> {
				JsonUtils.writeObjectAndAttributes(generator, project);
			});
		}
		generator.writeEndArray();
		//
		generator.writeArrayFieldStart("publications"); //$NON-NLS-1$
		for (final Publication publication : getPublications()) {
			publications.writeReferenceOrObject(publication, () -> {
				JsonUtils.writeObjectAndAttributes(generator, publication);
			});
		}
		generator.writeEndArray();
		//
		generator.writeArrayFieldStart("memberships"); //$NON-NLS-1$
		for (final Membership membership : getMemberships()) {
			memberships.writeReferenceOrObject(membership, () -> {
				JsonUtils.writeObjectAndAttributes(generator, membership);
			});
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

	/** Replies the acronym or the name of the scientific axis, that order.
	 *
	 * @return the acronym or name.
	 * @see #getNameOrAcronym()
	 * @see #getAcronym()
	 * @see #getName()
	 */
	public String getAcronymOrName() {
		return Strings.isNullOrEmpty(this.acronym) ? this.name : this.acronym;
	}

	/** Replies the name or the acronym of the scientific axis, that order.
	 *
	 * @return the name or acronym.
	 * @see #getAcronymOrName()
	 * @see #getAcronym()
	 * @see #getName()
	 */
	public String getNameOrAcronym() {
		return Strings.isNullOrEmpty(this.name) ? this.acronym: this.name;
	}

	/** Replies the acronym of the scientific axis.
	 *
	 * @return the acronym.
	 */
	public String getAcronym() {
		return this.acronym;
	}

	/** Change the acronym of the scientific axis.
	 *
	 * @param acronym the acronym.
	 */
	public void setAcronym(String acronym) {
		this.acronym = Strings.emptyToNull(acronym);
	}

	/** Replies the name of the scientific axis.
	 *
	 * @return the name.
	 */
	public String getName() {
		return this.name;
	}

	/** Change the name of the scientific axis.
	 *
	 * @param name the name.
	 */
	public void setName(String name) {
		this.name = Strings.emptyToNull(name);
	}

	/** Replies the start date of the scientific axis.
	 *
	 * @return the start date.
	 */
	public LocalDate getStartDate() {
		return this.startDate;
	}

	/** Change the start date of the scientific axis.
	 *
	 * @param date the start date.
	 */
	public void setStartDate(LocalDate date) {
		this.startDate = date;
	}

	/** Change the start date of the scientific axis.
	 *
	 * @param date the start date.
	 */
	public final void setStartDate(String date) {
		if (Strings.isNullOrEmpty(date)) {
			setStartDate((LocalDate) null);
		} else {
			setStartDate(LocalDate.parse(date));
		}
	}

	/** Replies the end date of the scientific axis.
	 *
	 * @return the end date.
	 */
	public LocalDate getEndDate() {
		return this.endDate;
	}

	/** Change the end date of the scientific axis.
	 *
	 * @param date the end date.
	 */
	public void setEndDate(LocalDate date) {
		this.endDate = date;
	}

	/** Change the end date of the scientific axis.
	 *
	 * @param date the end date.
	 */
	public final void setEndDate(String date) {
		if (Strings.isNullOrEmpty(date)) {
			setEndDate((LocalDate) null);
		} else {
			setEndDate(LocalDate.parse(date));
		}
	}

	/** Replies if the scientific axis is active.
	 * An axis is active if the current date is inside the axis time window.
	 *
	 * @return {@code true} if the axis time window contains the current date.
	 */
	public boolean isActive() {
		final LocalDate now = LocalDate.now();
		return isActiveAt(now);
	}

	/** Replies if the scientific axis is active.
	 * An axis is active if the current date is inside the axis time window.
	 *
	 * @param now the given date to consider.
	 * @return {@code true} if the axis time window contains the given date.
	 */
	public boolean isActiveAt(LocalDate now) {
		final LocalDate start = getStartDate();
		if (start != null && now.isBefore(start)) {
			return false;
		}
		final LocalDate end = getEndDate();
		if (end != null && now.isAfter(end)) {
			return false;
		}
		return true;
	}

	/** Replies if the scientific axis is active in the given time range.
	 * An axis is active if the current date is inside the axis time window.
	 *
	 * @param windowStart is the start date of the window, never {@code null}.
	 * @param windowEnd is the end date of the window, never {@code null}.
	 * @return {@code true} if the axis time window intersects the given date window.
	 */
	public boolean isActiveIn(LocalDate windowStart, LocalDate windowEnd) {
		assert windowStart != null;
		assert windowEnd != null;
		final LocalDate start = getStartDate();
		final LocalDate end = getEndDate();
		if (start != null) {
			if (end != null) {
				return !windowEnd.isBefore(start) && !windowStart.isAfter(end);
			}
			return !windowEnd.isBefore(start);
		}
		if (end != null) {
			return !windowStart.isAfter(end);
		}
		return true;
	}

	/** Replies if this organization was validated by an authority.
	 *
	 * @return {@code true} if the organization is validated.
	 */
	public boolean isValidated() {
		return this.validated;
	}

	/** Change the flag that indicates if this organization was validated by an authority.
	 *
	 * @param validated {@code true} if the organization is validated.
	 */
	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	/** Change the flag that indicates if this organization was validated by an authority.
	 *
	 * @param validated {@code true} if the organization is validated.
	 */
	public final void setValidated(Boolean validated) {
		if (validated == null) {
			setValidated(false);
		} else {
			setValidated(validated.booleanValue());
		}
	}

	/** Replies the projects that are associated to this scientific axis.
	 *
	 * @return the projects.
	 */
	public Set<Project> getProjects() {
		if (this.projects == null) {
			this.projects = new HashSet<>();
		}
		return this.projects;
	}

	/** Change the projects that are associated to this scientific axis.
	 * This function does not update the relationship from the publications to the axis.
	 *
	 * @param projects the projects associated to this axis.
	 */
	public void setProjects(Collection<Project> projects) {
		if (this.projects == null) {
			this.projects = new HashSet<>();
		} else {
			this.projects.clear();
		}
		if (projects != null && !projects.isEmpty()) {
			this.projects.addAll(projects);
		}
	}

	/** Replies the publications that are associated to this scientific axis.
	 *
	 * @return the publications.
	 */
	public Set<Publication> getPublications() {
		if (this.publications == null) {
			this.publications = new HashSet<>();
		}
		return this.publications;
	}

	/** Change the publications that are associated to this scientific axis.
	 * This function does not update the relationship from the publications to the axis.
	 *
	 * @param publications the publications associated to this axis.
	 */
	public void setPublications(Collection<Publication> publications) {
		if (this.publications == null) {
			this.publications = new HashSet<>();
		} else {
			this.publications.clear();
		}
		if (publications != null && !publications.isEmpty()) {
			this.publications.addAll(publications);
		}
	}

	/** Replies the memberships that are associated to this scientific axis.
	 *
	 * @return the memberships.
	 */
	public Set<Membership> getMemberships() {
		if (this.memberships == null) {
			this.memberships = new HashSet<>();
		}
		return this.memberships;
	}

	/** Change the memberships that are associated to this scientific axis.
	 * This function does not update the relationship from the publications to the axis.
	 *
	 * @param memberships the memberships associated to this axis.
	 */
	public void setMemberships(Collection<Membership> memberships) {
		if (this.memberships == null) {
			this.memberships = new HashSet<>();
		} else {
			this.memberships.clear();
		}
		if (memberships != null && !memberships.isEmpty()) {
			this.memberships.addAll(memberships);
		}
	}

}
