package com.spring.rest.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.rest.entities.PublicationType;
import com.spring.rest.entities.ReadingCommitteeJournalPopularizationPaper;
import com.spring.rest.services.ReadingCommitteeJournalPopularizationPaperServ;

@RestController
@CrossOrigin
public class ReadingCommitteeJournalPopularizationPaperCtrl {
	
	@Autowired
	private ReadingCommitteeJournalPopularizationPaperServ reaComConfPopPapServ;
	
	//Get all entities
	@RequestMapping(value="/getReadingCommitteeJournalPopularizationPapers", method=RequestMethod.POST, headers="Accept=application/json")
	public List<ReadingCommitteeJournalPopularizationPaper> getAllReadingCommitteeJournalPopularizationPapers() {
		return reaComConfPopPapServ.getAllReadingCommitteeJournalPopularizationPapers();
	}

	//Get one specific entity based on its Id
	@RequestMapping(value="/getReadingCommitteeJournalPopularizationPaper", method=RequestMethod.POST, headers="Accept=application/json")
	public List<ReadingCommitteeJournalPopularizationPaper> getReadingCommitteeJournalPopularizationPaper(int index) {
		return reaComConfPopPapServ.getReadingCommitteeJournalPopularizationPaper(index);
	}

	//Remove one specific entity based on its Id
	@RequestMapping(value="/removeReadingCommitteeJournalPopularizationPaper", method=RequestMethod.POST, headers="Accept=application/json")
	public void removeReadingCommitteeJournalPopularizationPaper(int index) {
		reaComConfPopPapServ.removeReadingCommitteeJournalPopularizationPaper(index);
	}

	//Creates one specific entity based on its fields (minus its relationship fields)
	@RequestMapping(value="/createReadingCommitteeJournalPopularizationPaper", method=RequestMethod.POST, headers="Accept=application/json")
	public int createReadingCommitteeJournalPopularizationPaper(String pubTitle, String pubAbstract,
			String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
			String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, PublicationType pubType, String reaComConfPopPapVolume, String reaComConfPopPapNumber, String reaComConfPopPapPages) {
		return reaComConfPopPapServ.createReadingCommitteeJournalPopularizationPaper(pubTitle, pubAbstract,
				pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN, 
				pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
				pubPaperAwardPath, pubType, reaComConfPopPapNumber, reaComConfPopPapPages, reaComConfPopPapVolume);
		//TMT 24/11/20 : use right parameters (wrong order)
	}

	//Updates one specific entity based on its fields (minus its relationship fields)
	@RequestMapping(value="/updateReadingCommitteeJournalPopularizationPaper", method=RequestMethod.POST, headers="Accept=application/json")
	public void updateReadingCommitteeJournalPopularizationPaper(int pubId, String pubTitle, String pubAbstract,
			String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
			String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, PublicationType pubType, String reaComConfPopPapVolume, String reaComConfPopPapNumber, String reaComConfPopPapPages) {
		reaComConfPopPapServ.updateReadingCommitteeJournalPopularizationPaper(pubId, pubTitle, pubAbstract,
				pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN, 
				pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
				pubPaperAwardPath, pubType, reaComConfPopPapNumber, reaComConfPopPapPages, reaComConfPopPapVolume);
		//TMT 24/11/20 : use right parameters (wrong order)
	}


}
