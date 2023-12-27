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

package fr.utbm.ciad.labmanager.views.components.assocstructures;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
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
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructure;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructureType;
import fr.utbm.ciad.labmanager.services.assostructure.AssociatedStructureService.EditingContext;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.converters.DoubleToFloatConverter;
import fr.utbm.ciad.labmanager.views.components.addons.converters.StringTrimer;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMark;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMarkStatusHandler;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityEditor;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotEmptyStringValidator;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotNullDateValidator;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotNullEnumerationValidator;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation for the editor of the information related to an associated structure.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Uses(Icon.class)
public abstract class AbstractAssociatedStructureEditor extends AbstractEntityEditor<AssociatedStructure> {

	private static final long serialVersionUID = -4585533792941598627L;

	private DetailsWithErrorMark descriptionDetails;

	private TextField acronym;

	private TextField name;

	private ComboBox<AssociatedStructureType> type;

	private DetailsWithErrorMark creationDetails;

	private DatePicker creationDate;

	private IntegerField creationDuration;

	private NumberField budget;

	private DetailsWithErrorMark communicationDetails;

	private Checkbox confidential;

	private TextArea description;

	private final EditingContext editingContext;

	/** Constructor.
	 *
	 * @param context the context for editing the entity.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 * @param logger the logger to be used by this view.
	 */
	public AbstractAssociatedStructureEditor(EditingContext context, 
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages, Logger logger) {
		super(AssociatedStructure.class, authenticatedUser, messages, logger,
				"views.associated_structure.administration_details", //$NON-NLS-1$
				"views.associated_structure.administration.validated_structure"); //$NON-NLS-1$
		this.editingContext = context;
	}
	
	@Override
	public AssociatedStructure getEditedEntity() {
		return this.editingContext.getAssociatedStructure();
	}

