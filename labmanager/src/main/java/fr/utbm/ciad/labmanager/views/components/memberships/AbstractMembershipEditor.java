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

package fr.utbm.ciad.labmanager.views.components.memberships;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.member.MemberStatus;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.member.Responsibility;
import fr.utbm.ciad.labmanager.services.member.MembershipService.EditingContext;
import fr.utbm.ciad.labmanager.utils.bap.FrenchBap;
import fr.utbm.ciad.labmanager.utils.cnu.CnuSection;
import fr.utbm.ciad.labmanager.utils.conrs.ConrsSection;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMark;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMarkStatusHandler;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityEditor;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotNullDateValidator;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotNullEnumerationValidator;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation for the editor of the information related to a membership.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Uses(Icon.class)
public abstract class AbstractMembershipEditor extends AbstractEntityEditor<Membership> {

	private static final long serialVersionUID = -592763051466628800L;

	private DetailsWithErrorMark employeeDetails;

	//	setPerson(Person)

	private DetailsWithErrorMark employerDetails;

	//	setResearchOrganization(ResearchOrganization)
	//	setOrganizationAddress(OrganizationAddress)

	private DetailsWithErrorMark positionDetails;

	private ComboBox<MemberStatus> status;

	private DatePicker since;

	private DatePicker to;

	private Checkbox permanentPosition; 

	private DetailsWithErrorMark sectionDetails;

	private ComboBox<CnuSection> cnu;

	private ComboBox<ConrsSection> conrs;

	private ComboBox<FrenchBap> bap;

	private DetailsWithErrorMark activityDetails;

	//	setScientificAxes(Collection<ScientificAxis>)

	private ComboBox<Responsibility> responsibility;

	private Checkbox publicPosition; 

	private final EditingContext context;

	/** Constructor.
	 *
	 * @param context the editing context for the membership.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 * @param logger the logger to be used by this view.
	 */
	public AbstractMembershipEditor(EditingContext context, AuthenticatedUser authenticatedUser,
			MessageSourceAccessor messages, Logger logger) {
		super(Membership.class, authenticatedUser, messages, logger,
				"views.membership.administration_details", //$NON-NLS-1$
				null);
		this.context = context;
	}

	@Override
	public Membership getEditedEntity() {
		return this.context.getMembership();
	}

	@Override
	protected void createEditorContent(VerticalLayout rootContainer) {
		createEmployeeDetails(rootContainer);
		createEmployerDetails(rootContainer);
		createPositionDetails(rootContainer);
		createSectionDetails(rootContainer);
		createActivityDetails(rootContainer);
		if (isBaseAdmin()) {
			createAdministrationComponents(rootContainer,
					content -> {
						this.responsibility = new ComboBox<>();
						this.responsibility.setItems(Responsibility.values());
						this.responsibility.setItemLabelGenerator(this::getResponsibilityLabel);
						this.responsibility.setPrefixComponent(VaadinIcon.TASKS.create());
						content.add(this.responsibility, 2);
						
						this.publicPosition = new Checkbox(); 
						content.add(this.publicPosition, 2);

						getEntityDataBinder().forField(this.responsibility)
							.bind(Membership::getResponsibility, Membership::setResponsibility);
						getEntityDataBinder().forField(this.publicPosition)
							.bind(Membership::isMainPosition, Membership::setMainPosition);
					},
					null);
		}
	}

