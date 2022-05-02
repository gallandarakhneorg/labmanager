package fr.ciadlab.pubprovider.entities;

public enum PublicationType {

	//TODO: a supprimer dès que possible
    TypeLess {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.Typeless;
		}
	},

    //These are already present in the current site database :

    //Mapped by ReadingCommitteeJournalPopularizationPaper
    //ACL if ranked (quartile)
    //ACLN if not ranked (quartile)
    InternationalJournalWithReadingCommittee {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.ReadingCommitteeJournalPopularizationPaper;
		}
	},
    //Mapped by ReadingCommitteeJournalPopularizationPaper
    //ACL if ranked (quartile)
    //ACLN if not ranked (quartile)
    NationalJournalWithReadingCommittee {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.ReadingCommitteeJournalPopularizationPaper;
		}
	},
    //Mapped by ReadingCommitteeJournalPopularizationPaper
    //ACLN
    InternationalJournalWithoutReadingCommittee {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.ReadingCommitteeJournalPopularizationPaper;
		}
	},
    //Mapped by ReadingCommitteeJournalPopularizationPaper
    //ACLN
    NationalJournalWithoutReadingCommittee {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.ReadingCommitteeJournalPopularizationPaper;
		}
	},
    //Mapped by ProceedingsConference
    //C-ACTI
    InternationalConferenceWithProceedings {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.ProceedingsConference;
		}
	},
    //Mapped by ProceedingsConference
    //C-ACTN
    NationalConferenceWithProceedings {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.ProceedingsConference;
		}
	},
    //Mapped by ProceedingsConference
    //C-COM
    InternationalConferenceWithoutProceedings {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.ProceedingsConference;
		}
	},
    //Mapped by ProceedingsConference
    //C-COM
    NationalConferenceWithoutProceedings {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.ProceedingsConference;
		}
	},
    //Mapped by Book
    //OS
    Book {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.Book;
		}
	},
    //Mapped by Book->BookChaper
    //COS
    BookChapter {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.BookChapter;
		}
	},
    //Mapped by Book->BookChaper
    //COV
    VulgarizationBookChapter {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.BookChapter;
		}
	},
    //Mapped by SeminarPatentInvitedConference
    //C-INV
    InvitedConference {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.SeminarPatentInvitedConference;
		}
	},
    //Mapped by Book
    //AP
    BookEdition {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.Book;
		}
	},
    //Mapped by SeminarPatentInvitedConference
    //C-AFF
    Seminar {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.SeminarPatentInvitedConference;
		}
	},
    //Mapped by UniversityDocument
    //TH
    HDRThesis {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.UniversityDocument;
		}
	},
    //Mapped by UniversityDocument
    //TH
    PHDThesis {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.UniversityDocument;
		}
	},
    //Mapped by UniversityDocument
    //TH
    MasterOnResearch {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.UniversityDocument;
		}
	},
    //Mapped by ReadingCommitteeJournalPopularizationPaper
    //PV
    PopularizationPaper {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.ReadingCommitteeJournalPopularizationPaper;
		}
	},
    //Mapped by SeminarPatentInvitedConference
    //AP
    Patent {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.SeminarPatentInvitedConference;
		}
	},

    //These are submitable types but there are none in the current site database :

    //Mapped by EngineeringActivity
    //AP
    EngineeringActivity {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.EngineeringActivity;
		}
	},
    //Mapped by UniversityDocument
    //TH
    EngineeringThesis {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.UniversityDocument;
		}
	},
    //Mapped by Book
    //OV
    ScientificPopularizationBook {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.Book;
		}
	},
    //Mapped by UserDocumentation
    //AP
    UserDocumentation {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.UserDocumentation;
		}
	};
	
	public abstract PublicationTypeGroup getPublicationTypeGroupFromPublicationType();

    public static String getPubTypeToString(PublicationType pubType) {
        switch(pubType){

            case InternationalJournalWithReadingCommittee:
                return "International journal with reading committee";
            case NationalJournalWithReadingCommittee:
                return "National journal with reading committee";
            case InternationalJournalWithoutReadingCommittee:
                return "International journal without reading committee";
            case NationalJournalWithoutReadingCommittee:
                return "National journal without reading committee";
            case InternationalConferenceWithProceedings:
                return "International conference with proceedings";
            case NationalConferenceWithProceedings:
                return "National conference with proceedings";
            case InternationalConferenceWithoutProceedings:
                return "International conference without proceedings";
            case NationalConferenceWithoutProceedings:
                return "National conference without proceedings";
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
            default:
                return "?";
        }
    }
}


