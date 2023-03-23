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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.publication.AbstractJournalBasedPublication;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.utils.HashCodeUtils;

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
public class JournalEdition extends AbstractJournalBasedPublication {

	private static final long serialVersionUID = -1849832728913103105L;

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
	}

	@Override
	public String getWherePublishedShortDescription() {
		final StringBuilder buf = new StringBuilder();
		buf.append(getJournal().getJournalName());
		if (!Strings.isNullOrEmpty(getVolume())) {
			buf.append(", vol. "); //$NON-NLS-1$
			buf.append(getVolume());
		}
		if (!Strings.isNullOrEmpty(getNumber())) {
			buf.append(", n. "); //$NON-NLS-1$
			buf.append(getNumber());
		}
		if (!Strings.isNullOrEmpty(getPages())) {
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

}
