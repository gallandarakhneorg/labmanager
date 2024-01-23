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

package fr.utbm.ciad.labmanager.views.components.jurys;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.jury.JuryMembership;
import fr.utbm.ciad.labmanager.data.jury.JuryMembershipType;
import fr.utbm.ciad.labmanager.data.jury.JuryType;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityEditingContext;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.converters.StringTrimer;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMark;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMarkStatusHandler;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityEditor;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotEmptyStringValidator;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotNullDateValidator;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotNullEnumerationValidator;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation for the editor of the information related to a jury.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Uses(Icon.class)
public abstract class AbstractJuryMembershipEditor extends AbstractEntityEditor<JuryMembership> {

	private static final long serialVersionUID = -5831566011340136766L;

	private DetailsWithErrorMark descriptionDetails;

	private TextField title;

	private ComboBox<JuryType> defenseType;

	private DatePicker date;

	private TextField university;

	private ComboBox<CountryCode> country;

	private DetailsWithErrorMark participantDetails;

	private ComboBox<JuryMembershipType> type;

	/** Constructor.
	 *
	 * @param context the editing context for the jury membership.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 * @param logger the logger to be used by this view.
	 */
	public AbstractJuryMembershipEditor(EntityEditingContext<JuryMembership> context, AuthenticatedUser authenticatedUser,
			MessageSourceAccessor messages, Logger logger) {
		super(JuryMembership.class, authenticatedUser, messages, logger, null, null, context);
	}

	@Override
	protected void createEditorContent(VerticalLayout rootContainer) {
		createDescriptionDetails(rootContainer);
		createParticipantDetails(rootContainer);
	}

	/** Create the section for editing the description of the jury membership.
	 *
	 * @param rootContainer the container.
	 */
	protected void createDescriptionDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.title = new TextField();
		this.title.setPrefixComponent(VaadinIcon.HASH.create());
		this.title.setRequired(true);
		this.title.setClearButtonVisible(true);
		content.add(this.title, 2);

		this.defenseType = new ComboBox<>();
		this.defenseType.setPrefixComponent(VaadinIcon.ACADEMY_CAP.create());
		this.defenseType.setRequired(true);
		this.defenseType.setItems(JuryType.values());
		this.defenseType.setItemLabelGenerator(this::getDefenseTypeLabel);
		content.add(this.title, 1);

		this.date = new DatePicker();
		this.date.setPrefixComponent(VaadinIcon.CALENDAR_O.create());
		this.date.setRequired(true);
		this.date.setClearButtonVisible(true);
		content.add(this.date, 1);

		this.university = new TextField();
		this.university.setPrefixComponent(VaadinIcon.INSTITUTION.create());
		this.university.setRequired(true);
		this.university.setClearButtonVisible(true);
		content.add(this.university, 1);

		this.country = ComponentFactory.newCountryComboBox(getLocale());
		this.country.setRequired(true);
		content.add(this.country, 1);

		this.descriptionDetails = new DetailsWithErrorMark(content);
		this.descriptionDetails.setOpened(true);
		rootContainer.add(this.descriptionDetails);

