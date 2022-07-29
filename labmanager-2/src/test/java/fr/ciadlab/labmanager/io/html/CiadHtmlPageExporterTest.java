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
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.io.html;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collections;

import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.publication.type.ConferencePaper;
import fr.ciadlab.labmanager.entities.publication.type.JournalPaper;
import fr.ciadlab.labmanager.entities.ranking.QuartileRanking;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link CiadHtmlPageExporter}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class CiadHtmlPageExporterTest {

	private static final String OUTPUT_FILE = "/home/sgalland/tmp/export.html";
	//private static final String OUTPUT_FILE = null;

	private CiadHtmlPageExporter test;

	@BeforeEach
	public void setUp() {
		this.test = new CiadHtmlPageExporter();
	}

	@Test
	public void exportPublications_Iterable_null() throws Exception {
		assertNull(this.test.exportPublications(null, new ExporterConfigurator()));
	}

	@Test
	public void exportPublications_Iterable_one() throws Exception {
		ConferencePaper p0 = mock(ConferencePaper.class);
		when(p0.getAddress()).thenReturn("location0");
		when(p0.getDOI()).thenReturn("doinumber0");
		when(p0.getEditors()).thenReturn("Dupont, Henri");
		when(p0.getISBN()).thenReturn("isbn0");
		when(p0.getISSN()).thenReturn("issn0");
		when(p0.getNumber()).thenReturn("num0");
		when(p0.getMajorLanguage()).thenReturn(PublicationLanguage.ENGLISH);
		when(p0.getOrganization()).thenReturn("organizer0");
		when(p0.getPages()).thenReturn("xx-yy");
		when(p0.getPublicationYear()).thenReturn(2022);
		when(p0.getScientificEventName()).thenReturn("conference0");
		when(p0.getSeries()).thenReturn("series0");
		when(p0.getTitle()).thenReturn("This is the title");
		when(p0.getType()).thenReturn(PublicationType.INTERNATIONAL_CONFERENCE_PAPER);
		when(p0.getVolume()).thenReturn("vol0");

		Journal j = mock(Journal.class);
		when(j.getJournalName()).thenReturn("Int. Journal of Science");
		when(j.getPublisher()).thenReturn("The Publisher");
		
		JournalPaper p1 = mock(JournalPaper.class);
		when(p1.getDOI()).thenReturn("doinumber1");
		when(p1.getISBN()).thenReturn("isbn1");
		when(p1.getISSN()).thenReturn("issn1");
		when(p1.getNumber()).thenReturn("num1");
		when(p1.getMajorLanguage()).thenReturn(PublicationLanguage.FRENCH);
		when(p1.getPages()).thenReturn("xx-yy");
		when(p1.getPublicationYear()).thenReturn(2020);
		when(p1.getTitle()).thenReturn("This is the title of an older publication");
		when(p1.getType()).thenReturn(PublicationType.INTERNATIONAL_JOURNAL_PAPER);
		when(p1.getVolume()).thenReturn("vol1");
		when(p1.getJournal()).thenReturn(j);
		when(p1.getScimagoQIndex()).thenReturn(QuartileRanking.Q1);
		when(p1.getWosQIndex()).thenReturn(QuartileRanking.Q2);
		when(p1.getImpactFactor()).thenReturn(8.901f);

		JournalPaper p2 = mock(JournalPaper.class);
		when(p2.getDOI()).thenReturn("doinumber2");
		when(p2.getISBN()).thenReturn("isbn2");
		when(p2.getISSN()).thenReturn("issn2");
		when(p2.getNumber()).thenReturn("num2");
		when(p2.getMajorLanguage()).thenReturn(PublicationLanguage.ENGLISH);
		when(p2.getPages()).thenReturn("xx-yy");
		when(p2.getPublicationYear()).thenReturn(2021);
		when(p2.getTitle()).thenReturn("This is the title of a publication");
		when(p2.getType()).thenReturn(PublicationType.INTERNATIONAL_JOURNAL_PAPER);
		when(p2.getVolume()).thenReturn("vol2");
		when(p2.getJournal()).thenReturn(j);
		when(p2.getScimagoQIndex()).thenReturn(QuartileRanking.Q1);
		when(p2.getWosQIndex()).thenReturn(QuartileRanking.Q1);
		when(p2.getImpactFactor()).thenReturn(5.45f);

		Person a0 = mock(Person.class);
		when(a0.getFirstName()).thenReturn("Stephane");
		when(a0.getLastName()).thenReturn("Martin");

		Person a1 = mock(Person.class);
		when(a1.getFirstName()).thenReturn("Etienne");
		when(a1.getLastName()).thenReturn("Dupont");

		Person a2 = mock(Person.class);
		when(a2.getFirstName()).thenReturn("Andrew");
		when(a2.getLastName()).thenReturn("Schmit");

		when(p0.getAuthors()).thenReturn(Arrays.asList(a0, a1));
		when(p1.getAuthors()).thenReturn(Arrays.asList(a2, a1, a0));
		when(p2.getAuthors()).thenReturn(Arrays.asList(a2, a0));

		Membership m0 = mock(Membership.class);
		when(m0.getMemberStatus()).thenReturn(MemberStatus.ASSOCIATE_PROFESSOR);
		when(m0.isActiveIn(any(), any())).thenReturn(true);

		when(a0.getResearchOrganizations()).thenReturn(Collections.singleton(m0));

		Membership m1 = mock(Membership.class);
		when(m1.getMemberStatus()).thenReturn(MemberStatus.PHD_STUDENT);
		when(m1.isActiveIn(any(), any())).thenReturn(true);

		when(a1.getResearchOrganizations()).thenReturn(Collections.singleton(m1));

		ExporterConfigurator configurator = new ExporterConfigurator();

		String content = this.test.exportPublications(Arrays.asList(p0, p1, p2), configurator);

		assertNotNull(content);

		if (OUTPUT_FILE != null) {
			File file = new File(OUTPUT_FILE);
			try {
				file.delete();
			} catch (Throwable ex) {
				//
			}
			try (FileOutputStream fos = new FileOutputStream(file)) {
				fos.write("<html><body>".getBytes());
				fos.write(content.getBytes());
				fos.write("</body></html>".getBytes());
			}
		}
	}

}
