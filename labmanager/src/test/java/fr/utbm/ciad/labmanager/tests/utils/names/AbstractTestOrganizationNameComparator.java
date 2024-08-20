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

package fr.utbm.ciad.labmanager.tests.utils.names;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.utbm.ciad.labmanager.utils.names.DefaultPersonNameParser;
import fr.utbm.ciad.labmanager.utils.names.OrganizationNameComparator;
import fr.utbm.ciad.labmanager.utils.names.PersonNameComparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Abstract tests for comparators of organization names 
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@SuppressWarnings("all")
public abstract class AbstractTestOrganizationNameComparator {

	private static final String ACRONYM1 = "Acronym";
	
	private static final String ACRONYM1b = new String("Acronym");

	private static final String ACRONYM1_1 = "Acronyme";

	private static final String ACRONYM1_2 = "Acrony";

	private static final String ACRONYM1_3 = "cronym";

	private static final String ACRONYM1_4 = "Acrônym";

	private static final String ACRONYM1_5 = "AcROnym";

	private static final String ACRONYM2 = "Another acronym";

	private static final String NAME1 = "Organization name";
	
	private static final String NAME1b = new String("Organization name");

	private static final String NAME1_1 = "Organization names";

	private static final String NAME1_2 = "Organization ame";

	private static final String NAME1_3 = "rganization name";

	private static final String NAME1_4 = "Organizâtion name";

	private static final String NAME1_5 = "OrGanIzation nAme";

	private static final String NAME2 = "Another organization";

	protected OrganizationNameComparator test;

	@Test
	@DisplayName("isSimilar same acronyms")
	public void isSimilar_equals_acro() {
		assertTrue(this.test.isSimilar(ACRONYM1, NAME1, ACRONYM1, NAME2));
		assertFalse(this.test.isSimilar(ACRONYM1, NAME1, ACRONYM2, NAME2));
		assertTrue(this.test.isSimilar(ACRONYM1, NAME1, ACRONYM1b, NAME2));
	}

	@Test
	@DisplayName("isSimilar same names")
	public void isSimilar_equals_name() {
		assertTrue(this.test.isSimilar(ACRONYM1, NAME1, ACRONYM2, NAME1));
		assertFalse(this.test.isSimilar(ACRONYM1, NAME1, ACRONYM2, NAME2));
		assertTrue(this.test.isSimilar(ACRONYM1, NAME1, ACRONYM2, NAME1b));
	}

	@Test
	@DisplayName("isSimilar null value")
	public void isSimilar_null() {
		assertTrue(this.test.isSimilar(null, NAME1, ACRONYM1, NAME1));
		assertTrue(this.test.isSimilar(ACRONYM1, NAME1, null, NAME1));
		assertTrue(this.test.isSimilar(ACRONYM1, null, ACRONYM1, NAME1));
		assertTrue(this.test.isSimilar(ACRONYM1, NAME1, ACRONYM1, null));
		assertTrue(this.test.isSimilar(null, NAME1, ACRONYM1, NAME2));
		assertTrue(this.test.isSimilar(ACRONYM1, NAME1, null, NAME2));
		assertTrue(this.test.isSimilar(ACRONYM1, null, ACRONYM2, NAME1));
		assertTrue(this.test.isSimilar(ACRONYM1, NAME1, ACRONYM2, null));
	}

	@Test
	@DisplayName("isSimilar approx acronyms")
	public void isSimilar_similar_acro() {
		assertTrue(this.test.isSimilar(ACRONYM1, NAME1, ACRONYM1_1, NAME2));
		assertTrue(this.test.isSimilar(ACRONYM1, NAME1, ACRONYM1_2, NAME2));
		assertTrue(this.test.isSimilar(ACRONYM1, NAME1, ACRONYM1_3, NAME2));
		assertTrue(this.test.isSimilar(ACRONYM1, NAME1, ACRONYM1_4, NAME2));
		assertTrue(this.test.isSimilar(ACRONYM1, NAME1, ACRONYM1_5, NAME2));
	}

	@Test
	@DisplayName("isSimilar approx names")
	public void isSimilar_similar_name() {
		assertTrue(this.test.isSimilar(ACRONYM1, NAME1, ACRONYM2, NAME1_1));
		assertTrue(this.test.isSimilar(ACRONYM1, NAME1, ACRONYM2, NAME1_2));
		assertTrue(this.test.isSimilar(ACRONYM1, NAME1, ACRONYM2, NAME1_3));
		assertTrue(this.test.isSimilar(ACRONYM1, NAME1, ACRONYM2, NAME1_4));
		assertTrue(this.test.isSimilar(ACRONYM1, NAME1, ACRONYM2, NAME1_5));
	}

}
