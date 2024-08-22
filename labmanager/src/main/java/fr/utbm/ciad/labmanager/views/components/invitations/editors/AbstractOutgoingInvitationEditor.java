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

package fr.utbm.ciad.labmanager.views.components.invitations.editors;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import fr.utbm.ciad.labmanager.data.invitation.PersonInvitation;
import fr.utbm.ciad.labmanager.data.invitation.PersonInvitationType;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityEditingContext;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.user.UserService;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.converters.StringTrimer;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMark;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMarkStatusHandler;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityEditor;
import fr.utbm.ciad.labmanager.views.components.addons.validators.DisjointEntityValidator;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotEmptyStringValidator;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotNullDateValidator;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotNullEnumerationValidator;
import fr.utbm.ciad.labmanager.views.components.persons.editors.PersonEditorFactory;
import fr.utbm.ciad.labmanager.views.components.persons.fields.SinglePersonNameField;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation for the editor of the information related to an outgoing invitation.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Uses(Icon.class)
public abstract class AbstractOutgoingInvitationEditor extends AbstractEntityEditor<PersonInvitation> {

	private static final long serialVersionUID = -4471218353673137134L;

	private DetailsWithErrorMark inviterDetails;

	private SinglePersonNameField inviter;

	private TextField university;

	private ComboBox<CountryCode> country;
	
	private DetailsWithErrorMark guestDetails;

	private SinglePersonNameField guest;

	private DetailsWithErrorMark informationDetails;

	private DatePicker startDate;

	private DatePicker endDate;

	private TextField title;

	private final PersonService personService;

	private final PersonEditorFactory personEditorFactory;

	private final UserService userService;

	/** Constructor.
	 *
	 * @param context the editing context for the person invitation.
	 * @param relinkEntityWhenSaving indicates if the editor must be relink to the edited entity when it is saved. This new link may
	 *     be required if the editor is not closed after saving in order to obtain a correct editing of the entity.
	 * @param personService the service for accessing the JPA entities for persons.
	 * @param personEditorFactory the factory for creating the person editors.
	 * @param userService the service for accessing the JPA entities for users.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 * @param logger the logger to be used by this view.
	 */
	public AbstractOutgoingInvitationEditor(EntityEditingContext<PersonInvitation> context, boolean relinkEntityWhenSaving,
			PersonService personService, PersonEditorFactory personEditorFactory, UserService userService, AuthenticatedUser authenticatedUser, MessageSourceAccessor messages, Logger logger) {
		super(PersonInvitation.class, authenticatedUser, messages, logger, null, null, context, relinkEntityWhenSaving);
		this.personService = personService;
		this.personEditorFactory = personEditorFactory;
		this.userService = userService;
	}

	@Override
	protected void doSave() throws Exception {
		// Force the type of the invitation to incoming
		getEditedEntity().setType(PersonInvitationType.OUTGOING_GUEST);
		super.doSave();
	}

	@Override
	protected void createEditorContent(VerticalLayout rootContainer) {
		createInviterDetails(rootContainer);
		createGuestDetails(rootContainer);
		createInformationDetails(rootContainer);
	}

	/** Create the section for editing the description of the guest.
	 *
	 * @param rootContainer the container.
	 */
	protected void createGuestDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.guest = new SinglePersonNameField(this.personService, this.personEditorFactory, this.userService, getAuthenticatedUser(),
				getTranslation("views.outgoing_invitation.new_guest"), getLogger()); //$NON-NLS-1$
		this.guest.setRequiredIndicatorVisible(true);
		this.guest.setPrefixComponent(VaadinIcon.USER.create());
		content.add(this.guest, 2);

		this.guestDetails = createDetailsWithErrorMark(rootContainer, content, "guest"); //$NON-NLS-1$

