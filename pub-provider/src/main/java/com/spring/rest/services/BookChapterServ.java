package com.spring.rest.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.rest.entities.BookChapter;
import com.spring.rest.entities.PublicationType;
import com.spring.rest.repository.BookChapterRepository;

@Service
public class BookChapterServ {
	
	@Autowired
	private BookChapterRepository repo;

	public List<BookChapter> getAllBookChapters() {
		return repo.findAll();
	}

	public List<BookChapter> getBookChapter(int index) {
		final List<BookChapter> result = new ArrayList<BookChapter>();
		final Optional<BookChapter> res = repo.findById(index);
		if(res.isPresent()) {
			result.add(res.get());
		}
		return result;
	}

	public void removeBookChapter(int index) {
		repo.deleteById(index);
	}

	public void createBookChapter(String pubTitle, String pubAbstract, String pubKeywords, Date pubDate, String pubNote,
			String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL, String pubDBLP,
			String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String bookEditor, String bookPublisher,
			String bookVolume, String bookSeries, String bookAddress, String bookEdition, String bookPages,
			String bookChapBookNameProceedings, String bookChapNumberOrName) {
		final BookChapter res = new BookChapter();
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
		//Book fields
		res.setBookEditor(bookEditor);
		res.setBookPublisher(bookPublisher);
		res.setBookVolume(bookVolume);
		res.setBookSeries(bookSeries);
		res.setBookAddress(bookAddress);
		res.setBookEdition(bookEdition);
		res.setBookPages(bookPages);
		//Book Chapter fields
		res.setBookChapBookNameProceedings(bookChapBookNameProceedings);
		res.setBookChapNumberOrName(bookChapNumberOrName);
		this.repo.save(res);
	}

	public void updateBookChapter(int pubId, String pubTitle, String pubAbstract, String pubKeywords, Date pubDate,
			String pubNote, String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL,
			String pubDBLP, String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String bookEditor,
			String bookPublisher, String bookVolume, String bookSeries, String bookAddress, String bookEdition,
			String bookPages, String bookChapBookNameProceedings, String bookChapNumberOrName) {
		final Optional<BookChapter> res = this.repo.findById(pubId);
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
			//Book fields
			if(bookEditor.isEmpty())
				res.get().setBookEditor(bookEditor);
			if(bookPublisher.isEmpty())
				res.get().setBookPublisher(bookPublisher);
			if(bookVolume.isEmpty())
				res.get().setBookVolume(bookVolume);
			if(bookSeries.isEmpty())
				res.get().setBookSeries(bookSeries);
			if(bookAddress.isEmpty())
				res.get().setBookAddress(bookAddress);
			if(bookEdition.isEmpty())
				res.get().setBookEdition(bookEdition);
			if(bookPages.isEmpty())
				res.get().setBookPages(bookPages);
			//Book chapter fields
			if(bookChapBookNameProceedings.isEmpty())
				res.get().setBookChapBookNameProceedings(bookChapBookNameProceedings);
			if(bookChapNumberOrName.isEmpty())
				res.get().setBookChapNumberOrName(bookChapNumberOrName);
			this.repo.save(res.get());
		}
	}
}

