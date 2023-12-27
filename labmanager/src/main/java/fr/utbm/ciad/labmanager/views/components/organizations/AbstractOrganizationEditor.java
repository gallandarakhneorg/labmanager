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

import static fr.utbm.ciad.labmanager.views.ViewConstants.DBLP_BASE_URL;
import static fr.utbm.ciad.labmanager.views.ViewConstants.DBLP_ICON;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganizationType;
import fr.utbm.ciad.labmanager.services.organization.ResearchOrganizationService.EditingContext;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.converters.StringTrimer;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMark;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMarkStatusHandler;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityEditor;
import fr.utbm.ciad.labmanager.views.components.addons.uploads.image.ServerSideUploadableImageField;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotEmptyStringValidator;
import fr.utbm.ciad.labmanager.views.components.addons.validators.UrlValidator;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation for the editor of the information related to an organization.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Uses(Icon.class)
public abstract class AbstractOrganizationEditor extends AbstractEntityEditor<ResearchOrganization> {

	private static final long serialVersionUID = -4405231976086480507L;

	private DetailsWithErrorMark descriptionDetails;

	private TextField acronym;

	private TextField name;

	private ComboBox<ResearchOrganizationType> type;

	private ComboBox<CountryCode> country;

	private Details identificationDetails;

	private TextField nationalIdentifier;

	private TextField rnsr;

	private DetailsWithErrorMark communicationDetails;

	private TextField organizationUrl;

	private TextArea description;

	private ServerSideUploadableImageField logo;

	private Checkbox majorOrganization;

	private final DownloadableFileManager fileManager;

	private final EditingContext editingContext;

	/** Constructor.
	 *
	 * @param context the context for editing the entity.
	 * @param fileManager the manager of the file system at server-side.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 * @param logger the logger to be used by this view.
	 */
	public AbstractOrganizationEditor(EditingContext context, 
			DownloadableFileManager fileManager, AuthenticatedUser authenticatedUser,
			MessageSourceAccessor messages, Logger logger) {
		super(ResearchOrganization.class, authenticatedUser, messages, logger,
				"views.organizations.administration_details", //$NON-NLS-1$
				"views.organizations.administration.validated_organization"); //$NON-NLS-1$
		this.fileManager = fileManager;
		this.editingContext = context;
	}
	
	@Override
	public ResearchOrganization getEditedEntity() {
		return this.editingContext.getOrganization();
	}

	@Override
	protected void createEditorContent(VerticalLayout rootContainer) {
		createDescriptionDetails(rootContainer);
		createIdentificationDetails(rootContainer);
		createCommunicationDetails(rootContainer);
		if (isBaseAdmin()) {
			createAdministrationComponents(rootContainer,
					content -> {
						this.majorOrganization = new Checkbox();
						content.add(this.majorOrganization, 1);

						getEntityDataBinder().forField(this.majorOrganization)
							.bind(ResearchOrganization::isMajorOrganization, ResearchOrganization::setMajorOrganization);
					},
					it -> it.bind(ResearchOrganization::isValidated, ResearchOrganization::setValidated));
		}
	}

	/** Create the section for editing the description of the organization.
	 *
	 * @param rootContainer the container.
	 */
	protected void createDescriptionDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.acronym = new TextField();
		this.acronym.setPrefixComponent(VaadinIcon.HASH.create());
		this.acronym.setRequired(true);
		this.acronym.setClearButtonVisible(true);
		content.add(this.acronym, 1);

		this.name = new TextField();
		this.name.setPrefixComponent(VaadinIcon.HASH.create());
		this.name.setRequired(true);
		this.name.setClearButtonVisible(true);
		content.add(this.name, 2);

		this.type = new ComboBox<>();
		this.type.setPrefixComponent(VaadinIcon.DIPLOMA.create());
		this.type.setItems(ResearchOrganizationType.values());
		this.type.setItemLabelGenerator(this::getTypeLabel);
		this.type.setValue(ResearchOrganizationType.DEFAULT);
		content.add(this.type, 2);

		this.country = ComponentFactory.newCountryComboBox(getLocale());
		this.country.setPrefixComponent(VaadinIcon.GLOBE_WIRE.create());
		content.add(this.country, 1);

		this.descriptionDetails = new DetailsWithErrorMark(content);
		this.descriptionDetails.setOpened(false);
		rootContainer.add(this.descriptionDetails);
		
