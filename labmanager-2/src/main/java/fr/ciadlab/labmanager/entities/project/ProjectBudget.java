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

package fr.ciadlab.labmanager.entities.project;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.AttributeProvider;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.IdentifiableEntity;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import fr.ciadlab.labmanager.utils.funding.FundingScheme;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Description of a specific budget for a project.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.0
 */
@Entity
@Table(name = "ProjectBudgets")
public class ProjectBudget implements Serializable, AttributeProvider, Comparable<ProjectBudget>, IdentifiableEntity {

	private static final long serialVersionUID = -2642101613859032867L;

	/** Identifier of the budget.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	/** Budget only for the research organization.
	 */
	@Column
	private float budget;

	/** Funding scheme of the project.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private FundingScheme fundingScheme = FundingScheme.NOT_FUNDED;

	/** Grant number of the project.
	 */
	@Column
	private String grant;

	/** Construct an empty member.
	 */
	public ProjectBudget() {
		//
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.budget);
		h = HashCodeUtils.add(h, this.fundingScheme);
		h = HashCodeUtils.add(h, this.grant);
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
		final ProjectBudget other = (ProjectBudget) obj;
		if (this.budget != other.budget) {
			return false;
		}
		if (!Objects.equals(this.fundingScheme, other.fundingScheme)) {
			return false;
		}
		if (!Objects.equals(this.grant, other.grant)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(ProjectBudget o) {
		return EntityUtils.getPreferredProjectBudgetComparator().compare(this, o);
	}

	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		if (getBudget() > 0f) {
			consumer.accept("budget", Float.valueOf(getBudget())); //$NON-NLS-1$
		}
		if (getFundingScheme() != null) {
			consumer.accept("fundingScheme", getFundingScheme()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getGrant())) {
			consumer.accept("grant", getGrant()); //$NON-NLS-1$
		}
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

	/** Replies the funding scheme of the project.
	 *
	 * @return the funding scheme.
	 */
	public FundingScheme getFundingScheme() {
		return this.fundingScheme;
	}

	/** Change the funding scheme of the project.
	 *
	 * @param fundingScheme the funding scheme.
	 */
	public void setFundingScheme(FundingScheme fundingScheme) {
		if (fundingScheme == null) {
			this.fundingScheme = FundingScheme.NOT_FUNDED;
		} else {
			this.fundingScheme = fundingScheme;
		}
	}

	/** Change the funding scheme of the project.
	 *
	 * @param fundingScheme the funding scheme.
	 */
	public final void setFundingScheme(String fundingScheme) {
		try {
			setFundingScheme(FundingScheme.valueOfCaseInsensitive(fundingScheme));
		} catch (Throwable ex) {
			setFundingScheme((FundingScheme) null);
		}
	}

	/** Replies the grant number of the project.
	 *
	 * @return the grant number.
	 */
	public String getGrant() {
		return this.grant;
	}

	/** Change the grant number of the project.
	 *
	 * @param grant the grant number.
	 */
	public void setGrant(String grant) {
		this.grant = Strings.emptyToNull(grant);
	}

	/** Replies the budget for the local organization in Kilo euros.
	 *
	 * @return the budget in kilo euros.
	 */
	public float getBudget() {
		return this.budget;
	}

	/** Set the budget for the local organization in Kilo euros.
	 *
	 * @param budget the budget in kilo euros.
	 */
	public void setBudget(float budget) {
		if (budget > 0f) {
			this.budget = budget;
		} else {
			this.budget = 0f;
		}
	}

	/** Set the budget for the local organization in Kilo euros.
	 *
	 * @param budget the budget in kilo euros.
	 */
	public final void setBudget(Number budget) {
		if (budget != null) {
			setBudget(budget.floatValue());
		} else {
			setBudget(0f);
		}
	}

}
