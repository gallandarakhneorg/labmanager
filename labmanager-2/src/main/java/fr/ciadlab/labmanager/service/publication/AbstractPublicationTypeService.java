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

package fr.ciadlab.labmanager.service.publication;

import java.net.URL;
import java.sql.Date;
import java.util.Base64;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import fr.ciadlab.labmanager.service.AbstractService;
import fr.ciadlab.labmanager.utils.files.DownloadableFileManager;
import org.springframework.beans.factory.annotation.Autowired;

/** Provides tool for the implemenation of a service for a specific type of publication.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractPublicationTypeService extends AbstractService {

	private DownloadableFileManager downloadableFileManager;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param downloadableFileManager downloadable file manager.
	 */
	public AbstractPublicationTypeService(@Autowired DownloadableFileManager downloadableFileManager) {
		this.downloadableFileManager = downloadableFileManager;
	}

	/** Update the book chapter with the given identifier.
	 * This function do not save the publication into the database. You must call the saving function
	 * explicitly.
	 *
	 * @param publication the publication to update.
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
	 */
	protected void updatePublicationNoSave(Publication publication, String title, PublicationType type, Date date,
			String abstractText, String keywords, String doi, String isbn, String issn, URL dblpUrl,
			URL extraUrl, PublicationLanguage language, String pdfContent, String awardContent,
			URL pathToVideo) {
		updatePublicationNoSave(publication, title, type, date, abstractText, keywords, doi, isbn,
				issn, dblpUrl.toExternalForm(), extraUrl.toExternalForm(), language, pdfContent, awardContent,
				pathToVideo.toExternalForm());
	}

	/** Update the book chapter with the given identifier.
	 * This function do not save the publication into the database. You must call the saving function
	 * explicitly.
	 *
	 * @param publication the publication to update.
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
	 */
	protected void updatePublicationNoSave(Publication publication, String title, PublicationType type, Date date,
			String abstractText, String keywords, String doi, String isbn, String issn, String dblpUrl,
			String extraUrl, PublicationLanguage language, String pdfContent, String awardContent,
			String pathToVideo) {
		if (!Strings.isNullOrEmpty(title)) {
			publication.setTitle(title);
		}
		if (type != null) {
			if (type.getInstanceType().equals(publication.getClass())) {
				throw new IllegalArgumentException("The publication type does not corresponds to the implementation class of the publication"); //$NON-NLS-1$
			}
			publication.setType(type);
		}
		if (date != null) {
			publication.setPublicationDate(date);
		}
		publication.setAbstractText(Strings.emptyToNull(abstractText));
		publication.setKeywords(Strings.emptyToNull(keywords));
		publication.setDOI(Strings.emptyToNull(doi));
		// TODO: Make the design better by creating separate classes for publications w/ and w/o ISBN/ISSN
		if (!(publication instanceof JournalPaper)) {
			publication.setISBN(Strings.emptyToNull(isbn));
			publication.setISSN(Strings.emptyToNull(issn));
		}
		publication.setDblpURL(Strings.emptyToNull(dblpUrl));
		publication.setExtraURL(Strings.emptyToNull(extraUrl));
		publication.setMajorLanguage(language);
		publication.setVideoURL(Strings.emptyToNull(pathToVideo));

		if (Strings.isNullOrEmpty(pdfContent)) {
			publication.setPathToDownloadablePDF(null);
			try {
				this.downloadableFileManager.deleteDownloadablePublicationPdfFile(publication.getId());
			} catch (Exception ex) {
				getLogger().error(ex.getLocalizedMessage(), ex);
			}
		} else {
			try {
				final String url = this.downloadableFileManager.saveDownloadablePublicationPdfFile(
						publication.getId(), pdfContent);
				publication.setPathToDownloadablePDF(url);
			} catch (Exception ex) {
				getLogger().error(ex.getLocalizedMessage(), ex);
				publication.setPathToDownloadablePDF(null);
			}
		}

		if (Strings.isNullOrEmpty(awardContent)) {
			publication.setPathToDownloadableAwardCertificate(null);
			try {
				this.downloadableFileManager.deleteDownloadableAwardPdfFile(publication.getId());
			} catch (Exception ex) {
				getLogger().error(ex.getLocalizedMessage(), ex);
			}
		} else {
			try {
				final String url = this.downloadableFileManager.saveDownloadableAwardPdfFile(
						publication.getId(), awardContent);
				publication.setPathToDownloadableAwardCertificate(url);
			} catch (Exception ex) {
				getLogger().error(ex.getLocalizedMessage(), ex);
				publication.setPathToDownloadableAwardCertificate(null);
			}
		}
	}

}
