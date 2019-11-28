package com.spring.rest.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.rest.entities.PublicationType;
import com.spring.rest.entities.ReadingCommitteeJournalPopularizationPaper;
import com.spring.rest.repository.ReadingCommitteeJournalPopularizationPaperRepository;

@Service
public class ReadingCommitteeJournalPopularizationPaperServ {
	
	@Autowired
	private ReadingCommitteeJournalPopularizationPaperRepository repo;

	public List<ReadingCommitteeJournalPopularizationPaper> getAllReadingCommitteeJournalPopularizationPapers() {
		return repo.findAll();
	}

	public List<ReadingCommitteeJournalPopularizationPaper> getReadingCommitteeJournalPopularizationPaper(int index) {
		final List<ReadingCommitteeJournalPopularizationPaper> result = new ArrayList<ReadingCommitteeJournalPopularizationPaper>();
		final Optional<ReadingCommitteeJournalPopularizationPaper> res = repo.findById(index);
		if(res.isPresent()) {
			result.add(res.get());
		}
		return result;
	}

	public void removeReadingCommitteeJournalPopularizationPaper(int index) {
		repo.deleteById(index);
	}

	public void createReadingCommitteeJournalPopularizationPaper(String pubTitle, String pubAbstract, String pubKeywords, Date pubDate, String pubNote,
			String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL, String pubDBLP,
			String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String reaComConfPopPapJournalName, String reaComConfPopPapNumber, String reaComConfPopPapPages, String reaComConfPopPapPublisher, String reaComConfPopPapVolume) {
		final ReadingCommitteeJournalPopularizationPaper res = new ReadingCommitteeJournalPopularizationPaper();
		//Generic pub fields
		res.setPubTitle(pubTitle);
		res.setPubAbstract(pubAbstract);
		res.setPubKeywords(pubKeywords);
		res.setPubDate(pubDate);
		res.setPubNote(pubNote);
		res.setPubAnnotations(pubAnnotations);
		res.setPubISBN(pubISBN);
		res.setPubISSN(pubISSN);
		res.setPubDOIRef(pubDOIRef);
		res.setPubURL(pubURL);
		res.setPubDBLP(pubDBLP);
		res.setPubPDFPath(pubPDFPath);
		res.setPubLanguage(pubLanguage);
		res.setPubPaperAwardPath(pubPaperAwardPath);
		res.setPubType(pubType);
		//ReadingCommitteeJournalPopularizationPaper fields
		res.setReaComConfPopPapJournalName(reaComConfPopPapJournalName);
		res.setReaComConfPopPapNumber(reaComConfPopPapNumber);
		res.setReaComConfPopPapPages(reaComConfPopPapPages);
		res.setReaComConfPopPapPublisher(reaComConfPopPapPublisher);
		res.setReaComConfPopPapVolume(reaComConfPopPapVolume);
		this.repo.save(res);
	}

	public void updateReadingCommitteeJournalPopularizationPaper(int pubId, String pubTitle, String pubAbstract, String pubKeywords, Date pubDate,
			String pubNote, String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL,
			String pubDBLP, String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String reaComConfPopPapJournalName, String reaComConfPopPapNumber, String reaComConfPopPapPages, String reaComConfPopPapPublisher, String reaComConfPopPapVolume) {
		final Optional<ReadingCommitteeJournalPopularizationPaper> res = this.repo.findById(pubId);
		if(res.isPresent()) {
			//Generic pub fields
			if(!pubTitle.isEmpty())
				res.get().setPubTitle(pubTitle);
			if(!pubAbstract.isEmpty())
				res.get().setPubAbstract(pubAbstract);
			if(!pubKeywords.isEmpty())
				res.get().setPubKeywords(pubKeywords);
			if(pubDate != null)
				res.get().setPubDate(pubDate);
			if(!pubNote.isEmpty())
				res.get().setPubNote(pubNote);
			if(!pubAnnotations.isEmpty())
				res.get().setPubAnnotations(pubAnnotations);
			if(!pubISBN.isEmpty())
				res.get().setPubISBN(pubISBN);
			if(!pubISSN.isEmpty())
				res.get().setPubISSN(pubISSN);
			if(!pubDOIRef.isEmpty())
				res.get().setPubDOIRef(pubDOIRef);
			if(!pubURL.isEmpty())
				res.get().setPubURL(pubURL);
			if(!pubDBLP.isEmpty())
				res.get().setPubDBLP(pubDBLP);
			if(!pubPDFPath.isEmpty())
				res.get().setPubPDFPath(pubPDFPath);
			if(!pubLanguage.isEmpty())
				res.get().setPubLanguage(pubLanguage);
			if(!pubPaperAwardPath.isEmpty())
				res.get().setPubPaperAwardPath(pubPaperAwardPath);
			if(!pubType.toString().isEmpty())
				res.get().setPubType(pubType);
			//ReadingCommitteeJournalPopularizationPaper fields
			if(!reaComConfPopPapJournalName.isEmpty())
				res.get().setReaComConfPopPapJournalName(reaComConfPopPapJournalName);
			if(!reaComConfPopPapNumber.isEmpty())
				res.get().setReaComConfPopPapNumber(reaComConfPopPapNumber);
			if(!reaComConfPopPapPages.isEmpty())
				res.get().setReaComConfPopPapPages(reaComConfPopPapPages);
			if(!reaComConfPopPapPublisher.isEmpty())
				res.get().setReaComConfPopPapPublisher(reaComConfPopPapPublisher);
			if(!reaComConfPopPapVolume.isEmpty())
				res.get().setReaComConfPopPapVolume(reaComConfPopPapVolume);
			this.repo.save(res.get());
		}
	}
}



