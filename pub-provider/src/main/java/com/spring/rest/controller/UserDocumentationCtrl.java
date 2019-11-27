package com.spring.rest.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.rest.services.UserDocumentationServ;
import com.spring.rest.entities.PublicationType;
import com.spring.rest.entities.UserDocumentation;

@RestController
@CrossOrigin
public class UserDocumentationCtrl{
	
	@Autowired
	private UserDocumentationServ userDocServ;
	
	//Get all entities
	@RequestMapping(value="/getUserDocumentations", method=RequestMethod.POST, headers="Accept=application/json")
	public List<UserDocumentation> getAllUserDocumentations() {
		return userDocServ.getAllUserDocumentations();
	}

	//Get one specific entity based on its Id
	@RequestMapping(value="/getUserDocumentation", method=RequestMethod.POST, headers="Accept=application/json")
	public List<UserDocumentation> getUserDocumentation(int index) {
		return userDocServ.getUserDocumentation(index);
	}

	//Remove one specific entity based on its Id
	@RequestMapping(value="/removeUserDocumentation", method=RequestMethod.POST, headers="Accept=application/json")
	public void removeUserDocumentation(int index) {
		userDocServ.removeUserDocumentation(index);
	}

	//Creates one specific entity based on its fields (minus its relationship fields)
	@RequestMapping(value="/createUserDocumentation", method=RequestMethod.POST, headers="Accept=application/json")
	public void createUserDocumentation(String pubTitle, String pubAbstract,
			String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
			String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, PublicationType pubType, String userDocOrganization, String userDocAddress, String userDocEdition, String userDocPublisher) {
		userDocServ.createUserDocumentation(pubTitle, pubAbstract,
				pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN, 
				pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
				pubPaperAwardPath, pubType, userDocOrganization, userDocAddress, userDocEdition, userDocPublisher);
	}

	//Updates one specific entity based on its fields (minus its relationship fields)
	@RequestMapping(value="/updateUserDocumentation", method=RequestMethod.POST, headers="Accept=application/json")
	public void updateUserDocumentation(int pubId, String pubTitle, String pubAbstract,
			String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
			String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, PublicationType pubType, String userDocOrganization, String userDocAddress, String userDocEdition, String userDocPublisher) {
		userDocServ.updateUserDocumentation(pubId, pubTitle, pubAbstract,
				pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN, 
				pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
				pubPaperAwardPath, pubType, userDocOrganization, userDocAddress, userDocEdition, userDocPublisher);
	}


}
