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

package fr.utbm.ciad.labmanager.views.components.organizations;

import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.organization.OrganizationAddress;
import fr.utbm.ciad.labmanager.services.organization.OrganizationAddressService.EditingContext;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.converters.StringTrimer;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMark;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMarkStatusHandler;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityEditor;
import fr.utbm.ciad.labmanager.views.components.addons.uploads.image.ServerSideUploadableImageField;
import fr.utbm.ciad.labmanager.views.components.addons.validators.GpsCoordinatesValidator;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotEmptyStringValidator;
import fr.utbm.ciad.labmanager.views.components.addons.validators.UrlValidator;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation for the editor of the information related to an organization address.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Uses(Icon.class)
public abstract class AbstractAddressEditor extends AbstractEntityEditor<OrganizationAddress> {

	private static final long serialVersionUID = 2100960882602478067L;

	private DetailsWithErrorMark postalInformationDetails;

	private TextField name;

	private TextArea complement;

	private TextField street;

	private TextField zipCode;

	private TextField city;

	private DetailsWithErrorMark geographicDetails;

	private TextField gps;

	private TextField googleMapUrl;

	private Details presentationDetails;

	private ServerSideUploadableImageField uploadImage;

	private final DownloadableFileManager fileManager;

	private final EditingContext context;

	/** Constructor.
	 *
	 * @param context the context for editing the entity.
	 * @param fileManager the manager of files at the server-side.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 * @param logger the logger to be used by this view.
	 */
	public AbstractAddressEditor(EditingContext context, DownloadableFileManager fileManager,
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages, Logger logger) {
		super(OrganizationAddress.class, authenticatedUser, messages, logger,
				"views.addresses.administration_details", //$NON-NLS-1$
				"views.addresses.administration.validated_address"); //$NON-NLS-1$
		this.fileManager = fileManager;
		this.context = context;
	}

	@Override
	public OrganizationAddress getEditedEntity() {
		return this.context.getAddress();
	}

	@Override
	protected void createEditorContent(VerticalLayout rootContainer) {
		createPostalAddressInformationDetails(rootContainer);
		createGeographicInformationDetails(rootContainer);
		createPresentationDetails(rootContainer);
		if (isBaseAdmin()) {
			createAdministrationComponents(rootContainer,
					null,
					it -> it.bind(OrganizationAddress::isValidated, OrganizationAddress::setValidated));
		}
	}

	/** Create the section for editing the postal address.
	 *
	 * @param rootContainer the container.
	 */
	protected void createPostalAddressInformationDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.name = new TextField();
		this.name.setPrefixComponent(VaadinIcon.HASH.create());
		this.name.setRequired(true);
		this.name.setClearButtonVisible(true);
		content.add(this.name, 2);

		this.complement = new TextArea();
		this.complement.setPrefixComponent(VaadinIcon.BUILDING.create());
		this.complement.setClearButtonVisible(true);
		content.add(this.complement, 2);

		this.street = new TextField();
		this.street.setPrefixComponent(VaadinIcon.BUILDING.create());
		this.street.setRequired(true);
		this.street.setClearButtonVisible(true);
		content.add(this.street, 2);
		
		this.zipCode = new TextField();
		this.zipCode.setPrefixComponent(VaadinIcon.BUILDING.create());
		this.zipCode.setRequired(true);
		this.zipCode.setClearButtonVisible(true);
		content.add(this.zipCode, 1);

		this.city = new TextField();
		this.city.setPrefixComponent(VaadinIcon.BUILDING.create());
		this.city.setRequired(true);
		this.city.setClearButtonVisible(true);
		content.add(this.city, 1);

		this.postalInformationDetails = new DetailsWithErrorMark(content);
		this.postalInformationDetails.setOpened(false);
		rootContainer.add(this.postalInformationDetails);
		
