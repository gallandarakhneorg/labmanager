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

package fr.utbm.ciad.labmanager.utils.io.html;

import java.io.File;
import java.util.Locale;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.utils.doi.DoiTools;
import fr.utbm.ciad.labmanager.utils.io.ExporterConfigurator;
import fr.utbm.ciad.labmanager.utils.io.filemanager.FileManager;
import fr.utbm.ciad.labmanager.utils.io.hal.HalTools;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;

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
	 * @param halTools the tools for manipulating HAL identifiers.
	 */
	public CiadHtmlPageExporter(@Autowired Constants constants, @Autowired MessageSourceAccessor messages, @Autowired DoiTools doiTools,
			@Autowired HalTools halTools) {
		super(constants, messages, doiTools, halTools);
	}

	private void buildPdfDownloadLink(StringBuilder html, String path, String label) {
		final var jpeg = FileSystem.replaceExtension(new File(path), FileManager.JPEG_FILE_EXTENSION).toString();
		html.append("<a class=\"btn btn-xs btn-success\" href=\"/"); //$NON-NLS-1$
		html.append(this.constants.getServerName());
		html.append("/"); //$NON-NLS-1$
		html.append(path);
		html.append("\"><img src=\"/"); //$NON-NLS-1$
		html.append(this.constants.getServerName());
		html.append("/"); //$NON-NLS-1$
		html.append(jpeg);
		html.append("\" class=\"publicationDetailsDownloadAttachment\" alt=\"?\"/><br/>"); //$NON-NLS-1$
		html.append(label);
		html.append("</a>"); //$NON-NLS-1$
	}

	@Override
	public String getButtonToDownloadPublicationPDF(String pdfUrl) {
		final var html = new StringBuilder();
		if (!Strings.isNullOrEmpty(pdfUrl)) {
			buildPdfDownloadLink(html, pdfUrl, "PDF"); //$NON-NLS-1$
		}
		return html.toString();
	}

	@Override
	public String getButtonToDownloadPublicationAwardCertificate(String awardUrl) {
		final var html = new StringBuilder();
		if (!Strings.isNullOrEmpty(awardUrl)) {
			buildPdfDownloadLink(html, awardUrl, "Award"); //$NON-NLS-1$
		}
		return html.toString();
	}

	private void exportPublicationButton(StringBuilder html, String buttonClass, String buttonIconClass,
			String buttonIconPath, String tooltip, String endpoint, long id, String label, ExporterConfigurator configurator) {
		var builder = new DefaultUriBuilderFactory().builder();
		builder = builder.path("/" + this.constants.getServerName() + "/" + endpoint); //$NON-NLS-1$ //$NON-NLS-2$
		builder = configurator.applyQueryParams(builder);
		builder = builder.queryParam("id", Long.valueOf(id)); //$NON-NLS-1$
		builder = builder.queryParam("inAttachment", Boolean.TRUE); //$NON-NLS-1$
		html.append("<a class=\"btn btn-xs btn-success "); //$NON-NLS-1$
		html.append(buttonClass);
		html.append("\" href=\"").append(builder.build().toString()); //$NON-NLS-1$
		html.append("\" title=\""); //$NON-NLS-1$
		html.append(tooltip);
		html.append("\">"); //$NON-NLS-1$
		if (!Strings.isNullOrEmpty(buttonIconPath)) {
			builder = new DefaultUriBuilderFactory().builder();
			builder = builder.path("/" + this.constants.getServerName() + "/" + buttonIconPath); //$NON-NLS-1$ //$NON-NLS-2$
			html.append("<img src=\""); //$NON-NLS-1$
			html.append(builder.build().toString());
			html.append("\" class=\"exportbuttonimage\"/>"); //$NON-NLS-1$
		} else if (!Strings.isNullOrEmpty(buttonIconClass)) {
			html.append("<i class=\"fa-solid "); //$NON-NLS-1$
			html.append(buttonIconClass);
			html.append("\">"); //$NON-NLS-1$
			html.append(label);
		}
		html.append("</i></a>"); //$NON-NLS-1$
	}

	@Override
	public String getButtonToExportPublicationToBibTeX(long publicationId, ExporterConfigurator configurator) {
		final var html = new StringBuilder();
		if (publicationId != 0) {
			exportPublicationButton(html, "btBibtex", "fa-file-lines", Constants.EXPORT_BIBTEX_WHITE_ICON, //$NON-NLS-1$ //$NON-NLS-2$
					getMessageSourceAccessor().getMessage("ciadHtmlPageExporter.exportToBibTeX", configurator.getLocale()),  //$NON-NLS-1$
					Constants.EXPORT_BIBTEX_ENDPOINT,
					publicationId, "BibTeX", configurator); //$NON-NLS-1$
		}
		return html.toString();
	}

	@Override
	public String getButtonToExportPublicationToRIS(long publicationId, ExporterConfigurator configurator) {
		final var html = new StringBuilder();
		if (publicationId != 0) {
			exportPublicationButton(html, "btRis", "fa-file-lines", Constants.EXPORT_RIS_WHITE_ICON, //$NON-NLS-1$ //$NON-NLS-2$
					getMessageSourceAccessor().getMessage("ciadHtmlPageExporter.exportToRIS", configurator.getLocale()),  //$NON-NLS-1$
					Constants.EXPORT_RIS_ENDPOINT,
					publicationId, "RIS", configurator); //$NON-NLS-1$
		}
		return html.toString();
	}

	@Override
	public String getButtonToExportPublicationToHtml(long publicationId, ExporterConfigurator configurator) {
		final var html = new StringBuilder();
		if (publicationId != 0) {
			exportPublicationButton(html, "btHtml", "fa-file-code", Constants.EXPORT_HTML_WHITE_ICON, //$NON-NLS-1$ //$NON-NLS-2$
					getMessageSourceAccessor().getMessage("ciadHtmlPageExporter.exportToHtml", configurator.getLocale()),  //$NON-NLS-1$
					Constants.EXPORT_HTML_ENDPOINT,
					publicationId, "HTML", configurator); //$NON-NLS-1$
		}
		return html.toString();
	}

	@Override
	public String getButtonToExportPublicationToOpenDocument(long publicationId, ExporterConfigurator configurator) {
		final var html = new StringBuilder();
		if (publicationId != 0) {
			exportPublicationButton(html, "btWord", "fa-file-word", Constants.EXPORT_ODT_WHITE_ICON, //$NON-NLS-1$ //$NON-NLS-2$
					getMessageSourceAccessor().getMessage("ciadHtmlPageExporter.exportToOdt", configurator.getLocale()),  //$NON-NLS-1$
					Constants.EXPORT_ODT_ENDPOINT,
					publicationId, "ODT", configurator); //$NON-NLS-1$
		}
		return html.toString();
	}

	@Override
	public String getButtonToEditPublication(long publicationId, Locale locale) {
		final var html = new StringBuilder();
		if (publicationId != 0) {
			html.append("<a class=\"btn btn-xs btn-success\" href=\"/"); //$NON-NLS-1$
			html.append(this.constants.getServerName()).append("/"); //$NON-NLS-1$
			html.append("addPublication?publicationId="); //$NON-NLS-1$
			html.append(publicationId);
			html.append("\" <i class=\"fa fa-edit\">"); //$NON-NLS-1$
			html.append(getMessageSourceAccessor().getMessage(MESSAGES_PREFIX + "EDIT_BUTTON_LABEL", locale)); //$NON-NLS-1$
			html.append("</i></a>"); //$NON-NLS-1$
		}
		return html.toString();
	}

	@Override
	public String getButtonToDeletePublication(long publicationId, Locale locale) {
		final var html = new StringBuilder();
		if (publicationId != 0) {
			html.append("<a class=\"btn btn-xs btn-danger\" href=\"/"); //$NON-NLS-1$
			html.append(this.constants.getServerName()).append("/deletePublication?publicationId="); //$NON-NLS-1$
			html.append(publicationId);
			html.append("\" <i class=\"fa fa-delete\">"); //$NON-NLS-1$
			html.append(getMessageSourceAccessor().getMessage(MESSAGES_PREFIX + "DELETE_BUTTON_LABEL", locale)); //$NON-NLS-1$
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
		final var html = new StringBuilder();
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
		final var loc = java.util.Locale.getDefault();
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
		final var links = new StringBuilder();
		if (!Strings.isNullOrEmpty(publication.getDOI())) {
			final var url = this.doiTools.getDOIUrlFromDOINumber(publication.getDOI());
			final var b0 = new StringBuilder();
			b0.append(HTML_ATAG_0).append(url.toExternalForm()).append(HTML_ATAG_1).append(publication.getDOI()).append(HTML_ATAG_2);
			links.append(getMessageSourceAccessor().getMessage(MESSAGES_PREFIX + "doiLabel", new Object[] {b0.toString()}, configurator.getLocale())); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(publication.getHalId())) {
			final var url = this.halTools.getHALUrlFromHALNumber(publication.getHalId());
			final var b0 = new StringBuilder();
			b0.append(HTML_ATAG_0).append(url.toExternalForm()).append(HTML_ATAG_1).append(publication.getHalId()).append(HTML_ATAG_2);
			links.append(getMessageSourceAccessor().getMessage(MESSAGES_PREFIX + "halidLabel", new Object[] {b0.toString()}, configurator.getLocale())); //$NON-NLS-1$
		}
		var url = publication.getDblpURLObject();
		if (url != null) {
			if (links.length() > 0 ) {
				links.append(HTML_NEWLINE);
			}
			final var b0 = new StringBuilder();
			b0.append(HTML_ATAG_0).append(url.toExternalForm()).append(HTML_ATAG_1).append(url.toExternalForm()).append(HTML_ATAG_2);
			links.append(getMessageSourceAccessor().getMessage(MESSAGES_PREFIX + "dblpLabel", new Object[] {b0.toString()}, configurator.getLocale())); //$NON-NLS-1$
		}
		url = publication.getVideoURLObject();
		if (url != null) {
			if (links.length() > 0 ) {
				links.append(HTML_NEWLINE);
			}
			final var b0 = new StringBuilder();
			b0.append(HTML_ATAG_0).append(url.toExternalForm()).append(HTML_ATAG_1).append(url.toExternalForm()).append(HTML_ATAG_2);
			links.append(getMessageSourceAccessor().getMessage(MESSAGES_PREFIX + "videoLabel", new Object[] {b0.toString()}, configurator.getLocale())); //$NON-NLS-1$
		}
		url = publication.getExtraURLObject();
		if (url != null) {
			if (links.length() > 0 ) {
				links.append(HTML_NEWLINE);
			}
			final var b0 = new StringBuilder();
			b0.append(HTML_ATAG_0).append(url.toExternalForm()).append(HTML_ATAG_1).append(url.toExternalForm()).append(HTML_ATAG_2);
			links.append(getMessageSourceAccessor().getMessage(MESSAGES_PREFIX + "extraUrlLabel", new Object[] {b0.toString()}, configurator.getLocale())); //$NON-NLS-1$
		}
		return links.toString();
	}

	@Override
	public String generateHtmlAuthors(Publication publication, ExporterConfigurator configurator) {
		final var authors = new StringBuilder();
		exportAuthors(authors, publication, configurator, false, (person, year) -> {
			final var webpage = person.getWebPageURI();
			final var b = new StringBuilder();
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
