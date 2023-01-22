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

package fr.ciadlab.labmanager.entities.assostructure;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

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
import fr.ciadlab.labmanager.entities.AttributeProvider;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.IdentifiableEntity;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.utils.HashCodeUtils;

/** Description of an holder for the creation of an associated structure.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.0
 */
@Entity
@Table(name = "AssociatedStructureHolders")
public class AssociatedStructureHolder implements Serializable, AttributeProvider, Comparable<AssociatedStructureHolder>, IdentifiableEntity {

	private static final long serialVersionUID = 4459761172461217306L;

	/** Identifier of the associated structure holder.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	/** Holder of the associated structure creation.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Person person;

	/** Role of the member.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private HolderRole role = HolderRole.SCIENTIFIC_HEAD;

	/** Role of the member.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String roleDescription;

	/**
	 * Organization of the person.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private ResearchOrganization organization;

	/**
	 * Super-Organization of the organization of the person.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private ResearchOrganization superOrganization;

	/** Construct an empty member.
	 */
	public AssociatedStructureHolder() {
		//
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.person);
		h = HashCodeUtils.add(h, this.role);
		h = HashCodeUtils.add(h, this.roleDescription);
		h = HashCodeUtils.add(h, this.organization);
		h = HashCodeUtils.add(h, this.superOrganization);
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
		final AssociatedStructureHolder other = (AssociatedStructureHolder) obj;
		if (!Objects.equals(this.role, other.role)) {
			return false;
		}
		if (!Objects.equals(this.roleDescription, other.roleDescription)) {
			return false;
		}
		if (!Objects.equals(this.organization, other.organization)) {
			return false;
		}
		if (!Objects.equals(this.superOrganization, other.superOrganization)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(AssociatedStructureHolder o) {
		return EntityUtils.getPreferredAssociatedStructureHolderComparator().compare(this, o);
	}

	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		if (getRole() != null) {
			consumer.accept("role", getRole()); //$NON-NLS-1$
			consumer.accept("roleDescription", getRoleDescription()); //$NON-NLS-1$
		}
	}

	@Override
	public int getId() {
		return this.id;
	}

	/** Change the holder identifier.
	 *
	 * @param id the identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/** Replies the holding person.
	 *
	 * @return the person.
	 */
	public Person getPerson() {
		return this.person;
	}

	/** Change the holding person.
	 *
	 * @param person the person.
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/** Replies the role of the person in the associated structure.
	 *
	 * @return the role.
	 */
	public HolderRole getRole() {
		return this.role;
	}

	/** Change the role of the member in the project.
	 *
	 * @param role the role.
	 */
	public void setRole(HolderRole role) {
		if (role == null) {
			this.role = HolderRole.SCIENTIFIC_HEAD;
		} else {
			this.role = role;
		}
	}

	/** Change the role of the member.
	 *
	 * @param role the role of the member.
	 */
	public final void setRole(String role) {
		if (Strings.isNullOrEmpty(role)) {
			setRole((HolderRole) null);
		} else {
			setRole(HolderRole.valueOfCaseInsensitive(role));
		}
	}

	/** Replies a short description of the role of the person in the associated structure.
	 *
	 * @return the role description.
	 */
	public String getRoleDescription() {
		return this.roleDescription;
	}

	/** Change short description of the role of the member in the project.
	 *
	 * @param description the role description.
	 */
	public void setRoleDescription(String description) {
		this.roleDescription = Strings.emptyToNull(description);
	}

	/** Replies the organization of the holding person.
	 *
	 * @return the organization.
	 */
	public ResearchOrganization getOrganization() {
		return this.organization;
	}

	/** Change the organization of the holding person.
	 *
	 * @param organization the organization.
	 */
	public void setOrganization(ResearchOrganization organization) {
		this.organization = organization;
	}

	/** Replies the super organization of the holding person.
	 *
	 * @return the organization.
	 */
	public ResearchOrganization getSuperOrganization() {
		return this.superOrganization;
	}

	/** Change the super organization of the holding person.
	 *
	 * @param organization the super organization.
	 */
	public void setSuperOrganization(ResearchOrganization organization) {
		this.superOrganization = organization;
	}

}