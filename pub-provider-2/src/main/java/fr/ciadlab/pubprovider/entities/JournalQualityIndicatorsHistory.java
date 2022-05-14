package fr.ciadlab.pubprovider.entities;

public class JournalQualityIndicatorsHistory {
    private int journalHistoryYear;
    private PublicationQuartile journalHistoryScimagoQuartile;
    private PublicationQuartile journalHistoryWosQuartile;
    private CoreRanking journalHistoryCoreRanking;
    private int journalHistoryImpactFactor;

    public JournalQualityIndicatorsHistory() {
        this.journalHistoryYear = 0;
        this.journalHistoryScimagoQuartile = null;
        this.journalHistoryWosQuartile = null;
        this.journalHistoryCoreRanking = null;
        this.journalHistoryImpactFactor = 0;
    }

    public JournalQualityIndicatorsHistory(int year, PublicationQuartile scimagoQuartile,
            PublicationQuartile wosQuartile, CoreRanking coreRanking, int journalHistoryImpactFactor) {
        this.journalHistoryYear = 0;
        this.journalHistoryScimagoQuartile = null;
        this.journalHistoryWosQuartile = null;
        this.journalHistoryCoreRanking = null;
        this.journalHistoryImpactFactor = 0;
    }

    public int getJournalHistoryYear() {
        return this.journalHistoryYear;
    }

    public PublicationQuartile getJournalHistoryScimagoQuartile() {
        return this.journalHistoryScimagoQuartile;
    }

    public PublicationQuartile getJournalHistoryWosQuartile() {
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

    public void setJournalHistoryScimagoQuartile(PublicationQuartile quartile) {
        this.journalHistoryScimagoQuartile = quartile;
    }

    public void setJournalHistoryWosQuartile(PublicationQuartile quartile) {
        this.journalHistoryWosQuartile = quartile;
    }

    public void setJournalHistoryCoreRanking(CoreRanking coreRanking) {
        this.journalHistoryCoreRanking = coreRanking;
    }

    public void setJournalHistoryImpactFactor(int impactFactor) {
        this.journalHistoryImpactFactor = impactFactor;
    }

}
