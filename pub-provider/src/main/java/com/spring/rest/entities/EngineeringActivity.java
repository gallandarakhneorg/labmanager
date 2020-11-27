package com.spring.rest.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "engineeringActivities")
@PrimaryKeyJoinColumn(name = "pubId")
public class EngineeringActivity extends Publication {


    /**
     *
     */
    private static final long serialVersionUID = -5337308013162850274L;

    @Column
    private String engActInstitName;

    @Column
    private String engActReportType;

    @Column
    private String engActNumber;

    public EngineeringActivity(String engActInstitName, String engActReportType, String engActNumber) {
        super();
        this.engActInstitName = engActInstitName;
        this.engActReportType = engActReportType;
        this.engActNumber = engActNumber;
    }

    public EngineeringActivity() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getEngActInstitName() {
        return engActInstitName;
    }

    public void setEngActInstitName(String engActInstitName) {
        this.engActInstitName = engActInstitName;
    }

    public String getEngActReportType() {
        return engActReportType;
    }

    public void setEngActReportType(String engActReportType) {
        this.engActReportType = engActReportType;
    }

    public String getEngActNumber() {
        return engActNumber;
    }

    public void setEngActNumber(String engActNumber) {
        this.engActNumber = engActNumber;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((engActInstitName == null) ? 0 : engActInstitName.hashCode());
        result = prime * result + ((engActNumber == null) ? 0 : engActNumber.hashCode());
        result = prime * result + ((engActReportType == null) ? 0 : engActReportType.hashCode());
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
        EngineeringActivity other = (EngineeringActivity) obj;
        if (engActInstitName == null) {
            if (other.engActInstitName != null)
                return false;
        } else if (!engActInstitName.equals(other.engActInstitName))
            return false;
        if (engActNumber == null) {
            if (other.engActNumber != null)
                return false;
        } else if (!engActNumber.equals(other.engActNumber))
            return false;
        if (engActReportType == null) {
            if (other.engActReportType != null)
                return false;
        } else if (!engActReportType.equals(other.engActReportType))
            return false;
        return true;
    }


}


