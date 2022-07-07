/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.pubprovider.controller;

import fr.ciadlab.pubprovider.entities.*;
import fr.ciadlab.pubprovider.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@RestController
@CrossOrigin
public class AuthorsController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PublicationService pubServ;
    @Autowired
    private BookChapterService bookChapterServ;
    @Autowired
    private BookService bookServ;
    @Autowired
    private EngineeringActivityService engineeringActivityServ;
    @Autowired
    private JournalService journalServ;
    @Autowired
    private ProceedingsConferenceService proceedingsConferenceServ;
    @Autowired
    private ResearchOrganizationService researchOrganizationServ;
    @Autowired
    private SeminarPatentInvitedConferenceService seminarPatentInvitedConferenceServ;
    @Autowired
    private UniversityDocumentService universityDocumentServ;
    @Autowired
    private UserDocumentationService userDocumentationServ;
    @Autowired
    private ReadingCommitteeJournalPopularizationPaperService readingCommitteeJournalPopularizationPaperServ;
    @Autowired
    private AuthorService authorServ;

    @GetMapping("/authorsTool")
    public ModelAndView showAuthorsTool() {
        final ModelAndView modelAndView = new ModelAndView("authorsTool");
        modelAndView.addObject("authors", authorServ.getAllAuthors());

        return modelAndView;
    }


    @RequestMapping(value = "/mergeAuthors", method = RequestMethod.POST)
    public void mergeAuthors(HttpServletResponse response, @RequestParam String publicationNewAuthor, @RequestParam String publicationOldAuthor) throws IOException {
        try {
            int pubCount = authorServ.mergeAuthors(publicationOldAuthor, publicationNewAuthor);
            response.sendRedirect("/SpringRestHibernate/authorsTool?success=" + pubCount);
        } catch (Exception ex) {
            response.sendRedirect("/SpringRestHibernate/authorsTool?error=1&message=" + ex.getMessage()); // Redirect on the same page
        }
    }
    
    @RequestMapping(value = "/mergeMultipleAuthors", method = RequestMethod.POST)
    public void mergeMultipleAuthors(HttpServletResponse response, @RequestParam String authorFirstName, @RequestParam String authorLastName, @RequestParam List<Integer> authorDuplicates) throws IOException {
        try {
            int pubCount = authorServ.mergeMultipleAuthors(authorFirstName, authorLastName, authorDuplicates);
            response.sendRedirect("/SpringRestHibernate/authorDuplicate?success=" + pubCount);
        } catch (Exception ex) {
            response.sendRedirect("/SpringRestHibernate/authorDuplicate?error=1&message=" + ex.getMessage()); // Redirect on the same page
        }
    }

    @RequestMapping(value = "/getAuthorData", method = RequestMethod.GET)
    public Author getAuthorData(@RequestParam String name) {
        final String oldFirstName = name.substring(0, name.indexOf(" "));
        final String oldLastName = name.substring(name.indexOf(" ")+1);
        return authorServ.getAuthor(authorServ.getAuthorIdByName(oldFirstName, oldLastName));
    }

    @RequestMapping(value = "/updateAuthor", method = RequestMethod.POST)
    public void updateAuthor(HttpServletResponse response, @RequestParam String author, @RequestParam String newFirstname, @RequestParam String newLastname , @RequestParam String autMail, @RequestParam Date newBirthDate) throws IOException {
        try {
            final String oldFirstName = author.substring(0, author.indexOf(" "));
            final String oldLastName = author.substring(author.indexOf(" ")+1);
            final int auteur = authorServ.getAuthorIdByName(oldFirstName, oldLastName);
           // authorServ.getAuthorIdByName()

            authorServ.updateAuthor(auteur,newFirstname,newLastname,newBirthDate,autMail);

            response.sendRedirect("/SpringRestHibernate/authorsTool?updated=true");
        } catch (Exception ex) {
            response.sendRedirect("/SpringRestHibernate/authorsTool?error=1&message=" + ex.getMessage()); // Redirect on the same page
        }
    }
    
    @RequestMapping(value = "/createAuthor", method = RequestMethod.POST)
    public void createAuthor(HttpServletResponse response, @RequestParam String autFirstName, @RequestParam String autLastName, @RequestParam Date autBirth, @RequestParam String autMail) throws IOException 
    {
    	try {
	    	authorServ.createAuthor(autFirstName, autLastName, autBirth, autMail);
	    	response.sendRedirect("/SpringRestHibernate/authorsTool?successCreate=" + autFirstName + " " + autLastName);
    	}
    	catch (Exception ex)
    	{
    		response.sendRedirect("/SpringRestHibernate/authorsToolCreate?error=1&message=" + ex.getMessage());
    	}
    }
    
    @RequestMapping(value = "/deleteAuthor", method = RequestMethod.POST)
    public void deleteAuthor(HttpServletResponse response, @RequestParam String author) throws IOException 
    {
    	try {
    		final String oldFirstName = author.substring(0, author.indexOf(" "));
            final String oldLastName = author.substring(author.indexOf(" ")+1);
            
            int index = authorServ.getAuthorIdByName(oldFirstName, oldLastName);
            
    		authorServ.removeAuthor(index);
	    	response.sendRedirect("/SpringRestHibernate/authorsTool?Deletesuccess=" + index);
    	}
    	catch (Exception ex)
    	{
    		response.sendRedirect("/SpringRestHibernate/authorsToolCreate?error=1&message=" + ex.getMessage());
    	}
    }
}


	
