/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.utbm.ciad.labmanager.services.publication.type;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import com.google.common.base.Strings;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.data.publication.PublicationLanguage;
import fr.utbm.ciad.labmanager.data.publication.PublicationType;
import fr.utbm.ciad.labmanager.data.publication.type.MiscDocument;
import fr.utbm.ciad.labmanager.data.publication.type.MiscDocumentRepository;
import fr.utbm.ciad.labmanager.services.publication.AbstractPublicationTypeService;
import fr.utbm.ciad.labmanager.utils.doi.DoiTools;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.utils.io.hal.HalTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param downloadableFileManager downloadable file manager.
	 * @param doiTools the tools for manipulating the DOI.
	 * @param halTools the tools for manipulating the HAL ids.
	 * @param repository the repository for this service.
	 */
	public MiscDocumentService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired DownloadableFileManager downloadableFileManager,
			@Autowired DoiTools doiTools,
			@Autowired HalTools halTools,
			@Autowired MiscDocumentRepository repository) {
		super(messages, constants, downloadableFileManager, doiTools, halTools);
		this.repository = repository;
	}

	/** Replies all the miscellaneous documents.
	 *
	 * @return the miscellaneous documents.
	 */
	public List<MiscDocument> getAllMiscDocuments() {
		return this.repository.findAll();
	}

	/** Replies all the miscellaneous documents.
	 *
	 * @param filter the filter of miscellaneous documents.
	 * @return the miscellaneous documents.
	 * @since 4.0
	 */
	public List<MiscDocument> getAllMiscDocuments(Specification<MiscDocument> filter) {
		return this.repository.findAll(filter);
	}

	/** Replies all the miscellaneous documents.
	 *
	 * @param filter the filter of miscellaneous documents.
	 * @param sortOrder the order specification to use for sorting the publications.
	 * @return the miscellaneous documents.
	 * @since 4.0
	 */
	public List<MiscDocument> getAllMiscDocuments(Specification<MiscDocument> filter, Sort sortOrder) {
		return this.repository.findAll(filter, sortOrder);
	}

	/** Replies all the miscellaneous documents.
	 *
	 * @param sortOrder the order specification to use for sorting the publications.
	 * @return the miscellaneous documents.
	 * @since 4.0
	 */
	public List<MiscDocument> getAllMiscDocuments(Sort sortOrder) {
		return this.repository.findAll(sortOrder);
	}

	/** Replies all the miscellaneous documents.
	 *
	 * @param pageable the manager of pages.
	 * @return the miscellaneous documents.
	 * @since 4.0
	 */
	public Page<MiscDocument> getAllMiscDocuments(Pageable pageable) {
		return this.repository.findAll(pageable);
	}

	/** Replies all the miscellaneous documents.
	 *
	 * @param pageable the manager of pages.
	 * @param filter the filter of miscellaneous documents.
	 * @return the miscellaneous documents.
	 * @since 4.0
	 */
	public Page<MiscDocument> getAllMiscDocuments(Pageable pageable, Specification<MiscDocument> filter) {
		return this.repository.findAll(filter, pageable);
	}

	/** Replies the miscellaneous document with the given identifier.
	 *
	 * @param identifier the identifier of the miscellaneous document.
	 * @return the miscellaneous document or {@code null}.
	 */
	public MiscDocument getMiscDocument(int identifier) {
		return this.repository.findById(Integer.valueOf(identifier)).orElse(null);
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
		final var res = new MiscDocument(publication, organization, address, howPublished,
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
	 * @param date the new date of publication. It may be {@code null}. In this case only the year should be considered.
	 * @param year the new year of the publication. 
	 * @param abstractText the new text of the abstract.
	 * @param keywords the new list of keywords.
	 * @param doi the new DOI number.
	 * @param halId the new HAL id.
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
			String title, PublicationType type, LocalDate date, int year, String abstractText, String keywords,
			String doi, String halId, String isbn, String issn, String dblpUrl, String extraUrl,
			PublicationLanguage language, String pdfContent, String awardContent, String pathToVideo,
			String number, String howPublished, String miscDocumentType,
			String organization, String publisher, String address) {
		final var res = this.repository.findById(Integer.valueOf(pubId));
		if (res.isPresent()) {
			final var document = res.get();

			updatePublicationNoSave(document, title, type, date, year,
					abstractText, keywords, doi, halId, isbn, issn, dblpUrl,
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
