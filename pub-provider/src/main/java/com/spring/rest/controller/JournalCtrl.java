package com.spring.rest.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.rest.entities.Journal;
import com.spring.rest.services.JournalServ;

@RestController
@CrossOrigin
public class JournalCtrl {
	
	@Autowired
	private JournalServ jourServ;
	
	//Get all entities
	@RequestMapping(value="/getJournals", method=RequestMethod.POST, headers="Accept=application/json")
	public List<Journal> getAllJournals() {
		return jourServ.getAllJournals();
	}

	//Get one specific entity based on its Id
	@RequestMapping(value="/getJournal", method=RequestMethod.POST, headers="Accept=application/json")
	public List<Journal> getJournal(int index) {
		return jourServ.getJournal(index);
	}

	//Remove one specific entity based on its Id
	@RequestMapping(value="/removeJournal", method=RequestMethod.POST, headers="Accept=application/json")
	public void removeJournal(int index) {
		jourServ.removeJournal(index);
	}

	//Creates one specific entity based on its fields (minus its relationship fields)
	@RequestMapping(value="/createJournal", method=RequestMethod.POST, headers="Accept=application/json")
	public void createJournal(String jourName, String jourPublisher, String jourElsevier, String jourScimago, String jourWos) {
		jourServ.createJournal(jourName, jourPublisher, jourElsevier, jourScimago, jourWos);
	}

	//Updates one specific entity based on its fields (minus its relationship fields)
	@RequestMapping(value="/updateJournal", method=RequestMethod.POST, headers="Accept=application/json")
	public void updateJournal(int index, String jourName, String jourPublisher, String jourElsevier, String jourScimago, String jourWos) {
		jourServ.updateJournal(index, jourName, jourPublisher, jourElsevier, jourScimago, jourWos);
	}

	//Get one specific entity based on its Id
	@RequestMapping(value="/getPubsJournal", method=RequestMethod.POST, headers="Accept=application/json")
	public Set<Journal> getPubsJournal(int index) {
		return jourServ.getPubsJournal(index);
	}

	//Get one specific entity based on its Id
	@RequestMapping(value="/addJournalLink", method=RequestMethod.POST, headers="Accept=application/json")
	public void addJournalLink(int pubId, int jourId) {
		jourServ.addJournalLink(pubId, jourId);
	}

	//Get one specific entity based on its Id
	@RequestMapping(value="/removeJournalLink", method=RequestMethod.POST, headers="Accept=application/json")
	public void removeJournalLink(int pubId) {
		jourServ.removeJournalLink(pubId);
	}

}
