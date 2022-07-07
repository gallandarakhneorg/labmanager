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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "ProceedingsConferences")
@PrimaryKeyJoinColumn(name = "pubId")
public class ProceedingsConference extends Publication {


    /**
     *
     */
    private static final long serialVersionUID = -389839033139487479L;

    @Column
    private String proConfBookNameProceedings;

    @Column
    private String proConfEditor;

    @Column
    private String proConfPages;

    @Column
    private String proConfOrganization;

    @Column
    private String proConfPublisher;

    @Column
    private String proConfAddress;

    @Column
    private String proConfSeries;


    public ProceedingsConference(Publication p, String proConfBookNameProceedings, String proConfEditor, String proConfPages,
                                 String proConfOrganization, String proConfPublisher, String proConfAddress, String proConfSeries) {
        super(p);
        this.proConfBookNameProceedings = proConfBookNameProceedings;
        this.proConfEditor = proConfEditor;
        this.proConfPages = proConfPages;
        this.proConfOrganization = proConfOrganization;
        this.proConfPublisher = proConfPublisher;
        this.proConfAddress = proConfAddress;
        this.proConfSeries = proConfSeries;
    }

    public ProceedingsConference() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getProConfBookNameProceedings() {
        return proConfBookNameProceedings;
    }

    public void setProConfBookNameProceedings(String proConfBookNameProceedings) {
        this.proConfBookNameProceedings = proConfBookNameProceedings;
    }

    public String getProConfEditor() {
        return proConfEditor;
    }

    public void setProConfEditor(String proConfEditor) {
        this.proConfEditor = proConfEditor;
    }

    public String getProConfPages() {
        return proConfPages;
    }

    public void setProConfPages(String proConfPages) {
        this.proConfPages = proConfPages;
    }

    public String getProConfOrganization() {
        return proConfOrganization;
    }

    public void setProConfOrganization(String proConfOrganization) {
        this.proConfOrganization = proConfOrganization;
    }

    public String getProConfPublisher() {
        return proConfPublisher;
    }

    public void setProConfPublisher(String proConfPublisher) {
        this.proConfPublisher = proConfPublisher;
    }

    public String getProConfAddress() {
        return proConfAddress;
    }

    public void setProConfAddress(String proConfAddress) {
        this.proConfAddress = proConfAddress;
    }

    public String getProConfSeries() {
        return proConfSeries;
    }

    public void setProConfSeries(String proConfSeries) {
        this.proConfSeries = proConfSeries;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((proConfAddress == null) ? 0 : proConfAddress.hashCode());
        result = prime * result + ((proConfBookNameProceedings == null) ? 0 : proConfBookNameProceedings.hashCode());
        result = prime * result + ((proConfEditor == null) ? 0 : proConfEditor.hashCode());
        result = prime * result + ((proConfOrganization == null) ? 0 : proConfOrganization.hashCode());
        result = prime * result + ((proConfPages == null) ? 0 : proConfPages.hashCode());
        result = prime * result + ((proConfPublisher == null) ? 0 : proConfPublisher.hashCode());
        result = prime * result + ((proConfSeries == null) ? 0 : proConfSeries.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProceedingsConference other = (ProceedingsConference) obj;
        if (proConfAddress == null) {
            if (other.proConfAddress != null)
                return false;
        } else if (!proConfAddress.equals(other.proConfAddress))
            return false;
        if (proConfBookNameProceedings == null) {
            if (other.proConfBookNameProceedings != null)
                return false;
        } else if (!proConfBookNameProceedings.equals(other.proConfBookNameProceedings))
            return false;
        if (proConfEditor == null) {
            if (other.proConfEditor != null)
                return false;
        } else if (!proConfEditor.equals(other.proConfEditor))
            return false;
        if (proConfOrganization == null) {
            if (other.proConfOrganization != null)
                return false;
        } else if (!proConfOrganization.equals(other.proConfOrganization))
            return false;
        if (proConfPages == null) {
            if (other.proConfPages != null)
                return false;
        } else if (!proConfPages.equals(other.proConfPages))
            return false;
        if (proConfPublisher == null) {
            if (other.proConfPublisher != null)
                return false;
        } else if (!proConfPublisher.equals(other.proConfPublisher))
            return false;
        if (proConfSeries == null) {
            if (other.proConfSeries != null)
                return false;
        } else if (!proConfSeries.equals(other.proConfSeries))
            return false;
        return true;
    }


}


