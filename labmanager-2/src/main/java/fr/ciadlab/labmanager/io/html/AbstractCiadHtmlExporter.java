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
import org.springframework.context.support.MessageSourceAccessor;

/** Utilities for exporting publications to HTML content based on the CIAD standard HTML style.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public abstract class AbstractCiadHtmlExporter extends AbstractHtmlExporter {

	/** Green color for CIAD lab.
	 */
	public static final String CIAD_GREEN = "95bc0f"; //$NON-NLS-1$

	/** Dark green color for CIAD lab.
	 */
	public static final String CIAD_DARK_GREEN = "4b5e08"; //$NON-NLS-1$

	/** Root URL of the CIAD lab website.
	 */
	protected static final String ROOT_URL = "http://www.ciad-lab.fr/"; //$NON-NLS-1$

	private static final String MESSAGES_PREFIX = "abstractCiadHtmlExporter."; //$NON-NLS-1$
	
	/** Constructor.
	 *
	 * @param messages the accessor to the localized messages.
	 */
	public AbstractCiadHtmlExporter(MessageSourceAccessor messages) {
		super(messages);
	}

	@Override
	protected String formatTitle(String title, ExporterConfigurator configurator) {
		final StringBuilder html = new StringBuilder();
		html.append("<i>"); //$NON-NLS-1$
		if (configurator.isColoredTitle()) {
			html.append("<font color=\"#"); //$NON-NLS-1$
			html.append(CIAD_GREEN);
			html.append("\">"); //$NON-NLS-1$
		}
		html.append(getLeftQuotes());
		html.append(title);
		html.append(getRightQuotes());
		html.append("."); //$NON-NLS-1$
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
				decorateBefore(publication.getVolume(), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, decorateBefore(publication.getSeries(), this.messages.getMessage(MESSAGES_PREFIX + "SERIES_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, decorateBefore(publication.getEditors(), this.messages.getMessage(MESSAGES_PREFIX + "EDITOR_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
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
				decorateBefore(publication.getBookTitle(), this.messages.getMessage(MESSAGES_PREFIX + "INBOOK_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getChapterNumber(), this.messages.getMessage(MESSAGES_PREFIX + "CHAPTER_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				publication.getEdition(),
				decorateBefore(publication.getVolume(), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, decorateBefore(publication.getSeries(), this.messages.getMessage(MESSAGES_PREFIX + "SERIES_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, decorateBefore(publication.getEditors(), this.messages.getMessage(MESSAGES_PREFIX + "EDITOR_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
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
		if (append(html, decorateBefore(publication.getScientificEventName(), this.messages.getMessage(MESSAGES_PREFIX + "CONFERENCE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(publication.getVolume(), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(publication.getEditors(), this.messages.getMessage(MESSAGES_PREFIX + "EDITOR_PREFIX")), //$NON-NLS-1$
				publication.getOrganization())) {
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, decorateBefore(publication.getSeries(), this.messages.getMessage(MESSAGES_PREFIX + "SERIES_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, publication.getAddress())) {
			html.append(". "); //$NON-NLS-1$
		}
		if (publication.getCoreRanking() != null && append(html,
				decorateBefore(publication.getCoreRanking().toString(), this.messages.getMessage(MESSAGES_PREFIX + "CORE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(StringBuilder html, JournalPaper publication) {
		final Journal journal = publication.getJournal();
		if (journal != null && append(html, ", ", //$NON-NLS-1$
				decorateBefore(journal.getJournalName(), this.messages.getMessage(MESSAGES_PREFIX + "JOURNAL_PREFIX")), //$NON-NLS-1$
				publication.getSeries())) {
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(publication.getVolume(), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getJournal().getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getJournal().getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
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
				decorateBefore(journal.getJournalName(), this.messages.getMessage(MESSAGES_PREFIX + "JOURNAL_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(publication.getVolume(), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		final String isbn;
		final String issn;
		if (journal != null) {
			isbn = journal.getISBN();
			issn = journal.getISSN();
		} else {
			isbn = null;
			issn = null;
		}
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(isbn, this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(issn, this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}		
		if (journal != null && append(html, journal.getPublisher())) {
			html.append(". "); //$NON-NLS-1$
		}
		appendRanks(html, publication.getScimagoQIndex(), publication.getWosQIndex(), publication.getImpactFactor());
	}

	@Override
	protected void exportDescription(StringBuilder html, KeyNote publication) {
		if (append(html, decorateBefore(publication.getScientificEventName(), this.messages.getMessage(MESSAGES_PREFIX + "CONFERENCE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(publication.getEditors(), this.messages.getMessage(MESSAGES_PREFIX + "EDITOR_PREFIX")), //$NON-NLS-1$
				publication.getOrganization())) {
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
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
				decorateBefore(publication.getReportNumber(), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				publication.getInstitution(),
				publication.getAddress())) {
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(StringBuilder html, Thesis publication) {
		if (append(html, ", ", //$NON-NLS-1$
				this.messages.getMessage(MESSAGES_PREFIX + publication.getType().name()),
				publication.getInstitution(),
				publication.getAddress())) {
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(StringBuilder html, Patent publication) {
		if (append(html, " ", //$NON-NLS-1$
				this.messages.getMessage(MESSAGES_PREFIX + publication.getType().name()),
				publication.getPatentNumber())) {
			if (!Strings.isNullOrEmpty(publication.getPatentType())) {
				final String typeStr = this.messages.getMessage(MESSAGES_PREFIX + "PATENT_TYPE", //$NON-NLS-1$
						new Object[] {publication.getPatentType()});
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
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
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
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
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
