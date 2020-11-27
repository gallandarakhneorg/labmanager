package com.spring.rest.entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "ReadingCommitteeJournalsPopularizationPapers")
@PrimaryKeyJoinColumn(name = "pubId")
//I realised way too late that this was a journal type and not a conference type
//As a result all of the attributes are still named reaComConf instead of reaComJour
public class ReadingCommitteeJournalPopularizationPaper extends Publication {


    /**
     *
     */
    private static final long serialVersionUID = 3154620399419184751L;

    @Column
    private String reaComConfPopPapVolume;

    @Column
    private String reaComConfPopPapNumber;

    @Column
    private String reaComConfPopPapPages;

    @ManyToOne
    private Journal reaComConfPopPapJournal;

    public ReadingCommitteeJournalPopularizationPaper(int pubId, List<Authorship> pubAuts, String pubTitle,
                                                      String pubAbstract, String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN,
                                                      String pubISSN, String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
                                                      String pubPaperAwardPath, PublicationType pubType, String reaComConfPopPapVolume,
                                                      String reaComConfPopPapNumber, String reaComConfPopPapPages, Journal reaComConfPopPapJournal) {
        super(pubId, pubAuts, pubTitle, pubAbstract, pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN,
                pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage, pubPaperAwardPath, pubType);
        this.reaComConfPopPapVolume = reaComConfPopPapVolume;
        this.reaComConfPopPapNumber = reaComConfPopPapNumber;
        this.reaComConfPopPapPages = reaComConfPopPapPages;
        this.reaComConfPopPapJournal = reaComConfPopPapJournal;
    }

    public ReadingCommitteeJournalPopularizationPaper() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ReadingCommitteeJournalPopularizationPaper(int pubId, List<Authorship> pubAuts, String pubTitle,
                                                      String pubAbstract, String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN,
                                                      String pubISSN, String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
                                                      String pubPaperAwardPath, PublicationType pubType) {
        super(pubId, pubAuts, pubTitle, pubAbstract, pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN, pubDOIRef,
                pubURL, pubDBLP, pubPDFPath, pubLanguage, pubPaperAwardPath, pubType);
        // TODO Auto-generated constructor stub
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

    public Journal getReaComConfPopPapJournal() {
        return reaComConfPopPapJournal;
    }

    public void setReaComConfPopPapJournal(Journal reaComConfPopPapJournal) {
        this.reaComConfPopPapJournal = reaComConfPopPapJournal;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        //result = prime * result + ((reaComConfPopPapJournal == null) ? 0 : reaComConfPopPapJournal.hashCode());
        result = prime * result + ((reaComConfPopPapNumber == null) ? 0 : reaComConfPopPapNumber.hashCode());
        result = prime * result + ((reaComConfPopPapPages == null) ? 0 : reaComConfPopPapPages.hashCode());
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
        if (reaComConfPopPapJournal == null) {
            if (other.reaComConfPopPapJournal != null)
                return false;
        } else if (!reaComConfPopPapJournal.equals(other.reaComConfPopPapJournal))
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
        if (reaComConfPopPapVolume == null) {
            if (other.reaComConfPopPapVolume != null)
                return false;
        } else if (!reaComConfPopPapVolume.equals(other.reaComConfPopPapVolume))
            return false;
        return true;
    }


}


