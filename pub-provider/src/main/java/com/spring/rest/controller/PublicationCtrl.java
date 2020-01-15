package com.spring.rest.controller;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.rest.entities.Publication;
import com.spring.rest.entities.PublicationType;
import com.spring.rest.services.PublicationServ;

@RestController
@CrossOrigin
public class PublicationCtrl {
	
	@Autowired
	private PublicationServ pubServ;

	//Get all entities
	@RequestMapping(value="/getgetPublications", method=RequestMethod.GET, headers="Accept=application/json")
	public List<Publication> getgetAllPublications() {
		return pubServ.getAllPublications();
	}
	
	//Get all entities
	@RequestMapping(value="/getPublications", method=RequestMethod.POST, headers="Accept=application/json")
	public List<Publication> getAllPublications() {
		return pubServ.getAllPublications();
	}

	//Get one specific entity based on its Id
	@RequestMapping(value="/getPublication", method=RequestMethod.POST, headers="Accept=application/json")
	public List<Publication> getPublication(int index) {
		return pubServ.getPublication(index);
	}

	//Remove one specific entity based on its Id
	@RequestMapping(value="/removePublication", method=RequestMethod.POST, headers="Accept=application/json")
	public void removePublication(int index) {
		pubServ.removePublication(index);
	}

	//Creates one specific entity based on its fields (minus its relationship fields)
	//made it so that createXXXX gives the Id of the created publication back for conveniance
	@RequestMapping(value="/createPublication", method=RequestMethod.POST, headers="Accept=application/json")
	public int createPublication(String pubTitle, String pubAbstract,
			String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
			String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, PublicationType pubType) {
		return pubServ.createPublication(pubTitle, pubAbstract,
				pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN, 
				pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
				pubPaperAwardPath, pubType);
	}

	//Updates one specific entity based on its fields (minus its relationship fields)
	@RequestMapping(value="/updatePublication", method=RequestMethod.POST, headers="Accept=application/json")
	public void updatePublication(int pubId, String pubTitle, String pubAbstract,
			String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
			String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, String pubType) {
		pubServ.updatePublication(pubId, pubTitle, pubAbstract,
				pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN, 
				pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
				pubPaperAwardPath, pubType);
	}
	
	//Import a bibTex file to the database. 
	//Returns a list of the IDs of the successfully imported publications.
	@RequestMapping(value="/importPublications", method=RequestMethod.POST, headers="Accept=application/json")
	public List<Integer> importPublications(String bibText) {
		return pubServ.importPublications(bibText);
	}

	//Export a bibTex file from a given json
	//The export contains a bibtex and an html export which can be extracted like in an html document
	@RequestMapping(value="/exportPublications", method=RequestMethod.POST, headers="Accept=application/json")
	public String exportPublications(String pubs) {
		if(pubs==null)
		{
			System.out.println("\n\nNo data received\n\n."); //This will pop up when the json is too big. 
		}
		return pubServ.exportPublications(pubs);
	}
	
	
	//Export a bibTex file from the database
	//Exports publications released between exportStart & exportEnd. What is being exported depends on exportContent.
	//exportContent format type "all" or "org:1754" or "aut:1487"... With the number being the ID of the concerned org or aut
	//The export contains a bibtex and an html export which can be extracted like in an html document
	@RequestMapping(value="/exportPublicationsFromDataBase", method=RequestMethod.POST, headers="Accept=application/json")
	public String exportPublicationsFromDataBase(String exportStart, String exportEnd, String exportContent) { 
		return pubServ.exportPublicationsFromDataBase(exportStart, exportEnd, exportContent);
	}

	//Get all entities in relation with one specific author
	@RequestMapping(value="/getLinkedPublications", method=RequestMethod.POST, headers="Accept=application/json")
	public Set<Publication> getLinkedPublications(int index) {
		return pubServ.getLinkedPublications(index);
	}


	//Gets all the publications of all members affiliated with this organization or its subOrganizations
	@RequestMapping(value="/getLinkedMembersPublications", method=RequestMethod.POST, headers="Accept=application/json")
	public Set<Publication> getLinkedMembersPublications(int index) {
		return pubServ.getLinkedMembersPublications(index);
	}
}


	
