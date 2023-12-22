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

package fr.utbm.ciad.labmanager.views.components.persons;

import static fr.utbm.ciad.labmanager.views.ViewConstants.ACADEMIA_EDU_BASE_URL;
import static fr.utbm.ciad.labmanager.views.ViewConstants.ACADEMIA_EDU_ICON;
import static fr.utbm.ciad.labmanager.views.ViewConstants.ADSCIENTIFICINDEX_ICON;
import static fr.utbm.ciad.labmanager.views.ViewConstants.DBLP_BASE_URL;
import static fr.utbm.ciad.labmanager.views.ViewConstants.DBLP_ICON;
import static fr.utbm.ciad.labmanager.views.ViewConstants.EU_CORDIS_BASE_URL;
import static fr.utbm.ciad.labmanager.views.ViewConstants.EU_CORDIS_ICON;
import static fr.utbm.ciad.labmanager.views.ViewConstants.FACEBOOK_ICON;
import static fr.utbm.ciad.labmanager.views.ViewConstants.GITHUB_ICON;
import static fr.utbm.ciad.labmanager.views.ViewConstants.GRAVATAR_ICON;
import static fr.utbm.ciad.labmanager.views.ViewConstants.GSCHOLAR_ICON;
import static fr.utbm.ciad.labmanager.views.ViewConstants.LINKEDIN_ICON;
import static fr.utbm.ciad.labmanager.views.ViewConstants.ORCID_ICON;
import static fr.utbm.ciad.labmanager.views.ViewConstants.RESEARCHGATE_ICON;
import static fr.utbm.ciad.labmanager.views.ViewConstants.SCOPUS_ICON;
import static fr.utbm.ciad.labmanager.views.ViewConstants.WOS_ICON;

import java.net.URL;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.member.Gender;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.member.WebPageNaming;
import fr.utbm.ciad.labmanager.data.user.User;
import fr.utbm.ciad.labmanager.data.user.UserRole;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.user.UserService;
import fr.utbm.ciad.labmanager.views.ViewConstants;
import fr.utbm.ciad.labmanager.views.components.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.details.DetailsWithErrorMark;
import fr.utbm.ciad.labmanager.views.components.details.DetailsWithErrorMarkStatusHandler;
import fr.utbm.ciad.labmanager.views.components.phones.PhoneNumberField;
import fr.utbm.ciad.labmanager.views.components.users.UserIdentityChangedObserver;
import fr.utbm.ciad.labmanager.views.components.validators.OrcidValidator;
import fr.utbm.ciad.labmanager.views.components.validators.UrlValidator;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation for the editor of the information related to a person.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Uses(Icon.class)
public abstract class AbstractPersonEditor extends Composite<VerticalLayout> implements LocaleChangeObserver {

	private static final long serialVersionUID = 6088942935576032576L;
	
	private final Logger logger;

	private final MessageSourceAccessor messages;

	private final AuthenticatedUser authenticatedUser;

	private DetailsWithErrorMark personalInformationDetails;

	private TextField lastname;

	private TextField firstname;

	private ComboBox<Gender> gender;

	private TextField gravatarId;

	private VerticalLayout photo;

	private DetailsWithErrorMark contactInformationDetails;

	private TextField email;

	private PhoneNumberField officePhone;

	private PhoneNumberField mobilePhone;

	private TextField officeRoom;

	private DetailsWithErrorMark researcherIdsDetails;

	private TextField orcid;

	private TextField scopus;

	private TextField wos;

	private TextField gscholar;

	private Details indexesDetails;

	private IntegerField wosHindex;

	private IntegerField scopusHindex;

	private IntegerField gscholarHindex;

	private IntegerField wosCitations;

	private IntegerField scopusCitations;

	private IntegerField gscholarCitations;

	private DetailsWithErrorMark socialLinksDetails;

	private TextField researchGate;

	private TextField adScientificIndex;

	private TextField dblp;

	private TextField academiaEdu;

	private TextField euCordis;

	private TextField linkedin;

	private TextField github;

	private TextField facebook;

	private Details administrationDetails;

	private ComboBox<WebPageNaming> webpageConvention;

	private Checkbox validatedPerson;

	private TextField userLogin;

	private ComboBox<UserRole> userRole;

	private final Person editedPerson;

	private final Binder<Person> personBinder = new Binder<>(Person.class);

	private final User editedUser;

	private boolean isJpaUser;

	private final Binder<User> userBinder = new Binder<>(User.class);

