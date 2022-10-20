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

package fr.ciadlab.labmanager.entities.supervision;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.AttributeProvider;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.IdentifiableEntity;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import fr.ciadlab.labmanager.utils.funding.FundingScheme;

/** Description of a person supervision.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.1
 */
@Entity
@Table(name = "Supervisions")
public class Supervision implements Serializable, AttributeProvider, Comparable<Supervision>, IdentifiableEntity {

	private static final long serialVersionUID = 1934029393633237499L;

	/** Identifier of the jury.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	/** Reference to the supervised membership.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Membership supervisedPerson;

	/** The promoters or directors of the supervised person.
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Supervisor> supervisors;

	/** Title of the works.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String title;

	/** Funding scheme.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private FundingScheme funding = FundingScheme.NOT_FUNDED;

	/** Some details on the funding scheme.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String fundingDetails;

	/** Date of the defense. It may be different than the end date of the associated membership to the supervision object.
	 */
	@Column
	private LocalDate defenseDate;

	/** Description of the position of the supervised person after the supervision period.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String positionAfterSupervision;

	/** Number of ATER positions given to the supervised person.
	 */
	@Column
	private int numberOfAterPositions;

	/** Indicates if the supervised person has a joint position from the different organizations
	 * of the supervisors.
	 */
	@Column
	private boolean isJointPosition;

	/** Indicates if the supervized person is also an entrepreneur that is creating his/her company.
	 */
	@Column
	private boolean isEntrepreneur;

	/** Indicates if the supervised person has abandoned its supervised work.
	 */
	@Column
	private boolean abandonment;

