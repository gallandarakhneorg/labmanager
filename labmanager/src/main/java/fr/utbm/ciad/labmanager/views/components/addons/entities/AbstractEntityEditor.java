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

package fr.utbm.ciad.labmanager.views.components.addons.entities;

import java.util.function.Consumer;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.BindingBuilder;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.user.UserRole;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.users.UserIdentityChangedObserver;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation for the editor of the information related to an entity.
 * 
 * @param <T> the type of the edited entity.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public abstract class AbstractEntityEditor<T> extends Composite<VerticalLayout> implements LocaleChangeObserver {

	private static final long serialVersionUID = -9123030449423137764L;

	private final Class<T> entityType;

	private final Logger logger;

	private final MessageSourceAccessor messages;

	private final AuthenticatedUser authenticatedUser;

	private final Binder<T> entityBinder;

	private Details administrationDetails;

	private Checkbox validatedEntity;
	
	private final boolean isAdmin;

	private final String administationSectionTranslationKey;

	private final String validationTranslationKey;

	/** Constructor.
	 *
	 * @param entityType the type of the edited entity.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 * @param logger the logger to be used by this view.
	 * @param administationSectionTranslationKey the key in the translation file that corresponds to
	 *     the label of the administration section.
	 * @param validationTranslationKey the key in the translation file that corresponds to the
	 *     label of the validation checkbox.
	 */
	public AbstractEntityEditor(Class<T> entityType, AuthenticatedUser authenticatedUser,
			MessageSourceAccessor messages, Logger logger, String administationSectionTranslationKey,
			String validationTranslationKey) {
		this.administationSectionTranslationKey = administationSectionTranslationKey;
		this.validationTranslationKey = validationTranslationKey;
		this.entityType = entityType;
		this.messages = messages;
		this.logger = logger;
		this.entityBinder = new Binder<>(this.entityType);
		this.authenticatedUser = authenticatedUser;

		this.isAdmin = this.authenticatedUser != null && this.authenticatedUser.get().isPresent()
				&& this.authenticatedUser.get().get().getRole() == UserRole.ADMIN;
	}

	/** Replies if the data inside the editor is valid.
	 *
	 * @return {@code true} if the fields have passed all the validation checks.
	 */
	public boolean isValidData() {
		return getEntityDataBinder().validate().isOk();
	}
	
	/** Replies if the editor is in admin mode.
	 * 
	 * @return {@code true} if in admin mode.
	 */
	public boolean isAdmin() {
		return this.isAdmin;
	}

	/** Replies the accessor to the localized strings.
	 *
	 * @return the accessor.
	 */
	protected MessageSourceAccessor getMessageSourceAccessor() {
		return this.messages;
	}

	/** Replies the binder between the data provider and the fields.
	 *
	 * @return the entity binder.
	 */
	protected Binder<T> getEntityDataBinder() {
		return this.entityBinder;
	}

	/** Replies the authenticated user.
	 *
	 * @return the user.
	 */
	protected AuthenticatedUser getAuthenticatedUser() {
		return this.authenticatedUser;
	}

	/** Replies the edited entity.
	 *
	 * @return the edited entity, never {@code null}.
	 */
	public abstract T getEditedEntity();

	/** Replies the logger.
	 *
	 * @return the logger.
	 */
	protected Logger getLogger() {
		return this.logger;
	}

	/** Replies the type of the edited entity.
	 * 
	 * @return the entity type.
	 */
	protected Class<T> getEntityType() {
		return this.entityType;
	}

	/** Create the editor content and links the beans
	 */
	protected final void createEditorContentAndLinkBeans() {
		final var rootContainer = getContent();
		rootContainer.setSpacing(false);
		createEditorContent(rootContainer, isAdmin());
		linkBeans();
	}

	/** Link the beans to the editor.
	 */
	protected void linkBeans() {
		getEntityDataBinder().setBean(getEditedEntity());
	}

	/** Create the content of the editor.
	 * This function should invoke {@link #createAdministrationComponents(VerticalLayout, boolean, Consumer)}.
	 *
	 * @param rootContainer the container.
	 * @param isAdmin indicates if the user has the administrator role.
	 * @see #createAdministrationComponents(VerticalLayout, boolean, Consumer)
	 */
	protected abstract void createEditorContent(VerticalLayout rootContainer, boolean isAdmin);

	/** Create the components for adminitrator.
	 *
	 * @param receiver the receiver of the component
	 * @param builderCallback the callback that is invoked to fill the administration form.
	 * @param validationBinder invoked to bind the validation attributes of the entity. 
	 */
	protected void createAdministrationComponents(VerticalLayout receiver,
			Consumer<FormLayout> builderCallback,
			Consumer<BindingBuilder<T, Boolean>> validationBinder) {
		final var content = ComponentFactory.newColumnForm(1);

		if (builderCallback != null) {
			builderCallback.accept(content);
		}

		if (validationBinder != null) {
			this.validatedEntity = new Checkbox();
			content.add(this.validatedEntity);
			validationBinder.accept(this.entityBinder.forField(this.validatedEntity));
		}

		if (content.getElement().getChildCount() > 0) {
			this.administrationDetails = new Details("", content); //$NON-NLS-1$
			this.administrationDetails.setOpened(false);
			this.administrationDetails.addThemeVariants(DetailsVariant.FILLED);
			receiver.add(this.administrationDetails);
		}
	}

	/** Invoked for validating the entity by an organizational structure manager. This function does not save.
	 * This function must be overridden by the child class that need to do a specific
	 * saving process.
	 * 
	 * @see #doValidate(Object)
	 */
	public final void validateByOrganizationalStructureManager() {
		doValidate(getEditedEntity());
	}

	/** Invoked for saving the conference. This function must be overridden by the child class that need to do a specific
	 * saving process. 
	 *
	 * @throws Exception if saving cannot be done.
	 */
	protected abstract void doSave() throws Exception;
	
	/** Invoked for saving the conference.
	 *
	 * @return {@code true} if the saving was a success.
	 * @see #doSave()
	 */
	public final boolean save() {
		try {
			doSave();
			// Relink the beans for ensuring the editor has
			// the last instance of the entity instance
			// that may have changed due to the saving process
			linkBeans();
			notifySaved();
			return true;
		} catch (Throwable ex) {
			notifySavingError(ex);
		}
		return false;
	}

	/** Invoked for validating the entity. This function does not save.
	 * This function must be overridden by the child class that need to do a specific
	 * saving process. 
	 *
	 * @param entity the entity to validate.
	 */
	protected void doValidate(T entity) {
		try {
			this.validatedEntity.setValue(Boolean.TRUE);
			notifyValidated();
		} catch (Throwable ex) {
			notifyValidationError(ex);
		}
	}

	/** Notify the user that the person was validated.
	 */
	public abstract void notifyValidated();

	/** Notify the user that the person was validated.
	 *
	 * @param message the validation message to show to the user.
	 */
	@SuppressWarnings("static-method")
	protected void notifyValidated(String message) {
		final var notification = Notification.show(message);
		notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
	}

	/** Notify the user that its personal information has changed.
	 */
	protected void notifyAuthenticatedUserChange() {
		UserIdentityChangedObserver app = null;
		final var view = UI.getCurrent().getCurrentView();
		if (view != null) {
			app = view.findAncestor(UserIdentityChangedObserver.class);
		}
		if (app != null) {
			app.authenticatedUserIdentityChange();
		}
		final var notification = Notification.show(getTranslation("views.authenticated_user_change")); //$NON-NLS-1$
		notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
	}

	/** Notify the user that the form contains invalid information.
	 */
	public void notifyInvalidity() {
		final var notification = Notification.show(getTranslation("views.save_invalid_data")); //$NON-NLS-1$
		notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
	}

	/** Notify the user that the person was saved.
	 */
	public abstract void notifySaved();

	/** Notify the user that the person was saved.
	 *
	 * @param message the message to show up to the user.
	 */
	protected void notifySaved(String message) {
		this.logger.info("Data successfully saved by " + AuthenticatedUser.getUserName(this.authenticatedUser) //$NON-NLS-1$
		+ ": " + message); //$NON-NLS-1$
		final Notification notification = Notification.show(message);
		notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
	}

	/** Notify the user that the person cannot be saved.
	 *
	 * @param error the error.
	 */
	public abstract void notifySavingError(Throwable error);

	/** Notify the user that the person cannot be saved.
	 *
	 * @param message the message to show up to the user.
	 */
	protected void notifySavingError(Throwable error, String message) {
		this.logger.warn("Error when saving entity data by " + AuthenticatedUser.getUserName(this.authenticatedUser) //$NON-NLS-1$
		+ ": " + message + "\n-> " + error.getLocalizedMessage(), error); //$NON-NLS-1$ //$NON-NLS-2$
		final var notification = Notification.show(message);
		notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
	}

	/** Notify the user that the person cannot be validated.
	 *
	 * @param error the error.
	 */
	public abstract void notifyValidationError(Throwable error);

	/** Notify the user that the person cannot be validated.
	 *
	 * @param message the message to show up to the user.
	 */
	protected void notifyValidationError(Throwable error, String message) {
		this.logger.warn("Error when validating the entity by " + AuthenticatedUser.getUserName(this.authenticatedUser) //$NON-NLS-1$
		+ ": " + message + "\n-> " + error.getLocalizedMessage(), error); //$NON-NLS-1$ //$NON-NLS-2$
		final var notification = Notification.show(message);
		notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		if (this.administrationDetails != null) {
			this.administrationDetails.setSummaryText(getTranslation(this.administationSectionTranslationKey));
		}
		if (this.validatedEntity != null) {
			this.validatedEntity.setLabel(getTranslation(this.validationTranslationKey));
		}
	}

}
