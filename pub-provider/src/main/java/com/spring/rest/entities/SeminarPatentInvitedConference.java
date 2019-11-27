package com.spring.rest.entities;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="seminarsPatentsInvitedConferences")
@PrimaryKeyJoinColumn(name="pubId")
//I forgot the type invited conference so this gets referenced as semPat instead of semPatInvConf
public class SeminarPatentInvitedConference extends Publication{ 
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -313315999200513804L;

	@Column
	private String semPatHowPub;

	public String getSemPatHowPub() {
		return semPatHowPub;
	}

	public void setSemPatHowPub(String semPatHowPub) {
		this.semPatHowPub = semPatHowPub;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((semPatHowPub == null) ? 0 : semPatHowPub.hashCode());
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
		SeminarPatentInvitedConference other = (SeminarPatentInvitedConference) obj;
		if (semPatHowPub == null) {
			if (other.semPatHowPub != null)
				return false;
		} else if (!semPatHowPub.equals(other.semPatHowPub))
			return false;
		return true;
	}

	public SeminarPatentInvitedConference(int pubId, Set<Author> pubAuthorsByJoint, String pubTitle, String pubAbstract,
			String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
			String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, PublicationType pubType, String semPatHowPub) {
		super(pubId, pubAuthorsByJoint, pubTitle, pubAbstract, pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN,
				pubISSN, pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage, pubPaperAwardPath, pubType);
		this.semPatHowPub = semPatHowPub;
	}

	public SeminarPatentInvitedConference() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SeminarPatentInvitedConference(int pubId, Set<Author> pubAuthorsByJoint, String pubTitle, String pubAbstract,
			String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
			String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, PublicationType pubType) {
		super(pubId, pubAuthorsByJoint, pubTitle, pubAbstract, pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN,
				pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage, pubPaperAwardPath, pubType);
		// TODO Auto-generated constructor stub
	}

	
	
}


