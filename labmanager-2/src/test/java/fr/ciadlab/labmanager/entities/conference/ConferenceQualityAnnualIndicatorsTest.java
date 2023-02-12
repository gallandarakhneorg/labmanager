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

package fr.ciadlab.labmanager.entities.conference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link ConferenceQualityAnnualIndicators}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ConferenceQualityAnnualIndicatorsTest {

	private ConferenceQualityAnnualIndicators test;

	@BeforeEach
	public void setUp() {
		this.test = new ConferenceQualityAnnualIndicators();
	}

	@Test
	public void getReferenceYear() {
		assertEquals(0, this.test.getReferenceYear());
	}

	@Test
	public void setReferenceYear() {
		assertEquals(0, this.test.getReferenceYear());

		this.test.setReferenceYear(1234);
		assertEquals(1234, this.test.getReferenceYear());

		this.test.setReferenceYear(0);
		assertEquals(0, this.test.getReferenceYear());

		this.test.setReferenceYear(-56884);
		assertEquals(-56884, this.test.getReferenceYear());
	}

	@Test
	public void getCoreIndex() {
		assertNull(this.test.getCoreIndex());
	}

	@Test
	public void setCoreIndex_enum() {
		assertNull(this.test.getCoreIndex());

		this.test.setCoreIndex(CoreRanking.A_STAR_STAR);
		assertSame(CoreRanking.A_STAR_STAR, this.test.getCoreIndex());

		this.test.setCoreIndex((CoreRanking) null);
		assertNull(this.test.getCoreIndex());
	}

	@Test
	public void setCoreIndex_string() {
		assertNull(this.test.getCoreIndex());

		this.test.setCoreIndex("A**");
		assertSame(CoreRanking.A_STAR_STAR, this.test.getCoreIndex());

		this.test.setCoreIndex((String) null);
		assertNull(this.test.getCoreIndex());
	}

}
