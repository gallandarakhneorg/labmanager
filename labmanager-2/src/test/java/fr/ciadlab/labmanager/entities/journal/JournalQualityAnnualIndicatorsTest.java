/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.entities.journal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link JournalQualityAnnualIndicators}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class JournalQualityAnnualIndicatorsTest {

	private JournalQualityAnnualIndicators test;

	@BeforeEach
	public void setUp() {
		this.test = new JournalQualityAnnualIndicators();
	}

	@Test
	public void getYear() {
		assertEquals(0, this.test.getYear());
	}

	@Test
	public void setYear() {
		assertEquals(0, this.test.getYear());

		this.test.setYear(1234);
		assertEquals(1234, this.test.getYear());

		this.test.setYear(0);
		assertEquals(0, this.test.getYear());

		this.test.setYear(-56884);
		assertEquals(-56884, this.test.getYear());
	}

	@Test
	public void getScimagoQIndex() {
		assertNull(this.test.getScimagoQIndex());
	}

	@Test
	public void setScimagoQIndex() {
		assertNull(this.test.getScimagoQIndex());

		this.test.setScimagoQIndex(QuartileRanking.Q2);
		assertSame(QuartileRanking.Q2, this.test.getScimagoQIndex());

		this.test.setScimagoQIndex(null);
		assertNull(this.test.getScimagoQIndex());
	}

	@Test
	public void getWosQIndex() {
		assertNull(this.test.getWosQIndex());
	}

	@Test
	public void setWosQIndex() {
		assertNull(this.test.getWosQIndex());

		this.test.setWosQIndex(QuartileRanking.Q2);
		assertSame(QuartileRanking.Q2, this.test.getWosQIndex());

		this.test.setWosQIndex(null);
		assertNull(this.test.getWosQIndex());
	}

	@Test
	public void getImpactFactor() {
		assertEquals(0f, this.test.getImpactFactor());
	}

	@Test
	public void setImpactFactor() {
		assertEquals(0f, this.test.getImpactFactor());

		this.test.setImpactFactor(12.56f);
		assertEquals(12.56f, this.test.getImpactFactor());

		this.test.setImpactFactor(-458f);
		assertEquals(12.56f, this.test.getImpactFactor());
	}

}
