package com.spring.rest.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "authors")
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class Author implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1623305979074421107L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int autId;

    @OneToMany(fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    private Set<Authorship> autPubs = new HashSet<>();

    @OneToMany(mappedBy = "aut", fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    private Set<Membership> autOrgs = new HashSet<>();


    @Column
    private String autFirstName;

    @Column
    private String autLastName;

    @Column
    //@Temporal(TemporalType.DATE)
    private Date autBirth;

    @Column
    private String autMail;

    @Column
    private boolean hasPage; //Wether or not the author has a page on the site.

    public Author(int autId, Set<Authorship> autPubs, Set<Membership> autOrgs, String autFirstName, String autLastName,
                  Date autBirth, String autMail, boolean hasPage) {
        super();
        this.autId = autId;
        this.autPubs = autPubs;
        this.autOrgs = autOrgs;
        this.autFirstName = autFirstName;
        this.autLastName = autLastName;
        this.autBirth = autBirth;
        this.autMail = autMail;
        this.hasPage = hasPage;
    }

    public Author() {
        super();
        // TODO Auto-generated constructor stub
    }

    public int getAutId() {
        return autId;
    }

    public void setAutId(int autId) {
        this.autId = autId;
    }

    public Set<Authorship> getAutPubs() {
        return autPubs;
    }

    public void setAutPubs(Set<Authorship> autPubs) {
        this.autPubs = autPubs;
    }

    public Set<Membership> getAutOrgs() {
        return autOrgs;
    }

    public void setAutOrgs(Set<Membership> autOrgs) {
        this.autOrgs = autOrgs;
    }

    public String getAutFirstName() {
        return autFirstName;
    }

    public void setAutFirstName(String autFirstName) {
        this.autFirstName = autFirstName;
    }

    public String getAutLastName() {
        return autLastName;
    }

    public void setAutLastName(String autLastName) {
        this.autLastName = autLastName;
    }

    public Date getAutBirth() {
        return autBirth;
    }

    public void setAutBirth(Date autBirth) {
        this.autBirth = autBirth;
    }

    public String getAutMail() {
        return autMail;
    }

    public void setAutMail(String autMail) {
        this.autMail = autMail;
    }

    public boolean isHasPage() {
        return hasPage;
    }

    public void setHasPage(boolean hasPage) {
        this.hasPage = hasPage;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((autBirth == null) ? 0 : autBirth.hashCode());
        result = prime * result + ((autFirstName == null) ? 0 : autFirstName.hashCode());
        result = prime * result + autId;
        result = prime * result + ((autLastName == null) ? 0 : autLastName.hashCode());
        result = prime * result + ((autMail == null) ? 0 : autMail.hashCode());
        //result = prime * result + ((autOrgs == null) ? 0 : autOrgs.hashCode());
        //result = prime * result + ((autPubs == null) ? 0 : autPubs.hashCode());
        result = prime * result + (hasPage ? 1231 : 1237);
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
        Author other = (Author) obj;
        if (autBirth == null) {
            if (other.autBirth != null)
                return false;
        } else if (!autBirth.equals(other.autBirth))
            return false;
        if (autFirstName == null) {
            if (other.autFirstName != null)
                return false;
        } else if (!autFirstName.equals(other.autFirstName))
            return false;
        if (autId != other.autId)
            return false;
        if (autLastName == null) {
            if (other.autLastName != null)
                return false;
        } else if (!autLastName.equals(other.autLastName))
            return false;
        if (autMail == null) {
            if (other.autMail != null)
                return false;
        } else if (!autMail.equals(other.autMail))
            return false;
        if (autOrgs == null) {
            if (other.autOrgs != null)
                return false;
        } else if (!autOrgs.equals(other.autOrgs))
            return false;
        if (autPubs == null) {
            if (other.autPubs != null)
                return false;
        } else if (!autPubs.equals(other.autPubs))
            return false;
        if (hasPage != other.hasPage)
            return false;
        return true;
    }


}