	/** Create the section for editing the description of the employee.
	 *
	 * @param rootContainer the container.
	 */
	protected void createEmployeeDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.employeeDetails = new DetailsWithErrorMark(content);
		this.employeeDetails.setOpened(false);
		rootContainer.add(this.employeeDetails);
	}

	/** Create the section for editing the description of the employer.
	 *
	 * @param rootContainer the container.
	 */
	protected void createEmployerDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.employerDetails = new DetailsWithErrorMark(content);
		this.employerDetails.setOpened(false);
		rootContainer.add(this.employerDetails);
	}

	/** Create the section for editing the description of the position.
	 *
	 * @param rootContainer the container.
	 */
	protected void createPositionDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.status = new ComboBox<>();
		this.status.setItems(MemberStatus.values());
		this.status.setItemLabelGenerator(this::getStatusLabel);
		this.status.setRequired(true);
		this.status.setPrefixComponent(VaadinIcon.DOCTOR.create());
		this.status.addValueChangeListener(it -> {
			final var value = it.getValue();
			//
			// Change the enable state of the other fields that
			// depend on the status of the position
			//
			this.permanentPosition.setEnabled(
					value != null && value.isPermanentPositionAllowed());
			this.cnu.setEnabled(
					value != null && value.isPermanentPositionAllowed() && value.isResearcher());
			this.conrs.setEnabled(
					value != null && value.isPermanentPositionAllowed() && value.isResearcher());
			this.bap.setEnabled(
					value != null && value.isPermanentPositionAllowed() && (value.isTechnicalStaff() || value.isAdministrativeStaff()));
		});
		content.add(this.status, 2);

		this.since = new DatePicker();
		this.since.setPrefixComponent(VaadinIcon.SIGN_IN_ALT.create());
		this.since.setClearButtonVisible(true);
		content.add(this.since, 1);
		
		this.to = new DatePicker();
		this.to.setPrefixComponent(VaadinIcon.SIGN_OUT_ALT.create());
		this.to.setClearButtonVisible(true);
		content.add(this.to, 1);

		this.permanentPosition = new Checkbox(); 
		content.add(this.to, 2);

		this.positionDetails = new DetailsWithErrorMark(content);
		this.positionDetails.setOpened(false);
		rootContainer.add(this.positionDetails);

		getEntityDataBinder().forField(this.status)
			.withValidator(new NotNullEnumerationValidator<>(getTranslation("views.membership.status.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.status, this.positionDetails))
			.bind(Membership::getMemberStatus, Membership::setMemberStatus);
		getEntityDataBinder().forField(this.since)
			.withValidator(new NotNullDateValidator(getTranslation("views.membership.since.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.since, this.positionDetails))
			.bind(Membership::getMemberSinceWhen, Membership::setMemberSinceWhen);
		getEntityDataBinder().forField(this.to)
			.withValidator(new NotNullDateValidator(getTranslation("views.membership.since.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.to, this.positionDetails))
			.bind(Membership::getMemberToWhen, Membership::setMemberToWhen);
		getEntityDataBinder().forField(this.permanentPosition)
			.bind(Membership::isPermanentPosition, Membership::setPermanentPosition);
	}

	private String getStatusLabel(MemberStatus status) {
		// TODO gender
		return status.getLabel(getMessageSourceAccessor(), null, false, getLocale());
	}

	/** Create the section for editing the description of the section.
	 *
	 * @param rootContainer the container.
	 */
	protected void createSectionDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.cnu = new ComboBox<>();
		this.cnu.setItems(CnuSection.values());
		this.cnu.setItemLabelGenerator(this::getCnuLabel);
		this.cnu.setPrefixComponent(VaadinIcon.COMPILE.create());
		content.add(this.cnu, 2);
		
		this.conrs = new ComboBox<>();
		this.conrs.setItems(ConrsSection.values());
		this.conrs.setItemLabelGenerator(this::getConrsLabel);
		this.conrs.setPrefixComponent(VaadinIcon.COMPILE.create());
		content.add(this.conrs, 2);

		this.bap = new ComboBox<>();
		this.bap.setItems(FrenchBap.values());
		this.bap.setItemLabelGenerator(this::getBapLabel);
		this.bap.setPrefixComponent(VaadinIcon.COMPILE.create());
		content.add(this.bap, 2);

		this.sectionDetails = new DetailsWithErrorMark(content);
		this.sectionDetails.setOpened(false);
		rootContainer.add(this.sectionDetails);

		getEntityDataBinder().forField(this.cnu)
			.bind(Membership::getCnuSection, Membership::setCnuSection);
		getEntityDataBinder().forField(this.conrs)
			.bind(Membership::getConrsSection, Membership::setConrsSection);
		getEntityDataBinder().forField(this.bap)
			.bind(Membership::getFrenchBap, Membership::setFrenchBap);
	}

	private String getCnuLabel(CnuSection cnu) {
		return new StringBuilder()
				.append(cnu.getNumber())
				.append(" - ") //$NON-NLS-1$
				.append(cnu.getLabel(getMessageSourceAccessor(), getLocale()))
				.toString();
	}

	private String getConrsLabel(ConrsSection conrs) {
		return new StringBuilder()
				.append(conrs.getNumber())
				.append(" - ") //$NON-NLS-1$
				.append(conrs.getLabel(getMessageSourceAccessor(), getLocale()))
				.toString();
	}

	private String getBapLabel(FrenchBap bap) {
		return new StringBuilder()
				.append(bap.getShortName())
				.append(" - ") //$NON-NLS-1$
				.append(bap.getLabel(getMessageSourceAccessor(), getLocale()))
				.toString();
	}

	/** Create the section for editing the description of the activities of the person.
	 *
	 * @param rootContainer the container.
	 */
	protected void createActivityDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.activityDetails = new DetailsWithErrorMark(content);
		this.activityDetails.setOpened(false);
		rootContainer.add(this.activityDetails);
	}

	private String getResponsibilityLabel(Responsibility responsability) {
		// TODO Gender
		return responsability.getLabel(getMessageSourceAccessor(), null, getLocale());
	}

	@Override
	protected void doSave() throws Exception {
		this.context.save();
	}

	@Override
	public void notifySaved() {
		notifySaved(getTranslation("views.membership.save_success", //$NON-NLS-1$
				"?"));
	}

	@Override
	public void notifyValidated() {
		notifyValidated(getTranslation("views.membership.validation_success", //$NON-NLS-1$
				"?"));
	}

	@Override
	public void notifySavingError(Throwable error) {
		notifySavingError(error, getTranslation("views.membership.save_error", //$NON-NLS-1$ 
				"?", error.getLocalizedMessage()));
	}

	@Override
	public void notifyValidationError(Throwable error) {
		notifyValidationError(error, getTranslation("views.membership.validation_error", //$NON-NLS-1$ 
				"?", error.getLocalizedMessage()));
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.employeeDetails.setSummaryText(getTranslation("views.membership.employee_details")); //$NON-NLS-1$

		this.employerDetails.setSummaryText(getTranslation("views.membership.employer_details")); //$NON-NLS-1$

		this.positionDetails.setSummaryText(getTranslation("views.membership.position_details")); //$NON-NLS-1$
		this.status.setLabel(getTranslation("views.membership.status")); //$NON-NLS-1$
		this.status.setItemLabelGenerator(this::getStatusLabel);
		this.since.setLabel(getTranslation("views.membership.since")); //$NON-NLS-1$
		this.to.setLabel(getTranslation("views.membership.to")); //$NON-NLS-1$
		this.permanentPosition.setLabel(getTranslation("views.membership.permanent_position")); //$NON-NLS-1$

		this.sectionDetails.setSummaryText(getTranslation("views.membership.section_details")); //$NON-NLS-1$
		this.cnu.setLabel(getTranslation("views.membership.cnu")); //$NON-NLS-1$
		this.cnu.setHelperText(getTranslation("views.membership.cnu.help")); //$NON-NLS-1$
		this.conrs.setLabel(getTranslation("views.membership.conrs")); //$NON-NLS-1$
		this.conrs.setHelperText(getTranslation("views.membership.conrs.help")); //$NON-NLS-1$
		this.bap.setLabel(getTranslation("views.membership.bap")); //$NON-NLS-1$
		this.bap.setHelperText(getTranslation("views.membership.bap.help")); //$NON-NLS-1$

		this.activityDetails.setSummaryText(getTranslation("views.membership.activity_details")); //$NON-NLS-1$

		this.responsibility.setLabel(getTranslation("views.membership.responsibility")); //$NON-NLS-1$
		this.publicPosition.setLabel(getTranslation("views.membership.public_position")); //$NON-NLS-1$
	}

}
