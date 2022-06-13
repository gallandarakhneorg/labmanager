package fr.ciadlab.pubprovider.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import fr.ciadlab.pubprovider.entities.Author;
import fr.ciadlab.pubprovider.entities.Book;
import fr.ciadlab.pubprovider.entities.BookChapter;
import fr.ciadlab.pubprovider.entities.CoreRanking;
import fr.ciadlab.pubprovider.entities.EngineeringActivity;
import fr.ciadlab.pubprovider.entities.Journal;
import fr.ciadlab.pubprovider.entities.Membership;
import fr.ciadlab.pubprovider.entities.ProceedingsConference;
import fr.ciadlab.pubprovider.entities.Publication;
import fr.ciadlab.pubprovider.entities.Quartile;
import fr.ciadlab.pubprovider.entities.PublicationType;
import fr.ciadlab.pubprovider.entities.PublicationsStat;
import fr.ciadlab.pubprovider.entities.ReadingCommitteeJournalPopularizationPaper;
import fr.ciadlab.pubprovider.entities.ResearchOrganization;
import fr.ciadlab.pubprovider.entities.SeminarPatentInvitedConference;
import fr.ciadlab.pubprovider.entities.UniversityDocument;
import fr.ciadlab.pubprovider.entities.UserDocumentation;
import fr.ciadlab.pubprovider.service.AuthorService;
import fr.ciadlab.pubprovider.service.JournalService;
import fr.ciadlab.pubprovider.service.PublicationService;
import fr.ciadlab.pubprovider.service.ResearchOrganizationService;
import info.debatty.java.stringsimilarity.SorensenDice;

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
            @RequestParam(required = false) String status) {
        // Default values
        if (organization == null || organization.isEmpty())
            organization = "CIAD";

        final ModelAndView modelAndView = new ModelAndView("authorsList");

        Optional<ResearchOrganization> researchOrganization = resOrgService.getResearchOrganizationByName(organization);
        if (researchOrganization.isPresent()) {
            List<Membership> members = resOrgService.getOrganizationMembers(researchOrganization.get(), active, status);

            // Add to view
            modelAndView.addObject("otherOrganisationsForMembers",
                    resOrgService.getOtherOrganizationsForMembers(members, organization));
            modelAndView.addObject("members", members);
            modelAndView.addObject("uuid", Math.abs(new Random().nextInt())); // UUID to generate unique html elements
        }

        return modelAndView;
    }

    @GetMapping("/deletePublication")
    public void deletePublication(HttpServletResponse response, @RequestParam Integer publicationId)
            throws IOException {
        pubServ.removePublication(publicationId);
        response.sendRedirect("/SpringRestHibernate/publicationsListPrivate?success=1");
    }

    @GetMapping("/addPublication")
    public ModelAndView addPublication(HttpServletRequest request
    								  ,HttpServletResponse response
    								  ,@RequestParam(required = false) boolean filling
    								  ,@RequestParam(required = false) Integer publicationId) throws IOException 
    {
    	ModelAndView modelAndView = null;
    	try
    	{
    		
    		Publication publication = null;
    		List<Publication> pubL;
	    	if(filling == true)
	    	{
	    		Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
	        	if(inputFlashMap != null)
	            {
	            	String bibtex = (String)inputFlashMap.get("bibtex");
	            	PublicationService pubServ = (PublicationService)inputFlashMap.get("pubServ");
	            	pubL = pubServ.BibTexToPublication(bibtex, false);
	            	//TODO: for now, we just take the first bibtex
	            	publication = pubL.get(0);
	            }
	            else
	            {
	            	throw new Exception("This bibtex does not fit with the publication filling.");
	            }
	        	
	    	}
	    	else if(publicationId != null)
	    	{
	    		publication = pubServ.getPublication(publicationId);
	    	}
	    	
	        modelAndView = new ModelAndView("addPublication");
	        Set<Author> authors = new HashSet<>();
	        List<Journal> journals = jourServ.getAllJournals();
	
	        resOrgService.getAllResearchOrganizations().forEach(o -> o.getOrgAuts().forEach(a -> authors.add(a.getAut())));
	
	        List<PublicationType> publicationsTypes = Arrays.asList(PublicationType.values()).stream()
	        		.filter(pubType -> pubType != PublicationType.TypeLess)
	        		.collect(Collectors.toList());
	        
	        List<Quartile> publicationsQuartiles = Arrays.asList(Quartile.values()).stream()
	                .collect(Collectors.toList());

	        List<CoreRanking> jCoreRankings = Arrays.asList(CoreRanking.values()).stream().collect(Collectors.toList());
	        modelAndView.addObject("publicationsTypes", publicationsTypes);
	        modelAndView.addObject("publicationsQuartiles", publicationsQuartiles);
	        modelAndView.addObject("journalCoreRankings", jCoreRankings);
	        modelAndView.addObject("authors", authors);
	        modelAndView.addObject("journals", journals);
	        modelAndView.addObject("journalServ", jourServ);
	        modelAndView.addObject("edit", false);
	        

	        // IF edit mode
	        if(publicationId != null || filling == true) 
	        {
	            if(publication != null) 
	            {
	            	if(publicationId != null)
	            	{
	            		modelAndView.addObject("pubAuthors", autServ.getLinkedAuthors(publicationId)); // Dynamic downcasting
	            		modelAndView.addObject("edit", true);
	            	}
	            	else if(filling == true)
	            	{
	            		modelAndView.addObject("pubAuts", publication.getAuthorsList());
	            	}
	                modelAndView.addObject("publication", publication.getPublicationClass().cast(publication)); // Dynamic downcasting
	                
	                switch(publication.getPubType().getPublicationTypeGroupFromPublicationType()) 
	                {
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
	            }
	        }
	    }
	    catch(Exception e)
	    {
	    	response.sendRedirect("/SpringRestHibernate/addPublication?error=1&publicationId=" + e.getMessage()); // Redirect on the same page
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
            @RequestParam(required = false) Integer authorId) {
        final ModelAndView modelAndView = new ModelAndView("publicationsList");
        
        modelAndView.addObject("authorsMap", autServ.getAllAuthors().parallelStream()
                .collect(Collectors.toMap(a -> a.getAutId(), a -> a.getAutFirstName() + " " + a.getAutLastName())));

        if (authorId == null)
            modelAndView.addObject("url", "/SpringRestHibernate/getPublicationsList");
        else
            modelAndView.addObject("url", "/SpringRestHibernate/getPublicationsList?authorId=" + authorId);

        modelAndView.addObject("uuid", Math.abs(new Random().nextInt())); // UUID to generate unique html elements
        return modelAndView;
    }

    @GetMapping("/publicationsListLight")
    public ModelAndView showPublicationsListLight(
            @RequestParam Integer id) {
        final ModelAndView modelAndView = new ModelAndView("publicationsListLight");
        modelAndView.addObject("url", "/SpringRestHibernate/getPublicationsList?authorId=" + id);
        modelAndView.addObject("uuid", Math.abs(new Random().nextInt())); // UUID to generate unique html elements
        return modelAndView;
    }

    @GetMapping("/publicationsListPrivate")
    public ModelAndView showPublicationsListPrivate(
            @RequestParam(required = false) Integer authorId) {
        final ModelAndView modelAndView = new ModelAndView("publicationsListPrivate");

        modelAndView.addObject("authorsMap", autServ.getAllAuthors().parallelStream()
                .collect(Collectors.toMap(a -> a.getAutId(), a -> a.getAutFirstName() + " " + a.getAutLastName())));

        if (authorId == null)
            modelAndView.addObject("url", "/SpringRestHibernate/getPublicationsList?onlyValid=false");
        else
            modelAndView.addObject("url",
                    "/SpringRestHibernate/getPublicationsList?authorId=" + authorId + "&onlyValid=false");

        modelAndView.addObject("uuid", Math.abs(new Random().nextInt())); // UUID to generate unique html elements
        return modelAndView;
    }

    @GetMapping("/publicationsStats")
    public ModelAndView showPublicationsStats(
            @RequestParam(required = false) Integer id) {
        final ModelAndView modelAndView = new ModelAndView("publicationsStats");

        List<Publication> publicationList;
        if (id != null) {
            publicationList = pubServ.getAuthorPublications(id);
        } else {
            publicationList = pubServ.getAllPublications();
        }
        publicationList = publicationList.stream().filter(p -> p.getPubAuts() != null && p.getPubAuts().size() > 0)
                .collect(Collectors.toList());

        Map<Integer, PublicationsStat> publicationsStats = new TreeMap<>();
        PublicationsStat globalStats = new PublicationsStat(0);
        for (Publication p : publicationList) {
            if (!publicationsStats.containsKey(p.getPubYear())) {
                publicationsStats.put(p.getPubYear(), new PublicationsStat(p.getPubYear()));
            }
            PublicationsStat publicationsStat = publicationsStats.get(p.getPubYear());
            switch (p.getPubType().getPublicationTypeGroupFromPublicationType()) {
                case Typeless:
                    publicationsStat.setOtherCount(publicationsStat.getOtherCount() + 1);
                    globalStats.setOtherCount(globalStats.getOtherCount() + 1);
                    break;
                case ReadingCommitteeJournalPopularizationPaper:
                    publicationsStat.setReadingCommitteeJournalPopularizationPaperCount(
                            publicationsStat.getReadingCommitteeJournalPopularizationPaperCount() + 1);
                    globalStats.setReadingCommitteeJournalPopularizationPaperCount(
                            globalStats.getReadingCommitteeJournalPopularizationPaperCount() + 1);
                    break;
                case ProceedingsConference:
                    publicationsStat
                            .setProceedingsConferenceCount(publicationsStat.getProceedingsConferenceCount() + 1);
                    globalStats.setProceedingsConferenceCount(globalStats.getProceedingsConferenceCount() + 1);
                    break;
                case Book:
                    publicationsStat.setBookCount(publicationsStat.getBookCount() + 1);
                    globalStats.setBookCount(globalStats.getBookCount() + 1);
                    break;
                case BookChapter:
                    publicationsStat.setBookChapterCount(publicationsStat.getBookChapterCount() + 1);
                    globalStats.setBookChapterCount(globalStats.getBookChapterCount() + 1);
                    break;
                case SeminarPatentInvitedConference:
                    publicationsStat.setSeminarPatentInvitedConferenceCount(
                            publicationsStat.getSeminarPatentInvitedConferenceCount() + 1);
                    globalStats.setSeminarPatentInvitedConferenceCount(
                            globalStats.getSeminarPatentInvitedConferenceCount() + 1);
                    break;
                case UniversityDocument:
                    publicationsStat.setUniversityDocumentCount(publicationsStat.getUniversityDocumentCount() + 1);
                    globalStats.setUniversityDocumentCount(globalStats.getUniversityDocumentCount() + 1);
                    break;
                case EngineeringActivity:
                    publicationsStat.setEngineeringActivityCount(publicationsStat.getEngineeringActivityCount() + 1);
                    globalStats.setEngineeringActivityCount(globalStats.getEngineeringActivityCount() + 1);
                    break;
                case UserDocumentation:
                    publicationsStat.setUserDocumentationCount(publicationsStat.getUserDocumentationCount() + 1);
                    globalStats.setUserDocumentationCount(globalStats.getUserDocumentationCount() + 1);
                    break;
            }
        }

        modelAndView.addObject("stats", publicationsStats);
        modelAndView.addObject("globalStats", globalStats);
        return modelAndView;
    }

    @GetMapping("/getPublicationsList")
    @ResponseBody
    public String getPublicationsList(@RequestParam(required = false) Integer authorId,
            @RequestParam(required = false) Boolean onlyValid) {
        List<Publication> publications = authorId == null ? pubServ.getAllPublications()
                : pubServ.getAuthorPublications(authorId);
        if (onlyValid != null && !onlyValid) {
            // Do not filter
        } else {
            publications = publications.stream().filter(p -> p.getPubAuts() != null && p.getPubAuts().size() > 0)
                    .collect(Collectors.toList());
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

            // addition of publication's journal quality indicators, if they exist
            if (p.getPubJournalScimagoQuartile() != null)
                data.addProperty("scimagoQuartile", p.getPubJournalScimagoQuartile().toString());
            if (p.getPubJournalWosQuartile() != null)
                data.addProperty("wosQuartile", p.getPubJournalWosQuartile().toString());
            if (p.getPubJournalCoreRanking() != null)
                data.addProperty("coreRanking",
                        p.getPubJournalCoreRanking().toString());
            if (p.getPubJournalImpactFactor() != 0)
                data.addProperty("impactFactor",
                        p.getPubJournalImpactFactor());

            String downloads = "";
            if (p.getPubPDFPath() != null && !p.getPubPDFPath().isEmpty())
                downloads += "<a class=\"btn btn-xs btn-success\" href=\"http://www.ciad-lab.fr/"
                        + p.getPubPDFPath().replace("/var/www/ciad-lab.fr", "")
                        + "\"><i class=\"fa fa-file-pdf-o\"></i>&nbsp;&nbsp;PDF</a>&nbsp;&nbsp;";
            if (p.getPubPaperAwardPath() != null && !p.getPubPaperAwardPath().isEmpty())
                downloads += "<a class=\"btn btn-xs btn-success\" href=\"http://www.ciad-lab.fr/"
                        + p.getPubPaperAwardPath().replace("/var/www/ciad-lab.fr", "")
                        + "\"><i class=\"fa fa-file-pdf-o\"></i>&nbsp;&nbsp;Award</a>&nbsp;&nbsp;";

            String exports = "";
            exports += "<a class=\"btn btn-xs btn-success btHtml\" href=\"\" data-href=\"" + p.getPubId()
                    + "\"><i class=\"fa fa-file-text-o\"></i>&nbsp;&nbsp;HTML</a>&nbsp;&nbsp;";
            exports += "<a class=\"btn btn-xs btn-success btWord\" href=\"\" data-href=\"" + p.getPubId()
                    + "\"><i class=\"fa fa-file-text-o\"></i>&nbsp;&nbsp;Odt</a>&nbsp;&nbsp;";
            exports += "<a class=\"btn btn-xs btn-success btBibtex\" href=\"\" data-href=\"" + p.getPubId()
                    + "\"><i class=\"fa fa-file-code-o\"></i>&nbsp;&nbsp;Bibtex</a>&nbsp;&nbsp;";

            data.addProperty("exports", exports);
            data.addProperty("downloads", downloads);
            data.addProperty("abstract", p.getPubAbstract());

            String edit = "";
            edit += "<a class=\"btn btn-xs btn-success\" href=\"/SpringRestHibernate/addPublication?publicationId="
                    + p.getPubId() + "\" <i class=\"fa fa-edit\"></i>&nbsp;&nbsp;Edit publication</a>&nbsp;&nbsp;";
            edit += "<a class=\"btn btn-xs btn-danger\" href=\"/SpringRestHibernate/deletePublication?publicationId="
                    + p.getPubId() + "\" <i class=\"fa fa-delete\"></i>&nbsp;&nbsp;Delete publication</a>&nbsp;&nbsp;";
            // edit += "<a class=\"btn btn-xs btn-warning\"
            // href=\"/SpringRestHibernate/mergePublication?publicationId=" + p.getPubId() +
            // "\" <i class=\"fa fa-edit\"></i>&nbsp;&nbsp;Merge
            // publications</a>&nbsp;&nbsp;";

            data.addProperty("edit", edit);
            publisJson.add(data);
        }

        JsonObject allPublisJson = new JsonObject();
        allPublisJson.add("data", publisJson);

        Gson gson = new Gson();
        return gson.toJson(allPublisJson);
    }

    @GetMapping("/authorDuplicate")
    public ModelAndView authorDuplicate() {
        final ModelAndView modelAndView = new ModelAndView("authorDuplicate");

        // Each list represents a group of authors that could be duplicate
        List<ArrayList<Author>> matchingAuthors = new ArrayList<ArrayList<Author>>();

        List<Author> authorsList = this.autServ.getAllAuthors();

        for (Iterator<Author> iterator = authorsList.iterator(); iterator.hasNext();) {
            Author author = iterator.next();
            iterator.remove();
            ArrayList<Author> currentAuthorMatch = new ArrayList<Author>();
            currentAuthorMatch.add(author);

            for (Author author2 : authorsList) {
                if (isAuthorSimilar(author, author2)) {
                    currentAuthorMatch.add(author2);
                }
            }
            if (currentAuthorMatch.size() > 1) {
                boolean addToMatchingAuthors = true;
                // check if the current group of similar authors is not a sub-group of another
                // group of similar authors
                for (ArrayList<Author> authors : matchingAuthors) {
                    if (authors.containsAll(currentAuthorMatch)) {
                        addToMatchingAuthors = false;
                        break;
                    }
                }
                if (addToMatchingAuthors) {
                    matchingAuthors.add(currentAuthorMatch);
                }
            }
        }

        modelAndView.addObject("matchingAuthors", matchingAuthors);
        return modelAndView;
    }

    /**
     * Check name similarity between two authors using Sorensen Dice algorithm
     * 
     * @param author  first author
     * @param author2 second author
     * @return true if similar, false if not
     */
    private boolean isAuthorSimilar(Author author, Author author2) {
        String authorFullName = author.getAutFirstName() + " " + author.getAutLastName();
        String author2FullName = author2.getAutFirstName() + " " + author2.getAutLastName();

        SorensenDice stringMatcher = new SorensenDice();

        // First check on the full name else on the individual strings
        if (stringMatcher.distance(authorFullName, author2FullName) <= 0.3) {
            return true;
        } else if (stringMatcher.distance(authorFullName, author2FullName) <= 0.6) {
            String authorFirstName = author.getAutFirstName().replace(".", "");
            String authorLastName = author.getAutLastName().replace(".", "");
            String author2FirstName = author2.getAutFirstName().replace(".", "");
            String author2LastName = author2.getAutLastName().replace(".", "");
            // Check with possibility of shortened name and inversion
            return isAuthorSimilarWithShortenedName(authorFirstName, authorLastName, author2FirstName, author2LastName)
                    || isAuthorSimilarWithShortenedName(authorFirstName, authorLastName, author2LastName,
                            author2FirstName);
        }
        return false;
    }

    /**
     * Check similarity with a part of the name shortened
     * ex: C. Durand / Christophe Durand
     * 
     * @param authorFirstName  first author string
     * @param authorLastName   first author string
     * @param author2FirstName second author string
     * @param author2LastName  second author string
     * @return true if similar, false if not
     */
    private boolean isAuthorSimilarWithShortenedName(String authorFirstName, String authorLastName,
            String author2FirstName, String author2LastName) {
        SorensenDice stringMatcher = new SorensenDice();

        String longestFirstName, smallestFirstName, longestLastName, smallestLastName;

        // Find smallest first name
        if (authorFirstName.length() > author2FirstName.length()) {
            longestFirstName = authorFirstName;
            smallestFirstName = author2FirstName;
        } else {
            longestFirstName = author2FirstName;
            smallestFirstName = authorFirstName;
        }

        double lastNameSimilarity = stringMatcher.distance(authorLastName, author2LastName);

        if (smallestFirstName.equals(longestFirstName.substring(0, smallestFirstName.length()))
                && lastNameSimilarity <= 0.4) {
            return true;
        }

        // Find smallest last name
        if (authorLastName.length() > author2LastName.length()) {
            longestLastName = authorLastName;
            smallestLastName = author2LastName;
        } else {
            longestLastName = author2LastName;
            smallestLastName = authorLastName;
        }

        double firstNameSimilarity = stringMatcher.distance(authorFirstName, author2FirstName);

        if (smallestLastName.equals(longestLastName.substring(0, smallestLastName.length()))
                && firstNameSimilarity <= 0.4) {
            return true;
        }
        return false;
    }

}
