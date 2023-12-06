/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the CIAD laboratory and the Universite de Technologie
 * de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD-UTBM.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.entities.conference;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.entities.conference.ConferenceUtils.ConferenceNameComponents;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests for {@link ConferenceUtils}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.8
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class ConferenceUtilsTest {

	@Test
	@DisplayName("parseConferenceName(null)")
	public void parseConferenceName_null() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName(null);
		assertNotNull(components);
		assertEquals(0, components.occurrenceNumber);
		assertNull(components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"\")")
	public void parseConferenceName_empty_01() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("");
		assertNotNull(components);
		assertEquals(0, components.occurrenceNumber);
		assertNull(components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"-\")")
	public void parseConferenceName_empty_02() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("-");
		assertNotNull(components);
		assertEquals(0, components.occurrenceNumber);
		assertNull(components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"----\")")
	public void parseConferenceName_empty_03() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("----");
		assertNotNull(components);
		assertEquals(0, components.occurrenceNumber);
		assertNull(components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"   \")")
	public void parseConferenceName_spaces() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("   ");
		assertNotNull(components);
		assertEquals(0, components.occurrenceNumber);
		assertNull(components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"International Conference...\")")
	public void parseConferenceName_withoutNumber() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(0, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"14 International Conference...\")")
	public void parseConferenceName_withoutPostfix() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("14 International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(14, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"14th International Conference...\")")
	public void parseConferenceName_withPostfix_0() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("14th International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(14, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"14 th International Conference...\")")
	public void parseConferenceName_withPostfix_1() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("14 th International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(14, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"1ST International Conference...\")")
	public void parseConferenceName_withPostfix_2() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("1ST International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(1, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"1 st International Conference...\")")
	public void parseConferenceName_withPostfix_3() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("1 st International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(1, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"2nD International Conference...\")")
	public void parseConferenceName_withPostfix_4() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("2nD International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(2, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"2 nd International Conference...\")")
	public void parseConferenceName_withPostfix_5() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("2 nd International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(2, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"3RD International Conference...\")")
	public void parseConferenceName_withPostfix_6() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("3RD International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(3, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"3 rd International Conference...\")")
	public void parseConferenceName_withPostfix_7() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("3 rd International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(3, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"4TH International Conference...\")")
	public void parseConferenceName_withPostfix_8() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("4TH International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(4, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"4 th International Conference...\")")
	public void parseConferenceName_withPostfix_9() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("4 th International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(4, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"1ère International Conference...\")")
	public void parseConferenceName_withPostfix_10() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("1ère International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(1, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"1 ère International Conference...\")")
	public void parseConferenceName_withPostfix_11() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("1 ère International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(1, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"1ere International Conference...\")")
	public void parseConferenceName_withPostfix_12() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("1ere International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(1, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"1 ere International Conference...\")")
	public void parseConferenceName_withPostfix_13() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("1 ere International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(1, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"1er International Conference...\")")
	public void parseConferenceName_withPostfix_14() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("1er International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(1, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"1 er International Conference...\")")
	public void parseConferenceName_withPostfix_15() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("1 er International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(1, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"125ème International Conference...\")")
	public void parseConferenceName_withPostfix_16() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("125ème International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(125, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"125 ème International Conference...\")")
	public void parseConferenceName_withPostfix_17() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("125 ème International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(125, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"125eme International Conference...\")")
	public void parseConferenceName_withPostfix_18() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("125eme International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(125, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("parseConferenceName(\"125 eme International Conference...\")")
	public void parseConferenceName_withPostfix_19() {
		ConferenceNameComponents components = ConferenceUtils.parseConferenceName("125 eme International Conference on Articial Intelligence");
		assertNotNull(components);
		assertEquals(125, components.occurrenceNumber);
		assertEquals("International Conference on Articial Intelligence", components.name);
	}

	@Test
	@DisplayName("normalizeString(null)")
	public void normalizeString_null() {
		assertNull(ConferenceUtils.normalizeString(null));
	}

	@Test
	@DisplayName("normalizeString(\"\")")
	public void normalizeString_empty_01() {
		assertNull(ConferenceUtils.normalizeString(""));
	}

	@Test
	@DisplayName("normalizeString(\"-\")")
	public void normalizeString_empty_02() {
		assertNull(ConferenceUtils.normalizeString("-"));
	}

	@Test
	@DisplayName("normalizeString(\"----\")")
	public void normalizeString_empty_03() {
		assertNull(ConferenceUtils.normalizeString("----"));
	}

	@Test
	@DisplayName("normalizeString(\"   \")")
	public void normalizeString_spaces() {
		assertNull(ConferenceUtils.normalizeString("   "));
	}

	@Test
	@DisplayName("normalizeString(\"International Conference...\")")
	public void normalizeString_withoutNumber() {
		String expected = "International Conference on Articial Intelligence";
		String actual = ConferenceUtils.normalizeString(expected);
		assertSame(expected, actual);
	}

	@Test
	@DisplayName("normalizeString(\"14 International Conference...\")")
	public void normalizeString_withNumber() {
		String expected = "14 International Conference on Articial Intelligence";
		String actual = ConferenceUtils.normalizeString(expected);
		assertSame(expected, actual);
	}

	@Test
	@DisplayName("normalizeString(\"14th International Conference...\")")
	public void normalizeString_withoutPrefix() {
		String expected = "14th International Conference on Articial Intelligence";
		String actual = ConferenceUtils.normalizeString(expected);
		assertSame(expected, actual);
	}

	@Test
	@DisplayName("removePrefixArticles(null)")
	public void removePrefixArticles_null() {
		assertNull(ConferenceUtils.removePrefixArticles(null));
	}

	@Test
	@DisplayName("removePrefixArticles(\"\")")
	public void removePrefixArticles_empty() {
		assertNull(ConferenceUtils.removePrefixArticles(""));
	}

	@Test
	@DisplayName("removePrefixArticles(\"   \")")
	public void removePrefixArticles_spaces() {
		assertNull(ConferenceUtils.removePrefixArticles("   "));
	}

	@Test
	@DisplayName("removePrefixArticles(\" thing \")")
	public void removePrefixArticles_noPrefix() {
		assertEquals("thing", ConferenceUtils.removePrefixArticles(" thing "));
	}

	@Test
	@DisplayName("removePrefixArticles(\" The  thing \")")
	public void removePrefixArticles_the() {
		assertEquals("thing", ConferenceUtils.removePrefixArticles(" The  thing "));
	}

	@Test
	@DisplayName("removePrefixArticles(\" A  thing \")")
	public void removePrefixArticles_a() {
		assertEquals("thing", ConferenceUtils.removePrefixArticles(" A  thing "));
	}

	@Test
	@DisplayName("removePrefixArticles(\" An  thing \")")
	public void removePrefixArticles_an() {
		assertEquals("thing", ConferenceUtils.removePrefixArticles(" An  thing "));
	}

	@Test
	@DisplayName("removePrefixArticles(\" Le  truc \")")
	public void removePrefixArticles_le() {
		assertEquals("truc", ConferenceUtils.removePrefixArticles(" The  truc "));
	}

	@Test
	@DisplayName("removePrefixArticles(\" La  chose \")")
	public void removePrefixArticles_la() {
		assertEquals("chose", ConferenceUtils.removePrefixArticles(" La  chose "));
	}

	@Test
	@DisplayName("removePrefixArticles(\" L' outil \")")
	public void removePrefixArticles_l() {
		assertEquals("outil", ConferenceUtils.removePrefixArticles(" L' outil "));
	}

	@Test
	@DisplayName("removePrefixArticles(\" Les  choses \")")
	public void removePrefixArticles_les() {
		assertEquals("choses", ConferenceUtils.removePrefixArticles(" Les  choses "));
	}

	@Test
	@DisplayName("removePrefixArticles(\" Un  truc \")")
	public void removePrefixArticles_un() {
		assertEquals("truc", ConferenceUtils.removePrefixArticles(" Un  truc "));
	}

	@Test
	@DisplayName("removePrefixArticles(\" Une  chose \")")
	public void removePrefixArticles_une() {
		assertEquals("chose", ConferenceUtils.removePrefixArticles(" Une chose "));
	}

	@Test
	@DisplayName("removePrefixArticles(\" Der  Abc \")")
	public void removePrefixArticles_der() {
		assertEquals("Abc", ConferenceUtils.removePrefixArticles(" Der  Abc "));
	}


	@Test
	@DisplayName("removePrefixArticles(\" Das  Abc \")")
	public void removePrefixArticles_das() {
		assertEquals("Abc", ConferenceUtils.removePrefixArticles(" Das  Abc "));
	}


	@Test
	@DisplayName("removePrefixArticles(\" Die  Abc \")")
	public void removePrefixArticles_die() {
		assertEquals("Abc", ConferenceUtils.removePrefixArticles(" Die  Abc "));
	}

	@Test
	@DisplayName("removePrefixArticles(\" Ein  Abc \")")
	public void removePrefixArticles_ein() {
		assertEquals("Abc", ConferenceUtils.removePrefixArticles(" Ein  Abc "));
	}

	@Test
	@DisplayName("removePrefixArticles(\" El  Abc \")")
	public void removePrefixArticles_el() {
		assertEquals("Abc", ConferenceUtils.removePrefixArticles(" El  Abc "));
	}

	@Test
	@DisplayName("removePrefixArticles(\" Las  Abc \")")
	public void removePrefixArticles_las() {
		assertEquals("Abc", ConferenceUtils.removePrefixArticles(" Las  Abc "));
	}

	@Test
	@DisplayName("removePrefixArticles(\" Los  Abc \")")
	public void removePrefixArticles_los() {
		assertEquals("Abc", ConferenceUtils.removePrefixArticles(" Los  Abc "));
	}

	@Test
	@DisplayName("removePrefixArticles(\" Una  Abc \")")
	public void removePrefixArticles_una() {
		assertEquals("Abc", ConferenceUtils.removePrefixArticles(" Una  Abc "));
	}

}
