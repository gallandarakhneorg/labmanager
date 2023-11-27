/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
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
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.AttributeProvider;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import org.hibernate.annotations.ColumnDefault;

/** History of the quality indicators for a conference.
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
@Table(name = "ConferenceAnnualIndicators")
public class ConferenceQualityAnnualIndicators implements Serializable, AttributeProvider {

	private static final long serialVersionUID = 1212711963054404563L;

	/** Identifier for the history entry.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	@JsonIgnore
	private int id;

	/** Year for the entry.
	 */
	@Column
	@ColumnDefault("0")
	@JsonIgnore
	private int referenceYear;

	/** CORE Index.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private CoreRanking coreIndex;

	/** Default constructor.
	 */
	public ConferenceQualityAnnualIndicators() {
		this.referenceYear = 0;
		this.coreIndex = null;
	}

	/** Construct an history with the given values.
	 *
	 * @param year the year of the entry.
	 * @param coreIndex the CORE index.
	 */
	public ConferenceQualityAnnualIndicators(int year, CoreRanking coreIndex) {
		this.referenceYear = year;
		this.coreIndex = coreIndex;
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.referenceYear);
		h = HashCodeUtils.add(h, this.coreIndex);
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
		final ConferenceQualityAnnualIndicators other = (ConferenceQualityAnnualIndicators) obj;
		if (this.id != other.id) {
			return false;
		}
		if (this.referenceYear != other.referenceYear) {
			return false;
		}
		if (!Objects.equals(this.coreIndex, other.coreIndex)) {
			return false;
		}
		return true;
	}

	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		if (getCoreIndex() != null) {
			consumer.accept("coreIndex", getCoreIndex()); //$NON-NLS-1$
		}
		if (getReferenceYear() > 0) {
			consumer.accept("referenceYear", Integer.valueOf(getReferenceYear())); //$NON-NLS-1$
		}
	}

	/** Replies the year for this history entry.
	 *
	 * @return the year.
	 */
	public int getReferenceYear() {
		return this.referenceYear;
	}

	/** Change the year for this history entry.
	 *
	 * @param year the year.
	 */
	public void setReferenceYear(int year) {
		this.referenceYear = year;
	}

	/** Change the year for this history entry.
	 *
	 * @param year the year.
	 */
	public final void setReferenceYear(Number year) {
		if (year == null) {
			setReferenceYear(0);
		} else {
			setReferenceYear(year.intValue());
		}
	}

	/** Replies the index of the conference from CORE source.
	 * 
	 * @return the index, or {@code null} if not defined.
	 */
	public CoreRanking getCoreIndex() {
		return this.coreIndex;
	}

	/** Change the index of the conference from CORE source.
	 *
	 * @param index the CORE index.
	 */
	public void setCoreIndex(CoreRanking index) {
		this.coreIndex = index;
	}

	/** Change the index of the conference from CORE source.
	 *
	 * @param index the CORE index.
	 */
	public void setCoreIndex(String index) {
		if (Strings.isNullOrEmpty(index)) {
			setCoreIndex((CoreRanking) null);
		} else {
			setCoreIndex(CoreRanking.valueOfCaseInsensitive(index));
		}
	}

}
