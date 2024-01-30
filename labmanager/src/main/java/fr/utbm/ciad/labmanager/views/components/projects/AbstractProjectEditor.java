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

package fr.utbm.ciad.labmanager.views.components.projects;

import java.util.function.Consumer;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.FloatRangeValidator;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.project.Project;
import fr.utbm.ciad.labmanager.data.project.ProjectActivityType;
import fr.utbm.ciad.labmanager.data.project.ProjectContractType;
import fr.utbm.ciad.labmanager.data.project.ProjectStatus;
import fr.utbm.ciad.labmanager.data.project.ProjectWebPageNaming;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityEditingContext;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.utils.trl.TRL;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.converters.DoubleToFloatConverter;
import fr.utbm.ciad.labmanager.views.components.addons.converters.StringTrimer;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMark;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMarkStatusHandler;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityEditor;
import fr.utbm.ciad.labmanager.views.components.addons.uploads.image.ServerSideUploadableImageField;
import fr.utbm.ciad.labmanager.views.components.addons.uploads.pdf.ServerSideUploadablePdfField;
import fr.utbm.ciad.labmanager.views.components.addons.uploads.powerpoint.ServerSideUploadablePowerpointField;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotEmptyStringValidator;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotNullDateValidator;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotNullEnumerationValidator;
import fr.utbm.ciad.labmanager.views.components.addons.validators.UrlValidator;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation for the editor of the information related to a project.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Uses(Icon.class)
public abstract class AbstractProjectEditor extends AbstractEntityEditor<Project> {

	private static final long serialVersionUID = -6206260640976206891L;

	private DetailsWithErrorMark descriptionDetails;

	private TextField acronym;

	private TextField scientificTitle;

	private ComboBox<ProjectStatus> projectStatus;

	private DatePicker startDate;

	private IntegerField duration;

	private DetailsWithErrorMark fundingDetails;

	private NumberField globalBudget;

	private DetailsWithErrorMark innovationDetails;

	private Checkbox openSource;

	private ComboBox<ProjectActivityType> activityType;

	private ComboBox<ProjectContractType> contractType;

	private ComboBox<TRL> trl;

	private DetailsWithErrorMark presentationDetails;

	private Checkbox confidential;

	private TextArea description;

	private ServerSideUploadablePdfField requirements;

	private ServerSideUploadablePowerpointField powerpoint;

	private DetailsWithErrorMark communicationDetails;

	private ServerSideUploadableImageField logo;

	private ComboBox<ProjectWebPageNaming> webpageConvention;

	private TextField projectUrl;
	
	private ServerSideUploadablePdfField pressDocument;

	private final DownloadableFileManager fileManager;

	/** Constructor.
	 *
	 * @param context the context for editing the project.
	 * @param relinkEntityWhenSaving indicates if the editor must be relink to the edited entity when it is saved. This new link may
	 *     be required if the editor is not closed after saving in order to obtain a correct editing of the entity.
	 * @param fileManager the manager of the downloadable files.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 * @param logger the logger to be used by this view.
	 */
	public AbstractProjectEditor(EntityEditingContext<Project> context, boolean relinkEntityWhenSaving, DownloadableFileManager fileManager,
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages, Logger logger) {
		super(Project.class, authenticatedUser, messages, logger,
				"views.projects.administration_details", //$NON-NLS-1$
				"views.projects.administration.validated_project", //$NON-NLS-1$
				context, relinkEntityWhenSaving);
		this.fileManager = fileManager;
	}

