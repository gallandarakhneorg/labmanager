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

package fr.utbm.ciad.labmanager.views.components.conferences;

import static fr.utbm.ciad.labmanager.views.ViewConstants.CORE_PORTAL_BASE_URL;
import static fr.utbm.ciad.labmanager.views.ViewConstants.CORE_PORTAL_ICON;

import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.conference.Conference;
import fr.utbm.ciad.labmanager.services.conference.ConferenceService.EditingContext;
import fr.utbm.ciad.labmanager.views.components.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.converters.StringTrimer;
import fr.utbm.ciad.labmanager.views.components.details.DetailsWithErrorMark;
import fr.utbm.ciad.labmanager.views.components.details.DetailsWithErrorMarkStatusHandler;
import fr.utbm.ciad.labmanager.views.components.entities.AbstractEntityEditor;
import fr.utbm.ciad.labmanager.views.components.validators.IsbnValidator;
import fr.utbm.ciad.labmanager.views.components.validators.IssnValidator;
import fr.utbm.ciad.labmanager.views.components.validators.NotEmptyStringValidator;
import fr.utbm.ciad.labmanager.views.components.validators.UrlValidator;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation for the editor of the information related to a scientific conference.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Uses(Icon.class)
public abstract class AbstractConferenceEditor extends AbstractEntityEditor<Conference> {

	private static final long serialVersionUID = 6750040717745583722L;

	private DetailsWithErrorMark descriptionDetails;

	private TextField acronym;

	private TextField name;
	
	private RadioButtonGroup<Boolean> openAccess;

	private TextField conferenceUrl;

	private Details rankingDetails;

	private TextField coreId;

	private DetailsWithErrorMark publicationDetails;

	private TextField publisherName;

	private TextField issn;

	private TextField isbn;

	private final EditingContext context;

	/** Constructor.
	 *
	 * @param context the editing context for the conference.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 * @param logger the logger to be used by this view.
	 */
	public AbstractConferenceEditor(EditingContext context, AuthenticatedUser authenticatedUser,
			MessageSourceAccessor messages, Logger logger) {
		super(Conference.class, authenticatedUser, messages, logger,
				"views.conferences.administration_details", //$NON-NLS-1$
				"views.conferences.administration.validated_conference"); //$NON-NLS-1$
		this.context = context;
	}

	@Override
	public Conference getEditedEntity() {
		return this.context.getConference();
	}

	@Override
	protected void createEditorContent(VerticalLayout rootContainer, boolean isAdmin) {
		createDescriptionDetails(rootContainer);
		createRankingDetails(rootContainer);
		createPublisherDetails(rootContainer);
		if (isAdmin) {
			createAdministrationComponents(rootContainer,
					null,
					it -> it.bind(Conference::isValidated, Conference::setValidated));
		}
	}

	/** Create the section for editing the description of the conference.
	 *
	 * @param rootContainer the container.
	 */
	protected void createDescriptionDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.acronym = new TextField();
		this.acronym.setPrefixComponent(VaadinIcon.HASH.create());
		this.acronym.setRequired(true);
		this.acronym.setClearButtonVisible(true);
		content.add(this.acronym, 2);

		this.name = new TextField();
		this.name.setPrefixComponent(VaadinIcon.HASH.create());
		this.name.setRequired(true);
		this.name.setClearButtonVisible(true);
		content.add(this.name, 2);
		
		this.openAccess = new RadioButtonGroup<>();
		this.openAccess.setItems(null, Boolean.TRUE, Boolean.FALSE);
		this.openAccess.setValue(null);
		setOpenAccessRenderer();
		content.add(this.openAccess, 2);

		this.conferenceUrl = new TextField();
		this.conferenceUrl.setPrefixComponent(VaadinIcon.GLOBE_WIRE.create());
		this.conferenceUrl.setClearButtonVisible(true);
		content.add(this.conferenceUrl, 2);

		this.descriptionDetails = new DetailsWithErrorMark(content);
		this.descriptionDetails.setOpened(false);
		rootContainer.add(this.descriptionDetails);

		final var invalidUrl = getTranslation("views.urls.invalid_format"); //$NON-NLS-1$

