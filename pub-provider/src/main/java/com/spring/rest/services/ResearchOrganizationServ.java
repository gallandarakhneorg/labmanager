package com.spring.rest.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

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
import com.spring.rest.repository.MembershipRepository;
import com.spring.rest.repository.ResearchOrganizationRepository;

@Service
public class ResearchOrganizationServ {
	
	//Not used so Ill comment it to prevent warnings
	//private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ResearchOrganizationRepository repo;
	
	@Autowired
	private MembershipRepository memRepo;
	
	@Autowired
	private AuthorRepository autRepo;

	public List<ResearchOrganization> getAllResearchOrganizations() {
		List<ResearchOrganization> orgs = repo.findAll();
		
		for(final ResearchOrganization org : orgs) {
			for(final Membership mem:org.getOrgAuts())
			{
				mem.setResOrg(null);
				mem.getAut().setAutOrgs(new HashSet<>());
				for(Authorship autShip:mem.getAut().getAutPubs())
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
			}
			//org.setOrgSup(null); //Either do that
			if(org.getOrgSup()!=null) 
				org.setOrgSup(cleanOrg(org.getOrgSup()));
			org.setOrgSubs(new HashSet<>()); //Or do that but neither will cause a recursion error
		}
		return orgs;
	}

	public List<ResearchOrganization> getResearchOrganization(int index) {
		List<ResearchOrganization> result = new ArrayList<ResearchOrganization>();
		final Optional<ResearchOrganization> res = repo.findById(index);
		if(res.isPresent()) {
			result.add(res.get());
		}
		
		for(final ResearchOrganization org : result) {
			for(final Membership mem:org.getOrgAuts())
			{
				mem.setResOrg(null);
				mem.getAut().setAutOrgs(new HashSet<>());
				for(Authorship autShip:mem.getAut().getAutPubs())
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
			}
			//org.setOrgSup(null); //Either do that
			if(org.getOrgSup()!=null) 
				org.setOrgSup(cleanOrg(org.getOrgSup()));
			org.setOrgSubs(new HashSet<>()); //Or do that but neither will cause a recursion error
		}
		return result;
	}

	public void removeResearchOrganization(int index) {
		final Optional<ResearchOrganization> res = repo.findById(index);
		if(res.isPresent()) 
		{
			if(res.get().getOrgSubs().isEmpty())
			{
				repo.deleteById(index);
			}
		}

	}

	public int createResearchOrganization(String resOrgName, String resOrgDesc) {
		final ResearchOrganization res = new ResearchOrganization();
		//ResearchOrganization fields
		res.setResOrgName(resOrgName);
		res.setResOrgDesc(resOrgDesc);
		this.repo.save(res);
		return res.getResOrgId();
	}

	public void updateResearchOrganization(int resOrgId, String resOrgName, String resOrgDesc) {
		final Optional<ResearchOrganization> res = this.repo.findById(resOrgId);
		if(res.isPresent()) {
			//Generic pub fields
			if(!resOrgName.isEmpty())
				res.get().setResOrgName(resOrgName);
			if(!resOrgDesc.isEmpty())
				res.get().setResOrgDesc(resOrgDesc);
			this.repo.save(res.get());
		}
	}

	//Here we assume we dont need further than the aut & the org associated
	public List<Membership> getMembership(int orgId, int autId) {
		final Optional<Membership> res = memRepo.findDistinctByResOrgResOrgIdAndAutAutId(orgId, autId);
		List<Membership> memL=new LinkedList<Membership>();
		if(res.isPresent()) {
			res.get().getAut().setAutPubs(new LinkedList<>());
			res.get().getAut().setAutOrgs(new HashSet<>());
			res.get().getResOrg().setOrgSup(null);
			res.get().getResOrg().setOrgSubs(new HashSet<>());
			res.get().getResOrg().setOrgAuts(new HashSet<>());
			memL.add(res.get());
		}
		return memL;
	}
	
	public void addMembership(int index, int autId, Date memSinceWhen, Date memToWhen, MemberStatus memStatus) 
	{
		final Optional<ResearchOrganization> org = repo.findById(index);
		final Optional<Author> aut = autRepo.findById(autId);
		if(org.isPresent() && aut.isPresent()) 
		{
			//disjoint checks if they already dont have an existing membership they share
			if(Collections.disjoint(org.get().getOrgAuts(), aut.get().getAutOrgs()))
			{
				final Membership mem = new Membership();
				mem.setAut(aut.get());
				mem.setResOrg(org.get());
				mem.setMemSinceWhen(memSinceWhen);
				mem.setMemToWhen(memToWhen);
				mem.setMemStatus(memStatus);
				this.memRepo.save(mem);
			}
		}
	}
	