	@Override
	protected void createEditorContent(VerticalLayout rootContainer) {
		createDescriptionDetails(rootContainer);
		createFundingDetails(rootContainer);
		createInnovationDetails(rootContainer);
		createPresentationDetails(rootContainer);
		createCommunicationDetails(rootContainer);
		if (isBaseAdmin()) {
			Consumer<FormLayout> builderCallback = null;
			if (isAdvancedAdmin()) {
				builderCallback = content -> {
					this.webpageConvention = new ComboBox<>();
					this.webpageConvention.setItems(ProjectWebPageNaming.values());
					this.webpageConvention.setItemLabelGenerator(this::getWebpageNamingLabel);
					this.webpageConvention.setValue(ProjectWebPageNaming.UNSPECIFIED);
					content.add(this.webpageConvention, 1);

					getEntityDataBinder().forField(this.webpageConvention).bind(Project::getWebPageNaming, Project::setWebPageNaming);
				};
			}
			createAdministrationComponents(rootContainer, builderCallback,
					it -> it.bind(Project::isValidated, Project::setValidated));
		}
	}

	private String getWebpageNamingLabel(ProjectWebPageNaming naming) {
		return naming.getLabel(getMessageSourceAccessor(), getLocale());
	}

	/** Create the section for editing the description of the project.
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

		this.scientificTitle = new TextField();
		this.scientificTitle.setPrefixComponent(VaadinIcon.HASH.create());
		this.scientificTitle.setRequired(true);
		this.scientificTitle.setClearButtonVisible(true);
		content.add(this.scientificTitle, 2);

		this.projectStatus = new ComboBox<>();
		this.projectStatus.setRequired(true);
		this.projectStatus.setItems(ProjectStatus.values());
		this.projectStatus.setItemLabelGenerator(this::getProjectStatusLabel);
		this.projectStatus.setPrefixComponent(VaadinIcon.ELLIPSIS_CIRCLE_O.create());
		content.add(this.projectStatus, 2);

		this.startDate = new DatePicker();
		this.startDate.setRequired(true);
		this.startDate.setPrefixComponent(VaadinIcon.SIGN_IN_ALT.create());
		this.startDate.setClearButtonVisible(true);
		content.add(this.startDate, 1);

		this.duration = new IntegerField();
		this.duration.setRequired(true);
		this.duration.setMin(0);
		this.duration.setPrefixComponent(VaadinIcon.TIMER.create());
		this.duration.setClearButtonVisible(true);
		content.add(this.duration, 1);

		this.descriptionDetails = new DetailsWithErrorMark(content);
		this.descriptionDetails.setOpened(false);
		rootContainer.add(this.descriptionDetails);

		getEntityDataBinder().forField(this.acronym)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.projects.acronym.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.acronym, this.descriptionDetails))
			.bind(Project::getAcronym, Project::setAcronym);
		getEntityDataBinder().forField(this.scientificTitle)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.projects.title.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.scientificTitle, this.descriptionDetails))
			.bind(Project::getScientificTitle, Project::setScientificTitle);
		getEntityDataBinder().forField(this.projectStatus)
			.withValidator(new NotNullEnumerationValidator<>(getTranslation("views.projects.status.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.projectStatus, this.descriptionDetails))
			.bind(Project::getStatus, Project::setStatus);
		getEntityDataBinder().forField(this.startDate)
			.withValidator(new NotNullDateValidator(getTranslation("views.projects.start_date.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.startDate, this.descriptionDetails))
			.bind(Project::getStartDate, Project::setStartDate);
		getEntityDataBinder().forField(this.duration)
			.withValidator(new IntegerRangeValidator(getTranslation("views.projects.duration.error"), Integer.valueOf(0), null)) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.duration, this.descriptionDetails))
			.bind(Project::getDuration, Project::setDuration);
	}

	private String getProjectStatusLabel(ProjectStatus status) {
		return status.getLabel(getMessageSourceAccessor(), getLocale());
	}

	/** Create the section for editing the funding of the project.
	 *
	 * @param rootContainer the container.
	 */
	protected void createFundingDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.globalBudget = new NumberField();
		this.globalBudget.setPrefixComponent(VaadinIcon.EURO.create());
		this.globalBudget.setClearButtonVisible(true);
		content.add(this.globalBudget, 2);

		this.fundingDetails = new DetailsWithErrorMark(content);
		this.fundingDetails.setOpened(false);
		rootContainer.add(this.fundingDetails);

