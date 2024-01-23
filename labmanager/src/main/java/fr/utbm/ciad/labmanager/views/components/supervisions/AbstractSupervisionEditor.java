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

package fr.utbm.ciad.labmanager.views.components.supervisions;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.supervision.Supervision;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityEditingContext;
import fr.utbm.ciad.labmanager.utils.funding.FundingScheme;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.converters.StringTrimer;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMark;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMarkStatusHandler;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityEditor;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotEmptyStringValidator;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotNullEnumerationValidator;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation for the editor of the information related to a supervision.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Uses(Icon.class)
public abstract class AbstractSupervisionEditor extends AbstractEntityEditor<Supervision> {

	private static final long serialVersionUID = 7189628237085364285L;

	private DetailsWithErrorMark supervisedWorkDetails;

	private TextField title;

	private DetailsWithErrorMark supervisorsDetails;

	private DetailsWithErrorMark fundDetails;

	private ComboBox<FundingScheme> funding;

	private TextField fundingDetails;

	private Checkbox jointPosition;

	private Checkbox entrepreneur;

	private DetailsWithErrorMark defenseDetails;

	private DatePicker defenseDate;
	
	private DetailsWithErrorMark afterDefenseDetails;

	private Checkbox abandonment;

	private TextField positionAfterSupervision;

	private IntegerField numberOfAterPositions;

	/** Constructor.
	 *
	 * @param context the editing context for the supervisison.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 * @param logger the logger to be used by this view.
	 */
	public AbstractSupervisionEditor(EntityEditingContext<Supervision> context, AuthenticatedUser authenticatedUser,
			MessageSourceAccessor messages, Logger logger) {
		super(Supervision.class, authenticatedUser, messages, logger, null, null, context);
	}

	@Override
	protected void createEditorContent(VerticalLayout rootContainer) {
		createSupervisedWorkDetails(rootContainer);
		createSupervisorDetails(rootContainer);
		createFundDetails(rootContainer);
		createDefenseDetails(rootContainer);
		createAfterDefenseDetails(rootContainer);
	}

