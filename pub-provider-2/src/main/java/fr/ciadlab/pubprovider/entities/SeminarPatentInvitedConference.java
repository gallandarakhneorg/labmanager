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
@Table(name = "seminarsPatentsInvitedConferences")
@PrimaryKeyJoinColumn(name = "pubId")
//I forgot the type invited conference so this gets referenced as semPat instead of semPatInvConf
public class SeminarPatentInvitedConference extends Publication {


    /**
     *
     */
    private static final long serialVersionUID = -313315999200513804L;

    @Column
    private String semPatHowPub;

    public SeminarPatentInvitedConference(Publication p, String semPatHowPub) {
        super(p);
        this.semPatHowPub = semPatHowPub;
    }

    public SeminarPatentInvitedConference() {
        super();
        // TODO Auto-generated constructor stub
    }


    public String getSemPatHowPub() {
        return semPatHowPub;
    }

    public void setSemPatHowPub(String semPatHowPub) {
        this.semPatHowPub = semPatHowPub;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((semPatHowPub == null) ? 0 : semPatHowPub.hashCode());
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
        SeminarPatentInvitedConference other = (SeminarPatentInvitedConference) obj;
        if (semPatHowPub == null) {
            if (other.semPatHowPub != null)
                return false;
        } else if (!semPatHowPub.equals(other.semPatHowPub))
            return false;
        return true;
    }


}


