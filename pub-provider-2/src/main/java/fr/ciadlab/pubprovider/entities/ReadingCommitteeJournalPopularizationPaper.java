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

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

@Entity
@Table(name = "ReadingCommitteeJournalsPopularizationPapers")
@PrimaryKeyJoinColumn(name = "pubId")
// I realised way too late that this was a journal type and not a conference
// type
// As a result all of the attributes are still named reaComConf instead of
// reaComJour
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

    public ReadingCommitteeJournalPopularizationPaper(Publication p, String reaComConfPopPapVolume,
            String reaComConfPopPapNumber, String reaComConfPopPapPages) {
        super(p);
        this.reaComConfPopPapVolume = reaComConfPopPapVolume;
        this.reaComConfPopPapNumber = reaComConfPopPapNumber;
        this.reaComConfPopPapPages = reaComConfPopPapPages;
    }

    public ReadingCommitteeJournalPopularizationPaper() {
        super();
    }

    //
    // public ReadingCommitteeJournalPopularizationPaper(int pubId, List<Authorship>
    // pubAuts, String pubTitle,
    // String pubAbstract, String pubKeywords, Date pubDate, String pubNote, String
    // pubAnnotations, String pubISBN,
    // String pubISSN, String pubDOIRef, String pubURL, String pubDBLP, String
    // pubPDFPath, String pubLanguage,
    // String pubPaperAwardPath, PublicationType pubType, String
    // reaComConfPopPapVolume,
    // String reaComConfPopPapNumber, String reaComConfPopPapPages, Journal
    // reaComConfPopPapJournal) {
    // super(pubId, pubAuts, pubTitle, pubAbstract, pubKeywords, pubDate, pubNote,
    // pubAnnotations, pubISBN, pubISSN,
    // pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage, pubPaperAwardPath,
    // pubType);
    // this.reaComConfPopPapVolume = reaComConfPopPapVolume;
    // this.reaComConfPopPapNumber = reaComConfPopPapNumber;
    // this.reaComConfPopPapPages = reaComConfPopPapPages;
    // this.reaComConfPopPapJournal = reaComConfPopPapJournal;
    // }

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
        // result = prime * result + ((reaComConfPopPapJournal == null) ? 0 :
        // reaComConfPopPapJournal.hashCode());
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

    public Quartile getPubJournalScimagoQuartile() {
        if (this.getReaComConfPopPapJournal() != null)
            return this.getReaComConfPopPapJournal().getScimagoQuartileByYear(this.getPubYear());
        return null;
    }

    public Quartile getPubJournalWosQuartile() {
        if (this.getReaComConfPopPapJournal() != null)
            return this.getReaComConfPopPapJournal().getWosQuartileByYear(this.getPubYear());
        return null;
    }

    public CoreRanking getPubJournalCoreRanking() {
        if (this.getReaComConfPopPapJournal() != null)
            return this.getReaComConfPopPapJournal().getCoreRankingByYear(this.getPubYear());
        return null;
    }

    public int getPubJournalImpactFactor() {
        if (this.getReaComConfPopPapJournal() != null)
            return this.getReaComConfPopPapJournal().getImpactFactorByYear(this.getPubYear());
        return 0;
    }

}
