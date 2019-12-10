package com.spring.rest.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.rest.services.BookServ;
import com.spring.rest.entities.Book;
import com.spring.rest.entities.PublicationType;

@RestController
@CrossOrigin
public class BookCtrl {
	
	@Autowired
	private BookServ bookServ;
	
	//Get all entities
	@RequestMapping(value="/getBooks", method=RequestMethod.POST, headers="Accept=application/json")
	public List<Book> getAllBooks() {
		return bookServ.getAllBooks();
	}

	//Get one specific entity based on its Id
	@RequestMapping(value="/getBook", method=RequestMethod.POST, headers="Accept=application/json")
	public List<Book> getBook(int index) {
		return bookServ.getBook(index);
	}

	//Remove one specific entity based on its Id
	@RequestMapping(value="/removeBook", method=RequestMethod.POST, headers="Accept=application/json")
	public void removeBook(int index) {
		bookServ.removeBook(index);
	}

	//Creates one specific entity based on its fields (minus its relationship fields)
	@RequestMapping(value="/createBook", method=RequestMethod.POST, headers="Accept=application/json")
	public int createBook(String pubTitle, String pubAbstract,
			String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
			String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, PublicationType pubType, String bookEditor, String bookPublisher, String bookVolume, String bookSeries, String bookAddress, String bookEdition, String bookPages) {
		return bookServ.createBook(pubTitle, pubAbstract,
				pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN, 
				pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
				pubPaperAwardPath, pubType, bookEditor, bookPublisher, bookVolume, bookSeries, bookAddress, bookEdition, bookPages);
	}

	//Updates one specific entity based on its fields (minus its relationship fields)
	@RequestMapping(value="/updateBook", method=RequestMethod.POST, headers="Accept=application/json")
	public void updateBook(int pubId, String pubTitle, String pubAbstract,
			String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
			String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
			String pubPaperAwardPath, PublicationType pubType, String bookEditor, String bookPublisher, String bookVolume, String bookSeries, String bookAddress, String bookEdition, String bookPages) {
		bookServ.updateBook(pubId, pubTitle, pubAbstract,
				pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN, 
				pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
				pubPaperAwardPath, pubType, bookEditor, bookPublisher, bookVolume, bookSeries, bookAddress, bookEdition, bookPages);
	}


}
