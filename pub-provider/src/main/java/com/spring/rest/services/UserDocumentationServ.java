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

import com.spring.rest.entities.PublicationType;
import com.spring.rest.entities.UserDocumentation;
import com.spring.rest.repository.UserDocumentationRepository;

@Service
public class UserDocumentationServ {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserDocumentationRepository repo;

	public List<UserDocumentation> getAllUserDocumentations() {
		return repo.findAll();
	}

	public List<UserDocumentation> getUserDocumentation(int index) {
		final List<UserDocumentation> result = new ArrayList<UserDocumentation>();
		final Optional<UserDocumentation> res = repo.findById(index);
		if(res.isPresent()) {
			result.add(res.get());
		}
		return result;
	}

	public void removeUserDocumentation(int index) {
		repo.deleteById(index);
	}

	public int createUserDocumentation(String pubTitle, String pubAbstract, String pubKeywords, Date pubDate, String pubNote,
			String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL, String pubDBLP,
			String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String userDocAddress, String userDocEdition, String userDocOrganization, String userDocPublisher) {
		final UserDocumentation res = new UserDocumentation();
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
		//UserDocumentation fields
		res.setUserDocAddress(userDocAddress);
		res.setUserDocEdition(userDocEdition);
		res.setUserDocOrganization(userDocOrganization);
		res.setUserDocPublisher(userDocPublisher);
		this.repo.save(res);

		File file;
		if(!pubPDFPath.isEmpty())
		{
			file=new File(PublicationServ.DownloadablesPath+"PDFs/PDF"+res.getPubId()+".pdf");
			try ( FileOutputStream fos = new FileOutputStream(file); )
			{
				byte[] decoder = Base64.getDecoder().decode(pubPDFPath);
				fos.write(decoder);
				res.setPubPDFPath(PublicationServ.DownloadablesPath+"PDFs/PDF"+res.getPubId()+".pdf");
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
			file=new File(PublicationServ.DownloadablesPath+"Awards/Award"+res.getPubId()+".pdf");
			try ( FileOutputStream fos = new FileOutputStream(file); )
			{
				byte[] decoder = Base64.getDecoder().decode(pubPaperAwardPath);
				fos.write(decoder);
				res.setPubPaperAwardPath(PublicationServ.DownloadablesPath+"Awards/Award"+res.getPubId()+".pdf");
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

	public void updateUserDocumentation(int pubId, String pubTitle, String pubAbstract, String pubKeywords, Date pubDate,
			String pubNote, String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL,
			String pubDBLP, String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String userDocAddress, String userDocEdition, String userDocOrganization, String userDocPublisher) {
		final Optional<UserDocumentation> res = this.repo.findById(pubId);
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
				file=new File(PublicationServ.DownloadablesPath+"PDFs/PDF"+res.get().getPubId()+".pdf");
				try ( FileOutputStream fos = new FileOutputStream(file); )
				{
					byte[] decoder = Base64.getDecoder().decode(pubPDFPath);
					fos.write(decoder);
					res.get().setPubPDFPath(PublicationServ.DownloadablesPath+"PDFs/PDF"+res.get().getPubId()+".pdf");
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
				file=new File(PublicationServ.DownloadablesPath+"Awards/Award"+res.get().getPubId()+".pdf");
				try ( FileOutputStream fos = new FileOutputStream(file); )
				{
					byte[] decoder = Base64.getDecoder().decode(pubPaperAwardPath);
					fos.write(decoder);
					res.get().setPubPaperAwardPath(PublicationServ.DownloadablesPath+"Awards/Award"+res.get().getPubId()+".pdf");
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
			//UserDocumentation fields
			if(!userDocAddress.isEmpty())
				res.get().setUserDocAddress(userDocAddress);
			if(!userDocEdition.isEmpty())
				res.get().setUserDocEdition(userDocEdition);
			if(!userDocOrganization.isEmpty())
				res.get().setUserDocOrganization(userDocOrganization);
			if(!userDocPublisher.isEmpty())
				res.get().setUserDocPublisher(userDocPublisher);
			this.repo.save(res.get());
		}
	}
}



