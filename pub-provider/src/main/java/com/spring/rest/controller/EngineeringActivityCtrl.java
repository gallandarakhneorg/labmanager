package com.spring.rest.controller;

import com.spring.rest.entities.EngineeringActivity;
import com.spring.rest.entities.PublicationType;
import com.spring.rest.services.EngineeringActivityServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@RestController
@CrossOrigin
public class EngineeringActivityCtrl {

    @Autowired
    private EngineeringActivityServ engActServ;

    //Get all entities
    @RequestMapping(value = "/getEngineeringActivitys", method = RequestMethod.POST, headers = "Accept=application/json")
    public List<EngineeringActivity> getAllEngineeringActivitys() {
        return engActServ.getAllEngineeringActivitys();
    }

    //Get one specific entity based on its Id
    @RequestMapping(value = "/getEngineeringActivity", method = RequestMethod.POST, headers = "Accept=application/json")
    public List<EngineeringActivity> getEngineeringActivity(int index) {
        return engActServ.getEngineeringActivity(index);
    }

    //Remove one specific entity based on its Id
    @RequestMapping(value = "/removeEngineeringActivity", method = RequestMethod.POST, headers = "Accept=application/json")
    public void removeEngineeringActivity(int index) {
        engActServ.removeEngineeringActivity(index);
    }

    //Creates one specific entity based on its fields (minus its relationship fields)
    @RequestMapping(value = "/createEngineeringActivity", method = RequestMethod.POST, headers = "Accept=application/json")
    public int createEngineeringActivity(String pubTitle, String pubAbstract,
                                         String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
                                         String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
                                         String pubPaperAwardPath, PublicationType pubType, String engActInstitName, String engActReportType, String engActNumber) {
        return engActServ.createEngineeringActivity(pubTitle, pubAbstract,
                pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN,
                pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
                pubPaperAwardPath, pubType, engActInstitName, engActNumber, engActReportType);
        //TMT 24/11/20 : use right parameters (wrong order)
    }

    //Updates one specific entity based on its fields (minus its relationship fields)
    @RequestMapping(value = "/updateEngineeringActivity", method = RequestMethod.POST, headers = "Accept=application/json")
    public void updateEngineeringActivity(int pubId, String pubTitle, String pubAbstract,
                                          String pubKeywords, Date pubDate, String pubNote, String pubAnnotations, String pubISBN, String pubISSN,
                                          String pubDOIRef, String pubURL, String pubDBLP, String pubPDFPath, String pubLanguage,
                                          String pubPaperAwardPath, PublicationType pubType, String engActInstitName, String engActReportType, String engActNumber) {
        engActServ.updateEngineeringActivity(pubId, pubTitle, pubAbstract,
                pubKeywords, pubDate, pubNote, pubAnnotations, pubISBN, pubISSN,
                pubDOIRef, pubURL, pubDBLP, pubPDFPath, pubLanguage,
                pubPaperAwardPath, pubType, engActInstitName, engActNumber, engActReportType);
        //TMT 24/11/20 : use right parameters (wrong order)
    }
}