	/** Construct an empty supervision.
	 */
	public Supervision() {
		//
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.supervisedPerson);
//		h = HashCodeUtils.add(h, this.supervisors);
		h = HashCodeUtils.add(h, this.title);
		h = HashCodeUtils.add(h, this.funding);
//		h = HashCodeUtils.add(h, this.fundingDetails);
		h = HashCodeUtils.add(h, this.defenseDate);
//		h = HashCodeUtils.add(h, this.positionAfterSupervision);
		h = HashCodeUtils.add(h, this.numberOfAterPositions);
		h = HashCodeUtils.add(h, this.isJointPosition);
		h = HashCodeUtils.add(h, this.isEntrepreneur);
		h = HashCodeUtils.add(h, this.abandonment);
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
		final Supervision other = (Supervision) obj;
		if (!Objects.equals(this.supervisedPerson, other.supervisedPerson)) {
			return false;
		}
//		if (!Objects.equals(this.supervisors, other.supervisors)) {
//			return false;
//		}
//		if (!Objects.equals(this.title, other.title)) {
//			return false;
//		}
		if (!Objects.equals(this.funding, other.funding)) {
			return false;
		}
//		if (!Objects.equals(this.fundingDetails, other.fundingDetails)) {
//			return false;
//		}
		if (!Objects.equals(this.defenseDate, other.defenseDate)) {
			return false;
		}
//		if (!Objects.equals(this.positionAfterSupervision, other.positionAfterSupervision)) {
//			return false;
//		}
		if (this.numberOfAterPositions != other.numberOfAterPositions) {
			return false;
		}
		if (this.isJointPosition != other.isJointPosition) {
			return false;
		}
		if (this.isEntrepreneur != other.isEntrepreneur) {
			return false;
		}
		if (this.abandonment != other.abandonment) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Supervision o) {
		return EntityUtils.getPreferredSupervisionComparator().compare(this, o);
	}

	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		if (getTitle() != null) {
			consumer.accept("title", getTitle()); //$NON-NLS-1$
		}
		if (getFunding() != null) {
			consumer.accept("funding", getFunding()); //$NON-NLS-1$
		}
		if (getFundingDetails() != null) {
			consumer.accept("fundingDetails", getFundingDetails()); //$NON-NLS-1$
		}
		if (getDefenseDate() != null) {
			consumer.accept("defenseDate", getDefenseDate()); //$NON-NLS-1$
		}
		if (getPositionAfterSupervision() != null) {
			consumer.accept("positionAfterSupervision", getPositionAfterSupervision()); //$NON-NLS-1$
		}
		if (getNumberOfAterPositions() > 0) {
			consumer.accept("numberOfAterPositions", Integer.valueOf(getNumberOfAterPositions())); //$NON-NLS-1$
		}
		consumer.accept("jointPosition", Boolean.valueOf(isJointPosition())); //$NON-NLS-1$
		consumer.accept("entrepreneur", Boolean.valueOf(isEntrepreneur())); //$NON-NLS-1$
		consumer.accept("abandonment", Boolean.valueOf(getAbandonment())); //$NON-NLS-1$
	}

	@Override
	public int getId() {
		return this.id;
	}

	/** Change the membership identifier.
	 *
	 * @param id the identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/** Replies the membership of the supervised person.
	 *
	 * @return the person.
	 */
	public Membership getSupervisedPerson() {
		return this.supervisedPerson;
	}

	/** Change the membership of the supervised person.
	 *
	 * @param supervisedPerson the supervised person.
	 */
	public void setSupervisedPerson(Membership supervisedPerson) {
		this.supervisedPerson = supervisedPerson;
	}

	/** Replies the supervisors of the supervised person.
	 *
	 * @return the supervisors.
	 */
	public List<Supervisor> getSupervisors() {
		if (this.supervisors == null) {
			this.supervisors = new ArrayList<>();
		}
		return this.supervisors;
	}

	/** Change the supervisors of the supervised person.
	 *
	 * @param supervisors the supervisors.
	 */
	public void setSupervisors(List<? extends Supervisor> supervisors) {
 		if (this.supervisors == null) {
			this.supervisors = new ArrayList<>();
		} else {
			this.supervisors.clear();
		}
		if (supervisors != null && !supervisors.isEmpty()) {
			this.supervisors.addAll(supervisors);
		}
	}

	/** Replies the title of the supervised works.
	 *
	 * @return the title of the supervised works.
	 */
	public String getTitle() {
		return this.title;
	}

	/** Replies the title of the supervised works.
	 *
	 * @param title the title of the supervised works.
	 */
	public void setTitle(String title) {
		this.title = Strings.emptyToNull(title);
	}

	/** Replies the funding scheme for this supervision.
	 *
	 * @return the funding scheme.
	 */
	public FundingScheme getFunding() {
		if (this.funding == null) {
			return FundingScheme.NOT_FUNDED;
		}
		return this.funding;
	}

	/** Change the funding scheme for this supervision.
	 *
	 * @param funding the funding scheme.
	 */
	public void setFunding(FundingScheme funding) {
		this.funding = funding == null ? FundingScheme.NOT_FUNDED : funding;
	}

	/** Change the funding scheme for this supervision.
	 *
	 * @param funding the funding scheme.
	 */
	public final void setFunding(String funding) {
		if (Strings.isNullOrEmpty(funding)) {
			setFunding((FundingScheme) null);
		} else {
			setFunding(FundingScheme.valueOfCaseInsensitive(funding));
		}
	}


	/** Replies the details for the funding.
	 *
	 * @return the funding details
	 */
	public String getFundingDetails() {
		return this.fundingDetails;
	}

	/** Replies the details for the funding.
	 *
	 * @param details the details for the funding.
	 */
	public void setFundingDetails(String details) {
		this.fundingDetails = Strings.emptyToNull(details);
	}

	/** Replies the known defense date.
	 *
	 * @return the defense date or {@code null} if unknown.
	 */
	public LocalDate getDefenseDate() {
		return this.defenseDate;
	}

	/** Change the known defense date.
	 *
	 * @param date the defense date.
	 */
	public void setDefenseDate(LocalDate date) {
		this.defenseDate = date;
	}

	/** Change the known defense date.
	 *
	 * @param date the defense date in format {@code "YYYY-MM-DD"}.
	 */
	public void setDefenseDate(String date) {
		if (Strings.isNullOrEmpty(date)) {
			setDefenseDate((LocalDate) null);
		} else {
			setDefenseDate(LocalDate.parse(date));
		}
	}

	/** Replies the description of the position of the supervised person AFTER the supervision period.
	 *
	 * @return the becoming of the supervised period.
	 */
	public String getPositionAfterSupervision() {
		return this.positionAfterSupervision;
	}

	/** Change the description of the position of the supervised person AFTER the supervision period.
	 *
	 * @param becoming the becoming of the supervised period.
	 */
	public void setPositionAfterSupervision(String becoming) {
		this.positionAfterSupervision = Strings.emptyToNull(becoming);
	}

	/** Replies the number of times the supervised person has received an ATER position at the end of the
	 * supervised person or just after.
	 *
	 * @return the number of ATER positions.
	 */
	public int getNumberOfAterPositions() {
		return this.numberOfAterPositions;
	}

	/** Change the number of times the supervised person has received an ATER position at the end of the
	 * supervised person or just after.
	 *
	 * @param times the number of ATER positions.
	 */
	public void setNumberOfAterPositions(int times) {
		if (times < 0) {
			this.numberOfAterPositions = 0;
		} else {
			this.numberOfAterPositions = times;
		}
	}

	/** Replies if the current supervision is a joint position between the institutions of the supervisors.
	 *
	 * @return {@code true} if the current supervising position is jointly supported by the institutions
	 *    of the supervisors.
	 */
	public boolean isJointPosition() {
		return this.isJointPosition;
	}

	/** Change the flag tat indicates if the current supervision is a joint position between the institutions of the supervisors.
	 *
	 * @param joint {@code true} if the current supervising position is jointly supported by the institutions
	 *    of the supervisors.
	 */
	public void setJointPosition(boolean joint) {
		this.isJointPosition = joint;
	}

	/** Replies if the supervised person is also an entrepreneur, i.e. s/he is creating a company in parallel
	 * to her/his supervision period.
	 *
	 * @return {@code true} if the supervised person is also an entrepreneur.
	 */
	public boolean isEntrepreneur() {
		return this.isEntrepreneur;
	}

	/** Change the flag that indicates if the supervised person is also an entrepreneur, i.e. s/he is creating a company in parallel
	 * to her/his supervision period.
	 *
	 * @param entrepreneur {@code true} if the supervised person is also an entrepreneur.
	 */
	public void setEntrepreneur(boolean entrepreneur) {
		this.isEntrepreneur = entrepreneur;
	}

	/** Replies if the supervised person has abandoned her/his position.
	 *
	 * @return {@code true} if the supervision was abandoned.
	 */
	public boolean getAbandonment() {
		return this.abandonment;
	}

	/** Change if the supervised person has abandoned her/his position.
	 *
	 * @param abandoned {@code true} if the supervision was abandoned.
	 */
	public void setAbandonment(boolean abandoned) {
		this.abandonment = abandoned;
	}

}
