/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
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

package fr.ciadlab.labmanager.io.ris;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import ch.difty.kris.KRisIO;
import ch.difty.kris.domain.RisRecord;
import ch.difty.kris.domain.RisType;
import fr.ciadlab.labmanager.entities.conference.Conference;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationCategory;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
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
import fr.ciadlab.labmanager.utils.IntegerRange;
import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.stereotype.Component;

/** Utilities for RIS based on the Kris library.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.7
 * @see "https://en.wikipedia.org/wiki/RIS_(file_format)"
 */
@Component
public class KrisRIS extends AbstractRIS {

	@Override
	public void exportPublications(Writer output, Iterable<? extends Publication> publications,
			ExporterConfigurator configurator) throws IOException {
		 final List<RisRecord> records = new ArrayList<>();
		 final Iterator<? extends Publication> iterator = publications.iterator();
		 while (iterator.hasNext()) {
			 final Publication publication = iterator.next();
			 exportPublication(publication, records);
		 }
		 KRisIO.export(records, output);
	}

	/** Export a single publication to RIS record.
	 * 
	 * @param publication the publication to export.
	 * @param records the receiver of the new record.
	 */
	protected void exportPublication(Publication publication, List<RisRecord> records) {
		RisRecord record = null;
		switch (publication.getType()) {
		case INTERNATIONAL_JOURNAL_PAPER:
		case INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE:
		case NATIONAL_JOURNAL_PAPER:
		case NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE:
			record = createRecord((JournalPaper) publication, RisType.JOUR);
			break;
		case INTERNATIONAL_CONFERENCE_PAPER:
		case NATIONAL_CONFERENCE_PAPER:
		case INTERNATIONAL_ORAL_COMMUNICATION:
		case NATIONAL_ORAL_COMMUNICATION:
		case INTERNATIONAL_POSTER:
		case NATIONAL_POSTER:
			record = createRecord((ConferencePaper) publication);
			break;
		case INTERNATIONAL_BOOK:
		case NATIONAL_BOOK:
		case SCIENTIFIC_CULTURE_BOOK:
			record = createRecord((Book) publication);
			break;
		case INTERNATIONAL_BOOK_CHAPTER:
		case NATIONAL_BOOK_CHAPTER:
		case SCIENTIFIC_CULTURE_BOOK_CHAPTER:
			record = createRecord((BookChapter) publication);
			break;
		case HDR_THESIS:
		case PHD_THESIS:
		case MASTER_THESIS:
			record = createRecord((Thesis) publication);
			break;
		case INTERNATIONAL_JOURNAL_EDITION:
		case NATIONAL_JOURNAL_EDITION:
			record = createRecord((JournalEdition) publication);
			break;
		case INTERNATIONAL_KEYNOTE:
		case NATIONAL_KEYNOTE:
			record = createRecord((KeyNote) publication);
			break;
		case TECHNICAL_REPORT:
		case PROJECT_REPORT:
		case RESEARCH_TRANSFERT_REPORT:
		case TEACHING_DOCUMENT:
		case TUTORIAL_DOCUMENTATION:
			record = createRecord((Report) publication);
			break;
		case INTERNATIONAL_PATENT:
		case EUROPEAN_PATENT:
		case NATIONAL_PATENT:
			record = createRecord((Patent) publication);
			break;
		case SCIENTIFIC_CULTURE_PAPER:
			record = createRecord((JournalPaper) publication, RisType.MGZN);
			break;
		case ARTISTIC_PRODUCTION:
			record = createRecord((MiscDocument) publication, RisType.ART);
			break;
		case RESEARCH_TOOL:
			record = createRecord((MiscDocument) publication, RisType.COMP);
			break;
		case INTERNATIONAL_PRESENTATION:
		case NATIONAL_PRESENTATION:
		case INTERNATIONAL_SCIENTIFIC_CULTURE_PRESENTATION:
		case NATIONAL_SCIENTIFIC_CULTURE_PRESENTATION:
			record = createRecord((MiscDocument) publication, RisType.HEAR);
			break;
		case OTHER:
			record = createRecord((MiscDocument) publication, RisType.GEN);
			break;
		default:
			throw new IllegalArgumentException("Unsupported publication type for export: " + publication.getType()); //$NON-NLS-1$
		}
		if (record != null) {
			records.add(record);
		}
	}

