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

package fr.utbm.ciad.labmanager.utils.io.od;

import fr.utbm.ciad.labmanager.data.journal.Journal;
import fr.utbm.ciad.labmanager.data.publication.type.Book;
import fr.utbm.ciad.labmanager.data.publication.type.BookChapter;
import fr.utbm.ciad.labmanager.data.publication.type.ConferencePaper;
import fr.utbm.ciad.labmanager.data.publication.type.JournalEdition;
import fr.utbm.ciad.labmanager.data.publication.type.JournalPaper;
import fr.utbm.ciad.labmanager.data.publication.type.KeyNote;
import fr.utbm.ciad.labmanager.data.publication.type.MiscDocument;
import fr.utbm.ciad.labmanager.data.publication.type.Patent;
import fr.utbm.ciad.labmanager.data.publication.type.Report;
import fr.utbm.ciad.labmanager.data.publication.type.Thesis;
import fr.utbm.ciad.labmanager.utils.io.ExporterConfigurator;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.odftoolkit.odfdom.dom.element.style.StyleTextPropertiesElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.odftoolkit.odfdom.dom.element.text.TextSpanElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/** Exporter of publications to Open Document Text based on the ODF toolkit.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 * @see "https://odftoolkit.org"
 */
@Component
@Primary
public class DefaultOdfToolkitOpenDocumentTextExporter extends AbstractOdfToolkitOpenDocumentTextPublicationExporter {

	private static final String MESSAGES_PREFIX = "defaultOdfToolkitOpenDocumentTextExporter."; //$NON-NLS-1$

	/** Constructor.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param textHelper the helper for building the text elements.
	 */
	public DefaultOdfToolkitOpenDocumentTextExporter(@Autowired MessageSourceAccessor messages,
			@Autowired OdfTextDocumentHelper textHelper) {
		super(messages, textHelper);
	}
	
	@Override
	protected void formatTitle(TextPElement odtText, String title, ExporterConfigurator configurator) {
		final TextSpanElement odtSpan = this.textHelper.newTextItalic(odtText);
		if (configurator.isColoredTitle()) {
			odtSpan.setProperty(StyleTextPropertiesElement.Color, OpenDocumentConstants.CIAD_GREEN.toString());
		}
		odtSpan.newTextNode(getLeftQuotes());
		odtSpan.newTextNode(title);
		odtSpan.newTextNode(getRightQuotes());
		odtSpan.newTextNode("."); //$NON-NLS-1$
		odtText.newTextNode(" "); //$NON-NLS-1$
	}

