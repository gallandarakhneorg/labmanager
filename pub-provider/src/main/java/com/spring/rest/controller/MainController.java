package com.spring.rest.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.spring.rest.entities.*;
import com.spring.rest.services.AuthorServ;
import com.spring.rest.services.PublicationServ;
import com.spring.rest.services.ResearchOrganizationServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;


@Controller
@CrossOrigin
public class MainController {

    @Autowired
    private ResearchOrganizationServ resOrgServ;
    @Autowired
    private PublicationServ pubServ;
    @Autowired
    private AuthorServ autServ;

    @GetMapping("/authorsList")
    public ModelAndView showAuthorsList(
            @RequestParam(required = false) String organization,
            @RequestParam(required = false) Integer active,
            @RequestParam(required = false) String status
    ) {
        // Default values
        if (organization == null || organization.isEmpty()) organization = "CIAD";


        final ModelAndView modelAndView = new ModelAndView("authorsList");

        final List<ResearchOrganization> allResearchOrganizations = resOrgServ.getAllResearchOrganizations();
        final String finalOrganizationName = organization;
        final List<Membership> members = new ArrayList<>();

        // Get the CIAD organization
        Optional<ResearchOrganization> ciadResearchOrganization = allResearchOrganizations.parallelStream().filter(o -> o.getResOrgName().equals(finalOrganizationName)).findFirst();
        if (ciadResearchOrganization.isPresent()) {
            // Fill members
            members.addAll(ciadResearchOrganization.get().getOrgAuts());

            // Now filter members with parameters
            if (active != null) {
                if (active == 1) {
                    // Keep only active members : remove year > 1970 && < date.now
                    members.removeIf(m -> m.getMemToWhen().getYear() + 1900 > 1970 && m.getMemToWhen().before(new Date()));
                } else {
                    // Keep only non active members : remove year == 1970 && > date.now
                    members.removeIf(m -> m.getMemToWhen().getYear() + 1900 == 1970 || m.getMemToWhen().after(new Date()));
                }
            } else {
                // Keep all members
            }

            if (status != null) {
                // Filter by status
                if (status.equals("Teacher-Researchers")) {
                    // Keep only following status
                    members.removeIf(m ->
                            m.getMemStatus() != MemberStatus.MCF &&
                                    m.getMemStatus() != MemberStatus.MCF_HDR &&
                                    m.getMemStatus() != MemberStatus.LRU &&
                                    m.getMemStatus() != MemberStatus.ECC &&
                                    m.getMemStatus() != MemberStatus.PR
                    );
                } else {
                    members.removeIf(m -> m.getMemStatus() != MemberStatus.valueOf(status));
                }
            } else {
                // Keep all members
            }

            // Compute other organizations
            Map<Integer, List<ResearchOrganization>> otherOrganisationsForMembers = new HashMap<>();
            for (Membership member : members) {
                List<ResearchOrganization> otherOrgs = allResearchOrganizations.parallelStream().filter(o ->
                        o.getOrgAuts().stream().anyMatch(m -> m.getAut().getAutId() == member.getAut().getAutId()) &&
                                !o.getResOrgName().equals(finalOrganizationName)
                ).collect(Collectors.toList());
                otherOrganisationsForMembers.put(member.getMemId(), otherOrgs);
            }


            // Add to view
            modelAndView.addObject("otherOrganisationsForMembers", otherOrganisationsForMembers);
            modelAndView.addObject("members", members);
            modelAndView.addObject("uuid", Math.abs(new Random().nextInt())); // UUID to generate unique html elements
        }

        return modelAndView;
    }

    @GetMapping("/addPublication")
    public ModelAndView addPublication() {
        final ModelAndView modelAndView = new ModelAndView("addPublication");
        Set<Author> authors = new HashSet<>();

        resOrgServ.getAllResearchOrganizations().forEach(o -> o.getOrgAuts().forEach(a -> authors.add(a.getAut() )));

        modelAndView.addObject("authors", authors);
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

    @GetMapping("/getPublicationsList")
    @ResponseBody
    public String getPublicationsList(@RequestParam(required = false) Integer authorId) {
        final List<Publication> publications = authorId == null ? pubServ.getAllPublications() : pubServ.getLinkedPublications(authorId);


        JsonArray publisJson = new JsonArray();
        for (Publication p : publications.parallelStream().filter(p -> p.getPubAuts() != null && p.getPubAuts().size() > 0).collect(Collectors.toList())) { // Keep only valid (with authors)
            JsonObject data = new JsonObject();
            data.addProperty("id", p.getPubId());
            data.addProperty("title", p.buildTitleHtml());
            data.addProperty("authors", p.buildAuthorsHtml());
            data.addProperty("year", p.getPubYear());
            data.addProperty("type", p.getPubTypeToString());
            data.addProperty("links", p.buildLinksHtml());
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
            publisJson.add(data);
        }

        JsonObject allPublisJson = new JsonObject();
        allPublisJson.add("data", publisJson);

        Gson gson = new Gson();
        return gson.toJson(allPublisJson);
    }

}
