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

package fr.utbm.ciad.labmanager.data.publication.type;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import fr.utbm.ciad.labmanager.data.EntityConstants;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.utils.HashCodeUtils;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.context.support.MessageSourceAccessor;

/** Chapter of a book.
 *
 * <p>This type is equivalent to the BibTeX types: {@code inbook}, {@code chapter}.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@DiscriminatorValue("BookChapter")
public class BookChapter extends Publication {

	private static final long serialVersionUID = 1191620361523483766L;

	/** Volume number of the book.
	 */
	@Column
	private String volume;

	/** Number of the book.
	 */
	@Column
	private String number;

	/** Page range of the chapter in the book.
	 */
	@Column
	private String pages;

	/** List of names of the editors of the book.
	 * The list of names is usually a sequence of names separated by {@code AND}, and each name has the format {@code LAST, VON, FIRST}.
	 */
	@Column(length = EntityConstants.LARGE_TEXT_SIZE)
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

	/** Title of the book in which the chapter was published.
	 */
	@Column(length = EntityConstants.LARGE_TEXT_SIZE)
	private String bookTitle;

	/** Number of the chapter in the book.
	 */
	@Column
	private String chapterNumber;

	/** Construct a book chapter with the given values.
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
	 * @param bookTitle the title of the book in which the chapter is.
	 * @param chapterNumber the number of the chapter in the book.
	 */
	public BookChapter(Publication publication, String volume, String number, String pages, String editors,
			String address, String series, String publisher, String edition, String bookTitle, String chapterNumber) {
		super(publication);
		this.volume = volume;
		this.number = number;
		this.pages = pages;
		this.editors = editors;
		this.address = address;
		this.series = series;
		this.publisher = publisher;
		this.edition = edition;
		this.bookTitle = bookTitle;
		this.chapterNumber = chapterNumber;
	}

	/** Construct an empty book chapter.
	 */
	public BookChapter() {
		//
	}

	@Override
	public int hashCode() {
		if (getId() != 0) {
			return Long.hashCode(getId());
		}
		var h = super.hashCode();
		h = HashCodeUtils.add(h, this.bookTitle);
		h = HashCodeUtils.add(h, this.publisher);
		h = HashCodeUtils.add(h, this.volume);
		h = HashCodeUtils.add(h, this.number);
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
		final var other = (BookChapter) obj;
		if (getId() != 0 && other.getId() != 0) {
			return getId() == other.getId();
		}
		return super.equals(other)
				&& Objects.equals(this.bookTitle, other.bookTitle)
				&& Objects.equals(this.publisher, other.publisher)
				&& Objects.equals(this.volume, other.volume)
				&& Objects.equals(this.number, other.number);
	}

	@Override
	public void forEachAttribute(MessageSourceAccessor messages, Locale locale, AttributeConsumer consumer) throws IOException {
		super.forEachAttribute(messages, locale, consumer);
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
		if (!Strings.isNullOrEmpty(getEdition())) {
			consumer.accept("edition", getEdition()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getBookTitle())) {
			consumer.accept("bookTitle", getBookTitle()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getChapterNumber())) {
			consumer.accept("chapterNumber", getChapterNumber()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getPublisher())) {
			consumer.accept("publisher", getPublisher()); //$NON-NLS-1$
		}
	}

	@Override
	@JsonIgnore
	public String getWherePublishedShortDescription() {
		final var buf = new StringBuilder();
		buf.append(getBookTitle());
		if (!Strings.isNullOrEmpty(getChapterNumber())) {
			buf.append(" (chap. "); //$NON-NLS-1$
			buf.append(getChapterNumber());
			buf.append(")"); //$NON-NLS-1$
		}
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
		if (!Strings.isNullOrEmpty(getPublisher())) {
			buf.append(", "); //$NON-NLS-1$
			buf.append(getPublisher());
		}
		if (!Strings.isNullOrEmpty(getAddress())) {
			buf.append(", "); //$NON-NLS-1$
			buf.append(getAddress());
		}
		if (!Strings.isNullOrEmpty(getISBN())) {
			buf.append(", ISBN "); //$NON-NLS-1$
			buf.append(getISBN());
		}
		if (!Strings.isNullOrEmpty(getISSN())) {
			buf.append(", ISSN "); //$NON-NLS-1$
			buf.append(getISSN());
		}
		return buf.toString();
	}

	@Override
	public String getPublicationTarget() {
		final var buf = new StringBuilder();
		buf.append(getBookTitle());
		if (!Strings.isNullOrEmpty(getPublisher())) {
			buf.append(", "); //$NON-NLS-1$
			buf.append(getPublisher());
		}
		return buf.toString();
	}

	/** Replies the volume number of the book chapter.
	 * 
	 * @return the volume number.
	 */
	public String getVolume() {
		return this.volume;
	}

	/** Change the volume number of the book chapter.
	 * 
	 * @param volume the volume number.
	 */
	public void setVolume(String volume) {
		this.volume = Strings.emptyToNull(volume);
	}

	/** Replies the number of the book chapter.
	 * 
	 * @return the number.
	 */
	public String getNumber() {
		return this.number;
	}

	/** Change the number of the book chapter.
	 * 
	 * @param number the number.
	 */
	public void setNumber(String number) {
		this.number = Strings.emptyToNull(number);
	}

	/** Replies the page range in the book chapter.
	 * 
	 * @return the number.
	 */
	public String getPages() {
		return this.pages;
	}

	/** Change the page range in the book chapter.
	 * 
	 * @param range the page range.
	 */
	public void setPages(String range) {
		this.pages = Strings.emptyToNull(range);
	}

	/** Replies the editors of the book chapter.
	 * The editors is usually a list of names, separated by {@code AND}, and each name has the format {@code LAST, VON, FIRST}.
	 *
	 * @return the editor names.
	 */
	public String getEditors() {
		return this.editors;
	}

	/** Change the editors of the book chapter.
	 * The editors is usually a list of names, separated by {@code AND}, and each name has the format {@code LAST, VON, FIRST}.
	 *
	 * @param names the editor names.
	 */
	public void setEditors(String names) {
		this.editors = Strings.emptyToNull(names);
	}

	/** Replies the geographic location of the publisher of the book chapter.
	 * The location is usually a city and a country.
	 *
	 * @return the address.
	 */
	public String getAddress() {
		return this.address;
	}

	/** Change the geographic location of tthe publisher of the book chapter.
	 * The location is usually a city and a country.
	 *
	 * @param address the address.
	 */
	public void setAddress(String address) {
		this.address = Strings.emptyToNull(address);
	}

	/** Replies the name or the number of series of the book chapter.
	 *
	 * @return the series.
	 */
	public String getSeries() {
		return this.series;
	}

	/** Change the name or the number of series of the book chapter.
	 *
	 * @param series the series.
	 */
	public void setSeries(String series) {
		this.series = Strings.emptyToNull(series);
	}

	/** Replies the name or the publisher of the book chapter.
	 *
	 * @return the publisher name.
	 */
	public String getPublisher() {
		return this.publisher;
	}

	/** Change the name or the publisher of the book chapter.
	 *
	 * @param name the publisher name.
	 */
	public void setPublisher(String name) {
		this.publisher = Strings.emptyToNull(name);
	}

	/** Replies the edition number the book chapter.
	 *
	 * @return the edition number.
	 */
	public String getEdition() {
		return this.edition;
	}

	/** Change the edition number the book chapter.
	 *
	 * @param edition the edition number.
	 */
	public void setEdition(String edition) {
		this.edition = Strings.emptyToNull(edition);
	}

	/** Replies the title of the book in which the chapter was published.
	 *
	 * @return the book title.
	 */
	public String getBookTitle() {
		return this.bookTitle;
	}

	/** Replies the title of the book in which the chapter was published.
	 *
	 * @param title the book title.
	 */
	public void setBookTitle(String title) {
		this.bookTitle = Strings.emptyToNull(title);
	}

	/** Replies the number of the chapter in the book.
	 *
	 * @return the chapter number.
	 */
	public String getChapterNumber() {
		return this.chapterNumber;
	}

	/** Change the number of the chapter in the book.
	 *
	 * @param number the chapter number.
	 */
	public void setChapterNumber(String number) {
		this.chapterNumber = Strings.emptyToNull(number);
	}

	@Override
	public boolean isRanked() {
		return false;
	}

}
