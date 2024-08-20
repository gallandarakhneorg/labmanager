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
import static org.mockito.Mockito.*;

import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.data.publication.PublicationType;
import fr.utbm.ciad.labmanager.data.publication.comparators.PublicationComparator;
import fr.utbm.ciad.labmanager.data.publication.comparators.PublicationTitleComparator;
import fr.utbm.ciad.labmanager.utils.names.DefaultPersonNameParser;
import fr.utbm.ciad.labmanager.utils.names.OrganizationNameComparator;
import fr.utbm.ciad.labmanager.utils.names.PersonNameComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Abstract tests for comparators of publications.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@SuppressWarnings("all")
public abstract class AbstractTestPublicationComparator {

	private static final int YEAR1 = 2024;

	private static final int YEAR2 = 2025;

	private static final PublicationType TYPE1 = PublicationType.INTERNATIONAL_JOURNAL_PAPER;

	private static final PublicationType TYPE2 = PublicationType.INTERNATIONAL_CONFERENCE_PAPER;

	private static final String TITLE1 = "This is the title of the first publication";
	
	private static final String TITLE1_1 = "This is the Title of the First Publication";

	private static final String TITLE1_2 = "This is the Tîtle of the First Publication";

	private static final String TITLE1_3 = "This is the title of the first publications";

	private static final String DESCRIPTION1 = "The article was published in a journal";

	private static final String DESCRIPTION1_1 = "The Article was Published in a Journal";

	private static final String DESCRIPTION1_2 = "The article was pûblished in a joûrnal";

	private static final String DESCRIPTION1_3 = "The article was published in journal";

	private static final String TITLE2 = "Another document";

	private static final String DESCRIPTION2 = "Another description";

	private Publication pub1;
	
	private Publication pub1b;

	private Publication pub1_1;

	private Publication pub1_2;

	private Publication pub1_3;

	private Publication pub1_4;

	private Publication pub1_5;

	private Publication pub1_6;

	private Publication pub1_7;

	private Publication pub1_8;

	private Publication pub2_1;

	private Publication pub2_2;

	protected PublicationComparator test;

	private static Publication mockPub(int year, PublicationType type, String title, String description) {
		final var pub = mock(Publication.class);
		when(pub.getPublicationYear()).thenReturn(year);
		when(pub.getType()).thenReturn(type);
		when(pub.getTitle()).thenReturn(title);
		when(pub.getWherePublishedShortDescription()).thenReturn(description);
		return pub;
	}
	
	public void setUp() {
		this.pub1 = mockPub(YEAR1, TYPE1, TITLE1, DESCRIPTION1);
		this.pub1b = mockPub(YEAR1, TYPE1, TITLE1, DESCRIPTION1);
		this.pub1_1 = mockPub(YEAR2, TYPE1, TITLE1, DESCRIPTION1);
		this.pub1_2 = mockPub(YEAR1, TYPE2, TITLE1, DESCRIPTION1);
		this.pub1_3 = mockPub(YEAR1, TYPE1, TITLE1_1, DESCRIPTION1);
		this.pub1_4 = mockPub(YEAR1, TYPE1, TITLE1_2, DESCRIPTION1);
		this.pub1_5 = mockPub(YEAR1, TYPE1, TITLE1_3, DESCRIPTION1);
		this.pub1_6 = mockPub(YEAR1, TYPE1, TITLE1, DESCRIPTION1_1);
		this.pub1_7 = mockPub(YEAR1, TYPE1, TITLE1, DESCRIPTION1_2);
		this.pub1_8 = mockPub(YEAR1, TYPE1, TITLE1, DESCRIPTION1_3);
		this.pub2_1 = mockPub(YEAR1, TYPE1, TITLE2, DESCRIPTION1);
		this.pub2_2 = mockPub(YEAR1, TYPE1, TITLE1, DESCRIPTION2);
	}

	@Test
	@DisplayName("isSimilar null value")
	public void isSimilar_null() {
		assertFalse(this.test.isSimilar(null, this.pub1));
		assertFalse(this.test.isSimilar(this.pub1, null));
	}

	@Test
	@DisplayName("isSimilar same publications")
	public void isSimilar_equals() {
		assertTrue(this.test.isSimilar(this.pub1, this.pub1));
		assertTrue(this.test.isSimilar(this.pub1, this.pub1b));
	}

	@Test
	@DisplayName("isSimilar similar publications")
	public void isSimilar_similar() {
		assertTrue(this.test.isSimilar(this.pub1, this.pub1_1));
		assertTrue(this.test.isSimilar(this.pub1, this.pub1_2));
		assertTrue(this.test.isSimilar(this.pub1, this.pub1_3));
		assertTrue(this.test.isSimilar(this.pub1, this.pub1_4));
		assertTrue(this.test.isSimilar(this.pub1, this.pub1_5));
		assertTrue(this.test.isSimilar(this.pub1, this.pub1_6));
		assertTrue(this.test.isSimilar(this.pub1, this.pub1_7));
		assertTrue(this.test.isSimilar(this.pub1, this.pub1_8));
	}

	@Test
	@DisplayName("isSimilar different publications")
	public void isSimilar_different() {
		assertFalse(this.test.isSimilar(this.pub1, this.pub2_1));
		assertFalse(this.test.isSimilar(this.pub1, this.pub2_2));
	}

}
