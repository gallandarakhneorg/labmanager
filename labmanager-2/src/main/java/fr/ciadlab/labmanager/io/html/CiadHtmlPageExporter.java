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

import java.io.File;
import java.net.URI;
import java.net.URL;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.utils.doi.DoiTools;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

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

	private static final String MESSAGES_PREFIX = "ciadHtmlPageExporter."; //$NON-NLS-1$

	private static final String HTML_ATAG_0 = "<a href=\""; //$NON-NLS-1$

	private static final String HTML_ATAG_1 = "\">"; //$NON-NLS-1$

	private static final String HTML_ATAG_2 = "</a>"; //$NON-NLS-1$

	private static final String HTML_NEWLINE = "<br/>"; //$NON-NLS-1$

	/** Constructor.
	 *
	 * @param constants the accessor to the application constants.
	 * @param messages the accessor to the localized messages.
	 * @param doiTools the accessor to the DOI tools.
	 */
	public CiadHtmlPageExporter(@Autowired Constants constants, @Autowired MessageSourceAccessor messages, @Autowired DoiTools doiTools) {
		super(constants, messages, doiTools);
	}

	private void buildPdfDownloadLink(StringBuilder html, String path, String label) {
		final String jpeg = FileSystem.replaceExtension(new File(path), ".jpg").toString(); //$NON-NLS-1$
		html.append("<a class=\"btn btn-xs btn-success\" href=\"/"); //$NON-NLS-1$
		html.append(this.constants.getServerName());
		html.append("/"); //$NON-NLS-1$
		html.append(path);
		html.append("\"><i class=\"fa\"><img src=\"/"); //$NON-NLS-1$
		html.append(this.constants.getServerName());
		html.append("/"); //$NON-NLS-1$
		html.append(jpeg);
		html.append("\" class=\"publicationDetailsDownloadAttachment\" /><br/>"); //$NON-NLS-1$
		html.append(label);
		html.append("</i></a>"); //$NON-NLS-1$
	}

	@Override
	public String getButtonToDownloadPublicationPDF(String pdfUrl) {
		final StringBuilder html = new StringBuilder();
		if (!Strings.isNullOrEmpty(pdfUrl)) {
			buildPdfDownloadLink(html, pdfUrl, "PDF"); //$NON-NLS-1$
		}
		return html.toString();
	}

	@Override
	public String getButtonToDownloadPublicationAwardCertificate(String awardUrl) {
		final StringBuilder html = new StringBuilder();
		if (!Strings.isNullOrEmpty(awardUrl)) {
			buildPdfDownloadLink(html, awardUrl, "Award"); //$NON-NLS-1$
		}
		return html.toString();
	}

	private void exportPublicationButton(StringBuilder html, String buttonClass, String buttonIconClass,
			String tooltip, String endpoint, int id, String label, ExporterConfigurator configurator) {
		UriBuilder builder = new DefaultUriBuilderFactory().builder();
		builder = builder.path("/" + this.constants.getServerName() + "/" + endpoint); //$NON-NLS-1$ //$NON-NLS-2$
		builder = configurator.applyQueryParams(builder);
		builder = builder.queryParam("id", Integer.valueOf(id)); //$NON-NLS-1$
		builder = builder.queryParam("inAttachment", Boolean.TRUE); //$NON-NLS-1$
		html.append("<a class=\"btn btn-xs btn-success "); //$NON-NLS-1$
		html.append(buttonClass);
		html.append("\" href=\"").append(builder.build().toString()); //$NON-NLS-1$
		html.append("\" title=\""); //$NON-NLS-1$
		html.append(tooltip);
		html.append("\"><i class=\"fa-solid "); //$NON-NLS-1$
		html.append(buttonIconClass);
		html.append("\">"); //$NON-NLS-1$
		html.append(label);
		html.append("</i></a>"); //$NON-NLS-1$
	}

	@Override
	public String getButtonToExportPublicationToBibTeX(int publicationId, ExporterConfigurator configurator) {
		final StringBuilder html = new StringBuilder();
		if (publicationId != 0) {
			exportPublicationButton(html, "btBibtex", "fa-file-lines", //$NON-NLS-1$ //$NON-NLS-2$
					this.messages.getMessage("ciadHtmlPageExporter.exportToBibTeX"),  //$NON-NLS-1$
					Constants.EXPORT_BIBTEX_ENDPOINT,
					publicationId, "BibTeX", configurator); //$NON-NLS-1$
		}
		return html.toString();
	}

	@Override
	public String getButtonToExportPublicationToHtml(int publicationId, ExporterConfigurator configurator) {
		final StringBuilder html = new StringBuilder();
		if (publicationId != 0) {
			exportPublicationButton(html, "btHtml", "fa-file-code", //$NON-NLS-1$ //$NON-NLS-2$
					this.messages.getMessage("ciadHtmlPageExporter.exportToHtml"),  //$NON-NLS-1$
					Constants.EXPORT_HTML_ENDPOINT,
					publicationId, "HTML", configurator); //$NON-NLS-1$
		}
		return html.toString();
	}

	@Override
	public String getButtonToExportPublicationToOpenDocument(int publicationId, ExporterConfigurator configurator) {
		final StringBuilder html = new StringBuilder();
		if (publicationId != 0) {
			exportPublicationButton(html, "btWord", "fa-file-word", //$NON-NLS-1$ //$NON-NLS-2$
					this.messages.getMessage("ciadHtmlPageExporter.exportToOdt"),  //$NON-NLS-1$
					Constants.EXPORT_ODT_ENDPOINT,
					publicationId, "ODT", configurator); //$NON-NLS-1$
		}
		return html.toString();
	}

	@Override
	public String getButtonToEditPublication(int publicationId) {
		final StringBuilder html = new StringBuilder();
		if (publicationId != 0) {
			html.append("<a class=\"btn btn-xs btn-success\" href=\"/"); //$NON-NLS-1$
			html.append(this.constants.getServerName()).append("/"); //$NON-NLS-1$
			html.append("addPublication?publicationId="); //$NON-NLS-1$
			html.append(publicationId);
			html.append("\" <i class=\"fa fa-edit\">"); //$NON-NLS-1$
			html.append(this.messages.getMessage(MESSAGES_PREFIX + "EDIT_BUTTON_LABEL")); //$NON-NLS-1$
			html.append("</i></a>"); //$NON-NLS-1$
		}
		return html.toString();
	}

	@Override
	public String getButtonToDeletePublication(int publicationId) {
		final StringBuilder html = new StringBuilder();
		if (publicationId != 0) {
			html.append("<a class=\"btn btn-xs btn-danger\" href=\"/"); //$NON-NLS-1$
			html.append(this.constants.getServerName()).append("/deletePublication?publicationId="); //$NON-NLS-1$
			html.append(publicationId);
			html.append("\" <i class=\"fa fa-delete\">"); //$NON-NLS-1$
			html.append(this.messages.getMessage(MESSAGES_PREFIX + "DELETE_BUTTON_LABEL")); //$NON-NLS-1$
			html.append("</i></a>"); //$NON-NLS-1$
		}
		return html.toString();
	}


	@Override
	public String exportPublications(Iterable<? extends Publication> publications, ExporterConfigurator configurator) throws Exception {
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
			exportAuthors(html, publication, configurator, true);
			exportDescription(html, publication, configurator);
		} finally {
			java.util.Locale.setDefault(loc);
		}
		html.append("</li>"); //$NON-NLS-1$
	}

	@Override
	public String generateHtmlLinks(Publication publication, ExporterConfigurator configurator) {
		final StringBuilder links = new StringBuilder();
		if (!Strings.isNullOrEmpty(publication.getDOI())) {
			final URL url = this.doiTools.getDOIUrlFromDOINumber(publication.getDOI());
			final StringBuilder b0 = new StringBuilder();
			b0.append(HTML_ATAG_0).append(url.toExternalForm()).append(HTML_ATAG_1).append(publication.getDOI()).append(HTML_ATAG_2);
			links.append(this.messages.getMessage(MESSAGES_PREFIX + "doiLabel", new Object[] {b0.toString()})); //$NON-NLS-1$
		}
		URL url = publication.getDblpURLObject();
		if (url != null) {
			if (links.length() > 0 ) {
				links.append(HTML_NEWLINE);
			}
			final StringBuilder b0 = new StringBuilder();
			b0.append(HTML_ATAG_0).append(url.toExternalForm()).append(HTML_ATAG_1).append(url.toExternalForm()).append(HTML_ATAG_2);
			links.append(this.messages.getMessage(MESSAGES_PREFIX + "dblpLabel", new Object[] {b0.toString()})); //$NON-NLS-1$
		}
		url = publication.getVideoURLObject();
		if (url != null) {
			if (links.length() > 0 ) {
				links.append(HTML_NEWLINE);
			}
			final StringBuilder b0 = new StringBuilder();
			b0.append(HTML_ATAG_0).append(url.toExternalForm()).append(HTML_ATAG_1).append(url.toExternalForm()).append(HTML_ATAG_2);
			links.append(this.messages.getMessage(MESSAGES_PREFIX + "videoLabel", new Object[] {b0.toString()})); //$NON-NLS-1$
		}
		url = publication.getExtraURLObject();
		if (url != null) {
			if (links.length() > 0 ) {
				links.append(HTML_NEWLINE);
			}
			final StringBuilder b0 = new StringBuilder();
			b0.append(HTML_ATAG_0).append(url.toExternalForm()).append(HTML_ATAG_1).append(url.toExternalForm()).append(HTML_ATAG_2);
			links.append(this.messages.getMessage(MESSAGES_PREFIX + "extraUrlLabel", new Object[] {b0.toString()})); //$NON-NLS-1$
		}
		return links.toString();
	}

	@Override
	public String generateHtmlAuthors(Publication publication, ExporterConfigurator configurator) {
		final StringBuilder authors = new StringBuilder();
		exportAuthors(authors, publication, configurator, false, (person, year) -> {
			final URI webpage = person.getWebPageURI();
			final StringBuilder b = new StringBuilder();
			if (webpage == null) {
				b.append(formatAuthorName(person, year.intValue(), configurator));
			} else {
				b.append(HTML_ATAG_0);
				b.append(person.getWebPageURI().toASCIIString());
				b.append(HTML_ATAG_1);
				b.append(formatAuthorName(person, year.intValue(), configurator));
				b.append(HTML_ATAG_2);
			}
			return b.toString();
		});
		return authors.toString();
	}

	@Override
	public String generateHtmlPublicationDetails(Publication publication, ExporterConfigurator configurator) {
		return publication.getWherePublishedShortDescription();
	}

}