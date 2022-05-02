package fr.ciadlab.pubprovider.entities;

public enum PublicationType {

	//TODO: a supprimer dès que possible
    TypeLess {
		@Override
		public PublicationTypeGroup getPublicationTypeGroupFromPublicationType() {
			return PublicationTypeGroup.Typeless;
		}

		@Override
		public String getAcronymFromPublicationType() {
			return null;
		}

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return null;
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
            return "ACL or/ou ACLN";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "International journal with reading committee";
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
            return "ACL or/ou ACLN";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "National journal with reading committee";
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
            return "ACLN";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "International journal without reading committee";
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
            return "ACLN";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "National journal without reading committee";
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
            return "C-ACTI";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "International conference with proceedings";
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
            return "C-ACTN";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "National conference with proceedings";
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
            return "C-COM";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "International conference without proceedings";
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
            return "C-COM";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "National conference without proceedings";
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
            return "OS";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "Book";
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
            return "COS";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "Book chapter";
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
            return "COV";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "Vulgarization book chapter";
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
            return "C-INV";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "Invited conference";
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
            return "AP";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "Book edition";
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
            return "C-AFF";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "Seminar";
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
            return "TH";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "HDR thesis";
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
            return "TH";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "PHD thesis";
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
            return "TH";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "Master on research";
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
            return "PV";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "Popularization paper";
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
            return "AP";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "Patent";
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
            return "AP";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "Engineering activity";
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
            return "TH";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "Engineering thesis";
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
            return "OV";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "Scientific popularization book";
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
            return "AP";
        }

		@Override
		public String getPubTypeTitleFromPublicationType() {
			return "User documentation";
		}
	};
	
	public abstract PublicationTypeGroup getPublicationTypeGroupFromPublicationType();

    public abstract String getAcronymFromPublicationType();

    public abstract String getPubTypeTitleFromPublicationType();
    
    public static String getPubTypeToString(PublicationType pubType) {
			return pubType.getPubTypeTitleFromPublicationType() + " ("+ pubType.getAcronymFromPublicationType() +")";
	}
   
}


