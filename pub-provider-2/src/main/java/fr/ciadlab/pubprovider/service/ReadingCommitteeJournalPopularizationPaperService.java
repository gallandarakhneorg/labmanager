package fr.ciadlab.pubprovider.service;

import fr.ciadlab.pubprovider.PubProviderApplication;
import fr.ciadlab.pubprovider.entities.Author;
import fr.ciadlab.pubprovider.entities.Publication;
import fr.ciadlab.pubprovider.entities.PublicationType;
import fr.ciadlab.pubprovider.entities.ReadingCommitteeJournalPopularizationPaper;
import fr.ciadlab.pubprovider.repository.JournalRepository;
import fr.ciadlab.pubprovider.repository.ReadingCommitteeJournalPopularizationPaperRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ReadingCommitteeJournalPopularizationPaperService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ReadingCommitteeJournalPopularizationPaperRepository repo;

    @Autowired
    private JournalRepository journalRepository;

    public List<ReadingCommitteeJournalPopularizationPaper> getAllReadingCommitteeJournalPopularizationPapers() {
        return repo.findAll();
    }

    public ReadingCommitteeJournalPopularizationPaper getReadingCommitteeJournalPopularizationPaper(int index) {
        Optional<ReadingCommitteeJournalPopularizationPaper> byId = repo.findById(index);
        return byId.orElse(null);
    }

    public void removeReadingCommitteeJournalPopularizationPaper(int index) {
        repo.deleteById(index);
    }

    public ReadingCommitteeJournalPopularizationPaper createReadingCommitteeJournalPopularizationPaper(Publication p, String reaComConfPopPapVolume, String reaComConfPopPapNumber, String reaComConfPopPapPages, Integer journalId) {
        ReadingCommitteeJournalPopularizationPaper res = new ReadingCommitteeJournalPopularizationPaper(p, reaComConfPopPapVolume, reaComConfPopPapNumber, reaComConfPopPapPages);
        if(journalId != null && journalRepository.findById(journalId).isPresent())
        res.setReaComConfPopPapJournal(journalRepository.getOne(journalId));
        res = this.repo.save(res); //Id is generated on save so I gotta save once before setting these
        return res;
    }

    @Deprecated
    public int createReadingCommitteeJournalPopularizationPaper(String pubTitle, String pubAbstract, String pubKeywords, Date pubDate, String pubNote,
                                                                String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL, String pubDBLP,
                                                                String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String reaComConfPopPapNumber, String reaComConfPopPapPages, String reaComConfPopPapVolume) {
        final ReadingCommitteeJournalPopularizationPaper res = new ReadingCommitteeJournalPopularizationPaper();
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
        //ReadingCommitteeJournalPopularizationPaper fields
        res.setReaComConfPopPapNumber(reaComConfPopPapNumber);
        res.setReaComConfPopPapPages(reaComConfPopPapPages);
        res.setReaComConfPopPapVolume(reaComConfPopPapVolume);

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

    public void updateReadingCommitteeJournalPopularizationPaper(int pubId, String pubTitle, String pubAbstract, String pubKeywords, Date pubDate,
                                                                 String pubNote, String pubAnnotations, String pubISBN, String pubISSN, String pubDOIRef, String pubURL,
                                                                 String pubDBLP, String pubPDFPath, String pubLanguage, String pubPaperAwardPath, PublicationType pubType, String reaComConfPopPapNumber, String reaComConfPopPapPages, String reaComConfPopPapVolume, Integer journalId) {
        final Optional<ReadingCommitteeJournalPopularizationPaper> res = this.repo.findById(pubId);
        File file;
        if (res.isPresent()) {
            //Generic pub fields
            if (pubTitle != null && !pubTitle.isEmpty())
                res.get().setPubTitle(pubTitle);
            if (pubAbstract  != null && !pubAbstract.isEmpty())
                res.get().setPubAbstract(pubAbstract);
            if (pubKeywords != null && !pubKeywords.isEmpty())
                res.get().setPubKeywords(pubKeywords);
            if (pubDate != null && pubDate != null)
                res.get().setPubDate(pubDate);
            if (pubNote != null && !pubNote.isEmpty())
                res.get().setPubNote(pubNote);
            if (pubAnnotations != null && !pubAnnotations.isEmpty())
                res.get().setPubAnnotations(pubAnnotations);
            if (pubISBN != null && !pubISBN.isEmpty())
                res.get().setPubISBN(pubISBN);
            if (pubISSN != null && !pubISSN.isEmpty())
                res.get().setPubISSN(pubISSN);
            if (pubDOIRef != null && !pubDOIRef.isEmpty())
                res.get().setPubDOIRef(pubDOIRef);
            if (pubURL != null && !pubURL.isEmpty())
                res.get().setPubURL(pubURL);
            if (pubDBLP != null && !pubDBLP.isEmpty())
                res.get().setPubDBLP(pubDBLP);

            if (pubPDFPath != null && !pubPDFPath.isEmpty()) {
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
            if (pubLanguage != null && !pubLanguage.isEmpty())
                res.get().setPubLanguage(pubLanguage);
            if (pubPaperAwardPath != null && !pubPaperAwardPath.isEmpty()) {
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
            if (pubType != null && !pubType.toString().isEmpty())
                res.get().setPubType(pubType);
            //ReadingCommitteeJournalPopularizationPaper fields
            if(journalId != null && journalId != 0)
                res.get().setReaComConfPopPapJournal(journalRepository.getOne(journalId));
            if (reaComConfPopPapNumber != null && !reaComConfPopPapNumber.isEmpty())
                res.get().setReaComConfPopPapNumber(reaComConfPopPapNumber);
            if (reaComConfPopPapPages != null && !reaComConfPopPapPages.isEmpty())
                res.get().setReaComConfPopPapPages(reaComConfPopPapPages);
            if (reaComConfPopPapVolume != null && !reaComConfPopPapVolume.isEmpty())
                res.get().setReaComConfPopPapVolume(reaComConfPopPapVolume);

            this.repo.save(res.get());

        }
    }
}



