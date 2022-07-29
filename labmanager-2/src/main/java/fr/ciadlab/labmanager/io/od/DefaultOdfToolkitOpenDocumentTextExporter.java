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

package fr.ciadlab.labmanager.io.od;

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
import org.odftoolkit.odfdom.dom.element.style.StyleTextPropertiesElement;
import org.odftoolkit.odfdom.dom.element.text.TextListElement;
import org.odftoolkit.odfdom.dom.element.text.TextListItemElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.odftoolkit.odfdom.dom.element.text.TextSpanElement;
import org.springframework.context.annotation.Primary;
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

	@Override
	public void exportPublication(TextListElement odt, Publication publication, ExporterConfigurator configurator) {
		assert odt != null;
		assert publication != null;
		final TextListItemElement item = odt.newTextListItemElement();
		final TextPElement odtText = item.newTextPElement();
		final java.util.Locale loc = java.util.Locale.getDefault();
		try {
			java.util.Locale.setDefault(publication.getMajorLanguage().getLocale());
			exportAuthors(odtText, publication, configurator);
			exportDescription(odtText, publication, configurator);
		} finally {
			java.util.Locale.setDefault(loc);
		}
	}

	/** Export in ODT the authors of a single publication.
	 *
	 * @param odtText the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 * @param configurator the configurator for the exporter.
	 */
	@SuppressWarnings("static-method")
	protected void exportAuthors(TextPElement odtText, Publication publication, ExporterConfigurator configurator) {
		assert configurator != null;
		final int year = publication.getPublicationYear();
		boolean first = true;
		for (final Person person : publication.getAuthors()) {
			if (first) {
				first = false;
			} else {
				odtText.newTextNode(", "); //$NON-NLS-1$
			}
			formatAuthorName(odtText, person, year, configurator);
		}
		odtText.newTextNode(". "); //$NON-NLS-1$
	}

	@Override
	protected void formatTitle(TextPElement odtText, String title, ExporterConfigurator configurator) {
		final TextSpanElement odtSpan = newTextItalic(odtText);
		if (configurator.isColoredTitle()) {
			// CIAD green : #95bc0f
			// CIAD dark green : #4b5e08
			odtSpan.setProperty(StyleTextPropertiesElement.Color, "4b5e08"); //$NON-NLS-1$
		}
		odtSpan.newTextNode("\""); //$NON-NLS-1$
		odtSpan.newTextNode(title);
		odtSpan.newTextNode("\"."); //$NON-NLS-1$
		odtText.newTextNode(" "); //$NON-NLS-1$
	}

	@Override
	protected void exportDescription(TextPElement odtText, Book publication) {
		if (append(odtText, ", ", //$NON-NLS-1$
				publication.getEdition(),
				decorateBefore(publication.getVolume(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "PAGE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, decorateBefore(publication.getSeries(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "SERIES_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, decorateBefore(publication.getEditors(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "EDITOR_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
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
				decorateBefore(publication.getVolume(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "PAGE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, decorateBefore(publication.getSeries(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "SERIES_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, decorateBefore(publication.getEditors(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "EDITOR_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
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
		if (append(odtText, decorateBefore(publication.getScientificEventName(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "CONFERENCE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getVolume(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "PAGE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getEditors(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "EDITOR_PREFIX")), //$NON-NLS-1$
				publication.getOrganization())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, decorateBefore(publication.getSeries(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "SERIES_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, publication.getAddress())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (publication.getCoreRanking() != null && append(odtText,
				decorateBefore(publication.getCoreRanking().toString(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "CORE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(TextPElement odtText, JournalPaper publication) {
		final Journal journal = publication.getJournal();
		if (journal != null && append(odtText,
				decorateBefore(journal.getJournalName(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "JOURNAL_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getVolume(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "PAGE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
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
				decorateBefore(journal.getJournalName(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "JOURNAL_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getVolume(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "VOLUME_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getNumber(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "NUMBER_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getPages(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "PAGE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}		
		if (journal != null && append(odtText, journal.getPublisher())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		appendRanks(odtText, publication.getScimagoQIndex(), publication.getWosQIndex(), publication.getImpactFactor());
	}

	@Override
	protected void exportDescription(TextPElement odtText, KeyNote publication) {
		if (append(odtText, decorateBefore(publication.getScientificEventName(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "CONFERENCE_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getEditors(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "EDITOR_PREFIX")), //$NON-NLS-1$
				publication.getOrganization())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
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
				decorateBefore(publication.getReportNumber(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "NUMBER_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				publication.getInstitution(),
				publication.getAddress())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(TextPElement odtText, Thesis publication) {
		if (append(odtText, ", ", //$NON-NLS-1$
				Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, publication.getType().name()),
				publication.getInstitution(),
				publication.getAddress())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (appendDoiLink(odtText, publication.getDOI(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
	}

	@Override
	protected void exportDescription(TextPElement odtText, Patent publication) {
		if (append(odtText, " ", //$NON-NLS-1$
				Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, publication.getType().name()),
				publication.getPatentNumber())) {
			if (!Strings.isNullOrEmpty(publication.getPatentType())) {
				final String typeStr = Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "PATENT_TYPE", publication.getPatentType()); //$NON-NLS-1$
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
		if (appendDoiLink(odtText, publication.getDOI(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
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
		if (appendDoiLink(odtText, publication.getDOI(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "DOI_PREFIX"))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, ", ", //$NON-NLS-1$
				decorateBefore(publication.getISBN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISBN_PREFIX")), //$NON-NLS-1$
				decorateBefore(publication.getISSN(), Locale.getString(DefaultOdfToolkitOpenDocumentTextExporter.class, "ISSN_PREFIX")))) { //$NON-NLS-1$
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
		if (append(odtText, publication.getPublisher())) {
			odtText.newTextNode(". "); //$NON-NLS-1$
		}
	}

}
