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

package fr.utbm.ciad.labmanager.tests.data.publication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.utbm.ciad.labmanager.configuration.messages.BaseMessageSource;
import fr.utbm.ciad.labmanager.data.publication.PublicationCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

/** Tests for {@link PublicationCategory}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class PublicationCategoryTest {

	private List<PublicationCategory> items;

	private MessageSourceAccessor messages;

	@BeforeEach
	public void setUp() {
		this.messages = BaseMessageSource.getGlobalMessageAccessor();
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(PublicationCategory.values()));
	}

	private PublicationCategory cons(PublicationCategory category) {
		assertTrue(this.items.remove(category), "Expecting enumeration item: " + category.toString());
		return category;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void isScientificJournalPaper() {
		assertFalse(cons(PublicationCategory.BRE).isScientificJournalPaper());
		assertTrue(cons(PublicationCategory.ACL).isScientificJournalPaper());
		assertTrue(cons(PublicationCategory.ACLN).isScientificJournalPaper());
		assertTrue(cons(PublicationCategory.ASCL).isScientificJournalPaper());
		assertFalse(cons(PublicationCategory.C_ACTI).isScientificJournalPaper());
		assertFalse(cons(PublicationCategory.C_ACTN).isScientificJournalPaper());
		assertFalse(cons(PublicationCategory.C_COM).isScientificJournalPaper());
		assertFalse(cons(PublicationCategory.C_AFF).isScientificJournalPaper());
		assertFalse(cons(PublicationCategory.DO).isScientificJournalPaper());
		assertFalse(cons(PublicationCategory.OS).isScientificJournalPaper());
		assertFalse(cons(PublicationCategory.COS).isScientificJournalPaper());
		assertFalse(cons(PublicationCategory.C_INV).isScientificJournalPaper());
		assertFalse(cons(PublicationCategory.TH).isScientificJournalPaper());
		assertFalse(cons(PublicationCategory.PT).isScientificJournalPaper());
		assertFalse(cons(PublicationCategory.OR).isScientificJournalPaper());
		assertFalse(cons(PublicationCategory.OV).isScientificJournalPaper());
		assertFalse(cons(PublicationCategory.COV).isScientificJournalPaper());
		assertFalse(cons(PublicationCategory.PV).isScientificJournalPaper());
		assertFalse(cons(PublicationCategory.PAT).isScientificJournalPaper());
		assertFalse(cons(PublicationCategory.AP).isScientificJournalPaper());
		assertAllTreated();
	}

	@Test
	public void isPatent() {
		assertTrue(cons(PublicationCategory.BRE).isPatent());
		assertFalse(cons(PublicationCategory.ACL).isPatent());
		assertFalse(cons(PublicationCategory.ACLN).isPatent());
		assertFalse(cons(PublicationCategory.ASCL).isPatent());
		assertFalse(cons(PublicationCategory.C_ACTI).isPatent());
		assertFalse(cons(PublicationCategory.C_ACTN).isPatent());
		assertFalse(cons(PublicationCategory.C_COM).isPatent());
		assertFalse(cons(PublicationCategory.C_AFF).isPatent());
		assertFalse(cons(PublicationCategory.DO).isPatent());
		assertFalse(cons(PublicationCategory.OS).isPatent());
		assertFalse(cons(PublicationCategory.COS).isPatent());
		assertFalse(cons(PublicationCategory.C_INV).isPatent());
		assertFalse(cons(PublicationCategory.TH).isPatent());
		assertFalse(cons(PublicationCategory.PT).isPatent());
		assertFalse(cons(PublicationCategory.OR).isPatent());
		assertFalse(cons(PublicationCategory.OV).isPatent());
		assertFalse(cons(PublicationCategory.COV).isPatent());
		assertFalse(cons(PublicationCategory.PV).isPatent());
		assertFalse(cons(PublicationCategory.PAT).isPatent());
		assertFalse(cons(PublicationCategory.AP).isPatent());
		assertAllTreated();
	}

	@Test
	public void isScientificEventPaper() {
		assertFalse(cons(PublicationCategory.BRE).isScientificEventPaper());
		assertFalse(cons(PublicationCategory.ACL).isScientificEventPaper());
		assertFalse(cons(PublicationCategory.ACLN).isScientificEventPaper());
		assertFalse(cons(PublicationCategory.ASCL).isScientificEventPaper());
		assertTrue(cons(PublicationCategory.C_ACTI).isScientificEventPaper());
		assertTrue(cons(PublicationCategory.C_ACTN).isScientificEventPaper());
		assertTrue(cons(PublicationCategory.C_COM).isScientificEventPaper());
		assertTrue(cons(PublicationCategory.C_AFF).isScientificEventPaper());
		assertFalse(cons(PublicationCategory.DO).isScientificEventPaper());
		assertFalse(cons(PublicationCategory.OS).isScientificEventPaper());
		assertFalse(cons(PublicationCategory.COS).isScientificEventPaper());
		assertTrue(cons(PublicationCategory.C_INV).isScientificEventPaper());
		assertFalse(cons(PublicationCategory.TH).isScientificEventPaper());
		assertFalse(cons(PublicationCategory.PT).isScientificEventPaper());
		assertFalse(cons(PublicationCategory.OR).isScientificEventPaper());
		assertFalse(cons(PublicationCategory.OV).isScientificEventPaper());
		assertFalse(cons(PublicationCategory.COV).isScientificEventPaper());
		assertFalse(cons(PublicationCategory.PV).isScientificEventPaper());
		assertFalse(cons(PublicationCategory.PAT).isScientificEventPaper());
		assertFalse(cons(PublicationCategory.AP).isScientificEventPaper());
		assertAllTreated();
	}

	@Test
	public void isScientificCultureDissemination() {
		assertFalse(cons(PublicationCategory.BRE).isScientificCultureDissemination());
		assertFalse(cons(PublicationCategory.ACL).isScientificCultureDissemination());
		assertFalse(cons(PublicationCategory.ACLN).isScientificCultureDissemination());
		assertFalse(cons(PublicationCategory.ASCL).isScientificCultureDissemination());
		assertFalse(cons(PublicationCategory.C_ACTI).isScientificCultureDissemination());
		assertFalse(cons(PublicationCategory.C_ACTN).isScientificCultureDissemination());
		assertFalse(cons(PublicationCategory.C_COM).isScientificCultureDissemination());
		assertFalse(cons(PublicationCategory.C_AFF).isScientificCultureDissemination());
		assertFalse(cons(PublicationCategory.DO).isScientificCultureDissemination());
		assertFalse(cons(PublicationCategory.OS).isScientificCultureDissemination());
		assertFalse(cons(PublicationCategory.COS).isScientificCultureDissemination());
		assertFalse(cons(PublicationCategory.C_INV).isScientificCultureDissemination());
		assertFalse(cons(PublicationCategory.TH).isScientificCultureDissemination());
		assertFalse(cons(PublicationCategory.PT).isScientificCultureDissemination());
		assertFalse(cons(PublicationCategory.OR).isScientificCultureDissemination());
		assertTrue(cons(PublicationCategory.OV).isScientificCultureDissemination());
		assertTrue(cons(PublicationCategory.COV).isScientificCultureDissemination());
		assertTrue(cons(PublicationCategory.PV).isScientificCultureDissemination());
		assertTrue(cons(PublicationCategory.PAT).isScientificCultureDissemination());
		assertFalse(cons(PublicationCategory.AP).isScientificCultureDissemination());
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() {
		assertEquals("Patents", cons(PublicationCategory.BRE).getLabel(this.messages, Locale.US));
		assertEquals("Articles in international or national journals with selection committee and ranked in international databases", cons(PublicationCategory.ACL).getLabel(this.messages, Locale.US));
		assertEquals("Articles in international or national journals with selection committee and not ranked in international databases", cons(PublicationCategory.ACLN).getLabel(this.messages, Locale.US));
		assertEquals("Articles in international or national journals without selection committee", cons(PublicationCategory.ASCL).getLabel(this.messages, Locale.US));
		assertEquals("Papers in the proceedings of an international conference", cons(PublicationCategory.C_ACTI).getLabel(this.messages, Locale.US));
		assertEquals("Papers in the proceedings of a national conference", cons(PublicationCategory.C_ACTN).getLabel(this.messages, Locale.US));
		assertEquals("Oral Communications without proceeding in international or national conference", cons(PublicationCategory.C_COM).getLabel(this.messages, Locale.US));
		assertEquals("Posters in international or national conference", cons(PublicationCategory.C_AFF).getLabel(this.messages, Locale.US));
		assertEquals("Editor of books or journals", cons(PublicationCategory.DO).getLabel(this.messages, Locale.US));
		assertEquals("Scientific books", cons(PublicationCategory.OS).getLabel(this.messages, Locale.US));
		assertEquals("Chapters in scientific books", cons(PublicationCategory.COS).getLabel(this.messages, Locale.US));
		assertEquals("Keynotes in international or national conference", cons(PublicationCategory.C_INV).getLabel(this.messages, Locale.US));
		assertEquals("Theses (HDR, PHD, Master)", cons(PublicationCategory.TH).getLabel(this.messages, Locale.US));
		assertEquals("Publications for research transfer", cons(PublicationCategory.PT).getLabel(this.messages, Locale.US));
		assertEquals("Research tools (databases...)", cons(PublicationCategory.OR).getLabel(this.messages, Locale.US));
		assertEquals("Books for scientific culture dissemination", cons(PublicationCategory.OV).getLabel(this.messages, Locale.US));
		assertEquals("Chapters in a book for scientific culture dissemination", cons(PublicationCategory.COV).getLabel(this.messages, Locale.US));
		assertEquals("Papers for scientific culture dissemination", cons(PublicationCategory.PV).getLabel(this.messages, Locale.US));
		assertEquals("Artistic research productions", cons(PublicationCategory.PAT).getLabel(this.messages, Locale.US));
		assertEquals("Other productions", cons(PublicationCategory.AP).getLabel(this.messages, Locale.US));
		assertAllTreated();
	}

	@Test
	public void getAcronym_US() {
		assertEquals("BRE", cons(PublicationCategory.BRE).getAcronym());
		assertEquals("ACL", cons(PublicationCategory.ACL).getAcronym());
		assertEquals("ACLN", cons(PublicationCategory.ACLN).getAcronym());
		assertEquals("ASCL", cons(PublicationCategory.ASCL).getAcronym());
		assertEquals("C-ACTI", cons(PublicationCategory.C_ACTI).getAcronym());
		assertEquals("C-ACTN", cons(PublicationCategory.C_ACTN).getAcronym());
		assertEquals("C-COM", cons(PublicationCategory.C_COM).getAcronym());
		assertEquals("C-AFF", cons(PublicationCategory.C_AFF).getAcronym());
		assertEquals("DO", cons(PublicationCategory.DO).getAcronym());
		assertEquals("OS", cons(PublicationCategory.OS).getAcronym());
		assertEquals("COS", cons(PublicationCategory.COS).getAcronym());
		assertEquals("C-INV", cons(PublicationCategory.C_INV).getAcronym());
		assertEquals("TH", cons(PublicationCategory.TH).getAcronym());
		assertEquals("PT", cons(PublicationCategory.PT).getAcronym());
		assertEquals("OR", cons(PublicationCategory.OR).getAcronym());
		assertEquals("OV", cons(PublicationCategory.OV).getAcronym());
		assertEquals("COV", cons(PublicationCategory.COV).getAcronym());
		assertEquals("PV", cons(PublicationCategory.PV).getAcronym());
		assertEquals("PAT", cons(PublicationCategory.PAT).getAcronym());
		assertEquals("AP", cons(PublicationCategory.AP).getAcronym());
		assertAllTreated();
	}

}