	/** Constructor.
	 *
	 * @param person the person to edit, never {@code null}.
	 * @param personService the service for accessing to the person. If it is {@code null},
	 *     the default implementation does nothing. This function must be overridden to save the person
	 *     without a repository.
	 * @param user the user associated to the person. It could be {@code null} if is it unknown.
	 * @param userService the service for accessing to the person. If it is {@code null},
	 *     the default implementation does nothing. This function must be overridden to save the user
	 *     without a repository.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 * @param logger the logger to be used by this view.
	 */
	public AbstractPersonEditor(Person person, PersonService personService, User user, UserService userService, 
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages, Logger logger) {
		assert person != null;

		this.messages = messages;
		this.logger = logger;
		this.editedPerson = person;
		this.isJpaUser = user != null;
		if (this.isJpaUser) {
			this.editedUser = user;
		} else {
			this.editedUser = new User();
			this.editedUser.setPerson(this.editedPerson);
		}
		this.authenticatedUser = authenticatedUser;

		final boolean isAdmin = this.authenticatedUser != null && this.authenticatedUser.get().isPresent()
				&& this.authenticatedUser.get().get().getRole() == UserRole.ADMIN;

		createContent(getContent(), isAdmin, personService, userService);
	}

	/** Create the content of the editor.
	 *
	 * @param rootContainer the container.
	 * @param isAdmin indicates if the user has the administrator role.
	 * @param personService the service for accessing to the person.
	 * @param userService the service for accessing to the user.
	 */
	protected void createContent(VerticalLayout rootContainer, boolean isAdmin, PersonService personService,
			UserService userService) {
		rootContainer.setSpacing(false);

		createPersonalInformationComponents(rootContainer);
		createContactInformationComponents(rootContainer);
		createResearcherIdsComponents(rootContainer);
		createIndexesComponents(rootContainer);
		createSocialLinksComponents(rootContainer);
		
		if (isAdmin) {
			createAdministrationComponents(rootContainer);
		}

		this.personBinder.setBean(getEditedPerson());
		this.userBinder.setBean(getEditedUser());
	}
	
	/** Replies the person which is shown and edited by this view.
	 *
	 * @return the person, never {@code null}.
	 */
	public Person getEditedPerson() {
		return this.editedPerson;
	}

	/** Replies the user associated to the {@link #getEditedPerson() edited person} which is shown and edited by this view.
	 * The provided user may be not a valid JPA entity since it was not yet saved in the database.
	 *
	 * @return the user, never {@code null}.
	 */
	public User getEditedUser() {
		return this.editedUser;
	}

	/** Replies the binder of person data to the fields.
	 *
	 * @return the person data binder.
	 * @see #getUserDataBinder()
	 */
	protected Binder<Person> getPersonDataBinder() {
		return this.personBinder;
	}

	/** Replies the binder of user data to the fields.
	 *
	 * @return the user data binder.
	 * @see #getPersonDataBinder()
	 */
	protected Binder<User> getUserDataBinder() {
		return this.userBinder;
	}

	/** Invoked for saving the person. This function must be overridden by the child class that need to do a specific
	 * saving process. 
	 *
	 * @param person the person to save.
	 * @param personService the service for accessing to the person. If it is {@code null},
	 *     the default implementation does nothing. This function must be overridden to save the person
	 *     without a repository.
	 * @param user the user to save.
	 * @param userService the service for accessing to the user. If it is {@code null},
	 *     the default implementation does nothing. This function must be overridden to save the user
	 *     without a repository.
	 * @return {@code true} if the saving was a success.
	 */
	protected boolean doSave(Person person, PersonService personService, User user, UserService userService) {
		boolean saved = false;
		if (personService != null) {
			try {
				personService.save(person);
				if (this.authenticatedUser != null && this.authenticatedUser.get().isPresent()) {
					final User loggedUser = this.authenticatedUser.get().get();
					if (loggedUser.getPerson().getId() == person.getId()) {
						notifyAuthenticatedUserChange();
					}
				}
				saved = true;
			} catch (Throwable ex) {
				notifySavingError(ex);
			}
		}
		if (userService != null) {
			try {
				if (Strings.isEmpty(user.getLogin())) {
					userService.remove(user);
				} else {
					userService.save(user);
				}
				saved = true;
			} catch (Throwable ex) {
				notifySavingError(ex);
			}
		}
		if (saved) {
			notifySaved();
			return true;
		}
		return false;
	}

	/** Invoked for validating the person. This function does not save.
	 * This function must be overridden by the child class that need to do a specific
	 * saving process. 
	 *
	 * @param person the person to validate.
	 */
	protected void doValidate(Person person) {
		try {
			this.validatedPerson.setValue(Boolean.TRUE);
			notifyValidated();
		} catch (Throwable ex) {
			notifyValidationError(ex);
		}
	}

