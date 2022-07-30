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
 * you entered into with the CIAD.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.utils.files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.arakhne.afc.vmutil.FileSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

/** Tests for {@link DefaultDownloadableFileManager}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class DefaultDownloadableFileManagerTest {

	private DefaultDownloadableFileManager test;

	private StreamFactory factory;

	private ByteArrayOutputStream stream;

	@BeforeEach
	public void setUp() throws IOException {
		this.stream = new ByteArrayOutputStream();
		this.factory = mock(StreamFactory.class);
		when(this.factory.openStream(any(File.class))).thenReturn(this.stream);
		this.test = new DefaultDownloadableFileManager(this.factory);
	}

	@Test
	public void saveDownloadablePublicationPdfFile() throws Exception {
		final String encoded = Base64.getEncoder().encodeToString("abc".getBytes());
		final String url = this.test.saveDownloadablePublicationPdfFile(1234, encoded);
		assertEquals("abc", new String(this.stream.toByteArray()));
		assertEquals(FileSystem.join(new File("Downloadables"), "PDFs", "PDF1234.pdf").toString(), url);
	}

	@Test
	public void saveDownloadableAwardPdfFile() throws Exception {
		final String encoded = Base64.getEncoder().encodeToString("abc".getBytes());
		final String url = this.test.saveDownloadableAwardPdfFile(1234, encoded);
		assertEquals("abc", new String(this.stream.toByteArray()));
		assertEquals(FileSystem.join(new File("Downloadables"), "Awards", "Award1234.pdf").toString(), url);
	}

	@Test
	public void getPdfRootFile() {
		assertNotNull(this.test.getPdfRootFile());
	}

	@Test
	public void getAwardRootFile() {
		assertNotNull(this.test.getAwardRootFile());
	}

	@Test
	public void makePdfFilename() {
		assertNotNull(this.test.makePdfFilename(123));
	}

	@Test
	public void makeAwardFilename() {
		assertNotNull(this.test.makeAwardFilename(123));
	}

	@Test
	public void saveFile() throws Exception {
		final MultipartFile file = mock(MultipartFile.class);
		when(file.getInputStream()).thenAnswer(it -> {
			return new ByteArrayInputStream("the content".getBytes());
		});
		final File outTarget = FileSystem.createTempDirectory("tests", "dir");
		try {
			this.test.saveFile(outTarget, "out.file", file);
			final File f = new File(outTarget, "out.file");
			assertTrue(f.exists());
		} finally {
			FileSystem.delete(outTarget);
		}
	}

}