	public void updateMembership(int index, int autId, Date memSinceWhen, Date memToWhen, MemberStatus memStatus) {
		final Optional<Membership> res = memRepo.findDistinctByResOrgResOrgIdAndAutAutId(index, autId);
		if(res.isPresent()) {
			//Generic pub fields
			if(memSinceWhen!=null)
				res.get().setMemSinceWhen(memSinceWhen);
			if(memToWhen!=null)
				res.get().setMemToWhen(memToWhen);
			if(memStatus!=null)
				res.get().setMemStatus(memStatus);
			this.memRepo.save(res.get());
		}
	}

	@Transactional
	//Dunno why but its specifically needed there
	public void removeMembership(int index, int autId) 
	{
		memRepo.deleteByResOrgResOrgIdAndAutAutId(index, autId);
	}

	public void addLinkedOrganization(int index, int resOrgId) {
		final Optional<ResearchOrganization> supOrg = repo.findById(index);
		final Optional<ResearchOrganization> subOrg = repo.findById(resOrgId);
		
		if(supOrg.isPresent() && subOrg.isPresent()) 
		{
			subOrg.get().getOrgSubs().add(subOrg.get());
			subOrg.get().setOrgSup(supOrg.get());
			repo.save(subOrg.get());
			repo.save(supOrg.get());
		}
	}

	public void removeSubOrganization(int index, int resOrgId) {
		final Optional<ResearchOrganization> supOrg = repo.findById(index);
		final Optional<ResearchOrganization> subOrg = repo.findById(resOrgId);
		
		if(supOrg.isPresent() && subOrg.isPresent()) 
		{
			subOrg.get().getOrgSubs().remove(subOrg.get());
			subOrg.get().setOrgSup(null);
			repo.save(subOrg.get());
			repo.save(supOrg.get());
		}
	}

	public Set<ResearchOrganization> getSubOrganizations(int index) {
		Set<ResearchOrganization> orgs = repo.findDistinctByOrgSupResOrgId(index);

		for(final ResearchOrganization org : orgs) {
			for(final Membership mem:org.getOrgAuts())
			{
				mem.setResOrg(null);
				mem.getAut().setAutOrgs(new HashSet<>());
				for(Authorship autShip:mem.getAut().getAutPubs())
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
			}
			//org.setOrgSup(null); //Either do that
			if(org.getOrgSup()!=null) 
				org.setOrgSup(cleanOrg(org.getOrgSup()));
			org.setOrgSubs(new HashSet<>()); //Or do that but neither will cause a recursion error
		}
		return orgs;
	}

	public Set<ResearchOrganization> getSupOrganization(int index) {
		Set<ResearchOrganization> orgs = repo.findDistinctByOrgSubsResOrgId(index);

		for(final ResearchOrganization org : orgs) {
			for(final Membership mem:org.getOrgAuts())
			{
				mem.setResOrg(null);
				mem.getAut().setAutOrgs(new HashSet<>());
				for(Authorship autShip:mem.getAut().getAutPubs())
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
			}
			//org.setOrgSup(null); //Either do that
			if(org.getOrgSup()!=null) 
				org.setOrgSup(cleanOrg(org.getOrgSup()));
			org.setOrgSubs(new HashSet<>()); //Or do that but neither will cause a recursion error
		}
		return orgs;
	}
	
	public Set<ResearchOrganization> getAllSubOrganizations(int index){
		Set<ResearchOrganization> orgL = new HashSet<>();
		final Optional<ResearchOrganization> supOrg = repo.findById(index);
		if(supOrg.isPresent())
		{
			Set<ResearchOrganization> orgs=supOrg.get().getOrgSubs();
			Set<ResearchOrganization> tempOrgs=new HashSet<>();

			while(!orgs.isEmpty())
			{
				orgL.addAll(orgs);
				tempOrgs.addAll(orgs);
				orgs.clear();
				for(ResearchOrganization org:tempOrgs)
				{
					orgs.addAll(org.getOrgSubs());
				}
				tempOrgs.clear();
			}
		}
		
		//clearing
		
		for(final ResearchOrganization org : orgL) {
			for(final Membership mem:org.getOrgAuts())
			{
				mem.setResOrg(null);
				mem.getAut().setAutOrgs(new HashSet<>());
				for(Authorship autShip:mem.getAut().getAutPubs())
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
			}
			//org.setOrgSup(null); //Either do that
			if(org.getOrgSup()!=null) 
				org.setOrgSup(cleanOrg(org.getOrgSup()));
			org.setOrgSubs(new HashSet<>()); //Or do that but neither will cause a recursion error
		}
		return orgL;
	}
	
