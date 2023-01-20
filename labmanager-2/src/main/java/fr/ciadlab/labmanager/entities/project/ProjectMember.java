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

package fr.ciadlab.labmanager.entities.project;

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
import fr.ciadlab.labmanager.utils.HashCodeUtils;

/** Description of a member of a project.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.0
 */
@Entity
@Table(name = "ProjectMembers")
public class ProjectMember implements Serializable, AttributeProvider, Comparable<ProjectMember>, IdentifiableEntity {

	private static final long serialVersionUID = 7198080633760598725L;

	/** Identifier of the member.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	/** Promoter or director of the candidate.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Person person;

	/** Role of the member.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private Role role = Role.PARTICIPANT;

	/** Construct an empty member.
	 */
	public ProjectMember() {
		//
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.person);
		h = HashCodeUtils.add(h, this.role);
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
		final ProjectMember other = (ProjectMember) obj;
		if (!Objects.equals(this.role, other.role)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(ProjectMember o) {
		return EntityUtils.getPreferredProjectMemberComparator().compare(this, o);
	}

	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		if (getRole() != null) {
			consumer.accept("role", getRole()); //$NON-NLS-1$
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

	/** Replies the member person.
	 *
	 * @return the person.
	 */
	public Person getPerson() {
		return this.person;
	}

	/** Change the member person.
	 *
	 * @param person the person.
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/** Replies the role of the member in the project.
	 *
	 * @return the role.
	 */
	public Role getRole() {
		return this.role;
	}

	/** Change the role of the member in the project.
	 *
	 * @param role the role.
	 */
	public void setRole(Role role) {
		if (role == null) {
			this.role = Role.PARTICIPANT;
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
			setRole((Role) null);
		} else {
			setRole(Role.valueOfCaseInsensitive(role));
		}
	}

}
