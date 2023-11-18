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

package fr.ciadlab.labmanager.dto.member;

import fr.ciadlab.labmanager.entities.member.Person;

/**
 * Data Transfer Object (DTO) for representing a Person.
 */
public class PersonDto {

    /** The unique identifier of the person. */
    private int id;

    /** The name of the person. */
    private String personFullName;
    
    /**
     * Parameterized constructor to initialize the PersonDto from a Person.
     *
     * @param person the person
     */
    public PersonDto(Person person) {
        this.id = person.getId();
        this.personFullName = person.getFullName();
    }

    /**
     * Get the unique identifier of the person.
     *
     * @return The unique identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the name of the person.
     *
     * @return The name of the person.
     */
    public String getpersonFullName() {
        return personFullName;
    }
}
