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

package fr.ciadlab.labmanager.entities.publication;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.utils.HashCodeUtils;

/** Author link between a person and a research publication.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "Authorship")
public class Authorship implements Serializable, Comparable<Authorship> {

	private static final long serialVersionUID = -6870718668893845051L;

	/** Identifier of the authorship in the database.
	 */
	@Id
	@GeneratedValue
	private int id;

	/** Reference to the publication.
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Publication publication;

	/** Reference to the person.
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Person person;

	/** Author rank determines the order of the author in the list of authors for the concerned publication.
	 */
	@Column(nullable = false)
	private int authorRank;

	/** Construct the authorship with empty fields.
	 */
	public Authorship() {
		//
	}

	/** Construct the authorship with the given values.
	 *
	 * @param publication the publication.
	 * @param author the author.
	 * @param authorRank the order of the author in the list of authors.
	 * @see Publication
	 */
	public Authorship(Publication publication, Person author, int authorRank) {
		this.publication = publication;
		this.person = author;
		this.authorRank = authorRank;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final Authorship that = (Authorship) o;
		return this.id == that.id
				&& this.publication == that.publication
				&& this.person == that.person
				&& this.authorRank == that.authorRank;
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.publication);
		h = HashCodeUtils.add(h, this.person);
		h = HashCodeUtils.add(h, this.authorRank);
		return h;
	}

	@Override
	public int compareTo(Authorship o) {
		return AuthorshipComparator.DEFAULT.compare(this, o);
	}

	/** Replies the identifier of the authorship.
	 *
	 * @return the identifier.
	 */
	public int getId() {
		return this.id;
	}

	/** Change the identifier of the authorship.
	 *
	 * @param id the identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/** Replies the publication.
	 *
	 * @return the publication.
	 */
	public Publication getPublication() {
		return this.publication;
	}

	/** Change the publication.
	 *
	 * @param publication the publication.
	 */
	public void setPublication(Publication publication) {
		this.publication = publication;
	}

	/** Replies the author.
	 *
	 * @return the author.
	 */
	public Person getPerson() {
		return this.person;
	}

	/** Change the author.
	 *
	 * @param person the author.
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/** Replies the rank of the author in the list of authors for the publication.
	 *
	 * @return the rank number.
	 */
	public int getAuthorRank() {
		return this.authorRank;
	}

	/** Change the rank of the author in the list of authors for the publication.
	 *
	 * @param rank the rank number.
	 */
	public void setAuthorRank(int rank) {
		this.authorRank = rank;
	}

}


