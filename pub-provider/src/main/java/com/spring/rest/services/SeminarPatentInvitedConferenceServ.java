package com.spring.rest.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.rest.entities.PublicationType;
import com.spring.rest.entities.SeminarPatentInvitedConference;
import com.spring.rest.repository.SeminarPatentInvitedConferenceRepository;

@Service
public class SeminarPatentInvitedConferenceServ {
	
	@Autowired
	private SeminarPatentInvitedConferenceRepository repo;

	public List<SeminarPatentInvitedConference> getAllSeminarPatentInvitedConferences() {
		return repo.findAll();
	}

	public List<SeminarPatentInvitedConference> getSeminarPatentInvitedConference(int index) {
		final List<SeminarPatentInvitedConference> result = new ArrayList<SeminarPatentInvitedConference>();
		final Optional<SeminarPatentInvitedConference> res = repo.findById(index);
		if(res.isPresent()) {
			result.add(res.get());
		}
		return result;
	}

	public void removeSeminarPatentInvitedConference(int index) {
		repo.deleteById(index);
	}

	public void createSeminarPatentInvitedConference(String pubTitle, String pubAbstract, String pubKeywords, Date pubDate, String pubNote,
			String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL, String pubDBLP,
			String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String semPatHowPub) {
		final SeminarPatentInvitedConference res = new SeminarPatentInvitedConference();
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
		//SeminarPatentInvitedConference fields
		res.setSemPatHowPub(semPatHowPub);
		this.repo.save(res);
	}

	public void updateSeminarPatentInvitedConference(int pubId, String pubTitle, String pubAbstract, String pubKeywords, Date pubDate,
			String pubNote, String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL,
			String pubDBLP, String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String semPatHowPub) {
		final Optional<SeminarPatentInvitedConference> res = this.repo.findById(pubId);
		if(res.isPresent()) {
			//Generic pub fields
			if(!pubTitle.isEmpty())
				res.get().setPubTitle(pubTitle);
			if(!pubAbstract.isEmpty())
				res.get().setPubAbstract(pubAbstract);
			if(pubKeywords.isEmpty())
				res.get().setPubKeywords(pubKeywords);
			if(pubDate != null)
				res.get().setPubDate(pubDate);
			if(pubNote.isEmpty())
				res.get().setPubNote(pubNote);
			if(pubAnnotations.isEmpty())
				res.get().setPubAnnotations(pubAnnotations);
			if(pubISBN.isEmpty())
				res.get().setPubISBN(pubISBN);
			if(pubISSN.isEmpty())
				res.get().setPubISSN(pubISSN);
			if(pubDOIRef.isEmpty())
				res.get().setPubDOIRef(pubDOIRef);
			if(pubURL.isEmpty())
				res.get().setPubURL(pubURL);
			if(pubDBLP.isEmpty())
				res.get().setPubDBLP(pubDBLP);
			if(pubPDFPath.isEmpty())
				res.get().setPubPDFPath(pubPDFPath);
			if(pubLanguage.isEmpty())
				res.get().setPubLanguage(pubLanguage);
			if(pubPaperAwardPath.isEmpty())
				res.get().setPubPaperAwardPath(pubPaperAwardPath);
			if(pubType.toString().isEmpty())
				res.get().setPubType(pubType);
			//SeminarPatentInvitedConference fields
			if(semPatHowPub.isEmpty())
				res.get().setSemPatHowPub(semPatHowPub);
			this.repo.save(res.get());
		}
	}
}



