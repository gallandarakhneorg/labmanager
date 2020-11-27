package com.spring.rest.controller;

import com.spring.rest.entities.ProceedingsConference;
import com.spring.rest.entities.PublicationType;
import com.spring.rest.services.ProceedingsConferenceServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@RestController
@CrossOrigin
public class ProceedingsConferenceCtrl {

    @Autowired
    private ProceedingsConferenceServ proConfServ;

    //Get all entities
    @RequestMapping(value = "/getProceedingsConferences", method = RequestMethod.POST, headers = "Accept=application/json")
    public List<ProceedingsConference> getAllProceedingsConferences() {
        return proConfServ.getAllProceedingsConferences();
    }

    //Get one specific entity based on its Id
    @RequestMapping(value = "/getProceedingsConference", method = RequestMethod.POST, headers = "Accept=application/json")
    public List<ProceedingsConference> getProceedingsConference(int index) {
        return proConfServ.getProceedingsConference(index);
    }

    //Remove one specific entity based on its Id
    @RequestMapping(value = "/removeProceedingsConference", method = RequestMethod.POST, headers = "Accept=application/json")
    public void removeProceedingsConference(int index) {
        proConfServ.removeProceedingsConference(index);
    }

    //Creates one specific entity based on its fields (minus its relationship fields)
    @RequestMapping(value = "/createProceedingsConference", method = RequestMethod.POST, headers = "Accept=application/json")
    public int createProceedingsConference(String pubTitle, String pubAbstract,
                                           String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
                                           String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
                                           String pubPaperAwardPath, PublicationType pubType, String proConfBookNameProceedings, String proConfEditor, String proConfPages, String proConfOrganization, String proConfPublisher, String proConfAddress, String proConfSeries) {
        return proConfServ.createProceedingsConference(pubTitle, pubAbstract,
                pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN,
                pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
                pubPaperAwardPath, pubType, proConfAddress,
                proConfBookNameProceedings,
                proConfEditor,
                proConfOrganization,
                proConfPages,
                proConfPublisher,
                proConfSeries); //TMT 24/11/20 : use right parameters (wrong order)
    }

    //Updates one specific entity based on its fields (minus its relationship fields)
    @RequestMapping(value = "/updateProceedingsConference", method = RequestMethod.POST, headers = "Accept=application/json")
    public void updateProceedingsConference(int pubId, String pubTitle, String pubAbstract,
                                            String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
                                            String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
                                            String pubPaperAwardPath, PublicationType pubType, String proConfBookNameProceedings, String proConfEditor, String proConfPages, String proConfOrganization, String proConfPublisher, String proConfAddress, String proConfSeries) {
        proConfServ.updateProceedingsConference(pubId, pubTitle, pubAbstract,
                pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN,
                pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
                pubPaperAwardPath, pubType,
                proConfAddress,
                proConfBookNameProceedings,
                proConfEditor,
                proConfOrganization,
                proConfPages,
                proConfPublisher,
                proConfSeries);//TMT 24/11/20 : use right parameters (wrong order)
    }


}
