package com.spring.rest.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.rest.entities.Journal;
import com.spring.rest.entities.Publication;
import com.spring.rest.entities.ReadingCommitteeJournalPopularizationPaper;
import com.spring.rest.repository.JournalRepository;
import com.spring.rest.repository.PublicationRepository;
import com.spring.rest.repository.ReadingCommitteeJournalPopularizationPaperRepository;

@Service
public class JournalServ {
	
	@Autowired
	private JournalRepository repo;
	
	@Autowired
	private ReadingCommitteeJournalPopularizationPaperRepository pubRepo;

	public List<Journal> getAllJournals() {
		List<Journal> jours = repo.findAll();
		//fix json recursion here, going further than pubs should be unnecessary
		for(Journal j : jours)
		{
			preventJournalRecursion(j);
		}
		return jours;
	}

	public List<Journal> getJournal(int index) {
		final List<Journal> result = new ArrayList<Journal>();
		final Optional<Journal> res = repo.findById(index);
		if(res.isPresent()) {
			preventJournalRecursion(res.get());
			result.add(res.get());
		}
		
		//fix json recursion here, going further than pubs should be unnecessary
		
		return result;
	}

	public void removeJournal(int index) {
		Optional<Journal> j = repo.findById(index);
		if(j.isPresent())
		{
			repo.deleteById(index);
		}
	}

	public void createJournal(String jourName, String jourPublisher, String jourElsevier, String jourScimago, String jourWos) {
		final Journal res = new Journal();
		//Generic pub fields
		res.setJourName(jourName);
		res.setJourPublisher(jourPublisher);
		res.setJourElsevier(jourElsevier);
		res.setJourScimago(jourScimago);
		res.setJourWos(jourWos);
		this.repo.save(res);
	}

	public void updateJournal(int pubId, String jourName, String jourPublisher, String jourElsevier, String jourScimago, String jourWos) {
		final Optional<Journal> res = this.repo.findById(pubId);
		if(res.isPresent()) {
			//Generic pub fields
			if(!jourName.isEmpty())
				res.get().setJourName(jourName);
			if(!jourName.isEmpty())
				res.get().setJourPublisher(jourPublisher);
			if(!jourName.isEmpty())
				res.get().setJourElsevier(jourElsevier);
			if(!jourName.isEmpty())
				res.get().setJourScimago(jourScimago);
			if(!jourName.isEmpty())
				res.get().setJourWos(jourWos);

			this.repo.save(res.get());
		}
	}

	public Set<Journal> getPubsJournal(int pubId) 
	{
		Set<Journal> jours = repo.findDistinctByJourPubsPubId(pubId);
		
		for(Journal j : jours) //There should only be one since its a onetoMany but just be to safe
		{
			preventJournalRecursion(j);
		}
		
		return jours;
	}
	
	public void preventJournalRecursion(Journal j)
	{
		Set<ReadingCommitteeJournalPopularizationPaper> pubs = j.getJourPubs();
		for(ReadingCommitteeJournalPopularizationPaper p : pubs)
		{
			p.setReaComConfPopPapJournal(null);
			p.setPubAuts(new HashSet<>());
		}
	}

	public void addJournalLink(int pubId, int jourId) {
		Optional<ReadingCommitteeJournalPopularizationPaper> pub = pubRepo.findById(pubId);
		Optional<Journal> jour = repo.findById(jourId);
		if(pub.isPresent() && jour.isPresent())
		{
			removeJournalLink(pubId);
			jour.get().getJourPubs().add(pub.get());
			pub.get().setReaComConfPopPapJournal(jour.get());
			repo.save(jour.get());
			pubRepo.save(pub.get());
		}
	}

	public void removeJournalLink(int pubId) {
		Optional<ReadingCommitteeJournalPopularizationPaper> pub = pubRepo.findById(pubId);
		Journal jour;

		if(pub.isPresent() && pub.get().getReaComConfPopPapJournal()!=null)
		{
			jour=pub.get().getReaComConfPopPapJournal();
			jour.getJourPubs().remove(pub.get());
			pub.get().setReaComConfPopPapJournal(null);
			repo.save(jour);
			pubRepo.save(pub.get());
		}
	}
}