		getEntityDataBinder().forField(this.acronym)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.organizations.acronym.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.acronym, this.descriptionDetails))
			.bind(ResearchOrganization::getAcronym, ResearchOrganization::setAcronym);
		getEntityDataBinder().forField(this.name)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.organizations.name.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.name, this.descriptionDetails))
			.bind(ResearchOrganization::getName, ResearchOrganization::setName);
		getEntityDataBinder().forField(this.type).bind(ResearchOrganization::getType, ResearchOrganization::setType);
		getEntityDataBinder().forField(this.country).bind(ResearchOrganization::getCountry, ResearchOrganization::setCountry);
	}

	/** Create the section for editing the identification of the organization.
	 *
	 * @param rootContainer the container.
	 */
	protected void createIdentificationDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.nationalIdentifier = new TextField();
		this.nationalIdentifier.setPrefixComponent(VaadinIcon.HASH.create());
		this.nationalIdentifier.setClearButtonVisible(true);
		content.add(this.nationalIdentifier, 2);

		this.rnsr = new TextField();
		this.rnsr.setPrefixComponent(VaadinIcon.HASH.create());
		this.rnsr.setClearButtonVisible(true);
		content.add(this.rnsr, 2);

		this.identificationDetails = new DetailsWithErrorMark(content);
		this.identificationDetails.setOpened(false);
		rootContainer.add(this.identificationDetails);
		
		getEntityDataBinder().forField(this.nationalIdentifier)
			.withConverter(new StringTrimer())
			.bind(ResearchOrganization::getNationalIdentifier, ResearchOrganization::setNationalIdentifier);
		getEntityDataBinder().forField(this.rnsr)
			.withConverter(new StringTrimer())
			.bind(ResearchOrganization::getRnsr, ResearchOrganization::setRnsr);
	}

	/** Create the section for editing the communication of the organization.
	 *
	 * @param rootContainer the container.
	 */
	protected void createCommunicationDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.organizationUrl = new TextField();
		this.organizationUrl = ComponentFactory.newClickableIconTextField(DBLP_BASE_URL, DBLP_ICON);
		this.organizationUrl.setClearButtonVisible(true);
		this.organizationUrl.setPrefixComponent(VaadinIcon.EXTERNAL_LINK.create());
		content.add(this.organizationUrl, 2);

		this.description = new TextArea();
		this.description.setClearButtonVisible(true);
		this.description.setPrefixComponent(VaadinIcon.INFO_CIRCLE_O.create());
		content.add(this.description, 2);

		this.logo = new ServerSideUploadableImageField(this.fileManager,
				ext -> this.fileManager.makeOrganizationLogoFilename(getEditedEntity().getId(), ext));
		this.logo.setClearButtonVisible(true);
		content.add(this.logo, 2);

		this.communicationDetails = new DetailsWithErrorMark(content);
		this.communicationDetails.setOpened(false);
		rootContainer.add(this.communicationDetails);
		
		final var invalidUrl = getTranslation("views.urls.invalid_format"); //$NON-NLS-1$

		getEntityDataBinder().forField(this.organizationUrl)
			.withConverter(new StringTrimer())
			.withValidator(new UrlValidator(invalidUrl, true))
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.organizationUrl, this.communicationDetails))
			.bind(ResearchOrganization::getOrganizationURL, ResearchOrganization::setOrganizationURL);
		getEntityDataBinder().forField(this.description)
			.withConverter(new StringTrimer())
			.bind(ResearchOrganization::getDescription, ResearchOrganization::setDescription);
		getEntityDataBinder().forField(this.logo)
			.withConverter(new StringTrimer())
			.bind(ResearchOrganization::getPathToLogo, ResearchOrganization::setPathToLogo);
	}

	private String getTypeLabel(ResearchOrganizationType type) {
		return type.getLabel(getMessageSourceAccessor(), getLocale());
	}

	@Override
	protected void doSave() throws Exception {
		this.editingContext.save(this.logo);
	}

	@Override
	public void notifySaved() {
		notifySaved(getTranslation("views.organizations.save_success", //$NON-NLS-1$
				getEditedEntity().getName()));
	}

	@Override
	public void notifyValidated() {
		notifyValidated(getTranslation("views.organizations.validation_success", //$NON-NLS-1$
				getEditedEntity().getName()));
	}

	@Override
	public void notifySavingError(Throwable error) {
		notifySavingError(error, getTranslation("views.organizations.save_error", //$NON-NLS-1$ 
				getEditedEntity().getName(), error.getLocalizedMessage()));
	}

	@Override
	public void notifyValidationError(Throwable error) {
		notifyValidationError(error, getTranslation("views.organizations.validation_error", //$NON-NLS-1$ 
				getEditedEntity().getName(), error.getLocalizedMessage()));
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);

		this.descriptionDetails.setSummaryText(getTranslation("views.organizations.description_informations")); //$NON-NLS-1$
		this.acronym.setLabel(getTranslation("views.organizations.acronym")); //$NON-NLS-1$
		this.name.setLabel(getTranslation("views.organizations.name")); //$NON-NLS-1$
		this.name.setHelperText(getTranslation("views.organizations.name.helper")); //$NON-NLS-1$
		this.type.setLabel(getTranslation("views.organizations.type")); //$NON-NLS-1$
		this.type.setHelperText(getTranslation("views.organizations.type.helper")); //$NON-NLS-1$);
		this.type.setItemLabelGenerator(this::getTypeLabel);
		this.country.setLabel(getTranslation("views.organizations.country")); //$NON-NLS-1$
		ComponentFactory.updateCountryComboBoxItems(this.country, getLocale());

		this.identificationDetails.setSummaryText(getTranslation("views.organizations.identification_informations")); //$NON-NLS-1$
		this.nationalIdentifier.setLabel(getTranslation("views.organizations.national_identifier")); //$NON-NLS-1$
		this.nationalIdentifier.setHelperText(getTranslation("views.organizations.national_identifier.helper")); //$NON-NLS-1$
		this.rnsr.setLabel(getTranslation("views.organizations.rnsr")); //$NON-NLS-1$
		this.rnsr.setHelperText(getTranslation("views.organizations.rnsr.helper")); //$NON-NLS-1$

		this.communicationDetails.setSummaryText(getTranslation("views.organizations.communication_informations")); //$NON-NLS-1$
		this.organizationUrl.setLabel(getTranslation("views.organizations.url")); //$NON-NLS-1$
		this.description.setLabel(getTranslation("views.organizations.description")); //$NON-NLS-1$
		this.logo.setLabel(getTranslation("views.organizations.logo")); //$NON-NLS-1$

		this.majorOrganization.setLabel(getTranslation("views.organizations.major_organization")); //$NON-NLS-1$
	}

}
