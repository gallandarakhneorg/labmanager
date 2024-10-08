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

package fr.utbm.ciad.labmanager.tests.utils.io.filemanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import fr.utbm.ciad.labmanager.data.project.Project;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DefaultProjectImageManager;
import fr.utbm.ciad.labmanager.views.components.addons.logger.ContextualLoggerFactory;
import org.arakhne.afc.vmutil.FileSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link DefaultProjectImageManager}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@SuppressWarnings("all")
public class DefaultProjectImageManagerTest {

	private File root;

	private DefaultProjectImageManager test;

	
	@BeforeEach
	public void setUp() throws IOException {
		this.root = new File(File.listRoots()[0], "rootuploads");
		this.test = new DefaultProjectImageManager(this.root.toString());
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
	public void getThumbnailPath() throws Exception {
		Project project = mock(Project.class);
		when(project.getId()).thenReturn(1234l);
		String path = this.test.getThumbnailPath(project, false);
		assertNotNull(path);
		assertEquals("Downloadables/ProjectThumbnails/Prj1234.jpg", path);
	}

}