		getEntityDataBinder().forField(this.globalBudget)
			.withConverter(new DoubleToFloatConverter())
			.withValidator(new FloatRangeValidator(getTranslation("views.projects.global_budget.error"), Float.valueOf(0f), null)) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.globalBudget, this.fundingDetails))
			.bind(Project::getGlobalBudget, Project::setGlobalBudget);
	}

	/** Create the section for editing the innovation elements of the project.
	 *
	 * @param rootContainer the container.
	 */
	protected void createInnovationDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.activityType = new ComboBox<>();
		this.activityType.setRequired(true);
		this.activityType.setItems(ProjectActivityType.values());
		this.activityType.setItemLabelGenerator(this::getActivityTypeLabel);
		this.activityType.setPrefixComponent(VaadinIcon.CLUSTER.create());
		content.add(this.activityType, 2);

		this.contractType = new ComboBox<>();
		this.contractType.setRequired(true);
		this.contractType.setItems(ProjectContractType.values());
		this.contractType.setItemLabelGenerator(this::getContractTypeLabel);
		this.contractType.setPrefixComponent(VaadinIcon.HANDSHAKE.create());
		content.add(this.contractType, 2);

		this.trl = new ComboBox<>();
		this.trl.setRequired(true);
		this.trl.setItems(TRL.values());
		this.trl.setItemLabelGenerator(this::getTrlLabel);
		this.trl.setPrefixComponent(VaadinIcon.LIST_OL.create());
		content.add(this.trl, 2);

		this.openSource = new Checkbox();
		content.add(this.openSource, 2);

		this.innovationDetails = new DetailsWithErrorMark(content);
		this.innovationDetails.setOpened(false);
		rootContainer.add(this.innovationDetails);

		getEntityDataBinder().forField(this.activityType)
			.withValidator(new NotNullEnumerationValidator<>(getTranslation("views.projects.activity_type.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.activityType, this.innovationDetails))
			.bind(Project::getActivityType, Project::setActivityType);
		getEntityDataBinder().forField(this.contractType)
			.withValidator(new NotNullEnumerationValidator<>(getTranslation("views.projects.contract_type.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.contractType, this.innovationDetails))
			.bind(Project::getContractType, Project::setContractType);
		getEntityDataBinder().forField(this.trl)
			.withValidator(new NotNullEnumerationValidator<>(getTranslation("views.projects.trl.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.trl, this.innovationDetails))
			.bind(Project::getTRL, Project::setTRL);
		getEntityDataBinder().forField(this.openSource)
			.bind(Project::isOpenSource, Project::setOpenSource);
	}

	private String getActivityTypeLabel(ProjectActivityType type) {
		return type.getLabel(getMessageSourceAccessor(), getLocale());
	}
	
	private String getContractTypeLabel(ProjectContractType type) {
		if (type == ProjectContractType.NOT_SPECIFIED) {
			return type.getLabel(getMessageSourceAccessor(), getLocale());
		}
		return new StringBuilder()
				.append(type.name()).append(" - ") //$NON-NLS-1$
				.append(type.getLabel(getMessageSourceAccessor(), getLocale()))
				.toString();
	}

	private String getTrlLabel(TRL trl) {
		return new StringBuilder()
				.append(trl.name()).append(" - ") //$NON-NLS-1$
				.append(trl.getLabel(getMessageSourceAccessor(), getLocale()))
				.toString();
	}

	/** Create the section for editing the presentation elements of the project.
	 *
	 * @param rootContainer the container.
	 */
	protected void createPresentationDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.confidential = new Checkbox();
		content.add(this.confidential, 2);

		this.description = new TextArea();
		this.description.setPrefixComponent(VaadinIcon.TEXT_INPUT.create());
		content.add(this.description, 2);

		this.requirements = new ServerSideUploadablePdfField(this.fileManager,
				ext -> this.fileManager.makeProjectScientificRequirementsFilename(getEditedEntity().getId()));
		this.requirements.setClearButtonVisible(true);
		content.add(this.requirements, 1);

		this.powerpoint = new ServerSideUploadablePowerpointField(this.fileManager,
				ext -> this.fileManager.makeProjectPowerpointFilename(getEditedEntity().getId(), ext));
		this.powerpoint.setClearButtonVisible(true);
		content.add(this.powerpoint, 1);

		this.presentationDetails = new DetailsWithErrorMark(content);
		this.presentationDetails.setOpened(false);
		rootContainer.add(this.presentationDetails);

		getEntityDataBinder().forField(this.confidential)
			.bind(Project::isConfidential, Project::setConfidential);
		getEntityDataBinder().forField(this.description)
			.withConverter(new StringTrimer())
			.bind(Project::getDescription, Project::setDescription);
		getEntityDataBinder().forField(this.requirements)
			.withConverter(new StringTrimer())
			.bind(Project::getPathToScientificRequirements, Project::setPathToScientificRequirements);
		getEntityDataBinder().forField(this.powerpoint)
			.withConverter(new StringTrimer())
			.bind(Project::getPathToPowerpoint, Project::setPathToPowerpoint);
	}

	/** Create the section for editing the communication elements of the project.
	 *
	 * @param rootContainer the container.
	 */
	protected void createCommunicationDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.logo = new ServerSideUploadableImageField(this.fileManager,
				ext -> this.fileManager.makeProjectLogoFilename(getEditedEntity().getId(), ext));
		this.logo.setClearButtonVisible(true);
		content.add(this.logo, 1);

		this.projectUrl = new TextField();
		this.projectUrl.setClearButtonVisible(true);
		this.projectUrl.setPrefixComponent(VaadinIcon.GLOBE_WIRE.create());
		content.add(this.projectUrl, 2);
		
		this.pressDocument = new ServerSideUploadablePdfField(this.fileManager,
				ext -> this.fileManager.makeProjectPressDocumentFilename(getEditedEntity().getId()));
		this.pressDocument.setClearButtonVisible(true);
		content.add(this.pressDocument, 1);

		this.communicationDetails = new DetailsWithErrorMark(content);
		this.communicationDetails.setOpened(false);
		rootContainer.add(this.communicationDetails);

		getEntityDataBinder().forField(this.logo)
			.withConverter(new StringTrimer())
			.bind(Project::getPathToLogo, Project::setPathToLogo);
		getEntityDataBinder().forField(this.projectUrl)
			.withConverter(new StringTrimer())
			.withValidator(new UrlValidator(getTranslation("views.projects.url.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.projectUrl, this.communicationDetails))
			.bind(Project::getProjectURL, Project::setProjectURL);
		getEntityDataBinder().forField(this.pressDocument)
			.withConverter(new StringTrimer())
			.bind(Project::getPathToPressDocument, Project::setPathToPressDocument);
	}

	@Override
	protected void doSave() throws Exception {
		getEditingContext().save(this.logo, this.pressDocument, this.requirements, this.powerpoint);
	}

	@Override
	protected String computeSavingSuccessMessage() {
		return getTranslation("views.projects.save_success", //$NON-NLS-1$
				getEditedEntity().getAcronymOrScientificTitle());
	}

	@Override
	protected String computeValidationSuccessMessage() {
		return getTranslation("views.projects.validation_success", //$NON-NLS-1$
				getEditedEntity().getAcronymOrScientificTitle());
	}

	@Override
	protected String computeDeletionSuccessMessage() {
		return getTranslation("views.projects.delete_success2", //$NON-NLS-1$
				getEditedEntity().getAcronymOrScientificTitle());
	}

	@Override
	protected String computeSavingErrorMessage(Throwable error) {
		return getTranslation("views.projects.save_error", //$NON-NLS-1$ 
				getEditedEntity().getAcronymOrScientificTitle(), error.getLocalizedMessage());
	}

	@Override
	protected String computeValidationErrorMessage(Throwable error) {
		return getTranslation("views.projects.validation_error", //$NON-NLS-1$ 
				getEditedEntity().getAcronymOrScientificTitle(), error.getLocalizedMessage());
	}

	@Override
	protected String computeDeletionErrorMessage(Throwable error) {
		return getTranslation("views.projects.delete_error2", //$NON-NLS-1$ 
				getEditedEntity().getAcronymOrScientificTitle(), error.getLocalizedMessage());
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.descriptionDetails.setSummaryText(getTranslation("views.projects.description_details")); //$NON-NLS-1$
		this.acronym.setLabel(getTranslation("views.projects.acronym")); //$NON-NLS-1$
		this.acronym.setHelperText(getTranslation("views.projects.acronym.help")); //$NON-NLS-1$
		this.scientificTitle.setLabel(getTranslation("views.projects.title")); //$NON-NLS-1$
		this.scientificTitle.setHelperText(getTranslation("views.projects.title.help")); //$NON-NLS-1$
		this.projectStatus.setLabel(getTranslation("views.projects.status")); //$NON-NLS-1$
		this.projectStatus.setHelperText(getTranslation("views.projects.status.help")); //$NON-NLS-1$
		this.projectStatus.setItemLabelGenerator(this::getProjectStatusLabel);
		this.startDate.setLabel(getTranslation("views.projects.start_date")); //$NON-NLS-1$
		this.startDate.setHelperText(getTranslation("views.projects.start_date.help")); //$NON-NLS-1$
		this.duration.setLabel(getTranslation("views.projects.duration")); //$NON-NLS-1$
		this.duration.setHelperText(getTranslation("views.projects.duration.help")); //$NON-NLS-1$

		this.fundingDetails.setSummaryText(getTranslation("views.projects.funding_details")); //$NON-NLS-1$
		this.globalBudget.setLabel(getTranslation("views.projects.global_budget")); //$NON-NLS-1$
		this.globalBudget.setHelperText(getTranslation("views.projects.global_budget.help")); //$NON-NLS-1$

		this.innovationDetails.setSummaryText(getTranslation("views.projects.innovation_details")); //$NON-NLS-1$
		this.activityType.setLabel(getTranslation("views.projects.activity_type")); //$NON-NLS-1$
		this.activityType.setHelperText(getTranslation("views.projects.activity_type.help")); //$NON-NLS-1$
		this.activityType.setItemLabelGenerator(this::getActivityTypeLabel);
		this.contractType.setLabel(getTranslation("views.projects.contract_type")); //$NON-NLS-1$
		this.contractType.setHelperText(getTranslation("views.projects.contract_type.help")); //$NON-NLS-1$
		this.contractType.setItemLabelGenerator(this::getContractTypeLabel);
		this.trl.setLabel(getTranslation("views.projects.trl")); //$NON-NLS-1$
		this.trl.setHelperText(getTranslation("views.projects.trl.help")); //$NON-NLS-1$
		this.trl.setItemLabelGenerator(this::getTrlLabel);
		this.openSource.setLabel(getTranslation("views.projects.open_source")); //$NON-NLS-1$

		this.presentationDetails.setSummaryText(getTranslation("views.projects.presentation_details")); //$NON-NLS-1$
		this.confidential.setLabel(getTranslation("views.projects.confidential")); //$NON-NLS-1$
		this.description.setLabel(getTranslation("views.projects.description")); //$NON-NLS-1$
		this.description.setHelperText(getTranslation("views.projects.description.help")); //$NON-NLS-1$
		this.requirements.setLabel(getTranslation("views.projects.requirements")); //$NON-NLS-1$
		this.powerpoint.setLabel(getTranslation("views.projects.powerpoint")); //$NON-NLS-1$

		this.communicationDetails.setSummaryText(getTranslation("views.projects.communication_details")); //$NON-NLS-1$
		this.logo.setLabel(getTranslation("views.projects.logo")); //$NON-NLS-1$
		this.projectUrl.setLabel(getTranslation("views.projects.url")); //$NON-NLS-1$
		this.projectUrl.setHelperText(getTranslation("views.projects.url.help")); //$NON-NLS-1$
		this.pressDocument.setLabel(getTranslation("views.projects.press_document")); //$NON-NLS-1$

		this.webpageConvention.setLabel(getTranslation("views.projects.webpage_convention")); //$NON-NLS-1$
	}

}
