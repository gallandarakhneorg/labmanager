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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import com.aspose.pdf.Document;
import com.aspose.pdf.devices.JpegDevice;
import com.aspose.pdf.devices.Resolution;
import com.aspose.slides.Presentation;
import com.google.common.base.Strings;
import org.arakhne.afc.sizediterator.SizedIterator;
import org.arakhne.afc.vmutil.FileSystem;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure3;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/** Utilities for managing the downloadable files. This implementation is dedicated to the WordPress service
 * of the lab.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0
 */
@Component
@Primary
public class DefaultDownloadableFileManager extends AbstractFileManager implements DownloadableFileManager {
	
	private static final int JPEG_RESOLUTION = 50;

	private static final float JPEG_RESOLUTION_F = .5f;

	private static final String TEMP_NAME = "labmanager_tmp"; //$NON-NLS-1$

	static final String DOWNLOADABLE_FOLDER_NAME = "Downloadables"; //$NON-NLS-1$

	private static final String ORGANIZATION_LOGO_FOLDER_NAME = "OrganizationLogos"; //$NON-NLS-1$

	private static final String ORGANIZATION_LOGO_FILE_PREFIX = "OrgLogo"; //$NON-NLS-1$

	private static final String ADDRESS_BACKGROUND_FOLDER_NAME = "AddressBgs"; //$NON-NLS-1$

	private static final String ADDRESS_BACKGROUND_FILE_PREFIX = "AddressBg"; //$NON-NLS-1$

	private static final String PDF_FOLDER_NAME = "PDFs"; //$NON-NLS-1$

	private static final String PDF_FILE_PREFIX = "PDF"; //$NON-NLS-1$

	private static final String AWARD_FOLDER_NAME = "Awards"; //$NON-NLS-1$

	private static final String AWARD_FILE_PREFIX = "Award"; //$NON-NLS-1$

	private static final String PROJECT_LOGO_FOLDER_NAME = "ProjectLogos"; //$NON-NLS-1$

	private static final String PROJECT_LOGO_FILE_PREFIX = "ProjectLogo"; //$NON-NLS-1$

	private static final String PROJECT_IMAGE_FOLDER_NAME = "ProjectImages"; //$NON-NLS-1$

	private static final String PROJECT_IMAGE_FILE_PREFIX = "ProjectImg"; //$NON-NLS-1$

	private static final String PROJECT_SCIENTIFIC_REQUIREMENTS_FOLDER_NAME = "ProjectRequirements"; //$NON-NLS-1$

	private static final String PROJECT_SCIENTIFIC_REQUIREMENTS_FILE_PREFIX = "ProjectRequirement"; //$NON-NLS-1$

	private static final String PROJECT_POWERPOINT_FOLDER_NAME = "ProjectPowerpoints"; //$NON-NLS-1$

	private static final String PROJECT_POWERPOINT_FILE_PREFIX = "ProjectPowerpoint"; //$NON-NLS-1$

	private static final String PROJECT_PRESS_DOCUMENT_FOLDER_NAME = "ProjectPressDocs"; //$NON-NLS-1$

	private static final String PROJECT_PRESS_DOCUMENT_FILE_PREFIX = "ProjectPress"; //$NON-NLS-1$

	private static final String TEACHING_ACTIVITY_SLIDES_FOLDER_NAME = "TeachingSlides"; //$NON-NLS-1$

	private static final String TEACHING_ACTIVITY_SLIDES_FILE_PREFIX = "Slides"; //$NON-NLS-1$

	private static final String SAVED_DATA_FOLDER_NAME = "Saves"; //$NON-NLS-1$

	private final File temporaryFolder;

	/** Constructor.
	 *
	 * @param uploadFolder the path of the upload folder. It is defined by the property {@code labmanager.file.upload-directory}.
	 * @param tempFolder the path of the temporary folder. It is defined by the property {@code labmanager.file.temp-directory}.
	 */
	public DefaultDownloadableFileManager(
			@Value("${labmanager.file.upload-directory}") String uploadFolder,
			@Value("${labmanager.file.temp-directory}") String tempFolder) {
		super(uploadFolder);
		final var f1 = Strings.emptyToNull(tempFolder);
		if (f1 == null) {
			this.temporaryFolder = null;
		} else {
			this.temporaryFolder = FileSystem.convertStringToFile(f1).getAbsoluteFile();
		}
	}

