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

package fr.ciadlab.labmanager.entities.member;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.BiConsumer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.utils.AttributeProvider;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import fr.ciadlab.labmanager.utils.JsonExportable;
import fr.ciadlab.labmanager.utils.JsonUtils;

/** Relation between a person and a research organization.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "Memberships")
public class Membership implements Serializable, JsonExportable, AttributeProvider, Comparable<Membership> {

	private static final long serialVersionUID = 297499358606685801L;

	/** Identifier of the membership.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	/** First date of involvement.
	 */
	@Column
	private Date memberSinceWhen;

	/** Last dat eof involvement.
	 */
	@Column
	private Date memberToWhen;

	/** Status of the person in the research organization.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private MemberStatus memberStatus;

	/** Number of the CNU section of the person related to which membership.
	 */
	@Column
	private int cnuSection;

	/** Reference to the person.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	private Person person;

	/** Reference to the research organization.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	private ResearchOrganization researchOrganization;

	/** Construct a membership with the given values.
	 *
	 * @param person the person.
	 * @param orga the research organization.
	 * @param since the start date of involvement.
	 * @param to the end date of involvement.
	 * @param status the status.
	 * @param cnuSection is the number of the CNU section, or {@code 0} if it is unknown or irrelevant.
	 */
	public Membership(Person person, ResearchOrganization orga, Date since, Date to, MemberStatus status, int cnuSection) {
		this.person = person;
		this.researchOrganization = orga;
		this.memberSinceWhen = since;
		this.memberToWhen = to;
		this.memberStatus = status;
		this.cnuSection = cnuSection;
	}

	/** Construct an empty membership.
	 */
	public Membership() {
		//
	}

	@Override
	public String toString() {
		return "{" + getPerson() + "}{" + getResearchOrganization() + "}{" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ getMemberStatus() + "}{" //$NON-NLS-1$
				+ getMemberSinceWhen() + "}{" //$NON-NLS-1$
				+ getMemberToWhen() + "}:" + this.id; //$NON-NLS-1$
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.person);
		h = HashCodeUtils.add(h, this.researchOrganization);
		h = HashCodeUtils.add(h, this.memberStatus);
		h = HashCodeUtils.add(h, this.memberSinceWhen);
		h = HashCodeUtils.add(h, this.memberToWhen);
		h = HashCodeUtils.add(h, this.cnuSection);
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
		final Membership other = (Membership) obj;
		if (!Objects.equals(this.person, other.person)) {
			return false;
		}
		if (!Objects.equals(this.researchOrganization, other.researchOrganization)) {
			return false;
		}
		if (!Objects.equals(this.memberStatus, other.memberStatus)) {
			return false;
		}
		if (!Objects.equals(this.memberSinceWhen, other.memberSinceWhen)) {
			return false;
		}
		if (!Objects.equals(this.memberToWhen, other.memberSinceWhen)) {
			return false;
		}
		if (this.cnuSection != other.cnuSection) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Membership o) {
		return MembershipComparator.DEFAULT.compare(this, o);
	}

	/** {@inheritDoc}
	 * <p>The attributes that are not considered by this function are:<ul>
	 * <li>{@code id}</li>
	 * </ul>
	 */
	@Override
	public void forEachAttribute(BiConsumer<String, Object> consumer) {
		if (getPerson() != null) {
			consumer.accept("person", getPerson()); //$NON-NLS-1$
		}
		if (getResearchOrganization() != null) {
			consumer.accept("researchOrganization", getResearchOrganization()); //$NON-NLS-1$
		}
		if (getMemberStatus() != null) {
			consumer.accept("memberStatus", getMemberStatus()); //$NON-NLS-1$
		}
		if (getMemberSinceWhen() != null) {
			consumer.accept("memberSinceWhen", getMemberSinceWhen()); //$NON-NLS-1$
		}
		if (getMemberToWhen() != null) {
			consumer.accept("memberToWhen", getMemberToWhen()); //$NON-NLS-1$
		}
		if (getCnuSection() > 0) {
			consumer.accept("cnuSection", Integer.valueOf(getCnuSection())); //$NON-NLS-1$
		}
	}

	@Override
	public void toJson(JsonObject json) {
		json.addProperty("id", Integer.valueOf(getId())); //$NON-NLS-1$
		forEachAttribute((name, value) -> JsonUtils.defaultBehavior(json, name, value));
	}

	/** Replies the membership identifier.
	 *
	 * @return the identifier.
	 */
	public int getId() {
		return this.id;
	}

	/** Replies the person related to this membership.
	 *
	 * @return the person.
	 */
	public Person getPerson() {
		return this.person;
	}

	/** Change the person related to this membership.
	 *
	 * @param person the person.
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/** Replies the research organization related to this membership.
	 *
	 * @return the organization.
	 */
	public ResearchOrganization getResearchOrganization() {
		return this.researchOrganization;
	}

	/** Change the research organization related to this membership.
	 *
	 * @param orga the organization.
	 */
	public void setResearchOrganization(ResearchOrganization orga) {
		this.researchOrganization = orga;
	}

	/** Replies the first date of involvement in the research organization.
	 *
	 * @return the date.
	 */
	public Date getMemberSinceWhen() {
		return this.memberSinceWhen;
	}

	/** Change the first date of involvement in the research organization.
	 *
	 * @param date the date.
	 */
	public void setMemberSinceWhen(Date date) {
		this.memberSinceWhen = date;
	}

	/** Change the first date of involvement in the research organization.
	 *
	 * @param date the date.
	 */
	public final void setMemberSinceWhen(String date) {
		if (Strings.isNullOrEmpty(date)) {
			setMemberSinceWhen((Date) null);
		} else {
			setMemberSinceWhen(Date.valueOf(date));
		}
	}

	/** Replies the last date of involvement in the research organization.
	 *
	 * @return the date.
	 */
	public Date getMemberToWhen() {
		return this.memberToWhen;
	}

	/** Change the last date of involvement in the research organization.
	 *
	 * @param date the date.
	 */
	public void setMemberToWhen(Date date) {
		this.memberToWhen = date;
	}

	/** Change the last date of involvement in the research organization.
	 *
	 * @param date the date.
	 */
	public final void setMemberToWhen(String date) {
		if (Strings.isNullOrEmpty(date)) {
			setMemberToWhen((Date) null);
		} else {
			setMemberToWhen(Date.valueOf(date));
		}
	}

	/** Replies the status of the member in the research organization.
	 *
	 * @return the status.
	 */
	public MemberStatus getMemberStatus() {
		return this.memberStatus;
	}

	/** Change the status of the member in the research organization.
	 *
	 * @param status the status.
	 */
	public void setMemberStatus(MemberStatus status) {
		this.memberStatus = status;
	}

	/** Change the status of the member in the research organization.
	 *
	 * @param status the status.
	 */
	public final void setMemberStatus(String status) {
		if (Strings.isNullOrEmpty(status)) {
			setMemberStatus((MemberStatus) null);
		} else {
			setMemberStatus(MemberStatus.valueOfCaseInsensitive(status));
		}
	}

	/** Replies the CNU section of the member in the research organization.
	 * CNU means "Conseil National des Universités". 
	 *
	 * @return the CNY section number or {@code 0} if unknown.
	 */
	public int getCnuSection() {
		return this.cnuSection;
	}

	/** Change the CNU section of the member in the research organization.
	 * CNU means "Conseil National des Universités". 
	 *
	 * @param cnu the CNU section number or {@code 0} if unknown.
	 */
	public void setCnuSection(int cnu) {
		if (cnu < 0) {
			this.cnuSection = 0;
		} else {
			this.cnuSection = cnu;
		}
	}

	/** Change the CNU section of the member in the research organization.
	 * CNU means "Conseil National des Universités". 
	 *
	 * @param cnu the CNU section number or {@code null} if unknown.
	 */
	public final void setCnuSection(Number cnu) {
		if (cnu == null) {
			setCnuSection(0);
		} else {
			setCnuSection(cnu.intValue());
		}
	}

	/** Replies if the membership is active.
	 * A membership is active if the current date is inside the membership time windows.
	 *
	 * @return {@code true} if the membership time windows contains the current date.
	 */
	public boolean isActive() {
		final LocalDate now = LocalDate.now();
		return isActiveAt(now);
	}

	/** Replies if the membership is active.
	 * A membership is active if the given date is inside the membership time windows.
	 *
	 * @param now the given date to consider.
	 * @return {@code true} if the membership time windows contains the given date.
	 */
	public boolean isActiveAt(LocalDate now) {
		final Date s = getMemberSinceWhen();
		if (s != null) {
			final LocalDate start = s.toLocalDate();
			if (now.isBefore(start)) {
				return false;
			}
		}
		final Date e = getMemberToWhen();
		if (e != null) {
			final LocalDate end = e.toLocalDate();
			if (now.isAfter(end)) {
				return false;
			}
		}
		return true;
	}

	/** Replies if the membership is active during the given time windows.
	 * A membership is active if the current date is inside the membership time windows.
	 *
	 * @param windowStart is the start date of the windows, never {@code null}.
	 * @param windowEnd is the end date of the windows, never {@code null}.
	 * @return {@code true} if the membership time windows intersects the given date window.
	 */
	public boolean isActiveIn(LocalDate windowStart, LocalDate windowEnd) {
		assert windowStart != null;
		assert windowEnd != null;
		final Date s = getMemberSinceWhen();
		final Date e = getMemberToWhen();
		if (s != null) {
			final LocalDate start = s.toLocalDate();
			if (e != null) {
				final LocalDate end = e.toLocalDate();
				return !windowEnd.isBefore(start) && !windowStart.isAfter(end);
			}
			return !windowEnd.isBefore(start);
		}
		if (e != null) {
			final LocalDate end = e.toLocalDate();
			return !windowStart.isAfter(end);
		}
		return true;
	}

	/** Replies if the membership is for former member.
	 * A membership is for former member if the current date is after the end of the membership.
	 *
	 * @return {@code true} if the membership is finished.
	 */
	public boolean isFormer() {
		final Date dt = getMemberToWhen();
		if (dt == null) {
			// Without a end date, the membership is active for ever.
			// So that it cannot be for a former member.
			return false;
		}
		final LocalDate now = LocalDate.now();
		final LocalDate end = dt.toLocalDate();
		return now.isAfter(end);
	}

	/** Replies if the membership is for future member.
	 * A membership is for future member if the current date is before the start of the membership.
	 *
	 * @return {@code true} if the membership is not yet started.
	 */
	public boolean isFuture() {
		final Date dt = getMemberSinceWhen();
		if (dt == null) {
			// Without a start date, the membership is active since ever.
			// So that it cannot be for a future member.
			return false;
		}
		final LocalDate now = LocalDate.now();
		final LocalDate end = dt.toLocalDate();
		return now.isBefore(end);
	}

}


