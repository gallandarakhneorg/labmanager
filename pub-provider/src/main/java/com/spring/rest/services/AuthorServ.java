package com.spring.rest.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.rest.entities.Author;
import com.spring.rest.entities.Authorship;
import com.spring.rest.entities.MemberStatus;
import com.spring.rest.entities.Membership;
import com.spring.rest.entities.Publication;
import com.spring.rest.entities.ReadingCommitteeJournalPopularizationPaper;
import com.spring.rest.entities.ResearchOrganization;
import com.spring.rest.repository.AuthorRepository;
import com.spring.rest.repository.AuthorshipRepository;
import com.spring.rest.repository.PublicationRepository;
import com.spring.rest.repository.ResearchOrganizationRepository;

@Service
public class AuthorServ {
	
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

	public List<Author> getAllAuthors() {
		List<Author> auts = repo.findAll();
		
		for(final Author aut : auts) {
			for(Authorship autShip:aut.getAutPubs())
			{
				Publication pub=autShip.getPub();
				autShip.setAut(null);
				pub.setPubAuts(new LinkedList<>());
				if(pub.getClass()==ReadingCommitteeJournalPopularizationPaper.class)
				{
					if(((ReadingCommitteeJournalPopularizationPaper)pub).getReaComConfPopPapJournal()!=null)
						((ReadingCommitteeJournalPopularizationPaper)pub).getReaComConfPopPapJournal().setJourPubs(new HashSet<>());
				}
			}
			for(final Membership mem:aut.getAutOrgs())
			{
				mem.setAut(null);
				mem.getResOrg().setOrgAuts(new HashSet<>());
				//We assume we dont need suborg infos from auts
				mem.getResOrg().setOrgSubs(new HashSet<>());
				mem.getResOrg().setOrgSup(null);
			}
			//Also block off suborgs
			encodeFiles(aut);
		}
		return auts;
	}

	public List<Author> getAuthor(int index) {
		List<Author> result = new ArrayList<Author>();
		final Optional<Author> res = repo.findById(index);
		if(res.isPresent()) {
			result.add(res.get());
		}
		
		for(final Author aut : result) {
			for(Authorship autShip:aut.getAutPubs())
			{
				Publication pub=autShip.getPub();
				autShip.setAut(null);
				pub.setPubAuts(new LinkedList<>());
				if(pub.getClass()==ReadingCommitteeJournalPopularizationPaper.class)
				{
					if(((ReadingCommitteeJournalPopularizationPaper)pub).getReaComConfPopPapJournal()!=null)
						((ReadingCommitteeJournalPopularizationPaper)pub).getReaComConfPopPapJournal().setJourPubs(new HashSet<>());
				}
			}
			for(final Membership mem:aut.getAutOrgs())
			{
				mem.setAut(null);
				mem.getResOrg().setOrgAuts(new HashSet<>());
				//We assume we dont need suborg infos from auts
				mem.getResOrg().setOrgSubs(new HashSet<>());
				mem.getResOrg().setOrgSup(null);
			}
			//Also block off suborgs
			encodeFiles(aut);
		}
		return result;
	}

