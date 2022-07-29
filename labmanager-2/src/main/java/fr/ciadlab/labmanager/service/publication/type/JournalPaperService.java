/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.service.publication.type;

import java.sql.Date;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import fr.ciadlab.labmanager.repository.publication.type.JournalPaperRepository;
import fr.ciadlab.labmanager.service.publication.AbstractPublicationTypeService;
import fr.ciadlab.labmanager.utils.files.DownloadableFileManager;
import org.springframework.beans.factory.annotation.Autowired;

/** Service for managing journal paper.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class JournalPaperService extends AbstractPublicationTypeService {

	private JournalPaperRepository repository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param downloadableFileManager downloadable file manager.
	 * @param repository the repository for this service.
	 */
	public JournalPaperService(@Autowired DownloadableFileManager downloadableFileManager,
			@Autowired JournalPaperRepository repository) {
		super(downloadableFileManager);
		this.repository = repository;
	}

	/** Replies all the journal papers.
	 *
	 * @return the journal papers.
	 */
	public List<JournalPaper> getAllJournalPapers() {
		return this.repository.findAll();
	}

	/** Replies the journal paper with the given identifier.
	 *
	 * @param identifier the identifier of the journal paper.
	 * @return the journal paper or {@code null}.
	 */
	public JournalPaper getJournalPaper(int identifier) {
		final Optional<JournalPaper> byId = this.repository.findById(Integer.valueOf(identifier));
		return byId.orElse(null);
	}

	/** Create a journal paper.
	 *
	 * @param publication the publication to copy.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 * @return the created journal paper.
	 */
	public JournalPaper createJournalPaper(Publication publication, String volume, String number, String pages) {
		return createJournalPaper(publication, volume, number, pages, true);
	}

	/** Create a journal paper.
	 *
	 * @param publication the publication to copy.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 * @param saveInDb {@code true} for saving the publication in the database.
	 * @return the created journal paper.
	 */
	public JournalPaper createJournalPaper(Publication publication, String volume, String number, String pages,
			boolean saveInDb) {
		final JournalPaper res = new JournalPaper(publication, volume, number, pages);
		if (saveInDb) {
			this.repository.save(res);
		}
		return res;
	}

	/** Update the journal paper with the given identifier.
	 *
	 * @param pubId identifier of the paper to change.
	 * @param title the new title of the publication, never {@code null} or empty.
	 * @param type the new type of publication, never {@code null}.
	 * @param date the new date of publication, never {@code null}.
	 * @param abstractText the new text of the abstract.
	 * @param keywords the new list of keywords.
	 * @param doi the new DOI number.
	 * @param isbn the new ISBN number.
	 * @param issn the new ISSN number.
	 * @param dblpUrl the new URL to the DBLP page of the publication.
	 * @param extraUrl the new URL to the page of the publication.
	 * @param language the new major language of the publication.
	 * @param pdfContent the content of the publication PDF that is encoded in {@link Base64}. The content will be saved into
	 *     the dedicated folder for PDF files.
	 * @param awardContent the content of the publication award certificate that is encoded in {@link Base64}. The content will be saved into
	 *     the dedicated folder for PDF files.
	 * @param pathToVideo the path that allows to download the video of the publication.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 */
	public void updateJournalPaper(int pubId,
			String title, PublicationType type, Date date, String abstractText, String keywords,
			String doi, String isbn, String issn, String dblpUrl, String extraUrl,
			PublicationLanguage language, String pdfContent, String awardContent, String pathToVideo,
			String volume, String number, String pages) {
		final Optional<JournalPaper> res = this.repository.findById(Integer.valueOf(pubId));
		if (res.isPresent()) {
			final JournalPaper paper = res.get();

			updatePublicationNoSave(paper, title, type, date,
					abstractText, keywords, doi, isbn, issn, dblpUrl,
					extraUrl, language, pdfContent, awardContent,
					pathToVideo);

			paper.setVolume(Strings.emptyToNull(volume));
			paper.setNumber(Strings.emptyToNull(number));
			paper.setPages(Strings.emptyToNull(pages));

			this.repository.save(res.get());
		}
	}

	/** Remove the journal paper from the database.
	 *
	 * @param identifier the identifier of the journal paper to be removed.
	 */
	public void removeJournalPaper(int identifier) {
		this.repository.deleteById(Integer.valueOf(identifier));
	}

}
