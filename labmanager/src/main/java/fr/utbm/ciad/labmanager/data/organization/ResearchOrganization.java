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

package fr.utbm.ciad.labmanager.data.organization;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.google.common.base.Strings;
import fr.utbm.ciad.labmanager.data.AttributeProvider;
import fr.utbm.ciad.labmanager.data.EntityUtils;
import fr.utbm.ciad.labmanager.data.IdentifiableEntity;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.teaching.TeachingActivity;
import fr.utbm.ciad.labmanager.utils.HashCodeUtils;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import fr.utbm.ciad.labmanager.utils.io.json.JsonUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.springframework.context.support.MessageSourceAccessor;

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
public class ResearchOrganization implements Serializable, JsonSerializable, Comparable<ResearchOrganization>, AttributeProvider, IdentifiableEntity {

	/** Default separator between the acronym and name.
	 *
	 * @see #getAcronymAndName()
	 * @since 4.0
	 */
	public static final String ACRONYM_NAME_SEPARATOR = "-"; //$NON-NLS-1$

	private static final String FULL_ACRONYM_NAME_SEPARATOR = new StringBuilder().append(" ").append(ACRONYM_NAME_SEPARATOR).append(" ").toString(); //$NON-NLS-1$ //$NON-NLS-2$

	private static final long serialVersionUID = -450531251083286848L;

	/** Default type for research organizations.
	 */
	public static final ResearchOrganizationType DEFAULT_TYPE = ResearchOrganizationType.LABORATORY;

	private static final String RNSR_URL = "https://appliweb.dgri.education.fr/rnsr/PresenteStruct.jsp?PUBLIC=OK&numNatStruct="; //$NON-NLS-1$

	/** Identifier of the organization.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private long id;

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

	/** Indicates if the organization is marked as a major organization on the server.
	 *
	 * @since 2.2
	 */
	@Column
	private boolean majorOrganization;

	/** Number of the organization in the "Repertoire National des Structures de Recherche"
	 * of the French Ministry of Research.
	 *
	 * @since 2.2
	 */
	@Column
	private String rnsr;

	/** Identifier of the organization for the French Ministry of Research.
	 *
	 * @since 2.2
	 */
	@Column
	private String nationalIdentifier;