	/** Create a record builder with the attributes that are shared by all the types of publications.
	 *
	 * @param risType the type of the record.
	 * @param publication the publication to export.
	 * @param insertIssnIsbn indicates if the ISBN/ISSN number should be inserted.
	 * @return the record builder.
	 */
	@SuppressWarnings("static-method")
	protected RisRecord.Builder createStandardRecord(RisType risType, Publication publication, boolean insertIssnIsbn) {
		final List<String> authors = publication.getAuthors().stream()
				.map(it -> it.getLastName() + ", " + it.getFirstName()) //$NON-NLS-1$
				.collect(Collectors.toList());
		final List<String> keywords = Arrays.asList(publication.getKeywords().split("\\s*[,;:./]\\s*")); //$NON-NLS-1$
		final String url = Arrays.asList(
					publication.getExtraURL(), publication.getDblpURL(), publication.getVideoURL()).stream()
				.filter(it -> !Strings.isNullOrEmpty(it))
				.findFirst().orElse(null);
		final PublicationType type = publication.getType();
		final PublicationCategory cat = publication.getCategory();
		// Force the Java locale to get the text that is corresponding to the language of the paper
		final java.util.Locale loc = java.util.Locale.getDefault();
		final String publicationType;
		final String publicationTypeName;
		try {
			java.util.Locale.setDefault(publication.getMajorLanguage().getLocale());
			if (cat != null) {
				publicationType = cat.name();
				publicationTypeName = cat.getLabel();
			} else if (type != null) {
				publicationType = type.name();
				publicationTypeName = type.getLabel();
			} else {
				publicationType = null;
				publicationTypeName = null;
			}
		} finally {
			java.util.Locale.setDefault(loc);
		}
		RisRecord.Builder builder = new RisRecord.Builder()
				.type(risType)
				.title(publication.getTitle())
				.authors(authors)
				.publicationYear(Integer.toString(publication.getPublicationYear()))
				.abstr(publication.getAbstractText())
				.keywords(keywords)
				.doi(publication.getDOI())
				.url(url)
				.language(publication.getMajorLanguage().name())
				.custom1(publicationType)
				.custom2(publicationTypeName);
		if (insertIssnIsbn) {
			final String isbnissn = Arrays.asList(publication.getISBN(), publication.getISSN()).stream()
					.filter(it -> !Strings.isNullOrEmpty(it))
					.findFirst().orElse(null);
			builder = builder.isbnIssn(isbnissn);
		}
		return builder;
	}

	/** Create the RIS record for the given journal paper.
	 *
	 * @param publication the publication to export.
	 * @param risType the type of paper.
	 * @return the RIS record.
	 */
	protected RisRecord createRecord(JournalPaper publication, RisType risType) {
		RisRecord.Builder builder = createStandardRecord(risType, publication, false);
		final Journal journal = publication.getJournal();
		if (journal != null) {
			final String isbnissn = Arrays.asList(journal.getISBN(), journal.getISSN()).stream()
					.filter(it -> !Strings.isNullOrEmpty(it))
					.findFirst().orElse(null);
			builder = builder.periodicalNameFullFormatJO(journal.getJournalName())
					.publisher(journal.getPublisher())
					.publishingPlace(journal.getAddress())
					.isbnIssn(isbnissn);
		}
		builder = builder
			.volumeNumber(publication.getVolume())
			.numberOfVolumes(publication.getNumber())
			.section(publication.getSeries());
		final IntegerRange range = parsePages(publication.getPages());
		if (range != null) {
			builder = builder.startPage(range.getMin().toString()).endPage(range.getMax().toString());
		}
		if (publication.getScimagoQIndex() != null && publication.getScimagoQIndex() != QuartileRanking.NR) {
			builder = builder.custom3(publication.getScimagoQIndex().name());
		}
		if (publication.getWosQIndex() != null && publication.getWosQIndex() != QuartileRanking.NR) {
			builder = builder.custom4(publication.getWosQIndex().name());
		}
		if (publication.getImpactFactor() > 0f) {
			builder = builder.custom5(Float.toString(publication.getImpactFactor()));
		}
		return builder.build();
	}

