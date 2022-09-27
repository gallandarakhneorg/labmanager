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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

	@BeforeEach
	public void setUp() {
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
	public void getLabel_US() {
		// Force the default locale to be US.
		Locale.setDefault(Locale.US);
		assertEquals("Patents", cons(PublicationCategory.BRE).getLabel());
		assertEquals("Articles in international or national journals with selection committee and ranked in international databases", cons(PublicationCategory.ACL).getLabel());
		assertEquals("Articles in international or national journals with selection committee and not ranked in international databases", cons(PublicationCategory.ACLN).getLabel());
		assertEquals("Articles in international or national journals without selection committee", cons(PublicationCategory.ASCL).getLabel());
		assertEquals("Papers in the proceedings of an international conference", cons(PublicationCategory.C_ACTI).getLabel());
		assertEquals("Papers in the proceedings of a national conference", cons(PublicationCategory.C_ACTN).getLabel());
		assertEquals("Oral Communications without proceeding in international or national conference", cons(PublicationCategory.C_COM).getLabel());
		assertEquals("Posters in international or national conference", cons(PublicationCategory.C_AFF).getLabel());
		assertEquals("Editor of books or journals", cons(PublicationCategory.DO).getLabel());
		assertEquals("Scientific books", cons(PublicationCategory.OS).getLabel());
		assertEquals("Chapters in scientific books", cons(PublicationCategory.COS).getLabel());
		assertEquals("Keynotes in international or national conference", cons(PublicationCategory.C_INV).getLabel());
		assertEquals("Theses (HDR, PHD, Master)", cons(PublicationCategory.TH).getLabel());
		assertEquals("Publications for research transfer", cons(PublicationCategory.PT).getLabel());
		assertEquals("Research tools (databases...)", cons(PublicationCategory.OR).getLabel());
		assertEquals("Books for scientific culture dissemination", cons(PublicationCategory.OV).getLabel());
		assertEquals("Chapters in a book for scientific culture dissemination", cons(PublicationCategory.COV).getLabel());
		assertEquals("Papers for scientific culture dissemination", cons(PublicationCategory.PV).getLabel());
		assertEquals("Artistic research productions", cons(PublicationCategory.PAT).getLabel());
		assertEquals("Other productions", cons(PublicationCategory.AP).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_FR() {
		// Force the default locale to be FR.
		Locale.setDefault(Locale.FRANCE);
		assertEquals("Patents", cons(PublicationCategory.BRE).getLabel());
		assertEquals("Articles in international or national journals with selection committee and ranked in international databases", cons(PublicationCategory.ACL).getLabel());
		assertEquals("Articles in international or national journals with selection committee and not ranked in international databases", cons(PublicationCategory.ACLN).getLabel());
		assertEquals("Articles in international or national journals without selection committee", cons(PublicationCategory.ASCL).getLabel());
		assertEquals("Papers in the proceedings of an international conference", cons(PublicationCategory.C_ACTI).getLabel());
		assertEquals("Papers in the proceedings of a national conference", cons(PublicationCategory.C_ACTN).getLabel());
		assertEquals("Oral Communications without proceeding in international or national conference", cons(PublicationCategory.C_COM).getLabel());
		assertEquals("Posters in international or national conference", cons(PublicationCategory.C_AFF).getLabel());
		assertEquals("Editor of books or journals", cons(PublicationCategory.DO).getLabel());
		assertEquals("Scientific books", cons(PublicationCategory.OS).getLabel());
		assertEquals("Chapters in scientific books", cons(PublicationCategory.COS).getLabel());
		assertEquals("Keynotes in international or national conference", cons(PublicationCategory.C_INV).getLabel());
		assertEquals("Theses (HDR, PHD, Master)", cons(PublicationCategory.TH).getLabel());
		assertEquals("Publications for research transfer", cons(PublicationCategory.PT).getLabel());
		assertEquals("Research tools (databases...)", cons(PublicationCategory.OR).getLabel());
		assertEquals("Books for scientific culture dissemination", cons(PublicationCategory.OV).getLabel());
		assertEquals("Chapters in a book for scientific culture dissemination", cons(PublicationCategory.COV).getLabel());
		assertEquals("Papers for scientific culture dissemination", cons(PublicationCategory.PV).getLabel());
		assertEquals("Artistic research productions", cons(PublicationCategory.PAT).getLabel());
		assertEquals("Other productions", cons(PublicationCategory.AP).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() {
		assertEquals("Patents", cons(PublicationCategory.BRE).getLabel(Locale.US));
		assertEquals("Articles in international or national journals with selection committee and ranked in international databases", cons(PublicationCategory.ACL).getLabel(Locale.US));
		assertEquals("Articles in international or national journals with selection committee and not ranked in international databases", cons(PublicationCategory.ACLN).getLabel(Locale.US));
		assertEquals("Articles in international or national journals without selection committee", cons(PublicationCategory.ASCL).getLabel(Locale.US));
		assertEquals("Papers in the proceedings of an international conference", cons(PublicationCategory.C_ACTI).getLabel(Locale.US));
		assertEquals("Papers in the proceedings of a national conference", cons(PublicationCategory.C_ACTN).getLabel(Locale.US));
		assertEquals("Oral Communications without proceeding in international or national conference", cons(PublicationCategory.C_COM).getLabel(Locale.US));
		assertEquals("Posters in international or national conference", cons(PublicationCategory.C_AFF).getLabel(Locale.US));
		assertEquals("Editor of books or journals", cons(PublicationCategory.DO).getLabel(Locale.US));
		assertEquals("Scientific books", cons(PublicationCategory.OS).getLabel(Locale.US));
		assertEquals("Chapters in scientific books", cons(PublicationCategory.COS).getLabel(Locale.US));
		assertEquals("Keynotes in international or national conference", cons(PublicationCategory.C_INV).getLabel(Locale.US));
		assertEquals("Theses (HDR, PHD, Master)", cons(PublicationCategory.TH).getLabel(Locale.US));
		assertEquals("Publications for research transfer", cons(PublicationCategory.PT).getLabel(Locale.US));
		assertEquals("Research tools (databases...)", cons(PublicationCategory.OR).getLabel(Locale.US));
		assertEquals("Books for scientific culture dissemination", cons(PublicationCategory.OV).getLabel(Locale.US));
		assertEquals("Chapters in a book for scientific culture dissemination", cons(PublicationCategory.COV).getLabel(Locale.US));
		assertEquals("Papers for scientific culture dissemination", cons(PublicationCategory.PV).getLabel(Locale.US));
		assertEquals("Artistic research productions", cons(PublicationCategory.PAT).getLabel(Locale.US));
		assertEquals("Other productions", cons(PublicationCategory.AP).getLabel(Locale.US));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR() {
		assertEquals("Brevets", cons(PublicationCategory.BRE).getLabel(Locale.FRANCE));
		assertEquals("Articles dans des revues internationales ou nationales avec comité de lecture répertoriées dans les bases de données internationales", cons(PublicationCategory.ACL).getLabel(Locale.FRANCE));
		assertEquals("Articles dans des revues avec comité de lecture non répertoriées dans des bases de données internationales", cons(PublicationCategory.ACLN).getLabel(Locale.FRANCE));
		assertEquals("Articles dans des revues sans comité de lecture", cons(PublicationCategory.ASCL).getLabel(Locale.FRANCE));
		assertEquals("Communications avec actes dans un congrès international", cons(PublicationCategory.C_ACTI).getLabel(Locale.FRANCE));
		assertEquals("Communications avec actes dans un congrès national", cons(PublicationCategory.C_ACTN).getLabel(Locale.FRANCE));
		assertEquals("Communications orales sans actes dans un congrès international ou national", cons(PublicationCategory.C_COM).getLabel(Locale.FRANCE));
		assertEquals("Communications par affiche dans un congrès international ou national", cons(PublicationCategory.C_AFF).getLabel(Locale.FRANCE));
		assertEquals("Directions d'ouvrages ou de revues", cons(PublicationCategory.DO).getLabel(Locale.FRANCE));
		assertEquals("Ouvrages scientifiques", cons(PublicationCategory.OS).getLabel(Locale.FRANCE));
		assertEquals("Chapitres dans un ouvrage scientifique", cons(PublicationCategory.COS).getLabel(Locale.FRANCE));
		assertEquals("Conférences données à l'invitation du comité d'organisation dans un congrès national ou international", cons(PublicationCategory.C_INV).getLabel(Locale.FRANCE));
		assertEquals("Thèses (HDR, doctorat, master)", cons(PublicationCategory.TH).getLabel(Locale.FRANCE));
		assertEquals("Publications de transfert", cons(PublicationCategory.PT).getLabel(Locale.FRANCE));
		assertEquals("Outils de recherche (bases de données, corpus de recherche...)", cons(PublicationCategory.OR).getLabel(Locale.FRANCE));
		assertEquals("Ouvrages de vulgarisation", cons(PublicationCategory.OV).getLabel(Locale.FRANCE));
		assertEquals("Chapitres dans un ouvrage de vulgarisation", cons(PublicationCategory.COV).getLabel(Locale.FRANCE));
		assertEquals("Publications de vulgarisation", cons(PublicationCategory.PV).getLabel(Locale.FRANCE));
		assertEquals("Productions artistiques théorisées", cons(PublicationCategory.PAT).getLabel(Locale.FRANCE));
		assertEquals("Autres productions", cons(PublicationCategory.AP).getLabel(Locale.FRANCE));
		assertAllTreated();
	}

}
