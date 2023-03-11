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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import com.aspose.pdf.Page;
import com.aspose.pdf.devices.JpegDevice;
import com.aspose.pdf.devices.Resolution;
import com.aspose.slides.ISlide;
import com.aspose.slides.ISlideCollection;
import com.aspose.slides.Presentation;
import org.apache.jena.ext.com.google.common.base.Strings;
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
public class DefaultDownloadableFileManager implements DownloadableFileManager {

	private static final int JPEG_RESOLUTION = 50;

	private static final float JPEG_RESOLUTION_F = .5f;

	private static final String TEMP_NAME = "labmanager_tmp"; //$NON-NLS-1$

	private static final String DOWNLOADABLE_FOLDER_NAME = "Downloadables"; //$NON-NLS-1$

	private static final String PDF_FOLDER_NAME = "PDFs"; //$NON-NLS-1$

	private static final String PDF_FILE_PREFIX = "PDF"; //$NON-NLS-1$

	private static final String PDF_FILE_EXTENSION = ".pdf"; //$NON-NLS-1$

	private static final String AWARD_FOLDER_NAME = "Awards"; //$NON-NLS-1$

	private static final String AWARD_FILE_PREFIX = "Award"; //$NON-NLS-1$

	private static final String ADDRESS_BACKGROUND_FOLDER_NAME = "AddressBgs"; //$NON-NLS-1$

	private static final String ADDRESS_BACKGROUND_FILE_PREFIX = "AddressBg"; //$NON-NLS-1$

	private static final String ORGANIZATION_LOGO_FOLDER_NAME = "OrganizationLogos"; //$NON-NLS-1$

	private static final String ORGANIZATION_LOGO_FILE_PREFIX = "OrgLogo"; //$NON-NLS-1$

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

	private final File uploadFolder;

	private final File temporaryFolder;

	/** Constructor with the given stream factory.
	 *
	 * @param factory the factory.
	 * @param uploadFolder the path of the upload folder. It is defined by the property {@code labmanager.file.upload-directory}.
	 * @param tempFolder the path of the temporary folder. It is defined by the property {@code labmanager.file.temp-directory}.
	 */
	public DefaultDownloadableFileManager(
			@Value("${labmanager.file.upload-directory}") String uploadFolder,
			@Value("${labmanager.file.temp-directory}") String tempFolder) {
		final String f0 = Strings.emptyToNull(uploadFolder);
		if (f0 == null) {
			this.uploadFolder = null;
		} else {
			this.uploadFolder = FileSystem.convertStringToFile(f0).getAbsoluteFile();
		}
		final String f1 = Strings.emptyToNull(tempFolder);
		if (f1 == null) {
			this.temporaryFolder = null;
		} else {
			this.temporaryFolder = FileSystem.convertStringToFile(f1).getAbsoluteFile();
		}
	}

