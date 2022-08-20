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
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.publication.type.KeyNote;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.repository.publication.type.KeyNoteRepository;
import fr.ciadlab.labmanager.service.publication.AbstractPublicationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for managing keynotes.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class KeyNoteService extends AbstractPublicationTypeService {

	private KeyNoteRepository repository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param downloadableFileManager downloadable file manager.
	 * @param repository the repository for this service.
	 */
	public KeyNoteService(
			@Autowired MessageSourceAccessor messages,
			@Autowired DownloadableFileManager downloadableFileManager,
			@Autowired KeyNoteRepository repository) {
		super(messages, downloadableFileManager);
		this.repository = repository;
	}

	/** Replies all the keynotes.
	 *
	 * @return the keynotes.
	 */
	public List<KeyNote> getAllKeyNotes() {
		return this.repository.findAll();
	}

	/** Replies the keynotes with the given identifier.
	 *
	 * @param identifier the identifier of the keynote.
	 * @return the keynote or {@code null}.
	 */
	public KeyNote getKeyNote(int identifier) {
		final Optional<KeyNote> byId = this.repository.findById(Integer.valueOf(identifier));
		return byId.orElse(null);
	}

	/** Create a keynote.
	 *
	 * @param publication the publication to copy.
	 * @param scientificEventName the name of the conference or the workshop.
	 * @param editors the list of the names of the editors. Each name may have the format {@code LAST, VON, FIRST} and the names may be separated
	 *     with {@code AND}.
	 * @param orga the name of the organization institution.
	 * @param address the geographical location of the event, usually a city and a country.
	 * @return the created keynote.
	 */
	public KeyNote createKeyNote(Publication publication, String scientificEventName, String editors, String orga, String address) {
		final KeyNote res = new KeyNote(publication, scientificEventName, editors, orga, address);
		this.repository.save(res);
		return res;
	}

	/** Update the keynote with the given identifier.
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
	 * @param scientificEventName the name of the conference or the workshop.
	 * @param editors the list of the names of the editors. Each name may have the format {@code LAST, VON, FIRST} and the names may be separated
	 *     with {@code AND}.
	 * @param orga the name of the organization institution.
	 * @param address the geographical location of the event, usually a city and a country.
	 */
	public void updateKeyNote(int pubId,
			String title, PublicationType type, LocalDate date, String abstractText, String keywords,
			String doi, String isbn, String issn, String dblpUrl, String extraUrl,
			PublicationLanguage language, String pdfContent, String awardContent, String pathToVideo,
			String scientificEventName, String editors, String orga, String address) {
		final Optional<KeyNote> res = this.repository.findById(Integer.valueOf(pubId));
		if (res.isPresent()) {
			final KeyNote paper = res.get();

			updatePublicationNoSave(paper, title, type, date,
					abstractText, keywords, doi, isbn, issn, dblpUrl,
					extraUrl, language, pdfContent, awardContent,
					pathToVideo);

			paper.setScientificEventName(Strings.emptyToNull(scientificEventName));
			paper.setEditors(Strings.emptyToNull(editors));
			paper.setOrganization(Strings.emptyToNull(orga));
			paper.setAddress(Strings.emptyToNull(address));

			this.repository.save(res.get());
		}
	}

	/** Remove the keynote from the database.
	 *
	 * @param identifier the identifier of the keynote to be removed.
	 */
	public void removeKeyNote(int identifier) {
		this.repository.deleteById(Integer.valueOf(identifier));
	}

}
