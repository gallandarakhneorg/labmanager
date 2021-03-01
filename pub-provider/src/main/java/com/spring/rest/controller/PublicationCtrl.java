package com.spring.rest.controller;

import com.spring.rest.PubProviderApplication;
import com.spring.rest.entities.*;
import com.spring.rest.services.*;
import com.spring.rest.utils.FileUploadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
public class PublicationCtrl {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PublicationServ pubServ;
    @Autowired
    private BookChapterServ bookChapterServ;
    @Autowired
    private BookServ bookServ;
    @Autowired
    private EngineeringActivityServ engineeringActivityServ;
    @Autowired
    private JournalServ journalServ;
    @Autowired
    private ProceedingsConferenceServ proceedingsConferenceServ;
    @Autowired
    private ResearchOrganizationServ researchOrganizationServ;
    @Autowired
    private SeminarPatentInvitedConferenceServ seminarPatentInvitedConferenceServ;
    @Autowired
    private UniversityDocumentServ universityDocumentServ;
    @Autowired
    private UserDocumentationServ userDocumentationServ;
    @Autowired
    private ReadingCommitteeJournalPopularizationPaperServ readingCommitteeJournalPopularizationPaperServ;
    @Autowired
    private AuthorServ authorServ;

    //Get all entities
    @RequestMapping(value = "/getgetPublications", method = RequestMethod.GET, headers = "Accept=application/json")
    public List<Publication> getgetAllPublications() {
        return pubServ.getAllPublications();
    }

    //Get all entities
    @RequestMapping(value = "/getPublications", method = RequestMethod.POST, headers = "Accept=application/json")
    public List<Publication> getAllPublications() {
        return pubServ.getAllPublications();
    }

    //Get one specific entity based on its Id
    @RequestMapping(value = "/getPublication", method = RequestMethod.POST, headers = "Accept=application/json")
    public List<Publication> getPublication(int index) {
        return pubServ.getPublication(index);
    }

    //Get one specific entity based on its Id, translates the files.
    @RequestMapping(value = "/getPublicationForDownload", method = RequestMethod.POST, headers = "Accept=application/json")
    public List<Publication> getPublicationForDownload(int index) {
        return pubServ.getPublicationForDownload(index);
    }

    //Remove one specific entity based on its Id
    @RequestMapping(value = "/removePublication", method = RequestMethod.POST, headers = "Accept=application/json")
    public void removePublication(int index) {
        pubServ.removePublication(index);
    }
//
//    //Creates one specific entity based on its fields (minus its relationship fields)
//    //made it so that createXXXX gives the Id of the created publication back for conveniance
//    @Deprecated
//    //@RequestMapping(value = "/createPublication", method = RequestMethod.POST, headers = "Accept=application/json")
//    public int createPublication(String pubTitle, String pubAbstract,
//                                 String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
//                                 String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
//                                 String pubPaperAwardPath, PublicationType pubType) {
//        return pubServ.createPublication(pubTitle, pubAbstract,
//                pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN,
//                pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
//                pubPaperAwardPath, pubType);
//    }

    //Updates one specific entity based on its fields (minus its relationship fields)
    @RequestMapping(value = "/updatePublication", method = RequestMethod.POST, headers = "Accept=application/json")
    public void updatePublication(int pubId, String pubTitle, String pubAbstract,
                                  String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
                                  String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
                                  String pubPaperAwardPath, String pubType) {
        pubServ.updatePublication(pubId, pubTitle, pubAbstract,
                pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN,
                pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
                pubPaperAwardPath, pubType);
    }

