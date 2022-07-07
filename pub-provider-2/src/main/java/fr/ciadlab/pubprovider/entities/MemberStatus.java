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

public enum MemberStatus {

    //teacher researcher types
    PR,
    MCF,
    MCF_HDR,
    ECC,
    LRU,
    PHD_Student,
    PostPHD,
    ITRF, //Includes IGE, IGE, ADM...
    Prag,
    Associate,
    //Some additional ones that can be useful when none of the above applies
    Intern,
    Contractless;
}
