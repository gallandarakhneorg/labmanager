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
	 * @param configurator the configuration for the exporter.
	 * @return the HTML code.
	 */
	String getButtonToExportPublicationToBibTeX(int publicationId,
			ExporterConfigurator configurator);

	/** Replies the button for exporting the publication to RIS.
	 *
	 * @param publicationId the identifier of the publication.
	 * @param configurator the configuration for the exporter.
	 * @return the HTML code.
	 * @since 3.7
	 */
	String getButtonToExportPublicationToRIS(int publicationId,
			ExporterConfigurator configurator);

	/** Replies the button for exporting the publication to HTML page.
	 *
	 * @param publicationId the identifier of the publication.
	 * @param configurator the configuration for the exporter.
	 * @return the HTML code.
	 */
	String getButtonToExportPublicationToHtml(int publicationId,
			ExporterConfigurator configurator);

	/** Replies the button for exporting the publication to Open Document.
	 *
	 * @param publicationId the identifier of the publication.
	 * @param configurator the configuration for the exporter.
	 * @return the HTML code.
	 */
	String getButtonToExportPublicationToOpenDocument(int publicationId,
			ExporterConfigurator configurator);

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
