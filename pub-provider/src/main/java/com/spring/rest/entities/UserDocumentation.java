package com.spring.rest.entities;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="userDocumentations")
@PrimaryKeyJoinColumn(name="pubId")
public class UserDocumentation extends Publication{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3250200064764956855L;

	@Column
	private String userDocOrganization;

	@Column
	private String userDocAddress;
	
	@Column
	private String userDocEdition;
	
	@Column
	private String userDocPublisher;

	public String getUserDocOrganization() {
		return userDocOrganization;
	}

	public void setUserDocOrganization(String userDocOrganization) {
		this.userDocOrganization = userDocOrganization;
	}

	public String getUserDocAddress() {
		return userDocAddress;
	}

	public void setUserDocAddress(String userDocAddress) {
		this.userDocAddress = userDocAddress;
	}

	public String getUserDocEdition() {
		return userDocEdition;
	}

	public void setUserDocEdition(String userDocEdition) {
		this.userDocEdition = userDocEdition;
	}

	public String getUserDocPublisher() {
		return userDocPublisher;
	}

	public void setUserDocPublisher(String userDocPublisher) {
		this.userDocPublisher = userDocPublisher;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((userDocAddress == null) ? 0 : userDocAddress.hashCode());
		result = prime * result + ((userDocEdition == null) ? 0 : userDocEdition.hashCode());
		result = prime * result + ((userDocOrganization == null) ? 0 : userDocOrganization.hashCode());
		result = prime * result + ((userDocPublisher == null) ? 0 : userDocPublisher.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDocumentation other = (UserDocumentation) obj;
		if (userDocAddress == null) {
			if (other.userDocAddress != null)
				return false;
		} else if (!userDocAddress.equals(other.userDocAddress))
			return false;
		if (userDocEdition == null) {
			if (other.userDocEdition != null)
				return false;
		} else if (!userDocEdition.equals(other.userDocEdition))
			return false;
		if (userDocOrganization == null) {
			if (other.userDocOrganization != null)
				return false;
		} else if (!userDocOrganization.equals(other.userDocOrganization))
			return false;
		if (userDocPublisher == null) {
			if (other.userDocPublisher != null)
				return false;
		} else if (!userDocPublisher.equals(other.userDocPublisher))
			return false;
		return true;
	}

	public UserDocumentation(int pubId, Set<Author> pubAuthorsByJoint, String pubTitle, String pubAbstract,
			String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
			String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, PublicationType pubType, String userDocOrganization, String userDocAddress,
			String userDocEdition, String userDocPublisher) {
		super(pubId, pubAuthorsByJoint, pubTitle, pubAbstract, pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN,
				pubISSN, pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage, pubPaperAwardPath, pubType);
		this.userDocOrganization = userDocOrganization;
		this.userDocAddress = userDocAddress;
		this.userDocEdition = userDocEdition;
		this.userDocPublisher = userDocPublisher;
	}

	public UserDocumentation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserDocumentation(int pubId, Set<Author> pubAuthorsByJoint, String pubTitle, String pubAbstract,
			String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
			String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, PublicationType pubType) {
		super(pubId, pubAuthorsByJoint, pubTitle, pubAbstract, pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN,
				pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage, pubPaperAwardPath, pubType);
		// TODO Auto-generated constructor stub
	}
	
	
	
}


