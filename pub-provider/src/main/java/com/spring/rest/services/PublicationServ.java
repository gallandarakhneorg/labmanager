package com.spring.rest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.rest.entities.*;
import com.spring.rest.repository.AuthorRepository;
import com.spring.rest.repository.AuthorshipRepository;
import com.spring.rest.repository.JournalRepository;
import com.spring.rest.repository.PublicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.*;

@Service
public class PublicationServ {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PublicationRepository repo;

    @Autowired
    private AuthorshipRepository autShipRepo;

    @Autowired
    private AuthorRepository autRepo;

    @Autowired
    private JournalRepository jourRepo;

    public List<Publication> getAllPublications() {
        List<Publication> publications = repo.findAll();

        for (Publication p : publications) {
            sortAuthorships(p);
            for (Authorship autShip : p.getPubAuts()) {
                Author aut = autShip.getAut();
                aut.setAutPubs(new LinkedList<>());
                autShip.setPub(null);
                for (final Membership mem : aut.getAutOrgs()) {
                    mem.setAut(null);
                    mem.getResOrg().setOrgAuts(new HashSet<>());
                    //We assume we dont need suborg infos from pubs
                    mem.getResOrg().setOrgSubs(new HashSet<>());
                    mem.getResOrg().setOrgSup(null);
                }
            }
            if (p.getClass() == ReadingCommitteeJournalPopularizationPaper.class) //I guess I could have done a pubType check instead
            {
                if (((ReadingCommitteeJournalPopularizationPaper) p).getReaComConfPopPapJournal() != null)
                    ((ReadingCommitteeJournalPopularizationPaper) p).getReaComConfPopPapJournal().setJourPubs(new HashSet<>());
            }
        }

        return publications;
    }

    public List<Publication> getPublication(int index) {
        List<Publication> result = new ArrayList<Publication>();
        final Optional<Publication> res = repo.findById(index);
        if (res.isPresent()) {
            result.add(res.get());
        }

        for (Publication p : result) {
            sortAuthorships(p);
            for (Authorship autShip : p.getPubAuts()) {
                Author aut = autShip.getAut();
                if(aut != null) {
                    aut.setAutPubs(new LinkedList<>());
                    autShip.setPub(null);
                    for (final Membership mem : aut.getAutOrgs()) {
                        mem.setAut(null);
                        mem.getResOrg().setOrgAuts(new HashSet<>());
                        //We assume we dont need suborg infos from pubs
                        mem.getResOrg().setOrgSubs(new HashSet<>());
                        mem.getResOrg().setOrgSup(null);
                    }
                }

            }
            if (p.getClass() == ReadingCommitteeJournalPopularizationPaper.class) {
                if (((ReadingCommitteeJournalPopularizationPaper) p).getReaComConfPopPapJournal() != null)
                    ((ReadingCommitteeJournalPopularizationPaper) p).getReaComConfPopPapJournal().setJourPubs(new HashSet<>());
            }
        }
        return result;
    }

    //Check if this sorts in the correct order
    public void sortAuthorships(Publication p) {
        Collections.sort(p.getPubAuts(), Comparator.comparing(s -> s.getRank()));
    }

    //On top of giving the publication back, it also translates the files to binary
    public List<Publication> getPublicationForDownload(int index) {
        List<Publication> result = new ArrayList<Publication>();
        final Optional<Publication> res = repo.findById(index);
        if (res.isPresent()) {
            result.add(res.get());
        }

        for (final Publication p : result) {
            for (Authorship autShip : p.getPubAuts()) {
                Author aut = autShip.getAut();
                autShip.setPub(null);
                aut.setAutPubs(new LinkedList<>());
                for (final Membership mem : aut.getAutOrgs()) {
                    mem.setAut(null);
                    mem.getResOrg().setOrgAuts(new HashSet<>());
                    //We assume we dont need suborg infos from pubs
                    mem.getResOrg().setOrgSubs(new HashSet<>());
                    mem.getResOrg().setOrgSup(null);
                }
            }
            encodeFiles(p);
            if (p.getClass() == ReadingCommitteeJournalPopularizationPaper.class) {
                if (((ReadingCommitteeJournalPopularizationPaper) p).getReaComConfPopPapJournal() != null)
                    ((ReadingCommitteeJournalPopularizationPaper) p).getReaComConfPopPapJournal().setJourPubs(new HashSet<>());
            }
        }
        return result;
    }

    public void removePublication(int index) {
        final Optional<Publication> res = repo.findById(index);
        if (res.isPresent()) {

            if (res.get().getClass() == ReadingCommitteeJournalPopularizationPaper.class) {
                Journal jour = ((ReadingCommitteeJournalPopularizationPaper) res.get()).getReaComConfPopPapJournal();
                if (jour != null && jour.getJourPubs().isEmpty()) {
                    jourRepo.delete(jour);
                }
            }

            repo.save(res.get());
            repo.deleteById(index);
        }
    }

    public Publication createPublication(String pubTitle, String pubAbstract, String pubKeywords, Date pubDate, String pubNote,
                                 String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL, String pubVideoURL, String pubDBLP,
                                 String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType) {
        final Publication res = new Publication();
        //Generic pub fields
        res.setPubTitle(pubTitle);
        res.setPubAbstract(pubAbstract);
        res.setPubKeywords(pubKeywords);
        res.setPubDate(pubDate);
        res.setPubNote(pubNote);
        res.setPubAnnotations(pubAnnotations);
        res.setPubISBN(pubISBN);
        res.setPubISSN(pubISSN);
        res.setPubDOIRef(pubDOIRef);
        res.setPubURL(pubURL);
        res.setPubDBLP(pubDBLP);
        res.setPubVideoURL(pubVideoURL);
        res.setPubPDFPath(pubPDFPath);
        res.setPubLanguage(pubLanguage);
        res.setPubPaperAwardPath(pubPaperAwardPath);
        res.setPubType(pubType);

        this.repo.save(res);

        return res;
    }


    public void updatePublication(int pubId, String pubTitle, String pubAbstract, String pubKeywords, Date pubDate,
                                  String pubNote, String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL,
                                  String pubDBLP, String pubPDFPath, String pubLanguage, String pubPaperAwardPath, String pubType) {
        final Optional<Publication> res = this.repo.findById(pubId);
        if (res.isPresent()) {
            //Generic pub fields
            if (pubTitle != null && !pubTitle.isEmpty())
                res.get().setPubTitle(pubTitle);
            if (pubAbstract != null && !pubAbstract.isEmpty())
                res.get().setPubAbstract(pubAbstract);
            if (pubKeywords != null && !pubKeywords.isEmpty())
                res.get().setPubKeywords(pubKeywords);
            if (pubDate != null && pubDate != null)
                res.get().setPubDate(pubDate);
            if (pubNote != null && !pubNote.isEmpty())
                res.get().setPubNote(pubNote);
            if (pubAnnotations != null && !pubAnnotations.isEmpty())
                res.get().setPubAnnotations(pubAnnotations);
            if (pubISBN != null && !pubISBN.isEmpty())
                res.get().setPubISBN(pubISBN);
            if (pubISSN != null && !pubISSN.isEmpty())
                res.get().setPubISSN(pubISSN);
            if (pubDOIRef != null && !pubDOIRef.isEmpty())
                res.get().setPubDOIRef(pubDOIRef);
            if (pubURL != null && !pubURL.isEmpty())
                res.get().setPubURL(pubURL);
            if (pubDBLP != null && !pubDBLP.isEmpty())
                res.get().setPubDBLP(pubDBLP);
            if (pubPDFPath != null && !pubPDFPath.isEmpty())
                res.get().setPubPDFPath(pubPDFPath);
            if (pubLanguage != null && !pubLanguage.isEmpty())
                res.get().setPubLanguage(pubLanguage);
            if (pubPaperAwardPath != null && !pubPaperAwardPath.isEmpty())
                res.get().setPubPaperAwardPath(pubPaperAwardPath);
            if (pubType != null && !pubType.isEmpty())
                res.get().setPubType(PublicationType.valueOf(pubType));
            this.repo.save(res.get());
        }
    }

