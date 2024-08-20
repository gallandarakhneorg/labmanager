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

package fr.utbm.ciad.labmanager.tests.data.publication.comparators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.utbm.ciad.labmanager.data.publication.comparators.PublicationTitleComparator;
import fr.utbm.ciad.labmanager.utils.names.DefaultPersonNameParser;
import fr.utbm.ciad.labmanager.utils.names.OrganizationNameComparator;
import fr.utbm.ciad.labmanager.utils.names.PersonNameComparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Abstract tests for comparators of publication titles.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@SuppressWarnings("all")
public abstract class AbstractTestPublicationTitleComparator {

	private static final String TITLE1 = "Organization name";
	
	private static final String TITLE1b = new String("Organization name");

	private static final String TITLE1_1 = "Organization names";

	private static final String TITLE1_2 = "Organization ame";

	private static final String TITLE1_3 = "rganization name";

	private static final String TITLE1_4 = "Organizâtion name";

	private static final String TITLE1_5 = "OrGanIzation nAme";

	private static final String TITLE2 = "Another organization";

	protected PublicationTitleComparator test;

	@Test
	@DisplayName("isSimilar same titles")
	public void isSimilar_equals_name() {
		assertTrue(this.test.isSimilar(TITLE1, TITLE1));
		assertFalse(this.test.isSimilar(TITLE1, TITLE2));
		assertTrue(this.test.isSimilar(TITLE1, TITLE1b));
	}

	@Test
	@DisplayName("isSimilar null value")
	public void isSimilar_null() {
		assertTrue(this.test.isSimilar(null, TITLE1));
		assertTrue(this.test.isSimilar(TITLE1, null));
	}

	@Test
	@DisplayName("isSimilar approx titles")
	public void isSimilar_similar_name() {
		assertTrue(this.test.isSimilar(TITLE1, TITLE1_1));
		assertTrue(this.test.isSimilar(TITLE1, TITLE1_2));
		assertTrue(this.test.isSimilar(TITLE1, TITLE1_3));
		assertTrue(this.test.isSimilar(TITLE1, TITLE1_4));
		assertTrue(this.test.isSimilar(TITLE1, TITLE1_5));
	}

}
