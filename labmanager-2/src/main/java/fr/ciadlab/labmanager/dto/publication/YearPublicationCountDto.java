/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the CIAD laboratory and the Université de Technologie
 * de Belfort-Montbéliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD-UTBM.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.dto.publication;

/**
 * Data Transfer Object (DTO) for representing an author and its publication count.
 */
public class YearPublicationCountDto {

    private int year;
    private Long publicationCount;

    public YearPublicationCountDto(int year, Long publicationCount) {
        this.year = year;
        this.publicationCount = publicationCount;
    }

    public int getYear() {
        return year;
    }

    public Long getPublicationCount() {
        return publicationCount;
    }
}
