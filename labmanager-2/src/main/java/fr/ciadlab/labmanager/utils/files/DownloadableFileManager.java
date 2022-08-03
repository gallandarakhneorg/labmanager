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

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/** Utilities for managing the downloadable files.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public interface DownloadableFileManager {

	/** Save the content of the publication PDF file into the server.
	 *
	 * @param id the identifier of the publication.
	 * @param pdfContent the content of the publication's PDF file.
	 * @return the URL for downloading the PDF file.
	 * @throws Exception if the file cannot be saved.
	 */
	String saveDownloadablePublicationPdfFile(int id, String pdfContent) throws Exception;

	/** Delete from the server the downloadable PDF file associated to the publication with given identifier.
	 *
	 * @param id the identifier of the publication.
	 * @throws Exception if the file cannot be deleted.
	 */
	void deleteDownloadablePublicationPdfFile(int id) throws Exception;

	/** Save the content of the award PDF file into the server.
	 *
	 * @param id the identifier of the publication.
	 * @param awardContent the content of the publication's award PDF file.
	 * @return the URL for downloading the award PDF file.
	 * @throws Exception if the file cannot be saved.
	 */
	String saveDownloadableAwardPdfFile(int id, String awardContent) throws Exception;

	/** Delete from the server the downloadable award's PDF file associated to the publication with given identifier.
	 *
	 * @param id the identifier of the publication.
	 * @throws Exception if the file cannot be deleted.
	 */
	void deleteDownloadableAwardPdfFile(int id) throws Exception;

	/** Replies the path to the root folder for pdf files.
	 *
	 * @return the path to the root folder.
	 */
	File getPdfRootFile();

	/** Replies the path to the root folder for award files.
	 *
	 * @return the path to the root folder.
	 */
	File getAwardRootFile();

	/** Make the path to the PDF downloadable file for the publication with the given identifier.
	 *
	 * @param publicationId the identifier of the publication.
	 * @return the path to the downloadable file.
	 */
	File makePdfFilename(int publicationId);

	/** Make the path to the award downloadable file for the publication with the given identifier.
	 *
	 * @param publicationId the identifier of the publication.
	 * @return the path to the downloadable file.
	 */
	File makeAwardFilename(int publicationId);

	/** Save an uploaded file.
	 *
	 * @param uploadDir the target folder on the local file system.
	 * @param basename the base name of the target file. 
	 * @param multipartFile the content of the file.
	 * @throws IOException if the file cannot be created.
	 */
    void saveFile(File uploadDir, String basename, MultipartFile multipartFile) throws IOException;

	/** Read an uploaded file as a text file.
	 *
	 * @param multipartFile the content of the file to read.
	 * @return the content of the file.
	 * @throws IOException if the file cannot be created.
	 */
   String readTextFile(MultipartFile multipartFile) throws IOException;

}
