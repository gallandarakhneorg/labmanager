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

package fr.ciadlab.labmanager.io.od;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collections;

import fr.ciadlab.labmanager.entities.member.MemberStatus;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.publication.type.ConferencePaper;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests for {@link DefaultOdfToolkitOpenDocumentTextExporter}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class DefaultOdfToolkitOpenDocumentTextExporterTest {

	//private static final String OUTPUT_FILE = "/tmp/export.odt";
	private static final String OUTPUT_FILE = null;

	private DefaultOdfToolkitOpenDocumentTextExporter test;

	@BeforeEach
	public void setUp() {
		this.test = new DefaultOdfToolkitOpenDocumentTextExporter();
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

		Person a0 = mock(Person.class);
		when(a0.getFirstName()).thenReturn("Stephane");
		when(a0.getLastName()).thenReturn("Martin");

		Person a1 = mock(Person.class);
		when(a1.getFirstName()).thenReturn("Etienne");
		when(a1.getLastName()).thenReturn("Dupont");

		when(p0.getAuthors()).thenReturn(Arrays.asList(a0, a1));

		Membership m0 = mock(Membership.class);
		when(m0.getMemberStatus()).thenReturn(MemberStatus.ASSOCIATE_PROFESSOR);
		when(m0.isActiveIn(any(), any())).thenReturn(true);

		when(a0.getResearchOrganizations()).thenReturn(Collections.singleton(m0));

		Membership m1 = mock(Membership.class);
		when(m1.getMemberStatus()).thenReturn(MemberStatus.PHD_STUDENT);
		when(m1.isActiveIn(any(), any())).thenReturn(true);

		when(a1.getResearchOrganizations()).thenReturn(Collections.singleton(m1));

		ExporterConfigurator configurator = new ExporterConfigurator();

		byte[] content = this.test.exportPublications(Arrays.asList(p0), configurator);

		assertNotNull(content);

		if (OUTPUT_FILE != null) {
			File file = new File(OUTPUT_FILE);
			try {
				file.delete();
			} catch (Throwable ex) {
				//
			}
			try (FileOutputStream fos = new FileOutputStream(file)) {
				fos.write(content);
			}
		}
	}

}
