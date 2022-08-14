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

		this.test.increment(type, false, 123);
		assertEquals(123, this.test.count(type));
		assertEquals(123, this.test.count(ucat));

		this.test.increment(type, true, 987);
		assertEquals(123+987, this.test.count(type));
		if (ucat == rcat) {
			assertEquals(123+987, this.test.count(rcat));
		} else {
			assertEquals(123, this.test.count(ucat));
			assertEquals(987, this.test.count(rcat));
		}

		assertEquals(123+987, this.test.getTotal());
	}

	@Test
	public void increment_internationalJournalPaper() {
		testType(PublicationType.INTERNATIONAL_JOURNAL_PAPER);
	}

	@Test
	public void increment_nationalJournalPaper() {
		testType(PublicationType.NATIONAL_JOURNAL_PAPER);
	}

	@Test
	public void increment_internationalConferencePaper() {
		testType(PublicationType.INTERNATIONAL_CONFERENCE_PAPER);
	}

	@Test
	public void increment_nationalConferencePaper() {
		testType(PublicationType.NATIONAL_CONFERENCE_PAPER);
	}

	@Test
	public void increment_internationalOralCommunication() {
		testType(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION);
	}

	@Test
	public void increment_nationalOralCommunication() {
		testType(PublicationType.NATIONAL_ORAL_COMMUNICATION);
	}

	@Test
	public void increment_internationalPoster() {
		testType(PublicationType.INTERNATIONAL_POSTER);
	}

	@Test
	public void increment_nationalPoster() {
		testType(PublicationType.NATIONAL_POSTER);
	}

	@Test
	public void increment_internationalJournalEdition() {
		testType(PublicationType.INTERNATIONAL_JOURNAL_EDITION);
	}

	@Test
	public void increment_nationalJournalEdition() {
		testType(PublicationType.NATIONAL_JOURNAL_EDITION);
	}

	@Test
	public void increment_internationalBook() {
		testType(PublicationType.INTERNATIONAL_BOOK);
	}

	@Test
	public void increment_nationalBook() {
		testType(PublicationType.NATIONAL_BOOK);
	}

	@Test
	public void increment_internationalBookChapter() {
		testType(PublicationType.INTERNATIONAL_BOOK_CHAPTER);
	}

	@Test
	public void increment_nationalBookChapter() {
		testType(PublicationType.NATIONAL_BOOK_CHAPTER);
	}

	@Test
	public void increment_internationalJournalPaperWithoutCommittee() {
		testType(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE);
	}

	@Test
	public void increment_nationalJournalPaperWithoutCommittee() {
		testType(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE);
	}

	@Test
	public void increment_internationalKeynote() {
		testType(PublicationType.INTERNATIONAL_KEYNOTE);
	}

	@Test
	public void increment_nationalKeynote() {
		testType(PublicationType.NATIONAL_KEYNOTE);
	}

	@Test
	public void increment_hdrThesis() {
		testType(PublicationType.HDR_THESIS);
	}

	@Test
	public void increment_phdThesis() {
		testType(PublicationType.PHD_THESIS);
	}

	@Test
	public void increment_masterThesis() {
		testType(PublicationType.MASTER_THESIS);
	}

	@Test
	public void increment_internationalPatent() {
		testType(PublicationType.INTERNATIONAL_PATENT);
	}

	@Test
	public void increment_europeanPatent() {
		testType(PublicationType.EUROPEAN_PATENT);
	}

	@Test
	public void increment_nationalPatent() {
		testType(PublicationType.NATIONAL_PATENT);
	}

	@Test
	public void increment_researchTransfertReport() {
		testType(PublicationType.RESEARCH_TRANSFERT_REPORT);
	}

	@Test
	public void increment_researchTools() {
		testType(PublicationType.RESEARCH_TOOL);
	}

	@Test
	public void increment_scientificCultureBook() {
		testType(PublicationType.SCIENTIFIC_CULTURE_BOOK);
	}

	@Test
	public void increment_scientificCultureBookChapter() {
		testType(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER);
	}

	@Test
	public void increment_scientificCulturePaper() {
		testType(PublicationType.SCIENTIFIC_CULTURE_PAPER);
	}

	@Test
	public void increment_artisticProduction() {
		testType(PublicationType.ARTISTIC_PRODUCTION);
	}

	@Test
	public void increment_technicalReports() {
		testType(PublicationType.TECHNICAL_REPORT);
	}

	@Test
	public void increment_teachingDocuments() {
		testType(PublicationType.TEACHING_DOCUMENT);
	}

	@Test
	public void increment_tutorialOrDocumentation() {
		testType(PublicationType.TUTORIAL_DOCUMENTATION);
	}

	@Test
	public void increment_other() {
		testType(PublicationType.OTHER);
	}

}