	@Override
	public File getTemporaryRootFile() {
		if (this.temporaryFolder == null) {
			final var tmpRoot = new File(System.getProperty("java.io.tmpdir")); //$NON-NLS-1$
			return new File(tmpRoot, TEMP_NAME);
		}
		return this.temporaryFolder;
	}
	
	@Override
	public File getPdfRootFile() {
		return FileSystem.join(new File(DOWNLOADABLE_FOLDER_NAME), PDF_FOLDER_NAME);
	}

	@Override
	public File getAwardRootFile() {
		return FileSystem.join(new File(DOWNLOADABLE_FOLDER_NAME), AWARD_FOLDER_NAME);
	}

	@Override
	public File getAddressBackgroundRootFile() {
		return FileSystem.join(new File(DOWNLOADABLE_FOLDER_NAME), ADDRESS_BACKGROUND_FOLDER_NAME);
	}

	@Override
	public File getOrganizationLogoRootFile() {
		return FileSystem.join(new File(DOWNLOADABLE_FOLDER_NAME), ORGANIZATION_LOGO_FOLDER_NAME);
	}

	@Override
	public File getProjectLogoRootFile() {
		return FileSystem.join(new File(DOWNLOADABLE_FOLDER_NAME), PROJECT_LOGO_FOLDER_NAME);
	}

	@Override
	public File getProjectImageRootFile() {
		return FileSystem.join(new File(DOWNLOADABLE_FOLDER_NAME), PROJECT_IMAGE_FOLDER_NAME);
	}

	@Override
	public File getProjectScientificRequirementsRootFile() {
		return FileSystem.join(new File(DOWNLOADABLE_FOLDER_NAME), PROJECT_SCIENTIFIC_REQUIREMENTS_FOLDER_NAME);
	}

	@Override
	public File getProjectPowerpointRootFile() {
		return FileSystem.join(new File(DOWNLOADABLE_FOLDER_NAME), PROJECT_POWERPOINT_FOLDER_NAME);
	}

	@Override
	public File getProjectPressDocumentRootFile() {
		return FileSystem.join(new File(DOWNLOADABLE_FOLDER_NAME), PROJECT_PRESS_DOCUMENT_FOLDER_NAME);
	}

	@Override
	public File getSavingDataRootFile() {
		return FileSystem.join(new File(DOWNLOADABLE_FOLDER_NAME), SAVED_DATA_FOLDER_NAME);
	}

	@Override
	public File makePdfFilename(long publicationId) {
		return FileSystem.join(getPdfRootFile(), PDF_FILE_PREFIX + Long.valueOf(publicationId) + PDF_FILE_EXTENSION);
	}

	@Override
	public File makePdfPictureFilename(long publicationId) {
		return FileSystem.join(getPdfRootFile(), PDF_FILE_PREFIX + Long.valueOf(publicationId) + JPEG_FILE_EXTENSION);
	}

	@Override
	public File makeAwardFilename(long publicationId) {
		return FileSystem.join(getAwardRootFile(), AWARD_FILE_PREFIX + Long.valueOf(publicationId) + PDF_FILE_EXTENSION);
	}

	@Override
	public File makeAwardPictureFilename(long publicationId) {
		return FileSystem.join(getAwardRootFile(), AWARD_FILE_PREFIX + Long.valueOf(publicationId) + JPEG_FILE_EXTENSION);
	}

	@Override
	public File makeAddressBackgroundImage(long addressId, String fileExtension) {
		return FileSystem.addExtension(
				makeAddressBackgroundImage(addressId),
				fileExtension);
	}

	private File makeAddressBackgroundImage(long addressId) {
		return FileSystem.join(getAddressBackgroundRootFile(), ADDRESS_BACKGROUND_FILE_PREFIX + Long.valueOf(addressId));
	}

	@Override
	public File makeOrganizationLogoFilename(long organizationId, String fileExtension) {
		return FileSystem.addExtension(
				makeOrganizationLogoFilename(organizationId),
				fileExtension);
	}

