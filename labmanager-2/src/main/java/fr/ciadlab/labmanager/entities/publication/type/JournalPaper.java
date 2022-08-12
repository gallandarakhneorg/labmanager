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

package fr.ciadlab.labmanager.entities.publication.type;

import java.io.IOException;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.publication.JournalBasedPublication;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.apache.jena.ext.com.google.common.base.Strings;

/** Paper in a journal.
 *
 * <p>This type is equivalent to the BibTeX types: {@code article}, {@code incollection}.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "JournalPapers")
@PrimaryKeyJoinColumn(name = "id")
public class JournalPaper extends Publication implements JournalBasedPublication {

	private static final long serialVersionUID = -3322028380433314352L;

	/** Number that represent the volume of the journal.
	 */
	@Column
	private String volume;

	/** Number of the journal.
	 */
	@Column
	private String number;

	/** Range of pages that corresponds to the paper in the journal.
	 */
	@Column
	private String pages;

	/** Name of series of the journal.
	 */
	@Column
	private String series;

	/** Reference to the journal.
	 */
	@ManyToOne
	private Journal journal;

	/** Construct a journal paper with the given values.
	 *
	 * @param publication the publication to copy.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 * @param series the series of the journal.
	 */
	public JournalPaper(Publication publication, String volume, String number, String pages, String series) {
		super(publication);
		this.volume = volume;
		this.number = number;
		this.pages = pages;
		this.series = series;
	}

	/** Construct an empty journal paper.
	 */
	public JournalPaper() {
		//
	}

	@Override
	public int hashCode() {
		int h = super.hashCode();
		h = HashCodeUtils.add(h, this.volume);
		h = HashCodeUtils.add(h, this.number);
		h = HashCodeUtils.add(h, this.pages);
		h = HashCodeUtils.add(h, this.series);
		h = HashCodeUtils.add(h, this.journal);
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		final JournalPaper other = (JournalPaper) obj;
		if (!Objects.equals(this.volume, other.volume)) {
			return false;
		}
		if (!Objects.equals(this.number, other.number)) {
			return false;
		}
		if (!Objects.equals(this.pages, other.pages)) {
			return false;
		}
		if (!Objects.equals(this.series, other.series)) {
			return false;
		}
		if (!Objects.equals(this.journal, other.journal)) {
			return false;
		}
		return true;
	}

	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		super.forEachAttribute(consumer);
		if (!Strings.isNullOrEmpty(getVolume())) {
			consumer.accept("volume", getVolume()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getNumber())) {
			consumer.accept("number", getNumber()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getPages())) {
			consumer.accept("pages", getPages()); //$NON-NLS-1$
		}		
		if (!Strings.isNullOrEmpty(getSeries())) {
			consumer.accept("series", getSeries()); //$NON-NLS-1$
		}
	}

	@Override
	public void serialize(JsonGenerator generator, SerializerProvider serializers) throws IOException {
		super.serialize(generator, serializers);
		if (getJournal() != null) {
			generator.writeNumberField("journal", getJournal().getId()); //$NON-NLS-1$
		}

	}

	@Override
	public String getWherePublishedShortDescription() {
		final StringBuilder buf = new StringBuilder();
		buf.append(getJournal().getJournalName());
		final boolean b0 = !Strings.isNullOrEmpty(getVolume());
		final boolean b1 = !Strings.isNullOrEmpty(getNumber());
		if (b0 || b1) {
			buf.append(", "); //$NON-NLS-1$
			if (b0) {
				buf.append(getVolume());
			}
			if (b1) {
				buf.append("("); //$NON-NLS-1$
				buf.append(getNumber());
				buf.append(")"); //$NON-NLS-1$
			}
		}
		final boolean b2 = !Strings.isNullOrEmpty(getPages());
		if (b2) {
			buf.append(", pp. "); //$NON-NLS-1$
			buf.append(getPages());
		}
		if (!Strings.isNullOrEmpty(getJournal().getPublisher())) {
			buf.append(", "); //$NON-NLS-1$
			buf.append(getJournal().getPublisher());
		}
		if (!Strings.isNullOrEmpty(getJournal().getISBN())) {
			buf.append(", ISBN "); //$NON-NLS-1$
			buf.append(getJournal().getISBN());
		}
		if (!Strings.isNullOrEmpty(getJournal().getISSN())) {
			buf.append(", ISSN "); //$NON-NLS-1$
			buf.append(getJournal().getISSN());
		}
		return buf.toString();
	}

	/** Replies the volume number of the journal in which the publication was published.
	 * 
	 * @return the volume number.
	 */
	public String getVolume() {
		return this.volume;
	}

	/** Change the volume number of the journal in which the publication was published.
	 * 
	 * @param volume the volume number.
	 */
	public void setVolume(String volume) {
		this.volume = Strings.emptyToNull(volume);
	}

	/** Replies the number of the journal in which the publication was published.
	 * 
	 * @return the number.
	 */
	public String getNumber() {
		return this.number;
	}

	/** Change the number of the journal in which the publication was published.
	 * 
	 * @param number the number.
	 */
	public void setNumber(String number) {
		this.number = Strings.emptyToNull(number);
	}

	/** Replies the page range in the journal in which the publication was published.
	 * 
	 * @return the number.
	 */
	public String getPages() {
		return this.pages;
	}

	/** Change the page range in the journal in which the publication was published.
	 * 
	 * @param range the page range.
	 */
	public void setPages(String range) {
		this.pages = Strings.emptyToNull(range);
	}

	/** Replies the series of the journal in which the publication was published.
	 * 
	 * @return the series.
	 */
	public String getSeries() {
		return this.series;
	}

	/** Change the series of the journal in which the publication was published.
	 * 
	 * @param series the series.
	 */
	public void setSeries(String series) {
		this.series = Strings.emptyToNull(series);
	}

	@Override
	public Journal getJournal() {
		return this.journal;
	}

	@Override
	public void setJournal(Journal journal) {
		this.journal = journal;
	}

	/** Replies the Scimago Q-index.
	 *
	 * @return the Scimago ranking.
	 */
	public QuartileRanking getScimagoQIndex() {
		final Journal journal = getJournal();
		if (journal != null) {
			return journal.getScimagoQIndexByYear(getPublicationYear());
		}
		return null;
	}

	/** Replies the JCR/Web-of-Science Q-index.
	 *
	 * @return the JCR/WOS ranking.
	 */
	public QuartileRanking getWosQIndex() {
		final Journal journal = getJournal();
		if (journal != null) {
			return journal.getWosQIndexByYear(getPublicationYear());
		}
		return null;
	}

	/** Replies the journal impact factor.
	 *
	 * @return the IF or zero.
	 */
	public float getImpactFactor() {
		final Journal journal = getJournal();
		if (journal != null) {
			return journal.getImpactFactorByYear(getPublicationYear());
		}
		return 0f;
	}

	@Override
	public boolean isRanked() {
		final Journal journal = getJournal();
		if (journal != null) {
			return journal.getScimagoQIndexByYear(getPublicationYear()) != null
				|| journal.getWosQIndexByYear(getPublicationYear()) != null;
		}
		return false;
	}

	/** Replies the ISBN number that is associated to this publication.
	 * This functions delegates to the journal.
	 *
	 * @return the ISBN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/ISBN"
	 * @deprecated See {@link Journal#getISBN()}
	 */
	@Override
	@Deprecated(since = "2.0.0")
	public String getISBN() {
		return this.journal.getISBN();
	}

	/** Change the ISBN number that is associated to this publication.
	 * This functions delegates to the journal.
	 *
	 * @param isbn the ISBN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/ISBN"
	 * @deprecated See {@link Journal#setISBN(String)}
	 */
	@Override
	@Deprecated(since = "2.0.0")
	public void setISBN(String isbn) {
		this.journal.setISBN(isbn);
	}

	/** Replies the ISSN number that is associated to this publication.
	 * This functions delegates to the journal.
	 *
	 * @return the ISSN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/International_Standard_Serial_Number"
	 * @deprecated See {@link Journal#getISSN()}
	 */
	@Override
	@Deprecated(since = "2.0.0")
	public String getISSN() {
		return this.journal.getISSN();
	}

	/** Change the ISSN number that is associated to this publication.
	 * This functions delegates to the journal.
	 *
	 * @param issn the ISSN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/International_Standard_Serial_Number"
	 * @deprecated See {@link Journal#setISSN(String)}
	 */
	@Override
	@Deprecated(since = "2.0.0")
	public final void setISSN(String issn) {
		this.journal.setISSN(issn);
	}

}
