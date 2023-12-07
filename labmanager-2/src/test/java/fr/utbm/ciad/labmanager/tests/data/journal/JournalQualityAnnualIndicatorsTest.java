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

package fr.utbm.ciad.labmanager.tests.data.journal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import fr.utbm.ciad.labmanager.data.journal.JournalQualityAnnualIndicators;
import fr.utbm.ciad.labmanager.utils.ranking.QuartileRanking;
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
	public void getScimagoQIndex() {
		assertNull(this.test.getScimagoQIndex());
	}

	@Test
	public void setScimagoQIndex_enum() {
		assertNull(this.test.getScimagoQIndex());

		this.test.setScimagoQIndex(QuartileRanking.Q2);
		assertSame(QuartileRanking.Q2, this.test.getScimagoQIndex());

		this.test.setScimagoQIndex((QuartileRanking) null);
		assertNull(this.test.getScimagoQIndex());
	}

	@Test
	public void setScimagoQIndex_string() {
		assertNull(this.test.getScimagoQIndex());

		this.test.setScimagoQIndex("Q2");
		assertSame(QuartileRanking.Q2, this.test.getScimagoQIndex());

		this.test.setScimagoQIndex((String) null);
		assertNull(this.test.getScimagoQIndex());
	}

	@Test
	public void getWosQIndex() {
		assertNull(this.test.getWosQIndex());
	}

	@Test
	public void setWosQIndex_enum() {
		assertNull(this.test.getWosQIndex());

		this.test.setWosQIndex(QuartileRanking.Q2);
		assertSame(QuartileRanking.Q2, this.test.getWosQIndex());

		this.test.setWosQIndex((QuartileRanking) null);
		assertNull(this.test.getWosQIndex());
	}

	@Test
	public void setWosQIndex_string() {
		assertNull(this.test.getWosQIndex());

		this.test.setWosQIndex("Q2");
		assertSame(QuartileRanking.Q2, this.test.getWosQIndex());

		this.test.setWosQIndex((String) null);
		assertNull(this.test.getWosQIndex());
	}

	@Test
	public void getImpactFactor() {
		assertEquals(0f, this.test.getImpactFactor());
	}

	@Test
	public void setImpactFactor_float() {
		assertEquals(0f, this.test.getImpactFactor());

		this.test.setImpactFactor(12.56f);
		assertEquals(12.56f, this.test.getImpactFactor());

		this.test.setImpactFactor(-458f);
		assertEquals(0f, this.test.getImpactFactor());
	}

	@Test
	public void setImpactFactor_string() {
		assertEquals(0f, this.test.getImpactFactor());

		this.test.setImpactFactor(Float.valueOf(12.56f));
		assertEquals(12.56f, this.test.getImpactFactor());

		this.test.setImpactFactor(Float.valueOf(-458f));
		assertEquals(0f, this.test.getImpactFactor());
	}

}