	private File makeOrganizationLogoFilename(long organizationId) {
		return FileSystem.join(getOrganizationLogoRootFile(), ORGANIZATION_LOGO_FILE_PREFIX + Long.valueOf(organizationId));
	}

	@Override
	public File makeProjectLogoFilename(long projectId, String fileExtension) {
		return FileSystem.addExtension(
				makeProjectLogoFilename(projectId),
				fileExtension);
	}

	private File makeProjectLogoFilename(long projectId) {
		return FileSystem.join(getProjectLogoRootFile(), PROJECT_LOGO_FILE_PREFIX + Long.valueOf(projectId));
	}

	@Override
	public File makeProjectImageFilename(long projectId, int imageIndex, String fileExtension) {
		final var f = makeProjectImageFilename(projectId);
		return FileSystem.addExtension(
				new File(f.getParentFile(), f.getName() + "_" + Integer.toString(imageIndex)), //$NON-NLS-1$
				fileExtension);
	}

	private File makeProjectImageFilename(long projectId) {
		return FileSystem.join(getProjectImageRootFile(), PROJECT_IMAGE_FILE_PREFIX + Long.valueOf(projectId));
	}

	@Override
	public File makeProjectScientificRequirementsFilename(long projectId) {
		return FileSystem.join(getProjectScientificRequirementsRootFile(), PROJECT_SCIENTIFIC_REQUIREMENTS_FILE_PREFIX + Long.valueOf(projectId) + PDF_FILE_EXTENSION);
	}

	@Override
	public File makeProjectScientificRequirementsPictureFilename(long projectId) {
		return FileSystem.join(getProjectScientificRequirementsRootFile(), PROJECT_SCIENTIFIC_REQUIREMENTS_FILE_PREFIX + Long.valueOf(projectId) + JPEG_FILE_EXTENSION);
	}

	@Override
	public File makeProjectPowerpointFilename(long projectId, String fileExtension) {
		return FileSystem.addExtension(
				makeProjectPowerpointFilename(projectId),
				fileExtension);
	}

	private File makeProjectPowerpointFilename(long projectId) {
		return FileSystem.join(getProjectPowerpointRootFile(), PROJECT_POWERPOINT_FILE_PREFIX + Long.valueOf(projectId));
	}

	@Override
	public File makeProjectPowerpointPictureFilename(long projectId) {
		return FileSystem.addExtension(
				FileSystem.join(getProjectPowerpointRootFile(), PROJECT_POWERPOINT_FILE_PREFIX + Long.valueOf(projectId)),
				JPEG_FILE_EXTENSION);
	}

	@Override
	public File makeProjectPressDocumentFilename(long projectId) {
		return FileSystem.join(getProjectPressDocumentRootFile(), PROJECT_PRESS_DOCUMENT_FILE_PREFIX + Long.valueOf(projectId) + PDF_FILE_EXTENSION);
	}

	@Override
	public File makeProjectPressDocumentPictureFilename(long projectId) {
		return FileSystem.join(getProjectPressDocumentRootFile(), PROJECT_PRESS_DOCUMENT_FILE_PREFIX + Long.valueOf(projectId) + JPEG_FILE_EXTENSION);
	}

	@Override
	public void deletePublicationPdfFile(long id) throws IOException {
		var file = makePdfFilename(id);
		var absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
		file = makePdfPictureFilename(id);
		absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
	}

	@Override
	public void deletePublicationAwardPdfFile(long id) throws IOException {
		var file = makeAwardFilename(id);
		var absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
		file = makeAwardPictureFilename(id);
		absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
	}

	@Override
	public void deleteAddressBackgroundImage(long id, String fileExtension) {
		final var file = makeAddressBackgroundImage(id, fileExtension);
		final var absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
	}

	private void deleteFiles(File reference) {
		final var absFile = normalizeForServerSide(reference);
		for (final var deletableFile : absFile.getParentFile().listFiles(new BasenameFilter(reference.getName()))) {
			if (deletableFile.exists()) {
				deletableFile.delete();
			}
		}
	}

