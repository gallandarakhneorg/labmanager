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

package fr.utbm.ciad.labmanager.tests.data.assostructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.utbm.ciad.labmanager.configuration.messages.BaseMessageSource;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructureType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.MessageSourceAccessor;

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

	private MessageSourceAccessor messages;

	@BeforeEach
	public void setUp() {
		this.messages = BaseMessageSource.getGlobalMessageAccessor();
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
		assertEquals(cons(AssociatedStructureType.EUROPEAN_RESEARCH_LAB), AssociatedStructureType.valueOfCaseInsensitive("EUROPEAN_RESEARCH_LAB"));
		assertEquals(cons(AssociatedStructureType.NATIONAL_RESEARCH_LAB), AssociatedStructureType.valueOfCaseInsensitive("NATIONAL_RESEARCH_LAB"));
		assertEquals(cons(AssociatedStructureType.INTERNATIONAL_SCIENTIFIC_INTEREST_GROUP), AssociatedStructureType.valueOfCaseInsensitive("INTERNATIONAL_SCIENTIFIC_INTEREST_GROUP"));
		assertEquals(cons(AssociatedStructureType.EUROPEAN_SCIENTIFIC_INTEREST_GROUP), AssociatedStructureType.valueOfCaseInsensitive("EUROPEAN_SCIENTIFIC_INTEREST_GROUP"));
		assertEquals(cons(AssociatedStructureType.NATIONAL_SCIENTIFIC_INTEREST_GROUP), AssociatedStructureType.valueOfCaseInsensitive("NATIONAL_SCIENTIFIC_INTEREST_GROUP"));
		assertEquals(cons(AssociatedStructureType.INTERNATIONAL_RESEARCH_GROUP), AssociatedStructureType.valueOfCaseInsensitive("INTERNATIONAL_RESEARCH_GROUP"));
		assertEquals(cons(AssociatedStructureType.EUROPEAN_RESEARCH_GROUP), AssociatedStructureType.valueOfCaseInsensitive("EUROPEAN_RESEARCH_GROUP"));
		assertEquals(cons(AssociatedStructureType.NATIONAL_RESEARCH_GROUP), AssociatedStructureType.valueOfCaseInsensitive("NATIONAL_RESEARCH_GROUP"));
		assertEquals(cons(AssociatedStructureType.HOSTED_INTERNATIONAL_COMPANY), AssociatedStructureType.valueOfCaseInsensitive("HOSTED_INTERNATIONAL_COMPANY"));
		assertEquals(cons(AssociatedStructureType.HOSTED_EUROPEAN_COMPANY), AssociatedStructureType.valueOfCaseInsensitive("HOSTED_EUROPEAN_COMPANY"));
		assertEquals(cons(AssociatedStructureType.HOSTED_NATIONAL_COMPANY), AssociatedStructureType.valueOfCaseInsensitive("HOSTED_NATIONAL_COMPANY"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> AssociatedStructureType.valueOfCaseInsensitive("XYZ"));
	}

	@Test
	public void valueOfCaseInsensitive_String_lowerCase() {
		assertEquals(cons(AssociatedStructureType.PRIVATE_COMPANY), AssociatedStructureType.valueOfCaseInsensitive("private_COMPANY"));
		assertEquals(cons(AssociatedStructureType.INDUSTRIAL_CHAIR), AssociatedStructureType.valueOfCaseInsensitive("INDUSTRIAL_chair"));
		assertEquals(cons(AssociatedStructureType.RESEARCH_CHAIR), AssociatedStructureType.valueOfCaseInsensitive("RESEARCH_chair"));
		assertEquals(cons(AssociatedStructureType.INTERNATIONAL_RESEARCH_LAB), AssociatedStructureType.valueOfCaseInsensitive("INTERNATIONAL_RESEARCH_lab"));
		assertEquals(cons(AssociatedStructureType.EUROPEAN_RESEARCH_LAB), AssociatedStructureType.valueOfCaseInsensitive("EUROPEAN_RESEARCH_lab"));
		assertEquals(cons(AssociatedStructureType.NATIONAL_RESEARCH_LAB), AssociatedStructureType.valueOfCaseInsensitive("NATIONAL_RESEARCH_lab"));
		assertEquals(cons(AssociatedStructureType.INTERNATIONAL_SCIENTIFIC_INTEREST_GROUP), AssociatedStructureType.valueOfCaseInsensitive("INTERNATIONAL_SCIENTIFIC_INTEREST_group"));
		assertEquals(cons(AssociatedStructureType.EUROPEAN_SCIENTIFIC_INTEREST_GROUP), AssociatedStructureType.valueOfCaseInsensitive("EUROPEAN_SCIENTIFIC_INTEREST_group"));
		assertEquals(cons(AssociatedStructureType.NATIONAL_SCIENTIFIC_INTEREST_GROUP), AssociatedStructureType.valueOfCaseInsensitive("NATIONAL_SCIENTIFIC_INTEREST_group"));
		assertEquals(cons(AssociatedStructureType.INTERNATIONAL_RESEARCH_GROUP), AssociatedStructureType.valueOfCaseInsensitive("INTERNATIONAL_RESEARCH_group"));
		assertEquals(cons(AssociatedStructureType.EUROPEAN_RESEARCH_GROUP), AssociatedStructureType.valueOfCaseInsensitive("EUROPEAN_RESEARCH_group"));
		assertEquals(cons(AssociatedStructureType.NATIONAL_RESEARCH_GROUP), AssociatedStructureType.valueOfCaseInsensitive("NATIONAL_RESEARCH_group"));
		assertEquals(cons(AssociatedStructureType.HOSTED_INTERNATIONAL_COMPANY), AssociatedStructureType.valueOfCaseInsensitive("HOSTED_INTERNATIONAL_company"));
		assertEquals(cons(AssociatedStructureType.HOSTED_EUROPEAN_COMPANY), AssociatedStructureType.valueOfCaseInsensitive("HOSTED_EUROPEAN_company"));
		assertEquals(cons(AssociatedStructureType.HOSTED_NATIONAL_COMPANY), AssociatedStructureType.valueOfCaseInsensitive("HOSTED_NATIONAL_company"));
		assertAllTreated();
		assertThrows(IllegalArgumentException.class, () -> AssociatedStructureType.valueOfCaseInsensitive("xyz"));
	}

	@Test
	public void getLabel() throws Exception {
		assertEquals("Private company", cons(AssociatedStructureType.PRIVATE_COMPANY).getLabel(this.messages, Locale.US));
		assertEquals("Industrial chair", cons(AssociatedStructureType.INDUSTRIAL_CHAIR).getLabel(this.messages, Locale.US));
		assertEquals("Research chair", cons(AssociatedStructureType.RESEARCH_CHAIR).getLabel(this.messages, Locale.US));
		assertEquals("International Research Lab", cons(AssociatedStructureType.INTERNATIONAL_RESEARCH_LAB).getLabel(this.messages, Locale.US));
		assertEquals("European Research Lab", cons(AssociatedStructureType.EUROPEAN_RESEARCH_LAB).getLabel(this.messages, Locale.US));
		assertEquals("National Research Lab", cons(AssociatedStructureType.NATIONAL_RESEARCH_LAB).getLabel(this.messages, Locale.US));
		assertEquals("International Group of Scientific Interest", cons(AssociatedStructureType.INTERNATIONAL_SCIENTIFIC_INTEREST_GROUP).getLabel(this.messages, Locale.US));
		assertEquals("European Group of Scientific Interest", cons(AssociatedStructureType.EUROPEAN_SCIENTIFIC_INTEREST_GROUP).getLabel(this.messages, Locale.US));
		assertEquals("National Group of Scientific Interest", cons(AssociatedStructureType.NATIONAL_SCIENTIFIC_INTEREST_GROUP).getLabel(this.messages, Locale.US));
		assertEquals("International Research Group", cons(AssociatedStructureType.INTERNATIONAL_RESEARCH_GROUP).getLabel(this.messages, Locale.US));
		assertEquals("European Research Group", cons(AssociatedStructureType.EUROPEAN_RESEARCH_GROUP).getLabel(this.messages, Locale.US));
		assertEquals("National Research Group", cons(AssociatedStructureType.NATIONAL_RESEARCH_GROUP).getLabel(this.messages, Locale.US));
		assertEquals("Hosted International Company", cons(AssociatedStructureType.HOSTED_INTERNATIONAL_COMPANY).getLabel(this.messages, Locale.US));
		assertEquals("Hosted European Company", cons(AssociatedStructureType.HOSTED_EUROPEAN_COMPANY).getLabel(this.messages, Locale.US));
		assertEquals("Hosted National Company", cons(AssociatedStructureType.HOSTED_NATIONAL_COMPANY).getLabel(this.messages, Locale.US));
		assertAllTreated();
	}

}
