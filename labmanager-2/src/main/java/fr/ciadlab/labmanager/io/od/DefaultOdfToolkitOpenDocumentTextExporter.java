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

package fr.ciadlab.labmanager.io.od;

import fr.ciadlab.labmanager.entities.journal.Journal;
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
public class DefaultOdfToolkitOpenDocumentTextExporter extends AbstractOdfToolkitOpenDocumentTextExporter {

	private static final String MESSAGES_PREFIX = "defaultOdfToolkitOpenDocumentTextExporter."; //$NON-NLS-1$

	/** Constructor.
	 *
	 * @param messages the accessor to the localized messages.
	 */
	public DefaultOdfToolkitOpenDocumentTextExporter(@Autowired MessageSourceAccessor messages) {
		super(messages);
	}
	
	@Override
	protected void formatTitle(TextPElement odtText, String title, ExporterConfigurator configurator) {
		final TextSpanElement odtSpan = newTextItalic(odtText);
		if (configurator.isColoredTitle()) {
			odtSpan.setProperty(StyleTextPropertiesElement.Color, CIAD_GREEN.toString());
		}
		odtSpan.newTextNode(getLeftQuotes());
		odtSpan.newTextNode(title);
		odtSpan.newTextNode(getRightQuotes());
		odtSpan.newTextNode("."); //$NON-NLS-1$
		odtText.newTextNode(" "); //$NON-NLS-1$
	}

	@Override
	protected void exportDescription(TextPElement odtText, Book publication) {
		if (append(odtText, ", ", //$NON-NLS-1$
				publication.getEdition(),
				decorateBefore(publication.getVolume(), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, decorateBefore(publication.getSeries(), this.messages.getMessage(MESSAGES_PREFIX + "SERIES_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, decorateBefore(publication.getEditors(), this.messages.getMessage(MESSAGES_PREFIX + "EDITOR_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, publication.getPublisher())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, publication.getAddress())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(TextPElement odtText, BookChapter publication) {
		if (append(odtText, ", ", //$NON-NLS-1$
				publication.getEdition(),
				decorateBefore(publication.getVolume(), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, decorateBefore(publication.getSeries(), this.messages.getMessage(MESSAGES_PREFIX + "SERIES_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, decorateBefore(publication.getEditors(), this.messages.getMessage(MESSAGES_PREFIX + "EDITOR_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, publication.getPublisher())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, publication.getAddress())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(TextPElement odtText, ConferencePaper publication) {
		if (append(odtText, decorateBefore(publication.getScientificEventName(), this.messages.getMessage(MESSAGES_PREFIX + "CONFERENCE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getVolume(), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getEditors(), this.messages.getMessage(MESSAGES_PREFIX + "EDITOR_PREFIX")), //$NON-NLS-1$
				publication.getOrganization())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, decorateBefore(publication.getSeries(), this.messages.getMessage(MESSAGES_PREFIX + "SERIES_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, publication.getAddress())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (publication.getCoreRanking() != null && append(odtText,
				decorateBefore(publication.getCoreRanking().toString(), this.messages.getMessage(MESSAGES_PREFIX + "CORE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(TextPElement odtText, JournalPaper publication) {
		final Journal journal = publication.getJournal();
		if (journal != null && append(odtText, ",", //$NON-NLS-1$
				decorateBefore(journal.getJournalName(), this.messages.getMessage(MESSAGES_PREFIX + "JOURNAL_PREFIX")), //$NON-NLS-1$
				publication.getSeries())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getVolume(), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getJournal().getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getJournal().getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}		
		if (journal != null && append(odtText, journal.getPublisher())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		appendRanks(odtText, publication.getScimagoQIndex(), publication.getWosQIndex(), publication.getImpactFactor());
	}

	@Override
	protected void exportDescription(TextPElement odtText, JournalEdition publication) {
		final Journal journal = publication.getJournal();
		if (journal != null && append(odtText,
				decorateBefore(journal.getJournalName(), this.messages.getMessage(MESSAGES_PREFIX + "JOURNAL_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getVolume(), this.messages.getMessage(MESSAGES_PREFIX + "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), this.messages.getMessage(MESSAGES_PREFIX + "PAGE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}		
		if (journal != null && append(odtText, journal.getPublisher())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		appendRanks(odtText, publication.getScimagoQIndex(), publication.getWosQIndex(), publication.getImpactFactor());
	}

	@Override
	protected void exportDescription(TextPElement odtText, KeyNote publication) {
		if (append(odtText, decorateBefore(publication.getScientificEventName(), this.messages.getMessage(MESSAGES_PREFIX + "CONFERENCE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getEditors(), this.messages.getMessage(MESSAGES_PREFIX + "EDITOR_PREFIX")), //$NON-NLS-1$
				publication.getOrganization())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, publication.getAddress())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(TextPElement odtText, Report publication) {
		if (append(odtText, ", ", //$NON-NLS-1$
				publication.getReportType(),
				decorateBefore(publication.getReportNumber(), this.messages.getMessage(MESSAGES_PREFIX + "NUMBER_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				publication.getInstitution(),
				publication.getAddress())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(TextPElement odtText, Thesis publication) {
		if (append(odtText, ", ", //$NON-NLS-1$
				this.messages.getMessage(MESSAGES_PREFIX + publication.getType().name()),
				publication.getInstitution(),
				publication.getAddress())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(TextPElement odtText, Patent publication) {
		if (append(odtText, " ", //$NON-NLS-1$
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
		if (append(odtText, ", ", //$NON-NLS-1$
				publication.getInstitution(),
				publication.getAddress())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}

	}

	@Override
	protected void exportDescription(TextPElement odtText, MiscDocument publication) {
		if (append(odtText, " ", //$NON-NLS-1$
				publication.getDocumentType(),
				publication.getDocumentNumber())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				publication.getHowPublished(),
				publication.getOrganization(),
				publication.getAddress())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), this.messages.getMessage(MESSAGES_PREFIX + "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getISBN(), this.messages.getMessage(MESSAGES_PREFIX + "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), this.messages.getMessage(MESSAGES_PREFIX + "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, publication.getPublisher())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
	}

}
