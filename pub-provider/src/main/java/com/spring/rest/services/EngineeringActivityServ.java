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

import com.spring.rest.entities.EngineeringActivity;
import com.spring.rest.entities.PublicationType;
import com.spring.rest.repository.EngineeringActivityRepository;

@Service
public class EngineeringActivityServ {
	
	@Autowired
	private EngineeringActivityRepository repo;

	public List<EngineeringActivity> getAllEngineeringActivitys() {
		return repo.findAll();
	}

	public List<EngineeringActivity> getEngineeringActivity(int index) {
		final List<EngineeringActivity> result = new ArrayList<EngineeringActivity>();
		final Optional<EngineeringActivity> res = repo.findById(index);
		if(res.isPresent()) {
			result.add(res.get());
		}
		return result;
	}

	public void removeEngineeringActivity(int index) {
		repo.deleteById(index);
	}

	public void createEngineeringActivity(String pubTitle, String pubAbstract, String pubKeywords, Date pubDate, String pubNote,
			String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL, String pubDBLP,
			String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String engActInstitName,
			String engActNumber, String engActReportType) {
		final EngineeringActivity res = new EngineeringActivity();
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
		//EngineeringActivity fields
		res.setEngActInstitName(engActInstitName);
		res.setEngActNumber(engActNumber);
		res.setEngActReportType(engActReportType);
		this.repo.save(res);

		File file;
		if(!pubPDFPath.isEmpty())
		{
			file=new File("Downloadables/PDFs/PDF"+res.getPubId()+".pdf");
			try ( FileOutputStream fos = new FileOutputStream(file); )
			{
				byte[] decoder = Base64.getDecoder().decode(pubPDFPath);
				fos.write(decoder);
				res.setPubPDFPath("Downloadables/PDFs/PDF"+res.getPubId()+".pdf");
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
			file=new File("Downloadables/Awards/Award"+res.getPubId()+".pdf");
			try ( FileOutputStream fos = new FileOutputStream(file); )
			{
				byte[] decoder = Base64.getDecoder().decode(pubPaperAwardPath);
				fos.write(decoder);
				res.setPubPaperAwardPath("Downloadables/Awards/Award"+res.getPubId()+".pdf");
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
	}

	public void updateEngineeringActivity(int pubId, String pubTitle, String pubAbstract, String pubKeywords, Date pubDate,
			String pubNote, String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL,
			String pubDBLP, String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String engActInstitName,
			String engActNumber, String engActReportType) {
		final Optional<EngineeringActivity> res = this.repo.findById(pubId);
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
				file=new File("Downloadables/PDFs/PDF"+res.get().getPubId()+".pdf");
				try ( FileOutputStream fos = new FileOutputStream(file); )
				{
					byte[] decoder = Base64.getDecoder().decode(pubPDFPath);
					fos.write(decoder);
					res.get().setPubPDFPath("Downloadables/PDFs/PDF"+res.get().getPubId()+".pdf");
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
				file=new File("Downloadables/Awards/Award"+res.get().getPubId()+".pdf");
				try ( FileOutputStream fos = new FileOutputStream(file); )
				{
					byte[] decoder = Base64.getDecoder().decode(pubPaperAwardPath);
					fos.write(decoder);
					res.get().setPubPaperAwardPath("Downloadables/Awards/Award"+res.get().getPubId()+".pdf");
				}
				catch (Exception e) 
				{
			    	res.get().setPubPaperAwardPath("");
			    	e.printStackTrace();
			    }
			}
			if(!pubType.toString().isEmpty())
				res.get().setPubType(pubType);
			//EngineeringActivity fields
			if(!engActInstitName.isEmpty())
				res.get().setEngActInstitName(engActInstitName);
			if(!engActNumber.isEmpty())
				res.get().setEngActNumber(engActNumber);
			if(!engActReportType.isEmpty())
				res.get().setEngActReportType(engActReportType);
			this.repo.save(res.get());
		}
	}
}



