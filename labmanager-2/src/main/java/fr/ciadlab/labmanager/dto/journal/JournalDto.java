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

package fr.ciadlab.labmanager.dto.journal;

/**
 * Data Transfer Object (DTO) for representing a Journal.
 */
public class JournalDto {

    /** The unique identifier of the journal. */
    private int id;

    /** The name of the journal. */
    private String journalName;

    public JournalDto() {
        // 
    }

    /**
     * Parameterized constructor to initialize the JournalDto.
     *
     * @param id   The unique identifier of the journal.
     * @param journalName The name of the journal.
     */
    public JournalDto(int id, String journalName) {
        this.id = id;
        this.journalName = journalName;
    }

    /**
     * Get the unique identifier of the journal.
     *
     * @return The unique identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the unique identifier of the journal.
     *
     * @param id The unique identifier to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the name of the journal.
     *
     * @return The name of the journal.
     */
    public String getjournalName() {
        return journalName;
    }

    /**
     * Set the name of the journal.
     *
     * @param journalName The name to set.
     */
    public void setjournalName(String journalName) {
        this.journalName = journalName;
    }
}
