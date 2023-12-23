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

package fr.utbm.ciad.labmanager.views.components.uploads;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.FailedEvent;
import com.vaadin.flow.component.upload.FileRejectedEvent;
import com.vaadin.flow.component.upload.FinishedEvent;
import com.vaadin.flow.component.upload.ProgressUpdateEvent;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.StartedEvent;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileData;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;
import fr.utbm.ciad.labmanager.utils.Unit;
import org.arakhne.afc.vmutil.FileSystem;

/** Abstract implementation of a field that enables to upload a file.
 * This field does not assume that the field's data is of a specific type.
 * Subclasses must implement function to handle the upload file data.
 *
 * @param <T> the type of the values managed by this field.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public abstract class AbstractUploadableFileField<T> extends CustomField<T> implements LocaleChangeObserver {

	private static final long serialVersionUID = 7202277567396559021L;

	/** Maximum size of the uploaded files (client-side check).
	 */
	protected static final int MAX_GIGA = (int) Unit.GIGA.convertToUnit(100);

	private final ResetableMemoryBuffer receiver = new ResetableMemoryBuffer();

	private VerticalLayout content;

	private final Upload upload;

	private final Span uploadDropLabel;

	private final Button uploadButton;

	/** Default constructor.
	 */
	public AbstractUploadableFileField() {
		this.uploadButton = new Button();
		this.uploadDropLabel = new Span();

		this.upload = new Upload(this::receiveUpload);
		this.upload.setUploadButton(this.uploadButton);
		this.upload.setDropLabel(this.uploadDropLabel);
		this.upload.setMaxFiles(1);
		this.upload.setMaxFileSize(MAX_GIGA);
		this.upload.addStartedListener(it -> uploadStarted(it.getContentLength()));
		this.upload.addSucceededListener(it -> uploadSucceeded());
		this.upload.addFailedListener(it -> uploadFailed(it.getReason()));
		this.upload.addFileRejectedListener(it -> uploadRejected(it.getErrorMessage()));

		this.content = new VerticalLayout();
		this.content.add(this.upload);
		add(this.content);
	}

	/** Replies the name of the uploaded file on the client computer.
	 *
	 * @return the name on the client computer.
	 */
	public File getClientSideFilename() {
		return FileSystem.convertStringToFile(getMemoryReceiver().getFileName());
	}
	
	/** Replies if a file was uploaded and ready to be used.
	 *
	 * @return {@code true} if a file was uploaded, otherwise {@code false}.
	 */
	public boolean hasUploadedData() {
		return getMemoryReceiver().hasFileData();
	}

	/** Replies the root content component.
	 *
	 * @return the content component.
	 */
	protected VerticalLayout getContent() {
		return this.content;
	}

	@SuppressWarnings("resource")
	private OutputStream receiveUpload(String filename, String mime) {
		return uploadStreamOpen(this.receiver.receiveUpload(filename, mime), filename, mime);
	}

	/** Invoked for applying filtering of the reading stream of the uploaded file.
	 *
	 * @param the opened stream to the uploaded file.
	 * @param filename the filename of the uploaded file on client side.
	 * @param mime the MIME of the uploaded file.
	 * @return the stream to be used for reading the file.
	 */
	@SuppressWarnings("static-method")
	protected OutputStream uploadStreamOpen(OutputStream stream, String filename, String mime) {
		return stream;
	}

	/** Invoked when the file was rejected for upload.
	 *
	 * @param error the reason of the rejection.
	 */
	protected void uploadRejected(String reason) {
		//
	}

	private void uploadStarted(long totalSize) {
		uploadStarted();
	}

	/** Invoked when the file was uploaded.
	 */
	protected void uploadStarted() {
		//
	}

	/** Invoked when the upload was started but failed.
	 *
	 * @param error the reason of the failure.
	 */
	protected void uploadFailed(Throwable error) {
		//
	}

	/** Invoked when the file was uploaded.
	 */
	protected void uploadSucceeded() {
		//
	}

	/** Replies the receiver of the uploaded file data.
	 *
	 * @return the receiver.
	 */
	protected ResetableMemoryBuffer getMemoryReceiver() {
		return this.receiver;
	}

	/** Specify the types of files that the server accepts.
	 *
	 * <p>Syntax: a MIME type pattern (wildcards are allowed) or file extensions. Notice that MIME
	 * types are widely supported, while file extensions are only implemented in
	 * certain browsers, so it should be avoided.
	 * 
	 * <p>Example: {@code "video/*","image/tiff"} or {@code ".pdf","audio/mp3"}.
	 *
	 * @param acceptedFileTypes the allowed file types to be uploaded, or {@code null} to
	 *     clear any restrictions
	 */
	public void setAcceptedFileTypes(String... acceptedFileTypes) {
		this.upload.setAcceptedFileTypes(acceptedFileTypes);
	}

	/** Replies the list of accepted file types for upload.
	 *
	 * @return a list of allowed file types, never {@code null}.
	 */
	public List<String> getAcceptedFileTypes() {
		return this.upload.getAcceptedFileTypes();
	}

	/** Specify the maximum file size in bytes allowed to upload. Notice that it
	 * is a client-side constraint, which will be checked before sending the
	 * request.
	 *
	 * @param maxFileSize the maximum file size in bytes
	 */
	public void setMaxFileSize(int maxFileSize) {
		this.upload.setMaxFileSize(maxFileSize);
	}

	/** Replies the maximum allowed file size in the client-side, in bytes.
	 *
	 * @return the maximum file size in bytes.
	 */
	public int getMaxFileSize() {
		return this.upload.getMaxFileSize();
	}

	/** Add a progress listener that is informed on upload progress.
	 *
	 * @param listener progress listener to add.
	 * @return registration for removal of listener.
	 */
	public Registration addProgressListener(ComponentEventListener<ProgressUpdateEvent> listener) {
		return this.upload.addProgressListener(listener);
	}

	/** Add a succeeded listener that is informed on upload failure.
	 *
	 * @param listener failed listener to add.
	 * @return registration for removal of listener.
	 */
	public Registration addFailedListener(ComponentEventListener<FailedEvent> listener) {
		return this.upload.addFailedListener(listener);
	}

	/** Add a succeeded listener that is informed on upload finished.
	 *
	 * @param listener finished listener to add.
	 * @return registration for removal of listener.
	 */
	public Registration addFinishedListener(ComponentEventListener<FinishedEvent> listener) {
		return this.upload.addFinishedListener(listener);
	}

	/** Add a succeeded listener that is informed on upload start.
	 *
	 * @param listener start listener to add.
	 * @return registration for removal of listener.
	 */
	public Registration addStartedListener(ComponentEventListener<StartedEvent> listener) {
		return this.upload.addStartedListener(listener);
	}

	/** Add a succeeded listener that is informed on upload succeeded.
	 *
	 * @param listener succeeded listener to add.
	 * @return registration for removal of listener.
	 */
	public Registration addSucceededListener(ComponentEventListener<SucceededEvent> listener) {
		return this.upload.addSucceededListener(listener);
	}

	/** Adds a listener for {@code file-reject} events fired when a file cannot
	 * be added due to some constrains: {@code setMaxFileSize, setMaxFiles, setAcceptedFileTypes}.
	 *
	 * @param listener the listener.
	 * @return registration for removal of listener.
	 */
	public Registration addFileRejectedListener(ComponentEventListener<FileRejectedEvent> listener) {
		return this.upload.addFileRejectedListener(listener);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		this.upload.setVisible(!readOnly);
	}

	@Override
	public boolean isReadOnly() {
		return !this.upload.isVisible();
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		this.uploadButton.setText(getTranslation("views.upload")); //$NON-NLS-1$
		this.uploadDropLabel.setText(getTranslation("views.upload_drop_here")); //$NON-NLS-1$
	}

	/** Reset the uploader only.
	 */
	protected void resetUploader() {
		this.upload.interruptUpload();
		this.receiver.reset();
	}

	/** Resetable memory buffer.
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	public static class ResetableMemoryBuffer implements Receiver {

		private static final long serialVersionUID = -2321968960435164371L;

		private FileData file;

		/** Default constructor.
		 */
		ResetableMemoryBuffer() {
			//
		}

		@Override
		public OutputStream receiveUpload(String fileName, String MIMEType) {
			final var outputBuffer = new ByteArrayOutputStream();
			this.file = new FileData(fileName, MIMEType, outputBuffer);
			return outputBuffer;
		}

		/** Get the file name for this buffer.
		 *
		 * @return file name or empty if no file
		 */
		public String getFileName() {
			return this.file != null ? this.file.getFileName() : ""; //$NON-NLS-1$
		}

		/** Get the input stream for file with filename.
		 *
		 * @return input stream for file or empty stream if file not found
		 */
		@SuppressWarnings("resource")
		public InputStream getInputStream() {
			if (this.file != null) {
				return new ByteArrayInputStream(
						((ByteArrayOutputStream) this.file.getOutputBuffer())
						.toByteArray());
			}
			return new ByteArrayInputStream(new byte[0]);
		}

		/** Reset the buffer.
		 */
		void reset() {
			this.file = null;
		}

		/** Replies if the buffer has file data.
		 *
		 * @return {@code true} if data is available.
		 */
		public boolean hasFileData() {
			return this.file != null;
		}


		/** Create a stream resource for the upload image.
		 * This function should be inoked when the file is uploaded.
		 *
		 * @return the stream resource for the uploaded file.
		 */
		public StreamResource createStreamResource() {
			final InputStreamFactory factory = () -> {
				return getInputStream();
			};
			return new StreamResource(getFileName(), factory);
		}

	}

}