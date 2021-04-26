package fr.ciadlab.pubprovider.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.ciadlab.pubprovider.entities.*;
import fr.ciadlab.pubprovider.service.AuthorService;
import fr.ciadlab.pubprovider.service.JournalService;
import fr.ciadlab.pubprovider.service.PublicationService;
import fr.ciadlab.pubprovider.service.ResearchOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@CrossOrigin
public class MainController {

    @Autowired
    private ResearchOrganizationService resOrgService;
    @Autowired
    private PublicationService pubServ;
    @Autowired
    private AuthorService autServ;
    @Autowired
    private JournalService jourServ;

    @GetMapping("/authorsList")
    public ModelAndView showAuthorsList(
            @RequestParam(required = false) String organization,
            @RequestParam(required = false) Integer active,
            @RequestParam(required = false) String status
    ) {
        // Default values
        if (organization == null || organization.isEmpty()) organization = "CIAD";


        final ModelAndView modelAndView = new ModelAndView("authorsList");

        Optional<ResearchOrganization> researchOrganization = resOrgService.getResearchOrganizationByName(organization);
        if(researchOrganization.isPresent()) {
            List<Membership> members = resOrgService.getOrganizationMembers(researchOrganization.get(), active, status);

            // Add to view
            modelAndView.addObject("otherOrganisationsForMembers", resOrgService.getOtherOrganizationsForMembers(members, organization));
            modelAndView.addObject("members", members);
            modelAndView.addObject("uuid", Math.abs(new Random().nextInt())); // UUID to generate unique html elements
        }

        return modelAndView;
    }


    @GetMapping("/deletePublication")
    public void deletePublication(HttpServletResponse response, @RequestParam Integer publicationId) throws IOException {
        pubServ.removePublication(publicationId);
        response.sendRedirect("/SpringRestHibernate/publicationsListPrivate?success=1");
    }

