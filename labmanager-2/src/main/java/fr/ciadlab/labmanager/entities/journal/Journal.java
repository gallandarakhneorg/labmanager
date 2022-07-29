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

package fr.ciadlab.labmanager.entities.journal;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.JsonObject;
import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import fr.ciadlab.labmanager.entities.ranking.QuartileRanking;
import fr.ciadlab.labmanager.utils.AttributeProvider;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import fr.ciadlab.labmanager.utils.JsonExportable;
import fr.ciadlab.labmanager.utils.JsonUtils;
import org.apache.jena.ext.com.google.common.base.Strings;

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
public class Journal implements Serializable, JsonExportable, AttributeProvider {

	private static final long serialVersionUID = -2046765660549008074L;

	/** Identifier of the journal in the database.
	 * 
	 * <p>Using this instead of {@link GenerationType#IDENTITY} allows for JOINED or TABLE_PER_CLASS inheritance types to work.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	/** Name of the journal.
	 */
	@Column
	private String journalName;

	/** Name of the publisher of the journal.
	 */
	@Column
	private String publisher;

	/** URL to the page of the journal on the publisher website.
	 */
	@Column
	private String journalUrl;

	/** Identifier of the journal on the Scimago website.
	 */
	@Column
	private String scimagoId;

	/** Identifier of the journal on the Web-Of-Science website.
	 */
	@Column
	private String wosId;

	/** List of papers that are published in this journal.
	 */
	@OneToMany(mappedBy = "journal")
	@JsonIgnore
	private Set<JournalPaper> publishedPapers = new HashSet<>();

	/** History of the quality indicators for this journal.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "indicatorsId")
	@MapKey(name = "year")
	private Map<Integer, JournalQualityAnnualIndicators> qualityIndicatorsHistory;

	/** Construct a journal with the given values.
	 * 
	 * @param identifier the identifier of the journal in the database.
	 * @param papers the set of papers that were published in this journal.
	 * @param name the name of the journal.
	 * @param publisher the name of the publisher of the journal.
	 * @param publisherUrl the URL to the page of the journal on the publisher website.
	 * @param scimagoId the identifier of the journal on the Scimago website.
	 * @param wosId the identifier of the journal on the Web-Of-Science website.
	 * @param indicators the history of the quality indicators for the journal.
	 */
	public Journal(int identifier, Set<JournalPaper> papers, String name,
			String publisher, String publisherUrl, String scimagoId, String wosId,
			Map<Integer, JournalQualityAnnualIndicators> indicators) {
		this.id = identifier;
		this.publishedPapers = papers;
		this.journalName = name;
		this.publisher = publisher;
		this.journalUrl = publisherUrl;
		this.scimagoId = scimagoId;
		this.wosId = wosId;
		this.qualityIndicatorsHistory = indicators == null ? new HashMap<>() : indicators;
	}

	/** Construct a journal with the given values.
	 * 
	 * @param identifier the identifier of the journal in the database.
	 * @param papers the set of papers that were published in this journal.
	 * @param name the name of the journal.
	 * @param publisher the name of the publisher of the journal.
	 * @param publisherUrl the URL to the page of the journal on the publisher website.
	 * @param scimagoId the identifier of the journal on the Scimago website.
	 * @param wosId the identifier of the journal on the Web-Of-Science website.
	 * @param indicators the history of the quality indicators for the journal.
	 */
	public Journal(int identifier, Set<JournalPaper> papers, String name,
			String publisher, URL publisherUrl, String scimagoId, String wosId,
			Map<Integer, JournalQualityAnnualIndicators> indicators) {
		this(identifier, papers, name, publisher,
				publisherUrl != null ? publisherUrl.toExternalForm() : null,
						scimagoId, wosId, indicators);
	}