	@Override
	public void deleteAddressBackgroundImage(long id) {
		deleteFiles(makeAddressBackgroundImage(id));
	}

	@Override
	public void deleteOrganizationLogo(long id, String fileExtension) {
		final var file = makeOrganizationLogoFilename(id, fileExtension);
		final var absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
	}

	@Override
	public void deleteOrganizationLogo(long id) {
		deleteFiles(makeOrganizationLogoFilename(id));
	}

	@Override
	public void deleteProjectLogo(long id, String fileExtension) {
		final var file = makeProjectLogoFilename(id, fileExtension);
		final var absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
	}

	@Override
	public void deleteProjectLogo(long id) {
		deleteFiles(makeProjectLogoFilename(id));
	}

	@Override
	public void deleteProjectImage(long id, int imageIndex, String fileExtension) {
		final var file = makeProjectImageFilename(id, imageIndex, fileExtension);
		final var absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
	}

	@Override
	public void deleteProjectImage(long id) {
		deleteFiles(makeProjectImageFilename(id));
	}

	@Override
	public void deleteProjectScientificRequirements(long id) {
		var file = makeProjectScientificRequirementsFilename(id);
		var absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
		file = makeProjectScientificRequirementsPictureFilename(id);
		absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
	}

	@Override
	public void deleteProjectPowerpoint(long id, String fileExtension) {
		final var file = makeProjectPowerpointFilename(id, fileExtension);
		final var absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
		deleteProjectPowerpointThumbnail(id);
	}

	private void deleteProjectPowerpointThumbnail(long id) {
		final var file = makeProjectPowerpointPictureFilename(id);
		final var absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
	}

	@Override
	public void deleteProjectPowerpoint(long id) {
		deleteFiles(makeProjectPowerpointFilename(id));
		deleteProjectPowerpointThumbnail(id);
	}

	@Override
	public void deleteProjectPressDocument(long id) {
		var file = makeProjectPressDocumentFilename(id);
		var absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
		file = makeProjectPressDocumentPictureFilename(id);
		absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
	}

	@Override
	public void ensurePictureFile(File inputFilename, File pictureFilename) throws IOException {
		final var inputFilenameAbs = normalizeForServerSide(inputFilename);
		if (inputFilenameAbs.canRead()) {
			final var pictureFilenameAbs = normalizeForServerSide(pictureFilename);
			if (!pictureFilenameAbs.exists()) {
				final var jpgUploadDir = pictureFilenameAbs.getParentFile();
				if (jpgUploadDir != null) {
					jpgUploadDir.mkdirs();
				}
				final var isPdf = FileSystem.hasExtension(inputFilename, PDF_FILE_EXTENSION);
				try (final var outputStream = new FileOutputStream(pictureFilenameAbs)) {
					if (isPdf) {
						convertPdfToJpeg(inputFilenameAbs, outputStream);
					} else {
						convertPptToJpeg(inputFilenameAbs, outputStream);
					}
				} catch (IOException ioe) {
					throw new IOException("Could not save picture file: " + pictureFilenameAbs.getName(), ioe); //$NON-NLS-1$
				}
			}
		}
	}
	
	@Override
	public File toThumbnailFilename(File file) {
		return FileSystem.replaceExtension(file, JPEG_FILE_EXTENSION);
	}

	private File saveMultipart(File filename, MultipartFile source, String errorMessage) throws IOException {
		final var normalizedFilename = normalizeForServerSide(filename);
		final var uploadDir = normalizedFilename.getParentFile();
		uploadDir.mkdirs();
		try (final var inputStream = source.getInputStream()) {
			final Path filePath = normalizedFilename.toPath();
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new IOException(errorMessage + normalizedFilename.getName(), ioe);
		}
		return normalizedFilename;
	}

	@Override
	public void saveImage(File filename, MultipartFile backgroundImage) throws IOException {
		saveMultipart(filename, backgroundImage, "Could not save image: "); //$NON-NLS-1$
	}

