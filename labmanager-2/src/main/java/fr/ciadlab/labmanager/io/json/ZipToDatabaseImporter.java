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

package fr.ciadlab.labmanager.io.json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ciadlab.labmanager.AbstractComponent;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.io.json.JsonToDatabaseImporter.FileCallback;
import fr.ciadlab.labmanager.io.json.JsonToDatabaseImporter.Stats;
import org.arakhne.afc.vmutil.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/** Importer of ZIP (JSON+Files) data into the database.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 * @see JsonToDatabaseImporter
 */
@Component
public class ZipToDatabaseImporter extends AbstractComponent {

	private JsonToDatabaseImporter jsonImporter;

	private DownloadableFileManager download;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param jsonImporter the importer of JSON data.
	 * @param download the manager of downloaded files.
	 */
	public ZipToDatabaseImporter(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired JsonToDatabaseImporter jsonImporter,
			@Autowired DownloadableFileManager download) {
		super(messages, constants);
		this.jsonImporter = jsonImporter;
		this.download = download;
	}

	/** Remove all the files from the target folders
	 *
	 * @throws IOException if there is problem for cleaning the folders.
	 */
	public void cleanTargetFolders() throws IOException {
		deleteContent(this.download.getPdfRootFile());
		deleteContent(this.download.getAwardRootFile());
		deleteContent(this.download.getAddressBackgroundRootFile());
		deleteContent(this.download.getProjectImageRootFile());
		deleteContent(this.download.getProjectLogoRootFile());
		deleteContent(this.download.getProjectPowerpointRootFile());
		deleteContent(this.download.getProjectPressDocumentRootFile());
		deleteContent(this.download.getProjectScientificRequirementsRootFile());
	}

	private void deleteContent(File file) throws IOException {
		if (file != null) {
			final File absFile = this.download.normalizeForServerSide(file);
			final File[] files = absFile.listFiles(it -> {
				return FileSystem.hasExtension(it, ".pdf") || FileSystem.hasExtension(it, ".jpg") //$NON-NLS-1$ //$NON-NLS-2$
						 || FileSystem.hasExtension(it, ".jpeg") || FileSystem.hasExtension(it, ".gif") //$NON-NLS-1$ //$NON-NLS-2$
						 || FileSystem.hasExtension(it, ".png") || FileSystem.hasExtension(it, ".ppt") //$NON-NLS-1$ //$NON-NLS-2$
						 || FileSystem.hasExtension(it, ".pptx"); //$NON-NLS-1$
			});
			if (files != null && files.length > 0) {
				for (final File child : files) {
					FileSystem.delete(child);
				}
			}
		}
	}

	/** Replies if the file with the given name is acceptable.
	 *
	 * @param filename the filename.
	 * @return {@code true} if the file is accepted.
	 */
	@SuppressWarnings("static-method")
	protected boolean isAcceptedDataFile(String filename) {
		final String ext = FileSystem.extension(filename);
		return ".pdf".equals(ext) || ".jpeg".equals(ext) //$NON-NLS-1$ //$NON-NLS-2$
			|| ".jpg".equals(ext) || ".gif".equals(ext) //$NON-NLS-1$ //$NON-NLS-2$
			|| ".png".equals(ext) || ".ppt".equals(ext) //$NON-NLS-1$ //$NON-NLS-2$
			|| ".pptx".equals(ext); //$NON-NLS-1$
	}

