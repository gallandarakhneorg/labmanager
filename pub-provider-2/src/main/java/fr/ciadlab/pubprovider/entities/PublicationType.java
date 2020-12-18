package fr.ciadlab.pubprovider.entities;

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

    public static String getPubTypeToString(PublicationType pubType) {
        switch(pubType){

            case InternationalJournalWithReadingCommittee:
                return "International journal with reading committee";
            case NationalJournalWithReadingCommittee:
                return "National journal with reading committee";
            case InternationalJournalWithoutReadingCommittee:
                return "International journal without reading committee";
            case NationalJournalWithoutReadingCommittee:
                return "Naational journal without reading committee";
            case InternationalConferenceWithProceedings:
                return "International conference with proceedings";
            case NationalConferenceWithProceedings:
                return "National conference with proceedings";
            case InternationalConferenceWithoutProceedings:
                return "International conference without proceedings";
            case NationalConferenceWithoutProceedings:
                return "Naational conference without proceedings";
            case Book:
                return "Book";
            case BookChapter:
                return "Book chapter";
            case VulgarizationBookChapter:
                return "Vulgarization book chapter";
            case InvitedConference:
                return "Invited conference";
            case BookEdition:
                return "Book edition";
            case Seminar:
                return "Seminar";
            case HDRThesis:
                return "HDR thesis";
            case PHDThesis:
                return "PHD thesis";
            case MasterOnResearch:
                return "Master on research";
            case PopularizationPaper:
                return "Popularization paper";
            case Patent:
                return "Patent";
            case EngineeringActivity:
                return "Engineering activity";
            case EngineeringThesis:
                return "Engineering thesis";
            case ScientificPopularizationBook:
                return "Scientific popularization book";
            case UserDocumentation:
                return "User documentation";
            case TypeLess:
            default:
                return "?";
        }
    }
}


