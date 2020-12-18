package fr.ciadlab.pubprovider.entities;

public enum PublicationTypeGroup {
    Typeless,
    ReadingCommitteeJournalPopularizationPaper,
    ProceedingsConference,
    Book,
    BookChapter,
    SeminarPatentInvitedConference,
    UniversityDocument,
    EngineeringActivity,
    UserDocumentation;

    public static PublicationTypeGroup getPublicationTypeGroupFromPublicationType(PublicationType publicationType) {
        PublicationTypeGroup typeGroup = Typeless;
        if(publicationType  == PublicationType.InternationalJournalWithReadingCommittee ||
                publicationType  == PublicationType.NationalJournalWithReadingCommittee ||
                publicationType  == PublicationType.InternationalJournalWithoutReadingCommittee ||
                publicationType  == PublicationType.NationalJournalWithoutReadingCommittee ||
                publicationType  == PublicationType.PopularizationPaper)
        {
            typeGroup=ReadingCommitteeJournalPopularizationPaper;
        }
        else if(publicationType  == PublicationType.InternationalConferenceWithProceedings ||
                publicationType  == PublicationType.NationalConferenceWithProceedings ||
                publicationType  == PublicationType.InternationalConferenceWithoutProceedings ||
                publicationType  == PublicationType.NationalConferenceWithoutProceedings)
        {
            typeGroup=ProceedingsConference;
        }
        else if(publicationType  == PublicationType.Book ||
                publicationType  == PublicationType.BookEdition ||
                publicationType  == PublicationType.ScientificPopularizationBook)
        {
            typeGroup=Book;
        }
        else if(publicationType  == PublicationType.BookChapter ||
                publicationType  == PublicationType.VulgarizationBookChapter)
        {
            typeGroup=BookChapter;
        }
        else if(publicationType  == PublicationType.Seminar ||
                publicationType  == PublicationType.Patent ||
                publicationType  == PublicationType.InvitedConference)
        {
            typeGroup=SeminarPatentInvitedConference;
        }
        else if(publicationType  == PublicationType.HDRThesis ||
                publicationType  == PublicationType.PHDThesis ||
                publicationType  == PublicationType.MasterOnResearch ||
                publicationType  == PublicationType.EngineeringThesis)
        {
            typeGroup=UniversityDocument;
        }
        else if(publicationType  == PublicationType.EngineeringActivity)
        {
            typeGroup=EngineeringActivity;
        }
        else if(publicationType  == PublicationType.UserDocumentation)
        {
            typeGroup=UserDocumentation;
        }
        return typeGroup;
    }
}
