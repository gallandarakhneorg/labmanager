package com.spring.rest.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.rest.PubProviderApplication;
import com.spring.rest.controller.JournalCtrl;
import com.spring.rest.entities.Journal;
import com.spring.rest.entities.ReadingCommitteeJournalPopularizationPaper;
import com.spring.rest.repository.JournalRepository;
import com.spring.rest.repository.ReadingCommitteeJournalPopularizationPaperRepository;

@Service
public class JournalServ {	
	
	@Autowired
	private JournalRepository repo;
	
	@Autowired
	private ReadingCommitteeJournalPopularizationPaperRepository pubRepo;

	public List<Journal> getAllJournals() {
		List<Journal> jours = repo.findAll();
		//fix json recursion here, going further than pubs should be unnecessary
		for(Journal j : jours)
		{
			preventJournalRecursion(j);
		}
		return jours;
	}

	public List<Journal> getJournal(int index) {
		final List<Journal> result = new ArrayList<Journal>();
		final Optional<Journal> res = repo.findById(index);
		if(res.isPresent()) {
			preventJournalRecursion(res.get());
			result.add(res.get());
		}
		
		//fix json recursion here, going further than pubs should be unnecessary
		
		return result;
	}

	public void removeJournal(int index) {
		Optional<Journal> j = repo.findById(index);
		if(j.isPresent())
		{
			//Deletion should be disabled if it still has pubs attached to it but just in case
			for(ReadingCommitteeJournalPopularizationPaper p : j.get().getJourPubs())
			{
				p.setReaComConfPopPapJournal(null);
				pubRepo.save(p);
			}
			repo.deleteById(index);
		}
	}

	public int createJournal(String jourName, String jourPublisher, String jourElsevier, String jourScimago, String jourWos) {
			
		final Journal res = new Journal();
		//Generic pub fields
		System.out.println("assigning fields");
		res.setJourName(jourName);
		res.setJourPublisher(jourPublisher);
		res.setJourElsevier(jourElsevier);
		res.setJourScimago(jourScimago);
		res.setJourWos(jourWos);
		res.setJourQuartil(getJournalQuartilInfo(jourScimago));

		if(res.getJourName().contains("LNCS") && res.getJourQuartil()=="2")
		{
			res.setJourQuartil("2 (LNCS)");
		}
		System.out.println("Saving");
		this.repo.save(res);
		
		System.out.println("exiting");
		return res.getJourId();
	}

	public void updateJournal(int pubId, String jourName, String jourPublisher, String jourElsevier, String jourScimago, String jourWos) {
		final Optional<Journal> res = this.repo.findById(pubId);
		if(res.isPresent()) {
			//Generic pub fields
			if(!jourName.isEmpty())
				res.get().setJourName(jourName);
			if(!jourPublisher.isEmpty())
				res.get().setJourPublisher(jourPublisher);
			if(!jourElsevier.isEmpty())
				res.get().setJourElsevier(jourElsevier);
			if(!jourScimago.isEmpty())
				res.get().setJourScimago(jourScimago);
				res.get().setJourQuartil(getJournalQuartilInfo(jourScimago));
			if(!jourWos.isEmpty())
				res.get().setJourWos(jourWos);

			if(res.get().getJourName().contains("LNCS") && res.get().getJourQuartil()=="2")
			{
				res.get().setJourQuartil("2 (LNCS)");
			}
			
			this.repo.save(res.get());
		}
	}

	public Set<Journal> getPubsJournal(int pubId) 
	{
		Set<Journal> jours = repo.findDistinctByJourPubsPubId(pubId);
		
		for(Journal j : jours) //There should only be one since its a onetoMany but just be to safe
		{
			preventJournalRecursion(j);
		}
		
		return jours;
	}
	
	public void preventJournalRecursion(Journal j)
	{
		Set<ReadingCommitteeJournalPopularizationPaper> pubs = j.getJourPubs();
		for(ReadingCommitteeJournalPopularizationPaper p : pubs)
		{
			p.setReaComConfPopPapJournal(null);
			p.setPubAuts(new HashSet<>());
		}
	}

	public void addJournalLink(int pubId, int jourId) {
		Optional<ReadingCommitteeJournalPopularizationPaper> pub = pubRepo.findById(pubId);
		Optional<Journal> jour = repo.findById(jourId);
		if(pub.isPresent() && jour.isPresent())
		{
			removeJournalLink(pubId);
			jour.get().getJourPubs().add(pub.get());
			pub.get().setReaComConfPopPapJournal(jour.get());
			repo.save(jour.get());
			pubRepo.save(pub.get());
		}
	}

	public void removeJournalLink(int pubId) {
		Optional<ReadingCommitteeJournalPopularizationPaper> pub = pubRepo.findById(pubId);
		Journal jour;

		if(pub.isPresent() && pub.get().getReaComConfPopPapJournal()!=null)
		{
			jour=pub.get().getReaComConfPopPapJournal();
			jour.getJourPubs().remove(pub.get());
			pub.get().setReaComConfPopPapJournal(null);
			repo.save(jour);
			pubRepo.save(pub.get());
		}
	}

//Review if optional as result when several result possible is a good idea
	public int getJournalIdByName(String jourName) {
		final List<Journal> result = new ArrayList<Journal>();
		final Optional<Journal> res = repo.findByJourName(jourName);
		if(res.isPresent()) {
			preventJournalRecursion(res.get());
			result.add(res.get());
		}
		
		
		if(!result.isEmpty())
		{
			return result.get(0).getJourId(); //We assume theres no name dupes
		}
		else
		{
			return 0;
		}
	}

	public String getJournalQuartilInfo(String scimagoID) {

		String result="0";
		
		BufferedImage image = getImageFromURL("https://www.scimagojr.com/journal_img.php?id="+scimagoID);
		int rgba=image.getRGB(5, 55);
		String r=Integer.toString((rgba>>16) & 0xff);
		switch(r)
		{
			case "164":
				result="1";
				break;
				
			case "232":
				result="2";
				break;
				
			case "251":
				result="3";
				break;
				
			case "221":
				result="4";
				break;
		}
		
		return result;
	}
	
	
	
	
	
	public static BufferedImage getImageFromURL(String url)
	{
		try
		{
			URL sciURL = new URL(url);
			BufferedImage image;
			
			if(PubProviderApplication.PROXYURL.compareTo("")!=0)
			{
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PubProviderApplication.PROXYURL, PubProviderApplication.PROXYPORT));			
				URLConnection connection = sciURL.openConnection(proxy);
				connection.connect();
				//InputStream in = new BufferedInputStream(sciURL.openStream());
				InputStream in = connection.getInputStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int n = 0;
				while (-1!=(n=in.read(buf)))
				{
				   out.write(buf, 0, n);
				}
				out.close();
				in.close();
				byte[] response = out.toByteArray();
				ByteArrayInputStream bis = new ByteArrayInputStream(response);
			    image = ImageIO.read(bis);
			}
			else //I assume this should be enough to work but given that Im behind a proxy I cant try it
			{ 
			    image = ImageIO.read(sciURL);
			}
			return image;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public Set<Journal> getJournalsByOrg(int index) {
		Set<Journal> jours = repo.findDistinctByJourPubsPubAutsAutOrgsResOrgResOrgId(index);
		//fix json recursion here, going further than pubs should be unnecessary
		for(Journal j : jours)
		{
			preventJournalRecursion(j);
		}
		return jours;
	}
	
	
	
	
	
	
	
	
	
}