	@Override
	public void savePowerpointAndThumbnailFiles(File pptFilename, File pictureFilename, MultipartFile powerpointDocument) throws IOException {
		final var normalizedPdfFilename = saveMultipart(pptFilename, powerpointDocument, "Could not save PowerPoint: "); //$NON-NLS-1$
		//
		final var normalizedJpgFilename = normalizeForServerSide(pictureFilename);
		final var jpgUploadDir = normalizedJpgFilename.getParentFile();
		if (jpgUploadDir != null) {
			jpgUploadDir.mkdirs();
		}
		try (final var outputStream = new FileOutputStream(normalizedJpgFilename)) {
			convertPptToJpeg(normalizedPdfFilename, outputStream);
		} catch (IOException ioe) {
			throw new IOException("Could not save picture file: " + normalizedJpgFilename.getName(), ioe); //$NON-NLS-1$
		}
	}

	private static void convertPptToJpeg(File pptFile, OutputStream jpgStream) throws IOException {
		BufferedImage thumbnail = null;
		try (final var pptStream = new FileInputStream(pptFile)) {
			final var pptDocument = new Presentation(pptStream);
			final var slides = pptDocument.getSlides();
			if (slides != null && slides.size() > 0) {
				final var slide = slides.get_Item(0);
				if (slide != null) {
					thumbnail = slide.getThumbnail(JPEG_RESOLUTION_F, JPEG_RESOLUTION_F);
				}
			}
		}
		if (thumbnail != null) {
			ImageIO.write(thumbnail, "jpeg", jpgStream); //$NON-NLS-1$
		}
	}

	private static void convertPptToJpeg(InputStream pptFile, OutputStream jpgStream) throws IOException {
		BufferedImage thumbnail = null;
		final var pptDocument = new Presentation(pptFile);
		final var slides = pptDocument.getSlides();
		if (slides != null && slides.size() > 0) {
			final var slide = slides.get_Item(0);
			if (slide != null) {
				thumbnail = slide.getThumbnail(JPEG_RESOLUTION_F, JPEG_RESOLUTION_F);
			}
		}
		if (thumbnail != null) {
			ImageIO.write(thumbnail, "jpeg", jpgStream); //$NON-NLS-1$
		}
	}
	
	@Override
	public void savePdfAndThumbnailFiles(File pdfFilename, File pictureFilename, MultipartFile multipartPdfFile) throws IOException {
		final var normalizedPdfFilename = saveMultipart(pdfFilename, multipartPdfFile, "Could not save PDF file: "); //$NON-NLS-1$
		//
		final var normalizedJpgFilename = normalizeForServerSide(pictureFilename);
		final var jpgUploadDir = normalizedJpgFilename.getParentFile();
		if (jpgUploadDir != null) {
			jpgUploadDir.mkdirs();
		}
		try (final var outputStream = new FileOutputStream(normalizedJpgFilename)) {
			convertPdfToJpeg(normalizedPdfFilename, outputStream);
		} catch (IOException ioe) {
			throw new IOException("Could not save picture file: " + normalizedJpgFilename.getName(), ioe); //$NON-NLS-1$
		}
	}

	private static void convertPdfToJpeg(File pdfFile, OutputStream jpgStream) throws IOException {
		try (final var pdfStream = new FileInputStream(pdfFile)) {
			try (final var pdfDocument = new Document(pdfStream)) {
				if (!pdfDocument.getPages().isEmpty()) {
					final var resolution = new Resolution(JPEG_RESOLUTION);
					// Create JpegDevice object where second argument indicates the quality of resultant image
					final var jpegDevice = new JpegDevice(resolution, 100);
					// Convert a particular page and save the image to stream
					try (final var page = pdfDocument.getPages().get_Item(1)) {
						jpegDevice.process(page, jpgStream);
					}
				}
			}
		}
	}

	private static void convertPdfToJpeg(InputStream pdfFile, OutputStream jpgStream) {
		try (final var pdfDocument = new Document(pdfFile)) {
			if (!pdfDocument.getPages().isEmpty()) {
				final var resolution = new Resolution(JPEG_RESOLUTION);
				// Create JpegDevice object where second argument indicates the quality of resultant image
				final var jpegDevice = new JpegDevice(resolution, 100);
				// Convert a particular page and save the image to stream
				try (final var page = pdfDocument.getPages().get_Item(1)) {
					jpegDevice.process(page, jpgStream);
				}
			}
		}
	}

