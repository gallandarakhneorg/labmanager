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

public enum PublicationTypeGroup {
    Typeless,
    //TODO: Renommer en JournalPaper
    ReadingCommitteeJournalPopularizationPaper,
    ProceedingsConference,
    Book,
    BookChapter,
    //TODO: Ajouter JournalPopularizationPaper
    //TODO: Ajouter PopularizationBookChapter
    SeminarPatentInvitedConference,
    UniversityDocument,
    EngineeringActivity,
    UserDocumentation;

}
