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

package fr.ciadlab.labmanager.entities.journal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.hibernate.annotations.ColumnDefault;

/** Histoty of the quality indicators for a journal.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "JournalAnnualIndicators")
public class JournalQualityAnnualIndicators implements Serializable {

	private static final long serialVersionUID = -3671513001937890573L;

	/** Identifier for the history entry.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	/** Year for the entry.
	 */
	@Column
	@ColumnDefault("0")
	private int referenceYear;

	/** Scimargo Q-Index.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private QuartileRanking scimagoQIndex;

	/** JCR/Web-Of-Science Q-Index.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private QuartileRanking wosQIndex;

	/** Impact factor.
	 */
	@Column
	@ColumnDefault("0")
	private float impactFactor;

	/** Default constructor.
	 */
	public JournalQualityAnnualIndicators() {
		this.referenceYear = 0;
		this.scimagoQIndex = null;
		this.wosQIndex = null;
		this.impactFactor = 0;
	}

	/** Construct an history with the given values.
	 *
	 * @param year the year of the entry.
	 * @param scimagoQuartile the Scimargo Q-index.
	 * @param wosQuartile the JCR/Web-Of-Science Q-Index.
	 * @param impactFactor the journal impact factor.
	 */
	public JournalQualityAnnualIndicators(int year, QuartileRanking scimagoQuartile,
			QuartileRanking wosQuartile, float impactFactor) {
		this.referenceYear = year;
		this.scimagoQIndex = scimagoQuartile;
		this.wosQIndex = wosQuartile;
		this.impactFactor = impactFactor;
	}

	@Override
	public String toString() {
		return ":" + this.id; //$NON-NLS-1$
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

	/** Replies the Q-Index of the journal from Scimago source.
	 * 
	 * @return the Q-Index, or {@code null} if not defined.
	 */
	public QuartileRanking getScimagoQIndex() {
		return this.scimagoQIndex;
	}

	/** Change the Q-Index of the journal from Scimago source.
	 *
	 * @param quartile the Q-Index.
	 */
	public void setScimagoQIndex(QuartileRanking  quartile) {
		this.scimagoQIndex = quartile;
	}

	/** Change the Q-Index of the journal from Scimago source.
	 *
	 * @param quartile the Q-Index.
	 */
	public final void setScimagoQIndex(String quartile) {
		if (Strings.isNullOrEmpty(quartile)) {
			setScimagoQIndex((QuartileRanking) null);
		} else {
			setScimagoQIndex(QuartileRanking.valueOfCaseInsensitive(quartile));
		}
	}

	/** Replies the Q-Index of the journal from JCR/Web-Of-Science source.
	 * 
	 * @return the Q-Index, or {@code null} if not defined.
	 */
	public QuartileRanking getWosQIndex() {
		return this.wosQIndex;
	}

	/** Change the Q-Index of the journal from JCR/Web-Of-Science source.
	 * 
	 * @param quartile the Q-Index.
	 */
	public void setWosQIndex(QuartileRanking quartile) {
		this.wosQIndex = quartile;
	}

	/** Change the Q-Index of the journal from JCR/Web-Of-Science source.
	 * 
	 * @param quartile the Q-Index.
	 */
	public final void setWosQIndex(String quartile) {
		if (Strings.isNullOrEmpty(quartile)) {
			setWosQIndex((QuartileRanking) null);
		} else {
			setWosQIndex(QuartileRanking.valueOfCaseInsensitive(quartile));
		}
	}

	/** Replies the impact factor of the journal.
	 * 
	 * @return the impact factor, or {@code 0} if not defined
	 */
	public float getImpactFactor() {
		return this.impactFactor;
	}

	/** Change the impact factor of the journal.
	 * 
	 * @param impactFactor the impact factor that is a positive number.
	 */
	public void setImpactFactor(float impactFactor) {
		if (impactFactor >= 0f) {
			this.impactFactor = impactFactor;
		} else {
			this.impactFactor = 0f;
		}
	}

	/** Change the impact factor of the journal.
	 * 
	 * @param impactFactor the impact factor that is a positive number.
	 */
	public final void setImpactFactor(Number impactFactor) {
		if (impactFactor == null) {
			setImpactFactor(0f);
		} else {
			setImpactFactor(impactFactor.floatValue());
		}
	}

}