	public Set<ResearchOrganization> getAllSupOrganizations(int index){
		Set<ResearchOrganization> orgL = new HashSet<>();
		final Optional<ResearchOrganization> subOrg = repo.findById(index);
		if(subOrg.isPresent())
		{
			ResearchOrganization org=subOrg.get().getOrgSup();

			while(org!=null)
			{
				orgL.add(org);
				org=org.getOrgSup();
			}
		}
		
		//Clearing

		for(final ResearchOrganization org : orgL) {
			for(final Membership mem:org.getOrgAuts())
			{
				mem.setResOrg(null);
				mem.getAut().setAutOrgs(new HashSet<>());
				for(Authorship autShip:mem.getAut().getAutPubs())
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
			}
			//org.setOrgSup(null); //Either do that
			if(org.getOrgSup()!=null) 
				org.setOrgSup(cleanOrg(org.getOrgSup()));
			org.setOrgSubs(new HashSet<>()); //Or do that but neither will cause a recursion error
		}
		return orgL;
	}	

	public Set<ResearchOrganization> getLinkedOrganizations(int index) {
		Set<ResearchOrganization> orgs = repo.findDistinctByOrgAutsAutAutId(index);

		for(final ResearchOrganization org : orgs) {
			for(final Membership mem:org.getOrgAuts())
			{
				mem.setResOrg(null);
				mem.getAut().setAutOrgs(new HashSet<>());
				for(Authorship autShip:mem.getAut().getAutPubs())
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
			}
			//org.setOrgSup(null); //Either do that
			if(org.getOrgSup()!=null) 
				org.setOrgSup(cleanOrg(org.getOrgSup()));
			org.setOrgSubs(new HashSet<>()); //Or do that but neither will cause a recursion error
		}
		return orgs;
	}

	
	public Set<ResearchOrganization> cleanOrgSet(Set<ResearchOrganization> orgs)
	{
		for(final ResearchOrganization org : orgs) {
			for(final Membership mem:org.getOrgAuts())
			{
				mem.setResOrg(null);
				mem.getAut().setAutOrgs(new HashSet<>());
				for(Authorship autShip:mem.getAut().getAutPubs())
				{
					Publication pub=autShip.getPub();
					pub.setPubAuts(new LinkedList<>());
					if(pub.getClass()==ReadingCommitteeJournalPopularizationPaper.class)
					{
						if(((ReadingCommitteeJournalPopularizationPaper)pub).getReaComConfPopPapJournal()!=null)
							((ReadingCommitteeJournalPopularizationPaper)pub).getReaComConfPopPapJournal().setJourPubs(new HashSet<>());
					}
				}
			}
			//org.setOrgSup(null); //Either do that
			if(org.getOrgSup()!=null) 
				org.setOrgSup(cleanOrg(org.getOrgSup()));
			org.setOrgSubs(new HashSet<>()); //Or do that but neither will cause a recursion error
		}
		return orgs;
	}
	
	public ResearchOrganization cleanOrg(ResearchOrganization org)
	{
		for(final Membership mem:org.getOrgAuts())
		{
			mem.setResOrg(null);
			mem.getAut().setAutOrgs(new HashSet<>());
			for(Authorship autShip:mem.getAut().getAutPubs())
			{
				Publication pub=autShip.getPub();
				pub.setPubAuts(new LinkedList<>());
				if(pub.getClass()==ReadingCommitteeJournalPopularizationPaper.class)
				{
					if(((ReadingCommitteeJournalPopularizationPaper)pub).getReaComConfPopPapJournal()!=null)
						((ReadingCommitteeJournalPopularizationPaper)pub).getReaComConfPopPapJournal().setJourPubs(new HashSet<>());
				}
			}
		}
		//org.setOrgSup(null); //Either do that
		if(org.getOrgSup()!=null) 
			org.setOrgSup(cleanOrg(org.getOrgSup()));
		org.setOrgSubs(new HashSet<>()); //Or do that but neither will cause a recursion error
		
		return org;
	}
}
