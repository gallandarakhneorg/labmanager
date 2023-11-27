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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.ciadlab.labmanager.configuration.BaseMessageSource;
import fr.ciadlab.labmanager.entities.conference.Conference;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.ConferenceBasedPublication;
import fr.ciadlab.labmanager.entities.publication.JournalBasedPublication;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationCategory;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.publication.type.Book;
import fr.ciadlab.labmanager.entities.publication.type.BookChapter;
import fr.ciadlab.labmanager.entities.publication.type.ConferencePaper;
import fr.ciadlab.labmanager.entities.publication.type.JournalEdition;
import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import fr.ciadlab.labmanager.entities.publication.type.KeyNote;
import fr.ciadlab.labmanager.entities.publication.type.MiscDocument;
import fr.ciadlab.labmanager.entities.publication.type.Patent;
import fr.ciadlab.labmanager.entities.publication.type.Report;
import fr.ciadlab.labmanager.entities.publication.type.Thesis;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.io.bibtex.JBibtexBibTeX.ConferenceNameComponents;
import fr.ciadlab.labmanager.io.bibtex.bugfix.BugfixLaTeXPrinter;
import fr.ciadlab.labmanager.service.conference.ConferenceService;
import fr.ciadlab.labmanager.service.journal.JournalService;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.publication.PrePublicationFactory;
import fr.ciadlab.labmanager.service.publication.type.BookChapterService;
import fr.ciadlab.labmanager.service.publication.type.BookService;
import fr.ciadlab.labmanager.service.publication.type.ConferencePaperService;
import fr.ciadlab.labmanager.service.publication.type.JournalPaperService;
import fr.ciadlab.labmanager.service.publication.type.MiscDocumentService;
import fr.ciadlab.labmanager.service.publication.type.ReportService;
import fr.ciadlab.labmanager.service.publication.type.ThesisService;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.Resources;
import org.jbibtex.LaTeXObject;
import org.jbibtex.LaTeXParser;
import org.jbibtex.LaTeXPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.MessageSourceAccessor;

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
