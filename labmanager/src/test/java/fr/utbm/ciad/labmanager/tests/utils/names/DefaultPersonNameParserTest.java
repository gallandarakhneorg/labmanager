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

import fr.utbm.ciad.labmanager.utils.names.DefaultPersonNameParser;
import fr.utbm.ciad.labmanager.utils.names.PersonNameParser.NameCallback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
	@DisplayName("parseFirstName(null)")
	public void parseFirstName_emptyInput() {
		assertNull(this.test.parseFirstName(null));	
		assertNull(this.test.parseFirstName(""));
		assertNull(this.test.parseFirstName(" "));
	}

	@Test
	@DisplayName("parseFirstName(\"First Last Last1\")")
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
	@DisplayName("parseFirstName(\"Last Last1, First\")")
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
	@DisplayName("parseFirstName(...)")
	public void parseFirstName_specials() {
		assertEquals("J. C.", this.test.parseFirstName("J.C. Creput"));
		assertEquals("J. C.", this.test.parseFirstName("J C Creput"));
		assertEquals("A.", this.test.parseFirstName("A. ABBAS-TURKI"));
		assertEquals("A.", this.test.parseFirstName("A ABBAS-TURKI"));
		assertEquals("I.", this.test.parseFirstName("I. EL KHADIRI"));
		assertEquals("I.", this.test.parseFirstName("I EL KHADIRI"));
		assertEquals("Y.", this.test.parseFirstName("Y. EL MERABET"));
		assertEquals("Y.", this.test.parseFirstName("Y EL MERABET"));
		assertEquals("I.", this.test.parseFirstName("I. EL-KHADIRI"));
		assertEquals("I.", this.test.parseFirstName("I EL-KHADIRI"));
		assertEquals("Y.", this.test.parseFirstName("Y. EL-MERABET"));
		assertEquals("Y.", this.test.parseFirstName("Y EL-MERABET"));
		assertEquals("A. S.", this.test.parseFirstName("A. S. MALIK"));
		assertEquals("A. S.", this.test.parseFirstName("A S MALIK"));
		assertEquals("T.", this.test.parseFirstName("T. MENDES DE FARIAS"));
		assertEquals("T.", this.test.parseFirstName("T MENDES DE FARIAS"));
		assertEquals("W. S. H.", this.test.parseFirstName("W.S.H. SYED"));
		assertEquals("W. S. H.", this.test.parseFirstName("W S H SYED"));
		assertEquals("W. S. H.", this.test.parseFirstName("W S. H SYED"));
		assertEquals("W. S. H.", this.test.parseFirstName("W. S H. SYED"));
		assertEquals("P.-J.", this.test.parseFirstName("P.-J. LAPRAY"));
		assertEquals("P.-J.", this.test.parseFirstName("P-J. LAPRAY"));
		assertEquals("P.-J.", this.test.parseFirstName("P.-J LAPRAY"));
		assertEquals("P.-J.", this.test.parseFirstName("P-J LAPRAY"));
		assertEquals("Jean-Michel", this.test.parseFirstName("Jean-Michel Dupont"));
		assertEquals("S.", this.test.parseFirstName("S.Galland"));
		assertEquals("S. R.", this.test.parseFirstName("S.R.Galland"));
	}

	@Test
	@DisplayName("parseLastName(null)")
	public void parseLastName_emptyInput() {
		assertNull(this.test.parseLastName(null));	
		assertNull(this.test.parseLastName(""));
		assertNull(this.test.parseLastName(" "));
	}

	@Test
	@DisplayName("parseLastName(\"First Last Last1\")")
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
	@DisplayName("parseLastName(\"Last Last1, First\")")
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
	@DisplayName("parseLastName(...)")
	public void parseLastName_specials() {
		assertEquals("Creput", this.test.parseLastName("J.C. Creput"));
		assertEquals("Creput", this.test.parseLastName("J C Creput"));
		assertEquals("Abbas-Turki", this.test.parseLastName("A. ABBAS-TURKI"));
		assertEquals("Abbas-Turki", this.test.parseLastName("A ABBAS-TURKI"));
		assertEquals("El Khadiri", this.test.parseLastName("I. EL KHADIRI"));
		assertEquals("El Khadiri", this.test.parseLastName("I EL KHADIRI"));
		assertEquals("El Merabet", this.test.parseLastName("Y. EL MERABET"));
		assertEquals("El Merabet", this.test.parseLastName("Y EL MERABET"));
		assertEquals("El-Khadiri", this.test.parseLastName("I. EL-KHADIRI"));
		assertEquals("El-Khadiri", this.test.parseLastName("I EL-KHADIRI"));
		assertEquals("El-Merabet", this.test.parseLastName("Y. EL-MERABET"));
		assertEquals("El-Merabet", this.test.parseLastName("Y EL-MERABET"));
		assertEquals("Malik", this.test.parseLastName("A. S. MALIK"));
		assertEquals("Malik", this.test.parseLastName("A S MALIK"));
		assertEquals("Mendes De Farias", this.test.parseLastName("T. MENDES DE FARIAS"));
		assertEquals("Mendes De Farias", this.test.parseLastName("T MENDES DE FARIAS"));
		assertEquals("Syed", this.test.parseLastName("W.S.H. SYED"));
		assertEquals("Syed", this.test.parseLastName("W S H SYED"));
		assertEquals("Syed", this.test.parseLastName("W S. H SYED"));
		assertEquals("Syed", this.test.parseLastName("W. S H. SYED"));
		assertEquals("Lapray", this.test.parseLastName("P.-J. LAPRAY"));
		assertEquals("Lapray", this.test.parseLastName("P-J. LAPRAY"));
		assertEquals("Lapray", this.test.parseLastName("P.-J LAPRAY"));
		assertEquals("Lapray", this.test.parseLastName("P-J LAPRAY"));
		assertEquals("Dupont", this.test.parseLastName("Jean-Michel Dupont"));
		assertEquals("Galland", this.test.parseLastName("S.Galland"));
		assertEquals("Galland", this.test.parseLastName("S.R.Galland"));
	}

	@Test
	@DisplayName("parseNames(null)")
	public void parseNames_null() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames(null, cb);
		assertEquals(0, n);
		verifyNoInteractions(cb);
	}

	@Test
	@DisplayName("parseNames(\"\")")
	public void parseNames_empty() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("", cb);
		assertEquals(0, n);
		verifyNoInteractions(cb);
	}

	@Test
	@DisplayName("parseNames(\"Last1, First1\")")
	public void parseNames_1name_format1() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Last1, First1", cb);
		assertEquals(1, n);
		verify(cb, only()).name(eq("First1"), isNull(), eq("Last1"), eq(0));
	}

	@Test
	@DisplayName("parseNames(\"Last1, Von1, First1\")")
	public void parseNames_1name_format1_von() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Last1, Von1, First1", cb);
		assertEquals(1, n);
		verify(cb, only()).name(eq("First1"), eq("Von1"), eq("Last1"), eq(0));
	}

	@Test
	@DisplayName("parseNames(\"First1 Last1\")")
	public void parseNames_1name_format2() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("First1 Last1", cb);
		assertEquals(1, n);
		verify(cb, only()).name(eq("First1"), isNull(), eq("Last1"), eq(0));
	}

	@Test
	@DisplayName("parseNames(\"Last1, First1 and Last2, First2\")")
	public void parseNames_2names_format1() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Last1, First1 and Last2, First2", cb);
		assertEquals(2, n);
		verify(cb).name(eq("First1"), isNull(), eq("Last1"), eq(0));
		verify(cb).name(eq("First2"), isNull(), eq("Last2"), eq(1));
	}

	@Test
	@DisplayName("parseNames(\"Last1, Von1, First1 and Last2, Von2, First2\")")
	public void parseNames_2names_format1_von0() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Last1, Von1, First1 and Last2, Von2, First2", cb);
		assertEquals(2, n);
		verify(cb).name(eq("First1"), eq("Von1"), eq("Last1"), eq(0));
		verify(cb).name(eq("First2"), eq("Von2"), eq("Last2"), eq(1));
	}

	@Test
	@DisplayName("parseNames(\"Last1, Von1, First1 and Last2, First2\")")
	public void parseNames_2names_format1_von1() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Last1, Von1, First1 and Last2, First2", cb);
		assertEquals(2, n);
		verify(cb).name(eq("First1"), eq("Von1"), eq("Last1"), eq(0));
		verify(cb).name(eq("First2"), isNull(), eq("Last2"), eq(1));
	}

	@Test
	@DisplayName("parseNames(\"Last1, First1 and Last2, Von2, First2\")")
	public void parseNames_2names_format1_von2() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Last1, First1 and Last2, Von2, First2", cb);
		assertEquals(2, n);
		verify(cb).name(eq("First1"), isNull(), eq("Last1"), eq(0));
		verify(cb).name(eq("First2"), eq("Von2"), eq("Last2"), eq(1));
	}

	@Test
	@DisplayName("parseNames(\"Last1, First1 and Last2, First2 and Last3, First3\")")
	public void parseNames_3names_format1() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Last1, First1 and Last2, First2 and Last3, First3", cb);
		assertEquals(3, n);
		verify(cb).name(eq("First1"), isNull(), eq("Last1"), eq(0));
		verify(cb).name(eq("First2"), isNull(), eq("Last2"), eq(1));
		verify(cb).name(eq("First3"), isNull(), eq("Last3"), eq(2));
	}

	@Test
	@DisplayName("parseNames(\"First1 Last1, First2 Last2, First3 Last3\")")
	public void parseNames_3names_format2_recognizedAsWithFormat2() {
		// The following names are recognized with the format 1 (AND-separator)
		// because it is assumed to be equivalent to "LAST, VON, FIRST".
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("First1 Last1 , First2  Last2, First3 Last3", cb);
		assertEquals(1, n);
		verify(cb).name(eq("First3 Last3"), eq("First2  Last2"), eq("First1 Last1"), eq(0));
	}

	@Test
	@DisplayName("parseNames(\"Last1, First1 and Last2, First2 and Last3, First3 and Last4, First4\")")
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
	@DisplayName("parseNames(\"First1 Last1, First2 Last2, First3 Last3, First4 Last4\")")
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
	@DisplayName("parseNames(\"Last1, First1 and Last2, First2 and Last3, First3 and Last4, First4 and Last5, First5\")")
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
	@DisplayName("parseNames(\"First1 Last1 , First2  Last2, First3 Last3, First4 Last4, First5 Last5\")")
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
	@DisplayName("parseNames(\"Last1, First1 and Last2, First2 and Last3, First3 and Last4, First4 and Last5, First5 and Last6, First 6\")")
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
	@DisplayName("parseNames(\"First1 Last1 , First2  Last2, First3 Last3, First4 Last4, First5 Last5, First6 Last 6\")")
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
	@DisplayName("parseNames(\"Smelik, Ruben and Galka, Krysztof and Kraker, de, Klaas Jan and Kuijper, Frido and Bidarra Rafael\")")
	public void parseNames_mixedFormat0() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Smelik, Ruben and Galka, Krysztof and Kraker, de, Klaas Jan and Kuijper, Frido and Bidarra Rafael", cb);
		assertEquals(5, n);
		verify(cb).name(eq("Ruben"), isNull(), eq("Smelik"), eq(0));
		verify(cb).name(eq("Krysztof"), isNull(), eq("Galka"), eq(1));
		verify(cb).name(eq("Klaas Jan"), eq("de"), eq("Kraker"), eq(2));
		verify(cb).name(eq("Frido"), isNull(), eq("Kuijper"), eq(3));
		verify(cb).name(eq("Bidarra"), isNull(), eq("Rafael"), eq(4));
	}

	@Test
	@DisplayName("parseNames(\"Smelik, Ruben and Tutenel, Tim and Kraker, de, Klaas Jan and Bidarra Rafael\")")
	public void parseNames_mixedFormat1() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Smelik, Ruben and Tutenel, Tim and Kraker, de, Klaas Jan and Bidarra Rafael", cb);
		assertEquals(4, n);
		verify(cb).name(eq("Ruben"), isNull(), eq("Smelik"), eq(0));
		verify(cb).name(eq("Tim"), isNull(), eq("Tutenel"), eq(1));
		verify(cb).name(eq("Klaas Jan"), eq("de"), eq("Kraker"), eq(2));
		verify(cb).name(eq("Bidarra"), isNull(), eq("Rafael"), eq(3));
	}

	@Test
	@DisplayName("parseNames(\"Tomas Vintr and Martin Rektoris and Jan Blaha and Jiri Ulrich and George Broughton and Zhi Yan and Tomas Krajník\")")
	public void parseNames_fromMember1() {
		NameCallback cb = mock(NameCallback.class);
		int n = this.test.parseNames("Tomáš Vintr and Martin Rektoris and Jan Blaha and Jiří Ulrich and George Broughton and Zhi Yan and Tomáš Krajník", cb);
		assertEquals(7, n);
		verify(cb).name(eq("Tomáš"), isNull(), eq("Vintr"), eq(0));
		verify(cb).name(eq("Martin"), isNull(), eq("Rektoris"), eq(1));
		verify(cb).name(eq("Jan"), isNull(), eq("Blaha"), eq(2));
		verify(cb).name(eq("Jiří"), isNull(), eq("Ulrich"), eq(3));
		verify(cb).name(eq("George"), isNull(), eq("Broughton"), eq(4));
		verify(cb).name(eq("Zhi"), isNull(), eq("Yan"), eq(5));
		verify(cb).name(eq("Tomáš"), isNull(), eq("Krajník"), eq(6));
	}

	@Test
	@DisplayName("normalizeName(null)")
	public void normalizeName_null() {
		assertNull(this.test.normalizeName(null));
	}

	@Test
	@DisplayName("normalizeName(\"\")")
	public void normalizeName_empty() {
		assertNull(this.test.normalizeName(""));
	}

	@Test
	@DisplayName("normalizeName(\"  \")")
	public void normalizeName_whitespaces() {
		assertNull(this.test.normalizeName("     "));
	}

	@Test
	@DisplayName("normalizeName(\" abc \")")
	public void normalizeName_0() {
		assertEquals("ABC", this.test.normalizeName(" abc   "));
	}

	@Test
	@DisplayName("normalizeName(\" aBc xyZ \")")
	public void normalizeName_1() {
		assertEquals("ABC XYZ", this.test.normalizeName(" aBc xyZ   "));
	}

	@Test
	@DisplayName("normalizeName(\" aBc éyZ \")")
	public void normalizeName_2() {
		assertEquals("ABC EYZ", this.test.normalizeName(" aBc éyZ   "));
	}

	@Test
	@DisplayName("normalizeName(\" aBc éyZ \")")
	public void normalizeName_3() {
		assertEquals("A EYZ", this.test.normalizeName(" a. éyZ   "));
	}

	@Test
	@DisplayName("normalizeName(\" j.-p. éyZ \")")
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
	@DisplayName("getNormalizedNamesFor(null) short no progress")
	public void getNormalizedNamesFor_null_withShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(null, true, false);
		assertTrue(actual.isEmpty());
	}

	@Test
	@DisplayName("getNormalizedNamesFor(null) short progress")
	public void getNormalizedNamesFor_null_withShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(null, true, true);
		assertTrue(actual.isEmpty());
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"\") short no progress")
	public void getNormalizedNamesFor_empty_withShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor("", true, false);
		assertTrue(actual.isEmpty());
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"\") short progress")
	public void getNormalizedNamesFor_empty_withShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor("", true, true);
		assertTrue(actual.isEmpty());
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\" \") short no progress")
	public void getNormalizedNamesFor_whitespaces_withShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor("    ", true, false);
		assertTrue(actual.isEmpty());
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\" \") short progress")
	public void getNormalizedNamesFor_whitespaces_withShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor("    ", true, true);
		assertTrue(actual.isEmpty());
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"Name\") short no progress")
	public void getNormalizedNamesFor_simpleName_withShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" Name ", true, false);
		assertEqualsSet(new String[] {"NAME", "N"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"Name\") short progress")
	public void getNormalizedNamesFor_simpleName_withShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" Name ", true, true);
		assertEqualsSet(new String[] {"NAME", "N"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"First Second\") short no progress")
	public void getNormalizedNamesFor_twoNames_withShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First   Second ", true, false);
		assertEqualsSet(new String[] {
				"FIRST SECOND",
				"F S", "FIRST S", "F SECOND"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"First Second\") short progress")
	public void getNormalizedNamesFor_twoNames_withShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First   Second ", true, true);
		assertEqualsSet(new String[] {
				"FIRST", "F",
				"FIRST SECOND",
				"F S", "FIRST S", "F SECOND"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"First Second Third\") short no progress")
	public void getNormalizedNamesFor_threeNames_withShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First   Second   Third ", true, false);
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
	@DisplayName("getNormalizedNamesFor(\"First Second Third\") short progress")
	public void getNormalizedNamesFor_threeNames_withShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First   Second   Third ", true, true);
		assertEqualsSet(new String[] {
				"FIRST", "F",
				"FIRST SECOND", "FIRST THIRD",
				"F SECOND",
				"FIRST S", "FIRST T",
				"F S", "F T",
				"F THIRD",
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
	@DisplayName("getNormalizedNamesFor(\"N\") short no progress")
	public void getNormalizedNamesFor_initial0_withShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" N ", true, false);
		assertEqualsSet(new String[] {"N"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"N\") short progress")
	public void getNormalizedNamesFor_initial0_withShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" N ", true, true);
		assertEqualsSet(new String[] {"N"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"N.\") short no progress")
	public void getNormalizedNamesFor_initial1_withShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" N. ", true, false);
		assertEqualsSet(new String[] {"N"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"N.\") short progress")
	public void getNormalizedNamesFor_initial1_withShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" N. ", true, true);
		assertEqualsSet(new String[] {"N"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"First-Second\") short no progress")
	public void getNormalizedNamesFor_composed0_withShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First -  Second ", true, false);
		assertEqualsSet(new String[] {
				"FIRST SECOND",
				"F S",
				"F SECOND",
				"FIRST S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"First-Second\") short progress")
	public void getNormalizedNamesFor_composed0_withShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First -  Second ", true, true);
		assertEqualsSet(new String[] {
				"FIRST", "F",
				"FIRST SECOND",
				"F S",
				"F SECOND",
				"FIRST S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F.-Second\") short no progress")
	public void getNormalizedNamesFor_composed1_withShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F. -  Second ", true, false);
		assertEqualsSet(new String[] {
				"F S",
				"F SECOND"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F.-Second\") short progress")
	public void getNormalizedNamesFor_composed1_withShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F. -  Second ", true, true);
		assertEqualsSet(new String[] {
				"F",
				"F S",
				"F SECOND"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F.-S.\") short no progress")
	public void getNormalizedNamesFor_composed2_withShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F. -  S. ", true, false);
		assertEqualsSet(new String[] {
				"F S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F.-S.\") short progress")
	public void getNormalizedNamesFor_composed2_withShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F. -  S. ", true, true);
		assertEqualsSet(new String[] {
				"F", "F S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"First-S.\") short no progress")
	public void getNormalizedNamesFor_composed3_withShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First -  S. ", true, false);
		assertEqualsSet(new String[] {
				"F S",
				"FIRST S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"First-S.\") short progress")
	public void getNormalizedNamesFor_composed3_withShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First -  S. ", true, true);
		assertEqualsSet(new String[] {
				"FIRST",
				"F",
				"F S",
				"FIRST S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F-Second\") short no progress")
	public void getNormalizedNamesFor_composed4_withShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F -  Second ", true, false);
		assertEqualsSet(new String[] {
				"F S",
				"F SECOND"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F-Second\") short progress")
	public void getNormalizedNamesFor_composed4_withShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F -  Second ", true, true);
		assertEqualsSet(new String[] {
				"F",
				"F S",
				"F SECOND"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F.-S\") short no progress")
	public void getNormalizedNamesFor_composed5_withShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F. -  S ", true, false);
		assertEqualsSet(new String[] {
				"F S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F.-S\") short progress")
	public void getNormalizedNamesFor_composed5_withShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F. -  S ", true, true);
		assertEqualsSet(new String[] {
				"F",
				"F S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F-S\") short no progress")
	public void getNormalizedNamesFor_composed6_withShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F -  S ", true, false);
		assertEqualsSet(new String[] {
				"F S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F-S\") short progress")
	public void getNormalizedNamesFor_composed6_withShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F -  S ", true, true);
		assertEqualsSet(new String[] {
				"F",
				"F S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F-S.\") short no progress")
	public void getNormalizedNamesFor_composed7_withShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F -  S. ", true, false);
		assertEqualsSet(new String[] {
				"F S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F-S.\") short progress")
	public void getNormalizedNamesFor_composed7_withShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F -  S. ", true, true);
		assertEqualsSet(new String[] {
				"F",
				"F S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"First-S\") short no progress")
	public void getNormalizedNamesFor_composed8_withShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First -  S ", true, false);
		assertEqualsSet(new String[] {
				"F S",
				"FIRST S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"First-S\") short progress")
	public void getNormalizedNamesFor_composed8_withShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First -  S ", true, true);
		assertEqualsSet(new String[] {
				"FIRST",
				"F",
				"F S",
				"FIRST S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(null) no short no progress")
	public void getNormalizedNamesFor_null_withoutShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(null, false, false);
		assertTrue(actual.isEmpty());
	}

	@Test
	@DisplayName("getNormalizedNamesFor(null) no short progress")
	public void getNormalizedNamesFor_null_withoutShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(null, false, true);
		assertTrue(actual.isEmpty());
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"\") no short no progress")
	public void getNormalizedNamesFor_empty_withoutShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor("", false, false);
		assertTrue(actual.isEmpty());
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"\") no short progress")
	public void getNormalizedNamesFor_empty_withoutShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor("", false, true);
		assertTrue(actual.isEmpty());
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\" \") no short no progress")
	public void getNormalizedNamesFor_whitespaces_withoutShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor("    ", false, false);
		assertTrue(actual.isEmpty());
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\" \") no short progress")
	public void getNormalizedNamesFor_whitespaces_withoutShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor("    ", false, true);
		assertTrue(actual.isEmpty());
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"Name\") no short no progress")
	public void getNormalizedNamesFor_simpleName_withoutShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" Name ", false, false);
		assertEqualsSet(new String[] {"NAME"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"Name\") no short progress")
	public void getNormalizedNamesFor_simpleName_withoutShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" Name ", false, true);
		assertEqualsSet(new String[] {"NAME"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"First Second\") no short no progress")
	public void getNormalizedNamesFor_twoNames_withoutShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First   Second ", false, false);
		assertEqualsSet(new String[] {
				"FIRST SECOND"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"First Second\") no short progress")
	public void getNormalizedNamesFor_twoNames_withoutShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First   Second ", false, true);
		assertEqualsSet(new String[] {
				"FIRST",
				"FIRST SECOND"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"First Second Third\") no short no progress")
	public void getNormalizedNamesFor_threeNames_withoutShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First   Second   Third ", false, false);
		assertEqualsSet(new String[] {
				"FIRST SECOND THIRD"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"First Second Third\") no short progress")
	public void getNormalizedNamesFor_threeNames_withoutShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First   Second   Third ", false, true);
		assertEqualsSet(new String[] {
				"FIRST",
				"FIRST SECOND",
				"FIRST THIRD",
				"FIRST SECOND THIRD"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"N\") no short no progress")
	public void getNormalizedNamesFor_initial0_withoutShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" N ", false, false);
		assertEqualsSet(new String[] {"N"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"N\") no short progress")
	public void getNormalizedNamesFor_initial0_withoutShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" N ", false, true);
		assertEqualsSet(new String[] {"N"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"N.\") no short no progress")
	public void getNormalizedNamesFor_initial1_withoutShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" N. ", false, false);
		assertEqualsSet(new String[] {"N"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"N.\") no short progress")
	public void getNormalizedNamesFor_initial1_withoutShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" N. ", false, true);
		assertEqualsSet(new String[] {"N"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"First-Second\") no short no progress")
	public void getNormalizedNamesFor_composed0_withoutShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First -  Second ", false, false);
		assertEqualsSet(new String[] {
				"FIRST SECOND"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"First-Second\") no short progress")
	public void getNormalizedNamesFor_composed0_withoutShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First -  Second ", false, true);
		assertEqualsSet(new String[] {
				"FIRST",
				"FIRST SECOND"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F.-Second\") no short no progress")
	public void getNormalizedNamesFor_composed1_withoutShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F. -  Second ", false, false);
		assertEqualsSet(new String[] {
				"F SECOND"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F.-Second\") no short progress")
	public void getNormalizedNamesFor_composed1_withoutShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F. -  Second ", false, true);
		assertEqualsSet(new String[] {
				"F",
				"F SECOND"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F.-S.\") no short no progress")
	public void getNormalizedNamesFor_composed2_withoutShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F. -  S. ", false, false);
		assertEqualsSet(new String[] {
				"F S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F.-S.\") no short progress")
	public void getNormalizedNamesFor_composed2_withoutShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F. -  S. ", false, true);
		assertEqualsSet(new String[] {
				"F",
				"F S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"First-S.\") no short no progress")
	public void getNormalizedNamesFor_composed3_withoutShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First -  S. ", false, false);
		assertEqualsSet(new String[] {
				"FIRST S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"First-S.\") no short progress")
	public void getNormalizedNamesFor_composed3_withoutShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First -  S. ", false, true);
		assertEqualsSet(new String[] {
				"FIRST",
				"FIRST S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F-Second\") no short no progress")
	public void getNormalizedNamesFor_composed4_withoutShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F -  Second ", false, false);
		assertEqualsSet(new String[] {
				"F SECOND"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F-Second\") no short progress")
	public void getNormalizedNamesFor_composed4_withoutShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F -  Second ", false, true);
		assertEqualsSet(new String[] {
				"F",
				"F SECOND"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F.-S\") no short no progress")
	public void getNormalizedNamesFor_composed5_withoutShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F. -  S ", false, false);
		assertEqualsSet(new String[] {
				"F S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F.-S\") no short progress")
	public void getNormalizedNamesFor_composed5_withoutShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F. -  S ", false, true);
		assertEqualsSet(new String[] {
				"F",
				"F S"}, actual);
	}
	
	@Test
	@DisplayName("getNormalizedNamesFor(\"F-S\") no short no progress")
	public void getNormalizedNamesFor_composed6_withoutShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F -  S ", false, false);
		assertEqualsSet(new String[] {
				"F S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F-S\") no short progress")
	public void getNormalizedNamesFor_composed6_withoutShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F -  S ", false, true);
		assertEqualsSet(new String[] {
				"F",
				"F S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F-S.\") no short no progress")
	public void getNormalizedNamesFor_composed7_withoutShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F -  S. ", false, false);
		assertEqualsSet(new String[] {
				"F S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"F-S.\") no short progress")
	public void getNormalizedNamesFor_composed7_withoutShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" F -  S. ", false, true);
		assertEqualsSet(new String[] {
				"F",
				"F S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"First-S\") no short no progress")
	public void getNormalizedNamesFor_composed8_withoutShortNames_0() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First -  S ", false, false);
		assertEqualsSet(new String[] {
				"FIRST S"}, actual);
	}

	@Test
	@DisplayName("getNormalizedNamesFor(\"First-S\") no short progress")
	public void getNormalizedNamesFor_composed8_withoutShortNames_1() {
		Set<String> actual = this.test.getNormalizedNamesFor(" First -  S ", false, true);
		assertEqualsSet(new String[] {
				"FIRST",
				"FIRST S"}, actual);
	}

	@Test
	@DisplayName("isShortName(null)")
	public void isShortName_null() {
		assertFalse(this.test.isShortName(null));
	}

	@Test
	@DisplayName("isShortName(\"\")")
	public void isShortName_empty() {
		assertFalse(this.test.isShortName(""));
	}

	@Test
	@DisplayName("isShortName(\" \")")
	public void isShortName_whitespaces() {
		assertFalse(this.test.isShortName("    "));
	}

	@Test
	@DisplayName("isShortName(\"S\")")
	public void isShortName_initial_0() {
		assertTrue(this.test.isShortName("  S  "));
	}

	@Test
	@DisplayName("isShortName(\"S.\")")
	public void isShortName_initial_1() {
		assertTrue(this.test.isShortName("  S.  "));
	}

	@Test
	@DisplayName("isShortName(\"S R\")")
	public void isShortName_initials_0() {
		assertTrue(this.test.isShortName("  S  R  "));
	}

	@Test
	@DisplayName("isShortName(\"S R.\")")
	public void isShortName_initials_1() {
		assertTrue(this.test.isShortName("  S  R.  "));
	}

	@Test
	@DisplayName("isShortName(\"S. R.\")")
	public void isShortName_initials_2() {
		assertTrue(this.test.isShortName("  S.  R.  "));
	}

	@Test
	@DisplayName("isShortName(\"S. R\")")
	public void isShortName_initials_3() {
		assertTrue(this.test.isShortName("  S.  R  "));
	}

	@Test
	@DisplayName("isShortName(\"S R G\")")
	public void isShortName_initials_4() {
		assertTrue(this.test.isShortName("  S  R G "));
	}

	@Test
	@DisplayName("isShortName(\"S R. G\")")
	public void isShortName_initials_5() {
		assertTrue(this.test.isShortName("  S  R. G "));
	}

	@Test
	@DisplayName("isShortName(\"S. R. G\")")
	public void isShortName_initials_6() {
		assertTrue(this.test.isShortName("  S.  R.G  "));
	}

	@Test
	@DisplayName("isShortName(\"S. R G\")")
	public void isShortName_initials_7() {
		assertTrue(this.test.isShortName("  S.  R  G"));
	}

	@Test
	@DisplayName("isShortName(\"S-R\")")
	public void isShortName_initials_8() {
		assertTrue(this.test.isShortName("  S-R  "));
	}

	@Test
	@DisplayName("isShortName(\"S-R.\")")
	public void isShortName_initials_9() {
		assertTrue(this.test.isShortName("  S-R.  "));
	}

	@Test
	@DisplayName("isShortName(\"S.-R.\")")
	public void isShortName_initials_10() {
		assertTrue(this.test.isShortName("  S.-R.  "));
	}

	@Test
	@DisplayName("isShortName(\"S.-R\")")
	public void isShortName_initials_11() {
		assertTrue(this.test.isShortName("  S.-R  "));
	}

	@Test
	@DisplayName("isShortName(\"S-R-G\")")
	public void isShortName_initials_12() {
		assertTrue(this.test.isShortName("  S-R-G "));
	}

	@Test
	@DisplayName("isShortName(\"S-R.-G\")")
	public void isShortName_initials_13() {
		assertTrue(this.test.isShortName("  S-R.-G "));
	}

	@Test
	@DisplayName("isShortName(\"S.-R.G\")")
	public void isShortName_initials_14() {
		assertTrue(this.test.isShortName("  S.-  R.G  "));
	}

	@Test
	@DisplayName("isShortName(\"S.-R-G\")")
	public void isShortName_initials_15() {
		assertTrue(this.test.isShortName("  S. -R -G"));
	}

	@Test
	@DisplayName("isShortName(\"Stephane R\")")
	public void isShortName_name_0() {
		assertFalse(this.test.isShortName("  Stephane  R"));
	}

	@Test
	@DisplayName("isShortName(\"R Stephane\")")
	public void isShortName_name_1() {
		assertFalse(this.test.isShortName("  R Stephane "));
	}

	@Test
	@DisplayName("isShortName(\"Stephane Stephane\")")
	public void isShortName_name_2() {
		assertFalse(this.test.isShortName("  Stephane Stephane "));
	}

	@Test
	@DisplayName("isShortName(\"Stephane-R\")")
	public void isShortName_name_3() {
		assertFalse(this.test.isShortName("  Stephane-R"));
	}

	@Test
	@DisplayName("isShortName(\"R-Stephane\")")
	public void isShortName_name_4() {
		assertFalse(this.test.isShortName("  R-Stephane "));
	}

	@Test
	@DisplayName("isShortName(\"Stephane-Stephane\")")
	public void isShortName_name_5() {
		assertFalse(this.test.isShortName("  Stephane-Stephane "));
	}

	@Test
	@DisplayName("formatNameForDisplay(null)")
	public void formatNameForDisplay_null() {
		assertNull(this.test.formatNameForDisplay(null));
	}

	@Test
	@DisplayName("formatNameForDisplay(\"\")")
	public void formatNameForDisplay_empty() {
		assertEquals("", this.test.formatNameForDisplay(""));
	}

	@Test
	@DisplayName("formatNameForDisplay(\" \")")
	public void formatNameForDisplay_whitespaces() {
		assertEquals("", this.test.formatNameForDisplay("     "));
	}

	@Test
	@DisplayName("formatNameForDisplay(\"abc\")")
	public void formatNameForDisplay_0() {
		assertEquals("Abc", this.test.formatNameForDisplay(" abc   "));
	}

	@Test
	@DisplayName("formatNameForDisplay(\"aBc xyZ\")")
	public void formatNameForDisplay_1() {
		assertEquals("Abc Xyz", this.test.formatNameForDisplay(" aBc xyZ   "));
	}

	@Test
	@DisplayName("formatNameForDisplay(\"aBc éyZ\")")
	public void formatNameForDisplay_2() {
		assertEquals("Abc \u00C9yz", this.test.formatNameForDisplay(" aBc éyZ   "));
	}

	@Test
	@DisplayName("formatNameForDisplay(\"a. éyZ\")")
	public void formatNameForDisplay_3() {
		assertEquals("A. \u00C9yz", this.test.formatNameForDisplay(" a. éyZ   "));
	}

	@Test
	@DisplayName("formatNameForDisplay(\"j.-p. éyZ\")")
	public void formatNameForDisplay_4() {
		assertEquals("J.-P. \u00C9yz", this.test.formatNameForDisplay(" j.-p. éyZ   "));
	}

	@Test
	@DisplayName("formatNameForDisplay(\"j.p.éyZ\")")
	public void formatNameForDisplay_5() {
		assertEquals("J.P.\u00C9yz", this.test.formatNameForDisplay(" j.p.éyZ   "));
	}

}
