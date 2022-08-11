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

package fr.ciadlab.labmanager.entities.organization;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.utils.AttributeProvider;
import fr.ciadlab.labmanager.utils.CountryCodeUtils;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import fr.ciadlab.labmanager.utils.JsonExportable;
import fr.ciadlab.labmanager.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.arakhne.afc.util.CountryCode;

/** Research organization or group or researchers.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "ResearchOrgs")
public class ResearchOrganization implements Serializable, Comparable<ResearchOrganization>, JsonExportable, AttributeProvider {

	/** Default country for research organizations.
	 */
	public static final CountryCode DEFAULT_COUNTRY = CountryCode.FRANCE;

	/** Default type for research organizations.
	 */
	public static final ResearchOrganizationType DEFAULT_TYPE = ResearchOrganizationType.LABORATORY;

	private static final long serialVersionUID = -450531251083286848L;

	/** Default comparator.
	 */
	public static final Comparator<ResearchOrganization> DEFAULT_COMPARATOR = new Comparator<>() {
		@Override
		public int compare(ResearchOrganization o1, ResearchOrganization o2) {
			if (o1 == o2) {
				return 0;
			}
			if (o1 == null) {
				return Integer.MIN_VALUE;
			}
			if (o2 == null) {
				return Integer.MAX_VALUE;
			}
			int cmp = Objects.compare(o1.getType(), o2.getType(), (a, b) -> a.compareTo(b));
			if (cmp != 0) {
				return cmp;
			}
			cmp = StringUtils.compare(o1.getAcronym(), o2.getAcronym());
			if (cmp != 0) {
				return cmp;
			}
			cmp = StringUtils.compare(o1.getName(), o2.getName());
			if (cmp != 0) {
				return cmp;
			}
			cmp = compareCountry(o1.getCountry(), o2.getCountry());
			if (cmp != 0) {
				return cmp;
			}
			return Integer.compare(o1.getId(), o2.getId());
		}
		private int compareCountry(CountryCode c0, CountryCode c1) {
			if (c0 == c1) {
				return 0;
			}
			if (c0 == null) {
				return Integer.MIN_VALUE;
			}
			if (c1 == null) {
				return Integer.MAX_VALUE;
			}
			return c0.compareTo(c1);
		}
	};

	/** Identifier of the organization.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	/** Acronym of the research organization.
	 */
	@Column
	private String acronym;

	/** Name of the research organization.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String name;

	/** Textual description of the research organization.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	@Lob
	private String description;

	/** The country of the organization.
	 */
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private CountryCode country = DEFAULT_COUNTRY;

	/** The type of organization.
	 */
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ResearchOrganizationType type = DEFAULT_TYPE;

	/** The URL of the organization.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String organizationUrl;

	/** Members of the organization.
	 */
	@OneToMany(mappedBy = "researchOrganization", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Membership> members = new HashSet<>();

	/** Reference to the super organization.
	 */
	@ManyToOne
	private ResearchOrganization superOrganization;

	/** References to the sub organizations.
	 */
	@OneToMany(mappedBy = "superOrganization")
	private Set<ResearchOrganization> subOrganizations = new HashSet<>();

	/** Construct a research organization from the given values.
	 * 
	 * @param id the identifier of the organization.
	 * @param members the members of the organization.
	 * @param subOrgas the references to the suborganizations.
	 * @param superOrga the reference to the super organization.
	 * @param acronym the acronym of the organization.
	 * @param name the name of the organization.
	 * @param description the textual description of the organization.
	 */
	public ResearchOrganization(int id, Set<Membership> members, Set<ResearchOrganization> subOrgas,
			ResearchOrganization superOrga, String acronym, String name, String description) {
		this.id = id;
		if (members == null) {
			this.members = new HashSet<>();
		} else {
			this.members = members;
		}
		if (subOrgas == null) {
			this.subOrganizations = new HashSet<>();
		} else {
			this.subOrganizations = subOrgas;
		}
		this.superOrganization = superOrga;
		this.acronym = acronym;
		this.name = name;
		this.description = description;
	}

	/** Construct an empty research organization.
	 */
	public ResearchOrganization() {
		//
	}

	@Override
	public String toString() {
		return getAcronymOrName() + ":" + this.id; //$NON-NLS-1$
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.acronym);
		h = HashCodeUtils.add(h, this.name);
		h = HashCodeUtils.add(h, this.description);
		h = HashCodeUtils.add(h, this.country);
		h = HashCodeUtils.add(h, this.type);
		h = HashCodeUtils.add(h, this.organizationUrl);
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
		final ResearchOrganization other = (ResearchOrganization) obj;
		if (this.id != other.id) {
			return false;
		}
		if (!Objects.equals(this.acronym, other.acronym)) {
			return false;
		}
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.type, other.type)) {
			return false;
		}
		if (!Objects.equals(this.country, other.country)) {
			return false;
		}
		if (!Objects.equals(this.organizationUrl, other.organizationUrl)) {
			return false;
		}
		if (!Objects.equals(this.description, other.description)) {
			return false;
		}
		if (!Objects.equals(this.superOrganization, other.superOrganization)) {
			return false;
		}
		if (!Objects.equals(this.subOrganizations, other.subOrganizations)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(ResearchOrganization o) {
		return DEFAULT_COMPARATOR.compare(this, o);
	}

	/** {@inheritDoc}
	 * <p>The attributes that are not considered by this function are:<ul>
	 * <li>{@code id}</li>
	 * <li>{@code type}</li>
	 * <li>{@code authorships}</li>
	 * <li>{@code authors}</li>
	 * <li>{@code publicationYear}</li>
	 * </ul>
	 */
	@Override
	public void forEachAttribute(BiConsumer<String, Object> consumer) {
		assert consumer != null : "How to consume an attribute if the consumer is null?"; //$NON-NLS-1$
		if (!Strings.isNullOrEmpty(getAcronym())) {
			consumer.accept("acronym", getAcronym()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getName())) {
			consumer.accept("name", getName()); //$NON-NLS-1$
		}
		if (getType() != null) {
			consumer.accept("type", getType()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getDescription())) {
			consumer.accept("description", getDescription()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getOrganizationURL())) {
			consumer.accept("organizationURL", getOrganizationURL()); //$NON-NLS-1$
		}
		consumer.accept("country", getCountry()); //$NON-NLS-1$
	}

	@Override
	public final void toJson(JsonObject json) {
		json.addProperty("id", Integer.valueOf(getId())); //$NON-NLS-1$
		if (getSuperOrganization() != null) {
			json.addProperty("superOrganization", Integer.valueOf(getSuperOrganization().getId())); //$NON-NLS-1$
		}
		final JsonArray suborgas = new JsonArray();
		for (final ResearchOrganization suborga : getSubOrganizations()) {
			suborgas.add(Integer.valueOf(suborga.getId()));
		}
		if (suborgas.size() > 0) {
			json.add("subOrganizations", suborgas); //$NON-NLS-1$
		}
		//
		forEachAttribute((name, value) -> JsonUtils.defaultBehavior(json, name, value));
	}

	/** Replies the identifier of the research organization.
	 *
	 * @return the identifier.
	 */
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

	/** Replies the members of the organization.
	 *
	 * @return the members.
	 */
	public Set<Membership> getMembers() {
		return this.members;
	}

	/** Change the members of the organization.
	 *
	 * @param members the members.
	 */
	public void setMembers(Set<Membership> members) {
		if (members == null) {
			this.members = new HashSet<>();
		} else {
			this.members = members;
		}
	}

	/** Replies the suborganizations.
	 *
	 * @return the set of suborganizations.
	 */
	public Set<ResearchOrganization> getSubOrganizations() {
		return this.subOrganizations;
	}

	/** Change the suborganizations.
	 *
	 * @param set the set of suborganizations.
	 */
	public void setSubOrganizations(Set<ResearchOrganization> set) {
		if (set == null) {
			this.subOrganizations = new HashSet<>();
		} else {
			this.subOrganizations = set;
		}
	}

	/** Replies the reference to the super organization.
	 *
	 * @return the super organization or {@code null}.
	 */
	public ResearchOrganization getSuperOrganization() {
		return this.superOrganization;
	}

	/** Change the reference to the super organization.
	 *
	 * @param orga the super organization or {@code null}.
	 */
	public void setSuperOrganization(ResearchOrganization orga) {
		this.superOrganization = orga;
	}

	/** Replies the acronym or the name of the research organization.
	 *
	 * @return the acronym or name.
	 */
	public String getAcronymOrName() {
		return Strings.isNullOrEmpty(this.acronym) ? this.name : this.acronym;
	}

	/** Replies the acronym of the research organization.
	 *
	 * @return the acronym.
	 */
	public String getAcronym() {
		return this.acronym;
	}

	/** Change the acronym of the research organization.
	 *
	 * @param acronym the acronym.
	 */
	public void setAcronym(String acronym) {
		this.acronym = Strings.emptyToNull(acronym);
	}

	/** Replies the name of the research organization.
	 *
	 * @return the name.
	 */
	public String getName() {
		return this.name;
	}

	/** Change the name of the research organization.
	 *
	 * @param name the name.
	 */
	public void setName(String name) {
		this.name = Strings.emptyToNull(name);
	}

	/** Replies the textual description.
	 *
	 * @return the textual description.
	 */
	public String getDescription() {
		return this.description;
	}

	/** Change the textual description.
	 *
	 * @param description the textual description.
	 */
	public void setDescription(String description) {
		this.description = Strings.emptyToNull(description);
	}

	/** Replies the name of the country of the organization.
	 *
	 * @return the display name of the country.
	 */
	public String getCountryDisplayName() {
		return CountryCodeUtils.getDisplayCountry(this.country);
	}

	/** Replies the country of the organization.
	 *
	 * @return the country, never {@code null}.
	 */
	public CountryCode getCountry() {
		return this.country;
	}

	/** Change the country of the organization.
	 *
	 * @param country the country, or {@code null} if the country is the default country.
	 * @see #DEFAULT_COUNTRY
	 */
	public void setCountry(CountryCode country) {
		if (country == null) {
			this.country = DEFAULT_COUNTRY;
		} else {
			this.country = country;
		}
	}

	/** Change the country of the organization.
	 *
	 * @param country the country.
	 */
	public final void setCountry(String country) {
		if (Strings.isNullOrEmpty(country)) {
			setCountry((CountryCode) null);
		} else {
			setCountry(CountryCodeUtils.valueOfCaseInsensitive(country));
		}
	}

	/** Replies the URL of the research organization.
	 *
	 * @return the URL.
	 */
	public String getOrganizationURL() {
		return this.organizationUrl;
	}

	/** Replies the URL of the research organization.
	 *
	 * @return the URL.
	 */
	public final URL getOrganizationURLObject() {
		try {
			return new URL(getOrganizationURL());
		} catch (MalformedURLException ex) {
			return null;
		}
	}

	/** Change the URL of the research organization.
	 *
	 * @param url the URL.
	 */
	public void setOrganizationURL(String url) {
		this.organizationUrl = Strings.emptyToNull(url);
	}

	/** Change the URL of the research organization.
	 *
	 * @param url the URL.
	 */
	public final void setOrganizationURL(URL url) {
		final String value;
		if (url != null) {
			value = url.toExternalForm();
		} else {
			value = null;
		}
		setOrganizationURL(value);
	}

	/** Replies the type of organization.
	 *
	 * @return the type.
	 */
	public ResearchOrganizationType getType() {
		return this.type;
	}

	/** Change the type of organization.
	 *
	 * @param type the country, or {@code null} if the type is the default one.
	 * @see #DEFAULT_TYPE
	 */
	public void setType(ResearchOrganizationType type) {
		if (type == null) {
			this.type = DEFAULT_TYPE;
		} else {
			this.type = type;
		}
	}

	/** Change the type of the organization.
	 *
	 * @param type the type.
	 */
	public final void setType(String type) {
		if (Strings.isNullOrEmpty(type)) {
			setType((ResearchOrganizationType) null);
		} else {
			setType(ResearchOrganizationType.valueOfCaseInsensitive(type));
		}
	}

}