	/** The country of the organization.
	 */
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private CountryCode country = CountryCode.getDefault();

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
	@OneToMany(mappedBy = "researchOrganization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Membership> memberships = new HashSet<>();

	/** Reference to the sub organizations.
	 */
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "SuperOrganization_SubOrganization", 
        joinColumns = { @JoinColumn(name = "superorganization_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "suborganization_id") }
    )
	private Set<ResearchOrganization> subOrganizations;

	/** Reference to the super organizations.
	 */
	@ManyToMany(mappedBy = "subOrganizations", fetch = FetchType.EAGER)
	private Set<ResearchOrganization> superOrganizations;

	/** References to the postal addresses of the organization.
	 */
	@OneToMany(fetch = FetchType.LAZY)
	private Set<OrganizationAddress> addresses = new HashSet<>();

	/** Name of the logo of the project if it has one.
	 *
	 * @since 3.2
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String pathToLogo;

	/** Indicates if the address was validated by an authority.
	 */
	@Column(nullable = false)
	private boolean validated;

	/** List of teaching activities for the organization.
	 */
	@OneToMany(mappedBy = "university", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<TeachingActivity> teachingActivities;

	/** Construct a research organization from the given values.
	 * 
	 * @param id the identifier of the organization.
	 * @param members the members of the organization.
	 * @param subOrgas the references to the sub organizations.
	 * @param superOrgas the references to the super organizations.
	 * @param acronym the acronym of the organization.
	 * @param name the name of the organization.
	 * @param description the textual description of the organization.
	 */
	public ResearchOrganization(int id, Set<Membership> members, Set<ResearchOrganization> subOrgas,
			Set<ResearchOrganization> superOrgas, String acronym, String name, String description) {
		this.id = id;
		if (members == null) {
			this.memberships = new HashSet<>();
		} else {
			this.memberships = members;
		}
		if (superOrgas == null) {
			this.superOrganizations = new HashSet<>();
		} else {
			this.superOrganizations = superOrgas;
		}
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
	public int hashCode() {
		if (this.id != 0) {
			return Long.hashCode(this.id);
		}
		var h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.acronym);
		h = HashCodeUtils.add(h, this.name);
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final var other = (ResearchOrganization) obj;
		if (this.id != 0 && other.id != 0) {
			return this.id == other.id;
		}
		return Objects.equals(this.acronym, other.acronym)
				&& Objects.equals(this.name, other.name);
	}

	@Override
	public int compareTo(ResearchOrganization o) {
		return EntityUtils.getPreferredResearchOrganizationComparator().compare(this, o);
	}

	/** {@inheritDoc}
	 * <p>The attributes that are not considered by this function are:<ul>
	 * <li>{@code id}</li>
	 * <li>{@code superOrganization}</li>
	 * <li>{@code subOrganizations}</li>
	 * </ul>
	 */
	@Override
	public void forEachAttribute(MessageSourceAccessor messages, Locale locale, AttributeConsumer consumer) throws IOException {
		assert consumer != null : "How to consume an attribute if the consumer is null?"; //$NON-NLS-1$
		if (getId() != 0) {
			consumer.accept("id", Long.valueOf(getId())); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getAcronym())) {
			consumer.accept("acronym", getAcronym()); //$NON-NLS-1$
		}
		if (getCountry() != null) {
			consumer.accept("country", getCountry()); //$NON-NLS-1$
			consumer.accept("countryLabel", getCountryDisplayName()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getDescription())) {
			consumer.accept("description", getDescription()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getName())) {
			consumer.accept("name", getName()); //$NON-NLS-1$
		}
		consumer.accept("majorOrganization", Boolean.valueOf(isMajorOrganization())); //$NON-NLS-1$
		if (!Strings.isNullOrEmpty(getRnsr())) {
			consumer.accept("rnsr", getRnsr()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getNationalIdentifier())) {
			consumer.accept("nationalIdentifier", getNationalIdentifier()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getOrganizationURL())) {
			consumer.accept("organizationURL", getOrganizationURL()); //$NON-NLS-1$
		}
		if (getType() != null) {
			consumer.accept("type", getType()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getPathToLogo())) {
			consumer.accept("pathToLogo", getPathToLogo()); //$NON-NLS-1$
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
		final var organizations = JsonUtils.cache(generator);
		final var persons = JsonUtils.cache(generator);
		//
		generator.writeArrayFieldStart("addresses"); //$NON-NLS-1$
		for (final var address : getAddresses()) {
			organizations.writeReferenceOrObject(address, () -> {
				JsonUtils.writeObjectAndAttributes(generator, address);
			});
		}
		generator.writeEndArray();
		//
		generator.writeArrayFieldStart("superOrganizations"); //$NON-NLS-1$
		for (final var superorga : getSuperOrganizations()) {
			organizations.writeReferenceOrObject(superorga, () -> {
				JsonUtils.writeObjectAndAttributes(generator, superorga);
			});
		}
		generator.writeEndArray();
		//
		generator.writeArrayFieldStart("subOrganizations"); //$NON-NLS-1$
		for (final var suborga : getSubOrganizations()) {
			organizations.writeReferenceOrObject(suborga, () -> {
				JsonUtils.writeObjectAndAttributes(generator, suborga);
			});
		}
		generator.writeEndArray();
		//
		generator.writeArrayFieldStart("memberships"); //$NON-NLS-1$
		for (final var membership : getMemberships()) {
			generator.writeStartObject();
			membership.forEachAttribute((attrName, attrValue) -> {
				JsonUtils.writeField(generator, attrName, attrValue);
			});
			persons.writeReferenceOrObjectField("person", membership.getPerson(), () -> { //$NON-NLS-1$
				JsonUtils.writeObjectAndAttributes(generator, membership.getPerson());
			});
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
	public long getId() {
		return this.id;
	}

	/** Change the identifier of the research organization.
	 *
	 * @param id the identifier.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/** Replies the members of the organization.
	 *
	 * @return the members.
	 */
	public Set<Membership> getMemberships() {
		return this.memberships;
	}

	/** Change the members of the organization.
	 *
	 * @param members the members.
	 */
	public void setMemberships(Set<Membership> members) {
		if (members == null) {
			this.memberships = new HashSet<>();
		} else {
			this.memberships = members;
		}
	}

	/** Replies the super organizations.
	 *
	 * @return the set of super organizations.
	 */
	public Set<ResearchOrganization> getSuperOrganizations() {
		if (this.superOrganizations == null) {
			this.superOrganizations = new HashSet<>();
		}
		return this.superOrganizations;
	}

	/** Change the super organizations. This function does not change the set
	 * of suborganizations of the provided super organizations.
	 *
	 * @param newSuperOrganizations the set of super organizations that will replace the current super organizations.
	 * @see #setSubOrganizations(Set)
	 */
	public void setSuperOrganizations(Set<ResearchOrganization> newSuperOrganizations) {
		if (this.superOrganizations == null) {
			this.superOrganizations = new HashSet<>();
		} else {
			this.superOrganizations.clear();
		}
		if (newSuperOrganizations != null) {
			this.superOrganizations.addAll(newSuperOrganizations);
		}
	}

	/** Replies the sub organizations.
	 *
	 * @return the set of sub organizations.
	 * @see #setSuperOrganizations(Set)
	 */
	public Set<ResearchOrganization> getSubOrganizations() {
		if (this.subOrganizations == null) {
			this.subOrganizations = new HashSet<>();
		}
		return this.subOrganizations;
	}

	/** Change the sub organizations. This function does not change the set
	 * of super organizations of the provided sub organizations.
	 *
	 * @param newSubOrganizations the set of sub organizations that will replace the current suborganizations.
	 * @see #setSuperOrganizations(Set)
	 */
	public void setSubOrganizations(Set<ResearchOrganization> newSubOrganizations) {
		if (this.subOrganizations == null) {
			this.subOrganizations = new HashSet<>();
		} else {
			this.subOrganizations.clear();
		}
		if (newSubOrganizations != null) {
			this.subOrganizations.addAll(newSubOrganizations);
		}
	}

	/** Replies the acronym or the name of the research organization, that order.
	 *
	 * @return the acronym or name.
	 * @see #getNameOrAcronym()
	 * @see #getAcronym()
	 * @see #getName()
	 * @see #getAcronymAndName()
	 */
	public String getAcronymOrName() {
		return Strings.isNullOrEmpty(this.acronym) ? this.name : this.acronym;
	}

	/** Replies the name or the acronym of the research organization, that order.
	 *
	 * @return the name or acronym.
	 * @see #getAcronymOrName()
	 * @see #getAcronym()
	 * @see #getName()
	 */
	public String getNameOrAcronym() {
		return Strings.isNullOrEmpty(this.name) ? this.acronym: this.name;
	}

	/** Replies the acronym and the name of the research organization, if they exist, with the format {@code "Acronym - Name"}.
	 *
	 * @return the acronym and the name, or {@code null} if there is neither acronym nor name.
	 * @see #getAcronymOrName()
	 * @see #ACRONYM_NAME_SEPARATOR
	 * @since 4.0
	 */
	public String getAcronymAndName() {
		return getAcronymAndName(FULL_ACRONYM_NAME_SEPARATOR);
	}

	/** Replies the acronym and the name of the research organization, if they exist.
	 *
	 * @param separator the string to be written between the acronym and the name.
	 * @return the acronym and the name, or {@code null} if there is neither acronym nor name.
	 * @see #getAcronymOrName()
	 * @since 4.0
	 */
	public String getAcronymAndName(String separator) {
		final var buffer = new StringBuilder();
		if (!Strings.isNullOrEmpty(this.acronym)) {
			buffer.append(this.acronym);
		}
		if (!Strings.isNullOrEmpty(this.name)) {
			if (buffer.length() > 0 && !Strings.isNullOrEmpty(separator)) {
				buffer.append(separator);
			}
			buffer.append(this.name);
		}
		return Strings.emptyToNull(buffer.toString());
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
		return this.country.getDisplayCountry();
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
			this.country = CountryCode.getDefault();
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
			setCountry(CountryCode.valueOfCaseInsensitive(country));
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

	/** Replies the addresses of this organization.
	 *
	 * @return the addresses.
	 * @since 2.0
	 */
	public Set<OrganizationAddress> getAddresses() {
		if (this.addresses == null) {
			this.addresses = new TreeSet<>(EntityUtils.getPreferredOrganizationAddressComparator());
		}
		return this.addresses;
	}

	/** Change the addresses of this organization.
	 *
	 * @param addresses the addresses.
	 * @since 2.0
	 */
	public void setAddresses(Set<OrganizationAddress> addresses) {
		if (this.addresses == null) {
			this.addresses = new TreeSet<>(EntityUtils.getPreferredOrganizationAddressComparator());
		} else {
			this.addresses.clear();
		}
		if (addresses != null) {
			this.addresses.addAll(addresses);
		}
	}

	/** Replies the number of the organization in the
	 * "Repertoire National des Structures de Recherche" (RNSR).
	 *
	 * @return the RNSR number.
	 * @see #getRnsrUrl()
	 */
	public String getRnsr() {
		return this.rnsr;
	}

	/** change the number of the organization in the
	 * "Repertoire National des Structures de Recherche" (RNSR).
	 *
	 * @param rnsr the RNSR number.
	 */
	public void setRnsr(String rnsr) {
		this.rnsr = Strings.emptyToNull(rnsr);
	}

	/** Replies the URL of the page of the organization on the
	 * "Repertoire National des Structures de Recherche" (RNSR).
	 *
	 * @return the URL to the RNSR, or {@code null} if the organization has no RNSR number.
	 * @see #getRnsr()
	 */
	public URL getRnsrUrl() {
		final var number = getRnsr();
		if (!Strings.isNullOrEmpty(number)) {
			try {
				return new URL(RNSR_URL + number);
			} catch (MalformedURLException ex) {
				return null;
			}
		}
		return null;
	}

	/** Replies the number of the organization for the national ministry of research.
	 *
	 * @return the national identifier.
	 */
	public String getNationalIdentifier() {
		return this.nationalIdentifier;
	}

	/** Change the number of the organization for the national ministry of research.
	 *
	 * @param identifier the national identifier.
	 */
	public void setNationalIdentifier(String identifier) {
		this.nationalIdentifier = Strings.emptyToNull(identifier);
	}

	/** Replies if this organization is mared as a major organization.
	 *
	 * @return {@code true} if this organization is major.
	 */
	public boolean isMajorOrganization() {
		return this.majorOrganization;
	}

	/** Change if this organization is marked as a major organization.
	 * The major organization is those for which the labmanager is deployed.
	 *
	 * @param major {@code true} if this organization is major.
	 */
	public void setMajorOrganization(boolean major) {
		this.majorOrganization = major;
	}

	/** Change if this organization is marked as a major organization.
	 * The major organization is those for which the labmanager is deployed.
	 *
	 * @param major {@code true} if this organization is major.
	 */
	public final void setMajorOrganization(Boolean major) {
		if (major == null) {
			setMajorOrganization(false);
		} else {
			setMajorOrganization(major.booleanValue());
		}
	}

	/** Replies a path to a downloadable logo that represents the organization.
	 *
	 * @return the path or {@code null}.
	 * @since 3.2
	 */
	public String getPathToLogo() {
		return this.pathToLogo;
	}

	/** Change the path to a downloadable logo that represents the organization.
	 *
	 * @param path the path or {@code null}.
	 */
	public void setPathToLogo(String path) {
		this.pathToLogo = Strings.emptyToNull(path);
	}

	/** Replies if this organization was validated by an authority.
	 *
	 * @return {@code true} if the organization is validated.
	 * @since 3.2
	 */
	public boolean isValidated() {
		return this.validated;
	}

	/** Change the flag that indicates if this organization was validated by an authority.
	 *
	 * @param validated {@code true} if the organization is validated.
	 * @since 3.2
	 */
	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	/** Change the flag that indicates if this organization was validated by an authority.
	 *
	 * @param validated {@code true} if the organization is validated.
	 * @since 3.2
	 */
	public final void setValidated(Boolean validated) {
		if (validated == null) {
			setValidated(false);
		} else {
			setValidated(validated.booleanValue());
		}
	}

	/** Replies if this organization is located outside Europe.
	 *
	 * @return {@code true} if the organization is outside Europe.
	 * @since 3.6
	 */
	public boolean isInternational() {
		return !getCountry().isEuropeanSovereign();
	}

	/** Replies if this organization is located inside Europe.
	 *
	 * @return {@code true} if the organization is inside Europe.
	 * @since 3.6
	 */
	public boolean isEuropean() {
		return getCountry().isEuropeanSovereign();
	}

	/** Replies if this organization is located in France.
	 *
	 * @return {@code true} if the organization is in France.
	 * @since 3.6
	 */
	public boolean isFrench() {
		return getCountry().isFrance();
	}

	@Override
	public String toString() {
		return new StringBuilder(getClass().getName()).append("@ID=").append(getId()).toString(); //$NON-NLS-1$
	}

	/** Replies the list of teaching activities associated to the organization.
	 *
	 * @return the list of teaching activities.
	 * @since 4.0
	 */
	public Set<TeachingActivity> getTeachingActivities() {
		if (this.teachingActivities == null) {
			this.teachingActivities = new HashSet<>();
		}
		return this.teachingActivities;
	}

	/** Change the list of teaching activities associated to the organization.
	 *
	 * @param list the list of activities.
	 * @since 4.0
	 */
	public void setTeachingActivities(Set<TeachingActivity> list) {
		if (this.teachingActivities != null) {
			this.teachingActivities.clear();
		} else {
			this.teachingActivities = new HashSet<>();
		}
		if (list != null) {
			this.teachingActivities.addAll(list);
		}
	}

}
