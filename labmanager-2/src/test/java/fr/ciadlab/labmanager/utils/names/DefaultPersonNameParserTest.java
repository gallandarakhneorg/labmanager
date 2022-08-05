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

package fr.ciadlab.labmanager.utils.names;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import fr.ciadlab.labmanager.utils.names.PersonNameParser.NameCallback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link DefaultPersonNameParser}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class DefaultPersonNameParserTest {

	private DefaultPersonNameParser test;

	@BeforeEach
	public void setUp() {
		this.test = new DefaultPersonNameParser();
	}

	@Test
	public void parseFirstName_emptyInput() {
		assertNull(this.test.parseFirstName(null));	
		assertNull(this.test.parseFirstName(""));
		assertNull(this.test.parseFirstName(" "));
	}

	@Test
	public void parseFirstName_formatFirstLast() {
		assertEquals("First", this.test.parseFirstName("First"));
		assertEquals("First", this.test.parseFirstName(" First"));
		assertEquals("First", this.test.parseFirstName("First "));
		assertEquals("First", this.test.parseFirstName(" First "));

		assertEquals("First", this.test.parseFirstName("First Last"));
		assertEquals("First", this.test.parseFirstName(" First Last"));
		assertEquals("First", this.test.parseFirstName("First Last "));
		assertEquals("First", this.test.parseFirstName(" First Last "));

		assertEquals("First", this.test.parseFirstName("First Last Last1"));
		assertEquals("First", this.test.parseFirstName(" First Last Last1"));
		assertEquals("First", this.test.parseFirstName("First Last Last1 "));
		assertEquals("First", this.test.parseFirstName(" First Last Last1 "));
	}

	@Test
	public void parseFirstName_formatLastFirst() {
		assertEquals("First", this.test.parseFirstName("Last , First"));
		assertEquals("First", this.test.parseFirstName(" Last , First"));
		assertEquals("First", this.test.parseFirstName("Last , First "));
		assertEquals("First", this.test.parseFirstName(" Last , First "));

		assertEquals("First First2", this.test.parseFirstName("Last , First First2"));
		assertEquals("First First2", this.test.parseFirstName(" Last , First First2"));
		assertEquals("First First2", this.test.parseFirstName("Last , First First2 "));
		assertEquals("First First2", this.test.parseFirstName(" Last , First First2 "));

		assertEquals("First First2", this.test.parseFirstName("Last Last2 , First First2"));
		assertEquals("First First2", this.test.parseFirstName(" Last Last2 , First First2"));
		assertEquals("First First2", this.test.parseFirstName("Last Last2 , First First2 "));
		assertEquals("First First2", this.test.parseFirstName(" Last Last2 , First First2 "));
	}

	@Test
	public void parseFirstName_specials() {
		assertEquals("J.C.", this.test.parseFirstName("J.C. Creput"));
		assertEquals("J C", this.test.parseFirstName("J C Creput"));
		assertEquals("A.", this.test.parseFirstName("A. ABBAS-TURKI"));
		assertEquals("A", this.test.parseFirstName("A ABBAS-TURKI"));
		assertEquals("I.", this.test.parseFirstName("I. EL KHADIRI"));
		assertEquals("I", this.test.parseFirstName("I EL KHADIRI"));
		assertEquals("Y.", this.test.parseFirstName("Y. EL MERABET"));
		assertEquals("Y", this.test.parseFirstName("Y EL MERABET"));
		assertEquals("I.", this.test.parseFirstName("I. EL-KHADIRI"));
		assertEquals("I", this.test.parseFirstName("I EL-KHADIRI"));
		assertEquals("Y.", this.test.parseFirstName("Y. EL-MERABET"));
		assertEquals("Y", this.test.parseFirstName("Y EL-MERABET"));
		assertEquals("A. S.", this.test.parseFirstName("A. S. MALIK"));
		assertEquals("A S", this.test.parseFirstName("A S MALIK"));
		assertEquals("T.", this.test.parseFirstName("T. MENDES DE FARIAS"));
		assertEquals("T", this.test.parseFirstName("T MENDES DE FARIAS"));
		assertEquals("W.S.H.", this.test.parseFirstName("W.S.H. SYED"));
		assertEquals("W S H", this.test.parseFirstName("W S H SYED"));
		assertEquals("W S. H", this.test.parseFirstName("W S. H SYED"));
		assertEquals("W. S H.", this.test.parseFirstName("W. S H. SYED"));
		assertEquals("P.-J.", this.test.parseFirstName("P.-J. LAPRAY"));
		assertEquals("P-J.", this.test.parseFirstName("P-J. LAPRAY"));
		assertEquals("P.-J", this.test.parseFirstName("P.-J LAPRAY"));
		assertEquals("P-J", this.test.parseFirstName("P-J LAPRAY"));
		assertEquals("Jean-Michel", this.test.parseFirstName("Jean-Michel Dupont"));
	}

	@Test
	public void parseLastName_emptyInput() {
		assertNull(this.test.parseLastName(null));	
		assertNull(this.test.parseLastName(""));
		assertNull(this.test.parseLastName(" "));
	}

	@Test
	public void parseLastName_formatFirstLast() {
		assertNull(this.test.parseLastName("First"));
		assertNull(this.test.parseLastName(" First"));
		assertNull(this.test.parseLastName("First "));
		assertNull(this.test.parseLastName(" First "));

		assertEquals("Last", this.test.parseLastName("First Last"));
		assertEquals("Last", this.test.parseLastName(" First Last"));
		assertEquals("Last", this.test.parseLastName("First Last "));
		assertEquals("Last", this.test.parseLastName(" First Last "));

		assertEquals("Last Last1", this.test.parseLastName("First Last Last1"));
		assertEquals("Last Last1", this.test.parseLastName(" First Last Last1"));
		assertEquals("Last Last1", this.test.parseLastName("First Last Last1 "));
		assertEquals("Last Last1", this.test.parseLastName(" First Last Last1 "));
	}

	@Test
	public void parseLastName_formatLastFirst() {
		assertEquals("Last", this.test.parseLastName("Last , First"));
		assertEquals("Last", this.test.parseLastName(" Last , First"));
		assertEquals("Last", this.test.parseLastName("Last , First "));
		assertEquals("Last", this.test.parseLastName(" Last , First "));

		assertEquals("Last", this.test.parseLastName("Last , First First2"));
		assertEquals("Last", this.test.parseLastName(" Last , First First2"));
		assertEquals("Last", this.test.parseLastName("Last , First First2 "));
		assertEquals("Last", this.test.parseLastName(" Last , First First2 "));

		assertEquals("Last Last2", this.test.parseLastName("Last Last2 , First First2"));
		assertEquals("Last Last2", this.test.parseLastName(" Last Last2 , First First2"));
		assertEquals("Last Last2", this.test.parseLastName("Last Last2 , First First2 "));
		assertEquals("Last Last2", this.test.parseLastName(" Last Last2 , First First2 "));
	}

	@Test
	public void parseLastName_specials() {
		assertEquals("Creput", this.test.parseLastName("J.C. Creput"));
		assertEquals("Creput", this.test.parseLastName("J C Creput"));
		assertEquals("ABBAS-TURKI", this.test.parseLastName("A. ABBAS-TURKI"));
		assertEquals("ABBAS-TURKI", this.test.parseLastName("A ABBAS-TURKI"));
		assertEquals("EL KHADIRI", this.test.parseLastName("I. EL KHADIRI"));
		assertEquals("EL KHADIRI", this.test.parseLastName("I EL KHADIRI"));
		assertEquals("EL MERABET", this.test.parseLastName("Y. EL MERABET"));
		assertEquals("EL MERABET", this.test.parseLastName("Y EL MERABET"));
		assertEquals("EL-KHADIRI", this.test.parseLastName("I. EL-KHADIRI"));
		assertEquals("EL-KHADIRI", this.test.parseLastName("I EL-KHADIRI"));
		assertEquals("EL-MERABET", this.test.parseLastName("Y. EL-MERABET"));
		assertEquals("EL-MERABET", this.test.parseLastName("Y EL-MERABET"));
		assertEquals("MALIK", this.test.parseLastName("A. S. MALIK"));
		assertEquals("MALIK", this.test.parseLastName("A S MALIK"));
		assertEquals("MENDES DE FARIAS", this.test.parseLastName("T. MENDES DE FARIAS"));
		assertEquals("MENDES DE FARIAS", this.test.parseLastName("T MENDES DE FARIAS"));
		assertEquals("SYED", this.test.parseLastName("W.S.H. SYED"));
		assertEquals("SYED", this.test.parseLastName("W S H SYED"));
		assertEquals("SYED", this.test.parseLastName("W S. H SYED"));
		assertEquals("SYED", this.test.parseLastName("W. S H. SYED"));
		assertEquals("LAPRAY", this.test.parseLastName("P.-J. LAPRAY"));
		assertEquals("LAPRAY", this.test.parseLastName("P-J. LAPRAY"));
		assertEquals("LAPRAY", this.test.parseLastName("P.-J LAPRAY"));
		assertEquals("LAPRAY", this.test.parseLastName("P-J LAPRAY"));
		assertEquals("Dupont", this.test.parseLastName("Jean-Michel Dupont"));
	}

	@Test
	public void parseNames_null() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames(null, cb);
		assertEquals(0, n);
		verifyNoInteractions(cb);
	}

	@Test
	public void parseNames_empty() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("", cb);
		assertEquals(0, n);
		verifyNoInteractions(cb);
	}

	@Test
	public void parseNames_1name_format1() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Last1, First1", cb);
		assertEquals(1, n);
		verify(cb, only()).name(eq("First1"), isNull(), eq("Last1"), eq(0));
	}

	@Test
	public void parseNames_1name_format1_von() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Last1, Von1, First1", cb);
		assertEquals(1, n);
		verify(cb, only()).name(eq("First1"), eq("Von1"), eq("Last1"), eq(0));
	}

	@Test
	public void parseNames_1name_format2() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("First1 Last1", cb);
		assertEquals(1, n);
		verify(cb, only()).name(eq("First1"), isNull(), eq("Last1"), eq(0));
	}

	@Test
	public void parseNames_2names_format1() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Last1, First1 and Last2, First2", cb);
		assertEquals(2, n);
		verify(cb).name(eq("First1"), isNull(), eq("Last1"), eq(0));
		verify(cb).name(eq("First2"), isNull(), eq("Last2"), eq(1));
	}

	@Test
	public void parseNames_2names_format1_von0() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Last1, Von1, First1 and Last2, Von2, First2", cb);
		assertEquals(2, n);
		verify(cb).name(eq("First1"), eq("Von1"), eq("Last1"), eq(0));
		verify(cb).name(eq("First2"), eq("Von2"), eq("Last2"), eq(1));
	}

	@Test
	public void parseNames_2names_format1_von1() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Last1, Von1, First1 and Last2, First2", cb);
		assertEquals(2, n);
		verify(cb).name(eq("First1"), eq("Von1"), eq("Last1"), eq(0));
		verify(cb).name(eq("First2"), isNull(), eq("Last2"), eq(1));
	}

	@Test
	public void parseNames_2names_format1_von2() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Last1, First1 and Last2, Von2, First2", cb);
		assertEquals(2, n);
		verify(cb).name(eq("First1"), isNull(), eq("Last1"), eq(0));
		verify(cb).name(eq("First2"), eq("Von2"), eq("Last2"), eq(1));
	}

	@Test
	public void parseNames_3names_format1() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Last1, First1 and Last2, First2 and Last3, First3", cb);
		assertEquals(3, n);
		verify(cb).name(eq("First1"), isNull(), eq("Last1"), eq(0));
		verify(cb).name(eq("First2"), isNull(), eq("Last2"), eq(1));
		verify(cb).name(eq("First3"), isNull(), eq("Last3"), eq(2));
	}

	@Test
	public void parseNames_3names_format2_recognizedAsWithFormat2() {
		// The following names are recognized with the format 1 (AND-separator)
		// because it is assumed to be equivalent to "LAST, VON, FIRST".
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("First1 Last1 , First2  Last2, First3 Last3", cb);
		assertEquals(1, n);
		verify(cb).name(eq("First3 Last3"), eq("First2  Last2"), eq("First1 Last1"), eq(0));
	}

	@Test
	public void parseNames_4names_format1() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Last1, First1 and Last2, First2 and Last3, First3 and Last4, First4", cb);
		assertEquals(4, n);
		verify(cb).name(eq("First1"), isNull(), eq("Last1"), eq(0));
		verify(cb).name(eq("First2"), isNull(), eq("Last2"), eq(1));
		verify(cb).name(eq("First3"), isNull(), eq("Last3"), eq(2));
		verify(cb).name(eq("First4"), isNull(), eq("Last4"), eq(3));
	}

	@Test
	public void parseNames_4names_format2() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("First1 Last1 , First2  Last2, First3 Last3, First4 Last4", cb);
		assertEquals(4, n);
		verify(cb).name(eq("First1"), isNull(), eq("Last1"), eq(0));
		verify(cb).name(eq("First2"), isNull(), eq("Last2"), eq(1));
		verify(cb).name(eq("First3"), isNull(), eq("Last3"), eq(2));
		verify(cb).name(eq("First4"), isNull(), eq("Last4"), eq(3));
	}

	@Test
	public void parseNames_5names_format1() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Last1, First1 and Last2, First2 and Last3, First3 and Last4, First4 and Last5, First5", cb);
		assertEquals(5, n);
		verify(cb).name(eq("First1"), isNull(), eq("Last1"), eq(0));
		verify(cb).name(eq("First2"), isNull(), eq("Last2"), eq(1));
		verify(cb).name(eq("First3"), isNull(), eq("Last3"), eq(2));
		verify(cb).name(eq("First4"), isNull(), eq("Last4"), eq(3));
		verify(cb).name(eq("First5"), isNull(), eq("Last5"), eq(4));
	}

	@Test
	public void parseNames_5names_format2() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("First1 Last1 , First2  Last2, First3 Last3, First4 Last4, First5 Last5", cb);
		assertEquals(5, n);
		verify(cb).name(eq("First1"), isNull(), eq("Last1"), eq(0));
		verify(cb).name(eq("First2"), isNull(), eq("Last2"), eq(1));
		verify(cb).name(eq("First3"), isNull(), eq("Last3"), eq(2));
		verify(cb).name(eq("First4"), isNull(), eq("Last4"), eq(3));
		verify(cb).name(eq("First5"), isNull(), eq("Last5"), eq(4));
	}

	@Test
	public void parseNames_6names_format1() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Last1, First1 and Last2, First2 and Last3, First3 and Last4, First4 and Last5, First5 and Last6, First 6", cb);
		assertEquals(6, n);
		verify(cb).name(eq("First1"), isNull(), eq("Last1"), eq(0));
		verify(cb).name(eq("First2"), isNull(), eq("Last2"), eq(1));
		verify(cb).name(eq("First3"), isNull(), eq("Last3"), eq(2));
		verify(cb).name(eq("First4"), isNull(), eq("Last4"), eq(3));
		verify(cb).name(eq("First5"), isNull(), eq("Last5"), eq(4));
		verify(cb).name(eq("First 6"), isNull(), eq("Last6"), eq(5));
	}

	@Test
	public void parseNames_6names_format2() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("First1 Last1 , First2  Last2, First3 Last3, First4 Last4, First5 Last5, First6 Last 6", cb);
		assertEquals(6, n);
		verify(cb).name(eq("First1"), isNull(), eq("Last1"), eq(0));
		verify(cb).name(eq("First2"), isNull(), eq("Last2"), eq(1));
		verify(cb).name(eq("First3"), isNull(), eq("Last3"), eq(2));
		verify(cb).name(eq("First4"), isNull(), eq("Last4"), eq(3));
		verify(cb).name(eq("First5"), isNull(), eq("Last5"), eq(4));
		verify(cb).name(eq("First6"), isNull(), eq("Last 6"), eq(5));
	}

	@Test
	public void normalizeName_null() {
		assertNull(this.test.normalizeName(null));
	}

	@Test
	public void normalizeName_empty() {
		assertNull(this.test.normalizeName(""));
	}

	@Test
	public void normalizeName_whitespaces() {
		assertNull(this.test.normalizeName("     "));
	}

	@Test
	public void normalizeName_0() {
		assertEquals("ABC", this.test.normalizeName(" abc   "));
	}

	@Test
	public void normalizeName_1() {
		assertEquals("ABC XYZ", this.test.normalizeName(" aBc xyZ   "));
	}

	@Test
	public void normalizeName_2() {
		assertEquals("ABC EYZ", this.test.normalizeName(" aBc éyZ   "));
	}

	@Test
	public void normalizeName_3() {
		assertEquals("A EYZ", this.test.normalizeName(" a. éyZ   "));
	}

	@Test
	public void normalizeName_4() {
		assertEquals("J P EYZ", this.test.normalizeName(" j.-p. éyZ   "));
	}

	private void assertEqualsSet(String[] expected, Set<String> actual) {
		final List<String> expected0 = Arrays.asList(expected);
		final Set<String> expected1 = new TreeSet<>(expected0);
		for (final String act : actual) {
			assertTrue(expected1.remove(act), "Too much element: " + act);
		}
		assertTrue(expected1.isEmpty(), "Expecting elements: " + expected1);
	}

	@Test
	public void getNormalizedNamesFor_null_withShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(null, true);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void getNormalizedNamesFor_empty_withShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor("", true);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void getNormalizedNamesFor_whitespaces_withShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor("    ", true);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void getNormalizedNamesFor_simpleName_withShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" Name ", true);
		assertEqualsSet(new String[] {"NAME", "N"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_twoNames_withShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First   Second ", true);
		assertEqualsSet(new String[] {
				"FIRST SECOND",
				"F S", "FIRST S", "F SECOND"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_threeNames_withShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First   Second   Third ", true);
		assertEqualsSet(new String[] {
				"F S T", "FIRST S T", "F SECOND T", "F S THIRD",
				"FIRST SECOND T", "FIRST S THIRD", "F SECOND THIRD",
				"FIRST SECOND THIRD",
				"FIRST S T", "F SECOND T",
				"F S THIRD",
				"FIRST SECOND T", 
				"FIRST S THIRD", 
		"F SECOND THIRD"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_initial0_withShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" N ", true);
		assertEqualsSet(new String[] {"N"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_initial1_withShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" N. ", true);
		assertEqualsSet(new String[] {"N"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_composed0_withShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First -  Second ", true);
		assertEqualsSet(new String[] {
				"FIRST SECOND",
				"F S",
				"F SECOND",
		"FIRST S"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_composed1_withShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F. -  Second ", true);
		assertEqualsSet(new String[] {
				"F S",
		"F SECOND"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_composed2_withShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F. -  S. ", true);
		assertEqualsSet(new String[] {
		"F S"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_composed3_withShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First -  S. ", true);
		assertEqualsSet(new String[] {
				"F S",
		"FIRST S"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_composed4_withShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F -  Second ", true);
		assertEqualsSet(new String[] {
				"F S",
		"F SECOND"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_composed5_withShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F. -  S ", true);
		assertEqualsSet(new String[] {
		"F S"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_composed6_withShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F -  S ", true);
		assertEqualsSet(new String[] {
		"F S"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_composed7_withShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F -  S. ", true);
		assertEqualsSet(new String[] {
		"F S"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_composed8_withShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First -  S ", true);
		assertEqualsSet(new String[] {
				"F S",
		"FIRST S"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_null_withoutShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(null, false);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void getNormalizedNamesFor_empty_withoutShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor("", false);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void getNormalizedNamesFor_whitespaces_withoutShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor("    ", false);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void getNormalizedNamesFor_simpleName_withoutShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" Name ", false);
		assertEqualsSet(new String[] {"NAME"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_twoNames_withoutShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First   Second ", false);
		assertEqualsSet(new String[] {
		"FIRST SECOND"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_threeNames_withoutShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First   Second   Third ", false);
		assertEqualsSet(new String[] {
		"FIRST SECOND THIRD"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_initial0_withoutShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" N ", false);
		assertEqualsSet(new String[] {"N"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_initial1_withoutShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" N. ", false);
		assertEqualsSet(new String[] {"N"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_composed0_withoutShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First -  Second ", false);
		assertEqualsSet(new String[] {
		"FIRST SECOND"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_composed1_withoutShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F. -  Second ", false);
		assertEqualsSet(new String[] {
		"F SECOND"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_composed2_withoutShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F. -  S. ", false);
		assertEqualsSet(new String[] {
		"F S"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_composed3_withoutShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First -  S. ", false);
		assertEqualsSet(new String[] {
		"FIRST S"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_composed4_withoutShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F -  Second ", false);
		assertEqualsSet(new String[] {
		"F SECOND"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_composed5_withoutShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F. -  S ", false);
		assertEqualsSet(new String[] {
		"F S"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_composed6_withoutShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F -  S ", false);
		assertEqualsSet(new String[] {
		"F S"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_composed7_withoutShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F -  S. ", false);
		assertEqualsSet(new String[] {
		"F S"}, actual);
	}

	@Test
	public void getNormalizedNamesFor_composed8_withoutShortNames() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First -  S ", false);
		assertEqualsSet(new String[] {
		"FIRST S"}, actual);
	}

	@Test
	public void isShortName_null() {
		assertFalse(this.test.isShortName(null));
	}

	@Test
	public void isShortName_empty() {
		assertFalse(this.test.isShortName(""));
	}

	@Test
	public void isShortName_whitespaces() {
		assertFalse(this.test.isShortName("    "));
	}

	@Test
	public void isShortName_initial_0() {
		assertTrue(this.test.isShortName("  S  "));
	}

	@Test
	public void isShortName_initial_1() {
		assertTrue(this.test.isShortName("  S.  "));
	}

	@Test
	public void isShortName_initials_0() {
		assertTrue(this.test.isShortName("  S  R  "));
	}

	@Test
	public void isShortName_initials_1() {
		assertTrue(this.test.isShortName("  S  R.  "));
	}

	@Test
	public void isShortName_initials_2() {
		assertTrue(this.test.isShortName("  S.  R.  "));
	}

	@Test
	public void isShortName_initials_3() {
		assertTrue(this.test.isShortName("  S.  R  "));
	}

	@Test
	public void isShortName_initials_4() {
		assertTrue(this.test.isShortName("  S  R G "));
	}

	@Test
	public void isShortName_initials_5() {
		assertTrue(this.test.isShortName("  S  R. G "));
	}

	@Test
	public void isShortName_initials_6() {
		assertTrue(this.test.isShortName("  S.  R.G  "));
	}

	@Test
	public void isShortName_initials_7() {
		assertTrue(this.test.isShortName("  S.  R  G"));
	}

	@Test
	public void isShortName_initials_8() {
		assertTrue(this.test.isShortName("  S-R  "));
	}

	@Test
	public void isShortName_initials_9() {
		assertTrue(this.test.isShortName("  S-R.  "));
	}

	@Test
	public void isShortName_initials_10() {
		assertTrue(this.test.isShortName("  S.-R.  "));
	}

	@Test
	public void isShortName_initials_11() {
		assertTrue(this.test.isShortName("  S.-R  "));
	}

	@Test
	public void isShortName_initials_12() {
		assertTrue(this.test.isShortName("  S-R-G "));
	}

	@Test
	public void isShortName_initials_13() {
		assertTrue(this.test.isShortName("  S-R.-G "));
	}

	@Test
	public void isShortName_initials_14() {
		assertTrue(this.test.isShortName("  S.-  R.G  "));
	}

	@Test
	public void isShortName_initials_15() {
		assertTrue(this.test.isShortName("  S. -R -G"));
	}

	@Test
	public void isShortName_name_0() {
		assertFalse(this.test.isShortName("  Stephane  R"));
	}

	@Test
	public void isShortName_name_1() {
		assertFalse(this.test.isShortName("  R Stephane "));
	}

	@Test
	public void isShortName_name_2() {
		assertFalse(this.test.isShortName("  Stephane Stephane "));
	}

	@Test
	public void isShortName_name_3() {
		assertFalse(this.test.isShortName("  Stephane-R"));
	}

	@Test
	public void isShortName_name_4() {
		assertFalse(this.test.isShortName("  R-Stephane "));
	}

	@Test
	public void isShortName_name_5() {
		assertFalse(this.test.isShortName("  Stephane-Stephane "));
	}

}
