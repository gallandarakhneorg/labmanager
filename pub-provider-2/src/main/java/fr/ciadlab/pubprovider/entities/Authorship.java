package fr.ciadlab.pubprovider.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "authorship")
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class Authorship implements Serializable {

    private static final long serialVersionUID = -1083342117826188053L;

    @Id
    private int autShipId;

    @Column(nullable = false)
    private int pubPubId;

    @Column(nullable = false)
    private int autAutId;

    //Rank determining the order of the author in the concerned publication
    @Column(nullable = false)
    private int autShipRank;

    public Authorship() {
    }

    public Authorship(int pubPubId, int autAutId, int autShipRank) {
        this.pubPubId = pubPubId;
        this.autAutId = autAutId;
        this.autShipRank = autShipRank;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getAutShipId() {
        return autShipId;
    }

    public void setAutShipId(int autShipId) {
        this.autShipId = autShipId;
    }

    public int getPubPubId() {
        return pubPubId;
    }

    public void setPubPubId(int pubPubId) {
        this.pubPubId = pubPubId;
    }

    public int getAutAutId() {
        return autAutId;
    }

    public void setAutAutId(int autAutId) {
        this.autAutId = autAutId;
    }

    public int getAutShipRank() {
        return autShipRank;
    }

    public void setAutShipRank(int autShipRank) {
        this.autShipRank = autShipRank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authorship that = (Authorship) o;
        return autShipId == that.autShipId &&
                pubPubId == that.pubPubId &&
                autAutId == that.autAutId &&
                autShipRank == that.autShipRank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(autShipId, pubPubId, autAutId, autShipRank);
    }
}


