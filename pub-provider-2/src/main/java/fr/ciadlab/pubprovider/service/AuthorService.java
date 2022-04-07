package fr.ciadlab.pubprovider.service;

import fr.ciadlab.pubprovider.entities.*;
import fr.ciadlab.pubprovider.repository.AuthorRepository;
import fr.ciadlab.pubprovider.repository.AuthorshipRepository;
import fr.ciadlab.pubprovider.repository.MembershipRepository;
import fr.ciadlab.pubprovider.repository.PublicationRepository;
import fr.ciadlab.pubprovider.repository.ResearchOrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;

@Service
public class AuthorService {

    //Not used so Ill comment it to prevent warnings
    //private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PublicationRepository pubRepo;

    @Autowired
    private AuthorshipRepository autShipRepo;

    @Autowired
    private AuthorRepository repo;

    @Autowired
    private ResearchOrganizationRepository orgRepo;
    
    @Autowired
    private MembershipRepository memRepo;

    public List<Author> getAllAuthors() {
        return repo.findAll();
    }

    public Author getAuthor(int index) {
        Optional<Author> byId = repo.findById(index);
        return byId.orElse(null);
    }

    public void removeAuthor(int index) {
        final Optional<Author> res = repo.findById(index);
        if (res.isPresent()) {

            //When removing an author, reduce the ranks of the other authorships for the pubs he made.
            for (Authorship autShip : res.get().getAutPubs()) {
                int rank = autShip.getAutShipRank();
                Optional<Publication> pub = pubRepo.findById(autShip.getPubPubId());
                if(pub.isPresent()){
                    for (Authorship autShipPub : pub.get().getPubAuts()) {
                        if (autShipPub.getAutShipRank() > rank) {
                            autShipPub.setAutShipRank(autShipPub.getAutShipRank() - 1);
                        }
                        autShipRepo.save(autShipPub);
                    }
                }

            }

            repo.deleteById(index); //mem & autship gets deleted by cascade
        }
    }

    public int createAuthor(String autFirstName, String autLastName, Date autBirth, String autMail) {
        final Author res = new Author();
        res.setAutFirstName(autFirstName);
        res.setAutLastName(autLastName);
        res.setAutBirth(autBirth);
        res.setAutMail(autMail);
        this.repo.save(res);

        return res.getAutId();
    }

    public void updateAuthor(int index, String autFirstName, String autLastName, Date autBirth, String autMail) {
        final Optional<Author> res = this.repo.findById(index);
        if (res.isPresent()) {
            if (!autFirstName.isEmpty())
                res.get().setAutFirstName(autFirstName);
            if (!autLastName.isEmpty())
                res.get().setAutLastName(autLastName);
            if (autBirth != null)
                res.get().setAutBirth(autBirth);
            if (autMail != null)
                res.get().setAutMail(autMail);

            this.repo.save(res.get());
        }
    }

    public void updateAuthorship(int index, int pubId, int rank) {
        final Optional<Authorship> autShip = autShipRepo.findDistinctByAutAutIdAndPubPubId(index, pubId);

        //Need to add on both sides but only one save is needed.
        //Ill still do both in case
        if (autShip.isPresent()) {
            autShip.get().setAutShipRank(rank);
            this.autShipRepo.save(autShip.get());
        }
    }

    public void addAuthorship(int index, int pubId) {
        addAuthorship(index, pubId,repo.findByAutPubsPubPubIdOrderByAutPubsAutShipRank(pubId).size());
    }

    public void addAuthorship(int index, int pubId, int rank) {
        final Optional<Author> author = repo.findById(index);
        final Optional<Publication> publication = pubRepo.findById(pubId);

        //Need to add on both sides but only one save is needed.
        //Ill still do both in case
        if (author.isPresent() && publication.isPresent()) {
            if (Collections.disjoint(author.get().getAutPubs(), publication.get().getPubAuts())) {
                Authorship autShip = new Authorship();
                autShip.setPubPubId(publication.get().getPubId());
                autShip.setAutAutId(author.get().getAutId());

                autShip.setAutShipRank(rank);
                this.autShipRepo.save(autShip);
            }
        }
    }

