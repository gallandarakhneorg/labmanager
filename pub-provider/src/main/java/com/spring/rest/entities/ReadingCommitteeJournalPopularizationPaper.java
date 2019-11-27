package com.spring.rest.entities;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="ReadingCommitteeJournalsPopularizationPapers")
@PrimaryKeyJoinColumn(name="pubId")
//I realised way too late that this was a journal type and not a conference type
//As a result all of the attributes are still named reaComConf instead of reaComJour
public class ReadingCommitteeJournalPopularizationPaper extends Publication{ 
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 3154620399419184751L;

	@Column
	private String reaComConfPopPapJournalName;

	@Column
	private String reaComConfPopPapVolume;

	@Column
	private String reaComConfPopPapNumber;

	@Column
	private String reaComConfPopPapPages;

	@Column
	private String reaComConfPopPapPublisher;

	public String getReaComConfPopPapJournalName() {
		return reaComConfPopPapJournalName;
	}

	public void setReaComConfPopPapJournalName(String reaComConfPopPapJournalName) {
		this.reaComConfPopPapJournalName = reaComConfPopPapJournalName;
	}

	public String getReaComConfPopPapVolume() {
		return reaComConfPopPapVolume;
	}

	public void setReaComConfPopPapVolume(String reaComConfPopPapVolume) {
		this.reaComConfPopPapVolume = reaComConfPopPapVolume;
	}

	public String getReaComConfPopPapNumber() {
		return reaComConfPopPapNumber;
	}

	public void setReaComConfPopPapNumber(String reaComConfPopPapNumber) {
		this.reaComConfPopPapNumber = reaComConfPopPapNumber;
	}

	public String getReaComConfPopPapPages() {
		return reaComConfPopPapPages;
	}

	public void setReaComConfPopPapPages(String reaComConfPopPapPages) {
		this.reaComConfPopPapPages = reaComConfPopPapPages;
	}

	public String getReaComConfPopPapPublisher() {
		return reaComConfPopPapPublisher;
	}

	public void setReaComConfPopPapPublisher(String reaComConfPopPapPublisher) {
		this.reaComConfPopPapPublisher = reaComConfPopPapPublisher;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((reaComConfPopPapJournalName == null) ? 0 : reaComConfPopPapJournalName.hashCode());
		result = prime * result + ((reaComConfPopPapNumber == null) ? 0 : reaComConfPopPapNumber.hashCode());
		result = prime * result + ((reaComConfPopPapPages == null) ? 0 : reaComConfPopPapPages.hashCode());
		result = prime * result + ((reaComConfPopPapPublisher == null) ? 0 : reaComConfPopPapPublisher.hashCode());
		result = prime * result + ((reaComConfPopPapVolume == null) ? 0 : reaComConfPopPapVolume.hashCode());
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
		ReadingCommitteeJournalPopularizationPaper other = (ReadingCommitteeJournalPopularizationPaper) obj;
		if (reaComConfPopPapJournalName == null) {
			if (other.reaComConfPopPapJournalName != null)
				return false;
		} else if (!reaComConfPopPapJournalName.equals(other.reaComConfPopPapJournalName))
			return false;
		if (reaComConfPopPapNumber == null) {
			if (other.reaComConfPopPapNumber != null)
				return false;
		} else if (!reaComConfPopPapNumber.equals(other.reaComConfPopPapNumber))
			return false;
		if (reaComConfPopPapPages == null) {
			if (other.reaComConfPopPapPages != null)
				return false;
		} else if (!reaComConfPopPapPages.equals(other.reaComConfPopPapPages))
			return false;
		if (reaComConfPopPapPublisher == null) {
			if (other.reaComConfPopPapPublisher != null)
				return false;
		} else if (!reaComConfPopPapPublisher.equals(other.reaComConfPopPapPublisher))
			return false;
		if (reaComConfPopPapVolume == null) {
			if (other.reaComConfPopPapVolume != null)
				return false;
		} else if (!reaComConfPopPapVolume.equals(other.reaComConfPopPapVolume))
			return false;
		return true;
	}

	public ReadingCommitteeJournalPopularizationPaper(int pubId, Set<Author> pubAuthorsByJoint, String pubTitle,
			String pubAbstract, String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN,
			String pubISSN, String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, PublicationType pubType, String reaComConfPopPapJournalName,
			String reaComConfPopPapVolume, String reaComConfPopPapNumber, String reaComConfPopPapPages,
			String reaComConfPopPapPublisher) {
		super(pubId, pubAuthorsByJoint, pubTitle, pubAbstract, pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN,
				pubISSN, pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage, pubPaperAwardPath, pubType);
		this.reaComConfPopPapJournalName = reaComConfPopPapJournalName;
		this.reaComConfPopPapVolume = reaComConfPopPapVolume;
		this.reaComConfPopPapNumber = reaComConfPopPapNumber;
		this.reaComConfPopPapPages = reaComConfPopPapPages;
		this.reaComConfPopPapPublisher = reaComConfPopPapPublisher;
	}

	public ReadingCommitteeJournalPopularizationPaper() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReadingCommitteeJournalPopularizationPaper(int pubId, Set<Author> pubAuthorsByJoint, String pubTitle,
			String pubAbstract, String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN,
			String pubISSN, String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, PublicationType pubType) {
		super(pubId, pubAuthorsByJoint, pubTitle, pubAbstract, pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN,
				pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage, pubPaperAwardPath, pubType);
		// TODO Auto-generated constructor stub
	}

}


