package com.spring.rest.entities;

public enum PublicationType {
    TypeLess,

    //These are already present in the current site database :

    //Mapped by ReadingCommitteeJournalPopularizationPaper
    InternationalJournalWithReadingCommittee,
    //Mapped by ReadingCommitteeJournalPopularizationPaper
    NationalJournalWithReadingCommittee,
    //Mapped by ReadingCommitteeJournalPopularizationPaper
    InternationalJournalWithoutReadingCommittee,
    //Mapped by ReadingCommitteeJournalPopularizationPaper
    NationalJournalWithoutReadingCommittee,
    //Mapped by ProceedingsConference
    InternationalConferenceWithProceedings,
    //Mapped by ProceedingsConference
    NationalConferenceWithProceedings,
    //Mapped by ProceedingsConference
    InternationalConferenceWithoutProceedings,
    //Mapped by ProceedingsConference
    NationalConferenceWithoutProceedings,
    //Mapped by Book
    Book,
    //Mapped by Book->BookChaper
    BookChapter,
    //Mapped by Book->BookChaper
    VulgarizationBookChapter,
    //Mapped by SeminarPatentInvitedConference
    InvitedConference,
    //Mapped by Book
    BookEdition,
    //Mapped by SeminarPatentInvitedConference
    Seminar,
    //Mapped by UniversityDocument
    HDRThesis,
    //Mapped by UniversityDocument
    PHDThesis,
    //Mapped by UniversityDocument
    MasterOnResearch,
    //Mapped by ReadingCommitteeJournalPopularizationPaper
    PopularizationPaper,
    //Mapped by SeminarPatentInvitedConference
    Patent,

    //These are submitable types but there are none in the current site database :

    //Mapped by EngineeringActivity
    EngineeringActivity,
    //Mapped by UniversityDocument
    EngineeringThesis,
    //Mapped by Book
    ScientificPopularizationBook,
    //Mapped by UserDocumentation
    UserDocumentation;
}