	/** Run the importer for ZIP data source only.
	 *
	 * @param url the URL of the ZIP file to read.
	 * @throws Exception if there is problem for importing.
	 */
	public void importArchiveFileToDatabase(URL url) throws Exception {
		cleanTargetFolders();
		deleteTemporaryArea();
		//
		try {
			JsonNode content = null;
			try (ZipInputStream zis = new ZipInputStream(url.openStream())) {
				ZipEntry entry = zis.getNextEntry();
				while (entry != null) {
					final String filename = entry.getName();
					try (final ZippedFileInputStream entryStream = new ZippedFileInputStream(zis)) {
						final String lower = filename.toLowerCase();
						if (lower.equals(Constants.DEFAULT_DBCONTENT_ATTACHMENT_BASENAME + ".json")) { //$NON-NLS-1$
							if (content == null) {
								content = readJson(entryStream);
							} else {
								throw new Exception("To many JSON file in the ZIP archive."); //$NON-NLS-1$
							}
						} else if (isAcceptedDataFile(lower)) {
							// Copy the files onto the server.
							copyAttachedFileToTemporaryArea(filename, entryStream);
						}
					}
					entry = zis.getNextEntry();
				}
			}
			// Inject the JSON content into the database; Change the uploaded files on the fly.
			if (content != null) {
				final UploadedFileManager callback = new UploadedFileManager();
				final Stats stats = this.jsonImporter.importJsonFileToDatabase(content, true, callback);
				getLogger().info("Summary of inserts from the ZIP archive:\n" //$NON-NLS-1$
						+ stats.addresses + " addresses;\n" //$NON-NLS-1$
						+ stats.organizations + " organizations;\n" //$NON-NLS-1$
						+ stats.journals + " journals;\n" //$NON-NLS-1$
						+ stats.conferences + " conferences;\n" //$NON-NLS-1$
						+ stats.persons + " explicit persons;\n" //$NON-NLS-1$
						+ stats.authors + " external authors;\n" //$NON-NLS-1$
						+ stats.organizationMemberships + " organization memberships;\n" //$NON-NLS-1$
						+ stats.publications + " publications;\n" //$NON-NLS-1$
						+ callback.getFileCount() + " attached files;\n" //$NON-NLS-1$
						+ stats.juryMemberships + " jury memberships;\n" //$NON-NLS-1$
						+ stats.invitations + " supervisions;\n" //$NON-NLS-1$
						+ stats.projects + " projects;\n" //$NON-NLS-1$
						+ stats.associatedStructures + " associated structures;\n" //$NON-NLS-1$
						+ stats.teachingActivities + " teaching activities;\n" //$NON-NLS-1$
						+ stats.scientificAxes + " scientific axes."); //$NON-NLS-1$
			} else {
				cleanTargetFolders();
				getLogger().info("Nothing to be inserted from: " + url); //$NON-NLS-1$
			}
		} finally {
			deleteTemporaryArea();
		}
	}

	private static void mkdirs(File file) {
		if (file != null && file.getParentFile() != null) {
			file.getParentFile().mkdirs();
		}
	}

	/** Copy the file from the ZIP file into the temporary folder.
	 *
	 * @param filename the name of the file in the ZIP archive, i.e., in the original file system.
	 * @param entryStream the stream of bytes for the file.
	 * @throws Exception if the file cannot be copied to the temporary folder.
	 */
	protected void copyAttachedFileToTemporaryArea(String filename, InputStream entryStream) throws Exception {
		File outputFile = FileSystem.convertStringToFile(filename);
		if (!outputFile.isAbsolute()) {
			outputFile = FileSystem.join(this.download.getTemporaryRootFile(), outputFile);
			outputFile = this.download.normalizeForServerSide(outputFile);
			getLogger().info("Copying attached file: " + filename + "; to: " + outputFile.toString()); //$NON-NLS-1$ //$NON-NLS-2$
			mkdirs(outputFile);
			try (FileOutputStream fos = new FileOutputStream(outputFile)) {
				// Size of "-1" means that the size of the copy buffer is decided according to the operating system
				FileSystem.copy(entryStream, -1, fos);
			}
		} else {
			throw new Exception("Filename cannot be absolute: " + filename); //$NON-NLS-1$
		}
	}

	/** Delete all the files in the temporary area.
	 */
	protected void deleteTemporaryArea() {
		File tempFile = this.download.getTemporaryRootFile();
		tempFile = this.download.normalizeForServerSide(tempFile);
		try {
			FileSystem.delete(tempFile);
		} catch (IOException ex) {
			getLogger().error(ex.getLocalizedMessage(), ex);
		}
	}
	
	/** Read the JSON content from the given ZIP entry.
	 *
	 * @param entryStream the stream for the ZIP entry.
	 * @return the content.
	 * @throws IOException if the stream cannot be read.
	 */
	protected JsonNode readJson(InputStream entryStream) throws IOException {
		getLogger().info("Reading JSON description"); //$NON-NLS-1$
		// Read the JSON content
		try (final InputStreamReader isr = new InputStreamReader(entryStream)) {
			final ObjectMapper mapper = JsonUtils.createMapper();
			return mapper.readTree(isr);
		}
	}

	/** Manager of the uploaded file for computing and fixing the names in the database. 
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 2.0.0
	 */
	private class UploadedFileManager implements FileCallback {

		private final File temporaryFolder;
	
		private int fileCount;

		/** Constructor.
		 */
		UploadedFileManager() {
			this.temporaryFolder = ZipToDatabaseImporter.this.download.normalizeForServerSide(
					ZipToDatabaseImporter.this.download.getTemporaryRootFile());
		}