    @RequestMapping(value = "/createPublication",
            method = RequestMethod.POST,
            headers = "Accept=application/json")
    public void createPublication(HttpServletResponse response,
                                  String publicationType,
                                  String publicationTitle,
                                  String publicationAbstract,
                                  String publicationKeywords,
                                  String publicationDate,
                                  String[] publicationAuthors,
                                  @RequestParam(required = false) String publicationNote,
                                  @RequestParam(required = false) String publicationIsbn,
                                  @RequestParam(required = false) String publicationIssn,
                                  @RequestParam(required = false) String publicationDoi,
                                  @RequestParam(required = false) String publicationUrl,
                                  @RequestParam(required = false) String publicationVideoUrl,
                                  @RequestParam(required = false) String publicationDblp,
                                  @RequestParam(required = false) MultipartFile publicationPdf,
                                  @RequestParam(required = false) MultipartFile publicationAward,
                                  @RequestParam(required = false) String publicationLanguage,
                                  @RequestParam(required = false) String reaComConfPopPapVolume,
                                  @RequestParam(required = false) String reaComConfPopPapNumber,
                                  @RequestParam(required = false) String reaComConfPopPapPages,
                                  @RequestParam(required = false) String proConfBookNameProceedings,
                                  @RequestParam(required = false) String proConfEditor,
                                  @RequestParam(required = false) String proConfPages,
                                  @RequestParam(required = false) String proConfOrganization,
                                  @RequestParam(required = false) String proConfPublisher,
                                  @RequestParam(required = false) String proConfAddress,
                                  @RequestParam(required = false) String proConfSeries,
                                  @RequestParam(required = false) String bookEditor,
                                  @RequestParam(required = false) String bookPublisher,
                                  @RequestParam(required = false) String bookVolume,
                                  @RequestParam(required = false) String bookSeries,
                                  @RequestParam(required = false) String bookAddress,
                                  @RequestParam(required = false) String bookEdition,
                                  @RequestParam(required = false) String bookPages,
                                  @RequestParam(required = false) String bookChapBookNameProceedings,
                                  @RequestParam(required = false) String bookChapNumberOrName,
                                  @RequestParam(required = false) String semPatHowPub,
                                  @RequestParam(required = false) String uniDocSchoolName,
                                  @RequestParam(required = false) String uniDocAddress,
                                  @RequestParam(required = false) String engActInstitName,
                                  @RequestParam(required = false) String engActReportType,
                                  @RequestParam(required = false) String engActNumber,
                                  @RequestParam(required = false) String userDocOrganization,
                                  @RequestParam(required = false) String userDocAddress,
                                  @RequestParam(required = false) String userDocEdition,
                                  @RequestParam(required = false) String userDocPublisher) throws IOException, ParseException {

        if(publicationAuthors == null) response.sendRedirect("/SpringRestHibernate/addPublication?error=1&message=Authors must be filled"); // Redirect on the same page
        try {
            PublicationTypeGroup publicationTypeGroup = PublicationTypeGroup.getPublicationTypeGroupFromPublicationType(PublicationType.valueOf(publicationType));
            Date publicationDateDate = new Date(new SimpleDateFormat("yyyy-MM-DD").parse(publicationDate).getTime());/*new Date(
                Integer.parseInt(publicationDate.split("-")[0]) - 1900,
                Integer.parseInt(publicationDate.split("-")[1]), 1
        );*/
            // Store pdfs
            String pdfUploadPath = "";
            if (publicationPdf != null && !publicationPdf.isEmpty()) {
                String publicationPdfPath = StringUtils.cleanPath(Objects.requireNonNull(publicationPdf.getOriginalFilename()));
                pdfUploadPath = PubProviderApplication.DownloadablesPath + "PDFs/" + publicationPdfPath;
                FileUploadUtils.saveFile(pdfUploadPath, publicationPdfPath, publicationPdf);
                logger.info("PDF uploaded at: " + pdfUploadPath);
            }
            String awardUploadPath = "";
            if (publicationAward != null && !publicationAward.isEmpty()) {
                String publicationAwardPath = StringUtils.cleanPath(Objects.requireNonNull(publicationAward.getOriginalFilename()));
                awardUploadPath = PubProviderApplication.DownloadablesPath + "Awards/" + publicationAwardPath;
                FileUploadUtils.saveFile(awardUploadPath, publicationAwardPath, publicationPdf);
                logger.info("Award uploaded at: " + awardUploadPath);
            }

            int pubId = 0;
            // First step : create the publication
            Publication publication = new Publication(publicationTitle, publicationAbstract, publicationKeywords, publicationDateDate, publicationNote, null, publicationIsbn, publicationIssn, publicationDoi, publicationUrl, publicationVideoUrl, publicationDblp, pdfUploadPath, publicationLanguage, awardUploadPath, PublicationType.valueOf(publicationType));
            pubId = publication.getPubId();

            // Second step : create the specific data of publication type
            switch (publicationTypeGroup) {
                case Typeless:
                    logger.error("Error during add publication: " + publicationType + " is not a valid type...");
                    response.sendRedirect("/SpringRestHibernate/addPublication?error=1"); // Redirect on the same page
                    return;
                case ReadingCommitteeJournalPopularizationPaper:
                    ReadingCommitteeJournalPopularizationPaper paper = readingCommitteeJournalPopularizationPaperServ.createReadingCommitteeJournalPopularizationPaper(
                            publication, reaComConfPopPapVolume, reaComConfPopPapNumber, reaComConfPopPapPages
                    );
                    pubId = paper.getPubId();
                    logger.info("ReadingCommitteeJournalPopularizationPaper created with id: " + paper.getPubId());
                    break;
                case ProceedingsConference:
                    ProceedingsConference proceedingsConference = proceedingsConferenceServ.createProceedingsConference(publication, proConfBookNameProceedings, proConfEditor, proConfPages, proConfOrganization, proConfPublisher, proConfAddress, proConfSeries);
                    pubId = proceedingsConference.getPubId();
                    logger.info("ProceedingsConference created with id: " + proceedingsConference.getPubId());
                    break;
                case Book:
                    Book book = bookServ.createBook(publication, bookEditor, bookPublisher, bookVolume, bookSeries, bookAddress, bookEdition, bookPages);
                    pubId = book.getPubId();
                    logger.info("Book created with id: " + book.getPubId());
                    break;
                case BookChapter:
                    Book book2 = new Book(publication, bookEditor, bookPublisher, bookVolume, bookSeries, bookAddress, bookEdition, bookPages);
                    logger.info("Book created with id: " + book2.getPubId());
                    BookChapter bookChapter = bookChapterServ.createBookChapter(publication, book2, bookChapBookNameProceedings, bookChapNumberOrName);
                    pubId = bookChapter.getPubId();
                    logger.info("BookChapter created with id: " + bookChapter.getPubId());
                    break;
                case SeminarPatentInvitedConference:
                    SeminarPatentInvitedConference seminarPatentInvitedConference = seminarPatentInvitedConferenceServ.createSeminarPatentInvitedConference(publication, semPatHowPub);
                    pubId = seminarPatentInvitedConference.getPubId();
                    logger.info("SeminarPatentInvitedConference created with id: " + seminarPatentInvitedConference.getPubId());
                    break;
                case UniversityDocument:
                    UniversityDocument universityDocument = universityDocumentServ.createUniversityDocument(publication, uniDocSchoolName, uniDocAddress);
                    pubId = universityDocument.getPubId();
                    logger.info("UniversityDocument created with id: " + universityDocument.getPubId());
                    break;
                case EngineeringActivity:
                    EngineeringActivity engineeringActivity = engineeringActivityServ.createEngineeringActivity(publication, engActInstitName, engActNumber, engActReportType);
                    pubId = engineeringActivity.getPubId();
                    logger.info("EngineeringActivity created with id: " + engineeringActivity.getPubId());
                    break;
                case UserDocumentation:
                    UserDocumentation userDocumentation = userDocumentationServ.createUserDocumentation(publication, userDocAddress, userDocEdition, userDocOrganization, userDocPublisher);
                    pubId = userDocumentation.getPubId();
                    logger.info("UserDocumentation created with id: " + userDocumentation.getPubId());
                    break;
            }

            int i = 0;
            // Third step create the authors and link them to the publication
            assert publicationAuthors != null;
            Set<String> authors = new LinkedHashSet<>(Arrays.asList(publicationAuthors)); // Unify
            for (String publicationAuthor : authors) {
                String firstName = publicationAuthor.substring(0, publicationAuthor.lastIndexOf(" "));
                String lastName = publicationAuthor.substring(publicationAuthor.lastIndexOf(" ")).replace(" ", "");

                int authorIdByName = authorServ.getAuthorIdByName(firstName, lastName);
                if (authorIdByName == 0) {
                    logger.info("Author " + publicationAuthor + " not found... Creating a new one.");
                    authorIdByName = authorServ.createAuthor(firstName, lastName, new Date(1970 - 1900, 1, 1), ""); //Temp birth date // TODO FIXME
                    logger.info("New author created with id: " + authorIdByName);
                }
                authorServ.addAuthorship(authorIdByName, pubId, i);
                i++;
                logger.info("Author " + publicationAuthor + " added as publication's author.");
            }

            // TODO change to works with wordpress
            response.sendRedirect("/SpringRestHibernate/addPublication?success=1");
        } catch (Exception ex) {
            // TODO change to works with wordpress
            response.sendRedirect("/SpringRestHibernate/addPublication?error=1&message=" + ex.getMessage()); // Redirect on the same page
        }

    }

