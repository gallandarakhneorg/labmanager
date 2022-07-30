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
 * you entered into with the CIAD.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.controller.publication;

import static org.junit.jupiter.api.Assertions.assertEquals;

import fr.ciadlab.labmanager.entities.publication.PublicationCategory;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link PublicationsStat}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class PublicationsStatTest {


	private PublicationsStat test;

	@BeforeEach
	public void setUp() {
		this.test = new PublicationsStat(2022);
	}

	@Test
	public void getYear() {
		assertEquals(2022, this.test.getYear());
	}

	private void testType(PublicationType type) {
		PublicationCategory ucat = type.getCategory(false);
		PublicationCategory rcat = type.getCategory(true);

		this.test.incrementCountForType(type, false, 123);
		assertEquals(123, this.test.getCountForType(type));
		assertEquals(123, this.test.getCountForCategory(ucat));

		this.test.incrementCountForType(type, true, 987);
		assertEquals(123+987, this.test.getCountForType(type));
		if (ucat == rcat) {
			assertEquals(123+987, this.test.getCountForCategory(rcat));
		} else {
			assertEquals(123, this.test.getCountForCategory(ucat));
			assertEquals(987, this.test.getCountForCategory(rcat));
		}

		assertEquals(123+987, this.test.getTotal());
	}

	@Test
	public void incrementCountForType_internationalJournalPaper() {
		testType(PublicationType.INTERNATIONAL_JOURNAL_PAPER);
	}

	@Test
	public void incrementCountForType_nationalJournalPaper() {
		testType(PublicationType.NATIONAL_JOURNAL_PAPER);
	}

	@Test
	public void incrementCountForType_internationalConferencePaper() {
		testType(PublicationType.INTERNATIONAL_CONFERENCE_PAPER);
	}

	@Test
	public void incrementCountForType_nationalConferencePaper() {
		testType(PublicationType.NATIONAL_CONFERENCE_PAPER);
	}

	@Test
	public void incrementCountForType_internationalOralCommunication() {
		testType(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION);
	}

	@Test
	public void incrementCountForType_nationalOralCommunication() {
		testType(PublicationType.NATIONAL_ORAL_COMMUNICATION);
	}

	@Test
	public void incrementCountForType_internationalPoster() {
		testType(PublicationType.INTERNATIONAL_POSTER);
	}

	@Test
	public void incrementCountForType_nationalPoster() {
		testType(PublicationType.NATIONAL_POSTER);
	}

	@Test
	public void incrementCountForType_internationalJournalEdition() {
		testType(PublicationType.INTERNATIONAL_JOURNAL_EDITION);
	}

	@Test
	public void incrementCountForType_nationalJournalEdition() {
		testType(PublicationType.NATIONAL_JOURNAL_EDITION);
	}

	@Test
	public void incrementCountForType_internationalBook() {
		testType(PublicationType.INTERNATIONAL_BOOK);
	}

	@Test
	public void incrementCountForType_nationalBook() {
		testType(PublicationType.NATIONAL_BOOK);
	}

	@Test
	public void incrementCountForType_internationalBookChapter() {
		testType(PublicationType.INTERNATIONAL_BOOK_CHAPTER);
	}

	@Test
	public void incrementCountForType_nationalBookChapter() {
		testType(PublicationType.NATIONAL_BOOK_CHAPTER);
	}

	@Test
	public void incrementCountForType_internationalJournalPaperWithoutCommittee() {
		testType(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE);
	}

	@Test
	public void incrementCountForType_nationalJournalPaperWithoutCommittee() {
		testType(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE);
	}

	@Test
	public void incrementCountForType_internationalKeynote() {
		testType(PublicationType.INTERNATIONAL_KEYNOTE);
	}

	@Test
	public void incrementCountForType_nationalKeynote() {
		testType(PublicationType.NATIONAL_KEYNOTE);
	}

	@Test
	public void incrementCountForType_hdrThesis() {
		testType(PublicationType.HDR_THESIS);
	}

	@Test
	public void incrementCountForType_phdThesis() {
		testType(PublicationType.PHD_THESIS);
	}

	@Test
	public void incrementCountForType_masterThesis() {
		testType(PublicationType.MASTER_THESIS);
	}

	@Test
	public void incrementCountForType_internationalPatent() {
		testType(PublicationType.INTERNATIONAL_PATENT);
	}

	@Test
	public void incrementCountForType_europeanPatent() {
		testType(PublicationType.EUROPEAN_PATENT);
	}

	@Test
	public void incrementCountForType_nationalPatent() {
		testType(PublicationType.NATIONAL_PATENT);
	}

	@Test
	public void incrementCountForType_researchTransfertReport() {
		testType(PublicationType.RESEARCH_TRANSFERT_REPORT);
	}

	@Test
	public void incrementCountForType_researchTools() {
		testType(PublicationType.RESEARCH_TOOLS);
	}

	@Test
	public void incrementCountForType_scientificCultureBook() {
		testType(PublicationType.SCIENTIFIC_CULTURE_BOOK);
	}

	@Test
	public void incrementCountForType_scientificCultureBookChapter() {
		testType(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER);
	}

	@Test
	public void incrementCountForType_scientificCulturePaper() {
		testType(PublicationType.SCIENTIFIC_CULTURE_PAPER);
	}

	@Test
	public void incrementCountForType_artisticProduction() {
		testType(PublicationType.ARTISTIC_PRODUCTION);
	}

	@Test
	public void incrementCountForType_technicalReports() {
		testType(PublicationType.TECHNICAL_REPORTS);
	}

	@Test
	public void incrementCountForType_teachingDocuments() {
		testType(PublicationType.TEACHING_DOCUMENTS);
	}

	@Test
	public void incrementCountForType_tutorialOrDocumentation() {
		testType(PublicationType.TUTORIAL_DOCUMENTATION);
	}

	@Test
	public void incrementCountForType_other() {
		testType(PublicationType.OTHER);
	}

}