	/** Create the RIS record for the given conference paper.
	 *
	 * @param publication the publication to export.
	 * @return the RIS record.
	 */
	protected RisRecord createRecord(ConferencePaper publication) {
		RisRecord.Builder builder = createStandardRecord(RisType.CPAPER, publication, false);
		final Conference conference = publication.getConference();
		if (conference != null) {
			final String isbnissn = Arrays.asList(conference.getISBN(), conference.getISSN()).stream()
					.filter(it -> !Strings.isNullOrEmpty(it))
					.findFirst().orElse(null);
			builder = builder.secondaryTitle(conference.getName())
					.publisher(conference.getPublisher())
					.isbnIssn(isbnissn);
		}
		builder = builder
			.secondaryTitle(publication.getPublicationTarget())
			.publishingPlace(publication.getAddress())
			.volumeNumber(publication.getVolume())
			.numberOfVolumes(publication.getNumber())
			.tertiaryTitle(publication.getSeries())
			.editor(publication.getEditors())
			.custom4(publication.getOrganization());
		final IntegerRange range = parsePages(publication.getPages());
		if (range != null) {
			builder = builder.startPage(range.getMin().toString()).endPage(range.getMax().toString());
		}
		if (publication.getCoreRanking() != null && publication.getCoreRanking() != CoreRanking.NR) {
			builder = builder.custom3(publication.getCoreRanking().name());
		}
		return builder.build();
	}

	/** Create the RIS record for the given book.
	 *
	 * @param publication the publication to export.
	 * @return the RIS record.
	 */
	protected RisRecord createRecord(Book publication) {
		RisRecord.Builder builder = createStandardRecord(RisType.BOOK, publication, true)
				.publisher(publication.getPublisher())
				.publishingPlace(publication.getAddress())
				.editor(publication.getEditors())
				.volumeNumber(publication.getVolume())
				.numberOfVolumes(publication.getNumber())
				.edition(publication.getEdition())
				.section(publication.getSeries());
		final IntegerRange range = parsePages(publication.getPages());
		if (range != null) {
			builder = builder.startPage(range.getMin().toString()).endPage(range.getMax().toString());
		}
		return builder.build();
	}

	/** Create the RIS record for the given book chapter.
	 *
	 * @param publication the publication to export.
	 * @return the RIS record.
	 */
	protected RisRecord createRecord(BookChapter publication) {
		RisRecord.Builder builder = createStandardRecord(RisType.CHAP, publication, true)
				.secondaryTitle(publication.getBookTitle())
				.section(publication.getChapterNumber())
				.publisher(publication.getPublisher())
				.publishingPlace(publication.getAddress())
				.editor(publication.getEditors())
				.volumeNumber(publication.getVolume())
				.numberOfVolumes(publication.getNumber())
				.edition(publication.getEdition())
				.tertiaryTitle(publication.getSeries());
		final IntegerRange range = parsePages(publication.getPages());
		if (range != null) {
			builder = builder.startPage(range.getMin().toString()).endPage(range.getMax().toString());
		}
		return builder.build();
	}

	/** Create the RIS record for the given thesis.
	 *
	 * @param publication the publication to export.
	 * @return the RIS record.
	 */
	protected RisRecord createRecord(Thesis publication) {
		// Force the Java locale to get the text that is corresponding to the language of the paper
		final java.util.Locale loc = java.util.Locale.getDefault();
		final String thesisType;
		try {
			java.util.Locale.setDefault(publication.getMajorLanguage().getLocale());
			thesisType = publication.getType().getLabel();
		} finally {
			java.util.Locale.setDefault(loc);
		}
		final RisRecord.Builder builder = createStandardRecord(RisType.THES, publication, true)
				.publisher(publication.getInstitution())
				.publishingPlace(publication.getAddress())
				.custom3(thesisType);
		return builder.build();
	}

