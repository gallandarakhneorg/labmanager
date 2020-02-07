package com.spring.rest.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="authorship")
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class Authorship implements Serializable {
	
	private static final long serialVersionUID = -1083342117826188053L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(nullable=false)
	private int autShipId;

	@ManyToOne
	private Publication pub;
	
	@ManyToOne
	private Author aut;
	
	//Rank determining the order of the author in the concerned publication
	@Column
	private int rank;

	public int getAutShipId() {
		return autShipId;
	}

	public void setAutShipId(int autShipId) {
		this.autShipId = autShipId;
	}

	public Publication getPub() {
		return pub;
	}

	public void setPub(Publication pub) {
		this.pub = pub;
	}

	public Author getAut() {
		return aut;
	}

	public void setAut(Author aut) {
		this.aut = aut;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//result = prime * result + ((aut == null) ? 0 : aut.hashCode());
		result = prime * result + autShipId;
		//result = prime * result + ((pub == null) ? 0 : pub.hashCode());
		result = prime * result + rank;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Authorship other = (Authorship) obj;
		if (aut == null) {
			if (other.aut != null)
				return false;
		} else if (!aut.equals(other.aut))
			return false;
		if (autShipId != other.autShipId)
			return false;
		if (pub == null) {
			if (other.pub != null)
				return false;
		} else if (!pub.equals(other.pub))
			return false;
		if (rank != other.rank)
			return false;
		return true;
	}

	public Authorship(int autShipId, Publication pub, Author aut, int rank) {
		super();
		this.autShipId = autShipId;
		this.pub = pub;
		this.aut = aut;
		this.rank = rank;
	}

	public Authorship() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
}