		getEntityDataBinder().forField(this.acronym)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.conferences.acronym.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.acronym, this.descriptionDetails))
			.bind(Conference::getAcronym, Conference::setAcronym);
		getEntityDataBinder().forField(this.name)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.conferences.name.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.name, this.descriptionDetails))
			.bind(Conference::getName, Conference::setName);
		getEntityDataBinder().forField(this.openAccess)
			.bind(Conference::getOpenAccess, Conference::setOpenAccess);
		getEntityDataBinder().forField(this.conferenceUrl)
			.withConverter(new StringTrimer())
			.withValidator(new UrlValidator(invalidUrl, true))
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.conferenceUrl, this.descriptionDetails))
			.bind(Conference::getConferenceURL, Conference::setConferenceURL);
	}

	private void setOpenAccessRenderer() {
		this.openAccess.setRenderer(new ComponentRenderer<>(it -> {
			final var span = new Span();
			if (it == null) {
				span.setText(getTranslation("views.conferences.open_access.indeterminate")); //$NON-NLS-1$
			} else if (it == Boolean.TRUE) {
				span.setText(getTranslation("views.conferences.open_access.yes")); //$NON-NLS-1$
			} else {
				span.setText(getTranslation("views.conferences.open_access.no")); //$NON-NLS-1$
			}
			return span;
		}));
	}

	/** Create the section for editing the ranking information of the conference.
	 *
	 * @param rootContainer the container.
	 */
	protected void createRankingDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.coreId = ComponentFactory.newClickableIconTextField(CORE_PORTAL_BASE_URL, CORE_PORTAL_ICON);
		this.coreId.setPrefixComponent(VaadinIcon.HASH.create());
		this.coreId.setClearButtonVisible(true);
		content.add(this.coreId, 2);

		this.rankingDetails = new Details("", content); //$NON-NLS-1$
		this.rankingDetails.setOpened(false);
		rootContainer.add(this.rankingDetails);

		getEntityDataBinder().forField(this.coreId)
			.withConverter(new StringTrimer())
			.bind(Conference::getCoreId, Conference::setCoreId);
	}

	/** Create the section for editing the publishing information of the conference.
	 *
	 * @param rootContainer the container.
	 */
	protected void createPublisherDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.publisherName = new TextField();
		this.publisherName.setPrefixComponent(VaadinIcon.OPEN_BOOK.create());
		this.publisherName.setRequired(true);
		this.publisherName.setClearButtonVisible(true);
		content.add(this.publisherName, 2);

		this.issn = new TextField();
		this.issn.setPrefixComponent(VaadinIcon.HASH.create());
		this.issn.setClearButtonVisible(true);
		content.add(this.issn, 1);

		this.isbn = new TextField();
		this.isbn.setPrefixComponent(VaadinIcon.HASH.create());
		this.isbn.setClearButtonVisible(true);
		content.add(this.isbn, 1);

		this.publicationDetails = new DetailsWithErrorMark(content);
		this.publicationDetails.setOpened(false);
		rootContainer.add(this.publicationDetails);

		getEntityDataBinder().forField(this.publisherName)
			.withConverter(new StringTrimer())
			.bind(Conference::getPublisher, Conference::setPublisher);
		getEntityDataBinder().forField(this.issn)
			.withConverter(new StringTrimer())
			.withValidator(new IssnValidator(getTranslation("views.conferences.issn.error"), true)) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.issn, this.publicationDetails))
			.bind(Conference::getISSN, Conference::setISSN);
		getEntityDataBinder().forField(this.isbn)
			.withConverter(new StringTrimer())
			.withValidator(new IsbnValidator(getTranslation("views.conferences.isbn.error"), true)) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.isbn, this.publicationDetails))
			.bind(Conference::getISBN, Conference::setISBN);
	}

	/** Invoked for saving the conference. This function must be overridden by the child class that need to do a specific
	 * saving process. 
	 */
	protected void doSave() throws Exception {
		this.context.save();
	}

	@Override
	public void notifySaved() {
		notifySaved(getTranslation("views.conferences.save_success", //$NON-NLS-1$
				getEditedEntity().getName()));
	}

	@Override
	public void notifyValidated() {
		notifyValidated(getTranslation("views.conferences.validation_success", //$NON-NLS-1$
				getEditedEntity().getName()));
	}

	@Override
	public void notifySavingError(Throwable error) {
		notifySavingError(error, getTranslation("views.conferences.save_error", //$NON-NLS-1$ 
				getEditedEntity().getName(), error.getLocalizedMessage()));
	}

	@Override
	public void notifyValidationError(Throwable error) {
		notifyValidationError(error, getTranslation("views.conferences.validation_error", //$NON-NLS-1$ 
				getEditedEntity().getName(), error.getLocalizedMessage()));
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);

		this.descriptionDetails.setSummaryText(getTranslation("views.conferences.description_informations")); //$NON-NLS-1$
		this.acronym.setLabel(getTranslation("views.conferences.acronym")); //$NON-NLS-1$
		this.acronym.setHelperText(getTranslation("views.conferences.acronym.help")); //$NON-NLS-1$
		this.name.setLabel(getTranslation("views.conferences.name")); //$NON-NLS-1$
		this.name.setHelperText(getTranslation("views.conferences.name.help")); //$NON-NLS-1$
		this.openAccess.setLabel(getTranslation("views.conferences.open_access")); //$NON-NLS-1$
		// Force the refreshing of the radio button items
		setOpenAccessRenderer();
		this.conferenceUrl.setLabel(getTranslation("views.conferences.url")); //$NON-NLS-1$

		this.rankingDetails.setSummaryText(getTranslation("views.conferences.ranking_informations")); //$NON-NLS-1$
		this.coreId.setLabel(getTranslation("views.conferences.core.id")); //$NON-NLS-1$
		this.coreId.setHelperText(getTranslation("views.conferences.core.id.help")); //$NON-NLS-1$

		this.publicationDetails.setSummaryText(getTranslation("views.conferences.publisher_informations")); //$NON-NLS-1$
		this.publisherName.setLabel(getTranslation("views.conferences.publisher_name")); //$NON-NLS-1$
		this.publisherName.setHelperText(getTranslation("views.conferences.publisher_name.help")); //$NON-NLS-1$
		this.issn.setLabel(getTranslation("views.journals.issn")); //$NON-NLS-1$
		this.isbn.setLabel(getTranslation("views.journals.isbn")); //$NON-NLS-1$
	}

}