    @GetMapping("/addPublication")
    public ModelAndView addPublication(@RequestParam(required = false) Integer publicationId) {
        final ModelAndView modelAndView = new ModelAndView("addPublication");
        Set<Author> authors = new HashSet<>();
        List<Journal> journals = jourServ.getAllJournals();

        resOrgService.getAllResearchOrganizations().forEach(o -> o.getOrgAuts().forEach(a -> authors.add(a.getAut() )));

        Map<PublicationType, String> publicationsTypes = new HashMap<>();
        for(PublicationType p : PublicationType.values()) {
            publicationsTypes.put(p, PublicationType.getPubTypeToString(p));
        }
        modelAndView.addObject("publicationsTypes", publicationsTypes);
        modelAndView.addObject("authors", authors);
        modelAndView.addObject("journals", journals);
        modelAndView.addObject("edit", false);

        // IF edit mode
        if(publicationId != null) {
            Publication publication = pubServ.getPublication(publicationId);
            if(publication != null) {
                modelAndView.addObject("publication", publication.getPublicationClass().cast(publication)); // Dynamic downcasting
                modelAndView.addObject("pubAuthors", autServ.getLinkedAuthors(publicationId)); // Dynamic downcasting
                switch(PublicationTypeGroup.getPublicationTypeGroupFromPublicationType(publication.getPubType())) {
                    case Typeless:
                        break;
                    case ReadingCommitteeJournalPopularizationPaper:
                        ReadingCommitteeJournalPopularizationPaper readingCommitteeJournalPopularizationPaper = (ReadingCommitteeJournalPopularizationPaper) publication;
                        modelAndView.addObject("reaComConfPopPapVolume", readingCommitteeJournalPopularizationPaper.getReaComConfPopPapVolume());
                        modelAndView.addObject("reaComConfPopPapNumber", readingCommitteeJournalPopularizationPaper.getReaComConfPopPapNumber());
                        modelAndView.addObject("reaComConfPopPapPages", readingCommitteeJournalPopularizationPaper.getReaComConfPopPapPages());
                        modelAndView.addObject("reaComConfPopPapJournal", readingCommitteeJournalPopularizationPaper.getReaComConfPopPapJournal());
                        break;
                    case ProceedingsConference:
                        ProceedingsConference proceedingsConference = (ProceedingsConference) publication;
                        modelAndView.addObject("proConfBookNameProceedings", proceedingsConference.getProConfBookNameProceedings());
                        modelAndView.addObject("proConfEditor", proceedingsConference.getProConfEditor());
                        modelAndView.addObject("proConfPages", proceedingsConference.getProConfPages());
                        modelAndView.addObject("proConfOrganization", proceedingsConference.getProConfOrganization());
                        modelAndView.addObject("proConfPublisher", proceedingsConference.getProConfPublisher());
                        modelAndView.addObject("proConfAddress", proceedingsConference.getProConfAddress());
                        modelAndView.addObject("proConfSeries", proceedingsConference.getProConfSeries());
                        break;
                    case Book:
                        Book book = (Book) publication;
                        modelAndView.addObject("bookEditor", book.getBookEditor());
                        modelAndView.addObject("bookPublisher", book.getBookPublisher());
                        modelAndView.addObject("bookVolume", book.getBookVolume());
                        modelAndView.addObject("bookSeries", book.getBookSeries());
                        modelAndView.addObject("bookAddress", book.getBookAddress());
                        modelAndView.addObject("bookEdition", book.getBookEdition());
                        modelAndView.addObject("bookPages", book.getBookPages());
                        break;
                    case BookChapter:
                        BookChapter bookChapter = (BookChapter) publication;
                        modelAndView.addObject("bookEditor", bookChapter.getBookEditor());
                        modelAndView.addObject("bookPublisher", bookChapter.getBookPublisher());
                        modelAndView.addObject("bookVolume", bookChapter.getBookVolume());
                        modelAndView.addObject("bookSeries", bookChapter.getBookSeries());
                        modelAndView.addObject("bookAddress", bookChapter.getBookAddress());
                        modelAndView.addObject("bookEdition", bookChapter.getBookEdition());
                        modelAndView.addObject("bookPages", bookChapter.getBookPages());
                        modelAndView.addObject("bookChapBookNameProceedings", bookChapter.getBookChapBookNameProceedings());
                        modelAndView.addObject("bookChapNumberOrName", bookChapter.getBookChapNumberOrName());
                        break;

                    case SeminarPatentInvitedConference:
                        SeminarPatentInvitedConference seminarPatentInvitedConference = (SeminarPatentInvitedConference) publication;
                        modelAndView.addObject("semPatHowPub", seminarPatentInvitedConference.getSemPatHowPub());
                        break;
                    case UniversityDocument:
                        UniversityDocument universityDocument = (UniversityDocument) publication;
                        modelAndView.addObject("uniDocSchoolName", universityDocument.getUniDocSchoolName());
                        modelAndView.addObject("uniDocAddress", universityDocument.getUniDocAddress());
                        break;
                    case EngineeringActivity:
                        EngineeringActivity engineeringActivity = (EngineeringActivity) publication;
                        modelAndView.addObject("engActInstitName", engineeringActivity.getEngActInstitName());
                        modelAndView.addObject("engActReportType", engineeringActivity.getEngActReportType());
                        modelAndView.addObject("engActNumber", engineeringActivity.getEngActNumber());
                        break;
                    case UserDocumentation:
                        UserDocumentation userDocumentation = (UserDocumentation) publication;
                        modelAndView.addObject("userDocOrganization", userDocumentation.getUserDocOrganization());
                        modelAndView.addObject("userDocAddress", userDocumentation.getUserDocAddress());
                        modelAndView.addObject("userDocEdition", userDocumentation.getUserDocEdition());
                        modelAndView.addObject("userDocPublisher", userDocumentation.getUserDocPublisher());
                        break;
                }

                modelAndView.addObject("edit", true);
            }
        }

        return modelAndView;
    }

    @GetMapping("/addPublicationFromBibtext")
    public ModelAndView addPublicationFromBibtext() {
        final ModelAndView modelAndView = new ModelAndView("addPublicationFromBibtext");
        return modelAndView;
    }


    @GetMapping("/publicationsList")
    public ModelAndView showPublicationsList(
            @RequestParam(required = false) Integer authorId
    ) {
        final ModelAndView modelAndView = new ModelAndView("publicationsList");

        modelAndView.addObject("authorsMap", autServ.getAllAuthors().parallelStream().collect(Collectors.toMap(a -> a.getAutId(), a -> a.getAutFirstName() + " " + a.getAutLastName())));

        if (authorId == null)
            modelAndView.addObject("url", "/SpringRestHibernate/getPublicationsList");
        else
            modelAndView.addObject("url", "/SpringRestHibernate/getPublicationsList?authorId=" + authorId);

        modelAndView.addObject("uuid", Math.abs(new Random().nextInt())); // UUID to generate unique html elements
        return modelAndView;
    }

