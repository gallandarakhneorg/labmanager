package com.spring.rest.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.rest.services.SeminarPatentInvitedConferenceServ;
import com.spring.rest.entities.PublicationType;
import com.spring.rest.entities.SeminarPatentInvitedConference;

@RestController
@CrossOrigin
public class SeminarPatentInvitedConferenceCtrl {
	
	@Autowired
	private SeminarPatentInvitedConferenceServ semPatServ;
	
	//Get all entities
	@RequestMapping(value="/getSeminarPatentInvitedConferences", method=RequestMethod.POST, headers="Accept=application/json")
	public List<SeminarPatentInvitedConference> getAllSeminarPatentInvitedConferences() {
		return semPatServ.getAllSeminarPatentInvitedConferences();
	}

	//Get one specific entity based on its Id
	@RequestMapping(value="/getSeminarPatentInvitedConference", method=RequestMethod.POST, headers="Accept=application/json")
	public List<SeminarPatentInvitedConference> getSeminarPatentInvitedConference(int index) {
		return semPatServ.getSeminarPatentInvitedConference(index);
	}

	//Remove one specific entity based on its Id
	@RequestMapping(value="/removeSeminarPatentInvitedConference", method=RequestMethod.POST, headers="Accept=application/json")
	public void removeSeminarPatentInvitedConference(int index) {
		semPatServ.removeSeminarPatentInvitedConference(index);
	}

	//Creates one specific entity based on its fields (minus its relationship fields)
	@RequestMapping(value="/createSeminarPatentInvitedConference", method=RequestMethod.POST, headers="Accept=application/json")
	public void createSeminarPatentInvitedConference(String pubTitle, String pubAbstract,
			String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
			String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, PublicationType pubType, String semPatHowPub) {
		semPatServ.createSeminarPatentInvitedConference(pubTitle, pubAbstract,
				pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN, 
				pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
				pubPaperAwardPath, pubType, semPatHowPub);
	}

	//Updates one specific entity based on its fields (minus its relationship fields)
	@RequestMapping(value="/updateSeminarPatentInvitedConference", method=RequestMethod.POST, headers="Accept=application/json")
	public void updateSeminarPatentInvitedConference(int pubId, String pubTitle, String pubAbstract,
			String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
			String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, PublicationType pubType, String semPatHowPub) {
		semPatServ.updateSeminarPatentInvitedConference(pubId, pubTitle, pubAbstract,
				pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN, 
				pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
				pubPaperAwardPath, pubType, semPatHowPub);
	}


}
