package com.spring.rest.services;

import com.spring.rest.PubProviderApplication;
import com.spring.rest.entities.Book;
import com.spring.rest.entities.Publication;
import com.spring.rest.entities.PublicationType;
import com.spring.rest.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class BookServ {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BookRepository repo;

    public List<Book> getAllBooks() {
        return repo.findAll();
    }

    public List<Book> getBook(int index) {
        List<Book> result = new ArrayList<Book>();
        final Optional<Book> res = repo.findById(index);
        if (res.isPresent()) {
            result.add(res.get());
        }
        return result;
    }

    public void removeBook(int index) {
        repo.deleteById(index);
    }

    public Book createBook(Publication p, String bookEditor, String bookPublisher,
                          String bookVolume, String bookSeries, String bookAddress, String bookEdition, String bookPages) {
        Book res = new Book(p, bookEditor, bookPublisher, bookVolume, bookSeries, bookAddress, bookEdition, bookPages);
        res = repo.save(res);
        return res;
    }
    @Deprecated
    public int createBook(String pubTitle, String pubAbstract, String pubKeywords, Date pubDate, String pubNote,
                          String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL, String pubDBLP,
                          String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String bookEditor, String bookPublisher,
                          String bookVolume, String bookSeries, String bookAddress, String bookEdition, String bookPages) {
        final Book res = new Book();
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
        //res.setPubPDFPath(pubPDFPath);
        res.setPubLanguage(pubLanguage);
        //res.setPubPaperAwardPath(pubPaperAwardPath);
        res.setPubType(pubType);
        //Book fields
        res.setBookEditor(bookEditor);
        res.setBookPublisher(bookPublisher);
        res.setBookVolume(bookVolume);
        res.setBookSeries(bookSeries);
        res.setBookAddress(bookAddress);
        res.setBookEdition(bookEdition);
        res.setBookPages(bookPages);
        this.repo.save(res);

        File file;
        if (!pubPDFPath.isEmpty()) {
            file = new File(PubProviderApplication.DownloadablesPath + "PDFs/PDF" + res.getPubId() + ".pdf");
            try (FileOutputStream fos = new FileOutputStream(file);) {
                byte[] decoder = Base64.getDecoder().decode(pubPDFPath);
                fos.write(decoder);
                res.setPubPDFPath(PubProviderApplication.DownloadablesPath + "PDFs/PDF" + res.getPubId() + ".pdf");
            } catch (Exception e) {
                res.setPubPDFPath("");
                e.printStackTrace();
                this.logger.error(e.getMessage(), e);
            }
        } else {
            res.setPubPDFPath("");
        }

        if (!pubPaperAwardPath.isEmpty()) {
            file = new File(PubProviderApplication.DownloadablesPath + "Awards/Award" + res.getPubId() + ".pdf");
            try (FileOutputStream fos = new FileOutputStream(file);) {
                byte[] decoder = Base64.getDecoder().decode(pubPaperAwardPath);
                fos.write(decoder);
                res.setPubPaperAwardPath(PubProviderApplication.DownloadablesPath + "Awards/Award" + res.getPubId() + ".pdf");
            } catch (Exception e) {
                res.setPubPaperAwardPath("");
                e.printStackTrace();
                this.logger.error(e.getMessage(), e);
            }
        } else {
            res.setPubPaperAwardPath("");
        }

        this.repo.save(res); //Id is generated on save so I gotta save once before setting these
        return res.getPubId();
    }

    public void updateBook(int pubId, String pubTitle, String pubAbstract, String pubKeywords, Date pubDate,
                           String pubNote, String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL,
                           String pubDBLP, String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String bookEditor,
                           String bookPublisher, String bookVolume, String bookSeries, String bookAddress, String bookEdition,
                           String bookPages) {
        final Optional<Book> res = this.repo.findById(pubId);
        File file;
        if (res.isPresent()) {
            //Generic pub fields
            if (!pubTitle.isEmpty())
                res.get().setPubTitle(pubTitle);
            if (!pubAbstract.isEmpty())
                res.get().setPubAbstract(pubAbstract);
            if (!pubKeywords.isEmpty())
                res.get().setPubKeywords(pubKeywords);
            if (pubDate != null)
                res.get().setPubDate(pubDate);
            if (!pubNote.isEmpty())
                res.get().setPubNote(pubNote);
            if (!pubAnnotations.isEmpty())
                res.get().setPubAnnotations(pubAnnotations);
            if (!pubISBN.isEmpty())
                res.get().setPubISBN(pubISBN);
            if (!pubISSN.isEmpty())
                res.get().setPubISSN(pubISSN);
            if (!pubDOIRef.isEmpty())
                res.get().setPubDOIRef(pubDOIRef);
            if (!pubURL.isEmpty())
                res.get().setPubURL(pubURL);
            if (!pubDBLP.isEmpty())
                res.get().setPubDBLP(pubDBLP);
            if (!pubPDFPath.isEmpty()) {
                file = new File(PubProviderApplication.DownloadablesPath + "PDFs/PDF" + res.get().getPubId() + ".pdf");
                try (FileOutputStream fos = new FileOutputStream(file);) {
                    byte[] decoder = Base64.getDecoder().decode(pubPDFPath);
                    fos.write(decoder);
                    res.get().setPubPDFPath(PubProviderApplication.DownloadablesPath + "PDFs/PDF" + res.get().getPubId() + ".pdf");
                } catch (Exception e) {
                    res.get().setPubPDFPath("");
                    e.printStackTrace();
                    this.logger.error(e.getMessage(), e);
                }
            }
            if (!pubLanguage.isEmpty())
                res.get().setPubLanguage(pubLanguage);
            if (!pubPaperAwardPath.isEmpty()) {
                file = new File(PubProviderApplication.DownloadablesPath + "Awards/Award" + res.get().getPubId() + ".pdf");
                try (FileOutputStream fos = new FileOutputStream(file);) {
                    byte[] decoder = Base64.getDecoder().decode(pubPaperAwardPath);
                    fos.write(decoder);
                    res.get().setPubPaperAwardPath(PubProviderApplication.DownloadablesPath + "Awards/Award" + res.get().getPubId() + ".pdf");
                } catch (Exception e) {
                    res.get().setPubPaperAwardPath("");
                    e.printStackTrace();
                    this.logger.error(e.getMessage(), e);
                }
            }
            if (!pubType.toString().isEmpty())
                res.get().setPubType(pubType);
            //Book fields
            if (!bookEditor.isEmpty())
                res.get().setBookEditor(bookEditor);
            if (!bookPublisher.isEmpty())
                res.get().setBookPublisher(bookPublisher);
            if (!bookVolume.isEmpty())
                res.get().setBookVolume(bookVolume);
            if (!bookSeries.isEmpty())
                res.get().setBookSeries(bookSeries);
            if (!bookAddress.isEmpty())
                res.get().setBookAddress(bookAddress);
            if (!bookEdition.isEmpty())
                res.get().setBookEdition(bookEdition);
            if (!bookPages.isEmpty())
                res.get().setBookPages(bookPages);
            this.repo.save(res.get());
        }
    }
}