	public void removeAuthor(int index) {
		final Optional<Author> res = repo.findById(index);
		if(res.isPresent()) {
			
			//When removing an author, reduce the ranks of the other authorships for the pubs he made.
			for(Authorship autShip:res.get().getAutPubs())
			{
				int rank=autShip.getRank();
				Publication pub=autShip.getPub();
				for(Authorship autShipPub:pub.getPubAuts())
				{
					if(autShipPub.getRank()>rank)
					{
						autShipPub.setRank(autShipPub.getRank()-1);
					}
					autShipRepo.save(autShipPub);
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
		if(res.isPresent()) {
			if(!autFirstName.isEmpty())
				res.get().setAutFirstName(autFirstName);
			if(!autLastName.isEmpty())
				res.get().setAutLastName(autLastName);
			if(autBirth != null)
				res.get().setAutBirth(autBirth);
			if(autMail != null)
				res.get().setAutMail(autMail);
			
			this.repo.save(res.get());
		}
	}

	public void addAuthorship(int index, int pubId) {
		final Optional<Author> author = repo.findById(index);
		final Optional<Publication> publication = pubRepo.findById(pubId);
		
		//Need to add on both sides but only one save is needed.
		//Ill still do both in case
		if(author.isPresent() && publication.isPresent()) 
		{
			if(Collections.disjoint(author.get().getAutPubs(), publication.get().getPubAuts()))
			{
				Authorship autShip = new Authorship();
				autShip.setPub(publication.get());
				autShip.setAut(author.get());
				
				autShip.setRank(repo.findDistinctByAutPubsPubPubId(pubId).size());
				this.autShipRepo.save(autShip);
			}
		}
	}

	public void removeAuthorship(int index, int pubId) {
		final Optional<Author> author = repo.findById(index);
		final Optional<Publication> publication = pubRepo.findById(pubId);
		
		Optional<Authorship> autShip=autShipRepo.findDistinctByAutAutIdAndPubPubId(index, pubId);
		
		if(autShip.isPresent())
		{
			int rank=autShip.get().getRank();
			Publication pub=autShip.get().getPub();
			for(Authorship autShipPub:pub.getPubAuts())
			{
				if(autShipPub.getRank()>rank)
				{
					autShipPub.setRank(autShipPub.getRank()-1);
				}
				autShipRepo.save(autShipPub);
			}
			autShipRepo.deleteByAutAutIdAndPubPubId(index, pubId);
		}
		
	}

	public void updateAuthorPage(int index, boolean hasPage) {
		final Optional<Author> res = this.repo.findById(index);
		if(res.isPresent()) {
			res.get().setHasPage(hasPage);
			repo.save(res.get());
		}
	}

	public Set<Author> getLinkedMembers(int index) {
		final Optional<ResearchOrganization> org = orgRepo.findById(index);
		Set<Author> autL=new HashSet<Author>();
		Set<ResearchOrganization> orgL=new HashSet<>();
		Set<ResearchOrganization> orgs=new HashSet<>();
		
		if(org.isPresent()) 
		{
			orgL.add(org.get());
			
			while(!orgL.isEmpty())
			{
				for(ResearchOrganization resOrg:orgL)
				{
					autL.addAll(repo.findDistinctByAutOrgsResOrgResOrgId(resOrg.getResOrgId()));
				}
				orgs.addAll(orgL);
				orgL.clear();

				for(ResearchOrganization resOrg:orgs)
				{
					orgL.addAll(resOrg.getOrgSubs());
				}
				orgs.clear();
			}
		}
		
		for(final Author aut : autL) {
			for(Authorship autShip:aut.getAutPubs())
			{
				Publication pub=autShip.getPub();
				autShip.setAut(null);
				pub.setPubAuts(new LinkedList<>());
				if(pub.getClass()==ReadingCommitteeJournalPopularizationPaper.class)
				{
					if(((ReadingCommitteeJournalPopularizationPaper)pub).getReaComConfPopPapJournal()!=null)
						((ReadingCommitteeJournalPopularizationPaper)pub).getReaComConfPopPapJournal().setJourPubs(new HashSet<>());
				}
			}
			for(final Membership mem:aut.getAutOrgs())
			{
				mem.setAut(null);
				mem.getResOrg().setOrgAuts(new HashSet<>());
				//We assume we dont need suborg infos from auts
				mem.getResOrg().setOrgSubs(new HashSet<>());
				mem.getResOrg().setOrgSup(null);
			}
			//Also block off suborgs
			encodeFiles(aut);
		}
		return autL;
	}

	public Set<Author> getDirectlyLinkedMembers(int index) {
		Set<Author> auts = repo.findDistinctByAutOrgsResOrgResOrgId(index);
		
		for(final Author aut : auts) {
			for(Authorship autShip:aut.getAutPubs())
			{
				Publication pub=autShip.getPub();
				autShip.setAut(null);
				pub.setPubAuts(new LinkedList<>());
				if(pub.getClass()==ReadingCommitteeJournalPopularizationPaper.class)
				{
					if(((ReadingCommitteeJournalPopularizationPaper)pub).getReaComConfPopPapJournal()!=null)
						((ReadingCommitteeJournalPopularizationPaper)pub).getReaComConfPopPapJournal().setJourPubs(new HashSet<>());
				}
			}
			for(final Membership mem:aut.getAutOrgs())
			{
				mem.setAut(null);
				mem.getResOrg().setOrgAuts(new HashSet<>());
				//We assume we dont need suborg infos from auts
				mem.getResOrg().setOrgSubs(new HashSet<>());
				mem.getResOrg().setOrgSup(null);
			}
			//Also block off suborgs
			encodeFiles(aut);
		}
		return auts;
	}

	public Set<Author> getLinkedAuthors(int index) {
		Set<Author> auts = repo.findDistinctByAutPubsPubPubId(index);
		
		for(final Author aut : auts) {
			for(Authorship autShip:aut.getAutPubs())
			{
				Publication pub=autShip.getPub();
				autShip.setAut(null);
				pub.setPubAuts(new LinkedList<>());
				if(pub.getClass()==ReadingCommitteeJournalPopularizationPaper.class)
				{
					if(((ReadingCommitteeJournalPopularizationPaper)pub).getReaComConfPopPapJournal()!=null)
						((ReadingCommitteeJournalPopularizationPaper)pub).getReaComConfPopPapJournal().setJourPubs(new HashSet<>());
				}
			}
			for(final Membership mem:aut.getAutOrgs())
			{
				mem.setAut(null);
				mem.getResOrg().setOrgAuts(new HashSet<>());
				//We assume we dont need suborg infos from auts
				mem.getResOrg().setOrgSubs(new HashSet<>());
				mem.getResOrg().setOrgSup(null);
			}
			//Also block off suborgs
			encodeFiles(aut);
		}
		return auts;
	}
	

//Review if optional as result when several result possible is a good idea
	public int getAuthorIdByName(String autFirstName, String autLastName) {
		List<Author> result = new ArrayList<>();
		final Optional<Author> res = repo.findByAutFirstNameAndAutLastName(autFirstName, autLastName);
		if(res.isPresent()) {
			result.add(res.get());
		}
		
		if(!result.isEmpty())
		{
			return result.get(0).getAutId(); //We assume theres no name dupes
		}
		else
		{
			return 0;
		}
	}

	public Set<Author> getAuthorsByOrgStatus(String orgName, String status) {

		Set<Author> auts;
		//Consider all 5 as one regrouped type : enseignant chercheur
		if(status.compareTo("Teacher-Researcher")==0)
		{
			auts = repo.findDistinctByAutOrgsResOrgResOrgNameAndAutOrgsMemStatus(orgName, MemberStatus.PR);
			auts.addAll(repo.findDistinctByAutOrgsResOrgResOrgNameAndAutOrgsMemStatus(orgName, MemberStatus.MCF));
			auts.addAll(repo.findDistinctByAutOrgsResOrgResOrgNameAndAutOrgsMemStatus(orgName, MemberStatus.MCF_HDR));
			auts.addAll(repo.findDistinctByAutOrgsResOrgResOrgNameAndAutOrgsMemStatus(orgName, MemberStatus.ECC));
			auts.addAll(repo.findDistinctByAutOrgsResOrgResOrgNameAndAutOrgsMemStatus(orgName, MemberStatus.LRU));
		}
		else
		{
			auts = repo.findDistinctByAutOrgsResOrgResOrgNameAndAutOrgsMemStatus(orgName, MemberStatus.valueOf(status));
		}
		
		for(final Author aut : auts) {
			for(Authorship autShip:aut.getAutPubs())
			{
				Publication pub=autShip.getPub();
				autShip.setAut(null);
				pub.setPubAuts(new LinkedList<>());
				if(pub.getClass()==ReadingCommitteeJournalPopularizationPaper.class)
				{
					if(((ReadingCommitteeJournalPopularizationPaper)pub).getReaComConfPopPapJournal()!=null)
						((ReadingCommitteeJournalPopularizationPaper)pub).getReaComConfPopPapJournal().setJourPubs(new HashSet<>());
				}
			}
			for(final Membership mem:aut.getAutOrgs())
			{
				mem.setAut(null);
				mem.getResOrg().setOrgAuts(new HashSet<>());
				//We assume we dont need suborg infos from auts
				mem.getResOrg().setOrgSubs(new HashSet<>());
				mem.getResOrg().setOrgSup(null);
			}
			//Also block off suborgs
			encodeFiles(aut);
		}
		return auts;
	}

	public void encodeFiles(Author aut)
	{
		//We're gonna try and store the profile picture client-side so we dont need this.
		//We'll save the pic on client are remember the path here is all.
		
		
		/*
		byte[] input_file;
		byte[] encodedBytes;
		String fileString;
		
		//Check if the path exists, if it is not empty and if not, if it is a valid path
		if(aut.getAutPic()!=null && !aut.getAutPic().isEmpty() && new File(aut.getAutPic()).exists())
		{
			try {
				input_file = Files.readAllBytes(Paths.get(aut.getAutPic()));
		        encodedBytes = Base64.getEncoder().encode(input_file);
		        fileString=new String(encodedBytes);
		        aut.setAutPic(fileString);
			} catch (IOException e) {
		        aut.setAutPic("");
				e.printStackTrace();
			}
		}
		
		*/
	}
	
}
