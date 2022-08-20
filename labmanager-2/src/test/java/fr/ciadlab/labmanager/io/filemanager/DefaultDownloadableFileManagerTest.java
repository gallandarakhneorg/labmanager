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

package fr.ciadlab.labmanager.io.filemanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

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

	private ByteArrayOutputStream stream;

	private File root;

	@BeforeEach
	public void setUp() throws IOException {
		this.stream = new ByteArrayOutputStream();
		this.root = new File(File.listRoots()[0], "rootuploads");
		this.test = new DefaultDownloadableFileManager(this.root.toString());
	}

	@Test
	public void normalizeForServerSide_null() {
		assertNull(this.test.normalizeForServerSide(null));
	}

	@Test
	public void normalizeForServerSide_empty() {
		final File file = FileSystem.join(new File("Downloadables"), "PDFs", "PDF1234.pdf");
		final File actual = this.test.normalizeForServerSide(file);
		assertNotNull(actual);
		final String expected = FileSystem.join(this.root, file).toString(); 
		assertEquals(expected, actual.getAbsolutePath());
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
			this.test.saveFiles(new File(outTarget, "out-file.pdf"), new File(outTarget, "out-file.jpg"), file);
			File f = new File(outTarget, "out-file.pdf");
			assertTrue(f.exists());
			f = new File(outTarget, "out-file.jpg");
			assertTrue(f.exists());
		} finally {
			FileSystem.delete(outTarget);
		}
	}

}