	@Override
	public void generateThumbnail(String basename, InputStream input, OutputStream output) throws IOException {
		final var isPdf = FileSystem.hasExtension(basename, PDF_FILE_EXTENSION);
		if (isPdf) {
			convertPdfToJpeg(input, output);
		} else {
			convertPptToJpeg(input, output);
		}
	}

	@Override
	public void moveFiles(long sourceId, long targetId, Procedure3<String, String, String> callback) throws IOException {
		final var sourcePdfRel = makePdfFilename(sourceId);
		final var sourcePdfAbs = normalizeForServerSide(sourcePdfRel);
		if (sourcePdfAbs.exists()) {
			final var targetPdfRel = makePdfFilename(targetId);
			final var targetPdfAbs = normalizeForServerSide(targetPdfRel);
			if (targetPdfAbs.exists()) {
				Files.deleteIfExists(sourcePdfAbs.toPath());
				if (callback != null) {
					callback.apply("pdf", sourcePdfRel.toString(), null); //$NON-NLS-1$
				}
			} else {
				Files.move(sourcePdfAbs.toPath(), targetPdfAbs.toPath());
				if (callback != null) {
					callback.apply("pdf", sourcePdfRel.toString(), targetPdfRel.toString()); //$NON-NLS-1$
				}
			}
		}

		final var sourcePdfPictureRel = makePdfPictureFilename(sourceId);
		final var sourcePdfPictureAbs = normalizeForServerSide(sourcePdfPictureRel);
		if (sourcePdfPictureAbs.exists()) {
			final var targetPdfPictureRel = makePdfPictureFilename(targetId);
			final var targetPdfPictureAbs = normalizeForServerSide(targetPdfPictureRel);
			if (targetPdfPictureAbs.exists()) {
				Files.deleteIfExists(sourcePdfPictureAbs.toPath());
				if (callback != null) {
					callback.apply("pdf_picture", sourcePdfPictureRel.toString(), null); //$NON-NLS-1$
				}
			} else {
				Files.move(sourcePdfPictureAbs.toPath(), targetPdfPictureAbs.toPath());
				if (callback != null) {
					callback.apply("pdf_picture", sourcePdfPictureRel.toString(), targetPdfPictureRel.toString()); //$NON-NLS-1$
				}
			}
		}

		final var sourceAwardRel = makeAwardFilename(sourceId);
		final var sourceAwardAbs = normalizeForServerSide(sourceAwardRel);
		if (sourceAwardAbs.exists()) {
			final var targetAwardRel = makeAwardFilename(targetId);
			final var targetAwardAbs = normalizeForServerSide(targetAwardRel);
			if (targetAwardAbs.exists()) {
				Files.deleteIfExists(sourceAwardAbs.toPath());
				if (callback != null) {
					callback.apply("award", sourceAwardRel.toString(), null); //$NON-NLS-1$
				}
			} else {
				Files.move(sourceAwardAbs.toPath(), targetAwardAbs.toPath());
				if (callback != null) {
					callback.apply("award", sourceAwardRel.toString(), targetAwardRel.toString()); //$NON-NLS-1$
				}
			}
		}

		final var sourceAwardPictureRel = makeAwardPictureFilename(sourceId);
		final var sourceAwardPictureAbs = normalizeForServerSide(sourceAwardPictureRel);
		if (sourceAwardPictureAbs.exists()) {
			final var targetAwardPictureRel = makeAwardPictureFilename(targetId);
			final var targetAwardPictureAbs = normalizeForServerSide(targetAwardPictureRel);
			if (targetAwardPictureAbs.exists()) {
				Files.deleteIfExists(sourceAwardPictureAbs.toPath());
				if (callback != null) {
					callback.apply("award_picture", sourceAwardPictureRel.toString(), null); //$NON-NLS-1$
				}
			} else {
				Files.move(sourceAwardPictureAbs.toPath(), targetAwardPictureAbs.toPath());
				if (callback != null) {
					callback.apply("award_picture", sourceAwardPictureRel.toString(), targetAwardPictureRel.toString()); //$NON-NLS-1$
				}
			}
		}
	}

