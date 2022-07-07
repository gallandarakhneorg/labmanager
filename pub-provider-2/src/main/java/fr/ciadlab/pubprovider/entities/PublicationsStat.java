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

public class PublicationsStat {
    private int year;
    private int readingCommitteeJournalPopularizationPaperCount;
    private int proceedingsConferenceCount;
    private int bookCount;
    private int bookChapterCount;
    private int seminarPatentInvitedConferenceCount;
    private int universityDocumentCount;
    private int engineeringActivityCount;
    private int userDocumentationCount;
    private int otherCount;

    public PublicationsStat(int year) {
        this.year = year;
    }

    public PublicationsStat(int year, int readingCommitteeJournalPopularizationPaperCount, int proceedingsConferenceCount, int bookCount, int bookChapterCount, int seminarPatentInvitedConferenceCount, int universityDocumentCount, int engineeringActivityCount, int userDocumentationCount, int otherCount) {
        this.year = year;
        this.readingCommitteeJournalPopularizationPaperCount = readingCommitteeJournalPopularizationPaperCount;
        this.proceedingsConferenceCount = proceedingsConferenceCount;
        this.bookCount = bookCount;
        this.bookChapterCount = bookChapterCount;
        this.seminarPatentInvitedConferenceCount = seminarPatentInvitedConferenceCount;
        this.universityDocumentCount = universityDocumentCount;
        this.engineeringActivityCount = engineeringActivityCount;
        this.userDocumentationCount = userDocumentationCount;
        this.otherCount = otherCount;
    }

    public int getTotal() {
        return readingCommitteeJournalPopularizationPaperCount + proceedingsConferenceCount + bookCount + bookChapterCount + seminarPatentInvitedConferenceCount + universityDocumentCount + engineeringActivityCount + userDocumentationCount + otherCount;
    }

    public int getYear() {
        return year;
    }

    public int getReadingCommitteeJournalPopularizationPaperCount() {
        return readingCommitteeJournalPopularizationPaperCount;
    }

    public int getProceedingsConferenceCount() {
        return proceedingsConferenceCount;
    }

    public int getBookCount() {
        return bookCount;
    }

    public int getBookChapterCount() {
        return bookChapterCount;
    }

    public int getSeminarPatentInvitedConferenceCount() {
        return seminarPatentInvitedConferenceCount;
    }

    public int getUniversityDocumentCount() {
        return universityDocumentCount;
    }

    public int getEngineeringActivityCount() {
        return engineeringActivityCount;
    }

    public int getUserDocumentationCount() {
        return userDocumentationCount;
    }

    public int getOtherCount() {
        return otherCount;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setReadingCommitteeJournalPopularizationPaperCount(int readingCommitteeJournalPopularizationPaperCount) {
        this.readingCommitteeJournalPopularizationPaperCount = readingCommitteeJournalPopularizationPaperCount;
    }

    public void setProceedingsConferenceCount(int proceedingsConferenceCount) {
        this.proceedingsConferenceCount = proceedingsConferenceCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }

    public void setBookChapterCount(int bookChapterCount) {
        this.bookChapterCount = bookChapterCount;
    }

    public void setSeminarPatentInvitedConferenceCount(int seminarPatentInvitedConferenceCount) {
        this.seminarPatentInvitedConferenceCount = seminarPatentInvitedConferenceCount;
    }

    public void setUniversityDocumentCount(int universityDocumentCount) {
        this.universityDocumentCount = universityDocumentCount;
    }

    public void setEngineeringActivityCount(int engineeringActivityCount) {
        this.engineeringActivityCount = engineeringActivityCount;
    }

    public void setUserDocumentationCount(int userDocumentationCount) {
        this.userDocumentationCount = userDocumentationCount;
    }

    public void setOtherCount(int otherCount) {
        this.otherCount = otherCount;
    }
}
