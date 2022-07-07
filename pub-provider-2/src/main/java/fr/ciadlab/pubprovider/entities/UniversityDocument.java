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
@Table(name = "universityDocuments")
@PrimaryKeyJoinColumn(name = "pubId")
public class UniversityDocument extends Publication {


    /**
     *
     */
    private static final long serialVersionUID = -8339849399791468729L;

    @Column
    private String uniDocSchoolName;

    @Column
    private String uniDocAddress;

    public UniversityDocument(Publication p, String uniDocSchoolName, String uniDocAddress) {
        super(p);
        this.uniDocSchoolName = uniDocSchoolName;
        this.uniDocAddress = uniDocAddress;
    }

    public UniversityDocument() {
        super();
        // TODO Auto-generated constructor stub
    }


    public String getUniDocSchoolName() {
        return uniDocSchoolName;
    }

    public void setUniDocSchoolName(String uniDocSchoolName) {
        this.uniDocSchoolName = uniDocSchoolName;
    }

    public String getUniDocAddress() {
        return uniDocAddress;
    }

    public void setUniDocAddress(String uniDocAddress) {
        this.uniDocAddress = uniDocAddress;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((uniDocAddress == null) ? 0 : uniDocAddress.hashCode());
        result = prime * result + ((uniDocSchoolName == null) ? 0 : uniDocSchoolName.hashCode());
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
        UniversityDocument other = (UniversityDocument) obj;
        if (uniDocAddress == null) {
            if (other.uniDocAddress != null)
                return false;
        } else if (!uniDocAddress.equals(other.uniDocAddress))
            return false;
        if (uniDocSchoolName == null) {
            if (other.uniDocSchoolName != null)
                return false;
        } else if (!uniDocSchoolName.equals(other.uniDocSchoolName))
            return false;
        return true;
    }


}


