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

package fr.ciadlab.labmanager.io.bibtex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.stream.Stream;

import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link AbstractBibtex}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class AbstractBibTeXTest {

	private AbstractBibTeX test;

	@BeforeEach
	public void setUp() {
		this.test = new AbstractBibTeX() {

			@Override
			public String parseTeXString(String texString) throws Exception {
				throw new UnsupportedOperationException();
			}

			@Override
			public Stream<Publication> getPublicationStreamFrom(Reader bibtex, boolean keepBibTeXId,
					boolean assignRandomId, boolean ensureAtLeastOneMember, boolean createMissedJournal,
					boolean createMissedConference) throws Exception {
				throw new UnsupportedOperationException();
			}

			@Override
			public void exportPublications(Writer output, Iterable<? extends Publication> publications,
					ExporterConfigurator configurator) throws IOException {
				throw new UnsupportedOperationException();
			}
			
		};
	}

	@Test
	public void toTeXString_null() {
		assertEquals("", this.test.toTeXString(null));
	}

	@Test
	public void toTeXString_empty() {
		assertEquals("", this.test.toTeXString(""));
	}

	@Test
	public void toTeXString_ascii() {
		assertEquals("abcdef", this.test.toTeXString("abcdef"));
	}

	@Test
	public void toTeXString_graveaccent_lowercase() {
		assertEquals("{\\`{a}}{\\`{\\ae}}{\\`{e}}{\\`{h}}{\\`{\\i}}{\\`{k}}{\\`{m}}{\\`{n}}{\\`{o}}{\\`{r}}{\\`{s}}{\\`{t}}{\\`{u}}{\\`{v}}{\\`{w}}{\\`{x}}{\\`{y}}{\\`{z}}",
				this.test.toTeXString("àæ̀èh̀ìk̀m̀ǹòr̀s̀t̀ùv̀ẁx̀ỳz̀"));
	}

	@Test
	public void toTeXString_graveaccent_uppercase() {
		assertEquals("{\\`{A}}{\\`{\\AE}}{\\`{E}}{\\`{H}}{\\`{I}}{\\`{K}}{\\`{M}}{\\`{N}}{\\`{O}}{\\`{R}}{\\`{S}}{\\`{T}}{\\`{U}}{\\`{V}}{\\`{W}}{\\`{X}}{\\`{Y}}{\\`{Z}}",
				this.test.toTeXString("ÀÆ̀ÈH̀ÌK̀M̀ǸÒR̀S̀T̀ÙV̀ẀX̀ỲZ̀"));
	}

	@Test
	public void toTeXString_acuteaccent_lowercase() {
		assertEquals("{\\'{a}}{\\'{\\ae}}{\\'{b}}{\\'{c}}{\\'{d}}{\\'{e}}{\\'{f}}{\\'{g}}{\\'{h}}{\\'{\\i}}{\\'{\\j}}{\\'{k}}{\\'{l}}{\\'{m}}{\\'{n}}{\\'{o}}{\\'{p}}{\\'{q}}{\\'{r}}{\\'{s}}{\\'{t}}{\\'{u}}{\\'{v}}{\\'{w}}{\\'{x}}{\\'{y}}{\\'{z}}",
				this.test.toTeXString("áǽb́ćd́éf́ǵh́íȷ́ḱĺḿńóṕq́ŕśt́úv́ẃx́ýź"));
	}

	@Test
	public void toTeXString_acuteaccent_uppercase() {
		assertEquals("{\\'{A}}{\\'{\\AE}}{\\'{B}}{\\'{C}}{\\'{D}}{\\'{E}}{\\'{F}}{\\'{G}}{\\'{H}}{\\'{I}}{\\'{J}}{\\'{K}}{\\'{L}}{\\'{M}}{\\'{N}}{\\'{O}}{\\'{P}}{\\'{Q}}{\\'{R}}{\\'{S}}{\\'{T}}{\\'{U}}{\\'{V}}{\\'{W}}{\\'{X}}{\\'{Y}}{\\'{Z}}",
				this.test.toTeXString("ÁǼB́ĆD́ÉF́ǴH́ÍJ́ḰĹḾŃÓṔQ́ŔŚT́ÚV́ẂX́ÝŹ"));
	}

	@Test
	public void toTeXString_cedil_lowercase() {
		assertEquals("{\\c{c}}{\\c{C}}", this.test.toTeXString("çÇ"));
	}

	@Test
	public void toTeXString_umlaccent_lowercase() {
		assertEquals("{\\\"{a}}{\\\"{e}}{\\\"{\\i}}{\\\"{o}}{\\\"{u}}",
				this.test.toTeXString("äëïöü"));
	}

	@Test
	public void toTeXString_umlaccent_uppercase() {
		assertEquals("{\\\"{A}}{\\\"{E}}{\\\"{I}}{\\\"{O}}{\\\"{U}}",
				this.test.toTeXString("ÄËÏÖÜ"));
	}

	@Test
	public void toTeXString_circaccent_lowercase() {
		assertEquals("{\\^{a}}{\\^{c}}{\\^{e}}{\\^{g}}{\\^{h}}{\\^{\\i}}{\\^{\\j}}{\\^{o}}{\\^{s}}{\\^{u}}{\\^{w}}{\\^{y}}",
				this.test.toTeXString("âĉêĝĥîĵôŝûŵŷ"));
	}

	@Test
	public void toTeXString_circaccent_uppercase() {
		assertEquals("{\\^{A}}{\\^{C}}{\\^{E}}{\\^{G}}{\\^{H}}{\\^{I}}{\\^{J}}{\\^{O}}{\\^{S}}{\\^{U}}{\\^{W}}{\\^{Y}}",
				this.test.toTeXString("ÂĈÊĜĤÎĴÔŜÛŴŶ"));
	}

	@Test
	public void toTeXString_ligature() {
		assertEquals("fffm", this.test.toTeXString("ﬀ㎙"));
	}

	@Test
	public void toTeXString_email() {
		assertEquals("prenom.nom@institution.fr", this.test.toTeXString("prenom.nom@institution.fr"));
	}

	@Test
	public void toTeXString_specialChars() {
		assertEquals("a\\{b\\}cd\\%e{\\textbackslash}f", this.test.toTeXString("a{b}cd%e\\f"));
	}

	@Test
	public void toTeXString_acronym_null() {
		assertEquals("", this.test.toTeXString(null, true));
	}

	@Test
	public void toTeXString_acronym_empty() {
		assertEquals("", this.test.toTeXString("", true));
	}

	@Test
	public void toTeXString_acronym_withoutAcronym() {
		assertEquals("this is a title without acronym", this.test.toTeXString("this is a title without acronym", true));
	}

	@Test
	public void toTeXString_acronym_withAcronym_01() {
		assertEquals("this is a {TITLE} with acronym", this.test.toTeXString("this is a TITLE with acronym", true));
	}

	@Test
	public void toTeXString_acronym_withAcronym_02() {
		assertEquals("this is a Title with acronym", this.test.toTeXString("this is a Title with acronym", true));
	}

	@Test
	public void toTeXString_acronym_withAcronym_03() {
		assertEquals("this is a {TiTLE} with acronym", this.test.toTeXString("this is a TiTLE with acronym", true));
	}

	@Test
	public void toTeXString_acronym_withAccentAcronym_03() {
		assertEquals("this is a {pr{\\'{E}}v} with acronym", this.test.toTeXString("this is a prÉv with acronym", true));
	}

}
