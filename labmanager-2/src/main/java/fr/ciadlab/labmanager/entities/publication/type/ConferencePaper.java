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

import fr.ciadlab.labmanager.entities.EntityFieldConfig;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.ranking.CoreRanking;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import org.apache.jena.ext.com.google.common.base.Strings;

/** Paper in a conference or a workshop.
 *
 * <p>This type is equivalent to the BibTeX types: {@code inproceedings}, {@code conference}.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "ConferencePapers")
@PrimaryKeyJoinColumn(name = "id")
public class ConferencePaper extends Publication {

	private static final long serialVersionUID = -6657050556989265460L;

	/** Name of the event that could be a conference or a workshop for examples.
	 */
	@Column
	private String scientificEventName;

	/** Volume number of the proceedings of the event.
	 */
	@Column
	private String volume;

	/** Number of the proceedings of the event.
	 */
	@Column
	private String number;

	/** Page range of the paper in the proceedings of the event.
	 */
	@Column
	private String pages;

	/** List of names of the editors of the proceedings of the event.
	 * The list of names is usually a sequence of names separated by {@code AND}, and each name has the format {@code LAST, VON, FIRST}.
	 */
	@Column(length = EntityFieldConfig.LARGE_TEXT_SIZE)
	private String editors;

	/** Name of the institution that has organized the event.
	 */
	@Column
	private String organization;

	/** Geographical location of the event. Usually, it is a city and a country.
	 */
	@Column
	private String address;

	/** Name of number of the series of the proceedings of the event.
	 */
	@Column
	private String series;

	/** Construct a conference paper with the given values.
	 *
	 * @param publication the publication to copy.
	 * @param scientificEventName the name of the conference or the workshop.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 * @param editors the list of the names of the editors. Each name may have the format {@code LAST, VON, FIRST} and the names may be separated
	 *     with {@code AND}.
	 * @param orga the name of the organization institution.
	 * @param address the geographical location of the event, usually a city and a country.
	 * @param series the number or the name of the series for the conference proceedings.
	 */
	public ConferencePaper(Publication publication, String scientificEventName, String volume, String number, String pages, String editors,
			String orga, String address, String series) {
		super(publication);
		this.scientificEventName = scientificEventName;
		this.volume = volume;
		this.number = number;
		this.pages = pages;
		this.editors = editors;
		this.organization = orga;
		this.address = address;
		this.series = series;
	}

	/** Construct an empty conference paper.
	 */
	public ConferencePaper() {
		//
	}

	@Override
	public int hashCode() {
		int h = super.hashCode();
		h = HashCodeUtils.add(h, this.scientificEventName);
		h = HashCodeUtils.add(h, this.volume);
		h = HashCodeUtils.add(h, this.number);
		h = HashCodeUtils.add(h, this.pages);
		h = HashCodeUtils.add(h, this.editors);
		h = HashCodeUtils.add(h, this.organization);
		h = HashCodeUtils.add(h, this.address);
		h = HashCodeUtils.add(h, this.series);
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		final ConferencePaper other = (ConferencePaper) obj;
		if (!Objects.equals(this.scientificEventName, other.scientificEventName)) {
			return false;
		}
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
		if (!Objects.equals(this.organization, other.organization)) {
			return false;
		}
		if (!Objects.equals(this.address, other.address)) {
			return false;
		}
		if (!Objects.equals(this.series, other.series)) {
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
		if (!Strings.isNullOrEmpty(getOrganization())) {
			consumer.accept("organization", getOrganization()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getAddress())) {
			consumer.accept("address", getAddress()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getSeries())) {
			consumer.accept("series", getSeries()); //$NON-NLS-1$
		}
	}

	/** Replies the name of event (conference or workshop) in which the publication was published.
	 * 
	 * @return the name.
	 */
	public String getScientificEventName() {
		return this.scientificEventName;
	}

	/** Change the name of event (conference or workshop) in which the publication was published.
	 * 
	 * @param name the name.
	 */
	public void setScientificEventName(String name) {
		this.scientificEventName = Strings.emptyToNull(name);
	}

	/** Replies the volume number of the event (conference or workshop) in which the publication was published.
	 * 
	 * @return the volume number.
	 */
	public String getVolume() {
		return this.volume;
	}

	/** Change the volume number of the event (conference or workshop) in which the publication was published.
	 * 
	 * @param volume the volume number.
	 */
	public void setVolume(String volume) {
		this.volume = Strings.emptyToNull(volume);
	}

	/** Replies the number of the event (conference or workshop) in which the publication was published.
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

	/** Replies the page range in the event (conference or workshop) in which the publication was published.
	 * 
	 * @return the number.
	 */
	public String getPages() {
		return this.pages;
	}

	/** Change the page range in the event (conference or workshop) in which the publication was published.
	 * 
	 * @param range the page range.
	 */
	public void setPages(String range) {
		this.pages = Strings.emptyToNull(range);
	}

	/** Replies the editors of the proceedings of the event (conference or workshop) in which the publication was published.
	 * The editors is usually a list of names, separated by {@code AND}, and each name has the format {@code LAST, VON, FIRST}.
	 *
	 * @return the editor names.
	 */
	public String getEditors() {
		return this.editors;
	}

	/** Change the editors of the proceedings of the event (conference or workshop) in which the publication was published.
	 * The editors is usually a list of names, separated by {@code AND}, and each name has the format {@code LAST, VON, FIRST}.
	 *
	 * @param names the editor names.
	 */
	public void setEditors(String names) {
		this.editors = Strings.emptyToNull(names);
	}

	/** Replies the name of the organization of the event (conference or workshop) in which the publication was published.
	 *
	 * @return the organization name.
	 */
	public String getOrganization() {
		return this.organization;
	}

	/** Change the name of the organization of the event (conference or workshop) in which the publication was published.
	 *
	 * @param name the organization name.
	 */
	public void setOrganization(String name) {
		this.organization = Strings.emptyToNull(name);
	}

	/** Replies the geographic location of the event (conference or workshop) in which the publication was published.
	 * The location is usually a city and a country.
	 *
	 * @return the address.
	 */
	public String getAddress() {
		return this.address;
	}

	/** Change the geographic location of the event (conference or workshop) in which the publication was published.
	 * The location is usually a city and a country.
	 *
	 * @param address the address.
	 */
	public void setAddress(String address) {
		this.address = Strings.emptyToNull(address);
	}

	/** Replies the name or the number of series of the proceedings of the event (conference or workshop) in which the publication was published.
	 *
	 * @return the series.
	 */
	public String getSeries() {
		return this.series;
	}

	/** Change the name or the number of series of the proceedings of the event (conference or workshop) in which the publication was published.
	 *
	 * @param series the series.
	 */
	public void setSeries(String series) {
		this.series = Strings.emptyToNull(series);
	}

	/** Replies the CORE ranking.
	 *
	 * @return the COREranking.
	 */
	@SuppressWarnings("static-method")
	public CoreRanking getCoreRanking() {
		// TODO: Implement ranking.
		return null;
	}

	@Override
	public boolean isRanked() {
		// TODO: Implement ranking.
		return false;
	}

}
