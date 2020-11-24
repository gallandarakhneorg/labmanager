package com.spring.rest.controller;

import java.sql.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.rest.services.UniversityDocumentServ;
import com.spring.rest.entities.PublicationType;
import com.spring.rest.entities.UniversityDocument;

@RestController
@CrossOrigin
public class UniversityDocumentCtrl {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UniversityDocumentServ uniDocServ;
	
	//Get all entities
	@RequestMapping(value="/getUniversityDocuments", method=RequestMethod.POST, headers="Accept=application/json")
	public List<UniversityDocument> getAllUniversityDocuments() {
		return uniDocServ.getAllUniversityDocuments();
	}

	//Get one specific entity based on its Id
	@RequestMapping(value="/getUniversityDocument", method=RequestMethod.POST, headers="Accept=application/json")
	public List<UniversityDocument> getUniversityDocument(int index) {
		return uniDocServ.getUniversityDocument(index);
	}

	//Remove one specific entity based on its Id
	@RequestMapping(value="/removeUniversityDocument", method=RequestMethod.POST, headers="Accept=application/json")
	public void removeUniversityDocument(int index) {
		uniDocServ.removeUniversityDocument(index);
	}

	//Creates one specific entity based on its fields (minus its relationship fields)
	@RequestMapping(value="/createUniversityDocument", method=RequestMethod.POST, headers="Accept=application/json")
	public int createUniversityDocument(String pubTitle, String pubAbstract,
			String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
			String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, PublicationType pubType, String uniDocSchoolName, String uniDocAddress) {
		return uniDocServ.createUniversityDocument(pubTitle, pubAbstract,
				pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN, 
				pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
				pubPaperAwardPath, pubType, uniDocAddress, uniDocSchoolName);
		//TMT 24/11/20 : use right parameters (wrong order)
	}

	//Updates one specific entity based on its fields (minus its relationship fields)
	@RequestMapping(value="/updateUniversityDocument", method=RequestMethod.POST, headers="Accept=application/json")
	public void updateUniversityDocument(int pubId, String pubTitle, String pubAbstract,
			String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
			String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, PublicationType pubType, String uniDocSchoolName, String uniDocAddress) {

	      this.logger.info("\n\n\n Reached the controller \n\n\n");
		uniDocServ.updateUniversityDocument(pubId, pubTitle, pubAbstract,
				pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN, 
				pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
				pubPaperAwardPath, pubType, uniDocAddress, uniDocSchoolName);
		//TMT 24/11/20 : use right parameters (wrong order)
	}	


}
