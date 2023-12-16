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

package fr.utbm.ciad.labmanager.views.appviews.persons;

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

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
import fr.utbm.ciad.labmanager.views.ViewConstants;
import fr.utbm.ciad.labmanager.views.components.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.DetailsWithErrorMark;
import fr.utbm.ciad.labmanager.views.components.DetailsWithErrorMarkStatusHandler;
import fr.utbm.ciad.labmanager.views.components.PhoneNumberField;
import fr.utbm.ciad.labmanager.views.components.UserIdentityChangedObserver;
import fr.utbm.ciad.labmanager.views.validators.OrcidValidator;
import fr.utbm.ciad.labmanager.views.validators.UrlValidator;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;

/** Abstract implementation for the information related to a person.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Uses(Icon.class)
public abstract class AbstractPersonEditorView extends Composite<VerticalLayout> implements LocaleChangeObserver {

	private static final long serialVersionUID = 6088942935576032576L;
	
	private final Logger logger;

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

	private final Binder<Person> binder = new Binder<>(Person.class);

	private Person editedPerson;

	/** Constructor.
	 *
	 * @param person the person to edit, never {@code null}.
	 * @param personService the service for accessing to the person. If it is {@code null},
	 *     the function {@link #doSave(Person)} will be invoked; otherwise the repository will be invoked.
	 * @param authenticatedUser the connected user.
	 * @param logger the logger to be used by this view.
	 */
	public AbstractPersonEditorView(Person person, PersonService personService, AuthenticatedUser authenticatedUser,
			Logger logger) {
		assert person != null;

		this.editedPerson = person;
		this.logger = logger;
		this.authenticatedUser = authenticatedUser;
		
		final VerticalLayout rootContainer = getContent();
		rootContainer.setSpacing(true);

		createPersonalInformationComponents(rootContainer);
		createContactInformationComponents(rootContainer);
		createResearcherIdsComponents(rootContainer);
		createIndexesComponents(rootContainer);
		createSocialLinksComponents(rootContainer);

		final boolean isAdmin = this.authenticatedUser != null && this.authenticatedUser.get().isPresent()
				&& this.authenticatedUser.get().get().getRole() == UserRole.ADMIN;
		
		if (isAdmin) {
			createAdministrationComponents(rootContainer);
		}

		this.binder.setBean(person);
		
		final HorizontalLayout bar = new HorizontalLayout();
		rootContainer.add(bar);
		
		final Button saveButton = new Button(getTranslation("views.save"), event -> { //$NON-NLS-1$
			if (this.binder.validate().isOk()) {
				doSave(this.editedPerson, personService);
			} else {
				notifyInvalidity();
			}
		});
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		saveButton.addClickShortcut(Key.ENTER);
		bar.add(saveButton);

		if (isAdmin) {
			final Button validateButton = new Button(getTranslation("views.validate"), event -> { //$NON-NLS-1$
				doValidate(this.editedPerson);
				if (this.binder.validate().isOk()) {
					doSave(this.editedPerson, personService);
				} else {
					notifyInvalidity();
				}
			});
			validateButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
			bar.add(validateButton);
		}
	}
	
	/** Replies the person which is shown and edited by this view.
	 *
	 * @return the person.
	 */
	public Person getEditedPerson() {
		return this.editedPerson;
	}

	/** Invoked for saving the person. This function must be overridden by the child class that need to do a specific
	 * saving process. 
	 *
	 * @param personService the service for accessing to the person. If it is {@code null},
	 *     the default implementation does nothing. This function must be overridden to save the person
	 *     without a repository.
	 * @param person the person to save.
	 */
	protected void doSave(Person person, PersonService personService) {
		if (personService != null) {
			try {
				personService.save(person);
				if (this.authenticatedUser != null && this.authenticatedUser.get().isPresent()) {
					final User user = this.authenticatedUser.get().get();
					if (user.getPerson().getId() == person.getId()) {
						notifyAuthenticatedUserChange();
					}
				}
				notifySaved();
			} catch (Throwable ex) {
				notifySavingError(ex);
			}
		}
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
	protected void notifyAuthenticatedUserChange() {
		final UserIdentityChangedObserver app = findAncestor(UserIdentityChangedObserver.class);
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
		final URL photoURL = this.editedPerson.getPhotoURL();
		this.photo.removeAll();
		if (photoURL != null) {
			final String url = photoURL.toExternalForm();
			final Image image = new Image(url, url);
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
		this.gender.setItemLabelGenerator(Gender::getLabel);
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

		this.binder.forField(this.lastname).bind(Person::getLastName, Person::setLastName);
		this.binder.forField(this.firstname).bind(Person::getFirstName, Person::setFirstName);
		this.binder.forField(this.gender).bind(Person::getGender, Person::setGender);
		this.binder.forField(this.gravatarId).bind(Person::getGravatarId, Person::setGravatarId);
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
		final FormLayout content = ComponentFactory.newColumnForm(1);

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

		this.binder.forField(this.email)
			.withValidator(new EmailValidator(getTranslation("views.forms.email.invalid_format"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.email, this.contactInformationDetails))
			.bind(Person::getEmail, Person::setEmail);
		this.binder.forField(this.officePhone)
			.withValidator(this.officePhone.getDefaultValidator())
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.officePhone, this.contactInformationDetails))
			.bind(Person::getOfficePhone, Person::setOfficePhone);
		this.binder.forField(this.mobilePhone)
			.withValidator(this.mobilePhone.getDefaultValidator())
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.mobilePhone, this.contactInformationDetails))
			.bind(Person::getMobilePhone, Person::setMobilePhone);
		this.binder.forField(this.officeRoom).bind(Person::getOfficeRoom, Person::setOfficeRoom);
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
		final FormLayout content = ComponentFactory.newColumnForm(2);

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

		this.binder.forField(this.orcid)
			.withValidator(new OrcidValidator(getTranslation("views.persons.invalid_orcid"), //$NON-NLS-1$
					getTranslation("views.persons.undesired_orcid"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.orcid, this.researcherIdsDetails))
			.bind(Person::getORCID, Person::setORCID);
		this.binder.forField(this.scopus).bind(Person::getScopusId, Person::setScopusId);
		this.binder.forField(this.wos).bind(Person::getResearcherId, Person::setResearcherId);
		this.binder.forField(this.gscholar).bind(Person::getGoogleScholarId, Person::setGoogleScholarId);
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
		final FormLayout content0 = ComponentFactory.newColumnForm(1);

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

		final FormLayout content1 = ComponentFactory.newColumnForm(1);

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

		this.binder.forField(this.wosHindex).bind(Person::getWosHindex, Person::setWosHindex);
		this.binder.forField(this.scopusHindex).bind(Person::getScopusHindex, Person::setScopusHindex);
		this.binder.forField(this.gscholarHindex).bind(Person::getGoogleScholarHindex, Person::setGoogleScholarHindex);
		this.binder.forField(this.wosCitations).bind(Person::getWosCitations, Person::setWosCitations);
		this.binder.forField(this.scopusCitations).bind(Person::getScopusCitations, Person::setScopusCitations);
		this.binder.forField(this.gscholarCitations).bind(Person::getGoogleScholarCitations, Person::setGoogleScholarCitations);
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
		
		this.binder.forField(this.researchGate).bind(Person::getResearchGateId, Person::setResearchGateId);
		this.binder.forField(this.adScientificIndex).bind(Person::getAdScientificIndexId, Person::setAdScientificIndexId);
		this.binder.forField(this.dblp)
			.withValidator(new UrlValidator(invalidUrl))
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.dblp, this.socialLinksDetails))
			.bind(Person::getDblpURL, Person::setDblpURL);
		this.binder.forField(this.academiaEdu)
			.withValidator(new UrlValidator(invalidUrl))
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.academiaEdu, this.socialLinksDetails))
			.bind(Person::getAcademiaURL, Person::setAcademiaURL);
		this.binder.forField(this.euCordis)
			.withValidator(new UrlValidator(invalidUrl))
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.euCordis, this.socialLinksDetails))
			.bind(Person::getCordisURL, Person::setCordisURL);
		this.binder.forField(this.linkedin).bind(Person::getLinkedInId, Person::setLinkedInId);
		this.binder.forField(this.github).bind(Person::getGithubId, Person::setGithubId);
		this.binder.forField(this.facebook).bind(Person::getFacebookId, Person::setFacebookId);
	}

	/** Create the components for adminitrator.
	 * The information includes:<ul>
	 * <li>Naming convention for the person's page on the institution website</li>
	 * <li>Is the person information validated by a local authority?</li>
	 * </ul>
	 *
	 * @param receiver the receiver of the component
	 */
	@RolesAllowed("ROLE_ADMIN")
	protected void createAdministrationComponents(VerticalLayout receiver) {
		final FormLayout content = ComponentFactory.newColumnForm(1);

		this.webpageConvention = new ComboBox<>();
		this.webpageConvention.setItems(WebPageNaming.values());
		this.webpageConvention.setItemLabelGenerator(WebPageNaming::getLabel);
		this.webpageConvention.setValue(WebPageNaming.UNSPECIFIED);
		content.add(this.webpageConvention);

		this.validatedPerson = new Checkbox();
		content.add(this.validatedPerson);

		this.administrationDetails = new Details("", content); //$NON-NLS-1$
		this.administrationDetails.setOpened(false);
		receiver.add(this.administrationDetails);

		this.binder.forField(this.webpageConvention).bind(Person::getWebPageNaming, Person::setWebPageNaming);
		this.binder.forField(this.validatedPerson).bind(Person::isValidated, Person::setValidated);
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		this.personalInformationDetails.setSummaryText(getTranslation("views.persons.personal_informations")); //$NON-NLS-1$
		this.lastname.setLabel(getTranslation("views.persons.last_name")); //$NON-NLS-1$
		this.firstname.setLabel(getTranslation("views.persons.last_name")); //$NON-NLS-1$
		this.gender.setLabel(getTranslation("views.persons.gender")); //$NON-NLS-1$
		this.gender.setHelperText(getTranslation("views.persons.gender.helper")); //$NON-NLS-1$);
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
		this.validatedPerson.setLabel(getTranslation("views.persons.administration.validated_person")); //$NON-NLS-1$
	}

}