    public List<Publication> getLinkedMembersPublications(int index) {
        List<Publication> publications = repo.findDistinctByPubAutsAutAutOrgsResOrgResOrgId(index);

        for (Publication p : publications) {
            sortAuthorships(p);
            for (Authorship autShip : p.getPubAuts()) {
                Author aut = autShip.getAut();
                aut.setAutPubs(new LinkedList<>());
                autShip.setPub(null);
                for (final Membership mem : aut.getAutOrgs()) {
                    mem.setAut(null);
                    mem.getResOrg().setOrgAuts(new HashSet<>());
                    //We assume we dont need suborg infos from pubs
                    mem.getResOrg().setOrgSubs(new HashSet<>());
                    mem.getResOrg().setOrgSup(null);
                }
            }
            if (p.getClass() == ReadingCommitteeJournalPopularizationPaper.class) {
                if (((ReadingCommitteeJournalPopularizationPaper) p).getReaComConfPopPapJournal() != null)
                    ((ReadingCommitteeJournalPopularizationPaper) p).getReaComConfPopPapJournal().setJourPubs(new HashSet<>());
            }
        }
        return publications;
    }

    public List<Publication> getLinkedPublications(int index) {
        List<Publication> publications = repo.findDistinctByPubAutsAutAutId(index);

        for (Publication p : publications) {
            sortAuthorships(p);
            for (Authorship autShip : p.getPubAuts()) {
                Author aut = autShip.getAut();
                aut.setAutPubs(new LinkedList<>());
                autShip.setPub(null);
                for (final Membership mem : aut.getAutOrgs()) {
                    mem.setAut(null);
                    mem.getResOrg().setOrgAuts(new HashSet<>());
                    //We assume we dont need suborg infos from pubs
                    mem.getResOrg().setOrgSubs(new HashSet<>());
                    mem.getResOrg().setOrgSup(null);
                }
            }
            if (p.getClass() == ReadingCommitteeJournalPopularizationPaper.class) {
                if (((ReadingCommitteeJournalPopularizationPaper) p).getReaComConfPopPapJournal() != null)
                    ((ReadingCommitteeJournalPopularizationPaper) p).getReaComConfPopPapJournal().setJourPubs(new HashSet<>());
            }
        }

        return publications;
    }