	@Override
	public File getTemporaryRootFile() {
		if (this.temporaryFolder == null) {
			final File tmpRoot = new File(System.getProperty("java.io.tmpdir")); //$NON-NLS-1$
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
	public File makePdfFilename(int publicationId) {
		return FileSystem.join(getPdfRootFile(), PDF_FILE_PREFIX + Integer.valueOf(publicationId) + PDF_FILE_EXTENSION);
	}

	@Override
	public File makePdfPictureFilename(int publicationId) {
		return FileSystem.join(getPdfRootFile(), PDF_FILE_PREFIX + Integer.valueOf(publicationId) + JPEG_FILE_EXTENSION);
	}

	@Override
	public File makeAwardFilename(int publicationId) {
		return FileSystem.join(getAwardRootFile(), AWARD_FILE_PREFIX + Integer.valueOf(publicationId) + PDF_FILE_EXTENSION);
	}

	@Override
	public File makeAwardPictureFilename(int publicationId) {
		return FileSystem.join(getAwardRootFile(), AWARD_FILE_PREFIX + Integer.valueOf(publicationId) + JPEG_FILE_EXTENSION);
	}

	@Override
	public File makeAddressBackgroundImage(int addressId, String fileExtension) {
		return FileSystem.addExtension(
				FileSystem.join(getAddressBackgroundRootFile(), ADDRESS_BACKGROUND_FILE_PREFIX + Integer.valueOf(addressId)),
				fileExtension);
	}

	@Override
	public File makeOrganizationLogoFilename(int organizationId, String fileExtension) {
		return FileSystem.addExtension(
				FileSystem.join(getOrganizationLogoRootFile(), ORGANIZATION_LOGO_FILE_PREFIX + Integer.valueOf(organizationId)),
				fileExtension);
	}

	@Override
	public File makeProjectLogoFilename(int projectId, String fileExtension) {
		return FileSystem.addExtension(
				FileSystem.join(getProjectLogoRootFile(), PROJECT_LOGO_FILE_PREFIX + Integer.valueOf(projectId)),
				fileExtension);
	}

	@Override
	public File makeProjectImageFilename(int projectId, int imageIndex, String fileExtension) {
		return FileSystem.addExtension(
				FileSystem.join(getProjectImageRootFile(), PROJECT_IMAGE_FILE_PREFIX + Integer.valueOf(projectId)
					+ "_" + Integer.toString(imageIndex)), //$NON-NLS-1$
				fileExtension);
	}

	@Override
	public File makeProjectScientificRequirementsFilename(int projectId) {
		return FileSystem.join(getProjectScientificRequirementsRootFile(), PROJECT_SCIENTIFIC_REQUIREMENTS_FILE_PREFIX + Integer.valueOf(projectId) + PDF_FILE_EXTENSION);
	}

	@Override
	public File makeProjectScientificRequirementsPictureFilename(int projectId) {
		return FileSystem.join(getProjectScientificRequirementsRootFile(), PROJECT_SCIENTIFIC_REQUIREMENTS_FILE_PREFIX + Integer.valueOf(projectId) + JPEG_FILE_EXTENSION);
	}

	@Override
	public File makeProjectPowerpointFilename(int projectId, String fileExtension) {
		return FileSystem.addExtension(
				FileSystem.join(getProjectPowerpointRootFile(), PROJECT_POWERPOINT_FILE_PREFIX + Integer.valueOf(projectId)),
				fileExtension);
	}

	@Override
	public File makeProjectPowerpointPictureFilename(int projectId) {
		return FileSystem.addExtension(
				FileSystem.join(getProjectPowerpointRootFile(), PROJECT_POWERPOINT_FILE_PREFIX + Integer.valueOf(projectId)),
				JPEG_FILE_EXTENSION);
	}

	@Override
	public File makeProjectPressDocumentFilename(int projectId) {
		return FileSystem.join(getProjectPressDocumentRootFile(), PROJECT_PRESS_DOCUMENT_FILE_PREFIX + Integer.valueOf(projectId) + PDF_FILE_EXTENSION);
	}

	@Override
	public File makeProjectPressDocumentPictureFilename(int projectId) {
		return FileSystem.join(getProjectPressDocumentRootFile(), PROJECT_PRESS_DOCUMENT_FILE_PREFIX + Integer.valueOf(projectId) + JPEG_FILE_EXTENSION);
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

	@Override
	public void deleteDownloadablePublicationPdfFile(int id) throws Exception {
		File file = makePdfFilename(id);
		File absFile = normalizeForServerSide(file);
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
	public void deleteDownloadableAwardPdfFile(int id) {
		File file = makeAwardFilename(id);
		File absFile = normalizeForServerSide(file);
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
	public void deleteAddressBackgroundImage(int id, String fileExtension) {
		final File file = makeAddressBackgroundImage(id, fileExtension);
		final File absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
	}

	@Override
	public void deleteOrganizationLogo(int id, String fileExtension) {
		final File file = makeOrganizationLogoFilename(id, fileExtension);
		final File absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
	}

	@Override
	public void deleteProjectLogo(int id, String fileExtension) {
		final File file = makeProjectLogoFilename(id, fileExtension);
		final File absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
	}

	@Override
	public void deleteProjectImage(int id, int imageIndex, String fileExtension) {
		final File file = makeProjectImageFilename(id, imageIndex, fileExtension);
		final File absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
	}

	@Override
	public void deleteProjectScientificRequirements(int id) {
		File file = makeProjectScientificRequirementsFilename(id);
		File absFile = normalizeForServerSide(file);
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
	public void deleteProjectPowerpoint(int id, String fileExtension) {
		File file = makeProjectPowerpointFilename(id, fileExtension);
		File absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
		file = makeProjectPowerpointPictureFilename(id);
		absFile = normalizeForServerSide(file);
		if (absFile.exists()) {
			absFile.delete();
		}
	}

	@Override
	public void deleteProjectPressDocument(int id) {
		File file = makeProjectPressDocumentFilename(id);
		File absFile = normalizeForServerSide(file);
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
		final File inputFilenameAbs = normalizeForServerSide(inputFilename);
		if (inputFilenameAbs.canRead()) {
			final File pictureFilenameAbs = normalizeForServerSide(pictureFilename);
			if (!pictureFilenameAbs.exists()) {
				final File jpgUploadDir = pictureFilenameAbs.getParentFile();
				if (jpgUploadDir != null) {
					jpgUploadDir.mkdirs();
				}
				final boolean isPdf = FileSystem.hasExtension(inputFilename, PDF_FILE_EXTENSION);
				try (final OutputStream outputStream = new FileOutputStream(pictureFilenameAbs)) {
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

	private File saveMultipart(File filename, MultipartFile source, String errorMessage) throws IOException {
		final File normalizedFilename = normalizeForServerSide(filename);
		final File uploadDir = normalizedFilename.getParentFile();
		uploadDir.mkdirs();
		try (final InputStream inputStream = source.getInputStream()) {
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
		final File normalizedPdfFilename = saveMultipart(pptFilename, powerpointDocument, "Could not save PowerPoint: "); //$NON-NLS-1$
		//
		final File normalizedJpgFilename = normalizeForServerSide(pictureFilename);
		final File jpgUploadDir = normalizedJpgFilename.getParentFile();
		if (jpgUploadDir != null) {
			jpgUploadDir.mkdirs();
		}
		try (final OutputStream outputStream = new FileOutputStream(normalizedJpgFilename)) {
			convertPptToJpeg(normalizedPdfFilename, outputStream);
		} catch (IOException ioe) {
			throw new IOException("Could not save picture file: " + normalizedJpgFilename.getName(), ioe); //$NON-NLS-1$
		}
	}

	private static void convertPptToJpeg(File pptFile, OutputStream jpgStream) throws IOException {
		BufferedImage thumbnail = null;
		try (final InputStream pptStream = new FileInputStream(pptFile)) {
			final Presentation pptDocument = new Presentation(pptStream);
			final ISlideCollection slides = pptDocument.getSlides();
			if (slides != null && slides.size() > 0) {
				final ISlide slide = slides.get_Item(0);
				if (slide != null) {
					thumbnail = slide.getThumbnail(JPEG_RESOLUTION_F, JPEG_RESOLUTION_F);
				}
			}
		}
		if (thumbnail != null) {
			ImageIO.write(thumbnail, "jpeg", jpgStream); //$NON-NLS-1$
		}
	}

	@Override
	public void savePdfAndThumbnailFiles(File pdfFilename, File pictureFilename, MultipartFile multipartPdfFile) throws IOException {
		final File normalizedPdfFilename = saveMultipart(pdfFilename, multipartPdfFile, "Could not save PDF file: "); //$NON-NLS-1$
		//
		final File normalizedJpgFilename = normalizeForServerSide(pictureFilename);
		final File jpgUploadDir = normalizedJpgFilename.getParentFile();
		if (jpgUploadDir != null) {
			jpgUploadDir.mkdirs();
		}
		try (final OutputStream outputStream = new FileOutputStream(normalizedJpgFilename)) {
			convertPdfToJpeg(normalizedPdfFilename, outputStream);
		} catch (IOException ioe) {
			throw new IOException("Could not save picture file: " + normalizedJpgFilename.getName(), ioe); //$NON-NLS-1$
		}
	}

	private static void convertPdfToJpeg(File pdfFile, OutputStream jpgStream) throws IOException {
		try (final InputStream pdfStream = new FileInputStream(pdfFile)) {
			try (final Document pdfDocument = new Document(pdfStream)) {
				if (!pdfDocument.getPages().isEmpty()) {
					final Resolution resolution = new Resolution(JPEG_RESOLUTION);
					// Create JpegDevice object where second argument indicates the quality of resultant image
					final JpegDevice jpegDevice = new JpegDevice(resolution, 100);
					// Convert a particular page and save the image to stream
					try (final Page page = pdfDocument.getPages().get_Item(1)) {
						jpegDevice.process(page, jpgStream);
					}
				}
			}
		}
	}

	@Override
	public void moveFiles(int sourceId, int targetId, Procedure3<String, String, String> callback) throws IOException {
		final File sourcePdfRel = makePdfFilename(sourceId);
		final File sourcePdfAbs = normalizeForServerSide(sourcePdfRel);
		if (sourcePdfAbs.exists()) {
			final File targetPdfRel = makePdfFilename(targetId);
			final File targetPdfAbs = normalizeForServerSide(targetPdfRel);
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

		final File sourcePdfPictureRel = makePdfPictureFilename(sourceId);
		final File sourcePdfPictureAbs = normalizeForServerSide(sourcePdfPictureRel);
		if (sourcePdfPictureAbs.exists()) {
			final File targetPdfPictureRel = makePdfPictureFilename(targetId);
			final File targetPdfPictureAbs = normalizeForServerSide(targetPdfPictureRel);
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

		final File sourceAwardRel = makeAwardFilename(sourceId);
		final File sourceAwardAbs = normalizeForServerSide(sourceAwardRel);
		if (sourceAwardAbs.exists()) {
			final File targetAwardRel = makeAwardFilename(targetId);
			final File targetAwardAbs = normalizeForServerSide(targetAwardRel);
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

		final File sourceAwardPictureRel = makeAwardPictureFilename(sourceId);
		final File sourceAwardPictureAbs = normalizeForServerSide(sourceAwardPictureRel);
		if (sourceAwardPictureAbs.exists()) {
			final File targetAwardPictureRel = makeAwardPictureFilename(targetId);
			final File targetAwardPictureAbs = normalizeForServerSide(targetAwardPictureRel);
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
		final File folder0 = normalizeForServerSide(getAwardRootFile());
		final File folder1 = normalizeForServerSide(getPdfRootFile());
		final List<File> files0 = asList(folder0.listFiles(it -> FileSystem.hasExtension(it, PDF_FILE_EXTENSION)));
		final List<File> files1 = asList(folder1.listFiles(it -> FileSystem.hasExtension(it, PDF_FILE_EXTENSION)));
		final Stream<File> combinedStream = Stream.concat(
				files0.stream(),
				files1.stream());
		return new FileSizedIterator(files0.size() + files1.size(), combinedStream.iterator());
	}

	@Override
	public SizedIterator<File> getThumbailFiles() {
		final File folder0 = normalizeForServerSide(getAwardRootFile());
		final File folder1 = normalizeForServerSide(getPdfRootFile());
		final List<File> files0 = asList(folder0.listFiles(it -> FileSystem.hasExtension(it, JPEG_FILE_EXTENSION)));
		final List<File> files1 = asList(folder1.listFiles(it -> FileSystem.hasExtension(it, JPEG_FILE_EXTENSION)));
		final Stream<File> combinedStream = Stream.concat(
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
	public void deleteTeachingActivitySlides(int id) {
		File file = makeTeachingActivitySlidesFilename(id);
		File absFile = normalizeForServerSide(file);
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
	public File makeTeachingActivitySlidesFilename(int activityId) {
		return FileSystem.join(getTeachingActivitySlidesRootFile(), TEACHING_ACTIVITY_SLIDES_FILE_PREFIX + Integer.valueOf(activityId) + PDF_FILE_EXTENSION);
	}

	@Override
	public File makeTeachingActivitySlidesPictureFilename(int activityId) {
		return FileSystem.join(getTeachingActivitySlidesRootFile(), TEACHING_ACTIVITY_SLIDES_FILE_PREFIX + Integer.valueOf(activityId) + JPEG_FILE_EXTENSION);
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

}
