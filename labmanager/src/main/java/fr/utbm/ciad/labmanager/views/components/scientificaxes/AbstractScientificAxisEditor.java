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

package fr.utbm.ciad.labmanager.views.components.scientificaxes;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.scientificaxis.ScientificAxis;
import fr.utbm.ciad.labmanager.services.scientificaxis.ScientificAxisService.EditingContext;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.converters.StringTrimer;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMark;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMarkStatusHandler;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityEditor;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotEmptyStringValidator;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation for the editor of the information related to a scientific axis.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Uses(Icon.class)
public abstract class AbstractScientificAxisEditor extends AbstractEntityEditor<ScientificAxis> {

	private static final long serialVersionUID = -4153205626114372905L;

	private DetailsWithErrorMark descriptionDetails;

	private TextField acronym;

	private TextField name;

	private DatePicker startDate;

	private DatePicker endDate;

	private final EditingContext context;

	/** Constructor.
	 *
	 * @param context the editing context for the scientific axis.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 * @param logger the logger to be used by this view.
	 */
	public AbstractScientificAxisEditor(EditingContext context, AuthenticatedUser authenticatedUser,
			MessageSourceAccessor messages, Logger logger) {
		super(ScientificAxis.class, authenticatedUser, messages, logger,
				"views.scientific_axes.administration_details", //$NON-NLS-1$
				"views.scientific_axes.administration.validated_address"); //$NON-NLS-1$
		this.context = context;
	}

	@Override
	public ScientificAxis getEditedEntity() {
		return this.context.getScientificAxis();
	}

	@Override
	protected void createEditorContent(VerticalLayout rootContainer) {
		createDescriptionDetails(rootContainer);
		if (isBaseAdmin()) {
			createAdministrationComponents(rootContainer,
					null,
					it -> it.bind(ScientificAxis::isValidated, ScientificAxis::setValidated));
		}
	}

	/** Create the section for editing the description of the scientific axis.
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
		content.add(this.name, 1);

		this.startDate = new DatePicker();
		this.startDate.setPrefixComponent(VaadinIcon.SIGN_IN_ALT.create());
		this.startDate.setClearButtonVisible(true);
		content.add(this.startDate, 1);

		this.endDate = new DatePicker();
		this.endDate.setPrefixComponent(VaadinIcon.SIGN_OUT_ALT.create());
		this.endDate.setClearButtonVisible(true);
		content.add(this.endDate, 1);

		this.descriptionDetails = new DetailsWithErrorMark(content);
		this.descriptionDetails.setOpened(false);
		rootContainer.add(this.descriptionDetails);

		getEntityDataBinder().forField(this.acronym)
		.withConverter(new StringTrimer())
		.withValidator(new NotEmptyStringValidator(getTranslation("views.scientific_axes.acronym.error"))) //$NON-NLS-1$
		.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.acronym, this.descriptionDetails))
		.bind(ScientificAxis::getAcronym, ScientificAxis::setAcronym);
		getEntityDataBinder().forField(this.name)
		.withConverter(new StringTrimer())
		.withValidator(new NotEmptyStringValidator(getTranslation("views.scientific_axes.name.error"))) //$NON-NLS-1$
		.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.name, this.descriptionDetails))
		.bind(ScientificAxis::getName, ScientificAxis::setName);
		getEntityDataBinder().forField(this.startDate)
		.bind(ScientificAxis::getStartDate, ScientificAxis::setStartDate);
		getEntityDataBinder().forField(this.endDate)
		.bind(ScientificAxis::getEndDate, ScientificAxis::setEndDate);
	}

	@Override
	protected void doSave() throws Exception {
		this.context.save();
	}

	@Override
	public void notifySaved() {
		notifySaved(getTranslation("views.scientific_axes.save_success", //$NON-NLS-1$
				getEditedEntity().getName()));
	}

	@Override
	public void notifyValidated() {
		notifyValidated(getTranslation("views.scientific_axes.validation_success", //$NON-NLS-1$
				getEditedEntity().getName()));
	}

	@Override
	public void notifySavingError(Throwable error) {
		notifySavingError(error, getTranslation("views.scientific_axes.save_error", //$NON-NLS-1$ 
				getEditedEntity().getName(), error.getLocalizedMessage()));
	}

	@Override
	public void notifyValidationError(Throwable error) {
		notifyValidationError(error, getTranslation("views.scientific_axes.validation_error", //$NON-NLS-1$ 
				getEditedEntity().getName(), error.getLocalizedMessage()));
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.descriptionDetails.setSummaryText(getTranslation("views.scientific_axes.description_details")); //$NON-NLS-1$
		this.acronym.setLabel(getTranslation("views.scientific_axes.acronym")); //$NON-NLS-1$
		this.acronym.setHelperText(getTranslation("views.scientific_axes.acronym.helper")); //$NON-NLS-1$
		this.name.setLabel(getTranslation("views.scientific_axes.name")); //$NON-NLS-1$
		this.name.setHelperText(getTranslation("views.scientific_axes.name.helper")); //$NON-NLS-1$
		this.startDate.setLocale(event.getLocale());
		this.startDate.setLabel(getTranslation("views.scientific_axes.start_date")); //$NON-NLS-1$
		this.startDate.setHelperText(getTranslation("views.scientific_axes.start_date.helper")); //$NON-NLS-1$
		this.endDate.setLocale(event.getLocale());
		this.endDate.setLabel(getTranslation("views.scientific_axes.end_date")); //$NON-NLS-1$
		this.endDate.setHelperText(getTranslation("views.scientific_axes.end_date.helper")); //$NON-NLS-1$
	}

}
