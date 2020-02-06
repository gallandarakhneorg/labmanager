package com.spring.rest.services;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
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

	public int createReadingCommitteeJournalPopularizationPaper(String pubTitle, String pubAbstract, String pubKeywords, Date pubDate, String pubNote,
			String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL, String pubDBLP,
			String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String reaComConfPopPapNumber, String reaComConfPopPapPages, String reaComConfPopPapVolume) {
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
		//res.setPubPDFPath(pubPDFPath);
		res.setPubLanguage(pubLanguage);
		//res.setPubPaperAwardPath(pubPaperAwardPath);
		res.setPubType(pubType);
		//ReadingCommitteeJournalPopularizationPaper fields
		res.setReaComConfPopPapNumber(reaComConfPopPapNumber);
		res.setReaComConfPopPapPages(reaComConfPopPapPages);
		res.setReaComConfPopPapVolume(reaComConfPopPapVolume);

		this.repo.save(res);

		File file;
		if(!pubPDFPath.isEmpty())
		{
			file=new File("/var/www/ciad-lab.fr/Downloadables/PDFs/PDF"+res.getPubId()+".pdf");
			try ( FileOutputStream fos = new FileOutputStream(file); )
			{
				byte[] decoder = Base64.getDecoder().decode(pubPDFPath);
				fos.write(decoder);
				res.setPubPDFPath("/var/www/ciad-lab.fr/Downloadables/PDFs/PDF"+res.getPubId()+".pdf");
		    }
			catch (Exception e) 
			{
			      res.setPubPDFPath("");
			      e.printStackTrace();
		    }
		}
		else
		{
		      res.setPubPDFPath("");
		}
		
		if(!pubPaperAwardPath.isEmpty())
		{
			file=new File("/var/www/ciad-lab.fr/Downloadables/Awards/Award"+res.getPubId()+".pdf");
			try ( FileOutputStream fos = new FileOutputStream(file); )
			{
				byte[] decoder = Base64.getDecoder().decode(pubPaperAwardPath);
				fos.write(decoder);
				res.setPubPaperAwardPath("/var/www/ciad-lab.fr/Downloadables/Awards/Award"+res.getPubId()+".pdf");
			}
			catch (Exception e) 
			{
		    	res.setPubPaperAwardPath("");
		    	e.printStackTrace();
		    }
		}
		else
		{
	    	res.setPubPaperAwardPath("");
		}
		
		this.repo.save(res); //Id is generated on save so I gotta save once before setting these
		
		return res.getPubId();
	}

	public void updateReadingCommitteeJournalPopularizationPaper(int pubId, String pubTitle, String pubAbstract, String pubKeywords, Date pubDate,
			String pubNote, String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL,
			String pubDBLP, String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String reaComConfPopPapNumber, String reaComConfPopPapPages, String reaComConfPopPapVolume) {
		final Optional<ReadingCommitteeJournalPopularizationPaper> res = this.repo.findById(pubId);
		File file;
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
			{
				file=new File("/var/www/ciad-lab.fr/Downloadables/PDFs/PDF"+res.get().getPubId()+".pdf");
				try ( FileOutputStream fos = new FileOutputStream(file); )
				{
					byte[] decoder = Base64.getDecoder().decode(pubPDFPath);
					fos.write(decoder);
					res.get().setPubPDFPath("/var/www/ciad-lab.fr/Downloadables/PDFs/PDF"+res.get().getPubId()+".pdf");
			    }
				catch (Exception e) 
				{
				      res.get().setPubPDFPath("");
				      e.printStackTrace();
			    }
			}
			if(!pubLanguage.isEmpty())
				res.get().setPubLanguage(pubLanguage);
			if(!pubPaperAwardPath.isEmpty())
			{
				file=new File("/var/www/ciad-lab.fr/Downloadables/Awards/Award"+res.get().getPubId()+".pdf");
				try ( FileOutputStream fos = new FileOutputStream(file); )
				{
					byte[] decoder = Base64.getDecoder().decode(pubPaperAwardPath);
					fos.write(decoder);
					res.get().setPubPaperAwardPath("/var/www/ciad-lab.fr/Downloadables/Awards/Award"+res.get().getPubId()+".pdf");
				}
				catch (Exception e) 
				{
			    	res.get().setPubPaperAwardPath("");
			    	e.printStackTrace();
			    }
			}
			if(!pubType.toString().isEmpty())
				res.get().setPubType(pubType);
			//ReadingCommitteeJournalPopularizationPaper fields
			if(!reaComConfPopPapNumber.isEmpty())
				res.get().setReaComConfPopPapNumber(reaComConfPopPapNumber);
			if(!reaComConfPopPapPages.isEmpty())
				res.get().setReaComConfPopPapPages(reaComConfPopPapPages);
			if(!reaComConfPopPapVolume.isEmpty())
				res.get().setReaComConfPopPapVolume(reaComConfPopPapVolume);

			this.repo.save(res.get());
			
		}
	}
}



