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

package fr.ciadlab.labmanager.entities.assostructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.ciadlab.labmanager.entities.assostructure.AssociatedStructureType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link AssociatedStructureType}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class AssociatedStructureTypeTest {

	private List<AssociatedStructureType> items;

	@BeforeEach
	public void setUp() {
		this.items = new ArrayList<>();
		this.items.addAll(Arrays.asList(AssociatedStructureType.values()));
	}

	private AssociatedStructureType cons(AssociatedStructureType status) {
		assertTrue(this.items.remove(status), "Expecting enumeration item: " + status.toString());
		return status;
	}

	private void assertAllTreated() {
		assertTrue(this.items.isEmpty(), "Missing enumeration items: " + this.items.toString());
	}

	@Test
	public void valueOfCaseInsensitive_String_special() {
		assertThrows(IllegalArgumentException.class, () -> AssociatedStructureType.valueOfCaseInsensitive(null));
		assertThrows(IllegalArgumentException.class, () -> AssociatedStructureType.valueOfCaseInsensitive(""));
	}

	@Test
	public void valueOfCaseInsensitive_String_upperCase() {
		assertEquals(cons(AssociatedStructureType.PRIVATE_COMPANY), AssociatedStructureType.valueOfCaseInsensitive("PRIVATE_COMPANY"));
		assertEquals(cons(AssociatedStructureType.INDUSTRIAL_CHAIR), AssociatedStructureType.valueOfCaseInsensitive("INDUSTRIAL_CHAIR"));
		assertEquals(cons(AssociatedStructureType.RESEARCH_CHAIR), AssociatedStructureType.valueOfCaseInsensitive("RESEARCH_CHAIR"));
		assertEquals(cons(AssociatedStructureType.INTERNATIONAL_RESEARCH_LAB), AssociatedStructureType.valueOfCaseInsensitive("INTERNATIONAL_RESEARCH_LAB"));
		assertEquals(cons(AssociatedStructureType.INTERNATIONAL_SCIENTIFIC_INTEREST_GROUP), AssociatedStructureType.valueOfCaseInsensitive("INTERNATIONAL_SCIENTIFIC_INTEREST_GROUP"));
		assertEquals(cons(AssociatedStructureType.NATIONAL_RESEARCH_LAB), AssociatedStructureType.valueOfCaseInsensitive("NATIONAL_RESEARCH_LAB"));
		assertEquals(cons(AssociatedStructureType.NATIONAL_SCIENTIFIC_INTEREST_GROUP), AssociatedStructureType.valueOfCaseInsensitive("NATIONAL_SCIENTIFIC_INTEREST_GROUP"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> AssociatedStructureType.valueOfCaseInsensitive("XYZ"));
	}

	@Test
	public void valueOfCaseInsensitive_String_lowerCase() {
		assertEquals(cons(AssociatedStructureType.PRIVATE_COMPANY), AssociatedStructureType.valueOfCaseInsensitive("private_COMPANY"));
		assertEquals(cons(AssociatedStructureType.INDUSTRIAL_CHAIR), AssociatedStructureType.valueOfCaseInsensitive("industrial_chair"));
		assertEquals(cons(AssociatedStructureType.RESEARCH_CHAIR), AssociatedStructureType.valueOfCaseInsensitive("research_chair"));
		assertEquals(cons(AssociatedStructureType.INTERNATIONAL_RESEARCH_LAB), AssociatedStructureType.valueOfCaseInsensitive("INTERNATIONAL_research_LAB"));
		assertEquals(cons(AssociatedStructureType.INTERNATIONAL_SCIENTIFIC_INTEREST_GROUP), AssociatedStructureType.valueOfCaseInsensitive("INTERNATIONAL_scientific_INTEREST_GROUP"));
		assertEquals(cons(AssociatedStructureType.NATIONAL_RESEARCH_LAB), AssociatedStructureType.valueOfCaseInsensitive("NATIONAL_RESEARCH_lab"));
		assertEquals(cons(AssociatedStructureType.NATIONAL_SCIENTIFIC_INTEREST_GROUP), AssociatedStructureType.valueOfCaseInsensitive("NATIONAL_SCIENTIFIC_interest_GROUP"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> AssociatedStructureType.valueOfCaseInsensitive("xyz"));
	}

	@Test
	public void getLabel_FR() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.FRANCE);
		assertEquals("Private company", cons(AssociatedStructureType.PRIVATE_COMPANY).getLabel());
		assertEquals("Industrial chair", cons(AssociatedStructureType.INDUSTRIAL_CHAIR).getLabel());
		assertEquals("Research chair", cons(AssociatedStructureType.RESEARCH_CHAIR).getLabel());
		assertEquals("International Research Lab", cons(AssociatedStructureType.INTERNATIONAL_RESEARCH_LAB).getLabel());
		assertEquals("International Group of Scientific Interest", cons(AssociatedStructureType.INTERNATIONAL_SCIENTIFIC_INTEREST_GROUP).getLabel());
		assertEquals("National Research Lab", cons(AssociatedStructureType.NATIONAL_RESEARCH_LAB).getLabel());
		assertEquals("National Group of Scientific Interest", cons(AssociatedStructureType.NATIONAL_SCIENTIFIC_INTEREST_GROUP).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_US() throws Exception {
		java.util.Locale.setDefault(java.util.Locale.US);
		assertEquals("Private company", cons(AssociatedStructureType.PRIVATE_COMPANY).getLabel());
		assertEquals("Industrial chair", cons(AssociatedStructureType.INDUSTRIAL_CHAIR).getLabel());
		assertEquals("Research chair", cons(AssociatedStructureType.RESEARCH_CHAIR).getLabel());
		assertEquals("International Research Lab", cons(AssociatedStructureType.INTERNATIONAL_RESEARCH_LAB).getLabel());
		assertEquals("International Group of Scientific Interest", cons(AssociatedStructureType.INTERNATIONAL_SCIENTIFIC_INTEREST_GROUP).getLabel());
		assertEquals("National Research Lab", cons(AssociatedStructureType.NATIONAL_RESEARCH_LAB).getLabel());
		assertEquals("National Group of Scientific Interest", cons(AssociatedStructureType.NATIONAL_SCIENTIFIC_INTEREST_GROUP).getLabel());
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_FR() throws Exception {
		assertEquals("Entreprise de droit privé", cons(AssociatedStructureType.PRIVATE_COMPANY).getLabel(Locale.FRANCE));
		assertEquals("Chaire industrielle", cons(AssociatedStructureType.INDUSTRIAL_CHAIR).getLabel(Locale.FRANCE));
		assertEquals("Chaire de recherche", cons(AssociatedStructureType.RESEARCH_CHAIR).getLabel(Locale.FRANCE));
		assertEquals("Laboratoire commun international", cons(AssociatedStructureType.INTERNATIONAL_RESEARCH_LAB).getLabel(Locale.FRANCE));
		assertEquals("Groupe international d'intérêt scientifique", cons(AssociatedStructureType.INTERNATIONAL_SCIENTIFIC_INTEREST_GROUP).getLabel(Locale.FRANCE));
		assertEquals("Laboratoire commun national", cons(AssociatedStructureType.NATIONAL_RESEARCH_LAB).getLabel(Locale.FRANCE));
		assertEquals("Groupe national d'intérêt scientifique", cons(AssociatedStructureType.NATIONAL_SCIENTIFIC_INTEREST_GROUP).getLabel(Locale.FRANCE));
		assertAllTreated();
	}

	@Test
	public void getLabel_Locale_US() throws Exception {
		assertEquals("Private company", cons(AssociatedStructureType.PRIVATE_COMPANY).getLabel(Locale.US));
		assertEquals("Industrial chair", cons(AssociatedStructureType.INDUSTRIAL_CHAIR).getLabel(Locale.US));
		assertEquals("Research chair", cons(AssociatedStructureType.RESEARCH_CHAIR).getLabel(Locale.US));
		assertEquals("International Research Lab", cons(AssociatedStructureType.INTERNATIONAL_RESEARCH_LAB).getLabel(Locale.US));
		assertEquals("International Group of Scientific Interest", cons(AssociatedStructureType.INTERNATIONAL_SCIENTIFIC_INTEREST_GROUP).getLabel(Locale.US));
		assertEquals("National Research Lab", cons(AssociatedStructureType.NATIONAL_RESEARCH_LAB).getLabel(Locale.US));
		assertEquals("National Group of Scientific Interest", cons(AssociatedStructureType.NATIONAL_SCIENTIFIC_INTEREST_GROUP).getLabel(Locale.US));
		assertAllTreated();
	}

}
