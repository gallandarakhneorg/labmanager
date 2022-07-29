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

package fr.ciadlab.labmanager.entities.publication.type;

import java.util.Objects;
import java.util.function.BiConsumer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.ranking.QuartileRanking;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import org.apache.jena.ext.com.google.common.base.Strings;

/** Edition of a journal or a special issue of a journal.
 *
 * <p>This type has no equivalent in the BibTeX types. It is inspired from {@code article}.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "JournalEditions")
@PrimaryKeyJoinColumn(name = "id")
public class JournalEdition extends Publication {

	private static final long serialVersionUID = 2950457929302009775L;

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

	/** Reference to the journal.
	 */
	@ManyToOne
	private Journal journal;

	/** Construct a journal edition with the given values.
	 *
	 * @param publication the publication to copy.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 */
	public JournalEdition(Publication publication, String volume, String number, String pages) {
		super(publication);
		this.volume = volume;
		this.number = number;
		this.pages = pages;
	}

	/** Construct an empty journal edition.
	 */
	public JournalEdition() {
		//
	}

	@Override
	public int hashCode() {
		int h = super.hashCode();
		h = HashCodeUtils.add(h, this.volume);
		h = HashCodeUtils.add(h, this.number);
		h = HashCodeUtils.add(h, this.pages);
		h = HashCodeUtils.add(h, this.journal);
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		final JournalEdition other = (JournalEdition) obj;
		if (!Objects.equals(this.volume, other.volume)) {
			return false;
		}
		if (!Objects.equals(this.number, other.number)) {
			return false;
		}
		if (!Objects.equals(this.pages, other.pages)) {
			return false;
		}
		if (!Objects.equals(this.journal, other.journal)) {
			return false;
		}
		return true;
	}

	@Override
	public void forEachAttribute(BiConsumer<String, Object> consumer) {
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
		final Journal journal = getJournal();
		if (journal != null) {
			consumer.accept("journal", journal); //$NON-NLS-1$
		}
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

	/** Replies the journal in which the publication was published.
	 *
	 * @return the journal.
	 */
	public Journal getJournal() {
		return this.journal;
	}

	/** Change the journal in which the publication was published.
	 *
	 * @param journal the journal.
	 */
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

}
