package com.spring.rest.entities;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "authorship")
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class Authorship implements Serializable {

    private static final long serialVersionUID = -1083342117826188053L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int autShipId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Publication pub;

    @ManyToOne(fetch = FetchType.EAGER)
    private Author aut;

    //Rank determining the order of the author in the concerned publication
    @Column
    private int autShipRank;

    public Authorship(int autShipId, Publication pub, Author aut, int autShipRank) {
        super();
        this.autShipId = autShipId;
        this.pub = pub;
        this.aut = aut;
        this.autShipRank = autShipRank;
    }

    public Authorship() {
        super();
        // TODO Auto-generated constructor stub
    }

    public int getAutShipId() {
        return autShipId;
    }

    public void setAutShipId(int autShipId) {
        this.autShipId = autShipId;
    }

    public Publication getPub() {
        return pub;
    }

    public void setPub(Publication pub) {
        this.pub = pub;
    }

    public Author getAut() {
        return aut;
    }

    public void setAut(Author aut) {
        this.aut = aut;
    }

    public int getAutShipRank() {
        return autShipRank;
    }

    public void setAutShipRank(int rank) {
        this.autShipRank = rank;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        //result = prime * result + ((aut == null) ? 0 : aut.hashCode());
        result = prime * result + autShipId;
        //result = prime * result + ((pub == null) ? 0 : pub.hashCode());
        result = prime * result + autShipRank;
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
        Authorship other = (Authorship) obj;
        if (aut == null) {
            if (other.aut != null)
                return false;
        } else if (!aut.equals(other.aut))
            return false;
        if (autShipId != other.autShipId)
            return false;
        if (pub == null) {
            if (other.pub != null)
                return false;
        } else if (!pub.equals(other.pub))
            return false;
        if (autShipRank != other.autShipRank)
            return false;
        return true;
    }


}