    public void removeAuthorship(int index, int pubId) {
        final Optional<Author> author = repo.findById(index);
        final Optional<Publication> publication = pubRepo.findById(pubId);

        if (author.isPresent() && publication.isPresent()) {
            publication.get().getPubAuts().remove(author.get()); //TODO FIXME
        }

        Optional<Authorship> autShip = autShipRepo.findDistinctByAutAutIdAndPubPubId(index, pubId);

        if (autShip.isPresent()) {
            int rank = autShip.get().getAutShipRank();
            Optional<Publication> pub = pubRepo.findById(autShip.get().getPubPubId());
            if(pub.isPresent()) {
                for (Authorship autShipPub : pub.get().getPubAuts()) {
                    if (autShipPub.getAutShipRank() > rank) {
                        autShipPub.setAutShipRank(autShipPub.getAutShipRank() - 1);
                    }
                    autShipRepo.save(autShipPub);
                }
            }

            autShipRepo.deleteByAutAutIdAndPubPubId(index, pubId);
            //autShipRepo.deleteById(autShip.get().getAutShipId()); //TMT 24/11/20 new delete method
            autShipRepo.flush();
            //autShipRepo.deleteByAutAutIdAndPubPubId(index, pubId);
        }

    }

    public void updateAuthorPage(int index, boolean hasPage) {
        final Optional<Author> res = this.repo.findById(index);
        if (res.isPresent()) {
            res.get().setHasPage(hasPage);
            repo.save(res.get());
        }
    }

    public Set<Author> getLinkedMembers(int index) {
        final Optional<ResearchOrganization> org = orgRepo.findById(index);
        Set<Author> autL = new HashSet<Author>();
        Set<ResearchOrganization> orgL = new HashSet<>();
        Set<ResearchOrganization> orgs = new HashSet<>();

        if (org.isPresent()) {
            orgL.add(org.get());

            while (!orgL.isEmpty()) {
                for (ResearchOrganization resOrg : orgL) {
                    autL.addAll(repo.findDistinctByAutOrgsResOrgResOrgId(resOrg.getResOrgId()));
                }
                orgs.addAll(orgL);
                orgL.clear();

                for (ResearchOrganization resOrg : orgs) {
                    orgL.addAll(resOrg.getOrgSubs());
                }
                orgs.clear();
            }
        }
        return autL;
    }

    public Set<Author> getDirectlyLinkedMembers(int index) {
        return repo.findDistinctByAutOrgsResOrgResOrgId(index);
    }

    public List<Author> getLinkedAuthors(int index) {
        return repo.findByAutPubsPubPubIdOrderByAutPubsAutShipRank(index);
    }


    //Review if optional as result when several result possible is a good idea
    public int getAuthorIdByName(String autFirstName, String autLastName) {
        List<Author> result = new ArrayList<>();
        final Set<Author> res = repo.findByAutFirstNameAndAutLastName(autFirstName, autLastName);
        if (res.size() > 0) {
            result.addAll(res);
        }

        if (!result.isEmpty()) {
            return result.get(0).getAutId(); //We assume theres no name dupes
        } else {
            return 0;
        }
    }

    public Set<Author> getAuthorsByOrgStatus(String orgName, String status) {

        Set<Author> auts;
        //Consider all 5 as one regrouped type : enseignant chercheur
        if (status.compareTo("Teacher-Researcher") == 0) {
            auts = repo.findDistinctByAutOrgsResOrgResOrgNameAndAutOrgsMemStatus(orgName, MemberStatus.PR);
            auts.addAll(repo.findDistinctByAutOrgsResOrgResOrgNameAndAutOrgsMemStatus(orgName, MemberStatus.MCF));
            auts.addAll(repo.findDistinctByAutOrgsResOrgResOrgNameAndAutOrgsMemStatus(orgName, MemberStatus.MCF_HDR));
            auts.addAll(repo.findDistinctByAutOrgsResOrgResOrgNameAndAutOrgsMemStatus(orgName, MemberStatus.ECC));
            auts.addAll(repo.findDistinctByAutOrgsResOrgResOrgNameAndAutOrgsMemStatus(orgName, MemberStatus.LRU));
        } else {
            auts = repo.findDistinctByAutOrgsResOrgResOrgNameAndAutOrgsMemStatus(orgName, MemberStatus.valueOf(status));
        }

        return auts;
    }

