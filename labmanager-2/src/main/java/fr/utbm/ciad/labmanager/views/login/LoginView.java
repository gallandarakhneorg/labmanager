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

package fr.utbm.ciad.labmanager.views.login;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;

/** View for logged in the users.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

	private static final long serialVersionUID = 7995735340418029759L;

	private final AuthenticatedUser authenticatedUser;

	/** Constructor.
	 *
	 * @param authenticatedUser the logged-in user.
	 */
	public LoginView(AuthenticatedUser authenticatedUser) {
		this.authenticatedUser = authenticatedUser;
		setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

		LoginI18n i18n = LoginI18n.createDefault();
		i18n.setHeader(new LoginI18n.Header());
		i18n.getHeader().setTitle("CIAD Lab Manager");
		i18n.getHeader().setDescription("Login using user/user or admin/admin");
		i18n.setAdditionalInformation(null);
		setI18n(i18n);

		setForgotPasswordButtonVisible(false);
		setOpened(true);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (this.authenticatedUser.get().isPresent()) {
			// Already logged in
			setOpened(false);
			event.forwardTo("");
		}
		setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
	}
}
