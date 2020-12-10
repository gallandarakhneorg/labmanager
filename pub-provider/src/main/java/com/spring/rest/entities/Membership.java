package com.spring.rest.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;


@Entity
@Table(name = "membership")
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class Membership implements Serializable {

    private static final long serialVersionUID = 3709293483578802302L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int memId;

    @ManyToOne
    private Author aut;

    @ManyToOne
    private ResearchOrganization resOrg;

    @Column
    private Date memSinceWhen;

    @Column
    private Date memToWhen;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberStatus memStatus;


    public Membership(Author aut, ResearchOrganization resOrg, Date memSinceWhen, Date memToWhen,
                      MemberStatus memStatus) {
        super();
        this.aut = aut;
        this.resOrg = resOrg;
        this.memSinceWhen = memSinceWhen;
        this.memToWhen = memToWhen;
        this.memStatus = memStatus;
    }

    public Membership() {
        super();
        // TODO Auto-generated constructor stub
    }

    public int getMemId() {
        return memId;
    }

    public Author getAut() {
        return aut;
    }

    public void setAut(Author aut) {
        this.aut = aut;
    }

    public ResearchOrganization getResOrg() {
        return resOrg;
    }

    public void setResOrg(ResearchOrganization resOrg) {
        this.resOrg = resOrg;
    }

    public Date getMemSinceWhen() {
        return memSinceWhen;
    }

    public void setMemSinceWhen(Date memSinceWhen) {
        this.memSinceWhen = memSinceWhen;
    }

    public Date getMemToWhen() {
        return memToWhen;
    }

    public void setMemToWhen(Date memToWhen) {
        this.memToWhen = memToWhen;
    }

    public MemberStatus getMemStatus() {
        return memStatus;
    }

    public String getMemStatusLongString() {
        switch(getMemStatus()) {
            case PR:
                return "Full professor";
            case MCF:
                return "Associate professor";
            case MCF_HDR:
                return "Professor";
            case ECC:
                return "Lecturer";
            case LRU:
                return "Lecturer according to law LRU";
            case PHD_Student:
                return "PHD Student";
            case PostPHD:
                return "Post PHD";
            case ITRF:
                return "Engineer";
            case Prag:
                return "Prag";
            case Associate:
                return "Associate";
            case Intern:
                return "Intern";
            case Contractless:
                return "Contractless";
            default:
                return "";
        }
    }

    public void setMemStatus(MemberStatus memStatus) {
        this.memStatus = memStatus;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        //result = prime * result + ((aut == null) ? 0 : aut.hashCode());
        result = prime * result + ((memSinceWhen == null) ? 0 : memSinceWhen.hashCode());
        result = prime * result + ((memStatus == null) ? 0 : memStatus.hashCode());
        result = prime * result + ((memToWhen == null) ? 0 : memToWhen.hashCode());
        //result = prime * result + ((resOrg == null) ? 0 : resOrg.hashCode());
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
        Membership other = (Membership) obj;
        if (aut == null) {
            if (other.aut != null)
                return false;
        } else if (!aut.equals(other.aut))
            return false;
        if (memSinceWhen == null) {
            if (other.memSinceWhen != null)
                return false;
        } else if (!memSinceWhen.equals(other.memSinceWhen))
            return false;
        if (memStatus != other.memStatus)
            return false;
        if (memToWhen == null) {
            if (other.memToWhen != null)
                return false;
        } else if (!memToWhen.equals(other.memToWhen))
            return false;
        if (resOrg == null) {
            if (other.resOrg != null)
                return false;
        } else if (!resOrg.equals(other.resOrg))
            return false;
        return true;
    }


}


