/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the CIAD laboratory and the Université de Technologie
 * de Belfort-Montbéliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD-UTBM.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.service.publication.type;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.publication.type.JournalEdition;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.repository.publication.type.JournalEditionRepository;
import fr.ciadlab.labmanager.service.publication.AbstractPublicationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for managing editions of journal and journal special issues.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class JournalEditionService extends AbstractPublicationTypeService {

	private JournalEditionRepository repository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param downloadableFileManager downloadable file manager.
	 * @param repository the repository for this service.
	 */
	public JournalEditionService(
			@Autowired MessageSourceAccessor messages,
			@Autowired DownloadableFileManager downloadableFileManager,
			@Autowired JournalEditionRepository repository) {
		super(messages, downloadableFileManager);
		this.repository = repository;
	}

	/** Replies all the journal editions.
	 *
	 * @return the journal editions.
	 */
	public List<JournalEdition> getAllJournalEditions() {
		return this.repository.findAll();
	}

	/** Replies the journal edition with the given identifier.
	 *
	 * @param identifier the identifier of the journal edition.
	 * @return the journal edition or {@code null}.
	 */
	public JournalEdition getJournalEdition(int identifier) {
		final Optional<JournalEdition> byId = this.repository.findById(Integer.valueOf(identifier));
		return byId.orElse(null);
	}

	/** Create a journal edition.
	 *
	 * @param publication the publication to copy.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 * @param journal the associated journal.
	 * @return the created journal edition.
	 */
	public JournalEdition createJournalEdition(Publication publication, String volume, String number, String pages, Journal journal) {
		final JournalEdition res = new JournalEdition(publication, volume, number, pages);
		res.setJournal(journal);
		this.repository.save(res);
		return res;
	}

	/** Update the journal edition with the given identifier.
	 *
	 * @param pubId identifier of the paper to change.
	 * @param title the new title of the publication, never {@code null} or empty.
	 * @param type the new type of publication, never {@code null}.
	 * @param date the new date of publication. It may be {@code null}. In this case only the year should be considered.
	 * @param year the new year of the publication. 
	 * @param abstractText the new text of the abstract.
	 * @param keywords the new list of keywords.
	 * @param doi the new DOI number.
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
	 * @param journal the associated journal.
	 * @param pages the pages in the journal.
	 */
	public void updateJournalEdition(int pubId,
			String title, PublicationType type, LocalDate date, int year, String abstractText, String keywords,
			String doi, String dblpUrl, String extraUrl,
			PublicationLanguage language, String pdfContent, String awardContent, String pathToVideo,
			String volume, String number, String pages, Journal journal) {
		final Optional<JournalEdition> res = this.repository.findById(Integer.valueOf(pubId));
		if (res.isPresent()) {
			final JournalEdition edition = res.get();

			updatePublicationNoSave(edition, title, type, date, year,
					abstractText, keywords, doi, null, null, dblpUrl,
					extraUrl, language, pdfContent, awardContent,
					pathToVideo);

			edition.setVolume(Strings.emptyToNull(volume));
			edition.setNumber(Strings.emptyToNull(number));
			edition.setPages(Strings.emptyToNull(pages));
			
			edition.setJournal(journal);

			this.repository.save(res.get());
		}
	}

	/** Remove the journal edition from the database.
	 *
	 * @param identifier the identifier of the journal edition to be removed.
	 */
	public void removeJournalEdition(int identifier) {
		this.repository.deleteById(Integer.valueOf(identifier));
	}

}
