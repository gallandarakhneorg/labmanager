package com.spring.rest.services;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.rest.PubProviderApplication;
import com.spring.rest.entities.ProceedingsConference;
import com.spring.rest.entities.PublicationType;
import com.spring.rest.repository.ProceedingsConferenceRepository;

@Service
public class ProceedingsConferenceServ {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ProceedingsConferenceRepository repo;

	public List<ProceedingsConference> getAllProceedingsConferences() {
		return repo.findAll();
	}

	public List<ProceedingsConference> getProceedingsConference(int index) {
		List<ProceedingsConference> result = new ArrayList<ProceedingsConference>();
		final Optional<ProceedingsConference> res = repo.findById(index);
		if(res.isPresent()) {
			result.add(res.get());
		}
		return result;
	}

	public void removeProceedingsConference(int index) {
		repo.deleteById(index);
	}

	public int createProceedingsConference(String pubTitle, String pubAbstract, String pubKeywords, Date pubDate, String pubNote,
			String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL, String pubDBLP,
			String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String proConfAddress, 
			String proConfBookNameProceedings, String proConfEditor, String proConfOrganization, String proConfPages, 
			String proConfPublisher, String proConfSeries) {
		final ProceedingsConference res = new ProceedingsConference();
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
		//ProceedingsConference fields
		res.setProConfAddress(proConfAddress);
		res.setProConfBookNameProceedings(proConfBookNameProceedings);
		res.setProConfEditor(proConfEditor);
		res.setProConfOrganization(proConfOrganization);
		res.setProConfPages(proConfPages);
		res.setProConfPublisher(proConfPublisher);
		res.setProConfSeries(proConfSeries);
		this.repo.save(res);

		File file;
		if(!pubPDFPath.isEmpty())
		{
			file=new File(PubProviderApplication.DownloadablesPath+"PDFs/PDF"+res.getPubId()+".pdf");
			try ( FileOutputStream fos = new FileOutputStream(file); )
			{
				byte[] decoder = Base64.getDecoder().decode(pubPDFPath);
				fos.write(decoder);
				res.setPubPDFPath(PubProviderApplication.DownloadablesPath+"PDFs/PDF"+res.getPubId()+".pdf");
		    }
			catch (Exception e) 
			{
			      res.setPubPDFPath("");
			      e.printStackTrace();
			      this.logger.error(e.getMessage(),e);
		    }
		}
		else
		{
		      res.setPubPDFPath("");
		}
		
		if(!pubPaperAwardPath.isEmpty())
		{
			file=new File(PubProviderApplication.DownloadablesPath+"Awards/Award"+res.getPubId()+".pdf");
			try ( FileOutputStream fos = new FileOutputStream(file); )
			{
				byte[] decoder = Base64.getDecoder().decode(pubPaperAwardPath);
				fos.write(decoder);
				res.setPubPaperAwardPath(PubProviderApplication.DownloadablesPath+"Awards/Award"+res.getPubId()+".pdf");
			}
			catch (Exception e) 
			{
		    	res.setPubPaperAwardPath("");
		    	e.printStackTrace();
			      this.logger.error(e.getMessage(),e);
		    }
		}
		else
		{
	    	res.setPubPaperAwardPath("");
		}
		
		this.repo.save(res); //Id is generated on save so I gotta save once before setting these
		return res.getPubId();
	}

	public void updateProceedingsConference(int pubId, String pubTitle, String pubAbstract, String pubKeywords, Date pubDate,
			String pubNote, String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL,
			String pubDBLP, String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String proConfAddress, 
			String proConfBookNameProceedings, String proConfEditor, String proConfOrganization, String proConfPages, 
			String proConfPublisher, String proConfSeries) {
		final Optional<ProceedingsConference> res = this.repo.findById(pubId);
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
				file=new File(PubProviderApplication.DownloadablesPath+"PDFs/PDF"+res.get().getPubId()+".pdf");
				try ( FileOutputStream fos = new FileOutputStream(file); )
				{
					byte[] decoder = Base64.getDecoder().decode(pubPDFPath);
					fos.write(decoder);
					res.get().setPubPDFPath(PubProviderApplication.DownloadablesPath+"PDFs/PDF"+res.get().getPubId()+".pdf");
			    }
				catch (Exception e) 
				{
				      res.get().setPubPDFPath("");
				      e.printStackTrace();
				      this.logger.error(e.getMessage(),e);
			    }
			}
			if(!pubLanguage.isEmpty())
				res.get().setPubLanguage(pubLanguage);
			if(!pubPaperAwardPath.isEmpty())
			{
				file=new File(PubProviderApplication.DownloadablesPath+"Awards/Award"+res.get().getPubId()+".pdf");
				try ( FileOutputStream fos = new FileOutputStream(file); )
				{
					byte[] decoder = Base64.getDecoder().decode(pubPaperAwardPath);
					fos.write(decoder);
					res.get().setPubPaperAwardPath(PubProviderApplication.DownloadablesPath+"Awards/Award"+res.get().getPubId()+".pdf");
				}
				catch (Exception e) 
				{
			    	res.get().setPubPaperAwardPath("");
			    	e.printStackTrace();
				      this.logger.error(e.getMessage(),e);
			    }
			}
			if(!pubType.toString().isEmpty())
				res.get().setPubType(pubType);
			//ProceedingsConference fields
			if(!proConfAddress.isEmpty())
				res.get().setProConfAddress(proConfAddress);
			if(!proConfBookNameProceedings.isEmpty())
				res.get().setProConfBookNameProceedings(proConfBookNameProceedings);
			if(!proConfEditor.isEmpty())
				res.get().setProConfEditor(proConfEditor);
			if(!proConfOrganization.isEmpty())
				res.get().setProConfOrganization(proConfOrganization);
			if(!proConfPages.isEmpty())
				res.get().setProConfPages(proConfPages);
			if(!proConfPublisher.isEmpty())
				res.get().setProConfPublisher(proConfPublisher);
			if(!proConfSeries.isEmpty())
				res.get().setProConfSeries(proConfSeries);
			this.repo.save(res.get());
		}
	}
}