		/** Replies the number of imported files.
		 *
		 * @return the number of imported files.
		 */
		public int getFileCount() {
			return this.fileCount;
		}

		@Override
		public String publicationPdfFile(int dbId, String filename) {
			return moveFile(filename, dbId,
					ZipToDatabaseImporter.this.download.makePdfFilename(dbId),
					ZipToDatabaseImporter.this.download.makePdfPictureFilename(dbId));
		}

		@Override
		public String publicationAwardFile(int dbId, String filename) {
			return moveFile(filename, dbId,
					ZipToDatabaseImporter.this.download.makeAwardFilename(dbId),
					ZipToDatabaseImporter.this.download.makeAwardPictureFilename(dbId));
		}

		@Override
		public String addressBackgroundImageFile(int dbId, String filename) {
			final String fileExtension = FileSystem.extension(filename);
			return moveFile(filename, dbId,
					ZipToDatabaseImporter.this.download.makeAddressBackgroundImage(dbId, fileExtension));
		}

		@Override
		public String organizationLogoFile(int dbId, String filename) {
			final String fileExtension = FileSystem.extension(filename);
			return moveFile(filename, dbId,
					ZipToDatabaseImporter.this.download.makeOrganizationLogoFilename(dbId, fileExtension));
		}

		@Override
		public String projectLogoFile(int dbId, String filename) {
			final String fileExtension = FileSystem.extension(filename);
			return moveFile(filename, dbId,
					ZipToDatabaseImporter.this.download.makeProjectLogoFilename(dbId, fileExtension));
		}

		@Override
		public String projectImageFile(int dbId, int index, String filename) {
			final String fileExtension = FileSystem.extension(filename);
			return moveFile(filename, dbId,
					ZipToDatabaseImporter.this.download.makeProjectImageFilename(dbId, index, fileExtension));
		}

		@Override
		public String projectScientificRequirementsFile(int dbId, String filename) {
			return moveFile(filename, dbId,
					ZipToDatabaseImporter.this.download.makeProjectScientificRequirementsFilename(dbId),
					ZipToDatabaseImporter.this.download.makeProjectScientificRequirementsPictureFilename(dbId));
		}

		@Override
		public String projectPressDocumentFile(int dbId, String filename) {
			return moveFile(filename, dbId,
					ZipToDatabaseImporter.this.download.makeProjectPressDocumentFilename(dbId),
					ZipToDatabaseImporter.this.download.makeProjectPressDocumentPictureFilename(dbId));
		}

		@Override
		public String projectPowerpointFile(int dbId, String filename) {
			final String fileExtension = FileSystem.extension(filename);
			return moveFile(filename, dbId,
					ZipToDatabaseImporter.this.download.makeProjectPowerpointFilename(dbId, fileExtension),
					ZipToDatabaseImporter.this.download.makeProjectPowerpointPictureFilename(dbId));
		}

		private String moveFile(String inFilename, int outId, File outFilename, File outPictureName) {
			try {
				final File inFile = FileSystem.join(this.temporaryFolder, inFilename);
				if (inFile.canRead()) {
					final File outFile = ZipToDatabaseImporter.this.download.normalizeForServerSide(outFilename);
					mkdirs(outFile);
					FileSystem.copy(inFile, outFile);
					FileSystem.delete(inFile);
					ZipToDatabaseImporter.this.download.ensurePictureFile(outFilename, outPictureName);
					++this.fileCount;
					return outFilename.toString();
				}
				return null;
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}

		private String moveFile(String inFilename, int outId, File outFilename) {
			try {
				final File inFile = FileSystem.join(this.temporaryFolder, inFilename);
				if (inFile.canRead()) {
					final File outFile = ZipToDatabaseImporter.this.download.normalizeForServerSide(outFilename);
					mkdirs(outFile);
					FileSystem.copy(inFile, outFile);
					FileSystem.delete(inFile);
					++this.fileCount;
					return outFilename.toString();
				}
				return null;
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}

	}

	/** Input stream for a ZIP entry.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 2.0.0
	 */
	private static class ZippedFileInputStream extends InputStream {

		private final ZipInputStream is;

		ZippedFileInputStream(ZipInputStream is) {
			this.is = is;
		}

		@Override
		public int read() throws IOException {
			return this.is.read();
		}

		@Override
		public int read(byte[] b) throws IOException {
			return this.is.read(b);
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			return this.is.read(b, off, len);
		}

		@Override
		public byte[] readAllBytes() throws IOException {
			return this.is.readAllBytes();
		}

		@Override
		public void close() throws IOException {
			//
		}
	}

}