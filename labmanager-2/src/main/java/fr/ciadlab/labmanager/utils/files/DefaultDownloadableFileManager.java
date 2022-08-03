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

package fr.ciadlab.labmanager.utils.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

import org.arakhne.afc.vmutil.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2.0.0
 */
@Component
@Primary
public class DefaultDownloadableFileManager implements DownloadableFileManager {

	private static final String DOWNLOADABLE_FOLDER_NAME = "Downloadables"; //$NON-NLS-1$

	private static final String PDF_FOLDER_NAME = "PDFs"; //$NON-NLS-1$

	private static final String PDF_FILE_PREFIX = "PDF"; //$NON-NLS-1$

	private static final String PDF_FILE_EXTENSION = ".pdf"; //$NON-NLS-1$

	private static final String AWARD_FOLDER_NAME = "Awards"; //$NON-NLS-1$

	private static final String AWARD_FILE_PREFIX = "Award"; //$NON-NLS-1$

	private StreamFactory streamFactory;

	/** Constructor with the given stream factory.
	 *
	 * @param factory the factory.
	 */
	public DefaultDownloadableFileManager(@Autowired StreamFactory factory) {
		this.streamFactory = factory;
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
	public File makePdfFilename(int publicationId) {
		return FileSystem.join(getPdfRootFile(), PDF_FILE_PREFIX + Integer.valueOf(publicationId) + PDF_FILE_EXTENSION);
	}

	@Override
	public File makeAwardFilename(int publicationId) {
		return FileSystem.join(getAwardRootFile(), AWARD_FILE_PREFIX + Integer.valueOf(publicationId) + PDF_FILE_EXTENSION);
	}

	/** Save the content of the publication PDF that was encoded in Base 64 into the server.
	 *
	 * @param content the content of the publication's PDF file, which is encoded in Base 64.
	 * @param stream the output stream that should received the encode content of the file (not encoded).
	 * @throws Exception if the file cannot be saved.
	 */
	@SuppressWarnings("static-method")
	public void saveDownloadableFile(String content, OutputStream stream) throws Exception {
		final byte[] decoder = Base64.getDecoder().decode(content);
		stream.write(decoder);
	}

	@Override
	public String saveDownloadablePublicationPdfFile(int id, String pdfContent) throws Exception {
		final File file = makePdfFilename(id);
		try (OutputStream os = this.streamFactory.openStream(file)) {
			saveDownloadableFile(pdfContent, os);
		}
		return file.getPath();
	}

	@Override
	public void deleteDownloadablePublicationPdfFile(int id) throws Exception {
		final File file = makePdfFilename(id);
		if (file.exists()) {
			file.delete();
		}
	}

	@Override
	public String saveDownloadableAwardPdfFile(int id, String awardContent) throws Exception {
		final File file = makeAwardFilename(id);
		try (OutputStream os = this.streamFactory.openStream(file)) {
			saveDownloadableFile(awardContent, os);
		}
		return file.getPath();
	}

	@Override
	public void deleteDownloadableAwardPdfFile(int id) {
		final File file = makeAwardFilename(id);
		if (file.exists()) {
			file.delete();
		}
	}

	@Override
	public void saveFile(File uploadDir, String basename, MultipartFile multipartFile) throws IOException {
		try (final InputStream inputStream = multipartFile.getInputStream()) {
			uploadDir.mkdirs();
			final Path filePath = uploadDir.toPath().resolve(basename);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new IOException("Could not save image file: " + basename, ioe); //$NON-NLS-1$
		}
	}

	@Override
	public String readTextFile(MultipartFile multipartFile) throws IOException {
		final StringBuilder buffer = new StringBuilder();
		try (final InputStream is = multipartFile.getInputStream()) {
			try (final InputStreamReader isr = new InputStreamReader(is)) {
				try (final BufferedReader br = new BufferedReader(isr)) {
					String line;
					while ((line = br.readLine()) != null) {
						buffer.append(line);
					}
				}
			}
		}
		return buffer.toString();
	}

}