    public List<Integer> importPublications(String bibText) {

        //The multiagent DB was formatted with " = {" so I based my import around that but the UB DB was formatted with "={" so I need to change it to what I know how to handle
        bibText = bibText.replaceAll("=\\{", " = \\{");
        //Also its legal in bibtex to end an object without a , after the last } but Id rather have it since it helps distinguishing end of the line instead of random } in the middle of a field
        bibText = bibText.replaceAll("\\}\r", "\\},\r");

        bibText = fixEncoding(bibText);

        bibText = "\n" + bibText;

        //I used to separate the pubs by \n\n\n but spacing is inconsistent between databases so its safer to separate using the @ which signifies a new pub. Splitter here removes the @ but we can add it back up
        String splitter = "\n@"; //The \n is necessary to make sure the splitter dont split in the middle of a name containing @. That also means that without proper formatting the import will fail
        String[] pubs = bibText.split(splitter);
        String[] auts;
        Author aut;
        String autFirstName;
        String autLastName;
        int i;
        int pubId;
        int autId;
        Optional<Publication> optPub;
        Optional<Author> optAut;
        Optional<Journal> optJour;

        Authorship autShip;

        //Holds the IDs of the successfully imported IDs. We'll need it for type differenciation later.
        List<Integer> importedPubIds = new LinkedList<Integer>();

        String pubType;
        PublicationType javaPubType;

        //General fields

        String pubTitle;
        String pubAbstract;
        String pubKeywords;
        int year;
        int month;
        Date pubDate;
        String pubNote;
        String pubAnnotations;
        String pubISBN;
        String pubISSN;
        String pubDOIRef;
        String pubURL;
        String pubDBLP;
        String pubPDFPath;
        String pubLanguage;
        String pubPaperAwardPath;

        //Type-specific fields

        String name;
        String publisher;
        String proceedings;
        String editor;
        String organization;
        String address;
        String series;
        String edition;
        String howPub;
        String reportType;
        String volume;
        String number;
        String pages;

        //Types to be persisted

        ReadingCommitteeJournalPopularizationPaper article;
        Journal jour;
        ProceedingsConference inproceedings;
        Book book;
        BookChapter inbook;
        SeminarPatentInvitedConference misc;
        UserDocumentation manual;
        EngineeringActivity techreport;
        UniversityDocument phdthesis;
        UniversityDocument masterthesis;

        //Used to persist

        List<Publication> pubL = new LinkedList<Publication>();
        List<Author> authorList = new LinkedList<Author>();
        String autL;
        boolean isDupe;

        for (String pub : pubs) {
            if (pub != pubs[0]) {
                pub = "@" + pub;
                //System.out.println(pub);

                pubType = "";
                javaPubType = PublicationType.TypeLess;
                autId = 0;
                pubId = 0;

                optPub = null;
                optAut = null;
                optJour = null;

                autShip = new Authorship();

                pubTitle = null; //Check for dupes later
                pubAbstract = null;
                pubKeywords = null;
                year = 0;
                month = 0;
                pubDate = new Date(0);
                pubNote = null;
                pubAnnotations = null;
                pubISBN = null;
                pubISSN = null;
                pubDOIRef = null;
                pubURL = null;
                pubDBLP = null;
                pubPDFPath = null;
                pubLanguage = null;
                pubPaperAwardPath = null;

                name = null;
                publisher = null;
                proceedings = null;
                editor = null;
                organization = null;
                address = null;
                series = null;
                edition = null;
                howPub = null;
                reportType = null;
                pages = null;
                volume = null;
                number = null;

                article = new ReadingCommitteeJournalPopularizationPaper();
                jour = new Journal();
                inproceedings = new ProceedingsConference();
                book = new Book();
                inbook = new BookChapter();
                misc = new SeminarPatentInvitedConference();
                manual = new UserDocumentation();
                techreport = new EngineeringActivity();
                phdthesis = new UniversityDocument();
                masterthesis = new UniversityDocument();

                isDupe = false;
                autFirstName = "";
                autLastName = "";

                pubL = repo.findAll();

                authorList = autRepo.findAll();

                splitter = "@";
                if (pub.contains(splitter)) //Dont do anything if it doesnt even have a type
                {
                    pubType = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("{", pub.indexOf(splitter)));

                    splitter = "	title = {";//I cant just look up title = { or itll pick up booktitle. So make sure you bibtext are properly formatted.
                    if (pub.contains(splitter)) {
                        pubTitle = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                        if (pubTitle.length() > 255) {
                            System.out.println("\n Warning : Publication Title too long, had to be truncated. \n Concerned publication : " + pubTitle + "\n");
                        }
                        pubTitle = truncate(pubTitle);

                        //Checking for dupes
                        for (Publication singlePub : pubL) {
                            if (singlePub.getPubTitle().compareTo(pubTitle) == 0) {
                                isDupe = true;
                            }
                        }

                        if (!isDupe) {

                            splitter = "abstract = {";
                            if (pub.contains(splitter)) {
                                pubAbstract = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                //pubAbstract=truncate(pubAbstract); abstract is 67k character long so no need to trunck it
                            }

                            splitter = "keywords = {";
                            if (pub.contains(splitter)) {
                                pubKeywords = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                pubKeywords = truncate(pubKeywords);
                            }


                            splitter = "year = {";
                            if (pub.contains(splitter)) {
                                year = Integer.parseInt(pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter))));

                                splitter = "month = {";
                                if (pub.contains(splitter)) {
                                    month = convertMonth(pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter))));

                                    pubDate = new Date(year - 1900, month, 2);
                                    //Day isnt specified so Im making it the first of the month
                                    //That means 2 since the bug reducing the date by one exists
                                }
                            } else //Because sometimes year is formatted without {}
                            {
                                splitter = "year = ";
                                if (pub.contains(splitter)) {
                                    year = Integer.parseInt(pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf(",", pub.indexOf(splitter))));

                                    splitter = "month = ";
                                    if (pub.contains(splitter)) {
                                        month = convertMonth(pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf(",", pub.indexOf(splitter))));

                                        pubDate = new Date(year - 1900, month, 2);
                                        //Day isnt specified so Im making it the first of the month
                                        //That means 2 since the bug reducing the date by one exists
                                    }
                                }
                            }

                            splitter = "note = {";
                            if (pub.contains(splitter)) {
                                pubNote = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                pubNote = truncate(pubNote);
                            }

                            splitter = "annotations = {";
                            if (pub.contains(splitter)) {
                                pubAnnotations = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                pubAnnotations = truncate(pubAnnotations);
                            }

                            splitter = "isbn = {";
                            if (pub.contains(splitter)) {
                                pubISBN = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                pubISBN = truncate(pubISBN);
                            }

                            splitter = "issn = {";
                            if (pub.contains(splitter)) {
                                pubISSN = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                pubISSN = truncate(pubISSN);
                            }

                            splitter = "doi = {";
                            if (pub.contains(splitter)) {
                                pubDOIRef = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                pubDOIRef = truncate(pubDOIRef);
                            }

                            splitter = "url = {";
                            if (pub.contains(splitter)) {
                                pubURL = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                pubURL = truncate(pubURL);
                            }

                            splitter = "dblp = {";
                            if (pub.contains(splitter)) {
                                pubDBLP = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                pubDBLP = truncate(pubDBLP);
                            }

                            splitter = "pdf = {";
                            if (pub.contains(splitter)) {
                                pubPDFPath = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                pubPDFPath = truncate(pubPDFPath);
                            }

                            splitter = "language = {";
                            if (pub.contains(splitter)) {
                                pubLanguage = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                pubLanguage = truncate(pubLanguage);
                            }

                            splitter = "award = {";
                            if (pub.contains(splitter)) {
                                pubPaperAwardPath = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                pubPaperAwardPath = truncate(pubPaperAwardPath);
                            }

                            //Once again, some DB do this differently so I streamlined the naming convention
                            pubType = pubType.substring(0, 1).toUpperCase() + pubType.substring(1).toLowerCase();
                            switch (pubType) {
                                //Can be InternationalJournalWithReadingCommittee, NationalJournalWithReadingCommittee, InternationalJournalWithoutReadingCommittee, NationalJournalWithoutReadingCommittee or PopularizationPaper.
                                case "Article":
                                    javaPubType = PublicationType.InternationalJournalWithReadingCommittee; //Default

                                    splitter = "journal = {";
                                    if (pub.contains(splitter)) {
                                        name = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        name = truncate(name);
                                    }

                                    splitter = "volume = {";
                                    if (pub.contains(splitter)) {
                                        volume = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        volume = truncate(volume);
                                    }

                                    splitter = "number = {";
                                    if (pub.contains(splitter)) {
                                        number = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        number = truncate(number);
                                    }

                                    splitter = "pages = {";
                                    if (pub.contains(splitter)) {
                                        pages = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        pages = truncate(pages);
                                    }

                                    splitter = "publisher = {";
                                    if (pub.contains(splitter)) {
                                        publisher = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        publisher = truncate(publisher);
                                    }


                                    //I cant just call the article's class dao because it needs to have a session which is created on controller call
                                    //So I just copypasted the whole thing here, it's dirty but it works.

                                    article.setPubTitle(pubTitle);
                                    article.setPubAbstract(pubAbstract);
                                    article.setPubKeywords(pubKeywords);
                                    article.setPubDate(pubDate);
                                    article.setPubNote(pubNote);
                                    article.setPubAnnotations(pubAnnotations);
                                    article.setPubISBN(pubISBN);
                                    article.setPubISSN(pubISSN);
                                    article.setPubDOIRef(pubDOIRef);
                                    article.setPubURL(pubURL);
                                    article.setPubDBLP(pubDBLP);
                                    article.setPubPDFPath(pubPDFPath);
                                    article.setPubLanguage(pubLanguage);
                                    article.setPubPaperAwardPath(pubPaperAwardPath);
                                    article.setPubType(javaPubType);
                                    article.setReaComConfPopPapVolume(volume);
                                    article.setReaComConfPopPapNumber(number);
                                    article.setReaComConfPopPapPages(pages);

                                    //Journal fields
                                    optJour = jourRepo.findByJourName(name);
                                    if (optJour.isPresent()) {
                                        //Checks if that journal already exists
                                        jour = optJour.get();
                                    } else {
                                        //Or if we need to make a new one
                                        jour.setJourName(name);
                                        jour.setJourPublisher(publisher);
                                    }

                                    //Needed to generate an Id in case journal doesnt exist yet
                                    jourRepo.save(jour);

                                    article.setReaComConfPopPapJournal(jour);

                                    repo.save(article);
                                    pubId = article.getPubId();
                                    break;


                                //Can be InternationalConferenceWithProceedings, NationalConferenceWithProceedings, InternationalConferenceWithoutProceedings or NationalConferenceWithoutProceedings.
                                case "Inproceedings":
                                    javaPubType = PublicationType.InternationalConferenceWithProceedings; //Default

                                    splitter = "booktitle = {";
                                    if (pub.contains(splitter)) {
                                        name = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        name = truncate(name);
                                    }

                                    splitter = "editor = {";
                                    if (pub.contains(splitter)) {
                                        editor = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        editor = truncate(editor);
                                    }

                                    splitter = "pages = {";
                                    if (pub.contains(splitter)) {
                                        pages = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        pages = truncate(pages);
                                    }

                                    splitter = "organization = {";
                                    if (pub.contains(splitter)) {
                                        organization = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        organization = truncate(organization);
                                    }

                                    splitter = "publisher = {";
                                    if (pub.contains(splitter)) {
                                        publisher = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        publisher = truncate(publisher);
                                    }

                                    splitter = "address = {";
                                    if (pub.contains(splitter)) {
                                        address = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        address = truncate(address);
                                    }

                                    splitter = "series = {";
                                    if (pub.contains(splitter)) {
                                        series = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        series = truncate(series);
                                    }

                                    inproceedings.setPubTitle(pubTitle);
                                    inproceedings.setPubAbstract(pubAbstract);
                                    inproceedings.setPubKeywords(pubKeywords);
                                    inproceedings.setPubDate(pubDate);
                                    inproceedings.setPubNote(pubNote);
                                    inproceedings.setPubAnnotations(pubAnnotations);
                                    inproceedings.setPubISBN(pubISBN);
                                    inproceedings.setPubISSN(pubISSN);
                                    inproceedings.setPubDOIRef(pubDOIRef);
                                    inproceedings.setPubURL(pubURL);
                                    inproceedings.setPubDBLP(pubDBLP);
                                    inproceedings.setPubPDFPath(pubPDFPath);
                                    inproceedings.setPubLanguage(pubLanguage);
                                    inproceedings.setPubPaperAwardPath(pubPaperAwardPath);
                                    inproceedings.setPubType(javaPubType);
                                    inproceedings.setProConfBookNameProceedings(name);
                                    inproceedings.setProConfEditor(editor);
                                    inproceedings.setProConfPages(pages);
                                    inproceedings.setProConfOrganization(organization);
                                    inproceedings.setProConfPublisher(publisher);
                                    inproceedings.setProConfAddress(address);
                                    inproceedings.setProConfSeries(series);

                                    repo.save(inproceedings);
                                    pubId = inproceedings.getPubId();

                                    break;


                                //Can be Book, BookEdition or ScientificPopularizationBook.
                                case "Book":
                                    javaPubType = PublicationType.Book; //Default

                                    splitter = "editor = {";
                                    if (pub.contains(splitter)) {
                                        editor = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        editor = truncate(editor);
                                    }

                                    splitter = "publisher = {";
                                    if (pub.contains(splitter)) {
                                        publisher = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        publisher = truncate(publisher);
                                    }

                                    splitter = "volume = {";
                                    if (pub.contains(splitter)) {
                                        volume = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        volume = truncate(volume);
                                    }

                                    splitter = "series = {";
                                    if (pub.contains(splitter)) {
                                        series = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        series = truncate(series);
                                    }

                                    splitter = "address = {";
                                    if (pub.contains(splitter)) {
                                        address = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        address = truncate(address);
                                    }

                                    splitter = "edition = {";
                                    if (pub.contains(splitter)) {
                                        edition = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        edition = truncate(edition);
                                    }

                                    splitter = "pages = {";
                                    if (pub.contains(splitter)) {
                                        pages = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        pages = truncate(pages);
                                    }

                                    book.setPubTitle(pubTitle);
                                    book.setPubAbstract(pubAbstract);
                                    book.setPubKeywords(pubKeywords);
                                    book.setPubDate(pubDate);
                                    book.setPubNote(pubNote);
                                    book.setPubAnnotations(pubAnnotations);
                                    book.setPubISBN(pubISBN);
                                    book.setPubISSN(pubISSN);
                                    book.setPubDOIRef(pubDOIRef);
                                    book.setPubURL(pubURL);
                                    book.setPubDBLP(pubDBLP);
                                    book.setPubPDFPath(pubPDFPath);
                                    book.setPubLanguage(pubLanguage);
                                    book.setPubPaperAwardPath(pubPaperAwardPath);
                                    book.setPubType(javaPubType);
                                    book.setBookEditor(editor);
                                    book.setBookPublisher(publisher);
                                    book.setBookVolume(volume);
                                    book.setBookSeries(series);
                                    book.setBookAddress(address);
                                    book.setBookEdition(edition);
                                    book.setBookPages(pages);

                                    repo.save(book);
                                    pubId = book.getPubId();

                                    break;

                                //Can only be bookchapter
                                case "Inbook":
                                    javaPubType = PublicationType.BookChapter; //Default

                                    splitter = "editor = {";
                                    if (pub.contains(splitter)) {
                                        editor = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        editor = truncate(editor);
                                    }

                                    splitter = "publisher = {";
                                    if (pub.contains(splitter)) {
                                        publisher = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        publisher = truncate(publisher);
                                    }

                                    splitter = "volume = {";
                                    if (pub.contains(splitter)) {
                                        volume = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        volume = truncate(volume);
                                    }

                                    splitter = "series = {";
                                    if (pub.contains(splitter)) {
                                        series = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        series = truncate(series);
                                    }

                                    splitter = "address = {";
                                    if (pub.contains(splitter)) {
                                        address = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        address = truncate(address);
                                    }

                                    splitter = "edition = {";
                                    if (pub.contains(splitter)) {
                                        edition = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        edition = truncate(edition);
                                    }

                                    splitter = "pages = {";
                                    if (pub.contains(splitter)) {
                                        pages = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        pages = truncate(pages);
                                    }

                                    splitter = "booktitle = {";
                                    if (pub.contains(splitter)) {
                                        proceedings = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        proceedings = truncate(proceedings);
                                    }

                                    splitter = "chapter = {";
                                    if (pub.contains(splitter)) {
                                        name = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        name = truncate(name);
                                    }

                                    inbook.setPubTitle(pubTitle);
                                    inbook.setPubAbstract(pubAbstract);
                                    inbook.setPubKeywords(pubKeywords);
                                    inbook.setPubDate(pubDate);
                                    inbook.setPubNote(pubNote);
                                    inbook.setPubAnnotations(pubAnnotations);
                                    inbook.setPubISBN(pubISBN);
                                    inbook.setPubISSN(pubISSN);
                                    inbook.setPubDOIRef(pubDOIRef);
                                    inbook.setPubURL(pubURL);
                                    inbook.setPubDBLP(pubDBLP);
                                    inbook.setPubPDFPath(pubPDFPath);
                                    inbook.setPubLanguage(pubLanguage);
                                    inbook.setPubPaperAwardPath(pubPaperAwardPath);
                                    inbook.setPubType(javaPubType);
                                    inbook.setBookEditor(editor);
                                    inbook.setBookPublisher(publisher);
                                    inbook.setBookVolume(volume);
                                    inbook.setBookSeries(series);
                                    inbook.setBookAddress(address);
                                    inbook.setBookEdition(edition);
                                    inbook.setBookPages(pages);
                                    inbook.setBookChapBookNameProceedings(proceedings);
                                    inbook.setBookChapNumberOrName(name);

                                    repo.save(inbook);

                                    pubId = inbook.getPubId();

                                    break;

                                //Can be seminar, patent or invitedconference
                                case "Misc":
                                    javaPubType = PublicationType.Seminar; //Default

                                    splitter = "howpublished = {";
                                    if (pub.contains(splitter)) {
                                        howPub = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        howPub = truncate(howPub);
                                    }

                                    misc.setPubTitle(pubTitle);
                                    misc.setPubAbstract(pubAbstract);
                                    misc.setPubKeywords(pubKeywords);
                                    misc.setPubDate(pubDate);
                                    misc.setPubNote(pubNote);
                                    misc.setPubAnnotations(pubAnnotations);
                                    misc.setPubISBN(pubISBN);
                                    misc.setPubISSN(pubISSN);
                                    misc.setPubDOIRef(pubDOIRef);
                                    misc.setPubURL(pubURL);
                                    misc.setPubDBLP(pubDBLP);
                                    misc.setPubPDFPath(pubPDFPath);
                                    misc.setPubLanguage(pubLanguage);
                                    misc.setPubPaperAwardPath(pubPaperAwardPath);
                                    misc.setPubType(javaPubType);
                                    misc.setSemPatHowPub(howPub);

                                    repo.save(misc);
                                    pubId = misc.getPubId();

                                    break;

                                //Can only be userdoc
                                case "Manual":
                                    javaPubType = PublicationType.UserDocumentation; //Default

                                    splitter = "organization = {";
                                    if (pub.contains(splitter)) {
                                        organization = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        organization = truncate(organization);
                                    }

                                    splitter = "address = {";
                                    if (pub.contains(splitter)) {
                                        address = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        address = truncate(address);
                                    }

                                    splitter = "edition = {";
                                    if (pub.contains(splitter)) {
                                        edition = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        edition = truncate(edition);
                                    }

                                    splitter = "publisher = {";
                                    if (pub.contains(splitter)) {
                                        publisher = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        publisher = truncate(publisher);
                                    }

                                    manual.setPubTitle(pubTitle);
                                    manual.setPubAbstract(pubAbstract);
                                    manual.setPubKeywords(pubKeywords);
                                    manual.setPubDate(pubDate);
                                    manual.setPubNote(pubNote);
                                    manual.setPubAnnotations(pubAnnotations);
                                    manual.setPubISBN(pubISBN);
                                    manual.setPubISSN(pubISSN);
                                    manual.setPubDOIRef(pubDOIRef);
                                    manual.setPubURL(pubURL);
                                    manual.setPubDBLP(pubDBLP);
                                    manual.setPubPDFPath(pubPDFPath);
                                    manual.setPubLanguage(pubLanguage);
                                    manual.setPubPaperAwardPath(pubPaperAwardPath);
                                    manual.setPubType(javaPubType);
                                    manual.setUserDocOrganization(organization);
                                    manual.setUserDocAddress(address);
                                    manual.setUserDocEdition(edition);
                                    manual.setUserDocPublisher(publisher);

                                    repo.save(manual);
                                    pubId = manual.getPubId();

                                    break;

                                //Can only be engineeringactivity
                                case "Techreport":
                                    javaPubType = PublicationType.EngineeringActivity; //Default

                                    splitter = "institution = {";
                                    if (pub.contains(splitter)) {
                                        name = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        name = truncate(name);
                                    }

                                    splitter = "type = {";
                                    if (pub.contains(splitter)) {
                                        reportType = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        reportType = truncate(reportType);
                                    }

                                    splitter = "number = {";
                                    if (pub.contains(splitter)) {
                                        number = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        number = truncate(number);
                                    }

                                    techreport.setPubTitle(pubTitle);
                                    techreport.setPubAbstract(pubAbstract);
                                    techreport.setPubKeywords(pubKeywords);
                                    techreport.setPubDate(pubDate);
                                    techreport.setPubNote(pubNote);
                                    techreport.setPubAnnotations(pubAnnotations);
                                    techreport.setPubISBN(pubISBN);
                                    techreport.setPubISSN(pubISSN);
                                    techreport.setPubDOIRef(pubDOIRef);
                                    techreport.setPubURL(pubURL);
                                    techreport.setPubDBLP(pubDBLP);
                                    techreport.setPubPDFPath(pubPDFPath);
                                    techreport.setPubLanguage(pubLanguage);
                                    techreport.setPubPaperAwardPath(pubPaperAwardPath);
                                    techreport.setPubType(javaPubType);
                                    techreport.setEngActInstitName(name);
                                    techreport.setEngActReportType(reportType);
                                    techreport.setEngActNumber(number);

                                    repo.save(techreport);
                                    pubId = techreport.getPubId();

                                    break;

                                //Can be phd or hdr thesis
                                case "Phdthesis":
                                    javaPubType = PublicationType.PHDThesis; //Default

                                    splitter = "school = {";
                                    if (pub.contains(splitter)) {
                                        name = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        name = truncate(name);
                                    }

                                    splitter = "address = {";
                                    if (pub.contains(splitter)) {
                                        address = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        address = truncate(address);
                                    }

                                    phdthesis.setPubTitle(pubTitle);
                                    phdthesis.setPubAbstract(pubAbstract);
                                    phdthesis.setPubKeywords(pubKeywords);
                                    phdthesis.setPubDate(pubDate);
                                    phdthesis.setPubNote(pubNote);
                                    phdthesis.setPubAnnotations(pubAnnotations);
                                    phdthesis.setPubISBN(pubISBN);
                                    phdthesis.setPubISSN(pubISSN);
                                    phdthesis.setPubDOIRef(pubDOIRef);
                                    phdthesis.setPubURL(pubURL);
                                    phdthesis.setPubDBLP(pubDBLP);
                                    phdthesis.setPubPDFPath(pubPDFPath);
                                    phdthesis.setPubLanguage(pubLanguage);
                                    phdthesis.setPubPaperAwardPath(pubPaperAwardPath);
                                    phdthesis.setPubType(javaPubType);
                                    phdthesis.setUniDocSchoolName(name);
                                    phdthesis.setUniDocAddress(address);

                                    repo.save(phdthesis);
                                    pubId = phdthesis.getPubId();

                                    break;

                                //Can be master or engineering thesis
                                case "Mastersthesis":
                                    javaPubType = PublicationType.MasterOnResearch; //Default

                                    splitter = "school = {";
                                    if (pub.contains(splitter)) {
                                        name = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        name = truncate(name);
                                    }

                                    splitter = "address = {";
                                    if (pub.contains(splitter)) {
                                        address = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                        address = truncate(address);
                                    }

                                    masterthesis.setPubTitle(pubTitle);
                                    masterthesis.setPubAbstract(pubAbstract);
                                    masterthesis.setPubKeywords(pubKeywords);
                                    masterthesis.setPubDate(pubDate);
                                    masterthesis.setPubNote(pubNote);
                                    masterthesis.setPubAnnotations(pubAnnotations);
                                    masterthesis.setPubISBN(pubISBN);
                                    masterthesis.setPubISSN(pubISSN);
                                    masterthesis.setPubDOIRef(pubDOIRef);
                                    masterthesis.setPubURL(pubURL);
                                    masterthesis.setPubDBLP(pubDBLP);
                                    masterthesis.setPubPDFPath(pubPDFPath);
                                    masterthesis.setPubLanguage(pubLanguage);
                                    masterthesis.setPubPaperAwardPath(pubPaperAwardPath);
                                    masterthesis.setPubType(javaPubType);
                                    masterthesis.setUniDocSchoolName(name);
                                    masterthesis.setUniDocAddress(address);

                                    repo.save(masterthesis);
                                    pubId = masterthesis.getPubId();

                                    break;
                            }
                            importedPubIds.add(pubId);

                            //Handle authors
                            splitter = "author = {";
                            if (pub.contains(splitter)) {
                                autL = pub.substring(pub.indexOf(splitter) + splitter.length(), pub.indexOf("},", pub.indexOf(splitter)));
                                if (autL.compareTo("") != 0) {
                                    splitter = " and ";
                                    auts = autL.split(splitter);

                                    for (i = 0; i < auts.length; i++) {
                                        autLastName = auts[i].substring(0, auts[i].indexOf(", "));
                                        autFirstName = auts[i].substring(auts[i].indexOf(", ") + 2);

                                        //Checking for dupes

                                        for (Author knownAut : authorList) {
                                            if (knownAut.getAutFirstName().compareTo(autFirstName) == 0 && knownAut.getAutLastName().compareTo(autLastName) == 0) {
                                                isDupe = true;
                                                autId = knownAut.getAutId();
                                            }
                                        }

                                        if (!isDupe) {
                                            //Creating new author
                                            aut = new Author();

                                            aut.setAutFirstName(autFirstName);
                                            aut.setAutLastName(autLastName);
                                            aut.setAutBirth(new Date(0));

                                            autRepo.save(aut);
                                            autId = aut.getAutId();

                                        }
                                        isDupe = false;


                                        //Assigning authorship
                                        optPub = repo.findById(pubId);
                                        optAut = autRepo.findById(autId);

                                        if (optPub.isPresent() && optAut.isPresent()) {

                                            if (Collections.disjoint(optPub.get().getPubAuts(), optAut.get().getAutPubs())) {
                                                autShip = new Authorship();
                                                autShip.setPub(optPub.get());
                                                autShip.setAut(optAut.get());
                                                autShip.setRank(autRepo.findDistinctByAutPubsPubPubId(optPub.get().getPubId()).size());
                                                repo.save(optPub.get());
                                                autShipRepo.save(autShip);

                                            }
                                        }

                                    }
                                } else //Author = ""
                                {
                                    //No nothing in particular I guess ?
                                    //No Author to create and no authorship to assign
                                }
                            }
                        }
                    }
                }
            }
        }

        return importedPubIds;
    }

    public String fixEncoding(String bibText) {
        //Manually deal with as much characters as possible yourself
        //Characters not dealt with manually just gets deleted
        //Depending on how the export is handled, we dont have to deal with any of this but I cant control how other DB export their stuff
        bibText = bibText.replaceAll("(\\{\\\\'E\\})", "É");
        bibText = bibText.replaceAll("(\\{\\\\'e\\})", "é");
        bibText = bibText.replaceAll("(\\{\\\\`E\\})", "È");
        bibText = bibText.replaceAll("(\\{\\\\`e\\})", "è");
        bibText = bibText.replaceAll("(\\{\\\\\\^E\\})", "Ê");
        bibText = bibText.replaceAll("(\\{\\\\\\^e\\})", "ê");
        bibText = bibText.replaceAll("(\\{\\\\\"E\\})", "Ë");
        bibText = bibText.replaceAll("(\\{\\\\\"e\\})", "ë");
        bibText = bibText.replaceAll("(\\{\\\\`A\\})", "À");
        bibText = bibText.replaceAll("(\\{\\\\`a\\})", "à");
        bibText = bibText.replaceAll("(\\{\\\\\\^A\\})", "Â");
        bibText = bibText.replaceAll("(\\{\\\\\\^a\\})", "â");
        bibText = bibText.replaceAll("(\\{\\\\\"A\\})", "Ä");
        bibText = bibText.replaceAll("(\\{\\\\\"a\\})", "ä");
        bibText = bibText.replaceAll("(\\{\\\\\\^\\{\\\\I\\}\\})", "Î");
        bibText = bibText.replaceAll("(\\{\\\\\\^\\{\\\\i\\}\\})", "î");
        bibText = bibText.replaceAll("(\\{\\\\\"\\{\\\\I\\}\\})", "Ï");
        bibText = bibText.replaceAll("(\\{\\\\\"\\{\\\\i\\}\\})", "ï");
        bibText = bibText.replaceAll("(\\{\\\\\\^O\\})", "Ô");
        bibText = bibText.replaceAll("(\\{\\\\\\^o\\})", "o");
        bibText = bibText.replaceAll("(\\{\\\\~O\\})", "Õ");
        bibText = bibText.replaceAll("(\\{\\\\~o\\})", "õ");
        bibText = bibText.replaceAll("(\\{\\\\\\^U\\})", "Û");
        bibText = bibText.replaceAll("(\\{\\\\\\^u\\})", "û");
        bibText = bibText.replaceAll("(\\{\\\\\"U\\})", "Ü");
        bibText = bibText.replaceAll("(\\{\\\\\"u\\})", "ü");
        bibText = bibText.replaceAll("(\\{\\\\c\\{C\\}\\})", "Ç");
        bibText = bibText.replaceAll("(\\{\\\\c\\{c\\}\\})", "ç");
        bibText = bibText.replaceAll("(\\{\\\\&\\})", "&");

        // TMT 25/11/20 : deal with {\string#####} chars
        bibText = bibText.replaceAll("\\{\\\\string(.)\\}", "$1");

        //Kind of a radical solution but we can just remove all characters we dont care about
        //We keep normal characters : a-z, A-Z, 0-9
        //Characters we need to read the bibText : @, ",", {, }, =
        //Some accents we know how to manage : é, É, è, È... (see above)
        //And characters java & the DB can handle : _, -, ., /, \, ;, :, ', ", ’, ^, !, ?, &, (, ), [, ], whitespaces, line jumps
        //System.out.println(bibText);

        String charsToKeep = "([^"
                + "a-zA-Z0-9"
                + "@,\\{\\}="
                + "ÉéÈèÊêËëÀàÂâÄäÎîÏïÔôÕõÛûÜüÇç&"
                + "\\(\\)\\[\\]’:!?_\\-./\\\\;'\"\\^\\s+"
                + "])";

        bibText = bibText.replaceAll(charsToKeep, "?");
        //System.out.println(bibText);
        return bibText;
    }

    public String truncate(String s) {
        return s.substring(0, Math.min(s.length(), 255));
    }

    public int convertMonth(String month) {
        int res = 0;

        switch (month) {
            case "jan":
                res = 0;
                break;

            case "feb":
                res = 1;
                break;

            case "mar":
                res = 2;
                break;

            case "apr":
                res = 3;
                break;

            case "may":
                res = 4;
                break;

            case "jun":
                res = 5;
                break;

            case "jul":
                res = 6;
                break;

            case "aug":
                res = 7;
                break;

            case "sep":
                res = 8;
                break;

            case "oct":
                res = 9;
                break;

            case "nov":
                res = 10;
                break;

            case "dec":
                res = 11;
                break;
        }
        return res;
    }

    public String convertBackMonth(String m) {
        String r = "";

        switch (m) {
            case "01":
                r = "jan";
                break;

            case "02":
                r = "feb";
                break;

            case "03":
                r = "mar";
                break;

            case "04":
                r = "apr";
                break;

            case "05":
                r = "may";
                break;

            case "06":
                r = "jun";
                break;

            case "07":
                r = "jul";
                break;

            case "08":
                r = "aug";
                break;

            case "09":
                r = "sep";
                break;

            case "10":
                r = "oct";
                break;

            case "11":
                r = "nov";
                break;

            case "12":
                r = "dec";
                break;
        }

        return r;
    }

    @Deprecated
    public String exportPublications(String pubs) {
        //System.out.println("in "+pubs);
        String export = "";

        export += "<bibTexExport>";
        export += pickExport(pubs, "bibTex");
        export += "</bibTexExport>";
        export += "<htmlExport>";
        export += pickExport(pubs, "html");
        export += "</htmlExport>";
		/*
		export+="<odtExport>";
		export+=pickExport(pubs, "odt");
		export+="</odtExport>";
		*/
        export += "<wosExport>";
        export += pickExport(pubs, "wos");
        export += "</wosExport>";

        //System.out.println("out "+export);
        return export;
    }

    @Deprecated
    public String pickExport(String pubs, String type) {
        String text = "";

        String[] pubL = pubs.split("\"pubId\":");
        int pubId;
        int i;
        for (i = 0; i < pubL.length; i++) {
            if (i != 0) //First split is just "[{"
            {
                pubId = Integer.parseInt(pubL[i].substring(0, pubL[i].indexOf(",")));

                switch (type) {
                    case "bibTex":
                        text += exportOneBibTex(pubId);
                        break;

                    case "html":
                        text += exportOneHtml(pubL, i);
                        break;

                    case "wos":
                        text += exportOneWos(pubId);
                        break;
                }
            }
        }

        return text;
    }

    public String exportOneBibTex(int pubId) {
        String bib = "";

        String groupType = "Typeless";

        Publication pub = getPublication(pubId).get(0);

        String data = "";

        //For each pub :

        groupType = groupPubType(pub.getPubType().toString());

        bib += "@";
        bib += groupType;
        bib += "{";
        bib += printAuthorsLastNames(pubId);
        bib += pub.getPubDate().toString().substring(0, 4);
        bib += "_";
        bib += pubId;
        bib += "\n\t";

        data = pub.getPubAbstract();
        if (data != null) //if data exists
        {
            bib += "abstract = {";
            bib += data;
            bib += "}, \n\t";
        }
        data = pub.getPubKeywords();
        if (data != null) {
            bib += "keywords = {";
            bib += data;
            bib += "}, \n\t";
        }
        data = pub.getPubNote();
        if (data != null) {
            bib += "note = {";
            bib += data;
            bib += "}, \n\t";
        }
        data = pub.getPubAnnotations();
        if (data != null) {
            bib += "annotations = {";
            bib += data;
            bib += "}, \n\t";
        }
        data = pub.getPubISBN();
        if (data != null) {
            bib += "isbn = {";
            bib += data;
            bib += "}, \n\t";
        }
        data = pub.getPubISSN();
        if (data != null) {
            bib += "issn = {";
            bib += data;
            bib += "}, \n\t";
        }
        data = pub.getPubDOIRef();
        if (data != null) {
            bib += "doi = {";
            bib += data;
            bib += "}, \n\t";
        }
        data = pub.getPubURL();
        if (data != null) {
            bib += "url = {";
            bib += data;
            bib += "}, \n\t";
        }
        data = pub.getPubDBLP();
        if (data != null) {
            bib += "dblp = {";
            bib += data;
            bib += "}, \n\t";
        }
        data = pub.getPubPDFPath();
        if (data != null) {
            bib += "pdf = {";
            bib += data;
            bib += "}, \n\t";
        }
        data = pub.getPubLanguage();
        if (data != null) {
            bib += "language = {";
            bib += data;
            bib += "}, \n\t";
        }
        data = pub.getPubPaperAwardPath();
        if (data != null) {
            bib += "award = {";
            bib += data;
            bib += "}, \n\t";
        }

        switch (groupType) {
            case "Article":
                if (((ReadingCommitteeJournalPopularizationPaper) pub).getReaComConfPopPapJournal() != null) {
                    data = ((ReadingCommitteeJournalPopularizationPaper) pub).getReaComConfPopPapJournal().getJourName();
                    if (data != null) {
                        bib += "journal = {";
                        bib += data;
                        bib += "}, \n\t";
                    }
                }
                data = ((ReadingCommitteeJournalPopularizationPaper) pub).getReaComConfPopPapVolume();
                if (data != null) {
                    bib += "volume = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((ReadingCommitteeJournalPopularizationPaper) pub).getReaComConfPopPapNumber();
                if (data != null) {
                    bib += "number = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((ReadingCommitteeJournalPopularizationPaper) pub).getReaComConfPopPapPages();
                if (data != null) {
                    bib += "pages = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                if (((ReadingCommitteeJournalPopularizationPaper) pub).getReaComConfPopPapJournal() != null) {
                    data = ((ReadingCommitteeJournalPopularizationPaper) pub).getReaComConfPopPapJournal().getJourPublisher();
                    if (data != null) {
                        bib += "publisher = {";
                        bib += data;
                        bib += "}, \n\t";
                    }
                }
                break;

            case "Inproceedings":
                data = ((ProceedingsConference) pub).getProConfBookNameProceedings();
                if (data != null) {
                    bib += "booktitle = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((ProceedingsConference) pub).getProConfEditor();
                if (data != null) {
                    bib += "editor = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((ProceedingsConference) pub).getProConfPages();
                if (data != null) {
                    bib += "pages = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((ProceedingsConference) pub).getProConfOrganization();
                if (data != null) {
                    bib += "organization = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((ProceedingsConference) pub).getProConfPublisher();
                if (data != null) {
                    bib += "publisher = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((ProceedingsConference) pub).getProConfAddress();
                if (data != null) {
                    bib += "address = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((ProceedingsConference) pub).getProConfSeries();
                if (data != null) {
                    bib += "series = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                break;

            case "Book":
                data = ((Book) pub).getBookEditor();
                if (data != null) {
                    bib += "editor = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((Book) pub).getBookPublisher();
                if (data != null) {
                    bib += "publisher = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((Book) pub).getBookVolume();
                if (data != null) {
                    bib += "volume = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((Book) pub).getBookSeries();
                if (data != null) {
                    bib += "series = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((Book) pub).getBookAddress();
                if (data != null) {
                    bib += "address = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((Book) pub).getBookEdition();
                if (data != null) {
                    bib += "edition = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((Book) pub).getBookPages();
                if (data != null) {
                    bib += "pages = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                break;

            case "Inbook":
                data = ((BookChapter) pub).getBookEditor();
                if (data != null) {
                    bib += "editor = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((BookChapter) pub).getBookPublisher();
                if (data != null) {
                    bib += "publisher = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((BookChapter) pub).getBookVolume();
                if (data != null) {
                    bib += "volume = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((BookChapter) pub).getBookSeries();
                if (data != null) {
                    bib += "series = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((BookChapter) pub).getBookAddress();
                if (data != null) {
                    bib += "address = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((BookChapter) pub).getBookEdition();
                if (data != null) {
                    bib += "edition = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((BookChapter) pub).getBookPages();
                if (data != null) {
                    bib += "pages = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((BookChapter) pub).getBookChapBookNameProceedings();
                if (data != null) {
                    bib += "booktitle = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((BookChapter) pub).getBookChapNumberOrName();
                if (data != null) {
                    bib += "chapter = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                break;

            case "Misc":
                data = ((SeminarPatentInvitedConference) pub).getSemPatHowPub();
                if (data != null) {
                    bib += "howpublished = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                break;

            case "Manual":
                data = ((UserDocumentation) pub).getUserDocOrganization();
                if (data != null) {
                    bib += "organization = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((UserDocumentation) pub).getUserDocAddress();
                if (data != null) {
                    bib += "address = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((UserDocumentation) pub).getUserDocEdition();
                if (data != null) {
                    bib += "edition = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((UserDocumentation) pub).getUserDocPublisher();
                if (data != null) {
                    bib += "publisher = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                break;

            case "Techreport":
                data = ((EngineeringActivity) pub).getEngActInstitName();
                if (data != null) {
                    bib += "institution = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((EngineeringActivity) pub).getEngActReportType();
                if (data != null) {
                    bib += "type = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((EngineeringActivity) pub).getEngActNumber();
                if (data != null) {
                    bib += "number = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                break;

            case "Phdthesis":
                data = ((UniversityDocument) pub).getUniDocSchoolName();
                if (data != null) {
                    bib += "school = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((UniversityDocument) pub).getUniDocAddress();
                if (data != null) {
                    bib += "address = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                break;

            case "Masterthesis":
                data = ((UniversityDocument) pub).getUniDocSchoolName();
                if (data != null) {
                    bib += "school = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                data = ((UniversityDocument) pub).getUniDocAddress();
                if (data != null) {
                    bib += "address = {";
                    bib += data;
                    bib += "}, \n\t";
                }
                break;
        }

        data = convertBackMonth(pub.getPubDate().toString().substring(5, 7));
        if (data != null) {
            bib += "month = ";
            bib += data;
            bib += ", \n\t";
        }
        data = pub.getPubDate().toString().substring(0, 4);
        if (data != null) {
            bib += "year = ";
            bib += data;
            bib += ", \n\t";
        }
        data = pub.getPubTitle();
        if (data != null) {
            bib += "title = {";
            bib += data;
            bib += "}, \n\t";
        }
        data = printAuthors(pubId);
        if (data != null) {
            bib += "author = {";
            bib += data;
            bib += "}, \n";
        }

        bib += "}\n\n\n";

        return bib;
    }

    /**
     * TMT 02/12/20 : create a new function to export a single publication in HTML using its ID
     * @param pubId the publication ID
     * @return the HTML encapsuled inside li tag
     */
    public String exportOneHtml(int pubId) {
        String html = "";

        Publication pub = getPublication(pubId).get(0);

        //details for each pub :
        // TODO use new functions availables into Publication class
        html += "<li align=\"justify\">";
        html += printPubAuthors(pub);
        html += printPubDesc(pub);
        html += printPubLinks(pub);
        html += "</li>";

        return html;
    }

    @Deprecated
    public String exportOneHtml(String[] pubL, int i) {
        String html = "";

        int pubId = Integer.parseInt(pubL[i].substring(0, pubL[i].indexOf(",")));
        Publication pub = getPublication(pubId).get(0);
        String year = pub.getPubDate().toString().substring(0, 4);
        String type = pub.getPubType().toString();

        String prevYear = "";
        String prevType = "";
        if (i != 1) //Index starts at 1 cus 0th one is {[ or smth
        {
            int prevPubId = Integer.parseInt(pubL[i - 1].substring(0, pubL[i - 1].indexOf(",")));
            Publication prevPub = getPublication(prevPubId).get(0);
            prevYear = prevPub.getPubDate().toString().substring(0, 4);
            prevType = prevPub.getPubType().toString();
        }

        if (year.compareTo(prevYear) != 0) {
            prevType = "";
            if (i != 1) {
                html += "</ul>";
                html += "</ul>";
            }
            html += "<h1>" + year + " - " + countList(pubL, i, "date") + " publications</h1>\n"; //Not sure if the \n is gonna mess anything up but itll be more readable in the console
            html += "<ul>";
        }

        if (type.compareTo(prevType) != 0) {
            if (i != 1 && prevType.compareTo("") != 0) {
                html += "</ul>";
            }
            html += "<h2>" + type + " - " + countList(pubL, i, "type") + " publications</h2>\n"; //Not sure if the \n is gonna mess anything up but itll be more readable in the console
            html += "<ul>";
        }

        //details for each pub :

        html += "<li align=\"justify\">";
        html += printPubAuthors(pub);
        html += printPubDesc(pub);
        html += printPubLinks(pub);
        html += "</li>\n"; //Not sure if the \n is gonna mess anything up but itll be more readable in the console


        if (i == pubL.length - 1) {
            html += "</ul>";
            html += "</ul>";
        }

        return html;
    }

    @Deprecated
    public String exportOneWos(int pubId) {
        String wos = "";

        //Not asked to do this yet

        return wos;
    }

    public String groupPubType(String pubType) {
        String group = "Typeless";

        if (pubType == "InternationalJournalWithReadingCommittee" || pubType == "NationalJournalWithReadingCommittee" || pubType == "InternationalJournalWithoutReadingCommittee" || pubType == "NationalJournalWithoutReadingCommittee" || pubType == "PopularizationPaper") {
            group = "Article";
        }
        if (pubType == "InternationalConferenceWithProceedings" || pubType == "NationalConferenceWithProceedings" || pubType == "InternationalConferenceWithoutProceedings" || pubType == "NationalConferenceWithoutProceedings") {
            group = "Inproceedings";
        }
        if (pubType == "Book" || pubType == "BookEdition" || pubType == "ScientificPopularizationBook") {
            group = "Book";
        }
        if (pubType == "BookChapter" || pubType == "VulgarizationBookChapter") {
            group = "Inbook";
        }
        if (pubType == "Seminar" || pubType == "Patent" || pubType == "InvitedConference") {
            group = "Misc";
        }
        if (pubType == "UserDocumentation") {
            group = "Manual";
        }
        if (pubType == "EngineeringActivity") {
            group = "Techreport";
        }
        if (pubType == "PHDThesis" || pubType == "HDRThesis") {
            group = "Phdthesis";
        }
        if (pubType == "MasterOnResearch" || pubType == "EngineeringThesis") {
            group = "Masterthesis";
        }

        return group;
    }

    public String printAuthorsLastNames(int pubId) {
        String auts = "";


        for (Authorship autShip : repo.findById(pubId).get().getPubAuts()) {
            Author aut = autShip.getAut();
            auts += capitalizeFirstLetter(aut.getAutLastName());
        }

        auts = auts.replaceAll("\\s", "");

        return auts;
    }

    public String printAuthors(int pubId) {
        String auts = "";

        List<Authorship> autShipL = new LinkedList<Authorship>();
        autShipL.addAll(repo.findById(pubId).get().getPubAuts());
        for (Authorship autShip : autShipL) {
            Author aut = autShip.getAut();
            if (aut != autShipL.get(0).getAut()) {
                auts += " and ";
            }
            auts += capitalizeFirstLetter(aut.getAutLastName()) + ", " + capitalizeFirstLetter(aut.getAutFirstName());
        }

        return auts;
    }

    public String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        } else {
            // TMT 25/11/20 enable capitalize first letter for every words
            if(original.contains("-")) {
                String newString = "";
                String[] split = original.split("-");
                for(String s : split) {
                    if(s.length() > 1)
                        newString += original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
                    else
                        newString += original.toUpperCase();
                }
                return newString;

            }
            else {
                return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
            }
        }
    }

    public String printPubAuthors(Publication pub) {
        String text = "";

        List<Authorship> autShipL = new LinkedList<Authorship>();
        autShipL.addAll(pub.getPubAuts());
        int i;
        for (i = 0; i < autShipL.size(); i++) {
            if (autShipL.get(i).getAut().isHasPage()) {
                text += "<u>";
            }

            text += autShipL.get(i).getAut().getAutFirstName();
            text += " ";
            text += autShipL.get(i).getAut().getAutLastName().toUpperCase();

            if (autShipL.get(i).getAut().isHasPage()) {
                text += "</u>";
            }
            if (i == autShipL.size() - 1) {
                text += ". ";
            } else {
                text += ", ";
            }
        }

        return text;
    }

    public String printPubDesc(Publication pub) {
        String text = "";

        text += "<i>\"" + pub.getPubTitle() + ".\"</i> ";

        String groupType = "Typeless";

        String data = "";

        groupType = groupPubType(pub.getPubType().toString());

        switch (groupType) {
            case "Article":
                if (((ReadingCommitteeJournalPopularizationPaper) pub).getReaComConfPopPapJournal() != null) {
                    data = ((ReadingCommitteeJournalPopularizationPaper) pub).getReaComConfPopPapJournal().getJourName();
                    if (data != null) {
                        text += "In ";
                        text += data;
                        text += ", ";
                    }
                }
                data = ((ReadingCommitteeJournalPopularizationPaper) pub).getReaComConfPopPapVolume();
                if (data != null) {
                    text += "vol ";
                    text += data;
                    text += ", ";
                }
                data = ((ReadingCommitteeJournalPopularizationPaper) pub).getReaComConfPopPapPages();
                if (data != null) {
                    text += "pp. ";
                    text += data;
                    text += ", ";
                }
                break;

            case "Inproceedings":
                data = ((ProceedingsConference) pub).getProConfBookNameProceedings();
                if (data != null) {
                    text += "In ";
                    text += data;
                    text += ", ";
                }
                data = ((ProceedingsConference) pub).getProConfPages();
                if (data != null) {
                    text += "pp. ";
                    text += data;
                    text += ", ";
                }
                data = ((ProceedingsConference) pub).getProConfEditor();
                if (data != null) {
                    text += data;
                    text += ", ";
                }
                data = ((ProceedingsConference) pub).getProConfAddress();
                if (data != null) {
                    text += data;
                    text += ", ";
                }

                break;

            case "Book":
                data = ((Book) pub).getBookVolume();
                if (data != null) {
                    text += "vol. ";
                    text += data;
                    text += ", ";
                }
                data = ((Book) pub).getBookPages();
                if (data != null) {
                    text += "pp. ";
                    text += data;
                    text += ", ";
                }
                data = ((Book) pub).getBookEditor();
                if (data != null) {
                    text += data;
                    text += ", ";
                }
                data = ((Book) pub).getBookAddress();
                if (data != null) {
                    text += data;
                    text += ", ";
                }

                break;

            case "Inbook":
                data = ((BookChapter) pub).getBookVolume();
                if (data != null) {
                    text += "vol. ";
                    text += data;
                    text += ", ";
                }
                data = ((BookChapter) pub).getBookPages();
                if (data != null) {
                    text += "pp. ";
                    text += data;
                    text += ", ";
                }
                data = ((BookChapter) pub).getBookEditor();
                if (data != null) {
                    text += data;
                    text += ", ";
                }
                data = ((BookChapter) pub).getBookAddress();
                if (data != null) {
                    text += data;
                    text += ", ";
                }

                break;

            case "Misc":
                //No additional fields
                break;

            case "Manual":
                data = ((UserDocumentation) pub).getUserDocAddress();
                if (data != null) {
                    text += data;
                    text += ", ";
                }

                break;

            case "TechReport":
                //No additional fields
                break;

            case "Phdthesis":
                data = ((UniversityDocument) pub).getUniDocAddress();
                if (data != null) {
                    text += data;
                    text += ", ";
                }

                break;

            case "Masterthesis":
                data = ((UniversityDocument) pub).getUniDocAddress();
                if (data != null) {
                    text += data;
                    text += ", ";
                }

                break;
        }

        text += convertBackMonth(pub.getPubDate().toString().substring(5, 7)) + " ";
        text += pub.getPubDate().toString().substring(0, 4) + ".";

        return text;
    }

    public String printPubLinks(Publication pub) {
        String text = "";

        if (pub.getPubISBN() != null) {
            text += " ISBN: ";
            text += pub.getPubISBN();
            text += ".";
        }
        if (pub.getPubISSN() != null) {
            text += " ISSN: ";
            text += pub.getPubISSN();
            text += ".";
        }
        if (pub.getPubDOIRef() != null) {
            text += " DOI: ";
            text += "<a href=\"" + "http://dx.doi.org/" + pub.getPubDOIRef() + "\">";
            text += pub.getPubDOIRef();
            text += "</a>";
            text += ".";
        }

        return text;
    }

    public int countList(String[] pubL, int i, String toCount) {
        int pubId = Integer.parseInt(pubL[i].substring(0, pubL[i].indexOf(",")));
        Publication pub = getPublication(pubId).get(0);

        int initialCount = i;
        String counted;
        String curValue;

        switch (toCount) {
            case "date":
                counted = pub.getPubDate().toString().substring(0, 4);
                curValue = pub.getPubDate().toString().substring(0, 4);
                while (i < pubL.length && counted.compareTo(curValue) == 0) {
                    pubId = Integer.parseInt(pubL[i].substring(0, pubL[i].indexOf(",")));
                    pub = getPublication(pubId).get(0);
                    curValue = pub.getPubDate().toString().substring(0, 4);
                    i++;
                }
                break;

            case "type":
                counted = pub.getPubType().toString();
                curValue = pub.getPubType().toString();
                while (i < pubL.length && counted.compareTo(curValue) == 0) {
                    pubId = Integer.parseInt(pubL[i].substring(0, pubL[i].indexOf(",")));
                    pub = getPublication(pubId).get(0);
                    curValue = pub.getPubType().toString();
                    i++;
                }
                break;
        }

        int finalCount = i - initialCount - 1;

        return finalCount;
    }

    public void encodeFiles(Publication p) {
        byte[] input_file;
        byte[] encodedBytes;
        String fileString;
        //Check if the path exists, if it is not empty and if not, if it is a valid path
        if (p.getPubPDFPath() != null && !p.getPubPDFPath().isEmpty() && new File(p.getPubPDFPath()).exists()) {
            try {
                input_file = Files.readAllBytes(Paths.get(p.getPubPDFPath()));
                encodedBytes = Base64.getEncoder().encode(input_file);
                fileString = new String(encodedBytes);
                p.setPubPDFPath(fileString);
            } catch (IOException e) {
                p.setPubPDFPath("");
                e.printStackTrace();
                this.logger.error(e.getMessage(), e);
            }
        }
        if (p.getPubPaperAwardPath() != null && !p.getPubPaperAwardPath().isEmpty() && new File(p.getPubPaperAwardPath()).exists()) {
            try {
                input_file = Files.readAllBytes(Paths.get(p.getPubPaperAwardPath()));
                encodedBytes = Base64.getEncoder().encode(input_file);
                fileString = new String(encodedBytes);
                p.setPubPaperAwardPath(fileString);
            } catch (IOException e) {
                p.setPubPDFPath("");
                e.printStackTrace();
                this.logger.error(e.getMessage(), e);
            }
        }
    }

    public String exportPublicationsFromDataBase(String exportStart, String exportEnd, String exportContent) {

        int start = Integer.parseInt(exportStart);
        int end = Integer.parseInt(exportEnd);
        List<Publication> pubList = new LinkedList<Publication>();
        Calendar calendar = Calendar.getInstance();
        int pubYear = 0;
        int i = 0;
        Publication pub;
        String json = "";
        String export = "";

        if (exportContent.compareTo("all") == 0) //export all pubs
        {
            pubList.addAll(repo.findAll());
        }
        else if (exportContent.contains("org:")) //export all pubs of a given organization
        {
            pubList.addAll(repo.findDistinctByPubAutsAutAutOrgsResOrgResOrgId(Integer.parseInt(exportContent.substring(exportContent.indexOf("org:") + 4))));
        }
        else if (exportContent.contains("aut:")) //export all pubs of a given author
        {
            pubList.addAll(repo.findDistinctByPubAutsAutAutId(Integer.parseInt(exportContent.substring(exportContent.indexOf("aut:") + 4))));
        }
        else if (exportContent.contains("pub:")) //export a single pub
        {
            pubList.add(repo.findById(Integer.parseInt(exportContent.substring(exportContent.indexOf("pub:") + 4))).get());
        }

        while (i < pubList.size()) {
            pub = pubList.get(i);

            calendar.setTime(pub.getPubDate());
            pubYear = calendar.get(Calendar.YEAR);

            if (pubYear < start || pubYear > end) //check date conversion
            {
                pubList.remove(pub);
            } else {
                i++;
            }
        }

        for (final Publication p : pubList) { //Preventing infinite recursion
            for (Authorship autShip : p.getPubAuts()) {
                Author aut = autShip.getAut();
                autShip.setPub(null);
                if(aut != null) {
                    aut.setAutPubs(new LinkedList<>());
                    for (final Membership mem : aut.getAutOrgs()) {
                        mem.setAut(null);
                        mem.getResOrg().setOrgAuts(new HashSet<>());
                        //We assume we dont need suborg infos from pubs
                        mem.getResOrg().setOrgSubs(new HashSet<>());
                        mem.getResOrg().setOrgSup(null);
                    }
                }
            }
            if (p.getClass() == ReadingCommitteeJournalPopularizationPaper.class) //I guess I could have done a pubType check instead
            {
                if (((ReadingCommitteeJournalPopularizationPaper) p).getReaComConfPopPapJournal() != null)
                    ((ReadingCommitteeJournalPopularizationPaper) p).getReaComConfPopPapJournal().setJourPubs(new HashSet<>());
            }
        }

        //Before converting to json, sort the list by year/type
        //We assume it's fine to always sort by date and group by type regardless of how the page was sorted

        Collections.sort(pubList, new Comparator<Publication>() {
            @Override
            public int compare(Publication o1, Publication o2) {
                return -o1.getPubType().compareTo(o2.getPubType());
            }
        });

        Collections.sort(pubList, new Comparator<Publication>() {
            @Override
            public int compare(Publication o1, Publication o2) {
                return -Integer.compare(o1.getPubYear(), o2.getPubYear());
            }
        });

        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(pubList);
            export = exportPublications(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            this.logger.error(e.getMessage(), e);
        }

        return export;
    }

}