	/** Create the section for editing the description of the supervised work.
	 *
	 * @param rootContainer the container.
	 */
	protected void createSupervisedWorkDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);
		
		this.title = new TextField();
		this.title.setPrefixComponent(VaadinIcon.HASH.create());
		this.title.setRequired(true);
		this.title.setClearButtonVisible(true);
		content.add(this.title, 2);

		this.supervisedWorkDetails = new DetailsWithErrorMark(content);
		this.supervisedWorkDetails.setOpened(false);
		rootContainer.add(this.supervisedWorkDetails);

		getEntityDataBinder().forField(this.title)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.supervision.title.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.title, this.supervisedWorkDetails))
			.bind(Supervision::getTitle, Supervision::setTitle);
	}

	/** Create the section for editing the description of the supervisors.
	 *
	 * @param rootContainer the container.
	 */
	protected void createSupervisorDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.supervisorsDetails = new DetailsWithErrorMark(content);
		this.supervisorsDetails.setOpened(false);
		rootContainer.add(this.supervisorsDetails);
	}

	/** Create the section for editing the description of the funding.
	 *
	 * @param rootContainer the container.
	 */
	protected void createFundDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);
		
		this.funding = new ComboBox<>();
		this.funding.setRequired(true);
		this.funding.setItems(FundingScheme.values());
		this.funding.setItemLabelGenerator(this::getFundingSchemeLabel);
		this.funding.setPrefixComponent(VaadinIcon.EURO.create());
		this.funding.setClearButtonVisible(true);
		content.add(this.funding, 1);

		this.fundingDetails = new TextField();
		this.fundingDetails.setPrefixComponent(VaadinIcon.EURO.create());
		content.add(this.fundingDetails, 1);

		this.jointPosition = new Checkbox();
		content.add(this.jointPosition, 1);

		this.entrepreneur = new Checkbox();
		content.add(this.entrepreneur, 1);

		this.fundDetails = new DetailsWithErrorMark(content);
		this.fundDetails.setOpened(false);
		rootContainer.add(this.fundDetails);

		getEntityDataBinder().forField(this.title)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.supervision.title.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.title, this.supervisedWorkDetails))
			.bind(Supervision::getTitle, Supervision::setTitle);
		getEntityDataBinder().forField(this.funding)
			.withValidator(new NotNullEnumerationValidator<>(getTranslation("views.supervision.funding_scheme.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.funding, this.fundDetails))
			.bind(Supervision::getFunding, Supervision::setFunding);
		getEntityDataBinder().forField(this.fundingDetails)
			.withConverter(new StringTrimer())
			.bind(Supervision::getFundingDetails, Supervision::setFundingDetails);
		getEntityDataBinder().forField(this.jointPosition)
			.bind(Supervision::isJointPosition, Supervision::setJointPosition);
		getEntityDataBinder().forField(this.entrepreneur)
			.bind(Supervision::isEntrepreneur, Supervision::setEntrepreneur);
	}

	private String getFundingSchemeLabel(FundingScheme scheme) {
		return scheme.getLabel(getMessageSourceAccessor(), getLocale());
	}

	/** Create the section for editing the description of the defense.
	 *
	 * @param rootContainer the container.
	 */
	protected void createDefenseDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);
		
		this.defenseDate = new DatePicker();
		this.defenseDate.setPrefixComponent(VaadinIcon.CALENDAR_O.create());
		this.defenseDate.setClearButtonVisible(true);
		content.add(this.defenseDate, 2);

		this.defenseDetails = new DetailsWithErrorMark(content);
		this.defenseDetails.setOpened(false);
		rootContainer.add(this.defenseDetails);

		getEntityDataBinder().forField(this.defenseDate)
			.bind(Supervision::getDefenseDate, Supervision::setDefenseDate);
	}

	/** Create the section for editing the description of the period after the defense.
	 *
	 * @param rootContainer the container.
	 */
	protected void createAfterDefenseDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);
		
		this.abandonment = new Checkbox();
		content.add(this.abandonment, 2);

		this.positionAfterSupervision = new TextField();
		this.positionAfterSupervision.setPrefixComponent(VaadinIcon.LAPTOP.create());
		this.positionAfterSupervision.setClearButtonVisible(true);
		content.add(this.positionAfterSupervision, 2);

		this.numberOfAterPositions = new IntegerField();
		this.numberOfAterPositions.setMin(0);
		this.numberOfAterPositions.setPrefixComponent(VaadinIcon.ACADEMY_CAP.create());
		this.numberOfAterPositions.setClearButtonVisible(true);
		content.add(this.numberOfAterPositions, 2);

		this.afterDefenseDetails = new DetailsWithErrorMark(content);
		this.afterDefenseDetails.setOpened(false);
		rootContainer.add(this.afterDefenseDetails);

		getEntityDataBinder().forField(this.abandonment)
			.bind(Supervision::isAbandonment, Supervision::setAbandonment);
		getEntityDataBinder().forField(this.positionAfterSupervision)
			.withConverter(new StringTrimer())
			.bind(Supervision::getPositionAfterSupervision, Supervision::setPositionAfterSupervision);
		getEntityDataBinder().forField(this.numberOfAterPositions)
			.withValidator(new IntegerRangeValidator(getTranslation("views.supervision.ater_count.error"), Integer.valueOf(0), null)) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.numberOfAterPositions, this.afterDefenseDetails))
			.bind(Supervision::getNumberOfAterPositions, Supervision::setNumberOfAterPositions);
	}

	@Override
	protected String computeSavingSuccessMessage() {
		return getTranslation("views.supervision.save_success", //$NON-NLS-1$
				getEditedEntity().getTitle());
	}

	@Override
	protected String computeValidationSuccessMessage() {
		return getTranslation("views.supervision.validation_success", //$NON-NLS-1$
				getEditedEntity().getTitle());
	}

	@Override
	protected String computeDeletionSuccessMessage() {
		return getTranslation("views.supervision.delete_success2", //$NON-NLS-1$
				getEditedEntity().getTitle());
	}

	@Override
	protected String computeSavingErrorMessage(Throwable error) {
		return getTranslation("views.supervision.save_error", //$NON-NLS-1$ 
				getEditedEntity().getTitle(), error.getLocalizedMessage());
	}

	@Override
	protected String computeValidationErrorMessage(Throwable error) {
		return getTranslation("views.supervision.validation_error", //$NON-NLS-1$ 
				getEditedEntity().getTitle(), error.getLocalizedMessage());
	}

	@Override
	protected String computeDeletionErrorMessage(Throwable error) {
		return getTranslation("views.supervision.delete_error2", //$NON-NLS-1$ 
				getEditedEntity().getTitle(), error.getLocalizedMessage());
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.supervisedWorkDetails.setSummaryText(getTranslation("views.supervision.supervised_work_details")); //$NON-NLS-1$
		this.title.setLabel(getTranslation("views.supervision.title")); //$NON-NLS-1$

		this.supervisorsDetails.setSummaryText(getTranslation("views.supervision.supervisor_details")); //$NON-NLS-1$

		this.fundDetails.setSummaryText(getTranslation("views.supervision.fund_details")); //$NON-NLS-1$
		this.funding.setLabel(getTranslation("views.supervision.funding_scheme")); //$NON-NLS-1$
		this.fundingDetails.setLabel(getTranslation("views.supervision.funding_details")); //$NON-NLS-1$
		this.fundingDetails.setHelperText(getTranslation("views.supervision.funding_details.help")); //$NON-NLS-1$
		this.jointPosition.setLabel(getTranslation("views.supervision.joint_position")); //$NON-NLS-1$
		this.entrepreneur.setLabel(getTranslation("views.supervision.entrepreneur")); //$NON-NLS-1$

		this.defenseDetails.setSummaryText(getTranslation("views.supervision.defense_details")); //$NON-NLS-1$
		this.defenseDate.setLabel(getTranslation("views.supervision.defense_date")); //$NON-NLS-1$

		this.afterDefenseDetails.setSummaryText(getTranslation("views.supervision.after_defense_details")); //$NON-NLS-1$
		this.abandonment.setLabel(getTranslation("views.supervision.abandonment")); //$NON-NLS-1$
		this.positionAfterSupervision.setLabel(getTranslation("views.supervision.position_after_supervision")); //$NON-NLS-1$
		this.positionAfterSupervision.setHelperText(getTranslation("views.supervision.position_after_supervision.help")); //$NON-NLS-1$
		this.numberOfAterPositions.setLabel(getTranslation("views.supervision.ater_count")); //$NON-NLS-1$
		this.numberOfAterPositions.setHelperText(getTranslation("views.supervision.ater_count.help")); //$NON-NLS-1$
	}

}
