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
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.pubprovider.entities;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.sql.Date;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "publications")
@Inheritance(strategy = InheritanceType.JOINED)
// @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class Publication implements Serializable {

    private static final long serialVersionUID = 4617703154899843388L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // Using this instead of IDENTITY allows for JOINED or TABLE_PER_CLASS
    // inheritance types to work
    @Column(nullable = false)
    private int pubId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "pubPubId", referencedColumnName = "pubId")
    private Set<Authorship> pubAuts = new HashSet<Authorship>();

    @Transient
    private List<Author> AuthorsList; 
    
    @Column
    private String pubTitle;

    @Column(columnDefinition = "TEXT")
    private String pubAbstract;

    @Column
    private String pubKeywords;
    
    

    @Column
    // @Temporal(TemporalType.DATE)
    private Date pubDate;

    @Column
    private int pubYear = getPubYear();

    @Column
    private String pubNote;

    @Column
    private String pubAnnotations;

    @Column
    private String pubISBN;

    @Column
    private String pubISSN;

    @Column
    private String pubDOIRef;

    @Column
    private String pubURL;

    @Column
    private String pubVideoURL;

    @Column
    private String pubDBLP;

    @Column
    private String pubPDFPath;

    @Column
    private String pubLanguage;

    @Column
    private String pubPaperAwardPath;

    @Column
    @Enumerated(EnumType.STRING)
    private PublicationType pubType;

    public Publication(Publication p) {
        this.pubId             = p.pubId;
        this.pubAuts           = p.pubAuts;
        this.AuthorsList       = p.AuthorsList;
        this.pubTitle          = p.pubTitle;
        this.pubAbstract       = p.pubAbstract;
        this.pubKeywords       = p.pubKeywords;
        this.pubDate           = p.pubDate;
        this.pubYear           = p.pubYear;
        this.pubNote           = p.pubNote;
        this.pubAnnotations    = p.pubAnnotations;
        this.pubISBN           = p.pubISBN;
        this.pubISSN           = p.pubISSN;
        this.pubDOIRef         = p.pubDOIRef;
        this.pubURL            = p.pubURL;
        this.pubVideoURL       = p.pubVideoURL;
        this.pubDBLP           = p.pubDBLP;
        this.pubPDFPath        = p.pubPDFPath;
        this.pubLanguage       = p.pubLanguage;
        this.pubPaperAwardPath = p.pubPaperAwardPath;
        this.pubType           = p.pubType;
    }

    public Publication(String pubTitle, String pubAbstract, String pubKeywords,
            Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
            String pubDOIRef, String pubURL, String pubVideoURL, String pubDBLP, String pubPDFPath, String pubLanguage,
            String pubPaperAwardPath, PublicationType pubType) {
        super();
        this.pubTitle = pubTitle;
        this.pubAbstract = pubAbstract;
        this.pubKeywords = pubKeywords;
        this.pubDate = pubDate;

        if (pubDate != null) {
            this.pubYear = pubDate.toLocalDate().getYear();
        } else {
            this.pubYear = 1970;
        }

        this.pubNote = pubNote;
        this.pubAnnotations = pubAnnotations;
        this.pubISBN = pubISBN;
        this.pubISSN = pubISSN;
        this.pubDOIRef = pubDOIRef;
        this.pubURL = pubURL;
        this.pubVideoURL = pubVideoURL;
        this.pubDBLP = pubDBLP;
        this.pubPDFPath = pubPDFPath;
        this.pubLanguage = pubLanguage;
        this.pubPaperAwardPath = pubPaperAwardPath;
        this.pubType = pubType;
    }

    public Publication() {
        super();
        // TODO Auto-generated constructor stub
    }

    public int getPubId() {
        return pubId;
    }

    public void setPubId(int pubId) {
        this.pubId = pubId;
    }

    public List<Authorship> getPubAuts() 
    {
        // Ordered by rank
        if (pubAuts != null)
            return pubAuts.stream().sorted(Comparator.comparingInt(Authorship::getAutShipRank))
                    .collect(Collectors.toList());
        return null;
    }

    public List<Author> getAuthorsList() 
    {
        // Ordered by rank
        if(this.AuthorsList != null)
            return this.AuthorsList;
        return null;
    }
    
    public void setPubAuts(Set<Authorship> pubAuts) {
        this.pubAuts = pubAuts;
    }
    
    public void setAuthorsList(List<Author> AuthorsList) {
        this.AuthorsList = AuthorsList;
    }

    public String getPubTitle() {
        return pubTitle;
    }

    public void setPubTitle(String pubTitle) {
        this.pubTitle = pubTitle;
    }

    public String getPubAbstract() {
        return pubAbstract;
    }

    public void setPubAbstract(String pubAbstract) {
        this.pubAbstract = pubAbstract;
    }

    public String getPubKeywords() {
        return pubKeywords;
    }

    public void setPubKeywords(String pubKeywords) {
        this.pubKeywords = pubKeywords;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;

        if (pubDate != null) {
            this.pubYear = pubDate.toLocalDate().getYear();
        } else {
            this.pubYear = 1970;
        }
    }

    public int getPubYear() {
        if (pubDate != null) {
            return pubDate.toLocalDate().getYear();
        } else {
            return 1970;
        }
    }

    public void setPubYear(int pubYear) {
        this.pubYear = pubYear;
    }

    public String getPubNote() {
        return pubNote;
    }

    public void setPubNote(String pubNote) {
        this.pubNote = pubNote;
    }

    public String getPubAnnotations() {
        return pubAnnotations;
    }

    public void setPubAnnotations(String pubAnnotations) {
        this.pubAnnotations = pubAnnotations;
    }

    public String getPubISBN() {
        return pubISBN;
    }

    public void setPubISBN(String pubISBN) {
        this.pubISBN = pubISBN;
    }

    public String getPubISSN() {
        return pubISSN;
    }

    public void setPubISSN(String pubISSN) {
        this.pubISSN = pubISSN;
    }

    public String getPubDOIRef() {
        return pubDOIRef;
    }

    public void setPubDOIRef(String pubDOIRef) {
        this.pubDOIRef = pubDOIRef;
    }

    public String getPubURL() {
        return pubURL;
    }

    public String getPubVideoURL() {
        return pubVideoURL;
    }

    public void setPubVideoURL(String pubVideoURL) {
        this.pubVideoURL = pubVideoURL;
    }

    public void setPubURL(String pubURL) {
        this.pubURL = pubURL;
    }

    public String getPubDBLP() {
        return pubDBLP;
    }

    public void setPubDBLP(String pubDBLP) {
        this.pubDBLP = pubDBLP;
    }

    public String getPubPDFPath() {
        return pubPDFPath;
    }

    public void setPubPDFPath(String pubPDFPath) {
        this.pubPDFPath = pubPDFPath;
    }

    public String getPubLanguage() {
        return pubLanguage;
    }

    public void setPubLanguage(String pubLanguage) {
        this.pubLanguage = pubLanguage;
    }

    public String getPubPaperAwardPath() {
        return pubPaperAwardPath;
    }

    public void setPubPaperAwardPath(String pubPaperAwardPath) {
        this.pubPaperAwardPath = pubPaperAwardPath;
    }

    public PublicationType getPubType() {
        return pubType;
    }

    /*
     * The only purpose of these methods is to avoid having to check if a
     * Publication is a
     * ReadingCommitteeJournalPopularizationPaper
     * and cast it in order to get its journal's quality indicators in
     * MainController.getPublicationsList()
     * (wich would be an heavy operation to do and would alter the application's
     * performance)
     */
    public Quartile getPubJournalScimagoQuartile() {
        return null;
    }

    public Quartile getPubJournalWosQuartile() {
        return null;
    }

    public CoreRanking getPubJournalCoreRanking() {
        return null;
    }

    public int getPubJournalImpactFactor() {
        return 0;
    }

    @Transactional
    public void deleteAuthorship(Authorship authorship) {
        pubAuts.remove(authorship);
    }

    // FIXME: Remove authors does not work on edit mode
    /*
     * @Transactional
     * public void removeAuthorshipFromAutId(int autId) {
     * this.pubAuts.removeIf(authorship -> authorship.getAutAutId() == autId);
     * }
     */

    public Class getPublicationClass() {
        switch (this.getPubType().getPublicationTypeGroupFromPublicationType()) {
            case Typeless:
                return Publication.class;
            case ReadingCommitteeJournalPopularizationPaper:
                return ReadingCommitteeJournalPopularizationPaper.class;
            case ProceedingsConference:
                return ProceedingsConference.class;
            case Book:
                return Book.class;
            case BookChapter:
                return BookChapter.class;
            case SeminarPatentInvitedConference:
                return SeminarPatentInvitedConference.class;
            case UniversityDocument:
                return UniversityDocument.class;
            case EngineeringActivity:
                return EngineeringActivity.class;
            case UserDocumentation:
                return UserDocumentation.class;
        }
        return Publication.class;
    }

    public void setPubType(PublicationType pubType) {
        this.pubType = pubType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pubAbstract == null) ? 0 : pubAbstract.hashCode());
        result = prime * result + ((pubAnnotations == null) ? 0 : pubAnnotations.hashCode());
        // result = prime * result + ((pubAuts == null) ? 0 : pubAuts.hashCode());
        result = prime * result + ((pubDBLP == null) ? 0 : pubDBLP.hashCode());
        result = prime * result + ((pubDOIRef == null) ? 0 : pubDOIRef.hashCode());
        result = prime * result + ((pubDate == null) ? 0 : pubDate.hashCode());
        result = prime * result + ((pubISBN == null) ? 0 : pubISBN.hashCode());
        result = prime * result + ((pubISSN == null) ? 0 : pubISSN.hashCode());
        result = prime * result + pubId;
        result = prime * result + ((pubKeywords == null) ? 0 : pubKeywords.hashCode());
        result = prime * result + ((pubLanguage == null) ? 0 : pubLanguage.hashCode());
        result = prime * result + ((pubNote == null) ? 0 : pubNote.hashCode());
        result = prime * result + ((pubPDFPath == null) ? 0 : pubPDFPath.hashCode());
        result = prime * result + ((pubPaperAwardPath == null) ? 0 : pubPaperAwardPath.hashCode());
        result = prime * result + ((pubTitle == null) ? 0 : pubTitle.hashCode());
        result = prime * result + ((pubType == null) ? 0 : pubType.hashCode());
        result = prime * result + ((pubURL == null) ? 0 : pubURL.hashCode());
        result = prime * result + pubYear;
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
        Publication other = (Publication) obj;
        if (pubAbstract == null) {
            if (other.pubAbstract != null)
                return false;
        } else if (!pubAbstract.equals(other.pubAbstract))
            return false;
        if (pubAnnotations == null) {
            if (other.pubAnnotations != null)
                return false;
        } else if (!pubAnnotations.equals(other.pubAnnotations))
            return false;
        if (pubAuts == null) {
            if (other.pubAuts != null)
                return false;
        } else if (!pubAuts.equals(other.pubAuts))
            return false;
        if (pubDBLP == null) {
            if (other.pubDBLP != null)
                return false;
        } else if (!pubDBLP.equals(other.pubDBLP))
            return false;
        if (pubDOIRef == null) {
            if (other.pubDOIRef != null)
                return false;
        } else if (!pubDOIRef.equals(other.pubDOIRef))
            return false;
        if (pubDate == null) {
            if (other.pubDate != null)
                return false;
        } else if (!pubDate.equals(other.pubDate))
            return false;
        if (pubISBN == null) {
            if (other.pubISBN != null)
                return false;
        } else if (!pubISBN.equals(other.pubISBN))
            return false;
        if (pubISSN == null) {
            if (other.pubISSN != null)
                return false;
        } else if (!pubISSN.equals(other.pubISSN))
            return false;
        if (pubId != other.pubId)
            return false;
        if (pubKeywords == null) {
            if (other.pubKeywords != null)
                return false;
        } else if (!pubKeywords.equals(other.pubKeywords))
            return false;
        if (pubLanguage == null) {
            if (other.pubLanguage != null)
                return false;
        } else if (!pubLanguage.equals(other.pubLanguage))
            return false;
        if (pubNote == null) {
            if (other.pubNote != null)
                return false;
        } else if (!pubNote.equals(other.pubNote))
            return false;
        if (pubPDFPath == null) {
            if (other.pubPDFPath != null)
                return false;
        } else if (!pubPDFPath.equals(other.pubPDFPath))
            return false;
        if (pubPaperAwardPath == null) {
            if (other.pubPaperAwardPath != null)
                return false;
        } else if (!pubPaperAwardPath.equals(other.pubPaperAwardPath))
            return false;
        if (pubTitle == null) {
            if (other.pubTitle != null)
                return false;
        } else if (!pubTitle.equals(other.pubTitle))
            return false;
        if (pubType != other.pubType)
            return false;
        if (pubURL == null) {
            if (other.pubURL != null)
                return false;
        } else if (!pubURL.equals(other.pubURL))
            return false;
        if (pubYear != other.pubYear)
            return false;
        return true;
    }

}
