package fr.ciadlab.pubprovider.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Entity
@Table(name = "journals")
public class Journal implements Serializable { // Supposed to be abstract but lets not do that to avoid unnecessary bugs

    private static final long serialVersionUID = 1567501055115722405L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // Using this instead of IDENTITY allows for JOINED or TABLE_PER_CLASS
    // inheritance types to work
    @Column(nullable = false)
    private int jourId;

    @OneToMany(mappedBy = "reaComConfPopPapJournal")
    @JsonIgnore
    private Set<ReadingCommitteeJournalPopularizationPaper> jourPubs = new HashSet<>();

    @Column
    private String jourName;

    @Column
    private String jourPublisher;

    @Column
    private String jourElsevier;

    @Column
    private String jourScimago;

    @Column
    private String jourWos;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "journal_id")
    private Set<JournalQualityIndicatorsHistory> jourQualityIndicatorsHistory;

    public Journal(int jourId, Set<ReadingCommitteeJournalPopularizationPaper> jourPubs, String jourName,
            String jourPublisher, String jourElsevier, String jourScimago, String jourWos,
            Set<JournalQualityIndicatorsHistory> jourQualityIndicatorsHistory) {
        super();
        this.jourId = jourId;
        this.jourPubs = jourPubs;
        this.jourName = jourName;
        this.jourPublisher = jourPublisher;
        this.jourElsevier = jourElsevier;
        this.jourScimago = jourScimago;
        this.jourWos = jourWos;
        this.jourQualityIndicatorsHistory = jourQualityIndicatorsHistory;
    }

    public Journal() {
        super();
        // creation of an empty set to avoid any null pointer exception while using the
        // history
        this.jourQualityIndicatorsHistory = Collections.emptySet();
        // TODO Auto-generated constructor stub
    }

    public int getJourId() {
        return jourId;
    }

    public void setJourId(int jourId) {
        this.jourId = jourId;
    }

    public Set<ReadingCommitteeJournalPopularizationPaper> getJourPubs() {
        return jourPubs;
    }

    public void setJourPubs(Set<ReadingCommitteeJournalPopularizationPaper> jourPubs) {
        this.jourPubs = jourPubs;
    }

    public String getJourName() {
        return jourName;
    }

    public void setJourName(String jourName) {
        this.jourName = jourName;
    }

    public String getJourPublisher() {
        return jourPublisher;
    }

    public void setJourPublisher(String jourPublisher) {
        this.jourPublisher = jourPublisher;
    }

    public String getJourElsevier() {
        return jourElsevier;
    }

    public void setJourElsevier(String jourElsevier) {
        this.jourElsevier = jourElsevier;
    }

    public String getJourScimago() {
        return jourScimago;
    }

    public void setJourScimago(String jourScimago) {
        this.jourScimago = jourScimago;
    }

    public String getJourWos() {
        return jourWos;
    }

    public void setJourWos(String jourWos) {
        this.jourWos = jourWos;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((jourElsevier == null) ? 0 : jourElsevier.hashCode());
        result = prime * result + jourId;
        result = prime * result + ((jourName == null) ? 0 : jourName.hashCode());
        result = prime * result + ((jourPublisher == null) ? 0 : jourPublisher.hashCode());
        // result = prime * result + ((jourPubs == null) ? 0 : jourPubs.hashCode());
        result = prime * result + ((jourScimago == null) ? 0 : jourScimago.hashCode());
        result = prime * result + ((jourWos == null) ? 0 : jourWos.hashCode());
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
        Journal other = (Journal) obj;
        if (jourElsevier == null) {
            if (other.jourElsevier != null)
                return false;
        } else if (!jourElsevier.equals(other.jourElsevier))
            return false;
        if (jourId != other.jourId)
            return false;
        if (jourName == null) {
            if (other.jourName != null)
                return false;
        } else if (!jourName.equals(other.jourName))
            return false;
        if (jourPublisher == null) {
            if (other.jourPublisher != null)
                return false;
        } else if (!jourPublisher.equals(other.jourPublisher))
            return false;
        if (jourPubs == null) {
            if (other.jourPubs != null)
                return false;
        } else if (!jourPubs.equals(other.jourPubs))
            return false;
        if (jourScimago == null) {
            if (other.jourScimago != null)
                return false;
        } else if (!jourScimago.equals(other.jourScimago))
            return false;
        if (jourWos == null) {
            if (other.jourWos != null)
                return false;
        } else if (!jourWos.equals(other.jourWos))
            return false;
        return true;
    }

    // History management methods
    public boolean hasJounralQualityIndicatorsHistoryForYear(int year) {
        return this.jourQualityIndicatorsHistory.stream().filter(history -> (history.getJournalHistoryYear() == year))
                .collect(Collectors.toList()).size() > 0;
    }

    public Quartile getScimagoQuartileByYear(int year) {
        return this.jourQualityIndicatorsHistory.stream().filter(history -> (history.getJournalHistoryYear() == year))
                .collect(Collectors.toList()).get(0).getJournalHistoryScimagoQuartile();
    }

    public Quartile getWosQuartileByYear(int year) {
        return this.jourQualityIndicatorsHistory.stream().filter(history -> (history.getJournalHistoryYear() == year))
                .collect(Collectors.toList()).get(0).getJournalHistoryWosQuartile();
    }

    public CoreRanking getCoreRankingByYear(int year) {
        return this.jourQualityIndicatorsHistory.stream().filter(history -> (history.getJournalHistoryYear() == year))
                .collect(Collectors.toList()).get(0).getJournalHistoryCoreRanking();
    }

    public int getImpactFactorByYear(int year) {
        return this.jourQualityIndicatorsHistory.stream().filter(history -> (history.getJournalHistoryYear() == year))
                .collect(Collectors.toList()).get(0).getJournalHistoryImpactFactor();
    }

    public void setScimagoQuartileByYear(int year, Quartile newQuartile) {
        this.jourQualityIndicatorsHistory.stream().filter(history -> (history.getJournalHistoryYear() == year))
                .collect(Collectors.toList()).get(0).setJournalHistoryScimagoQuartile(newQuartile);
    }

    public void setWosQuartileByYear(int year, Quartile newQuartile) {
        this.jourQualityIndicatorsHistory.stream().filter(history -> (history.getJournalHistoryYear() == year))
                .collect(Collectors.toList()).get(0).setJournalHistoryWosQuartile(newQuartile);
    }

    public void setCoreRankingByYear(int year, CoreRanking newCoreRanking) {
        this.jourQualityIndicatorsHistory.stream().filter(history -> (history.getJournalHistoryYear() == year))
                .collect(Collectors.toList()).get(0).setJournalHistoryCoreRanking(newCoreRanking);
    }

    public void setImpactFactorByYear(int year, int newImpactFactor) {
        this.jourQualityIndicatorsHistory.stream().filter(history -> (history.getJournalHistoryYear() == year))
                .collect(Collectors.toList()).get(0).setJournalHistoryImpactFactor(newImpactFactor);
    }

}
