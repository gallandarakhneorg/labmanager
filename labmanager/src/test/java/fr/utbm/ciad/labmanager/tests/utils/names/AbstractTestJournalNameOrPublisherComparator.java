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

import fr.utbm.ciad.labmanager.utils.names.ConferenceNameComparator;
import fr.utbm.ciad.labmanager.utils.names.DefaultPersonNameParser;
import fr.utbm.ciad.labmanager.utils.names.JournalNameOrPublisherComparator;
import fr.utbm.ciad.labmanager.utils.names.OrganizationNameComparator;
import fr.utbm.ciad.labmanager.utils.names.PersonNameComparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Abstract tests for comparators of journal names  and publisher names.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@SuppressWarnings("all")
public abstract class AbstractTestJournalNameOrPublisherComparator {

	private static final String NAME1 = "Name";
	
	private static final String NAME1b = new String("Name");

	private static final String NAME1_1 = "Name";

	private static final String NAME1_2 = "Nam";

	private static final String NAME1_3 = "ame";

	private static final String NAME1_4 = "Nâme";

	private static final String NAME1_5 = "NaMe";

	private static final String NAME2 = "Another identification";

	private static final String PUBLISHER1 = "Publisher";
	
	private static final String PUBLISHER1b = new String("Publisher");

	private static final String PUBLISHER1_1 = "Publishers";

	private static final String PUBLISHER1_2 = "Pubisher";

	private static final String PUBLISHER1_3 = "ublisher";

	private static final String PUBLISHER1_4 = "Pûblisher";

	private static final String PUBLISHER1_5 = "PuBliShEr";

	private static final String PUBLISHER2 = "Another entity";

	protected JournalNameOrPublisherComparator test;

	@Test
	@DisplayName("isSimilar same names")
	public void isSimilar_equals_name() {
		assertTrue(this.test.isSimilar(NAME1, PUBLISHER1, NAME1, PUBLISHER2));
		assertFalse(this.test.isSimilar(NAME1, PUBLISHER1, NAME2, PUBLISHER2));
		assertTrue(this.test.isSimilar(NAME1, PUBLISHER1, NAME1b, PUBLISHER2));
	}

	@Test
	@DisplayName("isSimilar same publishers")
	public void isSimilar_equals_pub() {
		assertTrue(this.test.isSimilar(NAME1, PUBLISHER1, NAME2, PUBLISHER1));
		assertFalse(this.test.isSimilar(NAME1, PUBLISHER1, NAME2, PUBLISHER2));
		assertTrue(this.test.isSimilar(NAME1, PUBLISHER1, NAME2, PUBLISHER1b));
	}

	@Test
	@DisplayName("isSimilar null value")
	public void isSimilar_null() {
		assertTrue(this.test.isSimilar(null, PUBLISHER1, NAME1, PUBLISHER1));
		assertTrue(this.test.isSimilar(NAME1, PUBLISHER1, null, PUBLISHER1));
		assertTrue(this.test.isSimilar(NAME1, null, NAME1, PUBLISHER1));
		assertTrue(this.test.isSimilar(NAME1, PUBLISHER1, NAME1, null));
		assertFalse(this.test.isSimilar(null, PUBLISHER1, NAME1, PUBLISHER2));
		assertFalse(this.test.isSimilar(NAME1, PUBLISHER1, null, PUBLISHER2));
		assertFalse(this.test.isSimilar(NAME1, null, NAME2, PUBLISHER1));
		assertFalse(this.test.isSimilar(NAME1, PUBLISHER1, NAME2, null));
	}

	@Test
	@DisplayName("isSimilar approx names")
	public void isSimilar_similar_name() {
		assertTrue(this.test.isSimilar(NAME1, PUBLISHER1, NAME1_1, PUBLISHER2));
		assertTrue(this.test.isSimilar(NAME1, PUBLISHER1, NAME1_2, PUBLISHER2));
		assertTrue(this.test.isSimilar(NAME1, PUBLISHER1, NAME1_3, PUBLISHER2));
		assertTrue(this.test.isSimilar(NAME1, PUBLISHER1, NAME1_4, PUBLISHER2));
		assertTrue(this.test.isSimilar(NAME1, PUBLISHER1, NAME1_5, PUBLISHER2));
	}

	@Test
	@DisplayName("isSimilar approx publishers")
	public void isSimilar_similar_pub() {
		assertTrue(this.test.isSimilar(NAME1, PUBLISHER1, NAME2, PUBLISHER1_1));
		assertTrue(this.test.isSimilar(NAME1, PUBLISHER1, NAME2, PUBLISHER1_2));
		assertTrue(this.test.isSimilar(NAME1, PUBLISHER1, NAME2, PUBLISHER1_3));
		assertTrue(this.test.isSimilar(NAME1, PUBLISHER1, NAME2, PUBLISHER1_4));
		assertTrue(this.test.isSimilar(NAME1, PUBLISHER1, NAME2, PUBLISHER1_5));
	}

}
