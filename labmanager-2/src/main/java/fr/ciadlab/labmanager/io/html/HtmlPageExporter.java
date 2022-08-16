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

package fr.ciadlab.labmanager.io.html;

import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.ExporterConfigurator;

/** Utilities for exporting publications to HTML content for a website.
 * This exporter is not dedicated to standalone HTML documents but for a specific website.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public interface HtmlPageExporter extends HtmlExporter {

	/** Replies the button for downloading the PDF file of a publication.
	 *
	 * @param pdfUrl the URL of the PDF file of a publication.
	 * @return the HTML code.
	 */
	String getButtonToDownloadPublicationPDF(String pdfUrl);

	/** Replies the button for downloading the PDF file of a publication award.
	 *
	 * @param awardUrl the URL of the PDF file of a publication award.
	 * @return the HTML code.
	 */
	String getButtonToDownloadPublicationAwardCertificate(String awardUrl);

	/** Replies the button for exporting the publication to BibTeX.
	 *
	 * @param publicationId the identifier of the publication.
	 * @return the HTML code.
	 */
	String getButtonToExportPublicationToBibTeX(int publicationId);

	/** Replies the button for exporting the publication to HTML page.
	 *
	 * @param publicationId the identifier of the publication.
	 * @return the HTML code.
	 */
	String getButtonToExportPublicationToHtml(int publicationId);

	/** Replies the button for exporting the publication to Open Document.
	 *
	 * @param publicationId the identifier of the publication.
	 * @return the HTML code.
	 */
	String getButtonToExportPublicationToOpenDocument(int publicationId);

	/** Replies the button for editing the publication.
	 *
	 * @param publicationId the identifier of the publication.
	 * @return the HTML code.
	 */
	String getButtonToEditPublication(int publicationId);

	/** Replies the button for deleting the publication.
	 *
	 * @param publicationId the identifier of the publication.
	 * @return the HTML code.
	 */
	String getButtonToDeletePublication(int publicationId);

	/** Generate the HTML links for the given publication.
	 *
	 * @param publication the publication to export.
	 * @param configurator the export configurator.
	 * @return the HTML code for the links.
	 */
	String generateHtmlLinks(Publication publication, ExporterConfigurator configurator);

	/** Generate the HTML authors for the given publication.
	 *
	 * @param publication the publication to export.
	 * @param configurator the export configurator.
	 * @return the HTML code for the authors.
	 */
	String generateHtmlAuthors(Publication publication, ExporterConfigurator configurator);

	/** Generate the HTML publication details for the given publication.
	 *
	 * @param publication the publication to export.
	 * @param configurator the export configurator.
	 * @return the HTML code for the publication details.
	 */
	String generateHtmlPublicationDetails(Publication publication, ExporterConfigurator configurator);

}
