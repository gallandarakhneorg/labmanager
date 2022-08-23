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

package fr.ciadlab.labmanager.entities.publication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link PublicationType}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class PublicationTypeTest {

	private static final Random RANDOM = new Random();

	private List<PublicationType> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(PublicationType.values()));
	}

	private PublicationType cons(PublicationType type) {
		assertTrue(this.items.remove(type), "Expecting enumeration item: " + type.toString());
		return type;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	private static String randomString(String text) {
		final StringBuilder buf = new StringBuilder();
		for (int i = 0; i < text.length(); ++i) {
			final String s = Character.toString(text.charAt(i));
			if (RANDOM.nextBoolean()) {
				buf.append(s.toLowerCase());
			} else {
				buf.append(s.toUpperCase());
			}
		}
		return buf.toString();
	}

	@Test
	public void getCategory_trueParam() {
		assertEquals(PublicationCategory.ACL, cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).getCategory(true));
		assertEquals(PublicationCategory.ACL, cons(PublicationType.NATIONAL_JOURNAL_PAPER).getCategory(true));
		assertEquals(PublicationCategory.C_ACTI, cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).getCategory(true));
		assertEquals(PublicationCategory.C_ACTN, cons(PublicationType.NATIONAL_CONFERENCE_PAPER).getCategory(true));
		assertEquals(PublicationCategory.C_COM, cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).getCategory(true));
		assertEquals(PublicationCategory.C_COM, cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).getCategory(true));
		assertEquals(PublicationCategory.C_AFF, cons(PublicationType.INTERNATIONAL_POSTER).getCategory(true));
		assertEquals(PublicationCategory.C_AFF, cons(PublicationType.NATIONAL_POSTER).getCategory(true));
		assertEquals(PublicationCategory.DO, cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).getCategory(true));
		assertEquals(PublicationCategory.DO, cons(PublicationType.NATIONAL_JOURNAL_EDITION).getCategory(true));
		assertEquals(PublicationCategory.OS, cons(PublicationType.INTERNATIONAL_BOOK).getCategory(true));
		assertEquals(PublicationCategory.OS, cons(PublicationType.NATIONAL_BOOK).getCategory(true));
		assertEquals(PublicationCategory.COS, cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).getCategory(true));
		assertEquals(PublicationCategory.COS, cons(PublicationType.NATIONAL_BOOK_CHAPTER).getCategory(true));
		assertEquals(PublicationCategory.ASCL, cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).getCategory(true));
		assertEquals(PublicationCategory.ASCL, cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).getCategory(true));
		assertEquals(PublicationCategory.C_INV, cons(PublicationType.INTERNATIONAL_KEYNOTE).getCategory(true));
		assertEquals(PublicationCategory.C_INV, cons(PublicationType.NATIONAL_KEYNOTE).getCategory(true));
		assertEquals(PublicationCategory.TH, cons(PublicationType.HDR_THESIS).getCategory(true));
		assertEquals(PublicationCategory.TH, cons(PublicationType.PHD_THESIS).getCategory(true));
		assertEquals(PublicationCategory.TH, cons(PublicationType.MASTER_THESIS).getCategory(true));
		assertEquals(PublicationCategory.BRE, cons(PublicationType.INTERNATIONAL_PATENT).getCategory(true));
		assertEquals(PublicationCategory.BRE, cons(PublicationType.EUROPEAN_PATENT).getCategory(true));
		assertEquals(PublicationCategory.BRE, cons(PublicationType.NATIONAL_PATENT).getCategory(true));
		assertEquals(PublicationCategory.PT, cons(PublicationType.RESEARCH_TRANSFERT_REPORT).getCategory(true));
		assertEquals(PublicationCategory.OR, cons(PublicationType.RESEARCH_TOOL).getCategory(true));
		assertEquals(PublicationCategory.OV, cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).getCategory(true));
		assertEquals(PublicationCategory.COV, cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).getCategory(true));
		assertEquals(PublicationCategory.PV, cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).getCategory(true));
		assertEquals(PublicationCategory.PAT, cons(PublicationType.ARTISTIC_PRODUCTION).getCategory(true));
		assertEquals(PublicationCategory.AP, cons(PublicationType.TECHNICAL_REPORT).getCategory(true));
		assertEquals(PublicationCategory.AP, cons(PublicationType.PROJECT_REPORT).getCategory(true));
		assertEquals(PublicationCategory.AP, cons(PublicationType.TEACHING_DOCUMENT).getCategory(true));
		assertEquals(PublicationCategory.AP, cons(PublicationType.TUTORIAL_DOCUMENTATION).getCategory(true));
		assertEquals(PublicationCategory.AP, cons(PublicationType.OTHER).getCategory(true));
		assertAllTreated();
	}

	@Test
	public void getCategory_falseParam() {
		assertEquals(PublicationCategory.ACLN, cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).getCategory(false));
		assertEquals(PublicationCategory.ACLN, cons(PublicationType.NATIONAL_JOURNAL_PAPER).getCategory(false));
		assertEquals(PublicationCategory.C_ACTI, cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).getCategory(false));
		assertEquals(PublicationCategory.C_ACTN, cons(PublicationType.NATIONAL_CONFERENCE_PAPER).getCategory(false));
		assertEquals(PublicationCategory.C_COM, cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).getCategory(false));
		assertEquals(PublicationCategory.C_COM, cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).getCategory(false));
		assertEquals(PublicationCategory.C_AFF, cons(PublicationType.INTERNATIONAL_POSTER).getCategory(false));
		assertEquals(PublicationCategory.C_AFF, cons(PublicationType.NATIONAL_POSTER).getCategory(false));
		assertEquals(PublicationCategory.DO, cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).getCategory(false));
		assertEquals(PublicationCategory.DO, cons(PublicationType.NATIONAL_JOURNAL_EDITION).getCategory(false));
		assertEquals(PublicationCategory.OS, cons(PublicationType.INTERNATIONAL_BOOK).getCategory(false));
		assertEquals(PublicationCategory.OS, cons(PublicationType.NATIONAL_BOOK).getCategory(false));
		assertEquals(PublicationCategory.COS, cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).getCategory(false));
		assertEquals(PublicationCategory.COS, cons(PublicationType.NATIONAL_BOOK_CHAPTER).getCategory(false));
		assertEquals(PublicationCategory.ASCL, cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).getCategory(false));
		assertEquals(PublicationCategory.ASCL, cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).getCategory(false));
		assertEquals(PublicationCategory.C_INV, cons(PublicationType.INTERNATIONAL_KEYNOTE).getCategory(false));
		assertEquals(PublicationCategory.C_INV, cons(PublicationType.NATIONAL_KEYNOTE).getCategory(false));
		assertEquals(PublicationCategory.TH, cons(PublicationType.HDR_THESIS).getCategory(false));
		assertEquals(PublicationCategory.TH, cons(PublicationType.PHD_THESIS).getCategory(false));
		assertEquals(PublicationCategory.TH, cons(PublicationType.MASTER_THESIS).getCategory(false));
		assertEquals(PublicationCategory.BRE, cons(PublicationType.INTERNATIONAL_PATENT).getCategory(false));
		assertEquals(PublicationCategory.BRE, cons(PublicationType.EUROPEAN_PATENT).getCategory(false));
		assertEquals(PublicationCategory.BRE, cons(PublicationType.NATIONAL_PATENT).getCategory(false));
		assertEquals(PublicationCategory.PT, cons(PublicationType.RESEARCH_TRANSFERT_REPORT).getCategory(false));
		assertEquals(PublicationCategory.OR, cons(PublicationType.RESEARCH_TOOL).getCategory(false));
		assertEquals(PublicationCategory.OV, cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).getCategory(false));
		assertEquals(PublicationCategory.COV, cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).getCategory(false));
		assertEquals(PublicationCategory.PV, cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).getCategory(false));
		assertEquals(PublicationCategory.PAT, cons(PublicationType.ARTISTIC_PRODUCTION).getCategory(false));
		assertEquals(PublicationCategory.AP, cons(PublicationType.TECHNICAL_REPORT).getCategory(false));
		assertEquals(PublicationCategory.AP, cons(PublicationType.PROJECT_REPORT).getCategory(false));
		assertEquals(PublicationCategory.AP, cons(PublicationType.TEACHING_DOCUMENT).getCategory(false));
		assertEquals(PublicationCategory.AP, cons(PublicationType.TUTORIAL_DOCUMENTATION).getCategory(false));
		assertEquals(PublicationCategory.AP, cons(PublicationType.OTHER).getCategory(false));
		assertAllTreated();
	}

	@Test
	public void getCategories() {
		assertEquals(Sets.newHashSet(PublicationCategory.ACL, PublicationCategory.ACLN), cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).getCategories());
		assertEquals(Sets.newHashSet(PublicationCategory.ACL, PublicationCategory.ACLN), cons(PublicationType.NATIONAL_JOURNAL_PAPER).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.C_ACTI), cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.C_ACTN), cons(PublicationType.NATIONAL_CONFERENCE_PAPER).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.C_COM), cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.C_COM), cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.C_AFF), cons(PublicationType.INTERNATIONAL_POSTER).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.C_AFF), cons(PublicationType.NATIONAL_POSTER).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.DO), cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.DO), cons(PublicationType.NATIONAL_JOURNAL_EDITION).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.OS), cons(PublicationType.INTERNATIONAL_BOOK).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.OS), cons(PublicationType.NATIONAL_BOOK).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.COS), cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.COS), cons(PublicationType.NATIONAL_BOOK_CHAPTER).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.ASCL), cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.ASCL), cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.C_INV), cons(PublicationType.INTERNATIONAL_KEYNOTE).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.C_INV), cons(PublicationType.NATIONAL_KEYNOTE).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.TH), cons(PublicationType.HDR_THESIS).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.TH), cons(PublicationType.PHD_THESIS).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.TH), cons(PublicationType.MASTER_THESIS).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.BRE), cons(PublicationType.INTERNATIONAL_PATENT).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.BRE), cons(PublicationType.EUROPEAN_PATENT).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.BRE), cons(PublicationType.NATIONAL_PATENT).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.PT), cons(PublicationType.RESEARCH_TRANSFERT_REPORT).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.OR), cons(PublicationType.RESEARCH_TOOL).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.OV), cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.COV), cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.PV), cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.PAT), cons(PublicationType.ARTISTIC_PRODUCTION).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.AP), cons(PublicationType.TECHNICAL_REPORT).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.AP), cons(PublicationType.PROJECT_REPORT).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.AP), cons(PublicationType.TEACHING_DOCUMENT).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.AP), cons(PublicationType.TUTORIAL_DOCUMENTATION).getCategories());
		assertEquals(Collections.singleton(PublicationCategory.AP), cons(PublicationType.OTHER).getCategories());
		assertAllTreated();
	}

	@Test
	public void isInternational() {
		assertTrue(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isInternational());
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isInternational());
		assertTrue(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isInternational());
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isInternational());
		assertTrue(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isInternational());
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isInternational());
		assertTrue(cons(PublicationType.INTERNATIONAL_POSTER).isInternational());
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isInternational());
		assertTrue(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isInternational());
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isInternational());
		assertTrue(cons(PublicationType.INTERNATIONAL_BOOK).isInternational());
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isInternational());
		assertTrue(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isInternational());
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isInternational());
		assertTrue(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isInternational());
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isInternational());
		assertTrue(cons(PublicationType.INTERNATIONAL_KEYNOTE).isInternational());
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isInternational());
		assertFalse(cons(PublicationType.HDR_THESIS).isInternational());
		assertFalse(cons(PublicationType.PHD_THESIS).isInternational());
		assertFalse(cons(PublicationType.MASTER_THESIS).isInternational());
		assertTrue(cons(PublicationType.INTERNATIONAL_PATENT).isInternational());
		assertTrue(cons(PublicationType.EUROPEAN_PATENT).isInternational());
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isInternational());
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isInternational());
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isInternational());
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isInternational());
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isInternational());
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isInternational());
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isInternational());
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isInternational());
		assertFalse(cons(PublicationType.PROJECT_REPORT).isInternational());
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isInternational());
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isInternational());
		assertFalse(cons(PublicationType.OTHER).isInternational());
		assertAllTreated();
	}

	@Test
	public void getLabel_US() {
		// Force the default locale to be US
		Locale.setDefault(Locale.US);
		assertEquals("Articles in international journals with selection committee", cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).getLabel());
		assertEquals("Articles in national journals with selection committee", cons(PublicationType.NATIONAL_JOURNAL_PAPER).getLabel());
		assertEquals("Papers in the proceedings of an international conference", cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).getLabel());
		assertEquals("Papers in the proceedings of a national conference", cons(PublicationType.NATIONAL_CONFERENCE_PAPER).getLabel());
		assertEquals("Oral Communications without proceeding in international conference", cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).getLabel());
		assertEquals("Oral Communications without proceeding in national conference", cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).getLabel());
		assertEquals("Posters in international conference", cons(PublicationType.INTERNATIONAL_POSTER).getLabel());
		assertEquals("Posters in national conference", cons(PublicationType.NATIONAL_POSTER).getLabel());
		assertEquals("Editor of international books or journals", cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).getLabel());
		assertEquals("Editor of national books or journals", cons(PublicationType.NATIONAL_JOURNAL_EDITION).getLabel());
		assertEquals("International scientific books", cons(PublicationType.INTERNATIONAL_BOOK).getLabel());
		assertEquals("National scientific books", cons(PublicationType.NATIONAL_BOOK).getLabel());
		assertEquals("Chapters in international scientific books", cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).getLabel());
		assertEquals("Chapters in national scientific books", cons(PublicationType.NATIONAL_BOOK_CHAPTER).getLabel());
		assertEquals("Articles in international journals without selection committee", cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).getLabel());
		assertEquals("Articles in national journals without selection committee", cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).getLabel());
		assertEquals("Keynotes in international conference", cons(PublicationType.INTERNATIONAL_KEYNOTE).getLabel());
		assertEquals("Keynotes in national conference", cons(PublicationType.NATIONAL_KEYNOTE).getLabel());
		assertEquals("HDR theses", cons(PublicationType.HDR_THESIS).getLabel());
		assertEquals("PhD theses", cons(PublicationType.PHD_THESIS).getLabel());
		assertEquals("Master theses", cons(PublicationType.MASTER_THESIS).getLabel());
		assertEquals("International patents", cons(PublicationType.INTERNATIONAL_PATENT).getLabel());
		assertEquals("European patents", cons(PublicationType.EUROPEAN_PATENT).getLabel());
		assertEquals("National patents", cons(PublicationType.NATIONAL_PATENT).getLabel());
		assertEquals("Publications for research transfer", cons(PublicationType.RESEARCH_TRANSFERT_REPORT).getLabel());
		assertEquals("Research tools", cons(PublicationType.RESEARCH_TOOL).getLabel());
		assertEquals("Books for scientific culture dissemination", cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).getLabel());
		assertEquals("Chapters in a book for scientific culture dissemination", cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).getLabel());
		assertEquals("Paper in a journal for scientific culture dissemination", cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).getLabel());
		assertEquals("Artistic research productions", cons(PublicationType.ARTISTIC_PRODUCTION).getLabel());
		assertEquals("Technical reports", cons(PublicationType.TECHNICAL_REPORT).getLabel());
		assertEquals("Project reports", cons(PublicationType.PROJECT_REPORT).getLabel());
		assertEquals("Teaching documents", cons(PublicationType.TEACHING_DOCUMENT).getLabel());
		assertEquals("Tutorials or documentations", cons(PublicationType.TUTORIAL_DOCUMENTATION).getLabel());
		assertEquals("Other productions", cons(PublicationType.OTHER).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_FR() {
		// Force the default locale to be FR
		Locale.setDefault(Locale.FRANCE);
		assertEquals("Articles in international journals with selection committee", cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).getLabel());
		assertEquals("Articles in national journals with selection committee", cons(PublicationType.NATIONAL_JOURNAL_PAPER).getLabel());
		assertEquals("Papers in the proceedings of an international conference", cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).getLabel());
		assertEquals("Papers in the proceedings of a national conference", cons(PublicationType.NATIONAL_CONFERENCE_PAPER).getLabel());
		assertEquals("Oral Communications without proceeding in international conference", cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).getLabel());
		assertEquals("Oral Communications without proceeding in national conference", cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).getLabel());
		assertEquals("Posters in international conference", cons(PublicationType.INTERNATIONAL_POSTER).getLabel());
		assertEquals("Posters in national conference", cons(PublicationType.NATIONAL_POSTER).getLabel());
		assertEquals("Editor of international books or journals", cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).getLabel());
		assertEquals("Editor of national books or journals", cons(PublicationType.NATIONAL_JOURNAL_EDITION).getLabel());
		assertEquals("International scientific books", cons(PublicationType.INTERNATIONAL_BOOK).getLabel());
		assertEquals("National scientific books", cons(PublicationType.NATIONAL_BOOK).getLabel());
		assertEquals("Chapters in international scientific books", cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).getLabel());
		assertEquals("Chapters in national scientific books", cons(PublicationType.NATIONAL_BOOK_CHAPTER).getLabel());
		assertEquals("Articles in international journals without selection committee", cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).getLabel());
		assertEquals("Articles in national journals without selection committee", cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).getLabel());
		assertEquals("Keynotes in international conference", cons(PublicationType.INTERNATIONAL_KEYNOTE).getLabel());
		assertEquals("Keynotes in national conference", cons(PublicationType.NATIONAL_KEYNOTE).getLabel());
		assertEquals("HDR theses", cons(PublicationType.HDR_THESIS).getLabel());
		assertEquals("PhD theses", cons(PublicationType.PHD_THESIS).getLabel());
		assertEquals("Master theses", cons(PublicationType.MASTER_THESIS).getLabel());
		assertEquals("International patents", cons(PublicationType.INTERNATIONAL_PATENT).getLabel());
		assertEquals("European patents", cons(PublicationType.EUROPEAN_PATENT).getLabel());
		assertEquals("National patents", cons(PublicationType.NATIONAL_PATENT).getLabel());
		assertEquals("Publications for research transfer", cons(PublicationType.RESEARCH_TRANSFERT_REPORT).getLabel());
		assertEquals("Research tools", cons(PublicationType.RESEARCH_TOOL).getLabel());
		assertEquals("Books for scientific culture dissemination", cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).getLabel());
		assertEquals("Chapters in a book for scientific culture dissemination", cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).getLabel());
		assertEquals("Paper in a journal for scientific culture dissemination", cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).getLabel());
		assertEquals("Artistic research productions", cons(PublicationType.ARTISTIC_PRODUCTION).getLabel());
		assertEquals("Technical reports", cons(PublicationType.TECHNICAL_REPORT).getLabel());
		assertEquals("Project reports", cons(PublicationType.PROJECT_REPORT).getLabel());
		assertEquals("Teaching documents", cons(PublicationType.TEACHING_DOCUMENT).getLabel());
		assertEquals("Tutorials or documentations", cons(PublicationType.TUTORIAL_DOCUMENTATION).getLabel());
		assertEquals("Other productions", cons(PublicationType.OTHER).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() {
		assertEquals("Articles in international journals with selection committee", cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).getLabel(Locale.US));
		assertEquals("Articles in national journals with selection committee", cons(PublicationType.NATIONAL_JOURNAL_PAPER).getLabel(Locale.US));
		assertEquals("Papers in the proceedings of an international conference", cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).getLabel(Locale.US));
		assertEquals("Papers in the proceedings of a national conference", cons(PublicationType.NATIONAL_CONFERENCE_PAPER).getLabel(Locale.US));
		assertEquals("Oral Communications without proceeding in international conference", cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).getLabel(Locale.US));
		assertEquals("Oral Communications without proceeding in national conference", cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).getLabel(Locale.US));
		assertEquals("Posters in international conference", cons(PublicationType.INTERNATIONAL_POSTER).getLabel(Locale.US));
		assertEquals("Posters in national conference", cons(PublicationType.NATIONAL_POSTER).getLabel(Locale.US));
		assertEquals("Editor of international books or journals", cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).getLabel(Locale.US));
		assertEquals("Editor of national books or journals", cons(PublicationType.NATIONAL_JOURNAL_EDITION).getLabel(Locale.US));
		assertEquals("International scientific books", cons(PublicationType.INTERNATIONAL_BOOK).getLabel(Locale.US));
		assertEquals("National scientific books", cons(PublicationType.NATIONAL_BOOK).getLabel(Locale.US));
		assertEquals("Chapters in international scientific books", cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).getLabel(Locale.US));
		assertEquals("Chapters in national scientific books", cons(PublicationType.NATIONAL_BOOK_CHAPTER).getLabel(Locale.US));
		assertEquals("Articles in international journals without selection committee", cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).getLabel(Locale.US));
		assertEquals("Articles in national journals without selection committee", cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).getLabel(Locale.US));
		assertEquals("Keynotes in international conference", cons(PublicationType.INTERNATIONAL_KEYNOTE).getLabel(Locale.US));
		assertEquals("Keynotes in national conference", cons(PublicationType.NATIONAL_KEYNOTE).getLabel(Locale.US));
		assertEquals("HDR theses", cons(PublicationType.HDR_THESIS).getLabel(Locale.US));
		assertEquals("PhD theses", cons(PublicationType.PHD_THESIS).getLabel(Locale.US));
		assertEquals("Master theses", cons(PublicationType.MASTER_THESIS).getLabel(Locale.US));
		assertEquals("International patents", cons(PublicationType.INTERNATIONAL_PATENT).getLabel(Locale.US));
		assertEquals("European patents", cons(PublicationType.EUROPEAN_PATENT).getLabel(Locale.US));
		assertEquals("National patents", cons(PublicationType.NATIONAL_PATENT).getLabel(Locale.US));
		assertEquals("Publications for research transfer", cons(PublicationType.RESEARCH_TRANSFERT_REPORT).getLabel(Locale.US));
		assertEquals("Research tools", cons(PublicationType.RESEARCH_TOOL).getLabel(Locale.US));
		assertEquals("Books for scientific culture dissemination", cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).getLabel(Locale.US));
		assertEquals("Chapters in a book for scientific culture dissemination", cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).getLabel(Locale.US));
		assertEquals("Paper in a journal for scientific culture dissemination", cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).getLabel(Locale.US));
		assertEquals("Artistic research productions", cons(PublicationType.ARTISTIC_PRODUCTION).getLabel(Locale.US));
		assertEquals("Technical reports", cons(PublicationType.TECHNICAL_REPORT).getLabel(Locale.US));
		assertEquals("Project reports", cons(PublicationType.PROJECT_REPORT).getLabel(Locale.US));
		assertEquals("Teaching documents", cons(PublicationType.TEACHING_DOCUMENT).getLabel(Locale.US));
		assertEquals("Tutorials or documentations", cons(PublicationType.TUTORIAL_DOCUMENTATION).getLabel(Locale.US));
		assertEquals("Other productions", cons(PublicationType.OTHER).getLabel(Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR() {
		assertEquals("Articles dans des revues internationales avec comité de lecture", cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).getLabel(Locale.FRANCE));
		assertEquals("Articles dans des revues nationales avec comité de lecture", cons(PublicationType.NATIONAL_JOURNAL_PAPER).getLabel(Locale.FRANCE));
		assertEquals("Articles dans des conférences internationales", cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).getLabel(Locale.FRANCE));
		assertEquals("Articles dans des conférences nationales", cons(PublicationType.NATIONAL_CONFERENCE_PAPER).getLabel(Locale.FRANCE));
		assertEquals("Communications orales dans une conférence internationale sans actes", cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).getLabel(Locale.FRANCE));
		assertEquals("Communications orales dans une conférence nationale sans actes", cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).getLabel(Locale.FRANCE));
		assertEquals("Posters dans une conférence internationale", cons(PublicationType.INTERNATIONAL_POSTER).getLabel(Locale.FRANCE));
		assertEquals("Posters dans une conférence nationale", cons(PublicationType.NATIONAL_POSTER).getLabel(Locale.FRANCE));
		assertEquals("\u00C9ditions d'ouvrage ou de revue scientifique international", cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).getLabel(Locale.FRANCE));
		assertEquals("\u00C9ditions d'ouvrage ou de revue scientifique national", cons(PublicationType.NATIONAL_JOURNAL_EDITION).getLabel(Locale.FRANCE));
		assertEquals("Ouvrages scientifiques internationaux", cons(PublicationType.INTERNATIONAL_BOOK).getLabel(Locale.FRANCE));
		assertEquals("Ouvrages scientifiques nationaux", cons(PublicationType.NATIONAL_BOOK).getLabel(Locale.FRANCE));
		assertEquals("Chapitres dans un ouvrage scientifique international", cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).getLabel(Locale.FRANCE));
		assertEquals("Chapitres dans un ouvrage scientifique national", cons(PublicationType.NATIONAL_BOOK_CHAPTER).getLabel(Locale.FRANCE));
		assertEquals("Articles dans des revues internationales sans comité de lecture", cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).getLabel(Locale.FRANCE));
		assertEquals("Articles dans des revues nationales sans comité de lecture", cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).getLabel(Locale.FRANCE));
		assertEquals("Conférenciers invités dans une conférence internationale", cons(PublicationType.INTERNATIONAL_KEYNOTE).getLabel(Locale.FRANCE));
		assertEquals("Conférenciers invités dans une conférence nationale", cons(PublicationType.NATIONAL_KEYNOTE).getLabel(Locale.FRANCE));
		assertEquals("Thèses d'HDR", cons(PublicationType.HDR_THESIS).getLabel(Locale.FRANCE));
		assertEquals("Thèses de doctorat", cons(PublicationType.PHD_THESIS).getLabel(Locale.FRANCE));
		assertEquals("Thèses de master", cons(PublicationType.MASTER_THESIS).getLabel(Locale.FRANCE));
		assertEquals("Brevets internationaux", cons(PublicationType.INTERNATIONAL_PATENT).getLabel(Locale.FRANCE));
		assertEquals("Brevets européens", cons(PublicationType.EUROPEAN_PATENT).getLabel(Locale.FRANCE));
		assertEquals("Brevets nationaux", cons(PublicationType.NATIONAL_PATENT).getLabel(Locale.FRANCE));
		assertEquals("Publications pour le transfert scientifique", cons(PublicationType.RESEARCH_TRANSFERT_REPORT).getLabel(Locale.FRANCE));
		assertEquals("Outils de recherche", cons(PublicationType.RESEARCH_TOOL).getLabel(Locale.FRANCE));
		assertEquals("Ouvrages de vulgarisation", cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).getLabel(Locale.FRANCE));
		assertEquals("Chapitres dans un ouvrage de vulgarisation", cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).getLabel(Locale.FRANCE));
		assertEquals("Article dans une revue de vulgarisation", cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).getLabel(Locale.FRANCE));
		assertEquals("Productions artistiques théorisées", cons(PublicationType.ARTISTIC_PRODUCTION).getLabel(Locale.FRANCE));
		assertEquals("Rapports techniques", cons(PublicationType.TECHNICAL_REPORT).getLabel(Locale.FRANCE));
		assertEquals("Rapports de projet", cons(PublicationType.PROJECT_REPORT).getLabel(Locale.FRANCE));
		assertEquals("Documents d'enseignement", cons(PublicationType.TEACHING_DOCUMENT).getLabel(Locale.FRANCE));
		assertEquals("Tutoriels ou documentations", cons(PublicationType.TUTORIAL_DOCUMENTATION).getLabel(Locale.FRANCE));
		assertEquals("Autres productions", cons(PublicationType.OTHER).getLabel(Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void valueOfCaseInsensitive_null() {
		assertThrows(IllegalArgumentException.class, () -> {
			PublicationType.valueOfCaseInsensitive(null);
		});
	}

	@Test
	public void valueOfCaseInsensitive_empty() {
		assertThrows(IllegalArgumentException.class, () -> {
			PublicationType.valueOfCaseInsensitive("");
		});
	}

	@Test
	public void valueOfCaseInsensitive_invalid() {
		assertThrows(IllegalArgumentException.class, () -> {
			PublicationType.valueOfCaseInsensitive("xvz");
		});
	}

	@Test
	public void valueOfCaseInsensitive_notNull() {
		for (PublicationType type : PublicationType.values()) {
			assertSame(
					cons(type),
					PublicationType.valueOfCaseInsensitive(randomString(type.name())),
					"valueOfCaseInsensitive: "+ type.name());
		}
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_null() {
		PublicationType input = null;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_0() {
		PublicationType input = PublicationType.INTERNATIONAL_JOURNAL_PAPER;
		assertTrue(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_1() {
		PublicationType input = PublicationType.NATIONAL_JOURNAL_PAPER;
		assertTrue(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_2() {
		PublicationType input = PublicationType.INTERNATIONAL_CONFERENCE_PAPER;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_3() {
		PublicationType input = PublicationType.NATIONAL_CONFERENCE_PAPER;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_4() {
		PublicationType input = PublicationType.INTERNATIONAL_ORAL_COMMUNICATION;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_5() {
		PublicationType input = PublicationType.NATIONAL_ORAL_COMMUNICATION;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_6() {
		PublicationType input = PublicationType.INTERNATIONAL_POSTER;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_7() {
		PublicationType input = PublicationType.NATIONAL_POSTER;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_8() {
		PublicationType input = PublicationType.INTERNATIONAL_JOURNAL_EDITION;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_9() {
		PublicationType input = PublicationType.NATIONAL_JOURNAL_EDITION;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_10() {
		PublicationType input = PublicationType.INTERNATIONAL_BOOK;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertTrue(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_11() {
		PublicationType input = PublicationType.NATIONAL_BOOK;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertTrue(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_12() {
		PublicationType input = PublicationType.INTERNATIONAL_BOOK_CHAPTER;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertTrue(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_13() {
		PublicationType input = PublicationType.NATIONAL_BOOK_CHAPTER;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertTrue(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_14() {
		PublicationType input = PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE;
		assertTrue(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_15() {
		PublicationType input = PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE;
		assertTrue(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_16() {
		PublicationType input = PublicationType.INTERNATIONAL_KEYNOTE;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_17() {
		PublicationType input = PublicationType.NATIONAL_KEYNOTE;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_18() {
		PublicationType input = PublicationType.HDR_THESIS;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertTrue(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertTrue(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertTrue(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_19() {
		PublicationType input = PublicationType.PHD_THESIS;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertTrue(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertTrue(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertTrue(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_20() {
		PublicationType input = PublicationType.MASTER_THESIS;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertTrue(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertTrue(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertTrue(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_21() {
		PublicationType input = PublicationType.INTERNATIONAL_PATENT;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_22() {
		PublicationType input = PublicationType.EUROPEAN_PATENT;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_23() {
		PublicationType input = PublicationType.NATIONAL_PATENT;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_24() {
		PublicationType input = PublicationType.RESEARCH_TRANSFERT_REPORT;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_25() {
		PublicationType input = PublicationType.RESEARCH_TOOL;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_26() {
		PublicationType input = PublicationType.SCIENTIFIC_CULTURE_BOOK;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertTrue(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_27() {
		PublicationType input = PublicationType.SCIENTIFIC_CULTURE_PAPER;
		assertTrue(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_28() {
		PublicationType input = PublicationType.ARTISTIC_PRODUCTION;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_29() {
		PublicationType input = PublicationType.TECHNICAL_REPORT;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_30() {
		PublicationType input = PublicationType.PROJECT_REPORT;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_31() {
		PublicationType input = PublicationType.TEACHING_DOCUMENT;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_32() {
		PublicationType input = PublicationType.TUTORIAL_DOCUMENTATION;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_33() {
		PublicationType input = PublicationType.OTHER;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertTrue(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertTrue(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

	@Test
	public void isCompatibleWith_34() {
		PublicationType input = PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER;
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_CONFERENCE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_ORAL_COMMUNICATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_POSTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_EDITION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_BOOK).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_BOOK).isCompatibleWith(input));
		assertTrue(cons(PublicationType.INTERNATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertTrue(cons(PublicationType.NATIONAL_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_JOURNAL_PAPER_WITHOUT_COMMITTEE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_KEYNOTE).isCompatibleWith(input));
		assertFalse(cons(PublicationType.HDR_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PHD_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.MASTER_THESIS).isCompatibleWith(input));
		assertFalse(cons(PublicationType.INTERNATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.EUROPEAN_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.NATIONAL_PATENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TRANSFERT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.RESEARCH_TOOL).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK).isCompatibleWith(input));
		assertTrue(cons(PublicationType.SCIENTIFIC_CULTURE_BOOK_CHAPTER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.SCIENTIFIC_CULTURE_PAPER).isCompatibleWith(input));
		assertFalse(cons(PublicationType.ARTISTIC_PRODUCTION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TECHNICAL_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.PROJECT_REPORT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TEACHING_DOCUMENT).isCompatibleWith(input));
		assertFalse(cons(PublicationType.TUTORIAL_DOCUMENTATION).isCompatibleWith(input));
		assertFalse(cons(PublicationType.OTHER).isCompatibleWith(input));
		assertAllTreated();
	}

}
