package com.spring.rest.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.rest.entities.PublicationType;
import com.spring.rest.entities.UniversityDocument;
import com.spring.rest.repository.UniversityDocumentRepository;

@Service
public class UniversityDocumentServ {
	
	@Autowired
	private UniversityDocumentRepository repo;

	public List<UniversityDocument> getAllUniversityDocuments() {
		return repo.findAll();
	}

	public List<UniversityDocument> getUniversityDocument(int index) {
		final List<UniversityDocument> result = new ArrayList<UniversityDocument>();
		final Optional<UniversityDocument> res = repo.findById(index);
		if(res.isPresent()) {
			result.add(res.get());
		}
		return result;
	}

	public void removeUniversityDocument(int index) {
		repo.deleteById(index);
	}

	public void createUniversityDocument(String pubTitle, String pubAbstract, String pubKeywords, Date pubDate, String pubNote,
			String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL, String pubDBLP,
			String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String uniDocAddress, String uniDocSchoolName) {
		final UniversityDocument res = new UniversityDocument();
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
		//UniversityDocument fields
		res.setUniDocAddress(uniDocAddress);
		res.setUniDocSchoolName(uniDocSchoolName);
		this.repo.save(res);
	}

	public void updateUniversityDocument(int pubId, String pubTitle, String pubAbstract, String pubKeywords, Date pubDate,
			String pubNote, String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL,
			String pubDBLP, String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String uniDocAddress, String uniDocSchoolName) {
		final Optional<UniversityDocument> res = this.repo.findById(pubId);
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
			//UniversityDocument fields
			if(!uniDocAddress.isEmpty())
				res.get().setUniDocAddress(uniDocAddress);
			if(!uniDocSchoolName.isEmpty())
				res.get().setUniDocSchoolName(uniDocSchoolName);
			this.repo.save(res.get());
		}
	}
}



