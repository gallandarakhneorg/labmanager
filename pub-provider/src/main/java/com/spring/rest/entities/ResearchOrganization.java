package com.spring.rest.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "researchorganizations")
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class ResearchOrganization implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8860550934888735536L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private int resOrgId;

    @OneToMany(mappedBy = "resOrg", cascade = CascadeType.ALL)
    private Set<Membership> orgAuts = new HashSet<>();

    @OneToMany(mappedBy = "orgSup")
    private Set<ResearchOrganization> orgSubs = new HashSet<>();

    @ManyToOne
    private ResearchOrganization orgSup;

    /* For list accessors, see Author*/

    @Column
    private String resOrgName;

    @Column
    private String resOrgDesc;

    public ResearchOrganization(int resOrgId, Set<Membership> orgAuts, Set<ResearchOrganization> orgSubs,
                                ResearchOrganization orgSup, String resOrgName, String resOrgDesc) {
        super();
        this.resOrgId = resOrgId;
        this.orgAuts = orgAuts;
        this.orgSubs = orgSubs;
        this.orgSup = orgSup;
        this.resOrgName = resOrgName;
        this.resOrgDesc = resOrgDesc;
    }

    public ResearchOrganization() {
        super();
        // TODO Auto-generated constructor stub
    }

    public int getResOrgId() {
        return resOrgId;
    }

    public void setResOrgId(int resOrgId) {
        this.resOrgId = resOrgId;
    }

    public Set<Membership> getOrgAuts() {
        return orgAuts;
    }

    public void setOrgAuts(Set<Membership> orgAuts) {
        this.orgAuts = orgAuts;
    }

    public Set<ResearchOrganization> getOrgSubs() {
        return orgSubs;
    }

    public void setOrgSubs(Set<ResearchOrganization> orgSubs) {
        this.orgSubs = orgSubs;
    }

    public ResearchOrganization getOrgSup() {
        return orgSup;
    }

    public void setOrgSup(ResearchOrganization orgSup) {
        this.orgSup = orgSup;
    }

    public String getResOrgName() {
        return resOrgName;
    }

    public void setResOrgName(String resOrgName) {
        this.resOrgName = resOrgName;
    }

    public String getResOrgDesc() {
        return resOrgDesc;
    }

    public void setResOrgDesc(String resOrgDesc) {
        this.resOrgDesc = resOrgDesc;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        //result = prime * result + ((orgAuts == null) ? 0 : orgAuts.hashCode());
        //result = prime * result + ((orgSubs == null) ? 0 : orgSubs.hashCode());
        //result = prime * result + ((orgSup == null) ? 0 : orgSup.hashCode());
        result = prime * result + ((resOrgDesc == null) ? 0 : resOrgDesc.hashCode());
        result = prime * result + resOrgId;
        result = prime * result + ((resOrgName == null) ? 0 : resOrgName.hashCode());
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
        ResearchOrganization other = (ResearchOrganization) obj;
        if (orgAuts == null) {
            if (other.orgAuts != null)
                return false;
        } else if (!orgAuts.equals(other.orgAuts))
            return false;
        if (orgSubs == null) {
            if (other.orgSubs != null)
                return false;
        } else if (!orgSubs.equals(other.orgSubs))
            return false;
        if (orgSup == null) {
            if (other.orgSup != null)
                return false;
        } else if (!orgSup.equals(other.orgSup))
            return false;
        if (resOrgDesc == null) {
            if (other.resOrgDesc != null)
                return false;
        } else if (!resOrgDesc.equals(other.resOrgDesc))
            return false;
        if (resOrgId != other.resOrgId)
            return false;
        if (resOrgName == null) {
            if (other.resOrgName != null)
                return false;
        } else if (!resOrgName.equals(other.resOrgName))
            return false;
        return true;
    }


}
	
	
	