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

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.conference.Conference;
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
import fr.ciadlab.labmanager.io.hal.HalTools;
import fr.ciadlab.labmanager.utils.doi.DoiTools;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
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

	private static final String MESSAGES_PREFIX = "abstractCiadHtmlExporter."; //$NON-NLS-1$

	/** Constructor.
	 *
	 * @param constants the accessor to the application constants.
	 * @param messages the accessor to the localized messages.
	 * @param doiTools the tools for managing DOI links.
	 * @param halTools the tools for managing HAL identifiers.
	 */
	public AbstractCiadHtmlExporter(Constants constants, MessageSourceAccessor messages, DoiTools doiTools, HalTools halTools) {
		super(constants, messages, doiTools, halTools);
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
		html.append(toHtml(title));
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
				toHtml(publication.getEdition()),
				decorateBefore(toHtml(publication.getVolume()), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getNumber()), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getPages()), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, decorateBefore(toHtml(publication.getSeries()), this.messages.getMessage(MESSAGES_PREFIX + "SERIES_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, decorateBefore(toHtml(publication.getEditors()), this.messages.getMessage(MESSAGES_PREFIX + "EDITOR_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getISBN()), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getISSN()), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, toHtml(publication.getPublisher()))) {
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, toHtml(publication.getAddress()))) {
			html.append(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(StringBuilder html, BookChapter publication) {
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(toHtml(publication.getBookTitle()), this.messages.getMessage(MESSAGES_PREFIX + "INBOOK_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getChapterNumber()), this.messages.getMessage(MESSAGES_PREFIX + "CHAPTER_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				publication.getEdition(),
				decorateBefore(toHtml(publication.getVolume()), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getNumber()), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getPages()), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, decorateBefore(toHtml(publication.getSeries()), this.messages.getMessage(MESSAGES_PREFIX + "SERIES_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, decorateBefore(toHtml(publication.getEditors()), this.messages.getMessage(MESSAGES_PREFIX + "EDITOR_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getISBN()), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getISSN()), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, toHtml(publication.getPublisher()))) {
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, toHtml(publication.getAddress()))) {
			html.append(". "); //$NON-NLS-1$
		}
	}

	private static String getISBN(Conference conference) {
		return conference == null ? null : conference.getISBN();
	}

	private static String getISSN(Conference conference) {
		return conference == null ? null : conference.getISSN();
	}

	@Override
	protected void exportDescription(StringBuilder html, ConferencePaper publication) {
		if (append(html, decorateBefore(toHtml(publication.getPublicationTarget()), this.messages.getMessage(MESSAGES_PREFIX + "CONFERENCE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(toHtml(publication.getVolume()), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getNumber()), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getPages()), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(toHtml(publication.getEditors()), this.messages.getMessage(MESSAGES_PREFIX + "EDITOR_PREFIX")), //$NON-NLS-1$
						toHtml(publication.getOrganization()))) {
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(getISBN(publication.getConference())), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(getISSN(publication.getConference())), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, decorateBefore(toHtml(publication.getSeries()), this.messages.getMessage(MESSAGES_PREFIX + "SERIES_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, publication.getAddress())) {
			html.append(". "); //$NON-NLS-1$
		}
		appendRanks(html, publication.getCoreRanking());
	}

	@Override
	protected void exportDescription(StringBuilder html, JournalPaper publication) {
		final Journal journal = publication.getJournal();
		if (journal != null && append(html, ", ", //$NON-NLS-1$
				decorateBefore(toHtml(journal.getJournalName()), this.messages.getMessage(MESSAGES_PREFIX + "JOURNAL_PREFIX")), //$NON-NLS-1$
						toHtml(publication.getSeries()))) {
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(toHtml(publication.getVolume()), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getNumber()), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getPages()), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(getISBN(publication.getJournal())), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(getISSN(publication.getJournal())), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}		
		if (journal != null && append(html, toHtml(journal.getPublisher()))) {
			html.append(". "); //$NON-NLS-1$
		}
		appendRanks(html, publication.getScimagoQIndex(), publication.getWosQIndex(), publication.getImpactFactor());
	}

	private static String getISBN(Journal journal) {
		return journal == null ? null : journal.getISBN();
	}

	private static String getISSN(Journal journal) {
		return journal == null ? null : journal.getISSN();
	}

	@Override
	protected void exportDescription(StringBuilder html, JournalEdition publication) {
		final Journal journal = publication.getJournal();
		if (journal != null && append(html,
				decorateBefore(toHtml(journal.getJournalName()), this.messages.getMessage(MESSAGES_PREFIX + "JOURNAL_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(toHtml(publication.getVolume()), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getNumber()), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getPages()), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		final String isbn = getISBN(journal);
		final String issn = getISSN(journal);
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(isbn, this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(issn, this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}		
		if (journal != null && append(html, toHtml(journal.getPublisher()))) {
			html.append(". "); //$NON-NLS-1$
		}
		appendRanks(html, publication.getScimagoQIndex(), publication.getWosQIndex(), publication.getImpactFactor());
	}

	@Override
	protected void exportDescription(StringBuilder html, KeyNote publication) {
		if (append(html, decorateBefore(toHtml(publication.getPublicationTarget()), this.messages.getMessage(MESSAGES_PREFIX + "CONFERENCE_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(toHtml(publication.getEditors()), this.messages.getMessage(MESSAGES_PREFIX + "EDITOR_PREFIX")), //$NON-NLS-1$
				toHtml(publication.getOrganization()))) {
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(getISBN(publication.getConference())), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(getISSN(publication.getConference())), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, toHtml(publication.getAddress()))) {
			html.append(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(StringBuilder html, Report publication) {
		if (append(html, ", ", //$NON-NLS-1$
				toHtml(publication.getReportType()),
				decorateBefore(toHtml(publication.getReportNumber()), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				toHtml(publication.getInstitution()),
				toHtml(publication.getAddress()))) {
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getISBN()), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getISSN()), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(StringBuilder html, Thesis publication) {
		if (append(html, ", ", //$NON-NLS-1$
				this.messages.getMessage(MESSAGES_PREFIX + publication.getType().name()),
				toHtml(publication.getInstitution()),
				toHtml(publication.getAddress()))) {
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getISBN()), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getISSN()), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(StringBuilder html, Patent publication) {
		if (append(html, " ", //$NON-NLS-1$
				this.messages.getMessage(MESSAGES_PREFIX + publication.getType().name()),
				toHtml(publication.getPatentNumber()))) {
			if (!Strings.isNullOrEmpty(publication.getPatentType())) {
				final String typeStr = this.messages.getMessage(MESSAGES_PREFIX + "PATENT_TYPE", //$NON-NLS-1$
						new Object[] {toHtml(publication.getPatentType())});
				if (!Strings.isNullOrEmpty(typeStr)) {
					html.append(" "); //$NON-NLS-1$
					html.append(typeStr);
				}
			}
			html.append(". "); //$NON-NLS-1$
		}		
		if (append(html, ", ", //$NON-NLS-1$
				toHtml(publication.getInstitution()),
				toHtml(publication.getAddress()))) {
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getISBN()), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getISSN()), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(StringBuilder html, MiscDocument publication) {
		if (append(html, " ", //$NON-NLS-1$
				toHtml(publication.getDocumentType()),
				toHtml(publication.getDocumentNumber()))) {
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, ", ", //$NON-NLS-1$
				toHtml(publication.getHowPublished()),
				toHtml(publication.getOrganization()),
				toHtml(publication.getAddress()))) {
			html.append(". "); //$NON-NLS-1$
		}
		final String doiLink = buildDoiLink(publication.getDOI());
		if (append(html, ", ", //$NON-NLS-1$
				decorateBefore(doiLink, this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getISBN()), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(toHtml(publication.getISSN()), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			html.append(". "); //$NON-NLS-1$
		}
		if (append(html, toHtml(publication.getPublisher()))) {
			html.append(". "); //$NON-NLS-1$
		}
	}

	/** Export in HTML the authors of a single publication.
	 *
	 * @param html the receiver of the HTML.
	 * @param publication the publication, never {@code null}.
	 * @param configurator the configurator for the exporter.
	 * @param appendEndPoint indicates if a point must be added at the end.
	 */
	protected final void exportAuthors(StringBuilder html, Publication publication, ExporterConfigurator configurator, boolean appendEndPoint) {
		exportAuthors(html, publication, configurator, appendEndPoint, null);
	}

	/** Export in HTML the authors of a single publication.
	 *
	 * @param html the receiver of the HTML.
	 * @param publication the publication, never {@code null}.
	 * @param configurator the configurator for the exporter.
	 * @param appendEndPoint indicates if a point must be added at the end.
	 * @param formatter a lambda expression that permits to format the names. It takes the name as argument and replies
	 *     the formatted name.
	 */
	protected void exportAuthors(StringBuilder html, Publication publication, ExporterConfigurator configurator, boolean appendEndPoint,
			Function2<Person, Integer, String> formatter) {
		assert configurator != null;
		final int year = publication.getPublicationYear();
		final Integer oyear = Integer.valueOf(year);
		boolean first = true;
		for (final Person person : publication.getAuthors()) {
			if (first) {
				first = false;
			} else {
				html.append(", "); //$NON-NLS-1$
			}
			final String name0;
			if (formatter != null) {
				name0 = formatter.apply(person, oyear);
			} else {
				name0 = formatAuthorName(person, year, configurator);
			}
			html.append(name0);
		}
		if (appendEndPoint) {
			html.append(". "); //$NON-NLS-1$
		}
	}

}
