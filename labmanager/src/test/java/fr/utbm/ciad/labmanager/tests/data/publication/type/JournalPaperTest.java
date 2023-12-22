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

package fr.utbm.ciad.labmanager.tests.data.publication.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import fr.utbm.ciad.labmanager.data.journal.Journal;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.data.publication.type.JournalPaper;
import fr.utbm.ciad.labmanager.data.publication.type.KeyNote;
import fr.utbm.ciad.labmanager.utils.ranking.QuartileRanking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for {@link JournalPaper}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class JournalPaperTest extends AbstractTypedPublicationTest<JournalPaper> {

	@Override
	protected JournalPaper createTest() {
		return new JournalPaper();
	}

	@Override
	protected JournalPaper createTest(Publication prePublication) {
		return new JournalPaper(prePublication, null, null, null, null);
	}

	@Test
	public void isRanked_notRanked() {
		final Journal jour = mock(Journal.class);
		when(jour.getScimagoQIndexByYear(anyInt())).thenCallRealMethod();
		when(jour.getWosQIndexByYear(anyInt())).thenCallRealMethod();
		this.test.setJournal(jour);
		this.test.setPublicationYear(2022);
		assertFalse(this.test.isRanked());
	}

	@Test
	public void isRanked_scimagoRanked() {
		final Journal jour = mock(Journal.class);
		when(jour.getScimagoQIndexByYear(anyInt())).thenReturn(QuartileRanking.Q1);
		when(jour.getWosQIndexByYear(anyInt())).thenCallRealMethod();
		this.test.setJournal(jour);
		this.test.setPublicationYear(2022);
		assertTrue(this.test.isRanked());
	}

	@Test
	public void isRanked_wosRanked() {
		final Journal jour = mock(Journal.class);
		when(jour.getScimagoQIndexByYear(anyInt())).thenCallRealMethod();
		when(jour.getWosQIndexByYear(anyInt())).thenReturn(QuartileRanking.Q2);
		this.test.setJournal(jour);
		this.test.setPublicationYear(2022);
		assertTrue(this.test.isRanked());
	}

	@Test
	public void isRanked_bothRanked() {
		final Journal jour = mock(Journal.class);
		when(jour.getScimagoQIndexByYear(anyInt())).thenReturn(QuartileRanking.Q3);
		when(jour.getWosQIndexByYear(anyInt())).thenReturn(QuartileRanking.Q4);
		this.test.setJournal(jour);
		this.test.setPublicationYear(2022);
		assertTrue(this.test.isRanked());
	}

	@Test
	public void getVolume() {
		assertNull(this.test.getVolume());
	}

	@Test
	public void setVolume() {
		this.test.setVolume("xyz");
		assertEquals("xyz", this.test.getVolume());

		this.test.setVolume("");
		assertNull(this.test.getVolume());

		this.test.setVolume(null);
		assertNull(this.test.getVolume());
	}

	@Test
	public void getNumber() {
		assertNull(this.test.getNumber());
	}

	@Test
	public void setNumber() {
		this.test.setNumber("xyz");
		assertEquals("xyz", this.test.getNumber());

		this.test.setNumber("");
		assertNull(this.test.getNumber());

		this.test.setNumber(null);
		assertNull(this.test.getNumber());
	}

	@Test
	public void getPages() {
		assertNull(this.test.getPages());
	}

	@Test
	public void setPages() {
		this.test.setPages("xyz");
		assertEquals("xyz", this.test.getPages());

		this.test.setPages("");
		assertNull(this.test.getPages());

		this.test.setPages(null);
		assertNull(this.test.getPages());
	}

	@Test
	public void getSeries() {
		assertNull(this.test.getSeries());
	}

	@Test
	public void setSeries() {
		this.test.setSeries("xyz");
		assertEquals("xyz", this.test.getSeries());

		this.test.setSeries("");
		assertNull(this.test.getSeries());

		this.test.setSeries(null);
		assertNull(this.test.getSeries());
	}

	@Test
	public void getJournal() {
		assertNull(this.test.getJournal());
	}

	@Test
	public void setJournal() {
		final Journal jour = mock(Journal.class);
		this.test.setJournal(jour);
		assertSame(jour, this.test.getJournal());

		this.test.setJournal(null);
		assertNull(this.test.getJournal());
	}

	@Test
	public void getScimagoQIndex() {
		assertSame(QuartileRanking.NR, this.test.getScimagoQIndex());

		final Journal jour = mock(Journal.class);
		when(jour.getScimagoQIndexByYear(anyInt())).thenReturn(QuartileRanking.Q3);
		this.test.setJournal(jour);
		this.test.setPublicationYear(2022);

		assertSame(QuartileRanking.Q3, this.test.getScimagoQIndex());
	}

	@Test
	public void getScimagoCategory() {
		assertNull(this.test.getScimagoCategory());

		final Journal jour = mock(Journal.class);
		when(jour.getScimagoCategory()).thenReturn("abcd");
		this.test.setJournal(jour);

		assertEquals("abcd", this.test.getScimagoCategory());
	}

	@Test
	public void getWosQIndex() {
		assertSame(QuartileRanking.NR, this.test.getWosQIndex());

		final Journal jour = mock(Journal.class);
		when(jour.getWosQIndexByYear(anyInt())).thenReturn(QuartileRanking.Q2);
		this.test.setJournal(jour);
		this.test.setPublicationYear(2022);

		assertSame(QuartileRanking.Q2, this.test.getWosQIndex());
	}

	@Test
	public void getWosCategory() {
		assertNull(this.test.getWosCategory());

		final Journal jour = mock(Journal.class);
		when(jour.getWosCategory()).thenReturn("abcd");
		this.test.setJournal(jour);

		assertEquals("abcd", this.test.getWosCategory());
	}

	@Test
	public void getImpactFactor() {
		assertEquals(0f, this.test.getImpactFactor());

		final Journal jour = mock(Journal.class);
		when(jour.getImpactFactorByYear(anyInt())).thenReturn(426f);
		this.test.setJournal(jour);
		this.test.setPublicationYear(2022);

		assertEquals(426f, this.test.getImpactFactor());
	}

	private static JournalPaper createTransient1() {
		var e = new JournalPaper();
		e.setTitle("A");
		return e;
	}	

	private static JournalPaper createTransient2() {
		var e = new JournalPaper();
		e.setTitle("B");
		return e;
	}	

	private static JournalPaper createManaged1() {
		var e = createTransient1();
		e.setId(10l);
		return e;
	}	

	private static JournalPaper createManaged2() {
		var e = createTransient2();
		e.setId(20l);
		return e;
	}	

	@Test
	@DisplayName("t1.equals(null)")
	public void test_equals_0() {
		assertFalse(createTransient1().equals(null));
	}
	
	@Test
	@DisplayName("m1.equals(null)")
	public void test_equals_1() {
		assertFalse(createManaged1().equals(null));
	}

	@Test
	@DisplayName("t1.equals(t1)")
	public void test_equals_2() {
		var t1 = createTransient1();
		assertTrue(t1.equals(t1));
	}

	@Test
	@DisplayName("m1.equals(m1)")
	public void test_equals_3() {
		var m1 = createManaged1();
		assertTrue(m1.equals(m1));
	}

	@Test
	@DisplayName("t1.equals(t2)")
	public void test_equals_4() {
		// 2 transient entities need to be NOT equal
		var t1 = createTransient1();
		var t2 = createTransient2();
		assertFalse(t1.equals(t2));
	}

	@Test
	@DisplayName("t2.equals(t1)")
	public void test_equals_5() {
		// 2 transient entities need to be NOT equal
		var t1 = createTransient1();
		var t2 = createTransient2();
		assertFalse(t2.equals(t1));
	}

	@Test
	@DisplayName("m1.equals(m2)")
	public void test_equals_6() {
		// 2 managed entities that represent different records need to be NOT equal
		var m1 = createManaged1();
		var m2 = createManaged2();
		assertFalse(m1.equals(m2));
	}

	@Test
	@DisplayName("m2.equals(m1)")
	public void test_equals_7() {
		// 2 managed entities that represent different records need to be NOT equal
		var m1 = createManaged1();
		var m2 = createManaged2();
		assertFalse(m2.equals(m1));
	}

	@Test
	@DisplayName("m1.equals(m1')")
	public void test_equals_8() {
		// 2 managed entities that represent the same record need to be equal
		var m1 = createManaged1();
		var m1p = createManaged1();
		assertTrue(m1.equals(m1p));
	}

	@Test
	@DisplayName("m1'.equals(m1)")
	public void test_equals_9() {
		// 2 managed entities that represent the same record need to be equal
		var m1 = createManaged1();
		var m1p = createManaged1();
		assertTrue(m1p.equals(m1));
	}

	@Test
	@DisplayName("m1.equals(t1)")
	public void test_equals_10() {
		// a detached/transient and a managed entity object that represent the same record need to be equal
		var t1 = createTransient1();
		var m1 = createManaged1();
		assertTrue(m1.equals(t1));
	}

	@Test
	@DisplayName("t1.equals(m1)")
	public void test_equals_11() {
		// a detached/transient and a managed entity object that represent the same record need to be equal
		var t1 = createTransient1();
		var m1 = createManaged1();
		assertTrue(t1.equals(m1));
	}

	@Test
	@DisplayName("m1.equals(t2)")
	public void test_equals_12() {
		var t2 = createTransient2();
		var m1 = createManaged1();
		assertFalse(m1.equals(t2));
	}

	@Test
	@DisplayName("t2.equals(m1)")
	public void test_equals_13() {
		var t2 = createTransient2();
		var m1 = createManaged1();
		assertFalse(t2.equals(m1));
	}

	@Test
	@DisplayName("t1.hashCode == t1.hashCode")
	public void test_hashCode_0() {
		var t1 = createTransient1();
		assertEquals(t1.hashCode(), t1.hashCode());
	}

	@Test
	@DisplayName("t1.hashCode == t1p.hashCode")
	public void test_hashCode_1() {
		var t1 = createTransient1();
		var t1p = createTransient1();
		assertEquals(t1.hashCode(), t1p.hashCode());
	}

	@Test
	@DisplayName("t1.hashCode != m1.hashCode")
	public void test_hashCode_2() {
		var t1 = createTransient1();
		var m1 = createManaged1();
		assertNotEquals(t1.hashCode(), m1.hashCode());
	}

}
