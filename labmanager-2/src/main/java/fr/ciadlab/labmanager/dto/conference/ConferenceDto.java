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

package fr.ciadlab.labmanager.dto.conference;

import fr.ciadlab.labmanager.entities.conference.Conference;

/**
 * Data Transfer Object (DTO) for representing a Conference.
 */
public class ConferenceDto {

    /** The unique identifier of the conference. */
    private int id;

    /** The name of the conference. */
    private String conferenceName;
    
    /**
     * Parameterized constructor to initialize the ConferenceDto from a Conference.
     *
     * @param conference the conference
     */
    public ConferenceDto(Conference conference) {
        this.id = conference.getId();
        this.conferenceName = conference.getName();
    }

    /**
     * Get the unique identifier of the conference.
     *
     * @return The unique identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the name of the conference.
     *
     * @return The name of the conference.
     */
    public String getconferenceName() {
        return conferenceName;
    }
}
