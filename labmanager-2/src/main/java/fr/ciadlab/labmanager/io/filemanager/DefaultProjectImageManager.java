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

package fr.ciadlab.labmanager.io.filemanager;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import fr.ciadlab.labmanager.entities.project.Project;
import org.arakhne.afc.vmutil.FileSystem;
import org.arakhne.afc.vmutil.Resources;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Utilities for managing the project images. This implementation is dedicated to the WordPress service
 * of the lab.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Component
@Primary
public class DefaultProjectImageManager extends AbstractFileManager implements ProjectImageManager {

	private static final String THUMBNAIL_FOLDER_NAME = "ProjectThumbnails"; //$NON-NLS-1$

	private static final String THUMBNAIL_FILE_PREFIX = "Prj"; //$NON-NLS-1$

	private static final String THUMBNAIL_RESOURCE_PREFIX = "static/images/projectThumbnails/projectThumbnail"; //$NON-NLS-1$

	private static final int THUMBNAIL_BACKGROUND_COUNT = 7;

	private static final float LOGO_THUMBNAIL_FACTOR = .8f;

	private static final int LOGO_THUMBNAIL_WIDTH = (int) (510 * LOGO_THUMBNAIL_FACTOR);

	private static final int LOGO_THUMBNAIL_HEIGHT = (int) (382 * LOGO_THUMBNAIL_FACTOR);
	
	/** Constructor.
	 *
	 * @param uploadFolder the path of the upload folder. It is defined by the property {@code labmanager.file.upload-directory}.
	 */
	public DefaultProjectImageManager(
			@Value("${labmanager.file.upload-directory}") String uploadFolder) {
		super(uploadFolder);
	}

	@Override
	public String getThumbnailPath(Project project) {
		try {
			return getThumbnailPath(project, true);
		} catch (IOException ex) {
			getLogger().error(ex.getLocalizedMessage(), ex);
			return null;
		}
	}

	/** Replies the path to the thumbnail image for the given project.
	 * If the thumbnail image is generated on the fly.
	 *
	 * @param project the project for which the thumbnail image must be generated.
	 * @param generateThumbnailsIfMissed indicates if the generation for missed thumbnails is activated.
	 * @return the path to the thumbnail image.
	 * @throws IOException if the thumbnail image cannot be generated.
	 */
	public String getThumbnailPath(Project project, boolean generateThumbnailsIfMissed) throws IOException {
		if (project != null) {
			final File filename = getThumbnailFilename(project);
			if (filename != null) {
				final File file = normalizeForServerSide(filename);
				if (file != null) {
					if (generateThumbnailsIfMissed && !file.exists()) {
						generateThumbnailImage(project, file);
					}
					return getThumbnailPublicPath(filename);
				}
			}
		}
		return null;
	}

	/** Build the filename of the thumbnail for the project.
	 *
	 * @param project the project.
	 * @return the filename.
	 */
	@SuppressWarnings("static-method")
	protected File getThumbnailFilename(Project project) {
		return FileSystem.join(new File(DOWNLOADABLE_FOLDER_NAME), THUMBNAIL_FOLDER_NAME, THUMBNAIL_FILE_PREFIX + project.getId() + JPEG_FILE_EXTENSION);
	}

	/** Build the public path of the thumbnail for the project.
	 *
	 * @param filename the local filename of the thumbnail.
	 * @return the public path.
	 */
	@SuppressWarnings("static-method")
	protected String getThumbnailPublicPath(File filename) {
		return filename.getPath();
	}

	/** Generate the thumbnail image for the project.
	 *
	 * @param project the project.
	 * @param file the output file.
	 * @throws IOException if the thumbnail image cannot be generated.
	 */
	public void generateThumbnailImage(Project project, File file) throws IOException {
		final File localLogo = normalizeForServerSide(new File(project.getPathToLogo()));
		final BufferedImage logoImage = ImageIO.read(localLogo);
		final int logoWidth = logoImage.getWidth();
		final int logoHeight = logoImage.getHeight();
		final Image smallLogo;
		if (logoWidth > logoHeight) {
			smallLogo = logoImage.getScaledInstance(LOGO_THUMBNAIL_WIDTH, -1, Image.SCALE_DEFAULT);
		} else {
			smallLogo = logoImage.getScaledInstance(-1, LOGO_THUMBNAIL_HEIGHT, Image.SCALE_DEFAULT);
		}
		//
		final URL background = geThumbnailBackground(project);
		assert background != null;
		final BufferedImage fullImage = ImageIO.read(background);
		final int iwidth = smallLogo.getWidth(null);
		final int iheight = smallLogo.getHeight(null);
		final int x = (fullImage.getWidth() - iwidth) / 2;
		final int y = (fullImage.getHeight() - iheight) / 2;
		final Graphics gd = fullImage.getGraphics();
		gd.drawImage(smallLogo, x, y, null);
		//
		file.getParentFile().mkdirs();
		ImageIO.write(fullImage, "jpg", file); //$NON-NLS-1$
	}

	private static URL geThumbnailBackground(Project project) {
		final int idx = project.getId() % THUMBNAIL_BACKGROUND_COUNT;
		return Resources.getResource(THUMBNAIL_RESOURCE_PREFIX + idx + JPEG_FILE_EXTENSION);
	}

}
