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

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.views.components.MainLayout;
import fr.utbm.ciad.labmanager.views.components.persons.AbstractPersonEditor;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.vaadin.lineawesome.LineAwesomeIcon;

/** Enable to edit the personal information for the user.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Route(value = "myprofile", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
public class MyProfileView extends AbstractPersonEditor implements HasDynamicTitle {

	private static final long serialVersionUID = 738063190104767506L;

	private static final Logger LOGGER = LoggerFactory.getLogger(MyProfileView.class);

	private Button saveButton;

	private Button validateButton;

	/** Constructor.
	 *
	 * @param personService the service to access to the person JPA.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (spring layer)
	 */
	public MyProfileView(@Autowired PersonService personService, @Autowired AuthenticatedUser authenticatedUser,
			@Autowired MessageSourceAccessor messages) {
		super(authenticatedUser.get().get().getPerson(), personService, authenticatedUser, messages, LOGGER);
	}

	@Override
	public String getPageTitle() {
		return getTranslation("views.persons.editor_title.user"); //$NON-NLS-1$
	}

	@Override
	protected void createContent(VerticalLayout rootContainer, boolean isAdmin, PersonService personService) {
		super.createContent(rootContainer, isAdmin, personService);

		rootContainer.setSpacing(true);
		
		final HorizontalLayout bar = new HorizontalLayout();
		rootContainer.add(bar);
		
		this.saveButton = new Button("", event -> { //$NON-NLS-1$
			if (getDataBinder().validate().isOk()) {
				doSave(getEditedPerson(), personService);
			} else {
				notifyInvalidity();
			}
		});
		this.saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		this.saveButton.addClickShortcut(Key.ENTER);
		this.saveButton.setIcon(LineAwesomeIcon.SAVE_SOLID.create());
		bar.add(this.saveButton);

		this.validateButton = null;
		if (isAdmin) {
			this.validateButton = new Button("", event -> { //$NON-NLS-1$
				doValidate(getEditedPerson());
				if (getDataBinder().validate().isOk()) {
					doSave(getEditedPerson(), personService);
				} else {
					notifyInvalidity();
				}
			});
			this.validateButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
			this.validateButton.setIcon(LineAwesomeIcon.CHECK_DOUBLE_SOLID.create());
			bar.add(this.validateButton);
		}
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.saveButton.setText(getTranslation("views.save")); //$NON-NLS-1$
		this.validateButton.setText(getTranslation("views.validate")); //$NON-NLS-1$
	}

}
