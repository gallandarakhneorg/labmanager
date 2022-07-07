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

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.ColumnDefault;

import net.bytebuddy.implementation.bind.annotation.Default;

@Entity
@Table(name = "journal_qality_indicators_histories")
public class JournalQualityIndicatorsHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int journalHistoryId;

    @Column
    // a null value for this column in the database would generate a crash when
    // fetching the histories (as ints cannot be null)
    @ColumnDefault("0")
    private int journalHistoryYear;

    @Column
    @Enumerated(EnumType.STRING)
    private Quartile journalHistoryScimagoQuartile;

    @Column
    @Enumerated(EnumType.STRING)
    private Quartile journalHistoryWosQuartile;

    @Column
    @Enumerated(EnumType.STRING)
    private CoreRanking journalHistoryCoreRanking;

    @Column
    // a null value for this column in the database would generate a crash when
    // fetching the histories (as ints cannot be null)
    @ColumnDefault("0")
    private int journalHistoryImpactFactor;

    public JournalQualityIndicatorsHistory() {
        this.journalHistoryYear = 0;
        this.journalHistoryScimagoQuartile = null;
        this.journalHistoryWosQuartile = null;
        this.journalHistoryCoreRanking = null;
        this.journalHistoryImpactFactor = 0;
    }

    public JournalQualityIndicatorsHistory(int year, Quartile scimagoQuartile,
            Quartile wosQuartile, CoreRanking coreRanking, int journalHistoryImpactFactor) {
        this.journalHistoryYear = year;
        this.journalHistoryScimagoQuartile = scimagoQuartile;
        this.journalHistoryWosQuartile = wosQuartile;
        this.journalHistoryCoreRanking = coreRanking;
        this.journalHistoryImpactFactor = journalHistoryImpactFactor;
    }

    public int getJournalHistoryYear() {
        return this.journalHistoryYear;
    }

    public Quartile getJournalHistoryScimagoQuartile() {
        return this.journalHistoryScimagoQuartile;
    }

    public Quartile getJournalHistoryWosQuartile() {
        return this.journalHistoryWosQuartile;
    }

    public CoreRanking getJournalHistoryCoreRanking() {
        return this.journalHistoryCoreRanking;
    }

    public int getJournalHistoryImpactFactor() {
        return this.journalHistoryImpactFactor;
    }

    public void setJournalHistoryYear(int year) {
        this.journalHistoryYear = year;
    }

    public void setJournalHistoryScimagoQuartile(Quartile quartile) {
        this.journalHistoryScimagoQuartile = quartile;
    }

    public void setJournalHistoryWosQuartile(Quartile quartile) {
        this.journalHistoryWosQuartile = quartile;
    }

    public void setJournalHistoryCoreRanking(CoreRanking coreRanking) {
        this.journalHistoryCoreRanking = coreRanking;
    }

    public void setJournalHistoryImpactFactor(int impactFactor) {
        this.journalHistoryImpactFactor = impactFactor;
    }

}
