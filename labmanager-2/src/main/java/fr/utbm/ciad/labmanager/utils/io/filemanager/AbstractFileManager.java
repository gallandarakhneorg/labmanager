/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.utbm.ciad.labmanager.utils.io.filemanager;

import java.io.File;

import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/** Utilities for managing the downloadable files. This implementation is dedicated to the WordPress service
 * of the lab.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
public class AbstractFileManager implements FileManager {

	/** Path to the upload folder.
	 */
	protected final File uploadFolder;

	private Logger logger;

	/** Constructor with the given stream factory.
	 *
	 * @param uploadFolder the path of the upload folder. It is defined by the property {@code labmanager.file.upload-directory}.
	 */
	public AbstractFileManager(
			@Value("${labmanager.file.upload-directory}") String uploadFolder) {
		final String f0 = Strings.emptyToNull(uploadFolder);
		if (f0 == null) {
			this.uploadFolder = null;
		} else {
			this.uploadFolder = FileSystem.convertStringToFile(f0).getAbsoluteFile();
		}
	}

	/** Replies the logger of this service.
	 *
	 * @return the logger.
	 */
	public Logger getLogger() {
		if (this.logger == null) {
			this.logger = createLogger();
		}
		return this.logger;
	}

	/** Change the logger of this controller.
	 *
	 * @param logger the logger.
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/** Factory method for creating the controller logger.
	 *
	 * @return the logger.
	 */
	protected Logger createLogger() {
		return LoggerFactory.getLogger(getClass());
	}

	@Override
	public File normalizeForServerSide(File file) {
		if (file == null) {
			return null;
		}
		if (file.isAbsolute()) {
			return file;
		}
		if (this.uploadFolder != null) {
			return FileSystem.join(this.uploadFolder, file);
		}
		return file.getAbsoluteFile();
	}

}
