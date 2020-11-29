package com.spring.rest.controller;

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
            modelAndView.addObject("uuid", new Random().nextInt()); // UUID to generate unique html elements
        }

        return modelAndView;
    }

    @GetMapping("/publicationsList")
    public ModelAndView showPublicationsList(
            @RequestParam(required = false) Integer authorId
    )  {
        final ModelAndView modelAndView = new ModelAndView("publicationsList");

        final List<Publication> publications = authorId == null ? pubServ.getAllPublications() : (List<Publication>) pubServ.getLinkedPublications(authorId);

        modelAndView.addObject("publications",publications);
        modelAndView.addObject("uuid", new Random().nextInt()); // UUID to generate unique html elements

        return modelAndView;
    }

}
