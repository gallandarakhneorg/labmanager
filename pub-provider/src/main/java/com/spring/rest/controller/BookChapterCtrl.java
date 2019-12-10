package com.spring.rest.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.rest.services.BookChapterServ;
import com.spring.rest.entities.BookChapter;
import com.spring.rest.entities.PublicationType;

@RestController
@CrossOrigin
public class BookChapterCtrl {
	
	@Autowired
	private BookChapterServ bookChapServ;
	
	//Get all entities
	@RequestMapping(value="/getBookChapters", method=RequestMethod.POST, headers="Accept=application/json")
	public List<BookChapter> getAllBookChapters() {
		return bookChapServ.getAllBookChapters();
	}

	//Get one specific entity based on its Id
	@RequestMapping(value="/getBookChapter", method=RequestMethod.POST, headers="Accept=application/json")
	public List<BookChapter> getBookChapter(int index) {
		return bookChapServ.getBookChapter(index);
	}

	//Remove one specific entity based on its Id
	@RequestMapping(value="/removeBookChapter", method=RequestMethod.POST, headers="Accept=application/json")
	public void removeBookChapter(int index) {
		bookChapServ.removeBookChapter(index);
	}

	//Creates one specific entity based on its fields (minus its relationship fields)
	@RequestMapping(value="/createBookChapter", method=RequestMethod.POST, headers="Accept=application/json")
	public int createBookChapter(String pubTitle, String pubAbstract,
			String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
			String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, PublicationType pubType, String bookEditor, String bookPublisher, String bookVolume, String bookSeries, String bookAddress, String bookEdition, String bookPages, String bookChapBookNameProceedings, String bookChapNumberOrName) {
		return bookChapServ.createBookChapter(pubTitle, pubAbstract,
				pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN, 
				pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
				pubPaperAwardPath, pubType, bookEditor, bookPublisher, bookVolume, bookSeries, bookAddress, bookEdition, bookPages, bookChapBookNameProceedings, bookChapNumberOrName);
	}

	//Updates one specific entity based on its fields (minus its relationship fields)
	@RequestMapping(value="/updateBookChapter", method=RequestMethod.POST, headers="Accept=application/json")
	public void updateBookChapter(int pubId, String pubTitle, String pubAbstract,
			String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
			String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, PublicationType pubType, String bookEditor, String bookPublisher, String bookVolume, String bookSeries, String bookAddress, String bookEdition, String bookPages, String bookChapBookNameProceedings, String bookChapNumberOrName) {
		bookChapServ.updateBookChapter(pubId, pubTitle, pubAbstract,
				pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN, 
				pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
				pubPaperAwardPath, pubType, bookEditor, bookPublisher, bookVolume, bookSeries, bookAddress, bookEdition, bookPages, bookChapBookNameProceedings, bookChapNumberOrName);
	}


}

