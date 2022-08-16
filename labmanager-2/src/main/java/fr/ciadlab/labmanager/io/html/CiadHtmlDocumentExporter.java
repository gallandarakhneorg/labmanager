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
import fr.ciadlab.labmanager.utils.doi.DoiTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.MessageSourceAccessor;
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
public class CiadHtmlDocumentExporter extends AbstractCiadHtmlExporter implements HtmlDocumentExporter {

	/** Constructor.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param doiTools the tools for managning DOI links.
	 */
	public CiadHtmlDocumentExporter(@Autowired MessageSourceAccessor messages, DoiTools doiTools) {
		super(messages, doiTools);
	}
	
	@Override
	public String exportPublications(Iterable<? extends Publication> publications, ExporterConfigurator configurator) throws Exception {
		assert configurator != null;
		if (publications == null) {
			return null;
		}
		final StringBuilder html = new StringBuilder();
		html.append("<html><body><ul>"); //$NON-NLS-1$
		for (final Publication publication : publications) {
			exportPublication(html, publication, configurator);
		}
		html.append("</ul></html></body>"); //$NON-NLS-1$
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

}