	private static List<File> asList(File[] files) {
		if (files == null || files.length == 0) {
			return Collections.emptyList();
		}
		return Arrays.asList(files);
	}

	@Override
	public SizedIterator<File> getUploadedPdfFiles() {
		final var folder0 = normalizeForServerSide(getAwardRootFile());
		final var folder1 = normalizeForServerSide(getPdfRootFile());
		final var files0 = asList(folder0.listFiles(it -> FileSystem.hasExtension(it, PDF_FILE_EXTENSION)));
		final var files1 = asList(folder1.listFiles(it -> FileSystem.hasExtension(it, PDF_FILE_EXTENSION)));
		final var combinedStream = Stream.concat(
				files0.stream(),
				files1.stream());
		return new FileSizedIterator(files0.size() + files1.size(), combinedStream.iterator());
	}

	@Override
	public SizedIterator<File> getThumbailFiles() {
		final var folder0 = normalizeForServerSide(getAwardRootFile());
		final var folder1 = normalizeForServerSide(getPdfRootFile());
		final var files0 = asList(folder0.listFiles(it -> FileSystem.hasExtension(it, JPEG_FILE_EXTENSION)));
		final var files1 = asList(folder1.listFiles(it -> FileSystem.hasExtension(it, JPEG_FILE_EXTENSION)));
		final var combinedStream = Stream.concat(
				files0.stream(),
				files1.stream());
		return new FileSizedIterator(files0.size() + files1.size(), combinedStream.iterator());
	}

	@Override
	public void regenerateThumbnail(File file) throws IOException {
		final File jpegFile = FileSystem.replaceExtension(file, JPEG_FILE_EXTENSION);
		ensurePictureFile(file, jpegFile);
	}

	@Override
	public void deleteTeachingActivitySlides(long id) {
		var file = makeTeachingActivitySlidesFilename(id);
		var absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
		file = makeTeachingActivitySlidesPictureFilename(id);
		absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
	}

	@Override
	public File getTeachingActivitySlidesRootFile() {
		return FileSystem.join(new File(DOWNLOADABLE_FOLDER_NAME), TEACHING_ACTIVITY_SLIDES_FOLDER_NAME);
	}

	@Override
	public File makeTeachingActivitySlidesFilename(long activityId) {
		return FileSystem.join(getTeachingActivitySlidesRootFile(), TEACHING_ACTIVITY_SLIDES_FILE_PREFIX + Long.valueOf(activityId) + PDF_FILE_EXTENSION);
	}

	@Override
	public File makeTeachingActivitySlidesPictureFilename(long activityId) {
		return FileSystem.join(getTeachingActivitySlidesRootFile(), TEACHING_ACTIVITY_SLIDES_FILE_PREFIX + Long.valueOf(activityId) + JPEG_FILE_EXTENSION);
	}

	/** Sized iterator on files.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 2.2
	 */
	public static class FileSizedIterator implements SizedIterator<File> {

		private final int totalSize;

		private final Iterator<File> iterator;

		private int index = -1;
		
		/** Constructor.
		 *
		 * @param totalSize the total number of elements in the iterated collection.
		 * @param iterator the iterator on the collection.
		 */
		FileSizedIterator(int totalSize, Iterator<File> iterator) {
			this.totalSize = totalSize;
			this.iterator = iterator;
		}

		@Override
		public boolean hasNext() {
			return this.iterator.hasNext();
		}

		@Override
		public File next() {
			++this.index;
			return this.iterator.next();
		}

		@Override
		public int totalSize() {
			return this.totalSize;
		}

		@Override
		public int index() {
			return this.index;
		}
		
	}

	/** Sized iterator on files.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 2.2
	 */
	private static class BasenameFilter implements FileFilter, Serializable {

		private static final long serialVersionUID = 973524364322175521L;

		private final String basename;
		
		/** Constructor.
		 *
		 * @param basename the basename.
		 */
		BasenameFilter(String basename) {
			this.basename = basename;
		}
		
		@Override
		public boolean accept(File pathname) {
			return pathname != null && pathname.getName().startsWith(this.basename);
		}

	}

}