package com.spring.rest.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.spring.rest.entities.*;
import com.spring.rest.services.PublicationServ;
import com.spring.rest.services.ResearchOrganizationServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@CrossOrigin
public class MainController {

    @Autowired
    private ResearchOrganizationServ resOrgServ;
    @Autowired
    private PublicationServ pubServ;

    @GetMapping("/authorsList")
    public ModelAndView showAuthorsList(
            @RequestParam(required = false) String organization,
            @RequestParam(required = false) Integer active,
            @RequestParam(required = false) String status
    )  {
        // Default values
        if(organization == null || organization.isEmpty()) organization = "CIAD";


        final ModelAndView modelAndView = new ModelAndView("authorsList");

        final List<ResearchOrganization> allResearchOrganizations = resOrgServ.getAllResearchOrganizations();
        final String finalOrganizationName = organization;
        final List<Membership> members = new ArrayList<>();

        // Get the CIAD organization
        Optional<ResearchOrganization> ciadResearchOrganization = allResearchOrganizations.parallelStream().filter(o -> o.getResOrgName().equals(finalOrganizationName)).findFirst();
        if(ciadResearchOrganization.isPresent()) {
            // Fill members
            members.addAll(ciadResearchOrganization.get().getOrgAuts());

            // Now filter members with parameters
            if(active != null) {
                if(active == 1) {
                    // Keep only active members : remove year > 1970 && < date.now
                    members.removeIf(m -> m.getMemToWhen().getYear() + 1900 > 1970 && m.getMemToWhen().before(new Date()));
                }
                else {
                    // Keep only non active members : remove year == 1970 && > date.now
                    members.removeIf(m -> m.getMemToWhen().getYear() + 1900 == 1970 || m.getMemToWhen().after(new Date()));
                }
            }
            else {
                // Keep all members
            }

            if(status != null) {
                // Filter by status
                if(status.equals("Teacher-Researchers")) {
                    // Keep only following status
                    members.removeIf(m ->
                            m.getMemStatus() !=  MemberStatus.MCF &&
                            m.getMemStatus() !=  MemberStatus.MCF_HDR &&
                            m.getMemStatus() !=  MemberStatus.LRU &&
                            m.getMemStatus() !=  MemberStatus.ECC &&
                            m.getMemStatus() !=  MemberStatus.PR
                    );
                }
                else {
                    members.removeIf(m -> m.getMemStatus() != MemberStatus.valueOf(status));
                }
            }
            else {
                // Keep all members
            }

            // Compute other organizations
            Map<Integer, List<ResearchOrganization>> otherOrganisationsForMembers = new HashMap<>();
            for(Membership member : members) {
                List<ResearchOrganization> otherOrgs = allResearchOrganizations.parallelStream().filter(o ->
                        o.getOrgAuts().stream().anyMatch(m -> m.getAut().getAutId() == member.getAut().getAutId()) &&
                                !o.getResOrgName().equals(finalOrganizationName)
                ).collect(Collectors.toList());
                otherOrganisationsForMembers.put(member.getMemId(), otherOrgs);
            }


            // Add to view
            modelAndView.addObject("otherOrganisationsForMembers",otherOrganisationsForMembers);
            modelAndView.addObject("members",members);
            modelAndView.addObject("uuid", Math.abs(new Random().nextInt())); // UUID to generate unique html elements
        }

        return modelAndView;
    }

    @GetMapping("/publicationsList")
    public ModelAndView showPublicationsList(
            @RequestParam(required = false) Integer authorId
    )  {
        final ModelAndView modelAndView = new ModelAndView("publicationsList");

        //final List<Publication> publications = authorId == null ? pubServ.getAllPublications() : (List<Publication>) pubServ.getLinkedPublications(authorId);
        //modelAndView.addObject("publications",publications);
        modelAndView.addObject("uuid", Math.abs(new Random().nextInt())); // UUID to generate unique html elements

        return modelAndView;
    }

    @GetMapping("/getPublicationsList")
    @ResponseBody
    public String getPublicationsList()  {
        final List<Publication> publications = pubServ.getAllPublications();


        JsonArray publisJson = new JsonArray();
        for(Publication p : publications) {
            JsonObject data = new JsonObject();
            data.addProperty("title", buildTitle(p));
            data.addProperty("authors",buildAuthors(p));
            data.addProperty("year", p.getPubYear());
            data.addProperty("type", p.getPubType().toString());
            data.addProperty("links", buildLinks(p));

            String downloads = "";
            if(p.getPubPDFPath() != null && !p.getPubPDFPath().isEmpty())
                downloads += "<a href=\"http://www.ciad-lab.fr/" + p.getPubPDFPath().replace("/var/www/ciad-lab.fr", "")  + "\">PDF</a>";
            if(p.getPubPaperAwardPath() != null && !p.getPubPaperAwardPath().isEmpty())
                downloads += "<a href=\"http://www.ciad-lab.fr/" + p.getPubPaperAwardPath().replace("/var/www/ciad-lab.fr", "")  + "\">Award</a>";

            data.addProperty("downloads", downloads);
            data.addProperty("abstract", p.getPubAbstract());
            publisJson.add(data);
        }

        JsonObject allPublisJson = new JsonObject();
        allPublisJson.add("data", publisJson);

        Gson gson = new Gson();
        return gson.toJson(allPublisJson);
    }

