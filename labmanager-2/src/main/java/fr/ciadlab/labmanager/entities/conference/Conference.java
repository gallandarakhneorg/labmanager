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

package fr.ciadlab.labmanager.entities.conference;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Predicate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.AttributeProvider;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.IdentifiableEntity;
import fr.ciadlab.labmanager.io.json.JsonUtils;
import fr.ciadlab.labmanager.io.json.JsonUtils.CachedGenerator;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import org.arakhne.afc.util.IntegerList;
import org.arakhne.afc.util.ListUtil;

/** Scientific or scientific culture dissemination conference.
 * This class provides all the necessary information and tools for managing the conferences and their quality indicators.
 * 
 * @author $Author: sgalland$
 * @author $Author: anoubli$
 * @author $Author: bpdj$
 * @author $Author: pgoubet$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Entity
@Table(name = "Conferences")
public class Conference implements Serializable, JsonSerializable, AttributeProvider, IdentifiableEntity {

	private static final long serialVersionUID = -2286554831898694393L;

	/** Identifier of the journal in the database.
	 * 
	 * <p>Using this instead of {@link GenerationType#IDENTITY} allows for JOINED or TABLE_PER_CLASS inheritance types to work.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private int id;

	/** Acronym of the conference.
	 */
	@Column
	private String acronym;

	/** Name of the conference.
	 */
	@Column
	private String name;

	/** Name of the publisher of the conference.
	 */
	@Column
	private String publisher;

	/** URL to the page of the conference if it is independent of the annual occurrence.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String conferenceUrl;

	/** Identifier of the conference on the Core ranking website.
	 */
	@Column
	private String coreId;

	/** ISBN number if the conference has one.
	 */
	@Column
	private String isbn;

	/** ISSN number if the conference has one.
	 */
	@Column
	private String issn;

	/** Indicates if the journal is an open-access journal. If this field
	 * is not set, we don't know if the journal is open access.
	 */
	@Column(nullable = true)
	private Boolean openAccess;

	/** Indicates if the journal was validated by an authority.
	 */
	@Column(nullable = false)
	private boolean validated;

	/** History of the quality indicators for this conference.
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "conference_conference_annual_indicators_mapping", 
	joinColumns = {
			@JoinColumn(name = "conference_id", referencedColumnName = "id")
	},
	inverseJoinColumns = {
			@JoinColumn(name = "indicators_id", referencedColumnName = "id")
	})
	@MapKey(name = "referenceYear")
	private Map<Integer, ConferenceQualityAnnualIndicators> qualityIndicators;

	/** Reference to the super conference.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Conference enclosingConference;

	/** Construct an empty conference.
	 */
	public Conference() {
		//
	}

	/** Construct by copying the given conference (exception id and quality indicators).
	 *
	 * @param conference the conference to be copied.
	 */
	public Conference(Conference conference) {
		assert conference != null;
		this.acronym = conference.getAcronym();
		this.name = conference.getName();
		this.publisher = conference.getPublisher();
		this.conferenceUrl = conference.getConferenceURL();
		this.coreId = conference.getCoreId();
		this.publisher = conference.getPublisher();
		this.isbn = conference.getISBN();
		this.issn = conference.getISSN();
		this.openAccess = conference.getOpenAccess();
		this.validated = conference.isValidated();
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.acronym);
		h = HashCodeUtils.add(h, this.name);
		h = HashCodeUtils.add(h, this.publisher);
		h = HashCodeUtils.add(h, this.conferenceUrl);
		h = HashCodeUtils.add(h, this.coreId);
		h = HashCodeUtils.add(h, this.isbn);
		h = HashCodeUtils.add(h, this.issn);
		h = HashCodeUtils.add(h, this.openAccess);
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
		final Conference other = (Conference) obj;
		if (this.id != other.id) {
			return false;
		}
		if (!Objects.equals(this.acronym, other.acronym)) {
			return false;
		}
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.publisher, other.publisher)) {
			return false;
		}
		if (!Objects.equals(this.conferenceUrl, other.conferenceUrl)) {
			return false;
		}
		if (!Objects.equals(this.coreId, other.coreId)) {
			return false;
		}
		if (!Objects.equals(this.isbn, other.isbn)) {
			return false;
		}
		if (!Objects.equals(this.issn, other.issn)) {
			return false;
		}
		if (!Objects.equals(this.openAccess, other.openAccess)) {
			return false;
		}
		if (this.validated != other.validated) {
			return false;
		}
		return true;
	}

	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		if (getId() != 0) {
			consumer.accept("id", Integer.valueOf(getId())); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getAcronym())) {
			consumer.accept("acronym", getAcronym()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getName())) {
			consumer.accept("name", getName()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getPublisher())) {
			consumer.accept("publisher", getPublisher()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getConferenceURL())) {
			consumer.accept("conferenceURL", getConferenceURL()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getCoreId())) {
			consumer.accept("coreId", getCoreId()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getISBN())) {
			consumer.accept("isbn", getISBN()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getISSN())) {
			consumer.accept("issn", getISSN()); //$NON-NLS-1$
		}
		if (getOpenAccess() != null) {
			consumer.accept("openAccess", getOpenAccess()); //$NON-NLS-1$
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
		generator.writeObjectFieldStart("qualityIndicators"); //$NON-NLS-1$
		for (final Entry<Integer, ConferenceQualityAnnualIndicators> indicators : getQualityIndicators().entrySet()) {
			generator.writeFieldId(indicators.getKey().intValue());
			generator.writeObject(indicators.getValue());
		}
		//
		final CachedGenerator conferences = JsonUtils.cache(generator);
		conferences.writeReferenceOrObjectField("enclosingConference", getEnclosingConference(), () -> { //$NON-NLS-1$
			JsonUtils.writeObjectAndAttributes(generator, getEnclosingConference());
		});
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

	/** Change the identifier of the journal in the database.
	 *
	 * @param id the identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/** Replies the acronym of the conference.
	 *
	 * @return the acronym.
	 */
	public String getAcronym() {
		return this.acronym;
	}

	/** Change the acronym of the conference.
	 *
	 * @param acronym the acronym.
	 */
	public void setAcronym(String acronym) {
		this.acronym = Strings.emptyToNull(acronym);
	}

	/** Replies the name of the conference.
	 *
	 * @return the name.
	 */
	public String getName() {
		return this.name;
	}

	/** Change the name of the conference.
	 *
	 * @param name the name.
	 */
	public void setName(String name) {
		this.name = Strings.emptyToNull(name);
	}

	/** Replies the acronym or the name of the conference, in that order.
	 *
	 * @return the acronym or name.
	 * @see #getNameOrAcronym()
	 * @see #getAcronym()
	 * @see #getName()
	 */
	public String getAcronymOrName() {
		return Strings.isNullOrEmpty(this.acronym) ? this.name : this.acronym;
	}

	/** Replies the name or the acronym of the conference, in that order.
	 *
	 * @return the name or acronym.
	 * @see #getAcronymOrName()
	 * @see #getAcronym()
	 * @see #getName()
	 */
	public String getNameOrAcronym() {
		return Strings.isNullOrEmpty(this.name) ? this.acronym: this.name;
	}

	/** Replies the name of publisher of the journal.
	 *
	 * @return the name.
	 */
	public String getPublisher() {
		return this.publisher;
	}

	/** Change the name of publisher of the journal.
	 *
	 * @param name the name.
	 */
	public void setPublisher(String name) {
		this.publisher = Strings.emptyToNull(name);
	}

	/** Replies the URL of conference if it is independent of the annual occurrence.
	 *
	 * @return the URL.
	 */
	public String getConferenceURL() {
		return this.conferenceUrl;
	}

	/** Replies the URL of conference if it is independent of the annual occurrence.
	 *
	 * @return the URL.
	 */
	public final URL getConferenceURLObject() {
		try {
			return new URL(getConferenceURL());
		} catch (MalformedURLException ex) {
			return null;
		}
	}

	/** Change the URL of conference if it is independent of the annual occurrence.
	 *
	 * @param url the URL.
	 */
	public void setConferenceURL(String url) {
		this.conferenceUrl = Strings.emptyToNull(url);
	}

	/** Change the URL of the conference if it is independent of the annual occurrence.
	 *
	 * @param url the URL.
	 */
	public final void setConferenceURL(URL url) {
		setConferenceURL(url == null ? null : url.toExternalForm());
	}

	/** Replies the identifier of the conference on the CORE website.
	 *
	 * @return the identifier.
	 */
	public String getCoreId() {
		return this.coreId;
	}

	/** Change the identifier of the conference on the CORE website.
	 *
	 * @param id the identifier.
	 */
	public void setCoreId(String id) {
		this.coreId = Strings.emptyToNull(id);
	}

	/** Replies if the conference is open access. If it is not known, this function returns {@code null}.
	 *
	 * @return the open-access status of the conference, or {@code null} if it is not known.
	 */
	public Boolean getOpenAccess() {
		return this.openAccess;
	}

	/** Change the flag that indicates if the conference is open access. If it is not known, this function returns {@code null}.
	 *
	 * @param openAccess the open-access status of the conference, or {@code null} if it is not known.
	 */
	public void setOpenAccess(Boolean openAccess) {
		this.openAccess = openAccess;
	}

	/** Replies the quality indicators of the conference.
	 *
	 * @return the indicators.
	 */
	public Map<Integer, ConferenceQualityAnnualIndicators> getQualityIndicators() {
		if (this.qualityIndicators == null) {
			this.qualityIndicators = new TreeMap<>();
		}
		return this.qualityIndicators;
	}

	/** Replies the quality indicators of the conference for the given year.
	 *
	 * @param year the year to search for.
	 * @return the indicators or {@code null} if none were defined.
	 */
	public final ConferenceQualityAnnualIndicators getQualityIndicatorsForYear(int year) {
		final Map<Integer, ConferenceQualityAnnualIndicators> allIndicators = getQualityIndicators();
		if (allIndicators == null) {
			return null;
		}
		return allIndicators.get(Integer.valueOf(year));
	}

	/** Replies the quality indicators that is fitting the given predicates and with an year lower or equal
	 * to the given value.
	 *
	 * @param year the year to search for.
	 * @param selector the object that permits to select the best quality indicators.
	 * @return the indicators or {@code null} if none were defined.
	 */
	public final ConferenceQualityAnnualIndicators getQualityIndicatorsFor(int year, Predicate<ConferenceQualityAnnualIndicators> selector) {
		final Map<Integer, ConferenceQualityAnnualIndicators> allIndicators = getQualityIndicators();
		if (allIndicators != null) {
			final IntegerList ids = new IntegerList(allIndicators.keySet());
			final int start = ListUtil.floorIndex(ids, (a, b) -> Integer.compare(a.intValue(), b.intValue()), Integer.valueOf(year));
			for (int i = start; i >= 0; --i) {
				final ConferenceQualityAnnualIndicators indicators = allIndicators.get(ids.get(i));
				if (indicators != null && selector.test(indicators)) {
					return indicators;
				}
			}
		}
		return null;
	}

	/** Replies if the conference has quality indicators for the given year.
	 *
	 * @param year the year to search for.
	 * @return {@code true} if the journal has quality indicators.
	 */
	public boolean hasQualityIndicatorsForYear(int year) {
		return getQualityIndicatorsForYear(year) != null;
	}

	/** Replies the CORE index of the conference from the CORE source.
	 * If there is no index known for the given year, this function replies
	 * the index for the highest year that is below the given one. 
	 *
	 * @param year the year to search for.
	 * @return the index of the conference for the given year, never {@code null}.
	 */
	public CoreRanking getCoreIndexByYear(int year) {
		final ConferenceQualityAnnualIndicators indicators = getQualityIndicatorsFor(year, it -> it.getCoreIndex() != null);
		if (indicators != null) {
			final CoreRanking ranking = indicators.getCoreIndex();
			if (ranking != null) {
				return ranking;
			}
		}
		return CoreRanking.NR;
	}

	/** Change the index of the conference from the CORE source.
	 *
	 * @param year the year to search for.
	 * @param index the CORE index of the conference for the given year, or {@code null} if not defined.
	 * @return the impacted annual indicators object
	 */
	public ConferenceQualityAnnualIndicators setCoreIndexByYear(int year, CoreRanking index) {
		ConferenceQualityAnnualIndicators indicators = getQualityIndicatorsForYear(year);
		if (indicators != null) {
			indicators.setCoreIndex(index);
		} else {
			indicators = new ConferenceQualityAnnualIndicators(year, index);
			if (this.qualityIndicators == null) {
				this.qualityIndicators = new TreeMap<>();
			}
			this.qualityIndicators.put(Integer.valueOf(year), indicators);
		}
		return indicators;
	}

	/** Replies if the conference has CORE index for the given year.
	 * If there is no CORE index known for the given year, this function replies
	 * the CORE index for the highest year that is below the given one. 
	 *
	 * @param year the year to search for.
	 * @return {@code true} if the journal has CORE index.
	 */
	public boolean hasCoreIndexForYear(int year) {
		final CoreRanking index = getCoreIndexByYear(year);
		return index != CoreRanking.NR;
	}

	/** Replies the ISBN number that is associated to this conference.
	 *
	 * @return the ISBN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/ISBN"
	 */
	public String getISBN() {
		return this.isbn;
	}

	/** Change the ISBN number that is associated to this conference.
	 *
	 * @param isbn the ISBN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/ISBN"
	 */
	public void setISBN(String isbn) {
		this.isbn = Strings.emptyToNull(isbn);
	}

	/** Replies the ISSN number that is associated to this conference.
	 *
	 * @return the ISSN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/International_Standard_Serial_Number"
	 */
	public String getISSN() {
		return this.issn;
	}

	/** Change the ISSN number that is associated to this conference.
	 *
	 * @param issn the ISSN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/International_Standard_Serial_Number"
	 */
	public void setISSN(String issn) {
		this.issn = Strings.emptyToNull(issn);
	}

	/** Replies if this conference was validated by an authority.
	 *
	 * @return {@code true} if the conference is validated.
	 */
	public boolean isValidated() {
		return this.validated;
	}

	/** Change the flag that indicates if this conference was validated by an authority.
	 *
	 * @param validated {@code true} if the conference is validated.
	 */
	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	/** Change the flag that indicates if this conference was validated by an authority.
	 *
	 * @param validated {@code true} if the conference is validated.
	 */
	public final void setValidated(Boolean validated) {
		if (validated == null) {
			setValidated(false);
		} else {
			setValidated(validated.booleanValue());
		}
	}

	/** Replies the reference to the enclosing conference.
	 *
	 * @return the enclosing conference or {@code null}.
	 */
	public Conference getEnclosingConference() {
		return this.enclosingConference;
	}

	/** Change the reference to the enclosing conference.
	 *
	 * @param conf the enclosing conference or {@code null}.
	 */
	public void setEnclosingConference(Conference conf) {
		this.enclosingConference = conf;
	}

}
