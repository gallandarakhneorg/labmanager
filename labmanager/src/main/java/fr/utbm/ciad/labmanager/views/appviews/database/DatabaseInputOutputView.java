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

package fr.utbm.ciad.labmanager.views.appviews.database;

import java.io.InputStream;
import java.util.Locale;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.user.UserRole;
import fr.utbm.ciad.labmanager.services.admin.DatabaseService;
import fr.utbm.ciad.labmanager.views.ViewConstants;
import fr.utbm.ciad.labmanager.views.appviews.MainLayout;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.download.DownloadExtension;
import jakarta.annotation.security.RolesAllowed;
import org.arakhne.afc.progress.Progression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/** Enable to manage the database.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Route(value = "databaseio", layout = MainLayout.class)
@RolesAllowed({UserRole.ADMIN_GRANT})
@Uses(Icon.class)
public class DatabaseInputOutputView extends Composite<HorizontalLayout> implements HasDynamicTitle, LocaleChangeObserver {

	private static final long serialVersionUID = -4957957381615358038L;

	private static final int BUTTON_WIDTH = 256;

	private static final int BUTTON_HEIGHT = 164;
	
	private static final int ICON_SIZE = 128;

	private static final String ICON_SIZE_STR = new StringBuilder().append(128).append(Unit.PIXELS.getSymbol()).toString();

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInputOutputView.class);

	private final DatabaseService databaseService;
	
	private final Button exportJson;
	
	private final Button exportZip;

	/** Constructor.
	 *
	 * @param databaseService the service for accessing the database tools.
	 */
    public DatabaseInputOutputView(@Autowired DatabaseService databaseService) {
    	this.databaseService = databaseService;
    	
		final var jsonImage0 = new Image(ComponentFactory.newStreamImage(ViewConstants.EXPORT_JSON_BLACK_ICON), null);
		jsonImage0.setMinHeight(ICON_SIZE, Unit.PIXELS);
		jsonImage0.setMinWidth(ICON_SIZE, Unit.PIXELS);
    	this.exportJson = new Button(jsonImage0);
    	this.exportJson.setMinHeight(BUTTON_HEIGHT, Unit.PIXELS);
    	this.exportJson.setMinWidth(BUTTON_WIDTH, Unit.PIXELS);
		final var jsonImage1 = new Image(ComponentFactory.newStreamImage(ViewConstants.EXPORT_JSON_BLACK_ICON), null);
        DownloadExtension.extend(this.exportJson)
	    	.withProgressIcon(jsonImage1)
	    	.withProgressTitle(getTranslation("views.databases.io.export_json")) //$NON-NLS-1$
        	.withFilename(() -> Constants.INITIALIZATION_JSON_DATA_FILENAME)
        	.withMimeType(() -> Constants.JSON_MIME)
	    	.withFailureListener(this::notifyExportError)
        	.withInputStreamFactory(progress -> exportJson(progress));

		final var zipImage0 = VaadinIcon.FILE_ZIP.create();
		zipImage0.setSize(ICON_SIZE_STR);
		zipImage0.setColor("black"); //$NON-NLS-1$
    	this.exportZip = new Button(zipImage0);
    	this.exportZip.setMinHeight(BUTTON_HEIGHT, Unit.PIXELS);
    	this.exportZip.setMinWidth(BUTTON_WIDTH, Unit.PIXELS);
		final var zipImage1 = VaadinIcon.FILE_ZIP.create();
		zipImage1.setColor("black"); //$NON-NLS-1$
        DownloadExtension.extend(this.exportZip)
	    	.withProgressIcon(zipImage1)
	    	.withProgressTitle(getTranslation("views.databases.io.export_zip")) //$NON-NLS-1$
        	.withFilename(() -> Constants.INITIALIZATION_ZIP_DATA_FILENAME)
        	.withMimeType(() -> Constants.ZIP_MIME)
	    	.withFailureListener(this::notifyExportError)
        	.withInputStreamFactory(progress -> exportZip(progress));

    	final var root = getContent();
    	root.setPadding(true);
		root.add(this.exportJson, this.exportZip);
    }

	/** Notify the user that the an error was encountered during exporting action.
	 *
	 * @param error the error.
	 */
	protected void notifyExportError(Throwable error) {
		final var ui = getUI().orElseThrow();
		ui.access(() -> {
			final var message = getTranslation("views.databases.io.export_error", error.getLocalizedMessage()); //$NON-NLS-1$
			LOGGER.error(message, error);
			ComponentFactory.showErrorNotification(message);
		});
	}
	
	/** Replies the locale of this view even if the function is invoked from outside the UI.
	 *
	 * @return the locale.
	 */
	protected Locale getLocaleSafe() {
		return getUI().orElseThrow().getLocale();
	}

	/** Export database content to Json.
	 *
	 * @param progression the progression indicator.
	 * @return the content of the file.
	 * @throws Exception the export error.
	 */
	protected InputStream exportJson(Progression progression) throws Exception {
		return this.databaseService.exportJson(getLocaleSafe(), progression);
	}

	/** Export database content to Zip.
	 *
	 * @param progression the progression indicator.
	 * @return the content of the file.
	 * @throws Exception the export error.
	 */
	protected InputStream exportZip(Progression progression) throws Exception {
		return this.databaseService.exportZip(getLocaleSafe(), progression);
	}

	@Override
	public String getPageTitle() {
		return getTranslation("views.databases.io.title"); //$NON-NLS-1$
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
    	this.exportJson.setText(getTranslation("views.databases.io.export_json")); //$NON-NLS-1$
    	this.exportZip.setText(getTranslation("views.databases.io.export_zip")); //$NON-NLS-1$
	}

}
