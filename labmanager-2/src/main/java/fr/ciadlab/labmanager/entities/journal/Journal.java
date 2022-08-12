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

package fr.ciadlab.labmanager.entities.journal;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import fr.ciadlab.labmanager.io.json.JsonUtils;
import fr.ciadlab.labmanager.utils.AttributeProvider;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.util.IntegerList;
import org.arakhne.afc.util.ListUtil;

/** Scientific or scientific culture dissemination journal.
 * This class provides all the necessary information and tools for managing the journals and their quality indicators.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "Journals")
public class Journal implements Serializable, JsonSerializable, AttributeProvider {

	private static final long serialVersionUID = -2046765660549008074L;

	private static final String SCIMAGO_URL = "https://www.scimagojr.com/journalsearch.php?tip=sid&clean=0&q="; //$NON-NLS-1$

	/** Identifier of the journal in the database.
	 * 
	 * <p>Using this instead of {@link GenerationType#IDENTITY} allows for JOINED or TABLE_PER_CLASS inheritance types to work.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private int id;

	/** Name of the journal.
	 */
	@Column
	private String journalName;

	/** Name of the publisher of the journal.
	 */
	@Column
	private String publisher;

	/** Address of the publisher of the journal.
	 */
	@Column
	private String address;

	/** URL to the page of the journal on the publisher website.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String journalUrl;

	/** Identifier of the journal on the Scimago website.
	 */
	@Column
	private String scimagoId;

	/** Identifier of the journal on the Web-Of-Science website.
	 */
	@Column
	private String wosId;

	/** ISBN number if the journal has one.
	 */
	@Column
	private String isbn;

	/** ISSN number if the journal has one.
	 */
	@Column
	private String issn;

	/** Indicates if the journal is an open-access journal. If this field
	 * is not set, we don't know if the journal is open access.
	 */
	@Column(nullable = true)
	private Boolean openAccess;

	/** List of papers that are published in this journal.
	 */
	@OneToMany(mappedBy = "journal")
	@JsonIgnore
	private Set<JournalPaper> publishedPapers;

	/** History of the quality indicators for this journal.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "journal_journal_annual_indicators_mapping", 
	joinColumns = {
			@JoinColumn(name = "journal_id", referencedColumnName = "id")
	},
	inverseJoinColumns = {
			@JoinColumn(name = "indicators_id", referencedColumnName = "id")
	})
	@MapKey(name = "referenceYear")
	private Map<Integer, JournalQualityAnnualIndicators> qualityIndicatorsHistory;

	/** Construct a journal with the given values.
	 * 
	 * @param identifier the identifier of the journal in the database.
	 * @param papers the set of papers that were published in this journal.
	 * @param name the name of the journal.
	 * @param publisher the name of the publisher of the journal.
	 * @param address the address of the publisher.
	 * @param publisherUrl the URL to the page of the journal on the publisher website.
	 * @param scimagoId the identifier of the journal on the Scimago website.
	 * @param wosId the identifier of the journal on the Web-Of-Science website.
	 * @param isbn the ISBN of the journal.
	 * @param issn the ISSN of the journal.
	 * @param indicators the history of the quality indicators for the journal.
	 */
	public Journal(int identifier, Set<JournalPaper> papers, String name,
			String publisher, String address, String publisherUrl, String scimagoId, String wosId,
			String isbn, String issn, Map<Integer, JournalQualityAnnualIndicators> indicators) {
		this.id = identifier;
		this.publishedPapers = papers;
		this.journalName = name;
		this.publisher = publisher;
		this.address = address;
		this.journalUrl = publisherUrl;
		this.scimagoId = scimagoId;
		this.wosId = wosId;
		this.isbn = isbn;
		this.issn = issn;
		this.qualityIndicatorsHistory = indicators;
	}

	/** Construct a journal with the given values.
	 * 
	 * @param identifier the identifier of the journal in the database.
	 * @param papers the set of papers that were published in this journal.
	 * @param name the name of the journal.
	 * @param publisher the name of the publisher of the journal.
	 * @param address the address of the publisher.
	 * @param publisherUrl the URL to the page of the journal on the publisher website.
	 * @param scimagoId the identifier of the journal on the Scimago website.
	 * @param wosId the identifier of the journal on the Web-Of-Science website.
	 * @param isbn the ISBN of the journal.
	 * @param issn the ISSN of the journal.
	 * @param indicators the history of the quality indicators for the journal.
	 */
	public Journal(int identifier, Set<JournalPaper> papers, String name,
			String publisher, String address, URL publisherUrl, String scimagoId, String wosId,
			String isbn, String issn, Map<Integer, JournalQualityAnnualIndicators> indicators) {
		this(identifier, papers, name, publisher, address,
				publisherUrl != null ? publisherUrl.toExternalForm() : null,
						scimagoId, wosId, isbn, issn, indicators);
	}

	/** Construct an empty journal.
	 */
	public Journal() {
		//
	}

	@Override
	public String toString() {
		return getJournalName() + ":" + getId(); //$NON-NLS-1$
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.journalName);
		h = HashCodeUtils.add(h, this.publisher);
		h = HashCodeUtils.add(h, this.address);
		h = HashCodeUtils.add(h, this.journalUrl);
		h = HashCodeUtils.add(h, this.scimagoId);
		h = HashCodeUtils.add(h, this.wosId);
		h = HashCodeUtils.add(h, this.isbn);
		h = HashCodeUtils.add(h, this.issn);
		h = HashCodeUtils.add(h, this.openAccess);
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
		final Journal other = (Journal) obj;
		if (this.id != other.id) {
			return false;
		}
		if (!Objects.equals(this.journalName, other.journalName)) {
			return false;
		}
		if (!Objects.equals(this.publisher, other.publisher)) {
			return false;
		}
		if (!Objects.equals(this.address, other.address)) {
			return false;
		}
		if (!Objects.equals(this.journalUrl, other.journalUrl)) {
			return false;
		}
		if (!Objects.equals(this.scimagoId, other.scimagoId)) {
			return false;
		}
		if (!Objects.equals(this.wosId, other.wosId)) {
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
		return true;
	}

	/** {@inheritDoc}
	 * <p>The attributes that are not considered by this function are:<ul>
	 * <li>{@code id}</li>
	 * <li>{@code publishedPapers}</li>
	 * <li>{@code qualityIndicators}</li>
	 * </ul>
	 */
	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		if (!Strings.isNullOrEmpty(getJournalName())) {
			consumer.accept("journalName", getJournalName()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getPublisher())) {
			consumer.accept("publisher", getPublisher()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getAddress())) {
			consumer.accept("address", getAddress()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getJournalURL())) {
			consumer.accept("journalURL", getJournalURL()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getScimagoId())) {
			consumer.accept("scimagoId", getScimagoId()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getWosId())) {
			consumer.accept("wosId", getWosId()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getISBN())) {
			consumer.accept("isbn", getISBN()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getISSN())) {
			consumer.accept("issn", getISSN()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getISSN())) {
			consumer.accept("openAccess", getOpenAccess()); //$NON-NLS-1$
		}
	}

	@Override
	public void serialize(JsonGenerator generator, SerializerProvider serializers) throws IOException {
		generator.writeStartObject();
		generator.writeNumberField("id", getId()); //$NON-NLS-1$
		forEachAttribute((name, value) -> {
			JsonUtils.writeField(generator, name, value);
		});
		if (!getPublishedPapers().isEmpty()) {
			generator.writeArrayFieldStart("publishedPapers"); //$NON-NLS-1$
			for (final Publication publication : getPublishedPapers()) {
				generator.writeNumber(publication.getId());
			}
			generator.writeEndArray();
		}
		if (!getQualityIndicators().isEmpty()) {
			generator.writeObjectFieldStart("qualityIndicators"); //$NON-NLS-1$
			for (final Entry<Integer, JournalQualityAnnualIndicators> indicators : getQualityIndicators().entrySet()) {
				generator.writeFieldName(indicators.getKey().toString());
				indicators.getValue().serialize(generator, serializers);
			}
			generator.writeEndObject();
		}
		generator.writeEndObject();
	}

	@Override
	public void serializeWithType(JsonGenerator generator, SerializerProvider serializers, TypeSerializer typeSer)
			throws IOException {
		serialize(generator, serializers);
	}

	/** Replies the identifier of the journal in the database.
	 *
	 * @return the identifier.
	 */
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

	/** Replies the name of the journal.
	 *
	 * @return the name.
	 */
	public String getJournalName() {
		return this.journalName;
	}

	/** Change the name of the journal.
	 *
	 * @param name the name.
	 */
	public void setJournalName(String name) {
		this.journalName = Strings.emptyToNull(name);
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

	/** Replies the address of publisher of the journal.
	 *
	 * @return the address.
	 */
	public String getAddress() {
		return this.address;
	}

	/** Change the address of publisher of the journal.
	 *
	 * @param address the address.
	 */
	public void setAddress(String address) {
		this.address = Strings.emptyToNull(address);
	}

	/** Replies the URL of the journal on the publisher website.
	 *
	 * @return the URL.
	 */
	public final URL getJournalURLObject() {
		try {
			return new URL(getJournalURL());
		} catch (MalformedURLException ex) {
			return null;
		}
	}

	/** Replies the URL of the journal on the publisher website.
	 *
	 * @return the URL.
	 */
	public String getJournalURL() {
		return this.journalUrl;
	}

	/** Change the URL of the journal on the publisher website.
	 *
	 * @param url the URL.
	 */
	public void setJournalURL(String url) {
		this.journalUrl = Strings.emptyToNull(url);
	}

	/** Change the URL of the journal on the publisher website.
	 *
	 * @param url the URL.
	 */
	public final void setJournalURL(URL url) {
		setJournalURL(url == null ? null : url.toExternalForm());
	}

	/** Replies the identifier of the journal on the Scimago website.
	 *
	 * @return the identifier.
	 */
	public String getScimagoId() {
		return this.scimagoId;
	}

	/** Replies the URL of the journal on the Scimago website.
	 *
	 * @return the URL.
	 */
	public URL getScimagoURL() {
		try {
			return new URL(SCIMAGO_URL + getScimagoId());
		} catch (Throwable ex) {
			return null;
		}
	}

	/** Change the identifier of the journal on the Scimago website.
	 *
	 * @param id the identifier.
	 */
	public void setScimagoId(String id) {
		this.scimagoId = Strings.emptyToNull(id);
	}

	/** Replies the identifier of the journal on the Web-Of-Science website.
	 *
	 * @return the identifier.
	 */
	public String getWosId() {
		return this.wosId;
	}

	/** Change the identifier of the journal on the Web-Of-Science website.
	 *
	 * @param id the identifier.
	 */
	public void setWosId(String id) {
		this.wosId = Strings.emptyToNull(id);
	}

	/** Replies if the journal is open access. If it is not known, this functio returns {@code null}.
	 *
	 * @return the open-access status of the journal, or {@code null} if it is not known.
	 */
	public Boolean getOpenAccess() {
		return this.openAccess;
	}

	/** Change the flag that indicates if the journal is open access. If it is not known, this functio returns {@code null}.
	 *
	 * @param openAccess the open-access status of the journal, or {@code null} if it is not known.
	 */
	public void setOpenAccess(Boolean openAccess) {
		this.openAccess = openAccess;
	}

	/** Replies the set of published papers in this journal.
	 *
	 * @return the published papers.
	 */
	public Set<JournalPaper> getPublishedPapers() {
		if (this.publishedPapers == null) {
			this.publishedPapers = new HashSet<>();
		}
		return this.publishedPapers;
	}

	/** Change the set of published papers in this journal.
	 *
	 * @param papers the published papers.
	 */
	public void setPublishedPapers(Set<JournalPaper> papers) {
		if (this.publishedPapers != null) {
			this.publishedPapers.clear();
			if (papers != null) {
				this.publishedPapers.addAll(papers);
			}
		} else if (papers == null) {
			this.publishedPapers = new HashSet<>();
		} else {
			this.publishedPapers = papers;
		}
	}

	/** Replies if the journal has at least one published paper attached to it.
	 *
	 * @return {@code true} if a paper is attached to this journal.
	 */
	public boolean hasPublishedPaper() {
		return this.publishedPapers != null && !this.publishedPapers.isEmpty();
	}

	/** Replies the quality indicators of the journal.
	 *
	 * @return the indicators.
	 * @since 2.0
	 */
	public final Map<Integer, JournalQualityAnnualIndicators> getQualityIndicators() {
		if (this.qualityIndicatorsHistory == null) {
			this.qualityIndicatorsHistory = new TreeMap<>();
		}
		return this.qualityIndicatorsHistory;
	}

	/** Replies the quality indicators of the journal for the given year.
	 *
	 * @param year the year to search for.
	 * @return the indicators or {@code null} if none were defined.
	 * @since 2.0
	 */
	public final JournalQualityAnnualIndicators getQualityIndicatorsForYear(int year) {
		if (this.qualityIndicatorsHistory == null) {
			return null;
		}
		return this.qualityIndicatorsHistory.get(Integer.valueOf(year));
	}

	/** Replies the quality indicators that is fitting the given predicates and with an year lower or equal
	 * to the given value.
	 *
	 * @param year the year to search for.
	 * @param selector the object that permits to select the best quality indicators.
	 * @return the indicators or {@code null} if none were defined.
	 * @since 2.0
	 */
	public final JournalQualityAnnualIndicators getQualityIndicatorsFor(int year, Predicate<JournalQualityAnnualIndicators> selector) {
		if (this.qualityIndicatorsHistory != null) {
			final IntegerList ids = new IntegerList(this.qualityIndicatorsHistory.keySet());
			final int start = ListUtil.floorIndex(ids, (a, b) -> Integer.compare(a.intValue(), b.intValue()), Integer.valueOf(year));
			for (int i = start; i >= 0; --i) {
				final JournalQualityAnnualIndicators indicators = this.qualityIndicatorsHistory.get(ids.get(i));
				if (indicators != null && selector.test(indicators)) {
					return indicators;
				}
			}
		}
		return null;
	}

	/** Replies if the journal has quality indicators for the given year.
	 *
	 * @param year the year to search for.
	 * @return {@code true} if the journal has quality indicators.
	 */
	public boolean hasQualityIndicatorsForYear(int year) {
		return getQualityIndicatorsForYear(year) != null;
	}

	/** Replies the Q-Index of the journal from the Scimago source.
	 * If there is no Q-Index known for the given year, this function replies
	 * the Q-Index for the highest year that is below the given one. 
	 *
	 * @param year the year to search for.
	 * @return the Q-Index of the journal for the given year, or {@code null} if not defined.
	 */
	public QuartileRanking getScimagoQIndexByYear(int year) {
		final JournalQualityAnnualIndicators indicators = getQualityIndicatorsFor(year, it -> it.getScimagoQIndex() != null);
		if (indicators != null) {
			return indicators.getScimagoQIndex();
		}
		return null;
	}

	/** Change the Q-Index of the journal from the Scimago source.
	 *
	 * @param year the year to search for.
	 * @param quartile the Q-Index of the journal for the given year, or {@code null} if not defined.
	 * @return the impacted annual indicators object
	 */
	public JournalQualityAnnualIndicators setScimagoQIndexByYear(int year, QuartileRanking quartile) {
		JournalQualityAnnualIndicators indicators = getQualityIndicatorsForYear(year);
		if (indicators != null) {
			indicators.setScimagoQIndex(quartile);
		} else {
			indicators = new JournalQualityAnnualIndicators(year, quartile, null, 0f);
			if (this.qualityIndicatorsHistory == null) {
				this.qualityIndicatorsHistory = new TreeMap<>();
			}
			this.qualityIndicatorsHistory.put(Integer.valueOf(year), indicators);
		}
		return indicators;
	}

	/** Replies if the journal has Scimago Q-Index for the given year.
	 * If there is no Q-Index known for the given year, this function replies
	 * the Q-Index for the highest year that is below the given one. 
	 *
	 * @param year the year to search for.
	 * @return {@code true} if the journal has Q-Index.
	 */
	public boolean hasScimagoQIndexForYear(int year) {
		return getScimagoQIndexByYear(year) != null;
	}

	/** Replies the Q-Index of the journal from the JCR/WOS source.
	 * If there is no Q-Index known for the given year, this function replies
	 * the Q-Index for the highest year that is below the given one. 
	 *
	 * @param year the year to search for.
	 * @return the Q-Index of the journal for the given year, or {@code null} if not defined.
	 */
	public QuartileRanking getWosQIndexByYear(int year) {
		final JournalQualityAnnualIndicators indicators = getQualityIndicatorsFor(year, it -> it.getWosQIndex() != null);
		if (indicators != null) {
			return indicators.getWosQIndex();
		}
		return null;
	}

	/** Change the Q-Index of the journal from the Web-Of-Science source.
	 *
	 * @param year the year to search for.
	 * @param quartile the Q-Index of the journal for the given year, or {@code null} if not defined.
	 * @return the impacted annual indicators object
	 */
	public JournalQualityAnnualIndicators setWosQIndexByYear(int year, QuartileRanking quartile) {
		JournalQualityAnnualIndicators indicators = getQualityIndicatorsForYear(year);
		if (indicators != null) {
			indicators.setWosQIndex(quartile);
		} else {
			indicators = new JournalQualityAnnualIndicators(year, null, quartile, 0f);
			if (this.qualityIndicatorsHistory == null) {
				this.qualityIndicatorsHistory = new TreeMap<>();
			}
			this.qualityIndicatorsHistory.put(Integer.valueOf(year), indicators);
		}
		return indicators;
	}

	/** Replies if the journal has WOS Q-Index for the given year.
	 * If there is no Q-Index known for the given year, this function replies
	 * the Q-Index for the highest year that is below the given one. 
	 *
	 * @param year the year to search for.
	 * @return {@code true} if the journal has Q-Index.
	 */
	public boolean hasWosQIndexForYear(int year) {
		return getWosQIndexByYear(year) != null;
	}

	/** Replies the impact factor of the journal.
	 * If there is no impact factor known for the given year, this function replies
	 * the impact factor for the highest year that is below the given one. 
	 *
	 * @param year the year to search for.
	 * @return the IF of the journal for the given year, or {@code 0} if not defined.
	 */
	public float getImpactFactorByYear(int year) {
		final JournalQualityAnnualIndicators indicators = getQualityIndicatorsFor(year, it -> it.getImpactFactor() > 0f);
		if (indicators != null) {
			return indicators.getImpactFactor();
		}
		return 0f;
	}

	/** Change the impact factor of the journal.
	 *
	 * @param year the year to search for.
	 * @param impactFactor the new impact factor of the journal for the given year, or {@code 0} if not defined.
	 * @return the impacted annual indicators object
	 */
	public JournalQualityAnnualIndicators setImpactFactorByYear(int year, float impactFactor) {
		assert impactFactor >= 0f;
		JournalQualityAnnualIndicators indicators = getQualityIndicatorsForYear(year);
		if (indicators != null) {
			indicators.setImpactFactor(impactFactor);
		} else {
			indicators = new JournalQualityAnnualIndicators(year, null, null, impactFactor);
			if (this.qualityIndicatorsHistory == null) {
				this.qualityIndicatorsHistory = new TreeMap<>();
			}
			this.qualityIndicatorsHistory.put(Integer.valueOf(year), indicators);
		}
		return indicators;
	}

	/** Replies if the journal has impact factor for the given year.
	 * If there is no impact factor known for the given year, this function replies
	 * the impact factor for the highest year that is below the given one. 
	 *
	 * @param year the year to search for.
	 * @return {@code true} if the journal has impact factor.
	 */
	public boolean hasImpactFactorForYear(int year) {
		return getImpactFactorByYear(year) > 0f;
	}

	/** Replies the ISBN number that is associated to this journal.
	 *
	 * @return the ISBN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/ISBN"
	 */
	public String getISBN() {
		return this.isbn;
	}

	/** Change the ISBN number that is associated to this journal.
	 *
	 * @param isbn the ISBN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/ISBN"
	 */
	public void setISBN(String isbn) {
		this.isbn = Strings.emptyToNull(isbn);
	}

	/** Replies the ISSN number that is associated to this journal.
	 *
	 * @return the ISSN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/International_Standard_Serial_Number"
	 */
	public String getISSN() {
		return this.issn;
	}

	/** Change the ISSN number that is associated to this journal.
	 *
	 * @param issn the ISSN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/International_Standard_Serial_Number"
	 */
	public void setISSN(String issn) {
		this.issn = Strings.emptyToNull(issn);
	}

}