		getEntityDataBinder().forField(this.title)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.jury_membership.title.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.title, this.descriptionDetails))
			.bind(JuryMembership::getTitle, JuryMembership::setTitle);
		getEntityDataBinder().forField(this.defenseType)
			.withValidator(new NotNullEnumerationValidator<>(getTranslation("views.jury_membership.defense_type.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.defenseType, this.descriptionDetails))
			.bind(JuryMembership::getDefenseType, JuryMembership::setDefenseType);
		getEntityDataBinder().forField(this.date)
			.withValidator(new NotNullDateValidator(getTranslation("views.jury_membership.date.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.date, this.descriptionDetails))
			.bind(JuryMembership::getDate, JuryMembership::setDate);
		getEntityDataBinder().forField(this.university)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.jury_membership.university.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.university, this.descriptionDetails))
			.bind(JuryMembership::getUniversity, JuryMembership::setUniversity);
		getEntityDataBinder().forField(this.country)
			.withValidator(new NotNullEnumerationValidator<>(getTranslation("views.jury_membership.country.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.country, this.descriptionDetails))
			.bind(JuryMembership::getCountry, JuryMembership::setCountry);
	}

	private String getDefenseTypeLabel(JuryType type) {
		return type.getLabel(getMessageSourceAccessor(), getLocale());
	}

	/** Create the section for editing the participants to the jury membership.
	 *
	 * @param rootContainer the container.
	 */
	protected void createParticipantDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.type = new ComboBox<>();
		this.type.setPrefixComponent(VaadinIcon.ACADEMY_CAP.create());
		this.type.setRequired(true);
		this.type.setItems(JuryMembershipType.values());
		this.type.setItemLabelGenerator(this::getMembershipTypeLabel);
		content.add(this.type, 1);

		this.participantDetails = new DetailsWithErrorMark(content);
		this.participantDetails.setOpened(false);
		rootContainer.add(this.participantDetails);

		getEntityDataBinder().forField(this.type)
			.withValidator(new NotNullEnumerationValidator<>(getTranslation("views.jury_membership.membership_type.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.type, this.participantDetails))
			.bind(JuryMembership::getType, JuryMembership::setType);
	}

	private String getMembershipTypeLabel(JuryMembershipType type) {
		return type.getLabel(getMessageSourceAccessor(), null, getLocale());
	}

	@Override
	protected String computeSavingSuccessMessage() {
		return getTranslation("views.jury_membership.save_success", //$NON-NLS-1$
				getEditedEntity().getTitle());
	}

	@Override
	protected String computeValidationSuccessMessage() {
		return getTranslation("views.jury_membership.validation_success", //$NON-NLS-1$
				getEditedEntity().getTitle());
	}

	@Override
	protected String computeDeletionSuccessMessage() {
		return getTranslation("views.jury_membership.delete_success2", //$NON-NLS-1$
				getEditedEntity().getTitle());
	}

	@Override
	protected String computeSavingErrorMessage(Throwable error) {
		return getTranslation("views.jury_membership.save_error", //$NON-NLS-1$ 
				getEditedEntity().getTitle(), error.getLocalizedMessage());
	}

	@Override
	protected String computeValidationErrorMessage(Throwable error) {
		return getTranslation("views.jury_membership.validation_error", //$NON-NLS-1$ 
				getEditedEntity().getTitle(), error.getLocalizedMessage());
	}

	@Override
	protected String computeDeletionErrorMessage(Throwable error) {
		return getTranslation("views.jury_membership.delete_error2", //$NON-NLS-1$ 
				getEditedEntity().getTitle(), error.getLocalizedMessage());
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.descriptionDetails.setSummaryText(getTranslation("views.jury_membership.description_details")); //$NON-NLS-1$
		this.title.setLabel(getTranslation("views.jury_membership.title")); //$NON-NLS-1$
		this.defenseType.setLabel(getTranslation("views.jury_membership.defense_type")); //$NON-NLS-1$
		this.defenseType.setItemLabelGenerator(this::getDefenseTypeLabel);
		this.date.setLabel(getTranslation("views.jury_membership.date")); //$NON-NLS-1$
		this.university.setLabel(getTranslation("views.jury_membership.university")); //$NON-NLS-1$
		this.country.setLabel(getTranslation("views.jury_membership.country")); //$NON-NLS-1$
		ComponentFactory.updateCountryComboBoxItems(this.country, getLocale());

		this.participantDetails.setSummaryText(getTranslation("views.jury_membership.participant_details")); //$NON-NLS-1$
		this.type.setLabel(getTranslation("views.jury_membership.membership_type")); //$NON-NLS-1$
		this.type.setItemLabelGenerator(this::getMembershipTypeLabel);
	}

}
