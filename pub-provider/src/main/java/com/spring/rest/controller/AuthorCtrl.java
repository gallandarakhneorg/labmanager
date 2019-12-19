package com.spring.rest.controller;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.rest.entities.Author;
import com.spring.rest.services.AuthorServ;

@RestController
@CrossOrigin
public class AuthorCtrl {
	
	@Autowired
	private AuthorServ autServ;
	
	//Get all entities
	@RequestMapping(value="/getgetAuthors", method=RequestMethod.GET, headers="Accept=application/json")
	public List<Author> getAllAuthors() {
		return autServ.getAllAuthors();
	}

	//Get all entities
	@RequestMapping(value="/getAuthors", method=RequestMethod.POST, headers="Accept=application/json")
	public List<Author> getgetAllAuthors() {
		return autServ.getAllAuthors();
	}

	//Get one specific entity based on its Id
	@RequestMapping(value="/getAuthor", method=RequestMethod.POST, headers="Accept=application/json")
	public List<Author> getAuthor(int index) {
		return autServ.getAuthor(index);
	}

	//Remove one specific entity based on its Id
	@RequestMapping(value="/removeAuthor", method=RequestMethod.POST, headers="Accept=application/json")
	public void removeAuthor(int index) {
		autServ.removeAuthor(index);
	}

	//Creates one specific entity based on its fields (minus its relationship fields)
	@RequestMapping(value="/createAuthor", method=RequestMethod.POST, headers="Accept=application/json")
	public int createAuthor(String autFirstName, String autLastName, Date autBirth, String autMail, String autPic) {
		return autServ.createAuthor(autFirstName, autLastName, autBirth, autMail, autPic);
	}

	//Updates one specific entity based on its fields (minus its relationship fields)
	@RequestMapping(value="/updateAuthor", method=RequestMethod.POST, headers="Accept=application/json")
	public void updateAuthor(int index, String autFirstName, String autLastName, Date autBirth, String autMail, String autPic) {
		autServ.updateAuthor(index, autFirstName, autLastName, autBirth, autMail, autPic);
	}

	
	
	//Creates a membership relation between 2 entities based on the relationship's fields and the concerned entities' Ids
	@RequestMapping(value="/addAuthorship", method=RequestMethod.POST, headers="Accept=application/json")
	public void addAuthorship(int index, int pubId) {
		autServ.addAuthorship(index, pubId);
	}

	//Removes a membership relation between 2 entities based on the concerned entities' Ids
	@RequestMapping(value="/removeAuthorship", method=RequestMethod.POST, headers="Accept=application/json")
	public void removeAuthorship(int index, int pubId) {
		autServ.removeAuthorship(index, pubId);
	}
	
	@RequestMapping(value="/updateAuthorPage", method=RequestMethod.POST, headers="Accept=application/json")
	public void updateAuthorPage(int index, boolean hasPage) {
		autServ.updateAuthorPage(index, hasPage);
	}

	//Get all entities in relation with one specific entity
	@RequestMapping(value="/getLinkedMembers", method=RequestMethod.POST, headers="Accept=application/json")
	public Set<Author> getLinkedMembers(int index) {
		return autServ.getLinkedMembers(index);
	}

	//Get all entities in relation with one specific entity
	@RequestMapping(value="/getDirectlyLinkedMembers", method=RequestMethod.POST, headers="Accept=application/json")
	public Set<Author> getDirectlyLinkedMembers(int index) {
		return autServ.getDirectlyLinkedMembers(index);
	}
	
	//Get all entities in relation with one specific entity
	@RequestMapping(value="/getLinkedAuthors", method=RequestMethod.POST, headers="Accept=application/json")
	public Set<Author> getLinkedAuthors(int index) {
		return autServ.getLinkedAuthors(index);
	}

	//Get one specific entity based on its Id
	@RequestMapping(value="/getAuthorIdByName", method=RequestMethod.POST, headers="Accept=application/json")
	public int getAuthorIdByName(String autFirstName, String autLastName) {
		return autServ.getAuthorIdByName(autFirstName, autLastName);
	}

	//Get all authors directly linked to an organization with a specific given status
	@RequestMapping(value="/getAuthorsByOrgStatus", method=RequestMethod.POST, headers="Accept=application/json")
	public Set<Author> getAuthorsByOrgStatus(String orgName, String status) {
		return autServ.getAuthorsByOrgStatus(orgName, status);
	}
}

// Legacy //

/*
	@GetMapping(value="/getFirstAuthor")
	public Author getFirstAuthor() {
		return autServ.getAuthor(1);
	  }
*/