	/** Construct an empty journal.
	 */
	public Journal() {
		// creation of an empty set to avoid any null pointer exception while using the
		// history
		this.qualityIndicatorsHistory = new HashMap<>();
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.journalName);
		h = HashCodeUtils.add(h, this.publisher);
		h = HashCodeUtils.add(h, this.journalUrl);
		h = HashCodeUtils.add(h, this.scimagoId);
		h = HashCodeUtils.add(h, this.wosId);
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
		if (!Objects.equals(this.journalUrl, other.journalUrl)) {
			return false;
		}
		if (!Objects.equals(this.scimagoId, other.scimagoId)) {
			return false;
		}
		if (!Objects.equals(this.wosId, other.wosId)) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc}
	 * <p>The attributes that are not considered by this function are:<ul>
	 * <li>{@code id}</li>
	 * </ul>
	 */
	@Override
	public void forEachAttribute(BiConsumer<String, Object> consumer) {
		if (!Strings.isNullOrEmpty(getJournalName())) {
			consumer.accept("journalName", getJournalName()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getPublisher())) {
			consumer.accept("publisher", getPublisher()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getJournalURL())) {
			consumer.accept("journalUrl", getJournalURL()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getScimagoId())) {
			consumer.accept("scimagoId", getScimagoId()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getWosId())) {
			consumer.accept("wosId", getWosId()); //$NON-NLS-1$
		}
	}

	@Override
	public void toJson(JsonObject json) {
		json.addProperty("id", Integer.valueOf(getId())); //$NON-NLS-1$
		forEachAttribute((name, value) -> JsonUtils.defaultBehavior(json, name, value));
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

	/** Replies the set of published papers in this journal.
	 *
	 * @return the published papers.
	 */
	public Set<JournalPaper> getPublishedPapers() {
		return this.publishedPapers;
	}

	/** Change the set of published papers in this journal.
	 *
	 * @param papers the published papers.
	 */
	public void setPublishedPapers(Set<JournalPaper> papers) {
		if (papers == null) {
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

	/** Replies the quality indicators of the journal for the given year.
	 * This function is equivalent to {@link #getQualityIndicatorsForYear(int, boolean)}
	 * with the second argument set to {@code false}.
	 *
	 * @param year the year to search for.
	 * @return the indicators or {@code null} if none were defined.
	 * @since 2.0
	 * @see #getQualityIndicatorsForYear(int, boolean)
	 */
	public final JournalQualityAnnualIndicators getQualityIndicatorsForYear(int year) {
		return getQualityIndicatorsForYear(year, false);
	}

	/** Replies the quality indicators of the journal for the given year.
	 * The function {@link #getQualityIndicatorsForYear(int)} is equivalent to this function
	 * with the second argument of this function set to {@code false}.
	 *
	 * @param year the year to search for.
	 * @param createInstance is {@code true} for forcing the creation of the indicator instance if it is
	 *     not yet into the internal data structure. It is {@code false} to not create automatically
	 *     the instance of the indicators if it was not created.
	 * @return the indicators or {@code null} if none were defined.
	 * @since 2.0
	 * @see #getQualityIndicatorsForYear(int)
	 */
	public JournalQualityAnnualIndicators getQualityIndicatorsForYear(int year, boolean createInstance) {
		return this.qualityIndicatorsHistory.get(Integer.valueOf(year));
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
	 *
	 * @param year the year to search for.
	 * @return the Q-Index of the journal for the given year, or {@code null} if not defined.
	 */
	public QuartileRanking getScimagoQIndexByYear(int year) {
		final JournalQualityAnnualIndicators indicators = getQualityIndicatorsForYear(year);
		if (indicators != null) {
			return indicators.getScimagoQIndex();
		}
		return null;
	}

	/** Change the Q-Index of the journal from the Scimago source.
	 *
	 * @param year the year to search for.
	 * @param quartile the Q-Index of the journal for the given year, or {@code null} if not defined.
	 */
	public void setScimagoQIndexByYear(int year, QuartileRanking quartile) {
		final JournalQualityAnnualIndicators indicators = getQualityIndicatorsForYear(year);
		if (indicators != null) {
			indicators.setScimagoQIndex(quartile);
		} else {
			this.qualityIndicatorsHistory.put(Integer.valueOf(year), new JournalQualityAnnualIndicators(year, quartile, null, 0f));
		}
	}

	/** Replies the Q-Index of the journal from the JCR/WOS source.
	 *
	 * @param year the year to search for.
	 * @return the Q-Index of the journal for the given year, or {@code null} if not defined.
	 */
	public QuartileRanking getWosQIndexByYear(int year) {
		final JournalQualityAnnualIndicators indicators = getQualityIndicatorsForYear(year);
		if (indicators != null) {
			return indicators.getWosQIndex();
		}
		return null;
	}

	/** Change the Q-Index of the journal from the Web-Of-Science source.
	 *
	 * @param year the year to search for.
	 * @param quartile the Q-Index of the journal for the given year, or {@code null} if not defined.
	 */
	public void setWosQIndexByYear(int year, QuartileRanking quartile) {
		final JournalQualityAnnualIndicators indicators = getQualityIndicatorsForYear(year);
		if (indicators != null) {
			indicators.setWosQIndex(quartile);
		} else {
			this.qualityIndicatorsHistory.put(Integer.valueOf(year), new JournalQualityAnnualIndicators(year, null, quartile, 0f));
		}
	}

	/** Replies the impact factor of the journal.
	 *
	 * @param year the year to search for.
	 * @return the IF of the journal for the given year, or {@code 0} if not defined.
	 */
	public float getImpactFactorByYear(int year) {
		final JournalQualityAnnualIndicators indicators = getQualityIndicatorsForYear(year);
		if (indicators != null) {
			return indicators.getImpactFactor();
		}
		return 0f;
	}

	/** Change the impact factor of the journal.
	 *
	 * @param year the year to search for.
	 * @param impactFactor the new impact factor of the journal for the given year, or {@code 0} if not defined.
	 */
	public void setImpactFactorByYear(int year, float impactFactor) {
		assert impactFactor >= 0f;
		final JournalQualityAnnualIndicators indicators = getQualityIndicatorsForYear(year);
		if (indicators != null) {
			indicators.setImpactFactor(impactFactor);
		} else {
			this.qualityIndicatorsHistory.put(Integer.valueOf(year), new JournalQualityAnnualIndicators(year, null, null, impactFactor));
		}
	}

}