    private String buildTitle(Publication p) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href=\"http://www.ciad-lab.fr/publication/" + p.getPubId() + "\"><header2>" + p.getPubTitle() + "</header2></a>");
        String authors = buildAuthors(p);
        if(!authors.isEmpty()) sb.append("<header>" + buildAuthors(p) + ".</header><br/>");

        sb.append(buildDescription(p));
        return sb.toString();
    }

    private String buildAuthors(Publication p) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(Authorship a : p.getPubAuts()) {
            if(a.getAut().isHasPage()) {
                // Has a page
                sb.append("<a href=\"/author-" + a.getAut().getAutId() + "\">");
                sb.append(a.getAut().getAutFirstName());
                sb.append(" ");
                sb.append(a.getAut().getAutLastName());
                sb.append("</a>");
            }
            else {
                // Nope
                sb.append(a.getAut().getAutFirstName());
                sb.append(" ");
                sb.append(a.getAut().getAutLastName());
            }
            if(i < p.getPubAuts().size() - 2) {
                sb.append(", ");
            }
            if(i == p.getPubAuts().size() - 2) {
                sb.append(" and ");
            }
            i++;
        }
        return sb.toString();
    }

    private String buildLinks(Publication p) {
        StringBuilder sb = new StringBuilder();

        if(p.getPubISBN() != null && !p.getPubISBN().isEmpty()) sb.append("ISBN: " + p.getPubISBN() + "<br/>");
        if(p.getPubISSN() != null && !p.getPubISSN().isEmpty()) sb.append("ISSN: " + p.getPubISSN() + "<br/>");
        if(p.getPubDOIRef() != null && !p.getPubDOIRef().isEmpty()) {
            if(p.getPubDOIRef().contains("http://dx.doi.org/"))
            {
                sb.append("DOI: <a href=\"" + p.getPubDOIRef() + "\">" + p.getPubDOIRef().replace("http://dx.doi.org/", "") + "</a>");
            }
            else if(p.getPubDOIRef().contains("https://doi.org/"))
            {
                sb.append("DOI: <a href=\"" + p.getPubDOIRef() + "\">" + p.getPubDOIRef().replace("https://doi.org/", "") + "</a>");
            }
            else if(p.getPubDOIRef().contains("//doi.org/"))
            {
                sb.append("DOI: <a href=\"" + p.getPubDOIRef() + "\">" + p.getPubDOIRef().replace("//doi.org/", "") + "</a>");
            }
            else {
                sb.append("DOI: <a href=\"http://dx.doi.org/" + p.getPubDOIRef() + "\">" + p.getPubDOIRef() + "</a>");
            }
        }
        return sb.toString();
    }

    private String buildDescription(Publication p) {
        StringBuilder sb = new StringBuilder();
        switch(p.getPubType()) {
            case InternationalJournalWithReadingCommittee:
            case NationalJournalWithReadingCommittee:
            case InternationalJournalWithoutReadingCommittee:
            case NationalJournalWithoutReadingCommittee:
            case PopularizationPaper:                 //ReadingCommitteeJournalPopularizationPaper
                if(((ReadingCommitteeJournalPopularizationPaper)p).getReaComConfPopPapJournal() != null) {
                    if(((ReadingCommitteeJournalPopularizationPaper)p).getReaComConfPopPapJournal().getJourName() != null && !((ReadingCommitteeJournalPopularizationPaper)p).getReaComConfPopPapJournal().getJourName().isEmpty())
                        sb.append("In " + ((ReadingCommitteeJournalPopularizationPaper)p).getReaComConfPopPapJournal().getJourName() + ", ");
                }
                if(((ReadingCommitteeJournalPopularizationPaper)p).getReaComConfPopPapVolume() != null && !((ReadingCommitteeJournalPopularizationPaper)p).getReaComConfPopPapVolume().isEmpty())  sb.append("vol. " + ((ReadingCommitteeJournalPopularizationPaper)p).getReaComConfPopPapVolume() + ", ");
                if(((ReadingCommitteeJournalPopularizationPaper)p).getReaComConfPopPapPages() != null && !((ReadingCommitteeJournalPopularizationPaper)p).getReaComConfPopPapPages().isEmpty())  sb.append("pp. " + ((ReadingCommitteeJournalPopularizationPaper)p).getReaComConfPopPapPages() + ", ");
                break;
            case InternationalConferenceWithProceedings:
            case NationalConferenceWithProceedings:
            case InternationalConferenceWithoutProceedings:
            case NationalConferenceWithoutProceedings:                //ProceedingsConference
                if(((ProceedingsConference)p).getProConfBookNameProceedings() != null && !((ProceedingsConference)p).getProConfBookNameProceedings().isEmpty())  sb.append("In " + ((ProceedingsConference)p).getProConfBookNameProceedings() + ", ");
                if(((ProceedingsConference)p).getProConfPages() != null && !((ProceedingsConference)p).getProConfPages().isEmpty())  sb.append("pp. " + ((ProceedingsConference)p).getProConfPages() + ", ");
                if(((ProceedingsConference)p).getProConfEditor() != null && !((ProceedingsConference)p).getProConfEditor().isEmpty())  sb.append("pp. " + ((ProceedingsConference)p).getProConfEditor() + ", ");
                if(((ProceedingsConference)p).getProConfAddress() != null && !((ProceedingsConference)p).getProConfAddress().isEmpty())  sb.append("pp. " + ((ProceedingsConference)p).getProConfAddress() + ", ");
                break;
            case Book:
            case BookEdition://Book
                if(((Book)p).getBookVolume() != null && !((Book)p).getBookVolume().isEmpty())  sb.append("vol. " + ((Book)p).getBookVolume() + ", ");
                if(((Book)p).getBookPages() != null && !((Book)p).getBookPages().isEmpty())  sb.append("pp. " + ((Book)p).getBookPages() + ", ");
                if(((Book)p).getBookAddress() != null && !((Book)p).getBookAddress().isEmpty())  sb.append(((Book)p).getBookAddress() + ", ");
                break;
            case BookChapter:
            case VulgarizationBookChapter:
                //Book Chapter
                break;
            case InvitedConference:
            case Patent:
            case Seminar:
                // SeminarPatentInvitedConference
                break;
            case HDRThesis:
            case PHDThesis:
            case MasterOnResearch: //UniversityDocument
                if(((UniversityDocument)p).getUniDocAddress() != null && !((UniversityDocument)p).getUniDocAddress().isEmpty())  sb.append(((UniversityDocument)p).getUniDocAddress() + ", ");
                if(((UniversityDocument)p).getUniDocSchoolName() != null && !((UniversityDocument)p).getUniDocSchoolName().isEmpty())  sb.append(((UniversityDocument)p).getUniDocSchoolName() + ", ");
                break;
            default:
                break;
        }
        sb.append(p.getPubYear() + ".");
        return sb.toString();
    }

    /*function printPubDescription(pub)
    {
        var text="";

        //Journal or Conference name
        if(pub["proConfBookNameProceedings"])
        {
            text+="In ";
            text+= pub["proConfBookNameProceedings"];
            text+=", ";
        }
        if(pub["reaComConfPopPapJournal"] && pub["reaComConfPopPapJournal"]["jourName"]) //Should be a required field but just in case
        {
            text+="In ";
            text+=pub["reaComConfPopPapJournal"]["jourName"];
            text+=", ";
        }

        //Volume

        if(pub["bookVolume"])
        {
            text+="vol. ";
            text+= pub["bookVolume"];
            text+=", ";
        }
        if(pub["reaComConfPopPapVolume"])
        {
            text+="vol. ";
            text+= pub["reaComConfPopPapVolume"];
            text+=", ";
        }

        //Pages

        if(pub["bookPages"])
        {
            text+="pp. ";
            text+= pub["bookPages"];
            text+=", ";
        }
        if(pub["proConfPages"])
        {
            text+="pp. ";
            text+= pub["proConfPages"];
            text+=", ";
        }
        if(pub["reaComConfPopPapPages"])
        {
            text+="pp. ";
            text+= pub["reaComConfPopPapPages"];
            text+=", ";
        }

        //Editor

        if(pub["bookEditor"])
        {
            text+= pub["bookEditor"];
            text+=", ";
        }
        if(pub["proConfEditor"])
        {
            text+= pub["proConfEditor"];
            text+=", ";
        }

        //Address

        if(pub["bookAddress"])
        {
            text+= pub["bookAddress"];
            text+=", ";
        }
        if(pub["proConfAddress"])
        {
            text+= pub["proConfAddress"];
            text+=", ";
        }
        if(pub["uniDocAddress"])
        {
            text+= pub["uniDocAddress"];
            text+=", ";
        }
        if(pub["userDocAddress"])
        {
            text+= pub["uniDocAddress"];
            text+=", ";
        }
        //Date

        if(pub["pubDate"])
        {
            text+=formatDateToYear(pub["pubDate"]);;
            text+=". ";
        }

        return text;
    }
*/

}
