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

import fr.ciadlab.labmanager.entities.journal.Journal;

/**
 * Data Transfer Object (DTO) for representing a Journal.
 */
public class JournalDto {

    /** The unique identifier of the journal. */
    private int id;

    /** The name of the journal. */
    private String journalName;
    
    /**
     * Parameterized constructor to initialize the JournalDto from a Journal.
     *
     * @param journal the journal
     */
    public JournalDto(Journal journal) {
        this.id = journal.getId();
        this.journalName = journal.getJournalName();
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
     * Get the name of the journal.
     *
     * @return The name of the journal.
     */
    public String getjournalName() {
        return journalName;
    }
}