    public int mergeAuthors(String publicationOldAuthor, String publicationNewAuthor) {
        final String oldFirstName = publicationOldAuthor.substring(0, publicationOldAuthor.indexOf(" "));
        final String oldLastName = publicationOldAuthor.substring(publicationOldAuthor.indexOf(" ")+1);
        final Set<Author> oldAuthor = repo.findByAutFirstNameAndAutLastName(oldFirstName, oldLastName);

        final String newFirstName = publicationNewAuthor.substring(0, publicationNewAuthor.indexOf(" "));
        final String newLastName = publicationNewAuthor.substring(publicationNewAuthor.indexOf(" ")+1);
        final Author newAuthor = (Author) repo.findByAutFirstNameAndAutLastName(newFirstName, newLastName).toArray()[0];

        int pubCount = 0;
        for(Author author : oldAuthor) {
            Set<Authorship> autPubs = Collections.unmodifiableSet(author.getAutPubs());
            pubCount = autPubs.size();
            // first : create new authorships

            Set<Integer> autorshipsId = new HashSet<>();
            for(Authorship authorship : autPubs) {
                // Add authorship to new author
                addAuthorship(newAuthor.getAutId(), authorship.getPubPubId(), authorship.getAutShipRank());
                pubRepo.findById(authorship.getPubPubId()).ifPresent(publication -> publication.deleteAuthorship(authorship));
                autorshipsId.add(authorship.getAutShipId());
            }
            // Remove the author
            author.deleteAllAuthorships();
            removeAuthor(author.getAutId());
            for(Integer id : autorshipsId) {
                autShipRepo.deleteById(id);
            }

        }
        return pubCount;
    }
    
    /**
     * Merges all given authors to one and reset name
     * @param authorFirstName new author first name
     * @param authorLastName new author last name
     * @param authorDuplicates list of all authors to merge
     * @return number of affected publications
     * @throws Exception if at least 2 of the authors have a page
     */
    public int mergeMultipleAuthors(String authorFirstName, String authorLastName, List<Integer> authorDuplicates) throws Exception {
        final List<Author> duplicateAuthors = repo.findByAutIdIn(authorDuplicates);

        Author destAuthor = null; 
        List<Membership> memberships = new ArrayList<Membership>(); 
        
        //try to find destination author, the one who has a page (to avoid modification in Wordpress)
        for(Author author : duplicateAuthors) {
        	if (author.isHasPage()) {
            	if (destAuthor != null) {
            		throw new Exception("At least 2 authors have an author page !");
            	}
            	else {
            		destAuthor = author;
            	}  
            }            
        }
        
        //if no author has a page, then take the first one
        if (destAuthor == null) {
        	destAuthor = duplicateAuthors.get(0);
        }
        
        int pubCount = 0;
        
        //loop to change authorship author
        for(Author author : duplicateAuthors) {
        	//use this loop to populate memberships list
        	memberships.addAll(memRepo.findByAutAutId(author.getAutId()));
        	//if not the destination author, then set all the authorship to that author
        	if (!author.equals(destAuthor)) {
                Set<Authorship> autPubs = Collections.unmodifiableSet(author.getAutPubs());
                pubCount += autPubs.size();

                for(Authorship authorship : autPubs) {
                    // Set authorship author id to destination author
                    authorship.setAutAutId(destAuthor.getAutId());
                    this.autShipRepo.save(authorship);
                }
                //set to empty to avoid cascade deletion
                author.setAutPubs(new HashSet<Authorship>());
                repo.save(author);
                repo.delete(author);
        	}
        }
        
        //use previously filled List to change all membership author
        for(Membership membership : memberships) {
            membership.setAut(destAuthor);
            this.memRepo.save(membership);
        }
        
        //change destination author name author name
        destAuthor.setAutFirstName(authorFirstName);
        destAuthor.setAutLastName(authorLastName);
        
        this.repo.save(destAuthor);
        
        return pubCount;
    }
}