		getEntityDataBinder().forField(this.guest)
			.withValidator(new DisjointEntityValidator<>(
					getTranslation("views.outgoing_invitation.guest.error.null"), //$NON-NLS-1$
					getTranslation("views.outgoing_invitation.guest.error.disjoint"), //$NON-NLS-1$
					this::checkGuestUnicity))
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.guest, this.guestDetails))
			.bind(PersonInvitation::getGuest, PersonInvitation::setGuest);
	}

	private boolean checkGuestUnicity(Person guest) {
		assert guest != null;
		final var inviter = getEditedEntity().getInviter();
		return !guest.equals(inviter);
	}

	/** Create the section for editing the description of the inviter.
	 *
	 * @param rootContainer the container.
	 */
	protected void createInviterDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.inviter = new SinglePersonNameField(this.personService, this.personEditorFactory, this.userService, getAuthenticatedUser(),
				getTranslation("views.outgoing_invitation.new_inviter"), getLogger()); //$NON-NLS-1$
		this.inviter.setRequiredIndicatorVisible(true);
		this.inviter.setPrefixComponent(VaadinIcon.USER.create());
		content.add(this.inviter, 2);

		this.university = new TextField();
		this.university.setPrefixComponent(VaadinIcon.INSTITUTION.create());
		this.university.setRequired(true);
		this.university.setClearButtonVisible(true);
		content.add(this.university, 1);

		this.country = ComponentFactory.newCountryComboBox(getLocale());
		this.country.setRequired(true);
		content.add(this.country, 1);

		this.inviterDetails = createDetailsWithErrorMark(rootContainer, content, "inviter", true); //$NON-NLS-1$

		getEntityDataBinder().forField(this.inviter)
			.withValidator(new DisjointEntityValidator<>(
					getTranslation("views.outgoing_invitation.inviter.error.null"), //$NON-NLS-1$
					getTranslation("views.outgoing_invitation.inviter.error.disjoint"), //$NON-NLS-1$
					this::checkInviterUnicity))
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.inviter, this.inviterDetails))
			.bind(PersonInvitation::getInviter, PersonInvitation::setInviter);
		getEntityDataBinder().forField(this.university)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.outgoing_invitation.university.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.university, this.inviterDetails))
			.bind(PersonInvitation::getUniversity, PersonInvitation::setUniversity);
		getEntityDataBinder().forField(this.country)
			.withValidator(new NotNullEnumerationValidator<>(getTranslation("views.outgoing_invitation.country.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.country, this.inviterDetails))
			.bind(PersonInvitation::getCountry, PersonInvitation::setCountry);
	}

	private boolean checkInviterUnicity(Person inviter) {
		assert inviter != null;
		final var guest = getEditedEntity().getGuest();
		return !inviter.equals(guest);
	}

	/** Create the section for editing the description of the invitation.
	 *
	 * @param rootContainer the container.
	 */
	protected void createInformationDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.startDate = new DatePicker();
		this.startDate.setPrefixComponent(VaadinIcon.SIGN_IN_ALT.create());
		this.startDate.setRequired(true);
		this.startDate.setClearButtonVisible(true);
		content.add(this.startDate, 1);

		this.endDate = new DatePicker();
		this.endDate.setPrefixComponent(VaadinIcon.SIGN_OUT_ALT.create());
		this.endDate.setRequired(true);
		this.endDate.setClearButtonVisible(true);
		content.add(this.endDate, 1);

		this.title = new TextField();
		this.title.setPrefixComponent(VaadinIcon.HASH.create());
		this.title.setRequired(true);
		this.title.setClearButtonVisible(true);
		content.add(this.title, 2);

		this.informationDetails = createDetailsWithErrorMark(rootContainer, content, "information"); //$NON-NLS-1$

		getEntityDataBinder().forField(this.startDate)
			.withValidator(new NotNullDateValidator(getTranslation("views.outgoing_invitation.start_date.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.startDate, this.informationDetails))
			.bind(PersonInvitation::getStartDate, PersonInvitation::setStartDate);
		getEntityDataBinder().forField(this.endDate)
			.withValidator(new NotNullDateValidator(getTranslation("views.outgoing_invitation.end_date.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.endDate, this.informationDetails))
			.bind(PersonInvitation::getEndDate, PersonInvitation::setEndDate);
		getEntityDataBinder().forField(this.title)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.outgoing_invitation.title.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.startDate, this.informationDetails))
			.bind(PersonInvitation::getTitle, PersonInvitation::setTitle);
	}

	@Override
	protected String computeSavingSuccessMessage() {
		return getTranslation("views.outgoing_invitation.save_success", //$NON-NLS-1$
				getEditedEntity().getTitle());
	}

	@Override
	protected String computeValidationSuccessMessage() {
		return getTranslation("views.outgoing_invitation.validation_success", //$NON-NLS-1$
				getEditedEntity().getTitle());
	}

	@Override
	protected String computeDeletionSuccessMessage() {
		return getTranslation("views.outgoing_invitation.delete_success2", //$NON-NLS-1$
				getEditedEntity().getTitle());
	}

	@Override
	protected String computeSavingErrorMessage(Throwable error) {
		return getTranslation("views.outgoing_invitation.save_error", //$NON-NLS-1$ 
				getEditedEntity().getTitle(), error.getLocalizedMessage());
	}

	@Override
	protected String computeValidationErrorMessage(Throwable error) {
		return getTranslation("views.outgoing_invitation.validation_error", //$NON-NLS-1$ 
				getEditedEntity().getTitle(), error.getLocalizedMessage());
	}

	@Override
	protected String computeDeletionErrorMessage(Throwable error) {
		return getTranslation("views.outgoing_invitation.delete_error2", //$NON-NLS-1$ 
				getEditedEntity().getTitle(), error.getLocalizedMessage());
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.guestDetails.setSummaryText(getTranslation("views.outgoing_invitation.guest_details")); //$NON-NLS-1$
		this.guest.setLabel(getTranslation("views.incoming_invitation.guest")); //$NON-NLS-1$
		this.guest.setHelperText(getTranslation("views.incoming_invitation.guest.help")); //$NON-NLS-1$

		this.inviterDetails.setSummaryText(getTranslation("views.outgoing_invitation.inviter_details")); //$NON-NLS-1$
		this.inviter.setLabel(getTranslation("views.incoming_invitation.inviter")); //$NON-NLS-1$
		this.inviter.setHelperText(getTranslation("views.incoming_invitation.inviter.help")); //$NON-NLS-1$
		this.university.setLabel(getTranslation("views.outgoing_invitation.university")); //$NON-NLS-1$
		this.country.setLabel(getTranslation("views.outgoing_invitation.country")); //$NON-NLS-1$
		ComponentFactory.updateCountryComboBoxItems(this.country, getLocale());

		this.informationDetails.setSummaryText(getTranslation("views.outgoing_invitation.information_details")); //$NON-NLS-1$
		this.startDate.setLabel(getTranslation("views.outgoing_invitation.start_date")); //$NON-NLS-1$
		this.endDate.setLabel(getTranslation("views.outgoing_invitation.end_date")); //$NON-NLS-1$
		this.title.setLabel(getTranslation("views.outgoing_invitation.title")); //$NON-NLS-1$
	}

}
