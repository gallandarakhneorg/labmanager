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
 * you entered into with the CIAD.
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
import fr.ciadlab.labmanager.entities.publication.type.MiscDocument;
import fr.ciadlab.labmanager.repository.publication.type.MiscDocumentRepository;
import fr.ciadlab.labmanager.service.publication.AbstractPublicationTypeService;
import fr.ciadlab.labmanager.utils.files.DownloadableFileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service for managing miscellaneous documents.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class MiscDocumentService extends AbstractPublicationTypeService {

	private MiscDocumentRepository repository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param downloadableFileManager downloadable file manager.
	 * @param repository the repository for this service.
	 */
	public MiscDocumentService(@Autowired DownloadableFileManager downloadableFileManager,
			@Autowired MiscDocumentRepository repository) {
		super(downloadableFileManager);
		this.repository = repository;
	}

	/** Replies all the miscellaneous documents.
	 *
	 * @return the miscellaneous documents.
	 */
	public List<MiscDocument> getAllMiscDocuments() {
		return this.repository.findAll();
	}

	/** Replies the miscellaneous document with the given identifier.
	 *
	 * @param identifier the identifier of the miscellaneous document.
	 * @return the miscellaneous document or {@code null}.
	 */
	public MiscDocument getMiscDocument(int identifier) {
		final Optional<MiscDocument> byId = this.repository.findById(Integer.valueOf(identifier));
		return byId.orElse(null);
	}

	/** Create a miscellaneous document.
	 *
	 * @param publication the publication to copy.
	 * @param number the number that is attached to the document.
	 * @param howPublished a description of how the document is published. 
	 * @param type a description of the type of document.
	 * @param organization the name of the organization that has published the document.
	 * @param publisher the name of the publisher if any.
	 * @param address the geographical location of the organization that has published the document. It is usually a city, country pair.
	 * @return the created miscellaneous document.
	 */
	public MiscDocument createMiscDocument(Publication publication,
			String number, String howPublished, String type,
			String organization, String publisher, String address) {
		return createMiscDocument(publication, number, howPublished, type, organization, publisher, address, true);
	}

	/** Create a miscellaneous document.
	 *
	 * @param publication the publication to copy.
	 * @param number the number that is attached to the document.
	 * @param howPublished a description of how the document is published. 
	 * @param type a description of the type of document.
	 * @param organization the name of the organization that has published the document.
	 * @param publisher the name of the publisher if any.
	 * @param address the geographical location of the organization that has published the document. It is usually a city, country pair.
	 * @param saveInDb {@code true} for saving the publication in the database.
	 * @return the created miscellaneous document.
	 */
	public MiscDocument createMiscDocument(Publication publication,
			String number, String howPublished, String type,
			String organization, String publisher, String address, boolean saveInDb) {
		final MiscDocument res = new MiscDocument(publication, organization, address, howPublished,
				publisher, number, type);
		if (saveInDb) {
			this.repository.save(res);
		}
		return res;
	}

	/** Update the miscellaneous document with the given identifier.
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
	 * @param number the number that is attached to the document.
	 * @param howPublished a description of how the document is published. 
	 * @param miscDocumentType a description of the type of document.
	 * @param organization the name of the organization that has published the document.
	 * @param publisher the name of the publisher if any.
	 * @param address the geographical location of the organization that has published the document. It is usually a city, country pair.
	 */
	public void updateMiscDocument(int pubId,
			String title, PublicationType type, Date date, String abstractText, String keywords,
			String doi, String isbn, String issn, String dblpUrl, String extraUrl,
			PublicationLanguage language, String pdfContent, String awardContent, String pathToVideo,
			String number, String howPublished, String miscDocumentType,
			String organization, String publisher, String address) {
		final Optional<MiscDocument> res = this.repository.findById(Integer.valueOf(pubId));
		if (res.isPresent()) {
			final MiscDocument document = res.get();

			updatePublicationNoSave(document, title, type, date,
					abstractText, keywords, doi, isbn, issn, dblpUrl,
					extraUrl, language, pdfContent, awardContent,
					pathToVideo);

			document.setDocumentNumber(Strings.emptyToNull(number));
			document.setHowPublished(Strings.emptyToNull(howPublished));
			document.setDocumentType(Strings.emptyToNull(miscDocumentType));
			document.setOrganization(Strings.emptyToNull(organization));
			document.setPublisher(Strings.emptyToNull(publisher));
			document.setAddress(Strings.emptyToNull(address));

			this.repository.save(res.get());
		}
	}

	/** Remove the miscellaneous document from the database.
	 *
	 * @param identifier the identifier of the miscellaneous document to be removed.
	 */
	public void removeMiscDocument(int identifier) {
		this.repository.deleteById(Integer.valueOf(identifier));
	}

}
