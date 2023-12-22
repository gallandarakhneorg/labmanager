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

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.member.Gender;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.member.WebPageNaming;
import fr.utbm.ciad.labmanager.data.user.User;
import fr.utbm.ciad.labmanager.data.user.UserRole;
import fr.utbm.ciad.labmanager.services.user.UserService.EditingContext;
import fr.utbm.ciad.labmanager.views.ViewConstants;
import fr.utbm.ciad.labmanager.views.components.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.converters.StringTrimer;
import fr.utbm.ciad.labmanager.views.components.details.DetailsWithErrorMark;
import fr.utbm.ciad.labmanager.views.components.details.DetailsWithErrorMarkStatusHandler;
import fr.utbm.ciad.labmanager.views.components.entities.AbstractEntityEditor;
import fr.utbm.ciad.labmanager.views.components.phones.PhoneNumberField;
import fr.utbm.ciad.labmanager.views.components.validators.NotEmptyStringValidator;
import fr.utbm.ciad.labmanager.views.components.validators.OrcidValidator;
import fr.utbm.ciad.labmanager.views.components.validators.UrlValidator;
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
public abstract class AbstractPersonEditor extends AbstractEntityEditor<Person> {

	private static final long serialVersionUID = 4532244485914692596L;

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

	private ComboBox<WebPageNaming> webpageConvention;

	private TextField userLogin;

	private ComboBox<UserRole> userRole;

	private final Binder<User> userBinder = new Binder<>(User.class);

	private final EditingContext userContext;

	/** Constructor.
	 *
	 * @param userContext the editing context for the user.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 * @param logger the logger to be used by this view.
	 */
	public AbstractPersonEditor(EditingContext userContext,
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages, Logger logger) {
		super(Person.class, authenticatedUser, messages, logger,
				"views.persons.administration_details", //$NON-NLS-1$
				"views.persons.administration.validated_person"); //$NON-NLS-1$
		this.userContext = userContext;
	}

	@Override
	public boolean isValidData() {
		return super.isValidData() && getUserDataBinder().validate().isOk();
	}

	@Override
	protected void createEditorContent(VerticalLayout rootContainer, boolean isAdmin) {
		createPersonalInformationComponents(rootContainer);
		createContactInformationComponents(rootContainer);
		createResearcherIdsComponents(rootContainer);
		createIndexesComponents(rootContainer);
		createSocialLinksComponents(rootContainer);
		if (isAdmin) {
			createAdministrationComponents(rootContainer,
					content -> {
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

						getEntityDataBinder().forField(this.webpageConvention).bind(Person::getWebPageNaming, Person::setWebPageNaming);
						getUserDataBinder().forField(this.userLogin).bind(User::getLogin, User::setLogin);
						getUserDataBinder().forField(this.userRole).bind(User::getRole, User::setRole);
					},
					it -> it.bind(Person::isValidated, Person::setValidated));
		}
	}
	
	@Override
	protected void linkBeans() {
		super.linkBeans();
		getUserDataBinder().setBean(getEditedUser());
	}

	@Override
	public Person getEditedEntity() {
		return this.userContext.getPersonContext().getPerson();
	}

	/** Replies the user associated to the {@link #getEditedEntity() edited person} which is shown and edited by this view.
	 * The provided user may be not a valid JPA entity since it was not yet saved in the database.
	 *
	 * @return the user.
	 */
	public User getEditedUser() {
		return this.userContext.getUser();
	}

	/** Replies the binder of user data to the fields.
	 *
	 * @return the user data binder.
	 * @see #getPersonDataBinder()
	 */
	protected Binder<User> getUserDataBinder() {
		return this.userBinder;
	}

	@Override
	protected void doSave() throws Exception {
		if (this.userContext.savePersonAndSaveOrDeleteUser()) {
			final var auser = getAuthenticatedUser();
			if (auser != null && auser.get() != null && auser.get().isPresent()) {
				final var loggedUser = auser.get().get();
				if (loggedUser.getPerson().getId() == getEditedEntity().getId()) {
					notifyAuthenticatedUserChange();
				}
			}
		}
	}