	@Override
	protected void createEditorContent(VerticalLayout rootContainer) {
		createDescriptionDetails(rootContainer);
		createCreationDetails(rootContainer);
		createCommunicationDetails(rootContainer);
		if (isBaseAdmin()) {
			createAdministrationComponents(rootContainer,
					null,
					it -> it.bind(AssociatedStructure::isValidated, AssociatedStructure::setValidated));
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
		this.type.setPrefixComponent(VaadinIcon.FACTORY.create());
		this.type.setItems(AssociatedStructureType.getAllDisplayTypes(getMessageSourceAccessor(), getLocale()));
		this.type.setItemLabelGenerator(this::getTypeLabel);
		this.type.setValue(AssociatedStructureType.PRIVATE_COMPANY);
		content.add(this.type, 2);

		this.descriptionDetails = new DetailsWithErrorMark(content);
		this.descriptionDetails.setOpened(false);
		rootContainer.add(this.descriptionDetails);

		getEntityDataBinder().forField(this.acronym)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.associated_structure.acronym.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.acronym, this.descriptionDetails))
			.bind(AssociatedStructure::getAcronym, AssociatedStructure::setAcronym);
		getEntityDataBinder().forField(this.name)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.associated_structure.name.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.name, this.descriptionDetails))
			.bind(AssociatedStructure::getName, AssociatedStructure::setName);
		getEntityDataBinder().forField(this.type)
			.withValidator(new NotNullEnumerationValidator<>(getTranslation("views.associated_structure.type.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.type, this.descriptionDetails))
			.bind(AssociatedStructure::getType, AssociatedStructure::setType);
	}

	private String getTypeLabel(AssociatedStructureType type) {
		return type.getLabel(getMessageSourceAccessor(), getLocale());
	}
	
	/** Create the section for editing the creation of the organization.
	 *
	 * @param rootContainer the container.
	 */
	protected void createCreationDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.creationDate = new DatePicker();
		this.creationDate.setPrefixComponent(VaadinIcon.CALENDAR_O.create());
		this.creationDate.setRequired(true);
		this.creationDate.setClearButtonVisible(true);
		content.add(this.creationDate, 1);

		this.creationDuration = new IntegerField();
		this.creationDuration.setPrefixComponent(VaadinIcon.TIMER.create());
		this.creationDuration.setRequired(true);
		this.creationDuration.setClearButtonVisible(true);
		content.add(this.creationDuration, 1);

		this.budget = new NumberField();
		this.budget.setPrefixComponent(VaadinIcon.EURO.create());
		this.budget.setClearButtonVisible(true);
		content.add(this.budget, 1);

		this.creationDetails = new DetailsWithErrorMark(content);
		this.creationDetails.setOpened(false);
		rootContainer.add(this.creationDetails);

		getEntityDataBinder().forField(this.creationDate)
			.withValidator(new NotNullDateValidator(getTranslation("views.associated_structure.creation_date.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.creationDate, this.creationDetails))
			.bind(AssociatedStructure::getCreationDate, AssociatedStructure::setCreationDate);
		getEntityDataBinder().forField(this.creationDuration)
			.withValidator(new IntegerRangeValidator(getTranslation("views.associated_structure.creation_duration.error"), Integer.valueOf(0), null)) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.creationDuration, this.creationDetails))
			.bind(AssociatedStructure::getCreationDuration, AssociatedStructure::setCreationDuration);
		getEntityDataBinder().forField(this.budget)
			.withConverter(new DoubleToFloatConverter())
			.withValidator(new FloatRangeValidator(getTranslation("views.associated_structure.budget.error"), Float.valueOf(0f), null)) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.budget, this.creationDetails))
			.bind(AssociatedStructure::getBudget, AssociatedStructure::setBudget);
	}

	/** Create the section for editing the communication of the organization.
	 *
	 * @param rootContainer the container.
	 */
	protected void createCommunicationDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.confidential = new Checkbox();
		content.add(this.confidential, 1);

		this.description = new TextArea();
		this.description.setPrefixComponent(VaadinIcon.TEXT_INPUT.create());
		this.description.setClearButtonVisible(true);
		content.add(this.description, 1);

		this.communicationDetails = new DetailsWithErrorMark(content);
		this.communicationDetails.setOpened(false);
		rootContainer.add(this.communicationDetails);

		getEntityDataBinder().forField(this.confidential)
			.bind(AssociatedStructure::isConfidential, AssociatedStructure::setConfidential);
		getEntityDataBinder().forField(this.description)
			.withConverter(new StringTrimer())
			.bind(AssociatedStructure::getDescription, AssociatedStructure::setDescription);
	}

	@Override
	protected void doSave() throws Exception {
		this.editingContext.save();
	}

	@Override
	public void notifySaved() {
		notifySaved(getTranslation("views.associated_structure.save_success", //$NON-NLS-1$
				getEditedEntity().getName()));
	}

	@Override
	public void notifyValidated() {
		notifyValidated(getTranslation("views.associated_structure.validation_success", //$NON-NLS-1$
				getEditedEntity().getName()));
	}

	@Override
	public void notifySavingError(Throwable error) {
		notifySavingError(error, getTranslation("views.associated_structure.save_error", //$NON-NLS-1$ 
				getEditedEntity().getName(), error.getLocalizedMessage()));
	}

	@Override
	public void notifyValidationError(Throwable error) {
		notifyValidationError(error, getTranslation("views.associated_structure.validation_error", //$NON-NLS-1$ 
				getEditedEntity().getName(), error.getLocalizedMessage()));
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);

		this.descriptionDetails.setSummaryText(getTranslation("views.associated_structure.description_informations")); //$NON-NLS-1$
		this.acronym.setLabel(getTranslation("views.associated_structure.acronym")); //$NON-NLS-1$
		this.name.setLabel(getTranslation("views.associated_structure.name")); //$NON-NLS-1$
		this.type.setLabel(getTranslation("views.associated_structure.type")); //$NON-NLS-1$
		this.type.setItemLabelGenerator(this::getTypeLabel);

		this.creationDetails.setSummaryText(getTranslation("views.associated_structure.creation_informations")); //$NON-NLS-1$
		this.creationDate.setLabel(getTranslation("views.associated_structure.creation_date")); //$NON-NLS-1$
		this.creationDate.setHelperText(getTranslation("views.associated_structure.creation_date.help")); //$NON-NLS-1$
		this.creationDuration.setLabel(getTranslation("views.associated_structure.creation_duration")); //$NON-NLS-1$
		this.creationDuration.setHelperText(getTranslation("views.associated_structure.creation_duration.help")); //$NON-NLS-1$
		this.budget.setLabel(getTranslation("views.associated_structure.budget")); //$NON-NLS-1$
		this.budget.setHelperText(getTranslation("views.associated_structure.budget.help")); //$NON-NLS-1$

		this.communicationDetails.setSummaryText(getTranslation("views.associated_structure.communication_informations")); //$NON-NLS-1$
		this.confidential.setLabel(getTranslation("views.associated_structure.confidential")); //$NON-NLS-1$
		this.description.setLabel(getTranslation("views.associated_structure.description")); //$NON-NLS-1$
		this.description.setHelperText(getTranslation("views.associated_structure.description.help")); //$NON-NLS-1$
	}

}