	/** Create the RIS record for the given journal edition.
	 *
	 * @param publication the publication to export.
	 * @return the RIS record.
	 */
	protected RisRecord createRecord(JournalEdition publication) {
		RisRecord.Builder builder = createStandardRecord(RisType.EDBOOK, publication, true);
		final Journal journal = publication.getJournal();
		if (journal != null) {
			final String isbnissn = Arrays.asList(journal.getISBN(), journal.getISSN()).stream()
					.filter(it -> !Strings.isNullOrEmpty(it))
					.findFirst().orElse(null);
			builder = builder.periodicalNameFullFormatJO(journal.getJournalName())
					.publisher(journal.getPublisher())
					.publishingPlace(journal.getAddress())
					.isbnIssn(isbnissn);
		}
		builder = builder
			.volumeNumber(publication.getVolume())
			.numberOfVolumes(publication.getNumber());
		final IntegerRange range = parsePages(publication.getPages());
		if (range != null) {
			builder = builder.startPage(range.getMin().toString()).endPage(range.getMax().toString());
		}
		if (publication.getScimagoQIndex() != null && publication.getScimagoQIndex() != QuartileRanking.NR) {
			builder = builder.custom3(publication.getScimagoQIndex().name());
		}
		if (publication.getWosQIndex() != null && publication.getWosQIndex() != QuartileRanking.NR) {
			builder = builder.custom4(publication.getWosQIndex().name());
		}
		if (publication.getImpactFactor() > 0f) {
			builder = builder.custom5(Float.toString(publication.getImpactFactor()));
		}
		return builder.build();
	}

	/** Create the RIS record for the given conference keynote.
	 *
	 * @param publication the publication to export.
	 * @return the RIS record.
	 */
	protected RisRecord createRecord(KeyNote publication) {
		// Force the Java locale to get the text that is corresponding to the language of the paper
		final java.util.Locale loc = java.util.Locale.getDefault();
		final String keyNoteType;
		try {
			java.util.Locale.setDefault(publication.getMajorLanguage().getLocale());
			keyNoteType = publication.getType().getLabel();
		} finally {
			java.util.Locale.setDefault(loc);
		}
		final RisRecord.Builder builder = createStandardRecord(RisType.HEAR, publication, true)
				.periodicalNameFullFormatJO(publication.getPublicationTarget())
				.editor(publication.getEditors())
				.publisher(publication.getOrganization())
				.publishingPlace(publication.getAddress())
				.custom3(keyNoteType);
		return builder.build();
	}

	/** Create the RIS record for the given report.
	 *
	 * @param publication the publication to export.
	 * @return the RIS record.
	 */
	protected RisRecord createRecord(Report publication) {
		final RisRecord.Builder builder = createStandardRecord(RisType.RPRT, publication, true)
				.volumeNumber(publication.getReportNumber())
				.publisher(publication.getInstitution())
				.publishingPlace(publication.getAddress())
				.custom3(publication.getReportType());
		return builder.build();
	}

	/** Create the RIS record for the given patent.
	 *
	 * @param publication the publication to export.
	 * @return the RIS record.
	 */
	protected RisRecord createRecord(Patent publication) {
		final RisRecord.Builder builder = createStandardRecord(RisType.PAT, publication, true)
				.publishingPlace(publication.getAddress())
				.publisherStandardNumber(publication.getPatentNumber())
				.publisher(publication.getInstitution())
				.custom3(publication.getPatentType()) ;
		return builder.build();
	}

	/** Create the RIS record for the given misc document.
	 *
	 * @param publication the publication to export.
	 * @param risType the RIS type for the given publication.
	 * @return the RIS record.
	 */
	protected RisRecord createRecord(MiscDocument publication, RisType risType) {
		final RisRecord.Builder builder = createStandardRecord(RisType.GEN, publication, true)
				.secondaryTitle(publication.getHowPublished())
				.volumeNumber(publication.getDocumentNumber())
				.editor(publication.getOrganization())
				.publisher(publication.getPublisher())
				.publishingPlace(publication.getAddress())
				.custom3(publication.getDocumentType()) ;
		return builder.build();
	}

}
