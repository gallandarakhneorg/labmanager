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
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.io.html;

import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.type.Book;
import fr.ciadlab.labmanager.entities.publication.type.BookChapter;
import fr.ciadlab.labmanager.entities.publication.type.ConferencePaper;
import fr.ciadlab.labmanager.entities.publication.type.JournalEdition;
import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import fr.ciadlab.labmanager.entities.publication.type.KeyNote;
import fr.ciadlab.labmanager.entities.publication.type.MiscDocument;
import fr.ciadlab.labmanager.entities.publication.type.Patent;
import fr.ciadlab.labmanager.entities.publication.type.Report;
import fr.ciadlab.labmanager.entities.publication.type.Thesis;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.locale.Locale;

/** Utilities for exporting publications to HTML content based on the CIAD standard HTML style.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public abstract class AbstractCiadHtmlExporter extends AbstractHtmlExporter {

	/** Root URL of the CIAD lab website.
	 */
	protected static final String ROOT_URL = "http://www.ciad-lab.fr/"; //$NON-NLS-1$

	@Override
	protected String formatTitle(String title, ExporterConfigurator configurator) {
		final StringBuilder html = new StringBuilder();
		html.append("<i>"); //$NON-NLS-1$
		if (configurator.isColoredTitle()) {
			// CIAD green : #95bc0f
			// CIAD dark green : #4b5e08
			html.append("<font color=\"#4b5e08\">"); //$NON-NLS-1$
		}
		html.append("\""); //$NON-NLS-1$
		html.append(title);
		html.append("\"."); //$NON-NLS-1$
		if (configurator.isColoredTitle()) {
			html.append("</font>"); //$NON-NLS-1$
		}
		html.append("</i> "); //$NON-NLS-1$
		return html.toString();
	}


	@Override
	protected void exportDescription(StringBuilder html, Book publication) {
		if (append(html, ", ", //$NON-NLS-1$
				publication.getEdition(),
				decorateBefore(publication.getVolume(), Locale.getString(AbstractCiadHtmlExporter.class, "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), Locale.getString(AbstractCiadHtmlExporter.class, "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), Locale.getString(AbstractCiadHtmlExporter.class, "PAGE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, decorateBefore(publication.getSeries(), Locale.getString(AbstractCiadHtmlExporter.class, "SERIES_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, decorateBefore(publication.getEditors(), Locale.getString(AbstractCiadHtmlExporter.class, "EDITOR_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, Locale.getString(AbstractCiadHtmlExporter.class, "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, publication.getPublisher())) {
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, publication.getAddress())) {
			html.append(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(StringBuilder html, BookChapter publication) {
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(publication.getBookTitle(), Locale.getString(AbstractCiadHtmlExporter.class, "INBOOK_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getChapterNumber(), Locale.getString(AbstractCiadHtmlExporter.class, "CHAPTER_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				publication.getEdition(),
				decorateBefore(publication.getVolume(), Locale.getString(AbstractCiadHtmlExporter.class, "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), Locale.getString(AbstractCiadHtmlExporter.class, "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), Locale.getString(AbstractCiadHtmlExporter.class, "PAGE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, decorateBefore(publication.getSeries(), Locale.getString(AbstractCiadHtmlExporter.class, "SERIES_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, decorateBefore(publication.getEditors(), Locale.getString(AbstractCiadHtmlExporter.class, "EDITOR_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, Locale.getString(AbstractCiadHtmlExporter.class, "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, publication.getPublisher())) {
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, publication.getAddress())) {
			html.append(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(StringBuilder html, ConferencePaper publication) {
		if (append(html, decorateBefore(publication.getScientificEventName(), Locale.getString(AbstractCiadHtmlExporter.class, "CONFERENCE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(publication.getVolume(), Locale.getString(AbstractCiadHtmlExporter.class, "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), Locale.getString(AbstractCiadHtmlExporter.class, "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), Locale.getString(AbstractCiadHtmlExporter.class, "PAGE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(publication.getEditors(), Locale.getString(AbstractCiadHtmlExporter.class, "EDITOR_PREFIX")), //$NON-NLS-1$
				publication.getOrganization())) {
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, Locale.getString(AbstractCiadHtmlExporter.class, "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, decorateBefore(publication.getSeries(), Locale.getString(AbstractCiadHtmlExporter.class, "SERIES_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, publication.getAddress())) {
			html.append(". "); //$NON-NLS-1$
		}
		if (publication.getCoreRanking() != null && append(html,
				decorateBefore(publication.getCoreRanking().toString(), Locale.getString(AbstractCiadHtmlExporter.class, "CORE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(StringBuilder html, JournalPaper publication) {
		final Journal journal = publication.getJournal();
		if (journal != null && append(html,
				decorateBefore(journal.getJournalName(), Locale.getString(AbstractCiadHtmlExporter.class, "JOURNAL_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(publication.getVolume(), Locale.getString(AbstractCiadHtmlExporter.class, "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), Locale.getString(AbstractCiadHtmlExporter.class, "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), Locale.getString(AbstractCiadHtmlExporter.class, "PAGE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, Locale.getString(AbstractCiadHtmlExporter.class, "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}		
		if (journal != null && append(html, journal.getPublisher())) {
			html.append(". "); //$NON-NLS-1$
		}
		appendRanks(html, publication.getScimagoQIndex(), publication.getWosQIndex(), publication.getImpactFactor());
	}

	@Override
	protected void exportDescription(StringBuilder html, JournalEdition publication) {
		final Journal journal = publication.getJournal();
		if (journal != null && append(html,
				decorateBefore(journal.getJournalName(), Locale.getString(AbstractCiadHtmlExporter.class, "JOURNAL_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(publication.getVolume(), Locale.getString(AbstractCiadHtmlExporter.class, "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), Locale.getString(AbstractCiadHtmlExporter.class, "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), Locale.getString(AbstractCiadHtmlExporter.class, "PAGE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, Locale.getString(AbstractCiadHtmlExporter.class, "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}		
		if (journal != null && append(html, journal.getPublisher())) {
			html.append(". "); //$NON-NLS-1$
		}
		appendRanks(html, publication.getScimagoQIndex(), publication.getWosQIndex(), publication.getImpactFactor());
	}

	@Override
	protected void exportDescription(StringBuilder html, KeyNote publication) {
		if (append(html, decorateBefore(publication.getScientificEventName(), Locale.getString(AbstractCiadHtmlExporter.class, "CONFERENCE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(publication.getEditors(), Locale.getString(AbstractCiadHtmlExporter.class, "EDITOR_PREFIX")), //$NON-NLS-1$
				publication.getOrganization())) {
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, Locale.getString(AbstractCiadHtmlExporter.class, "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, publication.getAddress())) {
			html.append(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(StringBuilder html, Report publication) {
		if (append(html, ", ", //$NON-NLS-1$
				publication.getReportType(),
				decorateBefore(publication.getReportNumber(), Locale.getString(AbstractCiadHtmlExporter.class, "NUMBER_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				publication.getInstitution(),
				publication.getAddress())) {
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, Locale.getString(AbstractCiadHtmlExporter.class, "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(StringBuilder html, Thesis publication) {
		if (append(html, ", ", //$NON-NLS-1$
				Locale.getString(AbstractCiadHtmlExporter.class, publication.getType().name()),
				publication.getInstitution(),
				publication.getAddress())) {
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, Locale.getString(AbstractCiadHtmlExporter.class, "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(StringBuilder html, Patent publication) {
		if (append(html, " ", //$NON-NLS-1$
				Locale.getString(AbstractCiadHtmlExporter.class, publication.getType().name()),
				publication.getPatentNumber())) {
			if (!Strings.isNullOrEmpty(publication.getPatentType())) {
				final String typeStr = Locale.getString(AbstractCiadHtmlExporter.class, "PATENT_TYPE", publication.getPatentType()); //$NON-NLS-1$
				if (!Strings.isNullOrEmpty(typeStr)) {
					html.append(" "); //$NON-NLS-1$
					html.append(typeStr);
				}
			}
			html.append(". "); //$NON-NLS-1$
		}		
		if (append(html, ", ", //$NON-NLS-1$
				publication.getInstitution(),
				publication.getAddress())) {
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, Locale.getString(AbstractCiadHtmlExporter.class, "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(StringBuilder html, MiscDocument publication) {
		if (append(html, " ", //$NON-NLS-1$
				publication.getDocumentType(),
				publication.getDocumentNumber())) {
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				publication.getHowPublished(),
				publication.getOrganization(),
				publication.getAddress())) {
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, Locale.getString(AbstractCiadHtmlExporter.class, "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(AbstractCiadHtmlExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, publication.getPublisher())) {
			html.append(". "); //$NON-NLS-1$
		}
	}

	/** Export in HTML the authors of a single publication.
	 *
	 * @param html the receiver of the HTML.
	 * @param publication the publication, never {@code null}.
	 * @param configurator the configurator for the exporter.
	 */
	protected void exportAuthors(StringBuilder html, Publication publication, ExporterConfigurator configurator) {
		assert configurator != null;
		final int year = publication.getPublicationYear();
		boolean first = true;
		for (final Person person : publication.getAuthors()) {
			if (first) {
				first = false;
			} else {
				html.append(", "); //$NON-NLS-1$
			}
			html.append(formatAuthorName(person, year, configurator));
		}
		html.append(". "); //$NON-NLS-1$
	}

}
