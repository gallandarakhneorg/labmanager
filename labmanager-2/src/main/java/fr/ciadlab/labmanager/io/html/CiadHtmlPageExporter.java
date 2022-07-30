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

package fr.ciadlab.labmanager.io.html;

import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import org.arakhne.afc.vmutil.locale.Locale;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Utilities for exporting publications to HTML content based on the CIAD standard HTML style.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Component
@Primary
public class CiadHtmlPageExporter extends AbstractCiadHtmlExporter implements HtmlPageExporter {

	private static String fixFilename(String filename) {
		String fn = filename.replace("/var/www/ciad-lab.fr", ""); //$NON-NLS-1$ //$NON-NLS-2$
		return fn;
	}

	private static void buildPdfDownloadLink(StringBuilder html, String path, String label) {
		html.append("<a class=\"btn btn-xs btn-success\" href=\""); //$NON-NLS-1$
		html.append(ROOT_URL);
		html.append(fixFilename(path));
		html.append("\"><i class=\"fa fa-file-pdf-o\">"); //$NON-NLS-1$
		html.append(label);
		html.append("</i></a>"); //$NON-NLS-1$
	}

	@Override
	public String getButtonToDownloadPublicationPDF(String pdfUrl) {
		final StringBuilder html = new StringBuilder();
		buildPdfDownloadLink(html, pdfUrl, "PDF"); //$NON-NLS-1$
		return html.toString();
	}

	@Override
	public String getButtonToDownloadPublicationAwardCertificate(String awardUrl) {
		final StringBuilder html = new StringBuilder();
		buildPdfDownloadLink(html, awardUrl, "Award"); //$NON-NLS-1$
		return html.toString();
	}

	private static void exportPublicationButton(StringBuilder html, String buttonClass, int id, String label) {
		html.append("<a class=\"btn btn-xs btn-success "); //$NON-NLS-1$
		html.append(buttonClass);
		html.append("\" href=\"\" data-href=\""); //$NON-NLS-1$
		html.append(id);
		html.append("\"><i class=\"fa fa-file-text-o\">"); //$NON-NLS-1$
		html.append(label);
		html.append("</i></a>"); //$NON-NLS-1$
	}

	@Override
	public String getButtonToExportPublicationToBibTeX(int publicationId) {
		final StringBuilder html = new StringBuilder();
		exportPublicationButton(html, "btBibtex", publicationId, "BibTeX"); //$NON-NLS-1$ //$NON-NLS-2$
		return html.toString();
	}

	@Override
	public String getButtonToExportPublicationToHtml(int publicationId) {
		final StringBuilder html = new StringBuilder();
		exportPublicationButton(html, "btHtml", publicationId, "HTML"); //$NON-NLS-1$ //$NON-NLS-2$
		return html.toString();
	}

	@Override
	public String getButtonToExportPublicationToOpenDocument(int publicationId) {
		final StringBuilder html = new StringBuilder();
		exportPublicationButton(html, "btWord", publicationId, "ODT"); //$NON-NLS-1$ //$NON-NLS-2$
		return html.toString();
	}

	@Override
	public String getButtonToEditPublication(int publicationId) {
		final StringBuilder html = new StringBuilder();
		html.append("<a class=\"btn btn-xs btn-success\" href=\"/SpringRestHibernate/addPublication?publicationId="); //$NON-NLS-1$
		html.append(publicationId);
		html.append("\" <i class=\"fa fa-edit\">"); //$NON-NLS-1$
		html.append(Locale.getString("EDIT_BUTTON_LABEL")); //$NON-NLS-1$
		html.append("</i></a>"); //$NON-NLS-1$
		return html.toString();
	}

	@Override
	public String getButtonToDeletePublication(int publicationId) {
		final StringBuilder html = new StringBuilder();
		html.append("<a class=\"btn btn-xs btn-danger\" href=\"/SpringRestHibernate/deletePublication?publicationId="); //$NON-NLS-1$
		html.append(publicationId);
		html.append("\" <i class=\"fa fa-delete\">"); //$NON-NLS-1$
		html.append(Locale.getString("DELETE_BUTTON_LABEL")); //$NON-NLS-1$
		html.append("</i></a>"); //$NON-NLS-1$
		return html.toString();
	}


	@Override
	public String exportPublications(Iterable<Publication> publications, ExporterConfigurator configurator) throws Exception {
		assert configurator != null;
		if (publications == null) {
			return null;
		}
		final StringBuilder html = new StringBuilder();
		html.append("<ul>"); //$NON-NLS-1$
		for (final Publication publication : publications) {
			exportPublication(html, publication, configurator);
		}
		html.append("</ul>"); //$NON-NLS-1$
		return html.toString();
	}

	/** Export in HTML a single publication.
	 *
	 * @param html the receiver of the HTML.
	 * @param publication the publication, never {@code null}.
	 * @param configurator the configurator for the exporter.
	 */
	public void exportPublication(StringBuilder html, Publication publication, ExporterConfigurator configurator) {
		assert html != null;
		assert publication != null;
		assert configurator != null;
		html.append("<li align=\"justify\">"); //$NON-NLS-1$
		final java.util.Locale loc = java.util.Locale.getDefault();
		try {
			java.util.Locale.setDefault(publication.getMajorLanguage().getLocale());
			exportAuthors(html, publication, configurator);
			exportDescription(html, publication, configurator);
		} finally {
			java.util.Locale.setDefault(loc);
		}
		html.append("</li>"); //$NON-NLS-1$
	}

}
