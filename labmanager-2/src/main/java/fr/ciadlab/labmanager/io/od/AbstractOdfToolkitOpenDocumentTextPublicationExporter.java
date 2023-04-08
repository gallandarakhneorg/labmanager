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

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;

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
import fr.ciadlab.labmanager.io.AbstractPublicationExporter;
import fr.ciadlab.labmanager.io.ExportedAuthorStatus;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.element.text.TextAElement;
import org.odftoolkit.odfdom.dom.element.text.TextHElement;
import org.odftoolkit.odfdom.dom.element.text.TextListElement;
import org.odftoolkit.odfdom.dom.element.text.TextListItemElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.odftoolkit.odfdom.dom.element.text.TextSpanElement;
import org.springframework.context.support.MessageSourceAccessor;

/** Exporter of publications to Open Document Text based on the ODF toolkit.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 * @see "https://odftoolkit.org"
 */
public abstract class AbstractOdfToolkitOpenDocumentTextPublicationExporter extends AbstractPublicationExporter implements OpenDocumentTextPublicationExporter {

	private static final String MESSAGES_PREFIX = "abstractOdfToolkitOpenDocumentTextExporter."; //$NON-NLS-1$

	/** Accessor to the localized messages.
	 */
	protected final MessageSourceAccessor messages;

	/** Text helper for building text elements in ODT format.
	 */
	protected final OdfTextDocumentHelper textHelper;

	/** Constructor.
	 *
	 * @param messages the accessor to the localized message.
	 * @param textHelper the helper for building the text elements.
	 */
	public AbstractOdfToolkitOpenDocumentTextPublicationExporter(MessageSourceAccessor messages, OdfTextDocumentHelper textHelper) {
		this.messages = messages;
		this.textHelper = textHelper;
	}

	/** Replies the string representation of left quotes.
	 *
	 * @return the left quotes.
	 */
	public String getLeftQuotes() {
		return this.messages.getMessage(MESSAGES_PREFIX + "LEFT_QUOTES"); //$NON-NLS-1$
	}

	/** Replies the string representation of right quotes.
	 *
	 * @return the left quotes.
	 */
	public String getRightQuotes() {
		return this.messages.getMessage(MESSAGES_PREFIX + "RIGHT_QUOTES"); //$NON-NLS-1$
	}

