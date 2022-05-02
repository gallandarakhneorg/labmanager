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

        @Override
        public String getAcronymFromPublicationType(){
            return "ACL or/ou ACLN"
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
        
        @Override
        public String getAcronymFromPublicationType(){
            return "ACL or/ou ACLN"
        }
	},
    //Mapped by ReadingCommitteeJournalPopularizationPaper
    //ACLN
    InternationalJournalWithoutReadingCommittee {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.ReadingCommitteeJournalPopularizationPaper;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "ACLN"
        }
	},
    //Mapped by ReadingCommitteeJournalPopularizationPaper
    //ACLN
    NationalJournalWithoutReadingCommittee {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.ReadingCommitteeJournalPopularizationPaper;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "ACLN"
        }
	},
    //Mapped by ProceedingsConference
    //C-ACTI
    InternationalConferenceWithProceedings {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.ProceedingsConference;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "C-ACTI"
        }
	},
    //Mapped by ProceedingsConference
    //C-ACTN
    NationalConferenceWithProceedings {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.ProceedingsConference;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "C-ACTN"
        }
	},
    //Mapped by ProceedingsConference
    //C-COM
    InternationalConferenceWithoutProceedings {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.ProceedingsConference;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "C-COM"
        }
	},
    //Mapped by ProceedingsConference
    //C-COM
    NationalConferenceWithoutProceedings {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.ProceedingsConference;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "C-COM"
        }
	},
    //Mapped by Book
    //OS
    Book {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.Book;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "OS"
        }
	},
    //Mapped by Book->BookChaper
    //COS
    BookChapter {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.BookChapter;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "COS"
        }
	},
    //Mapped by Book->BookChaper
    //COV
    VulgarizationBookChapter {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.BookChapter;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "COV"
        }
	},
    //Mapped by SeminarPatentInvitedConference
    //C-INV
    InvitedConference {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.SeminarPatentInvitedConference;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "C-INV"
        }
	},
    //Mapped by Book
    //AP
    BookEdition {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.Book;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "AP"
        }
	},
    //Mapped by SeminarPatentInvitedConference
    //C-AFF
    Seminar {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.SeminarPatentInvitedConference;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "C-AFF"
        }
	},
    //Mapped by UniversityDocument
    //TH
    HDRThesis {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.UniversityDocument;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "TH"
        }
	},
    //Mapped by UniversityDocument
    //TH
    PHDThesis {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.UniversityDocument;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "TH"
        }
	},
    //Mapped by UniversityDocument
    //TH
    MasterOnResearch {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.UniversityDocument;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "TH"
        }
	},
    //Mapped by ReadingCommitteeJournalPopularizationPaper
    //PV
    PopularizationPaper {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.ReadingCommitteeJournalPopularizationPaper;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "PV"
        }
	},
    //Mapped by SeminarPatentInvitedConference
    //AP
    Patent {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.SeminarPatentInvitedConference;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "AP"
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
        
        @Override
        public String getAcronymFromPublicationType(){
            return "AP"
        }
	},
    //Mapped by UniversityDocument
    //TH
    EngineeringThesis {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.UniversityDocument;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "TH"
        }
	},
    //Mapped by Book
    //OV
    ScientificPopularizationBook {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.Book;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "OV"
        }
	},
    //Mapped by UserDocumentation
    //AP
    UserDocumentation {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.UserDocumentation;
		}
        
        @Override
        public String getAcronymFromPublicationType(){
            return "AP"
        }
	};
	
	public abstract PublicationTypeGroup getPublicationTypeGroupFromPublicationType();

    public abstract String getAcronymFromPublicationType();

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