    @GetMapping("/publicationsListPrivate")
    public ModelAndView showPublicationsListPrivate(
            @RequestParam(required = false) Integer authorId
    ) {
        final ModelAndView modelAndView = new ModelAndView("publicationsListPrivate");

        modelAndView.addObject("authorsMap", autServ.getAllAuthors().parallelStream().collect(Collectors.toMap(a -> a.getAutId(), a -> a.getAutFirstName() + " " + a.getAutLastName())));

        if (authorId == null)
            modelAndView.addObject("url", "/SpringRestHibernate/getPublicationsList?onlyValid=false");
        else
            modelAndView.addObject("url", "/SpringRestHibernate/getPublicationsList?authorId=" + authorId + "&onlyValid=false");

        modelAndView.addObject("uuid", Math.abs(new Random().nextInt())); // UUID to generate unique html elements
        return modelAndView;
    }

    @GetMapping("/getPublicationsList")
    @ResponseBody
    public String getPublicationsList(@RequestParam(required = false) Integer authorId, @RequestParam(required = false) Boolean onlyValid) {
        List<Publication> publications = authorId == null ? pubServ.getAllPublications() : pubServ.getAuthorPublications(authorId);
        if(onlyValid != null && !onlyValid) {
            // Do not filter
        }
        else {
            publications = publications.stream().filter(p -> p.getPubAuts() != null && p.getPubAuts().size() > 0).collect(Collectors.toList());
        }


        JsonArray publisJson = new JsonArray();
        for (Publication p : publications) { // Keep only valid (with authors)
            JsonObject data = new JsonObject();
            data.addProperty("id", p.getPubId());
            data.addProperty("title", pubServ.buildTitleHtml(p));
            data.addProperty("authors", pubServ.buildAuthorsHtml(p));
            data.addProperty("year", p.getPubYear());
            data.addProperty("type", PublicationType.getPubTypeToString(p.getPubType()));
            data.addProperty("links", pubServ.buildLinksHtml(p));
            data.addProperty("note", p.getPubNote());
            data.addProperty("keywords", p.getPubKeywords());

            String downloads = "";
            if (p.getPubPDFPath() != null && !p.getPubPDFPath().isEmpty())
                downloads += "<a class=\"btn btn-xs btn-success\" href=\"http://www.ciad-lab.fr/" + p.getPubPDFPath().replace("/var/www/ciad-lab.fr", "") + "\"><i class=\"fa fa-file-pdf-o\"></i>&nbsp;&nbsp;PDF</a>&nbsp;&nbsp;";
            if (p.getPubPaperAwardPath() != null && !p.getPubPaperAwardPath().isEmpty())
                downloads += "<a class=\"btn btn-xs btn-success\" href=\"http://www.ciad-lab.fr/" + p.getPubPaperAwardPath().replace("/var/www/ciad-lab.fr", "") + "\"><i class=\"fa fa-file-pdf-o\"></i>&nbsp;&nbsp;Award</a>&nbsp;&nbsp;";

            String exports = "";
            exports += "<a class=\"btn btn-xs btn-success btHtml\" href=\"\" data-href=\"" + p.getPubId() + "\"><i class=\"fa fa-file-text-o\"></i>&nbsp;&nbsp;HTML</a>&nbsp;&nbsp;";
            exports += "<a class=\"btn btn-xs btn-success btWord\" href=\"\" data-href=\"" + p.getPubId() + "\"><i class=\"fa fa-file-text-o\"></i>&nbsp;&nbsp;Odt</a>&nbsp;&nbsp;";
            exports += "<a class=\"btn btn-xs btn-success btBibtex\" href=\"\" data-href=\"" + p.getPubId() + "\"><i class=\"fa fa-file-code-o\"></i>&nbsp;&nbsp;Bibtex</a>&nbsp;&nbsp;";

            data.addProperty("exports", exports);
            data.addProperty("downloads", downloads);
            data.addProperty("abstract", p.getPubAbstract());

            String edit = "";
            edit += "<a class=\"btn btn-xs btn-success\" href=\"/SpringRestHibernate/addPublication?publicationId=" + p.getPubId() + "\" <i class=\"fa fa-edit\"></i>&nbsp;&nbsp;Edit publication</a>&nbsp;&nbsp;";
            edit += "<a class=\"btn btn-xs btn-danger\" href=\"/SpringRestHibernate/deletePublication?publicationId=" + p.getPubId() + "\" <i class=\"fa fa-delete\"></i>&nbsp;&nbsp;Delete publication</a>&nbsp;&nbsp;";
            //edit += "<a class=\"btn btn-xs btn-warning\" href=\"/SpringRestHibernate/mergePublication?publicationId=" + p.getPubId() + "\" <i class=\"fa fa-edit\"></i>&nbsp;&nbsp;Merge publications</a>&nbsp;&nbsp;";

            data.addProperty("edit", edit);
            publisJson.add(data);
        }

        JsonObject allPublisJson = new JsonObject();
        allPublisJson.add("data", publisJson);

        Gson gson = new Gson();
        return gson.toJson(allPublisJson);
    }

}
