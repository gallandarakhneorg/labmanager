package com.spring.rest.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "authorship")
@AssociationOverrides({
        @AssociationOverride(name = "pk.publication",
                joinColumns = @JoinColumn(name = "pub_id")),
        @AssociationOverride(name = "pk.author",
                joinColumns = @JoinColumn(name = "aut_id")) })
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class Authorship implements Serializable {

    private static final long serialVersionUID = -1083342117826188053L;

    @EmbeddedId
    private AuthorshipPK pk = new AuthorshipPK();

    //Rank determining the order of the author in the concerned publication
    @Column(nullable = false)
    private int autShipRank;

    public Authorship(AuthorshipPK pk, int autShipRank) {
        this.pk = pk;
        this.autShipRank = autShipRank;
    }

    public Authorship() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public AuthorshipPK getPk() {
        return pk;
    }

    public void setPk(AuthorshipPK pk) {
        this.pk = pk;
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
        return autShipRank == that.autShipRank &&
                Objects.equals(pk, that.pk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pk, autShipRank);
    }
}