    //Import a bibTex file to the database.
    //Returns a list of the IDs of the successfully imported publications.
    @RequestMapping(value = "/importPublications", method = RequestMethod.POST, headers = "Accept=application/json")
    public List<Integer> importPublications(String bibText) {
        return pubServ.importPublications(bibText);
    }

    //Export a bibTex file from a given json
    //The export contains a bibtex and an html export which can be extracted like in an html document
    @Deprecated
    @RequestMapping(value = "/exportPublications", method = RequestMethod.POST, headers = "Accept=application/json")
    public String exportPublications(String pubs) {
        if (pubs == null) {
            System.out.println("\n\nNo data received\n\n."); //This will pop up when the json is too big.
        }
        return pubServ.exportPublications(pubs);
    }


    //Export a bibTex file from the database
    //Exports publications released between exportStart & exportEnd. What is being exported depends on exportContent.
    //exportContent format type "all" or "org:1754" or "aut:1487"... With the number being the ID of the concerned org or aut
    //The export contains a bibtex and an html export which can be extracted like in an html document
    @Deprecated
    @RequestMapping(value = "/exportPublicationsFromDataBase", method = RequestMethod.POST, headers = "Accept=application/json")
    public String exportPublicationsFromDataBase(String exportStart, String exportEnd, String exportContent) {
        return pubServ.exportPublicationsFromDataBase(exportStart, exportEnd, exportContent);
    }

