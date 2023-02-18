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

package fr.ciadlab.labmanager.entities.publication.type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.entities.publication.ConferenceBasedPublication;
import fr.ciadlab.labmanager.entities.publication.JournalBasedPublication;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.scientificaxis.ScientificAxis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Abstract implementation for tests of typed publications.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public abstract class AbstractTypedPublicationTest<T extends Publication> {

	protected T test;

	protected abstract T createTest();
	
	protected abstract T createTest(Publication prePublication);

	@BeforeEach
	public void setUp() {
		this.test = createTest();
	}

	@Test
	public void constructor_Publication() {
		Publication original = new Publication() {
			@Override
			public boolean isRanked() {
				throw new UnsupportedOperationException();
			}
			@Override
			public String getWherePublishedShortDescription() {
				throw new UnsupportedOperationException();
			}
			@Override
			public String getPublicationTarget() {
				throw new UnsupportedOperationException();
			}
		};
		original.setAbstractText("abstract");
		
		Person p0 = mock(Person.class);
		when(p0.getId()).thenReturn(1234);
		Authorship a0 = mock(Authorship.class);
		when(a0.getPerson()).thenReturn(p0);
		Person p1 = mock(Person.class);
		when(p1.getId()).thenReturn(2345);
		Authorship a1 = mock(Authorship.class);
		when(a1.getPerson()).thenReturn(p1);
		original.setAuthorships(Sets.newHashSet(a0, a1));
		
		original.setDblpURL("http://dblp.com");
		original.setDOI("doi");
		original.setExtraURL("http://extra.com");
		original.setHalId("hal");
		original.setISBN("isbn");
		original.setISSN("issn");
		original.setKeywords("kw");
		original.setMajorLanguage(PublicationLanguage.ITALIAN);
		original.setManualValidationForced(true);
		original.setPathToDownloadableAwardCertificate("path/to/award");
		original.setPathToDownloadablePDF("path/to/pdf");
		original.setPreferredStringId("pref");
		original.setPublicationDate(LocalDate.of(2023, 2, 8));
		
		ScientificAxis x0 = mock(ScientificAxis.class);
		ScientificAxis x1 = mock(ScientificAxis.class);
		original.setScientificAxes(Arrays.asList(x0, x1));
		
		original.setTitle("title");
		original.setType(PublicationType.INTERNATIONAL_BOOK_CHAPTER);
		original.setValidated(true);
		original.setVideoURL("http://video.fr");
		//
		this.test = createTest(original);
		//
		assertEquals("abstract", this.test.getAbstractText());
		Set<Authorship> authors = this.test.getAuthorshipsRaw();
		assertEquals(2, authors.size());
		assertTrue(authors.contains(a0));
		assertTrue(authors.contains(a1));
		assertEquals("http://dblp.com", this.test.getDblpURL());
		assertEquals("doi", this.test.getDOI());
		assertEquals("http://extra.com", this.test.getExtraURL());
		assertEquals("hal", this.test.getHalId());
		if (!(this.test instanceof JournalBasedPublication) && !(this.test instanceof ConferenceBasedPublication)) {
			assertEquals("isbn", this.test.getISBN());
			assertEquals("issn", this.test.getISSN());
		}
		assertEquals("kw", this.test.getKeywords());
		assertSame(PublicationLanguage.ITALIAN, this.test.getMajorLanguage());
		assertTrue(this.test.getManualValidationForced());
		assertEquals("path/to/award", this.test.getPathToDownloadableAwardCertificate());
		assertEquals("path/to/pdf", this.test.getPathToDownloadablePDF());
		assertEquals("pref", this.test.getPreferredStringId());
		assertEquals(LocalDate.of(2023, 2, 8), this.test.getPublicationDate());
		List<ScientificAxis> axes = this.test.getScientificAxes();
		assertEquals(2, axes.size());
		assertTrue(axes.contains(x0));
		assertTrue(axes.contains(x1));
		assertEquals("title", this.test.getTitle());
		assertEquals(PublicationType.INTERNATIONAL_BOOK_CHAPTER, this.test.getType());
		assertTrue(this.test.isValidated());
		assertEquals("http://video.fr", this.test.getVideoURL());
	}

}