		getEntityDataBinder().forField(this.name)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.addresses.name.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.name, this.postalInformationDetails))
			.bind(OrganizationAddress::getName, OrganizationAddress::setName);
		getEntityDataBinder().forField(this.complement)
			.withConverter(new StringTrimer())
			.bind(OrganizationAddress::getComplement, OrganizationAddress::setComplement);
		getEntityDataBinder().forField(this.street)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.addresses.street.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.street, this.postalInformationDetails))
			.bind(OrganizationAddress::getStreet, OrganizationAddress::setStreet);
		getEntityDataBinder().forField(this.zipCode)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.addresses.zip_code.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.zipCode, this.postalInformationDetails))
			.bind(OrganizationAddress::getZipCode, OrganizationAddress::setZipCode);
		getEntityDataBinder().forField(this.city)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.addresses.city.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.city, this.postalInformationDetails))
			.bind(OrganizationAddress::getCity, OrganizationAddress::setCity);
	}

	/** Create the section for editing the geographic informations.
	 *
	 * @param rootContainer the container.
	 */
	protected void createGeographicInformationDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.gps = new TextField();
		this.gps.setPrefixComponent(VaadinIcon.MAP_MARKER.create());
		this.gps.setClearButtonVisible(true);
		content.add(this.gps, 2);

		this.googleMapUrl = new TextField();
		this.googleMapUrl.setPrefixComponent(VaadinIcon.GLOBE_WIRE.create());
		this.googleMapUrl.setClearButtonVisible(true);
		content.add(this.googleMapUrl, 2);

		this.geographicDetails = new DetailsWithErrorMark(content);
		this.geographicDetails.setOpened(false);
		rootContainer.add(this.geographicDetails);

		getEntityDataBinder().forField(this.gps)
			.withConverter(new StringTrimer())
			.withValidator(new GpsCoordinatesValidator(getTranslation("views.addresses.gps.error"), null)) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.gps, this.geographicDetails))
			.bind(OrganizationAddress::getMapCoordinates, OrganizationAddress::setMapCoordinates);
		getEntityDataBinder().forField(this.googleMapUrl)
			.withConverter(new StringTrimer())
			.withValidator(new UrlValidator(getTranslation("views.urls.invalid_format"), true)) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.googleMapUrl, this.geographicDetails))
			.bind(OrganizationAddress::getGoogleMapLink, OrganizationAddress::setGoogleMapLink);
	}
	
	/** Create the section for editing the presentation details.
	 *
	 * @param rootContainer the container.
	 */
	protected void createPresentationDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.uploadImage = new ServerSideUploadableImageField(this.fileManager,
				ext -> this.fileManager.makeAddressBackgroundImage(getEditedEntity().getId(), ext));
		this.uploadImage.setClearButtonVisible(true);
		content.add(this.uploadImage, 2);

		this.presentationDetails = new Details("", content); //$NON-NLS-1$
		this.presentationDetails.setOpened(false);
		rootContainer.add(this.presentationDetails);

		getEntityDataBinder().forField(this.uploadImage)
			.withConverter(new StringTrimer())
			.bind(OrganizationAddress::getPathToBackgroundImage, OrganizationAddress::setPathToBackgroundImage);
	}

	@Override
	protected void doSave() throws Exception {
		this.context.save(this.uploadImage);
	}

	@Override
	public void notifySaved() {
		notifySaved(getTranslation("views.addresses.save_success", //$NON-NLS-1$
				getEditedEntity().getName()));
	}

	@Override
	public void notifyValidated() {
		notifyValidated(getTranslation("views.addresses.validation_success", //$NON-NLS-1$
				getEditedEntity().getName()));
	}

	@Override
	public void notifySavingError(Throwable error) {
		notifySavingError(error, getTranslation("views.addresses.save_error", //$NON-NLS-1$ 
				getEditedEntity().getName(), error.getLocalizedMessage()));
	}

	@Override
	public void notifyValidationError(Throwable error) {
		notifyValidationError(error, getTranslation("views.addresses.validation_error", //$NON-NLS-1$ 
				getEditedEntity().getName(), error.getLocalizedMessage()));
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.postalInformationDetails.setSummaryText(getTranslation("views.addresses.postal_informations")); //$NON-NLS-1$
		this.name.setLabel(getTranslation("views.addresses.name")); //$NON-NLS-1$
		this.name.setHelperText(getTranslation("views.addresses.name.helper")); //$NON-NLS-1$
		this.name.setErrorMessage(getTranslation("views.addresses.name.error")); //$NON-NLS-1$
		this.complement.setLabel(getTranslation("views.addresses.complement")); //$NON-NLS-1$
		this.complement.setHelperText(getTranslation("views.addresses.complement.helper")); //$NON-NLS-1$
		this.street.setLabel(getTranslation("views.addresses.street")); //$NON-NLS-1$
		this.street.setHelperText(getTranslation("views.addresses.street.helper")); //$NON-NLS-1$
		this.street.setErrorMessage(getTranslation("views.addresses.street.error")); //$NON-NLS-1$
		this.zipCode.setLabel(getTranslation("views.addresses.zip_code")); //$NON-NLS-1$
		this.zipCode.setErrorMessage(getTranslation("views.addresses.zip_code.error")); //$NON-NLS-1$
		this.city.setLabel(getTranslation("views.addresses.city")); //$NON-NLS-1$
		this.city.setErrorMessage(getTranslation("views.addresses.city.error")); //$NON-NLS-1$

		this.geographicDetails.setSummaryText(getTranslation("views.addresses.geographic_informations")); //$NON-NLS-1$
		this.gps.setLabel(getTranslation("views.addresses.gps")); //$NON-NLS-1$
		this.gps.setHelperText(getTranslation("views.addresses.gps.helper")); //$NON-NLS-1$
		//this.gps.setErrorMessage(getTranslation("views.addresses.gps.error")); //$NON-NLS-1$
		this.googleMapUrl.setLabel(getTranslation("views.addresses.googlemap")); //$NON-NLS-1$
		this.googleMapUrl.setHelperText(getTranslation("views.addresses.googlemap.helper")); //$NON-NLS-1$

		this.presentationDetails.setSummaryText(getTranslation("views.addresses.presentation_informations")); //$NON-NLS-1$
		this.uploadImage.setLabel(getTranslation("views.addresses.background_image")); //$NON-NLS-1$
		this.uploadImage.setHelperText(getTranslation("views.addresses.background_image.helper")); //$NON-NLS-1$
	}

}
