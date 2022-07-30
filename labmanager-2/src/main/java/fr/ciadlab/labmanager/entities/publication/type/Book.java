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
 * you entered into with the CIAD.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.entities.publication.type;

import java.util.Objects;
import java.util.function.BiConsumer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import org.apache.jena.ext.com.google.common.base.Strings;

/** Book.
 *
 * <p>This type is equivalent to the BibTeX types: {@code book}, {@code booklet}.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "Books")
@PrimaryKeyJoinColumn(name = "id")
public class Book extends Publication {

	private static final long serialVersionUID = -3794504301844157501L;

	/** Volume number of the book.
	 */
	@Column
	private String volume;

	/** Number of the book.
	 */
	@Column
	private String number;

	/** Page range of the paper in the book.
	 */
	@Column
	private String pages;

	/** List of names of the editors of the book.
	 * The list of names is usually a sequence of names separated by {@code AND}, and each name has the format {@code LAST, VON, FIRST}.
	 */
	@Column
	private String editors;

	/** Geographical location of the publisher of the book. It is usually a city and a country.
	 */
	@Column
	private String address;

	/** Number or name of series in which the book was published.
	 */
	@Column
	private String series;

	/** Name of the publisher of the book.
	 */
	@Column
	private String publisher;

	/** Edition number of the book.
	 */
	@Column
	private String edition;

	/** Construct a book with the given values.
	 *
	 * @param publication the publication to copy.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 * @param editors the list of the names of the editors. Each name may have the format {@code LAST, VON, FIRST} and the names may be separated
	 *     with {@code AND}.
	 * @param address the geographical location of the event, usually a city and a country.
	 * @param series the number or the name of the series for the conference proceedings.
	 * @param publisher the name of the publisher of the book.
	 * @param edition the edition number of the book.
	 */
	public Book(Publication publication, String volume, String number, String pages, String editors,
			String address, String series, String publisher, String edition) {
		super(publication);
		this.volume = volume;
		this.number = number;
		this.pages = pages;
		this.editors = editors;
		this.address = address;
		this.series = series;
		this.publisher = publisher;
		this.edition = edition;
	}

	/** Construct an empty book.
	 */
	public Book() {
		//
	}

	@Override
	public int hashCode() {
		int h = super.hashCode();
		h = HashCodeUtils.add(h, this.volume);
		h = HashCodeUtils.add(h, this.number);
		h = HashCodeUtils.add(h, this.pages);
		h = HashCodeUtils.add(h, this.editors);
		h = HashCodeUtils.add(h, this.address);
		h = HashCodeUtils.add(h, this.series);
		h = HashCodeUtils.add(h, this.publisher);
		h = HashCodeUtils.add(h, this.edition);
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		final Book other = (Book) obj;
		if (!Objects.equals(this.volume, other.volume)) {
			return false;
		}
		if (!Objects.equals(this.number, other.number)) {
			return false;
		}
		if (!Objects.equals(this.pages, other.pages)) {
			return false;
		}
		if (!Objects.equals(this.editors, other.editors)) {
			return false;
		}
		if (!Objects.equals(this.address, other.address)) {
			return false;
		}
		if (!Objects.equals(this.series, other.series)) {
			return false;
		}
		if (!Objects.equals(this.publisher, other.publisher)) {
			return false;
		}
		if (!Objects.equals(this.edition, other.edition)) {
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
		if (!Strings.isNullOrEmpty(getEditors())) {
			consumer.accept("editors", getEditors()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getAddress())) {
			consumer.accept("address", getAddress()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getSeries())) {
			consumer.accept("series", getSeries()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getPublisher())) {
			consumer.accept("publisher", getPublisher()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getEdition())) {
			consumer.accept("edition", getEdition()); //$NON-NLS-1$
		}
	}

	/** Replies the volume number of the book.
	 * 
	 * @return the volume number.
	 */
	public String getVolume() {
		return this.volume;
	}

	/** Change the volume number of the book.
	 * 
	 * @param volume the volume number.
	 */
	public void setVolume(String volume) {
		this.volume = Strings.emptyToNull(volume);
	}

	/** Replies the number of the book.
	 * 
	 * @return the number.
	 */
	public String getNumber() {
		return this.number;
	}

	/** Change the number of the book.
	 * 
	 * @param number the number.
	 */
	public void setNumber(String number) {
		this.number = Strings.emptyToNull(number);
	}

	/** Replies the page range in the book.
	 * 
	 * @return the number.
	 */
	public String getPages() {
		return this.pages;
	}

	/** Change the page range in the book.
	 * 
	 * @param range the page range.
	 */
	public void setPages(String range) {
		this.pages = Strings.emptyToNull(range);
	}

	/** Replies the editors of the book.
	 * The editors is usually a list of names, separated by {@code AND}, and each name has the format {@code LAST, VON, FIRST}.
	 *
	 * @return the editor names.
	 */
	public String getEditors() {
		return this.editors;
	}

	/** Change the editors of the book.
	 * The editors is usually a list of names, separated by {@code AND}, and each name has the format {@code LAST, VON, FIRST}.
	 *
	 * @param names the editor names.
	 */
	public void setEditors(String names) {
		this.editors = Strings.emptyToNull(names);
	}

	/** Replies the geographic location of the publisher of the book.
	 * The location is usually a city and a country.
	 *
	 * @return the address.
	 */
	public String getAddress() {
		return this.address;
	}

	/** Change the geographic location of tthe publisher of the book.
	 * The location is usually a city and a country.
	 *
	 * @param address the address.
	 */
	public void setAddress(String address) {
		this.address = Strings.emptyToNull(address);
	}

	/** Replies the name or the number of series of the book.
	 *
	 * @return the series.
	 */
	public String getSeries() {
		return this.series;
	}

	/** Change the name or the number of series of the book.
	 *
	 * @param series the series.
	 */
	public void setSeries(String series) {
		this.series = Strings.emptyToNull(series);
	}

	/** Replies the name or the publisher of the book.
	 *
	 * @return the publisher name.
	 */
	public String getPublisher() {
		return this.publisher;
	}

	/** Change the name or the publisher of the book.
	 *
	 * @param name the publisher name.
	 */
	public void setPublisher(String name) {
		this.publisher = Strings.emptyToNull(name);
	}

	/** Replies the edition number the book.
	 *
	 * @return the edition number.
	 */
	public String getEdition() {
		return this.edition;
	}

	/** Change the edition number the book.
	 *
	 * @param edition the edition number.
	 */
	public void setEdition(String edition) {
		this.edition = Strings.emptyToNull(edition);
	}

	@Override
	public boolean isRanked() {
		return false;
	}

}