	@SuppressWarnings("resource")
	@Override
	public byte[] exportPublications(Iterable<? extends Publication> publications, ExporterConfigurator configurator) throws Exception {
		if (publications == null) {
			return null;
		}
		final OdfTextDocument odt = OdfTextDocument.newTextDocument();
		exportPublicationsWithGroupingCriteria(publications, configurator,
				it -> {
					try {
						final TextHElement element = odt.getContentRoot().newTextHElement(1);
						element.setTextContent(it);
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				},
				it -> {
					try {
						final TextHElement element = odt.getContentRoot().newTextHElement(2);
						element.setTextContent(it);
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				},
				it -> {
					try {
						exportFlatList(odt.getContentRoot().newTextListElement(), it, configurator);
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}					
				});
		final byte[] content;
		try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			odt.save(output);
			output.flush();
			content = output.toByteArray();
		}
		return content;
	}

	/** Export the publications in a flat list.
	 *
	 * @param list the ODT list in which the publications must be exported.
	 * @param publications the publications to export.
	 * @param configurator the exporter configurator.
	 */
	protected void exportFlatList(TextListElement list, Iterable<? extends Publication> publications, ExporterConfigurator configurator) {
		for (final Publication publication : publications) {
			exportPublication(list, publication, configurator);
		}
	}

	/** Export in ODT a single publication.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 * @param configurator the configurator for the exporter.
	 */
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

	/** Replies the name of the person with a format compliant with the HTML output.
	 *
	 * @param odtText the receiver of the formatted name.
	 * @param person the person.
	 * @param year the year of the publication associated to the author.
	 * @param configurator the configurator for the exporter.
	 */
	protected void formatAuthorName(TextPElement odtText, Person person, int year, ExporterConfigurator configurator) {
		assert configurator != null;
		final ExportedAuthorStatus status = configurator.getExportedAuthorStatusFor(person, year);
		final StringBuilder innerName = new StringBuilder();
		innerName.append(person.getFirstName());
		innerName.append(" "); //$NON-NLS-1$
		innerName.append(person.getLastName().toUpperCase());

		TextSpanElement span;
		switch (status) {
		case SELECTED_PERSON:
			span = this.textHelper.newTextBoldUnderline(odtText);
			span.newTextNode(innerName.toString());
			break;
		case RESEARCHER:
			span = this.textHelper.newTextBold(odtText);
			span.newTextNode(innerName.toString());
			break;
		case PHD_STUDENT:
			span = this.textHelper.newTextUnderline(odtText);
			span.newTextNode(innerName.toString());
			break;
		case POSTDOC_ENGINEER:
			span = this.textHelper.newTextItalic(odtText);
			span.newTextNode(innerName.toString());
			break;
		case OTHER:
		default:
			odtText.newTextNode(innerName.toString());
			break;
		}
	}

	/** Append the DOI link if the DOI is not empty.
	 *
	 * @param receiver the receiver of the ODT content.
	 * @param doi the DOI identifier.
	 * @param prefix the prefix to append to the DOI link.
	 * @return {@code true} if the receiver is changed.
	 */
	@SuppressWarnings("static-method")
	protected boolean appendDoiLink(TextPElement receiver, String doi, String prefix) {
		if (!Strings.isNullOrEmpty(doi)) {
			receiver.newTextNode(prefix);
			final TextAElement aElement = receiver.newTextAElement("https://doi.org/" + doi, null); //$NON-NLS-1$
			aElement.newTextNode(doi);
			return true;
		}
		return false;
	}

	/** Replies the title with a format compliant with the HTML output.
	 * 
	 * @param odtText the receiver of the ODT content.
	 * @param title the title.
	 * @param configurator the configurator of the exporter.
	 */
	protected abstract void formatTitle(TextPElement odtText, String title, ExporterConfigurator configurator);

	/** Append the given ranks to the receiver.
	 *
	 * @param receiver the receiver of the HTML.
	 * @param scimago the Scimago ranking.
	 * @param wos the Web-of-Science ranking.
	 * @param impactFactor the journal's impact factor.
	 * @return {@code true} if the receiver has changed.
	 */
	protected boolean appendRanks(TextPElement receiver, QuartileRanking scimago, QuartileRanking wos, float impactFactor) {
		final String impactFactorStr = this.textHelper.formatNumberIfStrictlyPositive(impactFactor);
		final QuartileRanking scimagoNorm = scimago == null || scimago == QuartileRanking.NR ? null : scimago;
		final QuartileRanking wosNorm = wos == null || wos == QuartileRanking.NR ? null : wos;
		String rank = null;
		if (scimagoNorm != null && wosNorm != null) {
			if (wosNorm != scimagoNorm) {
				final String scimagoStr = scimagoNorm.toString();
				final String wosStr = wosNorm.toString();
				if (this.textHelper.append(receiver, ", ", //$NON-NLS-1$
						this.textHelper.decorateBefore(scimagoStr, this.messages.getMessage(MESSAGES_PREFIX + "SCIMAGO_PREFIX")), //$NON-NLS-1$
						this.textHelper.decorateBefore(wosStr, this.messages.getMessage(MESSAGES_PREFIX + "WOS_PREFIX")), //$NON-NLS-1$
						this.textHelper.decorateBefore(impactFactorStr, this.messages.getMessage(MESSAGES_PREFIX + "IMPACTFACTOR_PREFIX")))) { //$NON-NLS-1$
					receiver.newTextNode(". "); //$NON-NLS-1$
					return true;
				}		
			} else {
				rank = scimagoNorm.toString();
			}
		} else if (scimagoNorm != null) {
			rank = scimagoNorm.toString();
		} else if (wosNorm != null) {
			rank = wosNorm.toString();
		}
		if (this.textHelper.append(receiver, ", ", //$NON-NLS-1$
				this.textHelper.decorateBefore(rank, this.messages.getMessage(MESSAGES_PREFIX + "JOURNALRANK_PREFIX")), //$NON-NLS-1$
				this.textHelper.decorateBefore(impactFactorStr, this.messages.getMessage(MESSAGES_PREFIX + "IMPACTFACTOR_PREFIX")))) { //$NON-NLS-1$
			receiver.newTextNode(". "); //$NON-NLS-1$
			return true;
		}		
		return false;
	}

	/** Append the given ranks to the receiver.
	 *
	 * @param receiver the receiver of the HTML.
	 * @param core the CORE ranking.
	 * @return {@code true} if the receiver has changed.
	 */
	protected boolean appendRanks(TextPElement receiver, CoreRanking core) {
		final CoreRanking coreNorm = core == null || core == CoreRanking.NR ? null : core;
		String rank = null;
		if (coreNorm != null) {
			rank = coreNorm.toString();
		}
		if (this.textHelper.append(receiver,
				this.textHelper.decorateBefore(rank, this.messages.getMessage(MESSAGES_PREFIX + "CORE_PREFIX")))) { //$NON-NLS-1$
			receiver.newTextNode(". "); //$NON-NLS-1$
		}
		return false;
	}

	/** Export in ODT the description of a single book.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, Book publication);

	/** Export in ODT the description of a single book chapter.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, BookChapter publication);

	/** Export in ODT the description of a single conference paper.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, ConferencePaper publication);

	/** Export in ODT the description of a single journal paper.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, JournalPaper publication);

	/** Export in ODT the description of a single journal edition.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, JournalEdition publication);

	/** Export in ODT the description of a single key-note.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, KeyNote publication);

	/** Export in ODT the description of a single report.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, Report publication);

	/** Export in ODT the description of a single thesis.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, Thesis publication);

	/** Export in ODT the description of a single patent.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, Patent publication);

	/** Export in ODT the description of a single document.
	 *
	 * @param odt the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 */
	protected abstract void exportDescription(TextPElement odt, MiscDocument publication);


	/** Export in ODT the description of a single publication.
	 *
	 * @param odtText the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 * @param configurator the configurator of the exporter.
	 */
	protected void exportDescription(TextPElement odtText, Publication publication, ExporterConfigurator configurator) {
		formatTitle(odtText, publication.getTitle(), configurator);

		final Class<? extends Publication> publicationClass = publication.getType().getInstanceType();
		assert publicationClass != null;

		if (publicationClass.equals(Book.class)) {
			exportDescription(odtText, (Book) publication);
		} else if (publicationClass.equals(BookChapter.class)) {
			exportDescription(odtText, (BookChapter) publication);
		} else if (publicationClass.equals(ConferencePaper.class)) {
			exportDescription(odtText, (ConferencePaper) publication);
		} else if (publicationClass.equals(JournalEdition.class)) {
			exportDescription(odtText, (JournalEdition) publication);
		} else if (publicationClass.equals(JournalPaper.class)) {
			exportDescription(odtText, (JournalPaper) publication);
		} else if (publicationClass.equals(KeyNote.class)) {
			exportDescription(odtText, (KeyNote) publication);
		} else if (publicationClass.equals(Report.class)) {
			exportDescription(odtText, (Report) publication);
		} else if (publicationClass.equals(Thesis.class)) {
			exportDescription(odtText, (Thesis) publication);
		} else if (publicationClass.equals(Patent.class)) {
			exportDescription(odtText, (Patent) publication);
		} else if (publicationClass.equals(MiscDocument.class)) {
			exportDescription(odtText, (MiscDocument) publication);
		} else {
			throw new IllegalArgumentException("Unsupported publication type: " + publication.getType()); //$NON-NLS-1$
		}

		final LocalDate date = publication.getPublicationDate();
		if (date != null) {
			final Month month = date.getMonth();
			assert month != null;
			final String displayName = month.getDisplayName(TextStyle.FULL_STANDALONE, java.util.Locale.getDefault());
			odtText.newTextNode(displayName);
			odtText.newTextNode(", "); //$NON-NLS-1$
		}
		final int year = publication.getPublicationYear();
		odtText.newTextNode(Integer.toString(year));
		odtText.newTextNode(". "); //$NON-NLS-1$
	}


	/** Export in ODT the authors of a single publication.
	 *
	 * @param odtText the receiver of the ODT content.
	 * @param publication the publication, never {@code null}.
	 * @param configurator the configurator for the exporter.
	 */
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

}