    //Get all entities in relation with one specific author
    @RequestMapping(value = "/getLinkedPublications", method = RequestMethod.POST, headers = "Accept=application/json")
    public List<Publication> getLinkedPublications(int index) {
        return pubServ.getLinkedPublications(index);
    }


    //Gets all the publications of all members affiliated with this organization or its subOrganizations
    @RequestMapping(value = "/getLinkedMembersPublications", method = RequestMethod.POST, headers = "Accept=application/json")
    public List<Publication> getLinkedMembersPublications(int index) {
        return pubServ.getLinkedMembersPublications(index);
    }

    /**
     * TMT 02/12/20
     * Export function for bibtex using a list of publication ids
     *
     * @param listPublicationsIds the array of publication id
     * @return the bibtex
     */
    @RequestMapping(value = "/exportBibtex", method = RequestMethod.POST, headers = "Accept=application/json")
    public String exportBibtex(Integer[] listPublicationsIds) {
        StringBuilder sb = new StringBuilder();
        for (Integer i : listPublicationsIds) {
            if (i == null) continue;
            try {
                sb.append(pubServ.exportOneBibTex(i));
            } catch (Exception ex) {
                this.logger.warn("Error during Bibtex export of publication ID=" + i);
            }
        }
        return sb.toString();
    }

    /**
     * TMT 02/12/20
     * Export function for html/odt using a list of publication ids
     *
     * @param listPublicationsIds the array of publication id
     * @return the html/odt
     */
    @RequestMapping(value = "/exportHtml", method = RequestMethod.POST, headers = "Accept=application/json")
    public String exportHtml(Integer[] listPublicationsIds) {
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        for (Integer i : listPublicationsIds) {
            if (i == null) continue;
            try {
                sb.append(pubServ.exportOneHtml(i));
            } catch (Exception ex) {
                this.logger.warn("Error during HTML export of publication ID=" + i);
            }
        }
        sb.append("</ul>");
        return sb.toString();
    }
}


	