	@Override
	public void notifySaved() {
		notifySaved(getTranslation("views.persons.save_success", //$NON-NLS-1$
				getEditedEntity().getFullName()));
	}

	@Override
	public void notifyValidated() {
		notifyValidated(getTranslation("views.persons.validation_success", //$NON-NLS-1$
				getEditedEntity().getFullName()));
	}

	@Override
	public void notifySavingError(Throwable error) {
		notifySavingError(error, getTranslation("views.persons.save_error", //$NON-NLS-1$ 
				getEditedEntity().getFullName(), error.getLocalizedMessage()));
	}

	@Override
	public void notifyValidationError(Throwable error) {
		notifyValidationError(error, getTranslation("views.persons.validation_error", //$NON-NLS-1$ 
				getEditedEntity().getFullName(), error.getLocalizedMessage()));
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
			final var size = new StringBuilder();
			size.append(ViewConstants.PHOTO_SIZE);
			size.append(Unit.PIXELS.getSymbol());
			final var image = LumoIcon.USER.create();
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
		final var content = ComponentFactory.newColumnForm(2);

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
		
		getEntityDataBinder().forField(this.lastname)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.persons.last_name.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.lastname, this.personalInformationDetails))
			.bind(Person::getLastName, Person::setLastName);
		getEntityDataBinder().forField(this.firstname)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.persons.first_name.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.firstname, this.personalInformationDetails))
			.bind(Person::getFirstName, Person::setFirstName);
		getEntityDataBinder().forField(this.gender).bind(Person::getGender, Person::setGender);
		getEntityDataBinder().forField(this.gravatarId)
			.withConverter(new StringTrimer())
			.bind(Person::getGravatarId, Person::setGravatarId);
	}

	private String getGenderLabel(Gender gender) {
		return gender.getLabel(getMessageSourceAccessor(), getLocale());
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

		getEntityDataBinder().forField(this.email)
			.withConverter(new StringTrimer())
			.withValidator(new EmailValidator(getTranslation("views.forms.email.invalid_format"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.email, this.contactInformationDetails))
			.bind(Person::getEmail, Person::setEmail);
		getEntityDataBinder().forField(this.officePhone)
			.withValidator(this.officePhone.getDefaultValidator())
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.officePhone, this.contactInformationDetails))
			.bind(Person::getOfficePhone, Person::setOfficePhone);
		getEntityDataBinder().forField(this.mobilePhone)
			.withValidator(this.mobilePhone.getDefaultValidator())
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.mobilePhone, this.contactInformationDetails))
			.bind(Person::getMobilePhone, Person::setMobilePhone);
		getEntityDataBinder().forField(this.officeRoom)
			.withConverter(new StringTrimer())
			.bind(Person::getOfficeRoom, Person::setOfficeRoom);
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

		getEntityDataBinder().forField(this.orcid)
			.withConverter(new StringTrimer())
			.withValidator(new OrcidValidator(getTranslation("views.persons.invalid_orcid"), //$NON-NLS-1$
					getTranslation("views.persons.undesired_orcid"), true)) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.orcid, this.researcherIdsDetails))
			.bind(Person::getORCID, Person::setORCID);
		getEntityDataBinder().forField(this.scopus)
			.withConverter(new StringTrimer())
			.bind(Person::getScopusId, Person::setScopusId);
		getEntityDataBinder().forField(this.wos)
			.withConverter(new StringTrimer())
			.bind(Person::getResearcherId, Person::setResearcherId);
		getEntityDataBinder().forField(this.gscholar)
			.withConverter(new StringTrimer())
			.bind(Person::getGoogleScholarId, Person::setGoogleScholarId);
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

		final var vl = new VerticalLayout(content0, content1);
		vl.setSpacing(false);

		this.indexesDetails = new Details("", vl); //$NON-NLS-1$
		this.indexesDetails.setOpened(false);
		receiver.add(this.indexesDetails);

		getEntityDataBinder().forField(this.wosHindex).bind(Person::getWosHindex, Person::setWosHindex);
		getEntityDataBinder().forField(this.scopusHindex).bind(Person::getScopusHindex, Person::setScopusHindex);
		getEntityDataBinder().forField(this.gscholarHindex).bind(Person::getGoogleScholarHindex, Person::setGoogleScholarHindex);
		getEntityDataBinder().forField(this.wosCitations).bind(Person::getWosCitations, Person::setWosCitations);
		getEntityDataBinder().forField(this.scopusCitations).bind(Person::getScopusCitations, Person::setScopusCitations);
		getEntityDataBinder().forField(this.gscholarCitations).bind(Person::getGoogleScholarCitations, Person::setGoogleScholarCitations);
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
		final var content = ComponentFactory.newColumnForm(1);

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

		final var invalidUrl = getTranslation("views.urls.invalid_format"); //$NON-NLS-1$
		
		getEntityDataBinder().forField(this.researchGate)
			.withConverter(new StringTrimer())
			.bind(Person::getResearchGateId, Person::setResearchGateId);
		getEntityDataBinder().forField(this.adScientificIndex)
			.withConverter(new StringTrimer())
			.bind(Person::getAdScientificIndexId, Person::setAdScientificIndexId);
		getEntityDataBinder().forField(this.dblp)
			.withConverter(new StringTrimer())
			.withValidator(new UrlValidator(invalidUrl, true))
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.dblp, this.socialLinksDetails))
			.bind(Person::getDblpURL, Person::setDblpURL);
		getEntityDataBinder().forField(this.academiaEdu)
			.withConverter(new StringTrimer())
			.withValidator(new UrlValidator(invalidUrl, true))
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.academiaEdu, this.socialLinksDetails))
			.bind(Person::getAcademiaURL, Person::setAcademiaURL);
		getEntityDataBinder().forField(this.euCordis)
			.withConverter(new StringTrimer())
			.withValidator(new UrlValidator(invalidUrl, true))
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.euCordis, this.socialLinksDetails))
			.bind(Person::getCordisURL, Person::setCordisURL);
		getEntityDataBinder().forField(this.linkedin)
			.withConverter(new StringTrimer())
			.bind(Person::getLinkedInId, Person::setLinkedInId);
		getEntityDataBinder().forField(this.github)
			.withConverter(new StringTrimer())
			.bind(Person::getGithubId, Person::setGithubId);
		getEntityDataBinder().forField(this.facebook)
			.withConverter(new StringTrimer())
			.bind(Person::getFacebookId, Person::setFacebookId);
	}

	private String getWebPageNamingLabel(WebPageNaming naming) {
		return naming.getLabel(getMessageSourceAccessor(), getLocale());
	}

	private String getUserRoleLabel(UserRole role) {
		return role.getLabel(getMessageSourceAccessor(), getLocale());
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.personalInformationDetails.setSummaryText(getTranslation("views.persons.personal_informations")); //$NON-NLS-1$
		this.lastname.setLabel(getTranslation("views.persons.last_name")); //$NON-NLS-1$
		this.firstname.setLabel(getTranslation("views.persons.first_name")); //$NON-NLS-1$
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

		if (this.webpageConvention != null) {
			this.webpageConvention.setLabel(getTranslation("views.persons.administration.webpage_naming")); //$NON-NLS-1$
			this.webpageConvention.setItemLabelGenerator(this::getWebPageNamingLabel);
		}
		if (this.userLogin != null) {
			this.userLogin.setLabel(getTranslation("views.persons.administration.user_login")); //$NON-NLS-1$
			this.userLogin.setHelperText(getTranslation("views.persons.administration.user_login.help")); //$NON-NLS-1$
		}
		if (this.userRole != null) {
			this.userRole.setLabel(getTranslation("views.persons.administration.user_role")); //$NON-NLS-1$
			this.userRole.setHelperText(getTranslation("views.persons.administration.user_role.help")); //$NON-NLS-1$
			this.userRole.setItemLabelGenerator(this::getUserRoleLabel);
		}
	}

}