	@Override
	protected void exportDescription(TextPElement odtText, Book publication) {
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				publication.getEdition(),
				this.textHelper.decorateBefore(publication.getVolume(), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getNumber(), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getPages(), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, this.textHelper.decorateBefore(publication.getSeries(), this.messages.getMessage(MESSAGES_PREFIX + "SERIES_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, this.textHelper.decorateBefore(publication.getEditors(), this.messages.getMessage(MESSAGES_PREFIX + "EDITOR_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, publication.getPublisher())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, publication.getAddress())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(TextPElement odtText, BookChapter publication) {
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				publication.getEdition(),
				this.textHelper.decorateBefore(publication.getVolume(), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getNumber(), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getPages(), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, this.textHelper.decorateBefore(publication.getSeries(), this.messages.getMessage(MESSAGES_PREFIX + "SERIES_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, this.textHelper.decorateBefore(publication.getEditors(), this.messages.getMessage(MESSAGES_PREFIX + "EDITOR_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, publication.getPublisher())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, publication.getAddress())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(TextPElement odtText, ConferencePaper publication) {
		if (this.textHelper.append(odtText, this.textHelper.decorateBefore(publication.getPublicationTarget(), this.messages.getMessage(MESSAGES_PREFIX + "CONFERENCE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getVolume(), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getNumber(), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getPages(), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getEditors(), this.messages.getMessage(MESSAGES_PREFIX + "EDITOR_PREFIX")), //$NON-NLS-1$
				publication.getOrganization())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (publication.getConference() != null) {
			if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
					this.textHelper.decorateBefore(publication.getConference().getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
					this.textHelper.decorateBefore(publication.getConference().getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")), //$NON-NLS-1$
					publication.getConference().getPublisher())) {
				odtText.newTextNode(". "); //$NON-NLS-1$
			}
		}
		if (this.textHelper.append(odtText, this.textHelper.decorateBefore(publication.getSeries(), this.messages.getMessage(MESSAGES_PREFIX + "SERIES_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, publication.getAddress())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		appendRanks(odtText, publication.getCoreRanking());
	}

	@Override
	protected void exportDescription(TextPElement odtText, JournalPaper publication) {
		final Journal journal = publication.getJournal();
		if (journal != null && this.textHelper.append(odtText, ",", //$NON-NLS-1$
				this.textHelper.decorateBefore(journal.getJournalName(), this.messages.getMessage(MESSAGES_PREFIX + "JOURNAL_PREFIX")), //$NON-NLS-1$
				publication.getSeries())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getVolume(), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getNumber(), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getPages(), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getJournal().getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getJournal().getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}		
		if (journal != null && this.textHelper.append(odtText, journal.getPublisher())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		appendRanks(odtText,
				publication.getScimagoQIndex(),
				publication.getWosQIndex(),
				publication.getImpactFactor());
	}

	@Override
	protected void exportDescription(TextPElement odtText, JournalEdition publication) {
		final Journal journal = publication.getJournal();
		if (journal != null && this.textHelper.append(odtText,
				this.textHelper.decorateBefore(journal.getJournalName(), this.messages.getMessage(MESSAGES_PREFIX + "JOURNAL_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getVolume(), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getNumber(), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getPages(), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		final String isbn;
		final String issn;
		if (journal != null) {
			isbn = journal.getISBN();
			issn = journal.getISSN();
		} else {
			isbn = null;
			issn = null;
		}
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				this.textHelper.decorateBefore(isbn, this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(issn, this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}		
		if (journal != null && this.textHelper.append(odtText, journal.getPublisher())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		appendRanks(odtText, publication.getScimagoQIndex(), publication.getWosQIndex(), publication.getImpactFactor());
	}

	@Override
	protected void exportDescription(TextPElement odtText, KeyNote publication) {
		if (this.textHelper.append(odtText, this.textHelper.decorateBefore(publication.getPublicationTarget(), this.messages.getMessage(MESSAGES_PREFIX + "CONFERENCE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getEditors(), this.messages.getMessage(MESSAGES_PREFIX + "EDITOR_PREFIX")), //$NON-NLS-1$
				publication.getOrganization())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getConference().getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getConference().getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, publication.getAddress())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(TextPElement odtText, Report publication) {
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				publication.getReportType(),
				this.textHelper.decorateBefore(publication.getReportNumber(), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				publication.getInstitution(),
				publication.getAddress())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(TextPElement odtText, Thesis publication) {
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				this.messages.getMessage(MESSAGES_PREFIX + publication.getType().name()),
				publication.getInstitution(),
				publication.getAddress())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(TextPElement odtText, Patent publication) {
		if (this.textHelper.append(odtText, " ", //$NON-NLS-1$
				this.messages.getMessage(MESSAGES_PREFIX + publication.getType().name()),
				publication.getPatentNumber())) {
			if (!Strings.isNullOrEmpty(publication.getPatentType())) {
				final String typeStr = this.messages.getMessage(MESSAGES_PREFIX + "PATENT_TYPE", publication.getPatentType()); //$NON-NLS-1$
				if (!Strings.isNullOrEmpty(typeStr)) {
					odtText.newTextNode(" "); //$NON-NLS-1$
					odtText.newTextNode(typeStr);
				}
			}
			odtText.newTextNode(". "); //$NON-NLS-1$
		}		
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				publication.getInstitution(),
				publication.getAddress())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}

	}

	@Override
	protected void exportDescription(TextPElement odtText, MiscDocument publication) {
		if (this.textHelper.append(odtText, " ", //$NON-NLS-1$
				publication.getDocumentType(),
				publication.getDocumentNumber())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				publication.getHowPublished(),
				publication.getOrganization(),
				publication.getAddress())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, ", ", //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (this.textHelper.append(odtText, publication.getPublisher())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
	}

}