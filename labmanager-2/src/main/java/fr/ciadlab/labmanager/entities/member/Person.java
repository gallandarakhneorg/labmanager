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
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.entities.member;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.JsonObject;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.utils.AttributeProvider;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import fr.ciadlab.labmanager.utils.JsonExportable;
import fr.ciadlab.labmanager.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ext.com.google.common.base.Strings;

/** Represent a person.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "Persons")
public class Person implements Serializable, JsonExportable, AttributeProvider, Comparable<Person> {

	private static final long serialVersionUID = -7738810647549936633L;

	/** Default comparator of persons. The order of the persons is based on the
	 * last name, first name, email, and the identifier.
	 */
	public static final Comparator<Person> PERSON_COMPARATOR = new Comparator<>() {
		@Override
		public int compare(Person o1, Person o2) {
			if (o1 == o2) {
				return 0;
			}
			if (o1 == null) {
				return Integer.MIN_VALUE;
			}
			if (o2 == null) {
				return Integer.MAX_VALUE;
			}
			int cmp = StringUtils.compare(o1.getLastName(), o2.getLastName());
			if (cmp != 0) {
				return cmp;
			}
			cmp = StringUtils.compare(o1.getFirstName(), o2.getFirstName());
			if (cmp != 0) {
				return cmp;
			}
			cmp = StringUtils.compare(o1.getEmail(), o2.getEmail());
			return Integer.compare(o1.getId(), o2.getId());
		}
	};

	/** Default comparator of lists of persons. The order of the persons in the list is based on
	 * {@link #PERSON_COMPARATOR}.
	 */
	public static final Comparator<List<Person>> PERSON_LIST_COMPARATOR = new Comparator<>() {
		@Override
		public int compare(List<Person> o1, List<Person> o2) {
			if (o1 == o2) {
				return 0;
			}
			if (o1 == null) {
				return Integer.MIN_VALUE;
			}
			if (o2 == null) {
				return Integer.MAX_VALUE;
			}
			final int max = Integer.max(o1.size(), o2.size());
			final Iterator<Person> it1 = o1.iterator();
			final Iterator<Person> it2 = o2.iterator();
			for (int i = 0; i < max; ++i) {
				final Person p1 = it1.next();
				final Person p2 = it2.next();
				final int cmp = PERSON_COMPARATOR.compare(p1, p2);
				if (cmp != 0) {
					return cmp;
				}
			}
			return Integer.compare(o1.size(), o2.size());
		}
	};

	/** Identifier of a person.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	/** First name of the person.
	 */
	@Column
	private String firstName;

	/** Last name of the person.
	 */
	@Column
	private String lastName;

	/** Email of the person.
	 */
	@Column
	private String email;

	/** List of research organizations for the person.
	 */
	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<Membership> researchOrganizations = new HashSet<>();

	/** List of publications of the person.
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="publicationId", referencedColumnName = "person")
	@JsonIgnore
	private Set<Authorship> publications = new HashSet<>();

	/** Construct a person with the given values.
	 *
	 * @param id the identifier of the person.
	 * @param publications the list of publications.
	 * @param orgas the memberships of the person.
	 * @param firstName the first name of the person.
	 * @param lastName the last name of the person.
	 * @param email the email of the person.
	 * @param hasPage indicates if the person has a personal page on the research organization website.
	 */
	public Person(int id, Set<Authorship> publications, Set<Membership> orgas, String firstName, String lastName,
			String email, boolean hasPage) {
		this.id = id;
		this.publications = publications;
		this.researchOrganizations = orgas;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	/** Construct an empty person.
	 */
	public Person() {
		//
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.firstName);
		h = HashCodeUtils.add(h, this.lastName);
		h = HashCodeUtils.add(h, this.email);
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
		final Person other = (Person) obj;
		if (this.id != other.id) {
			return false;
		}
		if (!Objects.equals(this.firstName, other.firstName)) {
			return false;
		}
		if (!Objects.equals(this.lastName, other.lastName)) {
			return false;
		}
		if (!Objects.equals(this.email, other.email)) {
			return false;
		}
		if (!Objects.equals(this.researchOrganizations, other.researchOrganizations)) {
			return false;
		}
		if (!Objects.equals(this.publications, other.publications)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Person o) {
		return PERSON_COMPARATOR.compare(this, o);
	}

	/** {@inheritDoc}
	 * <p>The attributes that are not considered by this function are:<ul>
	 * <li>{@code id}</li>
	 * </ul>
	 */
	@Override
	public void forEachAttribute(BiConsumer<String, Object> consumer) {
		if (!Strings.isNullOrEmpty(getFirstName())) {
			consumer.accept("firstName", getFirstName()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getLastName())) {
			consumer.accept("lastName", getLastName()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getEmail())) {
			consumer.accept("email", getEmail()); //$NON-NLS-1$
		}
	}

	@Override
	public void toJson(JsonObject json) {
		json.addProperty("id", Integer.valueOf(getId())); //$NON-NLS-1$
		forEachAttribute((name, value) -> JsonUtils.defaultBehavior(json, name, value));
	}

	/** Replies the identifier of the person.
	 *
	 * @return the identifier.
	 */
	public int getId() {
		return this.id;
	}

	/** Change the identifier of the person.
	 *
	 * @param id the identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/** Replies the first name of the person.
	 *
	 * @return the first name.
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/** Change the first name of the person.
	 *
	 * @param name the first name.
	 */
	public void setFirstName(String name) {
		this.firstName = Strings.emptyToNull(name);
	}

	/** Replies the last name of the person.
	 *
	 * @return the last name.
	 */
	public String getLastName() {
		return this.lastName;
	}

	/** Change the last name of the person.
	 *
	 * @param name the last name.
	 */
	public void setLastName(String name) {
		this.lastName = Strings.emptyToNull(name);
	}

	/** Replies the sequence of first and last names of the person.
	 *
	 * @return the full name in the form {@code FIRST LAST}.
	 */
	public String getFullName() {
		return getFirstName() + " " + getLastName(); //$NON-NLS-1$
	}

	/** Replies the email of the person.
	 *
	 * @return the email.
	 */
	public String getEmail() {
		return this.email;
	}

	/** Change the email of the person.
	 *
	 * @param email the email.
	 */
	public void setEmail(String email) {
		this.email = Strings.emptyToNull(email);
	}

	/** Replies the list of publications of the person.
	 *
	 * @return the list of publications.
	 */
	public Set<Authorship> getPublications() {
		if (this.publications == null) {
			return Collections.emptySet();
		}
		return this.publications;
	}

	/** Change the list of publications of the person.
	 *
	 * @param list the list of publications.
	 */
	public void setPublications(Set<Authorship> list) {
		this.publications = list;
	}

	/** Replies the memberships of the person.
	 *
	 * @return the research organizations in which the person is involved.
	 */
	public Set<Membership> getResearchOrganizations() {
		if (this.researchOrganizations == null) {
			return Collections.emptySet();
		}
		return this.researchOrganizations;
	}

	/** Change the memberships of the person.
	 *
	 * @param orgas the research organizations in which the person is involved.
	 */
	public void setResearchOrganizations(Set<Membership> orgas) {
		this.researchOrganizations = orgas;
	}

	/** Delete a publication for the person.
	 *
	 * @param pub is the publication authorship.
	 */
	@Transactional
	public void deleteAuthorship(Authorship pub) {
		if (this.publications != null) {
			this.publications.remove(pub);
		}
	}

	/** Delete all the publications for the person.
	 */
	@Transactional
	public void deleteAllAuthorships() {
		if (this.publications != null) {
			this.publications.clear();
		}
	}

}
