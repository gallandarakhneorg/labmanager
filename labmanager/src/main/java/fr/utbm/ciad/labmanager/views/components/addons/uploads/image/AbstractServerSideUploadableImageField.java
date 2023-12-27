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

package fr.utbm.ciad.labmanager.views.components.addons.uploads.image;

import java.io.File;

import fr.utbm.ciad.labmanager.views.components.addons.uploads.generic.AbstractServerSideUploadableFileImageViewerField;

/** A field that enables to upload an image to the server.
 * This field does not assume that the field's data is of a specific type.
 * Subclasses must implement function to handle the upload file data.
 *
 * <p>CAUTION: Data is in memory only until the function {@link #saveUploadedFileOnServer(File)} is invoked.
 *
 * @param <T> the type of the values managed by this field.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public abstract class AbstractServerSideUploadableImageField<T> extends AbstractServerSideUploadableFileImageViewerField<T> {

	private static final long serialVersionUID = 8918559663409687792L;

	/** Default accepted MIME types.
	 */
	public static final String[] DEFAULT_ACCEPTED_MIME_TYPES = new String[] {
			".png", //$NON-NLS-1$
			"image/png", //$NON-NLS-1$
			".jpg", //$NON-NLS-1$
			".jpeg", //$NON-NLS-1$
			"image/jpeg", //$NON-NLS-1$
			".svg", //$NON-NLS-1$
			"image/svg+xml" //$NON-NLS-1$
	};

	/** Default constructor.
	 */
	public AbstractServerSideUploadableImageField() {
		setAcceptedFileTypes(DEFAULT_ACCEPTED_MIME_TYPES);
	}

}