	/** Notify the user that its personal information has changed.
	 */
	@SuppressWarnings("static-method")
	protected void notifyAuthenticatedUserChange() {
		UserIdentityChangedObserver app = null;
		final Component view = UI.getCurrent().getCurrentView();
		if (view != null) {
			app = view.findAncestor(UserIdentityChangedObserver.class);
		}
		if (app != null) {
			app.authenticatedUserIdentityChange();
		}
	}

	/** Notify the user that the form contains invalid information.
	 */
	protected void notifyInvalidity() {
		final Notification notification = Notification.show(getTranslation("views.save_invalid_data")); //$NON-NLS-1$
		notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
	}

	/** Notify the user that the person was saved.
	 */
	protected void notifySaved() {
		this.logger.info("Data for person named " + this.editedPerson.getFullName() + "(id: " + this.editedPerson.getId() + ") was successfully saved by " + AuthenticatedUser.getUserName(this.authenticatedUser)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		final Notification notification = Notification.show(getTranslation("views.persons.save_success", //$NON-NLS-1$
				this.editedPerson.getFullName()));
		notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
	}

	/** Notify the user that the person was validated.
	 */
	protected void notifyValidated() {
		final Notification notification = Notification.show(getTranslation("views.persons.validation_success", //$NON-NLS-1$
				this.editedPerson.getFullName()));
		notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
	}

	/** Notify the user that the person cannot be saved.
	 */
	protected void notifySavingError(Throwable error) {
		this.logger.warn("Error when saving data for person named " + this.editedPerson.getFullName() + "(id: " + this.editedPerson.getId() + "): " + error.getLocalizedMessage(), error); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		final Notification notification = Notification.show(getTranslation("views.persons.save_error", //$NON-NLS-1$ 
				this.editedPerson.getFullName(), error.getLocalizedMessage()));
		notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
	}

	/** Notify the user that the person cannot be validated.
	 */
	protected void notifyValidationError(Throwable error) {
		this.logger.warn("Error when validating the person named " + this.editedPerson.getFullName() + "(id: " + this.editedPerson.getId() + "): " + error.getLocalizedMessage(), error); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		final Notification notification = Notification.show(getTranslation("views.persons.validation_error", //$NON-NLS-1$ 
				this.editedPerson.getFullName(), error.getLocalizedMessage()));
		notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
	}

	private void updatePhoto() {
		final var photoURL = getEditedEntity().getPhotoURL();
		this.photo.removeAll();
		if (photoURL != null) {
			final var url = photoURL.toExternalForm();
			final var image = new Image(url, url);
			image.setMinHeight(ViewConstants.PHOTO_SIZE, Unit.PIXELS);
			image.setMaxHeight(ViewConstants.PHOTO_SIZE, Unit.PIXELS);
			image.setMinWidth(ViewConstants.PHOTO_SIZE, Unit.PIXELS);
			image.setMaxWidth(ViewConstants.PHOTO_SIZE, Unit.PIXELS);
			this.photo.add(image);			
		} else {
			final StringBuilder size = new StringBuilder();
			size.append(ViewConstants.PHOTO_SIZE);
			size.append(Unit.PIXELS.getSymbol());
			final Icon image = LumoIcon.USER.create();
			image.setSize(size.toString());
			this.photo.add(image);			
		}
	}
	
	/** Create the components for entering the personal informations.
	 * The information includes:<ul>
	 * <li>Last Name</li>
	 * <li>First Name</li>
	 * <li>Gender</li>
	 * <li>Gravatar identifier</li>
	 * </ul>
	 *
	 * @param receiver the receiver of the component
	 */
	protected void createPersonalInformationComponents(VerticalLayout receiver) {
		final FormLayout content = ComponentFactory.newColumnForm(2);

		this.lastname = new TextField();
		this.lastname.setRequired(true);
		this.lastname.setClearButtonVisible(true);
		content.add(this.lastname);

		this.firstname = new TextField();
		this.firstname.setRequired(true);
		this.firstname.setClearButtonVisible(true);
		content.add(this.firstname);

		this.gender = new ComboBox<>();
		this.gender.setItems(Gender.values());
		this.gender.setItemLabelGenerator(this::getGenderLabel);
		this.gender.setValue(Gender.NOT_SPECIFIED);
		content.add(this.gender, 2);

		this.gravatarId = ComponentFactory.newClickableIconTextField(Person.GRAVATAR_ROOT_URL, GRAVATAR_ICON);
		this.gravatarId.setPrefixComponent(VaadinIcon.CAMERA.create());
		this.gravatarId.setClearButtonVisible(true);
		this.gravatarId.addValidationStatusChangeListener(it -> {
			updatePhoto();
		});
		content.add(this.gravatarId, 2);
		
		this.photo = new VerticalLayout();
		this.photo.addClassNames(LumoUtility.Border.ALL, LumoUtility.BorderColor.CONTRAST_10);
		this.photo.setAlignItems(Alignment.CENTER);
		this.photo.setJustifyContentMode(JustifyContentMode.CENTER);
		content.add(this.photo, 2);
		updatePhoto();
		
		this.personalInformationDetails = new DetailsWithErrorMark(content);
		this.personalInformationDetails.setOpened(false);
		receiver.add(this.personalInformationDetails);

		this.personBinder.forField(this.lastname).bind(Person::getLastName, Person::setLastName);
		this.personBinder.forField(this.firstname).bind(Person::getFirstName, Person::setFirstName);
		this.personBinder.forField(this.gender).bind(Person::getGender, Person::setGender);
		this.personBinder.forField(this.gravatarId).bind(Person::getGravatarId, Person::setGravatarId);
	}

	private String getGenderLabel(Gender gender) {
		return gender.getLabel(this.messages, getLocale());
	}

	/** Create the components for entering the contact informations.
	 * The information includes:<ul>
	 * <li>Email</li>
	 * <li>Office phone</li>
	 * <li>Mobile phone</li>
	 * <li>Office room</li>
	 * </ul>
	 *
	 * @param receiver the receiver of the component
	 */
	protected void createContactInformationComponents(VerticalLayout receiver) {
		final var content = ComponentFactory.newColumnForm(1);

		this.email = new TextField();
		this.email.setRequired(true);
		this.email.setPrefixComponent(VaadinIcon.AT.create());
		this.email.setClearButtonVisible(true);
		content.add(this.email, 2);

		this.officePhone = new PhoneNumberField();
		this.officePhone.setClearButtonVisible(true);
		this.officePhone.setPrefixComponent(VaadinIcon.PHONE.create());
		content.add(this.officePhone, 2);

		this.mobilePhone = new PhoneNumberField();
		this.mobilePhone.setClearButtonVisible(true);
		this.mobilePhone.setPrefixComponent(VaadinIcon.MOBILE.create());
		content.add(this.mobilePhone, 2);

		this.officeRoom = new TextField();
		this.officeRoom.setClearButtonVisible(true);
		this.officeRoom.setPrefixComponent(VaadinIcon.OFFICE.create());
		content.add(this.officeRoom, 2);

		this.contactInformationDetails = new DetailsWithErrorMark(content);
		this.contactInformationDetails.setOpened(false);
		receiver.add(this.contactInformationDetails);

		this.personBinder.forField(this.email)
			.withValidator(new EmailValidator(getTranslation("views.forms.email.invalid_format"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.email, this.contactInformationDetails))
			.bind(Person::getEmail, Person::setEmail);
		this.personBinder.forField(this.officePhone)
			.withValidator(this.officePhone.getDefaultValidator())
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.officePhone, this.contactInformationDetails))
			.bind(Person::getOfficePhone, Person::setOfficePhone);
		this.personBinder.forField(this.mobilePhone)
			.withValidator(this.mobilePhone.getDefaultValidator())
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.mobilePhone, this.contactInformationDetails))
			.bind(Person::getMobilePhone, Person::setMobilePhone);
		this.personBinder.forField(this.officeRoom).bind(Person::getOfficeRoom, Person::setOfficeRoom);
	}

	/** Create the components for entering the researcher identifiers.
	 * The information includes:<ul>
	 * <li>ORCID</li>
	 * <li>Identifier on Scopus (Elsevier)</li>
	 * <li>Identifier on ResearcherID, WoS or Publon</li>
	 * <li>Identifier for Google Scholar</li>
	 * </ul>
	 *
	 * @param receiver the receiver of the component
	 */
	protected void createResearcherIdsComponents(VerticalLayout receiver) {
		final var content = ComponentFactory.newColumnForm(2);

		this.orcid = ComponentFactory.newClickableIconTextField(Person.ORCID_BASE_URL, ORCID_ICON);
		this.orcid.setClearButtonVisible(true);
		this.orcid.setPrefixComponent(VaadinIcon.HASH.create());
		content.add(this.orcid, 2);

		this.wos = ComponentFactory.newClickableIconTextField(Person.WOS_BASE_URL, WOS_ICON);
		this.wos.setClearButtonVisible(true);
		this.wos.setPrefixComponent(VaadinIcon.HASH.create());
		content.add(this.wos, 2);

		this.scopus = ComponentFactory.newClickableIconTextField(Person.SCOPUS_BASE_URL, SCOPUS_ICON);
		this.scopus.setClearButtonVisible(true);
		this.scopus.setPrefixComponent(VaadinIcon.HASH.create());
		content.add(this.scopus, 2);

		this.gscholar = ComponentFactory.newClickableIconTextField(Person.GSCHOLAR_BASE_URL, GSCHOLAR_ICON);
		this.gscholar.setClearButtonVisible(true);
		this.gscholar.setPrefixComponent(VaadinIcon.HASH.create());
		content.add(this.gscholar, 2);

		this.researcherIdsDetails = new DetailsWithErrorMark(content);
		this.researcherIdsDetails.setOpened(false);
		receiver.add(this.researcherIdsDetails);

		this.personBinder.forField(this.orcid)
			.withValidator(new OrcidValidator(getTranslation("views.persons.invalid_orcid"), //$NON-NLS-1$
					getTranslation("views.persons.undesired_orcid"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.orcid, this.researcherIdsDetails))
			.bind(Person::getORCID, Person::setORCID);
		this.personBinder.forField(this.scopus).bind(Person::getScopusId, Person::setScopusId);
		this.personBinder.forField(this.wos).bind(Person::getResearcherId, Person::setResearcherId);
		this.personBinder.forField(this.gscholar).bind(Person::getGoogleScholarId, Person::setGoogleScholarId);
	}

	/** Create the components for entering the researcher indicators and indexes.
	 * The information includes:<ul>
	 * <li>H-index on Web-of-Science (WoS)</li>
	 * <li>H-index on Scopus</li>
	 * <li>H-index on Google Scholar</li>
	 * <li>Number of paper citations on Web-of-Science (WoS)</li>
	 * <li>Number of paper citations on Scopus</li>
	 * <li>Number of paper citations on Google Scholar</li>
	 * </ul>
	 *
	 * @param receiver the receiver of the component
	 */
	protected void createIndexesComponents(VerticalLayout receiver) {
		final var content0 = ComponentFactory.newColumnForm(1);

		this.wosHindex = new IntegerField();
		this.wosHindex.setMin(0);
		this.wosHindex.setMax(100);
		this.wosHindex.setClearButtonVisible(true);
		this.wosHindex.setPrefixComponent(VaadinIcon.CONTROLLER.create());
		content0.add(this.wosHindex);

		this.scopusHindex = new IntegerField();
		this.scopusHindex.setMin(0);
		this.scopusHindex.setMax(100);
		this.scopusHindex.setClearButtonVisible(true);
		this.scopusHindex.setPrefixComponent(VaadinIcon.CONTROLLER.create());
		content0.add(this.scopusHindex);

		this.gscholarHindex = new IntegerField();
		this.gscholarHindex.setMin(0);
		this.gscholarHindex.setMax(100);
		this.gscholarHindex.setClearButtonVisible(true);
		this.gscholarHindex.setPrefixComponent(VaadinIcon.CONTROLLER.create());
		content0.add(this.gscholarHindex);

		final var content1 = ComponentFactory.newColumnForm(1);

		this.wosCitations = new IntegerField();
		this.wosCitations.setMin(0);
		this.wosCitations.setMax(100000);
		this.wosCitations.setClearButtonVisible(true);
		this.wosCitations.setPrefixComponent(VaadinIcon.TRENDING_UP.create());
		this.wosCitations.setSuffixComponent(new Div(new Text(getTranslation("views.persons.citations")))); //$NON-NLS-1$
		content1.add(this.wosCitations);

		this.scopusCitations = new IntegerField();
		this.scopusCitations.setMin(0);
		this.scopusCitations.setMax(100000);
		this.scopusCitations.setClearButtonVisible(true);
		this.scopusCitations.setPrefixComponent(VaadinIcon.TRENDING_UP.create());
		this.scopusCitations.setSuffixComponent(new Div(new Text(getTranslation("views.persons.citations")))); //$NON-NLS-1$
		content1.add(this.scopusCitations);

		this.gscholarCitations = new IntegerField();
		this.gscholarCitations.setMin(0);
		this.gscholarCitations.setMax(100000);
		this.gscholarCitations.setClearButtonVisible(true);
		this.gscholarCitations.setPrefixComponent(VaadinIcon.TRENDING_UP.create());
		this.gscholarCitations.setSuffixComponent(new Div(new Text(getTranslation("views.persons.citations")))); //$NON-NLS-1$
		content1.add(this.gscholarCitations);

		final VerticalLayout vl = new VerticalLayout(content0, content1);
		vl.setSpacing(false);

		this.indexesDetails = new Details("", vl); //$NON-NLS-1$
		this.indexesDetails.setOpened(false);
		receiver.add(this.indexesDetails);

		this.personBinder.forField(this.wosHindex).bind(Person::getWosHindex, Person::setWosHindex);
		this.personBinder.forField(this.scopusHindex).bind(Person::getScopusHindex, Person::setScopusHindex);
		this.personBinder.forField(this.gscholarHindex).bind(Person::getGoogleScholarHindex, Person::setGoogleScholarHindex);
		this.personBinder.forField(this.wosCitations).bind(Person::getWosCitations, Person::setWosCitations);
		this.personBinder.forField(this.scopusCitations).bind(Person::getScopusCitations, Person::setScopusCitations);
		this.personBinder.forField(this.gscholarCitations).bind(Person::getGoogleScholarCitations, Person::setGoogleScholarCitations);
	}

	/** Create the components for entering the social links.
	 * The information includes:<ul>
	 * <li>Identifier on Research Gate</li>
	 * <li>Identifier on AD Scientific Index</li>
	 * <li>Internet address of the DBLP page</li>
	 * <li>Internet address of the Academia.edu</li>
	 * <li>Internet address of the EU CORDIS page</li>
	 * <li>Identifier on Linked-In</li>
	 * <li>Github Identifier</li>
	 * <li>Facebook Identifier</li>
	 * </ul>
	 *
	 * @param receiver the receiver of the component
	 */
	protected void createSocialLinksComponents(VerticalLayout receiver) {
		final FormLayout content = ComponentFactory.newColumnForm(1);

		this.researchGate = ComponentFactory.newClickableIconTextField(Person.RESEARCHGATE_BASE_URL, RESEARCHGATE_ICON);
		this.researchGate.setClearButtonVisible(true);
		this.researchGate.setPrefixComponent(VaadinIcon.HASH.create());
		content.add(this.researchGate);

		this.adScientificIndex = ComponentFactory.newClickableIconTextField(Person.ADSCIENTIFICINDEX_BASE_URL, ADSCIENTIFICINDEX_ICON);
		this.adScientificIndex.setClearButtonVisible(true);
		this.adScientificIndex.setPrefixComponent(VaadinIcon.HASH.create());
		content.add(this.adScientificIndex);

		this.dblp = ComponentFactory.newClickableIconTextField(DBLP_BASE_URL, DBLP_ICON);
		this.dblp.setClearButtonVisible(true);
		this.dblp.setPrefixComponent(VaadinIcon.EXTERNAL_LINK.create());
		content.add(this.dblp);

		this.academiaEdu = ComponentFactory.newClickableIconTextField(ACADEMIA_EDU_BASE_URL, ACADEMIA_EDU_ICON);
		this.academiaEdu.setPrefixComponent(VaadinIcon.EXTERNAL_LINK.create());
		this.academiaEdu.setClearButtonVisible(true);
		content.add(this.academiaEdu);

		this.euCordis = ComponentFactory.newClickableIconTextField(EU_CORDIS_BASE_URL, EU_CORDIS_ICON);
		this.euCordis.setPrefixComponent(VaadinIcon.EXTERNAL_LINK.create());
		this.euCordis.setClearButtonVisible(true);
		content.add(this.euCordis);

		this.linkedin = ComponentFactory.newClickableIconTextField(Person.LINKEDIN_BASE_URL, LINKEDIN_ICON);
		this.linkedin.setClearButtonVisible(true);
		this.linkedin.setPrefixComponent(VaadinIcon.HASH.create());
		content.add(this.linkedin);

		this.github = ComponentFactory.newClickableIconTextField(Person.GITHUB_BASE_URL, GITHUB_ICON);
		this.github.setClearButtonVisible(true);
		this.github.setPrefixComponent(VaadinIcon.HASH.create());
		content.add(this.github);

		this.facebook = ComponentFactory.newClickableIconTextField(Person.FACEBOOK_BASE_URL, FACEBOOK_ICON);
		this.facebook.setClearButtonVisible(true);
		this.facebook.setPrefixComponent(VaadinIcon.HASH.create());
		content.add(this.facebook);

		this.socialLinksDetails = new DetailsWithErrorMark(content);
		this.socialLinksDetails.setOpened(false);
		receiver.add(this.socialLinksDetails);

		final String invalidUrl = getTranslation("views.urls.invalid_format"); //$NON-NLS-1$
		
		this.personBinder.forField(this.researchGate).bind(Person::getResearchGateId, Person::setResearchGateId);
		this.personBinder.forField(this.adScientificIndex).bind(Person::getAdScientificIndexId, Person::setAdScientificIndexId);
		this.personBinder.forField(this.dblp)
			.withValidator(new UrlValidator(invalidUrl))
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.dblp, this.socialLinksDetails))
			.bind(Person::getDblpURL, Person::setDblpURL);
		this.personBinder.forField(this.academiaEdu)
			.withValidator(new UrlValidator(invalidUrl))
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.academiaEdu, this.socialLinksDetails))
			.bind(Person::getAcademiaURL, Person::setAcademiaURL);
		this.personBinder.forField(this.euCordis)
			.withValidator(new UrlValidator(invalidUrl))
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.euCordis, this.socialLinksDetails))
			.bind(Person::getCordisURL, Person::setCordisURL);
		this.personBinder.forField(this.linkedin).bind(Person::getLinkedInId, Person::setLinkedInId);
		this.personBinder.forField(this.github).bind(Person::getGithubId, Person::setGithubId);
		this.personBinder.forField(this.facebook).bind(Person::getFacebookId, Person::setFacebookId);
	}

	/** Create the components for adminitrator.
	 * The information includes:<ul>
	 * <li>Naming convention for the person's page on the institution website</li>
	 * <li>Is the person information validated by a local authority?</li>
	 * </ul>
	 *
	 * @param receiver the receiver of the component
	 */
	protected void createAdministrationComponents(VerticalLayout receiver) {
		final FormLayout content = ComponentFactory.newColumnForm(1);

		this.webpageConvention = new ComboBox<>();
		this.webpageConvention.setItems(WebPageNaming.values());
		this.webpageConvention.setItemLabelGenerator(this::getWebPageNamingLabel);
		this.webpageConvention.setValue(WebPageNaming.UNSPECIFIED);
		content.add(this.webpageConvention, 2);

		this.userLogin = new TextField();
		this.userLogin.setClearButtonVisible(true);
		this.userLogin.setPrefixComponent(VaadinIcon.HASH.create());
		content.add(this.userLogin);
		
		this.userRole = new ComboBox<>();
		this.userRole.setItems(UserRole.values());
		this.userRole.setItemLabelGenerator(this::getUserRoleLabel);
		this.userRole.setPrefixComponent(VaadinIcon.MEDAL.create());
		this.userRole.setValue(UserRole.USER);
		content.add(this.userRole);

		this.validatedPerson = new Checkbox();
		content.add(this.validatedPerson);

		this.administrationDetails = new Details("", content); //$NON-NLS-1$
		this.administrationDetails.setOpened(false);
		this.administrationDetails.addThemeVariants(DetailsVariant.FILLED);
		receiver.add(this.administrationDetails);

		this.personBinder.forField(this.webpageConvention).bind(Person::getWebPageNaming, Person::setWebPageNaming);
		this.personBinder.forField(this.validatedPerson).bind(Person::isValidated, Person::setValidated);
		this.userBinder.forField(this.userLogin).bind(User::getLogin, User::setLogin);
		this.userBinder.forField(this.userRole).bind(User::getRole, User::setRole);
	}

	private String getWebPageNamingLabel(WebPageNaming naming) {
		return naming.getLabel(this.messages, getLocale());
	}

	private String getUserRoleLabel(UserRole role) {
		return role.getLabel(this.messages, getLocale());
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		this.personalInformationDetails.setSummaryText(getTranslation("views.persons.personal_informations")); //$NON-NLS-1$
		this.lastname.setLabel(getTranslation("views.persons.last_name")); //$NON-NLS-1$
		this.firstname.setLabel(getTranslation("views.persons.last_name")); //$NON-NLS-1$
		this.gender.setLabel(getTranslation("views.persons.gender")); //$NON-NLS-1$
		this.gender.setHelperText(getTranslation("views.persons.gender.helper")); //$NON-NLS-1$);
		this.gender.setItemLabelGenerator(this::getGenderLabel);
		this.gravatarId.setLabel(getTranslation("views.persons.gravatar_id")); //$NON-NLS-1$
		this.gravatarId.setHelperText(getTranslation("views.persons.gravatar_id.helper")); //$NON-NLS-1$

		final String helpPhone = getTranslation("views.phonenumber.help"); //$NON-NLS-1$
		final String errPhone = getTranslation("views.phonenumber.invalid"); //$NON-NLS-1$

		this.contactInformationDetails.setSummaryText(getTranslation("views.persons.contact_informations")); //$NON-NLS-1$
		this.email.setLabel(getTranslation("views.persons.email")); //$NON-NLS-1$
		this.email.setErrorMessage(getTranslation("views.persons.email.error")); //$NON-NLS-1$
		this.officePhone.setLabel(getTranslation("views.persons.officephone")); //$NON-NLS-1$
		this.officePhone.setDynamicHelperText(helpPhone);
		this.officePhone.setErrorMessage(errPhone);
		this.mobilePhone.setLabel(getTranslation("views.persons.mobilephone")); //$NON-NLS-1$
		this.mobilePhone.setDynamicHelperText(helpPhone);
		this.officeRoom.setLabel(getTranslation("views.persons.officeroom")); //$NON-NLS-1$
		this.officeRoom.setHelperText(getTranslation("views.persons.officeroom.helper")); //$NON-NLS-1$

		this.researcherIdsDetails.setSummaryText(getTranslation("views.persons.researcher_ids")); //$NON-NLS-1$
		this.orcid.setLabel(getTranslation("views.persons.orcid")); //$NON-NLS-1$
		this.orcid.setHelperText(getTranslation("views.persons.orcid.helper")); //$NON-NLS-1$
		this.scopus.setLabel(getTranslation("views.persons.scopus")); //$NON-NLS-1$
		this.scopus.setHelperText(getTranslation("views.persons.scopus.helper")); //$NON-NLS-1$
		this.wos.setLabel(getTranslation("views.persons.wos")); //$NON-NLS-1$
		this.wos.setHelperText(getTranslation("views.persons.wos.helper")); //$NON-NLS-1$
		this.gscholar.setLabel(getTranslation("views.persons.gscholar")); //$NON-NLS-1$
		this.gscholar.setHelperText(getTranslation("views.persons.gscholar.helper")); //$NON-NLS-1$

		this.indexesDetails.setSummaryText(getTranslation("views.persons.indexes")); //$NON-NLS-1$
		this.wosHindex.setLabel(getTranslation("views.persons.wos_hindex")); //$NON-NLS-1$
		this.wosHindex.setHelperText(getTranslation("views.persons.wos_update.helper")); //$NON-NLS-1$
		this.scopusHindex.setLabel(getTranslation("views.persons.scopus_hindex")); //$NON-NLS-1$
		this.scopusHindex.setHelperText(getTranslation("views.persons.scopus_update.helper")); //$NON-NLS-1$
		this.gscholarHindex.setLabel(getTranslation("views.persons.gscholar_hindex")); //$NON-NLS-1$
		this.gscholarHindex.setHelperText(getTranslation("views.persons.gscholar_update.helper")); //$NON-NLS-1$
		this.wosCitations.setLabel(getTranslation("views.persons.wos_citations")); //$NON-NLS-1$
		this.wosCitations.setHelperText(getTranslation("views.persons.wos_update.helper")); //$NON-NLS-1$
		this.scopusCitations.setLabel(getTranslation("views.persons.scopus_citations")); //$NON-NLS-1$
		this.scopusCitations.setHelperText(getTranslation("views.persons.scopus_update.helper")); //$NON-NLS-1$
		this.gscholarCitations.setLabel(getTranslation("views.persons.gscholar_citations")); //$NON-NLS-1$
		this.gscholarCitations.setHelperText(getTranslation("views.persons.gscholar_update.helper")); //$NON-NLS-1$

		this.socialLinksDetails.setSummaryText(getTranslation("views.persons.social_links")); //$NON-NLS-1$
		this.researchGate.setLabel(getTranslation("views.persons.researchgate")); //$NON-NLS-1$
		this.researchGate.setHelperText(getTranslation("views.persons.researchgate.helper")); //$NON-NLS-1$
		this.adScientificIndex.setLabel(getTranslation("views.persons.adscientificindex")); //$NON-NLS-1$
		this.dblp.setLabel(getTranslation("views.persons.dblp")); //$NON-NLS-1$
		this.academiaEdu.setLabel(getTranslation("views.persons.academia_edu")); //$NON-NLS-1$
		this.euCordis.setLabel(getTranslation("views.persons.eu_cordis")); //$NON-NLS-1$
		this.euCordis.setHelperText(getTranslation("views.persons.eu_cordis.helper")); //$NON-NLS-1$
		this.linkedin.setLabel(getTranslation("views.persons.linkedin")); //$NON-NLS-1$
		this.linkedin.setHelperText(getTranslation("views.persons.linkedin.helper")); //$NON-NLS-1$
		this.github.setLabel(getTranslation("views.persons.github")); //$NON-NLS-1$
		this.github.setHelperText(getTranslation("views.persons.github.helper")); //$NON-NLS-1$
		this.facebook.setLabel(getTranslation("views.persons.facebook")); //$NON-NLS-1$
		this.facebook.setHelperText(getTranslation("views.persons.facebook.helper")); //$NON-NLS-1$

		this.administrationDetails.setSummaryText(getTranslation("views.persons.administration_details")); //$NON-NLS-1$
		this.webpageConvention.setLabel(getTranslation("views.persons.administration.webpage_naming")); //$NON-NLS-1$
		this.webpageConvention.setItemLabelGenerator(this::getWebPageNamingLabel);
		this.validatedPerson.setLabel(getTranslation("views.persons.administration.validated_person")); //$NON-NLS-1$
		this.userLogin.setLabel(getTranslation("views.persons.administration.user_login")); //$NON-NLS-1$
		this.userLogin.setHelperText(getTranslation("views.persons.administration.user_login.help")); //$NON-NLS-1$
		this.userRole.setLabel(getTranslation("views.persons.administration.user_role")); //$NON-NLS-1$
		this.userRole.setHelperText(getTranslation("views.persons.administration.user_role.help")); //$NON-NLS-1$
		this.userRole.setItemLabelGenerator(this::getUserRoleLabel);
	}

}
