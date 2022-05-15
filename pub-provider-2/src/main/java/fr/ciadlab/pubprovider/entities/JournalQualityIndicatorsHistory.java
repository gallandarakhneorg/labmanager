package fr.ciadlab.pubprovider.entities;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "journal_qality_indicators_histories")
public class JournalQualityIndicatorsHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int journalHistoryId;

    @Column
    private int journalHistoryYear;

    @Column
    private Quartile journalHistoryScimagoQuartile;

    @Column
    private Quartile journalHistoryWosQuartile;

    @Column
    private CoreRanking journalHistoryCoreRanking;

    @Column
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
        this.journalHistoryYear = 0;
        this.journalHistoryScimagoQuartile = null;
        this.journalHistoryWosQuartile = null;
        this.journalHistoryCoreRanking = null;
        this.journalHistoryImpactFactor = 0;
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
